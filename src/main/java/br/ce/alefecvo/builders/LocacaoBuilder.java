package br.ce.alefecvo.builders;

import br.ce.alefecvo.entidades.Locacao;
import br.ce.alefecvo.entidades.Usuario;
import br.ce.alefecvo.utils.DataUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static br.ce.alefecvo.builders.FilmeBuilder.*;
import static br.ce.alefecvo.builders.UsuarioBuilder.*;

public class LocacaoBuilder {
    private Locacao locacao;

    private LocacaoBuilder(){}

    public static LocacaoBuilder umaLocacao(){
        LocacaoBuilder builder = new LocacaoBuilder();
        inicializarDadosPadroes(builder);

        return builder;
    }

    public static void inicializarDadosPadroes(LocacaoBuilder builder){
        builder.locacao = new Locacao();
        Locacao elemento = builder.locacao;

        elemento.setUsuario(umUsuario().agora());
        elemento.setFilme(List.of(umFilme().agora()));
        elemento.setDataLocacao(new Date());
        elemento.setDataLocacao(DataUtils.obterDataComDiferencaDias(1));
        elemento.setValor(4.0);

    }

    public Locacao agora(){
        return locacao;
    }

    public LocacaoBuilder comDataRetorno(Date valor) {
        this.locacao.setDataLocacao(valor);
        return this;
    }

    public LocacaoBuilder comUsuario(Usuario valor) {
        this.locacao.setUsuario(valor);
        return this;
    }

    public LocacaoBuilder atrasada() {
        this.locacao.setDataLocacao(DataUtils.obterDataComDiferencaDias(-4));
        this.locacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(-2));
        return this;
    }

}
