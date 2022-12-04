package org.example;

public class Board {
    /**
     * Символ Пользователя (player)
     */
    public static final char SYMBOL_1 = '@';

    /**
     * Символ Компьютера или второго игрока (comp)
     */
    public static final char SYMBOL_2 = '$';

    /**
     * Символ обозначения возможных ходов на поле
     */
    public static final char SYMBOL_3 = '?';

    /**
     * Игровое поле с фишками
     */
    public char[][] board;

    private final int sizeX;

    private final int sizeY;

    /**
     * Конструктор копирования
     *
     * @param sizeX Размер X игрового поля
     * @param sizeY Размер Y игрового поля
     */
    public Board(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        board = new char[sizeX][sizeY];
    }

    /**
     * Конструктор копирования
     *
     * @param boardNew Игровое поле для копирования
     * @param sizeX Размер X игрового поля
     * @param sizeY Размер Y игрового поля
     */
    public Board(char[][] boardNew, int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        board = new char[sizeX][sizeX];
        for (int row = 0; row < sizeX; row++) {
            System.arraycopy(boardNew[row], 0, board[row], 0, sizeY);
        }
    }


    /**
     * Отобразить текущее игровое поле
     */
    public void display() {
        StringBuilder stringBuilder = new StringBuilder("\n ");
        // Вывод шапки таблицы
        for (int col = 0; col < sizeY; col++) {
            stringBuilder.append(String.format("   %d", col + 1));
        }
        stringBuilder.append("\n");
        // Вывод всего поля (таблицы)
        for (int row = 0; row < sizeX; row++) {
            stringBuilder.append("  +").append("---+".repeat(sizeY)).append(String.format("\n%2d|", row + 1));
            for (int col = 0; col < sizeY; col++) {
                stringBuilder.append(String.format(Reversi.getColorChar(board[row][col]) + "|"));
            }
            stringBuilder.append("\n");
        }
        // Вывод низа таблицы
        stringBuilder.append("  +").append("---+".repeat(sizeY)).append("\n");
        System.out.print(stringBuilder);
    }
}
