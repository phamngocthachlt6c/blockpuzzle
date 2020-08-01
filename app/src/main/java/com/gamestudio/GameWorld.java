package com.gamestudio;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;

import com.thachpham.blockpuzzle_rockstone.R;
import com.gamestudio.common.GameConfig;
import com.gamestudio.common.ResourceManager;
import com.gamestudio.gameobject.Board;
import com.gamestudio.gameobject.GameOverMenu;
import com.gamestudio.gameobject.GamePlayHeader;
import com.gamestudio.gameobject.MovedGroupBlock;
import com.gamestudio.gameobject.MovedGroupBlockManager;
import com.gamestudio.gameobject.TutorialHand;

public class GameWorld implements View.OnTouchListener {
    private static final long TIME_FOR_SUSPEND_UPDATE_AND_DRAW = 1000; // Note: every animation time is smaller than this

    private static final float BOARD_X = 0.05f;
    private static final float BOARD_Y = 0.15f;
    private static final float BOARD_WIDTH = 0.9f;

    //TODO: here
    private static final int TUTORIAL_STEP_1 = 1;
    private static final int TUTORIAL_STEP_2 = 2;
    private static final int TUTORIAL_STEP_3 = 3;
    private static final int TUTORIAL_STEP_FINISH = 4;
    private int tutorialStep;

    // TODO: back ground co cac loai day leo kieu rung ru

    private static final long GAME_OVER_GO_IN_TIME = 700;
    private static final long GAME_PLAY_GO_IN_TIME = 400;

    public static final int STATE_GOTO_PLAYING = 1;
    public static final int STATE_PLAYING = 2;
    public static final int STATE_GAME_OVER_GO_IN = 3;
    public static final int STATE_GAME_OVER = 4;
    public static final int STATE_GAME_OVER_GO_OUT = 5;
    private int gameState = STATE_GOTO_PLAYING;

    private static final float STANDARD_SCREEN_WIDTH = 1000f;

    private long splashBeginFramePassed;

    private GamePlayHeader gamePlayHeader;
    private float gamePlayHeaderFixedPosY;
    private float gamePlayHeaderSpeedGoIn;
    private GameOverMenu gameOverMenu;
    private float gameOverMenuFixedPosY;
    private float gameOverMenuSpeedGoIn;
    private MainActivity mainActivity;
    private Paint mPaint;

    private int highScore, gold;

    private Vibrator mVibrator;

    // Game objects
    private MovedGroupBlockManager movedGroupBlockPooler;
    private MovedGroupBlock movedGroupBlockFirst;
    private MovedGroupBlock movedGroupBlockSecond;
    private MovedGroupBlock movedGroupBlockThird;
    private Board board;
    private int backgroundGreen;
    private Bitmap bitmapFooter;

    // Fixed positions
    private float blocksHookedPositionY;
    private float blocksFirstHookedPosX;
    private float blocksSecondHookedPosX;
    private float blocksThirdHookedPosX;
    private Rect footerRect;

    private Context context;

    private GameConfig gameConfig;
    private TutorialHand tutorialHand;

    private boolean isFirstSetResolution = true;

    public GameWorld(Context context) {
        gameConfig = GameConfig.getInstance(context);
        gameConfig.loadConfig();
        mainActivity = (MainActivity) context;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.context = context;
        initObjectGame();

        tutorialHand = new TutorialHand();
        tutorialStep = gameConfig.getTutorialStep();
        if (tutorialStep < TUTORIAL_STEP_FINISH) {
            tutorialStep = TUTORIAL_STEP_1;
            board.setTutorialStep1();
        }
    }

