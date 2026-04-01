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
    private float scaleAtual = 1.0f;
    private float alpha = 0f;
    private boolean selecionado = false; //Variável que permite mudar o estado do botão


    public Botao(Texture imagem, Texture hover, float x, float y, float largura, float altura) {
        this.imagem = imagem;
        this.hover = hover;
        this.area = new Rectangle(x, y, largura, altura);
        corBorda = new Color(1,1,1,1);
    }

    public Rectangle getArea() {
        return new Rectangle(area);
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

    public boolean foiClicado(Vector2 mouse, boolean clicou) {
        return estaSobre(mouse) && clicou;
    }

    public void atualizarCursor(Vector2 mouse){

        if(estaSobre(mouse)){
            Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
        }

    }

    public void Exibir(SpriteBatch batch, Vector2 mouse) {

        boolean hoverAtivo = estaSobre(mouse);
        Texture texturaAtual = hoverAtivo ? hover : imagem;

        // animações
        boolean clicando = estaSobre(mouse) && Gdx.input.isTouched();
        float targetScale = clicando ? 0.97f : (hoverAtivo ? 1.05f : 1.0f);
        scaleAtual += (targetScale - scaleAtual) * 0.15f;

        alpha += (1f - alpha) * 0.05f;

        // cálculo centralizado
        float drawWidth = area.width * scaleAtual;
        float drawHeight = area.height * scaleAtual;

        float drawX = area.x - (drawWidth - area.width) / 2;
        float drawY = area.y - (drawHeight - area.height) / 2;

        // glow + alpha combinados
        float brilho = hoverAtivo ? 1.2f : 1f;

        batch.setColor(brilho, brilho, brilho, alpha);

        batch.draw(texturaAtual, drawX, drawY, drawWidth, drawHeight);

        // borda
        if (selecionado) {
            batch.setColor(corBorda);
            batch.draw(texturaAtual, drawX, drawY, drawWidth, drawHeight);
        }

        // reset obrigatório
        batch.setColor(Color.WHITE);
    }

    public void dispose() {
        imagem.dispose();
        hover.dispose();
    }

}
