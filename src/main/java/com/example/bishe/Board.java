package com.example.bishe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board implements Cloneable{
    int m;
    int n;
    int[][] board;
    int zeroRow;
    int zeroCol;
    int step;
    List<Integer> path;
    int curM;
    int curN;

    public Board(int m, int n) {
        this.m = m;
        this.n = n;
        this.curM = m;
        this.curN = n;
        this.path = new ArrayList<>();

        board = new int[m][n];
        init();
        initMove(m * n * 5);
        DirectionUtil.initIndexArr(this);
    }

    private void init() {
        int s = 1;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
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
        int[][] directions = DirectionUtil.directions;
        int tempRow;
        int tempCol;
        Random random = new Random();
        for (int i = 0; i < times; i++) {
            int index = random.nextInt(4);
            tempRow = zeroRow + directions[index][0];
            tempCol = zeroCol + directions[index][1];
            if (tempRow >= 0 && tempRow < m && tempCol >= 0 && tempCol < n) {
                swapZero(tempRow, tempCol);
            }

        }
    }

    public void swapZero(int i, int j) {
        int temp = board[i][j];
        board[i][j] = board[zeroRow][zeroCol];
        board[zeroRow][zeroCol] = temp;
        zeroRow = i;
        zeroCol = j;
        step++;
    }

    /**
     * 1 2 3
     * 0 6 7
     * 5 4 8
     */
    public void moveLeftWhenLeft() {
        swapZero(zeroRow, zeroCol + 1);
    }

    /**
     * 1 2 3
     * 5 6 7
     * 0 4 8
     */
    public void moveLeftWhenLowLeft() {
        swapZero(zeroRow - 1, zeroCol);
        moveLeftWhenLeft();
    }

    /**
     * 0 2 3
     * 1 6 7
     * 5 4 8
     */
    public void moveLeftWhenUpLeft() {
        swapZero(zeroRow + 1, zeroCol);
        moveLeftWhenLeft();
    }


    /**
     * 2 0 3
     * 1 6 7
     * 5 4 8
     */
    public void moveLeftWhenUp() {
        swapZero(zeroRow, zeroCol - 1);
        moveLeftWhenUpLeft();
    }

    /**
     * 2 3 0
     * 1 6 7
     * 5 4 8
     */
    public void moveLeftWhenUpRight() {
        swapZero(zeroRow, zeroCol - 1);
        moveLeftWhenUp();
    }

    /**
     * 2 3 7
     * 1 6 0
     * 5 4 8
     */
    public void moveLeftWhenRight() {
        swapZero(zeroRow - 1, zeroCol);
        moveLeftWhenUpRight();
    }

    /**
     * 2 3 7
     * 1 6 8
     * 5 4 0
     */
    public void moveLeftWhenLowRight() {
        swapZero(zeroRow - 1, zeroCol);
        moveLeftWhenRight();
    }


    /**
     * 1 2 3
     * 5 7 6
     * 4 0 8
     */
    public void moveDownWhenLow() {
        swapZero(zeroRow - 1, zeroCol);
    }

    /**
     * 1 2 3
     * 5 7 6
     * 4 8 0
     */
    public void moveDownWhenLowRight() {
        swapZero(zeroRow, zeroCol - 1);
        moveDownWhenLow();
    }

    /**
     * 1 2 3
     * 5 7 0
     * 4 8 6
     */
    public void moveDownWhenRight() {
        swapZero(zeroRow + 1, zeroCol);
        moveDownWhenLowRight();
    }

    /**
     * 1 2 0
     * 5 7 3
     * 4 8 6
     */
    public void moveDownWhenUpRight() {
        swapZero(zeroRow + 1, zeroCol);
        moveDownWhenRight();
    }

    /**
     * 1 0 2
     * 5 7 3
     * 4 8 6
     */
    public void moveDownWhenUp() {
        swapZero(zeroRow, zeroCol + 1);
        moveDownWhenUpRight();
    }

    /**
     * 0 1 2
     * 5 7 3
     * 4 8 6
     */
    public void moveDownWhenUpLeft() {
        swapZero(zeroRow, zeroCol + 1);
        moveDownWhenUp();
    }

    /**
     * 5 1 2
     * 0 7 3
     * 4 8 6
     */
    public void moveDownWhenLeft() {
        swapZero(zeroRow - 1, zeroCol);
        moveDownWhenUpLeft();
    }

    /**
     * 5 1 2
     * 3 8 0
     * 4 7 6
     */
    public void moveRightWhenRight() {
        swapZero(zeroRow, zeroCol - 1);
    }

    /**
     * 5 1 0
     * 3 8 2
     * 4 7 6
     */
    public void moveRightWhenUpRight() {
        swapZero(zeroRow + 1, zeroCol);
        moveRightWhenRight();
    }

    /**
     * 5 0 1
     * 3 8 2
     * 4 7 6
     */
    public void moveRightWhenUp() {
        swapZero(zeroRow, zeroCol + 1);
        moveRightWhenUpRight();
    }

    /**
     * 0 5 1
     * 3 8 2
     * 4 7 6
     */
    public void moveRightWhenUpLeft() {
        swapZero(zeroRow, zeroCol + 1);
        moveRightWhenUp();
    }

    /**
     * 3 5 1
     * 0 8 2
     * 4 7 6
     */
    public void moveRightWhenLeft() {
        swapZero(zeroRow - 1, zeroCol);
        moveRightWhenUpLeft();
    }


    public void print() {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < board[0].length; j++) {
                System.out.print(board[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println("---------------");
    }

    /**
     * 空格在考虑位置的方向
     *
     * @param curRow
     * @param curCol
     * @return: 1:上 2：下 3：左 4：右 5：左上 6：左下 7：右上 8：右下 0：不相邻
     */
    public int handleNeighbor(int curRow, int curCol) {
        if (zeroRow == curRow - 1 && zeroCol == curCol) {
            return 1;
        }
        if (zeroRow == curRow + 1 && zeroCol == curCol) {
            return 2;
        }
        if (zeroRow == curRow && zeroCol == curCol - 1) {
            return 3;
        }
        if (zeroRow == curRow && zeroCol == curCol + 1) {
            return 4;
        }
        if (zeroRow == curRow - 1 && zeroCol == curCol - 1) {
            return 5;
        }
        if (zeroRow == curRow + 1 && zeroCol == curCol - 1) {
            return 6;
        }
        if (zeroRow == curRow - 1 && zeroCol == curCol + 1) {
            return 7;
        }
        if (zeroRow == curRow + 1 && zeroCol == curCol + 1) {
            return 8;
        }
        return 0;
    }


    @Override
    public Board clone()  {
        try {
            Object o = super.clone();
            Board b = (Board)o;
            b.board = b.board.clone();
            b.path = new ArrayList<>(b.path);
            return b;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }


}
