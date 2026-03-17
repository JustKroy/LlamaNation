package com.TowerDefense.jogo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class HeroScreen extends ScreenAdapter {

    private final Main game; //Variável que referencia a classe principal
    private final SpriteBatch batch; //Variável que permite desenhar na tela
    private final StretchViewport viewport; //Variável que permite definir o tamanho da tela
    private Animation<TextureRegion> heroAnimacaoAtual; //Variável que permite mexer com animação
    private float tempoAnimacao; //Variável que permite mexer com animação (tempo)
    private HeroType heroSelecionado; //Variável que inicializa o tipo do herói
    private HeroClasse classeSelecionada; //Variável que inicializa o tipo da classe
    private BackgroundType backgroundSelecionado; //Variável que inicializa o tipo do background
    private Color corBackground; //Variável que permite mudar a cor do background
    private Stage stage; //Variável que permite desenhar na tela
    private Skin skin; //Variável que permite carregar imagens
    private SelectBox<HeroClasse> ListaClasse; //Variável que inicia um SelectBox


        //--------- ARRAY ---------
    //Variável que permite carregar imagens
    private Texture[] LlamasClassico;
    private Texture[] LlamasAereo;
    private Texture[] HUDimg;
    private Botao[] btnLlamasClassico;
    private Botao[] btnLlamasSuporte;
    private Botao[] btnLlamasAereo;
    private Botao[] btnLlamasLenda;

    private Botao[] HUDbtn;
    private Texture heroSpriteSheetAtual, heroImagemEstatica;

    // Vetor para armazenar a posição do mouse convertida para o sistema do jogo
    private Vector2 posMouse = new Vector2();

    //------------------ CONSTRUTOR ------------------

    public HeroScreen(Main game) {
        this.game = game; //Recebe a classe principal
        this.batch = new SpriteBatch(); //Recebe o batch para desenhar na tela
        this.viewport = new StretchViewport(1920, 1080); //Define o tamanho da tela
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("ui/uiskin.json")); //Carrega a skin

        //--------- COMBOBOX ---------
        ListaClasse = new SelectBox<>(skin);
            ListaClasse.setItems(
                HeroClasse.CLASSICOS,
                HeroClasse.SUPORTES,
                HeroClasse.AEREOS,
                HeroClasse.LENDAS
            );
            ListaClasse.setPosition(100,850);
            ListaClasse.setSize(300,50);
            ListaClasse.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                   classeSelecionada = ListaClasse.getSelected();
                }
            });
        //Centraliza o texto que aparece no botão do SelectBox
        ListaClasse.setAlignment(Align.center);

        //Define o limite de itens que aparecem no selectbox
        ListaClasse.setMaxListCount(4);

        //Centraliza o texto que aparece no botão do SelectBox
        ListaClasse.getList().setAlignment(Align.center);

        //Altera a cor
        ListaClasse.getStyle().fontColor = Color.WHITE;

        ListaClasse.getList().getStyle().fontColorSelected = Color.WHITE;
        ListaClasse.getList().getStyle().fontColorUnselected = Color.LIGHT_GRAY;

        stage.addActor(ListaClasse);
        Gdx.input.setInputProcessor(stage); //Entende o clique do mouse


        //--------- Imagens -----------

        LlamasClassico = new Texture[4];
            LlamasClassico[0] = new Texture("Llama.png");
            LlamasClassico[1] = new Texture("LlamaMage.png");
            LlamasClassico[2] = new Texture("LlamaNinja.png");
            LlamasClassico[3] = new Texture("LlamaRobo.png");
        LlamasAereo = new Texture[1];
            LlamasAereo[0] = new Texture("LlamaAnjo.png");

        HUDimg = new Texture[11];
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
            HUDimg[10] = new Texture("glow.png");
            HUDimg[10].setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

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

        btnLlamasClassico = new Botao[5];

            //Llama
            btnLlamasClassico[0] = new Botao(
                new Texture("frame_classic.png"),
                new Texture("frame_classic.png"),
                20, 580, 200, 200
            );

            //LlamaMage
            btnLlamasClassico[1] = new Botao(
                new Texture("frame_classic.png"),
                new Texture("frame_classic.png"),
                270, 580, 200, 200
            );

            //LlamaNinja
            btnLlamasClassico[2] = new Botao(
                new Texture("frame_classic.png"),
                new Texture("frame_classic.png"),
                20, 330, 200, 200
            );

            //LlamaRobo
            btnLlamasClassico[3] = new Botao(
                new Texture("frame_classic.png"),
                new Texture("frame_classic.png"),
                270, 330, 200, 200
            );

            //LlamaAnjo
            btnLlamasClassico[4] = new Botao(
                new Texture("frame_classic.png"),
                new Texture("frame_classic.png"),
                20, 80, 200, 200
            );
        btnLlamasSuporte = new Botao[5];

            //Llama
            btnLlamasSuporte[0] = new Botao(
                new Texture("frame_support.png"),
                new Texture("frame_support.png"),
                20, 580, 200, 200
            );

            //LlamaMage
            btnLlamasSuporte[1] = new Botao(
                new Texture("frame_support.png"),
                new Texture("frame_support.png"),
                270, 580, 200, 200
            );

            //LlamaNinja
            btnLlamasSuporte[2] = new Botao(
                new Texture("frame_support.png"),
                new Texture("frame_support.png"),
                20, 330, 200, 200
            );

            //LlamaRobo
            btnLlamasSuporte[3] = new Botao(
                new Texture("frame_support.png"),
                new Texture("frame_support.png"),
                270, 330, 200, 200
            );

            //LlamaAnjo
            btnLlamasSuporte[4] = new Botao(
                new Texture("frame_support.png"),
                new Texture("frame_support.png"),
                20, 80, 200, 200
            );
        btnLlamasAereo = new Botao[5];

            //Llama
            btnLlamasAereo[0] = new Botao(
                new Texture("frame_aerial.png"),
                new Texture("frame_aerial.png"),
                20, 580, 200, 200
            );

            //LlamaMage
            btnLlamasAereo[1] = new Botao(
                new Texture("frame_aerial.png"),
                new Texture("frame_aerial.png"),
                270, 580, 200, 200
            );

            //LlamaNinja
            btnLlamasAereo[2] = new Botao(
                new Texture("frame_aerial.png"),
                new Texture("frame_aerial.png"),
                20, 330, 200, 200
            );

            //LlamaRobo
            btnLlamasAereo[3] = new Botao(
                new Texture("frame_aerial.png"),
                new Texture("frame_aerial.png"),
                270, 330, 200, 200
            );

            //LlamaAnjo
            btnLlamasAereo[4] = new Botao(
                new Texture("frame_aerial.png"),
                new Texture("frame_aerial.png"),
                20, 80, 200, 200
            );
        btnLlamasLenda = new Botao[5];

            //Llama
            btnLlamasLenda[0] = new Botao(
                new Texture("frame_legend.png"),
                new Texture("frame_legend.png"),
                20, 580, 200, 200
            );

            //LlamaMage
            btnLlamasLenda[1] = new Botao(
                new Texture("frame_legend.png"),
                new Texture("frame_legend.png"),
                270, 580, 200, 200
            );

            //LlamaNinja
            btnLlamasLenda[2] = new Botao(
                new Texture("frame_legend.png"),
                new Texture("frame_legend.png"),
                20, 330, 200, 200
            );

            //LlamaRobo
            btnLlamasLenda[3] = new Botao(
                new Texture("frame_legend.png"),
                new Texture("frame_legend.png"),
                270, 330, 200, 200
            );

            //LlamaAnjo
            btnLlamasLenda[4] = new Botao(
                new Texture("frame_legend.png"),
                new Texture("frame_legend.png"),
                20, 80, 200, 200
            );


        //----------- PRÉ IMG ----------
            setClasseSelecionada(HeroClasse.CLASSICOS);
            trocarBackground(BackgroundType.CLASSICO); //Inicializa o background com CLASSICOS
            trocarHeroi(HeroType.LLAMA); //Inicializa o herói com LLAMA

    }

    public enum HeroClasse {
        CLASSICOS,
        SUPORTES,
        AEREOS,
        LENDAS;

    }

    public void setClasseSelecionada (HeroClasse classe) {
        this.classeSelecionada = classe;
    }

    private Botao[] getBotoesClasseAtual() {
        switch (classeSelecionada) {
            case CLASSICOS:
                return btnLlamasClassico;
            case SUPORTES:
                return btnLlamasSuporte;
            case AEREOS:
                return btnLlamasAereo;
            case LENDAS:
                return btnLlamasLenda;
            default:
                return btnLlamasClassico;
        }
    }

    //Função que inicializa imagem de acordo com o tipo(animação ou estática)
    public enum HeroType {

        //FALSE - ESTÁTICO && TRUE - ANIMADO
        LLAMA("Llama.png", false, 260, 280),
        MAGELLAMA("LlamaMage.png", false, 300, 300),
        NINJALLAMA("LlamaNinja.png", false, 330, 300),
        ROBOTLLAMA("LlamaRobo.png", false, 260, 280),
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
                corBackground = new Color(0.55f, 0.68f, 0.82f, 1f);
                break;

            case SUPPORT:
                corBackground = new Color(0.60f, 0.78f, 0.60f, 1f);
                break;

            case AERIAL:
                corBackground = new Color(0.68f, 0.60f, 0.85f, 1f);
                break;

            case LENDA:
                corBackground = new Color(0.85f, 0.76f, 0.52f, 1f);
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

        for (Botao btn : getBotoesClasseAtual()) {
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

            // glow externo
            batch.setColor(corBackground.r, corBackground.g, corBackground.b, 1f); //0.25f
            batch.draw(HUDimg[10], 720, 260, 1000, 1000);

            // glow interno
            batch.setColor(corBackground.r, corBackground.g, corBackground.b, 1f); //0.45f
            batch.draw(HUDimg[10], 880, 420, 650, 650);

            // volta a cor normal
            batch.setColor(1f, 1f, 1f, 1f);

            //Define a largura e altura do herói selecionado
            float largura = heroSelecionado.largura;
            float altura = heroSelecionado.altura;


            //Se estiver animado, desenha a animação, caso contrário, desenha a imagem estática
            if (heroAnimacaoAtual != null) {
                TextureRegion frameAtual = heroAnimacaoAtual.getKeyFrame(tempoAnimacao, true); //Pega o frame atual da animação
                batch.draw(frameAtual, 1050, 600, largura, altura); //Desenha
            } else {
                batch.draw(heroImagemEstatica, 1050, 600, largura, altura);
            }

            batch.draw(HUDimg[1], 0, 0, 500, 950);
            batch.draw(HUDimg[2], 700, 950, 700, 120);


        //------------ BOTOES ------------

        for (Botao btn : getBotoesClasseAtual()) {
            btn.Exibir(batch, posMouse);
        }

            for(Botao btn : HUDbtn) {
                btn.Exibir(batch, posMouse);
            }

        batch.end();
        stage.act(delta); //Atualiza o stage
        stage.draw(); //Desenha o stage

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

            if(btnLlamasClassico[0].foiClicado(posMouse)){
                trocarHeroi(HeroType.LLAMA);
                trocarBackground(BackgroundType.CLASSICO);
            }

            if(btnLlamasClassico[1].foiClicado(posMouse)){
                trocarHeroi(HeroType.MAGELLAMA);
                trocarBackground(BackgroundType.SUPPORT);
            }

            if(btnLlamasClassico[2].foiClicado(posMouse)){
                trocarHeroi(HeroType.NINJALLAMA);
                trocarBackground(BackgroundType.AERIAL);
            }

            if(btnLlamasClassico[3].foiClicado(posMouse)){
                trocarHeroi(HeroType.ROBOTLLAMA);
                trocarBackground(BackgroundType.LENDA);
            }

            if(btnLlamasClassico[4].foiClicado(posMouse)){
                trocarHeroi(HeroType.ANJOLLAMA);
                trocarBackground(BackgroundType.LENDA);
            }

        }
    }

    @Override
    public void resize(int width, int height) {

        viewport.update(width, height, true); //Atualiza o viewport de acordo com o tamanho da tela
        stage.getViewport().update(width, height, true); //Atualiza o stage de acordo com o tamanho da tela
    }

    @Override
    public void dispose() {
        batch.dispose();
        //------ IMG ------
        for (Texture img : LlamasClassico) {
            img.dispose();
        }
        for (Texture img : LlamasAereo) {
            img.dispose();
        }

        for (Texture img : HUDimg) {
            img.dispose();
        }

        if (heroSpriteSheetAtual != null) {
            heroSpriteSheetAtual.dispose();
        }

        if(heroImagemEstatica != null) {
            heroImagemEstatica.dispose();
        }

        //------ STAGE ------
        stage.dispose();

        //------ SKIN ------
        skin.dispose();


        //----- BTN ------
        for(Botao btn : btnLlamasClassico) {
            btn.dispose();
        }
        for(Botao btn : btnLlamasSuporte) {
            btn.dispose();
        }
        for(Botao btn : btnLlamasAereo) {
            btn.dispose();
        }
        for(Botao btn : btnLlamasLenda) {
            btn.dispose();
        }
        for (Botao btn : HUDbtn) {
            btn.dispose();
        }
    }
}
