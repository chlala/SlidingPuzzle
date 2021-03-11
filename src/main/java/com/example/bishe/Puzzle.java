package com.example.bishe;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Puzzle {
    int m;
    int n;
    int curM;
    int curN;
    int[][] board;
    int[][] initBoard;
    int zeroRow;
    int zeroCol;
    List<Integer> restorePath;
    List<Integer> initPath;
    private int[][] indexArr;

    public int[][] directions = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1}
    };

    public Puzzle(int m, int n) {
        this.m = m;
        this.n = n;
        this.curM = m;
        this.curN = n;
        this.initPath = new ArrayList<>();
        this.restorePath = new ArrayList<>();
        board = new int[m][n];
        indexArr = new int[m * n][2];

        init();
        initMove(m * n * 7);
    }

    public Puzzle() {
        this.m = 2;
        this.n = 2;
        this.curM = 2;
        this.curN = 2;
        this.initPath = new ArrayList<>();
        this.restorePath = new ArrayList<>();
        board = new int[][]{
                {1, 3},
                {0, 2},
        };
        indexArr = new int[m * n][2];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                indexArr[board[i][j]] = new int[]{i, j};
            }
        }
        zeroRow = 1;
        zeroCol = 0;

    }


    private void init() {
        int s = 1;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                indexArr[s] = new int[]{i, j};
                if (i != 0 || j != 0) {
                    board[i][j] = s++;
                } else {
                    zeroRow = i;
                    zeroCol = j;
                }
            }
        }
    }

    private void initMove(int times) {
        int tempRow;
        int tempCol;
        Random random = new Random();
        for (int i = 1; i < times; i++) {
            int index = random.nextInt(4);
            tempRow = zeroRow + directions[index][0];
            tempCol = zeroCol + directions[index][1];
            if (tempRow >= 0 && tempRow < m && tempCol >= 0 && tempCol < n) {
                initSwap(tempRow, tempCol);
            }
        }
    }


    public int[] getIndex(int num) {
        return indexArr[num];
    }

    public void setIndex(int num, int row, int col) {
        indexArr[num][0] = row;
        indexArr[num][1] = col;
    }


    public void swapZero(List<Integer> path, int i, int j) {
        if (i == zeroRow - 1) {
            path.add(1);
        } else if (i == zeroRow + 1) {
            path.add(2);
        } else if (j == zeroCol - 1) {
            path.add(3);
        } else {
            path.add(4);
        }
        int temp = board[i][j];
        setIndex(temp, zeroRow, zeroCol);
        board[i][j] = board[zeroRow][zeroCol];
        board[zeroRow][zeroCol] = temp;
        zeroRow = i;
        zeroCol = j;
    }


    private void initSwap(int i, int j) {
        swapZero(initPath, i, j);
    }


    public void restoreSwap(int i, int j) {
        swapZero(restorePath, i, j);
//        print();
    }


    public void print() {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == 0) {
                    System.out.print("\t");
                } else {
                    System.out.print(board[i][j] + "\t");
                }
            }
            System.out.println();
        }
        System.out.println("--------------------------");
    }

}
