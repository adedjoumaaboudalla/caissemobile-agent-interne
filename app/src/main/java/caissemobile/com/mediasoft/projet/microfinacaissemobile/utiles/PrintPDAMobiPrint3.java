package caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.serialport.api.PrintService;
import android.serialport.api.PrinterClass;
import android.serialport.api.PrinterClassSerialPort;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.Toast;

import com.nbbse.mobiprint3.Printer;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import caissemobile.com.mediasoft.projet.microfinacaissemobile.R;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Categorie;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Compte;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Credit;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Membre;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Operation;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.AdminDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.AgenceDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CaissierDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CategorieDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CompteDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CreditDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.DAOBase;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.OperationDAO;


/**
 * Created by Mayi on 12/01/2016.
 */
public class PrintPDAMobiPrint3 {

    private final Printer print;
    private SharedPreferences preferences;
    protected static final String TAG = "Print Utils";
    private Handler mHandler;
    OperationDAO operationDAO = null ;
    CaissierDAO caissierDAO = null ;
    private String msgFin = null;
    private String agence = null;
    private String societeAdresse = null;
    String msg;
    Context context;
    public static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy H:m:s") ;
    String macAddress = null ;
    WifiManager wifiManager ;
    private CompteDAO compteDAO;
    private CreditDAO creditDAO;


    public PrintPDAMobiPrint3(Context context){
        preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        macAddress = wInfo.getMacAddress();
        operationDAO = new OperationDAO(context) ;
        caissierDAO = new CaissierDAO(context) ;
        creditDAO = new CreditDAO(context) ;
        compteDAO = new CompteDAO(context) ;
        this.context = context ;
        print = Printer.getInstance();
        AgenceDAO agenceDAO = new AgenceDAO(context) ;
        agence = agenceDAO.getOne(caissierDAO.getLast().getAgence_id()).getNomagence() ;
        societeAdresse = preferences.getString("adresse", "21 38 22 24 / 61 23 92 92") ;
        msgFin = preferences.getString("messagefinal", "Copyright Médiasoft") ;
    }


	/*
	 * Imprimer
	 */
/*
    public void printTicket(ArrayList<Operation> operations){

        Calendar cal = Calendar.getInstance() ;
        msg = "##############################";
        msg+= "\n";
        msg +=  societeNom ;
        msg+= "\n";
        msg +=  societeAdresse ;
        msg+= "\n";
        msg += "################################";
        msg+= "\n";
        msg+= "DU : "+ formatter.format(operations.get(0)) +  " AU : "+ formatter.format(operations.get(operations.size()-1));
        msg+= "\n";
        msg += "--------------------------------";
        msg += "\n";
        msg+= "DESIGNATION                 Mte";
        msg += "\n";
        msg += "--------------------------------";
        msg+= "\n";
        int n = operations.size() ;
        int total = 0 ;
        for (int i = 0; i < n; i++){
            Operation pv = operations.get(i) ;
            total += pv.getMontant() ;
            msg+= operationlibelle(pv.getDescription()) + " " + montant(String.valueOf(pv.getMontant())) ;
            msg+= "\n";
        }
        msg += "--------------------------------";
        msg+= "\n";
        msg+= "TOTAL     ";
        msg+= totaux(String.valueOf(total))  + " FCFA" ;
        msg+= "\n";
        msg += "--------------------------------";
        msg += "\n";
        msg += "################################";
        msg += "\n";
        msg += "\n";
        msg += "\n";
        msg += "\n";
        msg += "\n";
        msg += "\n";


        try {
            printTicket(msg);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

*/



