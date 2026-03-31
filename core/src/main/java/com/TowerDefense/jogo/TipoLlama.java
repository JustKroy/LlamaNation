package com.TowerDefense.jogo;

public enum TipoLlama {
    NORMAL("Llama Normal", 50, 5),
    NINJA("Llama Ninja", 150, 3),
    MAGE("Llama Mage", 250, 3),
    CYBORG("Llama Cyborg", 300, 3),
    CHEF("Llama Chef", 2500, 1),
    ANGEL("Llama Angel", 500, 3),
    BURGUESA("Llama Burguesa", 1000, 2),
    NEVES("Llama das Neves", 650, 2);

    public final String nome;
    public final int preco;
    public final int limite;

    TipoLlama(String nome, int preco, int limite) {
        this.nome = nome;
        this.preco = preco;
        this.limite = limite;
    }
}
