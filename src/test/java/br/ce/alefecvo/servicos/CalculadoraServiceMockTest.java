package br.ce.alefecvo.servicos;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class CalculadoraServiceMockTest {

	@Mock
	private CalculadoraService calculadoraMock;

	@Spy
	private CalculadoraService calculadoraSpy;


	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void deveMostrarDiferencaEntreMockSpy(){
		Mockito.when(calculadoraMock.somar(1,2)).thenReturn(8);
		Mockito.when(calculadoraSpy.somar(1,2)).thenReturn(8);


		System.out.println("Mock " + calculadoraMock.somar(1,2));
		System.out.println("Spy " + calculadoraSpy.somar(1,2));
	}

	@Test
	public void teste(){
		CalculadoraService calc = Mockito.mock(CalculadoraService.class);
		Mockito.when(calc.somar(Mockito.eq(1), Mockito.anyInt())).thenReturn(5);
		
//		System.out.println(calc.somar(1, 100000));
	}
}
