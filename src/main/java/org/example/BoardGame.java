package org.example;

import java.util.Stack;

public abstract class BoardGame implements Game {

    /**
     * Размер Игрового поля
     */
    public static final int SIZE = 8;

    protected int gamesCount = 0;

    protected int movesCount = 0;

    /**
     * Игровое поле с фишками
     */
    protected Board board = new Board(SIZE, SIZE);

    protected final Stack<Board> boards = new Stack<>();


    /**
     * Игровое поле с возмодными ходами (из 0 и 1)
     */
    protected final int[][] moves = new int[SIZE][SIZE];

    /**
     * Запускает игру
     */
    public abstract void play();

    /**
     * Поситать счёт для заданного символа на Игровом поле
     *
     * @param board  Игровое поле
     * @param player Символ игрока
     * @return Счёт игрока
     */
    public abstract int getScore(Board board, char player);

    /**
     * Отобразить текущее игровое поле
     */
    public abstract void display(final Board board);
}

