package vn.loitp.livestream.utils;

import java.io.Serializable;

/**
 * Created by loitp on 28/03/2017.
 */

public class Resolution implements Serializable {
    public final int width;
    public final int height;

    public Resolution(int width, int height) {
        this.width = width;
        this.height = height;
    }
}