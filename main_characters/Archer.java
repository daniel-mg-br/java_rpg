package java_rpg.main_characters;

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

    @Override public String getResourceName() {return "Foco";}
    @Override public String getClassName() {return "Arqueiro";}

    @Override protected void levelUp(){
        // Melhora na rapidez e no dano
        this.setMaxHealth(this.getMaxHealth()+12);
        this.setMaxMp(this.getMaxMp()+8);
        this.setAtack(this.getAtack()+3);
        this.setAgility(this.getAgility()+2);
        this.setDefense(this.getDefense()+1);
        System.out.println("Sua mira e reflexos ficaram mais afiados");
    }
}
