package java_rpg.main_characters;

// Classe filha Assassino, foco em ataque e agilidade
public class Assassin extends Character {
    public Assassin (String name){
        super(name);
        // Status iniciais do Assassino
        this.setMaxHealth(90);
        this.setMaxHealth(this.getMaxHealth());
        this.setMaxMp(45);
        this.setMp(this.getMaxMp());
        this.setAtack(12);
        this.setDefense(3);
        this.setAgility(10);
        this.setIntellig(5);
    }

    @Override public String getResourceName() {return "Stamina";}
    @Override public String getClassName() {return "Assassino";}

    @Override protected void levelUp(){
        // Foco total em dano e velocidade
        this.setMaxHealth(this.getMaxHealth()+10);
        this.setMaxMp(this.getMaxMp()+8);
        this.setAtack(this.getAtack()+4);
        this.setAgility(this.getAgility()+3);
        this.setDefense(this.getDefense()+1);
        System.out.println("Você ficou mais letal e rápido!");
    }
}
