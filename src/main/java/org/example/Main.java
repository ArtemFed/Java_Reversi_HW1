package org.example;

public class Main {
    public static void main(String[] args) {
        // На всякий случай
        try {
            Reversi myGame = new Reversi();
            myGame.play();
        } catch (Exception e) {
            System.out.println("\nПроизошла неизвестная ошибка! Игра будет перезапущена.");
            main(args);
        }
    }
}

