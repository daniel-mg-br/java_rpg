package com.javarpg.model.entities;

// Importando os outros diretórios do projeto
import com.javarpg.model.items.Item;
import com.javarpg.model.items.Equipment;
import com.javarpg.model.items.Consumable;
import com.javarpg.model.quests.*;
// Importando as estruturas de dados necessárias
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


// Classe abstrata que serve de molde para as outras classes
public abstract class Character {
    // Atributos protegidos para que os filhos possam acessar
    protected String name;
    protected int health, maxHealth;
    protected int atack, defense, agility, intellig;
    protected int mp, maxMp;
    // Sistema de nível e XP
    protected int level, exp, nxtLevelXp;
    // Ouro que o personagem carrega
    protected int gold;

    // Inventário implementado com uma lista, junto de um variável para controlar o espaço
    protected List <Item> inventory;
    protected int maxInventorySize = 30;

    // Itens equipáveis: arma, armadura e capacete
    protected Equipment weapon;
    protected Equipment armor;
    protected Equipment helmet;

    // Atributos para buffs temporários em batalhas
    protected int battleDefenseBuff = 0;
    protected int battleAttackBuff = 0;

    // O diário de missões como uma lista de Quests
    protected List <Quest> questLog;

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

    public int getGold() {return this.gold;}
    public void setGold(int gold) {this.gold = gold;}

    public int getBattleDefenseBuff() {return this.battleDefenseBuff;}
    public void setBattleDefenseBuff(int battleDefenseBuff) {this.battleDefenseBuff = battleDefenseBuff;}

    public int getBattleAttackBuff() {return this.battleAttackBuff;}
    public void setBattleAttackBuff(int battleAttackBuff) {this.battleAttackBuff = battleAttackBuff;}

