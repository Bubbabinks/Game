package game.world.worldGenerator;

import game.world.BlockType;
import game.world.Chunk;

import java.io.Serializable;

public class EarthLikeGenerator extends WorldGenerator {


    @Override
    public void generateChunk(Chunk chunk) {
        for (int y = 0; y < Chunk.chunkSize; y++) {
            for (int x = 0; x < Chunk.chunkSize; x++) {
                if (chunk.y+y < -5) {
                    chunk.setBlock(x, y, BlockType.stone);
                }else if (chunk.y+y < -1) {
                    chunk.setBlock(x, y, BlockType.dirt);
                }else if (chunk.y+y < 0) {
                    chunk.setBlock(x, y, BlockType.grass);
                }
            }
        }
    }
}
