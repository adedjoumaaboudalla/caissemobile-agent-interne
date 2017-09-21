package caissemobile.com.mediasoft.projet.microfinacaissemobile.services;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import caissemobile.com.mediasoft.projet.microfinacaissemobile.R;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.activities.ConnexionActivity;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Billet;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Caissier;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Categorie;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Membre;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Nationalite;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Operation;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.OperationSec;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Profession;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Zone;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.BilletDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CaissierDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CategorieDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CompteDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.DAOBase;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.MembreDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.NationaliteDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.OperationDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.OperationSecDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.ProfessionDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.ZoneDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.database.BilletHelper;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.database.CaissierHelper;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.database.CategorieHelper;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.database.MembreHelper;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.database.NationaliteHelper;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.database.ProfessionHelper;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.database.ZoneHelper;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.Url;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.Utiles;
import okhttp3.FormBody;

/**
 * Created by mediasoft on 17/11/2016.
 */
public class CaisseSyncAdapter extends AbstractThreadedSyncAdapter {

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
    private static final String CORRECT = "success";
    private static final String ECHEC = "echec";
    public static final String JOURNEE = "dateouvert";
    public static final String DEBUTPIECE = "debut_piece";


    public CaisseSyncAdapter(Context context, boolean autoInitialize) {
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
        Log.e("CAISSE ADAPTER","SYNC") ;
        etape = 1 ;

        if (caissierDAO.getLast()==null) return;
        loadCaisse(Url.getLoadCaissierUrl(context)) ;
    }

