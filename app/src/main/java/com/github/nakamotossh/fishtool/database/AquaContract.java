package com.github.nakamotossh.fishtool.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.github.nakamotossh.fishtool.database.AquaContract.AquaEntry.AQUA_TABLE;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.PARAM_TABLE;

public class AquaContract {

    private AquaContract(){}

    /* Autority Setup */
    public static final String CONTENT_AUTORITY = "com.github.nakamotossh.fishtool";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTORITY);
    public static final String PATH_AQUA = AQUA_TABLE;
    public static final String PATH_PARAM = PARAM_TABLE;

    public static class AquaEntry implements BaseColumns{

        /* Content Uri */
        public static final Uri AQUA_CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_AQUA);

        /* MIME types*/
        public static final String AQUA_CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTORITY + "/" + PATH_AQUA;
        public static final String AQUA_CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTORITY + "/" + PATH_AQUA;

        /* Table Constant */
        public static final String AQUA_TABLE = "aquarium";

        /* Columns Constants */
        public static final String _aquaID = BaseColumns._ID;
        public static final String NAME_COLUMN = "name";
        public static final String LITERS_COLUMN = "liters";
        public static final String DATE_AQUA_COLUMN = "date";
        public static final String TYPE_COLUMN = "type";
        public static final String STATUS_COLUMN = "status";
        public static final String LIGHT_COLUMN = "light";
        public static final String CO2_COLUMN = "co2";
        public static final String DOSAGE_COLUMN = "dosage";
        public static final String SUBSTRATE_COLUMN = "substrate";
        public static final String NOTES_COLUMN = "notes";
        public static final String IMAGE_URI_COLUMN = "images_uri";
        public static final String SIZE_COLUMN = "size";
        public static final String FILTER_COLUMN = "filter";
    }

    public static class ParamEntry implements BaseColumns{

        /* Content Uri */
        public static final Uri PARAM_CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PARAM);

        /* MIME Types */
        public static final String PARAM_CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTORITY + "/" + PATH_PARAM;
        public static final String PARAM_CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTORITY + "/" + PATH_PARAM;

        /* Table Constant */
        public static final String PARAM_TABLE = "parameters";

        /* Columns Constants */
        // TODO: insert all tables
        public static final String _paramID = BaseColumns._ID;
        public static final String PH_COLUMN = "ph";
        public static final String NH3_COLUMN = "nh3";
//        public static final String NO3_COLUMN = "no3";
//        public static final String NO4_COLUMN = "no4";
//        public static final String SALT_COLUMN = "salinity";
        public static final String TEMP_COLUMN = "temperature";
//        public static final String PHOSPHATE_COLUMN = "phosphate";
//        public static final String TDS_COLUMN = "tds";
//        public static final String ORP_COLUMN = "orp";
//        public static final String MAGNESIUM_COLUMN = "magnesium";
//        public static final String CALCIUM_COLUMN = "calcium";
//        public static final String ALKALINITY_COLUMN = "alkalinity";
        public static final String DATE_PARAM_COLUMN = "date";
        public static final String AQUA_FKEY = "aqua_fkey";
    }
}
