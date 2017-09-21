package caissemobile.com.mediasoft.projet.microfinacaissemobile.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mediasoft on 09/05/2016.
 */
public class CategorieHelper extends SQLiteOpenHelper {

    public static final String TABLE_KEY = "id";

    public static final String NUMCATEGORIE = "numCategorie";

    public static final String LIBELLE = "libelleCategorie";

    public static final String LIBELLEFRAIS = "libellefrais";

    public static final String VALEURFRAIS = "valeurfrais";

    public static final String NUMPRODUIT = "numproduit";

    public static final String _TABLE_NAME = "categorie";

    public static final String _TABLE_CREATE =
            "CREATE TABLE " + _TABLE_NAME + " (" +
                    TABLE_KEY + " INTEGER PRIMARY KEY , " +
                    NUMCATEGORIE + " TEXT, " +
                    LIBELLE + " TEXT, " +
                    LIBELLEFRAIS + " TEXT, " +
                    NUMPRODUIT + " TEXT, " +
                    VALEURFRAIS + " REAL);";


    public static final String _TABLE_DROP = "DROP TABLE IF EXISTS " + _TABLE_NAME + ";";

    private static CategorieHelper instance ;

    private CategorieHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static synchronized CategorieHelper getHelper(Context context,String name, int version){
        SQLiteDatabase mDb = null;

        if (instance==null) instance = new CategorieHelper(context, name, null, version) ;

        try{
            mDb = instance.getWritableDatabase();
            String sql = "SELECT " + TABLE_KEY + " FROM " + _TABLE_NAME;
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
