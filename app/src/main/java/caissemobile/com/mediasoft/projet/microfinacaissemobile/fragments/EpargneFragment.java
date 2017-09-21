package caissemobile.com.mediasoft.projet.microfinacaissemobile.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nbbse.mobiprint3.Printer;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import caissemobile.com.mediasoft.projet.microfinacaissemobile.MainActivity;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.R;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.activities.CompteActivity;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.activities.ConnexionActivity;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.activities.MembreActivity;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.activities.ParametreActivity;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.activities.ScannerActivity;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Agence;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Caissier;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Compte;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Membre;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Operation;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.OperationSec;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.AgenceDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CaissierDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CompteDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.DAOBase;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.MembreDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.OperationDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.OperationSecDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.services.OperationSyncAdapter;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.PrintPDA;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.PrintPDAMobiPrint3;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.PrinterUtils;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.Url;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.Utiles;
import okhttp3.FormBody;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EpargneFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EpargneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EpargneFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "EpargneFragment";
    private static final int CHOOSE_COMPTE_REQUEST = 1;
    public static final String ENUMPIECE = "E0000";
    private static final int CHOOSE_MEMBRE_REQUEST = 2;
    private static final int CHOOSE_CODE_BAR_REQUEST = 3;
    public static final String NOCOMPTE = "nocompte";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private static final int REQUEST_CODE = 0;
    private static final int PROGRESS_DIALOG_ID = 1;
    private static final int MAX_SIZE = 1;
    private SharedPreferences preferences = null;

    private OnFragmentInteractionListener mListener;
    private ArrayList<Agence> agences;
    private AgenceDAO agenceDAO = null;
    private ArrayAdapter<String> agenceAdapter;

    RadioButton depot = null;
    RadioButton retrait = null;
    Spinner agence = null;
    EditText numero = null;
    EditText montant = null;
    TextView agencecaissier = null;
    ImageButton rech = null;
    ImageButton detailsButton = null;
    ImageButton imageButton = null;
    ImageButton compteButton = null;
    TextView numpiece = null;
    TextView numcompt = null;
    TextView nomprenom = null;
    TextView soldedisponible = null;
    TextView solde = null;
    TextView sexe = null;
    TextView produit = null;
    TextView numproduit = null;

    Button membres = null;
    Button valider = null;
    Button annuler = null;
    public LoadCLientInfoTask loadCLientInfoTask = null;
    private SendOperationTask sendOperationTask = null;
    private LoadCLientDetailsTask loadCLientDetailsTask = null;
    String top = null;
    private String bluetoothConfig = "imprimenteexterne";
    private CaissierDAO caissierDAO;
    private MainActivity mParent;
    private String msg = null;

    private MembreDAO membreDAO = null;
    private CompteDAO compteDAO = null;
    private Membre membre;
    private Compte compte;
    private OperationDAO operationDAO;
    private String result = "";
    private boolean groupe = false;
    private Caissier caissier;
    private String[] agencedata;
    private ImageButton camBtn;
    private AlertDialog alertDialog;

    boolean resultat_distant = false ;

    public EpargneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EpargneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EpargneFragment newInstance(String param1, String param2) {
        EpargneFragment fragment = new EpargneFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_epargne, container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        depot = (RadioButton) v.findViewById(R.id.depot);
        retrait = (RadioButton) v.findViewById(R.id.retrait);
        agence = (Spinner) v.findViewById(R.id.agence);
        numero = (EditText) v.findViewById(R.id.numero);
        montant = (EditText) v.findViewById(R.id.montant);
        rech = (ImageButton) v.findViewById(R.id.rech);
        compteButton = (ImageButton) v.findViewById(R.id.compteBtn);
        camBtn = (ImageButton) v.findViewById(R.id.camBtn);
        agencecaissier = (TextView) v.findViewById(R.id.agencecaissier);
        numcompt = (TextView) v.findViewById(R.id.numcompte);
        numpiece = (TextView) v.findViewById(R.id.numpiece);
        nomprenom = (TextView) v.findViewById(R.id.nomprenom);
        soldedisponible = (TextView) v.findViewById(R.id.soldedisponible);
        solde = (TextView) v.findViewById(R.id.solde);
        sexe = (TextView) v.findViewById(R.id.sexe);
        numproduit = (TextView) v.findViewById(R.id.numproduit);
        produit = (TextView) v.findViewById(R.id.produit);
        membres = (Button) v.findViewById(R.id.membresbtn);
        valider = (Button) v.findViewById(R.id.valider);
        annuler = (Button) v.findViewById(R.id.annuler);

        detailsButton = (ImageButton) v.findViewById(R.id.details);
        imageButton = (ImageButton) v.findViewById(R.id.image);

        agenceDAO = new AgenceDAO(getActivity());
        membreDAO = new MembreDAO(getActivity());

        agences = agenceDAO.getAll();
        agencedata = new String[agences.size()];
        int n = 0 ;
        for (int i = 0; i < agences.size(); ++i) {
            agencedata[n] = agences.get(i).getNomagence();
            n++ ;
        }

        if (!preferences.getBoolean("agencemulti",false)) agence.setEnabled(false);

        caissierDAO = new CaissierDAO(getActivity());
        caissier = caissierDAO.getLast() ;

        operationDAO = new OperationDAO(getActivity());

        if (caissierDAO.getLast() != null && agenceDAO.getOne(caissierDAO.getLast().getAgence_id()) != null)
            agencecaissier.setText(agenceDAO.getOne(caissierDAO.getLast().getAgence_id()).getNomagence() + "   -   " + caissierDAO.getLast().getJournee());

        agenceAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, agencedata);
        agenceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        agence.setAdapter(agenceAdapter);

        compteDAO = new CompteDAO(getActivity());

        agence.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                init();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                init();
            }
        });

        compteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CompteActivity.class);
                intent.putExtra("TYPE", Compte.EPARGNE);
                groupe = false ;
                startActivityForResult(intent, CHOOSE_COMPTE_REQUEST);
                membreDAO.updateSaisie() ;
            }
        });

        membres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MembreActivity.class);
                intent.putExtra("nummembre", compte.getNummembre());
                intent.putExtra("numcompte", compte.getNumcompte());
                startActivityForResult(intent, CHOOSE_MEMBRE_REQUEST);
            }
        });

        camBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //new IntentIntegrator(getActivity()).initiateScan();

                Intent intent = new Intent(getActivity(), ScannerActivity.class);
                startActivityForResult(intent, CHOOSE_CODE_BAR_REQUEST);

            }
        });

        rech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compteDAO.deleteLasts();
                if (preferences.getBoolean("membre", false)) {
                    compte = compteDAO.getOne(numero.getText().toString());
                    if (compte != null) {
                        refreshData(compte);
                        Toast.makeText(getActivity(), R.string.inforecup, Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(getActivity(), R.string.echecinfo, Toast.LENGTH_SHORT).show();
                } else {

                    loadCLientInfoTask = new LoadCLientInfoTask(numero.getText().toString(), agence.getSelectedItemPosition());

                    if (numero.getText().length() > 0)
                        loadCLientInfoTask.execute(Url.getLoadCLientInfoUrl(getActivity()));
                    else
                        Toast.makeText(getActivity(), R.string.numcptincorect, Toast.LENGTH_LONG).show();

                    /*
                    // SI le mode de connexion est Internet
                    if (!preferences.getBoolean(ParametreActivity.TYPE_COMMUNICATION, false)) {
                    }
                    // Sinon si le mode de connexion est Sms
                    else {
                        String msg = numero.getText().toString() + ":E:" + agences.get(agence.getSelectedItemPosition()).getNumagence();
                        mParent.sendSMS(msg, 0);
                    }
                    */
                }
            }
        });


        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // SI le mode de connexion est Internet
                if (!Utiles.isConnected(getActivity())) {
                    Toast.makeText(getActivity(), R.string.anyconnexion, Toast.LENGTH_LONG).show();
                    return;
                }

                if (!preferences.getBoolean(ParametreActivity.TYPE_COMMUNICATION, false)) {
                    loadCLientDetailsTask = new LoadCLientDetailsTask(numero.getText().toString(), agence.getSelectedItemPosition());
                    if (numero.getText().length() > 0)
                        loadCLientDetailsTask.execute(Url.getLoadCLientDetailUrl(getActivity()));
                    else
                        Toast.makeText(getActivity(), R.string.numcptincorect, Toast.LENGTH_LONG).show();
                }
                // Sinon si le mode de connexion est Sms
                else {

                }
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // SI le mode de connexion est Internet
                if (!preferences.getBoolean(ParametreActivity.TYPE_COMMUNICATION, false)) {
                    if (!Utiles.isConnected(getActivity())) {
                        Toast.makeText(getActivity(), R.string.anyconnexion, Toast.LENGTH_LONG).show();
                        return;
                    }
                    affichePhoto();

                    //LoadCLientPhotoTask loadCLientPhotoTask = new LoadCLientPhotoTask(numero.getText().toString(), agence.getSelectedItemPosition());
                    //loadCLientPhotoTask.execute(Url.getLoadCLientPhotoUrl(getActivity()));
                }
                // Sinon si le mode de connexion est Sms
                else {

                }
            }
        });


        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isCorrect()) return;
                if (depot.isChecked()) top = "VSE";
                if (retrait.isChecked()) top = "RE";

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.app_name);

                if (depot.isChecked()) builder.setTitle(getString(R.string.depott));
                if (retrait.isChecked()) builder.setTitle(getString(R.string.retrai));

                builder.setMessage(getString(R.string.continuer));
                builder.setPositiveButton(R.string.valider, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (retrait.isChecked() && resultat_distant==false){
                            Toast.makeText(getActivity(), R.string.detailcomptdistant, Toast.LENGTH_LONG).show();
                            return;
                        }

                        Operation operation = new Operation();

                        operation.setNom(compte.getNom());
                        operation.setPrenom(compte.getPrenom());
                        operation.setNumproduit(numproduit.getText().toString());
                        operation.setUser_id(caissierDAO.getLast().getId());
                        operation.setNumcompte(compte.getNumcompte());
                        operation.setMontant(Float.parseFloat(montant.getText().toString()));
                        operation.setAgence(agences.get(agence.getSelectedItemPosition()).getNumagence());
                        operation.setMise(0);
                        operation.setSync(0);

                        String lib = "" ;
                        if (top.equals("RE")) {
                            lib += "RETRAIT, No : " + operation.getNumcompte() + " , Mte : " + String.valueOf(operation.getMontant());
                            operation.setTypeoperation(1);
                        } else {
                            lib += "DEPOT, No : " + operation.getNumcompte() + " , Mte : " + String.valueOf(operation.getMontant());
                            operation.setTypeoperation(0);
                        }
                        operation.setLibelle(lib);



                        if (operation.getMontant() > 0) {

                            float sold = 0;
                            sold = refreshSolde();

                            if (top.equals("VSE") && caissier.getRetraitMax() < (operation.getMontant() + sold)) {
                                Toast.makeText(getActivity(), R.string.soldeattend, Toast.LENGTH_SHORT).show();
                                return;
                            } else if (top.equals("RE") && sold < operation.getMontant()) {
                                Toast.makeText(getActivity(), R.string.mtni, Toast.LENGTH_SHORT).show();
                                return;
                            } else if (top.equals("RE") && compte.getSolde() < operation.getMontant()) {
                                Toast.makeText(getActivity(), R.string.soldedispoerror, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (top.equals("RE") && preferences.getBoolean("demande_pin",true)){
                                checkPin(operation) ;
                                return;
                            }
                        }

                        saveOperation(operation);
                                /*
                                //Toast.makeText(getActivity(), preferences.getInt("groupemembre",0) + "",Toast.LENGTH_SHORT).show();
                                sendOperationTask = new SendOperationTask(top, numproduit.getText().toString(), numcompt.getText().toString(), Float.parseFloat(montant.getText().toString().replace(",", ".")), agence.getSelectedItemPosition());
                                //sendOperationTask.execute(Url.getSendOperationUrl(getActivity())) ;

                                if (operation.getMontant() > 0) {
                                    if (top.equals("RE") && Float.parseFloat(montant.getText().toString().replace(",", ".")) < Float.parseFloat(solde.getText().toString().replace(",", ".")))
                                        sendOperationTask.execute(Url.getSendOperationUrl(getActivity()));
                                    else if (top.equals("VSE"))
                                        sendOperationTask.execute(Url.getSendOperationUrl(getActivity()));
                                    else
                                        Toast.makeText(getActivity(), getString(R.string.mteincorrect), Toast.LENGTH_LONG).show();
                                }
                                */

                    }
                });
                builder.setNegativeButton(R.string.annuler, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                if (montant.getText().toString().trim().length() > 0 && Float.valueOf(montant.getText().toString()) > 0) {

                    final AlertDialog alertdialog = builder.create();
                    alertdialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            if (retrait.isChecked()) {
                                alertdialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.state0));
                                alertdialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.state0));
                                alertdialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.state0));
                            } else {
                                alertdialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.my_accent));
                                alertdialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.my_accent));
                            }
                        }
                    });
                    alertdialog.show();
                } else
                    Toast.makeText(getActivity(), R.string.mtnincorrect, Toast.LENGTH_SHORT).show();


            }
        });

        montant.setText("");

        return v;
    }

    private void saveOperation(Operation operation) {

        ArrayList<Membre> membreArrayList = membreDAO.getAllByNumMembre(compte.getNummembre());
        int n = membreArrayList.size();
        float total = 0;
        if (groupe) {
            for (int j = 0; j < n; j++) {
                total += membreArrayList.get(j).getSaisi();
            }
            // SI le compte est un groupe vérifier si le montant des membres concorde avec celui du groupe saisi

            if (total > 0) {
                if (total != Float.valueOf(montant.getText().toString())) {
                    Toast.makeText(getActivity(), R.string.nogrouptotal, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }

        int nump = caissier.getNumpiece() ;
        nump++ ;
        operation.setNumpiece(preferences.getString(ConnexionActivity.DEBUTPIECE,"")+ "" + Utiles.generer(nump));
        operation.setToken(caissier.getCodeguichet() + "/" + compte.getNumcompte() + "/" + System.currentTimeMillis());
        try {
            operation.setDateoperation(DAOBase.formatterj.parse(caissier.getJournee()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long id = operationDAO.add(operation) ;
        if (id>0){
            compte.setSolde(compte.getSolde()+operation.getMontant());
            if (operation.getTypeoperation() == 0) compte.setSoldedisponible(compte.getSoldedisponible()+operation.getMontant());
            else compte.setSoldedisponible(compte.getSoldedisponible()-operation.getMontant());
            compteDAO.update(compte) ;
            caissier.setNumpiece(nump);
            caissierDAO.update(caissier) ;
            numpiece.setText(operation.getNumpiece());
            boolean imp = preferences.getBoolean(bluetoothConfig, false);
            if (imp) {
                try {
                    PrinterUtils printerUtils = new PrinterUtils(getActivity());
                    printerUtils.printTicket(operation, "OPERATION D'EPARGNE");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    //PrintPDA printPDA = new PrintPDA(getActivity());
                    //printPDA.printTicket(operation, "OPERATION D'EPARGNE");

                    PrintPDAMobiPrint3 printPDAMobiPrint3 = new PrintPDAMobiPrint3(getActivity());
                    printPDAMobiPrint3.printTicket(operation, "OPERATION D'EPARGNE");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Toast.makeText(getActivity(), "Impression lancée", Toast.LENGTH_SHORT).show();

            if (total>0){

                OperationSec operationSec = null ;
                OperationSecDAO operationSecDAO = new OperationSecDAO(getActivity()) ;
                for (int j = 0; j < n; j++) {
                    if (membreArrayList.get(j).getSaisi()<=0) continue;
                    operationSec = new OperationSec() ;
                    operationSec.setIdpersonne(membreArrayList.get(j).getIdpersonne()); ;
                    operationSec.setMte(membreArrayList.get(j).getSaisi()); ;
                    operationSec.setOperation_id(id); ;
                    operationSec.setNummembre(membreArrayList.get(j).getCodemembre());

                    long res = operationSecDAO.add(operationSec) ;
                    if (res>0){
                        membreArrayList.get(j).setSaisi(0);
                        membreDAO.update(membreArrayList.get(j)) ;
                    }
                }
            }

            refreshSolde() ;
            resultat_distant = false ;
            Toast.makeText(getActivity(), "Opération sauvegarder", Toast.LENGTH_SHORT).show();
            init();
        }
    }


    private void checkPin(final Operation operation) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()) ;
        builder.setTitle(getString(R.string.saisirpass)) ;
        LinearLayout linearLayout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.demandepinform,null);
        builder.setView(linearLayout) ;
        final EditText et_password = (EditText) linearLayout.findViewById(R.id.password);
        final EditText et_confirmed = (EditText) linearLayout.findViewById(R.id.confirmation);
        Button valider = (Button) linearLayout.findViewById(R.id.valider);
        Button annuler = (Button) linearLayout.findViewById(R.id.annuler);
        Button sendpin = (Button) linearLayout.findViewById(R.id.sendpin);


        alertDialog = builder.show() ;
        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_password.getText().length()>0){
                    String pass = et_password.getText().toString();
                    String pin = membreDAO.getByNumMembre(compte.getNummembre()).getCodeRetrait() ;
                    if (pin.equals(pass)){
                        if (Utiles.isConnected(getActivity())) {
                            //saveOperation(operation);
                            if (operation.getTypeoperation()==0)top = "VSE";
                            else top = "RE" ;

                            sendOperationTask = new SendOperationTask(top, numproduit.getText().toString(), numcompt.getText().toString(), Float.parseFloat(montant.getText().toString().replace(",", ".")), agence.getSelectedItemPosition()) ;
                            sendOperationTask.execute(Url.getSendOperationUrl(getActivity())) ;
                        }
                        else {
                            Toast.makeText(getActivity(), R.string.noconnexion, Toast.LENGTH_SHORT).show();
                        }
                        alertDialog.dismiss();
                    }
                    else {
                        makeAlertDialog(getString(R.string.pinerror),false);
                    };
                }
            }
        });

        sendpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendPinTask sendPinTask = new SendPinTask() ;
                sendPinTask.execute() ;
            }
        });
    }


    private float refreshSolde() {
        ArrayList<Operation> operations = operationDAO.getAll();
        float sold = caissier.getSolde();
        for (int j = 0; j < operations.size(); ++j) {
            if (operations.get(j).getTypeoperation() == 0 || operations.get(j).getTypeoperation()==3 || operations.get(j).getTypeoperation()==4) sold += operations.get(j).getMontant();
            else sold -= operations.get(j).getMontant();
        }
        getActivity().setTitle("Solde : " + Utiles.formatMtn(sold) + " F");

        return sold ;
    }

    private boolean isCorrect() {
        if (numpiece.getText().length() > 3) {
            Toast.makeText(getActivity(), R.string.infoactu, Toast.LENGTH_SHORT).show();
            return false;
        }


        if (!depot.isChecked() && !retrait.isChecked()) {
            Toast.makeText(getActivity(), getString(R.string.versmterror), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (montant.getText().length() == 0) {
            Toast.makeText(getActivity(), getString(R.string.solderror), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (Float.parseFloat(solde.getText().toString().replace(",", ".")) < Float.parseFloat(montant.getText().toString().replace(",", ".")) && retrait.isChecked()) {
            Toast.makeText(getActivity(), getString(R.string.montanterror), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (numero.getText().toString().equals("")) {
            Toast.makeText(getActivity(), getString(R.string.numeroerror), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*
        if (preferences.getBoolean("membre", false)) {
            for (int i = 0; i < agences.size(); ++i) {
                if (agences.get(i).getNumagence().equals(caissierDAO.getLast().getAgence_id()))
                    pos = i;
            }

            agence.setSelection(pos);
            agence.setEnabled(false);
        }
        */

        // SELECTIONNER L'AGENCE DU CAISSIER COMME AGENCE
        //
        // PAR DEFAUT
        int pos = 0;
        for (int i = 0; i < agences.size(); ++i) {
            Log.e("CA" , caissier.getAgence_id()) ;
            Log.e("A" + i , agences.get(i).getNumagence()) ;
            if (agences.get(i).getNumagence().equals(caissier.getAgence_id())) {
                Log.e("A" + i , agences.get(i).getNomagence()) ;
                pos = i ;
            }
        }

        agence.setSelection(pos);
        Log.e("POS" , String.valueOf(pos)) ;
        Log.e("SELECTED", String.valueOf(agence.getSelectedItemPosition())) ;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("DIX","1") ;
        if (resultCode == getActivity().RESULT_OK) {
            init();
            ((MainActivity) getActivity()).selectCaisseFragment();
            if (requestCode == CHOOSE_COMPTE_REQUEST) {
                compte = compteDAO.getOne(data.getLongExtra(MainActivity.COMPTE, 0));
                Log.e("DEBUG", String.valueOf(data.getLongExtra(MainActivity.COMPTE, 0)));
                if (compte != null) {
                    refreshData(compte);
                }
            } else if (requestCode == CHOOSE_MEMBRE_REQUEST) {
                JSONArray jsonArray = null;
                JSONObject jsonObject = null;
                if (compte == null) compte = new Compte();
                compte = compteDAO.getOne(data.getStringExtra("numcompte")) ;
                montant.post(new Runnable() {
                    @Override
                    public void run() {
                        montant.setText(String.valueOf(data.getFloatExtra("montant",0)));
                    }
                }) ;
                refreshData(compte);
            }
            else if (requestCode == CHOOSE_CODE_BAR_REQUEST) {
                String nocompte = data.getStringExtra(NOCOMPTE);
                Log.e("Nocompte", nocompte) ;
                if (nocompte!=null) {
                    compte = compteDAO.getOne(nocompte);
                    if (compte != null) {
                        refreshData(compte);
                    }
                    else
                        Toast.makeText(getActivity(), R.string.cmptint, Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mParent = (MainActivity) getActivity();
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        init();

        Intent intent = getActivity().getIntent();
        if (intent != null) {
            msg = intent.getStringExtra("MSG");
            if (msg != null && msg.endsWith("E")) {
                refreshDataSMS(msg);
            }
            msg = intent.getStringExtra("MTN");
            if (msg != null && msg.endsWith("E")) {
                processOperationResultSMS(msg);
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public class SendOperationTask extends AsyncTask<String, Void, String> {

        String numcompte = null;
        String interfac = "E";
        String typop = null;
        String agence = null;
        String numprod = null;
        float montant = 0;
        int pos = 0;

        OperationDAO operationDAO = null;
        Caissier c = null;

        public SendOperationTask(String typop, String numproduit, String numCompte, float montant, int p) {
            this.numcompte = numCompte;
            this.typop = typop;
            this.montant = montant;
            this.numprod = numproduit;
            this.pos = p;
        }

        @Override
        protected String doInBackground(String... urls) {
            //TelephonyManager telephonyManager =  ( TelephonyManager ) getSystemService (Context.TELEPHONY_SERVICE);
            //String imei = telephonyManager.getDeviceId ();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            ArrayList<Compte> comptes = compteDAO.getLasts();
            int n = comptes.size();
            float total = 0;

            WifiManager wifiManager = null;
            wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wInfo = wifiManager.getConnectionInfo();
            String macAddress ="";
            if(wInfo!=null)macAddress = wInfo.getMacAddress();

            TelephonyManager telephonyManager = (TelephonyManager) getActivity().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            String iMei = null ;
            iMei = telephonyManager.getDeviceId();
            try {
                Log.d("IMEI", iMei);
                Log.d("MACADRESS", macAddress);
            }catch (Exception e){
                e.printStackTrace();
            }

            if (typop.equals("RE")) {
                for (int i = 0; i < n; i++) {
                    if (comptes.get(i).getSolde() > comptes.get(i).getSoldedisponible()) {
                        //montant-=comptes.get(i).getSolde() ;
                        //comptes.get(i).setSolde(0);
                        //compteDAO.update(comptes.get(i)) ;
                        return "SOLDE:" + comptes.get(i).getNom();
                    }
                }
            }

            Date date = new Date();
            FormBody.Builder formBuilder = new FormBody.Builder();
            formBuilder.add("iMei", iMei) ;
            formBuilder.add("macAddress", macAddress);
            formBuilder.add("NumCompte", numcompte);
            formBuilder.add("interface", "E");
            formBuilder.add("NumProduit", numprod);
            formBuilder.add("journee", caissier.getJournee());
            formBuilder.add("montant1", Utiles.formatMtn2(montant));
            formBuilder.add("montant2", Utiles.formatMtn2(montant));
            formBuilder.add("TypeOperation", typop);
            formBuilder.add("mise", "0");

            Log.e("MTE TOTAL", String.valueOf(montant));

            formBuilder.add("AgenceClient", String.valueOf(agences.get(pos).getNumagence()));
            formBuilder.add("guichet", String.valueOf(c.getCodeguichet()));
            formBuilder.add("numagence", String.valueOf(c.getAgence_id()));


            formBuilder.add("ddb", c.getDdb());
            formBuilder.add("dl", c.getDl());
            formBuilder.add("dp", c.getDp());
            formBuilder.add("di", c.getDi());


            if (groupe) {
                formBuilder.add("nbre", String.valueOf(n));
                //total += compte.getSoldedisponible() ;
                n = comptes.size();
                if (n > 0)
                    for (int i = 0; i < n; i++) {
                        if (comptes.get(i).getSolde() <= 0) continue;
                        formBuilder.add("nummembre" + i, String.valueOf(comptes.get(i).getNumcompte()));
                        formBuilder.add("mte" + i, Utiles.formatMtn2(comptes.get(i).getSolde()));
                        formBuilder.add("idpersonne" + i, comptes.get(i).getPrenom());
                    }
            }
            total = compte.getSolde();

            if (montant > total && typop.equals("RE")) return "MTE";
            else {
                String result = "" ;

                try {
                    result = Utiles.POST(urls[0],formBuilder.build());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result ;
            }
        }


        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            super.onPostExecute(result);

            Log.e("DEBUG", result);

            //Toast.makeText(getActivity(), preferences.getInt("groupemembre",0) + "",Toast.LENGTH_SHORT).show();
            if (result.equals("MTE"))
                Toast.makeText(getActivity(), R.string.soldedispoinsuf, Toast.LENGTH_SHORT).show();
            if (result.startsWith("SOLDE"))
                Toast.makeText(getActivity(), getResources().getString(R.string.soldemembreincorrect) + " : " + result.split(":")[1], Toast.LENGTH_SHORT).show();
            else if (result.contains("ConnexionBase"))
                Toast.makeText(getActivity(), R.string.connexionechec, Toast.LENGTH_SHORT).show();
            else processOperationResult(result);

            getActivity().dismissDialog(PROGRESS_DIALOG_ID);
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getActivity().showDialog(PROGRESS_DIALOG_ID);
            if (operationDAO == null) operationDAO = new OperationDAO(getActivity());

            c = new CaissierDAO(getActivity()).getLast();
        }
    }

    public void processOperationResultSMS(String result) {

        Log.e("ETAPE", "SMS");
        Caissier c = new CaissierDAO(getActivity()).getLast();
        OperationDAO operationDAO = new OperationDAO(getActivity());
        if (result.equals("NUMPIECE")) numpiece.setText(R.string.echec1);
        if (result.contains("AGENCEDIFF")) numpiece.setText(R.string.echec);
        if (result.contains("NUMPECEECHEC1")) numpiece.setText(R.string.echec2p);
        //if (result.contains("NUMPECEECHEC1")) numpiece.setText(R.string.echec2);
        if (result.contains("NUMPECEECHEC2")) numpiece.setText(R.string.echec2p);
        //if (result.contains("NUMPECEECHEC2")) numpiece.setText(R.string.echec3);
        if (result.contains("NUMPECEECHEC3")) numpiece.setText(R.string.echec2p);
        //if (result.contains("NUMPECEECHEC3")) numpiece.setText(R.string.echec4);

        if (result.contains("OK:") && result.split(":").length >= 3) {
            compteDAO.deleteLasts();
            Toast.makeText(getActivity(), R.string.opsuccess, Toast.LENGTH_LONG).show();
            String lib = "";
            Operation op = operationDAO.getLast();
            op.setNumpiece(result.split(":")[1]);
            numpiece.setText(result.split(":")[1]);
            if (top.equals("RE")) {
                lib += "RETRAIT, No : " + op.getNumcompte() + " , Mte : " + String.valueOf(op.getMontant());
                op.setTypeoperation(1);
            } else {
                lib += "DEPOT, No : " + op.getNumcompte() + " , Mte : " + String.valueOf(op.getMontant());
                op.setTypeoperation(0);
            }
            op.setLibelle(lib);
            try {
                op.setDateoperation(DAOBase.formatter.parse(c.getJournee()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            op.setUser_id(c.getId());

            if (operationDAO.update(op) > 0) {

                refreshSolde();

                float f = Float.parseFloat(solde.getText().toString().replace(",", "."));
                if (top.equals("RE")) {
                    f -= op.getMontant();
                } else {
                    f += op.getMontant();
                }

                String l = String.format("%.2f", f);
                solde.setText(l);


                boolean imp = preferences.getBoolean(bluetoothConfig, false);
                if (imp) {
                    try {
                        PrinterUtils printerUtils = new PrinterUtils(getActivity());
                        printerUtils.printTicket(operationDAO.getLast(), "OPERATION D'EPARGNE");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        PrintPDAMobiPrint3 printPDAMobiPrint3 = new PrintPDAMobiPrint3(getActivity());
                        printPDAMobiPrint3.printTicket(operationDAO.getLast(), "OPERATION D'EPARGNE");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
            ;
            //init();
            montant.setText("0");
        } else Toast.makeText(getActivity(), R.string.serverno, Toast.LENGTH_LONG).show();
    }


    public void processOperationResult(String result) {

        Caissier c = new CaissierDAO(getActivity()).getLast();
        OperationDAO operationDAO = new OperationDAO(getActivity());

        if (result.contains("NUMPIECE")) numpiece.setText(R.string.echec1);
        if (result.contains("AGENCEDIFF")) numpiece.setText(R.string.echec);
        if (result.contains("ConnectTimeoutException")) numpiece.setText(R.string.connexionechec);
        if (result.contains("NUMPECEECHEC1")) numpiece.setText(R.string.echec2p);
        //if (result.contains("NUMPECEECHEC1")) numpiece.setText(R.string.echec2);
        if (result.contains("NUMPECEECHEC2")) numpiece.setText(R.string.echec2p);
        //if (result.contains("NUMPECEECHEC2")) numpiece.setText(R.string.echec3);
        if (result.contains("NUMPECEECHEC3")) numpiece.setText(R.string.echec2p);
        //if (result.contains("NUMPECEECHEC3")) numpiece.setText(R.string.echec4);

        if (result.contains("OK:") && result.split(":").length >= 3) {
            //compteDAO.deleteLasts();
            Toast.makeText(getActivity(), R.string.opsuccess, Toast.LENGTH_LONG).show();
            String lib = "";
            Operation op = new Operation();
            op.setAgence(c.getAgence_id());
            op.setMontant(Float.parseFloat(montant.getText().toString()));
            op.setNom(nomprenom.getText().toString().split(" ")[0]);
            if (nomprenom.getText().toString().split(" ").length > 1)
                op.setPrenom(nomprenom.getText().toString().split(" ")[1]);
            op.setNumcompte(numcompt.getText().toString());
            op.setNummenbre(numero.getText().toString());
            op.setNumpiece(result.split(":")[1]);
            op.setNumpicedef(result.split(":")[1]);
            op.setSync(1);
            try {
                op.setDateoperation(DAOBase.formatter.parse(c.getJournee()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            numpiece.setText(result.split(":")[1]);
            if (top.equals("RE")) {
                lib += "RETRAIT, No : " + op.getNumcompte() + " , Mte : " + String.valueOf(op.getMontant());
                op.setTypeoperation(1);
            } else {
                lib += "DEPOT, No : " + op.getNumcompte() + " , Mte : " + String.valueOf(op.getMontant());
                op.setTypeoperation(0);
            }
            op.setLibelle(lib);
            op.setUser_id(c.getId());

            if (operationDAO.add(op) > 0) {

                refreshSolde();

                float f = Float.parseFloat(solde.getText().toString().replace(",", "."));

                if (top.equals("RE")) {
                    f -= op.getMontant();
                    compte.setSolde(compte.getSolde()-op.getMontant());
                } else {
                    f += op.getMontant();
                    compte.setSolde(compte.getSolde()+op.getMontant());
                }

                compteDAO.update(compte) ;

                String l = Utiles.formatMtn2(f);
                solde.setText(l);

                boolean imp = preferences.getBoolean(bluetoothConfig, false);
                if (imp) {
                    try {
                        PrinterUtils printerUtils = new PrinterUtils(getActivity());
                        printerUtils.printTicket(operationDAO.getLast(), "OPERATION D'EPARGNE");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        PrintPDAMobiPrint3 printPDAMobiPrint3 = new PrintPDAMobiPrint3(getActivity());
                        printPDAMobiPrint3.printTicket(operationDAO.getLast(), "OPERATION D'EPARGNE");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
            ;
            //init();
            montant.setText("0");
        }
    }


    public void saveOperation() {

        if (depot.isChecked()) top = "VSE";
        if (retrait.isChecked()) top = "RE";

        Caissier c = new CaissierDAO(getActivity()).getLast();
        OperationDAO operationDAO = new OperationDAO(getActivity());
        compteDAO = new CompteDAO(getActivity());
        compte = compteDAO.getOne(numero.getText().toString());
        Toast.makeText(getActivity(), R.string.opsuccess, Toast.LENGTH_LONG).show();
        String lib = "";
        Operation op = new Operation();
        op.setAgence(c.getAgence_id());
        op.setMontant(Float.parseFloat(montant.getText().toString()));
        op.setNom(compte.getNom());
        op.setPrenom(compte.getPrenom());
        op.setNumcompte(compte.getNumcompte());
        op.setNummenbre(compte.getNummembre());
        op.setNumpiece(ENUMPIECE);
        numpiece.setText(op.getNumpiece());
        if (top.equals("RE")) {
            lib += "RETRAIT, No : " + op.getNumcompte() + " , Mte : " + String.valueOf(op.getMontant());
            op.setTypeoperation(1);
            compte.setSolde(compte.getSolde() - op.getMontant());
        } else {
            lib += "DEPOT, No : " + op.getNumcompte() + " , Mte : " + String.valueOf(op.getMontant());
            op.setTypeoperation(0);
            compte.setSolde(compte.getSolde() + op.getMontant());
        }
        op.setLibelle(lib);
        op.setUser_id(c.getId());

        if (operationDAO.add(op) > 0) {

            compteDAO.update(compte);
            refreshSolde();

            float f = Float.parseFloat(solde.getText().toString().replace(",", "."));

            if (top.equals("RE")) {
                f -= op.getMontant();
            } else {
                f += op.getMontant();
            }

            solde.setText(Utiles.formatMtn(f));

            boolean imp = preferences.getBoolean(bluetoothConfig, false);
            if (imp) {
                try {
                    PrinterUtils printerUtils = new PrinterUtils(getActivity());
                    printerUtils.printTicket(operationDAO.getLast(), "OPERATION D'EPARGNE");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    PrintPDAMobiPrint3 printPDAMobiPrint3 = new PrintPDAMobiPrint3(getActivity());
                    printPDAMobiPrint3.printTicket(operationDAO.getLast(), "OPERATION D'EPARGNE");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        ;
        //init();
        montant.setText("0");
    }


    public class LoadCLientInfoTask extends AsyncTask<String, Void, String> {

        String numcompte = null;
        int pos = 0;

        public LoadCLientInfoTask(String numcompte, int p) {
            this.numcompte = numcompte;
            pos = p;
        }

        @Override
        protected String doInBackground(String... urls) {
            //TelephonyManager telephonyManager =  ( TelephonyManager ) getSystemService (Context.TELEPHONY_SERVICE);
            //String imei = telephonyManager.getDeviceId ();

            FormBody.Builder formBuilder = new FormBody.Builder();;
            formBuilder.add("numcompte", numcompte);
            formBuilder.add("token", "E");

            formBuilder.add("agence", String.valueOf(agences.get(pos).getNumagence()));


            String result = "" ;

            try {
                result = Utiles.POST(urls[0],formBuilder.build());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result ;
        }

        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            super.onPostExecute(result);

            Log.e("DEBUG", result);
            if (result.contains("CONNEXION"))
                Toast.makeText(getActivity(), R.string.connexionechec, Toast.LENGTH_LONG).show();
            else if (result.contains("ECHEC"))
                Toast.makeText(getActivity(), R.string.nocompt, Toast.LENGTH_LONG).show();
            else {
                resultat_distant = true ;
                refreshData(result);
            }
            getActivity().dismissDialog(PROGRESS_DIALOG_ID);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getActivity().showDialog(PROGRESS_DIALOG_ID);
        }
    }


    public class LoadCLientDetailsTask extends AsyncTask<String, Void, String> {

        String numcompte = null;
        int pos = 0;

        public LoadCLientDetailsTask(String numcompte, int pos) {
            this.numcompte = numcompte;
            this.pos = pos;
        }

        @Override
        protected String doInBackground(String... urls) {
            //TelephonyManager telephonyManager =  ( TelephonyManager ) getSystemService (Context.TELEPHONY_SERVICE);
            //String imei = telephonyManager.getDeviceId ();

            FormBody.Builder formBuilder = new FormBody.Builder();
            formBuilder.add("numcompte", numcompte);
            formBuilder.add("agence", String.valueOf(agences.get(pos).getNumagence()));
            formBuilder.add("token", "E");



            String result = "" ;

            try {
                result = Utiles.POST(urls[0],formBuilder.build());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result ;
        }


        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            super.onPostExecute(result);

            Log.e("REPONSEEEEEEEEEEEE", result);
            if (result.equals("ECHEC CONNEXION"))
                Toast.makeText(getActivity(), "Echec de connexion à la base de donnée", Toast.LENGTH_LONG).show();
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                RelativeLayout rl = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.infolistlayout, null);
                LinearLayout infoitemlayout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.infoitemlayout, null);

                Button fermer = (Button) rl.findViewById(R.id.annuler);

                LinearLayout racine = (LinearLayout) rl.findViewById(R.id.racine);
                racine.removeAllViews();

                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                param.setMargins(5, 5, 5, 5);

                builder.setView(rl);

                final AlertDialog alert = builder.create();
                alert.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.my_accent));
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.my_accent));
                    }
                });
                alert.show();

                fermer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.dismiss();
                    }
                });


                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray infos = jsonObject.getJSONArray("infos");
                    JSONObject info = null;

                    TextView piece = null;
                    TextView cpt = null;
                    TextView savt = null;
                    TextView sapres = null;
                    TextView libelle = null;

                    for (int i = 0; i < infos.length(); ++i) {
                        info = infos.getJSONObject(i);
                        infoitemlayout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.infoitemlayout, null);

                        piece = (TextView) infoitemlayout.findViewById(R.id.numpiece);
                        cpt = (TextView) infoitemlayout.findViewById(R.id.numcpt);
                        savt = (TextView) infoitemlayout.findViewById(R.id.savt);
                        sapres = (TextView) infoitemlayout.findViewById(R.id.sapres);
                        libelle = (TextView) infoitemlayout.findViewById(R.id.libelle);

                        Date d = DAOBase.formatter.parse(info.getString("DATEVALEUR"));
                        piece.setText(info.getString("NUMPIECE"));
                        cpt.setText(DAOBase.formatterj.format(d));
                        savt.setText(info.getString("MONTANTCREDIT") + " F");
                        sapres.setText(info.getString("MONTANTDEBIT") + " F");
                        libelle.setText(info.getString("LIBELLEOPERATION"));

                        racine.addView(infoitemlayout, param);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            getActivity().dismissDialog(PROGRESS_DIALOG_ID);
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getActivity().showDialog(PROGRESS_DIALOG_ID);
        }
    }


    public class LoadCLientPhotoTask extends AsyncTask<String, Void, String> {

        String numcompte = null;
        int pos = 0;

        public LoadCLientPhotoTask(String numcompte, int pos) {
            this.numcompte = numcompte;
            this.pos = pos;
        }

        @Override
        protected String doInBackground(String... urls) {
            //TelephonyManager telephonyManager =  ( TelephonyManager ) getSystemService (Context.TELEPHONY_SERVICE);
            //String imei = telephonyManager.getDeviceId ();

            FormBody.Builder formBuilder = new FormBody.Builder();
            formBuilder.add("numcompte", numcompte);
            formBuilder.add("agence", String.valueOf(agences.get(pos).getNumagence()));
            formBuilder.add("token", "E");



            String result = "" ;

            try {
                result = Utiles.POST(urls[0],formBuilder.build());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result ;
        }


        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            super.onPostExecute(result);

            Log.e("DEBUG", result);
            if (result.equals("ECHEC CONNEXION"))
                Toast.makeText(getActivity(), "Echec de connexion à la base de donnée", Toast.LENGTH_LONG).show();
            else {
                showPhoto(result);
            }
            getActivity().dismissDialog(PROGRESS_DIALOG_ID);
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getActivity().showDialog(PROGRESS_DIALOG_ID);
        }
    }




    public class SendPinTask extends AsyncTask<String, Void, String> {


        public SendPinTask() {

        }

        @Override
        protected String doInBackground(String... urls) {
            //TelephonyManager telephonyManager =  ( TelephonyManager ) getSystemService (Context.TELEPHONY_SERVICE);
            //String imei = telephonyManager.getDeviceId ();

            membre = membreDAO.getByNumMembre(compte.getNummembre()) ;
            String pin = membre.getCodeRetrait() ;
            String tel = membre.getTel() ;
            String msg = "ALIDE : Votre code pin de retrait est le : " + pin + ".\nVous ne devez le communiquer à personne.\nMerci" ;

            Log.e("CODE", pin) ;
            Log.e("MSG", msg) ;
            if (!tel.startsWith("00229") && !tel.startsWith("+229")) tel =  "+229"+ tel ;
            if (tel.startsWith("00229")) tel = tel.replace("00229","+229") ;
            Log.e("TEL", tel) ;


            /*
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(14);
            formBuilder.add("msg",msg));
            formBuilder.add("tel",tel));
            Utiles.POST(Url.getSend(context),nameValuePairs);
            */

            String res = "";
            try {
                res = Utiles.postTwillio(tel,msg);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return res ;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(getActivity(),"Message envoyé",Toast.LENGTH_LONG).show();
            getActivity().dismissDialog(PROGRESS_DIALOG_ID);

        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getActivity().showDialog(PROGRESS_DIALOG_ID);
        }
    }

    private void showPhoto(String result) {

        RelativeLayout rl = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.image_layout, null);

        if (numero.getText().length() > 0) ;
        else {
            Toast.makeText(getActivity(), R.string.numcptincorect, Toast.LENGTH_LONG).show();
            return;
        }
        ImageView imageView = (ImageView) rl.findViewById(R.id.imageview);
        ProgressBar progressBar = (ProgressBar) rl.findViewById(R.id.progressBar);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Button fermer = (Button) rl.findViewById(R.id.annuler);

        builder.setView(rl);

        final AlertDialog alert = builder.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.my_accent));
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.my_accent));
            }
        });
        alert.show();

        fermer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });

        try {
            JSONArray jsonArray = new JSONArray(result);
            String photo = jsonArray.getJSONObject(0).getString("photo").replace("//", "/");
            photo = photo.replace("///", "//");
            if (photo.length() > 1 && getActivity() != null)
                Picasso.with(getActivity()).load(photo).into(imageView);
            else Toast.makeText(getActivity(), R.string.echec, Toast.LENGTH_SHORT).show();
            Log.e("PHOTO", photo);
        }
        catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), R.string.echec, Toast.LENGTH_SHORT).show();
        }
    }



    private void affichePhoto() {

        RelativeLayout rl = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.image_layout, null);

        if (numero.getText().length() > 0) ;
        else {
            Toast.makeText(getActivity(), R.string.numcptincorect, Toast.LENGTH_LONG).show();
            return;
        }
        final ImageView imageView = (ImageView) rl.findViewById(R.id.imageview);
        final ProgressBar progressBar = (ProgressBar) rl.findViewById(R.id.progressBar);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Button fermer = (Button) rl.findViewById(R.id.annuler);

        builder.setView(rl);

        final AlertDialog alert = builder.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.my_accent));
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.my_accent));
            }
        });
        alert.show();

        fermer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });

        String path = Url.getPhotoPath(getActivity())+ "/caissemobile/PHOTOS/"+ numero.getText().toString().replace("E01","").replace("/","") + ".JPG"  ;
        Log.e("PATH",path) ;

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                try {
                    progressBar.setVisibility(View.GONE);
                    imageView.setImageBitmap(bitmap);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                try {
                    progressBar.setVisibility(View.GONE);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Toast.makeText(getActivity(), R.string.loding, Toast.LENGTH_SHORT).show();
            }
        } ;
        Picasso.with(getActivity()).load(path).into(target);
    }


    private void refreshData(String result) {
        JSONArray jsonArray = null;
        JSONObject jsonObject = null;
        this.result = result;
        if (compte == null) compte = new Compte();
        try {

            jsonObject = new JSONObject(result);
            jsonArray = jsonObject.getJSONArray("info");
            init();

            numcompt.setText(jsonArray.getString(0));
            compte.setNumcompte(jsonArray.getString(0));

            nomprenom.setText(jsonArray.getString(1) + " " + jsonArray.getString(2));
            compte.setNom(jsonArray.getString(1));
            compte.setPrenom(jsonArray.getString(2));

            solde.setText(jsonArray.getString(3));
            soldedisponible.setText(jsonArray.getString(4));
            compte.setSolde((float) jsonArray.getDouble(3));
            compte.setSoldedisponible((float) jsonArray.getDouble(4));


            sexe.setText(jsonArray.getString(5));
            compte.setSexe(jsonArray.getString(5));

            numproduit.setText(jsonArray.getString(8));
            compte.setNumProduit(jsonArray.getString(8));

            produit.setText(jsonArray.getString(9));
            compte.setProduit(jsonArray.getString(9));

            numpiece.setText("-");
            Toast.makeText(getActivity(), R.string.inforecup, Toast.LENGTH_SHORT).show();

            Log.e("GROUPE", jsonArray.getString(11));
            /*if (jsonArray.getString(11).equals("O")) {
                groupe = true;
                membres.setEnabled(true);
                JSONArray jsonCompteArray = jsonObject.getJSONArray("membres");
                compteDAO = new CompteDAO(getActivity());
                compteDAO.deleteLasts();

                for (int i = 0; i < jsonCompteArray.length(); i++) {
                    JSONObject object = (JSONObject) jsonCompteArray.get(i);

                    compte = new Compte();
                    compte.setNom(object.getString("nomprenom"));
                    compte.setNumcompte(object.getString("codemembre"));
                    compte.setPrenom(object.getString("idpersonne"));
                    compte.setSoldedisponible((float) object.getDouble("solde"));
                    compteDAO.add(compte);
                }

                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("groupemembre", jsonCompteArray.length());
                editor.commit();

                if (jsonCompteArray.length() > 0) {
                    Intent intent = new Intent(getActivity(), MembreActivity.class);
                    intent.putExtra("result", result);
                    startActivityForResult(intent, CHOOSE_MEMBRE_REQUEST);
                }
            }
            */
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), R.string.serverno, Toast.LENGTH_SHORT).show();
        }

    }


    private void refreshData(final Compte compte) {
        JSONArray jsonArray = null;
        this.compte = compte;
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
        init();

        Log.e("DEBUG", compte.getNom() + " : " + String.valueOf(compte.getSolde()));
        Log.e("DEBUG", nomprenom.getText().toString());


        numero.post(new Runnable() {
            @Override
            public void run() {
                numero.setText(compte.getNumcompte());
            }
        });

        numcompt.post(new Runnable() {
            @Override
            public void run() {
                numcompt.setText(compte.getNumcompte());
            }
        });
        nomprenom.post(new Runnable() {
            @Override
            public void run() {
                nomprenom.setText(compte.getNom() + " " + compte.getPrenom());
            }
        });
        soldedisponible.post(new Runnable() {
            @Override
            public void run() {
                soldedisponible.setText(String.valueOf(compte.getSoldedisponible()));
            }
        });
        solde.post(new Runnable() {
            @Override
            public void run() {
                solde.setText(String.valueOf(compte.getSolde()));
            }
        });
        sexe.post(new Runnable() {
            @Override
            public void run() {
                sexe.setText(compte.getSexe());
            }
        });
        numproduit.post(new Runnable() {
            @Override
            public void run() {
                numproduit.setText(compte.getNumProduit());
            }
        });
        produit.post(new Runnable() {
            @Override
            public void run() {
                produit.setText(compte.getProduit());
            }
        });
        numpiece.post(new Runnable() {
            @Override
            public void run() {
                numpiece.setText("-");
            }
        });

        //mise.setText(compte.ge());
        editor.putString("ML", compte.getMiseLibre());
        editor.commit();
        if (compte.getNumcompte().contains("/G/")){
            groupe = true ;
            sexe.post(new Runnable() {
                @Override
                public void run() {
                    sexe.setText("PM");
                }
            });
            nomprenom.post(new Runnable() {
                @Override
                public void run() {
                    nomprenom.setText(compte.getNom());
                }
            });
            membres.post(new Runnable() {
                @Override
                public void run() {
                    membres.setEnabled(true);
                }
            }) ;
        }
        else {
            sexe.post(new Runnable() {
                @Override
                public void run() {
                    sexe.setText(compte.getSexe());
                }
            });
            nomprenom.post(new Runnable() {
                @Override
                public void run() {
                    nomprenom.setText(compte.getNom() + " " + compte.getPrenom());
                }
            });
            groupe = false ;
        }
    }

    public void refreshDataSMS(String result) {
        if (result != null) {
            Log.e("MSG", result);
            if (result.contains("ECHEC CONNEXION"))
                Toast.makeText(getActivity(), R.string.echecinfo, Toast.LENGTH_SHORT).show();

            String[] res = result.split(":");
            try {
                init();
                if (compte == null) compte = new Compte();
                if (res[0].contains("Aucun")) {
                    numpiece.setText(res[0]);
                }

                numcompt.setText(res[0]);
                compte.setNumcompte(res[0]);

                nomprenom.setText(res[1] + " " + res[2]);
                compte.setNom(res[1]);
                compte.setPrenom(res[2]);

                solde.setText(res[3]);
                soldedisponible.setText(res[4]);
                compte.setSolde(Float.parseFloat(res[3]));

                sexe.setText(res[5]);
                compte.setSexe(res[5]);

                numproduit.setText(res[7]);
                compte.setNumProduit(res[7]);

                produit.setText(res[8]);
                compte.setProduit(res[8]);

                numpiece.setText("-");
                Toast.makeText(getActivity(), R.string.inforecup, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), R.string.echecinfo, Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void init() {
        solde.setText("0");
        numcompt.setText("0");
        sexe.setText("");
        numproduit.setText("");
        produit.setText("");

        membres.setEnabled(false);
        montant.setText("");

        nomprenom.setText("");
        if (depot.isChecked()) depot.toggle();
        if (retrait.isChecked()) retrait.toggle();

    }


    public void loadWebImage(final String name, final ImageView imageTV, final ProgressBar progressBar) {

        final boolean[] b = {true};
        final Target target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageTV.setImageBitmap(bitmap);
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                        //Utiles.saveImageExternalStorage(bitmap, getActivity(), name, Utiles.BEEZY_ARTICLE_IMAGE_DIR) ;
                        //Toast.makeText(getApplicationContext(),"Sauvegarde reussie",Toast.LENGTH_LONG).show();
                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageTV.setImageResource(R.mipmap.ic_launcher);
                            Toast.makeText(getActivity(), R.string.echecloading, Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getActivity(),"Echec de chargement",Toast.LENGTH_LONG).show();
                b[0] = false;
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                //Toast.makeText(getApplicationContext(),"Sauvegarde en cours...",Toast.LENGTH_LONG).show();
            }
        };

        //Log.e("DEBUG", Url.getImageArticleBigUrl(name)) ;

        Toast.makeText(getActivity(), name, Toast.LENGTH_LONG).show();
        Picasso.with(getActivity()).load(name).into(target);

    }





    private void makeAlertDialog(String string, final boolean delete) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()) ;
        builder.setTitle(R.string.app_name) ;
        builder.setMessage(string) ;
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (delete){
                    operationDAO.clean() ;
                }

            }
        }) ;

        final AlertDialog alert = builder.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.my_accent));
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.my_accent));
            }
        });
        alert.show();
    }


}
