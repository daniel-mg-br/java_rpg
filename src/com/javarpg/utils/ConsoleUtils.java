package com.javarpg.utils;

// Métodos para alterar a visualização do console
public class ConsoleUtils {
    // 1) Limpar a tela do terminal, como o system("cls") do C
    public static void clearScreen() {
        try {
            String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                // Se for windows, pede para o CMD limpar a tela
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Se for linux/mac usa o código ANSI de limpar a tela
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e){
            // Plano B: se a IDE não suporta, empurra 50 linhas em branco na tela
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    // Método para "Pressione Enter para continuar..."
    public static void pressEnter() {
        System.out.println("\n[Pressione ENTER para continuar...]");

        try {
            // Pausa o programa até o jogador digitar algo
            System.in.read();

            // O loop limpa o "lixo" que fica no teclado (como o enter) para não bugar o scanner
            while(System.in.available() > 0) {
                System.in.read();
            }
        } catch (Exception e) {
            // Ignora silenciosamente
        }
    }
}
