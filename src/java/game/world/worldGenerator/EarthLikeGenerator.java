package game.world.worldGenerator;

import game.world.BlockType;
import game.world.Chunk;
import not_my_code.OpenSimplex2S;

import java.io.Serializable;
import java.util.Random;

public class EarthLikeGenerator extends WorldGenerator {

    private static final float SCALE_TERRAIN = 3f;

    private long seed = new Random().nextLong();


    @Override
    public void generateChunk(Chunk chunk) {
        for (int x = 0; x < Chunk.chunkSize; x++) {
            int ax = chunk.x+x;
            float terrainOffset = OpenSimplex2S.noise2_ImproveX(seed, (double)ax/10d, 0)*SCALE_TERRAIN;
            for (int y = 0; y < Chunk.chunkSize; y++) {
                int ay = chunk.y+y;
                float m = (float)ay;
                m = -m/4f;
                m = (m - 4f);
                if (m > .075f) {
                    m = .075f;
                }
                float cave = OpenSimplex2S.noise2_ImproveX(seed, (double)ax/15d, (double)ay/15d)+m;
                if (ay < -5+terrainOffset) {
                    if (cave < 0.1) {
                        chunk.setBlock(x, y, BlockType.stone);
                    }
                }else if (ay < -1+terrainOffset) {
                    chunk.setBlock(x, y, BlockType.dirt);
                }else if (ay < 0+terrainOffset) {
                    chunk.setBlock(x, y, BlockType.grass);
                }
            }

        }

    }
}
