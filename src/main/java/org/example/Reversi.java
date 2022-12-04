package org.example;

import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public final class Reversi extends BoardGame implements EnvTheme {

    /**
     * Список имён для случайной генерации имени пользователя
     */
    private static final String[] PLAYER_NAMES = {"Игрок", "Пользователь", "Кожаный мешок", "Личность",};

    /**
     * Список имён для случайной генерации имени компьютера (или второго игрока)
     */
    private static final String[] COMPUTER_NAMES = {"Компьютер", "Бип-Буп", "MyComputer", "Windows XP", "Windows 98",};

    private int bestEasyScore;

    private int bestHardScore;

    // Выбор мода игры (Easy = 1; Hard = 2; Duo = 3)
    private int answerGameMode;

    /**
     * Расскрасить символ в зависимости от значения
     *
     * @param ch Символ
     * @return Расскрашенный символ
     */
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


    /**
     * Расскрасить строку в зависимости от значения полученного символа
     *
     * @param line Строка
     * @param ch   Символ
     * @return Расскрашенная строка
     */
    private String getColorString(String line, char ch) {
        if (ch == SYMBOL_1) {
            return ANSI_YELLOW + line + ANSI_RESET;
        } else if (ch == SYMBOL_2) {
            return ANSI_GREEN + line + ANSI_RESET;
        } else if (ch == SYMBOL_3) {
            return ANSI_CYAN + line + ANSI_RESET;
        }
        return " " + ch + " ";
    }


    /**
     * Запускает игру
     */
    public void play() {
        // Для чтения с консоли
        Scanner in = new Scanner(System.in);

        // Работа с интерфейсом
        String answer;

        // Количество пропусков хода подряд (если == 2, то game over) для преждевременного завершения игры
        int invalidMovesCount = 0;

        // Два игрока
        Player player, opponent;

        // Счётчики за сессию
        bestEasyScore = 0;
        bestHardScore = 0;

        // Перезапуск всей игры
        do {
            // Игроки
            player = new Player();
            opponent = new Player();

            // Номер выбранного цвета (Для очерёдности хода)
            int answerColor = prepareSessionOfGames(player, opponent, in);

            // Перезапуск режима игры для серии)
            do {
                // Подготовка к игре
                prepareSeriesOfGames(player, opponent);

                // В четных играх начинает symbol1 = @, в нечетных играх начинает symbol2 = $
                int currentPlayer = answerColor + gamesCount % 2;
                movesCount = 4;

                do {
                    System.out.printf(ANSI_PURPLE + "\nИгровое поле. Ход: %d.\n" + "Текущий счёт: " +
                                    getColorString(player.name, player.symbol) + " %d | %d " +
                                    getColorString(opponent.name, opponent.symbol) + "\n"
                                    + ANSI_RESET, movesCount - 3,
                            getScore(board, player.symbol), getScore(board, opponent.symbol)
                    );
                    // Количество возможных ходов
                    int valid_moves_count;

                    // Запись предыдущего хода
                    for (int row = 0; row < SIZE; row++) {
                        System.arraycopy(board[row], 0, boardPrev[row], 0, SIZE);
                    }

                    if (currentPlayer++ % 2 == 0) {
                        // Ход игрока
                        valid_moves_count = validMoves(board, moves, player.symbol);
                        // Выводим валидные шаги
                        if (valid_moves_count != 0) {
                            displayWithTips();
                            // Обнуляем счётчик не валидных ходов подряд
                            invalidMovesCount = 0;
                            playerMove(player, in);
                        } else if (++invalidMovesCount < 2) {
                            System.out.print("\n" +
                                    getColorString(player.name, player.symbol) + " " + getColorChar(player.symbol) +
                                    " не может сходить, поэтому пропускает."
                            );
                        } else {
                            System.out.print("\n!  Никто из Игроков не может сходить, так что игра окончена.\n");
                        }
                    } else {
                        // Ход оппонента
                        valid_moves_count = validMoves(board, moves, opponent.symbol);
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
                                        + " не может сходить, поэтому пропускает\n"
                                );
                            } else {
                                System.out.print("\n!  Никто из Игроков не может сходить, так что игра окончена.\n");
                            }
                        }
                    }
                } while (movesCount < SIZE * SIZE && invalidMovesCount < 2);

                // Game over
                finishReversi(player, opponent, answerGameMode);

                System.out.print("\n\nХочешь поиграть снова с тем же соперником? (очерёдность хода поменяется) (" +
                        ANSI_GREEN + "y" + ANSI_RESET + "/" + ANSI_RED + "n" + ANSI_RESET + "): "
                );
                answer = in.next();

            } while (Objects.equals(answer.toLowerCase(), "y") || Objects.equals(answer.toLowerCase(), "yes"));

            System.out.println("\nНаилучший результат в сессии таков:" +
                    "\n\tПротив Лёгкого бота: " + ANSI_BLUE + bestEasyScore + ANSI_RESET +
                    "\n\tПротив Продвинутого бота: " + ANSI_BLUE + bestHardScore + ANSI_RESET
            );

            System.out.print("\nХочешь поиграть снова (выбрать другого соперника)? " +
                    ANSI_GREEN + "y" + ANSI_RESET + "/" + ANSI_RED + "n" + ANSI_RESET + "): "
            );
            answer = in.next();

        } while (Objects.equals(answer.toLowerCase(), "y") || Objects.equals(answer.toLowerCase(), "yes"));

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
     *
     * @param player Символ игрока
     * @param in     Для чтения
     */
    private void playerMove(Player player, Scanner in) {
        int x = 0, y = 0;
        boolean flag = true;
        // Считывание ходов игрока до тех пор, пока не будет введен один из действительных ходов
        while (flag) {
            System.out.print("Введите ход для " + getColorString(player.name, player.symbol) + "-" +
                    getColorChar(player.symbol) + " (строка столбец): "
            );
            String xStr = in.next();
            String yStr = in.next();
            if (isNotNumeric(xStr) || isNotNumeric(yStr)) {
                System.out.print(ANSI_RED +
                        "!!! Некорректные данные, попробуйте ещё раз. (Пример: 2 3)\n" + ANSI_RESET);
                continue;
            }
            x = Integer.parseInt(xStr) - 1;
            y = Integer.parseInt(yStr) - 1;
            if (x < 0 || y < 0 || x >= SIZE || y >= SIZE || moves[x][y] != 1) {
                System.out.print(ANSI_RED +
                        "!!! Не валидный ход, попробуйте ещё раз. (Пример: 2 3)\n" + ANSI_RESET);
            } else {
                flag = false;
            }
        }
        makeMove(board, x, y, player.symbol);
        movesCount++;
    }


    /**
     * Проверка на НЕ число
     *
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
     *
     * @param isSmart Умный ход, опираясь на действия игрока или обычный, лучший в данный момент
     * @param player  Символ компьютера
     */
    private void computerMove(Player player, boolean isSmart) {
        // Индексы лучшего хода
        int bestRow = -1;
        int bestCol = -1;

        // Счёт
        double score = -100;
        char[][] tempBoard = new char[SIZE][SIZE];
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
                ": (" + (bestRow + 1) + " " + (bestCol + 1) + ")"
        );
    }


    /**
     * Получить символ оппонетна
     *
     * @param player Символ игрока
     * @return Символ оппонента
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
    private int validMoves(char[][] board, int[][] moves, char player) {
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
                if (board[row][col] != ' ') { // Проверка на пустую клетку (Непустые не рассматриваем)
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
                        if (board[row + rowDelta][col + colDelta] == opponent) {
                            // Если мы нашли противника, то двигаемся в диагональном направлении через фишки противника
                            // в поисках фишки игрока
                            x = row + rowDelta;
                            y = col + colDelta;

                            x += rowDelta;
                            y += colDelta;

                            // Не допускаем выход за край поля и Если мы наткнулись на пустую клетку - останавливаемся
                            while (x >= 0 && x < SIZE && y >= 0 && y < SIZE && board[x][y] != ' ') {
                                // Если мы наткнулись на клетку игрока - +1 возможный ход
                                if (board[x][y] == player) {
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
     *
     * @param board  Игровое поле
     * @param moves  Поле валидных шагов
     * @param player Символ игрока
     * @return Tuple Лучший счёт (счёт, строка, столбец)
     */
    private TupleThree bestMove(char[][] board, int[][] moves, char player) {
        // Для копирования Игрового поля
        char[][] tempBoard = new char[SIZE][SIZE];

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
                    System.arraycopy(board[i], 0, tempBoard[i], 0, SIZE);
                }

                double new_score = makeMove(tempBoard, row, col, player);

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
     *
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
     *
     * @param board  Игровое поле
     * @param row    Индекс строки
     * @param col    Индекс столбца
     * @param player Символ игрока
     * @return Счёт с этого шага
     */
    private double makeMove(char[][] board, int row, int col, char player) {
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
     * Подготовить поле к серии игр
     *
     * @param player   Игрок 1
     * @param opponent Игрок 2
     * @param in Поток чтения
     * @return Номер выбранного цвета (Для очерёдности хода)
     */
    private int prepareSessionOfGames(Player player, Player opponent, Scanner in) {
        Random rnd = new Random();
        int answerColor;

        // Очистка консоли
        System.out.print("\033[H\033[J");

        System.out.println(ANSI_PURPLE + "\n\tREVERSI" + ANSI_RESET +
                "\n\nВыберите Фишку для Первого Игрока (В первой игре вначале ходят жёлтые):" +
                "\n\tЖёлтый (" + getColorChar(SYMBOL_1) + ")  -  " +
                ANSI_BLUE + "Любое значение" + ANSI_RESET +
                "\n\tЗелёный(" + getColorChar(SYMBOL_2) + ")  -  " +
                ANSI_BLUE + "2" + ANSI_RESET +
                "\n\tСлучайно      -  " +
                ANSI_BLUE + "3" + ANSI_RESET +
                "\nВведите число:\s"
        );
        String answerColorStr = in.next();

        if (Objects.equals(answerColorStr, "3")) {
            answerColorStr = Integer.toString(rnd.nextInt(2));
        }
        if (Objects.equals(answerColorStr, "2")) {
            answerColor = 0;
            player.symbol = SYMBOL_2;
            opponent.symbol = SYMBOL_1;
        } else {
            answerColor = 1;
            player.symbol = SYMBOL_1;
            opponent.symbol = SYMBOL_2;
        }

        player.name = PLAYER_NAMES[rnd.nextInt(PLAYER_NAMES.length)];
        opponent.name = COMPUTER_NAMES[rnd.nextInt(COMPUTER_NAMES.length)];
        System.out.println("\n" +
                "\nНик Первого игрока: " + getColorString(player.name, player.symbol) +
                "\nНик Второго игрока: " + getColorString(opponent.name, opponent.symbol) +
                "\n\n" +
                getColorString(player.name, player.symbol) + " будет играть: " +
                getColorChar(player.symbol) + "\n" +
                getColorString(opponent.name, opponent.symbol) + " будет играть: " +
                getColorChar(opponent.symbol) +
                "\n\n\nВыберите вариант игры:" +
                "\n\tЛёгкий компьютер       -  " + ANSI_BLUE + "Любое значение" + ANSI_RESET +
                "\n\tПродвинутый компьютер  -  " + ANSI_BLUE + "2" + ANSI_RESET +
                "\n\tПротив второго игрока  -  " + ANSI_BLUE + "3" + ANSI_RESET +
                "\nВведите число:\s"
        );

        String answerGameModeStr = in.next();
        if (Objects.equals(answerGameModeStr, "2")) {
            // Выбран Hard режим
            answerGameMode = 2;
        } else if (Objects.equals(answerGameModeStr, "3")) {
            // Выбран Duo режим
            answerGameMode = 3;
        } else {
            // Выбран Easy режим
            answerGameMode = 1;
        }
        // Счётчики
        gamesCount = 0;
        movesCount = 0;
        return answerColor;
    }


    /**
     * Подготовить поле к серии игр
     *
     * @param player   Игрок 1
     * @param opponent Игрок 2
     */
    private void prepareSeriesOfGames(Player player, Player opponent) {
        System.out.printf(ANSI_PURPLE + "\n\nREVERSI. Игра номер: %d\n" + ANSI_RESET, ++gamesCount);

        if (player.winCount + opponent.winCount > 0) {
            System.out.printf(ANSI_PURPLE + "\n\nСЧЁТ: " + getColorString(player.name, player.symbol) +
                    " %d | %d " + getColorString(opponent.name, opponent.symbol) + "\n"
                    + ANSI_RESET, player.winCount, opponent.winCount
            );
        }

        // Очищаем поле
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                board[row][col] = ' ';
            }
        }

        // Расставляем начальные фишки в центре
        board[SIZE / 2 - 1][SIZE / 2 - 1] = board[SIZE / 2][SIZE / 2] = player.symbol;
        board[SIZE / 2 - 1][SIZE / 2] = board[SIZE / 2][SIZE / 2 - 1] = opponent.symbol;
    }


    /**
     * Завершить игру (посчитать и вывести счёт за серию игр и за сессию)
     *
     * @param player   Игрок 1
     * @param opponent Игрок 2
     * @param answerGameMode Режим игры
     */
    private void finishReversi(Player player, Player opponent, int answerGameMode) {
        System.out.print(ANSI_PURPLE + "\nФинальная доска:\n" + ANSI_RESET);
        display(board);

        int comp_score = 0;
        int user_score = 0;

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                comp_score += board[row][col] == opponent.symbol ? 1 : 0;
                user_score += board[row][col] == player.symbol ? 1 : 0;
            }
        }

        System.out.printf("Окончательный счет таков:" +
                "\n\t" + getColorString(player.name, player.symbol) + ":  %d" +
                "\n\t" + getColorString(opponent.name, opponent.symbol) + ":  %d\n", user_score, comp_score
        );

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
        System.out.printf("\n\nПобеды игроков в серии игр: %d : %d", player.winCount, opponent.winCount);

        if (user_score > player.bestScore) {
            player.bestScore = user_score;
        }
        if (comp_score > opponent.bestScore) {
            opponent.bestScore = comp_score;
        }

        if (answerGameMode == 1 && user_score > bestEasyScore) {
            bestEasyScore = user_score;
        } else if (answerGameMode == 2 && user_score > bestHardScore) {
            bestHardScore = user_score;
        }

        System.out.printf("\n\nЛучший счёт обоих игроков в серии игр:" +
                "\n\t" + getColorString(player.name, player.symbol) + ":  %d" +
                "\n\t" + getColorString(opponent.name, opponent.symbol) + ":  %d", player.bestScore, opponent.bestScore
        );

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
        // Суммарный счёт Игрока
        int score = 0;

        for (int row = 0; row < SIZE; row++)
            for (int col = 0; col < SIZE; col++) {
                score += board[row][col] == player ? 1 : 0;
            }
        return score;
    }


    /**
     * Отобразить текущее игровое поле
     *
     * @param board Поле для вывода
     */
    @Override
    public void display(char[][] board) {
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
                stringBuilder.append(String.format(getColorChar(board[row][col]) + "|"));
            }
            stringBuilder.append("\n");
        }
        // Вывод низа таблицы
        stringBuilder.append("  +").append("---+".repeat(SIZE)).append("\n");
        System.out.print(stringBuilder);
    }
}
