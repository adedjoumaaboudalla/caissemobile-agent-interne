package caissemobile.com.mediasoft.projet.microfinacaissemobile.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import caissemobile.com.mediasoft.projet.microfinacaissemobile.R;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Compte;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Credit;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CreditDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.DAOBase;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.fragments.RembroussementFragment;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.Url;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.Utiles;

public class CreditActivity extends AppCompatActivity {
    private static final int MAX_SIZE = 2;
    private SearchView mSearchView;
    private static final int PROGRESS_DIALOG_ID = 1;
    ListView lv = null;
    private LayoutInflater mInflater;
    ListeCreditAdapter adapter = null ;
    private ProgressDialog mProgressBar;
    private ArrayList<Credit> credits;
    private String nocompte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);

        setupToolbar();


        lv = (ListView) findViewById(R.id.list);
        mInflater = LayoutInflater.from(this) ;
        credits = new ArrayList<Credit>() ;

        adapter = new ListeCreditAdapter(credits) ;
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent result = new Intent();
                Credit credit = adapter.getItem(i);
                Log.e("CREDIT", String.valueOf(credit.getId()));
                result.putExtra(RembroussementFragment.CREDIT, credit.getId());
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
        getMenuInflater().inflate(R.menu.menu_credit, menu);

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
                filtrer(credits,newText);
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

        if (id == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }


    public class ListeCreditAdapter extends BaseAdapter {

        ArrayList<Credit> Credits = new ArrayList<Credit>() ;

        public ListeCreditAdapter(ArrayList<Credit> pv){
            Credits = pv ;
        }

        @Override
        public int getCount() {
            return Credits.size() ;
        }

        @Override
        public Credit getItem(int position) {
            return Credits.get(position) ;
        }

        @Override
        public long getItemId(int position) {
            return Credits.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if(convertView == null) {
                convertView  = mInflater.inflate(R.layout.credit_item, null);
                holder = new ViewHolder(convertView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            Credit credit = (Credit) getItem(position);


            if(credit != null) {
                holder.numcredit.setText(credit.getNumcredit());
                if (credit.getDatedeblocage()!=null)holder.date.setText(DAOBase.formatterj.format(credit.getDatedeblocage()));
                else holder.date.setText("");
                holder.mtn.setText("Mte "+ credit.getMontantpret());
                holder.mensualite.setText("Mensulité "+ credit.getMensualite());
                holder.nomprenom.setText(credit.getNomproduit());
            };

            return convertView;
        }

    }



    static class ViewHolder{
        TextView numcredit ;
        TextView date ;
        TextView mtn ;
        TextView mensualite ;
        TextView nomprenom ;

        public ViewHolder(View v) {
            numcredit = (TextView)v.findViewById(R.id.numcredit);
            date = (TextView)v.findViewById(R.id.date);
            mtn = (TextView)v.findViewById(R.id.mtn);
            mensualite = (TextView)v.findViewById(R.id.mensualite);
            nomprenom = (TextView)v.findViewById(R.id.nomprenom);
        }
    }


    public void filtrer(ArrayList<Credit> Credits, String query){
        Credit credit = null ;
        ArrayList<Credit> data = new ArrayList<Credit>() ;

        if(credit != null)
            for(int i = 0 ; i < Credits.size() ; i++){
                credit = Credits.get(i) ;
                if(credit.getNumcredit().toLowerCase().contains(query)) data.add(credit) ;
            }
        adapter = new ListeCreditAdapter(data) ;
        lv.setAdapter(adapter) ;
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent() ;
        if (intent!=null){
            nocompte = intent.getStringExtra(Compte.CREDIT) ;
            if (nocompte!=null){
                ChargementCredit chargementCredit = new ChargementCredit(nocompte) ;
                chargementCredit.execute(Url.getListeCreditUrl(this)) ;
            }
            else{
                ChargementCredit chargementCredit = new ChargementCredit() ;
                chargementCredit.execute() ;
            }
        }

    }



    public class ChargementCredit extends AsyncTask<String,Void,String> {

        CreditDAO creditDAO = null ;
        Credit c  =null;
        String nocompte = null ;
        JSONArray creditArray = null ;
        private SharedPreferences preferences;

        public ChargementCredit(String nocompte) {
            this.nocompte = nocompte ;
        }
        public ChargementCredit() {
            this.nocompte = null ;
        }

        @Override
        protected String doInBackground(String... urls) {
            //TelephonyManager telephonyManager =  ( TelephonyManager ) getSystemService (Context.TELEPHONY_SERVICE);
            //String imei = telephonyManager.getDeviceId ();


            if (this.nocompte==null) credits = creditDAO.getAll() ;
            else {
                credits = creditDAO.getOne(nocompte.substring(0,nocompte.length()-3)) ;
            }
            Log.e("DEBUG", String.valueOf(credits.size())) ;
            return "" ;
        }


        @Override
        protected void onPostExecute(String res) {
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            super.onPostExecute(res);

            adapter = new ListeCreditAdapter(credits) ;
            lv.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            if (credits.size()==0)  Toast.makeText(CreditActivity.this, R.string.anyresult, Toast.LENGTH_SHORT).show();

            if (credits.size()==1){
                Intent result = new Intent();
                Credit credit = credits.get(0);
                Log.e("CREDIT", String.valueOf(credit.getId()));
                result.putExtra(RembroussementFragment.CREDIT, credit.getId());
                setResult(RESULT_OK, result);
                finish();
            }
            try {
                dismissDialog(PROGRESS_DIALOG_ID);
            }catch (Exception e){
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
            if (creditDAO==null) creditDAO = new CreditDAO(CreditActivity.this) ;
            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()) ;
        }

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        super.onCreateDialog(id);

        if (mProgressBar == null) {
            mProgressBar = new ProgressDialog(this);
            mProgressBar.setCancelable(true);
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
