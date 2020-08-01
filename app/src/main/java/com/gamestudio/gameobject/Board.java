package com.gamestudio.gameobject;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.gamestudio.common.Constant;
import com.gamestudio.common.FunctionUtil;

public class Board {
    private static final int STATE_IDLE = 0;
    private static final int STATE_ANIM_BLOCK_DELETED = 1;
    private static final int STATE_BOARD_OVER = 2;

    private static final long BLOCK_DELETED_TIME = 300;
    private static final long EACH_BLOCK_DELETED_TIME = 20;

    private int[][] matrix;
    private int[][] matrixShadow;
    private int[][] matrixAnim;
    private int[] animRowBlockDeleted;
    private int[] animColBlockDeleted;
    private float width;
    private MovedBlock movedBlockTiledGround;
    private MovedBlock blockLander;
    private MovedBlock shadowBlock;
    private MovedBlock animBlock;

    private float x, y;
    private Index index;
    private Index indexForCheckBoard;

    private int state = STATE_IDLE;
    private float blockStartSize;
    private float blockSizeCurrently;
    private float speedBlockDeleted;

    private int rowDeleteGameOver, colDeleteGameOver;
    private long startBlockDeletedTime;

    private BoardCallback boardCallback;

    public Board(int size, Bitmap tiled, Bitmap shadowBlockImage, Bitmap blockLanderImage) {
        matrix = new int[size][size];
        matrixShadow = new int[size][size];
        matrixAnim = new int[size][size];
        animRowBlockDeleted = new int[size];
        animColBlockDeleted = new int[size];
        movedBlockTiledGround = new MovedBlock(0, 0, 0, 0, tiled);
        blockLander = new MovedBlock(0, 0, 0, 0, blockLanderImage);
        shadowBlock = new MovedBlock(0, 0, 0, 0, shadowBlockImage);
        animBlock = new MovedBlock(0, 0, 0, 0, shadowBlockImage);
        shadowBlock.setEnableAlpha(true);
        index = new Index();
        indexForCheckBoard = new Index();
    }

