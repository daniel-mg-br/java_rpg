package com.javarpg.world;
import com.javarpg.model.entities.Character;
import com.javarpg.model.entities.Enemy;
import com.javarpg.model.items.Consumable;
import com.javarpg.model.items.Equipment;
import com.javarpg.utils.ConsoleUtils;
import com.javarpg.system.BattleManager;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

// Classe filha Dungeon, para ambientes hostis onde o jogador explora, enfrenta monstros e chefões
public class Dungeon extends Location {
    // Controle de progesso da área
    private boolean bossDefeated = false;
    private boolean bossDiscovered = false; 

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

            // Escondendo o chefe caso não tenha sido encontrado!
            if (bossDiscovered) System.out.println("[2] Ir atrás do chefe da área");
            else System.out.println("[2] ??? Área desconhecida");

            System.out.println("[3] Acampar e descansar");
            System.out.println("[4] Ver Status e Mochila");
            System.out.println("[5] Fugir / Viajar");
            System.out.println("---------------------------------------");

            System.out.print("Sua escolha: ");
            int choice = scanner.nextInt();

            if (choice == 1) {
                explore(player, scanner);
                // Se o jogador morrer na exploração, renasce na cidade anterior
                if (player.getHealth() <= 0) return handleDeath(player);
            }
            else if (choice == 2) {
                if (!bossDiscovered) {      // Caso o Chefe não foi descoberto ainda
                    System.out.println("\nParece não haver nada por aqui...");
                    ConsoleUtils.pressEnter();
                }
                else if (bossDefeated) {    // Caso o chefe já foi derrotado
                    System.out.println("O boss já foi derrotado, não há o quê temer!");
                    ConsoleUtils.pressEnter();
                }
                else {
                    fightBoss(player, scanner);
                    // Se o jogador morrer para o boss, também retorna para a cidade
                    if (player.getHealth() <= 0) return handleDeath(player);
                }   
            }
            else if (choice == 3) {
                camp(player);
            }
            else if (choice == 4) {
                openBackPack(player, scanner);
            }
            else if (choice == 5) {
                Location destination = travelMenu(player, scanner);

                // Em breve: se o jogador não derrotou o boss, não consegue prosseguir
                if (destination != null) return destination;
            }
            else {
                System.out.println("Opção inválida!");
                ConsoleUtils.pressEnter();
            }
        }
    }

    // Método para lutas comuns
    private void explore(Character player, Scanner scanner) {
        System.out.println("Você se embrenha na área, procurando por problemas...");
        ConsoleUtils.pressEnter();

        // Chance de encontrar o Boss (25%)
        if (!bossDefeated && Math.random() <= 0.25) {
            System.out.println("UM RUGIDO ASSUSTADOR ECOA PELA FLORESTA!");
            System.out.println("Você trombou com o Chefe da área sem querer!");
            this.bossDiscovered = true; // Parte desbloqueada no menu

            fightBoss(player, scanner);
            return; // Encerra a exploração normal
        }

        // Luta normal
        Enemy monstro = new Enemy("Goblin Esguio", 45, 12, 3, 60, 15);

        BattleManager.startBattle(player, monstro, scanner);

        // Sistema de drops (só se o monstro morrer)
        if (monstro.getHealth() <= 0) {
            double dropChance = Math.random();  // Gera um número entre 0.0 a 1.0

            if (dropChance <= 0.15) {
                // 15% de chance de um item intermediário/equipamento cair
                System.out.println("\nÓtimo! O monstro deixou um equipamento cair!");
                Equipment adaga = new Equipment("Adaga de Goblin", 25, 6, Equipment.Type.WEAPON, Equipment.StatBonus.ATTACK);

                player.addItem(adaga);
            }
            else if (dropChance <= 0.45) {
                // 30% de chance de um item básico cair
                System.out.println("\nLegal! O monstro deixou um consumível cair!");
                Consumable pocao = new Consumable("Poção pequena", 10, 30, "Vida");
                
                player.addItem(pocao);
            }
            // Se for maior que 0.45, não dropa nada
            ConsoleUtils.pressEnter();
        }
    }

    // Método para o chefe final da área
    private void fightBoss(Character player, Scanner scanner) {
        System.out.println("\nVocê entra no covil do chefe...");
        ConsoleUtils.pressEnter();

        // Placeholder para criar o boss
        Enemy boss = new Enemy("Troll Gigante", 150, 20, 8, 300, 150);

        BattleManager.startBattle(player, boss, scanner);

        // Verifica se o jogador venceu
        if (boss.getHealth() <= 0) {
            this.bossDefeated = true;
            System.out.println("Parabéns, você limpou essa área! O caminho adiante está seguro agora!");
            ConsoleUtils.pressEnter();
        }
    }

    // Método para descansar, recuperando HP e MP
    private void camp(Character player) {
        System.out.println("Você encontra um canto seguro e monta um pequeno acampamento...");

        // Calcula 40% do HP máximo
        int hpRecovery = (int)(player.getMaxHealth() * 0.4);
        // Calcula 60% do MP máximo
        int mpRecovery = (int)(player.getMaxMp() * 0.6);

        // Aplica a recuperação de HP e MP
        player.heal(hpRecovery);
        player.restoreMp(mpRecovery);

        System.out.println("Você comeu algumas frutas e descansou!");
        System.out.println("Recuperou: " + hpRecovery + " de HP e " + mpRecovery + " de MP!");

        ConsoleUtils.pressEnter();
    }

    // Menu principal da mochila
    private void openBackPack(Character player, Scanner scanner) {
        while (true) {
            ConsoleUtils.clearScreen();
            System.out.println("===== STATUS E MOCHILA =====");
            player.getStatus();

            System.out.println("\n O quê você deseja fazer?");
            System.out.println("[1] Ver itens (equipar/desequipar)");
            System.out.println("[2] Desequipar itens");
            System.out.println("[0] Fechar mochila");
            System.out.println("---------------------------------------");

            System.out.print("Escolha seu destino: ");
            int choice = scanner.nextInt();

            if (choice == 1) {
                useItemMenu(player, scanner);
            }
            else if (choice == 2) {
                unequipMenu(player, scanner);
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

    // Sub-menu para usar/equipar itens
    private void useItemMenu(Character player, Scanner scanner) {
        System.out.println("\n===== SEUS ITENS =====");
        if (player.getInventory().isEmpty()) {
            System.out.println("Sua mochila está vazia!");
            ConsoleUtils.pressEnter();
            return;
        }

        // Listando todos os itens disponíveis
        for (int i = 0; i < player.getInventory().size(); i++) {
            System.out.println("[" + (i+1) + "] " + player.getInventory().get(i).getItemName());
        }
        System.out.println("[0] Cancelar");
        System.out.println("---------------------------------------");

        System.out.print("Escolha um para usar ou equipar: ");
        int choice = scanner.nextInt();

        if (choice > 0 && choice <= player.getInventory().size()) {
            // Pega o item escolhido (subtraindo 1 por causa do índice do array)
            // Método useItem da classe Character
            player.useItem(player.getInventory().get(choice-1));
            ConsoleUtils.pressEnter();
        }
        else if (choice != 0){
            System.out.println("Item inválido!");
            ConsoleUtils.pressEnter();
        }
    }

    // Sub-menu para desequipar
    private void unequipMenu(Character player, Scanner scanner) {
        System.out.println("\n===== DESEQUIPAR ITEM =====");
        System.out.println("[1] Arma");
        System.out.println("[2] Armadura");
        System.out.println("[3] Capacete");
        System.out.println("[0] Cancelar");
        System.out.println("---------------------------------------");

        System.out.println("Escolha um slot para desequipar: ");
        int choice = scanner.nextInt();

        // Chama o método unequipItem da classe Character
        if (choice == 1) player.unequipItem(Equipment.Type.WEAPON);
        else if (choice == 2) player.unequipItem(Equipment.Type.ARMOR);
        else if (choice == 3) player.unequipItem(Equipment.Type.HELMET);
        else if (choice != 0) {
            System.out.println("Slot inválido!");
            ConsoleUtils.pressEnter();
        }
    }

    // Sub-menu de viajem com as regras de Fuga e Bloqueio do Boss
    @Override
    protected Location travelMenu(Character player, Scanner scanner) {
        System.out.println("\n===== DESTINOS DISPONÍVEIS =====");
        List <String> paths = new ArrayList<>();

        // Regra 1: bloqueio de caminhos
        for (String exitName : this.exits.keySet()) {
            if (!bossDefeated) {
                // Se o boss está vivo, oculta os caminhos de "avançar"
                // Só mostra as opções se o texto contiver "voltar" ou "retornar"
                if (exitName.toLowerCase().contains("voltar") || exitName.toLowerCase().contains("Retornar")) {
                    paths.add(exitName);
                }
            }
            else {
                // Se o boss morreu, mostra todos os caminhos
                paths.add(exitName);
            }
        }

        // Se a lista ficar vazia (em uma área que não dá pra voltar)
        if (paths.isEmpty()) {
            System.out.println("As tropas do chefe bloqueiam o caminho adiante e não há rotas de fuga!");
            ConsoleUtils.pressEnter();
            return null; // Prende o jogador na Dungeon
        }

        // Imprime as opções filtradas
        for (int i = 0; i < paths.size(); i++) {
            System.out.println("[" + (i+1) + "]" + paths.get(i));
        }
        System.out.println("[0] Cancelar e continuar explorando");
        System.out.println("---------------------------------------");
        
        System.out.print("Escolha seu destino: ");
        int choice = scanner.nextInt();

        if (choice > 0 && choice <= paths.size()) {
            String pathChosen = paths.get(choice-1);
            Location destination = this.exits.get(pathChosen);

            // Regra 2: chance de fuga baseada na Inteligência
            if (!bossDefeated) {
                System.out.println("\nVocê tenta escapar sorrateiramente pelo caminho de volta...");

                // Cálculo: 30% base + 5% por ponto de inteligência
                double escapeChance = 0.3 + (player.getEffectiveIntelligence() * 0.05);

                if (Math.random() <= escapeChance) {
                    System.out.println("Sucesso! Você usou sua inteligência para despistar os monstros!");
                    ConsoleUtils.pressEnter();
                    return destination; // Devolve o mapa anterior
                }
                else {
                    System.out.println("Falha! Você se perdeu e voltou para o acampamento!");
                    ConsoleUtils.pressEnter();
                    return null;    // Retorna nulo, cancelando a viagem!
                }
            }
            else {
                // Regra 3: chefe morto, viagem instantânea
                System.out.println("Você viaja com segurança pelos caminhos tranquilos");
                ConsoleUtils.pressEnter();
                return destination; // Viajou com sucesso
            }
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

    // Método para quando ojogador morre em combate
    private Location handleDeath(Character player) {
        ConsoleUtils.clearScreen();
        System.out.println("VOCÊ FOI DERROTADO...");
        System.out.println("Sua visão escurece...Você foi resgatado, mas não sabe por quem ou o quê...");

        // Revive o jogador com 60% da vida
        int reviveHP = (int)(player.getMaxHealth() * 0.6);
        // Garante que vida não fique em 0
        if (reviveHP <= 0) reviveHP = 1;
        
        player.heal(reviveHP);
        player.restoreMp(player.getMaxMp());

        // Penalidade: o jogador perde 60% do ouro quando morre
        int lostGold = (int)(player.getGold() * 0.3);
        player.setGold(player.getGold() - lostGold);

        System.out.println("\nVocê acorda em segurança na cidade de onde partiu");
        System.out.println("\nVocê está ferido e percebe que perdeu " + lostGold + " moedas de ouro");
        ConsoleUtils.pressEnter();

        // Procura a cidade anterior à Dungeon para enviar o jogador
        for (String exitName : this.exits.keySet()) {
            if (exitName.toLowerCase().contains("voltar") || exitName.toLowerCase().contains("retornar")) {
                return this.exits.get(exitName);    // Devolve a cidade como próximo mapa
            }
        }
        return null;    // Caso a àrea não tiver volta, dá Game Over
    }
}