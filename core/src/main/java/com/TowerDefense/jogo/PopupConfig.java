package com.TowerDefense.jogo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.util.*;

public class PopupConfig {

//----------------------- VARIÁVEIS GLOBAIS ------------------------
    private final ShapeRenderer shapeRenderer; // Variável que permite desenhar na tela
    private final BitmapFont fonte; // Variável que permite carregar imagens

//----------------------- ARRAYS ------------------------
    private Rectangle[] opcoesMenu; //Array que armazena as opções do menu
    private Map<TipoConfig, List<OpcaoConfig>> opcoes = new HashMap<>(); //Map que armazena as opções dos menus
    private String[] textosMenu = {"Geral", "Áudio", "Vídeo", "Atalhos", "Acessibilidade"}; //Array que armazena os textos dos menus

//---------------------- OUTRAS VARIÁVEIS -----------------
    private boolean aberto = false; //Define o estado do menu
    private Rectangle areaPopup, areaBotoes; //Define a área do menu e dos botões
    private TipoConfig tipoSelecionado = TipoConfig.GERAL; //Define o tipo padrão como opções GERAIS

    //------------------ CONSTRUTOR --------------------
    public PopupConfig() {
        shapeRenderer = new ShapeRenderer(); //Recebe o batch para desenhar na tela
        fonte = new BitmapFont(); //Recebe o batch para fonte

        atualizarLayout(); //Chama a função que atualiza o layout do menu
        inicializarOpcoes(); //Chama a função que inicializa as opções do menu
    }

    //--------------- RENDER DAS OPÇÕES ---------------------
    public void render(SpriteBatch batch) {

        if (!aberto) return; //Se o menu estiver fechado, não renderiza nada

        //Loop que exibe as opções de cada menu
        for(int i=0;i<textosMenu.length;i++){
            desenharTextoCentralizado(fonte,batch,textosMenu[i],opcoesMenu[i]);
        }

        //Desenha o conteúdo do menu
        desenharConteudo(batch);
    }

    public void renderShapes() {

        if (!aberto) return; //Se o menu estiver fechado, não renderiza nada

        Gdx.gl.glEnable(GL20.GL_BLEND); //Permite mexer com transarência

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled); //Inicia o shapeRenderer preenchendo

        // Fundo escuro
        shapeRenderer.setColor(0,0,0,0.6f);
        shapeRenderer.rect(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        // Popup
        shapeRenderer.setColor(0.25f,0.25f,0.25f,0f);
        shapeRenderer.rect(areaPopup.x,areaPopup.y,areaPopup.width,areaPopup.height);

        // Barra que contém os botões
        shapeRenderer.setColor(0,0,0.2f,0.8f);
        shapeRenderer.rect(areaBotoes.x,areaBotoes.y,areaBotoes.width,areaBotoes.height);

        // Aba selecionada
        Rectangle sel = opcoesMenu[getIndice()]; //Pega a opção selecionada
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.rect(sel.x,sel.y,sel.width,sel.height);

        shapeRenderer.end();

        // Bordas
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);

