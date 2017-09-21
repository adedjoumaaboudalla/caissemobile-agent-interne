package caissemobile.com.mediasoft.projet.microfinacaissemobile.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.OperationSec;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.database.OperationSecHelper;

/**
 * Created by Abdallah on 06/04/2016.
 */
public class OperationSecDAO extends DAOBase implements Crud<OperationSec> {

    Context context = null ;
    private SharedPreferences preferences;
    public static final String ENUMPIECE = "E0000";
    public static final String TNUMPIECE = "T0000";

    public OperationSecDAO(Context pContext) {
        super(pContext);
        context = pContext ;
        this.mHandler = OperationSecHelper.getHelper(pContext, DATABASE, VERSION);
        preferences = PreferenceManager.getDefaultSharedPreferences(pContext) ;
        open();
    }

    @Override
    public long add(OperationSec operationSec) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(OperationSecHelper.MTE, operationSec.getMte());
        contentValues.put(OperationSecHelper.NUMMEMBRE, operationSec.getNummembre());
        contentValues.put(OperationSecHelper.IDPERSONNE, operationSec.getIdpersonne());
        contentValues.put(OperationSecHelper.OPERATION_ID, operationSec.getOperation_id());

        Long l = mDb.insert(OperationSecHelper._TABLE_NAME, null, contentValues);

        Log.e("DEBUG", String.valueOf(l)) ;
        return l;
    }


    @Override
    public int delete(long id) {
        return  mDb.delete(OperationSecHelper._TABLE_NAME,"id = ? ",new String[] { Long.toString(id)});
    }

    public int clean() {
        return  mDb.delete(OperationSecHelper._TABLE_NAME, null,null);
    }


    @Override
    public int update(OperationSec operationSec) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(OperationSecHelper.MTE, operationSec.getMte());
        contentValues.put(OperationSecHelper.NUMMEMBRE, operationSec.getNummembre());
        contentValues.put(OperationSecHelper.IDPERSONNE, operationSec.getIdpersonne());
        contentValues.put(OperationSecHelper.OPERATION_ID, operationSec.getOperation_id());

        int l = mDb.update(OperationSecHelper._TABLE_NAME, contentValues, OperationSecHelper.ID + " = ? ", new String[]{Long.toString(operationSec.getId())});


        Log.e("UPDATE", String.valueOf(l)) ;
        return l;
    }


    @Override
    public  OperationSec getOne(long id) {

        OperationSec operationSec = null ;

        Cursor res =  mDb.rawQuery( "select * from "+ OperationSecHelper._TABLE_NAME +" where id="+id+"", null );

        if (res.moveToFirst()){
            operationSec = new OperationSec();

            operationSec.setId(res.getLong(res.getColumnIndex(OperationSecHelper.ID)));
            operationSec.setNummembre(res.getString(res.getColumnIndex(OperationSecHelper.NUMMEMBRE)));
            operationSec.setIdpersonne(res.getString(res.getColumnIndex(OperationSecHelper.IDPERSONNE)));
            operationSec.setMte(res.getFloat(res.getColumnIndex(OperationSecHelper.MTE)));
            operationSec.setOperation_id(res.getLong(res.getColumnIndex(OperationSecHelper.OPERATION_ID)));

        }

        return operationSec ;
    }

    public  OperationSec getLast() {

        OperationSec operationSec = null ;

        //Cursor res =  mDb.rawQuery( "select * from "+ OperationSecHelper._TABLE_NAME +" where " + OperationSecHelper.NUMPIECE + " = '-'  order by id desc", null );
        Cursor res =  mDb.rawQuery( "select * from "+ OperationSecHelper._TABLE_NAME +"  order by id desc", null );

        if (res.moveToFirst()){
            operationSec = new OperationSec();

            operationSec.setId(res.getLong(res.getColumnIndex(OperationSecHelper.ID)));
            operationSec.setNummembre(res.getString(res.getColumnIndex(OperationSecHelper.NUMMEMBRE)));
            operationSec.setIdpersonne(res.getString(res.getColumnIndex(OperationSecHelper.IDPERSONNE)));
            operationSec.setMte(res.getFloat(res.getColumnIndex(OperationSecHelper.MTE)));
            operationSec.setOperation_id(res.getLong(res.getColumnIndex(OperationSecHelper.OPERATION_ID)));

        }

        return operationSec ;
    }

    @Override
    public ArrayList<OperationSec> getAll() {

        ArrayList<OperationSec> operationSecs = new ArrayList<OperationSec>();
        OperationSec operationSec = null ;

        Cursor res =  mDb.rawQuery("select * from " + OperationSecHelper._TABLE_NAME + " order by id desc ", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            operationSec = new OperationSec();

            operationSec.setId(res.getLong(res.getColumnIndex(OperationSecHelper.ID)));
            operationSec.setNummembre(res.getString(res.getColumnIndex(OperationSecHelper.NUMMEMBRE)));
            operationSec.setIdpersonne(res.getString(res.getColumnIndex(OperationSecHelper.IDPERSONNE)));
            operationSec.setMte(res.getFloat(res.getColumnIndex(OperationSecHelper.MTE)));
            operationSec.setOperation_id(res.getLong(res.getColumnIndex(OperationSecHelper.OPERATION_ID)));

            operationSecs.add(operationSec);
            res.moveToNext();
        }
        return operationSecs;
    }



    public ArrayList<OperationSec> getAllByNumOperation(long id) {

        ArrayList<OperationSec> operationSecs = new ArrayList<OperationSec>();
        OperationSec operationSec = null ;

        Cursor res =  mDb.rawQuery("select * from " + OperationSecHelper._TABLE_NAME + " where " + OperationSecHelper.OPERATION_ID +  " = " + id + " order by id desc ", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            operationSec = new OperationSec();

            operationSec.setId(res.getLong(res.getColumnIndex(OperationSecHelper.ID)));
            operationSec.setNummembre(res.getString(res.getColumnIndex(OperationSecHelper.NUMMEMBRE)));
            operationSec.setIdpersonne(res.getString(res.getColumnIndex(OperationSecHelper.IDPERSONNE)));
            operationSec.setMte(res.getFloat(res.getColumnIndex(OperationSecHelper.MTE)));
            operationSec.setOperation_id(res.getLong(res.getColumnIndex(OperationSecHelper.OPERATION_ID)));

            operationSecs.add(operationSec);
            res.moveToNext();
        }
        return operationSecs;
    }



}
