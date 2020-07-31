package com.thachpham.blockpuzzlerockstone.gameobject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MovedGroupBlockManager {

    private List<MovedGroupBlock> listBlock;
    private Random random;

    public MovedGroupBlockManager() {
        random = new Random();
        listBlock = new ArrayList<>();
        MovedGroupBlockFactory factory = new MovedGroupBlockFactory();
        listBlock.add(factory.getGroupBlockOne());
        listBlock.add(factory.getGroupBlockTowHorizontal());
        listBlock.add(factory.getGroupBlockTowVertical());
        listBlock.add(factory.getGroupBlockThreeHorizontal());
        listBlock.add(factory.getGroupBlockThreeVertical());
        listBlock.add(factory.getGroupBlockThreeC());
        listBlock.add(factory.getGroupBlockFourTriangle());
        listBlock.add(factory.getGroupBlockFourZ());
        listBlock.add(factory.getGroupBlockFourL());
        listBlock.add(factory.getGroupBlockFourVertical());
        listBlock.add(factory.getGroupBlockFourHorizontal());
        listBlock.add(factory.getGroupBlockFourSqure());
        listBlock.add(factory.getGroupBlockFiveL());
        listBlock.add(factory.getGroupBlockFiveHorizontal());
        listBlock.add(factory.getGroupBlockNine());
    }

    public MovedGroupBlock getGroupBlockById(int id) {
        if (id >= listBlock.size() || id < 0) {
            id = 0;
        }
        return listBlock.get(id);
    }

    public void setBlockMaxWidth(float width) {
        for (MovedGroupBlock groupBlock : listBlock) {
            groupBlock.setSizeMaximum(width);
            groupBlock.setChildSizeNormal(width * 0.4f);
        }
    }

    public MovedGroupBlock getBlockRandom() {
        return getGroupBlockById(random.nextInt(listBlock.size())).clone();
    }

    public MovedGroupBlock getAFixedGroupBlock(Board board) {
        for (int i = listBlock.size() - 1; i > 0; i--) {
            if (board.checkGroupBlockCanPlaceOnBoard(getGroupBlockById(i).getBlockMatrix())) {
                return getGroupBlockById(i).clone();
            }
        }
        return listBlock.get(0).clone();
    }
}