    public void setBound(float x, float y, float width) {
        this.width = width;
        this.x = x;
        this.y = y;
        movedBlockTiledGround.setSize(width / matrix.length);
        blockLander.setSize(width / matrix.length);
        shadowBlock.setSize(width / matrix.length);
        blockStartSize = width / matrix.length;
    }

    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(Color.YELLOW);
        float cellWidth = width / matrix.length;
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[0].length; col++) {
                movedBlockTiledGround.setX(col * cellWidth + this.x);
                movedBlockTiledGround.setY(row * cellWidth + this.y);
                movedBlockTiledGround.draw(canvas, paint);
                if (matrix[row][col] == 1) {
                    blockLander.setX(col * cellWidth + this.x);
                    blockLander.setY(row * cellWidth + this.y);
                    blockLander.draw(canvas, paint);
                } else if (matrix[row][col] == 2) {
                    paint.setColor(Color.BLACK);
                    paint.setAlpha(Constant.ALPHA_TUTORIAL);
                    canvas.drawRect(col * cellWidth + this.x, row * cellWidth + this.y, col * cellWidth + cellWidth + this.x, row * cellWidth + cellWidth + this.y, paint);
                    paint.setAlpha(Constant.MAX_ALPHA);
                }
                if (matrixShadow[row][col] == 1) {
                    shadowBlock.setX(col * cellWidth + this.x);
                    shadowBlock.setY(row * cellWidth + this.y);
                    shadowBlock.draw(canvas, paint);
                }
            }
        }
        if (state == STATE_ANIM_BLOCK_DELETED) {
            for (int row = 0; row < matrixAnim.length; row++) {
                for (int col = 0; col < matrixAnim[0].length; col++) {
                    if (matrixAnim[row][col] == 1) {
                        animBlock.setX(col * cellWidth + this.x + (blockStartSize - blockSizeCurrently) / 2);
                        animBlock.setY(row * cellWidth + this.y + (blockStartSize - blockSizeCurrently) / 2);
                        animBlock.draw(canvas, paint);
                    }
                }
            }
        }
    }

    public Index getIndexByCoordinate(float x, float y) {
        float blockWidth = this.width / 8;
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[0].length; col++) {
                float blockX1 = col * blockWidth + this.x;
                float blockX2 = blockX1 + blockWidth;
                float blockY1 = row * blockWidth + this.y;
                float blockY2 = blockY1 + blockWidth;
                if (x >= blockX1 && x < blockX2 && y >= blockY1 && y < blockY2) {
                    index.col = col;
                    index.row = row;
                    return index;
                }
            }
        }
        return null;
    }

    public void setShadow(Index index, int[][] matrixBlock) {
        for (int row = 0; row < matrixBlock.length; row++) {
            for (int col = 0; col < matrixBlock[0].length; col++) {
                int matrixRow = index.row + row;
                int matrixCol = index.col + col;
                if (matrixBlock[row][col] == 1 && FunctionUtil.checkIndexBound(matrixRow, matrixCol, matrixShadow)) {
                    matrixShadow[matrixRow][matrixCol] = 1;
                }
            }
        }
    }

    public boolean canPlaceGroupBlock(Index index, int[][] matrixBlock) {
        for (int row = 0; row < matrixBlock.length; row++) {
            for (int col = 0; col < matrixBlock[0].length; col++) {
                int matrixRow = index.row + row;
                int matrixCol = index.col + col;
                if (matrixRow >= matrix.length || matrixCol >= matrix[0].length) {
                    return false;
                }
                if (matrixBlock[row][col] == 1 && matrix[matrixRow][matrixCol] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public void placeGroupBlock(Index index, int[][] matrixBlock) {
        for (int row = 0; row < matrixBlock.length; row++) {
            for (int col = 0; col < matrixBlock[0].length; col++) {
                int matrixRow = index.row + row;
                int matrixCol = index.col + col;
                if (matrixBlock[row][col] == 1 && FunctionUtil.checkIndexBound(matrixRow, matrixCol, matrix)) {
                    matrix[matrixRow][matrixCol] = 1;
                }
            }
        }
    }

    public void resetShadowBoard() {
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[0].length; col++) {
                matrixShadow[row][col] = 0;
            }
        }
    }

    public void resetBoard() {
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[0].length; col++) {
                matrix[row][col] = 0;
            }
        }
    }

    public void startResetBoard() {
        rowDeleteGameOver = 0;
        colDeleteGameOver = 0;
        state = STATE_BOARD_OVER;
        startBlockDeletedTime = System.currentTimeMillis();
    }

    public void startDeleteBlocks() {
        state = STATE_ANIM_BLOCK_DELETED;
        blockSizeCurrently = blockStartSize;
        speedBlockDeleted = (blockStartSize - 0) / BLOCK_DELETED_TIME;
    }

    public void update(long deltaTime) {
        if (state == STATE_ANIM_BLOCK_DELETED) {
            blockSizeCurrently -= speedBlockDeleted * deltaTime;
            if (blockSizeCurrently < 0) {
                blockSizeCurrently = 0;
                state = STATE_IDLE;

                // Reset anim arrays
                for (int row = 0; row < matrixAnim.length; row++) {
                    setRow(row, 0, matrixAnim);
                }
            }
            animBlock.setSize(blockSizeCurrently);
        } else if (state == STATE_BOARD_OVER) {
            if (System.currentTimeMillis() - startBlockDeletedTime > EACH_BLOCK_DELETED_TIME) {
                startBlockDeletedTime = System.currentTimeMillis();
                matrix[rowDeleteGameOver][colDeleteGameOver] = 0;

                colDeleteGameOver++;
                if (colDeleteGameOver >= matrix[0].length) {
                    rowDeleteGameOver++;
                    colDeleteGameOver = 0;
                }
                if (rowDeleteGameOver >= matrix.length) {
                    if (boardCallback != null) {
                        boardCallback.onDeletedBoardAnimFinish();
                    }
                    state = STATE_IDLE;
                }
            }
        }
    }

    public int checkDeletedBlocks() {
        int point = 0;
        for (int row = 0; row < matrix.length; row++) {
            if (checkRow(row, 1, matrix)) {
                animRowBlockDeleted[row] = 1;
                point += 10;
            }
        }
        for (int col = 0; col < matrix[0].length; col++) {
            if (checkCol(col, 1, matrix)) {
                animColBlockDeleted[col] = 1;
                point += 10;
            }
        }

        for (int row = 0; row < animRowBlockDeleted.length; row++) {
            if (animRowBlockDeleted[row] == 1) {
                setRow(row, 0, matrix);
                setRow(row, 1, matrixAnim);
                animRowBlockDeleted[row] = 0;
            }
        }
        for (int col = 0; col < animColBlockDeleted.length; col++) {
            if (animColBlockDeleted[col] == 1) {
                setCol(col, 0, matrix);
                setCol(col, 1, matrixAnim);
                animColBlockDeleted[col] = 0;
            }
        }
        return point;
    }

    public boolean checkGroupBlockCanPlaceOnBoard(int[][] matrixBlockGroup) {
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[0].length; col++) {
                indexForCheckBoard.row = row;
                indexForCheckBoard.col = col;
                if (canPlaceGroupBlock(indexForCheckBoard, matrixBlockGroup)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkRow(int row, int value, int[][] matrix) {
        for (int col = 0; col < matrix[0].length; col++) {
            if (matrix[row][col] != value) {
                return false;
            }
        }
        return true;
    }

    private boolean checkCol(int col, int value, int[][] matrix) {
        for (int row = 0; row < matrix.length; row++) {
            if (matrix[row][col] != value) {
                return false;
            }
        }
        return true;
    }

    private void setRow(int row, int value, int[][] matrix) {
        for (int col = 0; col < matrix[0].length; col++) {
            matrix[row][col] = value;
        }
    }

    private void setCol(int col, int value, int[][] matrix) {
        for (int row = 0; row < matrix.length; row++) {
            matrix[row][col] = value;
        }
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public class Index {
        private int row, col;

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }
    }

    public void setBoardCallback(BoardCallback boardCallback) {
        this.boardCallback = boardCallback;
    }

    public interface BoardCallback {
        void onDeletedBoardAnimFinish();
    }

    public void setTutorialStep1() {
        for (int row = 0; row < matrix.length; row++) {
            setRow(row, 2, matrix);
        }
        setRow(4, 1, matrix);
        matrix[4][3] = matrix[4][4] = 0;
    }

    public void setTutorialStep2() {
        for (int row = 0; row < matrix.length; row++) {
            setRow(row, 2, matrix);
        }
        setCol(3, 1, matrix);
        matrix[4][3] = matrix[3][3] = 0;
    }

    public void setTutorialStep3() {
        for (int row = 0; row < matrix.length; row++) {
            setRow(row, 2, matrix);
        }
        setRow(3, 1, matrix);
        setRow(4, 1, matrix);
        setCol(3, 1, matrix);
        setCol(4, 1, matrix);
        matrix[3][3] = matrix[3][4] = matrix[4][3] = matrix[4][4] = 0;
    }
}
