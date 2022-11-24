package org.example;

public final class TupleThree {

    private final Object first;
    private final Object second;
    private final Object third;

    public TupleThree(Object first, Object second, Object third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public Object getFirst() {
        return first;
    }

    public Object getSecond() {
        return second;
    }

    public Object getThird() {
        return third;
    }

    @Override
    public String toString() {
        return "Tuple(first=" + first + ", second=" + second + ", third=" + third + ")";
    }
}
