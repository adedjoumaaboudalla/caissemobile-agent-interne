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
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Profession;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.ProfessionDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.fragments.AdhesionFragment;

public class ProfessionActivity extends AppCompatActivity {

    private SearchView mSearchView;
    ListView lv = null;
    private LayoutInflater mInflater;
    ArrayList<Profession> professions = null ;
    ListeProfessionAdapter adapter = null ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profession);

        setupToolbar();


        lv = (ListView) findViewById(R.id.list);
        mInflater = LayoutInflater.from(this) ;
        professions = new ArrayList<Profession>() ;
        ProfessionDAO professionDAO = new ProfessionDAO(getApplicationContext()) ;
        professions = professionDAO.getAll() ;
        adapter = new ListeProfessionAdapter(professions) ;
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent result = new Intent();
                Profession profession = adapter.getItem(i) ;
                result.putExtra(AdhesionFragment.PROFESSION, profession.getNumprofession());
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
        getMenuInflater().inflate(R.menu.menu_profession_liste, menu);

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
                filtrer(professions,newText);
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


    public class ListeProfessionAdapter extends BaseAdapter {

        ArrayList<Profession> professions = new ArrayList<Profession>() ;

        public ListeProfessionAdapter(ArrayList<Profession> pv){
            professions = pv ;
        }

        @Override
        public int getCount() {
            return professions.size() ;
        }

        @Override
        public Profession getItem(int position) {
            return professions.get(position) ;
        }

        @Override
        public long getItemId(int position) {
            return professions.get(position).getNumprofession();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if(convertView == null) {
                convertView  = mInflater.inflate(R.layout.profession_item, null);
                holder = new ViewHolder(convertView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            Profession profession = (Profession) getItem(position);


            if(profession != null) {
                holder.libelleTV.setText(profession.getLibelle());
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


    public void filtrer(ArrayList<Profession> professions, String query){
        Profession profession = null ;
        ArrayList<Profession> data = new ArrayList<Profession>() ;

        if(professions != null)
            for(int i = 0 ; i < professions.size() ; i++){
                profession = professions.get(i) ;
                if(profession.getLibelle().toLowerCase().contains(query)) data.add(profession) ;
            }
        adapter = new ListeProfessionAdapter(data) ;
        lv.setAdapter(adapter) ;
        adapter.notifyDataSetChanged();
    }


}
