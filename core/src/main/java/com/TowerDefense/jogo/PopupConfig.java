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
    private final ShapeRenderer shapeRenderer; //Variável que permite desenhar na tela (formas)
    private final BitmapFont fonte; //Variável que permite desenhar texto

    //----------------------- ARRAYS ------------------------
    private Rectangle[] opcoesMenu; //Array com os botões clicáveis
    private Map<TipoConfig, List<OpcaoConfig>> opcoes = new HashMap<>(); //Map que armazena as opções de configurações (tipo e texto)
    private String[] textosMenu = {"Geral", "Áudio", "Vídeo", "Atalhos", "Acessibilidade"}; //Array que armazena os textos dos botões

    //---------------------- OUTRAS VARIÁVEIS -----------------
    private boolean aberto; //Inicializa a variável que indica se o popup está aberto ou fechado
    private Rectangle areaPopup, areaBotoes; //Variáveis que armazenam as posições do popup e dos botões
    private TipoConfig tipoSelecionado; //Variável que armazena o tipo de configuração selecionado ativo

    //------------------ CONSTRUTOR --------------------
    public PopupConfig() {

        //---- Inicializa as variáveis ------
        shapeRenderer = new ShapeRenderer(); //Objetoo que permite desenhar na tela
        fonte = new BitmapFont(); //Objeto que permite desenhar texto
        aberto = false; //Começa fechado

        //Define padrão
        tipoSelecionado = TipoConfig.GERAL;

        //Atualiza o layout e chama as opções de configurações
        atualizarLayout();
        inicializarOpcoes();
    }

    //--------------- RENDER DAS OPÇÕES ---------------------
    public void render(SpriteBatch batch) {

        if (!aberto) return; //Se o popup não está aberto, não executa nada

        //Loop que exibe os botões e o texto centralizado
        for(int i=0;i<textosMenu.length;i++){
            desenharTextoCentralizado(fonte,batch,textosMenu[i],opcoesMenu[i]);
        }

        //Desenha o conteúdo do popup
        desenharConteudo(batch);
    }

    public void renderShapes() {

        if (!aberto) return; //Se o popup não está aberto, não executa nada

        //Ativa a opção de blending (Mexer com opacidade)
        Gdx.gl.glEnable(GL20.GL_BLEND);

        //Incia o shaperenderer preenchendo o fundo com a cor desejada
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Fundo - escurecido
        shapeRenderer.setColor(0,0,0,0.6f);
        shapeRenderer.rect(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        // Popup
        shapeRenderer.setColor(0.25f,0.25f,0.25f,0f);
        shapeRenderer.rect(areaPopup.x,areaPopup.y,areaPopup.width,areaPopup.height);

        // Barra
        shapeRenderer.setColor(0,0,0.2f,0.8f);
        shapeRenderer.rect(areaBotoes.x,areaBotoes.y,areaBotoes.width,areaBotoes.height);

        // Aba selecionada
        Rectangle sel = opcoesMenu[getIndice()]; //Pega a posição do botão selecionado (ativo)
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.rect(sel.x,sel.y,sel.width,sel.height); //Destaca ele com uma bordas

        shapeRenderer.end();

        // Bordas
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);

        //Loop que desenha as bordas dos botões
        for(Rectangle r : opcoesMenu){
            shapeRenderer.rect(r.x,r.y,r.width,r.height);
        }



        shapeRenderer.end();




        // SLIDERS
        List<OpcaoConfig> lista = opcoes.get(tipoSelecionado); //Pega a lista de opções do tipo selecionado
        if (lista == null) return; //Se a lista estiver vazia, não executa nada

        atualizarAreasOpcoes(lista); //Atualiza as áreas dos botões

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        //Loop que desenha o tipo de config de acordo com a opção
        for (OpcaoConfig op : lista) {

            //Se o tipo for slider e a area não estiver vazia
            if (op.tipo == TipoOpcao.SLIDER && op.area != null) {

                float barX = op.area.x + op.area.width - 200; //Pega a posição da barra
                float barY = op.area.y + 20;

                shapeRenderer.setColor(Color.DARK_GRAY);
                shapeRenderer.rect(barX, barY, 150, 10);

                float knobX = barX + op.valor * 150; //Pega a posição do knob (valor vai de 0 a 1) multiplica pelo valor da barra

                shapeRenderer.setColor(Color.WHITE);
                shapeRenderer.rect(knobX - 5, barY - 5, 10, 20);
            }

        }

        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void renderDropdownTop(SpriteBatch batch) {


        List<OpcaoConfig> lista = opcoes.get(tipoSelecionado);
        if (lista == null) return;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (OpcaoConfig op : lista) {

            if (op.tipo == TipoOpcao.DROPDOWN && op.aberto && op.area != null) {

                for (int j = 0; j < op.opcoes.length; j++) {

                    float y = op.area.y - (j + 1) * 50;

                    shapeRenderer.setColor(0.15f, 0.15f, 0.15f, 1f);

                    float dropdownWidth = op.area.width * 0.1f;
                    float dropdownX = op.area.x + op.area.width - dropdownWidth; // alinhado à direita
                    shapeRenderer.rect(dropdownX -65, y, dropdownWidth, 50);
                }
            }
        }

        shapeRenderer.end();

        batch.begin();

        for (OpcaoConfig op : lista) {
            float dropdownWidth = op.area.width * 0.3f;
            float dropdownX = op.area.x + op.area.width - dropdownWidth; // alinhado à direita
            if (op.tipo == TipoOpcao.DROPDOWN && op.aberto && op.area != null) {

                for (int j = 0; j < op.opcoes.length; j++) {

                    float y = op.area.y - (j + 1) * 50;

                    desenharTextoCentralizado(fonte, batch, op.opcoes[j], new Rectangle(dropdownX + 35, y, dropdownWidth, 50));
                }
            }
        }

        batch.end();
    }

    //------------------ INPUT ------------------
    public void handleInput(float mouseX, float mouseY) {

        if (!aberto) return;

        //-------------- TROCAR ABA ----------------
        if (Gdx.input.justTouched()) {
            //Loop que verifica se o mouse clicou em algum botão
            for (int i = 0; i < opcoesMenu.length; i++) {
                if (opcoesMenu[i].contains(mouseX, mouseY)) { //Detecta clique em aba
                    tipoSelecionado = TipoConfig.values()[i]; //Se o mouse clicou, muda o tipo de configuração selecionado
                }
            }
        }

        List<OpcaoConfig> lista = opcoes.get(tipoSelecionado); //Pega a lista de opções do tipo selecionado
        if (lista == null) return; //Se a lista estiver vazia, não executa nada

        atualizarAreasOpcoes(lista);

        OpcaoConfig dropdownAberto = null;

        for (OpcaoConfig op : lista) {
            if (op.tipo == TipoOpcao.DROPDOWN && op.aberto) {
                dropdownAberto = op;
                break;
            }
        }

        if (dropdownAberto != null) {

            boolean clicouEmOpcao = false;

            for (int j = 0; j < dropdownAberto.opcoes.length; j++) {

                float dropdownWidth = 200;
                float dropdownX = dropdownAberto.area.x + dropdownAberto.area.width - dropdownWidth;

                Rectangle r = new Rectangle(
                    dropdownX,
                    dropdownAberto.area.y - (j + 1) * 50,
                    dropdownWidth,
                    50
                );

                if (Gdx.input.justTouched() && r.contains(mouseX, mouseY)) {
                    dropdownAberto.selecionado = j;
                    dropdownAberto.aberto = false;
                    clicouEmOpcao = true;
                }
            }

            //clique fora do dropdown fecha
            if (Gdx.input.justTouched() && !clicouEmOpcao) {
                dropdownAberto.aberto = false;
            }

            return; //ISSO AQUI É A CHAVE
        }

        for (OpcaoConfig op : lista) {

            if (op.area == null) continue;

            // clique
            if (Gdx.input.justTouched() && op.area.contains(mouseX, mouseY)) {

                switch (op.tipo) {

                    case TOGGLE:
                        op.estado = !op.estado; //Liga/Desliga
                        break;

                    case SLIDER:
                        op.arrastando = true;
                        break;

                    case DROPDOWN:
                        for (OpcaoConfig other : lista) {
                            other.aberto = false;
                        }
                        op.aberto = true;
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

                op.valor = (mouseX - barX) / 150f; //Converte posição do mouse em valor (0-1)
                op.valor = Math.max(0f, Math.min(1f, op.valor));
            }

            // dropdown
            if (op.tipo == TipoOpcao.DROPDOWN && op.aberto) {

                for (int j = 0; j < op.opcoes.length; j++) {

                    Rectangle r = new Rectangle(
                        op.area.x,
                        op.area.y - (j + 1) * 50,
                        op.area.width,
                        50
                    );

                    if (Gdx.input.justTouched() && r.contains(mouseX, mouseY)) {
                        op.selecionado = j; //Seleciona opçãpo
                        op.aberto = false;
                    }
                }
            }

            // keybind
            if (op.tipo == TipoOpcao.KEYBIND && op.esperandoTecla) {

                for (int key = 0; key < 256; key++) {
                    if (Gdx.input.isKeyJustPressed(key)) { //Detecta tecla pressionada
                        op.tecla = key;
                        op.esperandoTecla = false;
                    }
                }
            }
        }
    }

    //----------------- DESENHAR CONTEÚDO -----------------
    //Desenha cada tipo - TOGGLE, SLIDER, DROPDOWN, KEYBIND
    private void desenharConteudo(SpriteBatch batch) {

        List<OpcaoConfig> lista = opcoes.get(tipoSelecionado);
        if (lista == null) return;

        atualizarAreasOpcoes(lista);

        for (OpcaoConfig op : lista) {

            desenharTextoCentralizado(fonte, batch, op.texto, op.area);

            switch (op.tipo) {

                case TOGGLE:
                    fonte.draw(batch, op.estado ? "ON" : "OFF",
                        op.area.x + op.area.width - 60,
                        op.area.y + 35);
                    break;

                case SLIDER:
                    fonte.draw(batch, (int)(op.valor*100) + "%",
                        op.area.x + op.area.width - 40,
                        op.area.y + 30);
                    break;

                case DROPDOWN:
                    fonte.draw(batch, op.opcoes[op.selecionado],
                        op.area.x + op.area.width - 150,
                        op.area.y + 35);
                    break;

                case KEYBIND:
                    String txt = op.esperandoTecla ? "..." : Input.Keys.toString(op.tecla);
                    fonte.draw(batch, txt,
                        op.area.x + op.area.width - 150,
                        op.area.y + 30);
                    break;
            }
        }
    }

    //----------------- ATUALIZA ÁREAS -----------------
    private void atualizarAreasOpcoes(List<OpcaoConfig> lista) {


        if (tipoSelecionado == TipoConfig.ATALHOS) {

            int colunas = 2;

            float larguraColuna = 500; // metade (ajusta se quiser)
            float espacamentoX = 40;

            float xBase = areaPopup.x + 80;
            float yBase = areaPopup.y + areaPopup.height - 150;

            for (int i = 0; i < lista.size(); i++) {

                OpcaoConfig op = lista.get(i);

                int coluna = i % colunas;   // 0 ou 1
                int linha = i / colunas;    // 0,1,2...

                float x = xBase + coluna * (larguraColuna + espacamentoX);
                float y = yBase - linha * 80;

                op.area = new Rectangle(x, y, larguraColuna, 50);
            }

        } else {
            // layout normal (1 coluna)

            for (int i = 0; i < lista.size(); i++) {

                OpcaoConfig op = lista.get(i);

                op.area = new Rectangle(
                    areaPopup.x + 80,
                    areaPopup.y + areaPopup.height - 150 - i * 80,
                    1000,
                    50
                );
            }
        }
    }

    //---------------- TEXTO CENTRAL ----------------
    private void desenharTextoCentralizado(BitmapFont font, SpriteBatch batch, String texto, Rectangle botao) {
        GlyphLayout layout = new GlyphLayout(font, texto);
        float x = botao.x + (botao.width - layout.width) / 2;
        float y = botao.y + (botao.height + layout.height) / 2;
        font.draw(batch, layout, x, y);
    }

    //---------------- OPÇÕES ----------------
    private void inicializarOpcoes() {

    //-------------------- ÁUDIO ---------------------------

        //Cria uma lista que guarda todas as opções de AUDIO
        List<OpcaoConfig> audio = new ArrayList<>();

    //-------- VOLUME ---------------------------------

        //Cria uma opção chamada VOLUME
        OpcaoConfig vol = new OpcaoConfig("Volume");
        vol.tipo = TipoOpcao.SLIDER; //Define volume como um slider
        vol.valor = 0.5f; //Valor inciial do slider
        audio.add(vol); //Adiciona o volume na lista

        audio.add(criarToggle("Mute")); //Utiliza método auxiliar que cria um botão que começa desligado

        opcoes.put(TipoConfig.AUDIO, audio); //Salva no map todas as opções de AUDIO

        //-------------------- VIDEO ---------------------------

        //Cria uma lista que guarda todas as opções de VIDEO
        List<OpcaoConfig> video = new ArrayList<>();

    //-------- RESOLUÇÃO ---------------------------------

        //Cria uma opção de Resolução
        OpcaoConfig res = new OpcaoConfig("Resolution");
        res.tipo = TipoOpcao.DROPDOWN; //Define o tipo da opção como DROPDOWN
        res.opcoes = new String[]{"800x600","1280x720","1920x1080"}; //Define as opções dentro do dropdown de resolução
        res.selecionado = 2; //Define a opção selecionada como a terceira opção
        video.add(res); //Adiciona a resolução como uma das opções de vídeo

        OpcaoConfig mode = new OpcaoConfig("Exibition Mode");
        mode.tipo = TipoOpcao.DROPDOWN;
        mode.opcoes = new String[]{"Windowed","Fullscreen"};
        mode.selecionado = 1;
        video.add(mode);

        video.add(criarToggle("Show FPS"));

        OpcaoConfig scale = new OpcaoConfig("Effects Scale");
        scale.tipo = TipoOpcao.SLIDER;
        scale.valor = 1f;
        video.add(scale);

        video.add(criarToggle("Map Effects"));


        opcoes.put(TipoConfig.VIDEO, video); //Salva no map todas as opções de VIDEO

        //-------------------- ATALHOS ---------------------------

        List<OpcaoConfig> atalhos = new ArrayList<>(); //Cria uma lista que guarda todas as opções de ATALHOS

    //-------- PULAR ---------------------------------

    //-------------- JOGABILIDADE --------------
        //Cria uma opção chamada PULAR
        atalhos.add(criarAtalho("Reproduzir/Acelerar", Input.Keys.SPACE));

        atalhos.add(criarAtalho("Pausar/Voltar", Input.Keys.BACKSPACE));

        atalhos.add(criarAtalho("Enviar next wave", Input.Keys.SHIFT_LEFT + Input.Keys.SPACE));

        atalhos.add(criarAtalho("Vender", Input.Keys.BACKSPACE));

        atalhos.add(criarAtalho("Melhorar Herói", Input.Keys.Q));

        //----------- LLAMAS -----------
        atalhos.add(criarAtalho("Llama 1", Input.Keys.NUM_1));

        atalhos.add(criarAtalho("Llama 2", Input.Keys.NUM_2));

        atalhos.add(criarAtalho("Llama 3", Input.Keys.NUM_3));

        atalhos.add(criarAtalho("Llama 4", Input.Keys.NUM_4));

        atalhos.add(criarAtalho("Llama 5", Input.Keys.NUM_5));

        atalhos.add(criarAtalho("Llama 6", Input.Keys.NUM_6));


        opcoes.put(TipoConfig.ATALHOS, atalhos); //Salva no map todas as opções de ATALHOS

        //-------------------- GERAL ---------------------------

        List<OpcaoConfig> geral = new ArrayList<>();

        geral.add(criarToggle("Invert Camera-X-Asis"));
        geral.add(criarToggle("Invert Camera-Y-Asis"));
        geral.add(criarToggle("Invert Mouse-X-Asis"));
        geral.add(criarToggle("Invert Mouse-Y-Asis"));

        //Vincular Steam
        //Vincular E-mail
        //Vincular Discord
        //Vincular Twitch
        //Nome de Exibição
        //UserID
        //Idioma

        opcoes.put(TipoConfig.GERAL, geral);


        //-------------------- ACESSIBILIDADE ---------------------------

        opcoes.put(TipoConfig.ACESSIBILIDADE, new ArrayList<>());
        //Daltonismo/Alto Contraste
    }

    private OpcaoConfig criarToggle(String texto) {
        OpcaoConfig op = new OpcaoConfig(texto);
        op.tipo = TipoOpcao.TOGGLE;
        op.estado = false;
        return op;
    }

    private OpcaoConfig criarAtalho(String texto, int tecla) {
        OpcaoConfig op = new OpcaoConfig(texto);
        op.tipo = TipoOpcao.KEYBIND;
        op.tecla = tecla;
        return op;
    }

    //---------------- LAYOUT ----------------
    public void atualizarLayout() {

        float largura = Gdx.graphics.getWidth();
        float altura = Gdx.graphics.getHeight();

        areaPopup = new Rectangle(largura*0.2f, altura*0.1f, largura*0.6f, altura*0.8f);
        areaBotoes = new Rectangle(0, altura-100, largura, 100);

        opcoesMenu = new Rectangle[textosMenu.length];

        float larguraBotao = 200;
        float espacamento = 20;
        float total = textosMenu.length * larguraBotao + (textosMenu.length - 1)*espacamento;

        float x = (largura-total)/2;
        float y = areaBotoes.y + 20;

        for(int i=0;i<opcoesMenu.length;i++){
            opcoesMenu[i] = new Rectangle(x + i*(larguraBotao+espacamento), y, larguraBotao,60);
        }
    }

    // ENUMS
    public enum TipoConfig {
        GERAL, AUDIO, VIDEO, ATALHOS, ACESSIBILIDADE
    }

    public enum TipoOpcao {
        TOGGLE, SLIDER, DROPDOWN, KEYBIND
    }

    private int getIndice() {
        return tipoSelecionado.ordinal();
    }

    public void toggle() {
        aberto = !aberto;
    }

    public boolean isAberto() {
        return aberto;
    }
}
