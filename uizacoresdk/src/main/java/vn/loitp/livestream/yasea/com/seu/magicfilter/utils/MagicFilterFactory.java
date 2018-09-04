package vn.loitp.livestream.yasea.com.seu.magicfilter.utils;

import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicAmaroFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicAntiqueFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicBeautyFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicBlackCatFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicBrannanFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicBrooklynFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicCalmFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicCoolFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicCrayonFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicEarlyBirdFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicEmeraldFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicEvergreenFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicFreudFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicHealthyFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicHefeFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicHudsonFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicImageAdjustFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicInkwellFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicKevinFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicLatteFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicLomoFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicN1977Filter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicNashvilleFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicNostalgiaFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicPixarFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicRiseFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicRomanceFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicSakuraFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicSierraFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicSketchFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicSkinWhitenFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicSunriseFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicSunsetFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicSutroFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicSweetsFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicTenderFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicToasterFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicValenciaFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicWaldenFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicWarmFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicWhiteCatFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.advanced.MagicXproIIFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.base.MagicLookupFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.base.gpuimage.GPUImageBrightnessFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.base.gpuimage.GPUImageContrastFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.base.gpuimage.GPUImageExposureFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.base.gpuimage.GPUImageFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.base.gpuimage.GPUImageHueFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.base.gpuimage.GPUImageSaturationFilter;
import vn.loitp.livestream.yasea.com.seu.magicfilter.base.gpuimage.GPUImageSharpenFilter;

public class MagicFilterFactory {

    public static GPUImageFilter initFilters(MagicFilterType type) {
        switch (type) {
            case NONE:
                return new GPUImageFilter();
            case WHITECAT:
                return new MagicWhiteCatFilter();
            case BLACKCAT:
                return new MagicBlackCatFilter();
            case SKINWHITEN:
                return new MagicSkinWhitenFilter();
            case BEAUTY:
                return new MagicBeautyFilter();
            case ROMANCE:
                return new MagicRomanceFilter();
            case SAKURA:
                return new MagicSakuraFilter();
            case AMARO:
                return new MagicAmaroFilter();
            case WALDEN:
                return new MagicWaldenFilter();
            case ANTIQUE:
                return new MagicAntiqueFilter();
            case CALM:
                return new MagicCalmFilter();
            case BRANNAN:
                return new MagicBrannanFilter();
            case BROOKLYN:
                return new MagicBrooklynFilter();
            case EARLYBIRD:
                return new MagicEarlyBirdFilter();
            case FREUD:
                return new MagicFreudFilter();
            case HEFE:
                return new MagicHefeFilter();
            case HUDSON:
                return new MagicHudsonFilter();
            case INKWELL:
                return new MagicInkwellFilter();
            case KEVIN:
                return new MagicKevinFilter();
            case LOCKUP:
                return new MagicLookupFilter("");
            case LOMO:
                return new MagicLomoFilter();
            case N1977:
                return new MagicN1977Filter();
            case NASHVILLE:
                return new MagicNashvilleFilter();
            case PIXAR:
                return new MagicPixarFilter();
            case RISE:
                return new MagicRiseFilter();
            case SIERRA:
                return new MagicSierraFilter();
            case SUTRO:
                return new MagicSutroFilter();
            case TOASTER2:
                return new MagicToasterFilter();
            case VALENCIA:
                return new MagicValenciaFilter();
            case XPROII:
                return new MagicXproIIFilter();
            case EVERGREEN:
                return new MagicEvergreenFilter();
            case HEALTHY:
                return new MagicHealthyFilter();
            case COOL:
                return new MagicCoolFilter();
            case EMERALD:
                return new MagicEmeraldFilter();
            case LATTE:
                return new MagicLatteFilter();
            case WARM:
                return new MagicWarmFilter();
            case TENDER:
                return new MagicTenderFilter();
            case SWEETS:
                return new MagicSweetsFilter();
            case NOSTALGIA:
                return new MagicNostalgiaFilter();
            case SUNRISE:
                return new MagicSunriseFilter();
            case SUNSET:
                return new MagicSunsetFilter();
            case CRAYON:
                return new MagicCrayonFilter();
            case SKETCH:
                return new MagicSketchFilter();
            //image adjust
            case BRIGHTNESS:
                return new GPUImageBrightnessFilter();
            case CONTRAST:
                return new GPUImageContrastFilter();
            case EXPOSURE:
                return new GPUImageExposureFilter();
            case HUE:
                return new GPUImageHueFilter();
            case SATURATION:
                return new GPUImageSaturationFilter();
            case SHARPEN:
                return new GPUImageSharpenFilter();
            case IMAGE_ADJUST:
                return new MagicImageAdjustFilter();
            default:
                return null;
        }
    }
}
