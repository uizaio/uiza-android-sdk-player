package io.uiza.live.interfaces;

import io.uiza.live.enums.RecordStatus;

public interface RecordListener {

    void onStatusChange(RecordStatus status);
}
