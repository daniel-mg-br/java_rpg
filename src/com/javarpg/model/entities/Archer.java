package com.javarpg.model.entities;

// Classe filha Arqueiro, foco em agilidade e ataque (à distância)
public class Archer extends Character{
    public Archer(String name){
        super(name);
        // Status iniciais do arqueiro
        this.setMaxHealth(110);
        this.setHealth(this.getMaxHealth());
        this.setMaxMp(40);
        this.setMp(this.getMaxMp());
        this.setAtack(12);
        this.setDefense(5);
        this.setAgility(9);
        this.setIntellig(6);
    }

    // Método para definir os nomes específicos da classe e do "tipo" de MP
    @Override public String getResourceName() {return "Foco";}
    @Override public String getClassName() {return "Arqueiro";}

    // Método que define como o personagem melhora os atributos
    @Override protected void levelUp(){
        // Melhora na rapidez e no dano
        this.setMaxHealth(this.getMaxHealth()+12);
        this.setMaxMp(this.getMaxMp()+8);
        this.setAtack(this.getAtack()+3);
        this.setAgility(this.getAgility()+2);
        this.setDefense(this.getDefense()+1);
        System.out.println("Sua mira e reflexos ficaram mais afiados");
    }

    // Método que retorna o dano total do personagem
    @Override public int calculateDamage() {
        return this.getEffectiveAttack();
    }

    // Polimorfismo da habilidade especial: ataque levemente mais forte baseado na agilidade
    @Override public boolean useSpecialHability(Character target) {
        int cost = 15;  // Custo do ataque

        if (this.useMp(cost)) {
            System.out.println("\n" + this.getName() + " dispara um TIRO PENETRANTE!");

            int damage = (int)((this.getEffectiveAttack() * 1.5) + this.getEffectiveAgility()); // Calculo do dano baseado também na agilidade

            target.receiveDamage(damage);
            return true;
        }
        return false; 
    }
}
