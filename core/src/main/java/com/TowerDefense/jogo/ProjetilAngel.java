package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class ProjetilAngel extends Projetil {

    private enum EstadoNuvem { NASCENDO, ATIVA, SUMINDO }
    private EstadoNuvem estadoAtual = EstadoNuvem.NASCENDO;

    private Animation<TextureRegion> animNascendo, animAtiva, animSumindo, animAttackVisual;
    private float tempoAnimacaoNuvem, tempoAnimacaoAttackVisual, timerVoo, timerVisualRaio;

    private Vector2 posBaseNuvem = new Vector2(), posNuvem = new Vector2();
    // Altura reduzida para ficar bem em cima do caramujo
    private float alturaNuvem = 100f, velocidadeMovimento = 300f, raioExplosao, raioVisualY;
    private float intervaloAtaque = 1.2f, timerDano = 0;

    private boolean danoPendente = false, precisaExplodir = false;
    private float origemTorreX, origemTorreY, raioPermitido;

    public ProjetilAngel(Inimigo alvo, float danoBase, Texture imgNasc, Texture imgAtiv, Texture imgSum, Texture imgAtq, float oX, float oY, float raioPerm, float raioExplo) {
        super(alvo.posicao.x, alvo.posicao.y + 100f, alvo, imgAtq, (int)(danoBase), 40f, 100f, 0f, 1f);

        this.origemTorreX = oX; this.origemTorreY = oY;
        this.raioPermitido = raioPerm; this.raioExplosao = raioExplo;
        this.dano = (int)(danoBase);

        // Inicializar Animações
        animNascendo = criarAnim(imgNasc, 8, 0.08f, Animation.PlayMode.NORMAL);
        animAtiva = criarAnim(imgAtiv, 5, 0.15f, Animation.PlayMode.LOOP);
        animSumindo = criarAnim(imgSum, 5, 0.08f, Animation.PlayMode.NORMAL);
        animAttackVisual = criarAnim(imgAtq, 10, 0.03f, Animation.PlayMode.NORMAL);

        // Nasce um pouco mais baixa
        this.posBaseNuvem.set(oX, oY + 60f);
    }

    private Animation<TextureRegion> criarAnim(Texture tex, int frames, float vel, Animation.PlayMode mode) {
        int w = tex.getWidth() / frames;
        Array<TextureRegion> f = new Array<>();
        for (int i = 0; i < frames; i++) f.add(new TextureRegion(tex, i * w, 0, w, tex.getHeight()));
        return new Animation<>(vel, f, mode);
    }

    @Override
    public void atualizar(float delta) {
        if (!ativo) return;
        tempoAnimacaoNuvem += delta; timerVoo += delta; tempoAnimacaoAttackVisual += delta;
        if (timerVisualRaio > 0) timerVisualRaio -= delta;

        // Lógica de Dano Sincronizado com o Frame
        if (danoPendente && tempoAnimacaoAttackVisual >= (9 * 0.03f)) {
            precisaExplodir = true;
            danoPendente = false;
        }

        if (estadoAtual == EstadoNuvem.NASCENDO && animNascendo.isAnimationFinished(tempoAnimacaoNuvem)) {
            estadoAtual = EstadoNuvem.ATIVA; tempoAnimacaoNuvem = 0;
        }
        else if (estadoAtual == EstadoNuvem.SUMINDO) {
            // Volta para a torre numa altura menor
            posBaseNuvem.lerp(new Vector2(origemTorreX, origemTorreY + 60f), 0.1f);
            if (animSumindo.isAnimationFinished(tempoAnimacaoNuvem)) { ativo = false; return; }
        }

        // Perseguição do Alvo
        // Perseguição do Alvo
        if (estadoAtual != EstadoNuvem.SUMINDO && alvo != null && alvo.vida > 0) {
            float dist = Vector2.dst(origemTorreX, origemTorreY, alvo.posicao.x, alvo.posicao.y);
            if (dist > raioPermitido + 100f) { alvo = null; }
            else {
                Vector2 destino = new Vector2(alvo.posicao.x + alvo.largura/2, alvo.posicao.y + alturaNuvem);
                posBaseNuvem.lerp(destino, 0.15f);

                // NOVIDADE: Mantém a mira do raio grudada no alvo na vertical enquanto "carrega"
                if (danoPendente) {
                    raioVisualY = alvo.posicao.y;
                }

                // Disparar Raio
                if (posBaseNuvem.dst(destino) < 30f && !danoPendente) {
                    timerDano += delta;
                    if (timerDano >= intervaloAtaque) {
                        tempoAnimacaoAttackVisual = 0;
                        timerVisualRaio = 0.3f;
                        raioVisualY = alvo.posicao.y;
                        danoPendente = true;
                        timerDano = 0;
                    }
                }
            }
        }

        // Balanço da nuvem
        posNuvem.set(posBaseNuvem.x + MathUtils.sin(timerVoo * 2f) * 20f, posBaseNuvem.y + MathUtils.cos(timerVoo * 1.5f) * 10f);
    }

    @Override
    public Inimigo checarColisao(Array<Inimigo> listaInimigos) {
        if (precisaExplodir) {
            for (int i = listaInimigos.size - 1; i >= 0; i--) {
                Inimigo in = listaInimigos.get(i);

                // NOVIDADE: Agora medimos do centro da explosão para o centro do inimigo
                float centroExplosaoY = raioVisualY + (in.altura / 2f);
                float d = Vector2.dst(posBaseNuvem.x, centroExplosaoY, in.posicao.x + in.largura/2, in.posicao.y + in.altura/2);

                if (d <= raioExplosao) {
                    in.vida -= this.dano;
                    if (in.vida <= 0 && alvo == in) { alvo = null; }
                }
            }
            precisaExplodir = false;
        }

        // Se perder o alvo, tenta achar outro por perto
        if (alvo == null && estadoAtual == EstadoNuvem.ATIVA && !danoPendente) {
            for (Inimigo in : listaInimigos) {
                if (in.vida > 0 && Vector2.dst(origemTorreX, origemTorreY, in.posicao.x, in.posicao.y) <= raioPermitido) {
                    alvo = in; break;
                }
            }
            if (alvo == null) { estadoAtual = EstadoNuvem.SUMINDO; tempoAnimacaoNuvem = 0; }
        }
        return null;
    }

    @Override
    public void desenhar(SpriteBatch batch) {
        if (!ativo) return;

        // Desenha o Raio (Ataque Visual mais fino)
        if (timerVisualRaio > 0) {
            TextureRegion frameRaio = animAttackVisual.getKeyFrame(tempoAnimacaoAttackVisual);
            float largRaio = raioExplosao * 2.0f; // Multiplicador reduzido para estreitar o raio
            batch.draw(frameRaio, posBaseNuvem.x - largRaio/2, raioVisualY, largRaio, posNuvem.y - raioVisualY + 20);
        }

        // Desenha a Nuvem (Escala reduzida)
        TextureRegion r = (estadoAtual == EstadoNuvem.NASCENDO) ? animNascendo.getKeyFrame(tempoAnimacaoNuvem) :
            (estadoAtual == EstadoNuvem.SUMINDO) ? animSumindo.getKeyFrame(tempoAnimacaoNuvem) :
                animAtiva.getKeyFrame(tempoAnimacaoNuvem);

        // Diminuí de 150x100 para 110x75
        batch.draw(r, posNuvem.x - 55, posNuvem.y, 110, 75);
    }

    public void mudarAlvo(Inimigo n) { if (estadoAtual != EstadoNuvem.SUMINDO) this.alvo = n; }
}
