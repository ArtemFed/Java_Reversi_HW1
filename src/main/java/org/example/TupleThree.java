package org.example;

public record TupleThree(Object first, Object second, Object third) {

    @Override
    public String toString() {
        return "Tuple(first=" + first + ", second=" + second + ", third=" + third + ")";
    }
}

