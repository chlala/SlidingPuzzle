package com.example.bishe;

public class Solution {

    private Puzzle puzzle;

    public Solution(Puzzle puzzle) {
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

    public void rightNotNeighborMove(int curRow, int curCol) {
        int zeroRow = puzzle.zeroRow;
        int zeroCol = puzzle.zeroCol;
        if (zeroCol > curCol) {
            // 空格在当前考虑的右边
            puzzle.restoreSwap(zeroRow, zeroCol - 1);
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

    public void bottomNormalMove(int curRow, int curCol, int targetRow, int targetCol) {
        int direction = handleNeighbor(curRow, curCol);
        if (direction == 0) {
            bottomNotNeighborMove(curRow, curCol, targetCol - 1);
            return;
        }
        int zeroRow = puzzle.zeroRow;
        int zeroCol = puzzle.zeroCol;
        switch (direction) {
            case 1:
                if (curCol > targetCol) {
                    // 当前位置在目标位置的右侧
                    puzzle.restoreSwap(zeroRow, zeroCol - 1);
                } else {
                    // 当前位置在目标位置的左侧或同一列
                    puzzle.restoreSwap(zeroRow, zeroCol + 1);
                }
                break;
            case 2:
                puzzle.restoreSwap(zeroRow - 1, zeroCol);
                break;
            case 3:
                if (curCol > targetCol) {
                    // 当前位置在目标位置的右侧
                    puzzle.restoreSwap(zeroRow, zeroCol + 1);
                } else {
                    // 当前位置在目标位置的右侧或同一列
                    if (zeroRow < targetRow - 1) {
                        // 空格不在倒数第二行
                        puzzle.restoreSwap(zeroRow + 1, zeroCol);
                    } else {
                        puzzle.restoreSwap(zeroRow - 1, zeroCol);
                    }
                }
                break;
            case 4:
                if (curCol > targetCol) {
                    // 当前位置在目标位置的右侧
                    if (zeroRow < targetRow) {
                        // 空格不在最后一行
                        puzzle.restoreSwap(zeroRow + 1, zeroCol);
                    } else {
                        puzzle.restoreSwap(zeroRow - 1, zeroCol);
                    }
                } else if (curCol < targetCol) {
                    // 当前位置在目标位置的右侧
                    puzzle.restoreSwap(zeroRow, zeroCol - 1);
                } else {
                    // 在同一列
                    puzzle.restoreSwap(zeroRow + 1, zeroCol);
                }
                break;
            case 5:
                if (curCol > targetCol) {
                    // 当前位置在目标位置的右侧
                    puzzle.restoreSwap(zeroRow + 1, zeroCol);
                } else {
                    // 当前位置在目标位置的左侧或同一列
                    puzzle.restoreSwap(zeroRow, zeroCol + 1);
                }
                break;
            case 6:
                if (curCol > targetCol) {
                    // 当前位置在目标位置的右侧
                    puzzle.restoreSwap(zeroRow - 1, zeroCol);
                } else {
                    // 当前位置在目标位置的左侧或同一列
                    puzzle.restoreSwap(zeroRow, zeroCol + 1);
                }
                break;
            case 7:
                if (curCol > targetCol) {
                    // 当前位置在目标位置的右侧
                    puzzle.restoreSwap(zeroRow, zeroCol - 1);
                } else {
                    // 当前位置在目标位置的左侧或同一列
                    puzzle.restoreSwap(zeroRow + 1, zeroCol);
                }
                break;
            case 8:
                if (curCol >= targetCol) {
                    // 当前位置在目标位置的右侧或同一列
                    puzzle.restoreSwap(zeroRow, zeroCol - 1);
                } else {
                    // 当前位置在目标位置的左侧
                    puzzle.restoreSwap(zeroRow - 1, zeroCol);
                }
                break;
        }
    }

    public void bottomLastMove(int curRow, int curCol) {
        int targetRow = puzzle.curM - 2;
        int targetCol = puzzle.curN - 1;
        int direction = handleNeighbor(curRow, curCol);
        if (direction == 0) {
            bottomNotNeighborMove(curRow, curCol, targetCol - 1);
            return;
        }
        int zeroRow = puzzle.zeroRow;
        int zeroCol = puzzle.zeroCol;
        switch (direction) {
            case 1:
                if (curCol == targetCol) {
                    // 当前位置在目标位置的同一列,即最后一列
                    puzzle.restoreSwap(zeroRow, zeroCol - 1);
                } else {
                    // 当前位置在目标位置的左侧
                    puzzle.restoreSwap(zeroRow, zeroCol + 1);
                }
                break;
            case 2:
            case 8:
                // 当前位置在目标位置的左侧
                puzzle.restoreSwap(zeroRow - 1, zeroCol);
                break;
            case 3:
                // 当前位置在目标位置的右侧或同一列
                if (zeroRow < targetRow) {
                    // 空格不在倒数第二行
                    puzzle.restoreSwap(zeroRow + 1, zeroCol);
                } else {
                    puzzle.restoreSwap(zeroRow - 1, zeroCol);
                }
                break;
            case 4:
                if (curCol < targetCol) {
                    // 当前位置在目标位置的右侧
                    puzzle.restoreSwap(zeroRow, zeroCol - 1);
                } else {
                    // 在同一列
                    puzzle.restoreSwap(zeroRow + 1, zeroCol);
                }
                break;
            case 5:
            case 6:
                // 当前位置在目标位置的左侧或同一列
                puzzle.restoreSwap(zeroRow, zeroCol + 1);
                break;
            case 7:
                // 当前位置在目标位置的左侧或同一列
                puzzle.restoreSwap(zeroRow + 1, zeroCol);
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
            System.out.println("解决:" + num);
            num++;
        }
        System.out.println("开始处理当前最右一个");
        if (puzzle.board[puzzle.curM - 1][puzzle.curN - 1] != num) {
            // 先将目标元素移动到target的正上方
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
                puzzle.restoreSwap(puzzle.curM - 2, puzzle.curN - 2);
                puzzle.restoreSwap(puzzle.curM - 1, puzzle.curN - 2);
                for (int j = puzzle.curN - 3; j >= 0; j--) {
                    puzzle.restoreSwap(puzzle.curM - 1, j);
                }
                puzzle.restoreSwap(puzzle.curM - 2, 0);
            }
        }
        puzzle.curM--;
    }

    public void solveLastCol() {
        int smallerNum = puzzle.curN - 1;
        int biggerNum = smallerNum + puzzle.n;
        int[] smallerIndex = puzzle.getIndex(smallerNum);
        int[] biggerIndex = puzzle.getIndex(biggerNum);
        if (smallerIndex[0] != 0) {
            moveSmallerToFirstRow(smallerIndex[0], smallerIndex[1]);
        }
        int direction = getReferenceDirection(smallerIndex[0], smallerIndex[1], biggerIndex[0], biggerIndex[1]);
        switch (direction) {
            case 1:

        }
        puzzle.curN--;
    }

    /**
     * 执行这个方法是前提条件是当前元素不在第一行
     *
     * @param row
     * @param col
     * @return
     */
    private void moveSmallerToFirstRow(int row, int col) {
        int direction = getReferenceDirection(puzzle.zeroRow, puzzle.zeroCol, row, col);
        switch (direction) {
            case 3:
                puzzle.restoreSwap(puzzle.zeroRow - 1, puzzle.zeroCol);
                puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol + 1);
                break;
            case 4:
                puzzle.restoreSwap(puzzle.zeroRow - 1, puzzle.zeroCol);
                puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol - 1);
                break;
            case 5:
                while (puzzle.zeroCol < col) {
                    puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol + 1);
                }
            case 6:
                while (puzzle.zeroCol > col) {
                    puzzle.restoreSwap(puzzle.zeroRow, puzzle.zeroCol - 1);
                }
        }
        puzzle.restoreSwap(puzzle.zeroRow + 1, puzzle.zeroCol);
    }

    /**
     * 1:当前位置在参考位置的正上方 2:正下 3:正左 4:正右 5:左上 6:左下 7:右上 8:右下
     *
     * @param referenceRow
     * @param referenceCol
     * @param curRow
     * @param curCol
     * @return
     */
    private int getReferenceDirection(int referenceRow, int referenceCol, int curRow, int curCol) {
        if (curRow < referenceRow && curCol == referenceCol) {
            return 1;
        }
        if (curRow > referenceRow && curCol == referenceCol) {
            return 2;
        }
        if (curRow == referenceRow && curCol < referenceCol) {
            return 3;
        }
        if (curRow == referenceRow && curCol > referenceCol) {
            return 4;
        }

        if (curRow < referenceRow && curCol < referenceCol) {
            return 5;
        }
        if (curRow > referenceRow && curCol < referenceCol) {
            return 6;
        }
        if (curRow < referenceRow) {
            return 7;
        }
        if (curRow > referenceRow) {
            return 8;
        }
        return 0;
    }


    public void solve() {
//        while (puzzle.curM > 2) {
//            solveLastRow();
//            System.out.printf("解决%s行\n", puzzle.m - puzzle.curM);
//            puzzle.print();
//        }
        solveLastCol();
    }
}