    private void initObjectGame() {
        gameOverMenu = new GameOverMenu(this);
        gamePlayHeader = new GamePlayHeader();
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        movedGroupBlockPooler = new MovedGroupBlockManager();
        generateGroupBlocks();
        board = new Board(8, ResourceManager.getInstance().getBitmap("tiled_stone_ground"),
                ResourceManager.getInstance().getBitmap("stone"),
                ResourceManager.getInstance().getBitmap("stone"));
        board.setBoardCallback(new Board.BoardCallback() {
            @Override
            public void onDeletedBoardAnimFinish() {
                changeState(STATE_GAME_OVER_GO_IN);
            }
        });

        backgroundGreen = context.getResources().getColor(R.color.background_green);
        bitmapFooter = ResourceManager.getInstance().getBitmap("footer_background");
        footerRect = new Rect();

    }

    private void generateGroupBlocks() {
        if (tutorialStep < TUTORIAL_STEP_FINISH) {
            movedGroupBlockFirst = movedGroupBlockPooler.getGroupBlockById(2);
            movedGroupBlockSecond = movedGroupBlockPooler.getGroupBlockById(1);
            movedGroupBlockThird = movedGroupBlockPooler.getGroupBlockById(11);
        } else {
            movedGroupBlockFirst = movedGroupBlockPooler.getBlockRandom();
            movedGroupBlockSecond = movedGroupBlockPooler.getBlockRandom();
            movedGroupBlockThird = movedGroupBlockPooler.getBlockRandom();
        }
    }

//    private void vibrate(int time) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            mVibrator.vibrate(VibrationEffect.createOneShot(time, VibrationEffect.DEFAULT_AMPLITUDE));
//        } else {
//            //deprecated in API 26
//            mVibrator.vibrate(time);
//        }
//    }

    public void setResolution(float deviceWidth, float deviceHeight) {
        if (!isFirstSetResolution) {
            return;
        }
        isFirstSetResolution = false;

        gameOverMenuFixedPosY = deviceHeight * 0.1f;
        gameOverMenu.setX(deviceWidth * 0.1f);
        gameOverMenu.setWidth(deviceWidth * 0.8f);
        gameOverMenu.setHeight(deviceHeight * 0.8f);
        gameOverMenu.setY(-gameOverMenu.getHeight());
        gameOverMenuSpeedGoIn = (gameOverMenuFixedPosY + gameOverMenu.getHeight()) / GAME_OVER_GO_IN_TIME;

        gamePlayHeaderFixedPosY = deviceHeight * (BOARD_Y / 6);
        gamePlayHeader.setH(deviceHeight * (BOARD_Y / 6 * 4));
        gamePlayHeader.setY(-gamePlayHeader.getH());
        gamePlayHeaderSpeedGoIn = (gamePlayHeaderFixedPosY + gamePlayHeader.getH()) / GAME_PLAY_GO_IN_TIME;
        gamePlayHeader.setX(BOARD_X * deviceWidth);
        gamePlayHeader.setW(BOARD_WIDTH * deviceWidth);

        float boardY = deviceHeight * BOARD_Y;
        float boardX = deviceWidth * BOARD_X;
        float boardWidth = deviceWidth * BOARD_WIDTH;
        board.setBound(boardX, boardY, boardWidth);
        movedGroupBlockPooler.setBlockMaxWidth(boardWidth / 8);

        // init fixed positions
        blocksHookedPositionY = boardY + boardWidth + deviceWidth * 0.25f;
        float distanceX = boardWidth / 6;
        blocksFirstHookedPosX = boardX + distanceX;
        blocksSecondHookedPosX = boardX + distanceX * 3;
        blocksThirdHookedPosX = boardX + distanceX * 5;

        footerRect.left = (int) (boardX);
        footerRect.right = (int) (deviceWidth - boardX);
        footerRect.top = (int) (blocksHookedPositionY - boardWidth / 8 * 0.4f * 5);
        footerRect.bottom = (int) (blocksHookedPositionY + boardWidth / 8 * 0.4f * 3);

        setGroupBlocksPosition();

        // First init group blocks
        movedGroupBlockFirst.setSizeMaximum(boardWidth / 8);
        movedGroupBlockFirst.setChildSizeNormal(boardWidth / 8 * 0.4f);
        movedGroupBlockSecond.setSizeMaximum(boardWidth / 8);
        movedGroupBlockSecond.setChildSizeNormal(boardWidth / 8 * 0.4f);
        movedGroupBlockThird.setSizeMaximum(boardWidth / 8);
        movedGroupBlockThird.setChildSizeNormal(boardWidth / 8 * 0.4f);

        tutorialHand.setW(deviceWidth * 0.15f);
        tutorialHand.setH(tutorialHand.getW() * 1.5f);
        tutorialHand.setHomeX(deviceWidth * 0.5f - tutorialHand.getW() / 2);
        tutorialHand.setHomeY(blocksHookedPositionY);
        tutorialHand.setTargetX(deviceWidth * 0.5f - tutorialHand.getW() / 2);
        tutorialHand.setTargetY(tutorialHand.getHomeY() - boardWidth / 8 * 6);
    }

