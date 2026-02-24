package com.javarpg.model.entities;

// Classe filha Inimigo, que luta contra o jogador
public class Enemy extends Character{
    // Método construtor flexível para qualquer monstro (slime, goblin, etc)

    public Enemy(String name, int maxHealth, int attack, int defense, int xpReward, int goldReward) {
        super(name);
        this.setMaxHealth(maxHealth);
        this.setHealth(maxHealth);
        this.setAtack(attack);
        this.setDefense(defense);
        this.setNxtLevelXp(xpReward);   // Quanto XP ele dá ao morrer
        this.setGold(goldReward);
    }

    // Inimigos usam stamina como MP
    @Override public String getResourceName() {return "Stamina";}

    // Nome padrão da classe
    @Override public String getClassName() {return "Inimigo";}

    // Inimigos não sobem de nível no meio da batalha
    @Override protected void levelUp() {}

    // Inimigos atacam ocom ataque base
    @Override public int calculateDamage() {
        return this.getAtack();
    }
}
