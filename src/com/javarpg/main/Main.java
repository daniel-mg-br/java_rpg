package com.javarpg.main;
import com.javarpg.model.entities.*;
import com.javarpg.model.items.*;
import com.javarpg.system.BattleManager;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== INICIANDO O RPG ===");

        // 1. Criando o Herói
        Warrior heroi = new Warrior("Kael");
        System.out.println("Herói criado: " + heroi.getName());

        // 2. Dando equipamentos iniciais para ele não morrer fácil
        Equipment espada = new Equipment("Espada Enferrujada", 10, 5, Equipment.Type.WEAPON, Equipment.StatBonus.ATTACK);
        Equipment armadura = new Equipment("Túnica Velha", 10, 3, Equipment.Type.ARMOR, Equipment.StatBonus.DEFENSE);
        Consumable pocao = new Consumable("Poção Menor", 5, 20, "Vida");

        heroi.addItem(espada);
        heroi.addItem(armadura);
        heroi.addItem(pocao);

        heroi.printInventory();
        
        // Equipando automaticamente para o teste
        heroi.useItem(espada);
        heroi.useItem(armadura);

        // 3. Criando o Monstro (Nome, HP, Atk, Def, XP, Gold)
        // Slime: 40 HP, 8 Atk, 1 Def, 50 XP, 10 Gold
        Enemy monstro = new Enemy("Slime Viscoso", 40, 8, 1, 50, 10);

        // 4. INICIANDO A BATALHA
        // Aqui acontece a mágica: passamos um 'Warrior' onde pede 'Character'
        BattleManager.startBattle(heroi, monstro, scanner);

        // 5. Pós-Batalha
        heroi.getStatus();
        scanner.close();
    }
}