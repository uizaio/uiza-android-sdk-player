package io.uiza.live.enums;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import com.pedro.encoder.input.gl.render.filters.AnalogTVFilterRender;
import com.pedro.encoder.input.gl.render.filters.AndroidViewFilterRender;
import com.pedro.encoder.input.gl.render.filters.BaseFilterRender;
import com.pedro.encoder.input.gl.render.filters.BasicDeformationFilterRender;
import com.pedro.encoder.input.gl.render.filters.BeautyFilterRender;
import com.pedro.encoder.input.gl.render.filters.BlackFilterRender;
import com.pedro.encoder.input.gl.render.filters.BlurFilterRender;
import com.pedro.encoder.input.gl.render.filters.BrightnessFilterRender;
import com.pedro.encoder.input.gl.render.filters.CartoonFilterRender;
import com.pedro.encoder.input.gl.render.filters.CircleFilterRender;
import com.pedro.encoder.input.gl.render.filters.ColorFilterRender;
import com.pedro.encoder.input.gl.render.filters.ContrastFilterRender;
import com.pedro.encoder.input.gl.render.filters.DuotoneFilterRender;
import com.pedro.encoder.input.gl.render.filters.EarlyBirdFilterRender;
import com.pedro.encoder.input.gl.render.filters.EdgeDetectionFilterRender;
import com.pedro.encoder.input.gl.render.filters.ExposureFilterRender;
import com.pedro.encoder.input.gl.render.filters.FireFilterRender;
import com.pedro.encoder.input.gl.render.filters.GammaFilterRender;
import com.pedro.encoder.input.gl.render.filters.GlitchFilterRender;
import com.pedro.encoder.input.gl.render.filters.GreyScaleFilterRender;
import com.pedro.encoder.input.gl.render.filters.HalftoneLinesFilterRender;
import com.pedro.encoder.input.gl.render.filters.Image70sFilterRender;
import com.pedro.encoder.input.gl.render.filters.LamoishFilterRender;
import com.pedro.encoder.input.gl.render.filters.MoneyFilterRender;
import com.pedro.encoder.input.gl.render.filters.NegativeFilterRender;
import com.pedro.encoder.input.gl.render.filters.NoFilterRender;
import com.pedro.encoder.input.gl.render.filters.PixelatedFilterRender;
import com.pedro.encoder.input.gl.render.filters.PolygonizationFilterRender;
import com.pedro.encoder.input.gl.render.filters.RGBSaturationFilterRender;
import com.pedro.encoder.input.gl.render.filters.RainbowFilterRender;
import com.pedro.encoder.input.gl.render.filters.RippleFilterRender;
import com.pedro.encoder.input.gl.render.filters.RotationFilterRender;
import com.pedro.encoder.input.gl.render.filters.SaturationFilterRender;
import com.pedro.encoder.input.gl.render.filters.SepiaFilterRender;
import com.pedro.encoder.input.gl.render.filters.SharpnessFilterRender;
import com.pedro.encoder.input.gl.render.filters.SnowFilterRender;
import com.pedro.encoder.input.gl.render.filters.SwirlFilterRender;
import com.pedro.encoder.input.gl.render.filters.TemperatureFilterRender;
import com.pedro.encoder.input.gl.render.filters.ZebraFilterRender;
import com.pedro.encoder.input.gl.render.filters.object.BaseObjectFilterRender;
import com.pedro.encoder.input.gl.render.filters.object.GifObjectFilterRender;
import com.pedro.encoder.input.gl.render.filters.object.ImageObjectFilterRender;
import com.pedro.encoder.input.gl.render.filters.object.SurfaceFilterRender;
import com.pedro.encoder.input.gl.render.filters.object.TextObjectFilterRender;

import java.io.IOException;
import java.io.InputStream;


public enum FilterRender {

