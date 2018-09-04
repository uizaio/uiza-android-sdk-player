package vn.loitp.livestream.yasea.com.coremedia.iso.boxes.apple;

import vn.loitp.livestream.yasea.com.googlecode.mp4parser.AbstractContainerBox;

/**
 * undocumented iTunes MetaData Box.
 */
public class AppleItemListBox extends AbstractContainerBox {
    public static final String TYPE = "ilst";

    public AppleItemListBox() {
        super(TYPE);
    }

}
