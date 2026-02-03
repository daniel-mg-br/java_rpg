package com.javarpg.model.entities;

// Classe filha Mago, foco em inteligência
public class Mage extends Character {
    public Mage(String name){
        super(name);
        // Status inicial do mago
        this.setMaxHealth(80);
        this.setHealth(this.getMaxHealth());
        this.setMaxMp(80);
        this.setMp(this.getMaxMp());
        this.setAtack(2);
        this.setDefense(2);
        this.setAgility(5);
        this.setIntellig(18);
    }

    @Override public String getResourceName(){return "Mana";}
    @Override public String getClassName() {return "Mago";}

    @Override protected void levelUp(){
        // Melhoria com foco em poder mágico (experiência)
        this.setMaxHealth(this.getMaxHealth()+8);
        this.setMaxMp(this.getMaxMp()+20);
        this.setIntellig(this.getIntellig()+5);
        this.setAgility(this.getAgility()+1);
        System.out.println("Seu conhecimento arcano expandiu!");
    }
}

