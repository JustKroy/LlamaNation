package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class ConstrutorDeTorres {
    private Texture imgLhama, imgLhamaNinja, imgCuspe, imgKunai;
    private BitmapFont font;
    private Texture imgMoedaPequena;

    private Texture texFundoMenu;
    private Texture texBotaoVender;
    private Rectangle areaMenuEsquerdo;
    public Rectangle btnVender;

    // Variável do fundo da loja
    private Texture texFundoLoja;

    // Ferramenta para medir os textos
    private GlyphLayout layout;

    private boolean posicionando = false;
    private boolean posicaoValida = false;
    private Texture texturaPosicionando = null;
    public Torre torreSelecionada = null;

    public Rectangle[] slotsLoja = new Rectangle[6];

    public ConstrutorDeTorres() {
        imgLhama = new Texture("lhama.png");
        imgLhamaNinja = new Texture("lhamaninja.png");
        imgCuspe = new Texture("guspe.png");
        imgKunai = new Texture("kunai.png");

        font = new BitmapFont(Gdx.files.internal("fontes.fnt"));
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        font.getData().setScale(0.23f);
        font.setColor(Color.WHITE);

        layout = new GlyphLayout(); // Inicializa a ferramenta de medir texto

        imgMoedaPequena = new Texture("moeda.png");

        texFundoLoja = new Texture("Loja.png");

        Pixmap pixFundo = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixFundo.setColor(new Color(0.1f, 0.1f, 0.1f, 0.8f));
        pixFundo.fill();
        texFundoMenu = new Texture(pixFundo);

        Pixmap pixBotao = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixBotao.setColor(new Color(0.8f, 0.2f, 0.2f, 1f));
        pixBotao.fill();
        texBotaoVender = new Texture(pixBotao);

        pixFundo.dispose();
        pixBotao.dispose();

        areaMenuEsquerdo = new Rectangle(20, 300, 260, 350);
        btnVender = new Rectangle(50, 320, 200, 50);

        int inicioX = 1500;
        int inicioY = 680;
        int espacoX = 200;
        int espacoY = 160;

        for (int i = 0; i < 6; i++) {
            int coluna = i % 2;
            int linha = i / 2;
            slotsLoja[i] = new Rectangle(inicioX + (coluna * espacoX), inicioY - (linha * espacoY), 120, 120);
        }
    }

    public int atualizar(Vector2 posMouse, boolean justTouched, boolean isTouched, int dinheiro, Array<Torre> listaTorres, Mapa mapa, Hud hud) {

        if (justTouched) {
            hud.verificarClique(posMouse.x, posMouse.y);
        }

        if (hud.pausado) return dinheiro;

        float arrastoLargura = 80f;
        float arrastoAltura = 80f;

        if (texturaPosicionando != null) {
            float proporcao = (float) texturaPosicionando.getWidth() / texturaPosicionando.getHeight();
            arrastoLargura = arrastoAltura * proporcao;
        }

        if (posicionando) {
            posicaoValida = true;
            Rectangle hitboxTemp = new Rectangle(posMouse.x - (arrastoLargura / 2), posMouse.y - (arrastoAltura / 2), arrastoLargura, arrastoAltura);

            if (posMouse.x > 1400) posicaoValida = false;

            for (Rectangle rectCaminho : mapa.hitboxesCaminho) {
                if (rectCaminho.overlaps(hitboxTemp)) posicaoValida = false;
            }
            for (Torre t : listaTorres) {
                if (t.hitbox.overlaps(hitboxTemp)) posicaoValida = false;
            }
        }

        if (justTouched) {
            if (posicionando && texturaPosicionando != null) {
                if (posicaoValida) {
                    Texture tiroCerto = (texturaPosicionando == imgLhamaNinja) ? imgKunai : imgCuspe;
                    float spawnX = posMouse.x - (arrastoLargura / 2);
                    float spawnY = posMouse.y - (arrastoAltura / 2);

                    if (texturaPosicionando == imgLhamaNinja) {
                        listaTorres.add(new LhamaNinja(spawnX, spawnY, texturaPosicionando, tiroCerto));
                        dinheiro -= 150;
                    } else {
                        listaTorres.add(new LhamaNormal(spawnX, spawnY, texturaPosicionando, tiroCerto));
                        dinheiro -= 50;
                    }
                }
                posicionando = false;
                texturaPosicionando = null;
                return dinheiro;
            }

            if (torreSelecionada != null && btnVender.contains(posMouse.x, posMouse.y)) {
                int valorVenda = (torreSelecionada.textura == imgLhamaNinja) ? (int)(150 * 0.6f) : (int)(50 * 0.6f);
                dinheiro += valorVenda;
                listaTorres.removeValue(torreSelecionada, true);
                torreSelecionada = null;
                return dinheiro;
            }

            Torre novaSelecao = null;
            for (Torre t : listaTorres) {
                if (t.hitbox.contains(posMouse.x, posMouse.y)) {
                    novaSelecao = t;
                }
            }

            if (novaSelecao != null) {
                torreSelecionada = novaSelecao;
            } else if (torreSelecionada != null && areaMenuEsquerdo.contains(posMouse.x, posMouse.y)) {
            } else {
                torreSelecionada = null;
            }

            if (slotsLoja[0].contains(posMouse.x, posMouse.y) && dinheiro >= 50) {
                posicionando = true; texturaPosicionando = imgLhama;
                torreSelecionada = null;
            } else if (slotsLoja[1].contains(posMouse.x, posMouse.y) && dinheiro >= 150) {
                posicionando = true; texturaPosicionando = imgLhamaNinja;
                torreSelecionada = null;
            }
        }
        return dinheiro;
    }

    public void desenharLojaEArrasto(SpriteBatch batch, Vector2 posMouse, Hud hud) {

        batch.draw(texFundoLoja, 1400, 0, 520, 1080);

        font.getData().setScale(0.55f);

        // --- SLOT 0: LHAMA NORMAL ---
        String precoNormal = "50";
        float propLhama = (float) imgLhama.getWidth() / imgLhama.getHeight();
        float largLhama = slotsLoja[0].height * propLhama;

        // 1. Desenha a Lhama na posição ORIGINAL (slotsLoja[0].x)
        batch.draw(imgLhama, slotsLoja[0].x, slotsLoja[0].y, largLhama, slotsLoja[0].height);

        // 2. Mede o texto e centraliza embaixo da Lhama
        layout.setText(font, precoNormal);
        float larguraBloco0 = 25 + 5 + layout.width;
        float meioDaLhama0 = slotsLoja[0].x + (largLhama / 2f); // Descobre onde é o centro da Lhama
        float xPreco0 = meioDaLhama0 - (larguraBloco0 / 2f);

        batch.draw(imgMoedaPequena, xPreco0, slotsLoja[0].y - 30, 25, 25);
        font.draw(batch, precoNormal, xPreco0 + 30, slotsLoja[0].y - 10);

        // --- SLOT 1: LHAMA NINJA ---
        String precoNinja = "150";
        float propNinja = (float) imgLhamaNinja.getWidth() / imgLhamaNinja.getHeight();
        float largNinja = slotsLoja[1].height * propNinja;

        // 1. Desenha a Lhama Ninja na posição ORIGINAL (slotsLoja[1].x)
        batch.draw(imgLhamaNinja, slotsLoja[1].x, slotsLoja[1].y, largNinja, slotsLoja[1].height);

        // 2. Mede o texto e centraliza embaixo da Lhama Ninja
        layout.setText(font, precoNinja);
        float larguraBloco1 = 25 + 5 + layout.width;
        float meioDaLhama1 = slotsLoja[1].x + (largNinja / 2f);
        float xPreco1 = meioDaLhama1 - (larguraBloco1 / 2f);

        batch.draw(imgMoedaPequena, xPreco1, slotsLoja[1].y - 30, 25, 25);
        font.draw(batch, precoNinja, xPreco1 + 30, slotsLoja[1].y - 10);


        // --- INTERFACE DA TORRE SELECIONADA ---
        if (torreSelecionada != null) {
            batch.draw(texFundoMenu, areaMenuEsquerdo.x, areaMenuEsquerdo.y, areaMenuEsquerdo.width, areaMenuEsquerdo.height);

            float propTorre = (float) torreSelecionada.textura.getWidth() / torreSelecionada.textura.getHeight();
            batch.draw(torreSelecionada.textura, 100, 520, 100 * propTorre, 100);

            font.getData().setScale(0.73f);
            String nomeLhama = (torreSelecionada.textura == imgLhamaNinja) ? "Ninja Llama" : "Llama";
            font.draw(batch, nomeLhama, 40, 490);

            font.getData().setScale(0.45f);
            font.draw(batch, "Attack Damage: " + torreSelecionada.dano, 40, 440);

            int valorVenda = (torreSelecionada.textura == imgLhamaNinja) ? (int)(150 * 0.6f) : (int)(50 * 0.6f);

            batch.draw(texBotaoVender, btnVender.x, btnVender.y, btnVender.width, btnVender.height);

            font.draw(batch, "SELL:", btnVender.x + 25, btnVender.y + 35);
            batch.draw(imgMoedaPequena, btnVender.x + 95, btnVender.y + 12, 25, 25);
            font.draw(batch, String.valueOf(valorVenda), btnVender.x + 125, btnVender.y + 35);
        }

        // --- EFEITO DE ARRASTAR A TORRE ---
        if (posicionando && texturaPosicionando != null && !hud.pausado) {
            batch.setColor(1, posicaoValida ? 1 : 0.3f, posicaoValida ? 1 : 0.3f, 0.7f);
            float propArrastando = (float) texturaPosicionando.getWidth() / texturaPosicionando.getHeight();
            float arrastoLargura = 80f * propArrastando;

            batch.draw(texturaPosicionando, posMouse.x - (arrastoLargura / 2), posMouse.y - 40, arrastoLargura, 80);
            batch.setColor(1, 1, 1, 1);
        }
    }

    public void desenharHitboxes(ShapeRenderer shape, Vector2 posMouse, Hud hud) {
        shape.setColor(0.2f, 0.2f, 0.2f, 0.5f);
        for (int i = 0; i < 6; i++) {
            shape.rect(slotsLoja[i].x, slotsLoja[i].y, slotsLoja[i].width, slotsLoja[i].height);
        }

        if (torreSelecionada != null) {
            shape.setColor(1, 1, 1, 0.15f);
            shape.circle(torreSelecionada.posicao.x + (torreSelecionada.larguraDesenho / 2), torreSelecionada.posicao.y + (torreSelecionada.alturaDesenho / 2), torreSelecionada.raio);
        }

        if (posicionando && !hud.pausado) {
            shape.setColor(posicaoValida ? 1 : 1, posicaoValida ? 1 : 0, 0, 0.2f);
            float raioP = (texturaPosicionando == imgLhamaNinja) ? 250f : 200f;
            shape.circle(posMouse.x, posMouse.y, raioP);
        }
    }

    public void dispose() {
        imgLhama.dispose();
        imgLhamaNinja.dispose();
        imgCuspe.dispose();
        imgKunai.dispose();
        font.dispose();
        imgMoedaPequena.dispose();
        texFundoMenu.dispose();
        texBotaoVender.dispose();
        texFundoLoja.dispose();
    }
}   
