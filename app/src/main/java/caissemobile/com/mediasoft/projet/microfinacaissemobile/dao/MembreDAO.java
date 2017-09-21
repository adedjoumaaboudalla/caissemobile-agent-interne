package caissemobile.com.mediasoft.projet.microfinacaissemobile.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;

import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Membre;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.database.MembreHelper;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.database.OperationHelper;

/**
 * Created by mediasoft on 09/05/2016.
 */
public class MembreDAO extends DAOBase implements Crud<Membre> {

    Context context = null ;

    public MembreDAO(Context pContext) {
        super(pContext);
        context = pContext ;
        this.mHandler = MembreHelper.getHelper(pContext, DATABASE, VERSION);
        open();
    }

    @Override
    public long add(Membre membre) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(MembreHelper.DATE, DAOBase.formatterj.format(membre.getDateadhesion()));
        contentValues.put(MembreHelper.DATENAISS, DAOBase.formatterj.format(membre.getDateNaiss()));
        contentValues.put(MembreHelper.NOM, membre.getNom());
        contentValues.put(MembreHelper.NUMMEMBRE, membre.getNummembre());
        contentValues.put(MembreHelper.PRENOM, membre.getPrenom());
        contentValues.put(MembreHelper.CATEGORIE, membre.getCategorie());
        contentValues.put(MembreHelper.PROFESSION, membre.getProfession());
        contentValues.put(MembreHelper.ZONE, membre.getZone());
        contentValues.put(MembreHelper.NATIONALITE, membre.getNationalite());
        contentValues.put(MembreHelper.ADRESSE, membre.getAdresse());
        contentValues.put(MembreHelper.SEXE, membre.getSexe());
        contentValues.put(MembreHelper.TEL, membre.getTel());
        contentValues.put(MembreHelper.CODERETRAIT, membre.getCodeRetrait());
        contentValues.put(MembreHelper.MONTANT, membre.getMontant());
        contentValues.put(MembreHelper.SAISI, membre.getSaisi());

        contentValues.put(MembreHelper.TYPE, membre.getType());
        contentValues.put(MembreHelper.NUMGROUPE, membre.getNumgroupe());
        contentValues.put(MembreHelper.POSTE, membre.getPoste());
        contentValues.put(MembreHelper.PHOTO, membre.getPhoto());
        contentValues.put(MembreHelper.ACTIVITE, membre.getActivite());
        contentValues.put(MembreHelper.NUMMEMBRE, membre.getNummembre());
        contentValues.put(MembreHelper.SYNC, membre.getSync());
        contentValues.put(MembreHelper.IDPERSONNE, membre.getIdpersonne());
        contentValues.put(MembreHelper.CODEMEMBRE, membre.getCodemembre());

        //contentValues.put(MembreHelper.DATECREATION, DAOBase.formatter.format(membre.getDatecreation()));

        Long l = mDb.insert(MembreHelper._TABLE_NAME, null, contentValues);

