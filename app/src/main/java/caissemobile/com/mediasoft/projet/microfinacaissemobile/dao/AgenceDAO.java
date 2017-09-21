package caissemobile.com.mediasoft.projet.microfinacaissemobile.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Agence;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.database.AgenceHelper;


/**
 * Created by Abdallah on 06/04/2016.
 */
public class AgenceDAO extends DAOBase implements Crud<Agence> {

    Context context = null ;

    public AgenceDAO(Context pContext) {
        super(pContext);
        context = pContext ;
        this.mHandler = AgenceHelper.getHelper(pContext, DATABASE, VERSION);
        open();
    }

    @Override
    public long add(Agence agence) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(AgenceHelper.NUMAGENCE, agence.getNumagence());
        contentValues.put(AgenceHelper.NOMAGENCE, agence.getNomagence());
        contentValues.put(AgenceHelper.COMPTE_LIAISON, agence.getCompteLiaison());
        contentValues.put(AgenceHelper.IP_DB, agence.getIpbd());
        contentValues.put(AgenceHelper.NOM_DB, agence.getNombd());
        contentValues.put(AgenceHelper.NUMERO_SMS, agence.getNumeroSms());
        contentValues.put(AgenceHelper.LOGIN, agence.getLogin());
        contentValues.put(AgenceHelper.PASSWORD_DB, agence.getPasswordbd());

        //contentValues.put(AgenceHelper.DATECREATION, DAOBase.formatter.format(agence.getDatecreation()));

        Long l = mDb.insert(AgenceHelper._TABLE_NAME, null, contentValues);

        Log.e("DEBUG", String.valueOf(l)) ;
        return l;
    }


    @Override
    public int delete(long id) {
        return  mDb.delete(AgenceHelper._TABLE_NAME,"id = ? ",new String[] { Long.toString(id)});
    }

    public int delete(String id) {
        return  mDb.delete(AgenceHelper._TABLE_NAME,"id = ? ",new String[] {id});
    }

    public int clean() {
        return  mDb.delete(AgenceHelper._TABLE_NAME, null,null);
    }


    @Override
    public int update(Agence agence) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(AgenceHelper.NUMAGENCE, agence.getNumagence());
        contentValues.put(AgenceHelper.NOMAGENCE, agence.getNomagence());
        contentValues.put(AgenceHelper.COMPTE_LIAISON, agence.getCompteLiaison());
        contentValues.put(AgenceHelper.IP_DB, agence.getIpbd());
        contentValues.put(AgenceHelper.NOM_DB, agence.getNombd());
        contentValues.put(AgenceHelper.NUMERO_SMS, agence.getNumeroSms());
        contentValues.put(AgenceHelper.LOGIN, agence.getLogin());
        contentValues.put(AgenceHelper.PASSWORD_DB, agence.getPasswordbd());

        int l = mDb.update(AgenceHelper._TABLE_NAME, contentValues, "id = ? ", new String[]{agence.getNumagence()});


        Log.e("UPDATE", String.valueOf(l)) ;
        return l;
    }


    @Override
    public  Agence getOne(long id) {

        Agence agence = null ;

        Cursor res =  mDb.rawQuery( "select * from "+ AgenceHelper._TABLE_NAME +" where "+AgenceHelper.NUMAGENCE +" = " + String.valueOf(id), null );

        if (res.moveToFirst()){
            agence = new Agence();

            agence.setNumagence(res.getString(res.getColumnIndex(AgenceHelper.NUMAGENCE)));
            agence.setNomagence(res.getString(res.getColumnIndex(AgenceHelper.NOMAGENCE)));
            agence.setIpbd(res.getString(res.getColumnIndex(AgenceHelper.IP_DB)));
            agence.setNombd(res.getString(res.getColumnIndex(AgenceHelper.NOM_DB)));
            agence.setLogin(res.getString(res.getColumnIndex(AgenceHelper.LOGIN)));
            agence.setPasswordbd(res.getString(res.getColumnIndex(AgenceHelper.PASSWORD_DB)));
            agence.setCompteLiaison(res.getString(res.getColumnIndex(AgenceHelper.COMPTE_LIAISON)));
            agence.setNumeroSms(res.getString(res.getColumnIndex(AgenceHelper.NUMERO_SMS)));
        }

        return agence ;
    }


    public  Agence getOne(String id) {

        Agence agence = null ;

        Cursor res =  mDb.rawQuery( "select * from "+ AgenceHelper._TABLE_NAME +" where "+AgenceHelper.NUMAGENCE +" = '" + id + "'", null );

        if (res.moveToFirst()){
            agence = new Agence();

            agence.setNumagence(res.getString(res.getColumnIndex(AgenceHelper.NUMAGENCE)));
            agence.setNomagence(res.getString(res.getColumnIndex(AgenceHelper.NOMAGENCE)));
            agence.setIpbd(res.getString(res.getColumnIndex(AgenceHelper.IP_DB)));
            agence.setNombd(res.getString(res.getColumnIndex(AgenceHelper.NOM_DB)));
            agence.setLogin(res.getString(res.getColumnIndex(AgenceHelper.LOGIN)));
            agence.setPasswordbd(res.getString(res.getColumnIndex(AgenceHelper.PASSWORD_DB)));
            agence.setCompteLiaison(res.getString(res.getColumnIndex(AgenceHelper.COMPTE_LIAISON)));
            agence.setNumeroSms(res.getString(res.getColumnIndex(AgenceHelper.NUMERO_SMS)));
        }

        return agence ;
    }

    @Override
    public ArrayList<Agence> getAll() {

        ArrayList<Agence> agences = new ArrayList<Agence>();
        Agence agence = null ;

        Cursor res =  mDb.rawQuery("select * from " + AgenceHelper._TABLE_NAME + " order by id desc ", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            agence = new Agence();

            agence.setNumagence(res.getString(res.getColumnIndex(AgenceHelper.NUMAGENCE)));
            agence.setNomagence(res.getString(res.getColumnIndex(AgenceHelper.NOMAGENCE)));
            agence.setIpbd(res.getString(res.getColumnIndex(AgenceHelper.IP_DB)));
            agence.setLogin(res.getString(res.getColumnIndex(AgenceHelper.LOGIN)));
            agence.setPasswordbd(res.getString(res.getColumnIndex(AgenceHelper.PASSWORD_DB)));
            agence.setNombd(res.getString(res.getColumnIndex(AgenceHelper.NOM_DB)));
            agence.setCompteLiaison(res.getString(res.getColumnIndex(AgenceHelper.COMPTE_LIAISON)));
            agence.setNumeroSms(res.getString(res.getColumnIndex(AgenceHelper.NUMERO_SMS)));

            /*
            try {
                agence.setDatecreation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(AgenceHelper.DATECREATION))));
            } catch (ParseException e) {
                e.printStackTrace();
            } */

            agences.add(agence);
            res.moveToNext();
        }
        return agences;
    }

}
