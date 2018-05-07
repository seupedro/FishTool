package com.example.nakamoto.fishtool;

import android.provider.BaseColumns;

public class AquaContract {

    private AquaContract(){}

    public static class AquaEntry implements BaseColumns{

        /* Table Constant */
        public static final String AQUA_TABLE = "aquarium";

        /* Columns Constants */
        public static final String _AquaID = BaseColumns._ID;
        public static final String NAME_AQUA = "name";
        public static final String DATE_AQUA = "date";
        public static final String AQUATYPE_AQUA = "type";
        public static final String STATUS_AQUA = "status";
        public static final String CO2_AQUA = "co2";
        public static final String DOSAGE_AQUA = "dosage";
        public static final String SUBSTRATE_AQUA = "substrate";
        public static final String NOTES_AQUA = "notes";
    }

    public static class ParamEntry implements BaseColumns{

        /* Table Constant */
        public static final String TABLE_NAME_PARAM = "parameters";

        /* Columns Constants */
        // TODO: insert all tables
        public static final String _paramID = BaseColumns._ID;
        public static final String PH_PARAM = "ph";
        public static final String NH3_PARAM = "nh3";
        public static final String DATE_PARAM = "date";
        public static final String AQUA_FKEY = "aqua_fkey";
    }
}
