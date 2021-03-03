package com.example;

import java.util.*;
import java.util.stream.Collectors;

public class LCDemoString {

    public int slidingPuzzle(int[][] board) {
        int m = 2;
        int n = 3;
        Set<String> visited = new HashSet<>();
        Queue<int[][]> queue = new LinkedList<>();
        queue.add(board);
        String targetString = getTargetString(m, n);
        int step = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                board = queue.poll();
                visited.add(changeBoardToString(board));
                if (targetString.equals(changeBoardToString(board))) {
                    return step;
                }
                handleNeighbor(board, queue, visited);
            }
            step++;
        }
        System.out.println(visited.size());
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

    private int[] findZeroIndex(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == 0) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[0];
    }

    private void swap(int[][] board, int i1, int j1, int i2, int j2) {
        int temp = board[i1][j1];
        board[i1][j1] = board[i2][j2];
        board[i2][j2] = temp;
    }

    private void handleNeighbor(int[][] board, Queue<int[][]> queue, Set<String> visited) {
        int[] indexArr = findZeroIndex(board);
        int i = indexArr[0], j = indexArr[1];
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}
        };
        for (int k = 0; k < directions.length; k++) {
            if (i + directions[k][0] >= 0 && i + directions[k][0] < board.length
            && j + directions[k][1] >= 0 && j + directions[k][1] < board[0].length) {
                int[][] newBoard = Arrays.stream(board).map(int[]::clone).toArray(int[][]::new);
                swap(newBoard, i, j, i + directions[k][0], j + directions[k][1]);
                if (!visited.contains(changeBoardToString(newBoard))) {
                    queue.add(newBoard);
                }
            }
        }
    }

    public static void main(String[] args) {
        int[][] board = {
                {1, 2, 3},
                {5, 4, 0}
        };
        LCDemoString lcDemoString = new LCDemoString();
        System.out.println(lcDemoString.slidingPuzzle(board));
    }
}
