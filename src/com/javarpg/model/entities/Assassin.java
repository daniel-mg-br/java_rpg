package com.javarpg.model.entities;

// Classe filha Assassino, foco em ataque e agilidade
public class Assassin extends Character {
    public Assassin (String name){
        super(name);
        // Status iniciais do Assassino
        this.setMaxHealth(90);
        this.setHealth(this.getMaxHealth());
        this.setMaxMp(45);
        this.setMp(this.getMaxMp());
        this.setAtack(12);
        this.setDefense(3);
        this.setAgility(10);
        this.setIntellig(5);
    }

    // Método para definir os nomes específicos da classe e do "tipo" de MP
    @Override public String getResourceName() {return "Stamina";}
    @Override public String getClassName() {return "Assassino";}

    // Método que define como o personagem melhora os atributos
    @Override protected void levelUp(){
        // Foco total em dano e velocidade
        this.setMaxHealth(this.getMaxHealth()+10);
        this.setMaxMp(this.getMaxMp()+8);
        this.setAtack(this.getAtack()+4);
        this.setAgility(this.getAgility()+3);
        this.setDefense(this.getDefense()+1);
        System.out.println("Você ficou mais letal e rápido!");
    }

    // Método que retorna o dano total do personagem
    @Override public int calculateDamage() {
        return this.getEffectiveAttack();
    }

    // Polimorfismo da habilidade especial: dano baseado puramente na agilidade
    @Override public boolean useSpecialHability(Character target) {
        int cost = 20;  // Custo do ataque
        
        if (this.useMp(cost)) {
            System.out.println("\n" + this.getName() + " usa um GOLPE DAS SOMBRAS!");

            int damage = this.getEffectiveAgility() * 3; // Influência exclusiva
            target.receiveDamage(damage);
            return true;
        }
        return false; 
    }
}
