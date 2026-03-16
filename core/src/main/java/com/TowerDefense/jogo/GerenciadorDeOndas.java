package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils; // Importante para o sistema de aleatoriedade
import com.badlogic.gdx.utils.Array;

public class GerenciadorDeOndas {

    // --- IMAGENS E ANIMAÇÕES ---
    private Texture texturaCaramujo, texturaCaramujoVirado;
    private Animation<TextureRegion> animCaramujo, animCaramujoVirado;
    private Texture texturaTanque;
    private Animation<TextureRegion> animTanque, animTanqueVirado;

    // --- CONTROLES DA WAVE ---
    public int waveAtual = 1;
    private int inimigosParaSpawnar = 0;

    // A nossa "Fila de Espera" (0 = Normal, 1 = Tanque)
    private Array<Integer> filaDeSpawn = new Array<>();

    // --- CRONÔMETROS (Timers) ---
    private float timerSpawn = 0;
    private float intervaloAtual = 1.8f; // Agora isso vai mudar a cada inimigo que nasce!
    private float timerDescanso = 0;

    public boolean waveEmAndamento = false;

    public GerenciadorDeOndas() {

        // 1. ANIMAÇÕES DO CARAMUJO NORMAL
        texturaCaramujo = new Texture("caramujo.png");
        TextureRegion[][] tmp = TextureRegion.split(texturaCaramujo, texturaCaramujo.getWidth() / 5, texturaCaramujo.getHeight());
        animCaramujo = new Animation<>(0.08f, tmp[0]);
        animCaramujo.setPlayMode(Animation.PlayMode.LOOP);

        texturaCaramujoVirado = new Texture("caramujovirado.png");
        TextureRegion[][] tmpVirado = TextureRegion.split(texturaCaramujoVirado, texturaCaramujoVirado.getWidth() / 5, texturaCaramujoVirado.getHeight());
        animCaramujoVirado = new Animation<>(0.08f, tmpVirado[0]);
        animCaramujoVirado.setPlayMode(Animation.PlayMode.LOOP);

        // 2. ANIMAÇÕES DO CARAMUJO TANQUE
        texturaTanque = new Texture("caramujotanque.png");
        TextureRegion[][] tmpTanque = TextureRegion.split(texturaTanque, texturaTanque.getWidth() / 5, texturaTanque.getHeight());

        TextureRegion[] framesTanque = tmpTanque[0];
        animTanque = new Animation<>(0.12f, framesTanque);
        animTanque.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion[] framesTanqueVirado = new TextureRegion[5];
        for (int i = 0; i < 5; i++) {
            framesTanqueVirado[i] = new TextureRegion(framesTanque[i]);
            framesTanqueVirado[i].flip(true, false);
        }
        animTanqueVirado = new Animation<>(0.12f, framesTanqueVirado);
        animTanqueVirado.setPlayMode(Animation.PlayMode.LOOP);

        // Prepara a PRIMEIRA wave assim que o jogo abre
        prepararProximaWave();
    }

    public void atualizar(float delta, Array<Inimigo> listaInimigos, Mapa mapa) {

        if (waveEmAndamento) {
            timerSpawn += delta;

            // Se ainda tem inimigos na fila e deu o tempo dinâmico atual
            if (filaDeSpawn.size > 0 && timerSpawn >= intervaloAtual) {

                // Puxa o próximo inimigo do baralho (tira o último da lista)
                int tipoInimigo = filaDeSpawn.pop();

                if (tipoInimigo == 1) {
                    listaInimigos.add(new CaramujoTanque(mapa.caminho.get(0).x, mapa.caminho.get(0).y, animTanque, animTanqueVirado));
                } else {
                    listaInimigos.add(new Caramujo(mapa.caminho.get(0).x, mapa.caminho.get(0).y, animCaramujo, animCaramujoVirado));
                }

                inimigosParaSpawnar--;
                timerSpawn = 0;

                // --- A MÁGICA DA IMPREVISIBILIDADE AQUI ---
                // O próximo inimigo pode nascer bem rápido (0.6s) ou demorar um pouco mais (2.2s)
                intervaloAtual = MathUtils.random(0.6f, 2.2f);
            }

            // VERIFICAÇÃO DE FIM DE WAVE
            if (filaDeSpawn.size == 0 && listaInimigos.size == 0) {
                waveEmAndamento = false;
                waveAtual++;

                // Só prepara inimigos se não tiver passado do limite do jogo (Wave 10)
                if(waveAtual <= 10) {
                    prepararProximaWave();
                }
            }
        } else {
            // INTERVALO DE DESCANSO
            timerDescanso += delta;

            if (timerDescanso >= 5.0f && waveAtual <= 10) {
                waveEmAndamento = true;
                timerDescanso = 0;
            }
        }
    }

    // --- MÉTODO PARA MONTAR E EMBARALHAR A WAVE ---
    private void prepararProximaWave() {
        filaDeSpawn.clear(); // Limpa o baralho da wave passada

        int totalInimigosNaWave = waveAtual * 5;
        // Adiciona um pouquinho de caos na quantidade total (ex: pode vir 1 a menos ou 2 a mais)
        totalInimigosNaWave += MathUtils.random(-1, 2);

        int tanques = 0;

        if (waveAtual < 3) {
            tanques = 0; // Sem tanques no início
        } else if (waveAtual >= 10) {
            tanques = totalInimigosNaWave; // SÓ TANQUES NA WAVE 10
        } else {
            // Inicia com 1 tanque na wave 3, e vai subindo, com chance de +1 ou +2 extras de surpresa
            tanques = (waveAtual - 2) + MathUtils.random(0, 2);
            // Garante que o número de tanques não ultrapasse o total de inimigos
            if (tanques > totalInimigosNaWave) tanques = totalInimigosNaWave;
        }

        int normais = totalInimigosNaWave - tanques;

        // Coloca todas as "cartas" no baralho
        for (int i = 0; i < normais; i++) filaDeSpawn.add(0);
        for (int i = 0; i < tanques; i++) filaDeSpawn.add(1);

        // EMBARALHA TUDO! Assim você nunca sabe quem é o próximo a sair.
        filaDeSpawn.shuffle();

        inimigosParaSpawnar = filaDeSpawn.size;
        intervaloAtual = 1.5f; // Tempo inicial padrão para o primeiro nascer
    }

    public void dispose() {
        texturaCaramujo.dispose();
        texturaCaramujoVirado.dispose();
        texturaTanque.dispose();
    }
}
