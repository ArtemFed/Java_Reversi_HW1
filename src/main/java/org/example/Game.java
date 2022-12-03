package org.example;

public interface Game {
    /**
     * Запускает игру
     * @throws InterruptedException задежрка, когда бот думает над ходом
     */
    void play() throws InterruptedException;

    /**
     * Отобразить текущее игровое поле
     */
    void display(Cell[][] board);

    /**
     * Поситать счёт для заданного символа на Игровом поле
     * @param board  Игровое поле
     * @param player Символ игрока
     * @return Счёт игрока
     */
    int getScore(Cell[][] board, char player);
}
