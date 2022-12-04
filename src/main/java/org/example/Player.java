package org.example;

public class Player {
    /**
     * Имя
     */
    public String name;

    /**
     * Символ - Фишка
     */
    public char symbol;

    /**
     * Количество побед в серии игр
     */
    public int winCount = 0;

    /**
     * Лучший счёт в серии игр
     */
    public int bestScore = 0;

    /**
     * Default конструктор
     */
    public Player() {
    }

    public Player(String name) {
        this.name = name;
    }

    public Player(String name, char symbol) {
        this.name = name;
        this.symbol = symbol;
    }
}
