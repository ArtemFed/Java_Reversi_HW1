package org.example;

public class Main {
    public static void main(String[] args) {
        // На всякий случай
        try {
            Reversi myGame = new Reversi();
            myGame.play();
        } catch (InterruptedException e) {
            System.out.println("\nКакая-то беда с потоками! Игра будет перезапущена.");
            main(args);
        } catch (Exception e) {
            System.out.println("\nПроизошла неизвестная ошибка! Игра будет перезапущена.");
            main(args);
        }
    }
}

