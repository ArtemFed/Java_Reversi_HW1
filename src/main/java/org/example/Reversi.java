package org.example;

import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public final class Reversi extends BoardGame {

    /**
     * Список имён для случайной генерации имени компьютера (или второго игрока)
     */
    private static final String[] COMPUTER_NAMES = {"Компьютер", "Бип-Буп", "MyComputer", "Windows XP", "Windows 98",};

    /**
     * Список имён для случайной генерации имени пользователя
     */
    private static final String[] PLAYER_NAMES = {"Игрок", "Пользователь", "Кожаный мешок", "Личность",};


    /**
     * Расскрасить символ в зависимости от значения
     * @param ch Символ
     * @return Расскрашенный символ
     */
    private String getColorChar(char ch) {
        if (ch == Cell.SYMBOL_1) {
            return Env.ANSI_YELLOW + " " + ch + " " + Env.ANSI_RESET;
        } else if (ch == Cell.SYMBOL_2) {
            return Env.ANSI_GREEN + " " + ch + " " + Env.ANSI_RESET;
        } else if (ch == Cell.SYMBOL_3) {
            return Env.ANSI_CYAN + " " + ch + " " + Env.ANSI_RESET;
        }
        return " " + ch + " ";
    }

    /**
     * Расскрасить строку в зависимости от значения полученного символа
     * @param line Строка
     * @param ch   Символ
     * @return Расскрашенная строка
     */
    private String getColorString(String line, char ch) {
        if (ch == Cell.SYMBOL_1) {
            return Env.ANSI_YELLOW + line + Env.ANSI_RESET;
        } else if (ch == Cell.SYMBOL_2) {
            return Env.ANSI_GREEN + line + Env.ANSI_RESET;
        } else if (ch == Cell.SYMBOL_3) {
            return Env.ANSI_CYAN + line + Env.ANSI_RESET;
        }
        return " " + ch + " ";
    }

    /**
     * Запускает игру
     * @throws InterruptedException задежрка, когда бот думает над ходом
     */
    public void play() throws InterruptedException {
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
        Random rnd = new Random();

        // Перезапуск всей игры
        do {
            // Очистка консоли
            System.out.print("\033[H\033[J");

            System.out.println(Env.ANSI_PURPLE + "\n\tREVERSI" + Env.ANSI_RESET);

            // Игроки
            player = new Player();
            opponent = new Player();

            System.out.print("" +
                    "\nВыберите Фишку для Первого Игрока (В первой игре вначале ходят жёлтые):" +
                    "\n\tЖёлтый (" + getColorChar(Cell.SYMBOL_1) + ")  -  1 или иначе" +
                    "\n\tЗелёный(" + getColorChar(Cell.SYMBOL_2) + ")  -  2" +
                    "\n\tСлучайно      -  3" +
                    "\nВведите число:\s");
            answerColorStr = in.next();

            if (Objects.equals(answerColorStr, "3")) {
                answerColorStr = Integer.toString(rnd.nextInt(2));
            }
            if (Objects.equals(answerColorStr, "2")) {
                answerColor = 0;
                player.symbol = Cell.SYMBOL_2;
                opponent.symbol = Cell.SYMBOL_1;
            } else {
                answerColor = 1;
                player.symbol = Cell.SYMBOL_1;
                opponent.symbol = Cell.SYMBOL_2;
            }

            player.name = PLAYER_NAMES[rnd.nextInt(PLAYER_NAMES.length)];
            opponent.name = COMPUTER_NAMES[rnd.nextInt(COMPUTER_NAMES.length)];
            System.out.println("\n" +
                    "\nНик Первого игрока: " + getColorString(player.name, player.symbol) +
                    "\nНик Второго игрока: " + getColorString(opponent.name, opponent.symbol) +
                    "\n\n" +
                    getColorString(player.name, player.symbol) + " будет играть: " +
                    getColorChar(player.symbol) + "\n" + getColorString(opponent.name, opponent.symbol) +
                    " будет играть: " + getColorChar(opponent.symbol));

            System.out.print("""
                    \nВыберите вариант игры:
                    \tЛёгкий компьютер       -  1 или иначе
                    \tПродвинутый компьютер  -  2
                    \tПротив второго игрока  -  3
                    Введите число:\s""");
            answerGameModeStr = in.next();

            if (Objects.equals(answerGameModeStr, "2")) {
                answerGameMode = 2;
            } else if (Objects.equals(answerGameModeStr, "3")) {
                answerGameMode = 3;
            } else {
                answerGameMode = 1;
            }

            // Счётчики
            gamesCount = 0;
            movesCount = 0;

            // Перезапуск режима игры (сессии)
            do {
                // Подготовка к игре
                {
                    System.out.printf(Env.ANSI_PURPLE + "\n\nREVERSI. Игра номер: %d\n" + Env.ANSI_RESET, ++gamesCount);

                    if (player.winCount + opponent.winCount > 0) {
                        System.out.printf(Env.ANSI_PURPLE + "\n\nСЧЁТ: " + getColorString(player.name, player.symbol) +
                                " %d | %d " + getColorString(opponent.name, opponent.symbol) + "\n"
                                + Env.ANSI_RESET, player.winCount, opponent.winCount);
                    }

                    // В четных играх начинает symbol1 = @, в нечетных играх начинает symbol2 = $
                    currentPlayer = answerColor + gamesCount % 2;
                    movesCount = 4;

                    // Очищаем поле
                    for (int row = 0; row < SIZE; row++) {
                        for (int col = 0; col < SIZE; col++) {
                            board[row][col].key = ' ';
                        }
                    }

                    // Расставляем начальные фишки в центре
                    board[SIZE / 2 - 1][SIZE / 2 - 1].key = board[SIZE / 2][SIZE / 2].key = player.symbol;
                    board[SIZE / 2 - 1][SIZE / 2].key = board[SIZE / 2][SIZE / 2 - 1].key = opponent.symbol;
                }
                do {
                    System.out.printf(Env.ANSI_PURPLE + "\nИгровое поле. Ход: %d.\n" + "Текущий счёт: " +
                                    getColorString(player.name, player.symbol) + " %d | %d " +
                                    getColorString(opponent.name, opponent.symbol) + "\n"
                                    + Env.ANSI_RESET, movesCount - 3,
                            getScore(board, player.symbol), getScore(board, opponent.symbol));
                    if (currentPlayer++ % 2 == 0) {
                        // Ход игрока
                        int valid_moves_count = validMoves(board, moves, player.symbol);
                        // Выводим валидные шаги
                        if (valid_moves_count != 0) {
                            displayWithTips();
                            // Обнуляем счётчик не валидных ходов подряд
                            invalidMovesCount = 0;
                            playerMove(player, in);
                        } else if (++invalidMovesCount < 2) {
                            System.out.print("\n" +
                                    getColorString(player.name, player.symbol) + " " + getColorChar(player.symbol) +
                                    " не может сходить, поэтому пропускает.");
                        } else {
                            System.out.print("\n!  Никто из Игроков не может сходить, так что игра окончена.\n");
                        }
                    } else {
                        // Ход оппонента
                        int valid_moves_count = validMoves(board, moves, opponent.symbol);
                        if (answerGameMode == 3) {
                            displayWithTips();
                        } else {
                            display(board);
                        }
                        // Ход второго игрока (Компьютера, другого пользователя или шизофреника)
                        if (valid_moves_count != 0) {
                            // Обнуляем счётчик не валидных ходов подряд
                            invalidMovesCount = 0;
                            switch (answerGameMode) {
                                case 2 -> computerMove(opponent, true);
                                case 3 -> playerMove(opponent, in);
                                default -> computerMove(opponent, false);
                            }
                            if (answerGameMode != 3) {
                                movesCount++;
                            }
                        } else {
                            if (++invalidMovesCount < 2) {
                                System.out.print("\n" +
                                        getColorString(opponent.name, opponent.symbol) + " " + getColorChar(opponent.symbol)
                                        + " не может сходить, поэтому пропускает\n");
                            } else {
                                System.out.print("\n!  Никто из Игроков не может сходить, так что игра окончена.\n");
                            }
                        }
                    }
                } while (movesCount < SIZE * SIZE && invalidMovesCount < 2);

                /* Game over */
                System.out.print(Env.ANSI_PURPLE + "\nФинальная доска:\n" + Env.ANSI_RESET);
                display(board);

                int comp_score = 0;
                int user_score = 0;

                for (int row = 0; row < SIZE; row++)
                    for (int col = 0; col < SIZE; col++) {
                        comp_score += board[row][col].key == opponent.symbol ? 1 : 0;
                        user_score += board[row][col].key == player.symbol ? 1 : 0;
                    }
                System.out.printf("Окончательный счет таков:" +
                        "\n\t" + getColorString(player.name, player.symbol) + ":  %d" +
                        "\n\t" + getColorString(opponent.name, opponent.symbol) + ":  %d\n", user_score, comp_score);

                if (user_score > comp_score) {
                    ++player.winCount;
                    System.out.print("Победил " + getColorString(player.name, player.symbol) + "!!! Мои поздравления!");
                } else if (user_score < comp_score) {
                    ++opponent.winCount;
                    System.out.print("Победил " + getColorString(opponent.name, opponent.symbol) + "!!! Мои поздравления!");
                } else {
                    ++player.winCount;
                    ++opponent.winCount;
                    System.out.print("Ничья!");
                }
                System.out.printf("\n\nПобеды игроков: %d : %d", player.winCount, opponent.winCount);

                if (user_score > player.bestScore) {
                    player.bestScore = user_score;
                }
                if (comp_score > opponent.bestScore) {
                    opponent.bestScore = comp_score;
                }
                System.out.printf("\n\nЛучший счёт каждого игрока в серии игр:" +
                        "\n\t" + getColorString(player.name, player.symbol) + ":  %d" +
                        "\n\t" + getColorString(opponent.name, opponent.symbol) + ":  %d", player.bestScore, opponent.bestScore);

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
    private void displayWithTips() {
        StringBuilder stringBuilder = new StringBuilder("Возможные ходы (отмечены знаком:" + getColorChar('?') + "): ");
        for (int row = 0; row < SIZE; ++row) {
            for (int col = 0; col < SIZE; ++col) {
                if (moves[row][col] == 1) {
                    stringBuilder.append("(").append(row + 1).append(" ").append(col + 1).append("); ");
                    board[row][col].key = Cell.SYMBOL_3;
                }
            }
        }
        display(board);
        System.out.println(stringBuilder);
        for (int row = 0; row < SIZE; ++row) {
            for (int col = 0; col < SIZE; ++col) {
                if (board[row][col].key == Cell.SYMBOL_3) {
                    board[row][col].key = ' ';
                }
            }
        }
    }

    /**
     * Обработать ход пользователя
     * @param player Символ игрока
     * @param in Для чтения
     */
    private void playerMove(Player player, Scanner in) {
        int x = 0, y = 0;
        boolean flag = true;
        // Считывание ходов игрока до тех пор, пока не будет введен один из действительных ходов
        while (flag) {
            System.out.print("Введите ход для " + getColorString(player.name, player.symbol) + "-" +
                    getColorChar(player.symbol) + " (строка столбец): ");
            String xStr = in.next();
            String yStr = in.next();
            if (isNotNumeric(xStr) || isNotNumeric(yStr)) {
                System.out.print(Env.ANSI_RED + "!  Некорректные данные, попробуйте ещё раз. (Пример: 2 3)\n" + Env.ANSI_RED);
                continue;
            }
            x = Integer.parseInt(xStr) - 1;
            y = Integer.parseInt(yStr) - 1;
            if (x < 0 || y < 0 || x >= SIZE || y >= SIZE || moves[x][y] != 1) {
                System.out.print(Env.ANSI_RED + "!  Не валидный ход, попробуйте ещё раз. (Пример: 2 3)\n" + Env.ANSI_RED);
            } else {
                flag = false;
            }
        }
        makeMove(board, x, y, player.symbol);
        movesCount++;
    }


    /**
     * Проверка на НЕ число
     * @param str Строка
     * @return Это НЕ число?
     */
    public static boolean isNotNumeric(String str) {
        try {
            Integer.parseInt(str);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }


    /**
     * Найти и выполнить лучший ход для компьютера, опираясь на возможные ходы соперника
     * @param isSmart Умный ход, опираясь на действия игрока или обычный, лучший в данный момент
     * @param player  Символ компьютера
     */
    private void computerMove(Player player, boolean isSmart) throws InterruptedException {
        // Индексы лучшего хода
        int bestRow = -1;
        int bestCol = -1;

        // Счёт
        double score = -100;
        Cell[][] tempBoard = new Cell[SIZE][SIZE];
        int[][] tempMoves = new int[SIZE][SIZE];

        // Символ игрока
        char opponent = getOpponentSymbol(player.symbol);

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
                    double new_score;
                    new_score = makeMove(tempBoard, row, col, player.symbol);

                    validMoves(tempBoard, tempMoves, opponent);

                    TupleThree tuple = bestMove(tempBoard, tempMoves, opponent);
                    new_score -= (double) tuple.first();

                    if (new_score > score) {
                        score = new_score;
                        bestRow = row;
                        bestCol = col;
                    }
                }
            }
        } else {
            TupleThree tuple = bestMove(board, moves, player.symbol);
            bestRow = (int) tuple.second();
            bestCol = (int) tuple.third();
        }

        // Делаем Лучший Шаг
        makeMove(board, bestRow, bestCol, player.symbol);
        System.out.println("Ход " + getColorString(player.name, player.symbol) + "-" + getColorChar(player.symbol) +
                ": (" + (bestRow + 1) + " " + (bestCol + 1) + ")");

        // Ожидание, чтобы лучше смотрелось
        Thread.sleep(1000);
    }


    /**
     * Получить символ оппонетна
     * @param player Символ игрока
     * @return Символ оппонента
     */
    private static char getOpponentSymbol(char player) {
        return (player == Cell.SYMBOL_1) ? Cell.SYMBOL_2 : Cell.SYMBOL_1;
    }


    /**
     * Считает количество валидных/возможных ходов + составляет "карту" этих шагов из 0 и 1
     * @param player Символ игрока на доске
     * @return Количество возможных ходов
     */
    private int validMoves(Cell[][] board, int[][] moves, char player) {
        // Переменные-итераторы по строкам и столбцам
        int row, col;

        // Координаты текущей клетки поиска
        int x, y;

        // Количество возможных ходов
        int numOfMoves = 0;

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
                if (board[row][col].key != ' ') { // Проверка на пустую клетку (Непустые не рассматриваем)
                    continue;
                }

                // Проверяем все клетки вокруг пустой на наличие фишки противника
                for (int rowDelta = -1; rowDelta <= 1; rowDelta++) {
                    for (int colDelta = -1; colDelta <= 1; colDelta++) {
                        // Проверка корректноси индексов
                        if (row + rowDelta < 0 || row + rowDelta >= SIZE ||
                                col + colDelta < 0 || col + colDelta >= SIZE ||
                                (rowDelta == 0 && colDelta == 0)) {
                            continue;
                        }

                        // Проверяем каждую возможную клетку и Ищем квадрат игрока в диагональном направлении
                        if (board[row + rowDelta][col + colDelta].key == opponent) {
                            // Если мы нашли противника, то двигаемся в диагональном направлении через фишки противника
                            // в поисках фишки игрока
                            x = row + rowDelta;
                            y = col + colDelta;

                            x += rowDelta;
                            y += colDelta;

                            // Не допускаем выход за край поля и Если мы наткнулись на пустую клетку - останавливаемся
                            while (x >= 0 && x < SIZE && y >= 0 && y < SIZE && board[x][y].key != ' ') {
                                // Если мы наткнулись на клетку игрока - +1 возможный ход
                                if (board[x][y].key == player) {
                                    // Помечаем валидность хода
                                    moves[row][col] = 1;
                                    numOfMoves++;
                                    break;
                                }

                                // Двигаемся дальше по диагонали
                                x += rowDelta;
                                y += colDelta;
                            }
                        }
                    }
                }
            }
        }
        return numOfMoves;
    }


    /**
     * Посчитать лучший счёт для данного поля и вариантов хода
     * @param board  Игровое поле
     * @param moves  Поле валидных шагов
     * @param player Символ игрока
     * @return Tuple Лучший счёт (счёт, строка, столбец)
     */
    private TupleThree bestMove(Cell[][] board, int[][] moves, char player) {
        // Для копирования Игрового поля
        Cell[][] newBoard = new Cell[SIZE][SIZE];

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


    /**
     * Получить очки в зависимости от расположения клетки
     * @param row Строка
     * @param col Колонка
     * @return Очков с клетки
     */
    private int getScorePoint(int row, int col) {
        if (row == 0 || row == (SIZE - 1) || col == 0 || col == (SIZE - 1)) {
            return 2;
        }
        return 1;
    }

    /**
     * Сделать шаг
     * @param board  Игровое поле
     * @param row    Индекс строки
     * @param col    Индекс столбца
     * @param player Символ игрока
     * @return Счёт с этого шага
     */
    private double makeMove(Cell[][] board, int row, int col, char player) {
        // Счёт с хода
        double score = 0;

        // Символ оппонента
        char opponent = getOpponentSymbol(player);

        // Ставим фишку игрока
        board[row][col].key = player;

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
                if (board[row + rowDelta][col + colDelta].key == opponent) {
                    int x = row + rowDelta;
                    int y = col + colDelta;

                    x += rowDelta;
                    y += colDelta;

                    // Не допускаем выход за край поля и Если мы наткнулись на пустую клетку - останавливаемся
                    while (x >= 0 && x < SIZE && y >= 0 && y < SIZE && board[x][y].key != ' ') {
                        // Если мы наткнулись на клетку игрока - то заменяем все фишки противника на пути на свои
                        if (board[x][y].key == player) {
                            while (board[x -= rowDelta][y -= colDelta].key == opponent) {
                                score += getScorePoint(x, y);
                                board[x][y].key = player;
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
     * @param board  Игровое поле
     * @param player Символ игрока на поле
     * @return Счёт игрока
     */
    @Override
    public int getScore(Cell[][] board, char player) {
        // Суммарный счёт Игрока
        int score = 0;

        for (int row = 0; row < SIZE; row++)
            for (int col = 0; col < SIZE; col++) {
                score += board[row][col].key == player ? 1 : 0;
            }
        return score;
    }

    /**
     * Отобразить текущее игровое поле
     * @param board Поле для вывода
     */
    @Override
    public final void display(Cell[][] board) {
        StringBuilder stringBuilder = new StringBuilder("\n ");
        // Вывод шапки таблицы
        for (int col = 0; col < SIZE; col++) {
            stringBuilder.append(String.format("   %d", col + 1));
        }
        stringBuilder.append("\n");
        // Вывод всего поля (таблицы)
        for (int row = 0; row < SIZE; row++) {
            stringBuilder.append("  +").append("---+".repeat(SIZE)).append(String.format("\n%2d|", row + 1));
            for (int col = 0; col < SIZE; col++) {
                stringBuilder.append(String.format(getColorChar(board[row][col].key) + "|"));
            }
            stringBuilder.append("\n");
        }
        // Вывод низа таблицы
        stringBuilder.append("  +").append("---+".repeat(SIZE)).append("\n");
        System.out.print(stringBuilder);
    }
}
