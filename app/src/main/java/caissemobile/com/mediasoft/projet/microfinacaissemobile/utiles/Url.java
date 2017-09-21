package caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Mayi on 07/10/2015.
 *
**/

public class Url {
    //public static String serverIp = "192.168.10.6" ;
    public static String serverIp = "164.160.141.134:81" ;
    public static String SMSNUMBER = "smsnumber" ;
    public static String IP = "ipserver" ;
    public static String smsNumber = "0022890885850" ;
    private static String dossier = "/caissemobile/serviceweb" ;
    //public static String smsNumber = "0022891260776" ;

    public static String getLoadAgenceUrl(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(IP, serverIp) + dossier + "/loadagence.php";
        return result ;
    }

    public static String getLoadCaissierUrl(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(IP, serverIp) + dossier + "/loadcaissier.php";
        return result ;
    }


    public static String getLoadCLientInfoUrl(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(IP, serverIp) + dossier + "/loadclientinfoMOBILE.php";
        return result ;
    }


    public static String getCheckPass(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(IP, serverIp) + dossier + "/checkpassword.php";
        return result ;
    }



    public static String getTestConnexion(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(IP, serverIp) + dossier + "/testconnexion.php";
        return result ;
    }


    public static String getCheckLicence(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(IP, serverIp) + dossier + "/checklicence.php";
        return result ;
    }

    public static String getSendImageUrl(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(IP, serverIp) + dossier + "/uploadimage.php";
        return result ;
    }


    public static String getLoadAdminUrl(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(IP, serverIp) + dossier + "/loadAdmin.php";
        return result ;
    }


    public static String getLoadCLientDetailUrl(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(IP, serverIp) + dossier + "/loadclientdetails.php";
        return result ;
    }



    public static String getLoadCLientPhotoUrl(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(IP, serverIp) + dossier + "/loadclientphoto.php";
        return result ;
    }




    public static String getPhotoPath(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(IP, serverIp);
        return result ;
    }

    public static String getSendOperationUrl(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(IP, serverIp) + dossier + "/traitementoperation.php";
        return result ;
    }

    public static String getAddMembreUrl(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(IP, serverIp) + dossier + "/addmembre.php";
        return result ;
    }


    public static String getAddMembreGroupeUrl(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(IP, serverIp) + dossier + "/addmembregroupe.php";
        return result ;
    }

    public static String getRembrUrl(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(IP, serverIp) + dossier + "/addrembroussement.php";
        return result ;
    }


    public static String getLoadCreditUrl(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(IP, serverIp) + dossier + "/loadcredit.php";
        return result ;
    }


    public static String getLoadCompte(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(IP, serverIp) + dossier + "/loadcompte.php";
        return result ;
    }


    public static String getLoadCarnet(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(IP, serverIp) + dossier + "/loadcarnet.php";
        return result ;
    }

    public static String getListeCreditUrl(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(IP, serverIp) + dossier + "/loadcreditbycpt.php";
        return result ;
    }

    public static String getLoadMembre(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(IP, serverIp) + dossier + "/loadmembre.php";
        return result ;
    }

    public static String getLoadImageUrl(Context context,String name) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(IP, serverIp) + "/caissemobile/PHOTOS/"+name;
        return result ;
    }



    public static String getSmsNumber(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = preferences.getString(SMSNUMBER, smsNumber);

        return result ;
    }

}
