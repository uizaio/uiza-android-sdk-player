package vn.loitp.livestream.yasea.com.coremedia.iso.boxes;

import java.nio.ByteBuffer;

import vn.loitp.livestream.yasea.com.coremedia.iso.IsoTypeReader;
import vn.loitp.livestream.yasea.com.coremedia.iso.Utf8;
import vn.loitp.livestream.yasea.com.googlecode.mp4parser.AbstractFullBox;

/**
 *
 */
public class XmlBox extends AbstractFullBox {
    public static final String TYPE = "xml ";
    String xml = "";

    public XmlBox() {
        super(TYPE);
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    @Override
    protected long getContentSize() {
        return 4 + Utf8.utf8StringLengthInBytes(xml);
    }

    @Override
    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        xml = IsoTypeReader.readString(content, content.remaining());
    }

    @Override
    protected void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        byteBuffer.put(Utf8.convert(xml));
    }

    @Override
    public String toString() {
        return "XmlBox{" +
                "xml='" + xml + '\'' +
                '}';
    }
}
