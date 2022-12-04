package org.example;

public interface Game {
    /**
     * Запускает игру
     */
    void play();

    /**
     * Поситать счёт для заданного символа на Игровом поле
     * @param board  Игровое поле
     * @param player Символ игрока
     * @return Счёт игрока
     */
    int getScore(Board board, char player);

    /**
     * Отобразить текущее игровое поле
     */
    void display(Board board);
}
