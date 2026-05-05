package com.TowerDefense.jogo;

import com.badlogic.gdx.audio.Sound;
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
    private Sound somClique;



    public Botao(Texture imagem, Texture hover, float x, float y, float largura, float altura, Sound somClique) {
        this.imagem = imagem;
        this.hover = hover;
        this.area = new Rectangle(x, y, largura, altura);
        this.corBorda = new Color(1,1,1,1);
        this.somClique = somClique;
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

    public void setTextura(Texture normal, Texture hover) {
        this.imagem = normal;
        this.hover = hover;
    }

    public void setCorBorda(Color cor) {
        corBorda = cor;
    }

    public Boolean estaSobre(Vector2 mouse){
        return area.contains(mouse.x, mouse.y);
    }

    public boolean foiClicado(Vector2 mouse) {
        if (estaSobre(mouse) && Gdx.input.justTouched()) {
            if (somClique != null) {
                somClique.play(0.3f);
            }
            return true;
        }
        return false;
    }

    public void atualizarCursor(Vector2 mouse) {
        if (area.contains(mouse)) {
            CursorManager.setHover();
        }
    }

    public void Exibir(SpriteBatch batch, Vector2 mouse) {

        boolean hoverAtivo = estaSobre(mouse);
        Texture texturaAtual = hoverAtivo && hover != null ? hover : imagem;

        if (texturaAtual == null) return;

        // 1. ANIMAÇÕES DE ESCALA:
        // Se estiver selecionado (aba aberta), ele trava afundado em 0.95f.
        // Se não, segue a lógica normal de clique e hover.
        boolean clicando = false;
        float targetScale = selecionado ? 0.95f : (clicando ? 0.97f : (hoverAtivo ? 1.05f : 1.0f));
        scaleAtual += (targetScale - scaleAtual) * 0.15f;

        alpha += (1f - alpha) * 0.05f;

        // 2. CÁLCULO CENTRALIZADO
        float drawWidth = area.width * scaleAtual;
        float drawHeight = area.height * scaleAtual;

        float drawX = area.x - (drawWidth - area.width) / 2;
        float drawY = area.y - (drawHeight - area.height) / 2;

        // 3. EFEITO GRAVIDADE (Afunda o botão)
        if (selecionado) {
            drawY -= 4f; // Desce 4 pixels na tela
        }

        // 4. BRILHO E COR:
        // Se selecionado, fica mais escuro (0.8f) para dar profundidade.
        float brilho = selecionado ? 0.8f : (hoverAtivo ? 1.2f : 1f);

        batch.setColor(brilho, brilho, brilho, alpha);
        batch.draw(texturaAtual, drawX, drawY, drawWidth, drawHeight);

        // 5. BORDA (Sua lógica de colorir quando selecionado)
        if (selecionado) {
            // Apliquei 60% de opacidade (0.6f * alpha) para a cor da borda
            // tingir o botão sem cobrir o desenho totalmente!
            batch.setColor(corBorda.r, corBorda.g, corBorda.b, 0.6f * alpha);
            batch.draw(texturaAtual, drawX, drawY, drawWidth, drawHeight);
        }

        // reset obrigatório
        batch.setColor(Color.WHITE);
    }

    public void dispose() {
        // NÃO dispose som aqui
    }

}
