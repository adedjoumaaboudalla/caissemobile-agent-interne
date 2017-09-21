package caissemobile.com.mediasoft.projet.microfinacaissemobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import caissemobile.com.mediasoft.projet.microfinacaissemobile.R;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Zone;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.ZoneDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.fragments.AdhesionFragment;

public class ZoneActivity extends AppCompatActivity {

    private SearchView mSearchView;
    ListView lv = null;
    private LayoutInflater mInflater;
    ArrayList<Zone> zones = null ;
    ListeZoneAdapter adapter = null ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone);

        setupToolbar();


        lv = (ListView) findViewById(R.id.list);
        mInflater = LayoutInflater.from(this) ;
        zones = new ArrayList<Zone>() ;
        ZoneDAO zoneDAO = new ZoneDAO(getApplicationContext()) ;
        zones = zoneDAO.getAll() ;
        adapter = new ListeZoneAdapter(zones) ;
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent result = new Intent();
                Zone zone = adapter.getItem(i) ;
                result.putExtra(AdhesionFragment.ZONE, zone.getId());
                setResult(RESULT_OK , result);
                finish();
            }
        });


    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Show menu icon
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_back);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_zone_liste, menu);

        // SearchView
        MenuItem itemSearch = menu.findItem(R.id.menu_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(itemSearch);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filtrer(zones,newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id==android.R.id.home) finish();
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }


    public class ListeZoneAdapter extends BaseAdapter {

        ArrayList<Zone> zones = new ArrayList<Zone>() ;

        public ListeZoneAdapter(ArrayList<Zone> pv){
            zones = pv ;
        }

        @Override
        public int getCount() {
            return zones.size() ;
        }

        @Override
        public Zone getItem(int position) {
            return zones.get(position) ;
        }

        @Override
        public long getItemId(int position) {
            return zones.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if(convertView == null) {
                convertView  = mInflater.inflate(R.layout.zone_item, null);
                holder = new ViewHolder(convertView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            Zone zone = (Zone) getItem(position);


            if(zone != null) {
                holder.libelleTV.setText(zone.getDescription());
            };

            return convertView;
        }

    }



    static class ViewHolder{
        TextView libelleTV ;

        public ViewHolder(View v) {
            libelleTV = (TextView)v.findViewById(R.id.libelleTV);
        }
    }


    public void filtrer(ArrayList<Zone> zones, String query){
        Zone zone = null ;
        ArrayList<Zone> data = new ArrayList<Zone>() ;

        if(zones != null)
            for(int i = 0 ; i < zones.size() ; i++){
                zone = zones.get(i) ;
                if(zone.getDescription().toLowerCase().contains(query)) data.add(zone) ;
            }
        adapter = new ListeZoneAdapter(data) ;
        lv.setAdapter(adapter) ;
        adapter.notifyDataSetChanged();
    }


}
