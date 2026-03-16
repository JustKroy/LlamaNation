package com.TowerDefense.jogo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

// ScreenAdapter é a classe do LibGDX que diz que esta é uma "Tela" do jogo.
public class GameScreen extends ScreenAdapter {

    // --- VARIÁVEIS DO SISTEMA ---
    private final Main game; // Referência ao jogo principal (usado para trocar de telas)
    private SpriteBatch batch; // O "pincel" que desenha imagens (texturas) na tela
    private ShapeRenderer shape; // O "lápis" que desenha formas geométricas (linhas, círculos)
    private OrthographicCamera camera; // A câmera que define o que estamos olhando no mundo do jogo
    private StretchViewport viewport; // Mantém a proporção da tela (1920x1080) não importa o tamanho do monitor

    // --- CLASSES AUXILIARES E GERENCIADORES ---
    private Mapa mapa; // Desenha o fundo e guarda o caminho (hitboxes) dos inimigos
    private Hud hud; // Desenha a interface do usuário (vidas, dinheiro, botões)
    private GerenciadorDeOndas ondas; // Controla o tempo e o surgimento (spawn) de novos inimigos
    private ConstrutorDeTorres construtor; // Controla a loja, a compra e o arrasto de novas torres

    // --- LISTAS DE ENTIDADES ---
    // Arrays do LibGDX são otimizados para jogos, melhores que o ArrayList do Java normal
    private Array<Inimigo> listaInimigos = new Array<>();
    private Array<Torre> listaTorres = new Array<>();
    private Array<Projetil> listaProjeteis = new Array<>();

    // --- STATUS DO JOGADOR ---
    private int vidas = 100, dinheiro = 100;
    private Vector2 posMouse = new Vector2(); // Guarda as coordenadas X e Y do mouse

    // --- CONSTRUTOR ---
    // Chamado uma única vez quando esta tela é aberta
    public GameScreen(Main game) {
        this.game = game;

        // Inicializando as ferramentas de desenho e câmera
        this.batch = new SpriteBatch();
        this.shape = new ShapeRenderer();
        this.camera = new OrthographicCamera();
        this.viewport = new StretchViewport(1920, 1080, camera);

        // Inicializando nossos objetos e gerenciadores
        this.mapa = new Mapa();
        this.hud = new Hud();
        this.ondas = new GerenciadorDeOndas();
        this.construtor = new ConstrutorDeTorres();
    }

