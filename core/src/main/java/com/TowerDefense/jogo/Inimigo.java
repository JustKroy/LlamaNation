package com.TowerDefense.jogo;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

// O Inimigo é um "molde" (abstract) para todos os monstros do jogo.
// Ele sabe como andar, como se animar e como seguir o caminho,
// mas deixa os status (vida, velocidade) para os filhos decidirem.
public abstract class Inimigo {

    // --- POSIÇÃO ---
    public Vector2 posicao; // Guarda o X e Y exatos do inimigo no mapa

    // --- STATUS (Atributos Básicos) ---
    // Essas variáveis estão vazias aqui. As classes filhas (Ex: Caramujo) é que vão preenchê-las!
    public int vida; // Quanto de dano ele aguenta antes de sumir
    public float velocidade; // Quão rápido ele anda (pixels por segundo)
    public int recompensaMoedas; // Quanto de dinheiro o jogador ganha ao matar ele

    // --- ANIMAÇÃO E DIREÇÃO ---
    // "protected" significa que apenas esta classe e as filhas dela podem mexer nisso
    protected Animation<TextureRegion> animacaoNormal; // Animação dele andando para a direita
    protected Animation<TextureRegion> animacaoVirada; // Animação dele andando para a esquerda

    private float tempoAnim = 0; // Um cronômetro para saber qual quadro da animação desenhar agora
    private int pontoAtual = 1; // Qual ponto da curva (waypoint) do mapa ele está buscando agora (começa no 1, porque o 0 é onde ele nasce)
    private boolean indoParaEsquerda = false; // Memória para saber para qual lado ele está olhando

    // --- CONSTRUTOR ---
    // Quando um inimigo nasce (lá no GerenciadorDeOndas), ele recebe onde vai nascer (X, Y) e suas animações.
    public Inimigo(float x, float y, Animation<TextureRegion> animacaoNormal, Animation<TextureRegion> animacaoVirada) {
        this.posicao = new Vector2(x, y); // Define o ponto de partida
        this.animacaoNormal = animacaoNormal;
        this.animacaoVirada = animacaoVirada;
    }

    // --- LÓGICA DE MOVIMENTO E NAVEGAÇÃO ---
    // Retorna "false" enquanto ele está caminhando. Retorna "true" se ele chegou no fim do mapa (fugiu).
    public boolean atualizar(float delta, Array<Vector2> caminho) {
        tempoAnim += delta; // O relógio da animação avança um pouquinho a cada frame

        // Se o inimigo ainda não chegou no último ponto do caminho...
        if (pontoAtual < caminho.size) {

            // 1. Descobre qual é a coordenada (X, Y) do próximo ponto que ele tem que alcançar
            Vector2 alvo = caminho.get(pontoAtual);

            // 2. Calcula a direção. É simplesmente o "Destino menos a Origem".
            // Isso cria uma flecha invisível apontando do inimigo para o alvo.
            Vector2 direcao = new Vector2(alvo.x - posicao.x, alvo.y - posicao.y);

            // 3. Vira o rosto do inimigo!
            // Se a direção X for bem negativa, ele está indo para a esquerda.
            if (direcao.x < -0.5f) indoParaEsquerda = true;
                // Se for bem positiva, está indo para a direita.
            else if (direcao.x > 0.5f) indoParaEsquerda = false;

            // 4. Verifica se ele já chegou no ponto alvo.
            // direcao.len() mede o tamanho da flecha (a distância até o alvo).
            // velocidade * delta é o tamanho do passo que ele vai dar neste frame.
            // Se a distância for menor que o passo... ele chegou!
            if (direcao.len() <= velocidade * delta) {
                posicao.set(alvo); // Crava o inimigo exatamente em cima do ponto (para não passar direto)
                pontoAtual++; // Muda o alvo para o PRÓXIMO ponto da lista do mapa
            }
            // 5. Se ainda não chegou, continua andando...
            else {
                // nor() = Normaliza o vetor (transforma a flecha para ter tamanho 1)
                // scl() = Escala (multiplica) esse tamanho 1 pela velocidade de movimento do frame
                direcao.nor().scl(velocidade * delta);

                // Soma esse passo à posição atual do inimigo, fazendo ele andar.
                posicao.add(direcao);
            }
            return false; // Retorna falso porque ele ainda está vivo e caminhando pelo mapa
        }

        // Se o 'if' lá de cima falhar, significa que ele passou por todos os pontos (pontoAtual >= caminho.size).
        // Ele chegou no fim da estrada!
        return true;
    }

    // --- DESENHO NA TELA ---
    public void desenhar(SpriteBatch batch) {

        // Pede para o LibGDX: "Me dê a imagem exata da animação para o tempo atual!"
        // Se ele estiver indo para a esquerda, pega a 'animacaoVirada', senão pega a normal.
        TextureRegion frame = indoParaEsquerda ? animacaoVirada.getKeyFrame(tempoAnim, true) : animacaoNormal.getKeyFrame(tempoAnim, true);

        // Desenha o quadro da animação na posição atual com o tamanho fixo de 50x50 pixels
        batch.draw(frame, posicao.x, posicao.y, 50, 50);
    }
}
