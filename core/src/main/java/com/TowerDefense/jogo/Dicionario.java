/*
========================================
RESUMO DOS PRINCIPAIS COMANDOS DO LIBGDX
========================================

LibGDX é um framework Java para desenvolvimento de jogos multiplataforma
(desktop, Android, iOS e web). Abaixo estão os principais conceitos e
classes utilizadas em projetos de jogos profissionais.

----------------------------------------
1. CLASSE PRINCIPAL DO JOGO
----------------------------------------
A classe principal normalmente estende Game ou ApplicationAdapter.

Game:
- Permite trabalhar com múltiplas telas (menu, jogo, pause, etc).

Exemplo:
public class Main extends Game {
    public void create() {
        setScreen(new MenuScreen(this));
    }
}

ApplicationAdapter:
- Usado em jogos simples que não precisam de múltiplas telas.


----------------------------------------
2. SCREEN (TELAS DO JOGO)
----------------------------------------
Usado para separar partes do jogo:

Exemplos:
- MenuScreen
- GameScreen
- PauseScreen
- HeroScreen

Normalmente estende ScreenAdapter.

Métodos principais:

render(float delta)
→ executado várias vezes por segundo (game loop)

resize(int width, int height)
→ chamado quando a tela muda de tamanho

dispose()
→ libera memória (texturas, sons, etc)

show() e hide()
→ chamados quando a tela é aberta ou trocada


----------------------------------------
3. SPRITEBATCH
----------------------------------------
Classe responsável por desenhar imagens na tela.

SpriteBatch batch = new SpriteBatch();

Uso:

batch.begin();
batch.draw(texture, x, y);
batch.end();

Regra importante:
Tudo que for desenhado precisa estar entre
begin() e end().


----------------------------------------
4. TEXTURE
----------------------------------------
Representa uma imagem carregada na memória.

Texture player = new Texture("player.png");

Desenho:

batch.draw(player, x, y);

Sempre liberar memória depois:

player.dispose();


----------------------------------------
5. TEXTUREREGION
----------------------------------------
Permite usar apenas uma parte de uma imagem.

Muito usado para:

- spritesheets
- animações
- tiles


----------------------------------------
6. ANIMATION
----------------------------------------
Usado para criar animações a partir de sprites.

Animation<TextureRegion> walk;

Permite trocar frames automaticamente com base no tempo.


----------------------------------------
7. INPUT (ENTRADA DO USUÁRIO)
----------------------------------------
LibGDX captura teclado, mouse e toque.

Exemplos:

Gdx.input.justTouched()
→ detecta clique/toque

Gdx.input.getX()
Gdx.input.getY()
→ posição do mouse

Gdx.input.isKeyPressed(Input.Keys.W)
→ tecla pressionada


----------------------------------------
8. CAMERA
----------------------------------------
Controla a visão do jogo.

OrthographicCamera camera = new OrthographicCamera();

Muito usada para:
- jogos 2D
- seguir o jogador
- mover o mapa

Exemplo:

camera.update();
batch.setProjectionMatrix(camera.combined);


----------------------------------------
9. VIEWPORT
----------------------------------------
Controla como o jogo se adapta a diferentes resoluções.

Tipos comuns:

StretchViewport
→ estica o jogo para caber na tela

FitViewport
→ mantém proporção sem distorção

ScreenViewport
→ usa tamanho real da tela

Exemplo:

StretchViewport viewport = new StretchViewport(1920, 1080);


----------------------------------------
10. SHAPERENDERER
----------------------------------------
Usado para desenhar formas simples:

- linhas
- retângulos
- círculos

Muito usado para:

- debug
- colisão
- grid

Exemplo:

shape.begin(ShapeRenderer.ShapeType.Line);
shape.rect(x, y, width, height);
shape.end();


----------------------------------------
11. RECTANGLE (COLISÃO)
----------------------------------------
Classe simples para detectar colisões.

Rectangle player;
Rectangle enemy;

Exemplo:

if(player.overlaps(enemy)) {
    // colisão detectada
}


----------------------------------------
12. VECTOR2 / VECTOR3
----------------------------------------
Representam posições e direções.

Vector2 position = new Vector2(x, y);

Muito usado para:
- movimento
- física
- direção


----------------------------------------
13. GDX (ACESSO GLOBAL)
----------------------------------------
Classe que dá acesso ao sistema.

Exemplos:

Gdx.graphics.getDeltaTime()
→ tempo entre frames

Gdx.graphics.getWidth()
Gdx.graphics.getHeight()
→ tamanho da tela

Gdx.app.log("TAG", "Mensagem");
→ log no console


----------------------------------------
14. DELTATIME (FPS INDEPENDENTE)
----------------------------------------
Para movimento suave independente do FPS.

float delta = Gdx.graphics.getDeltaTime();

posição += velocidade * delta;


----------------------------------------
15. DISPOSE (GERENCIAMENTO DE MEMÓRIA)
----------------------------------------
Texturas, sons e outros recursos devem ser liberados.

Exemplo:

texture.dispose();
batch.dispose();

Evita vazamento de memória.


----------------------------------------
16. ESTRUTURA COMUM DE PROJETO LIBGDX
----------------------------------------

core/
→ lógica do jogo

desktop/
→ launcher para PC

android/
→ launcher para Android

assets/
→ imagens, sons, fontes


----------------------------------------
17. BOAS PRÁTICAS EM PROJETOS PROFISSIONAIS
----------------------------------------

✔ separar telas usando Screen
✔ usar AssetManager para carregar recursos
✔ reutilizar SpriteBatch
✔ organizar código em pacotes
✔ evitar criar objetos dentro do render()
✔ usar deltaTime para movimento


----------------------------------------
18. GAME LOOP (CICLO DO JOGO)
----------------------------------------

O LibGDX executa automaticamente este ciclo:

1. processar input
2. atualizar lógica
3. desenhar na tela
4. repetir

Isso acontece dentro do método render().


========================================
FIM DO RESUMO
========================================
*/
/*
========================================
IMAGEM DE FUNDO NO LIBGDX - SIMPLES
========================================

Para colocar uma imagem de fundo em uma tela do jogo (menu, mapa, etc),
utilizamos a classe Texture junto com SpriteBatch.

----------------------------------------
1. CARREGAR A IMAGEM
----------------------------------------

Primeiro carregamos a imagem da pasta assets.

Texture fundo = new Texture("fundo.png");

A imagem deve estar dentro da pasta:

assets/

Exemplo de estrutura:

assets/
    fundo.png
    play.png
    hero.png

----------------------------------------
2. DESENHAR A IMAGEM
----------------------------------------

A imagem é desenhada d  entro do método render().

batch.begin();
batch.draw(fundo, x, y);
batch.end();

Exemplo:

batch.draw(fundo, 0, 0);

Isso desenha a imagem no canto inferior esquerdo da tela.

----------------------------------------
3. AJUSTAR PARA COBRIR TODA A TELA
----------------------------------------

Para usar como fundo completo:

batch.draw(fundo, 0, 0, largura, altura);

Exemplo:

batch.draw(fundo, 0, 0, 1920, 1080);

ou usando o tamanho da tela:

batch.draw(fundo, 0, 0,
           Gdx.graphics.getWidth(),
           Gdx.graphics.getHeight());

----------------------------------------
4. EXEMPLO COMPLETO
----------------------------------------

private Texture fundo;

public MenuScreen(Main game) {

    fundo = new Texture("fundo.png");

}

@Override
public void render(float delta) {

    ScreenUtils.clear(0,0,0,1);

    batch.begin();

    batch.draw(fundo, 0, 0, 1920, 1080);

    batch.end();

}

----------------------------------------
5. LIBERAR MEMÓRIA
----------------------------------------

Sempre liberar a textura no método dispose().

@Override
public void dispose() {
    fundo.dispose();
}

----------------------------------------
6. ORDEM DE DESENHO
----------------------------------------

O fundo deve sempre ser desenhado primeiro.

Exemplo correto:

batch.begin();

batch.draw(fundo, 0, 0);
batch.draw(botaoPlay, x, y);
batch.draw(personagem, x2, y2);

batch.end();

O que for desenhado depois aparece na frente.

----------------------------------------
7. BOAS PRÁTICAS
----------------------------------------

✔ usar imagens na resolução do jogo
✔ desenhar fundo antes de outros objetos
✔ evitar criar Texture dentro do render()
✔ sempre usar dispose()

========================================
*/
/*
========================================
IMAGEM DE FUNDO NO LIBGDX - AVANÇADO
========================================

private Texture sky;
private Texture mountains;
private Texture trees;

private float scroll = 0;

@Override
public void render(float delta){

    scroll += 50 * delta;

    batch.begin();

        batch.draw(sky, 0, 0);

        batch.draw(mountains, -scroll * 0.3f, 0);

        batch.draw(trees, -scroll * 0.6f, 0);

    batch.end();
}


private Texture ceu;
private Texture montanhas;
private Texture arvores;

private float scrollCeu = 0;
private float scrollMontanhas = 0;
private float scrollArvores = 0;

@Override
public void render(float delta) {

    scrollCeu += 10 * delta;
    scrollMontanhas += 30 * delta;
    scrollArvores += 60 * delta;

    if (scrollCeu >= ceu.getWidth()) scrollCeu = 0;
    if (scrollMontanhas >= montanhas.getWidth()) scrollMontanhas = 0;
    if (scrollArvores >= arvores.getWidth()) scrollArvores = 0;

    batch.begin();

    // céu
    batch.draw(ceu, -scrollCeu, 0);
    batch.draw(ceu, ceu.getWidth() - scrollCeu, 0);

    // montanhas
    batch.draw(montanhas, -scrollMontanhas, 0);
    batch.draw(montanhas, montanhas.getWidth() - scrollMontanhas, 0);

    // árvores
    batch.draw(arvores, -scrollArvores, 0);
    batch.draw(arvores, arvores.getWidth() - scrollArvores, 0);

    batch.end();
}

========================================
*/
/*
========================================
IMAGEM DE FUNDO NO LIBGDX - FUNDO INFINITO
========================================

private Texture fundo;
private float scroll = 0;

@Override
public void render(float delta){

    scroll += 100 * delta;

    batch.begin();

        batch.draw(fundo, -scroll, 0);
        batch.draw(fundo, fundo.getWidth() - scroll, 0);

    batch.end();
}

========================================
*/
/*
========================================
DICIONÁRIO: BOTÕES CLICÁVEIS NO LIBGDX
========================================

Este dicionário reúne os principais conceitos, classes e comandos
usados para criar botões clicáveis em jogos LibGDX.

========================================
1. TEXTURE
========================================
O que é:
- A imagem do botão.

Para que serve:
- Mostrar visualmente o botão na tela.

Exemplo:
Texture imgPlay = new Texture("play.png");

Uso:
batch.draw(imgPlay, x, y, largura, altura);

Observação:
- A imagem precisa estar na pasta assets.
- Sempre use dispose() depois.


========================================
2. RECTANGLE
========================================
O que é:
- Um retângulo invisível que representa a área clicável do botão.

Para que serve:
- Detectar se o mouse clicou dentro do botão.

Exemplo:
Rectangle btnPlay = new Rectangle(760, 500, 400, 150);

Significado dos valores:
- 760 = posição X
- 500 = posição Y
- 400 = largura
- 150 = altura

Uso:
if (btnPlay.contains(mouseX, mouseY)) {
    // botão clicado
}


========================================
3. VECTOR2
========================================
O que é:
- Um objeto que guarda uma posição com X e Y.

Para que serve:
- Armazenar a posição do mouse convertida para o sistema do jogo.

Exemplo:
Vector2 posMouse = new Vector2();

Uso:
posMouse = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));


========================================
4. Gdx.input.justTouched()
========================================
O que é:
- Comando que detecta se houve um clique ou toque.

Para que serve:
- Saber o momento em que o jogador clicou.

Exemplo:
if (Gdx.input.justTouched()) {
    // houve clique
}

Observação:
- Detecta apenas o instante do clique.
- Não fica repetindo enquanto segura o mouse.


========================================
5. Gdx.input.getX() e getY()
========================================
O que é:
- Capturam a posição atual do mouse na tela.

Para que serve:
- Descobrir onde o jogador clicou.

Exemplo:
int x = Gdx.input.getX();
int y = Gdx.input.getY();

Observação:
- Esses valores vêm no sistema de coordenadas da tela.
- Normalmente precisam ser convertidos com viewport.unproject().


========================================
6. VIEWPORT.UNPROJECT()
========================================
O que é:
- Converte a posição do mouse da tela real para o sistema de coordenadas do jogo.

Para que serve:
- Fazer o clique bater corretamente com os botões desenhados.

Exemplo:
Vector2 posMouse = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

Por que usar:
- Sem isso, o clique pode ficar desalinhado em resoluções diferentes.


========================================
7. contains()
========================================
O que é:
- Método da classe Rectangle.

Para que serve:
- Verificar se um ponto está dentro do retângulo.

Exemplo:
if (btnPlay.contains(posMouse.x, posMouse.y)) {
    System.out.println("Clicou no botão!");
}

Leitura:
- "Se a posição do mouse estiver dentro do botão..."


========================================
8. SPRITEBATCH
========================================
O que é:
- Classe responsável por desenhar as imagens na tela.

Para que serve:
- Desenhar o botão.

Exemplo:
batch.begin();
batch.draw(imgPlay, btnPlay.x, btnPlay.y, btnPlay.width, btnPlay.height);
batch.end();

Regra:
- Sempre desenhar entre begin() e end().


========================================
9. ORDEM DE DESENHO
========================================
O que é:
- A sequência em que os elementos são desenhados.

Para que serve:
- Garantir que o botão apareça na frente do fundo.

Exemplo correto:
batch.begin();
batch.draw(imgFundo, 0, 0, 1920, 1080);
batch.draw(imgPlay, btnPlay.x, btnPlay.y, btnPlay.width, btnPlay.height);
batch.end();

Regra:
- Fundo primeiro
- Botão depois


========================================
10. game.setScreen()
========================================
O que é:
- Comando para trocar de tela.

Para que serve:
- Fazer o botão levar o jogador para outra tela.

Exemplo:
if (btnPlay.contains(posMouse.x, posMouse.y)) {
    game.setScreen(new GameScreen(game));
}

Usos comuns:
- botão Play -> GameScreen
- botão Heroes -> HeroScreen
- botão Voltar -> MenuScreen


========================================
11. BOTÃO CLICÁVEL COMPLETO
========================================
Exemplo completo e simples:

private Texture imgPlay;
private Rectangle btnPlay;
private Vector2 posMouse = new Vector2();

public MenuScreen(Main game) {
    imgPlay = new Texture("play.png");
    btnPlay = new Rectangle(760, 500, 400, 150);
}

@Override
public void render(float delta) {
    ScreenUtils.clear(0, 0, 0, 1);

    viewport.apply();
    batch.setProjectionMatrix(viewport.getCamera().combined);

    if (Gdx.input.justTouched()) {
        posMouse = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

        if (btnPlay.contains(posMouse.x, posMouse.y)) {
            game.setScreen(new GameScreen(game));
        }
    }

    batch.begin();
    batch.draw(imgPlay, btnPlay.x, btnPlay.y, btnPlay.width, btnPlay.height);
    batch.end();
}


========================================
12. BOTÃO "VOLTAR"
========================================
Exemplo:

private Texture imgVoltar;
private Rectangle btnVoltar;

imgVoltar = new Texture("voltar.png");
btnVoltar = new Rectangle(50, 900, 200, 100);

if (btnVoltar.contains(posMouse.x, posMouse.y)) {
    game.setScreen(new MenuScreen(game));
}


========================================
13. BOTÃO COM EFEITO HOVER
========================================
O que é:
- Quando o mouse passa em cima e o botão muda visualmente.

Como fazer:
- verificar se o mouse está dentro do Rectangle
- desenhar outra textura ou mudar tamanho/cor

Exemplo:
if (btnPlay.contains(posMouse.x, posMouse.y)) {
    batch.draw(imgPlayHover, btnPlay.x, btnPlay.y, btnPlay.width, btnPlay.height);
} else {
    batch.draw(imgPlay, btnPlay.x, btnPlay.y, btnPlay.width, btnPlay.height);
}

Observação:
- Para hover, você verifica a posição do mouse a todo momento,
  não só no justTouched().


========================================
14. BOTÃO COM DUAS IMAGENS
========================================
O que é:
- Uma imagem normal
- Uma imagem quando o mouse passa em cima ou quando é clicado

Exemplo:
Texture imgNormal = new Texture("play.png");
Texture imgHover = new Texture("play_hover.png");

Uso:
if (btnPlay.contains(posMouse.x, posMouse.y)) {
    batch.draw(imgHover, ...);
} else {
    batch.draw(imgNormal, ...);
}


========================================
15. BOTÃO COM CLASSE PRÓPRIA
========================================
O que é:
- Criar uma classe Botao para reaproveitar.

Para que serve:
- Evitar repetir Rectangle + Texture em toda tela.

Exemplo de ideia:

public class Botao {
    Texture imagem;
    Rectangle area;

    public Botao(Texture imagem, float x, float y, float largura, float altura) {
        this.imagem = imagem;
        this.area = new Rectangle(x, y, largura, altura);
    }

    public void desenhar(SpriteBatch batch) {
        batch.draw(imagem, area.x, area.y, area.width, area.height);
    }

    public boolean foiClicado(Vector2 mouse) {
        return area.contains(mouse.x, mouse.y);
    }
}

Vantagem:
- Código mais limpo
- Mais profissional
- Reaproveitamento


========================================
16. resize()
========================================
O que é:
- Método chamado quando o tamanho da tela muda.

Para que serve:
- Atualizar o viewport e manter os botões funcionando corretamente.

Exemplo:
@Override
public void resize(int width, int height) {
    viewport.update(width, height, true);
}


========================================
17. dispose()
========================================
O que é:
- Método usado para liberar memória.

Para que serve:
- Evitar vazamento de memória no jogo.

Exemplo:
@Override
public void dispose() {
    batch.dispose();
    imgPlay.dispose();
    imgHeroes.dispose();
}

Regra:
- Toda Texture deve ser liberada no dispose().


========================================
18. ERROS COMUNS
========================================

ERRO:
- O botão aparece, mas não clica.

Causa comum:
- esqueceu de usar viewport.unproject().

----------------------------------------

ERRO:
- O clique está fora do lugar.

Causa comum:
- coordenadas do mouse sem conversão.

----------------------------------------

ERRO:
- A tela troca, mas fica tudo preto.

Causa comum:
- a nova screen não tem render().

----------------------------------------

ERRO:
- O botão some atrás do fundo.

Causa comum:
- ordem de desenho errada.

----------------------------------------

ERRO:
- Travamento ou consumo alto de memória.

Causa comum:
- criando Texture dentro do render().


========================================
19. PASSO A PASSO PARA CRIAR UM BOTÃO
========================================

1. carregar a imagem
2. criar um Rectangle
3. detectar clique com justTouched()
4. converter mouse com unproject()
5. verificar contains()
6. executar ação
7. desenhar a imagem do botão
8. liberar a textura no dispose()


========================================
20. MODELO RESUMIDO
========================================

Texture imgBotao = new Texture("botao.png");
Rectangle btn = new Rectangle(700, 400, 300, 120);
Vector2 mouse = new Vector2();

if (Gdx.input.justTouched()) {
    mouse = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

    if (btn.contains(mouse.x, mouse.y)) {
        System.out.println("Botão clicado!");
    }
}

batch.begin();
batch.draw(imgBotao, btn.x, btn.y, btn.width, btn.height);
batch.end();


========================================
FIM DO DICIONÁRIO
========================================
*/
