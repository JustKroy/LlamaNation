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

    private final Main game; //Variável que referencia a classe principal
    private final SpriteBatch batch; //Variável que permite desenhar na tela
    private final StretchViewport viewport; //Variável que permite definir o tamanho da tela
    private Animation<TextureRegion> heroAnimacaoAtual; //Variável que permite mexer com animação
    private float tempoAnimacao; //Variável que permite mexer com animação (tempo)
    private HeroType heroSelecionado; //Variável que inicializa o tipo do herói
    private BackgroundType backgroundSelecionado; //Variável que inicializa o tipo do background


        //--------- ARRAY ---------
    //Variável que permite carregar imagens
    private Texture[] imagensLlamas;
    private Texture[] HUDimg;
    private Texture[] backgroundCircular;
    private Botao[] btnLlamas;
    private Botao[] HUDbtn;
    private Texture heroSpriteSheetAtual, heroImagemEstatica, backgroundAtual;

    // Vetor para armazenar a posição do mouse convertida para o sistema do jogo
    private Vector2 posMouse = new Vector2();

    //------------------ CONSTRUTOR ------------------

    public HeroScreen(Main game) {
        this.game = game; //Recebe a classe principal
        this.batch = new SpriteBatch(); //Recebe o batch para desenhar na tela
        this.viewport = new StretchViewport(1920, 1080); //Define o tamanho da tela

        //--------- Imagens -----------
        imagensLlamas = new Texture[4];
            imagensLlamas[0] = new Texture("Llama.png");
            imagensLlamas[1] = new Texture("LlamaMage.png");
            imagensLlamas[2] = new Texture("LlamaNinja.png");
            imagensLlamas[3] = new Texture("LlamaRobo.png");

        HUDimg = new Texture[10];
            HUDimg[0] = new Texture("backgroundHero.jpg");
            HUDimg[1] = new Texture("painel.jpg");
            HUDimg[2] = new Texture("frame_aerial.png");
            HUDimg[3] = new Texture("frame_classic.png");
            HUDimg[4] = new Texture("frame_legend.png");
            HUDimg[5] = new Texture("frame_support.png");
            HUDimg[6] = new Texture("botaovoltar.png");
            HUDimg[7] = new Texture("verde.png");
            HUDimg[8] = new Texture("icon_attributes.png");
            HUDimg[9] = new Texture("icon_skin.png");

        //------------- BACKGROUND ------------
        backgroundCircular = new Texture[4];
            backgroundCircular[0] = new Texture("BackgroundClassicos.png");
            backgroundCircular[1] = new Texture("BackgroundSupport.png");
            backgroundCircular[2] = new Texture("BackgroundAerial.png");
            backgroundCircular[3] = new Texture("BackgroundLendas.png");

        //-------- Botões ----------
        HUDbtn = new Botao[4];
            //Voltar
            HUDbtn[0] = new Botao(
                new Texture("botaovoltar.png"),
                //hover
                new Texture("botaovoltar.png"),
                20,975,180,80
            );

            //Select
            HUDbtn[1] = new Botao(
                new Texture("verde.png"),
                new Texture("verde.png"),
                1050, 430, 350, 100
            );

            //Skins
            HUDbtn[2] = new Botao(
                new Texture("icon_skin.png"),
                new Texture("icon_skin.png"),
                850, 430, 100, 100
            );

            //Infos
            HUDbtn[3] = new Botao(
                new Texture("icon_attributes.png"),
                new Texture("icon_attributes.png"),
                700, 430, 100, 100
            );

        btnLlamas = new Botao[5];

            //Llama
            btnLlamas[0] = new Botao(
                new Texture("frame_classic.png"),
                new Texture("frame_classic.png"),
                20, 700, 200, 200
            );

            //LlamaMage
            btnLlamas[1] = new Botao(
                new Texture("frame_classic.png"),
                new Texture("frame_classic.png"),
                270, 700, 200, 200
            );

            //LlamaNinja
            btnLlamas[2] = new Botao(
                new Texture("frame_classic.png"),
                new Texture("frame_classic.png"),
                20, 450, 200, 200
            );

            //LlamaRobo
            btnLlamas[3] = new Botao(
                new Texture("frame_classic.png"),
                new Texture("frame_classic.png"),
                270, 450, 200, 200
            );

            //LlamaAnjo
            btnLlamas[4] = new Botao(
                new Texture("frame_classic.png"),
                new Texture("frame_classic.png"),
                20, 200, 200, 200
            );

        //----------- PRÉ IMG ----------
            backgroundSelecionado = BackgroundType.CLASSICO;
            backgroundAtual = backgroundCircular[0];
            trocarHeroi(HeroType.LLAMA); //Inicializa o herói com LLAMA

    }

    //Função que inicializa imagem de acordo com o tipo(animação ou estática)
    public enum HeroType {

        //FALSE - ESTÁTICO && TRUE - ANIMADO
        LLAMA("Llama.png", false, 270, 300),
        MAGELLAMA("LlamaMage.png", false, 300, 300),
        NINJALLAMA("LlamaNinja.png", false, 350, 300),
        ROBOTLLAMA("LlamaRobo.png", false, 300, 300),
        ANJOLLAMA("LlamaAnjo.png", false, 350, 300);

        public final String sprite; //Caminho da imagem
        public final boolean animado; //Se é animado ou estático
        public final float largura; //Largura
        public final float altura; //Altura

        //Construtor que passa os parâmetros para a classe
        HeroType(String sprite, boolean animado, float largura, float altura) {
            this.sprite = sprite;
            this.animado = animado;
            this.largura = largura;
            this.altura = altura;
        }
    }

    //Função que troca o herói passano o parâmetro tipo
    private void trocarHeroi(HeroType tipo) {

        heroSelecionado = tipo; //Salva o tipo

        //--------- LIMPA A MEMÓRIA ---------
        if(heroSpriteSheetAtual != null){
            heroSpriteSheetAtual.dispose();
        }

        if(heroImagemEstatica != null){
            heroImagemEstatica.dispose();
        }

        //--------- CARREGA A IMG ---------
        if(tipo.animado){ //Se for animação

            heroSpriteSheetAtual = new Texture(tipo.sprite); //Carrega a imagem

            TextureRegion[][] tmp = TextureRegion.split(heroSpriteSheetAtual, 64, 64); //Quebra a imagem em partes  de 64x64(Split)

            heroAnimacaoAtual = new Animation<>(0.08f, tmp[0]); //Cria a animação, juntando essas partes

            heroAnimacaoAtual.setPlayMode(Animation.PlayMode.LOOP); //Faz a animação repetir

            heroImagemEstatica = null; //Limpa a imagem estática

        }else{
            //Carrega a imagem estática
            heroImagemEstatica = new Texture(tipo.sprite);

            //Limpa a animação
            heroAnimacaoAtual = null;
        }

        tempoAnimacao = 0; //Limpa o tempo da animação
    }

    public enum BackgroundType {
        CLASSICO("BackgroundClassicos.png", 300, 300),
        SUPPORT("BackgroundSupport.png", 300, 300),
        AERIAL("BackgroundAerial.png", 300, 300),
        LENDA("BackgroundLendas.png", 300, 300);

        public final String background;
        public final float largura;
        public final float altura;

        BackgroundType(String background, float largura, float altura) {
            this.background = background;
            this.largura = largura;
            this.altura = altura;

        }
    }

    public void trocarBackground(BackgroundType tipo) {
        backgroundSelecionado = tipo;

        switch (tipo) {

            case CLASSICO:
                backgroundAtual = backgroundCircular[0];
                break;

            case SUPPORT:
                backgroundAtual = backgroundCircular[1];
                break;

            case AERIAL:
                backgroundAtual = backgroundCircular[2];
                break;

            case LENDA:
                backgroundAtual = backgroundCircular[3];
                break;
        }

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

        for(Botao btn : btnLlamas) {
            btn.atualizarCursor(posMouse);
        }

        for(Botao btn : HUDbtn) {
            btn.atualizarCursor(posMouse);
        }

        //----------- ANIMACAO -------------
        tempoAnimacao += delta;
        /*representa o tempo, em segundos, decorrido entre o quadro atual e o quadro anterior. Sua principal função é tornar o movimento
         * e a lógica do jogo independentes da taxa de quadros (FPS), garantindo que tudo corra na mesma velocidade, seja em um celular rápido
         * ou lento, mantendo a consistência.
         */

        //----------- IMAGENS -------------
        //Desenha na tela
        batch.begin();
            batch.draw(HUDimg[0], 0, 0, 1920, 1080);
            batch.draw(HUDimg[1], 0, 0, 500, 950);
            batch.draw(HUDimg[2], 700, 950, 700, 120);

            //Define a largura e altura do herói selecionado
            float largura = heroSelecionado.largura;
            float altura = heroSelecionado.altura;
            float larguraB = backgroundSelecionado.largura;
            float alturaB = backgroundSelecionado.altura;


            //Se estiver animado, desenha a animação, caso contrário, desenha a imagem estática
            if (heroAnimacaoAtual != null) {
                TextureRegion frameAtual = heroAnimacaoAtual.getKeyFrame(tempoAnimacao, true); //Pega o frame atual da animação
                batch.draw(backgroundAtual, 1030, 570, larguraB, alturaB);
                batch.draw(frameAtual, 1050, 600, largura, altura); //Desenha
            } else {
                batch.draw(backgroundAtual, 0, 0, larguraB, alturaB);
                batch.draw(heroImagemEstatica, 1050, 600, largura, altura);
            }

        //------------ BOTOES ------------

            for(Botao btn : btnLlamas) {
                btn.Exibir(batch, posMouse);
            }

            for(Botao btn : HUDbtn) {
                btn.Exibir(batch, posMouse);
            }

        batch.end();

        //---------------- RECTANGLE --------------
        // Detecta se houve um clique na tela ou mouse
        if (Gdx.input.justTouched()) {

            // Converte a posição do mouse da tela para o sistema de coordenadas do jogo
            posMouse = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

            // Verifica se o clique aconteceu dentro da área do botão PLAY
            if (HUDbtn[0].foiClicado(posMouse)) {
                // Troca a tela atual para a tela do jogo
                game.setScreen(new MenuScreen(game));
            }

            if(btnLlamas[0].foiClicado(posMouse)){
                trocarHeroi(HeroType.LLAMA);
                trocarBackground(BackgroundType.CLASSICO);
            }

            if(btnLlamas[1].foiClicado(posMouse)){
                trocarHeroi(HeroType.MAGELLAMA);
                trocarBackground(BackgroundType.SUPPORT);
            }

            if(btnLlamas[2].foiClicado(posMouse)){
                trocarHeroi(HeroType.NINJALLAMA);
                trocarBackground(BackgroundType.AERIAL);
            }

            if(btnLlamas[3].foiClicado(posMouse)){
                trocarHeroi(HeroType.ROBOTLLAMA);
                trocarBackground(BackgroundType.LENDA);
            }

            if(btnLlamas[4].foiClicado(posMouse)){
                trocarHeroi(HeroType.ANJOLLAMA);
                trocarBackground(BackgroundType.LENDA);
            }

        }
    }

    @Override
    public void resize(int width, int height) {

        viewport.update(width, height, true); //Atualiza o viewport de acordo com o tamanho da tela
    }

    @Override
    public void dispose() {
        batch.dispose();
        //------ IMG ------
        for (Texture img : imagensLlamas) {
            img.dispose();
        }

        for (Texture img : HUDimg) {
            img.dispose();
        }

        for (Texture img : backgroundCircular) {
            img.dispose();
        }

        if (heroSpriteSheetAtual != null) {
            heroSpriteSheetAtual.dispose();
        }

        if(heroImagemEstatica != null) {
            heroImagemEstatica.dispose();
        }

        //----- BTN ------
        for(Botao btn : btnLlamas) {
            btn.dispose();
        }

        for (Botao btn : HUDbtn) {
            btn.dispose();
        }
    }
}
