package com.TowerDefense.jogo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class PopupConfig {

    //------------ VARIÁVEIS GLOBAIS ------------
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont fonte;

    //----------- ARRAYS ------------
    private Rectangle[] opcoesMenu;
    private String[] textosMenu = {"Geral", "Áudio", "Vídeo", "Controles"};

    //---------- OUTRAS VARIÁVEIS ------------
    private boolean aberto = false;
    private Rectangle areaPopup, areaBotoes;
    private int opcaoSelecionada = 0;

    //------------ CONSTRUTOR -------------
    public PopupConfig() {
        shapeRenderer = new ShapeRenderer(); //Puxa o objeto para a classe
        fonte = new BitmapFont(); //Puxa o objeto para a Classe

        areaPopup = new Rectangle(650, 120, 1100, 850); //Define a área do popup
        areaBotoes = new Rectangle(200, 120, 400, 850); //Define a área dos botões
        opcoesMenu = new Rectangle[textosMenu.length]; //Define a quantidade de opções do menu de acordo com a quanidade de itens registrados em "textosMenu"

        float x = 230; //Define a posição x do menu
        float y = 760; //Define a posição y do menu

        //Cria um retângulo para cada opção do menu
        for (int i=0; i < opcoesMenu.length; i++) {
            opcoesMenu[i] = new Rectangle(x, y - i * 80, 200, 60); //Define a posição x, y, largura e altura do retângulo(Opções)
        }
    }

    //---------------- RENDER --------------
    public void render(SpriteBatch batch) {

        if (!aberto) return; //se estiver fechado (popup), não renderiza nada

        //----------- FORMAS ---------------
        batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND); //Habilita o modo de blending(Transparencia)

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled); //Inicia o objeto (Preenche a tela)

        //------------- FUNDO ----------------

        shapeRenderer.setColor(0, 0, 0, 0.6f); //Cor Preta e transparência 50%
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); //Define a posição e o tamanho do retângulo

        //------------- POPUP PRINCIPAL -----------------

        shapeRenderer.setColor(Color.LIGHT_GRAY); //Cinza claro
        rectArredondado(shapeRenderer, areaPopup.x, areaPopup.y, areaPopup.width, areaPopup.height, 20);

        //------------- POPUP BOTÕES --------------

        shapeRenderer.setColor(0.59f,0.39f,0.24f,1f); //Marrom
        rectArredondado(shapeRenderer, areaBotoes.x, areaBotoes.y, areaBotoes.width, areaBotoes.height, 20);

        //------------- OPÇÕES -----------------

        //---------- HOVER SELECIONADO ----------
        Rectangle sel = opcoesMenu[opcaoSelecionada]; //Define a opção selecionada

        //--- RETÂNGULO ---
        shapeRenderer.setColor(Color.GRAY); //Cinza
        shapeRenderer.rect(sel.x, sel.y, sel.width, sel.height); //Define a posição e o tamanho do retângulo (Botões)

        //--- TEXTO ---
        fonte.setColor(Color.BLACK); //Preto

        shapeRenderer.end();

        //------------- LINHAS -----------
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line); //Inicia o objeto (Desenha as linhas)

        shapeRenderer.setColor(Color.BLACK); //Cor das linhas - PRETO;

        //------------- OPÇÕES -----------------

        //Loop para desenhar as opções do menu
        for (Rectangle r : opcoesMenu) {
            shapeRenderer.rect(r.x, r.y, r.width, r.height); //Define a posição e o tamanho do retângulo (Botões)
        }

        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND); //Desabilita o modo de blending

        //------------- TEXTOS -----------------

        batch.begin();

            fonte.setColor(Color.BLACK); //Cor do texto - PRETO

            //Loop para desenhar os textos do menu
            for (int i = 0; i < textosMenu.length; i++) {
                fonte.draw(batch, textosMenu[i], opcoesMenu[i].x + 20, opcoesMenu[i].y + 40);
            }

        batch.end();
    }

    //------------ FUNÇÃO QUE DEIXA AS BORDAS ARREDONDADAS ----------------
    private void rectArredondado(ShapeRenderer sr, float x, float y, float w, float h, float r) {

        // centro
        sr.rect(x + r, y, w - 2*r, h);

        // lados
        sr.rect(x, y + r, r, h - 2*r);
        sr.rect(x + w - r, y + r, r, h - 2*r);

        // cantos (círculos)
        sr.circle(x + r, y + r, r); // baixo esquerdo
        sr.circle(x + w - r, y + r, r); // baixo direito
        sr.circle(x + r, y + h - r, r); // cima esquerdo
        sr.circle(x + w - r, y + h - r, r); // cima direito
    }

    //------------ FUNÇÃO DA LÓGICA DO CLIQUE ------------
    public void handleInput(float mouseX, float mouseY) {

        if (!aberto) return; //se estiver fechado (popup), não renderiza nada

        //Se clicado em uma das opções do menu, define a opção selecionada
        if (Gdx.input.justTouched()) {
            for (int i = 0; i < opcoesMenu.length; i++) {
                if (opcoesMenu[i].contains(mouseX, mouseY)) {
                    opcaoSelecionada = i;
                    acaoBotao(i);
                }
            }
        }
    }

    //------------ FUNÇÃO QUE DETECTA A AÇÃO DO BOTÃO -----------------
    private void acaoBotao(int i) {

        switch(i) {
            case 0:
                System.out.println("Geral");
                break;
            case 1:
                System.out.println("Áudio");
                break;
            case 2:
                System.out.println("Vídeo");
                break;
            case 3:
                System.out.println("Controles");
                break;
        }

    }

    //------------ FUNÇÃO PARA ABRIR E FECHAR O POPUP ------------
    public void toggle() {
        aberto = !aberto;
    }

    //------------ FUNÇÃO PARA VERIFICAR SE O POPUP ESTÁ ABERTO ------------
    public boolean isAberto() {
        return aberto;
    }

}
