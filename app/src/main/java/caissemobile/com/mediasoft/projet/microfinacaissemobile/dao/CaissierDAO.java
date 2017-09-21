package caissemobile.com.mediasoft.projet.microfinacaissemobile.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Caissier;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.database.CaissierHelper;

/**
 * Created by Abdallah on 06/04/2016.
 */
public class CaissierDAO  extends DAOBase implements Crud<Caissier> {

    Context context = null ;

    public CaissierDAO(Context pContext) {
        super(pContext);
        context = pContext ;
        this.mHandler = CaissierHelper.getHelper(pContext, DATABASE, VERSION);
        open();
    }

    @Override
    public long add(Caissier caissier) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(CaissierHelper.ID, caissier.getId());
        contentValues.put(CaissierHelper.NOM_PRENOM, caissier.getNomprenom());
        contentValues.put(CaissierHelper.LOGIN, caissier.getLogin());
        contentValues.put(CaissierHelper.PASSWORD, caissier.getPassword());
        contentValues.put(CaissierHelper.AGENCE_ID, caissier.getAgence_id());
        contentValues.put(CaissierHelper.SOLDE, caissier.getSolde());
        contentValues.put(CaissierHelper.RETRAITMAX, caissier.getRetraitMax());
        contentValues.put(CaissierHelper.CODE_GUICHET, caissier.getCodeguichet());

        contentValues.put(CaissierHelper.DESCRIPTION, caissier.getDescription());
        contentValues.put(CaissierHelper.JOURNEE, caissier.getJournee());
        contentValues.put(CaissierHelper.NUMPIECE, caissier.getNumpiece());
        contentValues.put(CaissierHelper.DDB, caissier.getDdb());
        contentValues.put(CaissierHelper.DL, caissier.getDl());
        contentValues.put(CaissierHelper.DP, caissier.getDp());
        contentValues.put(CaissierHelper.DI, caissier.getDi());

        //contentValues.put(CaissierHelper.DATECREATION, DAOBase.formatter.format(caissier.getDatecreation()));

        Long l = mDb.insert(CaissierHelper._TABLE_NAME, null, contentValues);

