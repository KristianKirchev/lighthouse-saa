package com.kris.lighthouse;

public class Block {
    private int x;
    private int y;

    public Block() {}

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public Block setX(int x) {
        this.x = x;
        return this;
    }

    public Block setY(int y) {
        this.y = y;
        return this;
    }
}
