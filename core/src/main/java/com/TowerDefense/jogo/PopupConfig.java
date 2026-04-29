package com.TowerDefense.jogo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.*;

public class PopupConfig {

    private final Main game;
    private final ShapeRenderer shapeRenderer;
    private final Color COR_BOTAO = new Color(0.2f, 0.7f, 0.3f, 1f);
    private final Color COR_BOTAO_HOVER = new Color(0.3f, 0.8f, 0.4f, 1f);
    private Rectangle botaoSalvar, botaoExit;

    private Rectangle[] opcoesMenu;
    private Map<TipoConfig, List<OpcaoConfig>> opcoes = new HashMap<>();
    private String[] textosMenu = {"Geral", "Áudio", "Vídeo", "Atalhos", "Acessibilidade"};
    private Texture[] imagensMenu;
    private float[] escalaMenu;
    private boolean aberto;
    private Rectangle areaPopup;
    private TipoConfig tipoSelecionado;
    private final Color COR_FUNDO = new Color(0.1f, 0.1f, 0.1f, 0.9f);
    private final Color COR_PRIMARIA = new Color(0.2f, 0.6f, 1f, 1f);
    private final Color COR_HOVER = new Color(1,1,1,0.05f);
    BitmapFont fonteTitulo;
    BitmapFont fonteNormal;
    BitmapFont fonte;
    private Vector2 mouse = new Vector2();
    private StretchViewport viewport;
    private static Texture[] texturasCompartilhadas;

    public PopupConfig(Main game) {
        this.game = game;
        shapeRenderer = new ShapeRenderer();
        aberto = false;
        viewport = new StretchViewport(1920, 1080);

        this.fonteTitulo = game.fonteTitulo;
        this.fonteNormal = game.fonteNormal;
        this.fonte = game.fonte;

        opcoesMenu = new Rectangle[textosMenu.length];
        escalaMenu = new float[textosMenu.length];
        Arrays.fill(escalaMenu, 1f);

        if (texturasCompartilhadas == null) {
            texturasCompartilhadas = new Texture[11];
            texturasCompartilhadas[0] = new Texture("Settings_Button.png");
            texturasCompartilhadas[1] = new Texture("Settings_Audio.png");
            texturasCompartilhadas[2] = new Texture("Settings_Video.png");
            texturasCompartilhadas[3] = new Texture("Settings_Controls.png");
            texturasCompartilhadas[4] = new Texture("Settings_Acessibility.png");
            texturasCompartilhadas[5] = new Texture("ui/On_ButtonWBall.png");
            texturasCompartilhadas[6] = new Texture("ui/Off_ButtonWBall.png");
            texturasCompartilhadas[7] = new Texture("Button_Save.png");
            texturasCompartilhadas[8] = new Texture("Button_SaveHover.png");
            texturasCompartilhadas[9] = new Texture("Button_ExitGame.png");
            texturasCompartilhadas[10] = new Texture("Button_ExitGameHover.png");
        }
        this.imagensMenu = texturasCompartilhadas;

        tipoSelecionado = TipoConfig.GERAL;

        atualizarLayout();
        inicializarOpcoes();
    }

    // Sincroniza a câmera com o ShapeRenderer interno
    public void setProjectionMatrix(Matrix4 matrix) {
        shapeRenderer.setProjectionMatrix(matrix);
    }

