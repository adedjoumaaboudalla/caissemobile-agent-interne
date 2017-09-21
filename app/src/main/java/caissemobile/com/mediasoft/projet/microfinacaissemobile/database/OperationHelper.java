package caissemobile.com.mediasoft.projet.microfinacaissemobile.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Abdallah on 06/04/2016.
 */
public class OperationHelper  extends SQLiteOpenHelper {

    public static final String ID = "id";

    public static final String DATE = "date";

    public static final String AGENCE = "agence";

    public static final String NUMPIECE = "numpiece";

    public static final String NUMCOMPTE = "numcompte";

    public static final String NUMPRODUIT = "numproduit";

    public static final String NUMMEMBRE = "nummembre";

    public static final String NOM = "nom";

    public static final String PRENOM = "prenom";

    public static final String LIBELLE = "libelle";

    public static final String MONTANT = "nombre";

    public static final String MISE = "mise";

    public static final String TYPEOPERATION = "typeoperation";

    public static final String USER_ID = "user_id";

    public static final String ARCHIVER = "archiver";

    public static final String NUMPIECEDEF = "numpiecedef";

    public static final String SYNC = "sync";

    public static final String TOKEN = "token";

    public static final String _TABLE_NAME = "operation";

    public static final String _TABLE_CREATE =
            "CREATE TABLE " + _TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DATE + " TEXT, " +
                    AGENCE + " TEXT, " +
                    NUMPIECE + " TEXT, " +
                    NUMCOMPTE + " TEXT, " +
                    NUMMEMBRE + " TEXT, " +
                    NUMPRODUIT + " TEXT, " +
                    NOM + " TEXT, " +
                    PRENOM + " TEXT, " +
                    LIBELLE + " TEXT, " +
                    TOKEN + " TEXT, " +
                    NUMPIECEDEF + " TEXT, " +
                    SYNC + " INTEGER, " +
                    MONTANT + " REAL, " +
                    MISE + " REAL, " +
                    TYPEOPERATION + " INT, " +
                    ARCHIVER + " INT, " +
                    USER_ID + " INT);";


    public static final String _TABLE_DROP = "DROP TABLE IF EXISTS " + _TABLE_NAME + ";";

    private static OperationHelper instance ;

    private OperationHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static synchronized OperationHelper getHelper(Context context,String name, int version){
        SQLiteDatabase mDb = null;

        if (instance==null) instance = new OperationHelper(context, name, null, version) ;

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
