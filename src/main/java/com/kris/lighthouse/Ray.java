package com.kris.lighthouse;

import lombok.Getter;

@Getter
public class Ray {
    public static char crossRays = 'X';

    private int side;
    private int xItterate;
    private int yItterate;

    private char ray;
    private char oppositeRay;

    private final Block left;
    private final Block right;
    private final Block diagonal;

    public Ray() {
        left = new Block();
        right = new Block();
        diagonal = new Block();
    }


    //2   1
    //  X
    //3   4

    public void setSide(int side) {
        this.side = side;
        setItterate();
    }

    private void setItterate() {
        xItterate = (side == 1 || side == 4) ? 1 : -1;
        yItterate = (side == 1 || side == 2) ? 1 : -1;
        ray = (side == 1 || side == 3) ? '/' : '\\';
        oppositeRay = (ray == '/') ? '\\' : '/';

        left.setX((side == 1 || side == 3) ? 0 : (side == 4) ? 1 : -1)
                .setY((side == 2 || side == 4) ? 0 : (side == 3) ? 1 : -1);

        right.setX((side == 2 || side == 4) ? 0 : (side == 1) ? 1 : -1)
                .setY((side == 1 || side == 3) ? 0 : (side == 4) ? 1 : -1);

        diagonal.setX((side == 1 || side == 4) ? 1 : -1)
                .setY((side == 3 || side == 4) ? 1 : -1);
    }

    public boolean stop(char left, char right, char diagonal) {
        String diagStopCase = "\\/.O";
        String lrStopCase = "*O";

        return (lrStopCase.contains(String.valueOf(left)) && lrStopCase.contains(String.valueOf(right))) || (diagStopCase.contains(String.valueOf(left)) && diagStopCase.contains(String.valueOf(right)) && diagonal == '*');
    }

    public int reflect(char left, char right, char diagonal) {
        int reflected = 0;

        if(left == '*' && diagonal == '*') {
            reflected = 1;
        }
        else if(right == '*' && diagonal == '*') {
            reflected = 2;
        }

        return reflected;
    }

    public void changeSide(int sideCase) {
        if(sideCase == 1) {
            setSide((side - 1 == 0) ? 4 : (side - 1));
        }
        else if(sideCase == 2) {
            setSide((side + 1 == 5) ? 1 : (side + 1));
        }
    }
}
