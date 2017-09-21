package caissemobile.com.mediasoft.projet.microfinacaissemobile.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Abdallah on 06/04/2016.
 */
public class CaissierHelper  extends SQLiteOpenHelper {

    public static final String ID = "id";

    public static final String NOM_PRENOM = "nom_prenom";

    public static final String LOGIN = "login";

    public static final String PASSWORD = "password";

    public static final String CODE_GUICHET = "code_guichet";

    public static final String SOLDE = "solde";

    public static final String RETRAITMAX = "retraitmax";

    public static final String AGENCE_ID = "agence_id";

    public static final String DESCRIPTION = "designation";

    public static final String JOURNEE = "journee";

    public static final String NUMPIECE = "finpiece";
    public static final String DDB = "ddb";
    public static final String DL = "dl";
    public static final String DP = "dp";
    public static final String DI = "di";

    public static final String _TABLE_NAME = "caissier";

    public static final String _TABLE_CREATE =
            "CREATE TABLE " + _TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY, " +
                    NOM_PRENOM + " TEXT, " +
                    DESCRIPTION + " TEXT, " +
                    JOURNEE + " TEXT, " +
                    NUMPIECE + " INTEGER, " +
                    DDB + " TEXT, " +
                    DL + " TEXT, " +
                    DP + " TEXT, " +
                    DI + " TEXT, " +
                    LOGIN + " TEXT, " +
                    PASSWORD + " TEXT, " +
                    AGENCE_ID + " TEXT, " +
                    SOLDE + " REAL, " +
                    RETRAITMAX + " REAL, " +
                    CODE_GUICHET + " TEXT);";


    public static final String _TABLE_DROP = "DROP TABLE IF EXISTS " + _TABLE_NAME + ";";

    private static CaissierHelper instance ;

    private CaissierHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static synchronized CaissierHelper getHelper(Context context,String name, int version){
        SQLiteDatabase mDb = null;

        if (instance==null) instance = new CaissierHelper(context, name, null, version) ;

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
