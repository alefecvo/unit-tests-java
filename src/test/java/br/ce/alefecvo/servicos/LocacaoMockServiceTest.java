package br.ce.alefecvo.servicos;

import br.ce.alefecvo.daos.LocacaoDAO;
import br.ce.alefecvo.entidades.Filme;
import br.ce.alefecvo.entidades.Locacao;
import br.ce.alefecvo.entidades.Usuario;
import br.ce.alefecvo.utils.DataUtils;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;


public class LocacaoMockServiceTest {

    @InjectMocks
    private LocacaoService locacaoService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Deve fazer locação de filme com execeção criada")
    public void teste01() {
        //cenário
        Usuario usuario = new Usuario("pedro");
        List<Filme> filmes = List.of(
                new Filme("harry potter e a pedra filosofal", 2, 5.0),
                new Filme("harry potter e camera secreta", 3, 15.0));

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
            Assertions.assertEquals(e.getMessage(),"SPC indisponível");
        }
    }

    @Test
    @DisplayName("Deve fazer locação de filme com execeção padrão")
    public void teste02() {
        //cenário
        Usuario usuario = new Usuario("lorenzo");
        List<Filme> filmes = List.of(new Filme("predador", 2, 5.0));

        //ação
        try {
            locacaoService.alugarFilme(usuario, filmes);
        }catch (Exception e){
            Assertions.assertEquals(e.getMessage(),"SPC indisponível");
        }
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar fazer locação de filme sem estoque")
    public void teste03() {
        //cenário
        Usuario usuario = new Usuario("luisa");
        List<Filme> filmes = List.of(new Filme("guerra mundial", 0, 5.0));


        //ação
        try {
            locacaoService.alugarFilme(usuario, filmes);
        } catch (Exception e) {
            //resultado
            MatcherAssert.assertThat(e.getMessage(), is("Filme sem estoque"));
        }
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar fazer locação de filme com usuário vazio")
    public void teste04() {
        //cenário
        List<Filme> filmes = List.of(new Filme("petter pan", 2, 5.0));

        //ação
        try {
            locacaoService.alugarFilme(null, filmes);
        } catch (Exception e) {
            //resultado
            MatcherAssert.assertThat(e.getMessage(), is("Usuário vazio"));
        }
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar fazer locação de filme com filme vazio")
    public void teste05() {
        //cenário
        Usuario usuario = new Usuario("lucas");

        //ação
        try {
            locacaoService.alugarFilme(usuario, null);
        } catch (Exception e) {
            //resultado
            MatcherAssert.assertThat(e.getMessage(), is("Filme vazio"));
        }
    }

}
