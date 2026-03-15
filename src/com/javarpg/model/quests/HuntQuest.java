package com.javarpg.model.quests;
import com.javarpg.model.entities.Character;

// Classe filha Hunt Quest, onde o jogador caça monstros conforme os requisitos da quest
public class HuntQuest extends Quest {
    private String targetEnemyName;
    private int requireAmount, currentAmount;

    // Métodos getter e setter padrão
    public String getTargetEnemyName() {return this.targetEnemyName;}
    public void setTargetEnemyName(String targetEnemyName) {this.targetEnemyName = targetEnemyName;}

    public int getRequireAmount() {return this.requireAmount;}
    public void setRequireAmount(int requireAmount) {this.requireAmount = requireAmount;}

    public int getCurrentAmount() {return this.currentAmount;}
    public void setCurrentAmount(int currentAmount) {this.currentAmount = currentAmount;}

    // Método Construtor
    public HuntQuest(String name, String description, int xpReward, int goldReward, String targetEnemyName, int requireAmount) {
        super(name, description, xpReward, goldReward);
        
        this.setTargetEnemyName(targetEnemyName);
        this.setRequireAmount(requireAmount);
        this.setCurrentAmount(0);
    }

    // Método que registra quando o jogador mata um monstro
    public void registerKill(String enemyName) {
        // Se a missão já acabou ou já foi entregue, ignora
        if (this.getIsComplete() || this.getIsTurnedIn()) return;

        // Compara os nomes ignorando maiúsculas e minúsculas
        if (this.getTargetEnemyName().equalsIgnoreCase(enemyName)) {
            this.setCurrentAmount(this.getCurrentAmount()+1);
            System.out.println("Progesso da Missão [" + this.getName() + "]: " + this.getCurrentAmount() + "/" + this.getRequireAmount() + " " + this.getTargetEnemyName());

            // Verifica se bateu a meta
            if (this.getCurrentAmount() == this.getRequireAmount()) {
                this.setIsComplete(true);
                System.out.println("OBJETIVO CONCLUÍDO: " + this.getName() + "! Volte à cidade para receber a recompensa!");
            }
        }
    }

    // Checagem principal acontece no Register Kill
    @Override public void checkProgress(Character player) {
        // Trava de segurança para o cumprimento da meta
        if (this.getCurrentAmount() >= this.getRequireAmount()) this.setIsComplete(true);
    }

    // Mostra os requisitos da quest
    @Override public String getObjectiveString() {
        return "Derrotar " + this.getTargetEnemyName() + ": " + this.getCurrentAmount() + "/" + this.getRequireAmount();
    }
}
