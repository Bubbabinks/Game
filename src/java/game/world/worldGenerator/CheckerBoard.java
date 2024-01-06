package game.world.worldGenerator;

import game.world.BlockType;
import game.world.Chunk;

public class CheckerBoard extends WorldGenerator {

    @Override
    public void generateChunk(Chunk chunk) {
        Thread thread = new Thread(() -> {
            gc(chunk);
        });
        thread.start();
    }

    private void gc(Chunk chunk) {
        for (int y = 0; y < Chunk.chunkSize; y++) {
            for (int x = 0; x < Chunk.chunkSize; x++) {
                if ((x+y)%2==0) {
                    chunk.setBlock(x, y, BlockType.stone);
                }
            }
        }
    }
}
