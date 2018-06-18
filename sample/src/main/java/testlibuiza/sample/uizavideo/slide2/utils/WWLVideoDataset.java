package testlibuiza.sample.uizavideo.slide2.utils;

import java.util.ArrayList;
import java.util.List;

import vn.loitp.core.common.Constants;

/**
 * Created by thangn on 2/27/17.
 */

public class WWLVideoDataset {
    public static List<DatasetItem> datasetItemList = new ArrayList<>();

    static {
        for (int i = 0; i < 50; i++) {
            DatasetItem datasetItem = new DatasetItem();
            datasetItem.setId(i);
            datasetItem.setTitle("This is title " + i);
            datasetItem.setSubtitle("This is subtitle " + i);
            datasetItem.setCover(Constants.URL_IMG_POSTER_SPIDER_MAN);
            datasetItemList.add(datasetItem);
        }
    }

    public static class DatasetItem {
        public int id;
        public String title;
        public String subtitle;
        public String cover;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }
    }
}
