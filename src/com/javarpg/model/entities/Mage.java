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

    // Método para definir os nomes específicos da classe e do "tipo" de MP
    @Override public String getResourceName(){return "Mana";}
    @Override public String getClassName() {return "Mago";}

    // Método que define como o personagem melhora os atributos
    @Override protected void levelUp(){
        // Melhoria com foco em poder mágico (experiência)
        this.setMaxHealth(this.getMaxHealth()+8);
        this.setMaxMp(this.getMaxMp()+20);
        this.setIntellig(this.getIntellig()+5);
        this.setAgility(this.getAgility()+1);
        System.out.println("Seu conhecimento arcano expandiu!");
    }

    // Método que retorna o dano total do personagem
    @Override public int calculateDamage() {
        return this.getEffectiveIntelligence();
    }
}

