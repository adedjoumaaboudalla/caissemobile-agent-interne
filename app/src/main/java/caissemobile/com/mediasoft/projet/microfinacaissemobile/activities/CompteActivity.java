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

import caissemobile.com.mediasoft.projet.microfinacaissemobile.MainActivity;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.R;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Compte;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Credit;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CompteDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CreditDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.DAOBase;

public class CompteActivity  extends AppCompatActivity {
    private static final int MAX_SIZE = 0;
    private SearchView mSearchView;
    ListView lv = null;
    private LayoutInflater mInflater;
    ArrayList<Compte> comptes = null ;
    ListeCompteAdapter adapter = null ;
    private CompteDAO compteDAO;
    private ProgressDialog mProgressBar;
    private int type = 2;
    private boolean remb = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compte);

        setupToolbar();


        Intent intent = getIntent() ;
        if (intent!=null){
            if (intent.getStringExtra("TYPE").equals(Compte.EPARGNE)) type = 0 ;
            else if (intent.getStringExtra("TYPE").equals(Compte.TONTINE)) type = 1 ;
            else  type = 2 ;

            remb = intent.getBooleanExtra("REMBOURSSEMENT",false) ;

            if (type==0)setTitle("Compte");
            if (type==1)setTitle("Carnet");
        }
        lv = (ListView) findViewById(R.id.list);
        mInflater = LayoutInflater.from(this) ;
        comptes = new ArrayList<Compte>() ;
        compteDAO = new CompteDAO(getApplicationContext()) ;

        adapter = new ListeCompteAdapter(comptes) ;
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent result = new Intent();
                Compte compte = adapter.getItem(i);
                result.putExtra(MainActivity.COMPTE, compte.getId());
                Log.e("DEBUG", String.valueOf(compte.getId())) ;
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
        getMenuInflater().inflate(R.menu.menu_compte, menu);

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
                filtrer(comptes,newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ChargementCompte chargementCompte = new ChargementCompte() ;
        chargementCompte.execute() ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) finish();
        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }


    public class ListeCompteAdapter extends BaseAdapter {

        ArrayList<Compte> comptes = new ArrayList<Compte>() ;

        public ListeCompteAdapter(ArrayList<Compte> pv){
            comptes = pv ;
        }

        @Override
        public int getCount() {
            return comptes.size() ;
        }

        @Override
        public Compte getItem(int position) {
            return comptes.get(position) ;
        }

        @Override
        public long getItemId(int position) {
            return comptes.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if(convertView == null) {
                convertView  = mInflater.inflate(R.layout.compte_item, null);
                holder = new ViewHolder(convertView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            Compte compte = (Compte) getItem(position);

            if(compte != null) {
                holder.compte.setText(compte.getNumcompte());
                if (compte.getDatecreation()!=null)holder.date.setText(DAOBase.formatter5.format(compte.getDatecreation()));
                if (compte.getNumcompte().contains("/G/")) holder.nomprenom.setText(compte.getNom());
                else  holder.nomprenom.setText(compte.getNom() + " " + compte.getPrenom());
            };

            return convertView;
        }

    }



    static class ViewHolder{
        TextView compte ;
        TextView date ;
        TextView nomprenom ;

        public ViewHolder(View v) {
            compte = (TextView)v.findViewById(R.id.compte);
            date = (TextView)v.findViewById(R.id.date);
            nomprenom = (TextView)v.findViewById(R.id.nomprenom);
        }
    }


    public void filtrer(ArrayList<Compte> comptes, String query){
        Compte compte = null ;
        ArrayList<Compte> data = new ArrayList<Compte>() ;

        query = query.toLowerCase() ;
        if(comptes != null)
            for(int i = 0 ; i < comptes.size() ; i++){
                compte = comptes.get(i) ;
                if(compte.getNom().toLowerCase().contains(query) || compte.getPrenom().toLowerCase().contains(query)||compte.getNumcompte().toLowerCase().contains(query)) data.add(compte) ;
            }
        adapter = new ListeCompteAdapter(data) ;
        lv.setAdapter(adapter) ;
        adapter.notifyDataSetChanged();
    }

    public class ChargementCompte extends AsyncTask<String,Void,String> {

        private static final int PROGRESS_DIALOG_ID = 0;
        CompteDAO compteDAO = null ;
        Compte c  =null;
        private CreditDAO creditDAO;

        @Override
        protected String doInBackground(String... urls) {
            //TelephonyManager telephonyManager =  ( TelephonyManager ) getSystemService (Context.TELEPHONY_SERVICE);
            //String imei = telephonyManager.getDeviceId ();

            if (!remb)  comptes = compteDAO.getAll(type) ;
            else   comptes = compteDAO.getAllRemb(type) ;

            Log.e("DEBUG", String.valueOf(comptes.size())) ;
            return "" ;
        }


        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            super.onPostExecute(result);

            adapter = new ListeCompteAdapter(comptes) ;
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
            if (compteDAO==null) compteDAO = new CompteDAO(CompteActivity.this) ;
            if (creditDAO==null) creditDAO = new CreditDAO(CompteActivity.this) ;

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
            mProgressBar.setMax(MAX_SIZE);
        }

        return mProgressBar;
    }
}
