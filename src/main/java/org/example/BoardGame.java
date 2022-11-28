package org.example;

public abstract class BoardGame implements Game{
    /**
     * Размер Игрового поля
     */
    protected static final Integer SIZE = 4;

    protected Integer gamesCount = 0;

    protected Integer movesCount = 0;

    /**
     * Игровое поле с фишками
     */
    protected final char[][] board = new char[SIZE][SIZE];


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
    public abstract int getScore(char[][] board, char player);

    /**
     * Отобразить текущее игровое поле
     */
    public abstract void display(char[][] board);
}

