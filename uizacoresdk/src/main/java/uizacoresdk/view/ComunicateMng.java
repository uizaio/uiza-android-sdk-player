package uizacoresdk.view;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by loitp on 1/4/2019.
 */

public class ComunicateMng {
    //================================== ACTIVITY TO SERVICE
    public final static String SHOW_MINI_PLAYER_CONTROLLER = "SHOW_MINI_PLAYER_CONTROLLER";
    public final static String HIDE_MINI_PLAYER_CONTROLLER = "HIDE_MINI_PLAYER_CONTROLLER";
    public final static String TOGGLE_MINI_PLAYER_CONTROLLER = "TOGGLE_MINI_PLAYER_CONTROLLER";

    public static class MsgFromActivity {
        private String msg;

        public MsgFromActivity(String msg) {
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    public static class MsgFromActivityPosition extends MsgFromActivity {
        private long position;

        public MsgFromActivityPosition(String msg) {
            super(msg);
        }

        public long getPosition() {
            return position;
        }

        public void setPosition(long position) {
            this.position = position;
        }
    }

    public static class MsgFromActivityIsInitSuccess extends MsgFromActivity {

        public MsgFromActivityIsInitSuccess(String msg) {
            super(msg);
        }

        private boolean isInitSuccess;

        public boolean isInitSuccess() {
            return isInitSuccess;
        }

        public void setInitSuccess(boolean initSuccess) {
            isInitSuccess = initSuccess;
        }
    }

    public static void postFromActivity(MsgFromActivity msg) {
        EventBus.getDefault().post(msg);
    }


    //-----------------------------------------------LOITP---Who want to be my lover?
    //
    //
    //
    //================================== SERVICE TO ACTIVITY
    public static class MsgFromService {
        private String msg;

        public MsgFromService(String msg) {
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    public static class MsgFromServiceIsInitSuccess extends MsgFromService {

        public MsgFromServiceIsInitSuccess(String msg) {
            super(msg);
        }

        private boolean isInitSuccess;

        public boolean isInitSuccess() {
            return isInitSuccess;
        }

        public void setInitSuccess(boolean initSuccess) {
            isInitSuccess = initSuccess;
        }
    }

    public static class MsgFromServicePosition extends MsgFromService {
        private long position;

        public MsgFromServicePosition(String msg) {
            super(msg);
        }

        public long getPosition() {
            return position;
        }

        public void setPosition(long position) {
            this.position = position;
        }
    }

    public static void postFromService(MsgFromService msg) {
        EventBus.getDefault().post(msg);
    }
}