    public void draw(Canvas canvas, int width, int height) {
        mPaint.setColor(backgroundGreen);
        canvas.drawRect(0, 0, width, height, mPaint);
        board.draw(canvas, mPaint);
        gamePlayHeader.draw(canvas, mPaint);
        canvas.drawBitmap(bitmapFooter, null, footerRect, mPaint);

        // draw block group
        movedGroupBlockFirst.draw(canvas, mPaint);
        movedGroupBlockSecond.draw(canvas, mPaint);
        movedGroupBlockThird.draw(canvas, mPaint);

        if (tutorialStep < TUTORIAL_STEP_FINISH) {
            tutorialHand.draw(canvas, mPaint);
        }
        switch (gameState) {
            case STATE_GOTO_PLAYING:
            case STATE_PLAYING:

                break;
            case STATE_GAME_OVER:
            case STATE_GAME_OVER_GO_IN:
            case STATE_GAME_OVER_GO_OUT:
                gameOverMenu.draw(canvas, mPaint);
                break;
        }

//        if (tutorialStep == TUTORIAL_STEP_1) {
//            canvas.drawBitmap(ResourceManager.getInstance().getBitmap("tutorial1_bg"), null, screenRect, mPaint);
//        } else if (tutorialStep == TUTORIAL_STEP_2) {
//            canvas.drawBitmap(ResourceManager.getInstance().getBitmap("tutorial2_bg"), null, screenRect, mPaint);
//        } else if (tutorialStep == TUTORIAL_STEP_3) {
//
//        }
    }

