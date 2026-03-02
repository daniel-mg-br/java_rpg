package com.javarpg.world;
import com.javarpg.model.entities.Character;
import com.javarpg.utils.ConsoleUtils;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

// Classe filha City, para cidades onde o jogador pode comprar itens, treinar e descansar
public class City extends Location {

    public City(String name, String description) {
        super(name, description);
    }

    // Sobrecarga do método entrar: o jogador fica na cidade para escolher o quê fazer
    @Override public Location enter(Character player, Scanner scanner) {
        while (true) {
            // Mostrando as informações da cidade
            ConsoleUtils.clearScreen();
            System.out.println("=======================================");
            System.out.println("\n" + this.getName().toUpperCase());
            System.out.println("---------------------------------------");
            System.out.println(this.getDescription());
            System.out.println("---------------------------------------");

            // Menu de opções do usuário
            System.out.println("\n O que deseja fazer agora?");
            System.out.println("[1] Visitar a loja");
            System.out.println("[2] Área de treinamento");
            System.out.println("[3] Descansar na estalagem");
            System.out.println("[4] Viajar");
            System.out.println("---------------------------------------");
            System.out.print("Sua escolha:");
            int choice = scanner.nextInt();

            if (choice == 1) {  // Loja para comprar e vender itens
                System.out.println("Loja fechada para balanço! Volte mais tarde.");
                ConsoleUtils.pressEnter();
            }
            else if (choice == 2) { // Treino para ganho de XP mínimo
                System.out.println("O boneco de palha está sendo costurado! Volte mais tarde.");
                ConsoleUtils.pressEnter();
            }
            else if (choice == 3) { // Local para recuperar vida e MP
                System.out.println("Você aluga um quarto aconchegante e descansa profundamente...");
                player.heal(player.getMaxHealth());
                player.restoreMp(player.getMaxMp());
                System.out.println("Seu HP e " + player.getResourceName() + " foram restaurados!");
                ConsoleUtils.pressEnter();
            }
            else if (choice == 4) { // Opção para viajar para outras localidades
                // Chama o sub-menu de viagens
                Location destination = travelMenu(scanner);

                // Se a viagem não foi cancelada, sai da cidade
                if (destination != null) return destination;
            }
            else {
                System.out.println("Opção inválida!");
                ConsoleUtils.pressEnter();
            }
        }
    }

    // Sub-menu privado apenas para organizar a lista de viagens
    private Location travelMenu(Scanner scanner) {
        System.out.println("\n===== DESTINOS DISPONÍVEIS =====");

        // Transformando os nomes das saídas (do Hash Map) em uma Lista
        List <String> paths = new ArrayList<>(this.exits.keySet());

        if (paths.isEmpty()) {
            System.out.println("Os portões da cidade estão trancados. Não há para onde ir!");
            ConsoleUtils.pressEnter();
            return null;
        }

        // Imprime as opções de saída
        for (int i = 0; i < paths.size(); i++) {
            System.out.println("[" + (i+1) + "]" + paths.get(i));
        }
        System.out.println("[0] Cancelar e voltar para a praça");
        System.out.println("---------------------------------------");

        System.out.print("Escolha seu destino: ");
        int choice = scanner.nextInt();

        if (choice > 0 && choice <= paths.size()) {
            String chosenPath = paths.get(choice-1);
            // Retorna o objeto Location correspondente àquela saída
            return this.exits.get(chosenPath);
        }
        else if (choice == 0) {
            return null;    // Retorna nulo, fazendo o loop na cidade continuar
        }
        else {
            System.out.println("Destino inválido!");
            ConsoleUtils.pressEnter();
            return null;
        }
    }
}
