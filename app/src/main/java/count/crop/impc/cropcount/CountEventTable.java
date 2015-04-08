package count.crop.impc.cropcount;

import android.provider.BaseColumns;

/**
 * A SQL database for the processed photos
 */
public final class CountEventTable {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    //public CountEventTable() {


   // }

    /* Inner class that defines the table contents */
    public static abstract class CountEvent implements BaseColumns {
        public static final String TABLE_NAME = "Green Count Gallery";

        public static final String COLUMN_NAME_URI = "imageUri";
        public static final String COLUMN_NAME_PERCENTAGE = "percentage";
        public static final String COLUMN_NAME_COUNT = "count";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_INDEX = "index";




    }

}
