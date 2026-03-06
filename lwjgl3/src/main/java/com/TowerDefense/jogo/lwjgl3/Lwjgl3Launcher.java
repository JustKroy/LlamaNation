package com.TowerDefense.jogo.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.TowerDefense.jogo.Main;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired())
            return; // This handles macOS support and helps on Windows.
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new Main(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("Meu Tower Defense");
        configuration.useVsync(true);
        configuration.setForegroundFPS(60);

        // ATIVA O MODO TELA CHEIA (Pega a resolução atual do seu monitor)
        configuration.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());

        return configuration;
    }
}
