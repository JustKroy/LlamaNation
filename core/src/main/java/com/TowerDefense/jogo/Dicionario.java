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
/*
=========================================================
DICIONÁRIO LIBGDX
TROCAR IMAGEM AO CLICAR EM QUADRADOS (SELEÇÃO DE HERÓI)
=========================================================

Objetivo:
Quando o jogador clicar em um dos quadrados vermelhos (esquerda),
a imagem exibida no quadrado grande (direita) deve mudar.

Esse sistema é usado em:
- seleção de personagem
- seleção de skin
- inventário
- seleção de torre

---------------------------------------------------------
1. TEXTURE
---------------------------------------------------------
Representa a imagem carregada na memória.

Exemplo:

Texture hero1 = new Texture("hero1.png");
Texture hero2 = new Texture("hero2.png");

Essas serão as imagens que aparecerão no quadrado grande.


---------------------------------------------------------
2. RECTANGLE
---------------------------------------------------------
Representa a área clicável de cada quadrado.

Exemplo:

Rectangle hero1Slot = new Rectangle(20,700,200,200);
Rectangle hero2Slot = new Rectangle(20,450,200,200);

Isso corresponde aos quadrados vermelhos da esquerda.


---------------------------------------------------------
3. VARIÁVEL DE IMAGEM SELECIONADA
---------------------------------------------------------
Criamos uma variável que guarda a imagem atual.

Exemplo:

Texture heroSelecionado;

Inicialização:

heroSelecionado = hero1;


---------------------------------------------------------
4. CAPTURAR POSIÇÃO DO MOUSE
---------------------------------------------------------
Usamos Vector2 para pegar a posição do mouse.

Vector2 posMouse = new Vector2();

Converter posição:

posMouse = viewport.unproject(
        new Vector2(Gdx.input.getX(), Gdx.input.getY())
);


---------------------------------------------------------
5. DETECTAR CLIQUE
---------------------------------------------------------
Usamos:

Gdx.input.justTouched()

Exemplo:

if(Gdx.input.justTouched()){

    if(hero1Slot.contains(posMouse.x,posMouse.y)){
        heroSelecionado = hero1;
    }

    if(hero2Slot.contains(posMouse.x,posMouse.y)){
        heroSelecionado = hero2;
    }

}


---------------------------------------------------------
6. DESENHAR A IMAGEM GRANDE
---------------------------------------------------------
No render:

batch.draw(heroSelecionado,1050,600,350,300);

Agora a imagem exibida depende da seleção.


---------------------------------------------------------
7. DESENHAR OS QUADRADOS DA ESQUERDA
---------------------------------------------------------

batch.draw(hero1,20,700,200,200);
batch.draw(hero2,20,450,200,200);


---------------------------------------------------------
8. EXEMPLO COMPLETO
---------------------------------------------------------

Texture hero1;
Texture hero2;
Texture heroSelecionado;

Rectangle hero1Slot;
Rectangle hero2Slot;

Vector2 posMouse = new Vector2();


public HeroScreen(){

    hero1 = new Texture("hero1.png");
    hero2 = new Texture("hero2.png");

    heroSelecionado = hero1;

    hero1Slot = new Rectangle(20,700,200,200);
    hero2Slot = new Rectangle(20,450,200,200);
}


@Override
public void render(float delta){

    posMouse = viewport.unproject(
            new Vector2(Gdx.input.getX(),Gdx.input.getY())
    );

    if(Gdx.input.justTouched()){

        if(hero1Slot.contains(posMouse.x,posMouse.y)){
            heroSelecionado = hero1;
        }

        if(hero2Slot.contains(posMouse.x,posMouse.y)){
            heroSelecionado = hero2;
        }

    }

    batch.begin();

    batch.draw(hero1,20,700,200,200);
    batch.draw(hero2,20,450,200,200);

    batch.draw(heroSelecionado,1050,600,350,300);

    batch.end();
}


---------------------------------------------------------
9. BOAS PRÁTICAS
---------------------------------------------------------

✔ usar Rectangle para áreas clicáveis
✔ sempre converter mouse com viewport.unproject()
✔ armazenar seleção em uma variável
✔ desenhar a imagem selecionada separadamente

---------------------------------------------------------
10. EXTENSÃO PROFISSIONAL
---------------------------------------------------------

Jogos normalmente adicionam:

- borda no item selecionado
- animação de destaque
- som de seleção
- efeito hover

Exemplo:

heroSelecionadoIndex = 0,1,2,3...

Isso permite trabalhar com arrays.


---------------------------------------------------------
11. VERSÃO COM ARRAY (MAIS PROFISSIONAL)
---------------------------------------------------------

Texture[] heroes;
Rectangle[] slots;

int heroSelecionado = 0;

Ao clicar:

heroSelecionado = i;

Desenhar:

batch.draw(heroes[heroSelecionado],1050,600,350,300);

Isso permite adicionar quantos heróis quiser.


=========================================================
RESUMO
=========================================================

Quadrado vermelho clicado
          ↓
detecta contains()
          ↓
troca variável heroSelecionado
          ↓
imagem grande desenhada muda


Isso é a base de qualquer sistema de seleção em jogos.
=========================================================
*/
/*
=========================================================
DICIONÁRIO LIBGDX
TROCAR IMAGEM USANDO A CLASSE BOTAO
=========================================================

Objetivo:
Ao clicar em um Botao da esquerda, a imagem grande da direita muda.

Nesse caso:
- os quadrados vermelhos da esquerda = objetos Botao
- a imagem grande verde = uma Texture variável
- ao clicar no botão, trocamos essa variável

=========================================================
1. IDEIA PRINCIPAL
=========================================================

Cada botão representa um herói.

Exemplo:
btnLlama
btnNinja
btnMage
btnRobo

Quando clicar em um deles:
imagemSelecionada = imagemDoHeroi

Depois, no render():
batch.draw(imagemSelecionada, x, y, largura, altura);

=========================================================
2. CLASSE BOTAO
=========================================================

A classe Botao continua responsável por:
- desenhar o botão
- detectar hover
- detectar clique

Exemplo:

public class Botao {

    private Texture imagem;
    private Texture hover;
    private Rectangle area;

    public Botao(Texture imagem, Texture hover, float x, float y, float largura, float altura) {
        this.imagem = imagem;
        this.hover = hover;
        this.area = new Rectangle(x, y, largura, altura);
    }

    public boolean estaSobre(Vector2 mouse) {
        return area.contains(mouse.x, mouse.y);
    }

    public boolean foiClicado(Vector2 mouse) {
        return area.contains(mouse.x, mouse.y);
    }

    public void exibir(SpriteBatch batch, Vector2 mouse) {
        if (estaSobre(mouse)) {
            batch.draw(hover, area.x, area.y, area.width, area.height);
        } else {
            batch.draw(imagem, area.x, area.y, area.width, area.height);
        }
    }

    public void dispose() {
        imagem.dispose();
        hover.dispose();
    }
}

=========================================================
3. VARIÁVEL DA IMAGEM PRINCIPAL
=========================================================

Criamos uma variável para guardar a imagem que aparece
no quadrado grande da direita.

Exemplo:

private Texture imgSelecionada;

Essa variável muda quando um botão é clicado.

=========================================================
4. CRIANDO AS IMAGENS DOS HERÓIS
=========================================================

private Texture imgLlama;
private Texture imgNinja;
private Texture imgMage;
private Texture imgRobo;

Essas imagens serão mostradas no quadro grande.

=========================================================
5. CRIANDO OS BOTÕES
=========================================================

private Botao btnLlama;
private Botao btnNinja;
private Botao btnMage;
private Botao btnRobo;

No construtor:

imgLlama = new Texture("llama.png");
imgNinja = new Texture("llamaNinja.png");
imgMage = new Texture("llamaMage.png");
imgRobo = new Texture("llamaRobo.png");

btnLlama = new Botao(
    new Texture("slotLlama.png"),
    new Texture("slotLlamaHover.png"),
    20, 700, 200, 200
);

btnNinja = new Botao(
    new Texture("slotNinja.png"),
    new Texture("slotNinjaHover.png"),
    20, 450, 200, 200
);

btnMage = new Botao(
    new Texture("slotMage.png"),
    new Texture("slotMageHover.png"),
    270, 700, 200, 200
);

btnRobo = new Botao(
    new Texture("slotRobo.png"),
    new Texture("slotRoboHover.png"),
    270, 450, 200, 200
);

=========================================================
6. DEFINIR UMA IMAGEM INICIAL
=========================================================

No construtor, escolha a primeira imagem que já aparece.

imgSelecionada = imgLlama;

Assim o quadrado grande não fica vazio.

=========================================================
7. ATUALIZAR POSIÇÃO DO MOUSE
=========================================================

No render():

posMouse = viewport.unproject(
    new Vector2(Gdx.input.getX(), Gdx.input.getY())
);

Isso precisa acontecer em todo frame.

=========================================================
8. DETECTAR CLIQUE NOS BOTÕES
=========================================================

if (Gdx.input.justTouched()) {

    if (btnLlama.foiClicado(posMouse)) {
        imgSelecionada = imgLlama;
    }

    if (btnNinja.foiClicado(posMouse)) {
        imgSelecionada = imgNinja;
    }

    if (btnMage.foiClicado(posMouse)) {
        imgSelecionada = imgMage;
    }

    if (btnRobo.foiClicado(posMouse)) {
        imgSelecionada = imgRobo;
    }
}

=========================================================
9. DESENHAR OS BOTÕES
=========================================================

batch.begin();

btnLlama.exibir(batch, posMouse);
btnNinja.exibir(batch, posMouse);
btnMage.exibir(batch, posMouse);
btnRobo.exibir(batch, posMouse);

batch.end();

=========================================================
10. DESENHAR A IMAGEM GRANDE
=========================================================

Ainda no render(), desenhamos a imagem atual:

batch.draw(imgSelecionada, 1050, 600, 350, 300);

Essa linha sempre usa a textura guardada em imgSelecionada.

=========================================================
11. EXEMPLO COMPLETO DA LÓGICA
=========================================================

private Texture imgLlama;
private Texture imgNinja;
private Texture imgMage;
private Texture imgRobo;

private Texture imgSelecionada;

private Botao btnLlama;
private Botao btnNinja;
private Botao btnMage;
private Botao btnRobo;

private Vector2 posMouse = new Vector2();

public HeroScreen(Main game) {

    imgLlama = new Texture("llama.png");
    imgNinja = new Texture("llamaNinja.png");
    imgMage = new Texture("llamaMage.png");
    imgRobo = new Texture("llamaRobo.png");

    imgSelecionada = imgLlama;

    btnLlama = new Botao(
        new Texture("slotLlama.png"),
        new Texture("slotLlamaHover.png"),
        20, 700, 200, 200
    );

    btnNinja = new Botao(
        new Texture("slotNinja.png"),
        new Texture("slotNinjaHover.png"),
        20, 450, 200, 200
    );

    btnMage = new Botao(
        new Texture("slotMage.png"),
        new Texture("slotMageHover.png"),
        270, 700, 200, 200
    );

    btnRobo = new Botao(
        new Texture("slotRobo.png"),
        new Texture("slotRoboHover.png"),
        270, 450, 200, 200
    );
}

@Override
public void render(float delta) {

    posMouse = viewport.unproject(
        new Vector2(Gdx.input.getX(), Gdx.input.getY())
    );

    if (Gdx.input.justTouched()) {

        if (btnLlama.foiClicado(posMouse)) {
            imgSelecionada = imgLlama;
        }

        if (btnNinja.foiClicado(posMouse)) {
            imgSelecionada = imgNinja;
        }

        if (btnMage.foiClicado(posMouse)) {
            imgSelecionada = imgMage;
        }

        if (btnRobo.foiClicado(posMouse)) {
            imgSelecionada = imgRobo;
        }
    }

    batch.begin();

    btnLlama.exibir(batch, posMouse);
    btnNinja.exibir(batch, posMouse);
    btnMage.exibir(batch, posMouse);
    btnRobo.exibir(batch, posMouse);

    batch.draw(imgSelecionada, 1050, 600, 350, 300);

    batch.end();
}

=========================================================
12. VANTAGEM DESSA FORMA
=========================================================

Sem classe Botao:
- você cria vários Rectangle
- vários batch.draw
- vários contains

Com classe Botao:
- cada quadrado já sabe se foi clicado
- o código fica mais limpo
- fica mais fácil adicionar novos heróis

=========================================================
13. MELHORIA PROFISSIONAL
=========================================================

Em vez de usar várias variáveis separadas, você pode usar arrays:

Texture[] imagensHerois;
Botao[] botoesHerois;

int indiceSelecionado = 0;

Ao clicar:
indiceSelecionado = i;

Ao desenhar:
batch.draw(imagensHerois[indiceSelecionado], 1050, 600, 350, 300);

Isso é melhor quando houver muitos heróis.

=========================================================
14. RESUMO FINAL
=========================================================

Botao clicado
    ↓
troca a Texture guardada em imgSelecionada
    ↓
o batch.draw(imgSelecionada, ...) mostra a nova imagem

Ou seja:
o botão não troca a imagem diretamente;
ele apenas muda a variável que o quadro grande usa.

=========================================================
FIM
=========================================================
*/
/*
=========================================================
DICIONÁRIO LIBGDX
CRIAÇÃO E ADMINISTRAÇÃO DE ARRAYS
=========================================================

Arrays permitem armazenar vários objetos do mesmo tipo
em uma única estrutura.

Em jogos LibGDX, arrays são usados para:

- heróis
- inimigos
- torres
- botões
- projéteis
- animações

=========================================================
1. ARRAY SIMPLES
=========================================================

Estrutura:

Tipo[] nomeArray;

Exemplo:

Texture[] imagensHerois;

=========================================================
2. CRIAR ARRAY
=========================================================

Exemplo com 4 heróis:

Texture[] imagensHerois = new Texture[4];

Isso cria espaço para 4 texturas.

=========================================================
3. INSERIR ELEMENTOS
=========================================================

imagensHerois[0] = new Texture("llama.png");
imagensHerois[1] = new Texture("llamaNinja.png");
imagensHerois[2] = new Texture("llamaMage.png");
imagensHerois[3] = new Texture("llamaRobo.png");

=========================================================
4. ACESSAR ELEMENTOS
=========================================================

batch.draw(imagensHerois[0], x, y);

=========================================================
5. USANDO ARRAY COM BOTÕES
=========================================================

Botao[] botoesHerois = new Botao[4];

botoesHerois[0] = new Botao(...);
botoesHerois[1] = new Botao(...);

=========================================================
6. PERCORRER ARRAY (LOOP)
=========================================================

for(int i = 0; i < imagensHerois.length; i++){

    batch.draw(imagensHerois[i], x, y);

}

=========================================================
7. ARRAY COM SELEÇÃO
=========================================================

int heroiSelecionado = 0;

Quando clicar:

heroiSelecionado = i;

Desenhar:

batch.draw(imagensHerois[heroiSelecionado], 1050,600,350,300);

=========================================================
8. DISPOSE EM ARRAYS
=========================================================

for(Texture img : imagensHerois){
    img.dispose();
}

=========================================================
9. VANTAGENS DOS ARRAYS
=========================================================

✔ menos código repetido
✔ fácil adicionar novos elementos
✔ lógica mais organizada
✔ usado em sistemas profissionais

=========================================================
FIM
=========================================================
*/
/*
=========================================================
DICIONÁRIO LIBGDX
BOTÕES QUE TROCAM IMAGEM E BACKGROUND CIRCULAR
=========================================================

Objetivo:

Quando clicar em um botão:

- trocar a imagem principal
- trocar o background circular atrás da imagem
- usar camadas (layers)
- aplicar transparência
- aplicar gradiente

=========================================================
1. CONCEITO DE CAMADAS
=========================================================

A ordem de desenho determina as camadas.

Exemplo:

batch.draw(background);
batch.draw(circulo);
batch.draw(personagem);

Último desenhado aparece na frente.

=========================================================
2. IMAGENS NECESSÁRIAS
=========================================================

Texture[] imagensHerois;
Texture[] backgroundsCirculares;

Exemplo:

imagensHerois[0] = new Texture("llama.png");
backgroundsCirculares[0] = new Texture("circleGreen.png");

=========================================================
3. BOTÕES
=========================================================

Botao[] botoesHerois = new Botao[4];

Cada botão seleciona um herói.

=========================================================
4. VARIÁVEL DE SELEÇÃO
=========================================================

int heroiSelecionado = 0;

=========================================================
5. DETECTAR CLIQUE
=========================================================

if(Gdx.input.justTouched()){

    for(int i = 0; i < botoesHerois.length; i++){

        if(botoesHerois[i].foiClicado(posMouse)){

            heroiSelecionado = i;

        }

    }

}

=========================================================
6. DESENHAR BACKGROUND CIRCULAR
=========================================================

batch.draw(
    backgroundsCirculares[heroiSelecionado],
    1020,570,
    410,360
);

=========================================================
7. DESENHAR PERSONAGEM
=========================================================

batch.draw(
    imagensHerois[heroiSelecionado],
    1050,600,
    350,300
);

=========================================================
8. TRANSPARÊNCIA
=========================================================

batch.setColor(1,1,1,0.6f);

batch.draw(backgroundCircular,...);

batch.setColor(1,1,1,1);

=========================================================
9. GRADIENTE
=========================================================

Gradientes normalmente são feitos em:

- Photoshop
- Aseprite
- Illustrator

Depois exportados como PNG.

=========================================================
10. RESULTADO
=========================================================

Botão clicado
↓
índice selecionado muda
↓
imagem e fundo circular trocam

=========================================================
FIM
=========================================================
*/
/*
=========================================================
DICIONÁRIO LIBGDX
BORDAS EM BOTÕES
=========================================================

Bordas em botões podem ser feitas de várias formas.

Métodos principais:

1. imagem com borda
2. ShapeRenderer
3. múltiplas camadas

=========================================================
1. BORDA COM IMAGEM (MAIS USADO)
=========================================================

Criar um PNG com:

- borda arredondada
- fundo transparente

Exemplo:

play_button.png

Depois:

batch.draw(botao, x, y);

=========================================================
2. BORDA COM SHAPERENDERER
=========================================================

shape.begin(ShapeRenderer.ShapeType.Line);

shape.setColor(Color.WHITE);

shape.rect(x,y,width,height);

shape.end();

=========================================================
3. BORDA COM CAMADAS
=========================================================

Desenhar duas imagens:

fundo
↓
botão

Exemplo:

batch.draw(borda, x-5, y-5, width+10, height+10);
batch.draw(botao, x, y, width, height);

=========================================================
4. ARREDONDAMENTO
=========================================================

Arredondamento geralmente é feito:

- no software gráfico
- exportado como PNG

=========================================================
5. COR DO BOTÃO
=========================================================

LibGDX permite mudar cor:

batch.setColor(0.8f,0.8f,1f,1);

batch.draw(botao,...);

batch.setColor(1,1,1,1);

=========================================================
6. BOTÃO COM HOVER
=========================================================

if(botao.estaSobre(mouse)){

    batch.setColor(1.2f,1.2f,1.2f,1);

}

=========================================================
7. BOTÃO COM SOMBRA
=========================================================

batch.draw(sombra, x+5, y-5, width, height);

batch.draw(botao, x, y, width, height);

=========================================================
8. BORDA PROFISSIONAL
=========================================================

Jogos profissionais usam:

NinePatch

Exemplo:

NinePatch patch = new NinePatch(texture, 10,10,10,10);

=========================================================
9. RESULTADO
=========================================================

Borda
↓
Botão
↓
Texto

=========================================================
FIM
=========================================================
*/


