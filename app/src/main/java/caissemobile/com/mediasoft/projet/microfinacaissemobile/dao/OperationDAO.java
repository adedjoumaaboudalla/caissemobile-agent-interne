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

import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Operation;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.database.OperationHelper;

/**
 * Created by Abdallah on 06/04/2016.
 */
public class OperationDAO extends DAOBase implements Crud<Operation> {

    Context context = null ;
    private SharedPreferences preferences;
    public static final String ENUMPIECE = "E0000";
    public static final String TNUMPIECE = "T0000";

    public OperationDAO(Context pContext) {
        super(pContext);
        context = pContext ;
        this.mHandler = OperationHelper.getHelper(pContext, DATABASE, VERSION);
        preferences = PreferenceManager.getDefaultSharedPreferences(pContext) ;
        open();
    }

    @Override
    public long add(Operation operation) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(OperationHelper.AGENCE, operation.getAgence());
        contentValues.put(OperationHelper.LIBELLE, operation.getLibelle());
        contentValues.put(OperationHelper.MONTANT, operation.getMontant());
        contentValues.put(OperationHelper.MISE, operation.getMise());
        contentValues.put(OperationHelper.NOM, operation.getNom());
        contentValues.put(OperationHelper.NUMCOMPTE, operation.getNumcompte());
        contentValues.put(OperationHelper.NUMMEMBRE, operation.getNummenbre());
        contentValues.put(OperationHelper.NUMPRODUIT, operation.getNumproduit());
        contentValues.put(OperationHelper.PRENOM, operation.getPrenom());
        contentValues.put(OperationHelper.TYPEOPERATION, operation.getTypeoperation());
        contentValues.put(OperationHelper.NUMPIECE, operation.getNumpiece());
        contentValues.put(OperationHelper.USER_ID, operation.getUser_id());
        contentValues.put(OperationHelper.ARCHIVER, operation.getArchiver());

        contentValues.put(OperationHelper.NUMPIECEDEF, operation.getNumpicedef());
        contentValues.put(OperationHelper.SYNC, operation.getSync());


        try {
            Date date = DAOBase.formatterj.parse(preferences.getString("dateouvert", "")) ;
            contentValues.put(OperationHelper.DATE,DAOBase.formatter2.format(date));
            Log.e("DAY", String.valueOf(DAOBase.formatter2.format(date))) ;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Long l = mDb.insert(OperationHelper._TABLE_NAME, null, contentValues);

        Log.e("DEBUG", String.valueOf(l)) ;
        return l;
    }


    @Override
    public int delete(long id) {
        return  mDb.delete(OperationHelper._TABLE_NAME,"id = ? ",new String[] { Long.toString(id)});
    }

    public int deleteNotSend(long id) {
        return  mDb.delete(OperationHelper._TABLE_NAME,OperationHelper.NUMPIECE + " = '-' ",null);
    }

    public int clean() {
        return  mDb.delete(OperationHelper._TABLE_NAME, null,null);
    }


    @Override
    public int update(Operation operation) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(OperationHelper.ID, operation.getId());
        contentValues.put(OperationHelper.AGENCE, operation.getAgence());
        contentValues.put(OperationHelper.LIBELLE, operation.getLibelle());
        contentValues.put(OperationHelper.MONTANT, operation.getMontant());
        contentValues.put(OperationHelper.MISE, operation.getMise());
        contentValues.put(OperationHelper.NOM, operation.getNom());
        contentValues.put(OperationHelper.NUMCOMPTE, operation.getNumcompte());
        contentValues.put(OperationHelper.NUMMEMBRE, operation.getNummenbre());
        contentValues.put(OperationHelper.NUMPRODUIT, operation.getNumproduit());
        contentValues.put(OperationHelper.PRENOM, operation.getPrenom());
        contentValues.put(OperationHelper.TYPEOPERATION, operation.getTypeoperation());
        contentValues.put(OperationHelper.NUMPIECE, operation.getNumpiece());
        contentValues.put(OperationHelper.USER_ID, operation.getUser_id());
        contentValues.put(OperationHelper.ARCHIVER, operation.getArchiver());

