package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class ProjetilAngel extends Projetil {

    private enum EstadoNuvem {
        NASCENDO, ATIVA, SUMINDO
    }
    private EstadoNuvem estadoAtual = EstadoNuvem.NASCENDO;

    private Animation<TextureRegion> animNascendo;
    private Animation<TextureRegion> animAtiva;
    private Animation<TextureRegion> animSumindo;

    private Animation<TextureRegion> animAttackVisual;
    private float tempoAnimacaoAttackVisual;

    private float velAttackVisual = 0.02f;

    private float tempoAnimacaoNuvem;
    private float velocidadeMovimento = 250f;

    private float velNascendo = 0.08f;
    private float velAtiva = 0.15f;
    private float velSumindo = 0.08f;

    private Texture imgAtaque;

    private Vector2 posBaseNuvem = new Vector2();
    private Vector2 posNuvem = new Vector2();
    private float alturaNuvem = 100f;

    private float intervaloAtaque = 1.0f;
    private float timerDano = intervaloAtaque;

    private float timerVisualRaio = 0f;
    private float raioVisualY = 0f;

    private Inimigo inimigoMorto = null;

    // 🔥 VARIÁVEIS NOVAS PARA ATRASAR O DANO
    private boolean danoPendente = false;
    private Inimigo alvoPendente = null;

    private float origemTorreX;
    private float origemTorreY;
    private float raioPermitido;

    private float timerVoo = 0f;

    public ProjetilAngel(Inimigo alvo, float danoBaseTorre, Texture imgNascendo, Texture imgAtiva, Texture imgSumindo, Texture imgAtaque, float origemX, float origemY, float raioPermitido) {
        super(alvo.posicao.x, alvo.posicao.y + 180f, alvo, imgAtaque, (int)(danoBaseTorre / 4f), 40f, 100f, 0f, 1f);

        this.imgAtaque = imgAtaque;
        this.ativo = true;
        this.dano = (int) (danoBaseTorre / 4f);
        this.origemTorreX = origemX;
        this.origemTorreY = origemY;
        this.raioPermitido = raioPermitido;

        imgNascendo.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        imgAtiva.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        imgSumindo.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        imgAtaque.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        int largNasc = imgNascendo.getWidth() / 8;
        int altNasc = imgNascendo.getHeight();
        Array<TextureRegion> framesNascendo = new Array<>();
        for (int i = 0; i < 8; i++) {
            framesNascendo.add(new TextureRegion(imgNascendo, i * largNasc, 0, largNasc, altNasc));
        }
        animNascendo = new Animation<>(velNascendo, framesNascendo, Animation.PlayMode.NORMAL);

        int largAtiva = imgAtiva.getWidth() / 5;
        int altAtiva = imgAtiva.getHeight();
        Array<TextureRegion> framesAtiva = new Array<>();
        for (int i = 0; i < 5; i++) {
            framesAtiva.add(new TextureRegion(imgAtiva, i * largAtiva, 0, largAtiva, altAtiva));
        }
        animAtiva = new Animation<>(velAtiva, framesAtiva, Animation.PlayMode.LOOP);

        int largSum = imgSumindo.getWidth() / 5;
        int altSum = imgSumindo.getHeight();
        Array<TextureRegion> framesSumindo = new Array<>();
        for (int i = 0; i < 5; i++) {
            framesSumindo.add(new TextureRegion(imgSumindo, i * largSum, 0, largSum, altSum));
        }
        animSumindo = new Animation<>(velSumindo, framesSumindo, Animation.PlayMode.NORMAL);

        int largAttackVisual = imgAtaque.getWidth() / 10;
        int altAttackVisual = imgAtaque.getHeight();
        Array<TextureRegion> framesAttackVisual = new Array<>();
        for (int i = 0; i < 10; i++) {
            framesAttackVisual.add(new TextureRegion(imgAtaque, i * largAttackVisual, 0, largAttackVisual, altAttackVisual));
        }
        animAttackVisual = new Animation<>(velAttackVisual, framesAttackVisual, Animation.PlayMode.NORMAL);

        this.tempoAnimacaoNuvem = 0f;

        this.posBaseNuvem.set(origemTorreX, origemTorreY + 50f);
        this.posNuvem.set(posBaseNuvem);
    }

    @Override
    public void atualizar(float delta) {
        if (!ativo) return;

        tempoAnimacaoNuvem += delta;
        timerVoo += delta;
        tempoAnimacaoAttackVisual += delta;

        if (timerVisualRaio > 0) {
            timerVisualRaio -= delta;
        }

        // 🔥 APLICA O DANO NO ÚLTIMO FRAME DA ANIMAÇÃO DO RAIO 🔥
        if (danoPendente) {
            float tempoUltimoFrame = 9 * velAttackVisual; // O 9º frame é o último da sua imagem
            if (tempoAnimacaoAttackVisual >= tempoUltimoFrame) {
                if (alvoPendente != null && alvoPendente.vida > 0) {
                    alvoPendente.vida -= this.dano;
                    if (alvoPendente.vida <= 0) {
                        inimigoMorto = alvoPendente;
                    }
                }
                danoPendente = false; // Finaliza o pendente
                alvoPendente = null;
            }
        }

        if (estadoAtual == EstadoNuvem.NASCENDO) {
            if (animNascendo.isAnimationFinished(tempoAnimacaoNuvem)) {
                estadoAtual = EstadoNuvem.ATIVA;
                tempoAnimacaoNuvem = 0f;
            }
        } else if (estadoAtual == EstadoNuvem.SUMINDO) {
            float destinoTorreX = origemTorreX;
            float destinoTorreY = origemTorreY + 50f;
            float dx = destinoTorreX - posBaseNuvem.x;
            float dy = destinoTorreY - posBaseNuvem.y;
            float distBaseTorre = Vector2.dst(posBaseNuvem.x, posBaseNuvem.y, destinoTorreX, destinoTorreY);

            if (distBaseTorre > 5f) {
                posBaseNuvem.x += (dx / distBaseTorre) * velocidadeMovimento * delta;
                posBaseNuvem.y += (dy / distBaseTorre) * velocidadeMovimento * delta;
            }

            if (animSumindo.isAnimationFinished(tempoAnimacaoNuvem)) {
                this.ativo = false;
                return;
            }
        }

        if (estadoAtual != EstadoNuvem.SUMINDO && alvo != null && alvo.vida > 0) {
            float alvoX = alvo.posicao.x + (alvo.largura / 2f);
            float alvoY = alvo.posicao.y + (alvo.altura / 2f);
            float distAteTorre = Vector2.dst(origemTorreX, origemTorreY, alvoX, alvoY);

            if (distAteTorre > raioPermitido) {
                alvo = null;
            } else {
                float destinoX = alvo.posicao.x + (alvo.largura / 2f);
                float destinoY = alvo.posicao.y + alturaNuvem;

                float difX = destinoX - posBaseNuvem.x;
                float difY = destinoY - posBaseNuvem.y;
                float distBase = Vector2.dst(posBaseNuvem.x, posBaseNuvem.y, destinoX, destinoY);

                if (distBase > 5f) {
                    posBaseNuvem.x += (difX / distBase) * velocidadeMovimento * delta;
                    posBaseNuvem.y += (difY / distBase) * velocidadeMovimento * delta;
                } else {
                    posBaseNuvem.set(destinoX, destinoY);
                }
            }
        }

        float offsetX = MathUtils.sin(timerVoo * 2f) * 30f;
        float offsetY = MathUtils.cos(timerVoo * 1.5f) * 10f;

        posNuvem.x = posBaseNuvem.x + offsetX;
        posNuvem.y = posBaseNuvem.y + offsetY;

        this.posicao.set(posNuvem);
        this.hitbox.setPosition(posNuvem.x, posNuvem.y);

        // Dispara o visual do ataque sem dar o dano imediatamente
        if (estadoAtual == EstadoNuvem.ATIVA && alvo != null && alvo.vida > 0) {
            float distBaseAlvo = Vector2.dst(posBaseNuvem.x, posBaseNuvem.y, alvo.posicao.x + (alvo.largura / 2f), alvo.posicao.y + alturaNuvem);
            if (distBaseAlvo <= 15f) {
                if (!danoPendente) {
                    timerDano += delta;

                    if (timerDano >= intervaloAtaque) {
                        // SÓ COMEÇA O RAIO!
                        tempoAnimacaoAttackVisual = 0f;
                        timerVisualRaio = 10 * velAttackVisual;
                        raioVisualY = alvo.posicao.y + (alvo.altura / 2f);

                        alvoPendente = alvo;
                        danoPendente = true; // Avisa o sistema que o dano está "voando"
                        timerDano = 0;
                    }
                }
            } else {
                if (timerDano < intervaloAtaque && !danoPendente) {
                    timerDano += delta;
                }
            }
        }
    }

    @Override
    public Inimigo checarColisao(Array<Inimigo> listaInimigos) {
        if (inimigoMorto != null) {
            listaInimigos.removeValue(inimigoMorto, true);
            inimigoMorto = null;
            if (alvo == inimigoMorto) alvo = null;
        }

        if (alvo == null && estadoAtual != EstadoNuvem.SUMINDO) {
            Inimigo novoAlvo = null;
            float menorDistancia = Float.MAX_VALUE;

            for (Inimigo in : listaInimigos) {
                if (in.vida > 0) {
                    float inX = in.posicao.x + (in.largura / 2f);
                    float inY = in.posicao.y + (in.altura / 2f);

                    float distAteTorre = Vector2.dst(origemTorreX, origemTorreY, inX, inY);

                    if (distAteTorre <= raioPermitido) {
                        float distAteNuvem = Vector2.dst(posNuvem.x, posNuvem.y, inX, in.posicao.y + alturaNuvem);
                        if (distAteNuvem < menorDistancia) {
                            menorDistancia = distAteNuvem;
                            novoAlvo = in;
                        }
                    }
                }
            }

            if (novoAlvo != null) {
                alvo = novoAlvo;
            } else {
                if (timerVisualRaio <= 0 && estadoAtual == EstadoNuvem.ATIVA && !danoPendente) {
                    estadoAtual = EstadoNuvem.SUMINDO;
                    tempoAnimacaoNuvem = 0f;
                }
            }
        }

        return null;
    }

    @Override
    public void desenhar(SpriteBatch batch) {
        if (!ativo) return;

        if (timerVisualRaio > 0 && estadoAtual != EstadoNuvem.SUMINDO) {
            TextureRegion frameAtaque = animAttackVisual.getKeyFrame(tempoAnimacaoAttackVisual);

            if (frameAtaque != null) {
                float larguraRaioVisual = 80f;

                float eixoY_TopoAtaque = posNuvem.y + 20f;
                float alturaAtaqueSuavizado = eixoY_TopoAtaque - raioVisualY;

                batch.draw(frameAtaque, posNuvem.x - (larguraRaioVisual / 2f), raioVisualY, larguraRaioVisual, alturaAtaqueSuavizado);
            }
        }

        TextureRegion regionAtual = null;

        if (estadoAtual == EstadoNuvem.NASCENDO) {
            regionAtual = animNascendo.getKeyFrame(tempoAnimacaoNuvem);
        } else if (estadoAtual == EstadoNuvem.ATIVA) {
            regionAtual = animAtiva.getKeyFrame(tempoAnimacaoNuvem);
        } else if (estadoAtual == EstadoNuvem.SUMINDO) {
            regionAtual = animSumindo.getKeyFrame(tempoAnimacaoNuvem);
        }

        if (regionAtual != null) {
            batch.draw(regionAtual, posNuvem.x - 60, posNuvem.y, 120f, 80f);
        }
    }

    public void mudarAlvo(Inimigo novoAlvo) {
        if (novoAlvo != null && this.alvo != novoAlvo && estadoAtual != EstadoNuvem.SUMINDO) {
            this.alvo = novoAlvo;
        }
    }
}
