package br.ce.alefecvo.servicos;

import static br.ce.alefecvo.utils.DataUtils.adicionarDias;

import java.util.Date;
import java.util.List;

import br.ce.alefecvo.daos.LocacaoDAO;
import br.ce.alefecvo.entidades.Filme;
import br.ce.alefecvo.entidades.Locacao;
import br.ce.alefecvo.entidades.Usuario;

public class LocacaoService {
	private LocacaoDAO locacaoDAO;
	private SpcService spcService;

	private EmailService emailService;

	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws Exception {
		if(usuario == null)
			throw new Exception("Usuário vazio");

		if(filmes == null || filmes.isEmpty())
			throw new Exception("Filme vazio");

		for (Filme filme: filmes) {
			if(filme.getEstoque() == 0)
				throw new Exception("Filme sem estoque");
		}

		if (spcService.possuiNegativacao(usuario)){
			throw new Exception("Usuário negativado");
		}

		Locacao locacao = new Locacao();
		locacao.setFilme(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());

		Double valorLocacao = 0d;
		for (Filme filme: filmes) {
			valorLocacao += filme.getPrecoLocacao();
		}
		locacao.setValor(valorLocacao);

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		locacaoDAO.salvar(locacao);
		
		return locacao;
	}

	public void notificarAtrasos(){
		List<Locacao> locacoes = locacaoDAO.obterLocacoesPendentes();
		for (Locacao locacao: locacoes) {
			emailService.notificarAtraso(locacao.getUsuario());

		}
	}


	public void setLocacaoDAO(LocacaoDAO locacaoDAO) {
		this.locacaoDAO = locacaoDAO;
	}

	public void setSpcService(SpcService spcService){
		this.spcService = spcService;
	}
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

}