    public void render(SpriteBatch batch, float mouseY, float mouseX) {
        if (!aberto) return;

        for(int i=0;i<opcoesMenu.length;i++){
            Rectangle r = opcoesMenu[i];
            boolean hover = r.contains(mouseX, mouseY);
            float alvo = (i == getIndice()) ? 1.15f : (hover ? 1.1f : 1f);
            escalaMenu[i] += (alvo - escalaMenu[i]) * 0.15f;
        }

        for(int i=0;i<opcoesMenu.length;i++){
            Rectangle r = opcoesMenu[i];
            float escala = escalaMenu[i];
            float width = r.width * escala;
            float height = r.height * escala;
            float x = r.x - (width - r.width) / 2;
            float y = r.y - (height - r.height) / 2;

            batch.setColor(1f, 1f, 1f, (i == getIndice()) ? 1f : 0.7f);
            batch.draw(imagensMenu[i], x, y, width, height);
        }
        batch.setColor(Color.WHITE);
        desenharConteudo(batch);

        // --- DESENHO DO BOTÃO SALVAR ---
        boolean hoverSalvar = botaoSalvar.contains(mouseX, mouseY);
        Texture texSalvar = hoverSalvar ? imagensMenu[8] : imagensMenu[7];

        float escalaSalvar = hoverSalvar ? 1.05f : 1.0f;
        float wS = botaoSalvar.width * escalaSalvar;
        float hS = botaoSalvar.height * escalaSalvar;
        float xS = botaoSalvar.x - (wS - botaoSalvar.width) / 2;
        float yS = botaoSalvar.y - (hS - botaoSalvar.height) / 2;

        batch.draw(texSalvar, xS, yS, wS, hS);

        // --- DESENHO DO BOTÃO EXIT ---
        boolean hoverExit = botaoExit.contains(mouseX, mouseY);
        Texture texExit = hoverExit ? imagensMenu[9] : imagensMenu[10];

        // CORREÇÃO AQUI: Criamos variáveis exclusivas para a escala do Exit
        float escalaExit = hoverExit ? 1.05f : 1.0f;
        float wE = botaoExit.width * escalaExit;
        float hE = botaoExit.height * escalaExit;
        float xE = botaoExit.x - (wE - botaoExit.width) / 2;
        float yE = botaoExit.y - (hE - botaoExit.height) / 2;

        batch.draw(texExit, xE, yE, wE, hE);
    }

    public void renderShapes(float mouseX, float mouseY) {
        if (!aberto) return;

        float LARGURA_VIRTUAL = 1920;
        float ALTURA_VIRTUAL = 1080;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(0, 0, 0, 0.6f);
        shapeRenderer.rect(0, 0, LARGURA_VIRTUAL, ALTURA_VIRTUAL);

        shapeRenderer.setColor(0,0,0,0.3f);
        shapeRenderer.rect(areaPopup.x + 8, areaPopup.y - 8, areaPopup.width, areaPopup.height);

        shapeRenderer.setColor(COR_FUNDO);
        shapeRenderer.rect(areaPopup.x,areaPopup.y,areaPopup.width,areaPopup.height);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);

