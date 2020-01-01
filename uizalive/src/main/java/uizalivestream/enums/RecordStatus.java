package uizalivestream.enums;

import com.pedro.rtplibrary.util.RecordController;

import java.util.HashMap;
import java.util.Map;

public enum RecordStatus {
    STARTED(RecordController.Status.STARTED),
    STOPPED(RecordController.Status.STOPPED),
    RECORDING(RecordController.Status.RECORDING),
    PAUSED(RecordController.Status.PAUSED),
    RESUMED(RecordController.Status.RESUMED);

    private static Map<RecordController.Status, RecordStatus> valueMaps = new HashMap<>();

    static {
        for (RecordStatus s : RecordStatus.values()) {
            valueMaps.put(s.getStatus(), s);
        }
    }

    private final RecordController.Status status;

    RecordStatus(RecordController.Status status) {
        this.status = status;
    }

    public RecordController.Status getStatus() {
        return status;
    }

    public static RecordStatus lookup(RecordController.Status status) {
        return valueMaps.get(status);
    }
}
