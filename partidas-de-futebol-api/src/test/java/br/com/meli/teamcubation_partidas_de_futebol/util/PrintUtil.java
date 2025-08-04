package br.com.meli.teamcubation_partidas_de_futebol.util;

public final class PrintUtil {

    private PrintUtil() {
    }

    public static void printInicioDoTeste(String nomeDoTeste) {
        System.out.println("==> Iniciando teste: " + nomeDoTeste);
    }

    public static void printMensagemDeErro(String mensagem) {
        System.out.println("Erro lançado: " + mensagem);
    }
}
