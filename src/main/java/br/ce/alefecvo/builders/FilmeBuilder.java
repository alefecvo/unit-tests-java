package br.ce.alefecvo.builders;

import br.ce.alefecvo.entidades.Filme;

public class FilmeBuilder {

    private Filme filme;

    private FilmeBuilder(){
    }

    public static FilmeBuilder umFilme(){
        FilmeBuilder builder = new FilmeBuilder();
        builder.filme = new Filme();
        builder.filme.setNome("Um filme");
        builder.filme.setEstoque(2);
        builder.filme.setPrecoLocacao(5.0);

        return builder;
    }

    public FilmeBuilder comNome(String valor){
        this.filme.setNome(valor);
        return this;
    }

    public FilmeBuilder semEstoque(){
        this.filme.setEstoque(0);
        return this;
    }

    public FilmeBuilder comEstoque(Integer valor){
        this.filme.setEstoque(valor);
        return this;
    }

    public FilmeBuilder semPrecoLocacao(){
        this.filme.setPrecoLocacao(0.0);
        return this;
    }

    public FilmeBuilder comPrecoLocacao(Double valor){
        this.filme.setPrecoLocacao(valor);
        return this;
    }


    public Filme agora(){
        return filme;
    }

}
