package java_rpg.main_characters;

// Classe abstrata que serve de molde para as outras classes
public abstract class Character {
    // Atributos protegidos para que os filhos possam acessar
    protected String name;
    protected int health, maxHealth;
    protected int atack, defense, agility, intellig;
    // Sistema de nível e XP
    protected int level, exp, nxtLevelXp;

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
    public String getStatus(){
        return this.getName() + " (lvl " + this.getLevel() + ") - Vida: " + this.getHealth() + "/" + this.getMaxHealth() + " | Atq: " + this.getAtack() + " | Def: " + this.getDefense() + " | Agi: " + this.getAgility() + " | Int: " + this.getIntellig();
    }
}