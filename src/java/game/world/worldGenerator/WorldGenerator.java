package game.world.worldGenerator;

import game.world.Chunk;

import java.io.Serializable;

public abstract class WorldGenerator implements Serializable {

    public abstract void generateChunk(Chunk chunk);

}