        //Loop que desenha as bordas dos menus
        for(Rectangle r : opcoesMenu){
            shapeRenderer.rect(r.x,r.y,r.width,r.height);
        }

        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND); //Desabilita a transparência
    }

    //---------------- FUNÇÃO QUE LÊ O MOUSE -------------------
    public void handleInput(float mouseX, float mouseY) {

        if (!aberto) return; //Se estiver fechado, não realiza nada

        //Detecta o clique no botão de opções
        if (Gdx.input.justTouched()) {

            // trocar aba
            for (int i = 0; i < opcoesMenu.length; i++) {
                if (opcoesMenu[i].contains(mouseX, mouseY)) {
                    tipoSelecionado = TipoConfig.values()[i]; //Troca o tipo selecionado
                }
            }

            // clique nas opções
            List<OpcaoConfig> lista = opcoes.get(tipoSelecionado); //Pega a lista de opções
            if (lista == null) return; //Se não tiver opções, não realiza nada

            //Loop que verifica se o clique está dentro das opções
            for (OpcaoConfig op : lista) {
                if (op.area != null && op.area.contains(mouseX, mouseY)) {
                    op.estado = !op.estado; //Troca o estado da opção (ON/OFF)
                }
            }
        }
    }

    //----------------- FUNÇÃO QUE DESENHA O CONTEÚDO -----------------
    private void desenharConteudo(SpriteBatch batch) {

        List<OpcaoConfig> lista = opcoes.get(tipoSelecionado); //Pega a lista de opções
        if (lista == null) return;

        //Loop que percorre o tamanho da lista para desenhar as opções de config
        for (int i = 0; i < lista.size(); i++) {

            OpcaoConfig op = lista.get(i);

            if (op.area == null) {
                op.area = new Rectangle(
                    areaPopup.x + 80,
                    areaPopup.y + areaPopup.height - 150 - i * 80,
                    1000,
                    50
                );
            }

            desenharTextoCentralizado(fonte, batch, op.texto, op.area); //Texto centralizado

            // desenhar estado
            fonte.draw(batch,
                op.estado ? "ON" : "OFF",
                op.area.x + op.area.width - 60,
                op.area.y + 35
            );
        }
    }

    //---------------------- FUNÇÃO QUE CENTRALIZA O TEXTO ------------------------
    private void desenharTextoCentralizado(BitmapFont font, SpriteBatch batch, String texto, Rectangle botao) {
        GlyphLayout layout = new GlyphLayout(font, texto); //Layout do texto
        float x = botao.x + (botao.width - layout.width) / 2;
        float y = botao.y + (botao.height + layout.height) / 2;
        font.draw(batch, layout, x, y);
    }

    //----------------------- FUNÇÃO QUE INICIALIZA AS OPÇÕES -----------------------
    private void inicializarOpcoes() {

        opcoes.put(TipoConfig.GERAL, criarLista(
            "Motion Sensor Function",
            "Invert Camera X-Axis",
            "Invert Camera Y-Axis",
            "Invert Mouse X-Axis",
            "Invert Mouse Y-Axis",
            "Language"
        ));

        opcoes.put(TipoConfig.AUDIO, criarLista(
            "Music Volume",
            "Sound Effects Volume",
            "Mute All"
        ));

        opcoes.put(TipoConfig.VIDEO, criarLista(
            "Exibition Mode",
            "Resolution",
            "Frame Rate"
        ));

        opcoes.put(TipoConfig.ATALHOS, criarLista(
            "Keybind 1",
            "Keybind 2"
        ));

        opcoes.put(TipoConfig.ACESSIBILIDADE, criarLista(
            "Menu Reader",
            "Voice",
            "Volume",
            "Colourblind Mode"
        ));
    }

    //------------------------ FUNÇÃO QUE CRIA A LISTA DE OPÇÕES -------------------------
    private List<OpcaoConfig> criarLista(String... textos) { //Variável que recebe os textos
        List<OpcaoConfig> lista = new ArrayList<>(); //Lista que armazena as opções
        //Loop que percorre o tamanho da lista para criar as opções
        for (String t : textos) {
            lista.add(new OpcaoConfig(t));
        }
        return lista;
    }

    //------------------------------ FUNÇÃO QUE FAZ O LAYOUT DO POPUP --------------------------------
    public void atualizarLayout() {

        float largura = Gdx.graphics.getWidth(); //Pega a largura da tela
        float altura = Gdx.graphics.getHeight(); //Pega a altura da tela

        areaPopup = new Rectangle(largura*0.2f, altura*0.1f, largura*0.6f, altura*0.8f);
        areaBotoes = new Rectangle(0, altura-100, largura, 100);

        opcoesMenu = new Rectangle[textosMenu.length]; //Array que armazena as opções do menu

        float larguraBotao = 200;
        float espacamento = 20;
        float total = textosMenu.length * larguraBotao + (textosMenu.length - 1)*espacamento; //Calcula o espaço total dos botões

        float x = (largura-total)/2;
        float y = areaBotoes.y + 20;

        //Loop que percorre a array opcoesMenu para gerar os botões
        for(int i=0;i<opcoesMenu.length;i++){
            opcoesMenu[i] = new Rectangle(x + i*(larguraBotao+espacamento), y, larguraBotao,60);
        }
    }

    //------------- FUNÇÃO ENUM QUE INCIALIZA O TIPO DE CONFIG ---------------
    public enum TipoConfig {
        GERAL, AUDIO, VIDEO, ATALHOS, ACESSIBILIDADE
    }

    //----------- Pega o índice do tipo selecionado -----------
    private int getIndice() {
        return tipoSelecionado.ordinal();
    }

    // ------ Troca o estado do menu (Aberto/Fechado) ------
    public void toggle() {
        aberto = !aberto;
    }

    // ------- Função que retorna o estado do menu -------
    public boolean isAberto() {
        return aberto;
    }
}
