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
     * @param curRow
     * @param curCol
     * @param targetRow
     * @param targetCol
     * @param num
     * @param type:     1还原行 2还原列
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
//        if (curPuzzle.board[targetRow][targetCol] != num) {
//            System.out.println(num + " 有问题");
//        }
        for (int i = size; i < curPuzzle.restorePath.size(); i++) {
            puzzle.restoreSwap(puzzle.zeroRow + directions[curPuzzle.restorePath.get(i)][0], puzzle.zeroCol + directions[curPuzzle.restorePath.get(i)][1]);
        }
    }

    private boolean valid(int x, int y, int targetRow, int targetCol, int type) {
        if (x < 0 || x >= puzzle.curM || y < 0 || y >= puzzle.curN) {
            return false;
        }
        if (type == 1) {
            return x != puzzle.curM - 1 || y > targetCol - 1;
        }
        return y != puzzle.curN - 1 || x > targetRow - 1;
    }

    public void solveLastRow() {
        int num = (puzzle.curM - 1) * puzzle.n;
        for (int j = 0; j < puzzle.curN - 1; j++) {
            if (puzzle.board[puzzle.curM - 1][j] != num) {
                int[] index = puzzle.getIndex(num);
                normalMove(index[0], index[1], puzzle.curM - 1, j, num, 1);
            }
//            System.out.println("解决:" + num);
            num++;
        }
//        System.out.println("开始处理当前最右一个:" + num);
        // 已经在目标位置上
        if (puzzle.board[puzzle.curM - 1][puzzle.curN - 1] == num) {
            return;
        }
        // 空格在目标位置且上一行就是待归位元素，直接下移还原
        if (puzzle.zeroRow == puzzle.curM - 1 && puzzle.zeroCol == puzzle.curN - 1 && puzzle.board[puzzle.curM - 2][puzzle.curN - 1] == num) {
            puzzle.restoreSwap(puzzle.zeroRow - 1, puzzle.zeroCol);
            return;
        }
        // 空格在目标位置，先将空格移动到上面
        if (puzzle.zeroRow == puzzle.curM - 1 && puzzle.zeroCol == puzzle.curN - 1) {
            puzzle.restoreSwap(puzzle.zeroRow - 1, puzzle.zeroCol);
        }
        // 将元素移动到目标位置的上面
        if (puzzle.board[puzzle.curM - 2][puzzle.curN - 1] != num) {
            int[] index = puzzle.getIndex(num);
            normalMove(index[0], index[1], puzzle.curM - 2, puzzle.curN - 1, num, 1);
        }
        // 固定移动方式
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

    public void solveLastCol() {
        int num = puzzle.curN - 1;
        for (int i = 0; i < puzzle.curM - 1; i++) {
            if (puzzle.board[i][puzzle.curN - 1] != num) {
                int[] index = puzzle.getIndex(num);
                normalMove(index[0], index[1], i, puzzle.curN - 1, num, 2);
            }
            num += puzzle.n;
        }
        // 已经在目标位置上
        if (puzzle.board[puzzle.curM - 1][puzzle.curN - 1] == num) {
            return;
        }
        // 空格在目标位置且左边就是待归位元素，直接右移移还原
        if (puzzle.zeroRow == puzzle.curM - 1 && puzzle.zeroCol == puzzle.curN - 1 && puzzle.board[puzzle.curM - 1][puzzle.curN - 2] == num) {
            puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol - 1);
            return;
        }
        // 空格在目标位置，先将空格移动到左边
        if (puzzle.zeroRow == puzzle.curM - 1 && puzzle.zeroCol == puzzle.curN - 1) {
            puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol - 1);
        }
        // 将元素移动到目标位置的左面
        if (puzzle.board[puzzle.curM - 1][puzzle.curN - 2] != num) {
            int[] index = puzzle.getIndex(num);
            normalMove(index[0], index[1], puzzle.curM - 1, puzzle.curN - 2, num, 2);
        }
        // 固定移动方式
        for (int i = puzzle.zeroRow - 1; i >= 0; i--) {
            puzzle.restoreSwap(i, puzzle.zeroCol);
        }
        for (int j = puzzle.zeroCol + 1; j < puzzle.curN; j++) {
            puzzle.restoreSwap(0, j);
        }
        for (int i = 1; i < puzzle.curM; i++) {
            puzzle.restoreSwap(i, puzzle.zeroCol);
        }
        // 将目标元素归位
        puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol - 1);
        // 将上边，下左移回去
        puzzle.restoreSwap(puzzle.zeroRow - 1, puzzle.zeroCol);
        puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol + 1);
        for (int i = puzzle.curM - 3; i >= 0; i--) {
            puzzle.restoreSwap(i, puzzle.zeroCol);
        }
        puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol - 1);
    }

    class Node implements Comparable<Node> {
        Puzzle puzzle;
        String boardStr;
        int depth;
        int f;

        public Node(Puzzle puzzle, int depth) {
            this.puzzle = puzzle;
            this.depth = depth;
            this.boardStr = changeBoardToString();
            calculate();
        }

        String changeBoardToString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < puzzle.curM; i++) {
                for (int j = 0; j < puzzle.curN; j++) {
                    sb.append(puzzle.board[i][j]).append(' ');
                }
            }
            return sb.toString();
        }

        void calculate() {
            for (int i = 0; i < puzzle.curM; i++) {
                for (int j = 0; j < puzzle.curN; j++) {
                    if (puzzle.board[i][j] != 0) {
                        int[] targetIndex = puzzle.getIndex(puzzle.board[i][j]);
                        // 计算在一维数组中存在的下标，从0开始
                        f += Math.abs(i - targetIndex[0]) + Math.abs(j - targetIndex[1]);
                    }
                }
            }
            f /= 2;
//            f += depth;
        }

        @Override
        public int compareTo(Node o) {
            return this.f - o.f;
        }
    }


    /**
     * 处理2*2或者2*3
     */
    private void solveFixedSize() {
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}
        };
        Set<String> set = new HashSet<>();
        Queue<Node> queue = new PriorityQueue<>();
        Puzzle startPuzzle = puzzle.clone();
        Node start = new Node(startPuzzle, 0);
        queue.add(start);
        set.add(start.boardStr);
        String targetString = getTargetString();
        int size = puzzle.restorePath.size();
        Node curNode = null;
        while (!queue.isEmpty()) {
            curNode = queue.poll();
            if (targetString.equals(curNode.boardStr)) {
                System.out.println("成功");
                break;
            }
            handleNeighbor(curNode, queue, set, directions);
        }
        for (int i = size; i < curNode.puzzle.restorePath.size(); i++) {
            puzzle.restoreSwap(puzzle.zeroRow + directions[curNode.puzzle.restorePath.get(i)][0], puzzle.zeroCol + directions[curNode.puzzle.restorePath.get(i)][1]);
        }
    }

    private void handleNeighbor(Node node, Queue<Node> queue, Set<String> set, int[][] directions) {
        for (int[] direction : directions) {
            int tempRow = node.puzzle.zeroRow + direction[0];
            int tempCol = node.puzzle.zeroCol + direction[1];
            if (tempRow >= 0 && tempRow < puzzle.curM && tempCol >= 0 && tempCol < puzzle.curN) {
                Puzzle newPuzzle = node.puzzle.clone();
                newPuzzle.swapZero(newPuzzle.restorePath, tempRow, tempCol);
                Node newNode = new Node(newPuzzle, node.depth + 1);
                if (!set.contains(newNode.boardStr)) {
                    set.add(newNode.boardStr);
                    queue.add(newNode);
                }
            }
        }
    }

    private String getTargetString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < puzzle.curM; i++) {
            for (int j = 0; j < puzzle.curN; j++) {
                sb.append(i * puzzle.n + j).append(' ');
            }
        }
        return sb.toString();
    }

    public void solve() {
//        while (puzzle.curM * puzzle.curN > 6) {
//            if (puzzle.curM >= puzzle.curN) {
//                solveLastRow();
//                puzzle.curM--;
//            } else {
//                solveLastCol();
//                puzzle.curN--;
//            }
//        }
        while (puzzle.curM > 3) {
            solveLastRow();
            puzzle.curM--;
        }
        while (puzzle.curN > 2) {
            solveLastCol();
            puzzle.curN--;
        }
        System.out.println("终极");
        solveFixedSize();
    }
}

