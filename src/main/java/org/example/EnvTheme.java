package org.example;

// Пытался заменить на enum, так как это было бы правильнее
// Но сталкнулся с такой проблемой, что цвета перестали читаться и стали просто текстом
// Из-за чего пришлось сделать интерфейс
public interface EnvTheme {
    // Различные цвета для красивого вывода в консоль

    // Очистить настройки цвета
    String ANSI_RESET = "\u001B[0m";
    // Чёрный
    String ANSI_BLACK = "\u001B[30m";
    //Красный
    String ANSI_RED = "\u001B[31m";
    //Зелёный
    String ANSI_GREEN = "\u001B[32m";
    //Жёлтый
    String ANSI_YELLOW = "\u001B[33m";
    // Синий
    String ANSI_BLUE = "\u001B[34m";
    // Фиолетовый
    String ANSI_PURPLE = "\u001B[35m";
    // Голубой (немного бирюзовый)
    String ANSI_CYAN = "\u001B[36m";

    /**
     * Символ Пользователя (player)
     */
    char SYMBOL_1 = '@';

    /**
     * Символ Компьютера или второго игрока (comp)
     */
    char SYMBOL_2 = '$';

    /**
     * Символ обозначения возможных ходов на поле
     */
    char SYMBOL_3 = '?';
}

/* Пробовал написать так:
public enum EnvTheme {
    ANSI_RESET("\u001B[0m"),
    ANSI_BLACK("\u001B[30m"),
    ANSI_RED("\u001B[31m"),
    ANSI_GREEN("\u001B[32m"),
    ANSI_YELLOW("\u001B[33m"),
    ANSI_BLUE("\u001B[34m"),
    ANSI_PURPLE("\u001B[35m"),
    ANSI_CYAN("\u001B[36m");

    private String color;

    Env(String envUrl) {
        this.color = envUrl;
    }

    public String getColor() {
        return color;
    }
} */