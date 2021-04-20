package com.example.bishe;

public class Client2 {

    public static void main(String[] args) {
//        Puzzle puzzle = new Puzzle();
        Puzzle puzzle = new Puzzle(4, 6);
        System.out.println("初始化完成:");
        puzzle.print();
        Solution2 solution = new Solution2(puzzle);
        solution.solve();
        System.out.println("全部归位!");
        puzzle.print();

        System.out.println("打乱步数:" + puzzle.initPath.size());
        System.out.println("还原步数:" + puzzle.restorePath.size());
    }
}
