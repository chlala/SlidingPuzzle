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

    class Solution {
        public int slidingPuzzle(int[][] board) {
            int R = board.length, C = board[0].length;
            int sr = 0, sc = 0;
            search:
            for (sr = 0; sr < R; sr++)
                for (sc = 0; sc < C; sc++)
                    if (board[sr][sc] == 0)
                        break search;

            int[][] directions = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
            Queue<Node> queue = new ArrayDeque<>();
            Node start = new Node(board, sr, sc, 0);
            queue.add(start);

            Set<String> seen = new HashSet();
            seen.add(start.boardstring);

            String target = Arrays.deepToString(new int[][]{{0, 1, 2}, {3, 4, 5}});

            while (!queue.isEmpty()) {
                Node node = queue.remove();
                if (node.boardstring.equals(target))
                    return node.depth;

                for (int[] di : directions) {
                    int nei_r = di[0] + node.zero_r;
                    int nei_c = di[1] + node.zero_c;

                    if ((Math.abs(nei_r - node.zero_r) + Math.abs(nei_c - node.zero_c) != 1) ||
                            nei_r < 0 || nei_r >= R || nei_c < 0 || nei_c >= C)
                        continue;

                    int[][] newboard = new int[R][C];
                    int t = 0;
                    for (int[] row : node.board)
                        newboard[t++] = row.clone();
                    newboard[node.zero_r][node.zero_c] = newboard[nei_r][nei_c];
                    newboard[nei_r][nei_c] = 0;

                    Node nei = new Node(newboard, nei_r, nei_c, node.depth + 1);
                    if (seen.contains(nei.boardstring))
                        continue;
                    queue.add(nei);
                    seen.add(nei.boardstring);
                }
            }

            return -1;
        }
    }

    class Node {
        int[][] board;
        String boardstring;
        int zero_r;
        int zero_c;
        int depth;

        Node(int[][] B, int r, int c, int d) {
            board = B;
            boardstring = Arrays.deepToString(board);
            zero_r = r;
            zero_c = c;
            depth = d;
        }
    }


    private void solve2multi3() {

    }


    public void solve() {
        while (puzzle.curM * puzzle.curN > 6) {
            if (puzzle.curM > puzzle.curN) {
                solveLastRow();
                puzzle.curM--;
            } else {
                solveLastCol();
                puzzle.curN--;
            }
        }

//        System.out.println("处理2*2");
//        solve2n();
    }
}


