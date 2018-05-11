package com.example.nakamoto.fishtool.database;

import android.provider.BaseColumns;

public class AquaContract {

    private AquaContract(){}

    public static class AquaEntry implements BaseColumns{

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

        /* Table Constant */
        public static final String PARAM_TABLE = "parameters";

        /* Columns Constants */
        // TODO: insert all tables
        public static final String _paramID = BaseColumns._ID;
        public static final String PH_COLUMN = "ph";
        public static final String NH3_COLUMN = "nh3";
        public static final String DATE_PARAM_COLUMN = "date";
        public static final String AQUA_FKEY = "aqua_fkey";
















    }
}
