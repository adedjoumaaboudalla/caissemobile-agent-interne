package caissemobile.com.mediasoft.projet.microfinacaissemobile.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;

import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Compte;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Credit;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.database.CompteHelper;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.database.CreditHelper;


/**
 * Created by mediasoft on 13/05/2016.
 */
public class CompteDAO extends DAOBase implements Crud<Compte> {

    private final CreditDAO creditDAO;
    Context context = null ;

    public CompteDAO(Context pContext) {
        super(pContext);
        context = pContext ;
        creditDAO = new CreditDAO(context) ;
        this.mHandler = CompteHelper.getHelper(pContext, DATABASE, VERSION);
        open();
    }

    @Override
    public long add(Compte compte) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(CompteHelper.NUMCOMPTE, compte.getNumcompte());
        contentValues.put(CompteHelper.NOM, compte.getNom());
        contentValues.put(CompteHelper.PRENOM, compte.getPrenom());
        contentValues.put(CompteHelper.PIN, compte.getPin());
        contentValues.put(CompteHelper.SOLDE, compte.getSolde());
        contentValues.put(CompteHelper.SOLDEDISPONIBLE, compte.getSoldedisponible());
        contentValues.put(CompteHelper.NUMMEMBRE, compte.getNummembre());
        contentValues.put(CompteHelper.SEXE, compte.getSexe());
        contentValues.put(CompteHelper.NUMPRODUIT, compte.getNumProduit());
        contentValues.put(CompteHelper.PRODUIT, compte.getProduit());
        contentValues.put(CompteHelper.MISE, compte.getMise());
        contentValues.put(CompteHelper.MISELIBRE, compte.getMiseLibre());
        contentValues.put(CompteHelper.TYPE, compte.getType());
        contentValues.put(CompteHelper.NBRE_CREDIT, compte.getNbrecredit());
        if (compte.getDatecreation()!=null)contentValues.put(CompteHelper.DATECREATION, DAOBase.formatter6.format(compte.getDatecreation()));

        //contentValues.put(CompteHelper.DATECREATION, DAOBase.formatter.format(compte.getDatecreation()));

        Long l = mDb.insert(CompteHelper._TABLE_NAME, null, contentValues);

