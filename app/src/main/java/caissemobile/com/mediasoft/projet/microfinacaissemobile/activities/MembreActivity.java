package caissemobile.com.mediasoft.projet.microfinacaissemobile.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import caissemobile.com.mediasoft.projet.microfinacaissemobile.R;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Membre;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.MembreDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.Utiles;

public class MembreActivity  extends AppCompatActivity {
    private static final int MAX_SIZE = 0;
    private SearchView mSearchView;
    ListView lv = null;
    private LayoutInflater mInflater;
    ArrayList<Membre> membres = null ;
    ListeMembreAdapter adapter = null ;
    private MembreDAO membreDAO;
    private ProgressDialog mProgressBar;
    private int type = 2;
    private String nummembre = "";
    private TextView total;
    private TextView nbre;
    private Button valider;
    private Button annuler;
    private float t = 0;
    private String numcompte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membre);

        setupToolbar();


        Intent intent = getIntent() ;
        if (intent!=null){
            nummembre = intent.getStringExtra("nummembre") ;
            numcompte = intent.getStringExtra("numcompte") ;
        }
        lv = (ListView) findViewById(R.id.list);
        mInflater = LayoutInflater.from(this) ;
        membres = new ArrayList<Membre>() ;
        membreDAO = new MembreDAO(getApplicationContext()) ;

        Log.e("NUM MEMBRE",nummembre) ;
        membres = membreDAO.getAllByNumMembre(nummembre) ;
        adapter = new ListeMembreAdapter(membres) ;
        lv.setAdapter(adapter);

        total = (TextView) findViewById(R.id.total);
        nbre = (TextView) findViewById(R.id.nbre);
        refresh();

        valider = (Button) findViewById(R.id.valider);
        annuler = (Button) findViewById(R.id.annuler);

        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //membreDAO.deleteLasts() ;
                finish();
            }
        });

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("montant",t);
                intent.putExtra("numcompte",numcompte);
                setResult(RESULT_OK , intent);
                finish();
            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Membre membre = adapter.getItem(i) ;
                AlertDialog.Builder builder = new AlertDialog.Builder(MembreActivity.this) ;
                LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(R.layout.membrepanel,null);
                final EditText montant = (EditText) ll.findViewById(R.id.montant);
                final TextView solde = (TextView) ll.findViewById(R.id.solde);
                solde.setText(getString(R.string.soldecpt) + Utiles.formatMtn(membre.getMontant()));

                montant.setText(String.valueOf(membre.getSaisi()));
                builder.setView(ll) ;
                builder.setPositiveButton(R.string.valider, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (montant.getText().toString().length()>0){
                            membre.setSaisi(Float.valueOf(montant.getText().toString()));
                            membreDAO.update(membre) ;
                            refresh() ;
                        }
                    }
                }) ;
                builder.setNegativeButton(R.string.annuler, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }) ;
                builder.setTitle(getString(R.string.mtcotise)) ;
                final AlertDialog alertdialog = builder.create();
                alertdialog.setOnShowListener( new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        alertdialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.my_accent));
                        alertdialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.my_accent));
                    }
                }) ;
                alertdialog.show();

            }
        });
    }

    private void refresh() {
        membres = membreDAO.getAllByNumMembre(nummembre) ;
        adapter.notifyDataSetChanged();
        nbre.setText(membres.size()+"");
        t = 0 ;
        for (int i = 0; i < membres.size(); i++) {
            t += membres.get(i).getSaisi() ;
        }
        //Toast.makeText(MembreActivity.this, ""+t, Toast.LENGTH_SHORT).show();
        total.setText(Utiles.formatMtn(t));
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
        getMenuInflater().inflate(R.menu.menu_membre2, menu);

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
    protected void onResume() {
        super.onResume();
        ChargementMembre chargementMembre = new ChargementMembre() ;
        chargementMembre.execute() ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menu_add) {
            appliquerAtous() ;
        };
        if (id == android.R.id.home) finish();
        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    private void appliquerAtous() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MembreActivity.this) ;
        LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(R.layout.appliquertouspanel,null);
        final EditText montant = (EditText) ll.findViewById(R.id.montant);

        builder.setView(ll) ;
        builder.setPositiveButton(R.string.valider, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (montant.getText().length()>0)
                for (int i = 0; i < membres.size(); i++) {
                    membres.get(i).setSaisi(Float.valueOf(montant.getText().toString()));
                    membreDAO.update(membres.get(i)) ;
                }
                refresh() ;
            }
        }) ;
        builder.setNegativeButton(R.string.annuler, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }) ;
        builder.setTitle(getString(R.string.mtcotise)) ;
        final AlertDialog alertdialog = builder.create();
        alertdialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alertdialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.my_accent));
                alertdialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.my_accent));
            }
        }) ;
        alertdialog.show();
    }


    public class ListeMembreAdapter extends BaseAdapter {

        ArrayList<Membre> membres = new ArrayList<Membre>() ;

        public ListeMembreAdapter(ArrayList<Membre> pv){
            membres = pv ;
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
                holder.membre.setText(membre.getCodemembre());
                holder.montant.setText("Mte : " + Utiles.formatMtn(membre.getSaisi()));
                holder.nomprenom.setText(membre.getNom());
            };

            return convertView;
        }

    }



    static class ViewHolder{
        TextView membre ;
        TextView montant ;
        TextView nomprenom ;

        public ViewHolder(View v) {
            membre = (TextView)v.findViewById(R.id.nummenbre);
            montant = (TextView)v.findViewById(R.id.date);
            nomprenom = (TextView)v.findViewById(R.id.categorie);
        }
    }


    public void filtrer(ArrayList<Membre> membres, String query){
        Membre membre = null ;
        ArrayList<Membre> data = new ArrayList<Membre>() ;

        query = query.toLowerCase() ;
        if(membres != null)
            for(int i = 0 ; i < membres.size() ; i++){
                membre = membres.get(i) ;
                if(membre.getNom().toLowerCase().contains(query) || membre.getNummembre().toLowerCase().contains(query)) data.add(membre) ;
            }
        adapter = new ListeMembreAdapter(data) ;
        lv.setAdapter(adapter) ;
        adapter.notifyDataSetChanged();
    }

    public class ChargementMembre extends AsyncTask<String,Void,String> {

        private static final int PROGRESS_DIALOG_ID = 0;
        MembreDAO membreDAO = null ;
        Membre c  =null;

        @Override
        protected String doInBackground(String... urls) {
            //TelephonyManager telephonyManager =  ( TelephonyManager ) getSystemService (Context.TELEPHONY_SERVICE);
            //String imei = telephonyManager.getDeviceId ();

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
            if (membreDAO==null) membreDAO = new MembreDAO(MembreActivity.this) ;

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
