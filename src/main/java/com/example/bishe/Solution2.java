package com.example.bishe;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.*;

public class Solution2 {

    private final Puzzle puzzle;

    public Solution2(Puzzle puzzle) {
        this.puzzle = puzzle;
    }


    /**
     * 空格在考虑位置的方向
     *
     * @param curRow
     * @param curCol
     * @return: 1:上 2：下 3：左 4：右 5：左上 6：左下 7：右上 8：右下 0：不相邻
     */
    public int handleNeighbor(int curRow, int curCol) {
        int zeroRow = puzzle.zeroRow;
        int zeroCol = puzzle.zeroCol;
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

    /**
     * 处理空格和目标滑块没有挨着的情况
     *
     * @param curRow
     * @param curCol
     */
    public void bottomNotNeighborMove(int curRow, int curCol, int stillCol) {
        int zeroRow = puzzle.zeroRow;
        int zeroCol = puzzle.zeroCol;
        if (zeroCol > curCol) {
            // 空格在当前考虑的右边
            if (zeroRow != puzzle.curM - 1 || stillCol < zeroCol - 1) {
                // 向右移不会影响之前固定好的
                puzzle.restoreSwap(zeroRow, zeroCol - 1);
            } else {
                // 会影响说明在空格在当前位置的右下方,向下移
                puzzle.restoreSwap(zeroRow - 1, zeroCol);
            }
        } else if (zeroCol < curCol) {
            // 空格在当前考虑的左边
            puzzle.restoreSwap(zeroRow, zeroCol + 1);
        } else if (zeroRow < curRow) {
            // 空格在当前考虑的正上方，向上移动
            puzzle.restoreSwap(zeroRow + 1, zeroCol);
        } else if (zeroRow > curRow) {
            // 空格在当前考虑的正下方，向下移动
            puzzle.restoreSwap(zeroRow - 1, zeroCol);
        }
    }

    class Grid {
        int x;
        int y;

        boolean target;

        List<Integer> path = new ArrayList<>();

        public Grid(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Grid grid = (Grid) o;
            return x == grid.x && y == grid.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        protected Grid clone() {
            Grid grid = new Grid(this.x, this.y);
            grid.path = new ArrayList<>(this.path);
            return grid;
        }

        @Override
        public String toString() {
            return "Grid{" +
                    "x=" + x +
                    ", y=" + y +
                    ", path=" + path +
                    '}';
        }
    }

    public void bottomNormalMove(int curRow, int curCol, int targetRow, int targetCol) {
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}
        };
        Grid targetGrid = new Grid(targetRow, targetCol);
        Grid startGrid = new Grid(puzzle.zeroRow, puzzle.zeroCol);
        Queue<Grid> queue = new LinkedList<>();
        queue.add(startGrid);
        Set<Grid> set = new HashSet<>();
        set.add(startGrid);
        Grid curGrid = null;
        while (!queue.isEmpty()) {
             curGrid = queue.poll();
            if (curGrid.target) {
                System.out.println(curGrid);
                break;
            }
            for (int i = 0; i < directions.length; i++) {
                int[] direction = directions[i];
                if (valid(curGrid.x + direction[0], curGrid.y + direction[1], targetCol - 1)) {
                    Grid newGrid = curGrid.clone();
                    newGrid.x += direction[0];
                    newGrid.y += direction[1];
                    if (!set.contains(newGrid)) {
                        newGrid.path.add(i);
                        set.add(newGrid);
                        queue.add(newGrid);
                        if (newGrid.x == targetRow && newGrid.y== targetCol) {
                            newGrid.target = true;
                        }
                    }
                }
            }
        }
        for (int index : curGrid.path) {
            puzzle.restoreSwap(puzzle.zeroRow + directions[index][0], puzzle.zeroCol + directions[index][1]);
        }

    }

    private boolean valid(int x, int y, int solvedCol) {
        if (x < 0 || x >= puzzle.curM || y < 0 || y >= puzzle.curN) {
            return false;
        }
        return x != puzzle.curM - 1 || y > solvedCol;
    }

