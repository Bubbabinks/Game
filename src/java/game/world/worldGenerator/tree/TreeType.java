package game.world.worldGenerator.tree;

import game.world.BlockType;
import main.FileManager;

import java.awt.*;
import java.util.ArrayList;

public enum TreeType {

    treeV1("treeV1"),
    treeV2("treeV2"),
    treeV3("treeV3");

    private TreeInfo treeInfo;

    public TreeInfo getTreeInfo() {
        return treeInfo;
    }

    private TreeType(String path) {
        treeInfo = FileManager.loadInternalTree(path);
    }

    public ArrayList<BlockType> getVertical(int x) {
        int hx = treeInfo.baseLocation.x-x;
        if (hx > -1 && hx < 15) {
            ArrayList<BlockType> blocks = new ArrayList<BlockType>();
            for (int ly = 0; ly < 15-(14-treeInfo.baseLocation.y); ly++) {
                blocks.add(treeInfo.blocks[hx][ly]);
            }
            return blocks;
        }
        return null;
    }

}
