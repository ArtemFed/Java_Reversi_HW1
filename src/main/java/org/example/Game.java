package org.example;

public interface Game {

    void display(Cell[][] board);

    int getScore(Cell[][] board, char player);
}
