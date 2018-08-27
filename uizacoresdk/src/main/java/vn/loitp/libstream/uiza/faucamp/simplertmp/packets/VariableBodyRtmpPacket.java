package vn.loitp.libstream.uiza.faucamp.simplertmp.packets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import vn.loitp.libstream.uiza.faucamp.simplertmp.amf.AmfBoolean;
import vn.loitp.libstream.uiza.faucamp.simplertmp.amf.AmfData;
import vn.loitp.libstream.uiza.faucamp.simplertmp.amf.AmfDecoder;
import vn.loitp.libstream.uiza.faucamp.simplertmp.amf.AmfNull;
import vn.loitp.libstream.uiza.faucamp.simplertmp.amf.AmfNumber;
import vn.loitp.libstream.uiza.faucamp.simplertmp.amf.AmfString;

/**
 * RTMP packet with a "variable" body structure (i.e. the structure of the
 * body depends on some other state/parameter in the packet.
 * <p>
 * Examples of this type of packet are Command and Data; this abstract class
 * exists mostly for code re-use.
 *
 * @author francois
 */
public abstract class VariableBodyRtmpPacket extends RtmpPacket {

    protected List<AmfData> data;

    public VariableBodyRtmpPacket(RtmpHeader header) {
        super(header);
    }

    public List<AmfData> getData() {
        return data;
    }

    public void addData(String string) {
        addData(new AmfString(string));
    }

    public void addData(double number) {
        addData(new AmfNumber(number));
    }

    public void addData(boolean bool) {
        addData(new AmfBoolean(bool));
    }

    public void addData(AmfData dataItem) {
        if (data == null) {
            data = new ArrayList<>();
        }
        if (dataItem == null) {
            dataItem = new AmfNull();
        }
        data.add(dataItem);
    }

    protected void readVariableData(final InputStream in, int bytesAlreadyRead) throws IOException {
        // ...now read in arguments (if any)
        do {
            AmfData dataItem = AmfDecoder.readFrom(in);
            addData(dataItem);
            bytesAlreadyRead += dataItem.getSize();
        } while (bytesAlreadyRead < header.getPacketLength());
    }

    protected void writeVariableData(final OutputStream out) throws IOException {
        if (data != null) {
            for (AmfData dataItem : data) {
                dataItem.writeTo(out);
            }
        } else {
            // Write a null
            AmfNull.writeNullTo(out);
        }
    }
}
