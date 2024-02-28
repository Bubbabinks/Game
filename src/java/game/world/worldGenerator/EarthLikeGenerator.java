package game.world.worldGenerator;

import game.world.BackgroundType;
import game.world.BlockType;
import game.world.Chunk;
import game.world.worldGenerator.tree.TreeType;
import not_my_code.OpenSimplex2S;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class EarthLikeGenerator extends WorldGenerator {

    private static final float SCALE_TERRAIN = 3f;

    private Random random = new Random();
    private long seed = random.nextLong();

    private float maxTreeHeight = 7, minTreeHeight = 5, seedOffsetForTree = 5, seedOffsetForWaterPocket = 7, treeRange = maxTreeHeight-minTreeHeight+1;


    public boolean checkForTree(int ax, float frequency) {
        return OpenSimplex2S.noise2(seed+(long)seedOffsetForTree+2, ax, 0)<-frequency && !(OpenSimplex2S.noise2(seed+(long)seedOffsetForTree+2, ax-1, 0)<-frequency);
    }

    public TreeType getTreeType(int ax) {
        TreeType[] treeTypes = TreeType.values();
        return treeTypes[(int)(((OpenSimplex2S.noise2(seed, ax+seedOffsetForTree+5, 0)+1f)/2f)*treeTypes.length)];
    }

    private int treeHeight(int ax) {
        return (int)(((OpenSimplex2S.noise2(seed, ax+seedOffsetForTree, 0)+1f)/2f)*treeRange+minTreeHeight);
    }

    private int terrainOffset(int ax) {
        return (int)(OpenSimplex2S.noise2_ImproveX(seed, (double)ax/10d, 0)*SCALE_TERRAIN);
    }

    private boolean waterPocket(int ax) {
        return OpenSimplex2S.noise2(seed+(long)seedOffsetForWaterPocket+2, ax, 0)<-0.85f && !(OpenSimplex2S.noise2(seed+(long)seedOffsetForWaterPocket+2, ax-1, 0)<-0.85f);
    }

    @Override
    public void generateChunk(Chunk chunk) {
        for (int x = 0; x < Chunk.chunkSize; x++) {
            //Terrain Generation
            int ax = chunk.x+x;
            float terrainOffset = terrainOffset(ax);
            boolean waterPocket = waterPocket(ax);
            for (int y = 0; y < Chunk.chunkSize; y++) {
                int ay = chunk.y+y;
                //Background
                if (ay < terrainOffset-100) {
                    chunk.setBackground(x, y, BackgroundType.deep_underground);
                }else if (ay < terrainOffset-5) {
                    chunk.setBackground(x, y, BackgroundType.underground);
                }else {
                    chunk.setBackground(x, y, BackgroundType.sky);
                }

                //Block
                float m = (float)ay;
                m = -m/4f;
                m = (m - 4f);
                if (m > .075f) {
                    m = .075f;
                }
                float cave = OpenSimplex2S.noise2_ImproveX(seed, (double)ax/15d, (double)ay/15d)+m;
                if (ay < -100+terrainOffset) {
                    if (cave < 0.1) {
                        chunk.setBlock(x, y, BlockType.deep_stone);
                    }
                }else if (ay > -5+terrainOffset && ay < terrainOffset &&  waterPocket) {
                      chunk.setBlock(x, y, BlockType.water);
                } else if (ay < -5+terrainOffset) {
                    if (cave < 0.1) {
                        chunk.setBlock(x, y, BlockType.stone);
                    }
                }else if (ay < -1+terrainOffset) {
                    chunk.setBlock(x, y, BlockType.dirt);
                }else if (ay < 0+terrainOffset) {
                    chunk.setBlock(x, y, BlockType.grass);
                }
            }

            float frequency = 0.65f;
            //Tree Generation
            for (int offset = -14; offset<15; offset++) {
                if (checkForTree(ax+offset, frequency)) {
                    ArrayList<BlockType> v = getTreeType(ax+offset).getVertical(offset);
                    if (v != null) {
                        int h = v.size();
                        int o = (int)terrainOffset(ax+offset)+h;
                        for (int y = 0; y<h; y++) {
                            int t = o-y-1-chunk.y;
                            if (t > -1 && t < 100) {
                                BlockType cb = chunk.getBlock(x,t);
                                if (cb == null) {
                                    chunk.setBlock(x,t, v.get(y));
                                }else if (cb == BlockType.leaves) {
                                    if (v.get(y) != null) {
                                        chunk.setBlock(x,t, v.get(y));
                                    }
                                }

                            }
                        }
                    }
                }
            }
            //Dirt under Tree
            if (checkForTree(ax, frequency)) {
                int o = (int)terrainOffset;
                int t = o-1-chunk.y;
                if (t > -1 && t < 100) {
                    chunk.setBlock(x,t, BlockType.dirt);
                }
            }
        }

    }
}
