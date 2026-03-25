package com.TowerDefense.jogo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class PopupConfig {

    //------------ VARIÁVEIS GLOBAIS ------------
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont fonte;

    //----------- ARRAYS ------------
    private Rectangle[] opcoesMenu, opcoesGerais;
    private String[] textosMenu = {"Geral", "Áudio", "Vídeo", "Atalhos", "Acessibilidade"};
    private String[] textosGerais = {"Motion Sensor Function", "Invert Camera X-Axis", "Invert Camera Y-Axis", "Invert Mouse X-Axis", "Invert Mouse Y-Axis","Language"};

    //---------- OUTRAS VARIÁVEIS ------------
    private boolean aberto = false;
    private Rectangle areaPopup, areaBotoes;
    private int opcaoSelecionada = 0;
    private TipoConfig tipoSelecionado;

    //------------ CONSTRUTOR -------------
    public PopupConfig() {
        shapeRenderer = new ShapeRenderer(); //Puxa o objeto para a classe
        fonte = new BitmapFont(); //Puxa o objeto para a Classe

        atualizarLayout();
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

        shapeRenderer.setColor(0.255f, 0.247f, 0.233f, 0f); //Cinza claro
        shapeRenderer.rect(areaPopup.x, areaPopup.y, areaPopup.width, areaPopup.height);

        //------------- POPUP BOTÕES --------------

        shapeRenderer.setColor(0f,0f,0.12f,0.8f); //Marrom
        shapeRenderer.rect(areaBotoes.x, areaBotoes.y, areaBotoes.width, areaBotoes.height);

        //------------- OPÇÕES -----------------

        //---------- HOVER SELECIONADO ----------
        Rectangle sel = opcoesMenu[opcaoSelecionada]; //Define a opção selecionada

        //--- RETÂNGULO ---
        shapeRenderer.setColor(Color.GRAY); //Cinza
        shapeRenderer.rect(sel.x, sel.y, sel.width, sel.height); //Define a posição e o tamanho do retângulo (Botões)

        //--- TEXTO ---
        fonte.setColor(Color.BLACK); //Preto

        shapeRenderer.end();

        if(opcoesGerais != null) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

            for (Rectangle r : opcoesGerais) {
                shapeRenderer.rect(r.x, r.y, r.width, r.height);
            }

            shapeRenderer.end();
        }

        //------------- LINHAS -----------
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line); //Inicia o objeto (Desenha as linhas)

        shapeRenderer.setColor(Color.WHITE); //Cor das linhas

        //------------- OPÇÕES -----------------

        //Loop para desenhar as opções do menu
        for (Rectangle r : opcoesMenu) {
            shapeRenderer.rect(r.x, r.y, r.width, r.height); //Define a posição e o tamanho do retângulo (Botões)
        }

        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND); //Desabilita o modo de blending

        //------------- TEXTOS -----------------

        batch.begin();

            fonte.setColor(Color.WHITE); //Cor do texto

            //Loop para desenhar os textos do menu
            for (int i = 0; i < textosMenu.length; i++) {
                desenharTextoCentralizado(fonte, batch, textosMenu[i], opcoesMenu[i]);
            }

            //------------- ABAS --------------
            desenharConteudo(batch);

        batch.end();
    }

    //------------ FUNÇÃO DA LÓGICA DO CLIQUE ------------
    public void handleInput(float mouseX, float mouseY) {

        if (!aberto) return; //se estiver fechado (popup), não renderiza nada

        //Se clicado em uma das opções do menu, define a opção selecionada
        if (Gdx.input.justTouched()) {
            for (int i = 0; i < opcoesMenu.length; i++) {
                if (opcoesMenu[i].contains(mouseX, mouseY)) {
                    opcaoSelecionada = i;
                }
            }
        }
    }

    //---------------- FUNÇÃO QUE CENTRALIZA TEXTO -----------------
    void desenharTextoCentralizado(BitmapFont font, SpriteBatch batch, String texto, Rectangle botao) {
        GlyphLayout layout = new GlyphLayout(font, texto);

        float x = botao.x + (botao.width - layout.width) / 2;
        float y = botao.y + (botao.height + layout.height) / 2;

        font.draw(batch, layout, x, y);
    }

    //------------ FUNÇÃO QUE DETECTA A AÇÃO DO BOTÃO -----------------
    private void desenharConteudo(SpriteBatch batch) {
        switch (opcaoSelecionada) {
            case 0:
                desenharAbaGeral(batch);
                break;
            case 1:
                desenharAbaAudio(batch);
                break;
            case 2:
                desenharAbaVideo(batch);
                break;
            case 3:
                desenharAbaAtalho(batch);
                break;
            case 4:
                desenharAbaAcessibilidade(batch);
                break;
            default:
                break;
        }
    }

    //------------ ABA GERAL ---------------
    public void desenharAbaGeral(SpriteBatch batch) {

        if(opcoesGerais == null) {
            opcoesGerais = new Rectangle[textosGerais.length];

            for (int i = 0; i < textosGerais.length; i++) {
                opcoesGerais[i] = new Rectangle(
                    areaPopup.x + 80,
                    areaPopup.y + areaPopup.height - 150 - i * 80,
                    400,
                    50
                );
            }
        }
        for (int i = 0; i < textosGerais.length; i++) {
            desenharTextoCentralizado(fonte, batch, textosGerais[i], opcoesGerais[i]);
        }

    }

    //------------ ABA VIDEO ---------------
    private void desenharAbaVideo(SpriteBatch batch) {
        fonte.draw(batch, "VIDEO OPTIONS", 700, 900);
        fonte.draw(batch, "Exibition Mode", 700, 820);
        fonte.draw(batch, "Resolution", 700, 760);
        fonte.draw(batch, "Frame Rate", 700, 700);

    }
    //------------ ABA AUDIO ---------------
    private void desenharAbaAudio(SpriteBatch batch) {
        fonte.draw(batch, "AUDIO OPTIONS", 700, 900);
        fonte.draw(batch, "Exibition Mode", 700, 820);
        fonte.draw(batch, "Resolution", 700, 760);
        fonte.draw(batch, "Frame Rate", 700, 700);

    }
    //------------ ABA ATALHOS ---------------
    private void desenharAbaAtalho(SpriteBatch batch) {
        fonte.draw(batch, "ATALHO OPTIONS", 700, 900);
        fonte.draw(batch, "Exibition Mode", 700, 820);
        fonte.draw(batch, "Resolution", 700, 760);
        fonte.draw(batch, "Frame Rate", 700, 700);

    }

    //------------ ABA ACESSIBILIDADE ---------------
    private void desenharAbaAcessibilidade(SpriteBatch batch) {
        fonte.draw(batch, "ACESSIBILIDADE OPTIONS", 700, 900);
        fonte.draw(batch, "Exibition Mode", 700, 820);
        fonte.draw(batch, "Resolution", 700, 760);
        fonte.draw(batch, "Frame Rate", 700, 700);

    }

    //------------- FUNÇÃO ATUALIZAÇÃO LAYOUT ----------------
    public void atualizarLayout() {
        float larguraTela = Gdx.graphics.getWidth(); //Pega a largura da tela do usuário
        float alturaTela = Gdx.graphics.getHeight(); //Pega a altura da tela do usuário

        float larguraPopup = larguraTela * 0.57f; //Define a largura do popup
        float alturaPopup = alturaTela * 0.78f; //Define a altura do popup

        float xPopup = (larguraTela - larguraPopup) / 2f; //Define a posição do popup (x)
        float yPopup = (alturaTela - alturaPopup) / 2f; //Define a posição do popup (y)

        areaPopup = new Rectangle(xPopup, yPopup, larguraPopup, alturaPopup); //Constrói o popup definindo a posição e o tamanho

        float alturaBarra = 100; //Define a altura da barra de botões
        areaBotoes = new Rectangle(0, alturaTela - alturaBarra, larguraTela, alturaBarra); //Constrói a barra de botões definindo a posição e o tamanho

        opcoesMenu = new Rectangle[textosMenu.length]; //Cria um array de retângulos para as opções do menu

        float larguraBotao = 200; //Define a largura dos botões
        float alturaBotao = 60; //Define a altura dos botões
        float espacamento = 20; //Define o espaço entre os botões

        float totalWidth = opcoesMenu.length * larguraBotao + (opcoesMenu.length - 1) * espacamento; //Calcula a largura total dos botões (Todos botões somados)
        float x = (larguraTela - totalWidth) / 2f; //Calcula a posição x dos botões
        float y = areaBotoes.y + (areaBotoes.height - alturaBotao) / 2f; //Calcula a posição y dos botões

        //Loop para criar os botões do menu de acordo com o número de registros no array
        for (int i = 0; i < opcoesMenu.length; i++) {
            opcoesMenu[i] = new Rectangle(
                x + i * (larguraBotao + espacamento), //Define a posição x do botão
                y, //Define a posição y do botão
                larguraBotao, //Define a largura do botão
                alturaBotao //Define a altura do botão
            );
        }
    }

    //------------ FUNÇÃO QUE INICIALIZA O TIPO DE CONFIG ----------------
    public enum TipoConfig {
        GERAL,
        AUDIO,
        VIDEO,
        ATALHOS,
        ACESSIBILIDADE
    }

    //------------ TIPO CONFIG SELECIONADA ------------
    public void setTipoConfig (TipoConfig tipo) {
        this.tipoSelecionado = tipo;
    }

    //------------ FUNÇÃO QUE RETORNA AS OPÇÕES DA CONFIG SELECIONADA ------------
    private void setOpcoesConfigSelecionada() {
        switch(tipoSelecionado) {
            case GERAL:
                opcoesGerais = new Rectangle[textosGerais.length];
                break;
            case AUDIO:
                break;
            case VIDEO:
                break;
            case ATALHOS:
                break;
            case ACESSIBILIDADE:
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
