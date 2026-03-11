package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

// O "abstract" significa que esta classe é apenas um MOLDE.
// Você nunca vai criar um "new Torre()", você sempre vai criar uma "new LhamaNormal()"
// ou "new LhamaNinja()", que herdam tudo o que está escrito aqui!
public abstract class Torre {

    // --- VARIÁVEIS DE FÍSICA E IMAGEM ---
    public Vector2 posicao; // Guarda o X e Y da torre no mapa
    public Rectangle hitbox; // O retângulo invisível que define o tamanho real da torre para colisões e cliques
    public Texture textura; // A imagem da lhama
    public Texture imgProjetil; // A imagem do tiro que essa lhama vai cuspir/jogar

    // --- ATRIBUTOS DE COMBATE (Status) ---
    // Valores vazios aqui. Eles serão preenchidos pelas lhamas específicas (Ninja, Normal, etc.)
    public float raio; // O alcance de visão da torre (área de tiro)
    public float cooldown; // O tempo de recarga entre um tiro e outro
    public int dano; // Quanto de vida ela tira do inimigo
    public float tamanhoProjetil; // O tamanho da imagem do tiro
    public float offsetProjetil; // Um ajuste fino para centralizar o tiro

    // --- CONTROLES DE DESENHO E ANIMAÇÃO ---
    public float tempoTiro = 0; // Cronômetro interno para saber quando pode atirar de novo
    public float alturaDesenho = 80f; // A altura padrão de todas as torres na tela
    public float larguraDesenho; // A largura vai depender do formato da imagem (gordinha, magrinha...)
    public boolean viradaParaEsquerda = false; // Controla se a lhama precisa olhar para trás para atirar

    // --- CONSTRUTOR ---
    // Prepara a lhama assim que ela é colocada no mapa
    public Torre(float x, float y, Texture textura, Texture imgProjetil) {
        this.posicao = new Vector2(x, y); // Salva onde o jogador clicou para largar a torre
        this.textura = textura; // Salva qual é a imagem dessa torre
        this.imgProjetil = imgProjetil; // Salva a munição dela

        // 1. CÁLCULO DE PROPORÇÃO (Aspect Ratio)
        // Divide a largura original da imagem pela altura para descobrir o formato real dela.
        float proporcao = (float) textura.getWidth() / textura.getHeight();

        // Multiplica a altura fixa (80) por essa proporção.
        // Se a imagem for quadrada, a largura vira 80. Se for retangular, ela se adapta!
        this.larguraDesenho = this.alturaDesenho * proporcao;

        // 2. Cria a caixa de colisão (hitbox) exatamente com as medidas calculadas acima
        this.hitbox = new Rectangle(x, y, larguraDesenho, alturaDesenho);
    }

    // --- LÓGICA DE JOGO (Pensamento da Torre) ---
    // Roda o tempo todo na GameScreen para ver se tem inimigo perto
    public void atualizar(float delta, Array<Inimigo> listaInimigos, Array<Projetil> listaProjeteis) {
        tempoTiro += delta; // O cronômetro avança um pouquinho a cada frame

        // A lhama olha para TODOS os inimigos do mapa, um por um
        for (Inimigo in : listaInimigos) {

            // Descobre onde é o centro exato da Lhama
            float centroTorreX = posicao.x + (larguraDesenho / 2f);
            float centroTorreY = posicao.y + (alturaDesenho / 2f);

            // Descobre onde é o centro exato do Inimigo (assumindo que ele tem 50x50, o meio é 25)
            float centroInimigoX = in.posicao.x + 25f;
            float centroInimigoY = in.posicao.y + 25f;

            // Usa a matemática do LibGDX para medir a distância em linha reta entre os dois centros
            float distancia = Vector2.dst(centroTorreX, centroTorreY, centroInimigoX, centroInimigoY);

            // Se a distância for menor que o alcance da arma... o inimigo está na mira!
            if (distancia <= raio) {

                // Vira o rosto da lhama: Se o X do inimigo for menor que o X da torre, ele está à esquerda.
                viradaParaEsquerda = (in.posicao.x < posicao.x);

                // Se o cronômetro passou do tempo de recarga (cooldown), PODE ATIRAR!
                if (tempoTiro >= cooldown) {

                    // LÓGICA DA BOCA DA LHAMA
                    // Se estiver olhando pra esquerda, o tiro sai de perto de 20% da largura (frente do rosto)
                    // Se estiver olhando pra direita, sai de perto de 80% da largura
                    float bocaX = viradaParaEsquerda ? posicao.x + (larguraDesenho * 0.2f) : posicao.x + (larguraDesenho * 0.8f);

                    // A altura do tiro sai mais ou menos do meio do rosto da lhama (55% da altura)
                    float bocaY = posicao.y + (alturaDesenho * 0.55f);

                    // Cria um novo projétil no mapa saindo da "boca" em direção ao inimigo
                    listaProjeteis.add(new Projetil(bocaX, bocaY, in, imgProjetil, dano, tamanhoProjetil, offsetProjetil));

                    tempoTiro = 0; // Zera o cronômetro para começar a carregar o próximo tiro
                }

                // Se já achou um alvo e atirou, para de procurar outros inimigos neste frame (break)
                break;
            }
        }
    }

    // --- DESENHO NA TELA ---
    // Chamado pelo SpriteBatch lá na GameScreen
    public void desenhar(SpriteBatch batch) {
        // Esse método draw gigante serve especificamente para podermos usar o recurso de "espelhar" a imagem (flip).
        // Ele pega a imagem, as posições, a largura e altura de desenho, as coordenadas originais da textura,
        // e por último usa o 'viradaParaEsquerda' (true/false) para virar a imagem no eixo X!
        batch.draw(textura, posicao.x, posicao.y, larguraDesenho, alturaDesenho,
            0, 0, textura.getWidth(), textura.getHeight(), viradaParaEsquerda, false);
    }
}
