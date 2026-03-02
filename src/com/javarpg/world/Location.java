package com.javarpg.world;
import com.javarpg.model.entities.Character;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

// Conceito base: Location é uma classe abstrata para servir de base para as cidades e as áreas exploráveis
public abstract class Location {
    // Atributos: nome e descrição do local
    protected String name;
    protected String description;

    // Hashmap para representar o mapa como um grafo
    // Chave (String): o nome do caminho
    // Valor (Location) : o objeto do mapa de destino 
    protected Map <String, Location> exits;

    // Métodos getter e setter padrão:
    public String getName() {return this.name;}
    public void setName(String name) {this.name = name;}

    public String getDescription() {return this.description;}
    public void setDescription(String description) {this.description = description;} 

    public Map <String, Location> getExits() {return this.exits;}

    // Método construtor
    public Location(String name, String description) {
        this.setName(name);
        this.setDescription(description);
        this.exits = new HashMap<>();
    }
    
    // Método para conectar este mapa a outro
    public void addExit(String exitName, Location destination) {
        this.exits.put(exitName, destination);
    }

    // Método para buscar aonde a ponte leva
    public Location getExit(String exitName) {
        return this.exits.get(exitName);
    }

    // Método abstrato, define o que acontece quando o jogador entra nele, retorna uma localização 
    public abstract Location enter(Character player, Scanner scanner);
}
