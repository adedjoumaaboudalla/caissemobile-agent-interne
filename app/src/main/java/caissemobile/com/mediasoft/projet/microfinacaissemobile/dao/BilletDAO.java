package caissemobile.com.mediasoft.projet.microfinacaissemobile.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Billet;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.database.BilletHelper;

/**
 * Created by Mayi on 18/12/2015.
 */
public class BilletDAO extends DAOBase implements Crud<Billet> {

    Context context = null ;

    public BilletDAO(Context pContext) {
        super(pContext);
        context = pContext ;
        this.mHandler = BilletHelper.getHelper(pContext, DATABASE, VERSION);
        open();
    }

    @Override
    public long add(Billet billet) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(BilletHelper.TABLE_KEY, billet.getId());
        contentValues.put(BilletHelper.LIBELLE, billet.getLibelle());
        contentValues.put(BilletHelper.MONTANT, billet.getMontant());
        contentValues.put(BilletHelper.DEVISE, billet.getDevise());

        Long l = mDb.insert(BilletHelper.TABLE_NAME, null, contentValues);

        Log.e("DEBUG", String.valueOf(l)) ;
        return l;
    }

    @Override
    public int delete(long id) {
        return  mDb.delete(BilletHelper.TABLE_NAME, "id = ? ",new String[] { Long.toString(id)});
    }

    public int clean() {
        return  mDb.delete(BilletHelper.TABLE_NAME, null,null);
    }

    @Override
    public int update(Billet billet) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(BilletHelper.TABLE_KEY, billet.getId());
        contentValues.put(BilletHelper.LIBELLE, billet.getLibelle());
        contentValues.put(BilletHelper.MONTANT, billet.getMontant());
        contentValues.put(BilletHelper.DEVISE, billet.getDevise());

        int l = mDb.update(BilletHelper.TABLE_NAME, contentValues, BilletHelper.TABLE_KEY + " = ? ", new String[]{Long.toString(billet.getId())});

        Log.e("DEBUG", String.valueOf(l)) ;
        return l;
    }

    @Override
    public Billet getOne(long id) {
        Billet billet = null ;

        Cursor res =  mDb.rawQuery( "select * from "+ BilletHelper.TABLE_NAME +" where " + BilletHelper.TABLE_KEY  + "=" + id, null );


        if (res.moveToFirst()) {
            billet = new Billet();

            billet.setId(res.getLong(res.getColumnIndex(BilletHelper.TABLE_KEY)));
            billet.setLibelle(res.getString(res.getColumnIndex(BilletHelper.LIBELLE)));
            billet.setMontant(res.getFloat(res.getColumnIndex(BilletHelper.MONTANT)));
            billet.setDevise(res.getString(res.getColumnIndex(BilletHelper.DEVISE)));
        }
        return billet;
    }

    @Override
    public ArrayList<Billet> getAll() {
        ArrayList<Billet> billets = new ArrayList<Billet>();

        Billet billet = null ;
        Cursor res =  mDb.rawQuery( "select * from "+ BilletHelper.TABLE_NAME  + " where "+ BilletHelper.DEVISE + "='XOF'  order by " + BilletHelper.MONTANT + " desc", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            billet = new Billet();

            billet.setId(res.getLong(res.getColumnIndex(BilletHelper.TABLE_KEY)));
            billet.setLibelle(res.getString(res.getColumnIndex(BilletHelper.LIBELLE)));
            billet.setMontant(res.getFloat(res.getColumnIndex(BilletHelper.MONTANT)));
            billet.setDevise(res.getString(res.getColumnIndex(BilletHelper.DEVISE)));

            billets.add(billet) ;
            res.moveToNext();
        }
        return billets;
    }

}