/*
=========================================================
DICIONÁRIO LIBGDX
COMBOBOX / SELECTBOX
CRIAÇÃO, ADMINISTRAÇÃO E TROCA DE VALORES
=========================================================

OBSERVAÇÃO IMPORTANTE:
No LibGDX, o componente equivalente ao "ComboBox" do Java Swing
normalmente é o:

SelectBox

Ou seja:
- Swing usa JComboBox
- LibGDX usa SelectBox

Então, quando falamos de "ComboBox no LibGDX",
na prática estamos falando de SelectBox.

=========================================================
1. O QUE É UM SELECTBOX
=========================================================

É um componente de interface que mostra:

- um valor selecionado atualmente
- uma lista de opções ao clicar

Exemplo:

[ Clássico ▼ ]

Ao clicar, ele abre:

- Clássico
- Aéreo
- Suporte
- Lendas

=========================================================
2. QUANDO USAR
=========================================================

Use SelectBox quando quiser:

- filtrar heróis
- escolher categoria
- escolher dificuldade
- escolher mapa
- escolher idioma
- escolher skin

Exemplo no seu jogo:
- Clássico -> mostra heróis clássicos
- Aéreo -> mostra heróis aéreos
- Suporte -> mostra heróis de suporte

=========================================================
3. BIBLIOTECAS NECESSÁRIAS
=========================================================

Para usar SelectBox, normalmente você usa Scene2D UI.

Imports comuns:

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

=========================================================
4. O QUE É STAGE
=========================================================

Stage é o "palco" da interface.

Ele gerencia:
- botões
- labels
- textfields
- selectbox
- cliques
- foco

Sem Stage, componentes de UI do Scene2D não funcionam direito.

=========================================================
5. O QUE É SKIN
=========================================================

Skin define o visual dos componentes.

Exemplo:
- fonte
- cor
- estilo
- fundo
- dropdown

Você normalmente carrega um arquivo .json de skin.

Exemplo muito comum:

Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

Esse skin costuma vir em projetos LibGDX de exemplo.

=========================================================
6. CRIAÇÃO BÁSICA DO STAGE
=========================================================

Variáveis da classe:

private Stage stage;
private Skin skin;
private SelectBox<String> comboClasse;

No construtor ou show():

stage = new Stage(new ScreenViewport());
skin = new Skin(Gdx.files.internal("uiskin.json"));

=========================================================
7. ATIVAR INPUT DO STAGE
=========================================================

Sem isso, o SelectBox não recebe clique.

Gdx.input.setInputProcessor(stage);

=========================================================
8. CRIAÇÃO DO SELECTBOX
=========================================================

comboClasse = new SelectBox<>(skin);

Aqui ele foi criado usando o skin.

=========================================================
9. ADICIONAR ITENS AO SELECTBOX
=========================================================

comboClasse.setItems(
    "Clássico",
    "Aéreo",
    "Suporte",
    "Lendas"
);

Agora ele já tem as opções.

=========================================================
10. DEFINIR POSIÇÃO E TAMANHO
=========================================================

comboClasse.setSize(250, 60);
comboClasse.setPosition(600, 900);

=========================================================
11. ADICIONAR AO STAGE
=========================================================

stage.addActor(comboClasse);

Sem isso ele não aparece na tela.

=========================================================
12. DESENHAR O STAGE
=========================================================

No render():

stage.act(delta);
stage.draw();

act(delta):
- atualiza a UI

draw():
- desenha a UI

=========================================================
13. EXEMPLO COMPLETO BÁSICO
=========================================================

private Stage stage;
private Skin skin;
private SelectBox<String> comboClasse;

public HeroScreen(Main game) {

    stage = new Stage(new ScreenViewport());
    skin = new Skin(Gdx.files.internal("uiskin.json"));

    comboClasse = new SelectBox<>(skin);
    comboClasse.setItems("Clássico", "Aéreo", "Suporte", "Lendas");
    comboClasse.setSize(250, 60);
    comboClasse.setPosition(600, 900);

    stage.addActor(comboClasse);

    Gdx.input.setInputProcessor(stage);
}

@Override
public void render(float delta) {

    ScreenUtils.clear(0, 0, 0, 1);

    stage.act(delta);
    stage.draw();
}

=========================================================
14. COMO PEGAR O VALOR SELECIONADO
=========================================================

Use:

comboClasse.getSelected()

Exemplo:

String valor = comboClasse.getSelected();

Se o usuário escolheu "Aéreo",
esse método retorna:

"Aéreo"

=========================================================
15. REAGIR À MUDANÇA DE VALOR
=========================================================

O jeito mais correto é usar ChangeListener.

comboClasse.addListener(new ChangeListener() {
    @Override
    public void changed(ChangeEvent event, Actor actor) {

        String selecionado = comboClasse.getSelected();

        System.out.println("Selecionado: " + selecionado);
    }
});

=========================================================
16. EXEMPLO PRÁTICO DE TROCA DE VALORES
=========================================================

Imagine que você tem 4 categorias de heróis:

- Clássico
- Aéreo
- Suporte
- Lendas

E quer que o SelectBox troque os heróis exibidos.

Você pode criar uma variável:

private String categoriaAtual = "Clássico";

Depois:

comboClasse.addListener(new ChangeListener() {
    @Override
    public void changed(ChangeEvent event, Actor actor) {

        categoriaAtual = comboClasse.getSelected();
    }
});

Agora o valor muda automaticamente.

=========================================================
17. APLICAÇÃO PRÁTICA: MOSTRAR HERÓIS DIFERENTES
=========================================================

Você pode ter arrays separados:

private Texture[] heroisClassicos;
private Texture[] heroisAereos;
private Texture[] heroisSuporte;
private Texture[] heroisLendas;

No render():

if(categoriaAtual.equals("Clássico")) {
    // desenha os clássicos
}

if(categoriaAtual.equals("Aéreo")) {
    // desenha os aéreos
}

if(categoriaAtual.equals("Suporte")) {
    // desenha os suportes
}

if(categoriaAtual.equals("Lendas")) {
    // desenha os lendários
}

=========================================================
//18. EXEMPLO PRÁTICO COM IF
=========================================================

comboClasse.addListener(new ChangeListener() {
    @Override
    public void changed(ChangeEvent event, Actor actor) {

        String selecionado = comboClasse.getSelected();

        if(selecionado.equals("Clássico")) {
            categoriaAtual = "Clássico";
        }

        if(selecionado.equals("Aéreo")) {
            categoriaAtual = "Aéreo";
        }

        if(selecionado.equals("Suporte")) {
            categoriaAtual = "Suporte";
        }

        if(selecionado.equals("Lendas")) {
            categoriaAtual = "Lendas";
        }
    }
});

=========================================================
19. MELHOR FORMA: USAR ENUM
=========================================================

Em vez de String, é mais profissional usar enum.

Exemplo:

public enum CategoriaHeroi {
    CLASSICO,
    AEREO,
    SUPORTE,
    LENDAS
}

Depois:

private CategoriaHeroi categoriaAtual = CategoriaHeroi.CLASSICO;

=========================================================
20. SELECTBOX COM ENUM
=========================================================

Você pode fazer assim:

private SelectBox<CategoriaHeroi> comboClasse;

comboClasse = new SelectBox<>(skin);
comboClasse.setItems(
    CategoriaHeroi.CLASSICO,
    CategoriaHeroi.AEREO,
    CategoriaHeroi.SUPORTE,
    CategoriaHeroi.LENDAS
);

=========================================================
21. PEGAR ENUM SELECIONADO
=========================================================

categoriaAtual = comboClasse.getSelected();

Agora fica mais seguro que usar String.

=========================================================
22. APLICAÇÃO COM ENUM
=========================================================

switch(categoriaAtual) {

    case CLASSICO:
        // desenha clássicos
        break;

    case AEREO:
        // desenha aéreos
        break;

    case SUPORTE:
        // desenha suportes
        break;

    case LENDAS:
        // desenha lendas
        break;
}

=========================================================
23. EXEMPLO COMPLETO COM ENUM
=========================================================

public enum CategoriaHeroi {
    CLASSICO,
    AEREO,
    SUPORTE,
    LENDAS
}

private Stage stage;
private Skin skin;
private SelectBox<CategoriaHeroi> comboClasse;
private CategoriaHeroi categoriaAtual = CategoriaHeroi.CLASSICO;

public HeroScreen(Main game) {

    stage = new Stage(new ScreenViewport());
    skin = new Skin(Gdx.files.internal("uiskin.json"));

    comboClasse = new SelectBox<>(skin);
    comboClasse.setItems(
        CategoriaHeroi.CLASSICO,
        CategoriaHeroi.AEREO,
        CategoriaHeroi.SUPORTE,
        CategoriaHeroi.LENDAS
    );

    comboClasse.setSize(250, 60);
    comboClasse.setPosition(600, 900);

    comboClasse.addListener(new ChangeListener() {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
            categoriaAtual = comboClasse.getSelected();
        }
    });

    stage.addActor(comboClasse);
    Gdx.input.setInputProcessor(stage);
}

@Override
public void render(float delta) {

    ScreenUtils.clear(0, 0, 0, 1);

    batch.begin();

    switch(categoriaAtual) {

        case CLASSICO:
            // desenha heróis clássicos
            break;

        case AEREO:
            // desenha heróis aéreos
            break;

        case SUPORTE:
            // desenha heróis de suporte
            break;

        case LENDAS:
            // desenha heróis lendários
            break;
    }

    batch.end();

    stage.act(delta);
    stage.draw();
}

=========================================================
24. COMO ADMINISTRAR O SELECTBOX
=========================================================

Administrar significa:

- criar
- posicionar
- definir valores
- detectar mudança
- usar o valor selecionado
- liberar memória

Resumo:

1. cria Stage
2. cria Skin
3. cria SelectBox
4. adiciona itens
5. adiciona listener
6. adiciona no Stage
7. desenha o Stage
8. usa o valor selecionado

=========================================================
25. COMO TROCAR OS BOTÕES/HERÓIS EXIBIDOS
=========================================================

Exemplo com arrays:

private Botao[] botoesClassicos;
private Botao[] botoesAereos;
private Botao[] botoesSuporte;
private Botao[] botoesLendas;

No render:

switch(categoriaAtual) {

    case CLASSICO:
        for(Botao b : botoesClassicos) {
            b.Exibir(batch, posMouse);
        }
        break;

    case AEREO:
        for(Botao b : botoesAereos) {
            b.Exibir(batch, posMouse);
        }
        break;

    case SUPORTE:
        for(Botao b : botoesSuporte) {
            b.Exibir(batch, posMouse);
        }
        break;

    case LENDAS:
        for(Botao b : botoesLendas) {
            b.Exibir(batch, posMouse);
        }
        break;
}

=========================================================
26. COMO TROCAR OS HERÓIS DISPONÍVEIS
=========================================================

Exemplo real:

Se categoriaAtual = CLASSICO
→ aparecem Llama, Mage, Ninja

Se categoriaAtual = AEREO
→ aparecem heróis voadores

Se categoriaAtual = SUPORTE
→ aparecem buffers/healers

Ou seja:
o SelectBox não desenha os heróis sozinho.
Ele apenas muda uma variável.
Seu código usa essa variável para decidir o que desenhar.

=========================================================
27. RESUMO DA LÓGICA
=========================================================

SelectBox
↓
usuário escolhe categoria
↓
listener detecta mudança
↓
categoriaAtual muda
↓
render() desenha outro grupo de heróis

=========================================================
28. RESIZE
=========================================================

No resize:

@Override
public void resize(int width, int height) {
    stage.getViewport().update(width, height, true);
}

=========================================================
29. DISPOSE
=========================================================

No dispose:

@Override
public void dispose() {
    stage.dispose();
    skin.dispose();
}

IMPORTANTE:
Stage e Skin também ocupam memória.

=========================================================
30. ERROS COMUNS
=========================================================

ERRO:
O SelectBox não responde clique.

CAUSA:
faltou:
Gdx.input.setInputProcessor(stage);

-----------------------------------------

ERRO:
O SelectBox não aparece.

CAUSA:
faltou:
stage.addActor(comboClasse);

-----------------------------------------

ERRO:
O SelectBox aparece, mas não atualiza.

CAUSA:
faltou:
stage.act(delta);
stage.draw();

-----------------------------------------

ERRO:
O jogo desenha por cima do SelectBox.

CAUSA:
ordem de render incorreta.

Normalmente:
1. desenha jogo/hud com batch
2. desenha stage por último

-----------------------------------------

ERRO:
O estilo está feio ou quebrado.

CAUSA:
skin ausente ou incompatível.

=========================================================
31. ORDEM CORRETA NO RENDER
=========================================================

@Override
public void render(float delta) {

    ScreenUtils.clear(0, 0, 0, 1);

    // desenha fundo, heróis, botões, etc
    batch.begin();
    // ...
    batch.end();

    // desenha UI por cima
    stage.act(delta);
    stage.draw();
}

=========================================================
32. QUANDO VALE A PENA USAR SELECTBOX
=========================================================

Vale a pena quando:

- você quer uma lista compacta
- há várias categorias
- o usuário precisa filtrar opções
- você quer algo mais organizado que vários botões

=========================================================
33. QUANDO NÃO VALE A PENA
=========================================================

Talvez não valha se:

- há poucas opções e botões grandes funcionam melhor
- a UI precisa ser muito estilizada
- você quer um menu visual bem customizado

Nesses casos, às vezes é melhor usar seus próprios botões.

=========================================================
34. EXEMPLO PRÁTICO DE FILTRO DE HERÓIS
=========================================================

Imagine:

comboClasse = [ Clássico ▼ ]

Se escolher "Aéreo":
- some a lista de clássicos
- aparece lista de aéreos

Isso pode ser feito assim:

if(categoriaAtual == CategoriaHeroi.AEREO) {
    // exibe botoes dos aéreos
}

=========================================================
35. IDEIA FINAL
=========================================================

SelectBox é um controlador de estado.

Ele não "troca imagens" sozinho.
Ele apenas muda o valor selecionado.
Seu código usa esse valor para:

- trocar heróis
- trocar botões
- trocar backgrounds
- trocar stats
- trocar filtros

=========================================================
FIM
=========================================================
*/

