package com.javarpg.system;

import com.javarpg.model.entities.Character;
import com.javarpg.model.entities.Enemy;
import com.javarpg.model.items.*;
import com.javarpg.utils.*;
import java.util.Scanner;

public class BattleManager {
    // Método esttático: possível chamá-lo de qualquer lugar
    public static void startBattle(Character player, Enemy enemy, Scanner scanner) {
        ConsoleUtils.clearScreen();
        System.out.println("\n UM " + enemy.getName().toUpperCase() + " APARECEU!\n");
        ConsoleUtils.pressEnter();

        // Loop principal: combate acontece até um dos dois morrer
        while (player.getHealth() > 0 && enemy.getHealth() > 0) {
            // 1. Fase de recuperação
            int mpRegen = 5;
            player.restoreMp(mpRegen);
            System.out.println(player.getName() + " recuperou " + mpRegen + " de " + player.getResourceName());

            // Iniciativa: o mais rápido começa
            boolean playerStarts = player.getEffectiveAgility() >= enemy.getEffectiveAgility();

            if (playerStarts) {
                // Jogador é mais rápido
                boolean fled = playerTurn(player, enemy, scanner);
                if (fled) return; // Se fugiu, encerra a batalha

                // Se o inimigo não morreu com o ataque, revida
                if (enemy.getHealth() > 0) enemyTurn(enemy, player); 
            } else {
                // Inimigo é mais rápido
                System.out.println("O " + enemy.getName() + " foi mais rápido!");
                enemyTurn(enemy, player);

                // Se o jogador sobreviveu ao ataque, tem seu turno
                if (player.getHealth() > 0) {
                    boolean fled = playerTurn(player, enemy, scanner);
                    if (fled) return;
                }
            }
            System.out.println("---------------------------------------------------");
        }

        // Fim do combate
        if (player.getHealth() > 0) {
            System.out.println("\nVITÓRIA! Você derrotou o " + enemy.getName() + "!");
            player.wonExperience(enemy.getNxtLevelXp());

            player.setGold(player.getGold() + enemy.getGold());
            System.out.println("Você ganhou " + enemy.getGold() + " de ouro!");
        } else {
            System.out.println("\nGAME OVER... O " + enemy.getName() + " derrotou você!");
        }

        player.resetBuffs();
        enemy.resetBuffs();
    }

    // O turno do jogador, retorna true se conseguir fugir e false se não conseguir
    private static boolean playerTurn(Character player, Enemy enemy, Scanner scanner) {
        // Limpa a tela
        ConsoleUtils.clearScreen();
        // Interface do usuário
        System.out.println("----- SEU TURNO -----");
        System.out.println("HP: " + player.getHealth() + "/" + player.getMaxHealth());
        System.out.println(player.getResourceName() + ": " + player.getMp() + "/" + player.getMaxMp());
        System.out.println(enemy.getName() + " | HP: " + enemy.getHealth() + "/" + enemy.getMaxHealth());

        System.out.println("----- Escolha sua ação: -----");
        System.out.println("[1] Atacar");
        System.out.println("[2] Usar Item");
        System.out.println("[3] Habilidade Especial");
        System.out.println("[4] Fugir");
        System.out.print("Sua escolha: ");

        // Lendo a escolha do jogador
        int choice = scanner.nextInt();

        if (choice == 1) {  // Escolheu atacar
            System.out.println("\nVocê ataca o " + enemy.getName() + "!");
            int damage = player.calculateDamage();
            enemy.receiveDamage(damage);
        }
        else if (choice == 2) { // Escolheu usar um item
            if (player.getInventory().isEmpty()) {
                System.out.println("Sua mochila está vazia! Perdeu o turno!");
            } else {
                System.out.println("----- MOCHILA -----");
                // Lista os itens com índice visual
                for (int i = 0; i < player.getInventory().size(); i++) {
                    System.out.println("[" + (i+1) + "] " + player.getInventory().get(i).getItemName());
                }

                System.out.println("Escolha o número do item (ou 0 para cancelar): ");
                int itemChoice = scanner.nextInt();

                if (itemChoice > 0 && itemChoice <= player.getInventory().size()) {
                    // Pega o item escolhido
                    Item chosenItem = player.getInventory().get(itemChoice-1);
                    player.useItem(chosenItem);
                } 
                else if (itemChoice == 0) {
                    System.out.println("Você fechou a mochila!");
                    // Se cancelar, chamamos o turno de novo, ele não perde a vez
                    return playerTurn(player, enemy, scanner);
                } 
                else {
                    System.out.println("Item inválido! Perdeu o turno!");
                }
            }
        }
        else if (choice == 3) {
            boolean success = player.useSpecialHability(enemy);

            if (!success) {
                // Se retornou false (não tem MP), não perde o turno
                System.out.println("Escolha outra ação!");
                return playerTurn(player, enemy, scanner);
            }   
        }
        else if (choice == 4) { // Escolheu tentar fugir
            System.out.println("\nVocê tenta fugir...");

            // Chance de fuga baseada na agilidade: 50% de chance + bônus se for mais rápido
            double chance = 0.5 + ((player.getEffectiveAgility() - enemy.getEffectiveAgility()) * 0.05);

            if (Math.random() < chance) {
                System.out.println("Você conseguiu escapar com sucesso!");
                return true;
            } else {
                System.out.println("O " + enemy.getName() + " bloqueou sua saída!");
            }
        }
        else System.out.println("Escolha inválida! Você tropecou e perdeu o turno!");
        return false; 
    }

    // O turno do inimigo
    public static void enemyTurn(Enemy enemy, Character player) {
        System.out.println("\n----- TURNO DO INIMIGO -----");
        
        // Math.random() gera um número entre 0 e 0.99
        if (Math.random() < 0.4 && enemy.getMp() >= 10) {
            enemy.useSpecialHability(player);
        } else {
            System.out.println("O " + enemy.getName() + " avança contra você!");
            int damage = enemy.calculateDamage();
            player.receiveDamage(damage);
        }
        ConsoleUtils.pressEnter();
    }
}
