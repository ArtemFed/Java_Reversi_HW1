package org.example;

public abstract class BoardGame implements Game{
    /**
     * Размер Игрового поля
     */
    protected static final int SIZE = 4;

    protected int gamesCount = 0;

    protected int movesCount = 0;

    /**
     * Игровое поле с фишками
     */
    protected final Cell[][] board = new Cell[SIZE][SIZE];


    /**
     * Игровое поле с возмодными ходами (из 0 и 1)
     */
    protected final int[][] moves = new int[SIZE][SIZE];

    /**
     * Поситать счёт для заданного символа на Игровом поле
     *
     * @param board  Игровое поле
     * @param player Символ игрока
     * @return Счёт игрока
     */
    public abstract int getScore(Cell[][] board, char player);

    /**
     * Отобразить текущее игровое поле
     */
    public abstract void display(Cell[][] board);
}