/*

---------------- SOMBRA E PROFUNDIDADE -----------------

// sombra (desenha antes)
shapeRenderer.setColor(0, 0, 0, 0.3f);
shapeRenderer.rect(x+3, y-3, width, height);

// botão
shapeRenderer.setColor(Color.RED);
shapeRenderer.rect(x, y, width, height);

---------------- GRADIENTE ---------------
for (int y = 0; y < altura; y++) {
    float t = (float)y / altura;
    pixmap.setColor(
        Color.RED.r * (1-t) + Color.DARK_GRAY.r * t,
        Color.RED.g * (1-t) + Color.DARK_GRAY.g * t,
        Color.RED.b * (1-t) + Color.DARK_GRAY.b * t,
        1
    );
    pixmap.drawLine(0, y, largura, y);
}

---------------- BORDA E OUTLINE ---------------
pixmap.setColor(Color.BLACK);
pixmap.drawRectangle(0, 0, largura, altura);

---------------- ANIMAÇÃO ----------------
botao.addListener(new ClickListener() {
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        botao.addAction(Actions.scaleTo(1.05f, 1.05f, 0.1f));
    }

    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        botao.addAction(Actions.scaleTo(1f, 1f, 0.1f));
    }
});

---------------- CONTAINER ---------------
Table panel = new Table();

// fundo gerado via Pixmap
panel.setBackground(new TextureRegionDrawable(new TextureRegion(criarFundo())));

--------------- LAYOUT --------------
table.add(labelNome).left().pad(10);
table.add(valor).right().pad(10);
table.row();

Nome à esquerda / valor à direita = padrão de jogo

------------- SLIDERS -------------
style.background = drawableBarra;
style.knob = drawableBolinha;
style.knobBefore = drawablePreenchido;


---------------- SHADER --------------------
🎨 1. BLUR REAL (Shader)
🧠 Ideia

Você:

Renderiza a tela em uma textura
Aplica um shader de blur nessa textura
Desenha ela de volta

👉 Isso cria aquele efeito de menu com fundo desfocado

🔧 Passo 1 – FrameBuffer

No MenuScreen:

private FrameBuffer fbo;
private Texture fboTexture;
private ShaderProgram blurShader;

No construtor:

fbo = new FrameBuffer(Pixmap.Format.RGBA8888, 1920, 1080, false);
🔧 Passo 2 – Shader (blur simples)
Vertex shader (blur.vert)
attribute vec4 a_position;
attribute vec2 a_texCoord0;

varying vec2 v_texCoord;

void main() {
    v_texCoord = a_texCoord0;
    gl_Position = a_position;
}
Fragment shader (blur.frag)
#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoord;
uniform sampler2D u_texture;

void main() {
    float offset = 1.0 / 300.0;

    vec4 color = vec4(0.0);

    color += texture2D(u_texture, v_texCoord + vec2(-offset, 0.0)) * 0.25;
    color += texture2D(u_texture, v_texCoord) * 0.5;
    color += texture2D(u_texture, v_texCoord + vec2(offset, 0.0)) * 0.25;

    gl_FragColor = color;
}
🔧 Passo 3 – Carregar shader
blurShader = new ShaderProgram(
    Gdx.files.internal("blur.vert"),
    Gdx.files.internal("blur.frag")
);

if (!blurShader.isCompiled()) {
    System.out.println(blurShader.getLog());
}
🔧 Passo 4 – Usar no render
1. Renderiza fundo no FBO:
fbo.begin();
ScreenUtils.clear(0, 0, 0, 1);

batch.begin();
batch.draw(HUDimg[0], 0, 0, 1920, 1080);
batch.end();

fbo.end();
2. Pega textura:
fboTexture = fbo.getColorBufferTexture();
3. Desenha com shader:

batch.begin();
batch.draw(fboTexture, 0, 0, 1920, 1080, 0, 0, 1, 1);
batch.end();

batch.setShader(null);
🔥 Resultado

👉 Fundo fica desfocado quando popup abre
👉 Parece jogo profissional na hora

✨ 2. POLIMENTO AAA (UI PROFISSIONAL)

Agora vem o que faz teu jogo parecer “não é PNG jogado”

🎯 2.1 Glow no hover

No Botao.Exibir:

if (hoverAtivo) {
    batch.setColor(1, 1, 1, 1.2f); // brilho leve
} else {
    batch.setColor(1, 1, 1, 1f);
}
🎯 2.2 Sombra dinâmica (melhorada)

No MenuScreen:

// sombra suave
shapeRenderer.setColor(0, 0, 0, 0.08f);
shapeRenderer.rect(r.x + 6, r.y - 6, r.width, r.height);

// sombra principal
shapeRenderer.setColor(0, 0, 0, 0.2f);
shapeRenderer.rect(r.x + 3, r.y - 3, r.width, r.height);
🎯 2.3 Animação de entrada (ESSENCIAL)

No botão:

private float alpha = 0f;

No render:

alpha += (1f - alpha) * 0.05f;
batch.setColor(1, 1, 1, alpha);

👉 Botões “aparecem suavemente”

🎯 2.4 Parallax (nível absurdo)

No fundo:

float offsetX = (posMouse.x - 960) * 0.01f;
float offsetY = (posMouse.y - 540) * 0.01f;

batch.draw(HUDimg[0], offsetX, offsetY, 1920, 1080);

👉 Fundo se mexe com o mouse
👉 Dá profundidade absurda

🎯 2.5 Botão “vivo” (o mais importante)

Você já começou com scale — agora deixa perfeito:

scaleAtual += (targetScale - scaleAtual) * 0.15f;

👉 mais responsivo
👉 sensação “snappy”


*/
