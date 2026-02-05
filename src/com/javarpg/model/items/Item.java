package com.javarpg.model.items;
import com.javarpg.model.entities.Character;

// Classe abstrata Item que servirá de molde para equipamentos / consumíveis
public abstract class Item {
    protected String itemName;
    protected int itemPrice;
    
    // Getters e Setters padrão
    public String getItemName() {return this.itemName;}
    public void setItemName(String name) {this.itemName = name;}

    public int getItemPrice() {return this.itemPrice;}
    public void setItemPrice(int price) {this.itemPrice = price;}

    // Método construtor
    public Item(String name, int price) {
        this.setItemName(name);
        this.setItemPrice(price);
    }

    // Método abstrato: cada item decide o quê faz quando é usado
    // Retorna uma string descrevendo o que aconteceu
    public abstract String apply(Character individual);        
}
