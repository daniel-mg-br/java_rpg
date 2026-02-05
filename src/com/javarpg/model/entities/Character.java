package com.javarpg.model.entities;

import com.javarpg.model.items.Item;
import com.javarpg.model.items.Equipment;
import java.util.ArrayList;
import java.util.List;


// Classe abstrata que serve de molde para as outras classes
public abstract class Character {
    // Atributos protegidos para que os filhos possam acessar
    protected String name;
    protected int health, maxHealth;
    protected int atack, defense, agility, intellig;
    protected int mp, maxMp;
    // Sistema de nível e XP
    protected int level, exp, nxtLevelXp;

    // Inventário e equipamentos implementados com listas
    protected List <Item> inventory = new ArrayList();
    protected Equipment weapon;
    protected Equipment armor;

    // Métodos Getter e Setter padrões
    public String getName() {return this.name;}
    public void setName(String name) {this.name = name;}

    public int getHealth() {return this.health;}
    public void setHealth(int health) {this.health = health;}

    public int getMaxHealth() {return this.maxHealth;}
    public void setMaxHealth(int maxHealth) {this.maxHealth = maxHealth;}

    public int getAtack() {return this.atack;}
    public void setAtack(int atack) {this.atack = atack;}

    public int getDefense() {return this.defense;}
    public void setDefense(int defense) {this.defense = defense;}

    public int getAgility() {return this.agility;}
    public void setAgility(int agility) {this.agility = agility;}

    public int getIntellig() {return this.intellig;}
    public void setIntellig(int intellig) {this.intellig = intellig;}

    public int getMp() {return this.mp;}
    public void setMp(int mp) {this.mp = mp;}

    public int getMaxMp() {return this.maxMp;}
    public void setMaxMp(int maxMp) {this.maxMp = maxMp;}

    public int getLevel() {return this.level;}
    public void setLevel(int level) {this.level = level;}

    public int getExp() {return this.exp;}
    public void setExp(int exp) {this.exp = exp;}

    public int getNxtLevelXp() {return this.nxtLevelXp;}
    public void setNxtLevelXp(int nxtLevelXp) {this.nxtLevelXp = nxtLevelXp;}

    // Método Construtor
    public Character (String name){
        this.setName(name);
        this.setLevel(1);               // Nível inicial
        this.setExp(0);                   // Experiência inicial
        this.setNxtLevelXp(100);   // Precisa de 100 exp para subir de nível  
    }

    // Métodos para pegar o tipo de MP e o nome da classe
    public abstract String getResourceName();
    public abstract String getClassName();

    // Método para gastar MP (retorna true se conseguir usar)
    public boolean useMp(int cost){
        if (this.getMp() >= cost){
            this.setMp(this.getMp()-cost);
            return true;
        } else {
            System.out.println(this.getName() + " não tem mana o suficiente!");
            return false;
        }
    }

    public void addItem(Item item) {
        inventory.add(item);
        System.out.println("Você pegou: " + item.getItemName());
    }

    public void equipItem(Equipment newEquipment) {
        if (newEquipment.getType() == Equipment.Type.WEAPON) {
            if (this.weapon != null) inventory.add(this.weapon);
            this.weapon = newEquipment;
        }
        else if (newEquipment.getType() == Equipment.Type.ARMOR) {
            if (this.armor != null) inventory.add(this.armor);
            this.armor = newEquipment;
        }
        inventory.remove(newEquipment);
    }

    public int getTotalAttack() {
        this.setAtack(10);
        int bonusWeapon = (weapon != null) ? weapon.getBuffStat() : 0;
        return this.getAtack() + bonusWeapon;
    }

    public void heal(int value) {
        this.setHealth(this.getHealth() + value);

        if (this.getHealth() > this.getMaxHealth()) {
            this.setHealth(this.getMaxHealth());
        }
    }

    // Método para ganho de experiência igual para todas as classes
    public void wonExperience(int xpWon){
        this.setExp(xpWon);
        System.out.println(this.getName() + " ganhou " + xpWon + " de experiência");

        // Verifica se subiu de nível (pode subir mais de um nível de uma vez)
        while (this.getExp() >= this.getNxtLevelXp()){
            this.setExp(this.getExp() - this.getNxtLevelXp());  // Remove o exp usado
            this.setLevel(this.getLevel()+1);
            this.setNxtLevelXp(this.getNxtLevelXp()+50);        // Dificulta para o próximo nível
            // Cada classe melhora atributos de um jeito diferente (Polimorfismo)
            levelUp();
            this.setHealth(this.getMaxHealth());    // Cura o personagem ao upar
            System.out.println("Parabéns! " + this.getName() + " subiu de nível!");
        }
    }

    // Cada classe filha deve implementar sua própria versão de como fica mais forte
    protected abstract void levelUp();

    // Getter básicos para exibir o status do personagem
    public void getStatus(){
        System.out.println("========================================================");
        System.out.println("STATUS DO JOGADOR");
        System.out.println("--------------------------------------------------------");
        System.out.println("Nome: " + this.getName() + " | " + this.getClassName());
        System.out.println("Vida: " + this.getHealth() + " / " + this.getMaxHealth());
        System.out.println(this.getResourceName() + ": " + this.getMp() + " / " + this.getMaxMp());
        System.out.println("Ataque: " + this.getAtack());
        System.out.println("Defesa: " + this.getDefense());
        System.out.println("Agilidade: " + this.getAgility());
        System.out.println("Inteligência: " + this.getIntellig());
        System.out.println("--------------------------------------------------------");
    }
}
