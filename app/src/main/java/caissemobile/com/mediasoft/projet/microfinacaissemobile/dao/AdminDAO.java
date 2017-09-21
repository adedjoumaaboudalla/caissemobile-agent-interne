package caissemobile.com.mediasoft.projet.microfinacaissemobile.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Admin;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.database.AdminHelper;


/**
 * Created by mediasoft on 13/05/2016.
 */
public class AdminDAO extends DAOBase implements Crud<Admin> {

    Context context = null ;

    public AdminDAO(Context pContext) {
        super(pContext);
        context = pContext ;
        this.mHandler = AdminHelper.getHelper(pContext, DATABASE, VERSION);
        open();
        open();
    }

    @Override
    public long add(Admin admin) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(AdminHelper.LOGIN, admin.getLogin());
        contentValues.put(AdminHelper.PASSWORD, admin.getPassword());

        //contentValues.put(AdminHelper.DATECREATION, DAOBase.formatter.format(admin.getDatecreation()));

        Long l = mDb.insert(AdminHelper._TABLE_NAME, null, contentValues);

        Log.e("DEBUG", String.valueOf(l)) ;
        return l;
    }


    @Override
    public int delete(long id) {
        return  mDb.delete(AdminHelper._TABLE_NAME,"id = ? ",new String[] { Long.toString(id)});
    }

    public int clean() {
        return  mDb.delete(AdminHelper._TABLE_NAME, null,null);
    }


    @Override
    public int update(Admin admin) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(AdminHelper.ID, admin.getId());
        contentValues.put(AdminHelper.LOGIN, admin.getLogin());
        contentValues.put(AdminHelper.PASSWORD, admin.getPassword());


        int l = mDb.update(AdminHelper._TABLE_NAME, contentValues, "id = ? ", new String[]{Long.toString(admin.getId())});


        Log.e("UPDATE", String.valueOf(l)) ;
        return l;
    }


    @Override
    public  Admin getOne(long id) {

        Admin admin = null ;

        Cursor res =  mDb.rawQuery( "select * from "+ AdminHelper._TABLE_NAME +" where "+AdminHelper.ID +" = " + String.valueOf(id), null );

        if (res.moveToFirst()){
            admin = new Admin();

            admin.setId(res.getLong(res.getColumnIndex(AdminHelper.ID)));
            admin.setLogin(res.getString(res.getColumnIndex(AdminHelper.LOGIN)));
            admin.setPassword(res.getString(res.getColumnIndex(AdminHelper.PASSWORD)));
        }

        return admin ;
    }

    @Override
    public ArrayList<Admin> getAll() {

        ArrayList<Admin> admins = new ArrayList<Admin>();
        Admin admin = null ;

        Cursor res =  mDb.rawQuery("select * from " + AdminHelper._TABLE_NAME + " order by " + AdminHelper.ID + " ", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            admin = new Admin();

            admin.setId(res.getLong(res.getColumnIndex(AdminHelper.ID)));
            admin.setLogin(res.getString(res.getColumnIndex(AdminHelper.LOGIN)));
            admin.setPassword(res.getString(res.getColumnIndex(AdminHelper.PASSWORD)));
            /*
            try {
                admin.setDatecreation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(AdminHelper.DATECREATION))));
            } catch (ParseException e) {
                e.printStackTrace();
            } */

            admins.add(admin);
            res.moveToNext();
        }
        return admins;
    }

}
