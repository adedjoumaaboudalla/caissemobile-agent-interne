package caissemobile.com.mediasoft.projet.microfinacaissemobile.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Produit;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.database.ProduitHelper;

/**
 * Created by mediasoft on 09/05/2016.
 */
public class ProduitDAO extends DAOBase implements Crud<Produit> {

    Context context = null ;

    public ProduitDAO(Context pContext) {
        super(pContext);
        context = pContext ;
        this.mHandler = ProduitHelper.getHelper(pContext, DATABASE, VERSION);
        open();
    }



    @Override
    public long add(Produit produit) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(ProduitHelper.LIBELLE, produit.getLibelle());
        contentValues.put(ProduitHelper.CODE, produit.getCode());
        contentValues.put(ProduitHelper.NUMPRODUIT, produit.getNumproduit());
        contentValues.put(ProduitHelper.MONTANT_MINI, produit.getDepotmini());

        //contentValues.put(ProduitHelper.DATECREATION, DAOBase.formatter.format(produit.getDatecreation()));

        Long l = mDb.insert(ProduitHelper._TABLE_NAME, null, contentValues);

        Log.e("DEBUG", String.valueOf(l)) ;
        return l;
    }


    @Override
    public int delete(long id) {
        return  mDb.delete(ProduitHelper._TABLE_NAME,"id = ? ",new String[] { Long.toString(id)});
    }

    public int clean() {
        return  mDb.delete(ProduitHelper._TABLE_NAME, null,null);
    }


    @Override
    public int update(Produit produit) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(ProduitHelper.LIBELLE, produit.getLibelle());
        contentValues.put(ProduitHelper.CODE, produit.getCode());
        contentValues.put(ProduitHelper.NUMPRODUIT, produit.getNumproduit());
        contentValues.put(ProduitHelper.MONTANT_MINI, produit.getDepotmini());


        int l = mDb.update(ProduitHelper._TABLE_NAME, contentValues, "id = ? ", new String[]{String.valueOf(produit.getId())});


        Log.e("UPDATE", String.valueOf(l)) ;
        return l;
    }


    @Override
    public  Produit getOne(long id) {

        Produit produit = null ;

        Cursor res =  mDb.rawQuery( "select * from "+ ProduitHelper._TABLE_NAME +" where "+ProduitHelper.ID +" = " + String.valueOf(id), null );

        if (res.moveToFirst()){
            produit = new Produit();

            produit.setId(res.getLong(res.getColumnIndex(ProduitHelper.ID)));
            produit.setNumproduit(res.getString(res.getColumnIndex(ProduitHelper.NUMPRODUIT)));
            produit.setCode(res.getString(res.getColumnIndex(ProduitHelper.CODE)));
            produit.setLibelle(res.getString(res.getColumnIndex(ProduitHelper.LIBELLE)));
            produit.setDepotmini(res.getDouble(res.getColumnIndex(ProduitHelper.MONTANT_MINI)));
        }

        return produit ;
    }

    @Override
    public ArrayList<Produit> getAll() {

        ArrayList<Produit> produits = new ArrayList<Produit>();
        Produit produit = null ;

        Cursor res =  mDb.rawQuery("select * from " + ProduitHelper._TABLE_NAME + " order by " + ProduitHelper.LIBELLE + " ", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            produit = new Produit();


            produit.setId(res.getLong(res.getColumnIndex(ProduitHelper.ID)));
            produit.setNumproduit(res.getString(res.getColumnIndex(ProduitHelper.NUMPRODUIT)));
            produit.setCode(res.getString(res.getColumnIndex(ProduitHelper.CODE)));
            produit.setLibelle(res.getString(res.getColumnIndex(ProduitHelper.LIBELLE)));
            produit.setDepotmini(res.getDouble(res.getColumnIndex(ProduitHelper.MONTANT_MINI)));

            produits.add(produit);
            res.moveToNext();
        }
        return produits;
    }

}
