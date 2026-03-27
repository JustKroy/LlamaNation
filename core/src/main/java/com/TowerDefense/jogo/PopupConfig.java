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
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont fonte;

    //----------------------- ARRAYS ------------------------
    private Rectangle[] opcoesMenu;
    private Map<TipoConfig, List<OpcaoConfig>> opcoes = new HashMap<>();
    private String[] textosMenu = {"Geral", "Áudio", "Vídeo", "Atalhos", "Acessibilidade"};

    //---------------------- OUTRAS VARIÁVEIS -----------------
    private boolean aberto;
    private Rectangle areaPopup, areaBotoes;
    private TipoConfig tipoSelecionado;

    //------------------ CONSTRUTOR --------------------
    public PopupConfig() {
        shapeRenderer = new ShapeRenderer();
        fonte = new BitmapFont();
        aberto = false;
        tipoSelecionado = TipoConfig.GERAL;

        atualizarLayout();
        inicializarOpcoes();
    }

    //--------------- RENDER DAS OPÇÕES ---------------------
    public void render(SpriteBatch batch) {

        if (!aberto) return;

        for(int i=0;i<textosMenu.length;i++){
            desenharTextoCentralizado(fonte,batch,textosMenu[i],opcoesMenu[i]);
        }

        desenharConteudo(batch);
    }

    public void renderShapes() {

        if (!aberto) return;

        Gdx.gl.glEnable(GL20.GL_BLEND);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Fundo
        shapeRenderer.setColor(0,0,0,0.6f);
        shapeRenderer.rect(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        // Popup
        shapeRenderer.setColor(0.25f,0.25f,0.25f,1f);
        shapeRenderer.rect(areaPopup.x,areaPopup.y,areaPopup.width,areaPopup.height);

        // Barra
        shapeRenderer.setColor(0,0,0.2f,0.8f);
        shapeRenderer.rect(areaBotoes.x,areaBotoes.y,areaBotoes.width,areaBotoes.height);

        // Aba selecionada
        Rectangle sel = opcoesMenu[getIndice()];
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.rect(sel.x,sel.y,sel.width,sel.height);

        shapeRenderer.end();

        // Bordas
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);

        for(Rectangle r : opcoesMenu){
            shapeRenderer.rect(r.x,r.y,r.width,r.height);
        }

        shapeRenderer.end();

        // SLIDERS
        List<OpcaoConfig> lista = opcoes.get(tipoSelecionado);
        if (lista == null) return;

        atualizarAreasOpcoes(lista);

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

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

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

        atualizarAreasOpcoes(lista);

        for (OpcaoConfig op : lista) {

            if (op.area == null) continue;

            // clique
            if (Gdx.input.justTouched() && op.area.contains(mouseX, mouseY)) {

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

    //----------------- DESENHAR CONTEÚDO -----------------
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

    //----------------- ATUALIZA ÁREAS -----------------
    private void atualizarAreasOpcoes(List<OpcaoConfig> lista) {
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

    //---------------- TEXTO CENTRAL ----------------
    private void desenharTextoCentralizado(BitmapFont font, SpriteBatch batch, String texto, Rectangle botao) {
        GlyphLayout layout = new GlyphLayout(font, texto);
        float x = botao.x + (botao.width - layout.width) / 2;
        float y = botao.y + (botao.height + layout.height) / 2;
        font.draw(batch, layout, x, y);
    }

    //---------------- OPÇÕES ----------------
    private void inicializarOpcoes() {

        List<OpcaoConfig> audio = new ArrayList<>();

        OpcaoConfig vol = new OpcaoConfig("Volume");
        vol.tipo = TipoOpcao.SLIDER;
        vol.valor = 0.5f;
        audio.add(vol);

        audio.add(criarToggle("Mute"));

        opcoes.put(TipoConfig.AUDIO, audio);

        List<OpcaoConfig> video = new ArrayList<>();

        OpcaoConfig res = new OpcaoConfig("Resolution");
        res.tipo = TipoOpcao.DROPDOWN;
        res.opcoes = new String[]{"800x600","1280x720","1920x1080"};
        res.selecionado = 2;

        video.add(res);

        opcoes.put(TipoConfig.VIDEO, video);

        List<OpcaoConfig> atalhos = new ArrayList<>();

        OpcaoConfig key = new OpcaoConfig("Pular");
        key.tipo = TipoOpcao.KEYBIND;
        key.tecla = Input.Keys.SPACE;

        atalhos.add(key);

        opcoes.put(TipoConfig.ATALHOS, atalhos);

        opcoes.put(TipoConfig.GERAL, new ArrayList<>());
        opcoes.put(TipoConfig.ACESSIBILIDADE, new ArrayList<>());
    }

    private OpcaoConfig criarToggle(String texto) {
        OpcaoConfig op = new OpcaoConfig(texto);
        op.tipo = TipoOpcao.TOGGLE;
        op.estado = false;
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
