package com.javarpg.world;
import com.javarpg.model.entities.Character;
import com.javarpg.model.entities.Enemy;
import com.javarpg.model.items.Item;
import com.javarpg.utils.ConsoleUtils;
import com.javarpg.model.quests.FetchQuest;
import com.javarpg.model.quests.Quest;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

// Classe filha City, para cidades onde o jogador pode comprar itens, treinar e descansar
public class City extends Location {

    // Vitrine da loja da cidade
    private List <Item> shopInventory;

    // Lista de missões da Guilda
    private List <Quest> questList;

    // Construtor da cidade
    public City(String name, String description) {
        super(name, description);
        this.shopInventory = new ArrayList<>(); // Inicializa a lista
        this.questList = new ArrayList<>();     //Inicializa  as missões
    }

    // Método para abastecer a loja no arquivo Main
    public void addShopItem(Item item) {
        this.shopInventory.add(item);
    }

    // Método para adicionar missões para a Guilda
    public void addQuestGuild(Quest quest) {
        this.questList.add(quest);
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
            System.out.println("[3] Guilda dos Aventureiros");
            System.out.println("[4] Descansar na estalagem");
            System.out.println("[5] Abrir mochila");
            System.out.println("[6] Viajar");
            System.out.println("---------------------------------------");
            System.out.print("Sua escolha:");
            int choice = scanner.nextInt();

            if (choice == 1) {  // Loja para comprar e vender itens
                visitShop(player, scanner);
            }
            else if (choice == 2) { // Treino para ganho de XP mínimo
                trainingArea(player, scanner);
            }
            else if (choice == 3) { // Visita a guilda em busca de missões
                visitGuild(player, scanner);
            }
            else if (choice == 4) { // Local para recuperar vida e MP
                System.out.println("Você aluga um quarto aconchegante e descansa profundamente...");
                player.heal(player.getMaxHealth());
                player.restoreMp(player.getMaxMp());
                System.out.println("Seu HP e " + player.getResourceName() + " foram restaurados!");
                ConsoleUtils.pressEnter();
            }
            else if (choice == 4) {
                openBackPack(player, scanner);
            }
            else if (choice == 5) { // Opção para viajar para outras localidades
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

            // Atualiza a quantidade de itens necessária para alguma missão
            player.updateQuests();
            
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

    // Método para a área de treinamento
    private void trainingArea(Character player, Scanner scanner) {
        System.out.println("\n===== ÁREA DE TREINAMENTO =====");
        System.out.println("Um velho boneco de palha cheio de remendos está ficando no centro do pátio");
        System.out.println("Aventureiro: Vamos lá! Se você bater forte talvez seja reconhecido!");

        // Alvo imortal para receber o dano
        Enemy doll = new Enemy("Boneco de palha", 9999, 0, 0, 0, 0);

        int minDamage = 50;     // Cota de dano necessária para ganhar o XP
        int accumDamage = 0;    // Dano acumulado total

        while (true) {
            ConsoleUtils.clearScreen(); // Limpa a tela a cada turno
            System.out.println("===== ÁREA DE TREINAMENTO =====");
            System.out.println("HP: " + player.getHealth() + "/" + player.getResourceName() + ": " + player.getMp());

            // Mostra a barra de vida do boneco
            System.out.println("Boneco de Palha: [" + accumDamage + "/" + minDamage + "]");
            System.out.println("------------------------------------------");

            System.out.println("[1] Ataque básico");
            System.out.println("[2] Testar habilidade especial");
            System.out.println("[0] Voltar para a praça");
            System.out.println("------------------------------------------");

            System.out.print("Sua escolha: ");
            int choice = scanner.nextInt();

            if (choice == 1) {
                int damage = player.calculateDamage();
                System.out.println("\nVocê golpeia o boneco, causando " + damage + " de dano!");
                accumDamage += damage;  // Soma o dano da cota
            }
            else if (choice == 2) {
                int preHealth = doll.getHealth();
                boolean success = player.useSpecialHability(doll);

                if (success) {
                    int damageDealt = preHealth - doll.getHealth();
                    System.out.println("O boneco balança violentamente e recebe " + damageDealt + " de dano!");

                    accumDamage += damageDealt;     // Soma o dano da cota
                    doll.setHealth(9999);   // Reseta a vida do boneco
                }
                else {
                    System.out.println("Você está exausto demais para usar a habilidade!");
                }
            }
            else if (choice == 0) {
                System.out.println("Você guarda sua arma e sai da área de treinamento");
                ConsoleUtils.pressEnter();
                return;
            }
            else {
                System.out.println("Opção inválida!");
            }

            // Verificando se a cota de dano foi atingida
            if (accumDamage >= minDamage) {
                System.out.println("\nCRACK! O boneco de palha não resiste e se despedaça!");
                System.out.println("Aventureiro: É isso aí! Pega isso aqui como recompensa!");
                player.wonExperience(15); // Dá o XP merecido

                // Desconta a cota, mas mantém o excesso para o próximo boneco
                accumDamage -= minDamage;
            }

            if (choice == 1 || choice == 2) ConsoleUtils.pressEnter();
        }
    }

    // Método para visitar a guilda e mostrar o mural de missões para o jogador
    private void visitGuild(Character player, Scanner scanner) {
        while (true) {
            ConsoleUtils.clearScreen();
            System.out.println("====== GUILDA DE AVENTUREIROS =====");
            System.out.println("Recepcionista: Bem-vindo(a) à Guilda! Procurando trabalho ou tem algo interessante aí?");

            System.out.println("[1] Ver mural de missões");
            System.out.println("[2] Entregar missão concluída");
            System.out.println("[0] Ir embora");
            System.out.println("------------------------------------------");

            System.out.print("Sua escolha: ");
            int choice = scanner.nextInt();

            // MURAL DE MISSÕES
            if (choice == 1) {
                questBoardMenu(player, scanner);
            }
            // ENTREGAR MISSÕES
            else if (choice == 2) {
                turnInQuestMenu(player, scanner);
            }
            else if (choice == 0) {
                System.out.println("Recepcionista: Até a próxima, amigo(a)!");
                ConsoleUtils.pressEnter();
                return;
            }
            else {
                System.out.println("Opção inválida!");
                ConsoleUtils.pressEnter();
            }
        }
    }

    // Método auxiliar para mostrar o quadro de missões
    private void questBoardMenu(Character player, Scanner scanner) {
        System.out.println("===== MURAL DE MISSÕES =====");
        if (questList.isEmpty()) {
            System.out.println("Recepcionista: Foi mal chefe, estamos meio parados agora!");
            ConsoleUtils.pressEnter();
            return;
        }

        // Imprime os detalhes das missões
        for (int i = 0; i < questList.size(); i++) {
            Quest q = questList.get(i);
            System.out.println("[ " + (i+1) + " ] " + q.getName() + " (Prize: " + q.getGoldReward() + " Ouro | " + q.getXpReward() + " XP)");
            System.out.println("Objetivo: " + q.getObjectiveString());
        }
        System.out.println("[0] Cancelar");
        System.out.println("------------------------------------------");

        System.out.printf("Escolha uma missão: ");
        int questChoice = scanner.nextInt();

        if (questChoice > 0 && questChoice <= questList.size()) {
            // Tira a missão do mural e coloca no diário do jogador
            Quest acceptedQuest = questList.remove(questChoice-1);
            player.addQuest(acceptedQuest);
            System.out.println("Recepcionista: Muito bem! Boa sorte lá fora!");
        }
        else if (questChoice != 0) {
            System.out.println("Opção inválida!");
        }
        ConsoleUtils.pressEnter();
    }

    // Método auxiliar para o jogador encerrar os dois tipos de missões
    private void turnInQuestMenu(Character player, Scanner scanner) {
        System.out.println("===== SUAS MISSÕES CONCLUÍDAS =====");
        List <Quest> readyQuests = new ArrayList<>();

        // Lista apenas as missões que estão prontas
        for (Quest q : player.getQuestLog()) {
            if (q.getIsComplete() && !q.getIsTurnedIn()) {
                readyQuests.add(q);
                System.out.println("[ " + readyQuests.size() + " ] " + q.getName());
            }
        }

        if (readyQuests.isEmpty()) {
            System.out.println("Você não tem nenhuma missão pronta!");
            ConsoleUtils.pressEnter();
            return;
        }

        System.out.println("[0] Cancelar");
        System.out.println("------------------------------------------");

        System.out.print("Qual missão deseja entregar? ");
        int turnInChoice = scanner.nextInt();

        if (turnInChoice > 0 && turnInChoice <= questList.size()) {
            Quest q = readyQuests.get(turnInChoice-1);

            // Se for uma missão de coletar itens, eles são confiscados
            if (q instanceof FetchQuest) {
                FetchQuest fq = (FetchQuest) q;
                int itensToRemove = fq.getRequiredAmount();

                // Remove os itens da mochila de trás para frente
                for (int i = player.getInventory().size()-1; i >= 0; i--) {
                    if (player.getInventory().get(i).getItemName().equalsIgnoreCase(fq.getTargetItemName())) {
                        player.getInventory().remove(i);
                        itensToRemove--;
                        if (itensToRemove == 0) break;  // Cancela quando pegar todos
                    }
                }
                System.out.println("Você entregou os itens ao Recepcionista");
            }

            // Entrega as recompensas
            q.setIsTurnedIn(true);
            player.setGold(player.getGold() + q.getGoldReward());
            player.wonExperience(q.getXpReward());
            System.out.println("MISSÃO ENTREGUE: " + q.getName());
            System.out.println("Você recebeu: " + q.getGoldReward() + " moedas e " + q.getXpReward() + " de XP!");
            
        }
        else if (turnInChoice != 0) {
            System.out.println("Opção inválida!");
        }
        ConsoleUtils.pressEnter();
    }
}