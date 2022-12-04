package org.example;

/**
 * Tuple из 3 элементов для возвращения нескольких значений из метода
 *
 * @param first
 * @param second
 * @param third
 */
public record TupleThree(Object first, Object second, Object third) {

    @Override
    public String toString() {
        return "Tuple(first=" + first + ", second=" + second + ", third=" + third + ")";
    }
}

