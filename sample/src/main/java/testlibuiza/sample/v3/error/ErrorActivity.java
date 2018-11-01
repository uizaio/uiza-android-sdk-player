package testlibuiza.sample.v3.error;

import android.os.Bundle;
import android.widget.TextView;

import testlibuiza.R;
import vn.uiza.core.base.BaseActivity;

public class ErrorActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tvErr = (TextView) findViewById(R.id.tv_err);
        tvErr.setText("Exception(\"Init onError: cannot get detail of entity \" + e.getMessage());//Goi API lâý thông tin entity failed\n" +
                "\n" +
                "\n" +
                "Exception(\"Error: Token streaming cannot be found\")//Không tìm thấy token streaming để call API lấy linkplay\n" +
                "\n" +
                "\n" +
                "Exception(\"This entity has no linkplay\")//Entity này không có linkplay\n" +
                "\n" +
                "\n" +
                "Exception(\"Entity ID cannot be null or empty\")//User init entity id ko hợp lệ\n" +
                "\n" +
                "\n" +
                "Exception(\"Tried to play all linkplay of this entity, but failed\")//1 entity có thể có nhiều linkplay, nếu linkplay này failed sẽ auto switch sang link khác, lỗi này trả về nếu all linkplay đều failed\n" +
                "\n" +
                "\n" +
                "Exception(\"Init failed: Cannot get link play or detail entity\")//Lỗi khi init player\n" +
                "\n" +
                "\n" +
                "Exception(\"Error: Cannot get list all entity.\")//Call API thành công nhưng dữ liệu rỗng\n" +
                "\n" +
                "\n" +
                "Exception(\"Bad Request: The request was unacceptable, often due to missing a required parameter.\")\n" +
                "\n" +
                "\n" +
                "Exception(\"Unauthorized: No valid API key provided.\")\n" +
                "\n" +
                "\n" +
                "Exception(\"Not Found: The requested resource does not exist.\")\n" +
                "\n" +
                "\n" +
                "Exception(\"Unprocessable: The syntax of the request entity is incorrect (often is wrong parameter).\")\n" +
                "\n" +
                "\n" +
                "Exception(\"Internal Server Error: We had a problem with our server. Try again later.\")\n" +
                "\n" +
                "\n" +
                "Exception(\"Service Unavailable: The server is overloaded or down for maintenance.\")\n" +
                "\n" +
                "\n" +
                "Exception(\"No internet connection.\")");
    }

    @Override
    protected boolean setFullScreen() {
        return false;
    }

    @Override
    protected String setTag() {
        return getClass().getSimpleName();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_error;
    }
}
