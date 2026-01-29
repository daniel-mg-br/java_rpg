package java_rpg.main_characters;

public class Teste {
    public static void main(String[] args) {
        Warrior w1 = new Warrior("Jay");
        w1.getStatus();
        w1.levelUp();
        w1.getStatus();

        Mage m1 = new Mage("Antedeguemon");
        m1.getStatus();
        m1.levelUp();
        m1.getStatus();
    }
}
