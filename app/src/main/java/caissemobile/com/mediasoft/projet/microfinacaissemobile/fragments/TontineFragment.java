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

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import caissemobile.com.mediasoft.projet.microfinacaissemobile.MainActivity;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.R;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.activities.CompteActivity;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.activities.ParametreActivity;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Agence;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Caissier;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Compte;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Membre;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Operation;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.AgenceDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CaissierDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CompteDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.DAOBase;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.MembreDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.OperationDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.PrintPDA;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.PrintPDAMobiPrint3;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.PrinterUtils;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.Url;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.Utiles;
import okhttp3.FormBody;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TontineFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TontineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TontineFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "TontineFragment";
    private static final int CHOOSE_COMPTE_REQUEST = 2;
    public static final String TNUMPIECE = "T0000";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ArrayList<Agence> agences;
    private AgenceDAO agenceDAO = null;
    private ArrayAdapter<String> agenceAdapter;


    private static final int REQUEST_CODE = 0;
    private static final int PROGRESS_DIALOG_ID = 1;
    private static final int MAX_SIZE = 1;
    private SharedPreferences preferences = null;

    RadioButton depot = null ;
    RadioButton retrait = null ;
    Spinner agence = null ;
    EditText numero = null ;
    EditText montant = null ;
    ImageButton rech = null ;
    TextView agencecaissier = null ;
    TextView numcompt = null ;
    TextView numpiece = null ;
    TextView nomprenom = null ;
    TextView solde = null ;
    TextView soldedisponible = null ;
    TextView mise = null ;
    TextView sexe = null ;
    TextView produit = null ;
    TextView numproduit = null ;
    ImageButton detailsButton = null ;
    ImageButton imageButton = null ;

    Button valider = null ;
    Button annuler = null ;
    public LoadCLientInfoTask loadCLientInfoTask = null;
    private SendOperationTask sendOperationTask = null ;
    private LoadCLientDetailsTask loadCLientDetailsTask = null;
    private String top = null;
    private String bluetoothConfig = "imprimenteexterne";
    private CaissierDAO caissierDAO;
    private MainActivity mParent;
    private String msg = null ;
    private MembreDAO membreDAO = null ;
    private CompteDAO compteDAO = null ;
    private Membre membre;
    private Compte compte;
    private ImageButton compteButton;
    private OperationDAO operationDAO;

    public TontineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TontineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TontineFragment newInstance(String param1, String param2) {
        TontineFragment fragment = new TontineFragment();
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
        View v = inflater.inflate(R.layout.fragment_tontine, container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity()) ;

        depot = (RadioButton) v.findViewById(R.id.depot);
        retrait = (RadioButton) v.findViewById(R.id.retrait);
        agence = (Spinner) v.findViewById(R.id.agence);
        numero = (EditText) v.findViewById(R.id.numero);
        montant = (EditText) v.findViewById(R.id.montant);
        agencecaissier = (TextView) v.findViewById(R.id.agencecaissier);
        mise = (TextView) v.findViewById(R.id.mise);
        rech = (ImageButton) v.findViewById(R.id.rech);
        numcompt = (TextView) v.findViewById(R.id.numcompte);
        numpiece = (TextView) v.findViewById(R.id.numpiece);
        nomprenom = (TextView) v.findViewById(R.id.nomprenom);
        solde = (TextView) v.findViewById(R.id.solde);
        soldedisponible = (TextView) v.findViewById(R.id.soldedisponible);
        sexe = (TextView) v.findViewById(R.id.sexe);
        numproduit = (TextView) v.findViewById(R.id.numproduit);
        produit = (TextView) v.findViewById(R.id.produit);
        valider = (Button) v.findViewById(R.id.valider);
        annuler = (Button) v.findViewById(R.id.annuler);
compteButton = (ImageButton) v.findViewById(R.id.compteBtn);
        detailsButton = (ImageButton) v.findViewById(R.id.details);
        imageButton = (ImageButton) v.findViewById(R.id.image);

        operationDAO = new OperationDAO(getActivity()) ;
        agenceDAO = new AgenceDAO(getActivity()) ;
        agences = agenceDAO.getAll() ;
        String[] data = new String[agences.size()] ;

        for (int i = 0; i<agences.size(); ++i){
            data[i] = agences.get(i).getNomagence();
        }

        caissierDAO = new CaissierDAO(getActivity()) ;


        compteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),CompteActivity.class) ;
                intent.putExtra("TYPE", Compte.TONTINE) ;
                startActivityForResult(intent, CHOOSE_COMPTE_REQUEST);
            }
        });

        if (caissierDAO.getLast()!=null &&  agenceDAO.getOne(caissierDAO.getLast().getAgence_id()) != null) agencecaissier.setText(agenceDAO.getOne(caissierDAO.getLast().getAgence_id()).getNomagence() + "   -   " + preferences.getString("dateouvert", "")) ;
        agenceAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, data) ;
        agenceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        agence.setAdapter(agenceAdapter);
        membreDAO = new MembreDAO(getActivity()) ;
        compteDAO = new CompteDAO(getActivity()) ;

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

        rech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                if (preferences.getBoolean("membre",false)){

                    compte = compteDAO.getOne(numero.getText().toString()) ;
                    if (compte!=null){
                        refreshData(compte);
                        Toast.makeText(getActivity(), R.string.inforecup,Toast.LENGTH_SHORT).show();
                    } else  Toast.makeText(getActivity(), R.string.echecinfo,Toast.LENGTH_SHORT).show();
                }
                else{
                    // SI le mode de connexion est Internet
                    if (!preferences.getBoolean(ParametreActivity.TYPE_COMMUNICATION, false)) {
                        if (!Utiles.isConnected(getActivity())) {
                            Toast.makeText(getActivity(), R.string.anyconnexion,Toast.LENGTH_LONG).show();
                            return;
                        }

                        loadCLientInfoTask = new LoadCLientInfoTask(numero.getText().toString(), agence.getSelectedItemPosition());

                        if (numero.getText().length()>0) loadCLientInfoTask.execute(Url.getLoadCLientInfoUrl(getActivity()));
                        else Toast.makeText(getActivity(), R.string.numcptincorect,Toast.LENGTH_LONG).show();

                    }
                    // Sinon si le mode de connexion est Sms
                    else {
                        String msg = numero.getText().toString() + ":T:" +agences.get(agence.getSelectedItemPosition()).getNumagence() ;
                        mParent.sendSMS(msg,0);
                    }
                }

            }
        });


        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!isCorrect()) return;

                if (depot.isChecked()) top = "VSE";
                if (retrait.isChecked()) top = "RE";


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()) ;
                builder.setTitle(R.string.app_name) ;

                if (depot.isChecked()) builder.setTitle(getString(R.string.depott))  ;
                if (retrait.isChecked()) builder.setTitle(getString(R.string.retrai))  ;

                builder.setMessage(getString(R.string.continuer)) ;
                builder.setPositiveButton(R.string.valider, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Operation operation = new Operation() ;

                        operation.setNom(compte.getNom());
                        operation.setPrenom(compte.getPrenom());
                        operation.setUser_id(caissierDAO.getLast().getId());
                        operation.setNumcompte(compte.getNumcompte());
                        operation.setMontant(Float.parseFloat(montant.getText().toString()));
                        operation.setAgence(agences.get(agence.getSelectedItemPosition()).getNumagence());
                        operation.setMise(Float.parseFloat(mise.getText().toString().replace(",", ".")));
                        operation.setNumpiece("-");

                        if (!preferences.getBoolean("membre",false)) {
                            // SI le mode de connexion est Internet
                            if (!preferences.getBoolean(ParametreActivity.TYPE_COMMUNICATION, false)) {
                                if (!Utiles.isConnected(getActivity())) {
                                    Toast.makeText(getActivity(), R.string.anyconnexion, Toast.LENGTH_LONG).show();
                                    return;
                                }

                                if (depot.isChecked()) top = "VSE";
                                if (retrait.isChecked()) top = "RE";
                                if (!isCorrect()) return;
                                sendOperationTask = new SendOperationTask(top,mise.getText().toString(), numproduit.getText().toString(), numcompt.getText().toString(), Float.parseFloat(montant.getText().toString().replace(",", ".")), agence.getSelectedItemPosition());
                                //sendOperationTask.execute(Url.getSendOperationUrl(getActivity())) ;

                                if (top.equals("VSE") && preferences.getString("ML", "n").toLowerCase().equals("o"))
                                    if (!isMteCorect()) {
                                        Toast.makeText(getActivity(), R.string.mtnmultiple, Toast.LENGTH_LONG).show();
                                        return;
                                    }

                                if (operation.getMontant()>0){
                                    if (top.equals("RE") && Float.parseFloat(montant.getText().toString().replace(",", ".")) < Float.parseFloat(soldedisponible.getText().toString().replace(",",".")))
                                        sendOperationTask.execute(Url.getSendOperationUrl(getActivity()));
                                    else if (top.equals("VSE"))
                                        sendOperationTask.execute(Url.getSendOperationUrl(getActivity()));
                                    else
                                        Toast.makeText(getActivity(), getString(R.string.mteincorrect), Toast.LENGTH_LONG).show();
                                }
                                 }
                            // Sinon si le mode de connexion est Sms
                            else {
                                if (!isCorrect()) return;
                                int pos = agence.getSelectedItemPosition();
                                Caissier c = caissierDAO.getLast();

                                String msg = top + ":" + numproduit.getText().toString() + ":" + numcompt.getText().toString() + ":" + montant.getText().toString() + ":" + agences.get(pos).getNumagence() + ":" + "T" + ":" + preferences.getString("dateouvert", "") + ":" + montant.getText().toString() + ":" + c.getCodeguichet() + ":" + c.getAgence_id() + ":" +  c.getDdb() + ":" +  c.getDl() + ":" +  c.getDp() + ":" + c.getDi();

                                if (operation.getMontant()>0){
                                    operationDAO.add(operation) ;

                                    if (top.equals("RE") && Float.parseFloat(montant.getText().toString().replace(",", ".")) < Float.parseFloat(soldedisponible.getText().toString().replace(",", ".")))
                                        mParent.sendSMS(msg, 1);
                                    else if (top.equals("VSE")) mParent.sendSMS(msg, 1);
                                    else
                                        Toast.makeText(getActivity(), getString(R.string.mteincorrect), Toast.LENGTH_LONG).show();
                                } 
                            }
                        }
                        else {
                            if (!isCorrect()) return ;
                            //saveOperation();
                        }

                    }
                });
                builder.setNegativeButton(R.string.annuler, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }) ;

                if (montant.getText().toString().trim().length()>0 && Float.valueOf(montant.getText().toString())>0){

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
                else Toast.makeText(getActivity(), R.string.mtnincorrect, Toast.LENGTH_SHORT).show();


            }
        });


        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // SI le mode de connexion est Internet
                if (!preferences.getBoolean(ParametreActivity.TYPE_COMMUNICATION,false)){
                    if (!Utiles.isConnected(getActivity())) {
                        Toast.makeText(getActivity(), R.string.anyconnexion,Toast.LENGTH_LONG).show();
                        return;
                    }

                    loadCLientDetailsTask = new LoadCLientDetailsTask(numero.getText().toString(),agence.getSelectedItemPosition());
                    if (numero.getText().length()>0)loadCLientDetailsTask.execute(Url.getLoadCLientDetailUrl(getActivity()));
                    else Toast.makeText(getActivity(),R.string.numcptincorect,Toast.LENGTH_LONG).show();
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
                if (!preferences.getBoolean(ParametreActivity.TYPE_COMMUNICATION,false)){

                    if (numero.getText().length()>0) ;
                    else {
                        Toast.makeText(getActivity(),R.string.numcptincorect,Toast.LENGTH_LONG).show();
                        return ;
                    }

                    LoadCLientPhotoTask loadCLientPhotoTask = new LoadCLientPhotoTask(numero.getText().toString(),agence.getSelectedItemPosition()) ;
                    loadCLientPhotoTask.execute(Url.getLoadCLientPhotoUrl(getActivity())) ;
                }
                // Sinon si le mode de connexion est Sms
                else {

                }
            }
        });







        return  v ;
    }





    private boolean isCorrect() {
        if (numpiece.getText().length()>3){
            Toast.makeText(getActivity(), R.string.infoactu, Toast.LENGTH_SHORT).show();
            return false ;
        }

        if (!depot.isChecked() && !retrait.isChecked()) {
            Toast.makeText(getActivity(), getString(R.string.versmterror), Toast.LENGTH_SHORT).show();
            return  false ;
        }
        if (montant.getText().length()==0) {
            Toast.makeText(getActivity(), getString(R.string.montanterror), Toast.LENGTH_SHORT).show();
            return  false ;
        }


        if(Float.parseFloat(soldedisponible.getText().toString().replace(",", "."))<Float.parseFloat(montant.getText().toString().replace(",", ".")) && retrait.isChecked()) {
            Toast.makeText(getActivity(), getString(R.string.soldedispoerror), Toast.LENGTH_SHORT).show();
            return false;
        }



        if(Float.parseFloat(soldedisponible.getText().toString().replace(",", "."))==0 && retrait.isChecked()) {
            Toast.makeText(getActivity(), getString(R.string.solderror), Toast.LENGTH_SHORT).show();
            return false;
        }



        if (numero.getText().toString().equals("")){
            Toast.makeText(getActivity(), getString(R.string.carneterror), Toast.LENGTH_SHORT).show();
            return false;
        }

        float mtn = Float.parseFloat(montant.getText().toString()) ;
        float mis = Float.parseFloat(mise.getText().toString().replace(",", "."));
        if (compte!=null) mis = Float.parseFloat(compte.getMise().replace(",", "."));

        if (mtn%mis!=0 && depot.isChecked()){
            Toast.makeText(getActivity(), getString(R.string.miseerror), Toast.LENGTH_SHORT).show();
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

        if (preferences.getBoolean("membre",false)){

            int pos = 0 ;
            for (int i = 0; i<agences.size(); ++i){
                if (agences.get(i).getNumagence().equals(caissierDAO.getLast().getAgence_id())) pos = i ;
            }

            agence.setSelection(pos);
            agence.setEnabled(false);
        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK ) {
            //((MainActivity)getActivity()).selectTontineFragment(); ;
            if (requestCode == CHOOSE_COMPTE_REQUEST) {
                compte = compteDAO.getOne(data.getLongExtra(MainActivity.COMPTE, 0));
                if (compte!=null) {
                    refreshData(compte);
                }
            }
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mParent = (MainActivity) getActivity() ;
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


        Intent intent = getActivity().getIntent() ;
        if (intent!=null){
            msg = intent.getStringExtra("MSG") ;
            if (msg!= null && msg.endsWith("T")) {
                Log.e("MSG",msg) ;
                refreshDataSMS(msg);
            }
            msg = intent.getStringExtra("MTN") ;
            if (msg!= null && msg.endsWith("T")) {
                Log.e("MTN", msg) ;
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public class SendOperationTask extends AsyncTask<String,Void,String> {

        String numcompte = null ;
        String interfac = "E" ;
        String typop = null ;
        String agence = null ;
        String mise = null ;
        String numprod = null ;
        float montant = 0 ;
        int pos = 0 ;

        OperationDAO operationDAO = null ;
        Caissier c  =null;

        public SendOperationTask(String typop,String mise, String numproduit, String numCompte,float montant,int p) {
            this.numcompte = numCompte ;
            this.typop = typop ;
            this.montant = montant ;
            this.numprod = numproduit ;
            this.mise = numproduit ;
            this.pos = p ;
        }

        @Override
        protected String doInBackground(String... urls) {
            //TelephonyManager telephonyManager =  ( TelephonyManager ) getSystemService (Context.TELEPHONY_SERVICE);
            //String imei = telephonyManager.getDeviceId ();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity()) ;

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

            Date date = new Date() ;
            FormBody.Builder formBuilder = new FormBody.Builder();
            formBuilder.add("iMei", iMei) ;
            formBuilder.add("macAddress", macAddress);
            formBuilder.add("NumCompte",numcompte);
            formBuilder.add("interface","T");
            formBuilder.add("NumProduit",numprod);
            formBuilder.add("journee", preferences.getString("dateouvert",""));
            formBuilder.add("montant1",String.valueOf(montant));
            formBuilder.add("montant2",String.valueOf(montant));
            formBuilder.add("TypeOperation",typop);
            formBuilder.add("mise",String.valueOf(mise));

            formBuilder.add("AgenceClient",String.valueOf(agences.get(pos).getNumagence()));
            formBuilder.add("guichet",String.valueOf(c.getCodeguichet()));
            formBuilder.add("numagence",String.valueOf(c.getAgence_id()));

            formBuilder.add("ddb",c.getDdb());
            formBuilder.add("dl",c.getDl());
            formBuilder.add("dp",c.getDp());
            formBuilder.add("di",c.getDi());



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

            Log.e("DEBUG", result) ;
            processOperationResult(result) ;

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
            if (operationDAO==null) operationDAO = new OperationDAO(getActivity()) ;

            c = new CaissierDAO(getActivity()).getLast() ;
        }
    }

    public void processOperationResultSMS(String result) {

        Log.e("ETAPE","SMS") ;
        Caissier c = new CaissierDAO(getActivity()).getLast() ;
        OperationDAO operationDAO = new OperationDAO(getActivity()) ;

        if (result.equals("NUMPIECE")) numpiece.setText(R.string.echec1);
        if (result.contains("AGENCEDIFF")) numpiece.setText(R.string.echec);
        if (result.contains("NUMPECEECHEC1")) numpiece.setText(R.string.echec2p);
        //if (result.contains("NUMPECEECHEC1")) numpiece.setText(R.string.echec2);
        if (result.contains("NUMPECEECHEC2")) numpiece.setText(R.string.echec2p);
        //if (result.contains("NUMPECEECHEC2")) numpiece.setText(R.string.echec3);
        if (result.contains("NUMPECEECHEC3")) numpiece.setText(R.string.echec2p);
        //if (result.contains("NUMPECEECHEC3")) numpiece.setText(R.string.echec4);

        if (result.contains("OK:") && result.split(":").length>=3) {
            Toast.makeText(getActivity(), R.string.opsuccess, Toast.LENGTH_LONG).show();
            String lib = "" ;
            Operation op = operationDAO.getLast() ;
            op.setNumpiece(result.split(":")[1]);
            numpiece.setText(result.split(":")[1]);


            if (top.equals("RE")){
                lib += "RETRAIT, No : "+ op.getNumcompte() + " , Mte : " + String.valueOf(op.getMontant()) ;
                op.setTypeoperation(1);
            }
            else {
                lib += "DEPOT, No : "+ op.getNumcompte() + " , Mte : " + String.valueOf(op.getMontant()) ;
                op.setTypeoperation(0);
            }
            try {
                op.setDateoperation(DAOBase.formatter.parse(preferences.getString("dateouvert", "")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            op.setLibelle(lib);
            op.setUser_id(c.getId());

            if (operationDAO.update(op)>0){

                ArrayList<Operation> operations = operationDAO.getAll() ;
                float sold = c.getSolde() ;
                for (int j = 0; j<operations.size();++j){
                    if (operations.get(j).getTypeoperation() == 0 && operations.get(j).getTypeoperation()==3) sold += operations.get(j).getMontant();
                    else sold -= operations.get(j).getMontant();
                }
                getActivity().setTitle("Solde : " + Utiles.formatMtn(sold) + " F");

                float f = Float.parseFloat(soldedisponible.getText().toString().replace(",", ".")) ;
                if (top.equals("RE")){
                    f -= op.getMontant() ;
                }
                else {
                    f += op.getMontant() ;
                }

                String l = String.format("%.2f",f) ;
                soldedisponible.setText(l);
                boolean imp = preferences.getBoolean(bluetoothConfig, false) ;
                if (imp){
                    try {
                        PrinterUtils printerUtils = new PrinterUtils(getActivity()) ;
                        printerUtils.printTicket(operationDAO.getLast(),"OPERATION D'EPARGNE");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        //PrintPDA printPDA = new PrintPDA(getActivity()) ;
                        //printPDA.printTicket(operationDAO.getLast(),"OPERATION D'EPARGNE");

                        PrintPDAMobiPrint3 printPDAMobiPrint3 = new PrintPDAMobiPrint3(getActivity());
                        printPDAMobiPrint3.printTicket(operationDAO.getLast(),"OPERATION D'EPARGNE");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            };
            //init();
            montant.setText("0");
        }
    }


    public void processOperationResult(String result) {

        Caissier c = new CaissierDAO(getActivity()).getLast() ;
        OperationDAO operationDAO = new OperationDAO(getActivity()) ;

        if (result.contains("NUMPIECE")) numpiece.setText(R.string.echec1);
        if (result.contains("AGENCEDIFF")) numpiece.setText(R.string.echec);
        if (result.contains("NUMPECEECHEC1")) numpiece.setText(R.string.echec2p);
        //if (result.contains("NUMPECEECHEC1")) numpiece.setText(R.string.echec2);
        if (result.contains("NUMPECEECHEC2")) numpiece.setText(R.string.echec2p);
        //if (result.contains("NUMPECEECHEC2")) numpiece.setText(R.string.echec3);
        if (result.contains("NUMPECEECHEC3")) numpiece.setText(R.string.echec2p);
        //if (result.contains("NUMPECEECHEC3")) numpiece.setText(R.string.echec4);

        if (result.contains("OK:") && result.split(":").length>=3) {
            Toast.makeText(getActivity(), R.string.opsuccess, Toast.LENGTH_LONG).show();
            String lib = "" ;
            Operation op = new Operation() ;
            op.setAgence(c.getAgence_id());
            op.setMontant(Float.parseFloat(montant.getText().toString()));
            op.setMise(Float.parseFloat(montant.getText().toString().replace(",",".")));
            op.setNom(nomprenom.getText().toString().split(" ")[0]);
            op.setPrenom(nomprenom.getText().toString().split(" ")[1]);
            op.setNumcompte(numcompt.getText().toString());
            op.setNummenbre(numero.getText().toString());
            op.setNumpiece(result.split(":")[1]);
            numpiece.setText(result.split(":")[1]);

            if (top.equals("RE")){
                lib += "RETRAIT, No : "+ op.getNumcompte() + " , Mte : " + String.valueOf(op.getMontant()) ;
                op.setTypeoperation(1);
            }
            else {
                lib += "DEPOT, No : "+ op.getNumcompte() + " , Mte : " + String.valueOf(op.getMontant()) ;
                op.setTypeoperation(0);
            }
            op.setLibelle(lib);
            op.setUser_id(c.getId());
            try {
                op.setDateoperation(DAOBase.formatter.parse(preferences.getString("dateouvert", "")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (operationDAO.add(op)>0){

                ArrayList<Operation> operations = operationDAO.getAll() ;
                float sold = c.getSolde() ;
                for (int j = 0; j<operations.size();++j){
                    if (operations.get(j).getTypeoperation() == 0 && operations.get(j).getTypeoperation()==3) sold += operations.get(j).getMontant();
                    else sold -= operations.get(j).getMontant();
                }
                getActivity().setTitle(getString(R.string.soldetext) + Utiles.formatMtn(sold) + " F");



                float f = Float.parseFloat(soldedisponible.getText().toString().replace(",", ".")) ;
                if (top.equals("RE")){
                    f -= op.getMontant() ;
                }
                else {
                    f += op.getMontant() ;
                }

                String l = String.format("%.2f",f) ;
                soldedisponible.setText(l);

                boolean imp = preferences.getBoolean(bluetoothConfig, false) ;
                if (imp){
                    try {
                        PrinterUtils printerUtils = new PrinterUtils(getActivity()) ;
                        printerUtils.printTicket(operationDAO.getLast(),"OPERATION DE TONTINE");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        //PrintPDA printPDA = new PrintPDA(getActivity()) ;
                        //printPDA.printTicket(operationDAO.getLast(),"OPERATION DE TONTINE");

                        PrintPDAMobiPrint3 printPDAMobiPrint3 = new PrintPDAMobiPrint3(getActivity());
                        printPDAMobiPrint3.printTicket(operationDAO.getLast(),"OPERATION DE TONTINE");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            };
            //init();
            montant.setText("0");
        }
    }





    public class LoadCLientDetailsTask extends AsyncTask<String,Void,String> {

        String numcompte = null ;
        int pos = 0 ;

        public LoadCLientDetailsTask(String numcompte,int pos) {
            this.numcompte = numcompte ;
            this.pos = pos;
        }

        @Override
        protected String doInBackground(String... urls) {
            //TelephonyManager telephonyManager =  ( TelephonyManager ) getSystemService (Context.TELEPHONY_SERVICE);
            //String imei = telephonyManager.getDeviceId ();


            FormBody.Builder formBuilder = new FormBody.Builder();
            formBuilder.add("numcompte", numcompte);
            formBuilder.add("agence", String.valueOf(agences.get(pos).getNumagence()));
            formBuilder.add("token", "T");



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
            if (result.equals("ECHEC CONNEXION")) Toast.makeText(getActivity(),R.string.echec6,Toast.LENGTH_LONG).show();
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()) ;
                RelativeLayout rl = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.infolistlayout, null);
                LinearLayout infoitemlayout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.infoitemlayout, null);

                Button fermer = (Button) rl.findViewById(R.id.annuler);

                LinearLayout racine = (LinearLayout) rl.findViewById(R.id.racine);
                racine.removeAllViews();

                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT) ;
                param.setMargins(5,5,5,5);

                builder.setView(rl) ;

                final AlertDialog alert = builder.create();
                alert.setOnShowListener( new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.my_accent));
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.my_accent));
                    }
                }) ;
                alert.show();

                fermer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.dismiss();
                    }
                });


                try {
                    JSONObject jsonObject = new JSONObject(result) ;
                    JSONArray infos = jsonObject.getJSONArray("infos") ;
                    JSONObject info = null ;

                    TextView piece = null ;
                    TextView cpt = null ;
                    TextView savt = null ;
                    TextView sapres = null ;
                    TextView libelle = null ;

                    for (int i = 0 ; i < infos.length() ; ++i){
                        info = infos.getJSONObject(i) ;
                        infoitemlayout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.infoitemlayout, null);

                        piece = (TextView) infoitemlayout.findViewById(R.id.numpiece);
                        cpt = (TextView) infoitemlayout.findViewById(R.id.numcpt);
                        savt = (TextView) infoitemlayout.findViewById(R.id.savt);
                        sapres = (TextView) infoitemlayout.findViewById(R.id.sapres);
                        libelle = (TextView) infoitemlayout.findViewById(R.id.libelle);


                        Date d = DAOBase.formatter.parse(info.getString("DATEVALEUR")) ;
                        piece.setText(info.getString("NUMPIECE"));
                        cpt.setText(DAOBase.formatterj.format(d));
                        savt.setText(info.getString("CREDIT") + " F");
                        sapres.setText(info.getString("DEBIT") + " F");
                        libelle.setText(info.getString("LIBELLE"));

                        racine.addView(infoitemlayout,param);
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











    public class LoadCLientPhotoTask extends AsyncTask<String,Void,String> {

        String numcompte = null ;
        int pos = 0 ;

        public LoadCLientPhotoTask(String numcompte,int pos) {
            this.numcompte = numcompte ;
            this.pos = pos;
        }

        @Override
        protected String doInBackground(String... urls) {
            //TelephonyManager telephonyManager =  ( TelephonyManager ) getSystemService (Context.TELEPHONY_SERVICE);
            //String imei = telephonyManager.getDeviceId ();


            FormBody.Builder formBuilder = new FormBody.Builder();
            formBuilder.add("numcompte", numcompte);
            formBuilder.add("agence", String.valueOf(agences.get(pos).getNumagence()));
            formBuilder.add("token", "T");



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
            if (result.equals("ECHEC CONNEXION")) Toast.makeText(getActivity(),"Echec de connexion à la base de donnée",Toast.LENGTH_LONG).show();
            else{
                showPhoto(result) ;
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

    private void showPhoto(String result) {

        RelativeLayout rl = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.image_layout, null);

        if (numero.getText().length()>0) ;
        else {
            Toast.makeText(getActivity(),R.string.numcptincorect,Toast.LENGTH_LONG).show();
            return ;
        }
        ImageView imageView = (ImageView) rl.findViewById(R.id.imageview);
        ProgressBar progressBar = (ProgressBar) rl.findViewById(R.id.progressBar);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()) ;

        Button fermer = (Button) rl.findViewById(R.id.annuler);

        builder.setView(rl) ;
        final AlertDialog alert = builder.create();
        alert.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.my_accent));
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.my_accent));
            }
        }) ;
        alert.show();
        fermer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });


        try {
            JSONArray jsonArray = new JSONArray(result);
            String photo = jsonArray.getJSONObject(0).getString("photo").replace("//", "/") ;
            photo = photo.replace("///", "//") ;
            if (photo.length()>1 && getActivity()!=null)  Picasso.with(getActivity()).load(photo).into(imageView);
            else Toast.makeText(getActivity(), R.string.echec,Toast.LENGTH_SHORT).show();
            Log.e("PHOTO",photo) ;
        }
        catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), R.string.echec,Toast.LENGTH_SHORT).show();
        }

    }




    public class LoadCLientInfoTask extends AsyncTask<String,Void,String> {

        String numcompte = null ;
        int pos = 0 ;
        public LoadCLientInfoTask(String numcompte, int p) {
            this.numcompte = numcompte ;
            pos = p ;
        }

        @Override
        protected String doInBackground(String... urls) {
            //TelephonyManager telephonyManager =  ( TelephonyManager ) getSystemService (Context.TELEPHONY_SERVICE);
            //String imei = telephonyManager.getDeviceId ();


            FormBody.Builder formBuilder = new FormBody.Builder();
            formBuilder.add("numcompte", numcompte);
            formBuilder.add("agence", String.valueOf(agences.get(pos).getNumagence()));
            formBuilder.add("token", "T");



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
            if (result.contains("CONNEXION")) Toast.makeText(getActivity(),R.string.connexionechec,Toast.LENGTH_LONG).show();
            else if (result.contains("ECHEC")) Toast.makeText(getActivity(), R.string.nocompt,Toast.LENGTH_LONG).show();
            else refreshData(result);
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

    public void refreshDataSMS(String result) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mParent).edit() ;

        if (result!=null){
            Log.e("MSG",result) ;
            if (result.contains("ECHEC CONNEXION"))   Toast.makeText(getActivity(), R.string.echecinfo,Toast.LENGTH_SHORT).show();

            String[] res = result.split(":") ;
            try {

                init();
                if (compte==null) compte=new Compte() ;
                if (res[0].contains("Aucun")){
                    numpiece.setText(res[0]);
                }

                numcompt.setText(res[0]);
                compte.setNumcompte(res[0]);

                nomprenom.setText(res[1] + " " + res[2]);
                compte.setNom(res[1]);
                compte.setPrenom(res[2]);

                solde.setText(res[3]);
                compte.setSolde(Float.parseFloat(res[3]));

                soldedisponible.setText(res[4]);
                compte.setSoldedisponible(Float.parseFloat(res[4]));

                sexe.setText(res[5]);
                compte.setSexe(res[5]);

                mise.setText(res[6]);
                compte.setMise(res[6]) ;

                numproduit.setText(res[7]);
                compte.setNumProduit(res[7]);

                produit.setText(res[8]);
                compte.setProduit(res[8]);

                editor.putString("ML", res[9]);
                editor.commit() ;
                numpiece.setText("-");
                Toast.makeText(getActivity(), R.string.inforecup,Toast.LENGTH_SHORT).show();
            }
            catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), R.string.echecinfo,Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void refreshData(String result) {
        JSONArray jsonArray = null;
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit() ;

        try {
            jsonArray = new JSONArray(result);
            if (compte==null) compte = new Compte() ;
            init();
            numcompt.setText(jsonArray.getString(0));
            compte.setNumcompte(jsonArray.getString(0));

            nomprenom.setText(jsonArray.getString(1) + " " + jsonArray.getString(2));
            compte.setNom(jsonArray.getString(1));
            compte.setPrenom(jsonArray.getString(2));

            solde.setText(jsonArray.getString(3));
            compte.setSolde((float) jsonArray.getDouble(3));

            soldedisponible.setText(jsonArray.getString(4));
            compte.setSoldedisponible((float) jsonArray.getDouble(4));

            sexe.setText(jsonArray.getString(5));
            compte.setSexe(jsonArray.getString(5));

            mise.setText(jsonArray.getString(7));
            compte.setMise(jsonArray.getString(7)) ;

            numproduit.setText(jsonArray.getString(8));
            compte.setNumProduit(jsonArray.getString(8));

            produit.setText(jsonArray.getString(9));
            compte.setProduit(jsonArray.getString(9));

            editor.putString("ML", jsonArray.getString(10));
            editor.commit() ;
            numpiece.setText("-");
            Toast.makeText(getActivity(), R.string.inforecup,Toast.LENGTH_SHORT).show();
        }
        catch (JSONException e) {
            e.printStackTrace();
         Toast.makeText(getActivity(), R.string.serverno,Toast.LENGTH_SHORT).show();
        }

    }


    private void refreshData(final Compte compte) {
        JSONArray jsonArray = null;
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit() ;
            init();

        this.compte = compte ;

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
        }) ;
        nomprenom.post(new Runnable() {
            @Override
            public void run() {
                nomprenom.setText(compte.getNom() + " " + compte.getPrenom());
            }
        }) ;
        solde.post(new Runnable() {
            @Override
            public void run() {
                solde.setText(String.valueOf(compte.getSolde()));
            }
        }) ;
        soldedisponible.post(new Runnable() {
            @Override
            public void run() {
                soldedisponible.setText(String.valueOf(compte.getSoldedisponible()));
            }
        }) ;
        sexe.post(new Runnable() {
            @Override
            public void run() {
                sexe.setText(compte.getSexe());
            }
        }) ;

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
        mise.post(new Runnable() {
            @Override
            public void run() {
                mise.setText(compte.getMise());
            }
        });
        editor.putString("ML", compte.getMiseLibre());
        editor.commit();
        numpiece.setText("-");

    }

    private void init() {
        nomprenom.setText("");
        solde.setText("0");
        soldedisponible.setText("0");
        numcompt.setText("0");
        sexe.setText("");
        numproduit.setText("");
        produit.setText("");
        SharedPreferences.Editor editor = preferences.edit() ;
        editor.putString("ML","N") ;
        editor.commit();
        mise.setText("0");

        montant.setText("0");

        if (depot.isChecked())depot.toggle();
        if (retrait.isChecked())retrait.toggle();
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
                        Toast.makeText(getActivity(),"Sauvegarde reussie",Toast.LENGTH_LONG).show();
                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                try{
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageTV.setImageResource(R.mipmap.ic_launcher);
                            Toast.makeText(getActivity(), R.string.echecloading,Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
                Toast.makeText(getActivity(),"Echec de chargement",Toast.LENGTH_LONG).show();
                b[0] = false ;
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Toast.makeText(getActivity(),"Sauvegarde en cours...",Toast.LENGTH_LONG).show();
            }
        };

        //Log.e("DEBUG", Url.getImageArticleBigUrl(name)) ;
        Toast.makeText(getActivity(),name,Toast.LENGTH_LONG).show();

        Picasso.with(getActivity()).load(name).into(target);

    }


    public boolean isMteCorect(){
        float mte = Float.parseFloat(montant.getText().toString().replace(",", ".")) ;
        float mse = Float.parseFloat(mise.getText().toString().replace(",",".")) ;

        if (mte%mse!=0) return false ;
        return true ;
    }




    public void saveOperation() {

        if (depot.isChecked()) top = "VSE" ;
        if (retrait.isChecked()) top = "RE" ;

        Caissier c = new CaissierDAO(getActivity()).getLast();
        OperationDAO operationDAO = new OperationDAO(getActivity()) ;
        compteDAO = new CompteDAO(getActivity()) ;
        compte = compteDAO.getOne(numero.getText().toString());
        Toast.makeText(getActivity(), R.string.opsuccess, Toast.LENGTH_LONG).show();
        String lib = "" ;
        Operation op = new Operation() ;
        op.setAgence(c.getAgence_id());
        op.setMontant(Float.parseFloat(montant.getText().toString()));
        op.setNom(compte.getNom());
        op.setPrenom(compte.getPrenom());
        op.setNumcompte(compte.getNumcompte());
        op.setNummenbre(compte.getNummembre());
        op.setNumpiece(TNUMPIECE);
        numpiece.setText(op.getNumpiece());
        if (top.equals("RE")){
            lib += "RETRAIT, No : "+ op.getNumcompte() + " , Mte : " + String.valueOf(op.getMontant()) ;
            op.setTypeoperation(1);
            compte.setSolde(compte.getSolde() - op.getMontant());
        }
        else {
            lib += "DEPOT, No : "+ op.getNumcompte() + " , Mte : " + String.valueOf(op.getMontant());
            op.setTypeoperation(0);
            compte.setSolde(compte.getSolde()+op.getMontant());
        }
        op.setLibelle(lib);
        op.setUser_id(c.getId());

        if (operationDAO.add(op)>0){

            compteDAO.update(compte) ;
            ArrayList<Operation> operations = operationDAO.getAll() ;
            float sold = c.getSolde() ;
            for (int j = 0; j<operations.size();++j){
                if (operations.get(j).getTypeoperation() == 0 && operations.get(j).getTypeoperation()==3) sold += operations.get(j).getMontant();
                else sold -= operations.get(j).getMontant();
            }
            getActivity().setTitle(getString(R.string.soldetext) + Utiles.formatMtn(sold) + " F");


            float f = Float.parseFloat(soldedisponible.getText().toString().replace(",",".")) ;
            if (top.equals("RE")){
                f -= op.getMontant() ;
            }
            else {
                f += op.getMontant() ;
            }

            soldedisponible.setText(Utiles.formatMtn(f));

            boolean imp = preferences.getBoolean(bluetoothConfig, false) ;
            if (imp){
                try {
                    PrinterUtils printerUtils = new PrinterUtils(getActivity()) ;
                    printerUtils.printTicket(operationDAO.getLast(),"OPERATION DE TONTINE");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                try {
                    //PrintPDA printPDA = new PrintPDA(getActivity()) ;
                    //printPDA.printTicket(operationDAO.getLast(),"OPERATION DE TONTINE");

                    PrintPDAMobiPrint3 printPDAMobiPrint3 = new PrintPDAMobiPrint3(getActivity());
                    printPDAMobiPrint3.printTicket(operationDAO.getLast(),"OPERATION DE TONTINE");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };
        //init();
        montant.setText("0");
    }

}
