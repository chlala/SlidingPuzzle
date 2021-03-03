package com.example;

import java.util.*;
import java.util.stream.Collectors;

public class LCDemoNode {

    public int slidingPuzzle(int[][] board) {
        int m = 2;
        int n = 3;
        Set<String> visited = new HashSet<>();
        Queue<Node> queue = new LinkedList<>();

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

    private class Node {
        int[][] board;
        String boardStr;
        int depth;
        int zeroRow;
        int zeroCol;

        Node(int[][] board, int depth, int zeroRow, int zeroCol) {
            this.board = board;
            this.depth = depth;
            this.zeroRow = zeroRow;
            this.zeroCol = zeroCol;
            this.boardStr = changeBoardToString(board);
        }
    }

    public static void main(String[] args) {
        int[][] board = {
                {1, 2, 3},
                {4, 0, 5}
        };
        LCDemoNode lcDemoString1 = new LCDemoNode();
        System.out.println(lcDemoString1.slidingPuzzle(board));
    }
}
