package com.example.bishe;

public class Solve {

    private Board board;

    public void setBoard(Board board) {
        this.board = board;
    }

    public int[] findIndex(int num) {
        for (int i = 0; i < board.m; i++) {
            for (int j = 0; j < board.n; j++) {
                if (board.board[i][j] == num) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[0];
    }


    /**
     * 处理空格和目标滑块没有挨着的情况
     *
     * @param curRow
     * @param curCol
     */
    public void notNeighborMove(int curRow, int curCol) {
        if (board.zeroCol > curCol) {
            // 空格在当前考虑的右边，向右移动
            board.swapZero(board.zeroRow, board.zeroCol - 1);
        } else if (board.zeroCol < curCol) {
            // 空格在当前考虑的左边，向左移动
            board.swapZero(board.zeroRow, board.zeroCol + 1);
        } else if (board.zeroRow < curRow) {
            // 空格在当前考虑的正上方，向上移动
            board.swapZero(board.zeroRow + 1, board.zeroCol);
        } else if (board.zeroRow > curRow) {
            // 空格在当前考虑的正下方，向下移动
            board.swapZero(board.zeroRow - 1, board.zeroCol);
        }
    }

    public void bottomNormalMove(int curRow, int curCol, int targetRow,  int targetCol) {
        int direction = board.handleNeighbor(curRow, curCol);
        if (direction == 0) {
            notNeighborMove(curRow, curCol);
            return;
        }
//        switch (direction) {
//            case 1:
//                 if (curCol > targetCol) {
//                    board.moveLeftWhenUp();
//                } else {
//                    board.moveRightWhenUp();
//                }
//                break;
//            case 2:
//                board.moveDownWhenLow();
//                break;
//            case 3:
//                if (curCol > targetCol) {
//                    board.moveLeftWhenLeft();
//                } else if (curRow < targetRow - 1) {
//                    board.moveDownWhenLeft();
//                } else {
//                    board.moveRightWhenLeft();
//                }
//                break;
//            case 4:
//                if (curCol < targetCol) {
//                    board.moveRightWhenRight();
//                } else if (curRow < targetRow - 1) {
//                    board.moveDownWhenRight();
//                } else {
//                    board.moveLeftWhenRight();
//                }
//                break;
//            case 5:
//                if (curCol > targetCol) {
//                    board.moveLeftWhenUpLeft();
//                } else if (curCol < targetCol){
//                    board.moveRightWhenUpLeft();
//                } else {
//                    if (curRow < targetRow - 1) {
//                        board.moveDownWhenUpLeft();
//                    } else {
//                        board.
//                    }
//                }
//                break;
//            case 6:
//                board.moveLeftWhenLowLeft();
//                break;
//            case 7:
//                board.moveLeftWhenUpRight();
//                break;
//            case 8:
//                board.moveDownWhenLowRight();
//                break;
//        }

        switch (direction) {
            case 1:
                if (curCol > targetCol) {
                    board.moveLeftWhenUp();
                } else {
                    board.moveRightWhenUp();
                }
                break;
            case 2:
                board.moveDownWhenLow();
                break;
            case 3:
                if (curCol > targetCol) {
                    board.moveLeftWhenLeft();
                } else if (curRow < targetRow - 1) {
                    board.moveDownWhenLeft();
                } else {
                    board.moveRightWhenLeft();
                }
                break;
            case 4:
                if (curCol < targetCol) {
                    board.moveRightWhenRight();
                } else if (curRow < targetRow - 1) {
                    board.moveDownWhenRight();
                } else {
                    board.moveLeftWhenRight();
                }
                break;
            case 5:
                if (curCol > targetCol) {
                    board.moveLeftWhenUpLeft();
                } else if (curCol < targetCol){
                    board.moveRightWhenUpLeft();
                } else {
                    if (curRow < targetRow - 1) {
                        board.moveDownWhenUpLeft();
                    } else {
                        board.
                    }
                }
                break;
            case 6:
                board.moveLeftWhenLowLeft();
                break;
            case 7:
                board.moveLeftWhenUpRight();
                break;
            case 8:
                board.moveDownWhenLowRight();
                break;
        }

    }


}
