package com.TowerDefense.jogo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
    private boolean aberto; //Define o estado do menu
    private Rectangle areaPopup, areaBotoes; //Define a área do menu e dos botões
    private TipoConfig tipoSelecionado; //Define o tipo padrão como opções GERAIS
    private OpcaoConfig opcaoSelecionada; //Define a opção selecionada


//----------------- VARIÁVEIS DE ÁUDIO ------------------
    private float volume = 0.5f;
    private Rectangle sliderBar;
    private Rectangle sliderKnob;
    private boolean arrastando = false;

// ---------------- VARIÁVEIS DE VÍDEO ------------------
    private String[] resolucoes = {"800x600", "1280x720", "1920x1080"};
    private int resolucaoSelecionada = 0;
    private boolean dropdownAberto = false;
    private Rectangle botaoResolucao;
    private Rectangle[] opcoesResolucao;

// ---------------- VARIÁVEIS DE ATALHOS ----------------
    private int teclaPular = Input.Keys.SPACE;
    private boolean esperandoTecla = false;
    private Rectangle botaoKeybind;

    //------------------ CONSTRUTOR --------------------
    public PopupConfig() {
        shapeRenderer = new ShapeRenderer(); //Recebe o batch para desenhar na tela
        fonte = new BitmapFont(); //Recebe o batch para fonte
        aberto = false;
        arrastando = false;
        tipoSelecionado = TipoConfig.GERAL; //Define o tipo padrão como opções GERAIS

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

        // sliders
        List<OpcaoConfig> lista = opcoes.get(tipoSelecionado);

        if(lista != null) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            for (OpcaoConfig op : lista) {

                if (op.tipo == TipoOpcao.SLIDER && op.area != null) {

                    float barX = op.area.x + op.area.width - 200;
                    float barY = op.area.y + 20;

                    shapeRenderer.setColor(Color.DARK_GRAY);
                    shapeRenderer.rect(barX, barY, 150, 10);

                    float knobX = barX + op.valor * 150;

                    shapeRenderer.setColor(Color.WHITE);
                    shapeRenderer.rect(knobX - 5, barY - 5, 10, 20);
                }
            }

            shapeRenderer.end();
        }

        Gdx.gl.glDisable(GL20.GL_BLEND); //Desabilita a transparência
    }

    //---------------- FUNÇÃO QUE LÊ O MOUSE -------------------
    //------------------ INPUT ------------------
    public void handleInput(float mouseX, float mouseY) {

        if (!aberto) return;

        // trocar aba
        if (Gdx.input.justTouched()) {
            for (int i = 0; i < opcoesMenu.length; i++) {
                if (opcoesMenu[i].contains(mouseX, mouseY)) {
                    tipoSelecionado = TipoConfig.values()[i];
                }
            }
        }

        List<OpcaoConfig> lista = opcoes.get(tipoSelecionado);
        if (lista == null) return;

        for (OpcaoConfig op : lista) {

            // clique
            if (Gdx.input.justTouched() && op.area != null && op.area.contains(mouseX, mouseY)) {

                switch (op.tipo) {

                    case TOGGLE:
                        op.estado = !op.estado;
                        break;

                    case SLIDER:
                        op.arrastando = true;
                        break;

                    case DROPDOWN:
                        op.aberto = !op.aberto;
                        break;

                    case KEYBIND:
                        op.esperandoTecla = true;
                        break;
                }
            }

            // soltar slider
            if (!Gdx.input.isTouched()) {
                op.arrastando = false;
            }

            // arrastar slider
            if (op.tipo == TipoOpcao.SLIDER && op.arrastando) {

                float barX = op.area.x + op.area.width - 200;

                op.valor = (mouseX - barX) / 150f;
                op.valor = Math.max(0f, Math.min(1f, op.valor));
            }

            // dropdown seleção
            if (op.tipo == TipoOpcao.DROPDOWN && op.aberto) {

                for (int j = 0; j < op.opcoes.length; j++) {

                    Rectangle r = new Rectangle(
                        op.area.x,
                        op.area.y - (j + 1) * 50,
                        op.area.width,
                        50
                    );

                    if (Gdx.input.justTouched() && r.contains(mouseX, mouseY)) {
                        op.selecionado = j;
                        op.aberto = false;
                    }
                }
            }

            // keybind
            if (op.tipo == TipoOpcao.KEYBIND && op.esperandoTecla) {

                for (int key = 0; key < 256; key++) {
                    if (Gdx.input.isKeyJustPressed(key)) {
                        op.tecla = key;
                        op.esperandoTecla = false;
                    }
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
            switch (op.tipo) {

                case TOGGLE:
                    fonte.draw(batch, op.estado ? "ON" : "OFF",
                        op.area.x + op.area.width - 60,
                        op.area.y + 35);
                    break;

                case SLIDER:
                    fonte.draw(batch, (int)(op.valor*100) + "%",
                        op.area.x + op.area.width - 80,
                        op.area.y + 35);
                    break;

                case DROPDOWN:
                    fonte.draw(batch, op.opcoes[op.selecionado],
                        op.area.x + op.area.width - 150,
                        op.area.y + 35);

                    if (op.aberto) {
                        for (int j = 0; j < op.opcoes.length; j++) {

                            float y = op.area.y - (j + 1) * 50;

                            fonte.draw(batch, op.opcoes[j],
                                op.area.x + op.area.width - 150,
                                y + 35);
                        }
                    }
                    break;

                case KEYBIND:
                    String txt = op.esperandoTecla ? "..." : Input.Keys.toString(op.tecla);
                    fonte.draw(batch, txt,
                        op.area.x + op.area.width - 150,
                        op.area.y + 35);
                    break;
            }
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

    //------------------ OPÇÕES ------------------
    private void inicializarOpcoes() {

        List<OpcaoConfig> audio = new ArrayList<>();

        OpcaoConfig vol = new OpcaoConfig("Volume");
        vol.valor = 0.5f;

        audio.add(vol);
        audio.add(criarToggle("Mute"));

        opcoes.put(TipoConfig.AUDIO, audio);

        List<OpcaoConfig> video = new ArrayList<>();

        OpcaoConfig res = new OpcaoConfig("Resolution");
        res.opcoes = new String[]{"800x600","1280x720","1920x1080"};
        res.selecionado = 2;

        video.add(res);

        opcoes.put(TipoConfig.VIDEO, video);

        List<OpcaoConfig> atalhos = new ArrayList<>();

        OpcaoConfig key = new OpcaoConfig("Pular");
        key.tecla = Input.Keys.SPACE;

        atalhos.add(key);

        opcoes.put(TipoConfig.ATALHOS, atalhos);

        opcoes.put(TipoConfig.GERAL, new ArrayList<>());
        opcoes.put(TipoConfig.ACESSIBILIDADE, new ArrayList<>());
    }
    //------------------------ FUNÇÃO QUE CRIA UM TOGGLE -------------------------
    private OpcaoConfig criarToggle(String texto) {
        OpcaoConfig op = new OpcaoConfig(texto);
        op.estado = false;
        return op;
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

    //------------- FUNÇÃO ENUM QUE INCIALIZA O TIPO DE OPÇÃO ---------------
    public enum TipoOpcao {
        TOGGLE, SLIDER, DROPDOWN, KEYBIND
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
