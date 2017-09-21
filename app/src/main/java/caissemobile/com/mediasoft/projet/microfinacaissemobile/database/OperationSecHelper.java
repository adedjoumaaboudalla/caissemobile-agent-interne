package caissemobile.com.mediasoft.projet.microfinacaissemobile.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Abdallah on 06/04/2016.
 */
public class OperationSecHelper extends SQLiteOpenHelper {

    public static final String ID = "id";

    public static final String NUMMEMBRE = "nummembre";

    public static final String MTE = "mte";

    public static final String OPERATION_ID = "operation_id";

    public static final String IDPERSONNE = "idpersonne";

    public static final String _TABLE_NAME = "operationSec";

    public static final String _TABLE_CREATE =
            "CREATE TABLE " + _TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NUMMEMBRE + " TEXT, " +
                    IDPERSONNE + " TEXT, " +
                    MTE + " REAL, " +
                    OPERATION_ID + " INT);";


    public static final String _TABLE_DROP = "DROP TABLE IF EXISTS " + _TABLE_NAME + ";";

    private static OperationSecHelper instance ;

    private OperationSecHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static synchronized OperationSecHelper getHelper(Context context, String name, int version){
        SQLiteDatabase mDb = null;

        if (instance==null) instance = new OperationSecHelper(context, name, null, version) ;

        try{
            mDb = instance.getWritableDatabase();
            String sql = "SELECT id FROM " + _TABLE_NAME;
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
