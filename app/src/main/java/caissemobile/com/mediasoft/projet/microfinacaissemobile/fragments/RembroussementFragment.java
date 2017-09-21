package caissemobile.com.mediasoft.projet.microfinacaissemobile.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import caissemobile.com.mediasoft.projet.microfinacaissemobile.MainActivity;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.R;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.activities.CompteActivity;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.activities.ConnexionActivity;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.activities.CreditActivity;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.activities.ParametreActivity;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.activities.ScannerActivity;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Caissier;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Compte;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Credit;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Operation;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CaissierDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CompteDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CreditDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.Crud;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.DAOBase;
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
 * {@link RembroussementFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RembroussementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RembroussementFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "RembroussementFragment";
    public static final String CREDIT = "credit";
    private static final int CHOOSE_CREDIT_REQUEST = 6;
    private static final String RNUMPIECE = "R0000";
    private static final int CHOOSE_COMPTE_REQUEST = 1;
    public static final String NOCOMPTE = "nocompte";
    private static final int CHOOSE_CODE_BAR_REQUEST = 3;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    EditText rembroussement = null ;
    EditText nocredit = null ;
    EditText numcompte = null ;

    TextView membre = null ;
    TextView mtnpret = null ;
    TextView datedeblocage = null ;
    TextView produit = null ;
    TextView capitalRestant = null ;
    TextView resteApayer = null ;
    TextView interetAttendu = null ;
    TextView penalite = null ;
    TextView capitalAttendu = null ;

    ImageButton rech = null ;
    ImageButton compteBtn = null ;
    Button valider = null ;
    Button annuler = null ;
    private Credit credit;
    private CreditDAO creditDAO;
    private SharedPreferences preferences;
    private CaissierDAO caissierDAO = null ;
    private String bluetoothConfig = "imprimenteexterne";
    private SendRembroussementTask sendRembroussementTask;
    private TextView numpiece;
    private Compte compte = null;
    private CompteDAO compteDAO = null ;
    private OperationDAO operationDAO;
    private ImageButton camBtn;

    public RembroussementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RembroussementFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RembroussementFragment newInstance(String param1, String param2) {
        RembroussementFragment fragment = new RembroussementFragment();
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
        View v = inflater.inflate(R.layout.fragment_rembroussement, container, false);


        operationDAO = new OperationDAO(getActivity()) ;
         rembroussement = (EditText) v.findViewById(R.id.rembroussement);
         nocredit = (EditText) v.findViewById(R.id.numero) ;
         numcompte = (EditText) v.findViewById(R.id.numcpt) ;

         membre = (TextView) v.findViewById(R.id.membre);
         mtnpret = (TextView) v.findViewById(R.id.mtn) ;
         datedeblocage = (TextView) v.findViewById(R.id.datedeblocage) ;
         produit = (TextView) v.findViewById(R.id.produit) ;
         numpiece = (TextView) v.findViewById(R.id.numpiece) ;

         capitalRestant = (TextView) v.findViewById(R.id.capitalrestant) ;
         resteApayer = (TextView) v.findViewById(R.id.resteapayer) ;
         capitalAttendu = (TextView) v.findViewById(R.id.capital_attendu) ;
         interetAttendu = (TextView) v.findViewById(R.id.interet_attendu) ;
         penalite = (TextView) v.findViewById(R.id.penalite) ;

         compteBtn = (ImageButton) v.findViewById(R.id.compteBtn) ;
        camBtn = (ImageButton) v.findViewById(R.id.camBtn);
         rech = (ImageButton) v.findViewById(R.id.rech) ;
         valider =  (Button) v.findViewById(R.id.valider) ;
         annuler =  (Button) v.findViewById(R.id.annuler) ;

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity()) ;


        creditDAO = new CreditDAO(getActivity()) ;
        caissierDAO = new CaissierDAO(getActivity()) ;
        compteDAO = new CompteDAO(getActivity()) ;

        compteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreditActivity.class);
                intent.putExtra("TYPE", Compte.EPARGNE);
                startActivityForResult(intent, CHOOSE_CREDIT_REQUEST);
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
                Intent intent = new Intent(getActivity(), CompteActivity.class);
                intent.putExtra("TYPE", Compte.EPARGNE);
                intent.putExtra("REMBOURSSEMENT", true);
                startActivityForResult(intent, CHOOSE_COMPTE_REQUEST);
            }
        });

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.app_name);

                builder.setTitle("Remboursement");

                builder.setMessage("Voulez-vous continuer?");
                builder.setPositiveButton(R.string.valider, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (!isCorrect()) return;

                        saveOperation();
                        /*
                        if (!preferences.getBoolean("membre", false)) {
                            // SI le mode de connexion est Internet

                            if (!preferences.getBoolean(ParametreActivity.TYPE_COMMUNICATION, false)) {
                                if (!Utiles.isConnected(getActivity())) {
                                    Toast.makeText(getActivity(), R.string.anyconnexion, Toast.LENGTH_LONG).show();
                                    return;
                                }
                                sendRembroussementTask = new SendRembroussementTask(credit.getNumcredit(), Float.parseFloat(rembroussement.getText().toString()));
                                sendRembroussementTask.execute(Url.getRembrUrl(getActivity()));
                            }
                            // Sinon si le mode de connexion est Sms
                            else {

                            }
                        } else {
                            if (!isCorrect()) return;
                            saveOperation();
                        }
                        */

                    }
                });
                builder.setNegativeButton(R.string.annuler, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();

            }
        });


        return  v ;
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {
            init();
            ((MainActivity)getActivity()).selectRembroussementFragment(); ;
            if (requestCode == CHOOSE_COMPTE_REQUEST) {
                compte = compteDAO.getOne(data.getLongExtra(MainActivity.COMPTE, 0));
                Log.e("DEBUG", String.valueOf(data.getLongExtra(MainActivity.COMPTE, 0)));
                numcompte.setText(compte.getNumcompte());
                if (compte != null) {
                    if (numcompte.getText().length()>0){
                        Intent intent = new Intent(getActivity(),CreditActivity.class) ;
                        intent.putExtra(Compte.CREDIT, numcompte.getText().toString()) ;
                        startActivityForResult(intent, CHOOSE_CREDIT_REQUEST);
                    }
                    else Toast.makeText(getActivity(), R.string.dataerror, Toast.LENGTH_SHORT).show();
                }
            }
            else if (requestCode == CHOOSE_CREDIT_REQUEST) {
                long idcredit = data.getLongExtra(CREDIT,0);
                credit = creditDAO.getOne(idcredit) ;
                Log.e("CREDIT", String.valueOf(credit.getNomproduit())) ;
                if (credit!=null) {
                    refreshData(credit);
                }
            }
            else if (requestCode == CHOOSE_CODE_BAR_REQUEST) {
                String nocompte = data.getStringExtra(NOCOMPTE);
                Log.e("Nocompte", nocompte) ;
                if (nocompte!=null) {
                    compte = compteDAO.getOne(nocompte);
                    if (compte != null) {
                        numcompte.setText(compte.getNumcompte());
                        if (numcompte.getText().length()>0){
                            Intent intent = new Intent(getActivity(),CreditActivity.class) ;
                            intent.putExtra(Compte.CREDIT, numcompte.getText().toString()) ;
                            startActivityForResult(intent, CHOOSE_CREDIT_REQUEST);
                        }
                        else Toast.makeText(getActivity(), R.string.dataerror, Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(getActivity(), R.string.cmptint, Toast.LENGTH_SHORT).show();
                }
            }
            /*
            else if (requestCode == CHOOSE_MEMBRE_REQUEST) {
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
            */
        }
        //if (groupe)  membres.setEnabled(true);

    }




    private void saveOperation() {

        Caissier c = new CaissierDAO(getActivity()).getLast() ;
        OperationDAO operationDAO = new OperationDAO(getActivity()) ;

        Toast.makeText(getActivity(), R.string.opsuccess, Toast.LENGTH_LONG).show();
        String lib = "" ;
        Operation op = new Operation() ;
        op.setAgence(c.getAgence_id());
        op.setMontant(Float.parseFloat(rembroussement.getText().toString()));
        op.setNumcompte(credit.getNumcredit());
        op.setNom(credit.getNom());
        op.setNummenbre(credit.getNummembre());
        op.setPrenom(credit.getPrenom());
        op.setNumpiece(RNUMPIECE);
        op.setTypeoperation(4);
        op.setSync(0);
        try {
            op.setDateoperation(DAOBase.formatterj.parse(c.getJournee()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        lib += "REMBOURSEMENT, No : "+ op.getNumcompte() + " , Mte : " + String.valueOf(op.getMontant()) ;
        op.setTypeoperation(4);
        Caissier caissier = caissierDAO.getLast();

        op.setLibelle(lib);
        op.setUser_id(c.getId());

        int nump = caissier.getNumpiece() ;
        nump++ ;
        op.setNumpiece(preferences.getString(ConnexionActivity.DEBUTPIECE,"")+ "" + Utiles.generer(nump));

        if (operationDAO.add(op)>0){

            caissier.setNumpiece(nump);
            caissierDAO.update(caissier) ;
            refreshSolde() ;
            boolean imp = preferences.getBoolean(bluetoothConfig, false) ;
            if (imp){
                try {
                    PrinterUtils printerUtils = new PrinterUtils(getActivity()) ;
                    printerUtils.printTicket(operationDAO.getLast(),"OPERATION DE REMBOURSEMENT");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                try {
                    //PrintPDA printPDA = new PrintPDA(getActivity()) ;
                    //printPDA.printTicket(operationDAO.getLast(),"OPERATION DE REMBROUSSEMENT");

                    PrintPDAMobiPrint3 printPDAMobiPrint3 = new PrintPDAMobiPrint3(getActivity());
                    printPDAMobiPrint3.printTicket(operationDAO.getLast(),"OPERATION DE REMBOURSEMENT");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };
        init();
    }



    private float refreshSolde() {
        ArrayList<Operation> operations = operationDAO.getAll();
        float sold = new CaissierDAO(getActivity()).getLast().getSolde();
        for (int j = 0; j < operations.size(); ++j) {
            if (operations.get(j).getTypeoperation() == 0 || operations.get(j).getTypeoperation()==3 || operations.get(j).getTypeoperation()==4) sold += operations.get(j).getMontant();
            else sold -= operations.get(j).getMontant();
        }
        getActivity().setTitle("Solde : " + Utiles.formatMtn(sold) + " F");

        return sold ;
    }

    private void init() {
        rembroussement.setText("");
        nocredit.setText("");
        //numpiece.setText("");

        membre.setText("");
        mtnpret.setText("");
        datedeblocage.setText("");
        capitalRestant.setText("");
        resteApayer.setText("");
        capitalAttendu.setText("");
        interetAttendu.setText("");
        penalite.setText("");
        produit.setText("");

        credit = null ;
    }


    private boolean isCorrect() {

        if (credit==null) {
            Toast.makeText(getActivity(), R.string.infoactu, Toast.LENGTH_SHORT).show();
            return false ;
        }

        if (rembroussement.getText().length()==0) {
            Toast.makeText(getActivity(), getString(R.string.rembrerror), Toast.LENGTH_SHORT).show();
            return  false ;
        }
/*
        if (credit.getMensualite() > Float.parseFloat(rembroussement.getText().toString())) {
            Toast.makeText(getActivity(), getString(R.string.rembrincorect), Toast.LENGTH_SHORT).show();
            return  false ;
        }

        */

        if (credit==null) {
            Toast.makeText(getActivity(), getString(R.string.crediterror), Toast.LENGTH_SHORT).show();
            return  false ;
        }

        return true;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    private void refreshData(final Credit credit) {
        //Toast.makeText(getActivity(), credit.getNomproduit(), Toast.LENGTH_SHORT).show();
        produit.post(new Runnable() {
            @Override
            public void run() {
                produit.setText(credit.getNomproduit());
            }
        });
        membre.post(new Runnable() {
            @Override
            public void run() {
                membre.setText(credit.getNom() + " " + credit.getPrenom());
            }
        });
        nocredit.post(new Runnable() {
            @Override
            public void run() {
                nocredit.setText(credit.getNumcredit());
            }
        });
        mtnpret.post(new Runnable() {
            @Override
            public void run() {
                mtnpret.setText(Utiles.formatMtn(credit.getMontantpret()));
            }
        });
        capitalAttendu.post(new Runnable() {
            @Override
            public void run() {
                capitalAttendu.setText(Utiles.formatMtn(credit.getCapital_attendu()));
            }
        });
        capitalRestant.post(new Runnable() {
            @Override
            public void run() {
                capitalRestant.setText(Utiles.formatMtn(credit.getCreaditencours()));
            }
        });
        resteApayer.post(new Runnable() {
            @Override
            public void run() {
                resteApayer.setText(Utiles.formatMtn(credit.getReste_apayer()));
            }
        });
        interetAttendu.post(new Runnable() {
            @Override
            public void run() {
                interetAttendu.setText(Utiles.formatMtn(credit.getInteret_attendu()));
            }
        });
        penalite.post(new Runnable() {
            @Override
            public void run() {
                penalite.setText(Utiles.formatMtn(credit.getPenalite()));
            }
        });

        datedeblocage.post(new Runnable() {
            @Override
            public void run() {
                if (credit.getDatedeblocage()!=null)datedeblocage.setText(DAOBase.formatterj.format(credit.getDatedeblocage()));
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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



    public class SendRembroussementTask extends AsyncTask<String,Void,String> {

        String numcompte = null ;
        String typop = null ;
        String numcredit = null ;
        float montant = 0 ;

        OperationDAO operationDAO = null ;
        Caissier c  =null;

        public SendRembroussementTask(String numcredit, float montant) {
            this.montant = montant ;
            this.numcredit = numcredit ;
        }

        @Override
        protected String doInBackground(String... urls) {
            //TelephonyManager telephonyManager =  ( TelephonyManager ) getSystemService (Context.TELEPHONY_SERVICE);
            //String imei = telephonyManager.getDeviceId ();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity()) ;

            Date date = new Date() ;
            FormBody.Builder formBuilder = new FormBody.Builder();
            formBuilder.add("numcredit",numcredit);
            formBuilder.add("journee", c.getJournee());
            formBuilder.add("montant",String.valueOf(montant));
            formBuilder.add("guichet",c.getCodeguichet());


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

            getActivity().dismissDialog(MainActivity.PROGRESS_DIALOG_ID);
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getActivity().showDialog(MainActivity.PROGRESS_DIALOG_ID);
            if (operationDAO==null) operationDAO = new OperationDAO(getActivity()) ;

            c = new CaissierDAO(getActivity()).getLast() ;
        }
    }



    public void processOperationResult(String result) {

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

        if (result.contains("OK:") && result.split(":").length==3) {

            Toast.makeText(getActivity(), R.string.opsuccess, Toast.LENGTH_LONG).show();
            String lib = "" ;
            Operation op = new Operation();
            op.setAgence(c.getAgence_id());
            op.setMontant(Float.parseFloat(rembroussement.getText().toString()));
            op.setNumcompte(credit.getNumcredit());
            op.setNom(credit.getNom());
            op.setPrenom(credit.getPrenom());
            op.setNumpiece(result.split(":")[1]);
            numpiece.setText(result.split(":")[1]);
            op.setTypeoperation(4);

            try {
                op.setDateoperation(DAOBase.formatterj.parse(c.getJournee()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            lib += "REMBROUSSEMENT, No : "+ op.getNumcompte() + " , Mte : " + String.valueOf(op.getMontant()) ;

            op.setLibelle(lib);
            op.setUser_id(c.getId());


            if (operationDAO.add(op)>0){

                refreshSolde() ;
                //init();
                credit=null;
                rembroussement.getText().clear();

                boolean imp = preferences.getBoolean(bluetoothConfig, false) ;
                if (imp){
                    try {
                        PrinterUtils printerUtils = new PrinterUtils(getActivity()) ;
                        printerUtils.printTicket(operationDAO.getLast(),"OPERATION DE REMBOURSEMENT");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        //PrintPDA printPDA = new PrintPDA(getActivity()) ;
                        //printPDA.printTicket(operationDAO.getLast(),"OPERATION DE REMBROUSSEMENT");

                        PrintPDAMobiPrint3 printPDAMobiPrint3 = new PrintPDAMobiPrint3(getActivity());
                        printPDAMobiPrint3.printTicket(operationDAO.getLast(),"OPERATION DE REMBOURSEMENT");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            };
        }
    }

}