    private void loadCaisse(String... urls) {

        String codeClient = caissierDAO.getLast().getCodeguichet() ;

        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add(CaissierHelper.ID, codeClient);
        formBuilder.add("licence", "MDIJ-IBSN-GWCT-UNXK");
        Log.e("DEBUG", urls[0]) ;


        String result = "" ;

        try {
            result = Utiles.POST(urls[0],formBuilder.build());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (result!=null) Log.e("DEBUG", result) ;

        JSONObject json = null ;
        try {
            json = new JSONObject(result);
            JSONArray caissiers = json.getJSONArray("caissiers") ;

            caissierDAO = new CaissierDAO(context) ;
            //caissierDAO.clean() ;
            Caissier caissier = null ;
            for (int i = 0; i < caissiers.length() ; i++){
                caissier = caissierDAO.getLast() ;

                //caissier.setId(caissiers.getJSONObject(i).getInt(CaissierHelper.ID));
                caissier.setAgence_id(caissiers.getJSONObject(i).getString(CaissierHelper.AGENCE_ID));
                caissier.setLogin(caissiers.getJSONObject(i).getString(CaissierHelper.LOGIN));
                caissier.setPassword(caissiers.getJSONObject(i).getString(CaissierHelper.PASSWORD));
                caissier.setNomprenom(caissiers.getJSONObject(i).getString(CaissierHelper.NOM_PRENOM));
                caissier.setCodeguichet(caissiers.getJSONObject(i).getString(CaissierHelper.CODE_GUICHET));
                //caissier.setSolde((float) caissiers.getJSONObject(i).getDouble(CaissierHelper.SOLDE));
                caissier.setRetraitMax(caissiers.getJSONObject(i).getDouble(CaissierHelper.RETRAITMAX));

                caissier.setDescription(caissiers.getJSONObject(i).getString(CaissierHelper.DESCRIPTION));
                caissier.setDdb(caissiers.getJSONObject(i).getString(CaissierHelper.DDB));
                caissier.setDi(caissiers.getJSONObject(i).getString(CaissierHelper.DI));
                caissier.setDp(caissiers.getJSONObject(i).getString(CaissierHelper.DP));
                caissier.setDl(caissiers.getJSONObject(i).getString(CaissierHelper.DL));
                caissier.setPassword(caissiers.getJSONObject(i).getString(CaissierHelper.PASSWORD));
                //caissier.setNumpiece(caissiers.getJSONObject(i).getInt(CaissierHelper.NUMPIECE));

                caissierDAO.update(caissier) ;

                SharedPreferences.Editor edit = preferences.edit() ;
                /*
                Date date = null ;
                try {
                    date = DAOBase.formatter.parse(caissiers.getJSONObject(i).getString("dateouvert")) ;
                    edit.putString(JOURNEE,DAOBase.formatterj.format(date)) ;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                */

                edit.putString(DEBUTPIECE,caissiers.getJSONObject(i).getString("debutpiece")) ;
                edit.commit() ;
                //refreshData();
            }

            JSONArray categories = json.getJSONArray("categories") ;
            categorieDAO = new CategorieDAO(context) ;
            categorieDAO.clean() ;
            Categorie categorie = null ;
            for (int i = 0; i < categories.length() ; i++){
                categorie = new Categorie() ;

                categorie.setId(i+1);
                categorie.setNumCategorie(categories.getJSONObject(i).getString("CATEGORIE"));
                categorie.setLibelleCategorie(categories.getJSONObject(i).getString(CategorieHelper.LIBELLE));
                categorie.setLibelleFrais(categories.getJSONObject(i).getString("LIBELLEFRAIS"));
                categorie.setValeurFrais((float) categories.getJSONObject(i).getDouble("VALEURFRAIS"));
                categorie.setNumproduit(categories.getJSONObject(i).getString("numproduit"));

                categorieDAO.add(categorie) ;
            }


            JSONArray billets = null ;
            billets = json.getJSONArray("billets") ;

            BilletDAO billetDAO = new BilletDAO(context);;
            billetDAO.clean() ;
            Billet billet = null ;
            for (int i = 0; i < billets.length() ; i++){
                billet = new Billet() ;
                billet.setId(billets.getJSONObject(i).getInt(BilletHelper.TABLE_KEY));
                billet.setLibelle(billets.getJSONObject(i).getString(BilletHelper.LIBELLE));
                billet.setDevise(billets.getJSONObject(i).getString(BilletHelper.DEVISE));
                billet.setMontant(billets.getJSONObject(i).getInt(BilletHelper.MONTANT));
                billetDAO.add(billet) ;
            }

            JSONArray nationalites = json.getJSONArray("nationalites") ;

            NationaliteDAO nationaliteDAO = new NationaliteDAO(context);;
            nationaliteDAO.clean() ;
            Nationalite nationalite = null ;
            for (int i = 0; i < nationalites.length() ; i++){
                nationalite = new Nationalite() ;

                nationalite.setNumnation(nationalites.getJSONObject(i).getInt(NationaliteHelper.NUMNATIONALITE));
                nationalite.setLibelle(nationalites.getJSONObject(i).getString(NationaliteHelper.LIBELLE));
                nationaliteDAO.add(nationalite) ;
            }


            JSONArray professions = json.getJSONArray("professions") ;

            ProfessionDAO professionDAO = new ProfessionDAO(context);;
            professionDAO.clean() ;
            Profession profession = null ;
            for (int i = 0; i < professions.length() ; i++){
                profession = new Profession() ;

                profession.setNumprofession(professions.getJSONObject(i).getInt(ProfessionHelper.NUMPROFESSION));
                profession.setLibelle(professions.getJSONObject(i).getString(ProfessionHelper.LIBELLE));
                professionDAO.add(profession) ;
            }

            JSONArray zones = json.getJSONArray("zones") ;


            //Toast.makeText(getApplicationContext(),"Veuille patientez SVP",Toast.LENGTH_SHORT).show();

            ZoneDAO zoneDAO = new ZoneDAO(context);;
            zoneDAO.clean() ;
            Zone zone = null ;

            for (int i = 0; i < zones.length() ; i++){
                zone = new Zone() ;

                zone.setDescription(zones.getJSONObject(i).getString(ZoneHelper.DESCRIPTION));
                zoneDAO.add(zone) ;
            }

            //Toast.makeText(ConnexionActivity.this, R.string.loadsuccess, Toast.LENGTH_SHORT).show();

            //dismissDialog(PROGRESS_DIALOG_ID);

            result = CORRECT ;
        } catch (JSONException e) {
            e.printStackTrace();
            //VALUE = 0 ;
            //Toast.makeText(ConnexionActivity.this, R.string.echec, Toast.LENGTH_SHORT).show();
            result =  ECHEC ;
        } catch (NullPointerException e) {
            e.printStackTrace();
            //VALUE = 0 ;
            result = ECHEC ;
            //Toast.makeText(ConnexionActivity.this, R.string.licenceexp, Toast.LENGTH_SHORT).show();
        }
    }

}
