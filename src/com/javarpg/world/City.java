package com.javarpg.world;
import com.javarpg.model.entities.Character;
import com.javarpg.model.items.Item;
import com.javarpg.utils.ConsoleUtils;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

// Classe filha City, para cidades onde o jogador pode comprar itens, treinar e descansar
public class City extends Location {

    // Vitrine da loja da cidade
    private List <Item> shopInventory;

    // Construtor da cidade
    public City(String name, String description) {
        super(name, description);
        this.shopInventory = new ArrayList<>(); // Inicializa a lista
    }

    // Método para abastecer a loja no arquivo Main
    public void addShopItem(Item item) {
        this.shopInventory.add(item);
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
                visitShop(player, scanner);
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
                Location destination = travelMenu(player, scanner);

                // Se a viagem não foi cancelada, sai da cidade
                if (destination != null) return destination;
            }
            else {
                System.out.println("Opção inválida!");
                ConsoleUtils.pressEnter();
            }
        }
    }

    // Menu principal da loja da cidade
    private void visitShop(Character player, Scanner scanner) {
        while (true) {
            ConsoleUtils.clearScreen();
            System.out.println("===== BAZAR DE " + this.getName().toUpperCase() + " =====");
            System.out.println("Mercador: Bem-vindo aventureiro! Dê uma olhada no quê eu tenho aqui!");
            System.out.println("--------------------------------------------------------------------");

            System.out.println("[1] Comprar itens");
            System.out.println("[2] Vender itens");
            System.out.println("[0] Sair da loja");
            System.out.println("--------------------------------------------------------------------");

            System.out.print("Sua escolha: ");
            int choice = scanner.nextInt();

            if (choice == 1) {
                buyMenu(player, scanner);
            }
            else if (choice == 2) {
                sellMenu(player, scanner);
            }
            else if (choice == 0) {
                System.out.println("\nMercador: volte sempre amigo(a)!");
                ConsoleUtils.pressEnter();
                return; // Sai da loja e retorna para a cidade
            }
            else {
                System.out.println("Opção inválida!");
                ConsoleUtils.pressEnter();
            }
        }
    }

    // Menu de compra
    private void buyMenu(Character player, Scanner scanner) {
        System.out.println("\n===== COMPRAR =====");
        if (shopInventory.isEmpty()) {
            System.out.println("Mercador: desculpe amigo(a), a carroça de suprimentos ainda não chegou!");
            ConsoleUtils.pressEnter();
            return;
        }

        // Imprimindo a vitrine da loja
        for (int i = 0; i < shopInventory.size(); i++) {
            Item item = shopInventory.get(i);
            // Mostra o item e seu preço
            System.out.println("[" + (i+1) + "] " + item.getItemName() + " - " + item.getItemPrice() + " moedas");
        }
        System.out.println("[0] Cancelar");
        System.out.println("------------------------------------------");

        System.out.print("Sua escolha: ");
        int choice = scanner.nextInt();

        if (choice > 0 && choice <= shopInventory.size()) {
            Item chosenItem = shopInventory.get(choice-1);

            // Verifica se o jogador tem dinheiro
            if (player.getGold() >= chosenItem.getItemPrice()) {
                // Tenta colocar na mochila do jogador
                if (player.addItem(chosenItem, true)) {
                    // Cobra o valor
                    player.setGold(player.getGold()-chosenItem.getItemPrice());
                    System.out.println("Você comprou: " + chosenItem.getItemName() + "!");
                    System.out.println("Mercador: Ótima escolha amigo(a)!");
                }
                else {
                    System.out.println("Sua mochila está cheia! Esvazie-a primeiro!");
                }
            }
            else {
                System.out.println("Você não tem ouro suficiente para isso!");
                System.out.println("Mercador: Sem fiado amigo(a)...");
            }
            ConsoleUtils.pressEnter();
        }
        else if (choice != 0) {
            System.out.println("Opção inválida!");
            ConsoleUtils.pressEnter();
        }
    }

    // Menu de venda
    private void sellMenu(Character player, Scanner scanner) {
        System.out.println("\n===== VENDER ====");
        if (player.getInventory().isEmpty()) {
            System.out.println("Você não tem nada na mochila para vender!");
            ConsoleUtils.pressEnter();
            return;
        }

        for (int i = 0; i < player.getInventory().size(); i++) {
            Item item = player.getInventory().get(i);
            // Calcula o preço de venda (40% do original)
            int sellPrice = (int)(item.getItemPrice() * 0.4);
            System.out.println("[" + (i+1) + "] " + item.getItemName() + " (Vender por: " + sellPrice + " moedas");
        }   
        System.out.println("[0] Cancelar");
        System.out.println("------------------------------------------");

        System.out.println("O que deseja vender? ");
        int choice = scanner.nextInt();

        if (choice > 0 && choice <= player.getInventory().size()) {
            Item sellItem = player.getInventory().get(choice-1);
            int sellPrice = (int)(sellItem.getItemPrice() * 0.4);

            // Remove da mochila o item que será vendido
            player.getInventory().remove(sellItem);
            // Dá o dinheiro ao jogador
            player.setGold(player.getGold() + sellPrice);

            System.out.println("Você vendeu " + sellItem.getItemName() + " por " + sellPrice + " moedas!");
            System.out.println("Mercador: Opa! Ótimo negócio, valeu!");
            ConsoleUtils.pressEnter();
        }
        else if (choice != 0) {
            System.out.println("Opção inválida!");
            ConsoleUtils.pressEnter();
        }
    }
}
