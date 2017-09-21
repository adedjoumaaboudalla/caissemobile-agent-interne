package caissemobile.com.mediasoft.projet.microfinacaissemobile.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Categorie;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.database.CategorieHelper;

/**
 * Created by mediasoft on 09/05/2016.
 */
public class CategorieDAO extends DAOBase implements Crud<Categorie> {

    Context context = null ;

    public CategorieDAO(Context pContext) {
        super(pContext);
        context = pContext ;
        this.mHandler = CategorieHelper.getHelper(pContext, DATABASE, VERSION);
        open();
    }

    @Override
    public long add(Categorie categorie) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(CategorieHelper.TABLE_KEY, categorie.getId());
        contentValues.put(CategorieHelper.NUMCATEGORIE, categorie.getNumCategorie());
        contentValues.put(CategorieHelper.LIBELLE, categorie.getLibelleCategorie());
        contentValues.put(CategorieHelper.LIBELLEFRAIS, categorie.getLibelleFrais());
        contentValues.put(CategorieHelper.NUMPRODUIT, categorie.getNumproduit());
        contentValues.put(CategorieHelper.VALEURFRAIS, categorie.getValeurFrais());

        Log.v(CategorieHelper.NUMPRODUIT,categorie.getNumproduit()) ;
        Long l = mDb.insert(CategorieHelper._TABLE_NAME, null, contentValues);

