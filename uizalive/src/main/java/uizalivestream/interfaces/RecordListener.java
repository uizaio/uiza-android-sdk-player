package uizalivestream.interfaces;


import uizalivestream.enums.RecordStatus;

public interface RecordListener {

    void onStatusChange(RecordStatus status);
}