        Log.e("DEBUG", String.valueOf(l)) ;
        return l;
    }


    @Override
    public int delete(long id) {
        return  mDb.delete(CompteHelper._TABLE_NAME,"id = ? ",new String[] { Long.toString(id)});
    }

    public int clean() {
        return  mDb.delete(CompteHelper._TABLE_NAME, null,null);
    }


    @Override
    public int update(Compte compte) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(CompteHelper.NUMCOMPTE, compte.getNumcompte());
        contentValues.put(CompteHelper.NOM, compte.getNom());
        contentValues.put(CompteHelper.PRENOM, compte.getPrenom());
        contentValues.put(CompteHelper.SOLDE, compte.getSolde());
        contentValues.put(CompteHelper.PIN, compte.getPin());
        contentValues.put(CompteHelper.SOLDEDISPONIBLE, compte.getSoldedisponible());
        contentValues.put(CompteHelper.NUMMEMBRE, compte.getNummembre());
        contentValues.put(CompteHelper.SEXE, compte.getSexe());
        contentValues.put(CompteHelper.NUMPRODUIT, compte.getNumProduit());
        contentValues.put(CompteHelper.PRODUIT, compte.getProduit());
        contentValues.put(CompteHelper.MISE, compte.getMise());
        contentValues.put(CompteHelper.MISELIBRE, compte.getMiseLibre());
        contentValues.put(CompteHelper.TYPE, compte.getType());
        contentValues.put(CompteHelper.NBRE_CREDIT, compte.getNbrecredit());
        if (compte.getDatecreation()!=null)contentValues.put(CompteHelper.DATECREATION, DAOBase.formatter6.format(compte.getDatecreation()));



        int l = mDb.update(CompteHelper._TABLE_NAME, contentValues, "id = ? ", new String[]{Long.toString(compte.getId())});


        Log.e("UPDATE", String.valueOf(l)) ;
        return l;
    }


    @Override
    public  Compte getOne(long id) {

        Compte compte = null ;
        Log.e("Q","select * from " + CompteHelper._TABLE_NAME + " where " + CompteHelper.ID + " = " + String.valueOf(id));
        Cursor res =  mDb.rawQuery("select * from " + CompteHelper._TABLE_NAME + " where " + CompteHelper.ID + " = " + String.valueOf(id), null);

        if (res.moveToFirst()){
            compte = new Compte();

            compte.setId(res.getLong(res.getColumnIndex(CompteHelper.ID)));
            compte.setNumcompte(res.getString(res.getColumnIndex(CompteHelper.NUMCOMPTE)));
            compte.setNom(res.getString(res.getColumnIndex(CompteHelper.NOM)));
            compte.setPin(res.getString(res.getColumnIndex(CompteHelper.PIN)));
            compte.setPrenom(res.getString(res.getColumnIndex(CompteHelper.PRENOM)));
            compte.setSolde(res.getFloat(res.getColumnIndex(CompteHelper.SOLDE)));
            compte.setSoldedisponible(res.getFloat(res.getColumnIndex(CompteHelper.SOLDEDISPONIBLE)));
            compte.setNummembre(res.getString(res.getColumnIndex(CompteHelper.NUMMEMBRE)));
            compte.setNumProduit(res.getString(res.getColumnIndex(CompteHelper.NUMPRODUIT)));
            compte.setProduit(res.getString(res.getColumnIndex(CompteHelper.PRODUIT)));
            compte.setMise(res.getString(res.getColumnIndex(CompteHelper.MISE)));
            compte.setMiseLibre(res.getString(res.getColumnIndex(CompteHelper.MISELIBRE)));
            compte.setType(res.getString(res.getColumnIndex(CompteHelper.TYPE)));
            compte.setNbrecredit(res.getInt(res.getColumnIndex(CompteHelper.NBRE_CREDIT)));
            compte.setSexe(res.getString(res.getColumnIndex(CompteHelper.SEXE)));


            /*
            try {
                compte.setDatecreation(DAOBase.formatter6.parse(res.getString(res.getColumnIndex(CompteHelper.DATECREATION))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            */

        }

        res.close();

        return compte ;
    }

    public  Compte getOne(String numcompte) {

        Compte compte = null ;

        Log.e("Q","select * from " + CompteHelper._TABLE_NAME + " where " + CompteHelper.NUMCOMPTE + " = " + numcompte);
        Cursor res =  mDb.rawQuery("select * from " + CompteHelper._TABLE_NAME + " where " + CompteHelper.NUMCOMPTE + " = '" + numcompte+"'", null);

        if (res.moveToFirst()){
            compte = new Compte();


            compte.setId(res.getLong(res.getColumnIndex(CompteHelper.ID)));
            compte.setNumcompte(res.getString(res.getColumnIndex(CompteHelper.NUMCOMPTE)));
            compte.setNom(res.getString(res.getColumnIndex(CompteHelper.NOM)));
            compte.setPin(res.getString(res.getColumnIndex(CompteHelper.PIN)));
            compte.setPrenom(res.getString(res.getColumnIndex(CompteHelper.PRENOM)));
            compte.setSolde(res.getFloat(res.getColumnIndex(CompteHelper.SOLDE)));
            compte.setSoldedisponible(res.getFloat(res.getColumnIndex(CompteHelper.SOLDEDISPONIBLE)));
            compte.setNummembre(res.getString(res.getColumnIndex(CompteHelper.NUMMEMBRE)));
            compte.setNumProduit(res.getString(res.getColumnIndex(CompteHelper.NUMPRODUIT)));
            compte.setProduit(res.getString(res.getColumnIndex(CompteHelper.PRODUIT)));
            compte.setMise(res.getString(res.getColumnIndex(CompteHelper.MISE)));
            compte.setMiseLibre(res.getString(res.getColumnIndex(CompteHelper.MISELIBRE)));
            compte.setType(res.getString(res.getColumnIndex(CompteHelper.TYPE)));
            compte.setNbrecredit(res.getInt(res.getColumnIndex(CompteHelper.NBRE_CREDIT)));
            compte.setSexe(res.getString(res.getColumnIndex(CompteHelper.SEXE)));

            Log.e("INTERNE", compte.getProduit()) ;
            try {
                compte.setDatecreation(DAOBase.formatter6.parse(res.getString(res.getColumnIndex(CompteHelper.DATECREATION))));
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }

        res.close();
        return compte ;
    }


    @Override
    public ArrayList<Compte> getAll() {

        ArrayList<Compte> comptes = new ArrayList<Compte>();
        Compte compte = null ;

        Cursor res =  mDb.rawQuery("select * from " + CompteHelper._TABLE_NAME + " order by " + CompteHelper.NUMCOMPTE + " ", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            compte = new Compte();


            compte.setId(res.getLong(res.getColumnIndex(CompteHelper.ID)));
            compte.setNumcompte(res.getString(res.getColumnIndex(CompteHelper.NUMCOMPTE)));
            compte.setNom(res.getString(res.getColumnIndex(CompteHelper.NOM)));
            compte.setPin(res.getString(res.getColumnIndex(CompteHelper.PIN)));
            compte.setPrenom(res.getString(res.getColumnIndex(CompteHelper.PRENOM)));
            compte.setSolde(res.getFloat(res.getColumnIndex(CompteHelper.SOLDE)));
            compte.setSoldedisponible(res.getFloat(res.getColumnIndex(CompteHelper.SOLDEDISPONIBLE)));
            compte.setNummembre(res.getString(res.getColumnIndex(CompteHelper.NUMMEMBRE)));
            compte.setNumProduit(res.getString(res.getColumnIndex(CompteHelper.NUMPRODUIT)));
            compte.setProduit(res.getString(res.getColumnIndex(CompteHelper.PRODUIT)));
            compte.setMise(res.getString(res.getColumnIndex(CompteHelper.MISE)));
            compte.setMiseLibre(res.getString(res.getColumnIndex(CompteHelper.MISELIBRE)));
            compte.setType(res.getString(res.getColumnIndex(CompteHelper.TYPE)));
            compte.setNbrecredit(res.getInt(res.getColumnIndex(CompteHelper.NBRE_CREDIT)));
            compte.setSexe(res.getString(res.getColumnIndex(CompteHelper.SEXE)));
            try {
                compte.setDatecreation(DAOBase.formatter6.parse(res.getString(res.getColumnIndex(CompteHelper.DATECREATION))));
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            comptes.add(compte);
            res.moveToNext();
        }

        res.close();
        return comptes;
    }


    public ArrayList<Compte> getLasts() {

        ArrayList<Compte> comptes = new ArrayList<Compte>();
        Compte compte = null ;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context) ;
        int nb = preferences.getInt("groupemembre",0) ;
        Log.e("NBRE", String.valueOf(nb)) ;


        Cursor res =  mDb.rawQuery("select * from " + CompteHelper._TABLE_NAME + " order by "+  CompteHelper.ID + " desc LIMIT 0," + nb, null);
        res.moveToFirst();

        //if (nb==0) return comptes ;
        while(res.isAfterLast() == false){
            compte = new Compte();

            compte.setId(res.getLong(res.getColumnIndex(CompteHelper.ID)));
            compte.setNumcompte(res.getString(res.getColumnIndex(CompteHelper.NUMCOMPTE)));
            compte.setNom(res.getString(res.getColumnIndex(CompteHelper.NOM)));
            compte.setPin(res.getString(res.getColumnIndex(CompteHelper.PIN)));
            compte.setPrenom(res.getString(res.getColumnIndex(CompteHelper.PRENOM)));
            compte.setSolde(res.getFloat(res.getColumnIndex(CompteHelper.SOLDE)));
            compte.setSoldedisponible(res.getFloat(res.getColumnIndex(CompteHelper.SOLDEDISPONIBLE)));
            compte.setNummembre(res.getString(res.getColumnIndex(CompteHelper.NUMMEMBRE)));
            compte.setNumProduit(res.getString(res.getColumnIndex(CompteHelper.NUMPRODUIT)));
            compte.setProduit(res.getString(res.getColumnIndex(CompteHelper.PRODUIT)));
            compte.setMise(res.getString(res.getColumnIndex(CompteHelper.MISE)));
            compte.setMiseLibre(res.getString(res.getColumnIndex(CompteHelper.MISELIBRE)));
            compte.setType(res.getString(res.getColumnIndex(CompteHelper.TYPE)));
            compte.setNbrecredit(res.getInt(res.getColumnIndex(CompteHelper.NBRE_CREDIT)));
            compte.setSexe(res.getString(res.getColumnIndex(CompteHelper.SEXE)));
            try {
                compte.setDatecreation(DAOBase.formatter6.parse(res.getString(res.getColumnIndex(CompteHelper.DATECREATION))));
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            comptes.add(compte);
            res.moveToNext();
        }
        res.close();
        return comptes;
    }

    public int deleteLasts() {

        ArrayList<Compte> comptes = new ArrayList<Compte>();
        Compte compte = null ;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context) ;
        int nb = preferences.getInt("groupemembre",0) ;

        Cursor res =  mDb.rawQuery("select * from " + CompteHelper._TABLE_NAME + " order by "+  CompteHelper.ID + " desc LIMIT 0," + nb, null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            compte = new Compte();
            compte.setId(res.getLong(res.getColumnIndex(CompteHelper.ID)));
            comptes.add(compte);
            res.moveToNext();
        }
        for (int i = 0; i < comptes.size(); i++) {
            compte = comptes.get(i) ;
            int k = delete(compte.getId()) ;
            Log.e("DELETE", String.valueOf(k)) ;
        }
        preferences.edit().putInt("groupemembre",0).commit() ;
        Log.e("NBRE", String.valueOf(nb)) ;
        return comptes.size();
    }

    public ArrayList<Compte> getAll(int type) {

        ArrayList<Compte> comptes = new ArrayList<Compte>();
        Compte compte = null ;

        String req = "select * from " + CompteHelper._TABLE_NAME + " order by " + CompteHelper.NUMCOMPTE + " " ;
        if (type==1) req = "select * from " + CompteHelper._TABLE_NAME + " where " + CompteHelper.TYPE + " = '" + Compte.TONTINE + "' order by " + CompteHelper.NUMCOMPTE + " " ;
        else if (type==0) req = "select * from " + CompteHelper._TABLE_NAME + " where " + CompteHelper.TYPE + " = '" + Compte.EPARGNE + "' order by " + CompteHelper.NUMCOMPTE + " " ;

        Log.e("DEBUG",req) ;

        Cursor res =  mDb.rawQuery(req, null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            compte = new Compte();


            compte.setId(res.getLong(res.getColumnIndex(CompteHelper.ID)));
            compte.setNumcompte(res.getString(res.getColumnIndex(CompteHelper.NUMCOMPTE)));
            compte.setNom(res.getString(res.getColumnIndex(CompteHelper.NOM)));
            compte.setPrenom(res.getString(res.getColumnIndex(CompteHelper.PRENOM)));
            compte.setPin(res.getString(res.getColumnIndex(CompteHelper.PIN)));
            compte.setSolde(res.getFloat(res.getColumnIndex(CompteHelper.SOLDE)));
            compte.setSoldedisponible(res.getFloat(res.getColumnIndex(CompteHelper.SOLDEDISPONIBLE)));
            compte.setNummembre(res.getString(res.getColumnIndex(CompteHelper.NUMMEMBRE)));
            compte.setNumProduit(res.getString(res.getColumnIndex(CompteHelper.NUMPRODUIT)));
            compte.setProduit(res.getString(res.getColumnIndex(CompteHelper.PRODUIT)));
            compte.setMise(res.getString(res.getColumnIndex(CompteHelper.MISE)));
            compte.setMiseLibre(res.getString(res.getColumnIndex(CompteHelper.MISELIBRE)));
            compte.setType(res.getString(res.getColumnIndex(CompteHelper.TYPE)));
            compte.setNbrecredit(res.getInt(res.getColumnIndex(CompteHelper.NBRE_CREDIT)));
            compte.setSexe(res.getString(res.getColumnIndex(CompteHelper.SEXE)));
            /*
            try {
                compte.setDatecreation(DAOBase.formatter6.parse(res.getString(res.getColumnIndex(CompteHelper.DATECREATION))));
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            */

            comptes.add(compte);
            res.moveToNext();
        }
        res.close();
        return comptes;
    }



    public ArrayList<Compte> getAllRemb(int type) {

        ArrayList<Compte> comptes = new ArrayList<Compte>();
        Compte compte = null ;

        String req = "select * from " + CompteHelper._TABLE_NAME + " order by " + CompteHelper.NUMCOMPTE + " " ;
        if (type==1) req = "select * from " + CompteHelper._TABLE_NAME + " where " + CompteHelper.TYPE + " = '" + Compte.TONTINE + "' and  " + CompteHelper.NBRE_CREDIT + " > 0 order by " + CompteHelper.NUMCOMPTE + " " ;
        else if (type==0) req = "select * from " + CompteHelper._TABLE_NAME + " where " + CompteHelper.TYPE + " = '" + Compte.EPARGNE + "'  and  " + CompteHelper.NBRE_CREDIT + " > 0 order by " + CompteHelper.NUMCOMPTE + " " ;

        Log.e("DEBUG",req) ;

        Cursor res =  mDb.rawQuery(req, null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            compte = new Compte();


            compte.setId(res.getLong(res.getColumnIndex(CompteHelper.ID)));
            compte.setNumcompte(res.getString(res.getColumnIndex(CompteHelper.NUMCOMPTE)));
            compte.setNom(res.getString(res.getColumnIndex(CompteHelper.NOM)));
            compte.setPrenom(res.getString(res.getColumnIndex(CompteHelper.PRENOM)));
            compte.setPin(res.getString(res.getColumnIndex(CompteHelper.PIN)));
            compte.setSolde(res.getFloat(res.getColumnIndex(CompteHelper.SOLDE)));
            compte.setSoldedisponible(res.getFloat(res.getColumnIndex(CompteHelper.SOLDEDISPONIBLE)));
            compte.setNummembre(res.getString(res.getColumnIndex(CompteHelper.NUMMEMBRE)));
            compte.setNumProduit(res.getString(res.getColumnIndex(CompteHelper.NUMPRODUIT)));
            compte.setProduit(res.getString(res.getColumnIndex(CompteHelper.PRODUIT)));
            compte.setMise(res.getString(res.getColumnIndex(CompteHelper.MISE)));
            compte.setMiseLibre(res.getString(res.getColumnIndex(CompteHelper.MISELIBRE)));
            compte.setType(res.getString(res.getColumnIndex(CompteHelper.TYPE)));
            compte.setNbrecredit(res.getInt(res.getColumnIndex(CompteHelper.NBRE_CREDIT)));
            compte.setSexe(res.getString(res.getColumnIndex(CompteHelper.SEXE)));
            /*
            try {
                compte.setDatecreation(DAOBase.formatter6.parse(res.getString(res.getColumnIndex(CompteHelper.DATECREATION))));
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            */

            comptes.add(compte);
            res.moveToNext();
        }
        res.close();
        return comptes;
    }


}
