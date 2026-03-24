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
import java.util.HashMap;

public class ConstrutorDeTorres {
    private Texture imgLhama, imgLhamaNinja, imgCuspe, imgKunai;
    private Texture imgLhamaMage, imgMagia;
    private Texture imgLlamaCyborg, imgCyborgAttack;
    private Texture imgLlamaAngel, imgNuvemNasc, imgNuvemAtiva, imgNuvemSum, imgAngelAttack;
    private Texture imgLlamaBurguesa;
    private Texture imgLlamaChef, imgArmaChef;

    private TextureRegion iconeLhama, iconeLhamaNinja, iconeLhamaMage, iconeLlamaCyborg, iconeLlamaAngel, iconeLlamaBurguesa, iconeLlamaChef;

    private HashMap<TipoLlama, TextureRegion> iconesLoja;
    public Array<TipoLlama> deckAtual;
    private TipoLlama lhamaSendoArrastada = null;

    private BitmapFont font;
    private Texture imgMoedaPequena;
    private Texture texFundoMenu, texBotaoAzul, texFundoLoja;
    private Texture texBtnSell, texBtnSellHover;

    private Rectangle areaMenuEsquerdo;
    public Rectangle btnModoAlvo;
    private Rectangle rectDesenhoVender;
    public Rectangle btnVender;

    private GlyphLayout layout;

    private boolean posicionando = false;
    private boolean posicaoValida = false;
    private TextureRegion texturaPosicionando = null;

    public Torre torreSelecionada = null;
    public Rectangle[] slotsLoja = new Rectangle[6];

    private Texture imgBtnAcelerar;
    public Rectangle btnAcelerar;
    public boolean jogoAcelerado = false;

    // 🔥 NOVO: Guarda uma referência das torres no mapa para podermos contar!
    private Array<Torre> torresReferencia;

