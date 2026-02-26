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

    // Método para definir os nomes específicos da classe e do "tipo" de MP
    @Override public String getResourceName(){return "Energia";}
    @Override public String getClassName() {return "Guerreiro";}

    // Método que define como o personagem melhora os atributos
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

    // Método que retorna o dano total do personagem
    @Override public int calculateDamage() {
        return this.getEffectiveAttack();
    }

    // Polimorfismo da habilidade especial: ataque com o dobro de dano
    @Override public boolean useSpecialHability(Character target) {
        int cost = 15; // Custo do ataque

        // O método useMp já verifica se tem Mp e subtrai
        if (this.useMp(cost)) {
            System.out.println("\n" + this.getName() + " usa GOLPE DEMOLIDOR!");

            int damage = this.getEffectiveAttack() * 2; // Dobro de dano normal
            target.receiveDamage(damage);
            return true;
        }
        return false; // Não tinha energia o suficiente
    }
}
