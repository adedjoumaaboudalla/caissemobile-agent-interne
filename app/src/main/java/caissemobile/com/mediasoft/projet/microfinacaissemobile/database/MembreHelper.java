package caissemobile.com.mediasoft.projet.microfinacaissemobile.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mediasoft on 09/05/2016.
 */
public class MembreHelper  extends SQLiteOpenHelper {

    public static final String ID = "id";

    public static final String DATE = "dateAdhesion";

    public static final String DATENAISS = "dateNaiss";

    public static final String NUMMEMBRE = "nummembre";

    public static final String NOM = "nom";

    public static final String PRENOM = "prenom";

    public static final String SEXE = "sexe";

    public static final String TEL = "telephone";

    public static final String CATEGORIE = "categorie";

    public static final String CODERETRAIT = "code_retrait";

    public static final String PROFESSION = "profession";

    public static final String NATIONALITE = "nationalite";

    public static final String ZONE = "zone";

    public static final String ADRESSE = "adresse";

    public static final String PHOTO = "photo";

    public static final String MONTANT = "montant";
    public static final String SAISI = "saisi";
    public static final String TYPE = "type";
    public static final String NUMGROUPE = "numgroupe";
    public static final String POSTE = "poste";
    public static final String ACTIVITE = "activite";
    public static final String SYNC = "sync";
    public static final String IDPERSONNE = "idpersonne";
    public static final String CODEMEMBRE = "codemembre";

    public static final String _TABLE_NAME = "membre";

    public static final String _TABLE_CREATE =
            "CREATE TABLE " + _TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NUMMEMBRE + " TEXT, " +
                    DATE + " TEXT, " +
                    TYPE + " INTEGER, " +
                    NUMGROUPE + " TEXT, " +
                    POSTE + " TEXT, " +
                    ACTIVITE + " TEXT, " +
                    SYNC + " INTEGER, " +
                    DATENAISS + " TEXT, " +
                    NOM + " INT, " +
                    PRENOM + " TEXT, " +
                    CATEGORIE + " TEXT, " +
                    PROFESSION + " TEXT, " +
                    CODERETRAIT + " TEXT, " +
                    SEXE + " TEXT, " +
                    TEL + " TEXT, " +
                    ZONE + " TEXT, " +
                    PHOTO + " TEXT, " +
                    NATIONALITE + " TEXT, " +
                    CODEMEMBRE + " TEXT, " +
                    IDPERSONNE + " TEXT, " +
                    ADRESSE + " TEXT, " +
                    SAISI + " REAL, " +
                    MONTANT + " REAL" +
                    ");";


    public static final String _TABLE_DROP = "DROP TABLE IF EXISTS " + _TABLE_NAME + ";";

    private static MembreHelper instance ;

    private MembreHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static synchronized MembreHelper getHelper(Context context,String name, int version){
        SQLiteDatabase mDb = null;

        if (instance==null) instance = new MembreHelper(context, name, null, version) ;

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
