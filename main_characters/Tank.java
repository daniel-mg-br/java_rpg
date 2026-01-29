package java_rpg.main_characters;

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

    @Override public String getResourceName() {return "Vigor";}
    @Override public String getClassName() {return "Tank";}

    @Override protected void levelUp(){
        // Foco maior na defesa
        this.setMaxHealth(this.getMaxHealth()+30);
        this.setMaxMp(this.getMaxMp()+5);
        this.setDefense(this.getDefense()+4);
        this.setAtack(this.getAtack()+1);
        System.out.println("Sua armadura ficou mais forte!");
    }
}
