package com.javarpg.model.items;
import com.javarpg.model.entities.Character;

public class Equipment extends Item {
    public enum Type {WEAPON, ARMOR, HELMET}
    private int buffStat;
    private Type type;

    public Type getType() {return this.type;}
    public void setType(Type type) {this.type = type;}

    public int getBuffStat() {return this.buffStat;}
    public void setBuffStat(int buff) {this.buffStat = buff;}

    public Equipment(String name, int price, int buff, Type type) {
        super(name, price);
        this.buffStat = buff;
        this.type = type;
    }

    @Override public String apply(Character individual) {
        //individual.equip(this);
        return "Você equipou " + this.getItemName();
    }
}
