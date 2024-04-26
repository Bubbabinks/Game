package game.world.worldGenerator;

import game.world.BackgroundType;
import game.world.BlockType;
import game.world.Chunk;
import game.world.worldGenerator.tree.TreeType;
import open_simplex_noise.OpenSimplex2S;

import java.util.ArrayList;
import java.util.Random;

public class EarthLikeGenerator extends WorldGenerator {

    private static final float SCALE_TERRAIN = 3f;

    private final Random random = new Random();
    private final long seed = random.nextLong();

    private final float maxTreeHeight = 7, minTreeHeight = 5, seedOffsetForTree = 5, seedOffsetForWaterPocket = 7, treeRange = maxTreeHeight-minTreeHeight+1;


    public boolean checkForTree(int ax, float frequency) {
        return OpenSimplex2S.noise2(seed+(long)seedOffsetForTree+2, ax, 0)<-frequency && !(OpenSimplex2S.noise2(seed+(long)seedOffsetForTree+2, ax-1, 0)<-frequency);
    }

    private boolean checkForWaterPocket(int ax, float frequency) {
        return OpenSimplex2S.noise2(seed+(long)seedOffsetForWaterPocket+2, ax, 0)<-frequency && !(OpenSimplex2S.noise2(seed+(long)seedOffsetForWaterPocket+2, ax-1, 0)<-frequency);
    }

    public TreeType getTreeType(int ax) {
        TreeType[] treeTypes = TreeType.values();
        return treeTypes[(int)(((OpenSimplex2S.noise2(seed, ax+seedOffsetForTree+5, 0)+1f)/2f)*treeTypes.length)];
    }

    //Will be used later!!
    private int treeHeight(int ax) {
        return ((int)(((OpenSimplex2S.noise2(seed, ax+seedOffsetForTree, 0)+1f)/2f)*treeRange+minTreeHeight))-5;
    }

    private int terrainOffset(int ax) {
        return (int)(OpenSimplex2S.noise2_ImproveX(seed, (double)ax/10d, 0)*SCALE_TERRAIN);
    }

    @Override
    public void generateChunk(Chunk chunk) {
        for (int x = 0; x < Chunk.chunkSize; x++) {
            //Terrain Generation
            int ax = chunk.x+x;
            float terrainOffset = terrainOffset(ax);
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

            //Water Pocket
            float frequency = 0.8f;
            for (int offset = -4; offset<5; offset++) {
                if (checkForWaterPocket(ax+offset, frequency)) {
                    int o = Math.abs(offset);
                    if (o == 0) {
                        for (int i=0; i < 3; i++) {
                            int b = -i-1;
                            if (chunk.y <= b && chunk.y+Chunk.chunkSize > b) {
                                chunk.setBlock(x, b-chunk.y, BlockType.water);
                            }
                        }
                        int b = -4;
                        if (chunk.y <= b && chunk.y+Chunk.chunkSize > b) {
                            chunk.setBlock(x, b-chunk.y, BlockType.sand);
                        }
                    }else if (o == 1 || o == 2) {
                        for (int i=0; i < 2; i++) {
                            int b = -i-1;
                            if (chunk.y <= b && chunk.y+Chunk.chunkSize > b) {
                                chunk.setBlock(x, b-chunk.y, BlockType.water);
                            }
                        }
                        int b = -3;
                        if (chunk.y <= b && chunk.y+Chunk.chunkSize > b) {
                            chunk.setBlock(x, b-chunk.y, BlockType.sand);
                        }
                    }else if (o == 3) {
                        int b = -1;
                        if (chunk.y <= b && chunk.y+Chunk.chunkSize > b) {
                            chunk.setBlock(x, b-chunk.y, BlockType.water);
                        }
                        b = -2;
                        if (chunk.y <= b && chunk.y+Chunk.chunkSize > b) {
                            chunk.setBlock(x, b-chunk.y, BlockType.sand);
                        }
                    }else  {
                        int b = -1;
                        if (chunk.y <= b && chunk.y+Chunk.chunkSize > b) {
                            chunk.setBlock(x, b-chunk.y, BlockType.sand);
                        }

                    }
                }
            }

            //Tree Generation
            frequency = 0.65f;
            for (int offset = -14; offset<15; offset++) {
                if (checkForTree(ax+offset, frequency)) {
                    int treeHeight = treeHeight(ax+offset);
                    ArrayList<BlockType> v = getTreeType(ax+offset).getVertical(offset);
                    if (v != null) {
                        int h = v.size();
                        int o = terrainOffset(ax+offset)+h;
                        for (int y = 0; y<h; y++) {
                            int t = o-y-1-chunk.y+treeHeight;
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
            //Dirt under Tree and extra logs
            if (checkForTree(ax, frequency)) {
                int o = (int)terrainOffset;
                int t = o-1-chunk.y;
                if (t > -1 && t < 100) {
                    chunk.setBlock(x,t, BlockType.dirt);
                }
                for (int i=0; i < treeHeight(ax); i++) {
                    int t2 = t+1+i;
                    if (t2 > -1 && t2 < 100) {
                        chunk.setBlock(x,t2, BlockType.log);
                    }
                }
            }
        }

    }
}