    // Método Construtor
    public Character (String name){
        this.setName(name);
        this.setLevel(1);               // Nível inicial
        this.setExp(0);                   // Experiência inicial
        this.setNxtLevelXp(100);   // Precisa de 100 exp para subir de nível  
        this.setGold(0);

        // Inicializando a a lista do inventário e a lista de missões
        this.inventory = new ArrayList<>();
        this.questLog = new ArrayList<>();
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

    // Método para adicionar itens ao inventário, usando sobrecarga de método
    public boolean addItem(Item item) {
        return this.addItem(item, false);
    }

    // O método principal, a sobrecarga é usada por questões de UX
    public boolean addItem(Item item, boolean silent) {
        // Verifica se tem espaço no inventário
        if (inventory.size() < maxInventorySize) {
            inventory.add(item);
            
            // Só imprime a mensagem se não for silencioso (pegou do chão)
            if (!silent) System.out.println("Você pegou: " + item.getItemName());

            // Contando os items para as side quests
            this.updateQuests();  
            return true;
        } else {
            if (!silent) System.out.println("Inventário cheio!");
            return false;
        }
    }

    // Método para remover itens do inventário
    public void dropItem(Item item) {
        // Confere se o item está na mochila primeiro
        if (inventory.contains(item)) {
            inventory.remove(item);
            System.out.println("Você jogou fora: " + item.getItemName());
        } else {
            System.out.println("Você não tem esse item na mochila!");
        }
    }

    // Método para equipar itens equipáveis
    public boolean equipItem(Equipment newEquipment) {
        // Se o item novo está na mochila, não ocupa espaço extra
        inventory.remove(newEquipment);
        Equipment oldItem = null;

        // Identifica se já tem algo equipado
        if (newEquipment.getType() == Equipment.Type.WEAPON) oldItem = this.weapon;
        else if (newEquipment.getType() == Equipment.Type.ARMOR) oldItem = this.armor;
        else if (newEquipment.getType() == Equipment.Type.HELMET) oldItem = this.helmet;

        // Se tiver, tentamos guardá-lo no inventário
        if (oldItem != null) {
            // Tenta adicionar no inventário
            if (inventory.size() < maxInventorySize) {
                inventory.add(oldItem);
                System.out.println("Você guardou: " + oldItem.getItemName());
            } else {
                // Se não cabe o item antigo, cancela a operação
                System.out.println("Iventário cheio! Não dá para trocar equipamento!");
                inventory.add(newEquipment);
                return false; // Sai do método sem equipar
            }
        }

        // Efetivando a ação de equipar
        if (newEquipment.getType() == Equipment.Type.WEAPON) this.weapon = newEquipment;
        else if (newEquipment.getType() == Equipment.Type.ARMOR) this.armor = newEquipment;
        else if (newEquipment.getType() == Equipment.Type.HELMET) this.helmet = newEquipment;

        return true;
    }

    public void unequipItem(Equipment.Type type) {
        Equipment itemToRemove = null;

        // Identificando o tipo de item a ser removido
        if (type == Equipment.Type.WEAPON) itemToRemove = this.weapon;
        else if (type == Equipment.Type.ARMOR) itemToRemove = this.armor;
        else if (type == Equipment.Type.HELMET) itemToRemove = this.helmet;

        // Verifica se tem algo para tirar
        if (itemToRemove == null) {
            System.out.println("Não há nada equipado nesse slot!");
            return;
        }

        // Tenta guardar na mochila, se estiver cheia, não é retirado
        if (this.addItem(itemToRemove, true)) {
            if (type == Equipment.Type.WEAPON) this.weapon = null;
            else if (type == Equipment.Type.ARMOR) this.armor = null;
            else if (type == Equipment.Type.HELMET) this.helmet = null;

            System.out.println("Você desequipou: " + itemToRemove.getItemName());
        } else {
            System.out.println("Esvazie o inventário antes de desequipar!");
        }
    }

    // Método para usar itens no geral, chamando os métodos dos equipáveis e consumíveis 
    public void useItem(Item item) {
        // O item executa o próprio efeito dele (recuperar HP/MP ou equipar)
        String mensagem = item.apply(this);
        System.out.println(mensagem);

        // Os consumíveis precisam ser removidos depois de usados
        if (item instanceof Consumable) {
            inventory.remove(item);
        }
    }

    // Calcula o ataque total do jogador após o buff do item equipável
    public int getEffectiveAttack() {
        int total = this.getAtack();

        // Confere se o atributo melhorado é o ATAQUE
        if (this.weapon != null && this.weapon.getStatBonus() == Equipment.StatBonus.ATTACK) {
            total += this.weapon.getBuffStat();
        }

        total += this.getBattleAttackBuff();
        return total;
    }

    // Calcula a defesa total do jogador após o buff do item equipável
    public int getEffectiveDefense() {
        int total = this.getDefense();

        // Confere se o atributo melhorado é a DEFESA
        if (this.armor != null && this.armor.getStatBonus() == Equipment.StatBonus.DEFENSE) {
            total += this.armor.getBuffStat();
        }
        // Mesma lógica para o capacete
        if (this.helmet != null && this.helmet.getStatBonus() == Equipment.StatBonus.DEFENSE) {
            total += this.helmet.getBuffStat();
        }

        total += this.getBattleAttackBuff();
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

    // Método abstrato: retorna true se conseguir usar a habilidade especial
    public abstract boolean useSpecialHability(Character target);

    // Método para o personagem receber o dano
    public void receiveDamage(int damage) {
        // Calcula primeiro o dano efetivo, descontando a defesa
        int effectiveDefense = this.getEffectiveDefense();
        int damageTaken = damage - effectiveDefense;

        // Garante que o dano seja negativo
        if (damageTaken <= 1) {
            damageTaken = 1;
            System.out.println("A armadura de " + this.getName() + " absorveu muito dano!");
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

    // Método para zerar os buffs temporários
    public void resetBuffs() {
        this.setBattleAttackBuff(0);
        this.setBattleDefenseBuff(0);
    }

    // Método para ganho de experiência igual para todas as classes
    public void wonExperience(int xpWon){
        // Somar a nova experiência à que o jogador já possuía  
        this.setExp(this.getExp() + xpWon);
        System.out.println(this.getName() + " ganhou " + xpWon + " de experiência!");

        // Verifica se subiu de nível (pode subir mais de um por vez)
        while (this.getExp() >= this.getNxtLevelXp()) {
            // Subtrair a experiência necessária para o nível
            this.setExp(this.getExp() - this.getNxtLevelXp());

            this.setLevel(this.getLevel() + 1);
            this.setNxtLevelXp(this.getNxtLevelXp() + 50);  // Dificulta para o próximo nível
            levelUp();

            this.setHealth(this.getMaxHealth());    // Cura o personagem ao subir de nível
            System.out.println("Parabéns " + this.getName() + " subiu para o nível " + this.getLevel() + "!");
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

    // Método que retorna o inventário do jogador
    public List <Item> getInventory() {
        return this.inventory;
    }

    // Método para mostrar o inventário para o usuário
    public void printInventory() {
        // Confere se a mochila está vazia primeiro
        if (inventory.isEmpty()) {
            System.out.println("Mochila vazia!");
            return;
        }

        System.out.println("===== MOCHILA (" + inventory.size() + "/" + maxInventorySize + ") =====");

        // Cria um mapa onde a chave é o nome e o valor é a lista de itens com aquele nome
        Map <String, List <Item>> groupedItems = inventory.stream().collect(Collectors.groupingBy(Item::getItemName));

        // Iterar sobre os grupos para mostrar
        int index = 1;
        for (Map.Entry <String, List <Item>> entry : groupedItems.entrySet()) {
            String nome = entry.getKey();
            List <Item> listaDeItens = entry.getValue();

            int quantidade = listaDeItens.size();
            // Pegamos o preço do primeiro item da lista
            int preco = listaDeItens.get(0).getItemPrice();

            System.out.println(index + ". " + nome + " (x" + quantidade + ") - Valor: " + preco);
            index++;
        }
    }

    // Retorna a lista de missões
    public List <Quest> getQuestLog() {
        return this.questLog;
    }

    // Adiciona uma nova missão ao diário
    public void addQuest(Quest quest) {
        this.questLog.add(quest);
        System.out.println("\nNOVA MISSÃO ACEITA: " + quest.getName());
    }

    // Método para checar o progresso de todas as missões ativas de uma vez
    public void updateQuests() {
        for (Quest quest : this.questLog) {
            if (!quest.getIsTurnedIn()) {   // Só checa se não tiver sido entregue
                quest.checkProgress(this);
            }
        }
    }

    // Método para "avisar" as missões de caça que um inimigo morreu
    public void countKills(String enemyName) {
        for (Quest quest : this.questLog) {
            // Verifica se a missão é uma Hunt Quest
            if (quest instanceof HuntQuest) {
                // Transforma a quest genérica em uma Hunt Quest para poder usar o registerKill
                ((HuntQuest) quest).registerKill(enemyName);
            }
        }
    }
}