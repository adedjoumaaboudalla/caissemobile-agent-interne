package caissemobile.com.mediasoft.projet.microfinacaissemobile.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Nationalite;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.database.NationaliteHelper;

/**
 * Created by mediasoft on 09/05/2016.
 */
public class NationaliteDAO  extends DAOBase implements Crud<Nationalite> {

    Context context = null ;

    public NationaliteDAO(Context pContext) {
        super(pContext);
        context = pContext ;
        this.mHandler = NationaliteHelper.getHelper(pContext, DATABASE, VERSION);
        open();
    }

    @Override
    public long add(Nationalite nationalite) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(NationaliteHelper.NUMNATIONALITE, nationalite.getNumnation());
        contentValues.put(NationaliteHelper.LIBELLE, nationalite.getLibelle());

        //contentValues.put(NationaliteHelper.DATECREATION, DAOBase.formatter.format(nationalite.getDatecreation()));

        Long l = mDb.insert(NationaliteHelper._TABLE_NAME, null, contentValues);

        Log.e("DEBUG", String.valueOf(l)) ;
        return l;
    }


    @Override
    public int delete(long id) {
        return  mDb.delete(NationaliteHelper._TABLE_NAME, NationaliteHelper.LIBELLE + " = ? ",new String[] { Long.toString(id)});
    }

    public int clean() {
        return  mDb.delete(NationaliteHelper._TABLE_NAME, null,null);
    }


    @Override
    public int update(Nationalite nationalite) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(NationaliteHelper.NUMNATIONALITE, nationalite.getNumnation());
        contentValues.put(NationaliteHelper.LIBELLE, nationalite.getLibelle());


        int l = mDb.update(NationaliteHelper._TABLE_NAME, contentValues, NationaliteHelper.NUMNATIONALITE + " = ? ", new String[]{Long.toString(nationalite.getNumnation())});


        Log.e("UPDATE", String.valueOf(l)) ;
        return l;
    }


    @Override
    public  Nationalite getOne(long id) {

        Nationalite nationalite = null ;

        Cursor res =  mDb.rawQuery( "select * from "+ NationaliteHelper._TABLE_NAME +" where "+NationaliteHelper.NUMNATIONALITE +" = " + String.valueOf(id), null );

        if (res.moveToFirst()){
            nationalite = new Nationalite();

            nationalite.setNumnation(res.getInt(res.getColumnIndex(NationaliteHelper.NUMNATIONALITE)));
            nationalite.setLibelle(res.getString(res.getColumnIndex(NationaliteHelper.LIBELLE)));
        }

        return nationalite ;
    }

    @Override
    public ArrayList<Nationalite> getAll() {

        ArrayList<Nationalite> nationalites = new ArrayList<Nationalite>();
        Nationalite nationalite = null ;

        Cursor res =  mDb.rawQuery("select * from " + NationaliteHelper._TABLE_NAME + " order by " + NationaliteHelper.LIBELLE + " ", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            nationalite = new Nationalite();


            nationalite.setNumnation(res.getInt(res.getColumnIndex(NationaliteHelper.NUMNATIONALITE)));
            nationalite.setLibelle(res.getString(res.getColumnIndex(NationaliteHelper.LIBELLE)));
            /*
            try {
                nationalite.setDatecreation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(NationaliteHelper.DATECREATION))));
            } catch (ParseException e) {
                e.printStackTrace();
            } */

            nationalites.add(nationalite);
            res.moveToNext();
        }
        return nationalites;
    }

}
