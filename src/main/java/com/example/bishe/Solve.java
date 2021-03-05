package com.example.bishe;

import java.util.*;

public class Solve {

    private Board board;



    public void setBoard(Board board) {
        this.board = board;
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

    }

    public Board moveToTarget(Grid targetGrid, int targetNum, Set<Grid> still) {
        Queue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(node -> node.board.step));
        queue.add(new Node(board.clone()));
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            if (node.board.board[targetGrid.row][targetGrid.col] == targetNum) {
                return node.board;
            }
            for (int i = 0; i < DirectionUtil.directions.length; i++) {
                int[] direction = DirectionUtil.directions[i];
                int swapRow = node.board.zeroRow + direction[0];
                int swapCol = node.board.zeroCol + direction[1];
                if (DirectionUtil.isValid(new Grid(swapRow, swapCol), node, still)) {
                    Node newNode = node.clone();
                    newNode.board.swapZero(node.board.zeroRow + direction[0], node.board.zeroCol + direction[1]);
                    newNode.board.path.add(i);
                    newNode.board.step++;
                    newNode.visited[swapRow][swapCol] = true;
                    queue.add(newNode);
                }
            }
        }
        return null;
    }

    public void solveLastRow() {
        Set<Grid> still = new HashSet<>();
        for (int j = 0; j < board.curN - 1; j++) {
            int num = (board.curM - 1) * board.n + j;
//            Grid curGrid = findIndex(num);
            Grid targetGrid = new Grid(board.curM - 1, j);
            Board partTargetBord = moveToTarget(targetGrid, num, still);
            still.add(targetGrid);
            this.board = partTargetBord;
        }
        int num = (board.curM - 2) * board.n + board.curN - 1;
        Grid targetGrid = new Grid(board.curM - 2, board.curN - 1);
        this.board = moveToTarget(targetGrid, num, still);
    }

    public void solveLastCol() {
        Set<Grid> still = new HashSet<>();
        for (int i = 0; i < board.curM - 1; i++) {
            int num = i * board.n + board.curN;
//            Grid curGrid = findIndex(num);
            Grid targetGrid = new Grid(i, board.curN - 1);
            Board partTargetBord = moveToTarget(targetGrid, num, still);
            still.add(targetGrid);
            this.board = partTargetBord;
        }
//            Grid curGrid = findIndex(num);
//        Grid targetGrid = new Grid(i, board.curN - 1);
//        Board partTargetBord = moveToTarget(targetGrid, num, still);
//        still.add(targetGrid);
//        this.board = partTargetBord;
    }

    public void solve() {
        while (board.curM >= 2 || board.curN >= 2) {
            if (board.curM >= board.curN) {
                solveLastRow();
                board.print();
                board.curM--;
            } /*else {
                solveLastCol();
                board.curN--;
            }*/
        }

    }


}