    public ConstrutorDeTorres(Array<TipoLlama> deckEscolhido) {
        this.deckAtual = deckEscolhido;

        imgLhama = new Texture("llama.png");
        imgLhamaNinja = new Texture("lhamaninja.png");
        imgCuspe = new Texture("guspe.png");
        imgKunai = new Texture("kunai.png");

        imgLhamaMage = new Texture("llamamage.png");
        imgMagia = new Texture("mageattack.png");

        imgLlamaCyborg = new Texture("LlamaCyborg.png");
        imgCyborgAttack = new Texture("CyborgAttack.png");

        imgLlamaAngel = new Texture("llamaAngel.png");
        imgNuvemNasc = new Texture("nuvemAngelnasc.png");
        imgNuvemAtiva = new Texture("nuvemAngel.png");
        imgNuvemSum = new Texture("nuvemAngelsum.png");
        imgAngelAttack = new Texture("Angel_Attack.png");

        imgLlamaBurguesa = new Texture("llamaBurguesa.png");

        imgLlamaChef = new Texture("llamaChef.png");
        imgArmaChef = new Texture("armaChef.png");

        imgBtnAcelerar = new Texture("Iniciar.png");
        btnAcelerar = new Rectangle(1450, 50, 100, 100);

        int largLhama = imgLhama.getWidth() / 9;
        iconeLhama = new TextureRegion(imgLhama, 0, 0, largLhama, imgLhama.getHeight());

        iconeLhamaNinja = new TextureRegion(imgLhamaNinja);

        int largMage = imgLhamaMage.getWidth() / 11;
        iconeLhamaMage = new TextureRegion(imgLhamaMage, 0, 0, largMage, imgLhamaMage.getHeight());

        int largCyborg = imgLlamaCyborg.getWidth() / 18;
        iconeLlamaCyborg = new TextureRegion(imgLlamaCyborg, 0, 0, largCyborg, imgLlamaCyborg.getHeight());

        int largAngel = imgLlamaAngel.getWidth() / 5;
        iconeLlamaAngel = new TextureRegion(imgLlamaAngel, 0, 0, largAngel, imgLlamaAngel.getHeight());

        int largBurguesa = imgLlamaBurguesa.getWidth() / 17;
        iconeLlamaBurguesa = new TextureRegion(imgLlamaBurguesa, 0, 0, largBurguesa, imgLlamaBurguesa.getHeight());

        int largChef = imgLlamaChef.getWidth() / 5;
        iconeLlamaChef = new TextureRegion(imgLlamaChef, 0, 0, largChef, imgLlamaChef.getHeight());

        iconesLoja = new HashMap<>();
        iconesLoja.put(TipoLlama.NORMAL, iconeLhama);
        iconesLoja.put(TipoLlama.NINJA, iconeLhamaNinja);
        iconesLoja.put(TipoLlama.MAGE, iconeLhamaMage);
        iconesLoja.put(TipoLlama.CYBORG, iconeLlamaCyborg);
        iconesLoja.put(TipoLlama.ANGEL, iconeLlamaAngel);
        iconesLoja.put(TipoLlama.BURGUESA, iconeLlamaBurguesa);
        iconesLoja.put(TipoLlama.CHEF, iconeLlamaChef);

        font = new BitmapFont(Gdx.files.internal("fontes.fnt"));
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        font.getData().setScale(0.23f);
        layout = new GlyphLayout();
        imgMoedaPequena = new Texture("moeda.png");
        texFundoLoja = new Texture("Loja.png");
        texFundoMenu = new Texture("fundosell.png");

        texBtnSell = new Texture("BUTTON_sell.png");
        texBtnSellHover = new Texture("BUTTON_sellhover.png");

        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(new Color(0.2f, 0.4f, 0.8f, 1f));
        pix.fill();
        texBotaoAzul = new Texture(pix);
        pix.dispose();

        areaMenuEsquerdo = new Rectangle(20, 260, 350, 530);
        btnModoAlvo = new Rectangle(105, 380, 180, 35);

        float largDesenho = 260f;
        float altDesenho = largDesenho * ((float) texBtnSell.getHeight() / texBtnSell.getWidth());
        rectDesenhoVender = new Rectangle(5, 270, largDesenho, altDesenho);

        float margemW = largDesenho * 0.25f;
        float margemH = altDesenho * 0.35f;

        btnVender = new Rectangle(
            rectDesenhoVender.x + margemW,
            rectDesenhoVender.y + margemH,
            largDesenho - (margemW * 2),
            altDesenho - (margemH * 2)
        );

        for (int i = 0; i < 6; i++) {
            int coluna = i % 2;
            int linha = i / 2;
            float x = 1480 + (coluna * 220);
            float y = 680 - (linha * 220);
            slotsLoja[i] = new Rectangle(x, y, 120, 120);
        }
    }

    // 🔥 NOVO MÉTODO: Conta quantas lhamas de um tipo já existem no mapa
    private int contarLlamasNoMapa(TipoLlama tipo) {
        if (torresReferencia == null) return 0;
        int count = 0;
        for (Torre t : torresReferencia) {
            if (t.tipoLlama == tipo) count++;
        }
        return count;
    }

