package com.javarpg.world;
import com.javarpg.model.entities.Character;
import com.javarpg.model.entities.Enemy;
import com.javarpg.utils.ConsoleUtils;
import com.javarpg.system.BattleManager;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

// Classe filha Dungeon, para ambientes hostis onde o jogador explora, enfrenta monstros e chefões
public class Dungeon extends Location {
    // Controle de progesso da área
    private boolean bossDefeated = false;

    public Dungeon(String name, String description) {
        super(name, description);
    }

    @Override public Location enter(Character player, Scanner scanner) {
        // O jogador fica na Dungeon até sair ou morrer
        while (true) {
            ConsoleUtils.clearScreen();
            System.out.println("=======================================");
            System.out.println("\n" + this.getName().toUpperCase());
            System.out.println("---------------------------------------");
            System.out.println(this.getDescription());
            System.out.println("---------------------------------------");

            if (bossDefeated) System.out.println("O ar parece mais leve. O Boss foi derrotado!");

            System.out.println("\n O quê você deseja fazer agora?");
            System.out.println("[1] Explorar a área");
            System.out.println("[2] Enfrentar o Chefe");
            System.out.println("[3] Ver Status e Mochila");
            System.out.println("[4] Fugir / Viajar");
            System.out.println("---------------------------------------");

            System.out.print("Sua escolha: ");
            int choice = scanner.nextInt();

            if (choice == 1) {
                explore(player, scanner);
                if (player.getHealth() <= 0) return null;
            }
            else if (choice == 2) {
                if (bossDefeated) {
                    System.out.println("O boss já foi derrotado, não há o quê temer!");
                    ConsoleUtils.pressEnter();
                }
                else {
                    fightBoss(player, scanner);
                    if (player.getHealth() <= 0) return null;
                }   
            }
            else if (choice == 3) {
                System.out.println("Abrindo a mochila...(em breve)");
                ConsoleUtils.pressEnter();
            }
            else if (choice == 4) {
                Location destination = travelMenu(scanner);

                if (destination != null) return destination;
            }
            else {
                System.out.println("Opção inválida!");
                ConsoleUtils.pressEnter();
            }
        }
    }

    private void explore(Character player, Scanner scanner) {
        System.out.println("Você se embrenha na área, procurando por problemas...");
        ConsoleUtils.pressEnter();

        Enemy monstro = new Enemy("Goblin Esguio", 45, 12, 3, 60, 15);

        BattleManager.startBattle(player, monstro, scanner);
    }

    private void fightBoss(Character player, Scanner scanner) {
        System.out.println("\nVocê entra no covil do chefe...");
        ConsoleUtils.pressEnter();

        Enemy boss = new Enemy("Troll Gigante", 150, 20, 8, 300, 150);

        BattleManager.startBattle(player, boss, scanner);

        if (boss.getHealth() <= 0) {
            this.bossDefeated = true;
            System.out.println("Parabéns, você limpou essa área! O caminho adiante está seguro agora!");
            ConsoleUtils.pressEnter();
        }
    }

    private Location travelMenu(Scanner scanner) {
        System.out.println("\n===== DESTINOS DISPONÍVEIS =====");
        List <String> paths = new ArrayList<>(this.exits.keySet());

        if (paths.isEmpty()) {
            System.out.println("Não há saídas conhecidas por aqui!");
            ConsoleUtils.pressEnter();
            return null;
        }

        for (int i = 0; i < paths.size(); i++) {
            System.out.println("[" + (i+1) + "]" + paths.get(i));
        }
        System.out.println("[0] Cancelar ou continuar explorando");
        System.out.println("---------------------------------------");
        
        int choice = scanner.nextInt();

        if (choice > 0 && choice <= paths.size()) {
            return this.exits.get(paths.get(choice-1));
        }
        else if (choice == 0) {
            return null;
        }
        else {
            System.out.println("Destino inválido!");
            ConsoleUtils.pressEnter();
            return null;
        }
    }
}