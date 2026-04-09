    package com.TowerDefense.jogo;

    import com.badlogic.gdx.Gdx;
    import com.badlogic.gdx.graphics.Color;
    import com.badlogic.gdx.graphics.GL20;
    import com.badlogic.gdx.graphics.Texture;
    import com.badlogic.gdx.graphics.g2d.BitmapFont;
    import com.badlogic.gdx.graphics.g2d.SpriteBatch;
    import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
    import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
    import com.badlogic.gdx.math.Matrix4;
    import com.badlogic.gdx.math.Rectangle;

    public class PainelSkins {

        // Cria os "Estados" do painel
        public enum AbaAtiva {
            NENHUMA, // Fechado
            SKINS,   // Aba de Skins aberta
            INFOS    // Aba de Infos aberta
        }

        private AbaAtiva abaAtual; // Controla o que está aparecendo

        //----------------------- VARIÁVEIS GLOBAIS ------------------------
        private final ShapeRenderer shapeRenderer; //Variável que permite desenhar na tela (formas)
        private final BitmapFont fonte; //Variável que permite desenhar texto
        // Variáveis que agora podem ser acessadas por qualquer função:
        private float larguraTela, alturaTela, mouseX, mouseY;

        //----------------------- ARRAYS ------------------------


        //---------------------- OUTRAS VARIÁVEIS -----------------
        private boolean aberto; //Inicializa a variável que indica se o popup está aberto ou fechado
        private Rectangle areaPainel; //Variáveis que armazenam as posições do popup e dos botões
        BitmapFont fonteTitulo;
        BitmapFont fonteNormal;

        public PainelSkins() {
            aberto = false;
            shapeRenderer = new ShapeRenderer();
            larguraTela = Gdx.graphics.getWidth();
            alturaTela = Gdx.graphics.getHeight();
            abaAtual = AbaAtiva.INFOS;

            desenharPainel();

            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/Raleway-Regular.ttf")
            );
            FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();

            // CONFIGURAÇÕES IMPORTANTES
            param.size = 28; // tamanho da fonte (ajusta aqui)
            param.color = Color.WHITE;

            // deixa suave (ANTI-SERRILHADO)
            param.minFilter = Texture.TextureFilter.Linear;
            param.magFilter = Texture.TextureFilter.Linear;

            param.borderWidth = 2; // espessura da borda
            param.borderColor = Color.BLACK;

            param.shadowOffsetX = 2;
            param.shadowOffsetY = 2;
            param.shadowColor = new Color(0, 0, 0, 0.5f);

            // gera a fonte
            fonte = generator.generateFont(param);

            // TÍTULO
            param.size = 32;
            fonteTitulo = generator.generateFont(param);

            // TEXTO NORMAL
            param.size = 22;
            fonteNormal = generator.generateFont(param);

            // limpa memória
            generator.dispose();
        }

        public void render(SpriteBatch batch) {
            if (abaAtual == AbaAtiva.NENHUMA) return;

            // Se for a aba de SKINS
            if (abaAtual == AbaAtiva.SKINS) {
                fonteTitulo.draw(batch, "SKINS", areaPainel.x + 20, areaPainel.y + areaPainel.height - 20);
                fonteNormal.draw(batch, "Escolha a sua skin favorita!", areaPainel.x + 20, areaPainel.y + areaPainel.height - 70);
                // Aqui você desenha os botões/imagens das skins
            }

            // Se for a aba de INFOS
            else if (abaAtual == AbaAtiva.INFOS) {
                fonteTitulo.draw(batch, "INFORMAÇÕES", areaPainel.x + 20, areaPainel.y + areaPainel.height - 20);
                fonteNormal.draw(batch, "Dano: 100\nVelocidade: Rápida\nAlcance: Curto", areaPainel.x + 20, areaPainel.y + areaPainel.height - 70);
                // Aqui você desenha os status da Llama
            }
        }

        public void renderShapes(Matrix4 projectionMatrix) {
            if (!aberto) return;

            atualizaMouse();

            shapeRenderer.setProjectionMatrix(projectionMatrix);

            //Ativa a opção de blending (Mexer com opacidade)
            Gdx.gl.glEnable(GL20.GL_BLEND);
            // Essa linha é essencial para o fundo ficar transparente (alpha funcionar)
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            shapeRenderer.setColor(0,0,0,0.6f);
            shapeRenderer.rect(areaPainel.x,areaPainel.y,areaPainel.width,areaPainel.height);

            // VOCÊ TINHA ESQUECIDO O shapeRenderer.end() AQUI!
            shapeRenderer.end();

            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

        private void desenharPainel() {
            // Como o botão de Infos está no X=700 e o Select vai até 1400,
            // criei um painel que fica exatamente embaixo deles!
            float panelX = 700;
            float panelY = 80;  // Quase na base da tela
            float panelWidth = 700; // Largura que pega todos os botões
            float panelHeight = 330; // Altura do botão até o fundo

            areaPainel = new Rectangle(panelX, panelY, panelWidth, panelHeight);
        }

        public void detectarClique(float mouseX, float mouseY) {
            if (!aberto) return;
        }

        public void textoCentralizado(String texto, Rectangle area) {

        }

        public void atualizaMouse() {
            // Pega a posição X normal
            mouseX = Gdx.input.getX();

            // Pega a posição Y e inverte para bater com o sistema de desenho do LibGDX
            mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
        }

        public void abrir() {
            aberto = true;
        }

        public void fechar() {
            aberto = false;
        }

        public boolean isAberto() {
            return aberto;
        }
    }
