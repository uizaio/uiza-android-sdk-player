package testlibuiza.sample.uizavideo.slide2.utils;

import testlibuiza.R;
import vn.loitp.utils.util.AppUtils;

/**
 * Created by thangn on 2/27/17.
 */

public class WWLVideoDataset {
    public static DatasetItem[] datasetItems;

    static {
        int n = 50;
        datasetItems = new DatasetItem[n];
        for (int i = 0; i < n; i++) {
            datasetItems[i] = new DatasetItem(i + 1);
        }
    }

    public static class DatasetItem {
        public int id;
        public String title;
        public String subtitle;
        public String url;

        public DatasetItem(int _id) {
            String _url = "android.resource://" + AppUtils.getAppPackageName() + "/" + R.raw.vid_bigbuckbunny;

            this.id = _id;
            this.title = String.format("This is element #%d", _id);
            this.subtitle = "Loitp " + id;
            this.url = _url;
        }
    }
}
