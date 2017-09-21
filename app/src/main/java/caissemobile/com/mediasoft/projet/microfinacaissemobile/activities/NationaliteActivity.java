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
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Nationalite;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.NationaliteDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.fragments.AdhesionFragment;

public class NationaliteActivity  extends AppCompatActivity {

    private SearchView mSearchView;
    ListView lv = null;
    private LayoutInflater mInflater;
    ArrayList<Nationalite> nationalites = null ;
    ListeNationaliteAdapter adapter = null ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nationalite);

        setupToolbar();


        lv = (ListView) findViewById(R.id.list);
        mInflater = LayoutInflater.from(this) ;
        nationalites = new ArrayList<Nationalite>() ;
        NationaliteDAO nationaliteDAO = new NationaliteDAO(getApplicationContext()) ;
        nationalites = nationaliteDAO.getAll() ;
        adapter = new ListeNationaliteAdapter(nationalites) ;
        lv.setAdapter(adapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent result = new Intent();
                Nationalite nationalite = adapter.getItem(i);
                result.putExtra(AdhesionFragment.NATIONALITE, nationalite.getNumnation());
                setResult(RESULT_OK, result);
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
        getMenuInflater().inflate(R.menu.menu_nationalite_liste, menu);

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
                filtrer(nationalites,newText);
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


    public class ListeNationaliteAdapter extends BaseAdapter {

        ArrayList<Nationalite> nationalites = new ArrayList<Nationalite>() ;

        public ListeNationaliteAdapter(ArrayList<Nationalite> pv){
            nationalites = pv ;
        }

        @Override
        public int getCount() {
            return nationalites.size() ;
        }

        @Override
        public Nationalite getItem(int position) {
            return nationalites.get(position) ;
        }

        @Override
        public long getItemId(int position) {
            return nationalites.get(position).getNumnation();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if(convertView == null) {
                convertView  = mInflater.inflate(R.layout.nationalite_item, null);
                holder = new ViewHolder(convertView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            Nationalite nationalite = (Nationalite) getItem(position);


            if(nationalite != null) {
                holder.libelleTV.setText(nationalite.getLibelle());
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


    public void filtrer(ArrayList<Nationalite> nationalites, String query){
        Nationalite nationalite = null ;
        ArrayList<Nationalite> data = new ArrayList<Nationalite>() ;

        if(nationalites != null)
            for(int i = 0 ; i < nationalites.size() ; i++){
                nationalite = nationalites.get(i) ;
                if(nationalite.getLibelle().toLowerCase().contains(query)) data.add(nationalite) ;
            }
        adapter = new ListeNationaliteAdapter(data) ;
        lv.setAdapter(adapter) ;
        adapter.notifyDataSetChanged();
    }


}
