package uizacoresdk.view.dlg.hq;

import android.widget.CheckedTextView;

//https://www.image-engineering.de/library/technotes/991-separating-sd-hd-full-hd-4k-and-8k
public class UZItem {
    private CheckedTextView checkedTextView;
    private String description;
    private Format format;

    public CheckedTextView getCheckedTextView() {
        return checkedTextView;
    }

    public void setCheckedTextView(CheckedTextView checkedTextView) {
        this.checkedTextView = checkedTextView;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public static class Format {
        //https://www.image-engineering.de/library/technotes/991-separating-sd-hd-full-hd-4k-and-8k
        public static final String F_UNKNOW = "Unknow";
        public static final String F_SD = "SD";
        public static final String F_HD = "HD";
        public static final String F_FHD = "FHD";
        public static final String F_UHD = "UHD";
        public static final String F_2K = "2K";
        public static final String F_4K = "4K";

        public static final String P_UNKNOW = "Unknow";
        public static final String P_270 = "270p";
        public static final String P_360 = "360p";
        public static final String P_480 = "480p";
        public static final String P_720 = "720p";
        public static final String P_1080 = "1080p";
        public static final String P_1440 = "1440p";
        public static final String P_2160 = "2160p";

        private String format;
        private String profile;

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public String getProfile() {
            return profile;
        }

        public void setProfile(String profile) {
            this.profile = profile;
        }
    }
}
