package com.gamestudio.gameobject;

import com.gamestudio.common.ResourceManager;

public class MovedGroupBlockFactory {

    public MovedGroupBlock getGroupBlockOne() {
        int[][] matrix = new int[1][1];
        matrix[0][0] = 1;
        return new MovedGroupBlock(0, 0, 50, 60, matrix, ResourceManager.getInstance().getBitmap("stone"));
    }

    public MovedGroupBlock getGroupBlockTowHorizontal() {
        int[][] matrix = new int[1][2];
        matrix[0][0] = 1;
        matrix[0][1] = 1;
        return new MovedGroupBlock(0, 0, 50, 60, matrix, ResourceManager.getInstance().getBitmap("stone"));
    }

    public MovedGroupBlock getGroupBlockTowVertical() {
        int[][] matrix = new int[2][1];
        matrix[0][0] = 1;
        matrix[1][0] = 1;
        return new MovedGroupBlock(0, 0, 50, 60, matrix, ResourceManager.getInstance().getBitmap("stone"));
    }

    public MovedGroupBlock getGroupBlockThreeHorizontal() {
        int[][] matrix = new int[1][3];
        matrix[0][0] = 1;
        matrix[0][1] = 1;
        matrix[0][2] = 1;
        return new MovedGroupBlock(0, 0, 50, 60, matrix, ResourceManager.getInstance().getBitmap("stone"));
    }

    public MovedGroupBlock getGroupBlockThreeVertical() {
        int[][] matrix = new int[3][1];
        matrix[0][0] = 1;
        matrix[1][0] = 1;
        matrix[2][0] = 1;
        return new MovedGroupBlock(0, 0, 50, 60, matrix, ResourceManager.getInstance().getBitmap("stone"));
    }

    public MovedGroupBlock getGroupBlockThreeC() {
        int[][] matrix = new int[2][2];
        matrix[0][0] = 1;
        matrix[1][0] = 1;
        matrix[1][1] = 1;
        return new MovedGroupBlock(0, 0, 50, 60, matrix, ResourceManager.getInstance().getBitmap("stone"));
    }

    public MovedGroupBlock getGroupBlockFourZ() {
        int[][] matrix = new int[2][3];
        matrix[0][1] = 1;
        matrix[0][2] = 1;
        matrix[1][0] = 1;
        matrix[1][1] = 1;
        return new MovedGroupBlock(0, 0, 50, 60, matrix, ResourceManager.getInstance().getBitmap("stone"));
    }

    public MovedGroupBlock getGroupBlockFourTriangle() {
        int[][] matrix = new int[2][3];
        matrix[0][1] = 1;
        matrix[1][0] = 1;
        matrix[1][1] = 1;
        matrix[1][2] = 1;
        return new MovedGroupBlock(0, 0, 50, 60, matrix, ResourceManager.getInstance().getBitmap("stone"));
    }

    public MovedGroupBlock getGroupBlockFourL() {
        int[][] matrix = new int[2][3];
        matrix[0][0] = 1;
        matrix[1][0] = 1;
        matrix[1][1] = 1;
        matrix[1][2] = 1;
        return new MovedGroupBlock(0, 0, 50, 60, matrix, ResourceManager.getInstance().getBitmap("stone"));
    }

    public MovedGroupBlock getGroupBlockFourHorizontal() {
        int[][] matrix = new int[1][4];
        matrix[0][0] = 1;
        matrix[0][1] = 1;
        matrix[0][2] = 1;
        matrix[0][3] = 1;
        return new MovedGroupBlock(0, 0, 50, 60, matrix, ResourceManager.getInstance().getBitmap("stone"));
    }

    public MovedGroupBlock getGroupBlockFourVertical() {
        int[][] matrix = new int[4][1];
        matrix[0][0] = 1;
        matrix[1][0] = 1;
        matrix[2][0] = 1;
        matrix[3][0] = 1;
        return new MovedGroupBlock(0, 0, 50, 60, matrix, ResourceManager.getInstance().getBitmap("stone"));
    }

    public MovedGroupBlock getGroupBlockFourSqure() {
        int[][] matrix = new int[2][2];
        matrix[0][0] = 1;
        matrix[0][1] = 1;
        matrix[1][0] = 1;
        matrix[1][1] = 1;
        return new MovedGroupBlock(0, 0, 50, 60, matrix, ResourceManager.getInstance().getBitmap("stone"));
    }

    public MovedGroupBlock getGroupBlockFiveL() {
        int[][] matrix = new int[3][3];
        matrix[0][0] = 1;
        matrix[0][1] = 1;
        matrix[0][2] = 1;
        matrix[1][2] = 1;
        matrix[2][2] = 1;
        return new MovedGroupBlock(0, 0, 50, 60, matrix, ResourceManager.getInstance().getBitmap("stone"));
    }

    public MovedGroupBlock getGroupBlockFiveHorizontal() {
        int[][] matrix = new int[1][5];
        matrix[0][0] = 1;
        matrix[0][1] = 1;
        matrix[0][2] = 1;
        matrix[0][3] = 1;
        matrix[0][4] = 1;
        return new MovedGroupBlock(0, 0, 50, 60, matrix, ResourceManager.getInstance().getBitmap("stone"));
    }

    public MovedGroupBlock getGroupBlockNine() {
        int[][] matrix = new int[3][3];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = 1;
            }
        }
        return new MovedGroupBlock(0, 0, 50, 60, matrix, ResourceManager.getInstance().getBitmap("stone"));
    }
}