    None(new NoFilterRender()),
    AnalogTV(new AnalogTVFilterRender()),
    AndroidView(new AndroidViewFilterRender()),
    BasicDeformation(new BasicDeformationFilterRender()),
    Beauty(new BeautyFilterRender()),
    Black(new BlackFilterRender()),
    Blur(new BlurFilterRender()),
    Brightness(new BrightnessFilterRender()),
    Cartoon(new CartoonFilterRender()),
    Circle(new CircleFilterRender()),
    Color(new ColorFilterRender()),
    Contrast(new ContrastFilterRender()),
    Duotone(new DuotoneFilterRender()),
    EarlyBird(new EarlyBirdFilterRender()),
    EdgeDetection(new EdgeDetectionFilterRender()),
    Exposure(new ExposureFilterRender()),
    Fire(new FireFilterRender()),
    Gamma(new GammaFilterRender()),
    GifObject(new GifObjectFilterRender()),
    Glitch(new GlitchFilterRender()),
    GreyScale(new GreyScaleFilterRender()),
    HalftoneLines(new HalftoneLinesFilterRender()),
    Image70s(new Image70sFilterRender()),
    ImageObject(new ImageObjectFilterRender()),
    Lamoish(new LamoishFilterRender()),
    Money(new MoneyFilterRender()),
    Negative(new NegativeFilterRender()),
    Pixelated(new PixelatedFilterRender()),
    Polygonization(new PolygonizationFilterRender()),
    Rainbow(new RainbowFilterRender()),
    RGBSaturation(new RGBSaturationFilterRender()),
    Ripple(new RippleFilterRender()),
    Rotation(new RotationFilterRender()),
    Saturation(new SaturationFilterRender()),
    Sepia(new SepiaFilterRender()),
    Sharpness(new SharpnessFilterRender()),
    Snow(new SnowFilterRender()),
    Swirl(new SwirlFilterRender()),
    Surface(new SurfaceFilterRender()),
    Temperature(new TemperatureFilterRender()),
    TextObject(new TextObjectFilterRender()),
    Zebra(new ZebraFilterRender());

    private final BaseFilterRender filterRender;

    FilterRender(BaseFilterRender filterRender) {
        this.filterRender = filterRender;
    }

    public BaseFilterRender getFilterRender() {
        return filterRender;
    }

    public void setRotation(int rotation) {
        if (filterRender instanceof RotationFilterRender) {
            ((RotationFilterRender) filterRender).setRotation(rotation);
        }
    }

    /**
     * Saturate red, green and blue colors 0% to 100% (0.0f to 1.0f)
     */
    public void setRGBSaturation(float r, float g, float b) {
        if (filterRender instanceof RGBSaturationFilterRender) {
            ((RGBSaturationFilterRender) filterRender).setRGBSaturation(r, g, b);
        }
    }

    @Nullable
    public android.view.Surface getSurface() {
        if (filterRender instanceof SurfaceFilterRender) {
            return ((SurfaceFilterRender) filterRender).getSurface();
        }
        return null;
    }

    public void setScale(float scaleX, float scaleY) {
        if (filterRender instanceof BaseObjectFilterRender) {
            ((BaseObjectFilterRender) filterRender).setScale(scaleX, scaleY);
        }
    }

    public void setText(String text, float textSize, int textColor) {
        if (filterRender instanceof TextObjectFilterRender) {
            ((TextObjectFilterRender) filterRender).setText(text, textSize, textColor);
        }
    }

    public void setDefaultScale(int width, int height) {
        if (filterRender instanceof BaseObjectFilterRender) {
            ((BaseObjectFilterRender) filterRender).setDefaultScale(width, height);
        }
    }

    public void setPosition(Translate translate) {
        if (filterRender instanceof BaseObjectFilterRender) {
            BaseObjectFilterRender objectFilterRender = (BaseObjectFilterRender) filterRender;
            objectFilterRender.setPosition(translate.getTranslateTo());
        }
    }

    public void setImage(Bitmap bitmap) {
        if (filterRender instanceof ImageObjectFilterRender) {
            ImageObjectFilterRender objectFilterRender = (ImageObjectFilterRender) filterRender;
            objectFilterRender.setImage(bitmap);
        }
    }

    public void setGif(InputStream inputStream) throws IOException {
        if (filterRender instanceof GifObjectFilterRender) {
            GifObjectFilterRender gifObjectFilterRender = (GifObjectFilterRender) filterRender;
            gifObjectFilterRender.setGif(inputStream);
        }
    }
}
