package com.TowerDefense.jogo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Preferences;

public class ConfigManager {

    private static Preferences prefs;

    // --- VARIÁVEIS DE ÁUDIO ---
    public static float volume;
    public static boolean mute;

    // --- VARIÁVEIS DE VÍDEO ---
    public static boolean isFullscreen;
    public static String resolucao;
    public static boolean showFps;
    public static float effectsScale;
    public static boolean mapEffects;

    // --- VARIÁVEIS GERAIS (INPUT/CÂMERA) ---
    public static boolean invertCameraX;
    public static boolean invertCameraY;
    public static boolean invertMouseX;
    public static boolean invertMouseY;

    // 1. Inicializa e carrega os dados salvos
    public static void construtor() {
        prefs = Gdx.app.getPreferences("TowerDefenseConfigs");

        volume = prefs.getFloat("volume", 0.5f);
        mute = prefs.getBoolean("mute", false);

        isFullscreen = prefs.getBoolean("isFullscreen", false);
        resolucao = prefs.getString("resolucao", "1280x720");
        showFps = prefs.getBoolean("showFps", false);
        effectsScale = prefs.getFloat("effectsScale", 1.0f);
        mapEffects = prefs.getBoolean("mapEffects", true);

        invertCameraX = prefs.getBoolean("invertCameraX", false);
        invertCameraY = prefs.getBoolean("invertCameraY", false);
        invertMouseX = prefs.getBoolean("invertMouseX", false);
        invertMouseY = prefs.getBoolean("invertMouseY", false);

        Graphics.DisplayMode[] modes = Gdx.graphics.getDisplayModes();
    }

    // 2. Salva os dados no disco
    public static void salvar() {
        if (prefs == null) {
            Gdx.app.error("CONFIG", "Tentativa de salvar sem carregar o ConfigManager!");
            return;
        }
        prefs.putFloat("volume", volume);
        prefs.putBoolean("mute", mute);

        prefs.putBoolean("isFullscreen", isFullscreen);
        prefs.putString("resolucao", resolucao);
        prefs.putBoolean("showFps", showFps);
        prefs.putFloat("effectsScale", effectsScale);
        prefs.putBoolean("mapEffects", mapEffects);

        prefs.putBoolean("invertCameraX", invertCameraX);
        prefs.putBoolean("invertCameraY", invertCameraY);
        prefs.putBoolean("invertMouseX", invertMouseX);
        prefs.putBoolean("invertMouseY", invertMouseY);

        prefs.flush(); // OBRIGATÓRIO: Grava as mudanças no arquivo físico
    }

    // 3. Aplica as configurações de VÍDEO
    public static void aplicarResolucao(int largura, int altura, boolean isFullscreen) {
        if (isFullscreen) {
            // TELA CHEIA SEM BORDAS (ALT+TAB INSTANTÂNEO)
            Graphics.DisplayMode modoAtual = Gdx.graphics.getDisplayMode();
            Gdx.graphics.setUndecorated(true); // Remove a barra superior do Windows

            // CORREÇÃO: Usar .width e .height do LibGDX
            Gdx.graphics.setWindowedMode(modoAtual.width, modoAtual.height);
        } else {
            // MODO JANELA NORMAL
            Gdx.graphics.setUndecorated(false); // Devolve a barra superior

            // CORREÇÃO: Como "largura" e "altura" já chegaram prontos pelos
            // parâmetros do método, é só aplicar direto!
            Gdx.graphics.setWindowedMode(largura, altura);
        }
    }

    // 4. Aplica as configurações de ÁUDIO
    public static void aplicarAudio() {
        // Como o volume é de 0.0 a 1.0, se estiver mutado, passamos 0.
        float volumeReal = mute ? 0f : volume;

        System.out.println("Áudio aplicado: Volume = " + (volumeReal * 100) + "%");
    }

    // 5. Aplica as configurações GERAIS
    public static void aplicarGeral() {

        System.out.println("Configurações Gerais (Inversões) atualizadas na memória!");
    }


    //============================================
    // ------------- EIXO DO MOUSE ---------------
    //============================================

    // * Processa a coordenada X do mouse/entrada.
    // * Se invertMouseX for true, ele inverte o valor baseado na largura da tela.
    public static float processarMouseX(float x) {
        if (invertMouseX) {
            return 1920 - x;
        }
        return x;
    }

    // * Processa a coordenada Y do mouse/entrada.
    // * Se invertMouseY for true, ele inverte o valor baseado na altura da tela.
    public static float processarMouseY(float y) {
        if (invertMouseY) {
            return 1080 - y;
        }
        return y;
    }

    // * Processa o delta (movimento relativo) do mouse.
    // * Útil para movimentação de câmera.
    public static float filtrarMovimentoX(float deltaX) {
        return invertCameraX ? -deltaX : deltaX;
    }

    public static float filtrarMovimentoY(float deltaY) {
        return invertCameraY ? -deltaY : deltaY;
    }

    //============================================
    // ------------- EIXO DA CAMERA ---------------
    //============================================


     // * Retorna o multiplicador de direção para a câmera.
     // * Útil para: camera.position.x += (movimento * getDirecaoCameraX())
    public static float getDirecaoCameraX() {
        return invertCameraX ? -1f : 1f;
    }

    public static float getDirecaoCameraY() {
        return invertCameraY ? -1f : 1f;
    }

     // * Aplica a inversão diretamente em um vetor de movimento da câmera.
     // * Útil se você estiver usando Vector3 para mover a câmera.
    public static void processarMovimentoCamera(com.badlogic.gdx.math.Vector3 movimento) {
        if (invertCameraX) movimento.x *= -1;
        if (invertCameraY) movimento.y *= -1;
    }


    //============================================
    // ------------- APLICAR TUDO ----------------
    //============================================

    public static void aplicarTudo() {
        // 1. APLICAR VÍDEO
        try {
            // Pega a string "1920x1080", divide no "x" e transforma em números
            String[] dimensoes = resolucao.split("x");
            int w = Integer.parseInt(dimensoes[0]);
            int h = Integer.parseInt(dimensoes[1]);

            aplicarResolucao(w, h, isFullscreen);
        } catch (Exception e) {
            Gdx.app.error("CONFIG", "Erro ao tentar aplicar resolução: " + resolucao);
        }

        // 2. APLICAR ÁUDIO
        aplicarAudio();

        // 3. APLICAR GERAL
        aplicarGeral();
    }
}
