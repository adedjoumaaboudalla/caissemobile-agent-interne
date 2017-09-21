package caissemobile.com.mediasoft.projet.microfinacaissemobile.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Compte;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Membre;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CategorieDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CompteDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CreditDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.DAOBase;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.MembreDAO;

public class ListeMembreActivity extends AppCompatActivity {

    private SearchView mSearchView;
    ListView lv = null;
    private LayoutInflater mInflater;
    ArrayList<Membre> membres = null ;
    ListeMembreAdapter adapter = null ;
    private ProgressDialog mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_membre);

        setupToolbar();


        lv = (ListView) findViewById(R.id.list);
        mInflater = LayoutInflater.from(this) ;
        membres = new ArrayList<Membre>() ;

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Membre membre = adapter.getItem(position) ;
                intent.putExtra("id",membre.getId()) ;
                setResult(RESULT_OK , intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ChargementMembre chargementMembre = new ChargementMembre() ;
        chargementMembre.execute() ;
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
        getMenuInflater().inflate(R.menu.menu_membre_liste, menu);

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
                filtrer(membres,newText);
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


    public class ListeMembreAdapter extends BaseAdapter {

        ArrayList<Membre> membres = new ArrayList<Membre>() ;
        private CategorieDAO categorieDAO;

        public ListeMembreAdapter(ArrayList<Membre> pv){
            membres = pv ;
            categorieDAO = new CategorieDAO(getApplicationContext()) ;
        }

        @Override
        public int getCount() {
            return membres.size() ;
        }

        @Override
        public Membre getItem(int position) {
            return membres.get(position) ;
        }

        @Override
        public long getItemId(int position) {
            return membres.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if(convertView == null) {
                convertView  = mInflater.inflate(R.layout.membre_item, null);
                holder = new ViewHolder(convertView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            Membre membre = (Membre) getItem(position);


            if(membre != null) {
                holder.categorieTV.setText(membre.getNom() + " " + membre.getPrenom());
                holder.nummembreTV.setText(membre.getNummembre());
                holder.dateTV.setText(DAOBase.formatterj.format(membre.getDateadhesion()));
            };

            Log.e("DATE NAISS", DAOBase.formatterj.format(membre.getDateNaiss())) ;

            return convertView;
        }

    }



    static class ViewHolder{
        TextView categorieTV ;
        TextView dateTV ;
        TextView nummembreTV ;

        public ViewHolder(View v) {
            categorieTV = (TextView)v.findViewById(R.id.categorie);
            dateTV = (TextView)v.findViewById(R.id.date);
            nummembreTV = (TextView)v.findViewById(R.id.nummenbre);
        }
    }


    public void filtrer(ArrayList<Membre> membres, String query){
        Membre membre = null ;
        ArrayList<Membre> data = new ArrayList<Membre>() ;

        query = query.toLowerCase() ;
        if(membres != null)
            for(int i = 0 ; i < membres.size() ; i++){
                membre = membres.get(i) ;
                if(membre.getNom().toLowerCase().contains(query) || membre.getPrenom().toLowerCase().contains(query) || membre.getAdresse().toLowerCase().contains(query)) data.add(membre) ;
            }
        adapter = new ListeMembreAdapter(data) ;
        lv.setAdapter(adapter) ;
        adapter.notifyDataSetChanged();
    }



    public class ChargementMembre extends AsyncTask<String,Void,String> {

        private static final int PROGRESS_DIALOG_ID = 0;
        CompteDAO compteDAO = null ;
        Compte c  =null;
        private CreditDAO creditDAO;

        @Override
        protected String doInBackground(String... urls) {
            //TelephonyManager telephonyManager =  ( TelephonyManager ) getSystemService (Context.TELEPHONY_SERVICE);
            //String imei = telephonyManager.getDeviceId ();

            MembreDAO membreDAO = new MembreDAO(getApplicationContext()) ;
            membres = membreDAO.getAllIndividuLibre() ;

            Log.e("DEBUG", String.valueOf(membres.size())) ;
            return "" ;
        }


        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            super.onPostExecute(result);

            adapter = new ListeMembreAdapter(membres) ;
            lv.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            try {
                dismissDialog(PROGRESS_DIALOG_ID);
            }catch(Exception e){
                e.printStackTrace();
            }
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG_ID);

        }

    }



    @Override
    protected Dialog onCreateDialog(int id) {
        super.onCreateDialog(id);

        if (mProgressBar == null) {
            mProgressBar = new ProgressDialog(this);
            mProgressBar.setCancelable(false);
            mProgressBar.setTitle(

                    getString(R.string.loding)

            );
            mProgressBar.setMessage(
                    getString(R.string.wait)
            );
            mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }

        return mProgressBar;
    }


}