    public int atualizar(Vector2 posMouse, boolean justTouched, boolean isTouched, int dinheiro, Array<Torre> listaTorres, Mapa mapa, Hud hud) {
        // 🔥 Atualiza a referência das torres para o método de contar funcionar
        this.torresReferencia = listaTorres;

        if (justTouched) hud.verificarClique(posMouse.x, posMouse.y);
        if (hud.pausado) return dinheiro;

        float arrastoAlt = 80f;
        float arrastoLarg = (texturaPosicionando != null) ? arrastoAlt * ((float) texturaPosicionando.getRegionWidth() / texturaPosicionando.getRegionHeight()) : 80f;

        if (posicionando) {
            posicaoValida = (posMouse.x <= 1400);
            Rectangle hbTemp = new Rectangle(posMouse.x - (arrastoLarg / 2), posMouse.y - (arrastoAlt / 2), arrastoLarg, arrastoAlt);
            for (Rectangle r : mapa.hitboxesCaminho) if (r.overlaps(hbTemp)) posicaoValida = false;
            for (Torre t : listaTorres) if (t.hitbox.overlaps(hbTemp)) posicaoValida = false;
        }

        if (justTouched) {
            if (btnAcelerar.contains(posMouse.x, posMouse.y)) {
                jogoAcelerado = !jogoAcelerado;
            }

            if (posicionando && lhamaSendoArrastada != null) {
                if (posicaoValida) {
                    float sx = posMouse.x - (arrastoLarg / 2), sy = posMouse.y - (arrastoAlt / 2);

                    Torre novaTorre = null; // Preparamos a variável

                    // Criamos a torre dependendo do tipo
                    switch(lhamaSendoArrastada) {
                        case BURGUESA: novaTorre = new LlamaBurguesa(sx, sy); break;
                        case ANGEL: novaTorre = new LlamaAngel(sx, sy, imgLlamaAngel, imgNuvemNasc, imgNuvemAtiva, imgNuvemSum, imgAngelAttack); break;
                        case CYBORG: novaTorre = new LlamaCyborg(sx, sy, imgLlamaCyborg, imgCyborgAttack); break;
                        case CHEF: novaTorre = new LlamaChef(sx, sy, imgLlamaChef, imgArmaChef); break;
                        case MAGE: novaTorre = new LhamaMage(sx, sy, imgLhamaMage, imgMagia); break;
                        case NINJA: novaTorre = new LhamaNinja(sx, sy, imgLhamaNinja, imgKunai); break;
                        case NORMAL: novaTorre = new LhamaNormal(sx, sy, imgLhama, imgCuspe); break;
                    }

                    // 🔥 O SEGREDO PARA O CONTADOR FUNCIONAR ESTÁ AQUI! 🔥
                    if (novaTorre != null) {
                        novaTorre.tipoLlama = lhamaSendoArrastada; // Carimba a identidade dela!
                        listaTorres.add(novaTorre);                // Coloca no mapa
                    }

                    dinheiro -= lhamaSendoArrastada.preco;
                }
                posicionando = false;
                lhamaSendoArrastada = null;
                texturaPosicionando = null;
            } else if (torreSelecionada != null && btnModoAlvo.contains(posMouse.x, posMouse.y)) {
                torreSelecionada.trocarModoAlvo();
            } else if (torreSelecionada != null && btnVender.contains(posMouse.x, posMouse.y)) {
                int preco = (torreSelecionada instanceof LlamaBurguesa) ? 500 : (torreSelecionada instanceof LlamaAngel) ? 400 : (torreSelecionada instanceof LlamaChef) ? 350 : (torreSelecionada instanceof LlamaCyborg) ? 300 : (torreSelecionada instanceof LhamaMage ? 250 : (torreSelecionada instanceof LhamaNinja ? 150 : 50));
                dinheiro += (int) (preco * 0.6f);

                if (torreSelecionada instanceof LlamaAngel) {
                    ((LlamaAngel) torreSelecionada).destruirNuvem();
                }

                listaTorres.removeValue(torreSelecionada, true);
                torreSelecionada = null;
            } else {
                Torre achou = null;
                for (Torre t : listaTorres)
                    if (t.hitbox.contains(posMouse.x, posMouse.y)) achou = t;
                if (achou != null) torreSelecionada = achou;
                else if (!areaMenuEsquerdo.contains(posMouse.x, posMouse.y) && !btnAcelerar.contains(posMouse.x, posMouse.y)) {
                    torreSelecionada = null;
                }
            }

            for (int i = 0; i < 6; i++) {
                if (slotsLoja[i].contains(posMouse.x, posMouse.y) && i < deckAtual.size) {
                    TipoLlama lhamaEscolhida = deckAtual.get(i);
                    int qtdAtual = contarLlamasNoMapa(lhamaEscolhida);

                    // 🔥 BARRICADA: Só deixa comprar se tiver dinheiro E se não atingiu o limite!
                    if (dinheiro >= lhamaEscolhida.preco && qtdAtual < lhamaEscolhida.limite) {
                        posicionando = true;
                        lhamaSendoArrastada = lhamaEscolhida;
                        texturaPosicionando = iconesLoja.get(lhamaEscolhida);
                    }
                }
            }
        }
        return dinheiro;
    }

