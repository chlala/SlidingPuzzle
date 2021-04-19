package com.example.bishe;

public class Client {

    public static void main(String[] args) {
//        Puzzle puzzle = new Puzzle();
        Puzzle puzzle = null;
//        try {
//            for (int i = 0; i < 8; i++) {
                puzzle = new Puzzle(20, 20);
//                puzzle = new Puzzle(4, 4);
                System.out.println("初始化完成:");
                puzzle.print();
                Solution solution = new Solution(puzzle);
                solution.solve();
                System.out.println("全部归位!");
                puzzle.print();
//            }
//        } catch (Exception e) {
//            System.out.println("******************最终结果******************");
//            puzzle.print();
//            e.printStackTrace();
//        } finally {
            System.out.println("打乱步数:" + puzzle.initPath.size());
            System.out.println("还原步数:" + puzzle.restorePath.size());
//        }
    }
}