        Log.e("DEBUG", String.valueOf(l)) ;
        return l;
    }


    @Override
    public int delete(long id) {
        return  mDb.delete(CategorieHelper._TABLE_NAME, CategorieHelper.TABLE_KEY +  " = ? ",new String[] { Long.toString(id)});
    }

    public int clean() {
        return  mDb.delete(CategorieHelper._TABLE_NAME, null,null);
    }


    @Override
    public int update(Categorie categorie) {

        ContentValues contentValues = new ContentValues();


        contentValues.put(CategorieHelper.TABLE_KEY, categorie.getId());
        contentValues.put(CategorieHelper.NUMCATEGORIE, categorie.getNumCategorie());
        contentValues.put(CategorieHelper.LIBELLE, categorie.getLibelleCategorie());
        contentValues.put(CategorieHelper.LIBELLEFRAIS, categorie.getLibelleFrais());
        contentValues.put(CategorieHelper.VALEURFRAIS, categorie.getValeurFrais());
        contentValues.put(CategorieHelper.NUMPRODUIT, categorie.getNumproduit());

        int l = mDb.update(CategorieHelper._TABLE_NAME, contentValues, CategorieHelper.NUMCATEGORIE  +" = ? ", new String[]{Long.toString(categorie.getId())});


        Log.e("UPDATE", String.valueOf(l)) ;
        return l;
    }


    @Override
    public  Categorie getOne(long id) {

        Categorie categorie = null ;

        Cursor res =  mDb.rawQuery( "select * from "+ CategorieHelper._TABLE_NAME +" where "+CategorieHelper.TABLE_KEY +" = " + String.valueOf(id), null );

        if (res.moveToFirst()){
            categorie = new Categorie();

            categorie.setId(res.getLong(res.getColumnIndex(CategorieHelper.TABLE_KEY)));
            categorie.setNumCategorie(res.getString(res.getColumnIndex(CategorieHelper.NUMCATEGORIE)));
            categorie.setLibelleCategorie(res.getString(res.getColumnIndex(CategorieHelper.LIBELLE)));
            categorie.setLibelleFrais(res.getString(res.getColumnIndex(CategorieHelper.LIBELLEFRAIS)));
            categorie.setNumproduit(res.getString(res.getColumnIndex(CategorieHelper.NUMPRODUIT)));
            categorie.setValeurFrais(res.getFloat(res.getColumnIndex(CategorieHelper.VALEURFRAIS)));
        }

        return categorie ;
    }



    public  Categorie getOne(String numcat) {

        Categorie categorie = null ;

        Cursor res =  mDb.rawQuery( "select * from "+ CategorieHelper._TABLE_NAME +" where "+CategorieHelper.NUMCATEGORIE +" = '" + numcat + "'", null );

        if (res.moveToFirst()){
            categorie = new Categorie();

            categorie.setId(res.getLong(res.getColumnIndex(CategorieHelper.TABLE_KEY)));
            categorie.setNumCategorie(res.getString(res.getColumnIndex(CategorieHelper.NUMCATEGORIE)));
            categorie.setLibelleCategorie(res.getString(res.getColumnIndex(CategorieHelper.LIBELLE)));
            categorie.setLibelleFrais(res.getString(res.getColumnIndex(CategorieHelper.LIBELLEFRAIS)));
            categorie.setNumproduit(res.getString(res.getColumnIndex(CategorieHelper.NUMPRODUIT)));
            categorie.setValeurFrais(res.getFloat(res.getColumnIndex(CategorieHelper.VALEURFRAIS)));

            Log.e("PRODUIT",res.getString(res.getColumnIndex(CategorieHelper.NUMPRODUIT))) ;
        }

        return categorie ;
    }


    public  Categorie getGroupe() {

        Categorie categorie = null ;

        Cursor res =  mDb.rawQuery( "select * from "+ CategorieHelper._TABLE_NAME +" where "+CategorieHelper.NUMCATEGORIE +" = 'G'", null );

        if (res.moveToFirst()){
            categorie = new Categorie();

            categorie.setId(res.getLong(res.getColumnIndex(CategorieHelper.TABLE_KEY)));
            categorie.setNumCategorie(res.getString(res.getColumnIndex(CategorieHelper.NUMCATEGORIE)));
            categorie.setLibelleCategorie(res.getString(res.getColumnIndex(CategorieHelper.LIBELLE)));
            categorie.setLibelleFrais(res.getString(res.getColumnIndex(CategorieHelper.LIBELLEFRAIS)));
            categorie.setNumproduit(res.getString(res.getColumnIndex(CategorieHelper.NUMPRODUIT)));
            categorie.setValeurFrais(res.getFloat(res.getColumnIndex(CategorieHelper.VALEURFRAIS)));
        }

        return categorie ;
    }

    @Override
    public ArrayList<Categorie> getAll() {

        ArrayList<Categorie> categories = new ArrayList<Categorie>();
        Categorie categorie = null ;

        Cursor res =  mDb.rawQuery("select DISTINCT " + CategorieHelper.NUMCATEGORIE + "  from " + CategorieHelper._TABLE_NAME + " order by " + CategorieHelper.LIBELLE + " ", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            categorie = new Categorie();

            categorie.setNumCategorie(res.getString(res.getColumnIndex(CategorieHelper.NUMCATEGORIE)));
            /*
            try {
                categorie.setDatecreation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(CategorieHelper.DATECREATION))));
            } catch (ParseException e) {
                e.printStackTrace();
            } */

            categories.add(categorie);
            res.moveToNext();
        }
        return categories;
    }

    public ArrayList<Categorie> getAll(String numcat,String numprod) {

        ArrayList<Categorie> categories = new ArrayList<Categorie>();
        Categorie categorie = null ;

        String rqt = "select * from " + CategorieHelper._TABLE_NAME + " where "+CategorieHelper.NUMCATEGORIE +" = '" + numcat + "' and  "+CategorieHelper.NUMPRODUIT +" = '" + numprod + "'  order by " + CategorieHelper.LIBELLE + " " ;
        Log.e("RQT NUMPRODUIT",rqt) ;
        Cursor res =  mDb.rawQuery(rqt, null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            categorie = new Categorie();

            categorie.setId(res.getLong(res.getColumnIndex(CategorieHelper.TABLE_KEY)));
            categorie.setNumCategorie(res.getString(res.getColumnIndex(CategorieHelper.NUMCATEGORIE)));
            categorie.setLibelleCategorie(res.getString(res.getColumnIndex(CategorieHelper.LIBELLE)));
            categorie.setLibelleFrais(res.getString(res.getColumnIndex(CategorieHelper.LIBELLEFRAIS)));
            categorie.setNumproduit(res.getString(res.getColumnIndex(CategorieHelper.NUMPRODUIT)));
            categorie.setValeurFrais(res.getFloat(res.getColumnIndex(CategorieHelper.VALEURFRAIS)));

            categories.add(categorie);
            res.moveToNext();
        }
        return categories;
    }


    public ArrayList<Categorie> getAllByNumProduit(String numpro) {

        ArrayList<Categorie> categories = new ArrayList<Categorie>();
        Categorie categorie = null ;

        Cursor res =  mDb.rawQuery("select * from " + CategorieHelper._TABLE_NAME + " where "+CategorieHelper.NUMPRODUIT +" = '" + numpro + "'  order by " + CategorieHelper.LIBELLE + " ", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            categorie = new Categorie();

            categorie.setId(res.getLong(res.getColumnIndex(CategorieHelper.TABLE_KEY)));
            categorie.setNumCategorie(res.getString(res.getColumnIndex(CategorieHelper.NUMCATEGORIE)));
            categorie.setLibelleCategorie(res.getString(res.getColumnIndex(CategorieHelper.LIBELLE)));
            categorie.setLibelleFrais(res.getString(res.getColumnIndex(CategorieHelper.LIBELLEFRAIS)));
            categorie.setNumproduit(res.getString(res.getColumnIndex(CategorieHelper.NUMPRODUIT)));
            categorie.setValeurFrais(res.getFloat(res.getColumnIndex(CategorieHelper.VALEURFRAIS)));

            categories.add(categorie);
            res.moveToNext();
        }
        return categories;
    }

}
