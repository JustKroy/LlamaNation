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
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.HashMap;
import java.util.Map;

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
    private final Stage stage; //Variável que permite desenhar na tela
    private final Skin skin; //Variável que permite carregar imagens
    private SelectBox<HeroClasse> listaClasse; //Variável que inicia um SelectBox
    private Map<labelLlama, Texture> labels = new HashMap<>();


        //--------- ARRAY ---------
    //Variável que permite carregar imagens
    private Texture[] HUDimg;
    private HeroType[][] heroisPorClasse;
    private labelLlama[][] labelPorLlama;
    private Botao[] botoesHerois;
    private float[] posX = {20, 270, 20, 270, 20};
    private float[] posY = {580, 580, 330, 330, 80};
    private Botao[] HUDbtn;
    private Texture heroSpriteSheetAtual, heroImagemEstatica, frameAtual, labelAtual;

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
        listaClasse = new SelectBox<>(skin);
            listaClasse.setItems(
                HeroClasse.values()
            );
            classeSelecionada = HeroClasse.CLASSICOS;
            listaClasse.setSelected(classeSelecionada);

            listaClasse.setPosition(100,850);
            listaClasse.setSize(300,50);
            listaClasse.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                   classeSelecionada = listaClasse.getSelected();
                   setBotoesClasseAtual();
                }
            });
        //Centraliza o texto que aparece no botão do SelectBox
        listaClasse.setAlignment(Align.center);

        //Define o limite de itens que aparecem no selectbox
        listaClasse.setMaxListCount(4);

        //Centraliza o texto que aparece no botão do SelectBox
        listaClasse.getList().setAlignment(Align.center);

        //Altera a cor
        listaClasse.getStyle().fontColor = Color.WHITE;

        listaClasse.getList().getStyle().fontColorSelected = Color.WHITE;
        listaClasse.getList().getStyle().fontColorUnselected = Color.LIGHT_GRAY;

        stage.addActor(listaClasse);
        Gdx.input.setInputProcessor(stage); //Entende o clique do mouse


        //--------- Imagens -----------

        heroisPorClasse = new HeroType[4][];

            //CLASSICOS
            heroisPorClasse[0] = new HeroType[]{
                HeroType.LLAMA,
                HeroType.MAGELLAMA,
                HeroType.NINJALLAMA,
                HeroType.ROBOTLLAMA
            };

            //SUPORTES
            heroisPorClasse[1] = new HeroType[]{
                HeroType.BURGUESA
            };

            //AEREOS
            heroisPorClasse[2] = new HeroType[]{
                HeroType.ANJOLLAMA
            };

            //LENDAS
            heroisPorClasse[3] = new HeroType[]{
                HeroType.CHEF
            };

        labelPorLlama = new labelLlama[4][];

            //CLÁSSICOS
            labelPorLlama[0] = new labelLlama[]{
                labelLlama.LABEL_LLAMA,
                labelLlama.LABEL_MAGELLAMA,
                labelLlama.LABEL_NINJALLAMA,
                labelLlama.LABEL_ROBOTLLAMA
            };

            //SUPORTES
            labelPorLlama[1] = new labelLlama[]{
                labelLlama.LABEL_BURGUESA
            };

            //AEREOS
            labelPorLlama[2] = new labelLlama[] {
                labelLlama.LABEL_ANJOLLAMA
            };

            //LENDAS
            labelPorLlama[3] = new labelLlama[] {
                labelLlama.LABEL_CHEF
            };


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

        //--------- LABEL LLAMAS -----------
        for (labelLlama l : labelLlama.values()) {
            labels.put(l, new Texture(Gdx.files.internal(l.label)));
        }

        //-------- PADRÃO ---------
        classeSelecionada = HeroClasse.CLASSICOS;
        frameAtual = HUDimg[3];
        trocarBackground(BackgroundType.CLASSICO);
        trocarHeroi(HeroType.LLAMA);
        trocarLabel(labelLlama.LABEL_LLAMA);
        setBotoesClasseAtual();

        //-------- Botões ----------
        botoesHerois = new Botao[5];
        for (int i = 0; i < botoesHerois.length; i++) {
            botoesHerois[i] = new Botao(frameAtual, frameAtual, posX[i], posY[i], 200, 200);
        }

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
                new Texture("BUTTON_select.png"),
                new Texture("BUTTON_selecthover.png"),
                970, 390, 470, 180
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


    }

    private int getIndiceClasse() {
        switch (classeSelecionada) {
            case CLASSICOS: return 0;
            case SUPORTES: return 1;
            case AEREOS: return 2;
            case LENDAS: return 3;
        }
        return 0;
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


    private void setBotoesClasseAtual() {

        if (botoesHerois == null) {
            botoesHerois = new Botao[5];
        }

        switch (classeSelecionada) {
            case CLASSICOS:
                frameAtual = HUDimg[3];
                trocarBackground(BackgroundType.CLASSICO);
                trocarHeroi(HeroType.LLAMA);
                trocarLabel(labelLlama.LABEL_LLAMA);
                break;
            case SUPORTES:
                frameAtual = HUDimg[5];
                trocarBackground(BackgroundType.SUPPORT);
                trocarHeroi(HeroType.BURGUESA);
                trocarLabel(labelLlama.LABEL_BURGUESA);
                break;
            case AEREOS:
                frameAtual = HUDimg[2];
                trocarBackground(BackgroundType.AERIAL);
                trocarHeroi(HeroType.ANJOLLAMA);
                trocarLabel(labelLlama.LABEL_ANJOLLAMA);
                break;
            case LENDAS:
                frameAtual = HUDimg[4];
                trocarBackground(BackgroundType.LENDA);
                trocarHeroi(HeroType.CHEF);
                trocarLabel(labelLlama.LABEL_CHEF);
                break;
        }

        for (int i = 0; i < botoesHerois.length; i++) {
            botoesHerois[i] = new Botao(frameAtual, frameAtual, posX[i], posY[i], 200, 200);
        }

    }

    //Função que inicializa imagem de acordo com o tipo(animação ou estática)
    public enum HeroType {

        //FALSE - ESTÁTICO && TRUE - ANIMADO
        LLAMA("Llama.png", false, 260, 280),
        MAGELLAMA("LlamaMage.png", false, 300, 300),
        NINJALLAMA("LlamaNinja.png", false, 330, 300),
        ROBOTLLAMA("LlamaRobo.png", false, 260, 280),
        ANJOLLAMA("LlamaAnjo.png", false, 350, 300),
        BURGUESA("LlamaBurguesa.png", false, 330, 300),
        CHEF("LlamaChef.png", false, 330, 300);

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

    public enum labelLlama {
        LABEL_LLAMA("LabelsLlamas.png"),
        LABEL_MAGELLAMA("LabelsLlamas.png"),
        LABEL_NINJALLAMA("LabelsLlamas.png"),
        LABEL_ROBOTLLAMA("LabelsLlamas.png"),
        LABEL_ANJOLLAMA("LabelsLlamas.png"),
        LABEL_BURGUESA("LabelsLlamas.png"),
        LABEL_CHEF("LabelsLlamas.png");

        public final String label;

        labelLlama(String label) {
            this.label = label;
        }

    }

        private void trocarLabel(labelLlama label) {
            labelAtual = labels.get(label);
        }

    public enum BackgroundType {
        CLASSICO,
        SUPPORT,
        AERIAL,
        LENDA;
    }

    public Color getCorBordaClasse() {
        return switch(classeSelecionada) {
            case CLASSICOS -> new Color(0.55f, 0.75f, 0.55f, 1f);
            case SUPORTES -> new Color(0.85f, 0.65f, 0.85f, 1f);
            case AEREOS -> new Color(0.45f, 0.65f, 0.85f, 1f);
            case LENDAS -> new Color(0.95f, 0.80f, 0.35f, 1f);
            default -> Color.WHITE;
        };
    }

    public void trocarBackground(BackgroundType tipo) {
        backgroundSelecionado = tipo;

        switch (tipo) {

            case CLASSICO:
                corBackground = new Color(0.55f, 0.75f, 0.55f, 1f);
                break;

            case SUPPORT:
                corBackground = new Color(0.85f, 0.60f, 0.75f, 1f);
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

        //Inicializa os botões de acordo com a classe selecionada
        HeroType[] heroisAtuais = heroisPorClasse[getIndiceClasse()];

        //Inicializa as label de acordo com a llama selecionada
        labelLlama[] labelsAtuais = labelPorLlama[getIndiceClasse()];

        // Se estiver sobre algum botão, vira mão

        for (Botao btn : botoesHerois) {
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
            //Fundo principal
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
                batch.draw(frameAtual, 1050, 580, largura, altura); //Desenha
            } else {
                batch.draw(heroImagemEstatica, 1050, 580, largura, altura);
            }

            batch.draw(HUDimg[1], 0, 0, 500, 950);
            //Label
        if (labelAtual != null) {
            batch.draw(labelAtual, 700, 950, 700, 120);
        }


        //------------ BOTOES ------------

        for (Botao btn : botoesHerois) {
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

            //Loop para exibir as llamas de acordo com o tipo de botão
            for (int i = 0; i < botoesHerois.length; i++) {
                if (botoesHerois[i].foiClicado(posMouse)) {

                    for (Botao btn : botoesHerois) {
                        btn.setSelecionado(false);
                    }

                    botoesHerois[i].setSelecionado(true); //Define o botão clicado como selecionado
                    botoesHerois[i].setCorBorda(getCorBordaClasse()); //Define a borda do botão clicado

                    if (i < heroisAtuais.length) {
                        trocarHeroi(heroisAtuais[i]);
                    }

                    //Troca a label de acordo com o botão clicado
                    if (i < labelsAtuais.length) {
                        trocarLabel(labelsAtuais[i]);
                    }

                    trocarBackground(backgroundSelecionado);
                }
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

        for (Texture img : HUDimg) {
            img.dispose();
        }

        if (heroSpriteSheetAtual != null) {
            heroSpriteSheetAtual.dispose();
        }

        if(heroImagemEstatica != null) {
            heroImagemEstatica.dispose();
        }

        for (Texture t : labels.values()) {
            t.dispose();
        }

        //------ STAGE ------
        stage.dispose();

        //------ SKIN ------
        skin.dispose();


        //----- BTN ------
        for(Botao btn : botoesHerois) {
            btn.dispose();
        }
        for (Botao btn : HUDbtn) {
            btn.dispose();
        }
    }
}
