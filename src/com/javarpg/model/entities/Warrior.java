package com.javarpg.model.entities;

// Classe filha Guerreira: foco em vida e ataque
public class Warrior extends Character{
    public Warrior (String name){
        super(name);
        // Status iniciais do guerreiro
        this.setMaxHealth(150);
        this.setHealth(this.maxHealth);
        this.setMaxMp(20);
        this.setMp(this.getMaxMp());
        this.setAtack(10);
        this.setDefense(8);
        this.setAgility(4);
        this.setIntellig(2);
    }

    @Override public String getResourceName(){return "Energia";}
    @Override public String getClassName() {return "Guerreiro";}

    @Override protected void levelUp(){
        // Guerreiro ganha mais vida e ataque
        this.setMaxHealth(this.getMaxHealth()+20);
        this.setMaxMp(this.getMaxMp()+5);
        this.setAtack(this.getAtack()+3);
        this.setDefense(this.getDefense()+2);
        this.setAgility(this.getAgility()+1);
        // Inteligência não sobe ou muito pouco
        System.out.println("Sua força e resistência aumentaram consideravelmente!");
    }    
}
