package caissemobile.com.mediasoft.projet.microfinacaissemobile.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import caissemobile.com.mediasoft.projet.microfinacaissemobile.R;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Agence;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Billet;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Operation;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.AgenceDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.BilletDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CaissierDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.DAOBase;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.OperationDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.PrintPDA;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.PrintPDAMobiPrint3;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.PrinterUtils;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.Utiles;

public class BilletageActivity extends AppCompatActivity {

    LinearLayout parent = null ;
    LayoutInflater inflater = null ;
    BilletDAO billetDAO = null ;
    OperationDAO operationDAO = null ;

    ArrayList<Billet> billets ;

    TextView recette = null ;
    TextView billetage = null ;
    private String agenceNom = "";
    private String agenceAdresse = "";
    private AgenceDAO agenceDAO;
    private CaissierDAO caissierDAO;
    SharedPreferences preferences ;
    private float solde;
    private Agence agence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billetage);
        setupToolbar();


        parent = (LinearLayout) findViewById(R.id.parent);
        inflater = LayoutInflater.from(this) ;
        billetDAO = new BilletDAO(this) ;
        operationDAO = new OperationDAO(this) ;
        agenceDAO = new AgenceDAO(this) ;
        caissierDAO = new CaissierDAO(this) ;
        preferences = PreferenceManager.getDefaultSharedPreferences(this) ;

        billetage = (TextView) findViewById(R.id.billetageTotal);
        recette = (TextView) findViewById(R.id.recetteTotale);


        agence = agenceDAO.getOne(caissierDAO.getLast().getAgence_id()) ;
        if (agence!=null){
            agenceNom = agence.getNomagence();
            //agenceAdresse = "BP : " + agence.ge().getBp() + "  TEL : " + agenceDAO.getLast().getTel();
        }


        ArrayList<Operation> operations = operationDAO.getAll() ;
        //solde = caissierDAO.getLast().getSolde() ;
        solde = 0 ;
        for (int i = 0; i<operations.size();++i){
            if (operations.get(i).getTypeoperation() == 0 || operations.get(i).getTypeoperation()==3 || operations.get(i).getTypeoperation()==4) solde += operations.get(i).getMontant();
            else solde -= operations.get(i).getMontant();
        }


        ////Toast.makeText(BilletageActivity.this, ""+solde, Toast.LENGTH_SHORT).show();
        recette.setText(Utiles.formatMtn(solde));
        billetage.setText("0");
        billetage() ;
    }

    private void billetage() {
        billets = billetDAO.getAll();

        for (int i = 0; i<billets.size(); ++i){
            LinearLayout billetageItem = (LinearLayout) inflater.inflate(R.layout.billetageitem, null, false);
            TextView billet = (TextView) billetageItem.getChildAt(0);
            TextView nbre = (TextView) billetageItem.getChildAt(1);
            TextView resultat = (TextView) billetageItem.getChildAt(2);

            nbre.setText("0");
            resultat.setText("0");
            billet.setText(billets.get(i).getLibelle());
            parent.addView(billetageItem);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_builletage, menu);
        return true;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        int total = 0 ;
        String msg = "" ;
        Calendar cal = Calendar.getInstance() ;

        if (id==android.R.id.home) finish();


        msg = "##############################";
        msg+= "\n";
        msg +=  "           BILLETAGE        " ;
        msg+= "\n";
        msg += "--------------------------------";
        msg+= "\n";
        msg +=  agenceNom ;
        msg+= "\n";
        msg +=  agenceAdresse ;
        msg+= "\n";
        msg += "################################";
        msg+= "\n";
        msg+= "Date      : "+ DAOBase.formatter.format(new Date());
        msg+= "\n";
        msg += "--------------------------------";
        msg+= "\n";

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_valid) {
            if (billets !=null){
                for (int i = 0; i<billets.size(); ++i){
                    LinearLayout billetageItem = (LinearLayout) parent.getChildAt(i);
                    TextView nbre = (TextView) billetageItem.getChildAt(1);
                    TextView resultat = (TextView) billetageItem.getChildAt(2);


                    resultat.setTextColor(getResources().getColor(R.color.my_divider));
                    float r = 0 ;

                    if (nbre.getText().toString().length()!=0) r = Float.valueOf(nbre.getText().toString()) * billets.get(i).getMontant() ;
                    if (r>0) {
                        ///Toast.makeText(BilletageActivity.this, ""+r, Toast.LENGTH_SHORT).show();
                        total += r ;
                        resultat.setTextColor(getResources().getColor(R.color.my_primary_dark));
                        if (Integer.valueOf(nbre.getText().toString())>1){
                            String s = new StringBuilder(billets.get(i).getLibelle()).insert(billets.get(i).getLibelle().indexOf("de") - 1, "s").toString() ;
                            msg +=   nbre.getText().toString()  + " " +  s ;
                        }
                        else msg +=   nbre.getText().toString()  + " " +   billets.get(i).getLibelle() ;
                        msg+= "\n";
                    }
                    resultat.setText(String.format("%.2f", r));
                }
                billetage.setText(Utiles.formatMtn(total));
                msg+= "\n";
                msg +=  "Solde          : " + Utiles.formatMtn(solde)+ " F";
                msg+= "\n";
                msg +=  "Nbre operation : " + String.valueOf(operationDAO.getAll().size()) ;
                msg+= "\n";
            }

            if (total == solde) {
                msg += "--------------------------------";
                msg +=  "Agence         : " + agence.getNomagence();
                msg+= "\n";
                msg +=  "Caissier       : "+ caissierDAO.getLast().getCodeguichet();
                msg+= "\n";
                msg += "--------------------------------";
                msg += "\n";
                msg += "Microfina caisse mobile";
                msg += "\n";
                msg += "################################";
                msg += "\n";
                msg += "\n";
                msg += "\n";
                msg += "\n";
                msg += "\n";
                msg += "\n";
                imprimeTicket(msg);
            }
            else{
                Toast.makeText(this, getString(R.string.billetageicorrect), Snackbar.LENGTH_LONG).show() ;
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void imprimeTicket(final String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getString(R.string.impr_confirm));
        builder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                try {
                    try {
                        if (preferences.getBoolean("imprimenteexterne",false)) {
                            PrinterUtils printerUtils = new PrinterUtils(BilletageActivity.this) ;
                            printerUtils.print(msg);
                        }
                        else{
                            //PrintPDA printPDA = new PrintPDA(BilletageActivity.this) ;
                            //printPDA.printTicket(msg);

                            PrintPDAMobiPrint3 printPDAMobiPrint3 = new PrintPDAMobiPrint3(BilletageActivity.this);
                            printPDAMobiPrint3.printTicketBillet(msg);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Log.e("Builletage", msg) ;
                }catch (Exception e){
                    e.printStackTrace();
                }
                Snackbar.make(findViewById(R.id.drLayout),getString(R.string.imprimessionlance),Snackbar.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

}
