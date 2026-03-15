package com.javarpg.model.quests;
import com.javarpg.model.entities.Character;

// Classe mãe Quest, uma base para as quests específicas que o jogador pode encontrar e resolver
public abstract class Quest {
    protected String name, description;
    protected int xpReward, goldReward;
    protected boolean isComplete, isTurnedIn;

    // Métodos getters e setters padrão
    public String getName() {return this.name;}
    public void setName(String name) {this.name = name;}

    public String getDescription() {return this.description;}
    public void setDescription(String description) {this.description = description;}

    public int getXpReward() {return this.xpReward;}
    public void setXpReward(int xpReward) {this.xpReward = xpReward;}

    public int getGoldReward() {return this.goldReward;}
    public void setGoldReward(int goldReward) {this.goldReward = goldReward;}

    public boolean getIsComplete() {return this.isComplete;}
    public void setIsComplete(boolean isComplete) {this.isComplete = isComplete;}

    public boolean getIsTurnedIn() {return this.isTurnedIn;}
    public void setIsTurnedIn(boolean isTurnedIn) {this.isTurnedIn = isTurnedIn;}

    // Método construtor
    public Quest(String name, String description, int xpReward, int goldReward) {
        this.setName(name);
        this.setDescription(description);
        this.setXpReward(xpReward);
        this.setGoldReward(goldReward);

        this.setIsComplete(false);
        this.setIsTurnedIn(false);
    }

    // Tag visual para ajudar a visualização nos menus
    public String getStatusTag() {
        if (this.getIsTurnedIn()) return "[Concluída]";
        if (this.getIsComplete()) return "[Pronta para entrega]";
        return "[Em progresso...]";
    }

    // Método abstrato: classes filhas vão ditar como a missão é completada
    public abstract void checkProgress(Character player);

    // Método abstrato: imprime objetivo na tela (Ex: "Matar Goblins: 2/5")
    public abstract String getObjectiveString();
}