    public void desenharLojaEArrasto(SpriteBatch batch, Vector2 posMouse, Hud hud) {
        batch.draw(texFundoLoja, 1400, 0, 520, 1080);
        font.getData().setScale(0.55f);

        for (int i = 0; i < 6; i++) {
            if (i < deckAtual.size) {
                TipoLlama lhamaMenu = deckAtual.get(i);
                int qtdAtual = contarLlamasNoMapa(lhamaMenu); // 🔥 Conta para mostrar na loja
                desenharItemLoja(batch, iconesLoja.get(lhamaMenu), String.valueOf(lhamaMenu.preco), slotsLoja[i], qtdAtual, lhamaMenu.limite);
            }
        }

        if (torreSelecionada != null) {
            batch.draw(texFundoMenu, areaMenuEsquerdo.x, areaMenuEsquerdo.y, areaMenuEsquerdo.width, areaMenuEsquerdo.height);

            TextureRegion renderMenu = iconeLhama;
            String nome = "Llama";
            int precoCheio = 50;

            if (torreSelecionada instanceof LlamaBurguesa) {
                renderMenu = iconeLlamaBurguesa; nome = "Llama Burguesa"; precoCheio = TipoLlama.BURGUESA.preco;
            } else if (torreSelecionada instanceof LlamaAngel) {
                renderMenu = iconeLlamaAngel; nome = "Angel Llama"; precoCheio = TipoLlama.ANGEL.preco;
            } else if (torreSelecionada instanceof LlamaCyborg) {
                renderMenu = iconeLlamaCyborg; nome = "Cyborg Llama"; precoCheio = TipoLlama.CYBORG.preco;
            } else if (torreSelecionada instanceof LlamaChef) {
                renderMenu = iconeLlamaChef; nome = "Chef Llama"; precoCheio = TipoLlama.CHEF.preco;
            } else if (torreSelecionada instanceof LhamaMage) {
                renderMenu = iconeLhamaMage; nome = "Mage Llama"; precoCheio = TipoLlama.MAGE.preco;
            } else if (torreSelecionada instanceof LhamaNinja) {
                renderMenu = iconeLhamaNinja; nome = "Ninja Llama"; precoCheio = TipoLlama.NINJA.preco;
            }

            float propTorre = (float) renderMenu.getRegionWidth() / renderMenu.getRegionHeight();
            float iconHeight = 110f;
            float iconWidth = iconHeight * propTorre;
            float iconX = areaMenuEsquerdo.x + (areaMenuEsquerdo.width / 2) - (iconWidth / 2);
            batch.draw(renderMenu, iconX, 580, iconWidth, iconHeight);

            font.getData().setScale(0.75f);
            layout.setText(font, nome);
            float nameX = areaMenuEsquerdo.x + (areaMenuEsquerdo.width / 2) - (layout.width / 2);
            font.draw(batch, nome, nameX, 550);

            font.getData().setScale(0.45f);

            float labelX = 85f, valueX = 195f, currentY = 500, spacingY = 25;

            font.draw(batch, "Dano:", labelX, currentY);
            font.draw(batch, String.valueOf((int) torreSelecionada.dano), valueX, currentY); currentY -= spacingY;

            font.draw(batch, "Range:", labelX, currentY);
            font.draw(batch, String.valueOf((int) torreSelecionada.raio), valueX, currentY); currentY -= spacingY;

            font.draw(batch, "SPA:", labelX, currentY);
            font.draw(batch, String.format(java.util.Locale.US, "%.1f", torreSelecionada.cooldown) + "s", valueX, currentY);

            batch.draw(texBotaoAzul, btnModoAlvo.x, btnModoAlvo.y, btnModoAlvo.width, btnModoAlvo.height);

            String textoFoco = "Foco: ";
            switch (torreSelecionada.modoAlvoAtual) {
                case PRIMEIRO: textoFoco += "PRIMEIRO"; break;
                case ULTIMO: textoFoco += "ÚLTIMO"; break;
                case MAIS_FORTE: textoFoco += "MAIS FORTE"; break;
            }

            font.getData().setScale(0.35f);
            layout.setText(font, textoFoco);
            float textXModoAlvo = btnModoAlvo.x + (btnModoAlvo.width / 2) - (layout.width / 2);
            float textYModoAlvo = btnModoAlvo.y + (btnModoAlvo.height / 2) + (layout.height / 2);
            font.draw(batch, textoFoco, textXModoAlvo, textYModoAlvo);

            int valorVenda = (int) (precoCheio * 0.6f);

            if (btnVender.contains(posMouse.x, posMouse.y)) {
                batch.draw(texBtnSellHover, rectDesenhoVender.x, rectDesenhoVender.y, rectDesenhoVender.width, rectDesenhoVender.height);
            } else {
                batch.draw(texBtnSell, rectDesenhoVender.x, rectDesenhoVender.y, rectDesenhoVender.width, rectDesenhoVender.height);
            }

            font.getData().setScale(0.40f);
            String valueSell = String.valueOf(valorVenda);
            layout.setText(font, valueSell);

            float spacingMoeda = -5f, iconSize = 30f;
            float startXMoeda = rectDesenhoVender.x + rectDesenhoVender.width + spacingMoeda;
            float centerYMoeda = rectDesenhoVender.y + (rectDesenhoVender.height / 2f);

            batch.draw(imgMoedaPequena, startXMoeda, centerYMoeda - (iconSize / 2f), iconSize, iconSize);
            font.draw(batch, valueSell, startXMoeda + iconSize - 2f, centerYMoeda + (layout.height / 2f));
        }

        if (jogoAcelerado) batch.setColor(0.5f, 1f, 0.5f, 1f);
        batch.draw(imgBtnAcelerar, btnAcelerar.x, btnAcelerar.y, btnAcelerar.width, btnAcelerar.height);
        batch.setColor(Color.WHITE);

        // 🔥 DESENHA A TORRE SENDO ARRASTADA E O TEXTO DO LIMITE "1/3" EMBAIXO DELA
        if (posicionando && texturaPosicionando != null && lhamaSendoArrastada != null) {
            batch.setColor(1, posicaoValida ? 1 : 0.3f, posicaoValida ? 1 : 0.3f, 0.7f);
            float arrL = 80f * ((float) texturaPosicionando.getRegionWidth() / texturaPosicionando.getRegionHeight());
            batch.draw(texturaPosicionando, posMouse.x - (arrL / 2), posMouse.y - 40, arrL, 80);
            batch.setColor(1, 1, 1, 1);

            // Calcula o limite para mostrar: "(quantidade no mapa + 1) / limite"
            int qtdNoMapa = contarLlamasNoMapa(lhamaSendoArrastada);
            String txtLimite = (qtdNoMapa + 1) + "/" + lhamaSendoArrastada.limite;

            font.getData().setScale(0.40f);
            layout.setText(font, txtLimite);

            // Fica vermelho se for a última
            if (qtdNoMapa + 1 >= lhamaSendoArrastada.limite) font.setColor(Color.RED);

            font.draw(batch, txtLimite, posMouse.x - (layout.width / 2), posMouse.y - 50);
            font.setColor(Color.WHITE); // Reseta a cor pra não estragar as outras letras
        }
    }

