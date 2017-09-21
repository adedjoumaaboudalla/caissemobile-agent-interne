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
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Categorie;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CategorieDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.fragments.AdhesionFragment;

public class CategorieActivity  extends AppCompatActivity {
    private SearchView mSearchView;
    ListView lv = null;
    private LayoutInflater mInflater;
    ArrayList<Categorie> categories = null ;
    ListeCategorieAdapter adapter = null ;
    private CategorieDAO categorieDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorie);

        setupToolbar();


        lv = (ListView) findViewById(R.id.list);
        mInflater = LayoutInflater.from(this) ;
        categories = new ArrayList<Categorie>() ;
        categorieDAO = new CategorieDAO(getApplicationContext()) ;
        categories = categorieDAO.getAll() ;
        adapter = new ListeCategorieAdapter(categories) ;
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent result = new Intent();
                Categorie categorie = adapter.getItem(i);
                result.putExtra(AdhesionFragment.CATEGORIE, categorie.getNumCategorie());
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
        getMenuInflater().inflate(R.menu.menu_categorie_liste, menu);

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
                filtrer(categories,newText);
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

        //noinspection SimplifiableIfStatement

        if (id==android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }


    public class ListeCategorieAdapter extends BaseAdapter {

        ArrayList<Categorie> categories = new ArrayList<Categorie>() ;

        public ListeCategorieAdapter(ArrayList<Categorie> pv){
            categories = pv ;
        }

        @Override
        public int getCount() {
            return categories.size() ;
        }

        @Override
        public Categorie getItem(int position) {
            return categories.get(position) ;
        }

        @Override
        public long getItemId(int position) {
            return categories.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if(convertView == null) {
                convertView  = mInflater.inflate(R.layout.categorie_item, null);
                holder = new ViewHolder(convertView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            Categorie categorie = (Categorie) getItem(position);
            categorie = categorieDAO.getOne(categorie.getNumCategorie()) ;

            if(categorie != null) {
                holder.libelleTV.setText(categorie.getLibelleCategorie());
                holder.montantMiniTV.setText(categorie.getLibelleFrais() + " : " + categorie.getValeurFrais());
            };

            return convertView;
        }

    }



    static class ViewHolder{
        TextView libelleTV ;
        TextView droitEntreTV ;
        TextView montantMiniTV ;

        public ViewHolder(View v) {
            libelleTV = (TextView)v.findViewById(R.id.libelleTV);
            droitEntreTV = (TextView)v.findViewById(R.id.droitEntreTV);
            montantMiniTV = (TextView)v.findViewById(R.id.montantTV);
        }
    }


    public void filtrer(ArrayList<Categorie> categories, String query){
        Categorie categorie = null ;
        ArrayList<Categorie> data = new ArrayList<Categorie>() ;

        if(categories != null)
            for(int i = 0 ; i < categories.size() ; i++){
                categorie = categories.get(i) ;
                if(categorie.getLibelleCategorie().toLowerCase().contains(query)) data.add(categorie) ;
            }
        adapter = new ListeCategorieAdapter(data) ;
        lv.setAdapter(adapter) ;
        adapter.notifyDataSetChanged();
    }

}
