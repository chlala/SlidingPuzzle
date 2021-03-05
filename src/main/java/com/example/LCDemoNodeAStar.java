package com.example;

import java.util.*;
import java.util.stream.Collectors;

public class LCDemoNodeAStar {

    public int slidingPuzzle(int[][] board) {
        int m = 2;
        int n = 3;
        Set<String> visited = new HashSet<>();
        Queue<Node> queue = new PriorityQueue<>();

        int zeroRow;
        int zeroCol = 0;
        search:
        for (zeroRow = 0; zeroRow < m; zeroRow++) {
            for (zeroCol = 0; zeroCol < n; zeroCol++) {
                if (board[zeroRow][zeroCol] == 0) {
                    break search;
                }
            }
        }

        queue.add(new Node(board, 0, zeroRow, zeroCol));
        String targetString = getTargetString(m, n);
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            visited.add(node.boardStr);
            if (targetString.equals(node.boardStr)) {
                return node.depth;
            }
            handleNeighbor(node, queue, visited, m, n);
        }
        return -1;
    }

    private String changeBoardToString(int[][] board) {
        return Arrays.stream(board)
                .flatMapToInt(Arrays::stream)
                .boxed().collect(Collectors.toList())
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining(" "))
                .trim();
    }

    private String getTargetString(int m, int n) {
        StringBuilder sb = new StringBuilder();
        int k = 1;
        for (int i = 0; i < m - 1; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(k).append(" ");
                k++;
            }
        }
        for (int j = 0; j < n - 1; j++) {
            sb.append(k).append(" ");
            k++;
        }
        sb.append("0");
        return sb.toString();
    }

    private void handleNeighbor(Node node, Queue<Node> queue, Set<String> visited, int m, int n) {
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}
        };
        int zeroRow = node.zeroRow;
        int zeroCol = node.zeroCol;
        int tempRow;
        int tempCol;
        for (int[] direction : directions) {
            tempRow = zeroRow + direction[0];
            tempCol = zeroCol + direction[1];
            if (tempRow >= 0 && tempRow < m && tempCol >= 0 && tempCol < n) {
                int[][] board = Arrays.stream(node.board).map(int[]::clone).toArray(int[][]::new);
                int temp = board[zeroRow][zeroCol];
                board[zeroRow][zeroCol] = board[tempRow][tempCol];
                board[tempRow][tempCol] = temp;
                Node newNode = new Node(board, node.depth + 1, tempRow, tempCol);
                if (!visited.contains(newNode.boardStr)) {
                    queue.add(newNode);
                }
            }
        }
    }

    private class Node implements Comparable<Node> {
        int[][] board;
        String boardStr;
        int depth;
        int zeroRow;
        int zeroCol;
        int f;

        Node(int[][] board, int depth, int zeroRow, int zeroCol) {
            this.board = board;
            this.depth = depth;
            this.zeroRow = zeroRow;
            this.zeroCol = zeroCol;
            this.boardStr = changeBoardToString(board);
            calculate();
        }

        void calculate() {
            int row = board.length;
            int col = board[0].length;
            for (int i = 0; i < row; i++) {
                for (int j = 0; j <col; j++) {
                    if (board[i][j] != 0) {
                        // 计算在一维数组中存在的下标，从0开始
                        int v = (board[i][j] + row * col - 1) % (row * col);
                        f += Math.abs(i - v/col) + Math.abs(j - v%col);
                    }
                }
            }
//            f /= 2;
            f += depth;
        }


        @Override
        public int compareTo(Node o) {
            return this.f - o.f;
        }
    }

    public static void main(String[] args) {
        int[][] board = {
                {1,2,3, 12},
                {4,6,5, 13},
                {9,8,7, 14},
                {11, 0, 10, 15}
        };
        LCDemoNodeAStar lcDemoString1 = new LCDemoNodeAStar();
        System.out.println(lcDemoString1.slidingPuzzle(board));
    }
}
