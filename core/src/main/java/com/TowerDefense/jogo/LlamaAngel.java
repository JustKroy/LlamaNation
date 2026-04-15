package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class LlamaAngel extends Torre {

    private TextureRegion[] framesAnimacao;
    private float tempoEstado;

    // 🔥 AGORA GUARDAMOS AS 3 IMAGENS DA NUVEM SEPARADAS
    private Texture imgNuvemNasc;
    private Texture imgNuvemAtiva;
    private Texture imgNuvemSum;

    private Texture imgAtaque;

    // A COLEIRA DA NUVEM
    private ProjetilAngel nuvemAtiva = null;

    // 🔥 CONSTRUTOR ATUALIZADO PARA RECEBER AS 3 IMAGENS
    public LlamaAngel(float x, float y, Texture imgLlamaAngel, Texture imgNuvemNasc, Texture imgNuvemAtiva, Texture imgNuvemSum, Texture imgAtaque) {
        super(x, y, imgLlamaAngel, imgAtaque);

        this.imgNuvemNasc = imgNuvemNasc;
        this.imgNuvemAtiva = imgNuvemAtiva;
        this.imgNuvemSum = imgNuvemSum;
        this.imgAtaque = imgAtaque;

        this.dano = 80; // Dano base da torre
        this.raio = 350f;
        this.cooldown = 2.0f;

        int larguraFrame = imgLlamaAngel.getWidth() / 5;
        int alturaFrame = imgLlamaAngel.getHeight();

        framesAnimacao = new TextureRegion[5];
        for (int i = 0; i < 5; i++) {
            framesAnimacao[i] = new TextureRegion(imgLlamaAngel, i * larguraFrame, 0, larguraFrame, alturaFrame);
        }

        this.larguraDesenho = 80f;
        this.alturaDesenho = 80f * ((float) alturaFrame / larguraFrame);

        this.hitbox.width = this.larguraDesenho;
        this.hitbox.height = this.alturaDesenho;

        this.tempoEstado = 0f;
    }

    @Override
    public int atualizar(float delta, Array<Inimigo> listaInimigos, Array<Projetil> listaProjeteis) {
        tempoTiro += delta;
        tempoEstado += delta;

        float centroTorreX = posicao.x + (larguraDesenho / 2f);
        float centroTorreY = posicao.y + (alturaDesenho / 2f);

        //Pegar inimigos no range
        Array<Inimigo> inimigosNoRange = new Array<>();
        for (Inimigo in : listaInimigos) {
            float centroInimigoX = in.posicao.x + (in.largura / 2f);
            float centroInimigoY = in.posicao.y + (in.altura / 2f);
            float distancia = Vector2.dst(centroTorreX, centroTorreY, centroInimigoX, centroInimigoY);

            if (distancia <= raio) {
                inimigosNoRange.add(in);
            }
        }

        // 2. PASSO: Escolher alvo baseado no botão de FOCO (herdado de Torre)
        Inimigo alvoEscolhido = null;

        if (inimigosNoRange.size > 0) {
            alvoEscolhido = inimigosNoRange.get(0);

            for (int i = 1; i < inimigosNoRange.size; i++) {
                Inimigo in = inimigosNoRange.get(i);

                if (modoAlvoAtual == ModoAlvo.PRIMEIRO) {
                    if (in.distanciaPercorrida > alvoEscolhido.distanciaPercorrida)
                        alvoEscolhido = in;
                } else if (modoAlvoAtual == ModoAlvo.ULTIMO) {
                    if (in.distanciaPercorrida < alvoEscolhido.distanciaPercorrida)
                        alvoEscolhido = in;
                } else if (modoAlvoAtual == ModoAlvo.MAIS_FORTE) {
                    if (in.vida > alvoEscolhido.vida) alvoEscolhido = in;
                }
            }
        }

        // 3. PASSO: Controlar a Nuvem
        if (alvoEscolhido != null) {
            viradaParaEsquerda = (alvoEscolhido.posicao.x < posicao.x);

            // SE A NUVEM ESTIVER ATIVA, MUDA A ROTA DELA PARA O NOVO ALVO DO FOCO
            if (nuvemAtiva != null && nuvemAtiva.ativo) {
                nuvemAtiva.mudarAlvo(alvoEscolhido);
            }

            if (tempoTiro >= cooldown) {
                // Só cria nuvem nova se não tiver nenhuma voando
                if (nuvemAtiva == null || !nuvemAtiva.ativo) {
                    System.out.println("☁️ INVOCANDO A TEMPESTADE!");

                    // 🔥 CRIA A NUVEM PASSANDO AS 3 IMAGENS
                    nuvemAtiva = new ProjetilAngel(alvoEscolhido, dano, imgNuvemNasc, imgNuvemAtiva, imgNuvemSum, imgAtaque, centroTorreX, centroTorreY, raio);
                    listaProjeteis.add(nuvemAtiva);

                    tempoTiro = 0;
                }
            }
        }
        return 0;
    }

    @Override
    public void desenhar(SpriteBatch batch) {
        int frameAtual = (int) (tempoEstado / 0.15f) % 5;
        TextureRegion frame = framesAnimacao[frameAtual];

        batch.draw(frame.getTexture(), posicao.x, posicao.y, larguraDesenho, alturaDesenho,
            frame.getRegionX(), frame.getRegionY(), frame.getRegionWidth(), frame.getRegionHeight(),
            viradaParaEsquerda, false);
    }

    // BOTÃO DE AUTODESTRUIÇÃO (Use quando for vender a torre)
    public void destruirNuvem() {
        if (nuvemAtiva != null) {
            nuvemAtiva.ativo = false; // Desativa a nuvem no jogo
            nuvemAtiva = null;
        }
    }
}
