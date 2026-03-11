package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

// O GerenciadorDeOndas controla o "ritmo" da partida.
// Ele cria os inimigos aos poucos, controla as waves (ondas) e os tempos de descanso.
public class GerenciadorDeOndas {

    // --- IMAGENS E ANIMAÇÕES ---
    private Texture texturaCaramujo, texturaCaramujoVirado; // As folhas de sprites (spritesheets) inteiras
    private Animation<TextureRegion> animCaramujo, animCaramujoVirado; // As animações já cortadas e prontas para rodar

    // --- CONTROLES DA WAVE ---
    public int waveAtual = 1; // Em qual onda o jogador está agora
    private int inimigosParaSpawnar = 5; // Quantos inimigos faltam nascer NESTA onda

    // --- CRONÔMETROS (Timers) ---
    private float timerSpawn = 0; // Cronômetro para o intervalo entre o nascimento de um inimigo e outro
    private float intervaloSpawn = 1.8f; // Nasce um inimigo a cada 1.8 segundos
    private float timerDescanso = 0; // Cronômetro para o tempo de paz entre uma wave e outra

    // --- ESTADO DO JOGO ---
    public boolean waveEmAndamento = false; // Começa como false para dar um tempinho antes da Wave 1 começar

    // --- CONSTRUTOR ---
    // Chamado uma vez quando o jogo começa. Ele prepara as animações dos inimigos.
    public GerenciadorDeOndas() {

        // 1. CARREGANDO A ANIMAÇÃO DO CARAMUJO INDO PARA A DIREITA
        texturaCaramujo = new Texture("caramujo.png"); // Carrega a imagem comprida com todos os quadros
        // O "split" corta a imagem em pedacinhos.
        // Como o caramujo tem 5 quadros de animação, dividimos a largura por 5.
        TextureRegion[][] tmp = TextureRegion.split(texturaCaramujo, texturaCaramujo.getWidth() / 5, texturaCaramujo.getHeight());
        // Cria a animação pegando a primeira linha da imagem cortada (tmp[0]).
        // Cada quadro dura 0.08 segundos na tela.
        animCaramujo = new Animation<>(0.08f, tmp[0]);
        animCaramujo.setPlayMode(Animation.PlayMode.LOOP); // Faz a animação se repetir infinitamente

        // 2. CARREGANDO A ANIMAÇÃO DO CARAMUJO INDO PARA A ESQUERDA (VIRADO)
        texturaCaramujoVirado = new Texture("caramujovirado.png");
        TextureRegion[][] tmpVirado = TextureRegion.split(texturaCaramujoVirado, texturaCaramujoVirado.getWidth() / 5, texturaCaramujoVirado.getHeight());
        animCaramujoVirado = new Animation<>(0.08f, tmpVirado[0]);
        animCaramujoVirado.setPlayMode(Animation.PlayMode.LOOP);
    }

    // --- ATUALIZAÇÃO DO TEMPO ---
    // Roda o tempo todo na GameScreen para checar os cronômetros
    public void atualizar(float delta, Array<Inimigo> listaInimigos, Mapa mapa) {

        // SE A ONDA ESTIVER ACONTECENDO...
        if (waveEmAndamento) {
            timerSpawn += delta; // O relógio do spawn avança

            // Se ainda tem inimigos na fila de espera E já deu o tempo de 1.8 segundos...
            if (inimigosParaSpawnar > 0 && timerSpawn >= intervaloSpawn) {
                // Cria um novo Caramujo lá no primeiro ponto do caminho (índice 0 do mapa)
                // e já entrega as duas animações para ele se virar sozinho.
                listaInimigos.add(new Caramujo(mapa.caminho.get(0).x, mapa.caminho.get(0).y, animCaramujo, animCaramujoVirado));

                inimigosParaSpawnar--; // Desconta um da fila de espera
                timerSpawn = 0; // Zera o cronômetro para o próximo caramujo nascer
            }

            // VERIFICAÇÃO DE FIM DE WAVE:
            // Se não tem mais inimigo pra nascer E a lista de inimigos no mapa está vazia (todos morreram ou passaram)
            // E a wave atual é menor que 10 (Limite do jogo, se quiser infinito é só tirar isso)
            if (inimigosParaSpawnar <= 0 && listaInimigos.size == 0 && waveAtual < 10) {
                waveEmAndamento = false; // A wave acaba, entra no modo "Descanso"
                waveAtual++; // Avança para a próxima wave

                // Aumenta a dificuldade! A próxima wave terá a (Wave * 5) inimigos.
                // Ex: Wave 2 = 10 inimigos, Wave 3 = 15 inimigos...
                inimigosParaSpawnar = waveAtual * 5;
            }

        }
        // SE A ONDA ACABOU (INTERVALO DE DESCANSO)...
        else {
            timerDescanso += delta; // O relógio do descanso avança

            // O jogador tem 5 segundos de paz para comprar/melhorar torres
            if (timerDescanso >= 5.0f) {
                waveEmAndamento = true; // Acabou a moleza, a nova wave começa!
                timerDescanso = 0; // Zera o relógio do descanso para a próxima vez
            }
        }
    }

    // --- FAXINA ---
    // Joga as imagens inteiras fora quando o jogo é fechado para não travar a memória RAM
    public void dispose() {
        texturaCaramujo.dispose();
        texturaCaramujoVirado.dispose();
    }
}
