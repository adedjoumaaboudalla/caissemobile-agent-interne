package caissemobile.com.mediasoft.projet.microfinacaissemobile.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mediasoft on 13/05/2016.
 */
public class CompteHelper extends SQLiteOpenHelper {

    public static final String ID = "id";

    public static final String NUMMEMBRE = "nummembre";
    public static final String NOM = "nom";
    public static final String PRENOM = "prenom";
    public static final String SEXE = "sexe";
    public static final String NUMCOMPTE = "numcompte";
    public static final String PIN = "pin";
    public static final String NUMPRODUIT = "numproduit";
    public static final String PRODUIT = "produit";
    public static final String MISE = "mise";
    public static final String MISELIBRE = "miselibre";
    public static final String TYPE = "type";
    public static final String DATECREATION = "datecreation";
    public static final String SOLDE = "solde";
    public static final String NBRE_CREDIT = "nbre_credit";
    public static final String SOLDEDISPONIBLE = "solde_disponible";

    public static final String _TABLE_NAME = "compte";

    public static final String _TABLE_CREATE =
            "CREATE TABLE " + _TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NUMMEMBRE + " TEXT, " +
                    NOM + " TEXT, " +
                    PRENOM + " TEXT, " +
                    SEXE + " TEXT, " +
                    NUMCOMPTE + " TEXT, " +
                    NUMPRODUIT + " TEXT, " +
                    PRODUIT + " TEXT, " +
                    PIN + " TEXT, " +
                    MISE + " TEXT, " +
                    MISELIBRE + " TEXT, " +
                    TYPE + " TEXT, " +
                    SOLDE + " REAL, " +
                    NBRE_CREDIT + " INT, " +
                    SOLDEDISPONIBLE + " REAL, " +
                    DATECREATION + " TEXT);";


    public static final String _TABLE_DROP = "DROP TABLE IF EXISTS " + _TABLE_NAME + ";";

    private static CompteHelper instance ;

    private CompteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static synchronized CompteHelper getHelper(Context context,String name, int version){
        SQLiteDatabase mDb = null;

        if (instance==null) instance = new CompteHelper(context, name, null, version) ;

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
