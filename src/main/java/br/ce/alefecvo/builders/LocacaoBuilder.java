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
        builder.locacao = new Locacao();
        builder.locacao.setUsuario(umUsuario().agora());
        builder.locacao.setFilme(List.of(umFilme().agora()));
        builder.locacao.setDataLocacao(new Date());
        builder.locacao.setDataLocacao(DataUtils.obterDataComDiferencaDias(1));
        builder.locacao.setValor(4.0);

        return builder;
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

}
