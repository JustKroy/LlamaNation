package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class ConstrutorDeTorres {
    private Texture imgLhama, imgLhamaNinja, imgCuspe, imgKunai;
    private boolean arrastando = false;
    private boolean posicaoValida = false;
    private Texture texturaArrastando = null;
    public Torre torreSelecionada = null;

    public ConstrutorDeTorres() {
        imgLhama = new Texture("lhama.png");
        imgLhamaNinja = new Texture("lhamaninja.png");
        imgCuspe = new Texture("guspe.png");
        imgKunai = new Texture("kunai.png");
    }

    // Retorna o dinheiro atualizado para a GameScreen
    public int atualizar(Vector2 posMouse, boolean justTouched, boolean isTouched, int dinheiro, Array<Torre> listaTorres, Mapa mapa, Hud hud) {
        if (justTouched) {
            hud.verificarClique(posMouse.x, posMouse.y);
            torreSelecionada = null;
            for (Torre t : listaTorres) {
                if (posMouse.dst(t.posicao.x + 40, t.posicao.y + 40) < 60) torreSelecionada = t;
            }
            // Verifica loja
            if (posMouse.x >= 1600 && posMouse.x <= 1720) {
                if (posMouse.y >= 850 && posMouse.y <= 970 && dinheiro >= 50) { arrastando = true; texturaArrastando = imgLhama; }
                else if (posMouse.y >= 650 && posMouse.y <= 770 && dinheiro >= 150) { arrastando = true; texturaArrastando = imgLhamaNinja; }
            }
        }

        if (arrastando) {
            posicaoValida = true;
            Rectangle hitboxTemp = new Rectangle(posMouse.x - 40, posMouse.y - 40, 80, 80);

            if (posMouse.x > 1350) posicaoValida = false;

            for (Rectangle rectCaminho : mapa.hitboxesCaminho) {
                if (rectCaminho.overlaps(hitboxTemp)) posicaoValida = false;
            }
            for (Torre t : listaTorres) {
                if (t.hitbox.overlaps(hitboxTemp)) posicaoValida = false;
            }
        }

        if (arrastando && !isTouched) {
            if (posicaoValida) {
                Texture tiroCerto = (texturaArrastando == imgLhamaNinja) ? imgKunai : imgCuspe;
                if (texturaArrastando == imgLhamaNinja) {
                    listaTorres.add(new LhamaNinja(posMouse.x - 40, posMouse.y - 40, texturaArrastando, tiroCerto));
                    dinheiro -= 150;
                } else {
                    listaTorres.add(new LhamaNormal(posMouse.x - 40, posMouse.y - 40, texturaArrastando, tiroCerto));
                    dinheiro -= 50;
                }
            }
            arrastando = false;
            texturaArrastando = null;
        }
        return dinheiro;
    }

    public void desenharLojaEArrasto(SpriteBatch batch, Vector2 posMouse) {
        float propLhama = (float) imgLhama.getWidth() / imgLhama.getHeight();
        batch.draw(imgLhama, 1600, 850, 120 * propLhama, 120);

        float propNinja = (float) imgLhamaNinja.getWidth() / imgLhamaNinja.getHeight();
        batch.draw(imgLhamaNinja, 1600, 650, 120 * propNinja, 120);

        if (arrastando && texturaArrastando != null) {
            batch.setColor(1, posicaoValida ? 1 : 0.3f, posicaoValida ? 1 : 0.3f, 0.7f);
            float propArrastando = (float) texturaArrastando.getWidth() / texturaArrastando.getHeight();
            batch.draw(texturaArrastando, posMouse.x - 40, posMouse.y - 40, 80 * propArrastando, 80);
            batch.setColor(1, 1, 1, 1);
        }
    }

    public void desenharHitboxes(ShapeRenderer shape, Vector2 posMouse) {
        if (torreSelecionada != null) {
            shape.setColor(1, 1, 1, 0.15f);
            shape.circle(torreSelecionada.posicao.x + 40, torreSelecionada.posicao.y + 40, torreSelecionada.raio);
        }
        if (arrastando) {
            shape.setColor(posicaoValida ? 1 : 1, posicaoValida ? 1 : 0, 0, 0.2f);
            float raioP = (texturaArrastando == imgLhamaNinja) ? 250f : 200f;
            shape.circle(posMouse.x, posMouse.y, raioP);
        }
    }

    public void dispose() {
        imgLhama.dispose(); imgLhamaNinja.dispose();
        imgCuspe.dispose(); imgKunai.dispose();
    }
}
