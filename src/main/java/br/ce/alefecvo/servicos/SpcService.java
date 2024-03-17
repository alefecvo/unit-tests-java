package br.ce.alefecvo.servicos;

import br.ce.alefecvo.entidades.Usuario;

public interface SpcService {

    boolean possuiNegativacao(Usuario usuario) throws Exception;
}
