package caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.serialport.api.PrintService;
import android.serialport.api.PrinterClass;
import android.serialport.api.PrinterClassSerialPort;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import caissemobile.com.mediasoft.projet.microfinacaissemobile.R;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Categorie;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Compte;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Membre;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Operation;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.AgenceDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CaissierDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CategorieDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CompteDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.DAOBase;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.OperationDAO;


/**
 * Created by Mayi on 12/01/2016.
 */
public class PrintPDA {

    private SharedPreferences preferences;
    PrintService printservice = new PrintService();
    protected static final String TAG = "Print Utils";
    private Handler mHandler;
    OperationDAO operationDAO = null ;
    CaissierDAO caissierDAO = null ;
    private String msgFin = null;
    private String agence = null;
    private String societeAdresse = null;
    String msg;
    Context context;
    PrinterClassSerialPort printerClass = null ;
    public static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy H:m:s") ;
    String macAddress = null ;
    WifiManager wifiManager ;
    private CompteDAO compteDAO;


    public PrintPDA(Context context){
        preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        macAddress = wInfo.getMacAddress();
        operationDAO = new OperationDAO(context) ;
        caissierDAO = new CaissierDAO(context) ;
        this.context = context ;
        AgenceDAO agenceDAO = new AgenceDAO(context) ;
        compteDAO = new CompteDAO(context) ;
        agence = agenceDAO.getOne(caissierDAO.getLast().getAgence_id()).getNomagence() ;
        societeAdresse = preferences.getString("adresse", "21 38 22 24 / 61 23 92 92") ;
        msgFin = preferences.getString("messagefinal", "Copyright Media Soft") ;
        initPrintPDA();
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
        msg+= "Date Sys         : "+ DAOBase.formatterj.format(new Date());
        msg+= "\n";
        msg += "Agence           : "+  agence ;
        msg+= "\n";
        msg += "Guichet          : "+  caissierDAO.getLast().getCodeguichet() ;
        msg+= "\n";
        msg+= "--------------------------------";
        msg+= "\n";
        ArrayList<Categorie> categories = categorieDAO.getAll(categorie.getNumCategorie(),numproduit) ;
        double frais = 0 ;
        for (int i = 0; i < categories.size(); i++) {
            msg += categories.get(i).getLibelleFrais() + " : " + String.format("%.2f", categories.get(i).getValeurFrais()) + "\n";
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

    public void printTicket(Operation operation, String caise){


        String op = "" ;
        Compte compte = compteDAO.getOne(operation.getNumcompte()) ;
        if(operation.getTypeoperation()==0) op ="Dépot";
        else if(operation.getTypeoperation()==4) op ="Remboursement";
        else op = "Retrait";
        Calendar cal = Calendar.getInstance() ;
        msg = "################################";
        msg+= "\n";
        msg +=  caise ;
        msg+= "\n";
        msg += "################################";
        msg+= "\n";
        msg+= "No piece      : "+ operation.getNumpicedef();
        msg+= "\n";
        msg+= "No piece temp : "+ operation.getNumpiece();
        msg+= "\n";
        msg+= "Date          : "+ DAOBase.formatterj.format(operation.getDateoperation());
        msg+= "\n";
        msg+= "Date Sys      : "+ DAOBase.formatterj.format(new Date());
        msg+= "\n";
        if (operation.getNumcompte().contains("/G/"))msg+= "Groupe        : "+ operation.getNom() ;
        else msg+= "Client        : "+ operation.getNom() + " " + operation.getPrenom() ;
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
        else msg+= "Crédit   :  " + operation.getNumcompte() ;
        msg+= "\n";
        msg+= "Montant       : " + Utiles.formatMtn(operation.getMontant()) + " F" ;
        //msg+= "\n";
        //msg+= "Nouveau solde :  " + Utiles.formatMtn(compte.getSolde()) + " F" ;
        msg+= "\n";
        msg += "--------------------------------";
        msg += "\n";
        msg += msgFin;
        msg += "\n";
        msg += "################################";
        msg += "\n";
        msg += "Tel : " + societeAdresse;
        msg += "\n";
        msg += "\n";
        msg += "\n";
        msg += "\n";
        msg += "\n";
        msg += "\n";
        msg += "\n";



        new Thread(
                new Runnable(){

                    @Override
                    public void run() {
                        try {
                            printTicket(msg);
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
        msg = "################################";
        msg+= "\n";
        msg +=  "RECAPITULATIF" ;
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
        msg+= "Date Sys  : "+ DAOBase.formatterj.format(new Date());
        msg+= "\n";
        msg+= "--------------------------------";
        msg += "\n";
        msg += msgFin;
        msg += "\n";
        msg += "################################";
        msg += "\n";
        msg += "Tel : " + societeAdresse;
        msg += "\n";
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

    public void printTicket(String msg) {

        try {
            printerClass.printText(msg);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }


    private Context getApplicationContext(){
        return this.context;
    }

    private void initPrintPDA(){
        printerClass = new PrinterClassSerialPort(getHandler());
        //printerClass.open(this);
        if(!printerClass.IsOpen())   printerClass.open(context);
    }

    private Handler getHandler(){
        Handler mhandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case PrinterClass.MESSAGE_READ:
                        byte[] readBuf = (byte[]) msg.obj;
                        Log.i(TAG, "readBuf:" + readBuf[0]);
                        if (readBuf[0] == 0x13) {
                            //PrintService.isFUll = true;
                            //ShowMsg(getResources().getString(R.string.str_printer_state)+":"+getResources().getString(R.string.str_printer_bufferfull));
                        } else if (readBuf[0] == 0x11) {
                            //PrintService.isFUll = false;
                            //ShowMsg(getResources().getString(R.string.str_printer_state)+":"+getResources().getString(R.string.str_printer_buffernull));
                        } else if (readBuf[0] == 0x08) {
                            //	ShowMsg(getResources().getString(R.string.str_printer_state)+":"+getResources().getString(R.string.str_printer_nopaper));
                            ShowMsg("No Paper !!");
                        } else if (readBuf[0] == 0x01) {
                            //ShowMsg(getResources().getString(R.string.str_printer_state)+":"+getResources().getString(R.string.str_printer_printing));
                        }  else if (readBuf[0] == 0x04) {
                            //ShowMsg(getResources().getString(R.string.str_printer_state)+":"+getResources().getString(R.string.str_printer_hightemperature));
                            ShowMsg("High Temperature !!");
                        } else if (readBuf[0] == 0x02) {
                            //ShowMsg(getResources().getString(R.string.str_printer_state)+":"+getResources().getString(R.string.str_printer_lowpower));
                            ShowMsg("Low Power !!");

                        } else {
                            String readMessage = new String(readBuf, 0, msg.arg1);
                            if (readMessage.contains("800"))// 80mm paper
                            {
                                PrintService.imageWidth = 72;
                                Toast.makeText(getApplicationContext(), "80mm",
                                        Toast.LENGTH_SHORT).show();
                            } else if (readMessage.contains("580"))// 58mm paper
                            {
                                PrintService.imageWidth = 48;
                                Toast.makeText(getApplicationContext(), "58mm",
                                        Toast.LENGTH_SHORT).show();
                            } else {

                            }
                        }
                        break;
                    case PrinterClass.MESSAGE_STATE_CHANGE:// 6��l��״
                        switch (msg.arg1) {
                            case PrinterClass.STATE_CONNECTED:// �Ѿ�l��
                                break;
                            case PrinterClass.STATE_CONNECTING:// ����l��
                                Toast.makeText(getApplicationContext(),
                                        "STATE_CONNECTING", Toast.LENGTH_SHORT).show();
                                break;
                            case PrinterClass.STATE_LISTEN:
                            case PrinterClass.STATE_NONE:
                                break;
                            case PrinterClass.SUCCESS_CONNECT:
                                printerClass.write(new byte[] { 0x1b, 0x2b });// ����ӡ���ͺ�
                                Toast.makeText(getApplicationContext(),
                                        "SUCCESS_CONNECT", Toast.LENGTH_SHORT).show();
                                break;
                            case PrinterClass.FAILED_CONNECT:
                                Toast.makeText(getApplicationContext(),
                                        "FAILED_CONNECT", Toast.LENGTH_SHORT).show();

                                break;
                            case PrinterClass.LOSE_CONNECT:
                                Toast.makeText(getApplicationContext(), "LOSE_CONNECT",
                                        Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case PrinterClass.MESSAGE_WRITE:

                        break;
                }
                super.handleMessage(msg);
            }
        };
        return mhandler;
    }

    static byte[] string2Unicode(String s) {
        try {
            byte[] bytes = s.getBytes("unicode");
            byte[] bt = new byte[bytes.length - 2];
            for (int i = 2, j = 0; i < bytes.length - 1; i += 2, j += 2) {
                bt[j] = (byte) (bytes[i + 1] & 0xff);
                bt[j + 1] = (byte) (bytes[i] & 0xff);
            }
            return bt;
        } catch (Exception e) {
            try {
                byte[] bt = s.getBytes("GBK");
                return bt;
            } catch (UnsupportedEncodingException e1) {
                Log.e(TAG, e.getMessage());
                return null;
            }
        }
    }



    private void ShowMsg(String msg){
        Toast.makeText(this.context, msg,
                Toast.LENGTH_SHORT).show();
    }

}
