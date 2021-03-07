package com.example.bishe;

public class Client {

    public static void main(String[] args) {
        Puzzle puzzle = new Puzzle();
        System.out.println("初始化完成:");
        puzzle.print();
        Solution solution = new Solution(puzzle);
        solution.solve();
        System.out.println("全部归位!");
        puzzle.print();
        System.out.println(puzzle.restorePath.size());
        System.out.println(puzzle.initPath.size());
    }
}
