package uizacoresdk.view.dlg.hq;

import android.widget.CheckedTextView;

//https://www.image-engineering.de/library/technotes/991-separating-sd-hd-full-hd-4k-and-8k
public class UZItem {
    private CheckedTextView checkedTextView;
    private String format;
    private String description;

    public CheckedTextView getCheckedTextView() {
        return checkedTextView;
    }

    public void setCheckedTextView(CheckedTextView checkedTextView) {
        this.checkedTextView = checkedTextView;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
