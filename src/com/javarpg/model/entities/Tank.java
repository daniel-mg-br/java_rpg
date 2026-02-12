package com.javarpg.model.entities;

// Classe filha Tank, foco em vida e defesa
public class Tank extends Character {
    public Tank(String name){
        super(name);
        // Status iniciais do Tank
        this.setMaxHealth(200);
        this.setHealth(this.getMaxHealth());
        this.setMaxMp(25);
        this.setMp(this.getMaxMp());
        this.setAtack(8);
        this.setDefense(15);
        this.setAgility(2);
        this.setIntellig(2);
    }

    // Método para definir os nomes específicos da classe e do "tipo" de MP
    @Override public String getResourceName() {return "Vigor";}
    @Override public String getClassName() {return "Tank";}

    // Método que define como o personagem melhora os atributos
    @Override protected void levelUp(){
        // Foco maior na defesa
        this.setMaxHealth(this.getMaxHealth()+30);
        this.setMaxMp(this.getMaxMp()+5);
        this.setDefense(this.getDefense()+4);
        this.setAtack(this.getAtack()+1);
        System.out.println("Sua armadura ficou mais forte!");
    }

    // Método que retorna o dano total do personagem
    @Override public int calculateDamage() {
        return this.getEffectiveAttack();
    }
}
