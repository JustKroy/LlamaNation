package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;

public class Botao {

    private Texture imagem, hover;
    private Rectangle area;

    public Botao(Texture imagem, Texture hover, float x, float y, float largura, float altura) {
        this.imagem = imagem;
        this.hover = hover;
        this.area = new Rectangle(x, y, largura, altura);
    }

    public Boolean estaSobre(Vector2 mouse){
        return area.contains(mouse.x, mouse.y);
    }

    public Boolean foiClicado(Vector2 mouse) {
        return area.contains(mouse.x,mouse.y);
    }

    public void atualizarCursor(Vector2 mouse){

        if(estaSobre(mouse)){
            Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
        }

    }

    public void Exibir(SpriteBatch batch, Vector2 mouse) {
        if (estaSobre(mouse)) {
            batch.draw(imagem, area.x, area.y, area.width, area.height);
        } else {
            batch.draw(imagem, area.x, area.y, area.width, area.height);
        }
    }

    public void dispose() {
        imagem.dispose();
        hover.dispose();
    }
}