    // --- MÉTODO RENDER ---
    // Este é o coração do jogo! Roda dezenas de vezes por segundo (ex: 60 FPS)
    // O 'delta' é o tempo que passou desde o último frame (usado para movimento suave)
    @Override
    public void render(float delta) {

        // Limpa a tela inteira com a cor preta (Red=0, Green=0, Blue=0, Alpha=1)
        ScreenUtils.clear(0, 0, 0, 1);

        // Atualiza a câmera
        camera.update();

        // Pega a posição real do mouse na tela do jogador e converte para as coordenadas do jogo (1920x1080)
        posMouse = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

        // --- 1. ATUALIZAÇÃO SEMPRE ATIVA (HUD E LOJA) ---
        // A loja e o hud precisam rodar mesmo com o jogo pausado para os botões funcionarem
        dinheiro = construtor.atualizar(posMouse, Gdx.input.justTouched(), Gdx.input.isTouched(), dinheiro, listaTorres, mapa, hud);

        // ==========================================
        // 2. A MAGIA DO PAUSE (O FREEZE)
        // ==========================================
        if (!hud.pausado) {

            // --- AQUI ESTÁ A MÁGICA DA VELOCIDADE 3x ---
            // Se o botão de acelerar estiver ativado na loja, nós multiplicamos o tempo (delta) por 3!
            if (construtor.jogoAcelerado) {
                delta = delta * 3f;
            }

            // Faz o jogo "pensar" antes de desenhar
            ondas.atualizar(delta, listaInimigos, mapa); // Spawna inimigos se for a hora

            // Atualiza os inimigos: anda com eles pelo caminho do mapa
            for (int i = 0; i < listaInimigos.size; i++) {
                // Se o inimigo chegou ao final do caminho (atualizar() retorna true)
                if (listaInimigos.get(i).atualizar(delta, mapa.caminho)) {
                    vidas--; // O jogador perde 1 vida
                    listaInimigos.removeIndex(i); // Remove o inimigo da lista
                }
            }

            // Atualiza as torres: faz elas procurarem alvos e atirarem novos projéteis
            for (Torre t : listaTorres) t.atualizar(delta, listaInimigos, listaProjeteis);

            // Atualiza os projéteis (tiros)
            // O loop é feito de trás para frente (size - 1 até 0) porque vamos remover itens da lista
            for (int i = listaProjeteis.size - 1; i >= 0; i--) {
                Projetil p = listaProjeteis.get(i);
                p.atualizar(delta); // Move o tiro pra frente

                // Verifica se o tiro bateu em alguém
                Inimigo inimigoAtingido = p.checarColisao(listaInimigos);

                // Se atingiu um inimigo e a vida dele zerou
                if (inimigoAtingido != null && inimigoAtingido.vida <= 0) {
                    listaInimigos.removeValue(inimigoAtingido, true); // Mata o inimigo
                    dinheiro += inimigoAtingido.recompensaMoedas; // Dá o dinheiro da recompensa
                }

                // Se o projétil bateu ou saiu da tela (ativo = false), destrói ele
                if (!p.ativo) listaProjeteis.removeIndex(i);
            }
        } // FIM DO BLOCO IF(!HUD.PAUSADO)
        // ==========================================

        // --- 3. DESENHO NA TELA ---

        // Primeiro, desenha o mapa de fundo (não usa transparência)
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        mapa.desenharMapa(batch);
        batch.end();

        // Configurações para desenhar formas geométricas com transparência (ex: sombras da loja)
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shape.setProjectionMatrix(camera.combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);

        // Desenha a sombra preta da loja e as áreas fantasmas onde a torre pode ou não ser colocada
        mapa.desenharFundo(shape);
        construtor.desenharHitboxes(shape, posMouse, hud);

        shape.end();
        Gdx.gl.glDisable(GL20.GL_BLEND); // Desliga a transparência para não bugar o resto

        // Agora desenha todas as imagens soltas por cima do mapa
        batch.begin();
        for (Inimigo in : listaInimigos) in.desenhar(batch);
        for (Torre t : listaTorres) t.desenhar(batch);
        for (Projetil p : listaProjeteis) p.desenhar(batch);

        // Desenha as imagens da loja e as texturas sendo arrastadas pelo mouse
        construtor.desenharLojaEArrasto(batch, posMouse, hud);
        // Desenha os textos, corações e moedas por cima de tudo
        hud.desenhar(batch, vidas, dinheiro, ondas.waveAtual);
        batch.end();

        // --- 4. DEPURADOR E GAME OVER ---

        // Se o botão de Hitboxes foi ativado no HUD, desenha as linhas de colisão
        if (hud.mostrarHitbox) renderizarDebugHitbox();

        // Se o jogador clicou no botão "Voltar" na tela de pause
        if (hud.voltarAoMenu) {
            this.dispose(); // Destrói a tela atual para liberar memória
            game.setScreen(new MenuScreen(game)); // Volta pro menu
        }

        // Se as vidas zeraram, fecha esta tela e abre o Menu
        if (vidas <= 0) {
            this.dispose();
            game.setScreen(new MenuScreen(game));
        }
    }

    // --- MÉTODOS AUXILIARES E DE LIMPEZA ---

    // Método que desenha os contornos exatos de colisão (hitboxes) para ajudar a encontrar bugs
    private void renderizarDebugHitbox() {
        shape.begin(ShapeRenderer.ShapeType.Line);

        // Desenha os retângulos do caminho do mapa em Vermelho
        shape.setColor(1, 0, 0, 1);
        for (Rectangle r : mapa.hitboxesCaminho) shape.rect(r.x, r.y, r.width, r.height);

        // Desenha os retângulos das torres em Azul
        shape.setColor(0, 0, 1, 1);
        for (Torre t : listaTorres) shape.rect(t.hitbox.x, t.hitbox.y, t.hitbox.width, t.hitbox.height);

        // Desenha os quadrados dos inimigos em Verde
        for (Inimigo in : listaInimigos) {
            shape.setColor(0, 1, 0, 1);
            shape.rect(in.posicao.x, in.posicao.y, 50, 50);
        }

        // Desenha os retângulos dos projéteis em Amarelo
        for (Projetil p : listaProjeteis) {
            shape.setColor(1, 1, 0, 1);
            shape.rect(p.hitbox.x, p.hitbox.y, p.hitbox.width, p.hitbox.height);
        }
        shape.end();
    }

    // Chamado sempre que o jogador redimensiona a janela do jogo no PC
    @Override
    public void resize(int w, int h) {
        viewport.update(w, h, true);
    }

    // Chamado quando a tela é fechada/destruída. Usado para liberar memória (RAM/VRAM)
    @Override
    public void dispose() {
        batch.dispose();
        shape.dispose();
        mapa.dispose();
        hud.dispose();
        ondas.dispose();
        construtor.dispose();
    }
}
