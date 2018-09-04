package vn.loitp.livestream.yasea.com.coremedia.iso.boxes;

import java.nio.ByteBuffer;

import vn.loitp.livestream.yasea.com.googlecode.mp4parser.AbstractBox;

/**
 *
 */
public class ItemDataBox extends AbstractBox {
    public static final String TYPE = "idat";
    ByteBuffer data = ByteBuffer.allocate(0);


    public ItemDataBox() {
        super(TYPE);
    }

    public ByteBuffer getData() {
        return data;
    }

    public void setData(ByteBuffer data) {
        this.data = data;
    }

    @Override
    protected long getContentSize() {
        return data.limit();
    }


    @Override
    public void _parseDetails(ByteBuffer content) {
        data = content.slice();
        content.position(content.position() + content.remaining());
    }

    @Override
    protected void getContent(ByteBuffer byteBuffer) {
        byteBuffer.put(data);
    }
}