    public void bottomLastMove(int curRow, int curCol) {
        int targetRow = puzzle.curM - 2;
        int targetCol = puzzle.curN - 1;
        int direction = handleNeighbor(curRow, curCol);
        if (direction == 0) {
            bottomNotNeighborMove(curRow, curCol, targetCol - 1);
            return;
        }
//        System.out.println(direction);
        // TODO:死循环问题 似乎已解决 还不确定
        switch (direction) {
            case 1:
                if (curCol == targetCol) {
                    // 当前位置在目标位置的同一列,即最后一列
                    puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol - 1);
                } else {
                    // 当前位置在目标位置的左侧
                    puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol + 1);
                }
                break;
            case 2:
            case 8:
                // 当前位置在目标位置的左侧
                puzzle.restoreSwap(puzzle.zeroRow - 1, puzzle.zeroCol);
                break;
            case 3:
                // 当前位置在目标位置的右侧或同一列
                if (puzzle.zeroRow < targetRow) {
                    // 空格不在倒数第二行
                    puzzle.restoreSwap(puzzle.zeroRow + 1, puzzle.zeroCol);
                } else {
                    puzzle.restoreSwap(puzzle.zeroRow - 1, puzzle.zeroCol);
                }
                break;
            case 4:
                if (curCol < targetCol) {
                    // 当前位置在目标位置的右侧
                    puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol - 1);
                } else {
                    // 在同一列
                    puzzle.restoreSwap(puzzle.zeroRow + 1, puzzle.zeroCol);
                }
                break;
            case 5:
                if (curCol == puzzle.curN - 1) {
                    puzzle.restoreSwap(puzzle.zeroRow + 1, puzzle.zeroCol);
                } else {
                    puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol + 1);
                }
            case 6:
                // 当前位置在目标位置的左侧或同一列
                puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol + 1);
                break;
            case 7:
                // 当前位置在目标位置的左侧或同一列
                puzzle.restoreSwap(puzzle.zeroRow + 1, puzzle.zeroCol);
                break;
        }
    }

    public void bottomLastMove(int num, int targetRow, int targetCol) {
        int[] index = puzzle.getIndex(num);
        int direction = handleNeighbor(index[0], index[1]);
        if (index[0] == targetRow && index[1] == targetCol) {
            return;
        }
        if (direction == 0) {
            bottomNotNeighborMove(index[0], index[1], targetCol - 1);
            return;
        }
//        System.out.println(direction);
        switch (direction) {
//            case 2:


            case 1:
                if (index[1] == targetCol) {
                    // 当前位置在目标位置的同一列,即最后一列
                    puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol - 1);
                } else {
                    // 当前位置在目标位置的左侧
                    puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol + 1);
                }
                break;
            case 2:
            case 8:
                // 当前位置在目标位置的左侧
                puzzle.restoreSwap(puzzle.zeroRow - 1, puzzle.zeroCol);
                break;
            case 3:
                // 当前位置在目标位置的右侧或同一列
                if (puzzle.zeroRow < targetRow) {
                    // 空格不在倒数第二行
                    puzzle.restoreSwap(puzzle.zeroRow + 1, puzzle.zeroCol);
                } else {
                    puzzle.restoreSwap(puzzle.zeroRow - 1, puzzle.zeroCol);
                }
                break;
            case 4:
                if (index[1] < targetCol) {
                    // 当前位置在目标位置的右侧
                    puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol - 1);
                } else {
                    // 在同一列
                    puzzle.restoreSwap(puzzle.zeroRow + 1, puzzle.zeroCol);
                }
                break;
            case 5:
                puzzle.restoreSwap(puzzle.zeroRow + 1, puzzle.zeroCol);
                break;
            case 6:
                // 当前位置在目标位置的左侧或同一列
                puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol + 1);
                break;
            case 7:
                // 当前位置在目标位置的左侧或同一列
                puzzle.restoreSwap(puzzle.zeroRow + 1, puzzle.zeroCol);
                break;
        }
    }

    public void solveLastRow() {
        int num = (puzzle.curM - 1) * puzzle.curN;
        for (int j = 0; j < puzzle.curN - 1; j++) {
            while (puzzle.board[puzzle.curM - 1][j] != num) {
                int[] index = puzzle.getIndex(num);
                bottomNormalMove(index[0], index[1], puzzle.curM - 1, j);
            }
//            System.out.println("解决:" + num);
            num++;
        }
//        System.out.println("开始处理当前最右一个:" + num);
        // 直接下移还原
        if (puzzle.zeroRow == puzzle.curM - 1 && puzzle.zeroCol == puzzle.curN - 1 && puzzle.board[puzzle.curM - 2][puzzle.curN - 1] == num) {
            puzzle.restoreSwap(puzzle.zeroRow - 1, puzzle.zeroCol);
            return;
        }
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
        int smallerNum = puzzle.curN - 1;
        int biggerNum = smallerNum + puzzle.n;
        if (puzzle.board[0][puzzle.curN - 1] == smallerNum && puzzle.board[1][puzzle.curN - 1] == biggerNum) {
            return;
        }
        if (puzzle.board[0][puzzle.curN - 1] == smallerNum && puzzle.zeroCol == puzzle.curN - 2 && puzzle.board[1][puzzle.curN - 2] == biggerNum) {
            return;
        }
        moveSmallerToFirstRow(smallerNum);
        moveTwoNumNeighbor(smallerNum, biggerNum);
        moveTwoNumTargetCol(smallerNum, biggerNum);
    }

    private void moveSmallerToFirstRow(int num) {
        int[] index = puzzle.getIndex(num);
        if (index[0] == 0) {
            return;
        }
        if (puzzle.zeroRow == 0 && puzzle.zeroCol == index[1]) {
            puzzle.restoreSwap(puzzle.zeroRow + 1, puzzle.zeroCol);
            return;
        }
        if (puzzle.zeroRow == 0) {
            while (puzzle.zeroCol < index[1]) {
                puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol + 1);
            }
            while (puzzle.zeroCol > index[1]) {
                puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol - 1);
            }
        } else {
            puzzle.restoreSwap(puzzle.zeroRow - 1, puzzle.zeroCol);
        }
        moveSmallerToFirstRow(num);
    }

    /**
     * 把两个数移动到挨在一起
     */
    private void moveTwoNumNeighbor(int smallerNum, int biggerNum) {
        int[] smallerIndex = puzzle.getIndex(smallerNum);
        int[] biggerIndex = puzzle.getIndex(biggerNum);
        if (biggerIndex[0] == 0 && smallerIndex[1] == biggerIndex[1] - 1) {
            return;
        }
        int direction = getComparePosition(smallerIndex[1], biggerIndex[0], biggerIndex[1]);
//        System.out.println(smallerNum + " " + biggerNum + " " + direction);
        switch (direction) {
            case 1:
                handleMoveTwoNumberNeighborWhenLine(smallerIndex);
                break;
            case 2:
                // 大的数在小的的正左边
                if (smallerIndex[1] == biggerIndex[1] + 1) {
                    // 两个数挨着但顺序反了
                    boolean fixedMove = false;
                    if (puzzle.zeroCol < biggerIndex[1]) {
                        if (puzzle.zeroRow == 1) {
                            puzzle.restoreSwap(puzzle.zeroRow - 1, puzzle.zeroCol);
                        } else {
                            while (puzzle.zeroCol < biggerIndex[1]) {
                                puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol + 1);
                            }
                            fixedMove = true;
                        }
                    } else if (puzzle.zeroCol > smallerIndex[1]) {
                        if (puzzle.zeroRow == 1) {
                            puzzle.restoreSwap(puzzle.zeroRow - 1, puzzle.zeroCol);
                        } else {
                            while (puzzle.zeroCol > smallerIndex[1]) {
                                puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol - 1);
                            }
                            fixedMove = true;
                        }
                    } else {
                        // 空格正好在两个数的下面
                        if (biggerIndex[1] == 0) {
                            while (puzzle.zeroCol <= smallerIndex[1]) {
                                puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol + 1);
                            }
                        } else {
                            while (puzzle.zeroCol >= biggerIndex[1]) {
                                puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol - 1);
                            }
                        }
                    }
                    if (fixedMove) {
                        puzzle.restoreSwap(puzzle.zeroRow + 1, puzzle.zeroCol);
                        puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol - 1);
                        puzzle.restoreSwap(puzzle.zeroRow - 1, puzzle.zeroCol);
                        puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol + 1);
                        puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol + 1);
                        puzzle.restoreSwap(puzzle.zeroRow + 1, puzzle.zeroCol);
                        puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol - 1);
                        puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol - 1);
                        puzzle.restoreSwap(puzzle.zeroRow - 1, puzzle.zeroCol);
                        puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol + 1);
                        puzzle.restoreSwap(puzzle.zeroRow + 1, puzzle.zeroCol);
                    }
                } else {
                    handleMoveTwoNumberNeighborWhenLine(biggerIndex);
                }
                break;
            case 3:
                // 大的数在第二行并且在小的数的左边,要做的事是将大的数移动到第一行，走case2逻辑
                if (puzzle.zeroRow == 1) {
                    if (puzzle.zeroCol == smallerIndex[1]) {
                        puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol - 1);
                    }
                    puzzle.restoreSwap(puzzle.zeroRow - 1, puzzle.zeroCol);
                } else {
                    while (puzzle.zeroCol < biggerIndex[1]) {
                        puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol + 1);
                    }
                    while (puzzle.zeroCol > biggerIndex[1]) {
                        puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol - 1);
                    }
                    puzzle.restoreSwap(puzzle.zeroRow + 1, puzzle.zeroCol);
                }
                break;
            case 4:
                // 大的数在第二行并且在小的数的右边
                if (puzzle.zeroRow == 0 && puzzle.zeroCol == biggerIndex[1]) {
                    // 空格在大的数的正上方，将大的数移到第一行，走case1逻辑
                    puzzle.restoreSwap(puzzle.zeroRow + 1, puzzle.zeroCol);
                } else if (puzzle.zeroRow == 0 && puzzle.zeroCol < biggerIndex[1]) {
                    // 空格在第一行并且在大的数的左边，将空格移动到大的数的正上方，以走上一个if逻辑
                    while (puzzle.zeroCol < biggerIndex[1]) {
                        puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol + 1);
                    }
                } else if (puzzle.zeroRow == 0 && puzzle.zeroCol > biggerIndex[1]) {
                    // 空格在第一行并且在大的数的右边，将空格移动到大的数的正上方，以走上一个if逻辑
                    while (puzzle.zeroCol > biggerIndex[1]) {
                        puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol - 1);
                    }
                } else if (puzzle.zeroRow == 1 && puzzle.zeroCol < biggerIndex[1]) {
                    // 空格在第二行并且在大的数的左边，将空格移动到第一行，以走上一个if逻辑
                    // 特殊情况：如果空格和小的数刚好在一列，不能上下移动
                    if (puzzle.zeroCol == smallerIndex[1]) {
                        if (puzzle.zeroCol == 0) {
                            // 不能向左移了，固定移法，移成两个数刚好挨着相反
                            puzzle.restoreSwap(puzzle.zeroRow - 1, puzzle.zeroCol);
                            puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol + 1);
                            puzzle.restoreSwap(puzzle.zeroRow + 1, puzzle.zeroCol);
                            puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol - 1);
                            puzzle.restoreSwap(puzzle.zeroRow - 1, puzzle.zeroCol);
                            puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol + 1);
                            puzzle.restoreSwap(puzzle.zeroRow + 1, puzzle.zeroCol);
                        } else {
                            puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol - 1);
                        }
                    } else {
                        puzzle.restoreSwap(puzzle.zeroRow - 1, puzzle.zeroCol);
                    }
                } else if (puzzle.zeroRow == 1 && puzzle.zeroCol > biggerIndex[1]) {
                    // 空格在第二行并且在大的数的右边
                    puzzle.restoreSwap(puzzle.zeroRow - 1, puzzle.zeroCol);
                }
                break;
            case 5:
                // 大的数正好在小的数正下边
                if (puzzle.zeroCol > smallerIndex[1]) {
                    // 空格在右边
                    while (puzzle.zeroCol > smallerIndex[1]) {
                        puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol - 1);
                    }
                } else {
                    // 空格在左边
                    while (puzzle.zeroCol < smallerIndex[1]) {
                        puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol + 1);
                    }
                }
                break;
        }
        moveTwoNumNeighbor(smallerNum, biggerNum);
    }

    private void handleMoveTwoNumberNeighborWhenLine(int[] leftIndex) {
        if (puzzle.zeroRow == 0) {
            if (puzzle.zeroCol > leftIndex[1]) {
                while (puzzle.zeroCol > leftIndex[1]) {
                    puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol - 1);
                }
            } else {
                puzzle.restoreSwap(puzzle.zeroRow + 1, puzzle.zeroCol);
            }
        } else {
            if (puzzle.zeroCol == leftIndex[1] + 1) {
                puzzle.restoreSwap(puzzle.zeroRow - 1, puzzle.zeroCol);
                return;
            }
            while (puzzle.zeroCol <= leftIndex[1]) {
                puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol + 1);
            }
            while (puzzle.zeroCol > leftIndex[1] + 1) {
                puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol - 1);
            }
        }
    }

    /**
     * 将两个挨着并且顺序正确的数移动到目标列
     *
     * @param smallerNum
     * @param biggerNum
     */
    private void moveTwoNumTargetCol(int smallerNum, int biggerNum) {
        int[] smallerIndex = puzzle.getIndex(smallerNum);
        int[] biggerIndex = puzzle.getIndex(biggerNum);
        if (smallerIndex[0] == 0 && smallerIndex[1] == puzzle.curN - 1 && biggerIndex[0] == 1 && biggerIndex[1] == puzzle.curN - 1) {
            return;
        }
        if (puzzle.zeroRow == 1) {
            if (puzzle.zeroCol == puzzle.curN - 1) {
                // 空格在最后一个位置
                puzzle.restoreSwap(puzzle.zeroRow - 1, puzzle.zeroCol);
                // 两个一起归位
                if (biggerIndex[1] == puzzle.curN - 1) {
                    puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol - 1);
                }
            } else {
                // 将空格移动到最后一个位置
                while (puzzle.zeroCol < puzzle.curN - 1) {
                    puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol + 1);
                }
            }
        } else {
            if (puzzle.zeroCol < smallerIndex[1]) {
                // 空格在第一行并且在小的数的左边，将空格移动到第二行
                puzzle.restoreSwap(puzzle.zeroRow + 1, puzzle.zeroCol);
            } else {
                // 空格在第一行并且在大的数的右边
                while (puzzle.zeroCol > smallerIndex[1]) {
                    puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol - 1);
                }
            }
        }
        moveTwoNumTargetCol(smallerNum, biggerNum);
    }

    private int getComparePosition(int smallerCol, int biggerRow, int biggerCol) {
        if (biggerRow == 0 && biggerCol > smallerCol) {
            return 1;
        }
        if (biggerRow == 0 && biggerCol < smallerCol) {
            return 2;
        }
        if (biggerRow == 1 && biggerCol < smallerCol) {
            return 3;
        }
        if (biggerRow == 1 && biggerCol > smallerCol) {
            return 4;
        }
        if (biggerRow == 1 && biggerCol == smallerCol) {
            return 5;
        }
        return 0;
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
        while (puzzle.curM > 2) {
            solveLastRow();
//            System.out.printf("解决%s行\n", puzzle.m - puzzle.curM);
        }
        while (puzzle.curN > 2) {
            solveLastCol();
            puzzle.curN--;
//            System.out.printf("解决%s列\n", puzzle.n - puzzle.curN);
        }
//        System.out.println("处理2*2");
        solve2n();
    }
}