        contentValues.put(OperationHelper.NUMPIECEDEF, operation.getNumpicedef());
        contentValues.put(OperationHelper.SYNC, operation.getSync());
        try {
            Date date = DAOBase.formatterj.parse(preferences.getString("dateouvert", "")) ;
            contentValues.put(OperationHelper.DATE,DAOBase.formatter2.format(date));
            Log.e("DAY", String.valueOf(DAOBase.formatter2.format(date))) ;
        } catch (ParseException e) {
            e.printStackTrace();
        }


        int l = mDb.update(OperationHelper._TABLE_NAME, contentValues, OperationHelper.ID + " = ? ", new String[]{Long.toString(operation.getId())});


        Log.e("UPDATE", String.valueOf(l)) ;
        return l;
    }



    public int archiver() {

        ContentValues contentValues = new ContentValues();

        contentValues.put(OperationHelper.ARCHIVER, 1);

        int l = mDb.update(OperationHelper._TABLE_NAME, contentValues, null, null);


        Log.e("ARCHIVE", String.valueOf(l)) ;
        return l;
    }


    @Override
    public  Operation getOne(long id) {

        Operation operation = null ;

        Cursor res =  mDb.rawQuery( "select * from "+ OperationHelper._TABLE_NAME +" where id="+id+"", null );

        if (res.moveToFirst()){
            operation = new Operation();

            operation.setId(res.getLong(res.getColumnIndex(OperationHelper.ID)));
            operation.setAgence(res.getString(res.getColumnIndex(OperationHelper.AGENCE)));
            operation.setLibelle(res.getString(res.getColumnIndex(OperationHelper.LIBELLE)));
            operation.setMise(res.getFloat(res.getColumnIndex(OperationHelper.MISE)));
            operation.setMontant(res.getFloat(res.getColumnIndex(OperationHelper.MONTANT)));
            operation.setNom(res.getString(res.getColumnIndex(OperationHelper.NOM)));
            operation.setPrenom(res.getString(res.getColumnIndex(OperationHelper.PRENOM)));
            operation.setNumcompte(res.getString(res.getColumnIndex(OperationHelper.NUMCOMPTE)));
            operation.setToken(res.getString(res.getColumnIndex(OperationHelper.TOKEN)));
            operation.setNumproduit(res.getString(res.getColumnIndex(OperationHelper.NUMPRODUIT)));
            operation.setNummenbre(res.getString(res.getColumnIndex(OperationHelper.NUMMEMBRE)));
            operation.setNumpiece(res.getString(res.getColumnIndex(OperationHelper.NUMPIECE)));
            operation.setTypeoperation(res.getInt(res.getColumnIndex(OperationHelper.TYPEOPERATION)));
            operation.setUser_id(res.getLong(res.getColumnIndex(OperationHelper.USER_ID)));
            operation.setArchiver(res.getInt(res.getColumnIndex(OperationHelper.ARCHIVER)));

            operation.setSync(res.getInt(res.getColumnIndex(OperationHelper.SYNC)));
            operation.setNumpicedef(res.getString(res.getColumnIndex(OperationHelper.NUMPIECEDEF)));

            try {
                operation.setDateoperation(DAOBase.formatter2.parse(res.getString(res.getColumnIndex(OperationHelper.DATE))));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return operation ;
    }

    public  Operation getLast() {

        Operation operation = null ;

        //Cursor res =  mDb.rawQuery( "select * from "+ OperationHelper._TABLE_NAME +" where " + OperationHelper.NUMPIECE + " = '-'  order by id desc", null );
        Cursor res =  mDb.rawQuery( "select * from "+ OperationHelper._TABLE_NAME +"  order by id desc", null );

        if (res.moveToFirst()){
            operation = new Operation();

            operation.setId(res.getLong(res.getColumnIndex(OperationHelper.ID)));
            operation.setAgence(res.getString(res.getColumnIndex(OperationHelper.AGENCE)));
            operation.setLibelle(res.getString(res.getColumnIndex(OperationHelper.LIBELLE)));
            operation.setMise(res.getFloat(res.getColumnIndex(OperationHelper.MISE)));
            operation.setMontant(res.getFloat(res.getColumnIndex(OperationHelper.MONTANT)));
            operation.setNom(res.getString(res.getColumnIndex(OperationHelper.NOM)));
            operation.setPrenom(res.getString(res.getColumnIndex(OperationHelper.PRENOM)));
            operation.setNumproduit(res.getString(res.getColumnIndex(OperationHelper.NUMPRODUIT)));
            operation.setNumcompte(res.getString(res.getColumnIndex(OperationHelper.NUMCOMPTE)));
            operation.setNummenbre(res.getString(res.getColumnIndex(OperationHelper.NUMMEMBRE)));
            operation.setNumpiece(res.getString(res.getColumnIndex(OperationHelper.NUMPIECE)));
            operation.setToken(res.getString(res.getColumnIndex(OperationHelper.TOKEN)));
            operation.setTypeoperation(res.getInt(res.getColumnIndex(OperationHelper.TYPEOPERATION)));
            operation.setUser_id(res.getLong(res.getColumnIndex(OperationHelper.USER_ID)));
            operation.setArchiver(res.getInt(res.getColumnIndex(OperationHelper.ARCHIVER)));


            operation.setSync(res.getInt(res.getColumnIndex(OperationHelper.SYNC)));
            operation.setNumpicedef(res.getString(res.getColumnIndex(OperationHelper.NUMPIECEDEF)));

            try {
                operation.setDateoperation(DAOBase.formatter2.parse(res.getString(res.getColumnIndex(OperationHelper.DATE))));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return operation ;
    }

    @Override
    public ArrayList<Operation> getAll() {

        ArrayList<Operation> operations = new ArrayList<Operation>();
        Operation operation = null ;

        Cursor res =  mDb.rawQuery("select * from " + OperationHelper._TABLE_NAME + " WHERE "  + OperationHelper.ARCHIVER + " = 0 order by " + OperationHelper.ID + " desc ", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            operation = new Operation();

            operation.setId(res.getLong(res.getColumnIndex(OperationHelper.ID)));
            operation.setAgence(res.getString(res.getColumnIndex(OperationHelper.AGENCE)));
            operation.setLibelle(res.getString(res.getColumnIndex(OperationHelper.LIBELLE)));
            operation.setMise(res.getFloat(res.getColumnIndex(OperationHelper.MISE)));
            operation.setMontant(res.getFloat(res.getColumnIndex(OperationHelper.MONTANT)));
            operation.setNom(res.getString(res.getColumnIndex(OperationHelper.NOM)));
            operation.setPrenom(res.getString(res.getColumnIndex(OperationHelper.PRENOM)));
            operation.setNumproduit(res.getString(res.getColumnIndex(OperationHelper.NUMPRODUIT)));
            operation.setNumcompte(res.getString(res.getColumnIndex(OperationHelper.NUMCOMPTE)));
            operation.setNummenbre(res.getString(res.getColumnIndex(OperationHelper.NUMMEMBRE)));
            operation.setNumpiece(res.getString(res.getColumnIndex(OperationHelper.NUMPIECE)));
            operation.setToken(res.getString(res.getColumnIndex(OperationHelper.TOKEN)));
            operation.setTypeoperation(res.getInt(res.getColumnIndex(OperationHelper.TYPEOPERATION)));
            operation.setUser_id(res.getLong(res.getColumnIndex(OperationHelper.USER_ID)));
            operation.setArchiver(res.getInt(res.getColumnIndex(OperationHelper.ARCHIVER)));

            operation.setSync(res.getInt(res.getColumnIndex(OperationHelper.SYNC)));
            operation.setNumpicedef(res.getString(res.getColumnIndex(OperationHelper.NUMPIECEDEF)));
            try {
                operation.setDateoperation(DAOBase.formatter2.parse(res.getString(res.getColumnIndex(OperationHelper.DATE))));
            } catch (Exception e) {
                e.printStackTrace();
            }


            Log.e("DAY", String.valueOf(DAOBase.formatter2.format(operation.getDateoperation()))) ;
            operations.add(operation);
            res.moveToNext();
        }
        return operations;
    }

    public ArrayList<Operation> getNonSync() {

        ArrayList<Operation> operations = new ArrayList<Operation>();
        Operation operation = null ;

        Cursor res =  mDb.rawQuery("select * from " + OperationHelper._TABLE_NAME + " where "  + OperationHelper.SYNC +  " = 0 order by id desc ", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            operation = new Operation();

            operation.setId(res.getLong(res.getColumnIndex(OperationHelper.ID)));
            operation.setAgence(res.getString(res.getColumnIndex(OperationHelper.AGENCE)));
            operation.setLibelle(res.getString(res.getColumnIndex(OperationHelper.LIBELLE)));
            operation.setMise(res.getFloat(res.getColumnIndex(OperationHelper.MISE)));
            operation.setMontant(res.getFloat(res.getColumnIndex(OperationHelper.MONTANT)));
            operation.setNom(res.getString(res.getColumnIndex(OperationHelper.NOM)));
            operation.setPrenom(res.getString(res.getColumnIndex(OperationHelper.PRENOM)));
            operation.setNumproduit(res.getString(res.getColumnIndex(OperationHelper.NUMPRODUIT)));
            operation.setNumcompte(res.getString(res.getColumnIndex(OperationHelper.NUMCOMPTE)));
            operation.setNummenbre(res.getString(res.getColumnIndex(OperationHelper.NUMMEMBRE)));
            operation.setNumpiece(res.getString(res.getColumnIndex(OperationHelper.NUMPIECE)));
            operation.setToken(res.getString(res.getColumnIndex(OperationHelper.TOKEN)));
            operation.setTypeoperation(res.getInt(res.getColumnIndex(OperationHelper.TYPEOPERATION)));
            operation.setUser_id(res.getLong(res.getColumnIndex(OperationHelper.USER_ID)));
            operation.setArchiver(res.getInt(res.getColumnIndex(OperationHelper.ARCHIVER)));

            operation.setSync(res.getInt(res.getColumnIndex(OperationHelper.SYNC)));
            operation.setNumpicedef(res.getString(res.getColumnIndex(OperationHelper.NUMPIECEDEF)));
            try {
                operation.setDateoperation(DAOBase.formatter2.parse(res.getString(res.getColumnIndex(OperationHelper.DATE))));
            } catch (Exception e) {
                e.printStackTrace();
            }

            operations.add(operation);
            res.moveToNext();
        }
        return operations;
    }


    public ArrayList<Operation> getOpCaisse() {

        ArrayList<Operation> operations = new ArrayList<Operation>();
        Operation operation = null ;

        Cursor res =  mDb.rawQuery("select * from " + OperationHelper._TABLE_NAME + " where    "  + OperationHelper.ARCHIVER +  " = 0 AND " + OperationHelper.TYPEOPERATION + " < 3 and (" + OperationHelper.NUMPIECE +  " = '" + ENUMPIECE + "' or " + OperationHelper.NUMPIECE +  " = '" + TNUMPIECE + "' ) order by id desc ", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            operation = new Operation();

            operation.setId(res.getLong(res.getColumnIndex(OperationHelper.ID)));
            operation.setAgence(res.getString(res.getColumnIndex(OperationHelper.AGENCE)));
            operation.setLibelle(res.getString(res.getColumnIndex(OperationHelper.LIBELLE)));
            operation.setMise(res.getFloat(res.getColumnIndex(OperationHelper.MISE)));
            operation.setMontant(res.getFloat(res.getColumnIndex(OperationHelper.MONTANT)));
            operation.setNom(res.getString(res.getColumnIndex(OperationHelper.NOM)));
            operation.setPrenom(res.getString(res.getColumnIndex(OperationHelper.PRENOM)));
            operation.setNumproduit(res.getString(res.getColumnIndex(OperationHelper.NUMPRODUIT)));
            operation.setNumcompte(res.getString(res.getColumnIndex(OperationHelper.NUMCOMPTE)));
            operation.setNummenbre(res.getString(res.getColumnIndex(OperationHelper.NUMMEMBRE)));
            operation.setNumpiece(res.getString(res.getColumnIndex(OperationHelper.NUMPIECE)));
            operation.setToken(res.getString(res.getColumnIndex(OperationHelper.TOKEN)));
            operation.setTypeoperation(res.getInt(res.getColumnIndex(OperationHelper.TYPEOPERATION)));
            operation.setUser_id(res.getLong(res.getColumnIndex(OperationHelper.USER_ID)));
            operation.setArchiver(res.getInt(res.getColumnIndex(OperationHelper.ARCHIVER)));

            operation.setSync(res.getInt(res.getColumnIndex(OperationHelper.SYNC)));
            operation.setNumpicedef(res.getString(res.getColumnIndex(OperationHelper.NUMPIECEDEF)));
            try {
                operation.setDateoperation(DAOBase.formatter2.parse(res.getString(res.getColumnIndex(OperationHelper.DATE))));
            } catch (Exception e) {
                e.printStackTrace();
            }

            operations.add(operation);
            res.moveToNext();
        }
        return operations;
    }



    public ArrayList<Operation> getOpAdhesion() {

        ArrayList<Operation> operations = new ArrayList<Operation>();
        Operation operation = null ;

        Cursor res =  mDb.rawQuery("select * from " + OperationHelper._TABLE_NAME + " where   "  + OperationHelper.ARCHIVER +  " = 0 AND  " + OperationHelper.TYPEOPERATION + " = 3 order by id desc ", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            operation = new Operation();

            operation.setId(res.getLong(res.getColumnIndex(OperationHelper.ID)));
            operation.setAgence(res.getString(res.getColumnIndex(OperationHelper.AGENCE)));
            operation.setLibelle(res.getString(res.getColumnIndex(OperationHelper.LIBELLE)));
            operation.setMise(res.getFloat(res.getColumnIndex(OperationHelper.MISE)));
            operation.setMontant(res.getFloat(res.getColumnIndex(OperationHelper.MONTANT)));
            operation.setNom(res.getString(res.getColumnIndex(OperationHelper.NOM)));
            operation.setPrenom(res.getString(res.getColumnIndex(OperationHelper.PRENOM)));
            operation.setNumproduit(res.getString(res.getColumnIndex(OperationHelper.NUMPRODUIT)));
            operation.setNumcompte(res.getString(res.getColumnIndex(OperationHelper.NUMCOMPTE)));
            operation.setNummenbre(res.getString(res.getColumnIndex(OperationHelper.NUMMEMBRE)));
            operation.setNumpiece(res.getString(res.getColumnIndex(OperationHelper.NUMPIECE)));
            operation.setToken(res.getString(res.getColumnIndex(OperationHelper.TOKEN)));
            operation.setTypeoperation(res.getInt(res.getColumnIndex(OperationHelper.TYPEOPERATION)));
            operation.setUser_id(res.getLong(res.getColumnIndex(OperationHelper.USER_ID)));
            operation.setArchiver(res.getInt(res.getColumnIndex(OperationHelper.ARCHIVER)));

            operation.setSync(res.getInt(res.getColumnIndex(OperationHelper.SYNC)));
            operation.setNumpicedef(res.getString(res.getColumnIndex(OperationHelper.NUMPIECEDEF)));
            try {
                operation.setDateoperation(DAOBase.formatter2.parse(res.getString(res.getColumnIndex(OperationHelper.DATE))));
            } catch (Exception e) {
                e.printStackTrace();
            }

            operations.add(operation);
            res.moveToNext();
        }
        return operations;
    }




    public ArrayList<Operation> getOpCredit() {

        ArrayList<Operation> operations = new ArrayList<Operation>();
        Operation operation = null ;

        Cursor res =  mDb.rawQuery("select * from " + OperationHelper._TABLE_NAME + " where   "  + OperationHelper.ARCHIVER +  " = 0 AND  " + OperationHelper.TYPEOPERATION + " = 4 order by id desc ", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            operation = new Operation();

            operation.setId(res.getLong(res.getColumnIndex(OperationHelper.ID)));
            operation.setAgence(res.getString(res.getColumnIndex(OperationHelper.AGENCE)));
            operation.setLibelle(res.getString(res.getColumnIndex(OperationHelper.LIBELLE)));
            operation.setMise(res.getFloat(res.getColumnIndex(OperationHelper.MISE)));
            operation.setMontant(res.getFloat(res.getColumnIndex(OperationHelper.MONTANT)));
            operation.setNom(res.getString(res.getColumnIndex(OperationHelper.NOM)));
            operation.setPrenom(res.getString(res.getColumnIndex(OperationHelper.PRENOM)));
            operation.setNumproduit(res.getString(res.getColumnIndex(OperationHelper.NUMPRODUIT)));
            operation.setNumcompte(res.getString(res.getColumnIndex(OperationHelper.NUMCOMPTE)));
            operation.setNummenbre(res.getString(res.getColumnIndex(OperationHelper.NUMMEMBRE)));
            operation.setNumpiece(res.getString(res.getColumnIndex(OperationHelper.NUMPIECE)));
            operation.setToken(res.getString(res.getColumnIndex(OperationHelper.TOKEN)));
            operation.setTypeoperation(res.getInt(res.getColumnIndex(OperationHelper.TYPEOPERATION)));
            operation.setUser_id(res.getLong(res.getColumnIndex(OperationHelper.USER_ID)));
            operation.setArchiver(res.getInt(res.getColumnIndex(OperationHelper.ARCHIVER)));

            operation.setSync(res.getInt(res.getColumnIndex(OperationHelper.SYNC)));
            operation.setNumpicedef(res.getString(res.getColumnIndex(OperationHelper.NUMPIECEDEF)));
            try {
                operation.setDateoperation(DAOBase.formatter2.parse(res.getString(res.getColumnIndex(OperationHelper.DATE))));
            } catch (Exception e) {
                e.printStackTrace();
            }

            operations.add(operation);
            res.moveToNext();
        }
        return operations;
    }


    public ArrayList<Operation> getAll(Date datedebut,Date datefin) {

        ArrayList<Operation> operations = new ArrayList<Operation>();
        Operation operation = null ;
        Log.e("SQL","select * from " + OperationHelper._TABLE_NAME + " where "  + OperationHelper.DATE +  " between '" + formatter.format(datedebut) + "' and '" + formatter.format(datefin) +  "'23:59:59' order by id desc ") ;

        Cursor res =  mDb.rawQuery("select * from " + OperationHelper._TABLE_NAME + " where   "  + OperationHelper.ARCHIVER +  " = 0 AND  "  + OperationHelper.DATE +  " between '" + formatter.format(datedebut) + "' and '" + formatter2.format(datefin) +  " 23:59:59' order by id desc ", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            operation = new Operation();

            operation.setId(res.getLong(res.getColumnIndex(OperationHelper.ID)));
            operation.setAgence(res.getString(res.getColumnIndex(OperationHelper.AGENCE)));
            operation.setLibelle(res.getString(res.getColumnIndex(OperationHelper.LIBELLE)));
            operation.setMise(res.getFloat(res.getColumnIndex(OperationHelper.MISE)));
            operation.setMontant(res.getFloat(res.getColumnIndex(OperationHelper.MONTANT)));
            operation.setNom(res.getString(res.getColumnIndex(OperationHelper.NOM)));
            operation.setPrenom(res.getString(res.getColumnIndex(OperationHelper.PRENOM)));
            operation.setNumproduit(res.getString(res.getColumnIndex(OperationHelper.NUMPRODUIT)));
            operation.setNumcompte(res.getString(res.getColumnIndex(OperationHelper.NUMCOMPTE)));
            operation.setNummenbre(res.getString(res.getColumnIndex(OperationHelper.NUMMEMBRE)));
            operation.setNumpiece(res.getString(res.getColumnIndex(OperationHelper.NUMPIECE)));
            operation.setToken(res.getString(res.getColumnIndex(OperationHelper.TOKEN)));
            operation.setTypeoperation(res.getInt(res.getColumnIndex(OperationHelper.TYPEOPERATION)));
            operation.setUser_id(res.getLong(res.getColumnIndex(OperationHelper.USER_ID)));
            operation.setArchiver(res.getInt(res.getColumnIndex(OperationHelper.ARCHIVER)));

            operation.setSync(res.getInt(res.getColumnIndex(OperationHelper.SYNC)));
            operation.setNumpicedef(res.getString(res.getColumnIndex(OperationHelper.NUMPIECEDEF)));
            try {
                operation.setDateoperation(DAOBase.formatter2.parse(res.getString(res.getColumnIndex(OperationHelper.DATE))));
            } catch (Exception e) {
                e.printStackTrace();
            }

            operations.add(operation);
            res.moveToNext();
        }
        return operations;
    }



    public ArrayList<Operation> getArchiver(Date datedebut,Date datefin) {

        try {
            if (datedebut==null) datedebut =  DAOBase.formatterj.parse(preferences.getString("dateouvert", "")) ;
            if (datefin==null) datefin =  DAOBase.formatterj.parse(preferences.getString("dateouvert", "")) ;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ArrayList<Operation> operations = new ArrayList<Operation>();
        Operation operation = null ;
        String sql = "select * from " + OperationHelper._TABLE_NAME + " where "  + OperationHelper.ARCHIVER +  " = 1 AND "  + OperationHelper.DATE +  " between '" + formatter2.format(datedebut) + " 00:00:00' and '" + formatter2.format(datefin) +  " 23:59:59' order by id desc " ;
        Log.e("SQL",sql) ;

        Cursor res =  mDb.rawQuery(sql, null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            operation = new Operation();

            operation.setId(res.getLong(res.getColumnIndex(OperationHelper.ID)));
            operation.setAgence(res.getString(res.getColumnIndex(OperationHelper.AGENCE)));
            operation.setLibelle(res.getString(res.getColumnIndex(OperationHelper.LIBELLE)));
            operation.setMise(res.getFloat(res.getColumnIndex(OperationHelper.MISE)));
            operation.setMontant(res.getFloat(res.getColumnIndex(OperationHelper.MONTANT)));
            operation.setNom(res.getString(res.getColumnIndex(OperationHelper.NOM)));
            operation.setPrenom(res.getString(res.getColumnIndex(OperationHelper.PRENOM)));
            operation.setNumproduit(res.getString(res.getColumnIndex(OperationHelper.NUMPRODUIT)));
            operation.setNumcompte(res.getString(res.getColumnIndex(OperationHelper.NUMCOMPTE)));
            operation.setNummenbre(res.getString(res.getColumnIndex(OperationHelper.NUMMEMBRE)));
            operation.setNumpiece(res.getString(res.getColumnIndex(OperationHelper.NUMPIECE)));
            operation.setToken(res.getString(res.getColumnIndex(OperationHelper.TOKEN)));
            operation.setTypeoperation(res.getInt(res.getColumnIndex(OperationHelper.TYPEOPERATION)));
            operation.setUser_id(res.getLong(res.getColumnIndex(OperationHelper.USER_ID)));
            operation.setArchiver(res.getInt(res.getColumnIndex(OperationHelper.ARCHIVER)));

            operation.setSync(res.getInt(res.getColumnIndex(OperationHelper.SYNC)));
            operation.setNumpicedef(res.getString(res.getColumnIndex(OperationHelper.NUMPIECEDEF)));
            try {
                operation.setDateoperation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE))));
            } catch (Exception e) {
                e.printStackTrace();
            }

            operations.add(operation);
            res.moveToNext();
        }
        return operations;
    }


    public ArrayList<Operation> getAll(int type,Date datedebut,Date datefin) {

        try {
            if (datedebut==null) datedebut =  DAOBase.formatterj.parse(preferences.getString("dateouvert", "")) ;
            if (datefin==null) datefin =  DAOBase.formatterj.parse(preferences.getString("dateouvert", "")) ;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ArrayList<Operation> operations = new ArrayList<Operation>();
        Operation operation = null ;
        String rqte = "select * from " + OperationHelper._TABLE_NAME + " where  "  + OperationHelper.ARCHIVER +  " = 0 AND  "  + OperationHelper.DATE +  " between '" + formatter2.format(datedebut) + " 00:00:00' and '" + formatter2.format(datefin) +  " 23:59:59'  order by id desc " ;
        if (type == 1) rqte ="select * from " + OperationHelper._TABLE_NAME + " where  "  + OperationHelper.ARCHIVER +  " = 0 AND  " + OperationHelper.TYPEOPERATION + " = " + 1 + " and "  + OperationHelper.DATE +  " between '" + formatter2.format(datedebut) + " 00:00:00' and '" + formatter2.format(datefin) +  " 23:59:59' order by id desc " ;
        else if (type == 2) rqte ="select * from " + OperationHelper._TABLE_NAME + " where  "  + OperationHelper.ARCHIVER +  " = 0 AND  " + OperationHelper.TYPEOPERATION + " = " + 0 + " and "  + OperationHelper.DATE +  " between '" + formatter2.format(datedebut) + " 00:00:00' and '" + formatter2.format(datefin) +  " 23:59:59' order by id desc " ;
        else if (type == 3) rqte ="select * from " + OperationHelper._TABLE_NAME + " where  "  + OperationHelper.ARCHIVER +  " = 0 AND  " + OperationHelper.TYPEOPERATION + " = " + 3 + " and "  + OperationHelper.DATE +  " between '" + formatter2.format(datedebut) + " 00:00:00' and '" + formatter2.format(datefin) +  " 23:59:59' order by id desc " ;
        else if (type == 4) rqte ="select * from " + OperationHelper._TABLE_NAME + " where  "  + OperationHelper.ARCHIVER +  " = 0 AND " + OperationHelper.TYPEOPERATION + " = " + 4 + " and "  + OperationHelper.DATE +  " between '" + formatter2.format(datedebut) + " 00:00:00' and '" + formatter2.format(datefin) +  " 23:59:59'  order by id desc " ;

        Cursor res =  mDb.rawQuery(rqte, null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            operation = new Operation();

            operation.setId(res.getLong(res.getColumnIndex(OperationHelper.ID)));
            operation.setAgence(res.getString(res.getColumnIndex(OperationHelper.AGENCE)));
            operation.setLibelle(res.getString(res.getColumnIndex(OperationHelper.LIBELLE)));
            operation.setMise(res.getFloat(res.getColumnIndex(OperationHelper.MISE)));
            operation.setMontant(res.getFloat(res.getColumnIndex(OperationHelper.MONTANT)));
            operation.setNom(res.getString(res.getColumnIndex(OperationHelper.NOM)));
            operation.setPrenom(res.getString(res.getColumnIndex(OperationHelper.PRENOM)));
            operation.setNumproduit(res.getString(res.getColumnIndex(OperationHelper.NUMPRODUIT)));
            operation.setNumcompte(res.getString(res.getColumnIndex(OperationHelper.NUMCOMPTE)));
            operation.setNummenbre(res.getString(res.getColumnIndex(OperationHelper.NUMMEMBRE)));
            operation.setNumpiece(res.getString(res.getColumnIndex(OperationHelper.NUMPIECE)));
            operation.setToken(res.getString(res.getColumnIndex(OperationHelper.TOKEN)));
            operation.setTypeoperation(res.getInt(res.getColumnIndex(OperationHelper.TYPEOPERATION)));
            operation.setUser_id(res.getLong(res.getColumnIndex(OperationHelper.USER_ID)));
            operation.setArchiver(res.getInt(res.getColumnIndex(OperationHelper.ARCHIVER)));

            operation.setSync(res.getInt(res.getColumnIndex(OperationHelper.SYNC)));
            operation.setNumpicedef(res.getString(res.getColumnIndex(OperationHelper.NUMPIECEDEF)));
            try {
                operation.setDateoperation(DAOBase.formatter2.parse(res.getString(res.getColumnIndex(OperationHelper.DATE))));
            } catch (Exception e) {
                e.printStackTrace();
            }

            operations.add(operation);
            res.moveToNext();
        }
        return operations;
    }

}
