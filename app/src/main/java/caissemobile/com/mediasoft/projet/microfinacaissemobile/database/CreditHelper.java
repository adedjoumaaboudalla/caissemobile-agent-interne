package caissemobile.com.mediasoft.projet.microfinacaissemobile.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mediasoft on 13/05/2016.
 */
public class CreditHelper extends SQLiteOpenHelper {


    public static final String ID = "id";

    public static final String NUMCREDIT = "numcredit";
    public static final String NOM = "nom";
    public static final String PRENOM = "prenom";
    public static final String MONTANTPRET = "montantpret";
    public static final String DATEDEBLOCAGE = "datedeblocage";
    public static final String CREDITENCOURS = "creditencours";
    public static final String NUMMEMBRE = "nummembre";
    public static final String MENSUALITE = "mensualite";
    public static final String DUREPRET = "durepret";
    public static final String DATEDEBUT = "datedebu";
    public static final String TAUXAN = "tauxan";
    public static final String SUIVI = "suivi";
    public static final String NOMPRODUIT = "nomproduit";
    public static final String CAPITAL_ATTENDU = "capital_attendu";
    public static final String CAPITAL_RETARD = "capital_retard";
    public static final String INTERET_ATTENDU = "interet_attendu";
    public static final String INTERET_RETARD = "interet_retard";
    public static final String TOTAL_RETARD = "total_retard";
    public static final String RESTE_A_PAYER = "reste_a_payer";
    public static final String PENALITE = "penalite";

    public static final String _TABLE_NAME = "credit";

    public static final String _TABLE_CREATE =
            "CREATE TABLE " + _TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NUMCREDIT + " TEXT, " +
                    NOM + " TEXT , " +
                    PRENOM + " TEXT , " +
                    MONTANTPRET + " REAL , " +
                    DATEDEBLOCAGE + " TEXT , " +
                    CREDITENCOURS + " REAL , " +
                    NUMMEMBRE + " TEXT , " +
                    MENSUALITE + " REAL , " +
                    DUREPRET + " INT , " +
                    DATEDEBUT + " TEXT , " +
                    TAUXAN + " REAL , " +
                    SUIVI + " TEXT , " +
                    NOMPRODUIT + " TEXT , " +
                    RESTE_A_PAYER + " REAL , " +
                    TOTAL_RETARD + " REAL , " +
                    INTERET_RETARD + " REAL , " +
                    CAPITAL_RETARD + " REAL , " +
                    PENALITE + " REAL , " +
                    CAPITAL_ATTENDU + " REAL , " +
                    INTERET_ATTENDU + " REAL);";


    public static final String _TABLE_DROP = "DROP TABLE IF EXISTS " + _TABLE_NAME + ";";

    private static CreditHelper instance ;

    private CreditHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static synchronized CreditHelper getHelper(Context context,String name, int version){
        SQLiteDatabase mDb = null;

        if (instance==null) instance = new CreditHelper(context, name, null, version) ;

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
