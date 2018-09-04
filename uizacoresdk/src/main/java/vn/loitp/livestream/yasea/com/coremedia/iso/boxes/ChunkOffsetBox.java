package vn.loitp.livestream.yasea.com.coremedia.iso.boxes;

import vn.loitp.livestream.yasea.com.googlecode.mp4parser.AbstractFullBox;

/**
 * Abstract Chunk Offset Box
 */
public abstract class ChunkOffsetBox extends AbstractFullBox {

    public ChunkOffsetBox(String type) {
        super(type);
    }

    public abstract long[] getChunkOffsets();


    public String toString() {
        return this.getClass().getSimpleName() + "[entryCount=" + getChunkOffsets().length + "]";
    }

}
