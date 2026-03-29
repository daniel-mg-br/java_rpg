
package com.javarpg.model.quests;
import com.javarpg.model.entities.Character;
import com.javarpg.model.items.Item;

// Classe filha FetchQuest, na qual o jogador precisa encontrar um (ou mais) itens específico(s)
public class FetchQuest extends Quest {
    // Atributos
    private String targetItemName;
    private int requiredAmount;

    // Métodos getter e setter padrão
    public String getTargetItemName() {return this.targetItemName;}
    public void setTargetItemName(String targetItemName) {this.targetItemName = targetItemName;}

    public int getRequiredAmount() {return this.requiredAmount;}
    public void setRequireAmount(int requireAmount) {this.requiredAmount = requireAmount;}

    // Método construtor
    public FetchQuest(String name, String description, int xpReward, int goldReward, String targetItemName, int requiredAmount) {
        super(name, description, xpReward, goldReward);
        this.setTargetItemName(targetItemName);
        this.setRequireAmount(requiredAmount);
    }

    // Método para checar o progesso com os items
    @Override public void checkProgress(Character player) {
        if (this.isTurnedIn) return;    // Se já entregou, ignora
        int currentAmount = 0;

        // Percorre a mochila contando quantos items batem com o nome, e incrementa a quantidade dos itens certos
        for (Item item : player.getInventory()) {
            if (item.getItemName().equalsIgnoreCase(this.getTargetItemName())) {
                currentAmount++;
            }
        }

        // Guarda o estado anterior para saber se acabou de completar ou se perdeu o progresso
        boolean completed = this.getIsComplete();

        // Atualiza o status
        this.setIsComplete(currentAmount >= this.getRequiredAmount());

        // Avisa o jogador 
        if (this.getIsComplete() && !completed) {
            System.out.println("\nOBJETIVO CONCLUÍDO: " + this.getName() + "! Você reuniu todos os items, volte à cidade para entregar!");
        }
        else {
            System.out.println("\nAtenção! Você se desfez de itens necessários para a missão: " + this.getName());
        }
    }

    // Método sobrescrito para retornar o objetivo da missão
    @Override public String getObjectiveString() {
        return "Reunir " + this.getRequiredAmount() + "x " + this.getTargetItemName();
    }
}
