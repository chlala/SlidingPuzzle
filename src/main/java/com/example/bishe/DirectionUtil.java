package com.example.bishe;

import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Set;

@EqualsAndHashCode
class Grid {
    int row;
    int col;

    public Grid(int row, int col) {
        this.row = row;
        this.col = col;
    }
}

class Node implements Cloneable {
    Board board;
    boolean[][] visited;

    public Node(Board board) {
        this.board = board;
        visited = new boolean[board.curN][board.curN];
        visited[board.zeroRow][board.zeroCol] = true;
    }

    @Override
    public Node clone()  {
        try {
            Object o = super.clone();
            Node node = (Node) o;
            node.board = node.board.clone();
            node.visited = visited.clone();
            return node;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}

public class DirectionUtil {

    public static int[][] directions = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1}
    };

   static Grid[] indexArr;

    public static void initIndexArr(Board board) {
        indexArr = new Grid[board.m * board.n];
        for (int i = 0; i < board.m; i++) {
            for (int j = 0; j < board.n; j++) {
                indexArr[board.board[i][j]] = new Grid(i, j);
            }
        }
    }

    public static Grid getIndex(int num) {
        return indexArr[num];
    }

    public static boolean isValid(Grid cur, Node node, Set<Grid> still) {
        if (cur.row >= node.board.curM || cur.row < 0 || cur.col >= node.board.curN
                || cur.col < 0 || node.visited[cur.row][cur.col]) {
            return false;
        }
        return !still.contains(cur);
    }


}