        for (int i = 0; i < opcoesMenu.length; i++) {
            Rectangle r = opcoesMenu[i];
            boolean hover = r.contains(mouseX, mouseY);
            if (i == getIndice()) shapeRenderer.setColor(0.4f, 0.4f, 0.4f, 1f);
            else if (hover) {
                shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 1f);
            } else shapeRenderer.setColor(0.2f, 0.2f, 0.2f, 1f);
        }

        boolean hoverSalvar = botaoSalvar.contains(mouseX, mouseY);
        if (hoverSalvar) {
            shapeRenderer.setColor(COR_BOTAO_HOVER);
        } else {
            shapeRenderer.setColor(COR_BOTAO);
        }

        boolean hoverExit = botaoExit.contains(mouseX, mouseY);
        if(hoverExit) {
            shapeRenderer.setColor(COR_BOTAO_HOVER);
        } else {
            shapeRenderer.setColor(COR_BOTAO);
        }
        shapeRenderer.end();

        List<OpcaoConfig> lista = opcoes.get(tipoSelecionado);
        if (lista == null) {
            Gdx.gl.glDisable(GL20.GL_BLEND);
            return;
        }

        atualizarAreasOpcoes(lista);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (OpcaoConfig op : lista) {
            if (op.area == null) continue;

            boolean hover = op.area.contains(mouseX, mouseY);
            if (hover && op.tipo != TipoOpcao.DROPDOWN) {
                shapeRenderer.setColor(COR_HOVER);
                shapeRenderer.rect(op.area.x, op.area.y, op.area.width, op.area.height);
            }

            float paddingRight = 20;

            if (op.tipo == TipoOpcao.SLIDER) {
                float barWidth = 150;
                float barX = op.area.x + op.area.width - barWidth - paddingRight;
                float barY = op.area.y + 16;

                shapeRenderer.setColor(0.2f, 0.2f, 0.2f, 1);
                shapeRenderer.rect(barX, barY, barWidth, 8);

                shapeRenderer.setColor(COR_PRIMARIA);
                shapeRenderer.rect(barX, barY, barWidth * op.valor, 8);

                float knobX = barX + op.valor * barWidth;
                shapeRenderer.setColor(Color.WHITE);
                shapeRenderer.circle(knobX, barY + 4, 8);
            }


        }
        renderDropdownFundo(shapeRenderer, mouseX, mouseY);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public boolean estaSobreElementoInterativo(Vector2 mouse) {
        if (!aberto) return false;

        // Botões do topo
        for (Rectangle r : opcoesMenu) {
            if (r.contains(mouse.x, mouse.y)) return true;
        }

        // Botão salvar
        if (botaoSalvar.contains(mouse.x, mouse.y)) return true;
        if (botaoExit.contains(mouse.x, mouse.y)) return true;

        // Botões de opções (slider, toggle, etc)

        // Opções (slider, toggle, etc)
        List<OpcaoConfig> lista = opcoes.get(tipoSelecionado);
        if (lista != null) {
            for (OpcaoConfig op : lista) {
                if (op.area != null && op.area.contains(mouse.x, mouse.y)) return true;

                // Dropdown aberto
                if (op.tipo == TipoOpcao.DROPDOWN && op.aberto) {
                    float dropdownWidth = 200;

                    for (int j = 0; j < op.opcoes.length; j++) {
                        float x = op.area.x + op.area.width - dropdownWidth - 20;
                        float y = op.area.y - (j + 1) * 50;

                        Rectangle r = new Rectangle(x, y, dropdownWidth, 50);
                        if (r.contains(mouse.x, mouse.y)) return true;
                    }
                }
            }
        }

        return false;
    }

    // Desenha APENAS a caixa/fundo do dropdown que estiver aberto
    public void renderDropdownFundo(ShapeRenderer shape, float mouseX, float mouseY) {
        if (!aberto) return;
        List<OpcaoConfig> lista = opcoes.get(tipoSelecionado);
        if (lista == null) return;

        OpcaoConfig dropdownAberto = null;
        for (OpcaoConfig op : lista) {
            if (op.tipo == TipoOpcao.DROPDOWN && op.aberto) {
                dropdownAberto = op;
                break;
            }
        }

        if (dropdownAberto != null) {
            for (int j = 0; j < dropdownAberto.opcoes.length; j++) {
                float dropdownWidth = 200;
                float dropdownX = dropdownAberto.area.x + dropdownAberto.area.width - dropdownWidth - 20;
                float y = dropdownAberto.area.y - (j + 1) * 50;

                // Aqui desenhamos o retângulo escuro da lista suspensa
                shape.setColor(0.15f, 0.15f, 0.15f, 1f);
                shape.rect(dropdownX, y, dropdownWidth, 50);
            }
        }
    }

    // Desenha APENAS os textos do dropdown que estiver aberto
    public void renderDropdownTextos(SpriteBatch batch, float mouseX, float mouseY) {
        if (!aberto) return;
        List<OpcaoConfig> lista = opcoes.get(tipoSelecionado);
        if (lista == null) return;

        OpcaoConfig dropdownAberto = null;
        for (OpcaoConfig op : lista) {
            if (op.tipo == TipoOpcao.DROPDOWN && op.aberto) {
                dropdownAberto = op;
                break;
            }
        }

        if (dropdownAberto != null) {
            float paddingRight = 20;
            for (int j = 0; j < dropdownAberto.opcoes.length; j++) {
                String textoOpcao = dropdownAberto.opcoes[j];
                float dropdownWidth = 200;
                float dropdownX = dropdownAberto.area.x + dropdownAberto.area.width - dropdownWidth - paddingRight;
                float y = dropdownAberto.area.y - (j + 1) * 50;

                Rectangle optionRect = new Rectangle(dropdownX, y, dropdownWidth, 50);

                // Força a cor do texto do dropdown aberto para branco para destaque
                fonteNormal.setColor(Color.WHITE);
                desenharTextoDireitaCentro(fonteNormal, batch, textoOpcao, optionRect, 10);
            }
        }
    }

    public boolean estaSobre(Vector2 mouse) {
        if (!aberto) return false;

        // Área principal do popup
        if (areaPopup.contains(mouse.x, mouse.y)) return true;

        // Botões do topo (menu)
        for (Rectangle r : opcoesMenu) {
            if (r.contains(mouse.x, mouse.y)) return true;
        }

        // Botão salvar
        if (botaoSalvar.contains(mouse.x, mouse.y)) return true;
        if (botaoExit.contains(mouse.x, mouse.y)) return true;


        // Opções (slider, toggle, etc)
        List<OpcaoConfig> lista = opcoes.get(tipoSelecionado);
        if (lista != null) {
            for (OpcaoConfig op : lista) {
                if (op.area != null && op.area.contains(mouse.x, mouse.y)) return true;

                // Dropdown aberto
                if (op.tipo == TipoOpcao.DROPDOWN && op.aberto) {
                    float dropdownWidth = 200;

                    for (int j = 0; j < op.opcoes.length; j++) {
                        float x = op.area.x + op.area.width - dropdownWidth - 20;
                        float y = op.area.y - (j + 1) * 50;

                        Rectangle r = new Rectangle(x, y, dropdownWidth, 50);

                        if (r.contains(mouse.x, mouse.y)) return true;
                    }
                }
            }
        }

        return false;
    }

    public void handleInput(float mouseX, float mouseY) {
        if (!aberto) return;

        this.mouse.set(mouseX, mouseY);

        // CLIQUE NO BOTÃO SALVAR
        if (Gdx.input.justTouched() && botaoSalvar.contains(mouseX, mouseY)) {
            ConfigManager.salvar();
            ConfigManager.aplicarTudo();
            atualizarLayout();
            System.out.println("Configurações salvas e aplicadas com sucesso!");
            toggle();
            return;
        }

        if (Gdx.input.justTouched() && botaoExit.contains(mouseX, mouseY)) {
            Gdx.app.exit();
        }

        if (Gdx.input.justTouched()) {
            for (int i = 0; i < opcoesMenu.length; i++) {
                if (opcoesMenu[i].contains(mouseX, mouseY)) {
                    tipoSelecionado = TipoConfig.values()[i];
                }
            }
        }

        OpcaoConfig dropdownAberto = null;
        List<OpcaoConfig> lista = opcoes.get(tipoSelecionado);
        if (lista == null) return;
        for (OpcaoConfig op : lista) {
            if (op.tipo == TipoOpcao.DROPDOWN && op.aberto) {
                dropdownAberto = op;
                break;
            }
        }

        atualizarAreasOpcoes(lista);

        if (dropdownAberto != null) {
            processarCliqueDropdown(dropdownAberto, mouseX, mouseY);
            return;
        }

        for (OpcaoConfig op : lista) {
            if (op.area == null) continue;

            if (Gdx.input.justTouched() && op.area.contains(mouseX, mouseY)) {
                switch (op.tipo) {
                    case TOGGLE:
                        op.estado = !op.estado;
                        if (op.texto.equalsIgnoreCase("Mute")) ConfigManager.mute = op.estado;
                        if (op.texto.equalsIgnoreCase("Show FPS")) ConfigManager.showFps = op.estado;
                        if (op.texto.equalsIgnoreCase("Map Effects")) ConfigManager.mapEffects = op.estado;
                        // Sincroniza IMEDIATAMENTE com o ConfigManager para teste em tempo real
                        if (op.texto.equals("Invert Camera-X-Axis")) ConfigManager.invertCameraX = op.estado;
                        if (op.texto.equals("Invert Camera-Y-Axis")) ConfigManager.invertCameraY = op.estado;
                        if (op.texto.equals("Invert Mouse-X-Axis")) ConfigManager.invertMouseX = op.estado;
                        if (op.texto.equals("Invert Mouse-Y-Axis")) ConfigManager.invertMouseY = op.estado;
                        break;
                    case SLIDER:
                        op.arrastando = true;
                        break;
                    case DROPDOWN:
                        for (OpcaoConfig other : lista) other.aberto = false;
                        op.aberto = true;
                        break;
                    case KEYBIND:
                        op.esperandoTecla = true;
                        break;
                }
            }

            if (!Gdx.input.isTouched() && op.arrastando) {
                op.arrastando = false;
                if (op.texto.equalsIgnoreCase("Volume")) ConfigManager.volume = op.valor;
            }

            if (op.tipo == TipoOpcao.SLIDER && op.arrastando) {
                float barWidth = 150;
                float paddingRight = 20;
                float barX = op.area.x + op.area.width - barWidth - paddingRight;
                op.valor = (mouseX - barX) / barWidth;
                op.valor = Math.max(0f, Math.min(1f, op.valor));
            }

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

    private void processarCliqueDropdown(OpcaoConfig op, float mx, float my) {
        if (Gdx.input.justTouched()) {
            float dropdownWidth = 200;
            float dx = op.area.x + op.area.width - dropdownWidth - 20;

            boolean clicouEmAlgo = false;
            for (int j = 0; j < op.opcoes.length; j++) {
                Rectangle r = new Rectangle(dx, op.area.y - (j + 1) * 50, dropdownWidth, 50);
                if (r.contains(mx, my)) {
                    op.selecionado = j;
                    op.aberto = false;
                    clicouEmAlgo = true;
                    // Sincroniza com ConfigManager
                    if (op.texto.contains("Resolution")) ConfigManager.resolucao = op.opcoes[j];
                    if (op.texto.contains("Mode")) ConfigManager.isFullscreen = (j == 1);
                }
            }
            if (!clicouEmAlgo) op.aberto = false; // Fecha se clicar fora
        }
    }

    private void desenharConteudo(SpriteBatch batch) {
        List<OpcaoConfig> lista = opcoes.get(tipoSelecionado);
        if (lista == null) return;

        float paddingRight = 20;

        for (OpcaoConfig op : lista) {
            fonte.setColor(Color.WHITE);
            float textY = op.area.y + (op.area.height + fonteNormal.getLineHeight()) / 2;
            fonteNormal.draw(batch, op.texto, op.area.x, textY);

            fonte.setColor(Color.LIGHT_GRAY);

            switch (op.tipo) {
                case TOGGLE:
                    float w = 90;
                    float h = 40;
                    float x = op.area.x + op.area.width - w - 20;
                    float y = op.area.y;

                    Rectangle areaToggle = new Rectangle(x, y, w, h);
                    boolean hover = areaToggle.contains(mouse.x, mouse.y);

                    Texture tex = op.estado ? imagensMenu[5] : imagensMenu[6];

                    // efeito de brilho no hover
                    if (hover) {
                        batch.setColor(1f, 1f, 1f, 1f); // normal
                    } else {
                        batch.setColor(1f, 1f, 1f, 0.8f); // levemente apagado
                    }

                    float scale = hover ? 1.1f : 1f;

                    float width = w * scale;
                    float height = h * scale;

                    float drawX = x - (width - w) / 2;
                    float drawY = y - (height - h) / 2;

                    batch.draw(tex, drawX, drawY, width, height);
                    batch.setColor(Color.WHITE);

                    break;
                case SLIDER:
                    desenharTextoDireitaCentro(fonteNormal, batch, (int)(op.valor * 100) + "%", op.area, paddingRight + 170);
                    break;
                case DROPDOWN:
                    desenharTextoDireitaCentro(fonteNormal, batch, op.opcoes[op.selecionado], op.area, paddingRight);
                    break;
                case KEYBIND:
                    String txt = op.esperandoTecla ? "..." : Input.Keys.toString(op.tecla);
                    desenharTextoDireitaCentro(fonteNormal, batch, txt, op.area, paddingRight);
                    break;
            }
        }
    }

    private void desenharTextoDireitaCentro(BitmapFont font, SpriteBatch batch, String texto, Rectangle area, float paddingRight) {
        GlyphLayout layout = new GlyphLayout(font, texto);
        float x = area.x + area.width - layout.width - paddingRight;
        float y = area.y + (area.height + layout.height) / 2;
        font.draw(batch, layout, x, y);
    }

    private void atualizarAreasOpcoes(List<OpcaoConfig> lista) {
        float margemLateral = 100;
        float offsetTopo = 220;
        float alturaLinha = 60;

        if (tipoSelecionado == TipoConfig.ATALHOS) {
            int colunas = 2;
            float espacamentoX = 80;
            float larguraColuna = (areaPopup.width - (margemLateral * 2) - espacamentoX) / 2;
            float xBase = areaPopup.x + margemLateral;
            float yBase = areaPopup.y + areaPopup.height - offsetTopo;

            for (int i = 0; i < lista.size(); i++) {
                OpcaoConfig op = lista.get(i);
                int coluna = i % colunas;
                int linha = i / colunas;

                op.area = new Rectangle(
                    xBase + coluna * (larguraColuna + espacamentoX),
                    yBase - (linha * alturaLinha),
                    larguraColuna,
                    40
                );
            }
        } else {
            for (int i = 0; i < lista.size(); i++) {
                OpcaoConfig op = lista.get(i);
                op.area = new Rectangle(
                    areaPopup.x + margemLateral,
                    areaPopup.y + areaPopup.height - offsetTopo - (i * alturaLinha),
                    areaPopup.width - (margemLateral * 2),
                    40
                );
            }
        }
    }

    private void desenharTextoCentralizado(BitmapFont font, SpriteBatch batch, String texto, Rectangle botao) {
        GlyphLayout layout = new GlyphLayout(font, texto);
        float x = botao.x + (botao.width - layout.width) / 2;
        float y = botao.y + (botao.height + layout.height) / 2;
        font.draw(batch, layout, x, y);
    }

    private void inicializarOpcoes() {
        // ÁUDIO
        List<OpcaoConfig> audio = new ArrayList<>();
        OpcaoConfig vol = new OpcaoConfig("Volume");
        vol.tipo = TipoOpcao.SLIDER;
        vol.valor = ConfigManager.volume;
        audio.add(vol);

        OpcaoConfig mute = criarToggle("Mute");
        mute.estado = ConfigManager.mute;
        audio.add(mute);
        opcoes.put(TipoConfig.AUDIO, audio);

        // VÍDEO
        List<OpcaoConfig> video = new ArrayList<>();
        OpcaoConfig res = new OpcaoConfig("Resolution");
        res.tipo = TipoOpcao.DROPDOWN;
        res.opcoes = new String[]{"800x600", "1280x720", "1920x1080"};
        res.selecionado = 1;
        for (int i = 0; i < res.opcoes.length; i++) {
            if (res.opcoes[i].equals(ConfigManager.resolucao)) {
                res.selecionado = i;
                break;
            }
        }
        video.add(res);

        OpcaoConfig mode = new OpcaoConfig("Exibition Mode");
        mode.tipo = TipoOpcao.DROPDOWN;
        mode.opcoes = new String[]{"Windowed", "Fullscreen"};
        mode.selecionado = ConfigManager.isFullscreen ? 1 : 0;
        video.add(mode);

        OpcaoConfig fps = criarToggle("Show FPS");
        fps.estado = ConfigManager.showFps;
        video.add(fps);

        OpcaoConfig scale = new OpcaoConfig("Effects Scale");
        scale.tipo = TipoOpcao.SLIDER;
        scale.valor = ConfigManager.effectsScale;
        video.add(scale);

        OpcaoConfig mapEff = criarToggle("Map Effects");
        mapEff.estado = ConfigManager.mapEffects;
        video.add(mapEff);
        opcoes.put(TipoConfig.VIDEO, video);

        // GERAL
        List<OpcaoConfig> geral = new ArrayList<>();

        OpcaoConfig invCamX = criarToggle("Invert Camera-X-Asis");
        invCamX.estado = ConfigManager.invertCameraX; // Puxa do ConfigManager
        geral.add(invCamX);

        OpcaoConfig invCamY = criarToggle("Invert Camera-Y-Asis");
        invCamY.estado = ConfigManager.invertCameraY;
        geral.add(invCamY);

        OpcaoConfig invMouseX = criarToggle("Invert Mouse-X-Asis");
        invMouseX.estado = ConfigManager.invertMouseX;
        geral.add(invMouseX);

        OpcaoConfig invMouseY = criarToggle("Invert Mouse-Y-Asis");
        invMouseY.estado = ConfigManager.invertMouseY;
        geral.add(invMouseY);

        opcoes.put(TipoConfig.GERAL, geral);

        // ATALHOS
        List<OpcaoConfig> atalhos = new ArrayList<>();
        atalhos.add(criarAtalho("Reproduzir/Acelerar", Input.Keys.SPACE));
        atalhos.add(criarAtalho("Pausar/Voltar", Input.Keys.BACKSPACE));
        atalhos.add(criarAtalho("Enviar next wave", Input.Keys.SHIFT_LEFT + Input.Keys.SPACE)); // Isso pode ser bugado de ler assim, recomendo arrumar
        atalhos.add(criarAtalho("Vender", Input.Keys.BACKSPACE));
        atalhos.add(criarAtalho("Melhorar Herói", Input.Keys.Q));
        atalhos.add(criarAtalho("Llama 1", Input.Keys.NUM_1));
        atalhos.add(criarAtalho("Llama 2", Input.Keys.NUM_2));
        atalhos.add(criarAtalho("Llama 3", Input.Keys.NUM_3));
        atalhos.add(criarAtalho("Llama 4", Input.Keys.NUM_4));
        atalhos.add(criarAtalho("Llama 5", Input.Keys.NUM_5));
        atalhos.add(criarAtalho("Llama 6", Input.Keys.NUM_6));
        opcoes.put(TipoConfig.ATALHOS, atalhos);

        // ACESSIBILIDADE
        opcoes.put(TipoConfig.ACESSIBILIDADE, new ArrayList<>());
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

    public void atualizarLayout() {
        float L_VIRTUAL = 1920;
        float A_VIRTUAL = 1080;

        float larguraPopup = 1200;
        float alturaPopup = 800;

        areaPopup = new Rectangle(
            (L_VIRTUAL - larguraPopup) / 2,
            (A_VIRTUAL - alturaPopup) / 2,
            larguraPopup,
            alturaPopup
        );

        float larguraBotao = 80;
        float espacamento = 40;
        float larguraTotalBotoes = textosMenu.length * larguraBotao + (textosMenu.length - 1) * espacamento;
        float startX = areaPopup.x + (areaPopup.width - larguraTotalBotoes) / 2;

        for(int i = 0; i < opcoesMenu.length; i++) {
            opcoesMenu[i] = new Rectangle(
                startX + i * (larguraBotao + espacamento),
                areaPopup.y + areaPopup.height - 130,
                larguraBotao,
                80
            );
        }

        float larguraSalvar = 300;
        float alturaSalvar = 80;
        botaoSalvar = new Rectangle(
            areaPopup.x + (areaPopup.width - larguraSalvar) / 3f,
            areaPopup.y + 40,
            larguraSalvar,
            alturaSalvar
        );

        float larguraExit = 300;
        float alturaExit = 80;
        botaoExit = new Rectangle(
            areaPopup.x + (areaPopup.width - larguraExit) / 1.4f,
            areaPopup.y + 40,
            larguraExit,
            alturaExit
        );
    }

    public enum TipoConfig { GERAL, AUDIO, VIDEO, ATALHOS, ACESSIBILIDADE }
    public enum TipoOpcao { TOGGLE, SLIDER, DROPDOWN, KEYBIND }

    private int getIndice() { return tipoSelecionado.ordinal(); }
    public void toggle() { aberto = !aberto; }
    public boolean isAberto() { return aberto; }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
