package br.ce.alefecvo.daos;

import br.ce.alefecvo.entidades.Locacao;

import java.util.List;

public interface LocacaoDAO {
    public void salvar(Locacao locacao);

    List<Locacao> obterLocacoesPendentes();
}
