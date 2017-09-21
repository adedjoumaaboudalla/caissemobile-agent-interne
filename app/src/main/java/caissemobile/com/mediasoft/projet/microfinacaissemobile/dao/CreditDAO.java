package caissemobile.com.mediasoft.projet.microfinacaissemobile.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;

import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Credit;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.database.CreditHelper;

/**
 * Created by mediasoft on 13/05/2016.
 */
public class CreditDAO extends DAOBase implements Crud<Credit> {

    Context context = null ;

    public CreditDAO(Context pContext) {
        super(pContext);
        context = pContext ;
        this.mHandler = CreditHelper.getHelper(pContext, DATABASE, VERSION);
        open();
    }

    @Override
    public long add(Credit credit) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(CreditHelper.NUMCREDIT, credit.getNumcredit());
        contentValues.put(CreditHelper.NOM, credit.getNom());
        contentValues.put(CreditHelper.PRENOM, credit.getPrenom());
        contentValues.put(CreditHelper.MONTANTPRET, credit.getMontantpret());
        if (credit.getDatedeblocage()!=null) contentValues.put(CreditHelper.DATEDEBLOCAGE, DAOBase.formatterj.format(credit.getDatedeblocage()));
        contentValues.put(CreditHelper.CREDITENCOURS, credit.getCreaditencours());
        contentValues.put(CreditHelper.NUMMEMBRE, credit.getNummembre());
        contentValues.put(CreditHelper.MENSUALITE, credit.getMensualite());
        contentValues.put(CreditHelper.DUREPRET, credit.getDurepret());
        if (credit.getDatedebut()!=null)contentValues.put(CreditHelper.DATEDEBUT, DAOBase.formatterj.format(credit.getDatedebut()));
        contentValues.put(CreditHelper.TAUXAN, credit.getTauxan());
        contentValues.put(CreditHelper.SUIVI, credit.getSuivi());
        contentValues.put(CreditHelper.NOMPRODUIT, credit.getNomproduit());
        contentValues.put(CreditHelper.CAPITAL_ATTENDU, credit.getCapital_attendu());
        contentValues.put(CreditHelper.INTERET_ATTENDU, credit.getInteret_attendu());
        contentValues.put(CreditHelper.CAPITAL_RETARD, credit.getCapital_retard());
        contentValues.put(CreditHelper.CAPITAL_RETARD, credit.getInteret_retard());
        contentValues.put(CreditHelper.TOTAL_RETARD, credit.getTotal_retard());
        contentValues.put(CreditHelper.RESTE_A_PAYER, credit.getReste_apayer());
        contentValues.put(CreditHelper.PENALITE, credit.getPenalite());

        //contentValues.put(CreditHelper.DATECREATION, DAOBase.formatter.format(credit.getDatecreation()));

        Long l = mDb.insert(CreditHelper._TABLE_NAME, null, contentValues);

