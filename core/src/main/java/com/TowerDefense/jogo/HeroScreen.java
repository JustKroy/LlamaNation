    package com.TowerDefense.jogo;

    import com.badlogic.gdx.Application;
    import com.badlogic.gdx.Gdx;
    import com.badlogic.gdx.Input;
    import com.badlogic.gdx.ScreenAdapter;
    import com.badlogic.gdx.graphics.Color;
    import com.badlogic.gdx.graphics.Cursor;
    import com.badlogic.gdx.graphics.GL20;
    import com.badlogic.gdx.graphics.Pixmap;
    import com.badlogic.gdx.graphics.Texture;
    import com.badlogic.gdx.graphics.g2d.Animation;
    import com.badlogic.gdx.graphics.g2d.BitmapFont;
    import com.badlogic.gdx.graphics.g2d.SpriteBatch;
    import com.badlogic.gdx.graphics.g2d.TextureRegion;
    import com.badlogic.gdx.graphics.glutils.FrameBuffer;
    import com.badlogic.gdx.graphics.glutils.ShaderProgram;
    import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
    import com.badlogic.gdx.math.Rectangle;
    import com.badlogic.gdx.math.Vector2;
    import com.badlogic.gdx.scenes.scene2d.Stage;
    import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
    import com.badlogic.gdx.scenes.scene2d.ui.Skin;
    import com.badlogic.gdx.utils.Array;
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
        private Map<labelLlama, Texture> labels;
        private Map<FramePorClasse, Texture> frames;
        private final FrameBuffer fbo;
        private Texture fboTexture;
        private final ShaderProgram blurShader;
        private final ShapeRenderer shapeRenderer;
        private float scaleAtual, alpha, HoverAlpha;
        private Boolean menuAberto = false;
        BitmapFont fonteTitulo;
        BitmapFont fonteNormal;

            //--------- ARRAY ---------
        //Variável que permite carregar imagens
        private Texture[] Dropdown;
        private HeroType[][] heroisPorClasse;
        private labelLlama[][] labelPorLlama;
        private FramePorClasse[][] framesPorClasse;
        private Botao[] botoesHerois;
        private float[] posX = {20, 270, 20, 270, 20};
        private float[] posY = {580, 580, 330, 330, 80};
        private Botao[] opcoesClasse;
        private Texture heroSpriteSheetAtual, heroImagemEstatica, frameAtual, labelAtual;
        private PainelSkins skinsPanel;
        private final BitmapFont fonte;
        private Array<Texture> HUDimg;
        private Array<Botao> HUDbtn;
        private Botao btnClasse;

        // Vetor para armazenar a posição do mouse convertida para o sistema do jogo
        private Vector2 posMouse = new Vector2();

        //------------------ CONSTRUTOR ------------------

        public HeroScreen(Main game) {
            this.game = game; //Recebe a classe principal
            this.batch = new SpriteBatch(); //Recebe o batch para desenhar na tela
            this.viewport = new StretchViewport(1920, 1080); //Define o tamanho da tela
            stage = new Stage(new ScreenViewport());
            skin = new Skin(Gdx.files.internal("ui/uiskin.json")); //Carrega a skin
            shapeRenderer = new ShapeRenderer();
            skinsPanel = new PainelSkins();
            scaleAtual = 1.0f;
            alpha = 0f;
            HoverAlpha = 0f;
            fbo = new FrameBuffer(Pixmap.Format.RGBA8888, 1920, 1080, false);
            fboTexture = fbo.getColorBufferTexture();
            fboTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

            this.fonte = game.fonte;
            this.fonteTitulo = game.fonteTitulo;
            this.fonteNormal = game.fonteNormal;

            HUDimg = new Array<>();
            HUDimg.add(new Texture("MenuScreen_Background.png"));
            HUDimg.add(new Texture("Painel.jpg"));
            HUDimg.add(new Texture("Frame_aerial.png"));
            HUDimg.add(new Texture("Frame_classic.png"));
            HUDimg.add(new Texture("Frame_legend.png"));
            HUDimg.add(new Texture("Frame_support.png"));
            HUDimg.add(new Texture("Glow.png"));
            HUDimg.get(6).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            HUDimg.get(6).setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

            Dropdown = new Texture[16];
            Dropdown[0] = new Texture("dropdown/Dropdown_Aerial.png");
            Dropdown[1] = new Texture("dropdown/Dropdown_AerialArrow.png");
            Dropdown[2] = new Texture("dropdown/Dropdown_AerialArrowHover.png");
            Dropdown[3] = new Texture("dropdown/Dropdown_AerialHover.png");
            Dropdown[4] = new Texture("dropdown/Dropdown_Classics.png");
            Dropdown[5] = new Texture("dropdown/Dropdown_ClassicsArrow.png");
            Dropdown[6] = new Texture("dropdown/Dropdown_ClassicsArrowHover.png");
            Dropdown[7] = new Texture("dropdown/Dropdown_ClassicsHover.png");
            Dropdown[8] = new Texture("dropdown/Dropdown_Legend.png");
            Dropdown[9] = new Texture("dropdown/Dropdown_LegendArrow.png");
            Dropdown[10] = new Texture("dropdown/Dropdown_LegendArrowHover.png");
            Dropdown[11] = new Texture("dropdown/Dropdown_LegendHover.png");
            Dropdown[12] = new Texture("dropdown/Dropdown_Support.png");
            Dropdown[13] = new Texture("dropdown/Dropdown_SupportArrow.png");
            Dropdown[14] = new Texture("dropdown/Dropdown_SupportArrowHover.png");
            Dropdown[15] = new Texture("dropdown/Dropdown_SupportHover.png");


            labels = new HashMap<>();
            frames = new HashMap<>();

            //--------- COMBOBOX ---------
            // Inicializa o botão principal com a classe padrão (CLASSICOS)
            int baseIndexPrincipal = getDropdownBaseIndex(HeroClasse.CLASSICOS);
            btnClasse = new Botao(
                Dropdown[baseIndexPrincipal + 1], // Normal com Seta
                Dropdown[baseIndexPrincipal + 2], // Hover com Seta
                100, 850, 300, 80, game.somClique
            );

            HeroClasse[] classes = HeroClasse.values();
            opcoesClasse = new Botao[classes.length];

            int i;
            for (i = 0; i < classes.length; i++) {
                int baseIndex = getDropdownBaseIndex(classes[i]);

                opcoesClasse[i] = new Botao(
                    Dropdown[baseIndex],       // Normal sem seta
                    Dropdown[baseIndex + 3],   // Hover sem seta
                    100,
                    850 - (i + 1) * 80,
                    300,
                    80,
                    game.somClique
                );
            }

            //--------- CURSOR ----------
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(0, 0, 0, 0);
            pixmap.fill();

            Cursor cursorVazio = Gdx.graphics.newCursor(pixmap, 0, 0);
            Gdx.graphics.setCursor(cursorVazio);

            pixmap.dispose();

            // -------- CONFIGURAÇÃO INICIAL --------
            classeSelecionada = HeroClasse.CLASSICOS;
            frameAtual = HUDimg.get(3);

            inicializarArraysDados();

            trocarBackground(BackgroundType.CLASSICO);
            trocarHeroi(HeroType.LLAMA);
            trocarLabel(labelLlama.LABEL_LLAMA);

            setBotoesClasseAtual();
            HUDbtn = new Array<>();
            //Voltar
            HUDbtn.add(new Botao(
                new Texture("Back_Button.png"),
                //hover
                new Texture("Back_Button.png"),
                20, 975, 180, 80, game.somClique
            ));

            //Select
            HUDbtn.add(new Botao(
                new Texture("Select_Button.png"),
                new Texture("Select_ButtonHover.png"),
                1000, 430, 400, 100, game.somClique
            ));

            //Skins
            HUDbtn.add(new Botao(
                new Texture("Skins_Button.png"),
                new Texture("Skins_ButtonHover.png"),
                850, 430, 100, 100, game.somClique
            ));

            //Infos
            HUDbtn.add(new Botao(
                new Texture("Infos_Button.png"),
                new Texture("Infos_ButtonHover.png"),
                700, 430, 100, 100, game.somClique
            ));
            //--------- SHADER ---------)
            ShaderProgram.pedantic = false;

            blurShader = new ShaderProgram(
                Gdx.files.internal("blur.vert"),
                Gdx.files.internal("blur.frag")
            );

            if (!blurShader.isCompiled()) {
                System.out.println(blurShader.getLog());
            }

            HeroType[] heroisAtuais = heroisPorClasse[getIndiceClasse()];

            HUDbtn.get(0).setOnClick(() -> game.setScreen(new MenuScreen(game)));
            HUDbtn.get(2).setOnClick(() -> skinsPanel.toggleSkins());
            HUDbtn.get(3).setOnClick(() -> skinsPanel.toggleInfos());

            btnClasse.setOnClick(() -> menuAberto = !menuAberto);
            for (i = 0; i < opcoesClasse.length; i++) {
                final int index = i;

                opcoesClasse[i].setOnClick(() -> {
                    classeSelecionada = HeroClasse.values()[index];
                    setBotoesClasseAtual();
                    menuAberto = false;
                });
            }

        }
        private void inicializarArraysDados() {
            // Inicializa os arrays principais
            heroisPorClasse = new HeroType[4][];
            labelPorLlama = new labelLlama[4][];
            framesPorClasse = new FramePorClasse[4][];

            // --- CLASSICOS ---
            heroisPorClasse[0] = new HeroType[]{
                HeroType.LLAMA, HeroType.MAGELLAMA, HeroType.NINJALLAMA, HeroType.ROBOTLLAMA
            };
            labelPorLlama[0] = new labelLlama[]{
                labelLlama.LABEL_LLAMA, labelLlama.LABEL_MAGELLAMA, labelLlama.LABEL_NINJALLAMA, labelLlama.LABEL_ROBOTLLAMA
            };
            framesPorClasse[0] = new FramePorClasse[]{
                FramePorClasse.LLAMACLASSIC, FramePorClasse.MAGECLASSIC, FramePorClasse.NINJACLASSIC, FramePorClasse.CYBORGCLASSIC
            };

            // --- SUPORTES ---
            heroisPorClasse[1] = new HeroType[]{ HeroType.BURGUESA, HeroType.YETI };
            labelPorLlama[1] = new labelLlama[]{ labelLlama.LABEL_BURGUESA };
            framesPorClasse[1] = new FramePorClasse[]{ FramePorClasse.BURGUESSUPPORT, FramePorClasse.YETISUPPORT };

            // --- AEREOS ---
            heroisPorClasse[2] = new HeroType[]{ HeroType.ANJOLLAMA };
            labelPorLlama[2] = new labelLlama[]{ labelLlama.LABEL_ANJOLLAMA };
            framesPorClasse[2] = new FramePorClasse[]{ FramePorClasse.ANGELAERIAL };

            // --- LENDAS ---
            heroisPorClasse[3] = new HeroType[]{ HeroType.CHEF };
            labelPorLlama[3] = new labelLlama[]{ labelLlama.LABEL_CHEF };
            framesPorClasse[3] = new FramePorClasse[]{ FramePorClasse.CHEFLEGEND };
        }

        private int getDropdownBaseIndex(HeroClasse classe) {
            return switch (classe) {
                case AEREOS -> 0;    // 0 a 3
                case CLASSICOS -> 4; // 4 a 7
                case LENDAS -> 8;    // 8 a 11
                case SUPORTES -> 12; // 12 a 15
            };
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
            LENDAS

        }

        public void setClasseSelecionada (HeroClasse classe) {
            this.classeSelecionada = classe;
        }


        private void setBotoesClasseAtual() {

            HeroType[] heroisAtuais = heroisPorClasse[getIndiceClasse()];
            FramePorClasse[] framesAtuais = framesPorClasse[getIndiceClasse()];
            labelLlama[] labelsAtuais = labelPorLlama[getIndiceClasse()];

            botoesHerois = new Botao[heroisAtuais.length];

            for (int i = 0; i < heroisAtuais.length; i++) {

                Texture texturaFrame;

                if (i < framesAtuais.length) {
                    texturaFrame = getFrameTextura(framesAtuais[i]);
                } else {
                    texturaFrame = frameAtual;
                }

                float x = 20 + (i % 2) * 250;
                float y = 580 - (i / 2) * 250;

                botoesHerois[i] = new Botao(texturaFrame, texturaFrame, x, y, 200, 200, game.somClique);

                final int index = i;

                botoesHerois[i].setOnClick(() -> {
                    for (Botao btn : botoesHerois) btn.setSelecionado(false);

                    botoesHerois[index].setSelecionado(true);
                    botoesHerois[index].setCorBorda(getCorBordaClasse());

                    if (index < heroisAtuais.length) trocarHeroi(heroisAtuais[index]);
                    if (index < labelsAtuais.length) trocarLabel(labelsAtuais[index]);
                });
            }

            // Atualiza padrão da classe
            switch (classeSelecionada) {
                case CLASSICOS:
                    frameAtual = HUDimg.get(3);
                    trocarBackground(BackgroundType.CLASSICO);
                    trocarHeroi(HeroType.LLAMA);
                    trocarLabel(labelLlama.LABEL_LLAMA);
                    break;

                case SUPORTES:
                    frameAtual = HUDimg.get(5);
                    trocarBackground(BackgroundType.SUPPORT);
                    trocarHeroi(HeroType.BURGUESA);
                    trocarLabel(labelLlama.LABEL_BURGUESA);
                    break;

                case AEREOS:
                    frameAtual = HUDimg.get(2);
                    trocarBackground(BackgroundType.AERIAL);
                    trocarHeroi(HeroType.ANJOLLAMA);
                    trocarLabel(labelLlama.LABEL_ANJOLLAMA);
                    break;

                case LENDAS:
                    frameAtual = HUDimg.get(4);
                    trocarBackground(BackgroundType.LENDA);
                    trocarHeroi(HeroType.CHEF);
                    trocarLabel(labelLlama.LABEL_CHEF);
                    break;
            }

            int baseIndex = getDropdownBaseIndex(classeSelecionada);
            btnClasse.setTextura(Dropdown[baseIndex + 1], Dropdown[baseIndex + 2]);
        }

        //Função que inicializa imagem de acordo com o tipo(animação ou estática)
        public enum HeroType {

            //FALSE - ESTÁTICO && TRUE - ANIMADO
            LLAMA("Llama.png", false, 260, 280, "Llama", "Cost: 50\nDamage: 80\nRange: 350\nSPA: 2.0s"),
            MAGELLAMA("MageLlama.png", false, 300, 300, "Mage Llama", "Cost: 100\nDamage: 100\nRange: 350\nSPA: 2.0s"),
            NINJALLAMA("NinjaLlama.png", false, 330, 300, "Ninja Llama", "Cost: 150\nDamage: 120\nRange: 350\nSPA: 2.0s"),
            ROBOTLLAMA("CyborgLlama.png", false, 260, 280, "Cyborg Llama", "Cost: 200\nDamage: 140\nRange: 350\nSPA: 2.0s"),
            ANJOLLAMA("AngelLlama.png", false, 350, 300, "Angel Llama", "Cost: 250\nDamage: 160\nRange: 350\nSPA: 2.0s"),
            BURGUESA("BourgeoisLlama.png", false, 330, 300, "Bourgeois Llama", "Cost: 300\nDamage: 180\nRange: 350\nSPA: 2.0s"),
            CHEF("ChefLlama.png", false, 330, 300, "Chef Llama", "Cost: 350\nDamage: 200\nRange: 350\nSPA: 2.0s"),
            YETI("YetiLlama.png", false, 260, 280, "Yeti Llama", "Cost: 400\nDamage: 220\nRange: 350\nSPA: 2.0s");

            public final String sprite; //Caminho da imagem
            public final boolean animado; //Se é animado ou estático
            public final float largura; //Largura
            public final float altura; //Altura
            public final String nome;
            public final String status;

            //Construtor que passa os parâmetros para a classe
            HeroType(String sprite, boolean animado, float largura, float altura, String nome, String status) {
                this.sprite = sprite;
                this.animado = animado;
                this.largura = largura;
                this.altura = altura;
                this.nome = nome;
                this.status = status;
            }
        }


        //Função que troca o herói passano o parâmetro tipo
        private void trocarHeroi(HeroType tipo) {

            alpha = 0f;
            scaleAtual = 0.7f; // começa menor e cresce

            heroSelecionado = tipo; //Salva o tipo

            //--------- LIMPA A MEMÓRIA ---------
            if (heroSpriteSheetAtual != null) heroSpriteSheetAtual.dispose();
            if (heroImagemEstatica != null) heroImagemEstatica.dispose();

            // Carrega apenas UMA VEZ
            Texture texturaBase = new Texture(Gdx.files.internal(tipo.sprite));

            //--------- CARREGA A IMG ---------
            if(tipo.animado){ //Se for animação

                heroSpriteSheetAtual = texturaBase; //Carrega a imagem

                TextureRegion[][] tmp = TextureRegion.split(texturaBase, 64, 64); //Quebra a imagem em partes  de 64x64(Split)

                heroAnimacaoAtual = new Animation<>(0.08f, tmp[0]); //Cria a animação, juntando essas partes

                heroAnimacaoAtual.setPlayMode(Animation.PlayMode.LOOP); //Faz a animação repetir

                heroImagemEstatica = null; //Limpa a imagem estática

            }else{
                //Carrega a imagem estática
                heroImagemEstatica = texturaBase;

                //Limpa a animação
                heroAnimacaoAtual = null;
                heroSpriteSheetAtual = null;
            }

            tempoAnimacao = 0; //Limpa o tempo da animação
            skinsPanel.setHeroiAtual(tipo);
        }

        public enum labelLlama {
            LABEL_LLAMA("LlamaLabel.png"),
            LABEL_MAGELLAMA("LlamaLabel.png"),
            LABEL_NINJALLAMA("LlamaLabel.png"),
            LABEL_ROBOTLLAMA("LlamaLabel.png"),
            LABEL_ANJOLLAMA("LlamaLabel.png"),
            LABEL_BURGUESA("LlamaLabel.png"),
            LABEL_CHEF("LlamaLabel.png");

            public final String label;

            labelLlama(String label) {
                this.label = label;
            }

        }

            private void trocarLabel(labelLlama tipo) {
                // Se a textura ainda não foi carregada, carregue-a agora
                if (!labels.containsKey(tipo)) {
                    labels.put(tipo, new Texture(Gdx.files.internal(tipo.label)));
                }
                labelAtual = labels.get(tipo);
            }

        private Texture getFrameTextura(FramePorClasse tipo) {
            if (!frames.containsKey(tipo)) {
                frames.put(tipo, new Texture(Gdx.files.internal(tipo.frame)));
            }
            return frames.get(tipo);
        }

        public enum BackgroundType {
            CLASSICO,
            SUPPORT,
            AERIAL,
            LENDA
        }

        public Color getCorBordaClasse() {
            return switch(classeSelecionada) {
                case CLASSICOS -> new Color(0.55f, 0.75f, 0.55f, 1f);
                case SUPORTES -> new Color(0.85f, 0.65f, 0.85f, 1f);
                case AEREOS -> new Color(0.45f, 0.65f, 0.85f, 1f);
                case LENDAS -> new Color(0.95f, 0.80f, 0.35f, 1f);
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

        public enum FramePorClasse {
            LLAMACLASSIC("LlamaFrame_Classic.png"),
            MAGECLASSIC("MageFrame_Classic.png"),
            CYBORGCLASSIC("CyborgFrame_Classic.png"),
            NINJACLASSIC("NinjaFrame_Classic.png"),
            BURGUESSUPPORT("BurguesaFrame_Suporte.png"),
            ANGELAERIAL("AngelFrame_Aereo.png"),
            YETISUPPORT("YetiFrame_Suporte.png"),
            CHEFLEGEND("ChefFrame_Legend.png");

            public final String frame;
            FramePorClasse(String frame) {
                this.frame = frame;
            }
        }

        @Override
        public void render(float delta) {

            // --- 1. LIMPAR TELA ---
            ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1);
            viewport.apply();
            batch.setProjectionMatrix(viewport.getCamera().combined);

            // --- 2. MOUSE ---
            Vector2 mouseCru = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

            posMouse.set(
                ConfigManager.processarMouseX(mouseCru.x),
                ConfigManager.processarMouseY(mouseCru.y)
            );

            boolean clicou = Gdx.input.justTouched(); // evento
            boolean pressionando = Gdx.input.isButtonPressed(Input.Buttons.LEFT); // estado

            // --- 3. CURSOR ---
            CursorManager.setDefault();

            if (skinsPanel.isAberto() && skinsPanel.estaSobre(posMouse)) {
                CursorManager.setHover();
            }

            // --- 4. ANIMAÇÃO ---
            tempoAnimacao += delta;

            if (alpha < 1f) alpha = Math.min(1f, alpha + delta * 3f);
            if (scaleAtual < 1f) scaleAtual = Math.min(1f, scaleAtual + delta * 2.5f);

            // --- 5. CLIQUES ---
            tratarCliquesHeroScreen();

            // --- 6. FUNDO ---
            float offsetX = (posMouse.x - 960) * 0.01f;
            float offsetY = (posMouse.y - 540) * 0.01f;

            batch.begin();
            batch.draw(HUDimg.get(0), offsetX, offsetY, 1920, 1080);
            batch.end();

            // --- 7. HERÓI + LABEL ---
            batch.begin();

            renderizarHeroiComGlow(delta);

            if (labelAtual != null) {
                batch.draw(labelAtual, 700, 900, 700, 150);
            }

            batch.end();

            // --- 8. BOTÕES ---
            batch.begin();

            for (Botao btn : botoesHerois) btn.Exibir(batch, posMouse, pressionando);
            for (Botao btn : HUDbtn) btn.Exibir(batch, posMouse, pressionando);

            btnClasse.Exibir(batch, posMouse, pressionando);

            // texto da classe
            fonteNormal.draw(batch, classeSelecionada.name(),
                btnClasse.getArea().x + 20,
                btnClasse.getArea().y + 50
            );

            if (menuAberto) {
                for (int i = 0; i < opcoesClasse.length; i++) {
                    opcoesClasse[i].Exibir(batch, posMouse,pressionando);
                    fonteNormal.draw(batch, HeroClasse.values()[i].name(),
                        opcoesClasse[i].getArea().x + 20,
                        opcoesClasse[i].getArea().y + 50);
                }
            }

            batch.end();

            // --- 9. SOMBRAS ---
            shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
            Gdx.gl.glEnable(GL20.GL_BLEND);

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            renderizarSombrasBotoes();
            shapeRenderer.end();

            // --- 10. PAINEL (SEMPRE POR CIMA) ---
            if (skinsPanel.isAberto()) {
                batch.begin();
                skinsPanel.render(batch);
                batch.end();
            }

            // --- 11. CURSOR FINAL ---
            if (Gdx.app.getType() != Application.ApplicationType.Android &&
                Gdx.app.getType() != Application.ApplicationType.iOS) {

                CursorManager.aplicarCursorInvisivel();

                batch.begin();
                CursorManager.desenhar(batch, posMouse);
                batch.end();
            }
        }

        private void renderizarHeroiComGlow(float delta) {
            float tempo = tempoAnimacao;
            float pulso = (float) Math.sin(tempo * 3f) * 0.5f + 0.5f;

            float larguraEscalada = heroSelecionado.largura * scaleAtual;
            float alturaEscalada = heroSelecionado.altura * scaleAtual;
            float x = 1050 + (heroSelecionado.largura - larguraEscalada) / 2;
            float y = 580 + (heroSelecionado.altura - alturaEscalada) / 2;
            float floatY = (float) Math.sin(tempo * 2f) * 8f * HoverAlpha;
            float yFinal = y + floatY;

            // Glow
            batch.setColor(corBackground.r, corBackground.g, corBackground.b, 0.85f + (0.15f * HoverAlpha));
            batch.draw(HUDimg.get(6), x - 220, yFinal - 190, 750, 750);

            // Lógica de Hover da Llama
            Rectangle areaLlama = new Rectangle(x, yFinal, larguraEscalada, alturaEscalada);
            if (areaLlama.contains(posMouse)) HoverAlpha += delta * 6f;
            else HoverAlpha -= delta * 6f;
            HoverAlpha = Math.max(0f, Math.min(1f, HoverAlpha));

            // Desenho do Herói (Sombra + Sprite)
            batch.setColor(0f, 0f, 0f, 0.35f * (0.5f + HoverAlpha * 0.5f));
            if (heroAnimacaoAtual != null) {
                batch.draw(heroAnimacaoAtual.getKeyFrame(tempoAnimacao, true), x + 10, yFinal - 10, larguraEscalada, alturaEscalada);
                batch.setColor(1, 1, 1, alpha);
                batch.draw(heroAnimacaoAtual.getKeyFrame(tempoAnimacao, true), x, yFinal, larguraEscalada, alturaEscalada);
            } else {
                batch.draw(heroImagemEstatica, x + 10, yFinal - 10, larguraEscalada, alturaEscalada);
                batch.setColor(1, 1, 1, alpha);
                batch.draw(heroImagemEstatica, x, yFinal, larguraEscalada, alturaEscalada);
            }
            batch.setColor(1, 1, 1, 1);
        }

        private void renderizarSombrasBotoes() {
            for (Botao btn : HUDbtn) {
                Rectangle r = btn.getArea();
                shapeRenderer.setColor(0, 0, 0, 0.2f);
                shapeRenderer.rect(r.x + 3, r.y - 3, r.width, r.height);
            }
        }

        private void tratarCliquesHeroScreen() {

            boolean pressionando = Gdx.input.isButtonPressed(Input.Buttons.LEFT);

            for (Botao btn : botoesHerois) btn.atualizar(posMouse, pressionando);
            for (Botao btn : HUDbtn) btn.atualizar(posMouse, pressionando);


            btnClasse.atualizar(posMouse, pressionando);

            if (menuAberto) {
                for (Botao b : opcoesClasse) {
                    b.atualizar(posMouse, pressionando);
                }
            }

            if (skinsPanel.isAberto()) {
                skinsPanel.detectarClique(posMouse.x, posMouse.y);
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