    // 🔥 ATUALIZADO: Agora recebe qtd e limite para desenhar na loja também
    private void desenharItemLoja(SpriteBatch batch, TextureRegion tex, String preco, Rectangle slot, int qtdAtual, int limite) {
        float larg = slot.height * ((float) tex.getRegionWidth() / tex.getRegionHeight());
        float xCentralizado = slot.x + (slot.width - larg) / 2f;

        // Desenha Lhama com sombra se estiver no limite máximo
        if (qtdAtual >= limite) batch.setColor(0.4f, 0.4f, 0.4f, 1f);
        batch.draw(tex, xCentralizado, slot.y, larg, slot.height);
        batch.setColor(Color.WHITE);

        // Preço e Moeda
        font.getData().setScale(0.55f);
        layout.setText(font, preco);
        batch.draw(imgMoedaPequena, slot.x + 15, slot.y - 35, 25, 25);
        font.draw(batch, preco, slot.x + 45, slot.y - 12);

        // 🔥 NOVO: Mostra "1/3" direto na carta da loja!
        font.getData().setScale(0.35f);
        String txtEstoque = qtdAtual + "/" + limite;
        layout.setText(font, txtEstoque);

        if (qtdAtual >= limite) {
            txtEstoque = "MAX";
            layout.setText(font, txtEstoque);
            font.setColor(Color.RED);
        }

        // Desenha no topo da carta
        font.draw(batch, txtEstoque, slot.x + slot.width - layout.width - 5, slot.y + slot.height - 5);
        font.setColor(Color.WHITE); // Reseta a cor
    }