        Log.e("DEBUG", String.valueOf(l)) ;
        return l;
    }


    @Override
    public int delete(long id) {
        return  mDb.delete(MembreHelper._TABLE_NAME,"id = ? ",new String[] { Long.toString(id)});
    }

    public long updateSaisie() {

        ContentValues contentValues = new ContentValues();
        contentValues.put(MembreHelper.SAISI, "0");
        //contentValues.put(MembreHelper.DATECREATION, DAOBase.formatter.format(membre.getDatecreation()));
        int l = mDb.update(MembreHelper._TABLE_NAME, contentValues,null, null);

        return l ;
    }

    public int clean() {
        return  mDb.delete(MembreHelper._TABLE_NAME, null,null);
    }


    @Override
    public int update(Membre membre) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(MembreHelper.DATE, DAOBase.formatterj.format(membre.getDateadhesion()));
        contentValues.put(MembreHelper.DATENAISS, DAOBase.formatterj.format(membre.getDateNaiss()));
        contentValues.put(MembreHelper.NOM, membre.getNom());
        contentValues.put(MembreHelper.NUMMEMBRE, membre.getNummembre());
        contentValues.put(MembreHelper.PRENOM, membre.getPrenom());
        contentValues.put(MembreHelper.CATEGORIE, membre.getCategorie());
        contentValues.put(MembreHelper.PROFESSION, membre.getProfession());
        contentValues.put(MembreHelper.ZONE, membre.getZone());
        contentValues.put(MembreHelper.CODERETRAIT, membre.getCodeRetrait());
        contentValues.put(MembreHelper.NATIONALITE, membre.getNationalite());
        contentValues.put(MembreHelper.ADRESSE, membre.getAdresse());
        contentValues.put(MembreHelper.SEXE, membre.getSexe());
        contentValues.put(MembreHelper.TEL, membre.getTel());
        contentValues.put(MembreHelper.MONTANT, membre.getMontant());
        contentValues.put(MembreHelper.SAISI, membre.getSaisi());

        contentValues.put(MembreHelper.TYPE, membre.getType());
        contentValues.put(MembreHelper.NUMGROUPE, membre.getNumgroupe());
        contentValues.put(MembreHelper.POSTE, membre.getPoste());
        contentValues.put(MembreHelper.PHOTO, membre.getPhoto());
        contentValues.put(MembreHelper.ACTIVITE, membre.getActivite());
        contentValues.put(MembreHelper.NUMMEMBRE, membre.getNummembre());
        contentValues.put(MembreHelper.SYNC, membre.getSync());
        contentValues.put(MembreHelper.IDPERSONNE, membre.getIdpersonne());
        contentValues.put(MembreHelper.CODEMEMBRE, membre.getCodemembre());

        int l = mDb.update(MembreHelper._TABLE_NAME, contentValues, MembreHelper.ID +" = ? ", new String[]{Long.toString(membre.getId())});

        Log.e("UPDATE", String.valueOf(l)) ;
        Log.e("PHOTO", String.valueOf(membre.getPhoto())) ;
        return l;
    }


    @Override
    public  Membre getOne(long id) {

        Membre membre = null ;

        Cursor res =  mDb.rawQuery( "select * from "+ MembreHelper._TABLE_NAME +" where "+MembreHelper.ID +" = " + String.valueOf(id), null );

        if (res.moveToFirst()){
            membre = new Membre();

            membre.setId(res.getLong(res.getColumnIndex(MembreHelper.ID)));
            membre.setNom(res.getString(res.getColumnIndex(MembreHelper.NOM)));
            membre.setNummembre(res.getString(res.getColumnIndex(MembreHelper.NUMMEMBRE)));
            membre.setPrenom(res.getString(res.getColumnIndex(MembreHelper.PRENOM)));
            membre.setCategorie(res.getLong(res.getColumnIndex(MembreHelper.CATEGORIE)));
            membre.setProfession(res.getLong(res.getColumnIndex(MembreHelper.PROFESSION)));
            membre.setNationalite(res.getLong(res.getColumnIndex(MembreHelper.NATIONALITE)));
            membre.setZone(res.getString(res.getColumnIndex(MembreHelper.ZONE)));
            membre.setSexe(res.getString(res.getColumnIndex(MembreHelper.SEXE)));
            membre.setTel(res.getString(res.getColumnIndex(MembreHelper.TEL)));
            membre.setAdresse(res.getString(res.getColumnIndex(MembreHelper.ADRESSE)));
            membre.setMontant(res.getFloat(res.getColumnIndex(MembreHelper.MONTANT)));
            membre.setSaisi(res.getFloat(res.getColumnIndex(MembreHelper.SAISI)));

            membre.setType(res.getInt(res.getColumnIndex(MembreHelper.TYPE)));
            membre.setNumgroupe(res.getString(res.getColumnIndex(MembreHelper.NUMGROUPE)));
            membre.setPoste(res.getString(res.getColumnIndex(MembreHelper.POSTE)));
            membre.setPhoto(res.getString(res.getColumnIndex(MembreHelper.PHOTO)));
            membre.setCodeRetrait(res.getString(res.getColumnIndex(MembreHelper.CODERETRAIT)));
            membre.setActivite(res.getString(res.getColumnIndex(MembreHelper.ACTIVITE)));
            membre.setSync(res.getInt(res.getColumnIndex(MembreHelper.SYNC)));
            membre.setIdpersonne(res.getString(res.getColumnIndex(MembreHelper.IDPERSONNE)));
            membre.setCodemembre(res.getString(res.getColumnIndex(MembreHelper.CODEMEMBRE)));

            try {
                membre.setDateNaiss(DAOBase.formatterj.parse(res.getString(res.getColumnIndex(MembreHelper.DATENAISS))));
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            try {
                membre.setDateadhesion(DAOBase.formatterj.parse(res.getString(res.getColumnIndex(MembreHelper.DATE))));
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        res.close();
        return membre ;
    }



    public  Membre getByNumMembre(String nummembre) {

        Membre membre = null ;

        Cursor res =  mDb.rawQuery( "select * from "+ MembreHelper._TABLE_NAME +" where "+MembreHelper.NUMMEMBRE +" = '" + nummembre + "'", null );

        if (res.moveToFirst()){
            membre = new Membre();

            membre.setId(res.getLong(res.getColumnIndex(MembreHelper.ID)));
            membre.setNom(res.getString(res.getColumnIndex(MembreHelper.NOM)));
            membre.setNummembre(res.getString(res.getColumnIndex(MembreHelper.NUMMEMBRE)));
            membre.setPrenom(res.getString(res.getColumnIndex(MembreHelper.PRENOM)));
            membre.setCategorie(res.getLong(res.getColumnIndex(MembreHelper.CATEGORIE)));
            membre.setProfession(res.getLong(res.getColumnIndex(MembreHelper.PROFESSION)));
            membre.setNationalite(res.getLong(res.getColumnIndex(MembreHelper.NATIONALITE)));
            membre.setZone(res.getString(res.getColumnIndex(MembreHelper.ZONE)));
            membre.setSexe(res.getString(res.getColumnIndex(MembreHelper.SEXE)));
            membre.setTel(res.getString(res.getColumnIndex(MembreHelper.TEL)));
            membre.setAdresse(res.getString(res.getColumnIndex(MembreHelper.ADRESSE)));
            membre.setMontant(res.getFloat(res.getColumnIndex(MembreHelper.MONTANT)));
            membre.setSaisi(res.getFloat(res.getColumnIndex(MembreHelper.SAISI)));


            membre.setType(res.getInt(res.getColumnIndex(MembreHelper.TYPE)));
            membre.setNumgroupe(res.getString(res.getColumnIndex(MembreHelper.NUMGROUPE)));
            membre.setPoste(res.getString(res.getColumnIndex(MembreHelper.POSTE)));
            membre.setPhoto(res.getString(res.getColumnIndex(MembreHelper.PHOTO)));
            membre.setCodeRetrait(res.getString(res.getColumnIndex(MembreHelper.CODERETRAIT)));
            membre.setActivite(res.getString(res.getColumnIndex(MembreHelper.ACTIVITE)));
            membre.setSync(res.getInt(res.getColumnIndex(MembreHelper.SYNC)));
            membre.setIdpersonne(res.getString(res.getColumnIndex(MembreHelper.IDPERSONNE)));
            membre.setCodemembre(res.getString(res.getColumnIndex(MembreHelper.CODEMEMBRE)));
            try {
                membre.setDateNaiss(DAOBase.formatterj.parse(res.getString(res.getColumnIndex(MembreHelper.DATENAISS))));
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            try {
                membre.setDateadhesion(DAOBase.formatterj.parse(res.getString(res.getColumnIndex(MembreHelper.DATE))));
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        res.close();
        return membre ;
    }


    public  Membre getLast() {

        Membre membre = null ;

        Cursor res =  mDb.rawQuery( "select * from "+ MembreHelper._TABLE_NAME +" order by " + MembreHelper.ID + " desc", null );

        if (res.moveToFirst()){
            membre = new Membre();

            membre.setId(res.getLong(res.getColumnIndex(MembreHelper.ID)));
            membre.setNom(res.getString(res.getColumnIndex(MembreHelper.NOM)));
            membre.setNummembre(res.getString(res.getColumnIndex(MembreHelper.NUMMEMBRE)));
            membre.setPrenom(res.getString(res.getColumnIndex(MembreHelper.PRENOM)));
            membre.setCategorie(res.getLong(res.getColumnIndex(MembreHelper.CATEGORIE)));
            membre.setProfession(res.getLong(res.getColumnIndex(MembreHelper.PROFESSION)));
            membre.setNationalite(res.getLong(res.getColumnIndex(MembreHelper.NATIONALITE)));
            membre.setZone(res.getString(res.getColumnIndex(MembreHelper.ZONE)));
            membre.setCodeRetrait(res.getString(res.getColumnIndex(MembreHelper.CODERETRAIT)));
            membre.setSexe(res.getString(res.getColumnIndex(MembreHelper.SEXE)));
            membre.setTel(res.getString(res.getColumnIndex(MembreHelper.TEL)));
            membre.setAdresse(res.getString(res.getColumnIndex(MembreHelper.ADRESSE)));
            membre.setMontant(res.getFloat(res.getColumnIndex(MembreHelper.MONTANT)));
            membre.setSaisi(res.getFloat(res.getColumnIndex(MembreHelper.SAISI)));


            membre.setType(res.getInt(res.getColumnIndex(MembreHelper.TYPE)));
            membre.setNumgroupe(res.getString(res.getColumnIndex(MembreHelper.NUMGROUPE)));
            membre.setPoste(res.getString(res.getColumnIndex(MembreHelper.POSTE)));
            membre.setPhoto(res.getString(res.getColumnIndex(MembreHelper.PHOTO)));
            membre.setActivite(res.getString(res.getColumnIndex(MembreHelper.ACTIVITE)));
            membre.setIdpersonne(res.getString(res.getColumnIndex(MembreHelper.IDPERSONNE)));
            membre.setCodemembre(res.getString(res.getColumnIndex(MembreHelper.CODEMEMBRE)));
            membre.setSync(res.getInt(res.getColumnIndex(MembreHelper.SYNC)));
            try {
                membre.setDateNaiss(DAOBase.formatterj.parse(res.getString(res.getColumnIndex(MembreHelper.DATENAISS))));
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            try {
                membre.setDateadhesion(DAOBase.formatterj.parse(res.getString(res.getColumnIndex(MembreHelper.DATE))));
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        res.close();
        return membre ;
    }

    @Override
    public ArrayList<Membre> getAll() {

        ArrayList<Membre> membres = new ArrayList<Membre>();
        Membre membre = null ;

        Cursor res =  mDb.rawQuery("select * from " + MembreHelper._TABLE_NAME + " order by id desc ", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            membre = new Membre();


            membre.setId(res.getLong(res.getColumnIndex(MembreHelper.ID)));
            membre.setNom(res.getString(res.getColumnIndex(MembreHelper.NOM)));
            membre.setNummembre(res.getString(res.getColumnIndex(MembreHelper.NUMMEMBRE)));
            membre.setPrenom(res.getString(res.getColumnIndex(MembreHelper.PRENOM)));
            membre.setCategorie(res.getLong(res.getColumnIndex(MembreHelper.CATEGORIE)));
            membre.setProfession(res.getLong(res.getColumnIndex(MembreHelper.PROFESSION)));
            membre.setNationalite(res.getLong(res.getColumnIndex(MembreHelper.NATIONALITE)));
            membre.setZone(res.getString(res.getColumnIndex(MembreHelper.ZONE)));
            membre.setCodeRetrait(res.getString(res.getColumnIndex(MembreHelper.CODERETRAIT)));
            membre.setSexe(res.getString(res.getColumnIndex(MembreHelper.SEXE)));
            membre.setTel(res.getString(res.getColumnIndex(MembreHelper.TEL)));
            membre.setAdresse(res.getString(res.getColumnIndex(MembreHelper.ADRESSE)));
            membre.setMontant(res.getFloat(res.getColumnIndex(MembreHelper.MONTANT)));
            membre.setSaisi(res.getFloat(res.getColumnIndex(MembreHelper.SAISI)));


            membre.setType(res.getInt(res.getColumnIndex(MembreHelper.TYPE)));
            membre.setNumgroupe(res.getString(res.getColumnIndex(MembreHelper.NUMGROUPE)));
            membre.setPoste(res.getString(res.getColumnIndex(MembreHelper.POSTE)));
            membre.setPhoto(res.getString(res.getColumnIndex(MembreHelper.PHOTO)));
            membre.setActivite(res.getString(res.getColumnIndex(MembreHelper.ACTIVITE)));
            membre.setIdpersonne(res.getString(res.getColumnIndex(MembreHelper.IDPERSONNE)));
            membre.setCodemembre(res.getString(res.getColumnIndex(MembreHelper.CODEMEMBRE)));
            membre.setSync(res.getInt(res.getColumnIndex(MembreHelper.SYNC)));
            try {
                membre.setDateNaiss(DAOBase.formatterj.parse(res.getString(res.getColumnIndex(MembreHelper.DATENAISS))));
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            try {
                membre.setDateadhesion(DAOBase.formatterj.parse(res.getString(res.getColumnIndex(MembreHelper.DATE))));
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            if (membre.getNumgroupe().equals(""))membres.add(membre);
            res.moveToNext();
        }
        res.close();
        return membres;
    }


    public ArrayList<Membre> getAllIndividuLibre() {

        ArrayList<Membre> membres = new ArrayList<Membre>();
        Membre membre = null ;

        Cursor res =  mDb.rawQuery("select * from " + MembreHelper._TABLE_NAME + " order by id desc ", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            membre = new Membre();


            membre.setId(res.getLong(res.getColumnIndex(MembreHelper.ID)));
            membre.setNom(res.getString(res.getColumnIndex(MembreHelper.NOM)));
            membre.setNummembre(res.getString(res.getColumnIndex(MembreHelper.NUMMEMBRE)));
            membre.setPrenom(res.getString(res.getColumnIndex(MembreHelper.PRENOM)));
            membre.setCategorie(res.getLong(res.getColumnIndex(MembreHelper.CATEGORIE)));
            membre.setProfession(res.getLong(res.getColumnIndex(MembreHelper.PROFESSION)));
            membre.setNationalite(res.getLong(res.getColumnIndex(MembreHelper.NATIONALITE)));
            membre.setZone(res.getString(res.getColumnIndex(MembreHelper.ZONE)));
            membre.setCodeRetrait(res.getString(res.getColumnIndex(MembreHelper.CODERETRAIT)));
            membre.setSexe(res.getString(res.getColumnIndex(MembreHelper.SEXE)));
            membre.setTel(res.getString(res.getColumnIndex(MembreHelper.TEL)));
            membre.setAdresse(res.getString(res.getColumnIndex(MembreHelper.ADRESSE)));
            membre.setMontant(res.getFloat(res.getColumnIndex(MembreHelper.MONTANT)));
            membre.setSaisi(res.getFloat(res.getColumnIndex(MembreHelper.SAISI)));


            membre.setType(res.getInt(res.getColumnIndex(MembreHelper.TYPE)));
            membre.setNumgroupe(res.getString(res.getColumnIndex(MembreHelper.NUMGROUPE)));
            membre.setPoste(res.getString(res.getColumnIndex(MembreHelper.POSTE)));
            membre.setPhoto(res.getString(res.getColumnIndex(MembreHelper.PHOTO)));
            membre.setActivite(res.getString(res.getColumnIndex(MembreHelper.ACTIVITE)));
            membre.setIdpersonne(res.getString(res.getColumnIndex(MembreHelper.IDPERSONNE)));
            membre.setCodemembre(res.getString(res.getColumnIndex(MembreHelper.CODEMEMBRE)));
            membre.setSync(res.getInt(res.getColumnIndex(MembreHelper.SYNC)));

            Log.i("DATE NAISS",res.getString(res.getColumnIndex(MembreHelper.DATENAISS))) ;

            try {
                membre.setDateNaiss(DAOBase.formatterj.parse(res.getString(res.getColumnIndex(MembreHelper.DATENAISS))));
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            try {
                membre.setDateadhesion(DAOBase.formatterj.parse(res.getString(res.getColumnIndex(MembreHelper.DATE))));
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            if (membre.getNummembre().contains("/I/"))membres.add(membre);
            res.moveToNext();
        }
        res.close();
        return membres;
    }


    public ArrayList<Membre> getNonSync() {

        ArrayList<Membre> membres = new ArrayList<Membre>();
        Membre membre = null ;

        Cursor res =  mDb.rawQuery("select * from " + MembreHelper._TABLE_NAME + " where " + MembreHelper.SYNC + " = 0 order by id desc ", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            membre = new Membre();


            membre.setId(res.getLong(res.getColumnIndex(MembreHelper.ID)));
            membre.setNom(res.getString(res.getColumnIndex(MembreHelper.NOM)));
            membre.setNummembre(res.getString(res.getColumnIndex(MembreHelper.NUMMEMBRE)));
            membre.setPrenom(res.getString(res.getColumnIndex(MembreHelper.PRENOM)));
            membre.setCategorie(res.getLong(res.getColumnIndex(MembreHelper.CATEGORIE)));
            membre.setProfession(res.getLong(res.getColumnIndex(MembreHelper.PROFESSION)));
            membre.setNationalite(res.getLong(res.getColumnIndex(MembreHelper.NATIONALITE)));
            membre.setZone(res.getString(res.getColumnIndex(MembreHelper.ZONE)));
            membre.setCodeRetrait(res.getString(res.getColumnIndex(MembreHelper.CODERETRAIT)));
            membre.setSexe(res.getString(res.getColumnIndex(MembreHelper.SEXE)));
            membre.setTel(res.getString(res.getColumnIndex(MembreHelper.TEL)));
            membre.setAdresse(res.getString(res.getColumnIndex(MembreHelper.ADRESSE)));
            membre.setMontant(res.getFloat(res.getColumnIndex(MembreHelper.MONTANT)));
            membre.setSaisi(res.getFloat(res.getColumnIndex(MembreHelper.SAISI)));


            membre.setType(res.getInt(res.getColumnIndex(MembreHelper.TYPE)));
            membre.setNumgroupe(res.getString(res.getColumnIndex(MembreHelper.NUMGROUPE)));
            membre.setPoste(res.getString(res.getColumnIndex(MembreHelper.POSTE)));
            membre.setPhoto(res.getString(res.getColumnIndex(MembreHelper.PHOTO)));
            membre.setActivite(res.getString(res.getColumnIndex(MembreHelper.ACTIVITE)));
            membre.setIdpersonne(res.getString(res.getColumnIndex(MembreHelper.IDPERSONNE)));
            membre.setCodemembre(res.getString(res.getColumnIndex(MembreHelper.CODEMEMBRE)));
            membre.setSync(res.getInt(res.getColumnIndex(MembreHelper.SYNC)));
            try {
                membre.setDateNaiss(DAOBase.formatterj.parse(res.getString(res.getColumnIndex(MembreHelper.DATENAISS))));
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            try {
                membre.setDateadhesion(DAOBase.formatterj.parse(res.getString(res.getColumnIndex(MembreHelper.DATE))));
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            if (membre.getNumgroupe().equals(""))membres.add(membre);
            res.moveToNext();
        }
        res.close();
        return membres;
    }



    public ArrayList<Membre> getNonPhotoSync() {

        ArrayList<Membre> membres = new ArrayList<Membre>();
        Membre membre = null ;

        Cursor res =  mDb.rawQuery("select * from " + MembreHelper._TABLE_NAME + " order by id desc ", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            membre = new Membre();


            membre.setId(res.getLong(res.getColumnIndex(MembreHelper.ID)));
            membre.setNom(res.getString(res.getColumnIndex(MembreHelper.NOM)));
            membre.setNummembre(res.getString(res.getColumnIndex(MembreHelper.NUMMEMBRE)));
            membre.setPrenom(res.getString(res.getColumnIndex(MembreHelper.PRENOM)));
            membre.setCategorie(res.getLong(res.getColumnIndex(MembreHelper.CATEGORIE)));
            membre.setProfession(res.getLong(res.getColumnIndex(MembreHelper.PROFESSION)));
            membre.setNationalite(res.getLong(res.getColumnIndex(MembreHelper.NATIONALITE)));
            membre.setZone(res.getString(res.getColumnIndex(MembreHelper.ZONE)));
            membre.setCodeRetrait(res.getString(res.getColumnIndex(MembreHelper.CODERETRAIT)));
            membre.setSexe(res.getString(res.getColumnIndex(MembreHelper.SEXE)));
            membre.setTel(res.getString(res.getColumnIndex(MembreHelper.TEL)));
            membre.setAdresse(res.getString(res.getColumnIndex(MembreHelper.ADRESSE)));
            membre.setMontant(res.getFloat(res.getColumnIndex(MembreHelper.MONTANT)));
            membre.setSaisi(res.getFloat(res.getColumnIndex(MembreHelper.SAISI)));


            membre.setType(res.getInt(res.getColumnIndex(MembreHelper.TYPE)));
            membre.setNumgroupe(res.getString(res.getColumnIndex(MembreHelper.NUMGROUPE)));
            membre.setPoste(res.getString(res.getColumnIndex(MembreHelper.POSTE)));
            membre.setPhoto(res.getString(res.getColumnIndex(MembreHelper.PHOTO)));
            membre.setActivite(res.getString(res.getColumnIndex(MembreHelper.ACTIVITE)));
            membre.setIdpersonne(res.getString(res.getColumnIndex(MembreHelper.IDPERSONNE)));
            membre.setCodemembre(res.getString(res.getColumnIndex(MembreHelper.CODEMEMBRE)));
            membre.setSync(res.getInt(res.getColumnIndex(MembreHelper.SYNC)));


            if (membre.getNummembre() != null && membre.getPhoto()!=null) membres.add(membre);

            res.moveToNext();
        }
        res.close();
        return membres;
    }

    public ArrayList<Membre> getAllByNumMembre(String nummembre) {

        ArrayList<Membre> membres = new ArrayList<Membre>();
        Membre membre = null ;

        Cursor res =  mDb.rawQuery("select * from " + MembreHelper._TABLE_NAME + " where " + MembreHelper.NUMGROUPE + " = '" + nummembre + "' order by id desc ", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            membre = new Membre();


            membre.setId(res.getLong(res.getColumnIndex(MembreHelper.ID)));
            membre.setNom(res.getString(res.getColumnIndex(MembreHelper.NOM)));
            membre.setNummembre(res.getString(res.getColumnIndex(MembreHelper.NUMMEMBRE)));
            membre.setPrenom(res.getString(res.getColumnIndex(MembreHelper.PRENOM)));
            membre.setCategorie(res.getLong(res.getColumnIndex(MembreHelper.CATEGORIE)));
            membre.setProfession(res.getLong(res.getColumnIndex(MembreHelper.PROFESSION)));
            membre.setNationalite(res.getLong(res.getColumnIndex(MembreHelper.NATIONALITE)));
            membre.setZone(res.getString(res.getColumnIndex(MembreHelper.ZONE)));
            membre.setCodeRetrait(res.getString(res.getColumnIndex(MembreHelper.CODERETRAIT)));
            membre.setSexe(res.getString(res.getColumnIndex(MembreHelper.SEXE)));
            membre.setTel(res.getString(res.getColumnIndex(MembreHelper.TEL)));
            membre.setAdresse(res.getString(res.getColumnIndex(MembreHelper.ADRESSE)));
            membre.setMontant(res.getFloat(res.getColumnIndex(MembreHelper.MONTANT)));
            membre.setSaisi(res.getFloat(res.getColumnIndex(MembreHelper.SAISI)));


            membre.setType(res.getInt(res.getColumnIndex(MembreHelper.TYPE)));
            membre.setNumgroupe(res.getString(res.getColumnIndex(MembreHelper.NUMGROUPE)));
            membre.setPoste(res.getString(res.getColumnIndex(MembreHelper.POSTE)));
            membre.setPhoto(res.getString(res.getColumnIndex(MembreHelper.PHOTO)));
            membre.setActivite(res.getString(res.getColumnIndex(MembreHelper.ACTIVITE)));
            membre.setIdpersonne(res.getString(res.getColumnIndex(MembreHelper.IDPERSONNE)));
            membre.setCodemembre(res.getString(res.getColumnIndex(MembreHelper.CODEMEMBRE)));
            membre.setSync(res.getInt(res.getColumnIndex(MembreHelper.SYNC)));
            try {
                membre.setDateNaiss(DAOBase.formatterj.parse(res.getString(res.getColumnIndex(MembreHelper.DATENAISS))));
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            try {
                membre.setDateadhesion(DAOBase.formatterj.parse(res.getString(res.getColumnIndex(MembreHelper.DATE))));
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            membres.add(membre);
            res.moveToNext();
        }
        res.close();
        return membres;
    }

}
