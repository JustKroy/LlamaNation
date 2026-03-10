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

    // Criamos um Array para guardar as 6 posições da loja
    public Rectangle[] slotsLoja = new Rectangle[6];

    public ConstrutorDeTorres() {
        imgLhama = new Texture("lhama.png");
        imgLhamaNinja = new Texture("lhamaninja.png");
        imgCuspe = new Texture("guspe.png");
        imgKunai = new Texture("kunai.png");

        // --- CONFIGURAR A GRELHA DA LOJA (2 Colunas, 3 Linhas) ---
        int inicioX = 1480; // Posição X da primeira coluna
        int inicioY = 750;  // Posição Y da primeira linha (no topo)
        int espacoX = 160;  // Largura do item (120) + Espaçamento (40)
        int espacoY = 160;  // Altura do item (120) + Espaçamento (40)

        for (int i = 0; i < 6; i++) {
            int coluna = i % 2; // Alterna entre 0 e 1 (Colunas)
            int linha = i / 2;  // Vai de 0 a 2 (Linhas)
            slotsLoja[i] = new Rectangle(inicioX + (coluna * espacoX), inicioY - (linha * espacoY), 120, 120);
        }
    }

    public int atualizar(Vector2 posMouse, boolean justTouched, boolean isTouched, int dinheiro, Array<Torre> listaTorres, Mapa mapa, Hud hud) {
        if (justTouched) {
            hud.verificarClique(posMouse.x, posMouse.y);
            torreSelecionada = null;

            for (Torre t : listaTorres) {
                if (t.hitbox.contains(posMouse.x, posMouse.y)) torreSelecionada = t;
            }

            // Verifica se clicou num dos 2 primeiros slots da loja (Lhama Normal e Ninja)
            if (slotsLoja[0].contains(posMouse.x, posMouse.y) && dinheiro >= 50) {
                arrastando = true; texturaArrastando = imgLhama;
            } else if (slotsLoja[1].contains(posMouse.x, posMouse.y) && dinheiro >= 150) {
                arrastando = true; texturaArrastando = imgLhamaNinja;
            }
            // Os slots 2, 3, 4 e 5 estão vazios por enquanto, prontos para novas lhamas!
        }

        float arrastoLargura = 80f;
        float arrastoAltura = 80f;

        // Calcula a largura dinâmica da imagem que está a ser arrastada
        if (texturaArrastando != null) {
            float proporcao = (float) texturaArrastando.getWidth() / texturaArrastando.getHeight();
            arrastoLargura = arrastoAltura * proporcao;
        }

        if (arrastando) {
            posicaoValida = true;
            // Hitbox dinâmica baseada no tamanho do arrasto
            Rectangle hitboxTemp = new Rectangle(posMouse.x - (arrastoLargura / 2), posMouse.y - (arrastoAltura / 2), arrastoLargura, arrastoAltura);

            if (posMouse.x > 1400) posicaoValida = false; // Impede largar em cima da loja

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
                float spawnX = posMouse.x - (arrastoLargura / 2);
                float spawnY = posMouse.y - (arrastoAltura / 2);

                if (texturaArrastando == imgLhamaNinja) {
                    listaTorres.add(new LhamaNinja(spawnX, spawnY, texturaArrastando, tiroCerto));
                    dinheiro -= 150;
                } else {
                    listaTorres.add(new LhamaNormal(spawnX, spawnY, texturaArrastando, tiroCerto));
                    dinheiro -= 50;
                }
            }
            arrastando = false;
            texturaArrastando = null;
        }
        return dinheiro;
    }

    public void desenharLojaEArrasto(SpriteBatch batch, Vector2 posMouse) {
        // Lhama Normal (Slot 0)
        float propLhama = (float) imgLhama.getWidth() / imgLhama.getHeight();
        batch.draw(imgLhama, slotsLoja[0].x, slotsLoja[0].y, slotsLoja[0].height * propLhama, slotsLoja[0].height);

        // Lhama Ninja (Slot 1)
        float propNinja = (float) imgLhamaNinja.getWidth() / imgLhamaNinja.getHeight();
        batch.draw(imgLhamaNinja, slotsLoja[1].x, slotsLoja[1].y, slotsLoja[1].height * propNinja, slotsLoja[1].height);

        // Desenhar a lhama a ser arrastada
        if (arrastando && texturaArrastando != null) {
            batch.setColor(1, posicaoValida ? 1 : 0.3f, posicaoValida ? 1 : 0.3f, 0.7f);
            float propArrastando = (float) texturaArrastando.getWidth() / texturaArrastando.getHeight();
            float arrastoLargura = 80f * propArrastando;

            batch.draw(texturaArrastando, posMouse.x - (arrastoLargura / 2), posMouse.y - 40, arrastoLargura, 80);
            batch.setColor(1, 1, 1, 1);
        }
    }

    public void desenharHitboxes(ShapeRenderer shape, Vector2 posMouse) {
        // Desenha fundos escuros transparentes para mostrar os 6 espaços vazios na loja
        shape.setColor(0.2f, 0.2f, 0.2f, 0.5f); // Cinzento escuro transparente
        for (int i = 0; i < 6; i++) {
            shape.rect(slotsLoja[i].x, slotsLoja[i].y, slotsLoja[i].width, slotsLoja[i].height);
        }

        if (torreSelecionada != null) {
            shape.setColor(1, 1, 1, 0.15f);
            shape.circle(torreSelecionada.posicao.x + (torreSelecionada.larguraDesenho / 2), torreSelecionada.posicao.y + (torreSelecionada.alturaDesenho / 2), torreSelecionada.raio);
        }

        if (arrastando) {
            shape.setColor(posicaoValida ? 1 : 1, posicaoValida ? 1 : 0, 0, 0.2f);
            float raioP = (texturaArrastando == imgLhamaNinja) ? 250f : 200f;
            shape.circle(posMouse.x, posMouse.y, raioP);
        }
    }

    public void dispose() {
        imgLhama.dispose();
        imgLhamaNinja.dispose();
        imgCuspe.dispose();
        imgKunai.dispose();
    }
}