        Log.e("DEBUG", String.valueOf(l)) ;
        return l;
    }


    @Override
    public int delete(long id) {
        return  mDb.delete(CaissierHelper._TABLE_NAME,"id = ? ",new String[] { Long.toString(id)});
    }

    public int clean() {
        return  mDb.delete(CaissierHelper._TABLE_NAME, null,null);
    }


    @Override
    public int update(Caissier caissier) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(CaissierHelper.ID, caissier.getId());
        contentValues.put(CaissierHelper.NOM_PRENOM, caissier.getNomprenom());
        contentValues.put(CaissierHelper.LOGIN, caissier.getLogin());
        contentValues.put(CaissierHelper.PASSWORD, caissier.getPassword());
        contentValues.put(CaissierHelper.AGENCE_ID, caissier.getAgence_id());
        contentValues.put(CaissierHelper.SOLDE, caissier.getSolde());
        contentValues.put(CaissierHelper.RETRAITMAX, caissier.getRetraitMax());
        contentValues.put(CaissierHelper.CODE_GUICHET, caissier.getCodeguichet());

        contentValues.put(CaissierHelper.DESCRIPTION, caissier.getDescription());
        contentValues.put(CaissierHelper.JOURNEE, caissier.getJournee());
        contentValues.put(CaissierHelper.NUMPIECE, caissier.getNumpiece());
        contentValues.put(CaissierHelper.DDB, caissier.getDdb());
        contentValues.put(CaissierHelper.DL, caissier.getDl());
        contentValues.put(CaissierHelper.DP, caissier.getDp());
        contentValues.put(CaissierHelper.DI, caissier.getDi());


        int l = mDb.update(CaissierHelper._TABLE_NAME, contentValues, "id = ? ", new String[]{Long.toString(caissier.getId())});


        Log.e("UPDATE", String.valueOf(l)) ;
        return l;
    }


    @Override
    public  Caissier getOne(long id) {

        Caissier caissier = null ;

        Cursor res =  mDb.rawQuery( "select * from "+ CaissierHelper._TABLE_NAME +" where id="+id+"", null );

        if (res.moveToFirst()){
            caissier = new Caissier();

            caissier.setId(res.getLong(res.getColumnIndex(CaissierHelper.ID)));
            caissier.setLogin(res.getString(res.getColumnIndex(CaissierHelper.LOGIN)));
            caissier.setAgence_id(res.getString(res.getColumnIndex(CaissierHelper.AGENCE_ID)));
            caissier.setPassword(res.getString(res.getColumnIndex(CaissierHelper.PASSWORD)));
            caissier.setSolde(res.getFloat(res.getColumnIndex(CaissierHelper.SOLDE)));
            caissier.setRetraitMax(res.getDouble(res.getColumnIndex(CaissierHelper.RETRAITMAX)));
            caissier.setCodeguichet(res.getString(res.getColumnIndex(CaissierHelper.CODE_GUICHET)));

            caissier.setDescription(res.getString(res.getColumnIndex(CaissierHelper.DESCRIPTION)));
            caissier.setJournee(res.getString(res.getColumnIndex(CaissierHelper.JOURNEE)));
            caissier.setNumpiece(res.getInt(res.getColumnIndex(CaissierHelper.NUMPIECE)));
            caissier.setDdb(res.getString(res.getColumnIndex(CaissierHelper.DDB)));
            caissier.setDl(res.getString(res.getColumnIndex(CaissierHelper.DL)));
            caissier.setDp(res.getString(res.getColumnIndex(CaissierHelper.DP)));
            caissier.setDi(res.getString(res.getColumnIndex(CaissierHelper.DI)));
        }

        return caissier ;
    }

    public  Caissier getLast() {

        Caissier caissier = null ;

        Cursor res =  mDb.rawQuery( "select * from "+ CaissierHelper._TABLE_NAME +" order by id desc", null );

        if (res.moveToFirst()){
            caissier = new Caissier();

            caissier.setId(res.getLong(res.getColumnIndex(CaissierHelper.ID)));
            caissier.setNomprenom(res.getString(res.getColumnIndex(CaissierHelper.NOM_PRENOM)));
            caissier.setLogin(res.getString(res.getColumnIndex(CaissierHelper.LOGIN)));
            caissier.setAgence_id(res.getString(res.getColumnIndex(CaissierHelper.AGENCE_ID)));
            caissier.setPassword(res.getString(res.getColumnIndex(CaissierHelper.PASSWORD)));
            caissier.setSolde(res.getFloat(res.getColumnIndex(CaissierHelper.SOLDE)));
            caissier.setRetraitMax(res.getDouble(res.getColumnIndex(CaissierHelper.RETRAITMAX)));
            caissier.setCodeguichet(res.getString(res.getColumnIndex(CaissierHelper.CODE_GUICHET)));

            caissier.setDescription(res.getString(res.getColumnIndex(CaissierHelper.DESCRIPTION)));
            caissier.setJournee(res.getString(res.getColumnIndex(CaissierHelper.JOURNEE)));
            caissier.setNumpiece(res.getInt(res.getColumnIndex(CaissierHelper.NUMPIECE)));
            caissier.setDdb(res.getString(res.getColumnIndex(CaissierHelper.DDB)));
            caissier.setDl(res.getString(res.getColumnIndex(CaissierHelper.DL)));
            caissier.setDp(res.getString(res.getColumnIndex(CaissierHelper.DP)));
            caissier.setDi(res.getString(res.getColumnIndex(CaissierHelper.DI)));
        }

        return caissier ;
    }

    @Override
    public ArrayList<Caissier> getAll() {

        ArrayList<Caissier> caissiers = new ArrayList<Caissier>();
        Caissier caissier = null ;

        Cursor res =  mDb.rawQuery("select * from " + CaissierHelper._TABLE_NAME + " order by id desc ", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            caissier = new Caissier();

            caissier.setId(res.getLong(res.getColumnIndex(CaissierHelper.ID)));
            caissier.setNomprenom(res.getString(res.getColumnIndex(CaissierHelper.NOM_PRENOM)));
            caissier.setLogin(res.getString(res.getColumnIndex(CaissierHelper.LOGIN)));
            caissier.setAgence_id(res.getString(res.getColumnIndex(CaissierHelper.AGENCE_ID)));
            caissier.setPassword(res.getString(res.getColumnIndex(CaissierHelper.PASSWORD)));
            caissier.setSolde(res.getFloat(res.getColumnIndex(CaissierHelper.SOLDE)));
            caissier.setRetraitMax(res.getDouble(res.getColumnIndex(CaissierHelper.RETRAITMAX)));
            caissier.setCodeguichet(res.getString(res.getColumnIndex(CaissierHelper.CODE_GUICHET)));

            caissier.setDescription(res.getString(res.getColumnIndex(CaissierHelper.DESCRIPTION)));
            caissier.setJournee(res.getString(res.getColumnIndex(CaissierHelper.JOURNEE)));
            caissier.setNumpiece(res.getInt(res.getColumnIndex(CaissierHelper.NUMPIECE)));
            caissier.setDdb(res.getString(res.getColumnIndex(CaissierHelper.DDB)));
            caissier.setDl(res.getString(res.getColumnIndex(CaissierHelper.DL)));
            caissier.setDp(res.getString(res.getColumnIndex(CaissierHelper.DP)));
            caissier.setDi(res.getString(res.getColumnIndex(CaissierHelper.DI)));
            /*
            try {
                caissier.setDatecreation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(CaissierHelper.DATECREATION))));
            } catch (ParseException e) {
                e.printStackTrace();
            } */

            caissiers.add(caissier);
            res.moveToNext();
        }
        return caissiers;
    }

}
