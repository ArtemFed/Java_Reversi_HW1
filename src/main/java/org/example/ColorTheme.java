package org.example;

interface ColorTheme {
    // Различные цвета для красивого вывода в консоль
    String ANSI_RESET = "\u001B[0m";
    String ANSI_BLACK = "\u001B[30m";
    String ANSI_RED = "\u001B[31m";
    String ANSI_GREEN = "\u001B[32m";
    String ANSI_YELLOW = "\u001B[33m";
    String ANSI_BLUE = "\u001B[34m";
    String ANSI_PURPLE = "\u001B[35m";
    String ANSI_CYAN = "\u001B[36m";

    char SYMBOL_1 = '@';
    char SYMBOL_2 = '$';
    char SYMBOL_3 = '?';
}