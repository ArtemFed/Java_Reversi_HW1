package org.example;

/**
 * Различные цвета для красивого вывода в консоль
 */
public enum EnvTheme {
    // Очистить настройки цвета
    ANSI_RESET("\u001B[0m"),
    // Чёрный
    ANSI_BLACK("\u001B[30m"),
    // Красный
    ANSI_RED("\u001B[31m"),
    // Зелёный
    ANSI_GREEN("\u001B[32m"),
    // Жёлтый
    ANSI_YELLOW("\u001B[33m"),
    // Синий
    ANSI_BLUE("\u001B[34m"),
    // Фиолетовый
    ANSI_PURPLE("\u001B[35m"),
    // Голубой (немного бирюзовый)
    ANSI_CYAN("\u001B[36m");

    private final String color;

    EnvTheme(String envUrl) {
        this.color = envUrl;
    }

    /**
     * Получить код цвета для раскраски текста в консоли
     *
     * @return Код цвета
     */
    public String getColor() {
        return color;
    }
}