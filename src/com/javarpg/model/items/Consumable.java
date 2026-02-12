package com.javarpg.model.items;
import com.javarpg.model.entities.Character;

// Classe filha Consumível, um item que pode ser usado pelo jogador para obter efeito de restauração
public class Consumable extends Item {
    private int healValue;  // Pode ser HP e MP
    private String itemType; // "Vida" ou "Mana"

    // Getters e Setters padrão
    public int getHealValue() {return this.healValue;}
    public void setHealValue(int heal) {this.healValue = heal;}

    public String getItemType() {return this.itemType;}
    public void setItemType(String type) {this.itemType = type;}

    // Método construtor
    public Consumable (String name, int price, int heal, String type) {
        super(name, price);
        this.setHealValue(heal);
        this.setItemType(type);
    }

    // Polimorfismo do método apply: aqui os items são usados para recuperar vida ou mana do jogador    
    @Override public String apply(Character individual) {
        if (this.getItemType().equalsIgnoreCase("Vida")) {
            individual.heal(this.getHealValue());
            return "Você usou " + this.getItemName() + " e curou " + this.getHealValue() + " de HP!";
        }

        if (this.getItemType().equalsIgnoreCase("Mana")) {
            individual.restoreMp(this.getHealValue());
            return "Você usou " + this.getItemName() + " e recuperou " + this.getHealValue() + " de MP!";
        }
        return "Item usado sem efeito...";
    }
}
