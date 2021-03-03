package com.example.bishe;

import java.util.Random;


public class InitWork {

    public int[][] board;

    public void init(int m, int n) {
        board = new int[m][n];
        int k = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i != m - 1 || j != n - 1) {
                    board[i][j] = ++k;
                }
            }
        }
        shuffle();
    }

    public void shuffle() {
        int m = board.length;
        int n = board[0].length;
        Random random = new Random();
        int len = m * n;
        for (int i = 0; i < m * n; i++) {
            int index = random.nextInt(len - i) + i;
            int temp = board[i / n][i % n];
            board[i / n][i % n] = board[index / n][index % n];
            board[index / n][index % n] = temp;
        }
    }

    public static void main(String[] args) {
        Board board = new Board(3, 3);
        board.print();
    }
}
