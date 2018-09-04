package vn.loitp.livestream.yasea.com.seu.magicfilter.base;

import android.opengl.GLES20;

import loitp.core.R;
import vn.loitp.livestream.yasea.com.seu.magicfilter.base.gpuimage.GPUImageFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.utils.MagicFilterType;
import vn.loitp.livestream.yasea.com.seu.magicfilter.utils.OpenGLUtils;

public class MagicLookupFilter extends GPUImageFilter {

    protected String table;
    private int mLookupTextureUniform;
    private int mLookupSourceTexture = OpenGLUtils.NO_TEXTURE;

    public MagicLookupFilter(String table) {
        super(MagicFilterType.LOCKUP, R.raw.lookup);
        this.table = table;
    }

    protected void onInit() {
        super.onInit();
        mLookupTextureUniform = GLES20.glGetUniformLocation(getProgram(), "inputImageTexture2");
    }

    protected void onInitialized() {
        super.onInitialized();
        runOnDraw(new Runnable() {
            public void run() {
                mLookupSourceTexture = OpenGLUtils.loadTexture(getContext(), table);
            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();
        int[] texture = new int[]{mLookupSourceTexture};
        GLES20.glDeleteTextures(1, texture, 0);
        mLookupSourceTexture = -1;
    }

    protected void onDrawArraysAfter() {
        if (mLookupSourceTexture != -1) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE3);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        }
    }

    protected void onDrawArraysPre() {
        if (mLookupSourceTexture != -1) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE3);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mLookupSourceTexture);
            GLES20.glUniform1i(mLookupTextureUniform, 3);
        }
    }
}
