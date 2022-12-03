package org.example;

public interface Game {
    void play() throws InterruptedException;

    void display(Cell[][] board);

    int getScore(Cell[][] board, char player);
}
