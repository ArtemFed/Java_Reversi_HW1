package org.example;

public class Player {
    public String name;
    public int winCount = 0;
    public int bestScore = 0;
    public char symbol;

    public Player(char symbol, String name) {
        this.symbol = symbol;
        this.name = name;
    }
}
