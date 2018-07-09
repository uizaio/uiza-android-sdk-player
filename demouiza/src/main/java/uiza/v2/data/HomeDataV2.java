package uiza.v2.data;

import vn.loitp.restapi.uiza.model.v2.listallmetadata.Datum;

/**
 * Created by www.muathu@gmail.com on 11/14/2017.
 */

public class HomeDataV2 {
    private static final HomeDataV2 ourInstance = new HomeDataV2();

    public static HomeDataV2 getInstance() {
        return ourInstance;
    }

    private HomeDataV2() {
    }

    private int currentPosition;

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    //API v2
    private Datum mCurrentDatum;

    public Datum getDatum() {
        return mCurrentDatum;
    }

    public void setDatum(Datum mCurrentDatum) {
        this.mCurrentDatum = mCurrentDatum;
    }

    //End API v2
}
