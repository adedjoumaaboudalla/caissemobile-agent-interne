package caissemobile.com.mediasoft.projet.microfinacaissemobile.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mediasoft on 09/05/2016.
 */
public class ZoneHelper extends SQLiteOpenHelper {

    public static final String ID = "id";

    public static final String DESCRIPTION = "libelle_zone";

    public static final String _TABLE_NAME = "zone";

    public static final String _TABLE_CREATE =
            "CREATE TABLE " + _TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DESCRIPTION + " TEXT);";


    public static final String _TABLE_DROP = "DROP TABLE IF EXISTS " + _TABLE_NAME + ";";

    private static ZoneHelper instance ;

    private ZoneHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static synchronized ZoneHelper getHelper(Context context,String name, int version){
        SQLiteDatabase mDb = null;

        if (instance==null) instance = new ZoneHelper(context, name, null, version) ;

        try{
            mDb = instance.getWritableDatabase();
            String sql = "SELECT " + ID + " FROM " + _TABLE_NAME;
            Cursor cursor = mDb.rawQuery(sql, null);
            cursor.close();
        }
        catch(SQLiteException s){
            mDb.execSQL(_TABLE_CREATE);
        }

        return instance ;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(_TABLE_DROP);
        onCreate(db);
    }
}
