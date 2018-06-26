package com.github.nakamotossh.fishtool.extras;

import java.text.NumberFormat;
import java.util.Locale;

public final class ParamUtils {

    public static final int PH_PARAM = 0;
    public static final int NITRITE_PARAM = 1;
    public static final int NITRATE_PARAM = 2;
    public static final int AMMONIA_PARAM = 3;
    public static final int SALINITY_PARAM = 4;
    public static final int TEMP_PARAM = 5;

    public static String formatNumber(String value, int formatCode){
        NumberFormat format = NumberFormat.getNumberInstance(Locale.US);
        /* Format according to the param */
        switch(formatCode){
            case ParamUtils.PH_PARAM:
                format.setMinimumIntegerDigits(1);
                format.setMaximumIntegerDigits(1);
                format.setMinimumFractionDigits(1);
                format.setMaximumFractionDigits(1);
                break;

            case ParamUtils.TEMP_PARAM:
                format.setMinimumIntegerDigits(1);
                format.setMaximumIntegerDigits(2);
                format.setMinimumFractionDigits(1);
                format.setMaximumFractionDigits(1);
                break;

            case ParamUtils.AMMONIA_PARAM:
                format.setMinimumIntegerDigits(1);
                format.setMaximumIntegerDigits(1);
                format.setMinimumFractionDigits(3);
                format.setMaximumFractionDigits(3);
                break;

            default:
                throw new IllegalArgumentException("This object cannot be formatted");
        }
        return format.format(Float.valueOf(value));
    }
}
