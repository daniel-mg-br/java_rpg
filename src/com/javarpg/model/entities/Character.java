package com.javarpg.model.entities;

// Importando os outros diretórios do projeto
import com.javarpg.model.items.Item;
import com.javarpg.model.items.Equipment;
// Importando as estruturas de dados necessárias
import java.util.ArrayList;
import java.util.List;


// Classe abstrata que serve de molde para as outras classes
public abstract class Character {
    // Atributos protegidos para que os filhos possam acessar
    protected String name;
    protected int health, maxHealth;
    protected int atack, defense, agility, intellig;
    protected int mp, maxMp;
    // Sistema de nível e XP
    protected int level, exp, nxtLevelXp;

    // Inventário implementado com uma lista
    protected List <Item> inventory = new ArrayList<>();

    // Itens equipáveis: arma, armadura e capacete
    protected Equipment weapon;
    protected Equipment armor;
    protected Equipment helmet;

    // Métodos Getter e Setter padrões
    public String getName() {return this.name;}
    public void setName(String name) {this.name = name;}

    public int getHealth() {return this.health;}
    public void setHealth(int health) {this.health = health;}

    public int getMaxHealth() {return this.maxHealth;}
    public void setMaxHealth(int maxHealth) {this.maxHealth = maxHealth;}

    public int getAtack() {return this.atack;}
    public void setAtack(int atack) {this.atack = atack;}

    public int getDefense() {return this.defense;}
    public void setDefense(int defense) {this.defense = defense;}

    public int getAgility() {return this.agility;}
    public void setAgility(int agility) {this.agility = agility;}

    public int getIntellig() {return this.intellig;}
    public void setIntellig(int intellig) {this.intellig = intellig;}

    public int getMp() {return this.mp;}
    public void setMp(int mp) {this.mp = mp;}

    public int getMaxMp() {return this.maxMp;}
    public void setMaxMp(int maxMp) {this.maxMp = maxMp;}

    public int getLevel() {return this.level;}
    public void setLevel(int level) {this.level = level;}

    public int getExp() {return this.exp;}
    public void setExp(int exp) {this.exp = exp;}

    public int getNxtLevelXp() {return this.nxtLevelXp;}
    public void setNxtLevelXp(int nxtLevelXp) {this.nxtLevelXp = nxtLevelXp;}

    // Método Construtor
    public Character (String name){
        this.setName(name);
        this.setLevel(1);               // Nível inicial
        this.setExp(0);                   // Experiência inicial
        this.setNxtLevelXp(100);   // Precisa de 100 exp para subir de nível  
    }

    // Métodos para pegar o tipo de MP e o nome da classe
    public abstract String getResourceName();
    public abstract String getClassName();

    // Método para gastar MP (retorna true se conseguir usar)
    public boolean useMp(int cost){
        if (this.getMp() >= cost){
            this.setMp(this.getMp()-cost);
            return true;
        } else {
            System.out.println(this.getName() + " não tem mana o suficiente!");
            return false;
        }
    }

    // Adicionando um item ao inventário (a lista)
    public void addItem(Item item) {
        inventory.add(item);
        System.out.println("Você pegou: " + item.getItemName());
    }

    // Método para equipar itens equipáveis
    public void equipItem(Equipment newEquipment) {
        // Caso o item seja uma arma
        if (newEquipment.getType() == Equipment.Type.WEAPON) {
            if (this.weapon != null) inventory.add(this.weapon);
            this.weapon = newEquipment;
        }
        // Caso o item seja uma armadura
        else if (newEquipment.getType() == Equipment.Type.ARMOR) {
            if (this.armor != null) inventory.add(this.armor);
            this.armor = newEquipment;
        }
        // Caso o item seja um capacete
        else if (newEquipment.getType() == Equipment.Type.HELMET) {
            if (this.helmet != null) inventory.add(this.helmet);
            this.helmet = newEquipment;
        }
        // Depois que atribuímos o valor para weapon/armor/helmet, tiramos o item do inventário
        inventory.remove(newEquipment);
    }

    // Calcula o ataque total do jogador após o buff do item equipável
    public int getEffectiveAttack() {
        int total = this.getAtack();

        // Confere se o atributo melhorado é o ATAQUE
        if (this.weapon != null && this.weapon.getStatBonus() == Equipment.StatBonus.ATTACK) {
            total += this.weapon.getBuffStat();
        }
        return total;
    }