        Log.e("DEBUG", String.valueOf(l)) ;
        return l;
    }


    @Override
    public int delete(long id) {
        return  mDb.delete(CreditHelper._TABLE_NAME,"id = ? ",new String[] { Long.toString(id)});
    }

    public int clean() {
        return  mDb.delete(CreditHelper._TABLE_NAME, null,null);
    }


    @Override
    public int update(Credit credit) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(CreditHelper.NUMCREDIT, credit.getNumcredit());
        contentValues.put(CreditHelper.NOM, credit.getNom());
        contentValues.put(CreditHelper.PRENOM, credit.getPrenom());
        contentValues.put(CreditHelper.MONTANTPRET, credit.getMontantpret());
        if (credit.getDatedeblocage()!=null) contentValues.put(CreditHelper.DATEDEBLOCAGE, DAOBase.formatterj.format(credit.getDatedeblocage()));
        contentValues.put(CreditHelper.CREDITENCOURS, credit.getCreaditencours());
        contentValues.put(CreditHelper.NUMMEMBRE, credit.getNummembre());
        contentValues.put(CreditHelper.MENSUALITE, credit.getMensualite());
        contentValues.put(CreditHelper.DUREPRET, credit.getDurepret());
        contentValues.put(CreditHelper.DATEDEBUT, DAOBase.formatterj.format(credit.getDatedebut()));
        contentValues.put(CreditHelper.TAUXAN, credit.getTauxan());
        contentValues.put(CreditHelper.SUIVI, credit.getSuivi());
        contentValues.put(CreditHelper.NOMPRODUIT, credit.getNomproduit());
        contentValues.put(CreditHelper.CAPITAL_ATTENDU, credit.getCapital_attendu());
        contentValues.put(CreditHelper.INTERET_ATTENDU, credit.getInteret_attendu());
        contentValues.put(CreditHelper.CAPITAL_RETARD, credit.getCapital_retard());
        contentValues.put(CreditHelper.CAPITAL_RETARD, credit.getInteret_retard());
        contentValues.put(CreditHelper.TOTAL_RETARD, credit.getTotal_retard());
        contentValues.put(CreditHelper.RESTE_A_PAYER, credit.getReste_apayer());
        contentValues.put(CreditHelper.PENALITE, credit.getPenalite());



        int l = mDb.update(CreditHelper._TABLE_NAME, contentValues, "id = ? ", new String[]{Long.toString(credit.getId())});


        Log.e("UPDATE", String.valueOf(l)) ;
        return l;
    }


    @Override
    public  Credit getOne(long id) {

        Credit credit = null ;

        Cursor res =  mDb.rawQuery( "select * from "+ CreditHelper._TABLE_NAME +" where "+CreditHelper.ID +" = " + String.valueOf(id), null );

        if (res.moveToFirst()){
            credit = new Credit();

            credit.setId(res.getLong(res.getColumnIndex(CreditHelper.ID)));
            credit.setNumcredit(res.getString(res.getColumnIndex(CreditHelper.NUMCREDIT)));
            credit.setNom(res.getString(res.getColumnIndex(CreditHelper.NOM)));
            credit.setPrenom(res.getString(res.getColumnIndex(CreditHelper.PRENOM)));
            credit.setMontantpret(res.getFloat(res.getColumnIndex(CreditHelper.MONTANTPRET)));
            credit.setCreaditencours(res.getFloat(res.getColumnIndex(CreditHelper.CREDITENCOURS)));
            credit.setNummembre(res.getString(res.getColumnIndex(CreditHelper.NUMMEMBRE)));
            credit.setMensualite(res.getFloat(res.getColumnIndex(CreditHelper.MENSUALITE)));
            credit.setDurepret(res.getInt(res.getColumnIndex(CreditHelper.DUREPRET)));
            try {
                credit.setDatedeblocage(DAOBase.formatterj.parse(res.getString(res.getColumnIndex(CreditHelper.DATEDEBLOCAGE))));
                credit.setDatedebut(DAOBase.formatterj.parse(res.getString(res.getColumnIndex(CreditHelper.DATEDEBUT))));
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            credit.setTauxan(res.getFloat(res.getColumnIndex(CreditHelper.TAUXAN)));
            credit.setSuivi(res.getString(res.getColumnIndex(CreditHelper.SUIVI)));
            credit.setNomproduit(res.getString(res.getColumnIndex(CreditHelper.NOMPRODUIT)));
            credit.setCapital_attendu(res.getFloat(res.getColumnIndex(CreditHelper.CAPITAL_ATTENDU)));
            credit.setInteret_attendu(res.getFloat(res.getColumnIndex(CreditHelper.INTERET_ATTENDU)));
            credit.setCapital_retard(res.getFloat(res.getColumnIndex(CreditHelper.CAPITAL_RETARD)));
            credit.setInteret_retard(res.getFloat(res.getColumnIndex(CreditHelper.INTERET_RETARD)));
            credit.setTotal_retard(res.getFloat(res.getColumnIndex(CreditHelper.TOTAL_RETARD)));
            credit.setReste_apayer(res.getFloat(res.getColumnIndex(CreditHelper.RESTE_A_PAYER)));
            credit.setPenalite(res.getFloat(res.getColumnIndex(CreditHelper.PENALITE)));
        }

        res.close();

        return credit ;
    }



    public  Credit getOneByNum(String id) {

        Credit credit = null ;

        Cursor res =  mDb.rawQuery( "select * from "+ CreditHelper._TABLE_NAME +" where "+CreditHelper.NUMCREDIT +" = '" + id + "'", null );

        if (res.moveToFirst()){
            credit = new Credit();

            credit.setId(res.getLong(res.getColumnIndex(CreditHelper.ID)));
            credit.setNumcredit(res.getString(res.getColumnIndex(CreditHelper.NUMCREDIT)));
            credit.setNom(res.getString(res.getColumnIndex(CreditHelper.NOM)));
            credit.setPrenom(res.getString(res.getColumnIndex(CreditHelper.PRENOM)));
            credit.setMontantpret(res.getFloat(res.getColumnIndex(CreditHelper.MONTANTPRET)));
            credit.setCreaditencours(res.getFloat(res.getColumnIndex(CreditHelper.CREDITENCOURS)));
            credit.setNummembre(res.getString(res.getColumnIndex(CreditHelper.NUMMEMBRE)));
            credit.setMensualite(res.getFloat(res.getColumnIndex(CreditHelper.MENSUALITE)));
            credit.setDurepret(res.getInt(res.getColumnIndex(CreditHelper.DUREPRET)));
            try {
                credit.setDatedeblocage(DAOBase.formatterj.parse(res.getString(res.getColumnIndex(CreditHelper.DATEDEBLOCAGE))));
                credit.setDatedebut(DAOBase.formatterj.parse(res.getString(res.getColumnIndex(CreditHelper.DATEDEBUT))));
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            credit.setTauxan(res.getFloat(res.getColumnIndex(CreditHelper.TAUXAN)));
            credit.setSuivi(res.getString(res.getColumnIndex(CreditHelper.SUIVI)));
            credit.setNomproduit(res.getString(res.getColumnIndex(CreditHelper.NOMPRODUIT)));
            credit.setCapital_attendu(res.getFloat(res.getColumnIndex(CreditHelper.CAPITAL_ATTENDU)));
            credit.setInteret_attendu(res.getFloat(res.getColumnIndex(CreditHelper.INTERET_ATTENDU)));
            credit.setCapital_retard(res.getFloat(res.getColumnIndex(CreditHelper.CAPITAL_RETARD)));
            credit.setInteret_retard(res.getFloat(res.getColumnIndex(CreditHelper.INTERET_RETARD)));
            credit.setTotal_retard(res.getFloat(res.getColumnIndex(CreditHelper.TOTAL_RETARD)));
            credit.setReste_apayer(res.getFloat(res.getColumnIndex(CreditHelper.RESTE_A_PAYER)));
            credit.setPenalite(res.getFloat(res.getColumnIndex(CreditHelper.PENALITE)));
        }

        res.close();
        return credit ;
    }


    public  ArrayList<Credit> getOne(String id) {

        ArrayList<Credit> credits = new ArrayList<Credit>();
        Credit credit = null ;

        Cursor res =  mDb.rawQuery( "select * from "+ CreditHelper._TABLE_NAME +" where "+CreditHelper.NUMMEMBRE +" = '" + String.valueOf(id) + "'", null );

        Log.e("SQL", "select * from "+ CreditHelper._TABLE_NAME +" where "+CreditHelper.NUMMEMBRE +" = '" + String.valueOf(id) + "'") ;

        res.moveToFirst();

        while(res.isAfterLast() == false){
            credit = new Credit();

            credit.setId(res.getLong(res.getColumnIndex(CreditHelper.ID)));
            credit.setNumcredit(res.getString(res.getColumnIndex(CreditHelper.NUMCREDIT)));
            credit.setNom(res.getString(res.getColumnIndex(CreditHelper.NOM)));
            credit.setPrenom(res.getString(res.getColumnIndex(CreditHelper.PRENOM)));
            credit.setMontantpret(res.getFloat(res.getColumnIndex(CreditHelper.MONTANTPRET)));
            credit.setCreaditencours(res.getFloat(res.getColumnIndex(CreditHelper.CREDITENCOURS)));
            credit.setNummembre(res.getString(res.getColumnIndex(CreditHelper.NUMMEMBRE)));
            credit.setMensualite(res.getFloat(res.getColumnIndex(CreditHelper.MENSUALITE)));
            credit.setDurepret(res.getInt(res.getColumnIndex(CreditHelper.DUREPRET)));
            try {
                credit.setDatedeblocage(DAOBase.formatterj.parse(res.getString(res.getColumnIndex(CreditHelper.DATEDEBLOCAGE))));
                credit.setDatedebut(DAOBase.formatterj.parse(res.getString(res.getColumnIndex(CreditHelper.DATEDEBUT))));
            } catch (ParseException e) {
                e.printStackTrace();
            }catch (NullPointerException e) {
                e.printStackTrace();
            }
            credit.setTauxan(res.getFloat(res.getColumnIndex(CreditHelper.TAUXAN)));
            credit.setSuivi(res.getString(res.getColumnIndex(CreditHelper.SUIVI)));
            credit.setNomproduit(res.getString(res.getColumnIndex(CreditHelper.NOMPRODUIT)));
            credit.setCapital_attendu(res.getFloat(res.getColumnIndex(CreditHelper.CAPITAL_ATTENDU)));
            credit.setInteret_attendu(res.getFloat(res.getColumnIndex(CreditHelper.INTERET_ATTENDU)));
            credit.setCapital_retard(res.getFloat(res.getColumnIndex(CreditHelper.CAPITAL_RETARD)));
            credit.setInteret_retard(res.getFloat(res.getColumnIndex(CreditHelper.INTERET_RETARD)));
            credit.setTotal_retard(res.getFloat(res.getColumnIndex(CreditHelper.TOTAL_RETARD)));
            credit.setReste_apayer(res.getFloat(res.getColumnIndex(CreditHelper.RESTE_A_PAYER)));
            credit.setPenalite(res.getFloat(res.getColumnIndex(CreditHelper.PENALITE)));

            credits.add(credit);
            res.moveToNext();
        }
        res.close();
        return credits;
    }



    public  long verifieCredit(String id) {
        ArrayList<Credit> credits = new ArrayList<Credit>();
        Credit credit = null ;

        Cursor res =  mDb.rawQuery( "select COUNT(*) as nbre from "+ CreditHelper._TABLE_NAME +" where "+CreditHelper.NUMMEMBRE +" = '" + String.valueOf(id) + "'", null );

        res.moveToFirst();
        long nbr = 0 ;
        if(res.isAfterLast() == false){
            nbr = res.getLong(res.getColumnIndex("nbre"));
        }
        res.close();
        return nbr ;
    }

    @Override
    public ArrayList<Credit> getAll() {

        ArrayList<Credit> credits = new ArrayList<Credit>();
        Credit credit = null ;

        Cursor res =  mDb.rawQuery("select * from " + CreditHelper._TABLE_NAME + " order by " + CreditHelper.NUMCREDIT + " ", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            credit = new Credit();

            credit.setId(res.getLong(res.getColumnIndex(CreditHelper.ID)));
            credit.setNumcredit(res.getString(res.getColumnIndex(CreditHelper.NUMCREDIT)));
            credit.setNom(res.getString(res.getColumnIndex(CreditHelper.NOM)));
            credit.setPrenom(res.getString(res.getColumnIndex(CreditHelper.PRENOM)));
            credit.setMontantpret(res.getFloat(res.getColumnIndex(CreditHelper.MONTANTPRET)));
            credit.setCreaditencours(res.getFloat(res.getColumnIndex(CreditHelper.CREDITENCOURS)));
            credit.setNummembre(res.getString(res.getColumnIndex(CreditHelper.NUMMEMBRE)));
            credit.setMensualite(res.getFloat(res.getColumnIndex(CreditHelper.MENSUALITE)));
            credit.setDurepret(res.getInt(res.getColumnIndex(CreditHelper.DUREPRET)));
            try {
                credit.setDatedeblocage(DAOBase.formatterj.parse(res.getString(res.getColumnIndex(CreditHelper.DATEDEBLOCAGE))));
                credit.setDatedebut(DAOBase.formatterj.parse(res.getString(res.getColumnIndex(CreditHelper.DATEDEBUT))));
            } catch (ParseException e) {
                e.printStackTrace();
            }catch (NullPointerException e) {
                e.printStackTrace();
            }
            credit.setTauxan(res.getFloat(res.getColumnIndex(CreditHelper.TAUXAN)));
            credit.setSuivi(res.getString(res.getColumnIndex(CreditHelper.SUIVI)));
            credit.setNomproduit(res.getString(res.getColumnIndex(CreditHelper.NOMPRODUIT)));
            credit.setCapital_attendu(res.getFloat(res.getColumnIndex(CreditHelper.CAPITAL_ATTENDU)));
            credit.setInteret_attendu(res.getFloat(res.getColumnIndex(CreditHelper.INTERET_ATTENDU)));
            credit.setCapital_retard(res.getFloat(res.getColumnIndex(CreditHelper.CAPITAL_RETARD)));
            credit.setInteret_retard(res.getFloat(res.getColumnIndex(CreditHelper.INTERET_RETARD)));
            credit.setTotal_retard(res.getFloat(res.getColumnIndex(CreditHelper.TOTAL_RETARD)));
            credit.setReste_apayer(res.getFloat(res.getColumnIndex(CreditHelper.RESTE_A_PAYER)));
            credit.setPenalite(res.getFloat(res.getColumnIndex(CreditHelper.PENALITE)));

            credits.add(credit);
            res.moveToNext();
        }
        res.close();
        return credits;
    }

}
