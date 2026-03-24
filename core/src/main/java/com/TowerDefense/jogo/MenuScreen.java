package com.TowerDefense.jogo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

// ScreenAdapter indica que esta é uma das telas do nosso jogo (como um "canal de TV")
public class MenuScreen extends ScreenAdapter {

    // --- FERRAMENTAS DO SISTEMA ---
    private final Main game; // O motor principal (usado para trocar de tela)
    private SpriteBatch batch; // O "pincel" para desenhar as imagens
    private StretchViewport viewport; // Mantém a tela esticada na proporção 1920x1080, não importa o monitor

    // --- IMAGENS (TEXTURAS) ---
    private Texture imgPlay; // Imagem do botão de jogar
    private Texture imgHeroes; // Imagem do botão da enciclopédia/loja de heróis
    private Texture imgFundo; // (Opcional) Uma imagem de fundo bem bonita para o menu

    // --- HITBOXES (Áreas de Clique) ---
    private Rectangle btnPlay; // O retângulo invisível que define a área clicável do "Play"
    private Rectangle btnHeroes; // O retângulo invisível do "Heroes"
    private Vector2 posMouse = new Vector2(); // Guarda a posição (X e Y) de onde o mouse/dedo tocou

    // --- CONSTRUTOR ---
    // É chamado no exato momento em que o Main diz: "Abra a tela de Menu!"
    public MenuScreen(Main game) {
        this.game = game;
        this.batch = new SpriteBatch();

        // Define o tamanho "virtual" do nosso menu. Tudo será calculado com base em 1920x1080.
        this.viewport = new StretchViewport(1920, 1080);

        // 1. CARREGANDO AS IMAGENS da pasta 'assets'
        imgPlay = new Texture("play.png");
        imgHeroes = new Texture("heroes.png");
        // imgFundo = new Texture("fundo_menu.png"); // Remova as barras (//) se for usar um fundo!

        // 2. DEFININDO O TAMANHO DOS BOTÕES
        float larguraBtn = 400; // Os botões terão 400 pixels de largura...
        float alturaBtn = 150;  // ...e 150 pixels de altura

        // 3. POSICIONANDO OS BOTÕES (A Matemática de Centralizar)
        // Para centralizar perfeitamente no eixo X:
        // Pegamos a largura total da tela (1920), dividimos por 2 (960, que é o meio),
        // e subtraímos a METADE da largura do botão.
        // Se a gente não subtrair a metade, o botão começa a desenhar no meio e vai para a direita, ficando torto.

        // Botão Play (Mais para cima, no Y = 600)
        btnPlay = new Rectangle(1920 / 2f - larguraBtn / 2f, 600, larguraBtn, alturaBtn);

        // Botão Heroes (Um pouco abaixo do Play, no Y = 400)
        btnHeroes = new Rectangle(1920 / 2f - larguraBtn / 2f, 400, larguraBtn, alturaBtn);
    }

    // --- ATUALIZAÇÃO E DESENHO ---
    // Roda várias vezes por segundo para manter a tela viva
    @Override
    public void render(float delta) {

        // 1. Limpa a tela inteira com a cor preta (para não borrar o fundo)
        ScreenUtils.clear(0, 0, 0, 1);

        // 2. Aplica as regras do Viewport (o esticamento da tela)
        viewport.apply();
        // Diz para o pincel (batch) usar a lente da câmera do Viewport
        batch.setProjectionMatrix(viewport.getCamera().combined);

        // --- LÓGICA DE CLIQUE ---
        // justTouched() só dá "true" no exato frame em que o jogador aperta o botão do mouse/tela
        if (Gdx.input.justTouched()) {

            // Pega a posição "física" do mouse no monitor e converte (unproject) para o mundo virtual do jogo (1920x1080)
            posMouse = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

            // Se o ponto do clique (X, Y) caiu dentro do retângulo do Botão Play...
            if (btnPlay.contains(posMouse.x, posMouse.y)) {
                // ...Pede para o Main fechar o Menu e abrir o Jogo!
                game.setScreen(new TelaSelecaoDeck(game));
            }

            // Se o ponto do clique caiu dentro do retângulo do Botão Heroes...
            if (btnHeroes.contains(posMouse.x, posMouse.y)) {
                // Aqui você pode colocar para ir para uma "HeroesScreen" no futuro!
                System.out.println("Clicou em Heróis!"); // Por enquanto, só avisa no console do computador
            }
        }

        // --- DESENHO DAS IMAGENS ---
        batch.begin(); // "Levanta a caneta e começa a pintar"

        // Se você ativou o fundo lá em cima, ele seria desenhado primeiro para ficar atrás de tudo
        // batch.draw(imgFundo, 0, 0, 1920, 1080);

        // Desenha a imagem do botão Play exatamente nas coordenadas do retângulo btnPlay
        batch.draw(imgPlay, btnPlay.x, btnPlay.y, btnPlay.width, btnPlay.height);

        // Desenha a imagem do botão Heroes exatamente nas coordenadas do retângulo btnHeroes
        batch.draw(imgHeroes, btnHeroes.x, btnHeroes.y, btnHeroes.width, btnHeroes.height);

        batch.end(); // "Guarda a caneta"
    }

    // --- REDIMENSIONAMENTO ---
    // Chamado sempre que o jogador arrasta as bordas da janela do PC para mudar o tamanho
    @Override
    public void resize(int width, int height) {
        // Pede para o Viewport recalcular o esticamento
        viewport.update(width, height, true);
    }

    // --- FAXINA ---
    // Quando o Menu fecha (ex: quando o jogador entra no jogo), jogamos fora as texturas para liberar memória de vídeo
    @Override
    public void dispose() {
        batch.dispose();
        imgPlay.dispose();
        imgHeroes.dispose();
        if (imgFundo != null) imgFundo.dispose();
    }
}
