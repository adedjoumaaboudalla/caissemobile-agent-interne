package caissemobile.com.mediasoft.projet.microfinacaissemobile.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mediasoft on 09/05/2016.
 */
public class ProfessionHelper extends SQLiteOpenHelper {

    public static final String NUMPROFESSION = "numprofession";

    public static final String LIBELLE = "libelle";

    public static final String _TABLE_NAME = "profession";

    public static final String _TABLE_CREATE =
            "CREATE TABLE " + _TABLE_NAME + " (" +
                    NUMPROFESSION + " INTEGER PRIMARY KEY, " +
                    LIBELLE + " TEXT);";


    public static final String _TABLE_DROP = "DROP TABLE IF EXISTS " + _TABLE_NAME + ";";

    private static ProfessionHelper instance ;

    private ProfessionHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static synchronized ProfessionHelper getHelper(Context context,String name, int version){
        SQLiteDatabase mDb = null;

        if (instance==null) instance = new ProfessionHelper(context, name, null, version) ;

        try{
            mDb = instance.getWritableDatabase();
            String sql = "SELECT " + NUMPROFESSION + " FROM " + _TABLE_NAME;
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
