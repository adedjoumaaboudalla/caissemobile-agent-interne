package caissemobile.com.mediasoft.projet.microfinacaissemobile.services;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;


import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import caissemobile.com.mediasoft.projet.microfinacaissemobile.R;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.activities.ConnexionActivity;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Caissier;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Membre;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Operation;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.OperationSec;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CaissierDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CategorieDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CompteDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.DAOBase;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.MembreDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.OperationDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.OperationSecDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.database.CaissierHelper;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.database.MembreHelper;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.Url;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.Utiles;
import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by mediasoft on 17/11/2016.
 */
public class OperationSyncAdapter extends AbstractThreadedSyncAdapter {

    private static final int ID_NOTIFICATION = 1;
    private final Context context;
    private final SharedPreferences preferences;
    private ArrayList<OperationSec> operations;
    private OperationDAO operationDAO;
    private OperationSecDAO operationSecDAO;
    private int etape = 0 ;
    private CompteDAO compteDAO;
    private MembreDAO membreDAO;
    private CaissierDAO caissierDAO;
    private CategorieDAO categorieDAO;


    public OperationSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        preferences = PreferenceManager.getDefaultSharedPreferences(context) ;
        this.context = context ;
        initialisation();
    }

    private void initialisation() {
        operationDAO = new OperationDAO(context) ;
        operationSecDAO = new OperationSecDAO(context) ;
        compteDAO = new CompteDAO(context) ;
        membreDAO = new MembreDAO(context) ;
        caissierDAO = new CaissierDAO(context) ;
        categorieDAO = new CategorieDAO(context) ;
    }



    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.e("OPERATION TMP","SYNC") ;
        if (compteDAO.getAll().size()==0) return;
        etape = 1 ;
        transfert();
    }



    private void transfert() {

        int sync = getSyncData() ;
        if (sync==0 && etape==1) {
            //makeAlertDialog(getString(R.string.nosyncdata),false);
            return;
        }

        if (operationDAO.getNonSync().size() != 0 && etape == 1){
            Log.e("SYNC","OPERATION LANCE") ;
            sendOperation();
            return;
        }
        else if (etape==1)etape = 2 ;


        if (operationDAO.getNonSync().size() != 0 && etape == 2){
            Log.e("SYNC","PARTENAIRE LANCE") ;
            sendMembre();
            return;
        }
        else if (etape==2) etape = 3 ;


        if (operationDAO.getNonSync().size() != 0 && etape == 3){
            Log.e("SYNC","REMBOURESEMENT") ;
            sendRemboursement();
            return;
        }
        else if (etape==3) etape = 4 ;


        if (membreDAO.getNonPhotoSync().size() != 0 && etape == 4){
            Log.e("SYNC","PHOTO LANCE") ;
            sendPhoto();
            return;
        }
        else if (etape==4) etape = 5 ;

        Log.e("PHOTO NON", String.valueOf(membreDAO.getNonPhotoSync().size())) ;




        sync = getSyncData() ;

        if (sync>0) {

        }
        else {
            etape=1 ;
            //makeAlertDialog(getString(R.string.trsuccess),false);
        }

    }

    private int getSyncData() {
        return  operationDAO.getNonSync().size()  + membreDAO.getNonPhotoSync().size();
    }



    public void sendOperation() {
        Caissier c  =null;
        ArrayList<Operation> operations;
        Operation operation ;
        String result;

        c = new CaissierDAO(context).getLast() ;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context) ;

        Log.e("DEBUG","ETAT") ;
        operations = operationDAO.getNonSync();

        String typop = "" ;
        Log.e("DEBUG", String.valueOf(operations.size())) ;


        WifiManager wifiManager = null;
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        String macAddress ="";
        if(wInfo!=null)macAddress = wInfo.getMacAddress();

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String iMei = null ;
        iMei = telephonyManager.getDeviceId();
        try {
            Log.d("IMEI", iMei);
            Log.d("MACADRESS", macAddress);
        }catch (Exception e){
            e.printStackTrace();
        }


        FormBody.Builder formBuilder = null;

        for (int j = 0;j<operations.size();++j){
            operation = operations.get(j) ;
            if (operation.getTypeoperation()>=3) continue;
            formBuilder = new FormBody.Builder();

            formBuilder.add("iMei", iMei) ;
            formBuilder.add("macAddress", macAddress);

            formBuilder.add("NumCompte", operation.getNumcompte());
            formBuilder.add("interface", "E");
            formBuilder.add("NumProduit", operation.getNumproduit());
            formBuilder.add("journee", DAOBase.formatterj.format(operation.getDateoperation()));
            formBuilder.add("montant1", Utiles.formatMtn2(operation.getMontant()));
            formBuilder.add("montant2", Utiles.formatMtn2(operation.getMontant()));
            if (operation.getTypeoperation()==0)formBuilder.add("TypeOperation", "VSE");
            else formBuilder.add("TypeOperation", "RE");
            formBuilder.add("mise", "0");

            formBuilder.add("AgenceClient", operation.getAgence());
            formBuilder.add("guichet", String.valueOf(c.getCodeguichet()));
            formBuilder.add("numagence", String.valueOf(c.getAgence_id()));


            formBuilder.add("ddb", c.getDdb());
            formBuilder.add("dl", c.getDl());
            formBuilder.add("dp", c.getDp());
            formBuilder.add("di", c.getDi());
            formBuilder.add("forcer","0") ;


            ArrayList<OperationSec> operationSecs = operationSecDAO.getAllByNumOperation(operation.getId());;
            int n = operationSecs.size();
            Log.e("NBRE SOUS OP", String.valueOf(n)) ;
            formBuilder.add("nbre", String.valueOf(n));
            if (n > 0)
                for (int i = 0; i < n; i++) {
                    if (operationSecs.get(i).getMte() <= 0) continue;
                    formBuilder.add("nummembre" + i, String.valueOf(operationSecs.get(i).getNummembre()));
                    formBuilder.add("mte" + i, Utiles.formatMtn2(operationSecs.get(i).getMte()));
                    formBuilder.add("idpersonne" + i, operationSecs.get(i).getIdpersonne());
                }


            result = "" ;
            try {
                result = Utiles.POST(Url.getSendOperationUrl(context),formBuilder.build());
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.e("RESULT OPERATION", result) ;


            if (result.equals("NUMPIECE")) Log.e("RESULT",context.getString(R.string.echec1));
            if (result.equals("AGENCEDIFF")) Log.e("RESULT",context.getString(R.string.echec));
            if (result.equals("NUMPECEECHEC1")) Log.e("RESULT",context.getString(R.string.echec2));
            if (result.equals("NUMPECEECHEC2")) Log.e("RESULT",context.getString(R.string.echec3));
            if (result.equals("NUMPECEECHEC3")) Log.e("RESULT", context.getString(R.string.echec4));
            else if (result.contains("OK:") && result.split(":").length>=3){
                operation.setNumpicedef(result.split(":")[1]);
                operation.setSync(1);
                operationDAO.update(operation) ;
            }
        }

        etape = 2 ;
        transfert() ;
    }




    private void sendMembre() {

        Membre membre = null ;
        ArrayList<Operation> operations;
        Operation operation ;
        String result;

        CaissierDAO caissierDAO = null ;
        categorieDAO = new CategorieDAO(context) ;
        Caissier c  =null;


        // Find your Account Sid and Token at twilio.com/user/account

        c = new CaissierDAO(context).getLast() ;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context) ;

        operations = operationDAO.getNonSync();

        String typop = "" ;


        Log.e("SIZE MEMBRE", String.valueOf(operations.size())) ;
        for (int j = 0;j<operations.size();++j) {
            operation = operations.get(j);
            Log.e("TYPE OP", String.valueOf(operation.getTypeoperation())) ;
            if (operation.getTypeoperation()!=3) continue;
            membre = membreDAO.getOne(operation.getUser_id()) ;

            String tel = membre.getTel() ;

            if (!tel.startsWith("00229") && !tel.startsWith("+229")) tel =  "+229"+ tel ;
            if (tel.startsWith("00229")) tel = tel.replace("00229","+229") ;

            WifiManager wifiManager = null;
            wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wInfo = wifiManager.getConnectionInfo();
            String macAddress ="";
            if(wInfo!=null)macAddress = wInfo.getMacAddress();

            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String iMei = null ;
            iMei = telephonyManager.getDeviceId();
            try {
                Log.d("IMEI", iMei);
                Log.d("MACADRESS", macAddress);
            }catch (Exception e){
                e.printStackTrace();
            }

            FormBody.Builder formBuilder = new FormBody.Builder();
            formBuilder.add("guichet",String.valueOf(c.getCodeguichet()));
            formBuilder.add("iMei", iMei) ;
            formBuilder.add("macAddress", macAddress);
            formBuilder.add("numproduit",operation.getNumproduit());
            formBuilder.add("journee", DAOBase.formatterj.format(operation.getDateoperation()));
            formBuilder.add("Mt_Operation", String.valueOf(membre.getMontant()));
            formBuilder.add("NumCategorie",String.valueOf(categorieDAO.getOne(membre.getCategorie()).getNumCategorie()));
            formBuilder.add("NumProfession",String.valueOf(membre.getProfession()));
            formBuilder.add("NumNationalite",String.valueOf(membre.getNationalite()));
            formBuilder.add(MembreHelper.NOM, membre.getNom());
            formBuilder.add(MembreHelper.PRENOM,membre.getPrenom());
            formBuilder.add(MembreHelper.SEXE,membre.getSexe());
            formBuilder.add(MembreHelper.TEL,tel);
            formBuilder.add(MembreHelper.ADRESSE,membre.getAdresse());
            formBuilder.add(MembreHelper.DATE, DAOBase.formatterj.format(membre.getDateadhesion()));
            formBuilder.add(MembreHelper.DATENAISS, DAOBase.formatterj.format(membre.getDateNaiss()));
            formBuilder.add("NumZone",membre.getZone());
            formBuilder.add("codeGuichet", String.valueOf(c.getCodeguichet()));
            formBuilder.add("forcer","0") ;

            formBuilder.add("lat","0");
            formBuilder.add("long","0");

            ArrayList<Membre> membreArrayList = membreDAO.getAllByNumMembre(String.valueOf(membre.getId()));

            Log.e("NOMBRE DE MEMBRE",String.valueOf(membreArrayList.size())) ;

            formBuilder.add("nbre",String.valueOf(membreArrayList.size()));

            for (int i = 0; i < membreArrayList.size(); i++) {
                formBuilder.add("nummembre"+i, membreArrayList.get(i).getNummembre());
                formBuilder.add("nom"+i, membreArrayList.get(i).getNom());
                formBuilder.add("prenom"+i, membreArrayList.get(i).getPrenom());
                formBuilder.add("poste"+i, membreArrayList.get(i).getPoste());
                formBuilder.add("activite"+i, membreArrayList.get(i).getActivite());
                formBuilder.add("sexe"+i, membreArrayList.get(i).getSexe());
                formBuilder.add("datenaiss"+i, DAOBase.formatterj.format(membreArrayList.get(i).getDateNaiss()));
                formBuilder.add("tel"+i, membreArrayList.get(i).getTel());
                formBuilder.add("adresse"+i, membreArrayList.get(i).getAdresse());
            }

            String url = "" ;
            if (membreArrayList.size()>0) url = Url.getAddMembreGroupeUrl(context);
            else url = Url.getAddMembreUrl(context);



            result = "" ;

            try {
                result = Utiles.POST(url,formBuilder.build());
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.e("RESULT MEMBRE",result) ;
            if (membre.getPhoto()!=null) Log.e("RESULT MEMBRE", membre.getPhoto()) ;

            if (result.contains("OK:") && result.split(":").length>=3) {
                membre.setNummembre(result.split(":")[2]);
                membreDAO.update(membre) ;
                operation.setSync(1);
                operation.setNumpicedef(result.split(":")[1]);
                operationDAO.update(operation) ;
                String coderetrait = result.split(":")[3] ;
                String msg = "ALIDE : Bienvenue au service mobile money d'ALIDE . Votre inscription est active et le code secret est : " +
                        coderetrait + "\nMerci" ;


                Log.e("TEL", tel) ;
                try {
                    Utiles.postTwillio(tel,msg) ;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(ConnexionActivity.this, R.string.adhesionsuccess,Toast.LENGTH_LONG).show() ;
            }
            else if (result.equals("BASE")){
                Log.e("RESULT", context.getString(R.string.baseerror)); ;
            }
            else if (result.equals("NUMPIECE")){
                Log.e("RESULT", context.getString(R.string.pieceerror)); ;
            }
            else if (result.equals("ECHEC")){
                Log.e("RESULT", context.getString(R.string.pieceerror)); ;
            }
        }

        etape = 3 ;
        transfert();

    }





    public void sendRemboursement() {
        Caissier c  =null;
        ArrayList<Operation> operations;
        Operation operation ;
        String result;

        c = new CaissierDAO(context).getLast() ;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context) ;

        Log.e("DEBUG","ETAT") ;
        operations = operationDAO.getNonSync();

        String typop = "" ;
        Log.e("DEBUG", String.valueOf(operations.size())) ;

        Date date = new Date() ;
        try {
            date = DAOBase.formatterj.parse(preferences.getString(ConnexionActivity.JOURNEE, "")) ;
        } catch (ParseException e) {
            e.printStackTrace();
        }


        WifiManager wifiManager = null;
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        String macAddress ="";
        if(wInfo!=null)macAddress = wInfo.getMacAddress();

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String iMei = null ;
        iMei = telephonyManager.getDeviceId();
        try {
            Log.d("IMEI", iMei);
            Log.d("MACADRESS", macAddress);
        }catch (Exception e){
            e.printStackTrace();
        }


        FormBody.Builder formBuilder = new FormBody.Builder();

        for (int j = 0;j<operations.size();++j){
            operation = operations.get(j) ;
            if (operation.getTypeoperation()!=4) continue;
            preferences = PreferenceManager.getDefaultSharedPreferences(context) ;
            date = new Date() ;
            formBuilder = new FormBody.Builder();
            formBuilder.add("iMei", iMei) ;
            formBuilder.add("macAddress", macAddress);

            formBuilder.add("numcredit",operation.getNumcompte());
            formBuilder.add("journee", DAOBase.formatterj.format(operation.getDateoperation()));
            formBuilder.add("montant",String.valueOf(operation.getMontant()));
            formBuilder.add("guichet",c.getCodeguichet());
            formBuilder.add("forcer","0") ;


            result = "" ;

            try {
                result = Utiles.POST(Url.getRembrUrl(context),formBuilder.build());
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.e("RESULT OPERATION", result) ;


            if (result.equals("NUMPIECE")) Log.e("RESULT",context.getString(R.string.echec1));
            if (result.equals("AGENCEDIFF")) Log.e("RESULT",context.getString(R.string.echec));
            if (result.equals("NUMPECEECHEC1")) Log.e("RESULT",context.getString(R.string.echec2));
            if (result.equals("NUMPECEECHEC2")) Log.e("RESULT",context.getString(R.string.echec3));
            if (result.equals("NUMPECEECHEC3")) Log.e("RESULT", context.getString(R.string.echec4));
            else if (result.contains("OK:") && result.split(":").length>=3){
                operation.setNumpicedef(result.split(":")[1]);
                operation.setSync(1);
                operationDAO.update(operation) ;
            }
        }

        etape = 4 ;
        transfert() ;
    }




    private void sendPhoto() {

        SharedPreferences preferences = null;
        Membre membre = null;
        String filepath = null;
        String[] path = null ;

        String response = "" ;
        ArrayList<Membre> membres = membreDAO.getNonPhotoSync() ;
        String filePath = null ;
        for (int i = 0; i < membres.size(); i++) {
            membre = membres.get(i) ;
            filePath = membre.getPhoto() ;
            if (filePath != null) {
                response = Utiles.uploadFile(filePath, membre.getNummembre().replace("/","") + "_" + caissierDAO.getLast().getAgence_id()+ "_" + membre.getNummembre().replace("/","*") , context);
                Log.e("NUMMEMBRE",membre.getNummembre()) ;
                Log.e("NUMMEMBRE",membre.getNummembre().replace("/","")) ;
                Log.e("DEGUB",response) ;
                if (response.contains("OK_") && response.split("_").length==2) {
                    //ImageProcess ip = new ImageProcess(this) ;
                    File file = new File(membre.getPhoto()) ;
                    if (file.exists())file.delete() ;
                    String name = null;
                    name = response.split(":")[1] ;
                    //name = article.getId() + "." + path[path.length - 1];
                    membre.setPhoto(null);
                    membreDAO.update(membre) ;

                }
            }
        }

        etape = 5 ;
        transfert();

    }

}
