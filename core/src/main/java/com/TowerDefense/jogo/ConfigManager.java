package com.TowerDefense.jogo;

import com.badlogic.gdx.Gdx;
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
    public static void carregar() {
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
    public static void aplicarVideo() {
        if (isFullscreen) {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        } else {
            String[] res = resolucao.split("x");
            if (res.length == 2) {
                int w = Integer.parseInt(res[0]);
                int h = Integer.parseInt(res[1]);
                Gdx.graphics.setWindowedMode(w, h);
            }
        }
    }

    // 4. Aplica as configurações de ÁUDIO
    public static void aplicarAudio() {
        // Como o volume é de 0.0 a 1.0, se estiver mutado, passamos 0.
        float volumeReal = mute ? 0f : volume;

        // TODO: Conecte isso ao seu gerenciador de som. Exemplo:
        // MeuGerenciadorDeAudio.setVolumeMusica(volumeReal);
        // MeuGerenciadorDeAudio.setVolumeEfeitos(volumeReal);

        System.out.println("Áudio aplicado: Volume = " + (volumeReal * 100) + "%");
    }

    // 5. Aplica as configurações GERAIS
    public static void aplicarGeral() {
        // As lógicas de inversão (invertCameraX, invertMouseX) geralmente
        // são lidas diretamente pela sua classe de Input ou Câmera em tempo real.
        // Mas se precisar reiniciar alguma variável do jogador, faça aqui:

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





    // 6. Atalho para aplicar TUDO quando clicar no botão SALVAR
    public static void aplicarTudo() {
        aplicarVideo();
        aplicarAudio();
        aplicarGeral();
    }
}
