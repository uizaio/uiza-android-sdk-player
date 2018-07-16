package uiza.v3.data;

import vn.loitp.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

/**
 * Created by www.muathu@gmail.com on 11/14/2017.
 */

public class HomeDataV3 {
    private static final HomeDataV3 ourInstance = new HomeDataV3();

    public static HomeDataV3 getInstance() {
        return ourInstance;
    }

    private HomeDataV3() {
    }

    private int currentPosition;

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    private Data mCurrentData;

    public Data getData() {
        return mCurrentData;
    }

    public void setData(Data data) {
        this.mCurrentData = data;
    }

    public void clearAll() {
        mCurrentData = null;
        currentPosition = 0;
    }
}