    public void update(long deltaTime) {
        if (tutorialStep < TUTORIAL_STEP_FINISH) {
            tutorialHand.update(deltaTime);
        }
        if (!movedGroupBlockFirst.isHidden()) {
            movedGroupBlockFirst.update(deltaTime);
        }
        if (!movedGroupBlockSecond.isHidden()) {
            movedGroupBlockSecond.update(deltaTime);
        }
        if (!movedGroupBlockThird.isHidden()) {
            movedGroupBlockThird.update(deltaTime);
        }
        board.update(deltaTime);
//        Log.d("aaa", "update: ");
        switch (gameState) {
            case STATE_GOTO_PLAYING:
                gamePlayHeader.setY(gamePlayHeader.getY() + gamePlayHeaderSpeedGoIn * deltaTime);
                if (gamePlayHeader.getY() > gamePlayHeaderFixedPosY) {
                    gamePlayHeader.setY(gamePlayHeaderFixedPosY);
                    changeState(STATE_PLAYING);
                }
                break;
            case STATE_PLAYING:
                gamePlayHeader.update(deltaTime);
                break;
            case STATE_GAME_OVER:

                break;
            case STATE_GAME_OVER_GO_IN:
                gameOverMenu.setY(gameOverMenu.getY() + gameOverMenuSpeedGoIn * deltaTime);
                if (gameOverMenu.getY() > gameOverMenuFixedPosY) {
                    gameOverMenu.setY(gameOverMenuFixedPosY);
                    changeState(STATE_GAME_OVER);
                }
                break;
            case STATE_GAME_OVER_GO_OUT:
                gameOverMenu.setY(gameOverMenu.getY() - gameOverMenuSpeedGoIn * deltaTime);
                if (gameOverMenu.getY() < -gameOverMenu.getHeight()) {
                    gameOverMenu.setY(-gameOverMenu.getHeight());
                    changeState(STATE_PLAYING);
                    resetGame();
                }
                break;
        }
//        Log.d("aaaa", "update: first " + movedGroupBlockFirst.hashCode()
//        + ";second " + movedGroupBlockSecond.hashCode()
//              +  ";third " + movedGroupBlockThird.hashCode());
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (gameState) {
            case STATE_PLAYING:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    processPressingOnMovedBlock(motionEvent);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    processTouchUpMovedBlock(motionEvent);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    processMovingMovedBlock(motionEvent);
                }
                break;
            case STATE_GAME_OVER:
                gameOverMenu.processTouch(motionEvent);
                break;
        }
        return true;
    }

    private void processPressingOnMovedBlock(MotionEvent motionEvent) {
        if (!movedGroupBlockFirst.isHidden() && movedGroupBlockFirst.isInArea(motionEvent.getX(), motionEvent.getY())
                && (tutorialStep >= TUTORIAL_STEP_FINISH || tutorialStep == TUTORIAL_STEP_2)) {
            movedGroupBlockFirst.startZoomOut();
            movedGroupBlockFirst.setIsPressed(true);
        }
        if (!movedGroupBlockSecond.isHidden() && movedGroupBlockSecond.isInArea(motionEvent.getX(), motionEvent.getY())
                && (tutorialStep >= TUTORIAL_STEP_FINISH || tutorialStep == TUTORIAL_STEP_1)) {
            movedGroupBlockSecond.startZoomOut();
            movedGroupBlockSecond.setIsPressed(true);
        }
        if (!movedGroupBlockThird.isHidden() && movedGroupBlockThird.isInArea(motionEvent.getX(), motionEvent.getY())
                && (tutorialStep >= TUTORIAL_STEP_FINISH || tutorialStep == TUTORIAL_STEP_3)) {
            movedGroupBlockThird.startZoomOut();
            movedGroupBlockThird.setIsPressed(true);
        }
    }

    private void processMovingMovedBlock(MotionEvent motionEvent) {
        if (!movedGroupBlockFirst.isHidden() && movedGroupBlockFirst.isPressed()) {
            movedGroupBlockFirst.setMiddleX(motionEvent.getX());
            movedGroupBlockFirst.setMiddleY(motionEvent.getY());

            Board.Index index = board.getIndexByCoordinate(movedGroupBlockFirst.getFirstBlockMiddleX(), movedGroupBlockFirst.getFirstBlockMiddleY());
            if (index != null && board.canPlaceGroupBlock(index, movedGroupBlockFirst.getBlockMatrix())) {
                board.resetShadowBoard();
                board.setShadow(index, movedGroupBlockFirst.getBlockMatrix());
            } else {
                board.resetShadowBoard();
            }
        }
        if (!movedGroupBlockSecond.isHidden() && movedGroupBlockSecond.isPressed()) {
            movedGroupBlockSecond.setMiddleX(motionEvent.getX());
            movedGroupBlockSecond.setMiddleY(motionEvent.getY());

            Board.Index index = board.getIndexByCoordinate(movedGroupBlockSecond.getFirstBlockMiddleX(), movedGroupBlockSecond.getFirstBlockMiddleY());
            if (index != null && board.canPlaceGroupBlock(index, movedGroupBlockSecond.getBlockMatrix())) {
                board.resetShadowBoard();
                board.setShadow(index, movedGroupBlockSecond.getBlockMatrix());
            } else {
                board.resetShadowBoard();
            }
        }
        if (!movedGroupBlockThird.isHidden() && movedGroupBlockThird.isPressed()) {
            movedGroupBlockThird.setMiddleX(motionEvent.getX());
            movedGroupBlockThird.setMiddleY(motionEvent.getY());

            Board.Index index = board.getIndexByCoordinate(movedGroupBlockThird.getFirstBlockMiddleX(), movedGroupBlockThird.getFirstBlockMiddleY());
            if (index != null && board.canPlaceGroupBlock(index, movedGroupBlockThird.getBlockMatrix())) {
                board.resetShadowBoard();
                board.setShadow(index, movedGroupBlockThird.getBlockMatrix());
            } else {
                board.resetShadowBoard();
            }
        }
    }

    private void processTouchUpMovedBlock(MotionEvent motionEvent) {
        if (!movedGroupBlockFirst.isHidden()) {
            if (movedGroupBlockFirst.isPressed()) {
                movedGroupBlockFirst.setIsPressed(false);
                Board.Index index = board.getIndexByCoordinate(movedGroupBlockFirst.getFirstBlockMiddleX(), movedGroupBlockFirst.getFirstBlockMiddleY());
                if (index != null && board.canPlaceGroupBlock(index, movedGroupBlockFirst.getBlockMatrix())) {
                    board.placeGroupBlock(index, movedGroupBlockFirst.getBlockMatrix());
                    movedGroupBlockFirst.setHidden(true);
                    int point;
                    if ((point = board.checkDeletedBlocks()) > 0) {
                        board.startDeleteBlocks();
                        gamePlayHeader.startUpPoint(point);
                    }
                    checkTutorial();
                    checkPlaceAbleForThreeGroupBlock();
                } else {
                    movedGroupBlockFirst.startMoveHome(blocksFirstHookedPosX, blocksHookedPositionY);
                }
            }
        }
        if (!movedGroupBlockSecond.isHidden()) {
            if (movedGroupBlockSecond.isPressed()) {
                movedGroupBlockSecond.setIsPressed(false);
                Board.Index index = board.getIndexByCoordinate(movedGroupBlockSecond.getFirstBlockMiddleX(), movedGroupBlockSecond.getFirstBlockMiddleY());
                if (index != null && board.canPlaceGroupBlock(index, movedGroupBlockSecond.getBlockMatrix())) {
                    board.placeGroupBlock(index, movedGroupBlockSecond.getBlockMatrix());
                    movedGroupBlockSecond.setHidden(true);
                    int point;
                    if ((point = board.checkDeletedBlocks()) > 0) {
                        board.startDeleteBlocks();
                        gamePlayHeader.startUpPoint(point);
                    }
                    checkTutorial();
                    checkPlaceAbleForThreeGroupBlock();
                } else {
                    movedGroupBlockSecond.startMoveHome(blocksSecondHookedPosX, blocksHookedPositionY);
                }
            }
        }
        if (!movedGroupBlockThird.isHidden()) {
            if (movedGroupBlockThird.isPressed()) {
                movedGroupBlockThird.setIsPressed(false);
                Board.Index index = board.getIndexByCoordinate(movedGroupBlockThird.getFirstBlockMiddleX(), movedGroupBlockThird.getFirstBlockMiddleY());
                if (index != null && board.canPlaceGroupBlock(index, movedGroupBlockThird.getBlockMatrix())) {
                    board.placeGroupBlock(index, movedGroupBlockThird.getBlockMatrix());
                    movedGroupBlockThird.setHidden(true);
                    int point;
                    if ((point = board.checkDeletedBlocks()) > 0) {
                        board.startDeleteBlocks();
                        gamePlayHeader.startUpPoint(point);
                    }
                    checkTutorial();
                    checkPlaceAbleForThreeGroupBlock();
                } else {
                    movedGroupBlockThird.startMoveHome(blocksThirdHookedPosX, blocksHookedPositionY);
                }
            }
        }

        board.resetShadowBoard();

        if (movedGroupBlockFirst.isHidden() && movedGroupBlockSecond.isHidden() && movedGroupBlockThird.isHidden()) {
            generateGroupBlocks();
            if (checkGameOver()) {
                // TODO: Generate a place able fragment set to first block
                movedGroupBlockFirst = movedGroupBlockPooler.getAFixedGroupBlock(board);
//                movedGroupBlockSecond = movedGroupBlockPooler.getAFixedGroupBlock(board);
//                movedGroupBlockThird = movedGroupBlockPooler.getAFixedGroupBlock(board);
            }
            setGroupBlocksPosition();
            checkPlaceAbleForThreeGroupBlock();
        } else {
            if (checkGameOver()) {
                board.startResetBoard();
            }
        }
    }

    private boolean checkGameOver() {
        boolean gameOver = true;
        if (!movedGroupBlockFirst.isHidden() && board.checkGroupBlockCanPlaceOnBoard(movedGroupBlockFirst.getBlockMatrix())) {
            gameOver = false;
        }
        if (!movedGroupBlockSecond.isHidden() && board.checkGroupBlockCanPlaceOnBoard(movedGroupBlockSecond.getBlockMatrix())) {
            gameOver = false;
        }
        if (!movedGroupBlockThird.isHidden() && board.checkGroupBlockCanPlaceOnBoard(movedGroupBlockThird.getBlockMatrix())) {
            gameOver = false;
        }
        return gameOver;
    }

    public void changeState(int state) {
        this.gameState = state;
    }

    private void checkPlaceAbleForThreeGroupBlock() {
        if (!movedGroupBlockFirst.isHidden()) {
            movedGroupBlockFirst.setEnableBlockAlpha(!board.checkGroupBlockCanPlaceOnBoard(movedGroupBlockFirst.getBlockMatrix()));
        }
        if (!movedGroupBlockSecond.isHidden()) {
            movedGroupBlockSecond.setEnableBlockAlpha(!board.checkGroupBlockCanPlaceOnBoard(movedGroupBlockSecond.getBlockMatrix()));
        }
        if (!movedGroupBlockThird.isHidden()) {
            movedGroupBlockThird.setEnableBlockAlpha(!board.checkGroupBlockCanPlaceOnBoard(movedGroupBlockThird.getBlockMatrix()));
        }
    }

    private void setGroupBlocksPosition() {
        movedGroupBlockFirst.setMiddleX(blocksFirstHookedPosX);
        movedGroupBlockFirst.setMiddleY(blocksHookedPositionY);
        movedGroupBlockFirst.resetStatus();
        movedGroupBlockSecond.setMiddleX(blocksSecondHookedPosX);
        movedGroupBlockSecond.setMiddleY(blocksHookedPositionY);
        movedGroupBlockSecond.resetStatus();
        movedGroupBlockThird.setMiddleX(blocksThirdHookedPosX);
        movedGroupBlockThird.setMiddleY(blocksHookedPositionY);
        movedGroupBlockThird.resetStatus();
    }

    public void resetGame() {
        board.resetBoard();
        generateGroupBlocks();
        setGroupBlocksPosition();
        gamePlayHeader.setCurrentPointLabel(0);
    }

    private void checkTutorial() {
        if (tutorialStep < TUTORIAL_STEP_FINISH) {
            tutorialStep++;
            switch (tutorialStep) {
                case TUTORIAL_STEP_2:
                    board.setTutorialStep2();
                    tutorialHand.setHomeX(blocksFirstHookedPosX - tutorialHand.getW() / 2);
                    tutorialHand.setTargetX((blocksSecondHookedPosX - blocksFirstHookedPosX) / 2 + blocksFirstHookedPosX);
                    break;
                case TUTORIAL_STEP_3:
                    board.setTutorialStep3();
                    tutorialHand.setHomeX(blocksThirdHookedPosX - tutorialHand.getW() / 2);
                    tutorialHand.setTargetX(blocksThirdHookedPosX - (blocksSecondHookedPosX - blocksFirstHookedPosX));
                    break;
                case TUTORIAL_STEP_FINISH:
                    board.resetBoard();
                    gameConfig.saveTutorialStep(tutorialStep);
                    break;
            }
        }
    }
}
