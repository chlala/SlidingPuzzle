package com.example.bishe;

public class Client {

    public static void main(String[] args) {
        Board board = new Board(5, 5);
        board.print();
        Solve solve = new Solve();
        solve.setBoard(board);
        solve.solve();
        System.out.println("-----------");
        board.print();
    }
}
