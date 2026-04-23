package com.TowerDefense.jogo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class LlamaBurguesa extends Torre {

    private Animation<TextureRegion> animacao;
    private float stateTime = 0f;
    private boolean jaDeuDinheiroNesseCiclo = false;

    // Variáveis para o texto flutuante
    private boolean mostrarTexto = false;
    private float tempoTexto = 0f;
    private float textoY = 0f;

    // Fonte estática: carrega uma vez só na memória
    private static BitmapFont fontTexto;

    public LlamaBurguesa(float x, float y) {
        // Usa o novo construtor simplificado da classe mãe
        super(x, y);
        this.raio = TipoLlama.BURGUESA.raioInicial;

        // Ajuste aqui o tamanho final da Llama no mapa se ficar muito grande ou pequena
        this.larguraDesenho = 80f;
        this.alturaDesenho = 80f;
        this.hitbox.width = larguraDesenho;
        this.hitbox.height = alturaDesenho;

        // Recortando a imagem de 17 frames na horizontal
        Texture sheet = new Texture("BourgeoisLlamaSS.png");
        int frameWidth = sheet.getWidth() / 17;
        int frameHeight = sheet.getHeight();

        TextureRegion[][] tmp = TextureRegion.split(sheet, frameWidth, frameHeight);
        TextureRegion[] frames = new TextureRegion[17];

        for (int i = 0; i < 17; i++) {
            frames[i] = tmp[0][i];
        }

        // Criando a animação com 0.1s por frame
        animacao = new Animation<>(0.1f, frames);
        animacao.setPlayMode(Animation.PlayMode.LOOP);

        // Carrega a fonte apenas na primeira Llama criada
        if (fontTexto == null) {
            fontTexto = new BitmapFont(Gdx.files.internal("fontes.fnt"));
            fontTexto.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        }
    }

    @Override
    public int atualizar(float delta, Array<Inimigo> listaInimigos, Array<Projetil> listaProjeteis) {
        stateTime += delta;
        int dinheiroGerado = 0;

        // Identifica qual frame está tocando (de 0 a 16)
        int frameAtual = animacao.getKeyFrameIndex(stateTime);

        // O frame 17 da imagem é o índice 16 no código
        if (frameAtual == 16) {
            if (!jaDeuDinheiroNesseCiclo) {
                dinheiroGerado = 10;
                jaDeuDinheiroNesseCiclo = true;

                // Dispara a subida do texto
                mostrarTexto = true;
                tempoTexto = 0f;
                textoY = posicao.y + alturaDesenho; // Começa no topo da cabeça
            }
        } else {
            // Se saiu do frame 17, destrava para o próximo giro da animação
            jaDeuDinheiroNesseCiclo = false;
        }

        // Movimenta o texto para cima
        if (mostrarTexto) {
            tempoTexto += delta;
            textoY += 40 * delta; // Sobe 40 pixels por segundo

            if (tempoTexto > 1.5f) { // Depois de 1.5s, o texto some
                mostrarTexto = false;
            }
        }

        return dinheiroGerado;
    }

    @Override
    public void desenhar(SpriteBatch batch) {
        // Desenha o frame atual da animação
        TextureRegion frameAtual = animacao.getKeyFrame(stateTime);
        batch.draw(frameAtual, posicao.x, posicao.y, larguraDesenho, alturaDesenho);

        // Desenha o texto flutuante "+50"
        if (mostrarTexto && fontTexto != null) {
            fontTexto.getData().setScale(0.8f);
            fontTexto.setColor(1f, 0.8f, 0f, 1f); // Amarelo Dourado

            fontTexto.draw(batch, "+10", posicao.x + (larguraDesenho / 2f) - 15, textoY);

            // Reseta a cor para não bugar outras fontes do jogo
            fontTexto.setColor(1f, 1f, 1f, 1f);
            fontTexto.getData().setScale(1.0f);
        }
    }
}
