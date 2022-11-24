package org.example;

import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class Reversi extends BoardGame implements ColorTheme {

    private String getColorChar(char ch) {
        if (ch == SYMBOL_1) {
            return ANSI_YELLOW + " " + ch + " " + ANSI_RESET;
        } else if (ch == SYMBOL_2) {
            return ANSI_GREEN + " " + ch + " " + ANSI_RESET;
        } else if (ch == SYMBOL_3) {
            return ANSI_CYAN + " " + ch + " " + ANSI_RESET;
        }
        return " " + ch + " ";
    }

    void play() {
        // Выбор цвета игрока
        int answerColor;
        String answerColorStr;

        // Выбор мода игры
        int answerGameMode;
        String answerGameModeStr;

        // Номер текущего игрока % 2 (кто должен ходить)
        int currentPlayer;

        // Для чтения с консоли
        Scanner in = new Scanner(System.in);

        // Работа с интерфейсом
        String answer;

        // Для преждевременного завершения игры
        int invalidMovesCount = 0;

        Player player, opponent;

        // Перезапуск всей игры
        do {
            // Игроки
            player = new Player("Игрок1");
            opponent = new Player("Игрок2");

            // Очистка консоли
            System.out.print("\033[H\033[J");

            System.out.println(ANSI_PURPLE + "\n\tREVERSI" + ANSI_RESET);

            System.out.printf("""
                    \nВыберите Фишку для Первого Игрока (В первой игре первыми ходят белые):
                    \tЖёлтый (%c)  -  0 или иначе
                    \tЗелёный(%c)  -  1
                    \tСлучайно   -  2
                    Введите число:\s""", SYMBOL_1, SYMBOL_2);
            answerColorStr = in.next();
            if (Objects.equals(answerColorStr, "2")) {
                Random rnd = new Random();
                answerColorStr = Integer.toString(rnd.nextInt(2));
            }
            if (Objects.equals(answerColorStr, "1")) {
                answerColor = 1;
                player.symbol = SYMBOL_2;
                opponent.symbol = SYMBOL_1;
                System.out.print("\n" + player.name + " будет играть: " + getColorChar(player.symbol) +
                        "\n" + opponent.name + " будет играть: " + getColorChar(opponent.symbol));
            } else {
                answerColor = 0;
                player.symbol = SYMBOL_1;
                opponent.symbol = SYMBOL_2;
                System.out.printf("\n" + player.name + " будет играть: " + getColorChar(player.symbol) +
                        "\n" + opponent.name + " будет играть: " + getColorChar(opponent.symbol));
            }

            System.out.print("""
                    \nВыберите вариант игры:
                    \tЛёгкий компьютер       -  0 или иначе
                    \tПродвинутый компьютер  -  1
                    \tПротив второго игрока  -  2
                    Введите число:\s""");
            answerGameModeStr = in.next();

            if (Objects.equals(answerGameModeStr, "1")) {
                answerGameMode = 1;
            } else if (Objects.equals(answerGameModeStr, "2")) {
                answerGameMode = 2;
            } else {
                answerGameMode = 0;
            }

            // Счётчики
            gamesCount = 0;
            movesCount = 0;

            // Перезапуск режима игры (сессии)
            do {
                // Подготовка к игре
                {
                    System.out.printf(ANSI_PURPLE + "\n\nREVERSI. Игра номер: %d\n" + ANSI_RESET, ++gamesCount);

                    if (player.winCount + opponent.winCount > 0) {
                        System.out.printf(ANSI_PURPLE + "\n\nСЧЁТ: " + player.name + " %d | %d " + opponent.name + "\n"
                                + ANSI_RESET, player.winCount, opponent.winCount);
                    }

                    // В четных играх начинает symbol1 = @, в нечетных играх начинает symbol2 = $
                    currentPlayer = 1 + answerColor + gamesCount % 2;
                    movesCount = 4;

                    // Очищаем поле
                    for (int row = 0; row < SIZE; row++) {
                        for (int col = 0; col < SIZE; col++) {
                            board[row][col] = ' ';
                        }
                    }

                    // Расставляем начальные фишки в центре
                    board[SIZE / 2 - 1][SIZE / 2 - 1] = board[SIZE / 2][SIZE / 2] = currentPlayer % 2 == 0 ? SYMBOL_1 : SYMBOL_2;
                    board[SIZE / 2 - 1][SIZE / 2] = board[SIZE / 2][SIZE / 2 - 1] = currentPlayer % 2 != 0 ? SYMBOL_1 : SYMBOL_2;
                }
                do {
                    System.out.printf(ANSI_PURPLE + "\nИгровое поле. Ход: %d\n" + ANSI_RESET, movesCount - 3);
                    if (currentPlayer++ % 2 == 0) {
                        // Ход игрока
                        int valid_moves_count = validMoves(board, moves, player.symbol);
                        // Выводим валидные шаги
                        displayWithTips();
                        if (valid_moves_count != 0) {
                            // Обнуляем счётчик не валидных ходов подряд
                            invalidMovesCount = 0;
                            playerMove(player.symbol, in);
                        } else if (++invalidMovesCount < 2) {
                            System.out.print("\n" + player.name + " не может сходить, поэтому пропускает");
                        } else {
                            System.out.print("\n!  Никто из Игроков не может сходить, так что игра окончена.\n");
                        }
                    } else {
                        // Ход оппонента
                        int valid_moves_count = validMoves(board, moves, opponent.symbol);
                        if (answerGameMode == 2) {
                            displayWithTips();
                        } else {
                            display(board);
                        }
                        // Ход второго игрока (Компьютера, другого пользователя или шизофреника)
                        if (valid_moves_count != 0) {
                            // Обнуляем счётчик не валидных ходов подряд
                            invalidMovesCount = 0;
                            switch (answerGameMode) {
                                case 1 -> computerMove(opponent.symbol, true);
                                case 2 -> playerMove(opponent.symbol, in);
                                default -> computerMove(opponent.symbol, false);
                            }
                            if (answerGameMode != 2) {
                                movesCount++;
                            }
                        } else {
                            if (++invalidMovesCount < 2) {
                                System.out.print("\n" + opponent.name + " не может сходить, поэтому пропускает\n");
                            } else {
                                System.out.print("\n!  Никто из Игроков не может сходить, так что игра окончена.\n");
                            }
                        }
                    }
                } while (movesCount < SIZE * SIZE && invalidMovesCount < 2);

                /* Game over */
                System.out.print(ANSI_PURPLE + "\nФинальная доска:\n" + ANSI_RESET);
                display(board);

                int comp_score = 0;
                int user_score = 0;

                for (int row = 0; row < SIZE; row++)
                    for (int col = 0; col < SIZE; col++) {
                        comp_score += board[row][col] == opponent.symbol ? 1 : 0;
                        user_score += board[row][col] == player.symbol ? 1 : 0;
                    }
                System.out.printf("Окончательный счет таков:" +
                        ANSI_YELLOW + "\n" + player.name + ANSI_RESET + ":  %d" +
                        ANSI_GREEN + "\n" + opponent.name + ANSI_RESET + ":  %d\n", user_score, comp_score);

                if (user_score > comp_score) {
                    ++player.winCount;
                    System.out.print("Победил" + ANSI_YELLOW + "\n" + player.name + ANSI_RESET + "!!! Мои поздравления!");
                } else if (user_score < comp_score) {
                    ++opponent.winCount;
                    System.out.print("Победил" + ANSI_GREEN + "\n" + opponent.name + ANSI_RESET + "!!! Мои поздравления!");
                } else {
                    ++player.winCount;
                    ++opponent.winCount;
                    System.out.print("Ничья!");
                }
                System.out.printf("\n\nСчёт между игроками: %d : %d", player.winCount, opponent.winCount);

                if (user_score > player.bestScore) {
                    player.bestScore = user_score;
                }
                if (comp_score > opponent.bestScore) {
                    opponent.bestScore = comp_score;
                }
                System.out.printf("\n\nЛучший счёт каждого игрока в серии игр:" +
                        "\n\t" + ANSI_YELLOW + player.name + ANSI_RESET + ":  %d" +
                        "\n\t" + ANSI_GREEN + player.name + ANSI_RESET + ":  %d", player.bestScore, opponent.bestScore);

                System.out.print("\n\nХочешь поиграть снова с тем же соперником (очерёдность хода поменяется)? (y/n): ");
                answer = in.next();

            } while (Objects.equals(answer, "y") || Objects.equals(answer, "yes"));

            System.out.print("\nХочешь поиграть снова (выбрать другого соперника)? (y/n): ");
            answer = in.next();

        } while (Objects.equals(answer, "y") || Objects.equals(answer, "yes"));

        // Good luck have fun
        System.out.print("\nGLHF! Пока!\n");

        in.close();
    }

    /**
     * Отобразить подсказки для живого игрока
     */
    void displayWithTips() {
        StringBuilder stringBuilder = new StringBuilder("Возможные ходы: ");
        for (int row = 0; row < SIZE; ++row) {
            for (int col = 0; col < SIZE; ++col) {
                if (moves[row][col] == 1) {
                    stringBuilder.append("(").append(row + 1).append(" ").append(col + 1).append("); ");
                    board[row][col] = SYMBOL_3;
                }
            }
        }
        display(board);
        System.out.println(stringBuilder);
        for (int row = 0; row < SIZE; ++row) {
            for (int col = 0; col < SIZE; ++col) {
                if (board[row][col] == SYMBOL_3) {
                    board[row][col] = ' ';
                }
            }
        }
    }

    /**
     * Обработать ход пользователя
     */
    void playerMove(char playerSymbol, Scanner in) {
        // Считывание ходов игрока до тех пор, пока не будет введен один из действительных ходов
        while (true) {
            System.out.print("Введите ваш ход для " + getColorChar(playerSymbol) + " (строка столбец): ");
            int x = in.nextInt() - 1;
            int y = in.nextInt() - 1;
            if (x >= 0 && y >= 0 && x < SIZE && y < SIZE && moves[x][y] == 1) {
                makeMove(board, x, y, playerSymbol);
                movesCount++;
                break;
            } else {
                System.out.print("!  Не валидный ход, попробуйте ещё раз.\n");
            }
        }
    }


    /**
     * Найти и выполнить лучший ход для компьютера, опираясь на возможные ходы соперника
     *
     * @param isSmart Умный ход, опираясь на действия игрока или обычный, лучший в данный момент
     * @param player  Символ компьютера
     */
    void computerMove(char player, boolean isSmart) {
        // Индексы лучшего хода
        int bestRow = 0;
        int bestCol = 0;

        // Счёт
        double score = 100;
        char[][] tempBoard = new char[SIZE][SIZE];
        int[][] tempMoves = new int[SIZE][SIZE];

        // Символ игрока
        char opponent = getOpponentSymbol(player);

        if (isSmart) {
            // Проходим все возможные шаги
            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    if (moves[row][col] == 0) {
                        continue;
                    }

                    // Делаем копию Игрового поля
                    for (int i = 0; i < SIZE; i++) {
                        System.arraycopy(board[i], 0, tempBoard[i], 0, SIZE);
                    }

                    makeMove(tempBoard, row, col, player);

                    validMoves(tempBoard, tempMoves, opponent);

                    TupleThree tuple = bestMove(tempBoard, tempMoves, opponent);
                    double new_score = (double) tuple.getFirst();

                    // Ищем наименьшее количество очков, которое получит игрок на своём ходе после нашёго
                    if (new_score < score) {
                        score = new_score;
                        bestRow = row;
                        bestCol = col;
                    }
                }
            }
        } else {
            TupleThree tuple = bestMove(board, moves, player);
            bestRow = (int) tuple.getSecond();
            bestCol = (int) tuple.getThird();
        }

        // Делаем Лучший Шаг
        makeMove(board, bestRow, bestCol, player);
    }


    /**
     * Получить символ оппонетна
     *
     * @param player Символ игрока
     * @return Символ оппонетнта
     */
    private static char getOpponentSymbol(char player) {
        return (player == SYMBOL_1) ? SYMBOL_2 : SYMBOL_1;
    }


    /**
     * Считает количество валидных/возможных ходов + составляет "карту" этих шагов из 0 и 1
     *
     * @param player Символ игрока на доске
     * @return Количество возможных ходов
     */
    int validMoves(char[][] board, int[][] moves, char player) {
        // Переменные-итераторы по строкам и столбцам
        int row, col;

        // Координаты текущей клетки поиска
        int x, y;

        // Количество возможных ходов
        int no_of_moves = 0;

        // Символ оппонента
        char opponent = getOpponentSymbol(player);

        // Чистим поле возможных ходов
        for (row = 0; row < SIZE; row++) {
            for (col = 0; col < SIZE; col++) {
                moves[row][col] = 0;
            }
        }

        // Находим клетки возможных ходов (новая фишка должна ставиться так, чтобы хотя бы одна из фишек противника
        // оказалась замкнутой своими фишками.)
        for (row = 0; row < SIZE; row++) {
            for (col = 0; col < SIZE; col++) {
                if (board[row][col] != ' ') { // Проверка на пустую клетку (Непустые не рассматриваем)
                    continue;
                }

                // Проверяем все клетки вокруг пустой на наличие фишки противника
                for (int row_delta = -1; row_delta <= 1; row_delta++) {
                    for (int col_delta = -1; col_delta <= 1; col_delta++) {
                        // Проверка корректноси индексов
                        if (row + row_delta < 0 || row + row_delta >= SIZE ||
                                col + col_delta < 0 || col + col_delta >= SIZE ||
                                (row_delta == 0 && col_delta == 0)) {
                            continue;
                        }

                        // Проверяем каждую возможную клетку и Ищем квадрат игрока в диагональном направлении
                        if (board[row + row_delta][col + col_delta] == opponent) {
                            // Если мы нашли противника, то двигаемся в диагональном направлении через фишки противника
                            // в поисках фишки игрока
                            x = row + row_delta;
                            y = col + col_delta;

                            x += row_delta;
                            y += col_delta;

                            // Не допускаем выход за край поля и Если мы наткнулись на пустую клетку - останавливаемся
                            while (x >= 0 && x < SIZE && y >= 0 && y < SIZE && board[x][y] != ' ') {
                                // Если мы наткнулись на клетку игрока - +1 возможный ход
                                if (board[x][y] == player) {
                                    // Помечаем валидность хода
                                    moves[row][col] = 1;
                                    no_of_moves++;
                                    break;
                                }

                                // Двигаемся дальше по диагонали
                                x += row_delta;
                                y += col_delta;
                            }
                        }
                    }
                }
            }
        }
        return no_of_moves;
    }


    /**
     * Посчитать лучший счёт для данного поля и вариантов хода
     *
     * @param board  Игровое поле
     * @param moves  Поле валидных шагов
     * @param player Символ игрока
     * @return Tuple Лучший счёт (счёт, строка, столбец)
     */
    TupleThree bestMove(char[][] board, int[][] moves, char player) {
        // Для копирования Игрового поля
        char[][] newBoard = new char[SIZE][SIZE];

        // Лучший счёт
        double score = 0;
        int rowBest = 0;
        int colBest = 0;

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                // Пропускаем не валидные ходы
                if (moves[row][col] == 0) {
                    continue;
                }

                // Копируем поле
                for (int i = 0; i < SIZE; i++) {
                    System.arraycopy(board[i], 0, newBoard[i], 0, SIZE);
                }

                double new_score = makeMove(newBoard, row, col, player);

                if (new_score > score) {
                    score = new_score;
                    rowBest = row;
                    colBest = col;
                }
            }
        }
        return new TupleThree(score, rowBest, colBest);
    }


    int getScorePoint(int row, int col) {
        if (row == 0 || row == (SIZE - 1) || col == 0 || col == (SIZE - 1)) {
            return 2;
        }
        return 1;
    }

    /**
     * Сделать шаг
     *
     * @param board  Игровое поле
     * @param row    Индекс строки
     * @param col    Индекс столбца
     * @param player Символ игрока
     */
    double makeMove(char[][] board, int row, int col, char player) {
        // Счёт с хода
        double score = 0;

        // Символ оппонента
        char opponent = getOpponentSymbol(player);

        // Ставим фишку игрока
        board[row][col] = player;

        if (row == 0 || row == (SIZE - 1) || col == 0 || col == (SIZE - 1)) {
            score = 0.4;
        }
        if (row + col == 0 || row + col == 2 * (SIZE - 1) || (row == 0 && col == (SIZE - 1)) || (row == (SIZE - 1) && col == 0)) {
            score = 0.8;
        }
        score += getScorePoint(row, col);

        // Проверяем все клетки вокруг на наличие фишки противника
        for (int rowDelta = -1; rowDelta <= 1; rowDelta++) {
            for (int colDelta = -1; colDelta <= 1; colDelta++) {
                // Проверка индексов
                if (row + rowDelta < 0 || row + rowDelta >= SIZE ||
                        col + colDelta < 0 || col + colDelta >= SIZE ||
                        (rowDelta == 0 && colDelta == 0)) {
                    continue;
                }

                // Если мы найдем фишку противника, то ищем в том же направлении фишку игрока
                if (board[row + rowDelta][col + colDelta] == opponent) {
                    int x = row + rowDelta;
                    int y = col + colDelta;

                    x += rowDelta;
                    y += colDelta;

                    // Не допускаем выход за край поля и Если мы наткнулись на пустую клетку - останавливаемся
                    while (x >= 0 && x < SIZE && y >= 0 && y < SIZE && board[x][y] != ' ') {
                        // Если мы наткнулись на клетку игрока - то заменяем все фишки противника на пути на свои
                        if (board[x][y] == player) {
                            while (board[x -= rowDelta][y -= colDelta] == opponent) {
                                score += getScorePoint(x, y);
                                board[x][y] = player;
                            }
                            break;
                        }

                        // Двигаемся дальше по диагонали
                        x += rowDelta;
                        y += colDelta;
                    }
                }
            }
        }
        return score;
    }

    /**
     * Поситать счёт (количество) для заданного символа на Игровом поле
     *
     * @param board  Игровое поле
     * @param player Символ игрока на поле
     * @return Счёт игрока
     */
    @Override
    public int getScore(char[][] board, char player) {
        // Символ оппонента
        char opponent = getOpponentSymbol(player);

        // Суммарный счёт Игрока
        int score = 0;

        for (int row = 0; row < SIZE; row++)
            for (int col = 0; col < SIZE; col++) {
                score -= board[row][col] == opponent ? 1 : 0;
                score += board[row][col] == player ? 1 : 0;
            }
        return score;
    }

    /**
     * Отобразить текущее игровое поле
     */
    @Override
    public void display(char[][] board) {
        {
            // Переменные-итераторы по строкам и столбцам
            int row, col;

            StringBuilder stringBuilder = new StringBuilder("\n ");

            // Вывод шапки таблицы
            for (col = 0; col < SIZE; col++) {
                stringBuilder.append(String.format("   %d", col + 1));
            }
            stringBuilder.append("\n");

            // Вывод всего поля (таблицы)
            for (row = 0; row < SIZE; row++) {
                stringBuilder.append("  +");
                for (col = 0; col < SIZE; col++) {
                    stringBuilder.append("---+");
                }
                stringBuilder.append(String.format("\n%2d|", row + 1));

                for (col = 0; col < SIZE; col++) {
                    stringBuilder.append(String.format(getColorChar(board[row][col]) + "|"));
                }
                stringBuilder.append("\n");
            }

            // Вывод низа таблицы
            stringBuilder.append("  +");
            for (col = 0; col < SIZE; col++) {
                stringBuilder.append("---+");
            }
            stringBuilder.append("\n");

            System.out.print(stringBuilder);
        }
    }
}
