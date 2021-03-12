package com.example.bishe;

public class Client {

    public static void main(String[] args) {
//        Puzzle puzzle = new Puzzle();
//        Puzzle puzzle = new Puzzle(10, 10);
//        System.out.println("初始化完成:");
//        puzzle.print();
//        Solution solution = new Solution(puzzle);
//        solution.solve();
//        System.out.println("全部归位!");
//        puzzle.print();
//        System.out.println("打乱步数:" + puzzle.initPath.size());
//        System.out.println("还原步数:" + puzzle.restorePath.size());

        Puzzle puzzle = new Puzzle(2, 4);
        try {
            System.out.println("初始化完成:");
            puzzle.print();
            Solution solution = new Solution(puzzle);
            solution.solve();
            System.out.println("全部归位!");
            puzzle.print();
        } finally {
            System.out.println("打乱步数:" + puzzle.initPath.size());
            System.out.println("还原步数:" + puzzle.restorePath.size());
        }
    }
}
