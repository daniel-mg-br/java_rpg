package com.javarpg.main;
import com.javarpg.model.entities.*;
import com.javarpg.model.items.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("===== INICIANDO O AMBIENTE DE TESTES =====\n");

        Warrior guerreiro = new Warrior("Arthur");
        System.out.println("Guerreiro criado!");
        guerreiro.getStatus();

        Equipment espadaFerro = new Equipment("Espada de Ferro", 50, 15, Equipment.Type.WEAPON, Equipment.StatBonus.ATTACK);

        Equipment capaceteCouro = new Equipment("Capacete de Couro", 30, 5, Equipment.Type.HELMET, Equipment.StatBonus.DEFENSE);

        Consumable pocaoVida = new Consumable("Poção de Vida Pequena", 10, 40, "Vida");

        System.out.println("\n----- Testando o Inventário -----\n");
        guerreiro.addItem(espadaFerro);
        guerreiro.addItem(capaceteCouro);
        guerreiro.addItem(pocaoVida);
        guerreiro.addItem(pocaoVida);
        guerreiro.printInventory();

        System.out.println("\n----- Testando Equipamentos e Buffs");
        guerreiro.useItem(espadaFerro);
        guerreiro.useItem(capaceteCouro);
        guerreiro.getStatus();
        guerreiro.printInventory();

        System.out.println("\n----- Simulando um combate -----");
        System.out.println("Ataque de monstro com 20 de dano!");
        guerreiro.receiveDamage(20);

        System.out.println("\n----- Testando consumíveis");
        System.out.println("HP atual: " + guerreiro.getHealth());
        guerreiro.useItem(pocaoVida);
        System.out.println("HP após poção: " + guerreiro.getHealth());

        guerreiro.printInventory();

        System.out.println("\n----- Testando o desequipar -----");
        guerreiro.unequipItem(Equipment.Type.WEAPON);

        System.err.println("\n----- STATUS FINAL -----");
        guerreiro.getStatus();
        guerreiro.printInventory();
    }
}