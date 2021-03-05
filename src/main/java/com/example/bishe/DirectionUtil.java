package com.example.bishe;

import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode
class Grid {
    int row;
    int col;
    public Grid(int row, int col) {
        this.row = row;
        this.col = col;
    }
}

public class DirectionUtil {

    public static int[][] directions = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1}
    };


    public static boolean isValid(Grid cur, int m, int n, Set<Grid> still) {
        if (cur.row >= m || cur.row < 0 || cur.col >= n || cur.col < 0) {
            return false;
        }
        return !still.contains(cur);
    }
}
