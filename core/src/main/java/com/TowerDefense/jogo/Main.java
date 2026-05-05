package com.TowerDefense.jogo;

import static com.TowerDefense.jogo.ConfigManager.isFullscreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

// A classe Main herda de "Game" do LibGDX.
// Ela é o motor principal que gerencia as múltiplas telas do seu jogo (Menu, Jogo, Game Over).
public class Main extends Game {

    public BitmapFont fonte, fonte32, fonteNormal, fonteTitulo, fonte18;
    public PopupConfig popup;
    public ShaderProgram blurShader;
    public FrameBuffer fbo;
    public SpriteBatch batch;
    public Music BackgroundMusic;
    public Sound somClique;
    public AssetManager manager;


    // --- MÉTODO CREATE (O "Start" do Motor) ---
    // É chamado automaticamente pelo sistema apenas UMA VEZ, assim que você abre o jogo.
    @Override
    public void create() {
        ConfigManager.construtor();
        this.batch = new SpriteBatch();
        manager = new AssetManager();
        Assets.load();
        Assets.finish();

        BackgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("musics/BackgroundMenuScreen.mp3"));
        ConfigManager.musicaAtual = BackgroundMusic;

        BackgroundMusic.setLooping(true);
        BackgroundMusic.setVolume(ConfigManager.getVolumeReal());
        BackgroundMusic.play();

        manager.load("sounds/somClique.wav", Sound.class);
        manager.finishLoading();
        this.somClique = manager.get("sounds/somClique.wav", Sound.class);
        ConfigManager.tocarEfeito(somClique);


        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Raleway-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();

        param.size = 32;
        fonteTitulo = generator.generateFont(param);

        param.size = 28;
        param.color = Color.WHITE;
        param.minFilter = Texture.TextureFilter.Linear;
        param.magFilter = Texture.TextureFilter.Linear;
        fonte = generator.generateFont(param);

        param.size = 32;
        fonte32 = generator.generateFont(param);

        param.size = 22;
        fonteNormal = generator.generateFont(param);

        param.size =18;
        fonte18 = generator.generateFont(param);

        generator.dispose();


        CursorManager.init();

        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, 1920, 1080, false);

        ShaderProgram.pedantic = false;
        blurShader = new ShaderProgram(
            Gdx.files.internal("blur.vert"),
            Gdx.files.internal("blur.frag")
        );

        ConfigManager.aplicarTudo();
        popup = new PopupConfig(this);

        // Define a tela inicial como o Menu

        // Define qual "canal" a TV vai sintonizar primeiro.
        // O 'this' está passando o próprio Main (a TV inteira) para o Menu,
        // assim o Menu consegue trocar de tela depois usando esse mesmo Main.
        this.setScreen(new MenuScreen(this));
    }

    // --- MÉTODO RENDER (A Atualização Contínua) ---
    // Roda dezenas de vezes por segundo (ex: 60 FPS) enquanto o jogo estiver aberto.
    @Override
    public void render() {
        // O super.render() é vital! É ele quem repassa a ordem de desenhar e atualizar
        // para a tela que está aberta no momento (seja o Menu, seja a GameScreen).
        // Se você apagar essa linha, sua tela vai ficar toda preta!
        super.render();
        // --- ATUALIZADOR DE FPS EM TEMPO REAL ---
        if (ConfigManager.showFps) {
            int fps = Gdx.graphics.getFramesPerSecond();
            String textoFps = "FPS: " + fps;

            // Inicia o desenho na tela
            batch.begin();

            // Define a cor do texto (ex: Amarelo ou Verde para destacar)
            fonte18.setColor(Color.GRAY);

            // Desenha o texto no canto superior esquerdo da tela
            // No LibGDX, o eixo Y=0 é embaixo, então usamos getHeight() para ir pro topo
            float x = 20;
            float y = Gdx.graphics.getHeight() - 20;

            fonte18.draw(batch, textoFps, x, y);

            // Retorna a cor da fonte para branco para não bugar outras telas
            fonte18.setColor(Color.WHITE);

            batch.end();
        }
    }

    @Override
    public void dispose() {
        manager.dispose();
    }
}
