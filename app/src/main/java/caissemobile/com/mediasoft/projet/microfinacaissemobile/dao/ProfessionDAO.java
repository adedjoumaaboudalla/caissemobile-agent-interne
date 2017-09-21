package caissemobile.com.mediasoft.projet.microfinacaissemobile.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Profession;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.database.ProfessionHelper;


/**
 * Created by mediasoft on 09/05/2016.
 */
public class ProfessionDAO extends DAOBase implements Crud<Profession> {

    Context context = null ;

    public ProfessionDAO(Context pContext) {
        super(pContext);
        context = pContext ;
        this.mHandler = ProfessionHelper.getHelper(pContext, DATABASE, VERSION);
        open();
    }

    @Override
    public long add(Profession profession) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(ProfessionHelper.NUMPROFESSION, profession.getNumprofession());
        contentValues.put(ProfessionHelper.LIBELLE, profession.getLibelle());

        //contentValues.put(ProfessionHelper.DATECREATION, DAOBase.formatter.format(profession.getDatecreation()));

        Long l = mDb.insert(ProfessionHelper._TABLE_NAME, null, contentValues);

        Log.e("DEBUG", String.valueOf(l)) ;
        return l;
    }


    @Override
    public int delete(long id) {
        return  mDb.delete(ProfessionHelper._TABLE_NAME, ProfessionHelper.NUMPROFESSION + " = ? ",new String[] { Long.toString(id)});
    }

    public int clean() {
        return  mDb.delete(ProfessionHelper._TABLE_NAME, null,null);
    }


    @Override
    public int update(Profession profession) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(ProfessionHelper.NUMPROFESSION, profession.getNumprofession());
        contentValues.put(ProfessionHelper.LIBELLE, profession.getLibelle());


        int l = mDb.update(ProfessionHelper._TABLE_NAME, contentValues, ProfessionHelper.NUMPROFESSION + " = ? ", new String[]{Long.toString(profession.getNumprofession())});


        Log.e("UPDATE", String.valueOf(l)) ;
        return l;
    }


    @Override
    public  Profession getOne(long id) {

        Profession profession = null ;

        Cursor res =  mDb.rawQuery( "select * from "+ ProfessionHelper._TABLE_NAME +" where "+ProfessionHelper.NUMPROFESSION +" = " + String.valueOf(id), null );

        if (res.moveToFirst()){
            profession = new Profession();

            profession.setNumprofession(res.getInt(res.getColumnIndex(ProfessionHelper.NUMPROFESSION)));
            profession.setLibelle(res.getString(res.getColumnIndex(ProfessionHelper.LIBELLE)));
        }

        return profession ;
    }

    @Override
    public ArrayList<Profession> getAll() {

        ArrayList<Profession> professions = new ArrayList<Profession>();
        Profession profession = null ;

        Cursor res =  mDb.rawQuery("select * from " + ProfessionHelper._TABLE_NAME + " order by " + ProfessionHelper.LIBELLE + " ", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            profession = new Profession();


            profession.setNumprofession(res.getInt(res.getColumnIndex(ProfessionHelper.NUMPROFESSION)));
            profession.setLibelle(res.getString(res.getColumnIndex(ProfessionHelper.LIBELLE)));
            /*
            try {
                profession.setDatecreation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(ProfessionHelper.DATECREATION))));
            } catch (ParseException e) {
                e.printStackTrace();
            } */

            professions.add(profession);
            res.moveToNext();
        }
        return professions;
    }

}
