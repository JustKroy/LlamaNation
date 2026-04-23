package com.TowerDefense.jogo;

public enum TipoLlama {
    // Adicione o raio como o último número de cada lhama!
    // Exemplo: NOME(preco, limite, raio)
    NORMAL("Normal", 100, 10, 200f),
    NINJA("Ninja", 150, 5, 250f),
    MAGE("Mago", 200, 5, 300f),
    CYBORG("Cyborg", 300, 3, 200f),
    ANGEL("Anjo", 500, 2, 400f),
    BURGUESA("Burguesa", 150, 5, 0f),
    CHEF("Chef", 250, 4, 200f),
    NEVES("Neves", 350, 3, 150f);

    public final String nome;
    public final int preco;
    public final int limite;
    public final float raioInicial;

    // Atualize o construtor para receber o raio
    TipoLlama(String nome, int preco, int limite, float raioInicial) {
        this.nome = nome;
        this.preco = preco;
        this.limite = limite;
        this.raioInicial = raioInicial;
    }
}
