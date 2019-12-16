package io.uiza.live.enums;

public enum CameraViewType {
    SurfaceView(0), OpenGL(1);

    private final int value;

    private CameraViewType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
