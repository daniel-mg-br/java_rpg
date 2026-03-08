package com.javarpg.world;
import com.javarpg.model.entities.Character;
import com.javarpg.model.items.*;
import com.javarpg.utils.ConsoleUtils;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

// Conceito base: Location é uma classe abstrata para servir de base para as cidades e as áreas exploráveis
public abstract class Location {
    // Atributos: nome e descrição do local
    protected String name;
    protected String description;

    // Hashmap para representar o mapa como um grafo
    // Chave (String): o nome do caminho
    // Valor (Location) : o objeto do mapa de destino 
    protected Map <String, Location> exits;

    // Métodos getter e setter padrão:
    public String getName() {return this.name;}
    public void setName(String name) {this.name = name;}

    public String getDescription() {return this.description;}
    public void setDescription(String description) {this.description = description;} 

    public Map <String, Location> getExits() {return this.exits;}

    // Método construtor
    public Location(String name, String description) {
        this.setName(name);
        this.setDescription(description);
        this.exits = new HashMap<>();
    }
    
    // Método para conectar este mapa a outro
    public void addExit(String exitName, Location destination) {
        this.exits.put(exitName, destination);
    }

    // Método para buscar aonde a ponte leva
    public Location getExit(String exitName) {
        return this.exits.get(exitName);
    }

    // Método abstrato, define o que acontece quando o jogador entra nele, retorna uma localização 
    public abstract Location enter(Character player, Scanner scanner);

    // Método concreto na classe mãe, o comportamento padrão de viagem
    protected Location travelMenu(Character player, Scanner scanner) {
        System.out.println("\n===== DESTINOS DISPONÍVEIS =====");

        // Converte as saídas para uma lista
        List <String> paths = new ArrayList<>(this.exits.keySet());
        if (paths.isEmpty()) {
            System.out.println("Os portões estão fechados, não há para onde ir!");
            ConsoleUtils.pressEnter();
            return null;
        }

        // Imprime as opções de saída
        for (int i = 0; i < paths.size(); i++) {
            System.out.println("[" + (i+1) + "] " + paths.get(i));
        }
        System.out.println("[0] Cancelar");
        System.out.println("---------------------------------------");

        System.out.print("Escolha seu destino: ");
        int choice = scanner.nextInt();

        if (choice > 0 && choice <= paths.size()) {
            // Retorna o objeto Location correspondente àquela saída
            String pathChosen = paths.get(choice-1);
            return this.exits.get(pathChosen);
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

    // Menu principal da mochila, acessível para City e Dungeon
    protected void openBackPack(Character player, Scanner scanner) {
        while (true) {
            ConsoleUtils.clearScreen();
            player.getStatus();

            System.out.println("\n[1] Ver Itens (usar/equipar)");
            System.out.println("[2] Desequipar um item");
            System.out.println("[3] Jogar item fora");
            System.out.println("[0] Fechar mochila");
            System.out.println("------------------------------------------");

            System.out.print("Sua escolha: ");
            int choice = scanner.nextInt();

            if (choice == 1) {
                useItemMenu(player, scanner);
            }
            else if (choice == 2) {
                unequipMenu(player, scanner);
            }
            else if (choice == 3) {
                dropItemMenu(player, scanner);
            }
            else if (choice == 0) {
                System.out.println("Fechando a mochila...");
                return;
            }
            else {
                System.out.println("Opção inválida!");
                ConsoleUtils.pressEnter();
            }
        }
    }

    // Sub-menu para equipar/usar itens
    private void useItemMenu(Character player, Scanner scanner) {
        System.out.println("\n===== SEUS ITENS =====");
        if (player.getInventory().isEmpty()) {
            System.out.println("Sua mochila está vazia!");
            ConsoleUtils.pressEnter();
            return;
        }

        for (int i = 0; i < player.getInventory().size(); i++) {
            System.out.println("[" + (i+1) + "] " + player.getInventory().get(i).getItemName());
        }
        System.out.println("[0] Cancelar");
        System.out.println("------------------------------------------");

        System.out.print("Sua escolha: ");
        int chosenItem = scanner.nextInt();

        if (chosenItem > 0 && chosenItem <= player.getInventory().size()) {
            player.useItem(player.getInventory().get(chosenItem-1));
            ConsoleUtils.pressEnter();
        }
        else System.out.println("Opção inválida!");
    }

    // Sub-menu para desequipar itens
    private void unequipMenu(Character player, Scanner scanner) {
        System.out.println("\n===== DESEQUIPAR =====");
        System.out.println("[1] Arma");
        System.out.println("[2] Armadura");
        System.out.println("[3] Capacete");
        System.out.println("[0] Cancelar");
        System.out.println("------------------------------------------");

        System.out.print("Escolha o slot: ");
        int chosenSlot = scanner.nextInt();

        // Desequipando o item escolhido baseado no tipo dele
        if (chosenSlot == 1) player.unequipItem(Equipment.Type.WEAPON);
        else if (chosenSlot == 2) player.unequipItem(Equipment.Type.ARMOR);
        else if (chosenSlot == 3) player.unequipItem(Equipment.Type.HELMET);
        
        if (chosenSlot != 0) ConsoleUtils.pressEnter();
    }

    // Sub-menu para jogar itens fora
    private void dropItemMenu(Character player, Scanner scanner) {
        System.out.println("\n===== JOGAR ITEM FORA =====");
        if (player.getInventory().isEmpty()) {
            System.out.println("Sua mochila está vazia!");
            ConsoleUtils.pressEnter();
            return;
        }

        // Listando os itens do inventário
        for (int i = 0; i < player.getInventory().size(); i++) {
            System.out.println("[" + (i+1) + "] " + player.getInventory().get(i).getItemName());
        }
        System.out.println("[0] Cancelar");
        System.out.println("------------------------------------------");

        System.out.print("Escolha o que quer jogar fora: ");
        int chosenDrop = scanner.nextInt();

        // Conferindo se o índice do item é válido
        if (chosenDrop > 0 && chosenDrop <= player.getInventory().size()) {
            Item dropItem = player.getInventory().remove(chosenDrop-1);
            System.out.println("Você jogou " + dropItem.getItemName() + " fora!");
        }
        else System.out.println("Item inválido!");
        ConsoleUtils.pressEnter();
    }
}
