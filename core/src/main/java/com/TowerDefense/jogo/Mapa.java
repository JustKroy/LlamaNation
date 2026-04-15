package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Mapa {
    public Array<Vector2> caminho = new Array<>();
    public Array<Rectangle> hitboxesCaminho = new Array<>();
    private Texture imgChao, imgCasa, imgFundo;

    public Mapa() {
        imgChao = new Texture("Point.png");
        imgCasa = new Texture("Chegada.png");
        imgFundo = new Texture("Mapa1.png");

        // --- OS WAYPOINTS (Onde o caracol anda) ---
        caminho.add(new Vector2(-100, 75));
        caminho.add(new Vector2(598, 75));
        caminho.add(new Vector2(598, 510));
        caminho.add(new Vector2(373, 510));
        caminho.add(new Vector2(373, 280));
        caminho.add(new Vector2(145, 280));
        caminho.add(new Vector2(145, 790));
        caminho.add(new Vector2(813, 790));
        caminho.add(new Vector2(813, 250));
        caminho.add(new Vector2(1035, 250));
        caminho.add(new Vector2(1035, 900));

        // =====================================================================
        // --- CONTROLE MANUAL DAS HITBOXES (O SEU "EDITOR") ---
        // Aqui você cria cada retângulo vermelho um por um.
        // Formato: new Rectangle(X, Y, LARGURA, ALTURA)
        // =====================================================================

        // 1. Primeiro trecho (Horizontal - Início)
        hitboxesCaminho.add(new Rectangle(-68,     60, 720, 47));

        // 2. Segundo trecho (Vertical)
        hitboxesCaminho.add(new Rectangle(592, 60, 60, 483));

        // 3. Terceiro trecho (Horizontal)
        hitboxesCaminho.add(new Rectangle(370, 493, 275, 50));

        // 4. Quarto trecho (Vertical)
        hitboxesCaminho.add(new Rectangle(367, 270, 58, 270));

        // 5. Quinto trecho (Horizontal)
        hitboxesCaminho.add(new Rectangle(140, 265, 288, 55));

        // 6. Sexto trecho (Vertical longo)
        hitboxesCaminho.add(new Rectangle(142, 267, 57, 560));

        // 7. Sétimo trecho (Horizontal superior)
        hitboxesCaminho.add(new Rectangle(142, 775, 718, 53));

        // 8. Oitavo trecho (Vertical descendo)
        hitboxesCaminho.add(new Rectangle(805, 235, 60, 590));

        // 9. Nono trecho (Horizontal pequeno embaixo)
        hitboxesCaminho.add(new Rectangle(803, 235, 288, 55));

        // 10. Décimo trecho (Vertical subindo para a casa)
        hitboxesCaminho.add(new Rectangle(1030, 240, 58, 650));

        // --- HITBOX DA CASA (FINAL) ---
        // Você também pode ajustar esses valores aqui embaixo
        float casaHitboxX = 965;
        float casaHitboxY = 870;
        float casaHitboxLargura = 185;
        float casaHitboxAltura = 195;
        hitboxesCaminho.add(new Rectangle(casaHitboxX, casaHitboxY, casaHitboxLargura, casaHitboxAltura));
    }

    public void desenharFundo(ShapeRenderer shape) {
        shape.setColor(0.15f, 0.15f, 0.15f, 1);
        shape.rect(1400, 0, 520, 1080);
    }

    public void desenharMapa(SpriteBatch batch) {
        batch.setColor(1, 1, 1, 1);
        batch.draw(imgFundo, 0, 0, 1400, 1080);

        for (Vector2 ponto : caminho) {
            batch.draw(imgChao, ponto.x - 16, ponto.y - 16, 32, 32);
        }
        batch.draw(imgCasa, 925, 800, 300, 300);
    }

    public void dispose() {
        imgChao.dispose();
        imgCasa.dispose();
        imgFundo.dispose();
    }
}
