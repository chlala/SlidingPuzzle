package com.example.bishe;

public class Client {

    public static void main(String[] args) {
//        Puzzle puzzle = new Puzzle();
        Puzzle puzzle = null;
//        try {
//            for (int i = 0; i < 8; i++) {
//                puzzle = new Puzzle();
        puzzle = new Puzzle(1000, 1000);
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


class TestResult {
    public static void main(String[] args) {
        Puzzle puzzle = null;
        int n = 1000;
        int m = 1000;
        for (int i = 900; i <= m; i+=50) {
            for (int j = 3;j <= 7;j+=2) {
                int max = 0;
                long all = 0;
                for (int k = 0; k < n; k++) {
                    puzzle =  new Puzzle(i,i);
//                    System.out.println("初始化完成:");
//                    puzzle.print();
                    Solution solution = new Solution(puzzle);
                    solution.solve();
//                    System.out.println("全部归位!");
//                    puzzle.print();
                    max = Math.max(max, puzzle.restorePath.size());
                    all += puzzle.restorePath.size();
                }
                System.out.printf("m=%d times=%d时最大=%d 平均=%d\n", i, j, max, all / n);
            }
        }

    }
}
