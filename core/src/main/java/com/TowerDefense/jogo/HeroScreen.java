package com.TowerDefense.jogo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class HeroScreen extends ScreenAdapter {

    private final Main game;
    private SpriteBatch batch;
    private StretchViewport viewport;

    private Animation<TextureRegion> heroAnimacaoAtual;
    private float tempoAnimacao;
    private HeroType heroSelecionado;

    private Texture imgLabel, imgPainel, imgBackGround, llama2, heroSpriteSheetAtual, heroImagemEstatica;

    private Botao btnVoltar, btnSelect, btnSkin, btnInfos, btnLlama, btnLlamaNinja, btnLlamaMage, btnLlamaRobo, btnLlamaAnjo;

    // Vetor para armazenar a posição do mouse convertida para o sistema do jogo
    private Vector2 posMouse = new Vector2();

    public HeroScreen(Main game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.viewport = new StretchViewport(1920, 1080);

        //--------- Imagens -----------
        imgBackGround = new Texture("backgroundHero.jpg");
        imgPainel = new Texture("painel.jpg");
        imgLabel = new Texture("teste.png");
        llama2 = new Texture("teste.png");

        //-------- Botões ----------
        btnVoltar = new Botao(
            new Texture("botaovoltar.png"),
            //hover
            new Texture("botaovoltar.png"),
            20,975,180,80
        );

        btnSelect = new Botao(
            new Texture("verde.png"),
            new Texture("verde.png"),
            1050, 430, 350, 100
        );

        btnSkin = new Botao(
            new Texture("painel.jpg"),
            new Texture("painel.jpg"),
            850, 430, 100, 100
        );

        btnInfos = new Botao(
            new Texture("painel.jpg"),
            new Texture("painel.jpg"),
            700, 430, 100, 100
        );

        btnLlama = new Botao(
            new Texture("teste.png"),
            new Texture("teste.png"),
            20, 700, 200, 200
        );

        btnLlamaMage = new Botao(
            new Texture("teste.png"),
            new Texture("teste.png"),
            270, 700, 200, 200
        );

        btnLlamaNinja = new Botao(
            new Texture("teste.png"),
            new Texture("teste.png"),
            20, 450, 200, 200
        );

        btnLlamaRobo = new Botao(
            new Texture("teste.png"),
            new Texture("teste.png"),
            270, 450, 200, 200
        );

        btnLlamaAnjo = new Botao(
            new Texture("teste.png"),
            new Texture("teste.png"),
            20, 200, 200, 200
        );

        //----------- PRÉ IMG ----------
            trocarHeroi(HeroType.LLAMA);

    }

        public enum HeroType {

            LLAMA("Llama.png", false, 300, 320),
            MAGELLAMA("LlamaMage.png", true, 350, 300),
            NINJALLAMA("LlamaNinja.png", false, 350, 300),
            ROBOTLLAMA("LlamaRobo.png", true, 350, 300),
            ANJOLLAMA("LlamaAnjo.png", false, 350, 300);

            public final String sprite;
            public final boolean animado;
            public final float largura;
            public final float altura;

            HeroType(String sprite, boolean animado, float largura, float altura) {
                this.sprite = sprite;
                this.animado = animado;
                this.largura = largura;
                this.altura = altura;
            }
        }


    private void trocarHeroi(HeroType tipo) {

        heroSelecionado = tipo;

        if(heroSpriteSheetAtual != null){
            heroSpriteSheetAtual.dispose();
        }

        if(heroImagemEstatica != null){
            heroImagemEstatica.dispose();
        }

        if(tipo.animado){

            heroSpriteSheetAtual = new Texture(tipo.sprite);

            TextureRegion[][] tmp = TextureRegion.split(heroSpriteSheetAtual, 64, 64);

            heroAnimacaoAtual = new Animation<>(0.08f, tmp[0]);

            heroImagemEstatica = null;

        }else{

            heroImagemEstatica = new Texture(tipo.sprite);

            heroAnimacaoAtual = null;
        }

        tempoAnimacao = 0;
    }

    @Override
    public void render(float delta) {

        //Limpa a tela
        ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1);

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        // ATUALIZA O MOUSE EM TODO FRAME
        posMouse = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

        // Cursor padrão
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);

        // Se estiver sobre algum botão, vira mão
        btnVoltar.atualizarCursor(posMouse);
        btnSelect.atualizarCursor(posMouse);
        btnSkin.atualizarCursor(posMouse);
        btnInfos.atualizarCursor(posMouse);
        btnLlama.atualizarCursor(posMouse);
        btnLlamaNinja.atualizarCursor(posMouse);
        btnLlamaMage.atualizarCursor(posMouse);
        btnLlamaRobo.atualizarCursor(posMouse);
        btnLlamaAnjo.atualizarCursor(posMouse);

        tempoAnimacao += delta;

        //----------- IMAGENS -------------
        batch.begin();
            batch.draw(imgBackGround, 0, 0, 1920, 1080);
            batch.draw(imgPainel, 0, 0, 500, 950);
            batch.draw(imgLabel, 700, 950, 700, 120);

            float largura = heroSelecionado.largura;
            float altura = heroSelecionado.altura;

            if (heroAnimacaoAtual != null) {
                TextureRegion frameAtual = heroAnimacaoAtual.getKeyFrame(tempoAnimacao, true);
                batch.draw(frameAtual, 1050, 600, largura, altura);
            } else {
                batch.draw(heroImagemEstatica, 1050, 600, largura, altura);
            }

        batch.draw(llama2, 270, 200, 200, 200);

        //------------ BOTOES ------------
            btnVoltar.Exibir(batch, posMouse);
            btnSelect.Exibir(batch, posMouse);
            btnSkin.Exibir(batch, posMouse);
            btnInfos.Exibir(batch, posMouse);
            btnLlama.Exibir(batch, posMouse);
            btnLlamaNinja.Exibir(batch, posMouse);
            btnLlamaMage.Exibir(batch, posMouse);
            btnLlamaRobo.Exibir(batch, posMouse);
            btnLlamaAnjo.Exibir(batch, posMouse);

        batch.end();

        //---------------- RECTANGLE --------------
        // Detecta se houve um clique na tela ou mouse
        if (Gdx.input.justTouched()) {

            // Converte a posição do mouse da tela para o sistema de coordenadas do jogo
            posMouse = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

            // Verifica se o clique aconteceu dentro da área do botão PLAY
            if (btnVoltar.foiClicado(posMouse)) {
                // Troca a tela atual para a tela do jogo
                game.setScreen(new MenuScreen(game));
            }

            if(btnSelect.foiClicado(posMouse)) {

            }

            if(btnSkin.foiClicado(posMouse)) {

            }

            if(btnInfos.foiClicado(posMouse)) {

            }

            if(btnLlama.foiClicado(posMouse)) {
                trocarHeroi(HeroType.LLAMA);
            }

            if(btnLlamaNinja.foiClicado(posMouse)) {
                trocarHeroi(HeroType.NINJALLAMA);
            }

            if(btnLlamaMage.foiClicado(posMouse)) {
                trocarHeroi(HeroType.MAGELLAMA);
            }

            if(btnLlamaRobo.foiClicado(posMouse)) {
                trocarHeroi(HeroType.ROBOTLLAMA);
            }

            if(btnLlamaAnjo.foiClicado(posMouse)) {
                trocarHeroi(HeroType.ANJOLLAMA);
            }


        }
    }

    @Override
    public void resize(int width, int height) {

        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        //------ IMG ------
        imgBackGround.dispose();
        imgPainel.dispose();
        imgLabel.dispose();
        llama2.dispose();

        if (heroSpriteSheetAtual != null) {
            heroSpriteSheetAtual.dispose();
        }

        //----- BTN ------
        btnSelect.dispose();
        btnVoltar.dispose();
        btnSkin.dispose();
        btnInfos.dispose();
        btnLlama.dispose();
        btnLlamaNinja.dispose();
        btnLlamaMage.dispose();
        btnLlamaRobo.dispose();
        btnLlamaAnjo.dispose();

    }
}
