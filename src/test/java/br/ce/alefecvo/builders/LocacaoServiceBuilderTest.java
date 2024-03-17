package br.ce.alefecvo.builders;

import br.ce.alefecvo.daos.LocacaoDAO;
import br.ce.alefecvo.entidades.Filme;
import br.ce.alefecvo.entidades.Locacao;
import br.ce.alefecvo.entidades.Usuario;
import br.ce.alefecvo.matchers.MatchersProprios;
import br.ce.alefecvo.servicos.EmailService;
import br.ce.alefecvo.servicos.LocacaoService;
import br.ce.alefecvo.servicos.SpcService;
import br.ce.alefecvo.utils.DataUtils;
import org.hamcrest.MatcherAssert;
import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.rules.ErrorCollector;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;


public class LocacaoServiceBuilderTest {

    private LocacaoService locacaoService;
    private LocacaoDAO locacaoDAO;
    private SpcService spcService;
    private EmailService emailService;

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @BeforeEach
    public void setup() {
        locacaoService = new LocacaoService();

        //Mockito para mockar dependencia externa LocacaoDAO
        locacaoDAO = Mockito.mock(LocacaoDAO.class);
        locacaoService.setLocacaoDAO(locacaoDAO);

        //Mockito para mockar dependencia externa SpcService
        spcService = Mockito.mock(SpcService.class);
        locacaoService.setSpcService(spcService);

        //Mockito para mockar dependencia externa EmailService
        emailService = Mockito.mock(EmailService.class);
        locacaoService.setEmailService(emailService);

    }

    @Test
    @DisplayName("Deve fazer locação de filme com execeção criada usando Builder")
    public void teste01() {
        //cenário
        Usuario usuario = UsuarioBuilder.umUsuario().nomeUsuario("Lucas").agora();
        List<Filme> filmes = List.of(
                FilmeBuilder.umFilme().comNome("harry potter e a pedra filosofal").comEstoque(2).comPrecoLocacao(5.0).agora(),
                FilmeBuilder.umFilme().comNome("harry potter e camera secreta").comEstoque(3).comPrecoLocacao(15.0).agora());

        //ação
        Locacao locacao;
        try {
            locacao = locacaoService.alugarFilme(usuario, filmes);

            //resultado
            Assertions.assertEquals("harry potter e a pedra filosofal", filmes.get(0).getNome());
            Assertions.assertEquals(2, filmes.get(0).getEstoque());
            Assertions.assertEquals(5.0, filmes.get(0).getPrecoLocacao(), 0.0);

            Assertions.assertEquals("harry potter e camera secreta", filmes.get(1).getNome());
            Assertions.assertEquals(3, filmes.get(1).getEstoque());
            Assertions.assertEquals(15.0, filmes.get(1).getPrecoLocacao(), 0.0);

            Assertions.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
            Assertions.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));

        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Não deveria lancar exceção");
        }
    }

    @Test
    @DisplayName("Deve fazer locação de filme com execeção padrão usando Builder")
    public void teste02() throws Exception {
        //cenário
        Usuario usuario = UsuarioBuilder.umUsuario().nomeUsuario("Mariana").agora();
        List<Filme> filmes = List.of(
                FilmeBuilder.umFilme().comNome("Guerra Mundial Z").comEstoque(2).comPrecoLocacao(5.0).agora()
        );

        //ação
        Locacao locacao = locacaoService.alugarFilme(usuario, filmes);

        //resultado
        //assertEquals and assertTrue
        Assertions.assertEquals(5.0, locacao.getValor(), 0.0);
        Assertions.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
        Assertions.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar fazer locação de filme sem estoque usando Builder")
    public void teste03() {
        //cenário
        Usuario usuario = UsuarioBuilder.umUsuario().nomeUsuario("Sabrina").agora();
        List<Filme> filmes = List.of(
                FilmeBuilder.umFilme().comNome("Projeto X").semEstoque().comPrecoLocacao(5.0).agora()
        );


        //ação
        try {
            locacaoService.alugarFilme(usuario, filmes);
        } catch (Exception e) {
            //resultado
            MatcherAssert.assertThat(e.getMessage(), is("Filme sem estoque"));
        }
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar fazer locação de filme com usuário vazio usando Builder")
    public void teste04() {
        //cenário
        List<Filme> filmes = List.of(
                FilmeBuilder.umFilme().comNome("Batman").comEstoque(2).comPrecoLocacao(5.0).agora()
        );

        //ação
        try {
            locacaoService.alugarFilme(null, filmes);
        } catch (Exception e) {
            //resultado
            MatcherAssert.assertThat(e.getMessage(), is("Usuário vazio"));
        }
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar fazer locação de filme com filme vazio usando Builder")
    public void teste05() {
        //cenário
        Usuario usuario = UsuarioBuilder.umUsuario().nomeUsuario("Felipe").agora();

        //ação
        try {
            locacaoService.alugarFilme(usuario, null);
        } catch (Exception e) {
            //resultado
            MatcherAssert.assertThat(e.getMessage(), is("Filme vazio"));
        }
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar fazer locação de filme para usuário negativado usando Builder")
    public void teste06() throws Exception {
        //cenário
        Usuario usuario = UsuarioBuilder.umUsuario().agora();
        List<Filme> filmes = List.of(
                FilmeBuilder.umFilme().agora()
        );

        //Quando possuiNegativacao for chamado passando usuário, então retono true
        when(spcService.possuiNegativacao(usuario)).thenReturn(true);

        //ação
        try {
            locacaoService.alugarFilme(usuario, filmes);
        } catch (Exception e) {
            //verificacao
            MatcherAssert.assertThat(e.getMessage(), is("Usuário negativado"));
        }

        verify(spcService).possuiNegativacao(usuario);
    }


    @Test
    @DisplayName("Deve enviar email para locacoes atrasadas usando Builder")
    public void teste07() {
        //cenário
        Usuario usuario = UsuarioBuilder.umUsuario().nomeUsuario("Usuário 1 atrasado").agora();

        List<Locacao> locacoes = List.of(
                LocacaoBuilder
                        .umaLocacao()
                        .comUsuario(usuario)
                        .atrasada()
                        .agora()
        );

        when(locacaoDAO.obterLocacoesPendentes()).thenReturn(locacoes);

        //ação
        locacaoService.notificarAtrasos();

        //verificacao
        //verificando que foi feito notificação
        verify(emailService).notificarAtraso(usuario);
        //verificando que não teve mais interações
        verifyNoInteractions(spcService);
        //verificando que teve pelo menos 1 notificação
        verify(emailService, atLeastOnce()).notificarAtraso(usuario);
        //verificando que a classe teve pelo menos 1 notificação
        verify(emailService, times(1)).notificarAtraso(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve prorrogar uma locacao usando Builder")
    public void test08(){
        //cenario
        Locacao locacao = LocacaoBuilder.umaLocacao().agora();

        //acao
        locacaoService.prorrogarLocacao(locacao, 3);

        //verificacao
        ArgumentCaptor<Locacao> argumentCaptor = ArgumentCaptor.forClass(Locacao.class);

        Mockito.verify(locacaoDAO).salvar(argumentCaptor.capture());

        Locacao locacaoRetorno = argumentCaptor.getValue();
        errorCollector.checkThat(locacaoRetorno.getValor(), is(12.0));
        errorCollector.checkThat(locacaoRetorno.getDataLocacao(), MatchersProprios.ehHoje());
        errorCollector.checkThat(locacaoRetorno.getDataRetorno(), MatchersProprios.ehHojeComDiferencaDias(3));
    }




}
