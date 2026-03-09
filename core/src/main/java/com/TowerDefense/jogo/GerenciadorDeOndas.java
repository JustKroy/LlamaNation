package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class GerenciadorDeOndas {
    private Texture texturaCaramujo, texturaCaramujoVirado;
    private Animation<TextureRegion> animCaramujo, animCaramujoVirado;

    public int waveAtual = 1;
    private int inimigosParaSpawnar = 5;
    private float timerSpawn = 0, intervaloSpawn = 1.8f, timerDescanso = 0;
    public boolean waveEmAndamento = false;

    public GerenciadorDeOndas() {
        texturaCaramujo = new Texture("caramujo.png");
        TextureRegion[][] tmp = TextureRegion.split(texturaCaramujo, texturaCaramujo.getWidth() / 5, texturaCaramujo.getHeight());
        animCaramujo = new Animation<>(0.08f, tmp[0]);
        animCaramujo.setPlayMode(Animation.PlayMode.LOOP);

        texturaCaramujoVirado = new Texture("caramujovirado.png");
        TextureRegion[][] tmpVirado = TextureRegion.split(texturaCaramujoVirado, texturaCaramujoVirado.getWidth() / 5, texturaCaramujoVirado.getHeight());
        animCaramujoVirado = new Animation<>(0.08f, tmpVirado[0]);
        animCaramujoVirado.setPlayMode(Animation.PlayMode.LOOP);
    }

    public void atualizar(float delta, Array<Inimigo> listaInimigos, Mapa mapa) {
        if (waveEmAndamento) {
            timerSpawn += delta;
            if (inimigosParaSpawnar > 0 && timerSpawn >= intervaloSpawn) {
                listaInimigos.add(new Caramujo(mapa.caminho.get(0).x, mapa.caminho.get(0).y, animCaramujo, animCaramujoVirado));
                inimigosParaSpawnar--;
                timerSpawn = 0;
            }
            if (inimigosParaSpawnar <= 0 && listaInimigos.size == 0 && waveAtual < 10) {
                waveEmAndamento = false;
                waveAtual++;
                inimigosParaSpawnar = waveAtual * 5;
            }
        } else {
            timerDescanso += delta;
            if (timerDescanso >= 5.0f) {
                waveEmAndamento = true;
                timerDescanso = 0;
            }
        }
    }

    public void dispose() {
        texturaCaramujo.dispose();
        texturaCaramujoVirado.dispose();
    }
}
