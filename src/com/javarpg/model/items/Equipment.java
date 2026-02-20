package com.javarpg.model.items;
import com.javarpg.model.entities.Character;

// Classe filha Equiváveis, para itens que o jogador pode segurar / vestir e que conferem uma melhora em algum atributo
public class Equipment extends Item {
    // Enum para relacionar qual tipo é o item equipado
    public enum Type {WEAPON, ARMOR, HELMET}
    // Enum para relacionar qual atributo o item melhora
    public enum StatBonus {ATTACK, DEFENSE, INTELLIGENCE, AGILITY}

    private Type type;              // Tipo do equipável
    private StatBonus statBonus;    // Qual atributo é melhorado
    private int buffStat;           // Quantos pontos de melhora

    // Métodos getter e setter padrão
    public Type getType() {return this.type;}
    public void setType(Type type) {this.type = type;}

    public StatBonus getStatBonus() {return this.statBonus;}
    public void setStatBonus(StatBonus statBonus) {this.statBonus = statBonus;}

    public int getBuffStat() {return this.buffStat;}
    public void setBuffStat(int buff) {this.buffStat = buff;}

    // Método construtor
    public Equipment(String name, int price, int buff, Type type, StatBonus statBonus) {
        super(name, price);
        this.setBuffStat(buff);
        this.setType(type);
        this.setStatBonus(statBonus);
    }

    //Polimorfismo do método aplicar: aqui os itens são equipados para melhorar algum atributo do jogador
    @Override public String apply(Character individual) {
        boolean success = individual.equipItem(this);
        
        if (success) return "Você equipou " + this.getItemName();
        else return "";
    }
}
