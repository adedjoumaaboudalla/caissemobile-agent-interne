package caissemobile.com.mediasoft.projet.microfinacaissemobile.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Abdallah on 06/04/2016.
 */
public class AgenceHelper  extends SQLiteOpenHelper {

    public static final String NUMAGENCE = "id";

    public static final String NOMAGENCE = "nomagence";

    public static final String COMPTE_LIAISON = "compte_liaison";

    public static final String NUMERO_SMS = "numero_sms";

    public static final String IP_DB = "ipdb";

    public static final String LOGIN = "login";

    public static final String PASSWORD_DB = "passworddb";

    public static final String NOM_DB = "nomdb";

    public static final String _TABLE_NAME = "agence";

    public static final String _TABLE_CREATE =
            "CREATE TABLE " + _TABLE_NAME + " (" +
                    NUMAGENCE + " TEXT PRIMARY KEY, " +
                    NOMAGENCE + " TEXT, " +
                    COMPTE_LIAISON + " TEXT, " +
                    NUMERO_SMS + " TEXT, " +
                    IP_DB + " TEXT, " +
                    LOGIN + " TEXT, " +
                    PASSWORD_DB + " TEXT, " +
                    NOM_DB + " TEXT);";


    public static final String _TABLE_DROP = "DROP TABLE IF EXISTS " + _TABLE_NAME + ";";

    private static AgenceHelper instance ;

    private AgenceHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static synchronized AgenceHelper getHelper(Context context,String name, int version){
        SQLiteDatabase mDb = null;

        if (instance==null) instance = new AgenceHelper(context, name, null, version) ;

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
