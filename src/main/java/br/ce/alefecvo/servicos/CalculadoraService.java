package br.ce.alefecvo.servicos;

import br.ce.alefecvo.exceptions.NaoPodeDividirPorZeroException;

public class CalculadoraService {

	public int somar(int a, int b) {
		return a + b;
	}

	public int subtrair(int a, int b) {
		return a - b;
	}

	public int divide(int a, int b) throws NaoPodeDividirPorZeroException {
		if(b == 0) {
			throw new NaoPodeDividirPorZeroException();
		}
		return a / b;
	}
	
	public int divide(String a, String b) {
		return Integer.valueOf(a) / Integer.valueOf(b);
	}
	
	public static void main(String[] args) {
		new CalculadoraService().divide("a", "b");
	}

}