    // Calcula a defesa total do jogador após o buff do item equipável
    public int getEffectiveDefense() {
        int total = this.getDefense();

        // Confere se o atributo melhorado é a DEFESA
        if (this.armor != null && this.helmet.getStatBonus() == Equipment.StatBonus.DEFENSE) {
            total += this.armor.getBuffStat();
        }
        // Mesma lógica para o capacete
        if (this.helmet != null && this.helmet.getStatBonus() == Equipment.StatBonus.DEFENSE) {
            total += this.helmet.getBuffStat();
        }
        return total;
    }

    // Calcula a inteligência total do jogador após o buff do item equipável
    public int getEffectiveIntelligence() {
        int total = this.getIntellig();
        
        // Confere se o atributo melhorado é a INTELIGÊNCIA
        if (this.weapon != null && this.weapon.getStatBonus() == Equipment.StatBonus.INTELLIGENCE) {
            total += this.weapon.getBuffStat();
        }
        return total;
    }

    // Calcula a agilidade total do jogador após o buff do item equipável
    public int getEffectiveAgility() {
        int total = this.getAgility();

        // Confere se o atributo melhorado é a AGILIDADE
        if (this.weapon != null && this.weapon.getStatBonus() == Equipment.StatBonus.AGILITY) {
            total += this.weapon.getBuffStat();
        }
        // Tanto armas quanto armaduras podem garantir uma melhora na agilidade
        if (this.armor != null && this.armor.getStatBonus() == Equipment.StatBonus.AGILITY) {
            total += this.armor.getBuffStat();
        }

        return total;
    }

    // Método abstrato para calcular o dano total do personagem
    public abstract int calculateDamage();

    // Método para o personagem receber o dano
    public void receiveDamage(int damage) {
        // Calcula primeiro o dano efetivo, descontando a defesa
        int effectiveDefense = this.getEffectiveDefense();
        int damageTaken = damage - effectiveDefense;

        // Garante que o dano seja negativo
        if (damageTaken <= 0) {
            damageTaken = 1;
            System.out.println(this.getName() + " recebeu " + damageTaken + " de dano!");
        } else {
            System.out.println(this.getName() + " recebeu " + damageTaken + " de dano!");
        }

        // Aplicando o dano
        this.setHealth(this.getHealth() - damageTaken);

        // Evita vida negativa
        if (this.getHealth() < 0) {
            this.setHealth(0);
        }
    }

    // Método para curar a vida do personagem
    public void heal(int value) {
        this.setHealth(this.getHealth() + value);

        // Caso o personagem ganhe mais vida do que consegue, fica com o máximo de vida
        if (this.getHealth() > this.getMaxHealth()) {
            this.setHealth(this.getMaxHealth());
        }
    }

    // Método para curar o MP do personagem
    public void restoreMp(int value) {
        this.setMp(this.getMp() + value);

        // Caso o personagem ganhe mais MP do que consegue, fica com o máximo de MP
        if (this.getMp() > this.getMaxMp()) {
            this.setMp(this.getMaxMp());
        }
    }

    // Método para ganho de experiência igual para todas as classes
    public void wonExperience(int xpWon){
        this.setExp(xpWon);
        System.out.println(this.getName() + " ganhou " + xpWon + " de experiência");

        // Verifica se subiu de nível (pode subir mais de um nível de uma vez)
        while (this.getExp() >= this.getNxtLevelXp()){
            this.setExp(this.getExp() - this.getNxtLevelXp());  // Remove o exp usado
            this.setLevel(this.getLevel()+1);
            this.setNxtLevelXp(this.getNxtLevelXp()+50);        // Dificulta para o próximo nível
            // Cada classe melhora atributos de um jeito diferente (Polimorfismo)
            levelUp();
            this.setHealth(this.getMaxHealth());    // Cura o personagem ao upar
            System.out.println("Parabéns! " + this.getName() + " subiu de nível!");
        }
    }

    // Cada classe filha deve implementar sua própria versão de como fica mais forte
    protected abstract void levelUp();

    // Getter básicos para exibir o status do personagem
    public void getStatus(){
        System.out.println("========================================================");
        System.out.println("STATUS DO JOGADOR");
        System.out.println("--------------------------------------------------------");
        System.out.println("Nome: " + this.getName() + " | " + this.getClassName());
        System.out.println("Vida: " + this.getHealth() + " / " + this.getMaxHealth());
        System.out.println(this.getResourceName() + ": " + this.getMp() + " / " + this.getMaxMp());
        System.out.println("Ataque: " + this.getEffectiveAttack());
        System.out.println("Defesa: " + this.getEffectiveDefense());
        System.out.println("Agilidade: " + this.getEffectiveAgility());
        System.out.println("Inteligência: " + this.getEffectiveIntelligence());
        System.out.println("--------------------------------------------------------");
    }
}
