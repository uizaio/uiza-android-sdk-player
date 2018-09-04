package vn.loitp.livestream.yasea.com.googlecode.mp4parser.boxes.adobe;

import java.nio.ByteBuffer;

import vn.loitp.livestream.yasea.com.coremedia.iso.boxes.Box;
import vn.loitp.livestream.yasea.com.coremedia.iso.boxes.sampleentry.SampleEntry;

/**
 * Sample Entry as used for Action Message Format tracks.
 */
public class ActionMessageFormat0SampleEntryBox extends SampleEntry {
    public ActionMessageFormat0SampleEntryBox() {
        super("amf0");
    }

    @Override
    protected long getContentSize() {
        long size = 8;
        for (Box box : boxes) {
            size += box.getSize();
        }

        return size;
    }


    @Override
    public void _parseDetails(ByteBuffer content) {
        _parseReservedAndDataReferenceIndex(content);
        _parseChildBoxes(content);
    }

    @Override
    protected void getContent(ByteBuffer byteBuffer) {
        _writeReservedAndDataReferenceIndex(byteBuffer);
        _writeChildBoxes(byteBuffer);
    }
}
