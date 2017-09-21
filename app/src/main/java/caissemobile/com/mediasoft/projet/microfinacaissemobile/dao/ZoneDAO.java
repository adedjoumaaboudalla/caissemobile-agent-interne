package caissemobile.com.mediasoft.projet.microfinacaissemobile.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Zone;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.database.ZoneHelper;

/**
 * Created by mediasoft on 09/05/2016.
 */
public class ZoneDAO extends DAOBase implements Crud<Zone> {

    Context context = null ;

    public ZoneDAO(Context pContext) {
        super(pContext);
        context = pContext ;
        this.mHandler = ZoneHelper.getHelper(pContext, DATABASE, VERSION);
        open();
    }

    @Override
    public long add(Zone zone) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(ZoneHelper.DESCRIPTION, zone.getDescription());

        //contentValues.put(ZoneHelper.DATECREATION, DAOBase.formatter.format(zone.getDatecreation()));

        Long l = mDb.insert(ZoneHelper._TABLE_NAME, null, contentValues);

        Log.e("DEBUG", String.valueOf(l)) ;
        return l;
    }


    @Override
    public int delete(long id) {
        return  mDb.delete(ZoneHelper._TABLE_NAME,"id = ? ",new String[] { Long.toString(id)});
    }

    public int clean() {
        return  mDb.delete(ZoneHelper._TABLE_NAME, null,null);
    }


    @Override
    public int update(Zone zone) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(ZoneHelper.ID, zone.getId());
        contentValues.put(ZoneHelper.DESCRIPTION, zone.getDescription());


        int l = mDb.update(ZoneHelper._TABLE_NAME, contentValues, "id = ? ", new String[]{Long.toString(zone.getId())});


        Log.e("UPDATE", String.valueOf(l)) ;
        return l;
    }


    @Override
    public  Zone getOne(long id) {

        Zone zone = null ;

        Cursor res =  mDb.rawQuery( "select * from "+ ZoneHelper._TABLE_NAME +" where "+ZoneHelper.ID +" = " + String.valueOf(id), null );

        if (res.moveToFirst()){
            zone = new Zone();

            zone.setId(res.getLong(res.getColumnIndex(ZoneHelper.ID)));
            zone.setDescription(res.getString(res.getColumnIndex(ZoneHelper.DESCRIPTION)));
        }

        return zone ;
    }

    @Override
    public ArrayList<Zone> getAll() {

        ArrayList<Zone> zones = new ArrayList<Zone>();
        Zone zone = null ;

        Cursor res =  mDb.rawQuery("select * from " + ZoneHelper._TABLE_NAME + " order by " + ZoneHelper.DESCRIPTION + " ", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            zone = new Zone();



            zone.setId(res.getLong(res.getColumnIndex(ZoneHelper.ID)));
            zone.setDescription(res.getString(res.getColumnIndex(ZoneHelper.DESCRIPTION)));
            /*
            try {
                zone.setDatecreation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(ZoneHelper.DATECREATION))));
            } catch (ParseException e) {
                e.printStackTrace();
            } */

            zones.add(zone);
            res.moveToNext();
        }
        return zones;
    }

}
