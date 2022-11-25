package org.example;

public class Player {
    public String name;
    public int winCount = 0;
    public int bestScore = 0;
    public char symbol;

    public Player() {
    }

    public Player(String name) {
        this.name = name;
    }
}
