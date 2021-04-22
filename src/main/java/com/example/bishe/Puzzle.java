package com.example.bishe;


import java.util.ArrayList;
import java.util.Arrays;
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
        initBoard = new int[m][n];

        init();
        initMove(m * n * 7);
        for (int i = 0; i < m; i++) {
            System.arraycopy(board[i], 0, initBoard[i], 0, this.n);
        }
    }

    public Puzzle() {
        this.m = 4;
        this.n = 4;
        this.curM = this.m;
        this.curN = this.n;
        this.initPath = new ArrayList<>();
        this.restorePath = new ArrayList<>();
        board = new int[][]{
                {1, 5, 2, 3},
                {4, 6, 10, 7},
                {13, 12, 11, 15},
                {8, 14, 9, 0}
        };
        indexArr = new int[m * n][2];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                indexArr[board[i][j]] = new int[]{i, j};
            }
        }
        zeroRow = 3;
        zeroCol = 3;

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
            path.add(0); // 空格往上移
        } else if (i == zeroRow + 1) {
            path.add(1); // 空格往下移
        } else if (j == zeroCol - 1) {
            path.add(2); // 空格往左移
        } else {
            path.add(3); // 空格往右移
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
        print();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Puzzle puzzle = (Puzzle) o;
        return Arrays.equals(board, puzzle.board);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(board);
    }

    @Override
    protected Puzzle clone() {
        Puzzle puzzle = new Puzzle();
        puzzle.m = this.m;
        puzzle.n = this.n;
        puzzle.curM = this.curM;
        puzzle.curN = this.curN;

        puzzle.board = new int[m][n];
        puzzle.indexArr = new int[m * n][2];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                puzzle.board[i][j] = this.board[i][j];
            }
        }
        for (int i = 0; i < m * n; i++) {
            puzzle.indexArr[i][0] = this.indexArr[i][0];
            puzzle.indexArr[i][1] = this.indexArr[i][1];
        }
        puzzle.zeroRow = this.zeroRow;
        puzzle.zeroCol = this.zeroCol;
        puzzle.restorePath = new ArrayList<>(this.restorePath);
        return puzzle;
    }


}
