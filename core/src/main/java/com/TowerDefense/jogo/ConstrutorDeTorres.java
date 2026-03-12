package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class ConstrutorDeTorres {
    private Texture imgLhama, imgLhamaNinja, imgCuspe, imgKunai;
    private Texture imgLhamaMage, imgMagia;
    private Texture imgLlamaCyborg, imgCyborgAttack;

    private TextureRegion iconeLhama, iconeLhamaNinja, iconeLhamaMage, iconeLlamaCyborg;

    private BitmapFont font;
    private Texture imgMoedaPequena;
    private Texture texFundoMenu, texBotaoVender, texFundoLoja;
    private Rectangle areaMenuEsquerdo;
    public Rectangle btnVender;
    private GlyphLayout layout;

    private boolean posicionando = false;
    private boolean posicaoValida = false;
    private TextureRegion texturaPosicionando = null;

    public Torre torreSelecionada = null;
    public Rectangle[] slotsLoja = new Rectangle[6];

    public ConstrutorDeTorres() {
        // Carregamento de Texturas
        imgLhama = new Texture("llama.png");
        imgLhamaNinja = new Texture("lhamaninja.png");
        imgCuspe = new Texture("guspe.png");
        imgKunai = new Texture("kunai.png");

        imgLhamaMage = new Texture("llamamage.png");
        imgMagia = new Texture("mageattack.png");

        imgLlamaCyborg = new Texture("LlamaCyborg.png");
        imgCyborgAttack = new Texture("CyborgAttack.png");

        // Criação dos Ícones com Recorte de Sprite Sheet
        int largLhama = imgLhama.getWidth() / 9;
        iconeLhama = new TextureRegion(imgLhama, 0, 0, largLhama, imgLhama.getHeight());

        iconeLhamaNinja = new TextureRegion(imgLhamaNinja);

        int largMage = imgLhamaMage.getWidth() / 11;
        iconeLhamaMage = new TextureRegion(imgLhamaMage, 0, 0, largMage, imgLhamaMage.getHeight());

        int largCyborg = imgLlamaCyborg.getWidth() / 18;
        iconeLlamaCyborg = new TextureRegion(imgLlamaCyborg, 0, 0, largCyborg, imgLlamaCyborg.getHeight());

        // UI e Fontes
        font = new BitmapFont(Gdx.files.internal("fontes.fnt"));
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        font.getData().setScale(0.23f);
        layout = new GlyphLayout();
        imgMoedaPequena = new Texture("moeda.png");
        texFundoLoja = new Texture("Loja.png");

        // Botões e Menus
        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(new Color(0.1f, 0.1f, 0.1f, 0.8f)); pix.fill();
        texFundoMenu = new Texture(pix);
        pix.setColor(new Color(0.8f, 0.2f, 0.2f, 1f)); pix.fill();
        texBotaoVender = new Texture(pix);
        pix.dispose();

        areaMenuEsquerdo = new Rectangle(20, 300, 260, 350);
        btnVender = new Rectangle(50, 320, 200, 50);

        // Configuração de Posição e Espaçamento dos Slots
        for (int i = 0; i < 6; i++) {
            int coluna = i % 2;
            int linha = i / 2;

            float x = 1480 + (coluna * 220);
            float y = 680 - (linha * 220);

            slotsLoja[i] = new Rectangle(x, y, 120, 120);
        }
    }

    public int atualizar(Vector2 posMouse, boolean justTouched, boolean isTouched, int dinheiro, Array<Torre> listaTorres, Mapa mapa, Hud hud) {
        if (justTouched) hud.verificarClique(posMouse.x, posMouse.y);
        if (hud.pausado) return dinheiro;

        float arrastoAlt = 80f;
        float arrastoLarg = (texturaPosicionando != null) ? arrastoAlt * ((float)texturaPosicionando.getRegionWidth()/texturaPosicionando.getRegionHeight()) : 80f;

        if (posicionando) {
            posicaoValida = (posMouse.x <= 1400);
            Rectangle hbTemp = new Rectangle(posMouse.x - (arrastoLarg/2), posMouse.y - (arrastoAlt/2), arrastoLarg, arrastoAlt);
            for (Rectangle r : mapa.hitboxesCaminho) if (r.overlaps(hbTemp)) posicaoValida = false;
            for (Torre t : listaTorres) if (t.hitbox.overlaps(hbTemp)) posicaoValida = false;
        }

        if (justTouched) {
            if (posicionando && texturaPosicionando != null) {
                if (posicaoValida) {
                    float sx = posMouse.x - (arrastoLarg/2), sy = posMouse.y - (arrastoAlt/2);
                    if (texturaPosicionando == iconeLhamaMage) { listaTorres.add(new LhamaMage(sx, sy, imgLhamaMage, imgMagia)); dinheiro -= 250; }
                    else if (texturaPosicionando == iconeLlamaCyborg) { listaTorres.add(new LlamaCyborg(sx, sy, imgLlamaCyborg, imgCyborgAttack)); dinheiro -= 300; }
                    else if (texturaPosicionando == iconeLhamaNinja) { listaTorres.add(new LhamaNinja(sx, sy, imgLhamaNinja, imgKunai)); dinheiro -= 150; }
                    else { listaTorres.add(new LhamaNormal(sx, sy, imgLhama, imgCuspe)); dinheiro -= 50; }
                }
                posicionando = false; texturaPosicionando = null;
            }
            else if (torreSelecionada != null && btnVender.contains(posMouse.x, posMouse.y)) {
                int preco = (torreSelecionada instanceof LlamaCyborg) ? 300 : (torreSelecionada instanceof LhamaMage ? 250 : (torreSelecionada instanceof LhamaNinja ? 150 : 50));
                dinheiro += (int)(preco * 0.6f);
                listaTorres.removeValue(torreSelecionada, true);
                torreSelecionada = null;
            }
            else {
                Torre achou = null;
                for (Torre t : listaTorres) if (t.hitbox.contains(posMouse.x, posMouse.y)) achou = t;
                if (achou != null) torreSelecionada = achou;
                else if (!areaMenuEsquerdo.contains(posMouse.x, posMouse.y)) torreSelecionada = null;
            }

            if (slotsLoja[0].contains(posMouse.x, posMouse.y) && dinheiro >= 50) { posicionando = true; texturaPosicionando = iconeLhama; }
            else if (slotsLoja[1].contains(posMouse.x, posMouse.y) && dinheiro >= 150) { posicionando = true; texturaPosicionando = iconeLhamaNinja; }
            else if (slotsLoja[2].contains(posMouse.x, posMouse.y) && dinheiro >= 250) { posicionando = true; texturaPosicionando = iconeLhamaMage; }
            else if (slotsLoja[3].contains(posMouse.x, posMouse.y) && dinheiro >= 300) { posicionando = true; texturaPosicionando = iconeLlamaCyborg; }
        }
        return dinheiro;
    }

    public void desenharLojaEArrasto(SpriteBatch batch, Vector2 posMouse, Hud hud) {
        batch.draw(texFundoLoja, 1400, 0, 520, 1080);
        font.getData().setScale(0.55f);

        desenharItemLoja(batch, iconeLhama, "50", slotsLoja[0]);
        desenharItemLoja(batch, iconeLhamaNinja, "150", slotsLoja[1]);
        desenharItemLoja(batch, iconeLhamaMage, "250", slotsLoja[2]);
        desenharItemLoja(batch, iconeLlamaCyborg, "300", slotsLoja[3]);

        // PAINEL LATERAL (TORRE SELECIONADA)
        if (torreSelecionada != null) {
            batch.draw(texFundoMenu, areaMenuEsquerdo.x, areaMenuEsquerdo.y, areaMenuEsquerdo.width, areaMenuEsquerdo.height);

            TextureRegion renderMenu = iconeLhama;
            String nome = "Llama";
            int precoCheio = 50;

            if (torreSelecionada instanceof LlamaCyborg) {
                renderMenu = iconeLlamaCyborg;
                nome = "Cyborg Llama";
                precoCheio = 300;
            } else if (torreSelecionada instanceof LhamaMage) {
                renderMenu = iconeLhamaMage;
                nome = "Mage Llama";
                precoCheio = 250;
            } else if (torreSelecionada instanceof LhamaNinja) {
                renderMenu = iconeLhamaNinja;
                nome = "Ninja Llama";
                precoCheio = 150;
            }

            float propTorre = (float) renderMenu.getRegionWidth() / renderMenu.getRegionHeight();
            batch.draw(renderMenu, 100, 520, 100 * propTorre, 100);

            font.getData().setScale(0.7f);
            font.draw(batch, nome, 40, 490);

            // --- AQUI ESTÃO AS INFORMAÇÕES NOVAS ---
            font.getData().setScale(0.40f);

            // Dano (convertido para int para ficar mais limpo)
            font.draw(batch, "Dano: " + (int)torreSelecionada.dano, 40, 450);

            // Range (raio de alcance)
            font.draw(batch, "Range: " + (int)torreSelecionada.raio, 40, 420);

            // SPA (Seconds Per Attack = cooldown)
            // Formatado para mostrar algo como "0.5s" em vez de um número gigante
            font.draw(batch, "SPA: " + String.format("%.1f", torreSelecionada.cooldown) + "s", 40, 390);
            // ---------------------------------------

            int valorVenda = (int)(precoCheio * 0.6f);
            batch.draw(texBotaoVender, btnVender.x, btnVender.y, btnVender.width, btnVender.height);
            font.draw(batch, "SELL:", btnVender.x + 25, btnVender.y + 35);
            batch.draw(imgMoedaPequena, btnVender.x + 95, btnVender.y + 12, 25, 25);
            font.draw(batch, String.valueOf(valorVenda), btnVender.x + 125, btnVender.y + 35);
        }

        // ARRASTO
        if (posicionando && texturaPosicionando != null) {
            batch.setColor(1, posicaoValida ? 1 : 0.3f, posicaoValida ? 1 : 0.3f, 0.7f);
            float arrL = 80f * ((float)texturaPosicionando.getRegionWidth()/texturaPosicionando.getRegionHeight());
            batch.draw(texturaPosicionando, posMouse.x - (arrL/2), posMouse.y - 40, arrL, 80);
            batch.setColor(1, 1, 1, 1);
        }
    }

    private void desenharItemLoja(SpriteBatch batch, TextureRegion tex, String preco, Rectangle slot) {
        float larg = slot.height * ((float)tex.getRegionWidth()/tex.getRegionHeight());
        float xCentralizado = slot.x + (slot.width - larg) / 2f;

        batch.draw(tex, xCentralizado, slot.y, larg, slot.height);

        layout.setText(font, preco);
        batch.draw(imgMoedaPequena, slot.x + 15, slot.y - 35, 25, 25);
        font.draw(batch, preco, slot.x + 45, slot.y - 12);
    }

    public void desenharHitboxes(ShapeRenderer shape, Vector2 posMouse, Hud hud) {
        if (torreSelecionada != null) {
            shape.setColor(1, 1, 1, 0.15f);
            shape.circle(torreSelecionada.posicao.x + (torreSelecionada.larguraDesenho/2), torreSelecionada.posicao.y + (torreSelecionada.alturaDesenho/2), torreSelecionada.raio);
        }

        if (posicionando && !hud.pausado) {
            if (posicaoValida) {
                shape.setColor(1f, 1f, 1f, 0.2f);
            } else {
                shape.setColor(1f, 0f, 0f, 0.3f);
            }

            float r = (texturaPosicionando == iconeLlamaCyborg) ? 300f : (texturaPosicionando == iconeLhamaMage ? 250f : 200f);
            shape.circle(posMouse.x, posMouse.y, r);
        }
    }

    public void dispose() {
        imgLhama.dispose(); imgLhamaNinja.dispose(); imgLhamaMage.dispose(); imgLlamaCyborg.dispose();
        imgCuspe.dispose(); imgKunai.dispose(); imgMagia.dispose(); imgCyborgAttack.dispose();
    }
}
