package com.example.bishe;


import lombok.AllArgsConstructor;

import java.util.*;

public class Solution2 {

    private final Puzzle puzzle;

    public Solution2(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    @AllArgsConstructor
    class Grid {
        int x;
        int y;
        int zeroX;
        int zeroY;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Grid grid = (Grid) o;
            return x == grid.x && y == grid.y && zeroX == grid.zeroX && zeroY == grid.zeroY;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, zeroX, zeroY);
        }
    }

    /**
     *
     * @param curRow
     * @param curCol
     * @param targetRow
     * @param targetCol
     * @param num
     * @param type: 1还原行 2还原列
     */
    public void normalMove(int curRow, int curCol, int targetRow, int targetCol, int num, int type) {
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}
        };
        Puzzle startPuzzle = puzzle.clone();
        Queue<Puzzle> queue = new LinkedList<>();
        queue.add(startPuzzle);
        Set<Grid> set = new HashSet<>();
        set.add(new Grid(curRow, curCol, puzzle.zeroRow, puzzle.zeroCol));
        Puzzle curPuzzle = null;
        int size = puzzle.restorePath.size();
        while (!queue.isEmpty()) {
            curPuzzle = queue.poll();
            if (curPuzzle.board[targetRow][targetCol] == num) {
                break;
            }
            for (int[] direction : directions) {
                if (valid(curPuzzle.zeroRow + direction[0], curPuzzle.zeroCol + direction[1], targetRow, targetCol, type)) {
                    Puzzle newPuzzle = curPuzzle.clone();
                    newPuzzle.swapZero(newPuzzle.restorePath, newPuzzle.zeroRow + direction[0], newPuzzle.zeroCol + direction[1]);
                    int[] index = newPuzzle.getIndex(num);
                    Grid newGrid = new Grid(index[0], index[1], newPuzzle.zeroRow, newPuzzle.zeroCol);
                    if (!set.contains(newGrid)) {
                        set.add(newGrid);
                        queue.add(newPuzzle);

                    }
                }
            }
        }
        for (int i = size;i < curPuzzle.restorePath.size(); i++) {
            puzzle.restoreSwap(puzzle.zeroRow + directions[curPuzzle.restorePath.get(i)][0], puzzle.zeroCol + directions[curPuzzle.restorePath.get(i)][1]);
        }
    }

    private boolean valid(int x, int y, int targetRow,int targetCol, int type) {
        if (x < 0 || x >= puzzle.curM || y < 0 || y >= puzzle.curN) {
            return false;
        }
        if (type == 1) {
            return x != puzzle.curM - 1 || y > targetCol - 1;
        }
        return y != puzzle.curN - 1 || x > targetRow - 1;
    }

    public void solveLastRow() {
        int num = (puzzle.curM - 1) * puzzle.curN;
        for (int j = 0; j < puzzle.curN - 1; j++) {
            if (puzzle.board[puzzle.curM - 1][j] != num) {
                int[] index = puzzle.getIndex(num);
                normalMove(index[0], index[1], puzzle.curM - 1, j, num,1);
            }
//            System.out.println("解决:" + num);
            num++;
        }
//        System.out.println("开始处理当前最右一个:" + num);
        // 处理特殊情况
        // 1.已经在目标位置上
        if (puzzle.board[puzzle.curM - 1][puzzle.curN - 1] == num) {
            return;
        }
        // 2.空格在目标位置且上一行就是待归位元素，直接下移还原
        if (puzzle.zeroRow == puzzle.curM - 1 && puzzle.zeroCol == puzzle.curN - 1 && puzzle.board[puzzle.curM - 2][puzzle.curN - 1] == num) {
            puzzle.restoreSwap(puzzle.zeroRow - 1, puzzle.zeroCol);
            return;
        }
        // 3.空格在目标位置但上一行不是待归位元素，先将空格移到上面

        if (puzzle.board[puzzle.curM - 1][puzzle.curN - 1] != num) {
            // 先将目标元素移动到target的正上方
//            int targetRow = puzzle.curM - 2;
//            int targetCol = puzzle.curN - 1;
//            bottomLastMove(num, targetRow, targetCol);
            while (puzzle.board[puzzle.curM - 2][puzzle.curN - 1] != num) {
                int[] index = puzzle.getIndex(num);
                bottomLastMove(index[0], index[1]);
            }
            if (puzzle.zeroRow == puzzle.curM - 1 && puzzle.zeroCol == puzzle.curN - 1) {
                // 当前位置下方正好是空格
                puzzle.restoreSwap(puzzle.zeroRow - 1, puzzle.zeroCol);
            } else {
                for (int j = puzzle.zeroCol - 1; j >= 0; j--) {
                    puzzle.restoreSwap(puzzle.zeroRow, j);
                }
                for (int i = puzzle.zeroRow + 1; i < puzzle.curM; i++) {
                    puzzle.restoreSwap(i, 0);
                }
                for (int j = 1; j < puzzle.curN; j++) {
                    puzzle.restoreSwap(puzzle.zeroRow, j);
                }
                // 将目标元素归位
                puzzle.restoreSwap(puzzle.zeroRow - 1, puzzle.zeroCol);
                // 将左边，左上移回去
                puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol - 1);
                puzzle.restoreSwap(puzzle.zeroRow + 1, puzzle.zeroCol);
                for (int j = puzzle.curN - 3; j >= 0; j--) {
                    puzzle.restoreSwap(puzzle.zeroRow, j);
                }
                puzzle.restoreSwap(puzzle.zeroRow - 1, puzzle.zeroCol);
            }
        }
        puzzle.curM--;
    }

    public void solveLastCol() {
        int num = puzzle.curN - 1;
        for (int i = 0; i < puzzle.curM - 1; i++) {
            if (puzzle.board[i][puzzle.curN - 1] != num) {
                int[] index = puzzle.getIndex(num);
                normalMove(index[0], index[1], i,puzzle.curN - 1, num, 2);
            }
            num += puzzle.n;
        }
    }

    // TODO:不能复原，按理不应该啊
    private void solve2n() {
        int[][] rotateDirection = new int[][]{
                {1, 0}, {0, 1}, {-1, 0}, {0, -1}
        };
        int index;
        if (puzzle.zeroRow == 1 && puzzle.zeroCol == 0) {
            index = 1;
        } else if (puzzle.zeroRow == 1 && puzzle.zeroCol == 1) {
            index = 2;
        } else {
            index = 3;
        }
        while (!(puzzle.board[0][0] == 0 && puzzle.board[0][1] == 1
                && puzzle.board[1][0] == puzzle.n && puzzle.board[1][1] == puzzle.n + 1)) {
            puzzle.restoreSwap(puzzle.zeroRow + rotateDirection[index][0],
                    puzzle.zeroCol + rotateDirection[index][1]);
            index = (index + 1) % 4;
        }
    }

    public void solve() {
//        while (puzzle.curM > 2) {
            solveLastRow();
//            System.out.printf("解决%s行\n", puzzle.m - puzzle.curM);
//        }
//        while (puzzle.curN > 2) {
//            solveLastCol();
//            puzzle.curN--;
//            System.out.printf("解决%s列\n", puzzle.n - puzzle.curN);
//        }
//        System.out.println("处理2*2");
//        solve2n();
    }
}