    public void desenharHitboxes(ShapeRenderer shape, Vector2 posMouse, Hud hud) {
        if (torreSelecionada != null) {
            shape.setColor(1, 1, 1, 0.15f);
            shape.circle(torreSelecionada.posicao.x + (torreSelecionada.larguraDesenho / 2), torreSelecionada.posicao.y + (torreSelecionada.alturaDesenho / 2), torreSelecionada.raio);
            shape.setColor(1f, 0f, 0f, 1f);
            shape.rect(btnVender.x, btnVender.y, btnVender.width, btnVender.height);
        }
        if (posicionando && !hud.pausado && lhamaSendoArrastada != null) {
            shape.setColor(1f, posicaoValida ? 1f : 0f, posicaoValida ? 1f : 0f, posicaoValida ? 0.2f : 0.3f);
            float r = 200f; // raio padrão
            switch(lhamaSendoArrastada) {
                case BURGUESA: r = 0f; break;
                case ANGEL: r = 350f; break;
                case CYBORG: r = 250f; break;
                case CHEF: r = 150f; break;
                case MAGE: r = 300f; break;
            }
            shape.circle(posMouse.x, posMouse.y, r);
        }
    }

    public void dispose() {
        imgLhama.dispose();
        imgLhamaNinja.dispose();
        imgLhamaMage.dispose();
        imgLlamaCyborg.dispose();
        imgLlamaChef.dispose();
        imgCuspe.dispose();
        imgKunai.dispose();
        imgMagia.dispose();
        imgCyborgAttack.dispose();
        imgArmaChef.dispose();
        imgLlamaAngel.dispose();
        imgNuvemNasc.dispose();
        imgNuvemAtiva.dispose();
        imgNuvemSum.dispose();
        imgAngelAttack.dispose();
        imgLlamaBurguesa.dispose();
        imgBtnAcelerar.dispose();
        imgMoedaPequena.dispose();
        texFundoLoja.dispose();
        texBotaoAzul.dispose();
        texFundoMenu.dispose();
        texBtnSell.dispose();
        texBtnSellHover.dispose();
        font.dispose();
    }
}