    public void printTicket(Categorie categorie, String lib, Membre membre, String numproduit) {
        String op = "" ;
        Calendar cal = Calendar.getInstance() ;

        //print.printBitmap(context.getResources().openRawResource(R.raw.logoalide));
        String path = "" ;
        if (Utiles.isSdReadable() && preferences.getBoolean("stockage",true)){
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Mediasoft/logo.bmp";
        }
        else if (!preferences.getBoolean("stockage",true)){
            path = context.getFilesDir().getAbsolutePath() + "/Mediasoft/logo.bmp";
        }
        else{

        }

        File file = new File(path) ;
        if (file.exists())  print.printBitmap(path);
        else print.printBitmap(context.getResources().openRawResource(R.raw.logoalide));

        CategorieDAO categorieDAO = new CategorieDAO(context);;

        msg = "################################";
        msg+= "\n";
        msg +=  "     "+lib ;
        msg+= "\n";
        msg += "################################";
        msg+= "\n";
        msg+= "Date             : "+ preferences.getString("dateouvert","");
        msg+= "\n";
        msg+= "Membre           : "+ membre.getNom() + " " + membre.getPrenom() ;
        msg+= "\n";
        msg += "Num membre Temp  : "+  membre.getNummembre() + "E01" ;
        msg+= "\n";
        msg+= "Date Sys         : "+ formatter.format(new Date());
        msg+= "\n";
        msg += "Agence           : "+  agence ;
        msg+= "\n";
        msg += "Guichet          : "+  caissierDAO.getLast().getCodeguichet() ;
        msg+= "\n";
        msg+= "--------------------------------";
        msg+= "\n";
        ArrayList<Categorie> categories = categorieDAO.getAll(categorie.getNumCategorie(), numproduit) ;
        double frais = 0 ;
        for (int i = 0; i < categories.size(); i++) {
            msg += categories.get(i).getLibelleFrais() + " : " + Utiles.formatMtn(categories.get(i).getValeurFrais()) + "\n";
            frais += categories.get(i).getValeurFrais() ;
        }
        msg+= "Dépot Initial     : "+ String.valueOf(membre.getMontant() - frais);
        msg+= "\n";
        msg += "--------------------------------";
        msg+= "\n";
        msg+= "Montant          :  " + Utiles.formatMtn(membre.getMontant()) + " F" ;
        msg += "\n";
        msg += msgFin;
        msg += "\n";
        msg += "################################";
        msg += "\n";
        msg += "Tel : " + societeAdresse;
        msg += "\n";
        msg += "\n";


        try {
            printTicket(msg);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void printTicket(Operation operation, String caise){

        String op = "" ;
        Compte compte = compteDAO.getOne(operation.getNumcompte()) ;
        if(operation.getTypeoperation()==0) op ="Depot";
        else if(operation.getTypeoperation()==4) {
            op ="Remboursement";
            compte = compteDAO.getOne(creditDAO.getOneByNum(operation.getNumcompte()).getNummembre()) ;
        }
        else op = "Retrait";
        Calendar cal = Calendar.getInstance() ;

        //print.printBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.logoalide));
        //msg = "################################";
        //printTicket(msg);
        //print.printBitmap(context.getResources().openRawResource(R.raw.logoalide));
        String path = "" ;
        if (Utiles.isSdReadable() && preferences.getBoolean("stockage",true)){
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Mediasoft/logo.bmp";
        }
        else if (!preferences.getBoolean("stockage",true)){
            path = context.getFilesDir().getAbsolutePath() + "/Mediasoft/logo.bmp";
        }
        else{

        }

        File file = new File(path) ;
        if (file.exists())  print.printBitmap(path);
        else print.printBitmap(context.getResources().openRawResource(R.raw.logoalide));


        msg= "################################";
        msg+= "\n";
        msg+= " " + caise ;
        msg+= "\n";
        msg += "################################";
        msg+= "\n";
        msg+= "No piece      : "+ operation.getNumpicedef();
        msg+= "\n";
        msg+= "No piece temp : "+ operation.getNumpiece();
        msg+= "\n";
        msg+= "Date          : "+ DAOBase.formatterj.format(operation.getDateoperation());
        msg+= "\n";
        msg+= "Date Sys      : "+ formatter.format(new Date());
        msg+= "\n";
        if (operation.getNumcompte().contains("/G/"))msg+= "Groupe    : "+ operation.getNom() ;
        else msg+= "Client   : "+ operation.getNom() + " " + operation.getPrenom() ;
        msg+= "\n";
        msg += "Agence       : "+  agence ;
        msg+= "\n";
        msg += "Guichet      : "+  caissierDAO.getLast().getCodeguichet() ;
        msg+= "\n";
        msg+= "--------------------------------";
        msg+= "\n";
        msg+= "Operation     :  " + op ;
        msg+= "\n";
        if (operation.getTypeoperation()!=4) msg+= "Compte        :  " + operation.getNumcompte() ;
        else msg+= "Crédit       :  " + operation.getNumcompte() ;
        msg+= "\n";
        msg+= "Montant       : " + Utiles.formatMtn(operation.getMontant()) + " F" ;
        msg+= "\n";
        //if (compte!=null)msg+= "Nouveau solde :  " + Utiles.formatMtn(compte.getSolde()) + " F" ;
        //msg+= "\n";
        msg += "--------------------------------";
        msg += "\n";
        msg += msgFin;
        msg += "\n";
        msg += "################################";
        msg += "\n";
        msg += "Tel : " + societeAdresse;
        msg += "\n";

        printTicket(msg);

        new Thread(
                new Runnable(){

                    @Override
                    public void run() {
                        try {
                        } catch (Exception e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }
                }).start();

    }

    public void printTicket(String dateDebut,String dateFin){

        DatePicker debut = new DatePicker(context) ;
        DatePicker fin = new DatePicker(context) ;
        if (dateDebut==null)dateDebut = String.valueOf(debut.getYear()) + "-" + String.valueOf(debut.getMonth() + 1) + "-" + String.valueOf(debut.getDayOfMonth());
        if (dateFin==null)dateFin = String.valueOf(fin.getYear()) + "-" + String.valueOf(fin.getMonth() + 1) + "-" + String.valueOf(fin.getDayOfMonth());

        String op = "" ;
        Calendar cal = Calendar.getInstance() ;
        //print.printBitmap(context.getResources().openRawResource(R.raw.logoalide));
        String path = "" ;
        if (Utiles.isSdReadable() && preferences.getBoolean("stockage",true)){
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Mediasoft/logo.bmp";
        }
        else if (!preferences.getBoolean("stockage",true)){
            path = context.getFilesDir().getAbsolutePath() + "/Mediasoft/logo.bmp";
        }
        else{

        }

        File file = new File(path) ;
        if (file.exists())  print.printBitmap(path);
        else print.printBitmap(context.getResources().openRawResource(R.raw.logoalide));

        msg = "################################";
        msg+= "\n";
        msg =  "RECAPITULATIF" ;
        msg+= "\n";
        msg += "################################";
        msg+= "\n";
        msg+= "Operation    | Nbre | Montant";
        msg+= "\n";

        ArrayList<Operation> operations = operationDAO.getAll(2,java.sql.Date.valueOf(dateDebut), java.sql.Date.valueOf(dateFin));

        float mtn = 0 ;
        for (int i = 0; i<operations.size();++i){
            mtn += operations.get(i).getMontant() ;
        }


        msg += "--------------------------------";
        msg+= "\n";
        msg+= context.getResources().getString(R.string.depo) + "       |  " + operations.size() + " |  " + Utiles.formatMtn(mtn) + " F";
        msg+= "\n";

        operations = operationDAO.getAll(1,java.sql.Date.valueOf(dateDebut), java.sql.Date.valueOf(dateFin));
        mtn = 0 ;
        for (int i = 0; i<operations.size();++i){
            mtn += operations.get(i).getMontant() ;
        }

        msg += "--------------------------------";
        msg+= "\n";
        msg+= context.getResources().getString(R.string.retrait) + "      |  " + operations.size() + "  | " + Utiles.formatMtn(mtn) + " F";
        msg+= "\n";

        operations = operationDAO.getAll(3,java.sql.Date.valueOf(dateDebut), java.sql.Date.valueOf(dateFin));
        mtn = 0 ;
        for (int i = 0; i<operations.size();++i){
            mtn += operations.get(i).getMontant() ;
        }

        msg += "--------------------------------";
        msg+= "\n";
        msg+= context.getResources().getString(R.string.adhesion) + "    |  " + operations.size() + "  | " + Utiles.formatMtn(mtn) + " F";
        msg+= "\n";

        operations = operationDAO.getAll(4,java.sql.Date.valueOf(dateDebut), java.sql.Date.valueOf(dateFin));
        mtn = 0 ;
        for (int i = 0; i<operations.size();++i){
            mtn += operations.get(i).getMontant() ;
        }


        msg += "--------------------------------";
        msg+= "\n";
        msg+= context.getResources().getString(R.string.rembr) + "|  " + operations.size() + "  | " + Utiles.formatMtn(mtn) + " F";
        msg+= "\n";
        msg += "--------------------------------";
        msg+= "\n";
        msg+= "Date      : "+ preferences.getString("dateouvert","");
        msg+= "\n";
        msg+= "Date Sys  : "+ formatter.format(new Date());
        msg+= "\n";
        msg+= "--------------------------------";
        msg += "\n";
        msg += msgFin;
        msg += "\n";
        msg += "################################";
        msg += "\n";
        msg += "Tel : " + societeAdresse;
        msg += "\n";

        try {
            printTicket(msg);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public String name(String name){
        int n = 11 ;
        int siz = 0 ;
        Log.e("NAME1",name) ;
        name = (name.length()>11)?(name.substring(0,10)):(name) ;
        siz = n-name.length() ;
        for (int i =0 ; i < siz; ++i){
            name += " " ;
        }
        Log.e("NAME2",name) ;
        return name ;
    }


    public String operationlibelle(String name){
        int n = 17 ;
        int siz = 0 ;
        Log.e("NAME1",name) ;
        name = (name.length()>17)?(name.substring(0,16)):(name) ;
        siz = n-name.length() ;
        for (int i =0 ; i < siz; ++i){
            name += " " ;
        }
        Log.e("NAME2",name) ;
        return name ;
    }

    public String prix(String prix){
        int n = 6 ;
        int siz = 0 ;
        prix = (prix.length()>6)?(prix.substring(0,5)):(prix) ;
        siz = n-prix.length() ;
        for (int i =0 ; i < siz; ++i){
            prix = " " + prix ;
        }
        return prix ;
    }

    public String montant(String prix){
        int n = 6 ;
        int siz = 0 ;
        prix = (prix.length()>6)?(prix.substring(0,5)):(prix) ;
        siz = n-prix.length() ;
        for (int i =0 ; i < siz; ++i){
            prix = " " + prix ;
        }
        return prix ;
    }

    public String quantite(String prix){
        int n = 5 ;
        int siz = 0 ;
        prix = (prix.length()>5)?(prix.substring(0,4)):(prix) ;
        siz = n-prix.length() ;
        for (int i =0 ; i < siz; ++i){
            prix = " " + prix ;
        }
        return prix ;
    }

    public String totaux(String totaux){
        int n = 9 ;
        int siz = 0 ;
        totaux = (totaux.length()>9)?(totaux.substring(0,8)):(totaux) ;
        siz = n-totaux.length() ;
        for (int i =0 ; i < siz; ++i){
            totaux = " " + totaux ;
        }
        return totaux ;
    }

    public void printTicketBillet(String msg) {

        //print.printBitmap(context.getResources().openRawResource(R.raw.logoalide));
        String path = "" ;
        if (Utiles.isSdReadable() && preferences.getBoolean("stockage",true)){
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Mediasoft/logo.bmp";
        }
        else if (!preferences.getBoolean("stockage",true)){
            path = context.getFilesDir().getAbsolutePath() + "/Mediasoft/logo.bmp";
        }
        else{

        }

        File file = new File(path) ;
        if (file.exists())  print.printBitmap(path);
        else print.printBitmap(context.getResources().openRawResource(R.raw.logoalide));

        printTicket(msg);
        //printTicket(msg);

    }

    public void printTicket(String msg) {

        try {
            print.printText(msg);
            print.printEndLine();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }


    private Context getApplicationContext(){
        return this.context;
    }



    private void ShowMsg(String msg){
        Toast.makeText(this.context, msg,
                Toast.LENGTH_SHORT).show();
    }

    public void printImage(){
        String path = "" ;
        if (Utiles.isSdReadable() && preferences.getBoolean("stockage",true)){
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Mediasoft/logo.bmp";
        }
        else if (!preferences.getBoolean("stockage",true)){
            path = context.getFilesDir().getAbsolutePath() + "/Mediasoft/logo.bmp";
        }
        else{

        }

        File file = new File(path) ;
        if (file.exists())  print.printBitmap(path);
        else print.printBitmap(context.getResources().openRawResource(R.raw.logoalide));
    }

}
