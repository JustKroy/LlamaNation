package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;

public class Botao {

    private Texture imagem, hover, borda;
    private Rectangle area;
    private Color corBorda;
    private boolean selecionado = false; //Variável que permite mudar o estado do botão


    public Botao(Texture imagem, Texture hover, float x, float y, float largura, float altura) {
        this.imagem = imagem;
        this.hover = hover;
        this.area = new Rectangle(x, y, largura, altura);
        corBorda = new Color(1,1,1,1);
    }
    public void setSelecionado(boolean selecionado) {
        this.selecionado = selecionado;
    }

    public boolean isSelecionado() {
        return selecionado;
    }

    public void setCorBorda(Color cor) {
        corBorda = cor;
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
            batch.draw(hover, area.x, area.y, area.width, area.height);
        } else {
            batch.draw(imagem, area.x, area.y, area.width, area.height);
        }

        if (selecionado) {
            batch.setColor(corBorda);
            batch.draw(imagem, area.x - 10, area.y - 10, area.width + 20, area.height + 20);
            batch.setColor(1, 1, 1, 1);
        }
    }

    public void dispose() {
        imagem.dispose();
        hover.dispose();
    }

}
