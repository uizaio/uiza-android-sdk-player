package uiza.activity.data;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by LENOVO on 5/14/2018.
 */

public class EventBusManager {
    public static class MessageEvent {
        private long positionOfPlayer;
        private String entityId;
        private String entityTitle;
        private String entityCover;

        public long getPositionOfPlayer() {
            return positionOfPlayer;
        }

        public void setPositionOfPlayer(long positionOfPlayer) {
            this.positionOfPlayer = positionOfPlayer;
        }

        public String getEntityId() {
            return entityId;
        }

        public void setEntityId(String entityId) {
            this.entityId = entityId;
        }

        public String getEntityTitle() {
            return entityTitle;
        }

        public void setEntityTitle(String entityTitle) {
            this.entityTitle = entityTitle;
        }

        public String getEntityCover() {
            return entityCover;
        }

        public void setEntityCover(String entityCover) {
            this.entityCover = entityCover;
        }
    }

    public static void sendEventClickFullScreenFromService(MessageEvent messageEvent) {
        EventBus.getDefault().post(messageEvent);
    }
}
