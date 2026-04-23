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
    private Texture imgLlamaNeves, imgNevesAttack;

    private TextureRegion iconeLhama, iconeLhamaNinja, iconeLhamaMage, iconeLlamaCyborg, iconeLlamaAngel, iconeLlamaBurguesa, iconeLlamaChef, iconeLlamaNeves;

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

    private Array<Torre> torresReferencia;

    // Variável para guardar o dinheiro atual sem precisar mudar a assinatura dos métodos de desenho
    private int dinheiroAtual = 0;

    public ConstrutorDeTorres(Array<TipoLlama> deckEscolhido) {
        this.deckAtual = deckEscolhido;

        // Carregamento de Texturas
        imgLhama = new Texture("LlamaSS.png");
        imgLhamaNinja = new Texture("NinjaLlama.png");
        imgCuspe = new Texture("Attack_Llama.png");
        imgKunai = new Texture("Attack_NinjaLlama.png");
        imgLhamaMage = new Texture("MageLlamaSS.png");
        imgMagia = new Texture("Attack_MageLlama.png");
        imgLlamaCyborg = new Texture("CyborgLlamaSS.png");
        imgCyborgAttack = new Texture("Attack_CyborgLlama.png");
        imgLlamaAngel = new Texture("AngelLlamaSS.png");
        imgNuvemNasc = new Texture("NuvemNasc_AngelLlamaSS.png");
        imgNuvemAtiva = new Texture("Nuvem_AngelLlamaSS.png");
        imgNuvemSum = new Texture("NuvemSum_AngelLlamaSS.png");
        imgAngelAttack = new Texture("Attack_AngelLlamaSS.png");
        imgLlamaBurguesa = new Texture("BourgeoisLlamaSS.png");
        imgLlamaChef = new Texture("ChefLlamaSS.png");
        imgArmaChef = new Texture("Arma_ChefLlamaSS.png");
        imgLlamaNeves = new Texture("YetiLlamaSS.png");
        imgNevesAttack = new Texture("Attack_YetiLlamaSS.png");

        imgBtnAcelerar = new Texture("Iniciar.png");
        btnAcelerar = new Rectangle(1450, 50, 100, 100);

        // Definição de Ícones da Loja
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
        int largNeves = imgLlamaNeves.getWidth() / 12;
        iconeLlamaNeves = new TextureRegion(imgLlamaNeves, 0, 0, largNeves, imgLlamaNeves.getHeight());

        iconesLoja = new HashMap<>();
        iconesLoja.put(TipoLlama.NORMAL, iconeLhama);
        iconesLoja.put(TipoLlama.NINJA, iconeLhamaNinja);
        iconesLoja.put(TipoLlama.MAGE, iconeLhamaMage);
        iconesLoja.put(TipoLlama.CYBORG, iconeLlamaCyborg);
        iconesLoja.put(TipoLlama.ANGEL, iconeLlamaAngel);
        iconesLoja.put(TipoLlama.BURGUESA, iconeLlamaBurguesa);
        iconesLoja.put(TipoLlama.CHEF, iconeLlamaChef);
        iconesLoja.put(TipoLlama.NEVES, iconeLlamaNeves);

        // UI e Fontes
        font = new BitmapFont(Gdx.files.internal("fontes.fnt"));
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        font.getData().setScale(0.23f);
        layout = new GlyphLayout();
        imgMoedaPequena = new Texture("moeda.png");
        texFundoLoja = new Texture("Loja.png");
        texFundoMenu = new Texture("PainelSell.png");
        texBtnSell = new Texture("Sell_Button.png");
        texBtnSellHover = new Texture("Sell_ButtonHover.png");

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
        btnVender = new Rectangle(rectDesenhoVender.x + margemW, rectDesenhoVender.y + margemH, largDesenho - (margemW * 2), altDesenho - (margemH * 2));

        for (int i = 0; i < 6; i++) {
            int coluna = i % 2;
            int linha = i / 2;
            slotsLoja[i] = new Rectangle(1480 + (coluna * 220), 680 - (linha * 220), 120, 120);
        }
    }

    private int contarLlamasNoMapa(TipoLlama tipo) {
        if (torresReferencia == null) return 0;
        int count = 0;
        for (Torre t : torresReferencia) if (t.tipoLlama == tipo) count++;
        return count;
    }

    public int atualizar(Vector2 posMouse, boolean justTouched, boolean isTouched, int dinheiro, Array<Torre> listaTorres, Mapa mapa, Hud hud) {
        this.torresReferencia = listaTorres;
        this.dinheiroAtual = dinheiro; // Guarda o dinheiro recebido

        boolean isMobile = Gdx.app.getType() == com.badlogic.gdx.Application.ApplicationType.Android ||
                           Gdx.app.getType() == com.badlogic.gdx.Application.ApplicationType.iOS;

        if (justTouched) hud.verificarClique(posMouse.x, posMouse.y);
        if (hud.pausado) return dinheiro;

        float arrastoLarg = 80f;
        float arrastoAlt = 80f;

        if (texturaPosicionando != null && lhamaSendoArrastada != null) {
            // Lógica isolada de tamanho: Angel usa base 90 de largura. O resto usa 80 de altura.
            if (lhamaSendoArrastada == TipoLlama.ANGEL) {
                arrastoLarg = 90f;
                arrastoAlt = arrastoLarg * ((float) texturaPosicionando.getRegionHeight() / texturaPosicionando.getRegionWidth());
            } else {
                arrastoAlt = 80f; // Padrão que funciona para Yeti, Normal, etc.
                arrastoLarg = arrastoAlt * ((float) texturaPosicionando.getRegionWidth() / texturaPosicionando.getRegionHeight());
            }
        }

        if (posicionando) {
            posicaoValida = (posMouse.x <= 1400);
            Rectangle hbTemp = new Rectangle(posMouse.x - (arrastoLarg / 2), posMouse.y - (arrastoAlt / 2), arrastoLarg, arrastoAlt);
            for (Rectangle r : mapa.hitboxesCaminho) if (r.overlaps(hbTemp)) posicaoValida = false;
            for (Torre t : listaTorres) if (t.hitbox.overlaps(hbTemp)) posicaoValida = false;
        }

        // --- LÓGICA DE SOLTAR (MOBILE) ---
        if (isMobile && posicionando && !isTouched && lhamaSendoArrastada != null) {
            if (posicaoValida) {
                float sx = posMouse.x - (arrastoLarg / 2), sy = posMouse.y - (arrastoAlt / 2);
                Torre novaTorre = null;
                switch(lhamaSendoArrastada) {
                    case BURGUESA: novaTorre = new LlamaBurguesa(sx, sy); break;
                    case ANGEL: novaTorre = new LlamaAngel(sx, sy, imgLlamaAngel, imgNuvemNasc, imgNuvemAtiva, imgNuvemSum, imgAngelAttack); break;
                    case CYBORG: novaTorre = new LlamaCyborg(sx, sy, imgLlamaCyborg, imgCyborgAttack); break;
                    case CHEF: novaTorre = new LlamaChef(sx, sy, imgLlamaChef, imgArmaChef); break;
                    case MAGE: novaTorre = new LhamaMage(sx, sy, imgLhamaMage, imgMagia); break;
                    case NINJA: novaTorre = new LhamaNinja(sx, sy, imgLhamaNinja, imgKunai); break;
                    case NEVES: novaTorre = new LlamaNeves(sx, sy, imgLlamaNeves, imgNevesAttack); break;
                    case NORMAL: novaTorre = new LhamaNormal(sx, sy, imgLhama, imgCuspe); break;
                }
                if (novaTorre != null) {
                    novaTorre.tipoLlama = lhamaSendoArrastada;
                    listaTorres.add(novaTorre);
                    dinheiro -= lhamaSendoArrastada.preco;
                }
            }
            posicionando = false; lhamaSendoArrastada = null; texturaPosicionando = null;
        }

        if (justTouched) {
            if (btnAcelerar.contains(posMouse.x, posMouse.y)) jogoAcelerado = !jogoAcelerado;

            if (!isMobile && posicionando && lhamaSendoArrastada != null) {
                if (posicaoValida) {
                    float sx = posMouse.x - (arrastoLarg / 2), sy = posMouse.y - (arrastoAlt / 2);
                    Torre novaTorre = null;
                    switch(lhamaSendoArrastada) {
                        case BURGUESA: novaTorre = new LlamaBurguesa(sx, sy); break;
                        case ANGEL: novaTorre = new LlamaAngel(sx, sy, imgLlamaAngel, imgNuvemNasc, imgNuvemAtiva, imgNuvemSum, imgAngelAttack); break;
                        case CYBORG: novaTorre = new LlamaCyborg(sx, sy, imgLlamaCyborg, imgCyborgAttack); break;
                        case CHEF: novaTorre = new LlamaChef(sx, sy, imgLlamaChef, imgArmaChef); break;
                        case MAGE: novaTorre = new LhamaMage(sx, sy, imgLhamaMage, imgMagia); break;
                        case NINJA: novaTorre = new LhamaNinja(sx, sy, imgLhamaNinja, imgKunai); break;
                        case NEVES: novaTorre = new LlamaNeves(sx, sy, imgLlamaNeves, imgNevesAttack); break;
                        case NORMAL: novaTorre = new LhamaNormal(sx, sy, imgLhama, imgCuspe); break;
                    }
                    if (novaTorre != null) {
                        novaTorre.tipoLlama = lhamaSendoArrastada;
                        listaTorres.add(novaTorre);
                        dinheiro -= lhamaSendoArrastada.preco;
                    }
                }
                posicionando = false; lhamaSendoArrastada = null; texturaPosicionando = null;
            } else if (torreSelecionada != null && btnModoAlvo.contains(posMouse.x, posMouse.y)) {
                torreSelecionada.trocarModoAlvo();
            } else if (torreSelecionada != null && btnVender.contains(posMouse.x, posMouse.y)) {
                int preco = torreSelecionada.tipoLlama.preco;
                dinheiro += (int) (preco * 0.6f);
                if (torreSelecionada instanceof LlamaAngel) ((LlamaAngel) torreSelecionada).destruirNuvem();
                listaTorres.removeValue(torreSelecionada, true);
                torreSelecionada = null;
            } else {
                Torre achou = null;
                for (Torre t : listaTorres) if (t.hitbox.contains(posMouse.x, posMouse.y)) achou = t;
                if (achou != null) torreSelecionada = achou;
                else if (!areaMenuEsquerdo.contains(posMouse.x, posMouse.y) && !btnAcelerar.contains(posMouse.x, posMouse.y)) {
                    torreSelecionada = null;
                }
            }

            for (int i = 0; i < 6; i++) {
                if (slotsLoja[i].contains(posMouse.x, posMouse.y) && i < deckAtual.size) {
                    TipoLlama lhamaEscolhida = deckAtual.get(i);
                    if (dinheiro >= lhamaEscolhida.preco && contarLlamasNoMapa(lhamaEscolhida) < lhamaEscolhida.limite) {
                        posicionando = true; lhamaSendoArrastada = lhamaEscolhida; texturaPosicionando = iconesLoja.get(lhamaEscolhida);
                    }
                }
            }
        }

        this.dinheiroAtual = dinheiro; // Atualiza caso tenha comprado ou vendido
        return dinheiro;
    }

    public void desenharLojaEArrasto(SpriteBatch batch, Vector2 posMouse, Hud hud) {
        batch.draw(texFundoLoja, 1400, 0, 520, 1080);
        for (int i = 0; i < 6; i++) {
            if (i < deckAtual.size) {
                TipoLlama lhamaMenu = deckAtual.get(i);
                // Usando a variável this.dinheiroAtual aqui
                desenharItemLoja(batch, iconesLoja.get(lhamaMenu), String.valueOf(lhamaMenu.preco), slotsLoja[i], contarLlamasNoMapa(lhamaMenu), lhamaMenu.limite, this.dinheiroAtual);
            }
        }

        if (torreSelecionada != null) {
            batch.draw(texFundoMenu, areaMenuEsquerdo.x, areaMenuEsquerdo.y, areaMenuEsquerdo.width, areaMenuEsquerdo.height);
            TextureRegion renderMenu = iconesLoja.get(torreSelecionada.tipoLlama);
            String nome = torreSelecionada.tipoLlama.name();

            float propTorre = (float) renderMenu.getRegionWidth() / renderMenu.getRegionHeight();
            float iconHeight = 110f; float iconWidth = iconHeight * propTorre;
            batch.draw(renderMenu, areaMenuEsquerdo.x + (areaMenuEsquerdo.width / 2) - (iconWidth / 2), 580, iconWidth, iconHeight);

            font.getData().setScale(0.55f);
            layout.setText(font, nome);
            font.draw(batch, nome, areaMenuEsquerdo.x + (areaMenuEsquerdo.width / 2) - (layout.width / 2), 550);

            font.getData().setScale(0.45f);
            float ly = 500;
            font.draw(batch, "Dano: " + (int)torreSelecionada.dano, 85f, ly); ly -= 25;
            font.draw(batch, "Range: " + (int)torreSelecionada.raio, 85f, ly); ly -= 25;
            font.draw(batch, "SPA: " + String.format(java.util.Locale.US, "%.1f", torreSelecionada.cooldown) + "s", 85f, ly);

            batch.draw(texBotaoAzul, btnModoAlvo.x, btnModoAlvo.y, btnModoAlvo.width, btnModoAlvo.height);
            String txtFoco = "Foco: " + torreSelecionada.modoAlvoAtual.name();
            font.getData().setScale(0.35f);
            layout.setText(font, txtFoco);
            font.draw(batch, txtFoco, btnModoAlvo.x + (btnModoAlvo.width/2) - (layout.width/2), btnModoAlvo.y + (btnModoAlvo.height/2) + (layout.height/2));

            int valorVenda = (int) (torreSelecionada.tipoLlama.preco * 0.6f);
            batch.draw(btnVender.contains(posMouse.x, posMouse.y) ? texBtnSellHover : texBtnSell, rectDesenhoVender.x, rectDesenhoVender.y, rectDesenhoVender.width, rectDesenhoVender.height);
            batch.draw(imgMoedaPequena, rectDesenhoVender.x + rectDesenhoVender.width - 5, rectDesenhoVender.y + (rectDesenhoVender.height/2) - 15, 30, 30);
            font.draw(batch, String.valueOf(valorVenda), rectDesenhoVender.x + rectDesenhoVender.width + 25, rectDesenhoVender.y + (rectDesenhoVender.height/2) + 10);
        }

        if (jogoAcelerado) batch.setColor(0.5f, 1f, 0.5f, 1f);
        batch.draw(imgBtnAcelerar, btnAcelerar.x, btnAcelerar.y, btnAcelerar.width, btnAcelerar.height);
        batch.setColor(Color.WHITE);

        if (posicionando && texturaPosicionando != null && lhamaSendoArrastada != null) {
            batch.setColor(1, posicaoValida ? 1 : 0.3f, posicaoValida ? 1 : 0.3f, 0.7f);

            float arrL = 80f;
            float arrA = 80f;

            if (lhamaSendoArrastada == TipoLlama.ANGEL) {
                arrL = 90f;
                arrA = arrL * ((float) texturaPosicionando.getRegionHeight() / texturaPosicionando.getRegionWidth());
            } else {
                arrA = 80f;
                arrL = arrA * ((float) texturaPosicionando.getRegionWidth() / texturaPosicionando.getRegionHeight());
            }

            batch.draw(texturaPosicionando, posMouse.x - (arrL / 2), posMouse.y - (arrA / 2), arrL, arrA);
            batch.setColor(1, 1, 1, 1);
        }
    }

    private void desenharItemLoja(SpriteBatch batch, TextureRegion tex, String preco, Rectangle slot, int qtdAtual, int limite, int granaAtual) {
        float larg = slot.height * ((float) tex.getRegionWidth() / tex.getRegionHeight());
        int custo = Integer.parseInt(preco);

        // Verifica se pode comprar (ter dinheiro E não ter batido o limite)
        boolean podeComprar = (granaAtual >= custo) && (qtdAtual < limite);

        if (!podeComprar) {
            // Se não puder comprar, aplica um tom vermelho escuro e transparente
            batch.setColor(1f, 0.3f, 0.3f, 0.6f);
        } else {
            batch.setColor(Color.WHITE);
        }

        batch.draw(tex, slot.x + (slot.width - larg) / 2f, slot.y, larg, slot.height);

        // Reset da cor para desenhar os textos normalmente
        batch.setColor(Color.WHITE);

        font.getData().setScale(0.55f);
        batch.draw(imgMoedaPequena, slot.x + 15, slot.y - 35, 25, 25);

        // Se o dinheiro for insuficiente, desenha o preço em vermelho
        if (granaAtual < custo) font.setColor(Color.RED);
        font.draw(batch, preco, slot.x + 45, slot.y - 12);
        font.setColor(Color.WHITE);

        font.getData().setScale(0.35f);
        // Se o limite estourou, desenha o contador em vermelho
        if (qtdAtual >= limite) font.setColor(Color.RED);
        font.draw(batch, qtdAtual + "/" + limite, slot.x + slot.width - 40, slot.y + slot.height - 5);
        font.setColor(Color.WHITE);
    }

    public void desenharHitboxes(ShapeRenderer shape, Vector2 posMouse, Hud hud) {
        if (torreSelecionada != null) {
            shape.setColor(1, 1, 1, 0.15f);
            shape.circle(torreSelecionada.posicao.x + (torreSelecionada.larguraDesenho / 2), torreSelecionada.posicao.y + (torreSelecionada.alturaDesenho / 2), torreSelecionada.raio);
        }

        if (posicionando && !hud.pausado && lhamaSendoArrastada != null) {
            shape.setColor(1f, posicaoValida ? 1f : 0f, posicaoValida ? 1f : 0f, 0.2f);

            // Puxa o raio exatamente como está definido no Enum!
            float raioExibicao = lhamaSendoArrastada.raioInicial;

            shape.circle(posMouse.x, posMouse.y, raioExibicao);
        }
    }

    public void dispose() {
        imgLhama.dispose(); imgLhamaNinja.dispose(); imgLhamaMage.dispose(); imgLlamaCyborg.dispose();
        imgLlamaChef.dispose(); imgCuspe.dispose(); imgKunai.dispose(); imgMagia.dispose();
        imgCyborgAttack.dispose(); imgArmaChef.dispose(); imgLlamaAngel.dispose(); imgNuvemNasc.dispose();
        imgNuvemAtiva.dispose(); imgNuvemSum.dispose(); imgAngelAttack.dispose(); imgLlamaBurguesa.dispose();
        imgLlamaNeves.dispose(); imgNevesAttack.dispose(); imgBtnAcelerar.dispose(); imgMoedaPequena.dispose();
        texFundoLoja.dispose(); texBotaoAzul.dispose(); texFundoMenu.dispose(); texBtnSell.dispose();
        texBtnSellHover.dispose(); font.dispose();
    }
}
