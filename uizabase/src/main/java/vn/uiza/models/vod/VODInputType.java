package vn.uiza.models.vod;

import com.google.gson.annotations.SerializedName;

public enum VODInputType {

   @SerializedName("0")
   UNKNOWN(0),
   @SerializedName("1")
   HTTP(1),
   @SerializedName("2")
   S3 (2),
   @SerializedName("3")
   FTP (3),
   @SerializedName("4")
   S3_UIZA (4);

   private final int value;

   VODInputType(int value) {
      this.value = value;
   }

   public int getValue() {
      return value;
   }

}
