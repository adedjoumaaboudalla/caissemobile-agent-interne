package caissemobile.com.mediasoft.projet.microfinacaissemobile.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import caissemobile.com.mediasoft.projet.microfinacaissemobile.MainActivity;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.R;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.activities.CategorieActivity;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.activities.ConnexionActivity;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.activities.NationaliteActivity;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.activities.ParametreActivity;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.activities.ProfessionActivity;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.activities.ZoneActivity;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Caissier;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Categorie;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Membre;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Nationalite;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Operation;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Produit;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Profession;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Zone;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CaissierDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CategorieDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.DAOBase;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.MembreDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.NationaliteDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.OperationDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.ProduitDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.ProfessionDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.ZoneDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.database.MembreHelper;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.CropingOption;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.PrintPDA;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.PrintPDAMobiPrint3;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.PrinterUtils;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.Url;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.Utiles;
import okhttp3.FormBody;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AdhesionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdhesionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdhesionFragment extends Fragment implements LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "AdhesionFragment";

    private static final int CAMERA_CODE = 101, GALLERY_CODE = 201, CROPING_CODE = 301;

    private Uri mImageCaptureUri;
    private File outPutFile = null;
    private String filePath = null;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static final String CATEGORIE = "categorie";
    public static final String ZONE = "zone";
    public static final String NATIONALITE = "nationalite";
    public static final String PROFESSION = "profession";


    public static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.FRENCH);
    private static final int CHOOSE_CAT_REQUEST = 0;
    private static final int CHOOSE_NAT_REQUEST = 1;
    private static final int CHOOSE_PROF_REQUEST = 2;
    private static final int CHOOSE_ZONE_REQUEST = 3;


    ImageButton catBtn = null;
    ImageButton profBtn = null;
    ImageButton natBtn = null;
    ImageButton zoneBtn = null;
    ImageButton dateBtn = null;

    EditText dateET = null;
    EditText datenaissET = null;
    EditText nomET = null;
    EditText prenomET = null;
    EditText telET = null;
    EditText catET = null;
    EditText profET = null;
    EditText zoneET = null;
    EditText natET = null;
    EditText adreseET = null;
    EditText mtnET = null;

    ImageView imageView = null;

    TextView transaction = null;
    TextView nummembre = null;

    Button validerBtn = null;
    Button efacerBtn = null;

    AlertDialog.Builder dateBox = null;


    CaissierDAO caissierDAO = null;
    CategorieDAO categorieDAO = null;
    ZoneDAO zoneDAO = null;
    ProfessionDAO professionDAO = null;
    NationaliteDAO nationaliteDAO = null;

    private Categorie categorie = null;
    private Zone zone = null;
    private Profession profession = null;
    private Nationalite nationalite = null;

    RadioGroup rg = null;
    public SendMembreTask sendMembreTask;
    private LocationManager locationManager;
    private Location mLocation = null;
    private CheckBox localisation;
    TextView position = null;
    private SharedPreferences preferences;
    private MembreDAO membreDAO;
    private String bluetoothConfig = "imprimenteexterne";
    private MainActivity mParent;
    private Date d;
    private Membre membre;
    private Caissier caissier;
    private ProduitDAO produitDAO;
    private Spinner produitSpinner;
    private ArrayList<Produit> produits;
    private ArrayAdapter<String> produitAdapter;


    public AdhesionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdhesionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdhesionFragment newInstance(String param1, String param2) {
        AdhesionFragment fragment = new AdhesionFragment();
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
        View v = inflater.inflate(R.layout.fragment_adhesion, container, false);

        caissierDAO  = new CaissierDAO(getActivity()) ;
        caissier = caissierDAO.getLast() ;


        catBtn = (ImageButton) v.findViewById(R.id.catbtn);
        profBtn = (ImageButton) v.findViewById(R.id.profBtn);
        natBtn = (ImageButton) v.findViewById(R.id.natBtn);
        zoneBtn = (ImageButton) v.findViewById(R.id.zoneBtn);
        dateBtn = (ImageButton) v.findViewById(R.id.datebox);

        d = new Date();

        imageView = (ImageView) v.findViewById(R.id.imageview);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImageOption();
            }
        });

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        membreDAO = new MembreDAO(getActivity());

        localisation = (CheckBox) v.findViewById(R.id.location);
        position = (TextView) v.findViewById(R.id.position);

        rg = (RadioGroup) v.findViewById(R.id.sexe);

        dateET = (EditText) v.findViewById(R.id.dateadhesion);
        datenaissET = (EditText) v.findViewById(R.id.datenaiss);
        nomET = (EditText) v.findViewById(R.id.nom);
        prenomET = (EditText) v.findViewById(R.id.prenom);
        telET = (EditText) v.findViewById(R.id.tel);
        catET = (EditText) v.findViewById(R.id.categorie);
        profET = (EditText) v.findViewById(R.id.profession);
        zoneET = (EditText) v.findViewById(R.id.zone);
        natET = (EditText) v.findViewById(R.id.nationalite);
        adreseET = (EditText) v.findViewById(R.id.adresse);
        mtnET = (EditText) v.findViewById(R.id.montant);

        transaction = (TextView) v.findViewById(R.id.transaction);
        nummembre = (TextView) v.findViewById(R.id.nummembre);
        validerBtn = (Button) v.findViewById(R.id.valider);
        efacerBtn = (Button) v.findViewById(R.id.annuler);

        validerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAdhesion();
            }
        });


        efacerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cleanData();
                transaction.setText("");
                nummembre.setText("");
            }
        });

        produitDAO = new ProduitDAO(getActivity()) ;
        produits = produitDAO.getAll() ;
        produitSpinner = (Spinner) v.findViewById(R.id.produit);

        ArrayList<String> produitString = new ArrayList<>() ;
        for (int i = 0; i < produits.size(); i++) {
            produitString.add(produits.get(i).getLibelle().toUpperCase()) ;
        }

        produitAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,produitString) ;
        produitSpinner.setAdapter(produitAdapter);
        produitSpinner.setSelection(0);

        outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");


        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateBox = new AlertDialog.Builder(getActivity());
                ScrollView scrollView = (ScrollView) getActivity().getLayoutInflater().inflate(R.layout.dialogbox, null);
                dateBox.setView(scrollView);
                dateBox.setTitle(getString(R.string.datechoice));

                final DatePicker debut = (DatePicker) scrollView.findViewById(R.id.dateDebut);
                final DatePicker fin = (DatePicker) scrollView.findViewById(R.id.dateFin);
                ((TextView) scrollView.findViewById(R.id.datefin)).setVisibility(View.GONE);
                ((TextView) scrollView.findViewById(R.id.datedebut)).setVisibility(View.GONE);
                fin.setVisibility(View.GONE);
                Button button = (Button) scrollView.findViewById(R.id.valider);
                final AlertDialog alert = dateBox.show();
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dateBox != null) {
                            String dateDebut = String.valueOf(debut.getYear()) + "-";
                            if (debut.getMonth() + 1 > 9)
                                dateDebut = dateDebut + String.valueOf(debut.getMonth() + 1) + "-";
                            else
                                dateDebut = dateDebut + "0" + String.valueOf(debut.getMonth() + 1) + "-";
                            if (debut.getDayOfMonth() > 9)
                                dateDebut = dateDebut + String.valueOf(debut.getDayOfMonth());
                            else
                                dateDebut = dateDebut + "0" + String.valueOf(debut.getDayOfMonth());

                            d = new Date();
                            try {
                                d = DAOBase.formatter2.parse(dateDebut);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            datenaissET.setText(DAOBase.formatterj.format(d));
                            alert.dismiss();
                        }
                    }
                });


            }
        });


        dateET.setText(caissier.getJournee());

        catBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CategorieActivity.class);
                startActivityForResult(intent, CHOOSE_CAT_REQUEST);
            }
        });

        profBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProfessionActivity.class);
                startActivityForResult(intent, CHOOSE_PROF_REQUEST);
            }
        });

        zoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ZoneActivity.class);
                startActivityForResult(intent, CHOOSE_ZONE_REQUEST);
            }
        });

        natBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NationaliteActivity.class);
                startActivityForResult(intent, CHOOSE_NAT_REQUEST);
            }
        });

        return v;
    }

    private void sendAdhesion() {
        if (isCorrect()) {
            membre = new Membre();
            try {
                membre.setDateadhesion(DAOBase.formatterj.parse(caissier.getJournee()));
                membre.setDateNaiss(DAOBase.formatterj.parse(datenaissET.getText().toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            membre.setMontant(Float.parseFloat(mtnET.getText().toString()));
            membre.setNom(nomET.getText().toString().toUpperCase());
            membre.setPrenom(prenomET.getText().toString());
            membre.setAdresse(adreseET.getText().toString());
            membre.setCategorie(categorie.getId());
            membre.setZone(zone.getDescription());
            membre.setTel(telET.getText().toString());
            membre.setNationalite(nationalite.getNumnation());
            membre.setProfession(profession.getNumprofession());
            if (rg.getCheckedRadioButtonId() == R.id.masculin) membre.setSexe("M");
            else if (rg.getCheckedRadioButtonId() == R.id.feminin) membre.setSexe("F");
            else if (rg.getCheckedRadioButtonId() == R.id.pm) membre.setSexe("N");

            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());


            LinearLayout ll = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.adhesionconfimed, null);
            dialog.setView(ll);
            TextView partSocial = (TextView) ll.findViewById(R.id.partsocial);
            TextView droit = (TextView) ll.findViewById(R.id.droitentre);
            TextView total = (TextView) ll.findViewById(R.id.total);
            TextView depot = (TextView) ll.findViewById(R.id.depotinitial);

            ArrayList<Categorie> categories = categorieDAO.getAll(categorie.getNumCategorie(),produits.get(produitSpinner.getSelectedItemPosition()).getNumproduit()) ;
            dialog.setTitle(R.string.adhesion);
            double frais = 0 ;
            String msg = "" ;
            for (int i = 0; i < categories.size(); i++) {
                if (i!=0) msg += "\n" ;
                msg +=  categories.get(i).getLibelleFrais() + " : " + String.format("%.2f", categories.get(i).getValeurFrais());
                frais += categories.get(i).getValeurFrais() ;
            }
            droit.setText(msg) ;
            depot.setText("Depot Initial : " + String.format("%.2f", membre.getMontant() - frais));
            total.setText("Total : " + String.format("%.2f", membre.getMontant()));

            dialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                        // SI le mode de connexion est Internet

                            OperationDAO dao = new OperationDAO(getActivity()) ;
                            Caissier caissier = caissierDAO.getLast() ;

                            float sold = refreshSolde() ;

                            if (caissier.getRetraitMax() < (membre.getMontant() + sold)) {
                                Toast.makeText(getActivity(), R.string.soldeattend, Toast.LENGTH_SHORT).show();
                                return;
                            }

                            membre.setNummembre("AD"+System.currentTimeMillis());
                            membre.setSync(0);
                            long k = membreDAO.add(membre) ;
                            if (k>0){
                                membre.setId(k);
                                Toast.makeText(getActivity(), R.string.adhesionsuccess,Toast.LENGTH_LONG).show() ;
                                if (filePath==null){
                                    transaction.setText("");
                                    nummembre.setText("");
                                }

                                Operation op = new Operation() ;
                                try {
                                    op.setDateoperation(DAOBase.formatterj.parse(caissier.getJournee()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                int nump = caissier.getNumpiece() ;
                                nump++ ;
                                op.setTypeoperation(3);
                                op.setMontant(membre.getMontant());
                                op.setNumpiece(preferences.getString(ConnexionActivity.DEBUTPIECE,"") + "" + Utiles.generer(nump));
                                op.setUser_id(k);
                                op.setNom(membre.getNom());
                                op.setNumproduit(produits.get(produitSpinner.getSelectedItemPosition()).getNumproduit());
                                op.setPrenom(membre.getPrenom());
                                op.setNumcompte(membre.getNummembre()+"E01");
                                op.setLibelle("ADHESION, No membre :"+ membre.getNummembre() + "E01");
                                op.setToken(caissier.getCodeguichet() + "/" + membre.getNom() + "/" + System.currentTimeMillis());
                                op.setAgence(caissierDAO.getLast().getAgence_id());
                                try {
                                    op.setDateoperation(DAOBase.formatter.parse(caissier.getJournee()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                op.setSync(0);
                                long num = dao.add(op) ;
                                if (num>0){
                                    caissier.setNumpiece(nump);
                                    caissierDAO.update(caissier) ;

                                    refreshSolde();

                                    imprimeTicket(membre, categorieDAO.getOne(membre.getCategorie()),op.getNumproduit()) ;
                                    transaction.setText(getString(R.string.numpiece) + op.getNumpiece());
                                    nummembre.setText(getString(R.string.nummembre) + membre.getNummembre());

                                    if (filePath!=null) {
                                        String name = null;

                                        name = membre.getNummembre() + ".jpg";

                                        try {
                                            Uri uri = Utiles.saveImageExternalStorage(((BitmapDrawable) imageView.getDrawable()).getBitmap(), getActivity(), name, Utiles.MCM);
                                            membre.setPhoto(uri.getPath());
                                            int j = membreDAO.update(membre);
                                            Log.e("SAVE IMAGE", membre.getPhoto());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                            Log.e("NOT SAVE IMAGE", membre.getPhoto());
                                        }
                                    }
                                }
                                cleanData();
                            }

                            /*
                            sendMembreTask = new SendMembreTask(membre, localisation.isChecked());
                            sendMembreTask.execute(Url.getAddMembreUrl(getActivity()));
                            */

                        /*
                        else {
                            Caissier c = caissierDAO.getLast();
                            membre.setNummembre("-");

                            //membreDAO.add(membre);
                            //mLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            double lat = 0;
                            double longi = 0;
                            if (mLocation != null) {
                                lat = mLocation.getLatitude();
                                longi = mLocation.getLongitude();
                            }
                            String msg = membre.getMontant() + ":" + categorieDAO.getOne(membre.getCategorie()).getNumCategorie() + ":" + membre.getProfession() + ":" + membre.getNationalite() + ":" + membre.getNom() + ":" + membre.getPrenom() + ":" + membre.getSexe() + ":" + membre.getTel() + ":" + membre.getAdresse() + ":" + DAOBase.formatterj.format(membre.getDateadhesion()) + ":" + membre.getZone() + ":" + c.getCodeguichet() + ":" + preferences.getString("dateouvert", "") + ":" + DAOBase.formatterj.format(membre.getDateNaiss()) + ":" + longi + ":" + lat;
                            mParent.sendSMS(msg, 2);
                        }
                        */
                }
            });
            dialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            final AlertDialog alert = dialog.create();
            alert.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface arg0) {
                    alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.my_accent));
                    alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.my_accent));
                }
            });
            alert.show();
        } else {
            //Toast.makeText(getActivity(), R.string.dataerror, Toast.LENGTH_SHORT).show();
        }
    }

    private float refreshSolde() {
        OperationDAO operationDAO = new OperationDAO(getActivity()) ;
        ArrayList<Operation> operations = operationDAO.getAll();
        float sold = caissier.getSolde();
        for (int j = 0; j < operations.size(); ++j) {
            if (operations.get(j).getTypeoperation() == 0 || operations.get(j).getTypeoperation()==3 || operations.get(j).getTypeoperation()==4) sold += operations.get(j).getMontant();
            else sold -= operations.get(j).getMontant();
        }
        getActivity().setTitle("Solde : " + Utiles.formatMtn(sold) + " F");

        return sold ;
    }


    private void imprimeTicket(Membre membre, Categorie categorie, String numproduit) {
        boolean imp = preferences.getBoolean(bluetoothConfig, false);
        if (imp) {
            try {
                PrinterUtils printerUtils = new PrinterUtils(getActivity());
                printerUtils.printTicket(categorie, "ADHESION", membre, numproduit);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                //PrintPDA printPDA = new PrintPDA(getActivity());
                //printPDA.printTicket(categorie, "ADHESION", membre);

                PrintPDAMobiPrint3 printPDAMobiPrint3 = new PrintPDAMobiPrint3(getActivity());
                printPDAMobiPrint3.printTicket(categorie, "ADHESION", membre, numproduit);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (categorie != null) outState.putLong(CATEGORIE, categorie.getId());
        if (nationalite != null) outState.putLong(NATIONALITE, nationalite.getNumnation());
        if (zone != null) outState.putLong(ZONE, zone.getId());
        if (profession != null) outState.putLong(PROFESSION, profession.getNumprofession());

    }


    private boolean isCorrect() {
        if (nomET.getText().toString().trim().length() < 2) {
            Toast.makeText(getActivity(), R.string.nomerror, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (prenomET.getText().toString().trim().length() < 2) {
            Toast.makeText(getActivity(), R.string.prenomerror, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (telET.getText().toString().trim().length() < 2) {
            Toast.makeText(getActivity(), R.string.telerror, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (adreseET.getText().toString().trim().length() < 2) adreseET.setText("");
        ;
        if (mtnET.getText().toString().trim().length() <= 0) {
            Toast.makeText(getActivity(), R.string.mtnerror, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (categorie == null) {
            Toast.makeText(getActivity(), R.string.caterror, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (zone == null) {
            Toast.makeText(getActivity(), R.string.zoneerror, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (nationalite == null) {
            Toast.makeText(getActivity(), R.string.naterror, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (profession == null) {
            Toast.makeText(getActivity(), R.string.preferror, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (rg.getCheckedRadioButtonId() <= 0) {
            Toast.makeText(getActivity(), R.string.sexeincorect, Toast.LENGTH_SHORT).show();
            return false;
        }

        ArrayList<Categorie> categories = categorieDAO.getAll(categorie.getNumCategorie(),produits.get(produitSpinner.getSelectedItemPosition()).getNumproduit()) ;
        double frais = 0 ;
        for (int i = 0; i < categories.size(); i++) {
            frais += categories.get(i).getValeurFrais() ;
        }

        frais += produits.get(produitSpinner.getSelectedItemPosition()).getDepotmini() ;

        if (Float.parseFloat(mtnET.getText().toString()) < frais) {
            Toast.makeText(getActivity(), getResources().getString(R.string.montantmini) + " " + frais, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        caissierDAO = new CaissierDAO(getActivity());
        categorieDAO = new CategorieDAO(getActivity());
        zoneDAO = new ZoneDAO(getActivity());
        nationaliteDAO = new NationaliteDAO(getActivity());
        professionDAO = new ProfessionDAO(getActivity());

        if (savedInstanceState != null) {
            categorie = categorieDAO.getOne(savedInstanceState.getInt(CATEGORIE));
            nationalite = nationaliteDAO.getOne(savedInstanceState.getInt(NATIONALITE));
            zone = zoneDAO.getOne(savedInstanceState.getInt(ZONE));
            profession = professionDAO.getOne(savedInstanceState.getInt(PROFESSION));
        }

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        //mLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        List<String> providers = locationManager.getProviders(true);
        for (int i = 0; i < providers.size(); ++i) {

            //mLocation = locationManager.getLastKnownLocation(providers.get(i));
            if (mLocation!=null) position.setText("Long : " + mLocation.getLongitude() + " ; Lat : " + mLocation.getLatitude());
            if (mLocation!=null) break ;
        }
        localisation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)  Utiles.checkGPS(locationManager,getActivity());
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    private void cleanData() {
        categorie = null ;
        zone = null ;
        nationalite = null ;
        profession = null ;
        nomET.setText("");
        prenomET.setText("");
        adreseET.setText("");
        telET.setText("");
        mtnET.setText("");
        profET.setText("");
        natET.setText("");
        zoneET.setText("");
        catET.setText("");
        filePath=null ;
        imageView.setImageBitmap(null);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK ) {
            ((MainActivity)getActivity()).selectAdhesionTab(1) ;

            if (requestCode == CHOOSE_CAT_REQUEST) {
                categorie = categorieDAO.getOne(data.getStringExtra(CATEGORIE));
                if (categorie!=null) catET.setText(categorie.getLibelleCategorie());
            }
            else if (requestCode == CHOOSE_ZONE_REQUEST) {
                zone = zoneDAO.getOne(data.getLongExtra(ZONE,0));
                if (zone!=null) zoneET.setText(zone.getDescription());
            }
            else if (requestCode == CHOOSE_PROF_REQUEST) {
                profession = professionDAO.getOne(data.getLongExtra(PROFESSION,0));
                if (profession!=null) profET.setText(profession.getLibelle());
            }
            else if (requestCode == CHOOSE_NAT_REQUEST) {
                nationalite = nationaliteDAO.getOne(data.getLongExtra(NATIONALITE,0));
                if (nationalite!=null) natET.setText(nationalite.getLibelle());
            }
        }


        if (requestCode == GALLERY_CODE && resultCode == getActivity().RESULT_OK && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(columnIndex);
            cursor.close();

            outPutFile = new File(filePath) ;

            //Bitmap img = BitmapFactory.decodeFile(filePath);
            //img = Utiles.getResizedBitmap(img, 750, 1200);
            //imageView.setImageBitmap(img);

            processImage();

        } else if (requestCode == CAMERA_CODE && resultCode == Activity.RESULT_OK) {

            Log.e("Camera Image URI : ", String.valueOf(mImageCaptureUri));
            cropingIMG();
            outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
            processImage();
        } else if (requestCode == CROPING_CODE  && resultCode == getActivity().RESULT_OK) {

            try {
                if(outPutFile.exists()){
                    Bitmap photo = Utiles.saveScaledPhotoToFile(Utiles.decodeFile(outPutFile));
                    filePath = outPutFile.getAbsolutePath();
                    Log.e("PATH", filePath);
                    imageView.setImageBitmap(photo);
                }
                else {
                    Toast.makeText(getActivity(),  getString(R.string.echec), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            mParent = (MainActivity) getActivity();
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

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        position.setText("Long : " + mLocation.getLongitude() + " ; Lat : " + mLocation.getLatitude());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void processOperationResult(String result) {

        if (result.contains("OK:") && result.split(":").length==3) {

        membre.setNummembre(result.split(":")[2]);
        //Toast.makeText(getActivity(), R.string.adhesionsuccess,Toast.LENGTH_LONG).show() ;
        long k = membreDAO.add(membre) ;
        if (k>0){
            Toast.makeText(getActivity(), R.string.adhesionsuccess,Toast.LENGTH_LONG).show() ;
            cleanData();
            transaction.setText("");
            nummembre.setText("");
            Operation op = new Operation() ;
            OperationDAO dao = new OperationDAO(getActivity()) ;
            op.setTypeoperation(3);
            op.setMontant(membre.getMontant());
            op.setNumpiece(result.split(":")[1]);
            op.setUser_id((int) k);
            op.setNom(membre.getNom());
            op.setPrenom(membre.getPrenom());
            op.setNumcompte(membre.getNummembre()+"E01");
            op.setAgence(caissierDAO.getLast().getAgence_id());
            long num = dao.add(op) ;
            if (num>0){
                Caissier c = new CaissierDAO(getActivity()).getLast() ;
                OperationDAO operationDAO = new OperationDAO(getActivity()) ;

                refreshSolde() ;

                imprimeTicket(membre, categorieDAO.getOne(membre.getCategorie()),op.getNumproduit()) ;
                transaction.setText(getString(R.string.numpiece)+op.getNumpiece());
                nummembre.setText(getString(R.string.nummembre)+membre.getNummembre());


                //ImageProcess ip = new ImageProcess(this) ;

                // Sauvegarder la photo si elle a été choisi
                if (filePath!=null) {
                    String name = null;

                    name = membre.getNummembre() + ".jpg";

                    try {
                        Uri uri = Utiles.saveImageExternalStorage(((BitmapDrawable) imageView.getDrawable()).getBitmap(), getActivity(), name, Utiles.MCM);
                        membre.setPhoto(uri.getPath());
                        membreDAO.update(membre);
                        Log.e("SAVE IMAGE", membre.getPhoto());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("NOT SAVE IMAGE", membre.getPhoto());
                    }
                }

            }
        }
    }
    else if (result.equals("BASE")){
        transaction.setText(R.string.baseerror) ;
            //membreDAO.delete(membre.getId()) ;
    }
    else if (result.equals("NUMPIECE")){
            transaction.setText(R.string.pieceerror) ;
            //membreDAO.delete(membre.getId()) ;
    }
    else if (result.equals("ECHEC")){
            transaction.setText(R.string.pieceerror) ;
            //membreDAO.delete(membre.getId()) ;
    }

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



    public class SendMembreTask extends AsyncTask<String,Void,String> {

        Membre membre = null ;
        MembreDAO membreDAO = null ;

        CaissierDAO caissierDAO = null ;
        Caissier c  =null;
        boolean gps = false ;

        public SendMembreTask(Membre membre,boolean gps) {
            this.membre = membre ;
            this.gps = gps ;
        }

        @Override
        protected String doInBackground(String... urls) {
            //TelephonyManager telephonyManager =  ( TelephonyManager ) getSystemService (Context.TELEPHONY_SERVICE);
            //String imei = telephonyManager.getDeviceId ();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity()) ;

            Date date = new Date() ;
            FormBody.Builder formBuilder = new FormBody.Builder();
            formBuilder.add("guichet",String.valueOf(c.getCodeguichet()));
            formBuilder.add("journee",c.getJournee());
            formBuilder.add("Mt_Operation",Utiles.formatMtn2(membre.getMontant()));
            formBuilder.add("NumCategorie",categorieDAO.getOne(membre.getCategorie()).getNumCategorie());
            formBuilder.add("NumProfession",String.valueOf(membre.getProfession()));
            formBuilder.add("NumNationalite",String.valueOf(membre.getNationalite()));
            formBuilder.add(MembreHelper.NOM, membre.getNom());
            formBuilder.add(MembreHelper.PRENOM,membre.getPrenom());
            formBuilder.add(MembreHelper.SEXE,membre.getSexe());
            formBuilder.add(MembreHelper.TEL,membre.getTel());
            formBuilder.add(MembreHelper.ADRESSE,membre.getAdresse());
            formBuilder.add(MembreHelper.DATE,DAOBase.formatterj.format(membre.getDateadhesion()));
            formBuilder.add(MembreHelper.DATENAISS,DAOBase.formatterj.format(membre.getDateNaiss()));
            formBuilder.add("NumZone",membre.getZone());
            formBuilder.add("codeGuichet",String.valueOf(c.getCodeguichet()));
            if (gps && mLocation!=null){
                formBuilder.add("lat",String.valueOf(mLocation.getLatitude()));
                formBuilder.add("long",String.valueOf(mLocation.getLongitude()));
            }
            else{
                formBuilder.add("lat","0");
                formBuilder.add("long","0");
            }

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

            if (result.contains("OK:") && result.split(":").length==3) {
                membre.setNummembre(result.split(":")[2]);
                //Toast.makeText(getActivity(), R.string.adhesionsuccess,Toast.LENGTH_LONG).show() ;
                long k = membreDAO.add(membre) ;
                if (k>0){
                    Toast.makeText(getActivity(), R.string.adhesionsuccess,Toast.LENGTH_LONG).show() ;
                    if (filePath==null){
                        cleanData();
                        transaction.setText("");
                        nummembre.setText("");
                    }
                    Operation op = new Operation() ;
                    try {
                        op.setDateoperation(DAOBase.formatter.parse(c.getJournee()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    OperationDAO dao = new OperationDAO(getActivity()) ;
                    op.setTypeoperation(3);
                    op.setMontant(membre.getMontant());
                    op.setNumpiece(result.split(":")[1]);
                    op.setUser_id((int) k);
                    op.setNom(membre.getNom());
                    op.setPrenom(membre.getPrenom());
                    op.setNumcompte(membre.getNummembre()+"E01");
                    op.setLibelle("ADHESION, No membre :"+ membre.getNummembre() + "E01");
                    op.setAgence(caissierDAO.getLast().getAgence_id());
                    long num = dao.add(op) ;
                    if (num>0){
                        Caissier c = new CaissierDAO(getActivity()).getLast() ;
                        OperationDAO operationDAO = new OperationDAO(getActivity()) ;

                        refreshSolde() ;

                        imprimeTicket(membre, categorieDAO.getOne(membre.getCategorie()),op.getNumproduit()) ;
                        transaction.setText(getString(R.string.numpiece) + op.getNumpiece());
                        nummembre.setText(getString(R.string.nummembre) + membre.getNummembre());

                        if (filePath!=null) {
                            String name = null;

                            name = membre.getNummembre() + ".jpg";

                            try {
                                Uri uri = Utiles.saveImageExternalStorage(((BitmapDrawable) imageView.getDrawable()).getBitmap(), getActivity(), name, Utiles.MCM);
                                membre.setPhoto(uri.getPath());
                                membreDAO.update(membre);
                                Log.e("SAVE IMAGE", membre.getPhoto());
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e("NOT SAVE IMAGE", membre.getPhoto());
                            }
                        }
                    }
                }
            }
            else if (result.equals("BASE")){
                Toast.makeText(getActivity(), R.string.baseerror,Toast.LENGTH_LONG).show() ;
            }
            else if (result.equals("NUMPIECE")){
                Toast.makeText(getActivity(), R.string.pieceerror,Toast.LENGTH_LONG).show() ;
            }
            else if (result.equals("ECHEC")){
                Toast.makeText(getActivity(), R.string.pieceerror, Toast.LENGTH_LONG).show() ;
            }

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
            if (membreDAO==null) membreDAO = new MembreDAO(getActivity()) ;
            if (caissierDAO==null) caissierDAO = new CaissierDAO(getActivity()) ;
            c = new CaissierDAO(getActivity()).getLast() ;
        }
    }




    private void cropingIMG() {

        final ArrayList<CropingOption> cropOptions = new ArrayList();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities(intent, 0);
        int size = list.size();
        if (size == 0) {
            imageView.setImageURI(Uri.fromFile(outPutFile));
            Toast.makeText(getActivity(), R.string.norognapp, Toast.LENGTH_SHORT).show();
            return;
        } else {
            intent.setData(mImageCaptureUri);
            intent.putExtra("outputX", 512);
            intent.putExtra("outputY", 512);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);

            //TODO: don't use return-data tag because it's not return large image data and crash not given any message
            //intent.putExtra("return-data", true);

            //Create output file here
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outPutFile));

            if (size == 1) {
                Intent i   = new Intent(intent);
                ResolveInfo res = (ResolveInfo) list.get(0);

                i.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                startActivityForResult(i, CROPING_CODE);
            } else {
                ResolveInfo res = null ;
                String items[] = new String[list.size()];
                for (int i = 0; i < list.size(); ++i) {
                    res = list.get(i) ;
                    final CropingOption co = new CropingOption();

                    co.title  = getActivity().getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                    co.icon  = getActivity().getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                    co.appIntent= new Intent(intent);
                    co.appIntent.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                    cropOptions.add(co);
                    items[i] = co.title.toString() ;
                }

                //CropingOptionAdapter adapter = new CropingOptionAdapter(getActivity(), cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Choose Croping App");
                builder.setCancelable(true);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult( cropOptions.get(which).appIntent, CROPING_CODE);
                    }
                }) ;
                builder.setOnCancelListener( new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel( DialogInterface dialog ) {

                        if (mImageCaptureUri != null ) {
                            //getActivity().getContentResolver().delete(mImageCaptureUri, null, null );
                            mImageCaptureUri = null;
                        }
                    }
                } );

                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }


    private void selectImageOption() {
        final CharSequence[] items = { getString(R.string.camera), getString(R.string.gallery), getString(R.string.delete), getString(R.string.annuler) };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.addfoto));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals(getString(R.string.camera))) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    mImageCaptureUri = Uri.fromFile(f);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                    startActivityForResult(intent, CAMERA_CODE);

                } else if (items[item].equals(getString(R.string.gallery))) {

                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, GALLERY_CODE);

                }else if (items[item].equals(getString(R.string.delete))) {

                    if(filePath!=null){
                        filePath = null;
                        imageView.setImageResource(R.mipmap.ic_membre);
                    }


                } else {
                    dialog.dismiss();
                }
            }
        });
        final AlertDialog alert = builder.create();
        alert.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.my_accent));
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.my_accent));
            }
        }) ;
        alert.show();
    }





    public void processImage(){
        try {
            if(outPutFile.exists()){
                Bitmap photo = Utiles.saveScaledPhotoToFile(Utiles.decodeFile(outPutFile));
                filePath = outPutFile.getAbsolutePath();
                Log.e("PATH", filePath) ;
                imageView.setImageBitmap(photo);
            }
            else {
                Toast.makeText(getActivity(),  getString(R.string.echec), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




/*

    public class UpdateImageTask extends AsyncTask<String, Void, String> {
        SharedPreferences preferences = null;
        String num = null ;
        private String[] path;
        String filepath = null ;

        public UpdateImageTask(String path ,String num) {
            this.num = num ;
            this.filepath = path ;
        }

        @Override
        protected String doInBackground(String... urls) {
            int response = 0 ;
            if (filePath != null) {
                String newpath = filePath.replace('.', ':');
                path = newpath.split(":");
                Log.e("DEBUGGG", String.valueOf(path.length));
                Log.e("DEBUGGG", filePath);
                filepath= android.os.Environment.getExternalStorageDirectory()+"/temp.jpg" ;
                response = Utiles.uploadFile(filePath,num, getActivity());
            }
            return String.valueOf(response) ;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            super.onPostExecute(response);

            if (Integer.parseInt(response) == 200) {
                //ImageProcess ip = new ImageProcess(this) ;

                /*String name = null;

                name = article.getId() + "." + path[path.length - 1];
                article.setImage(name);
                articleDAO.update(article);

                try {
                    Utiles.saveImageExternalStorage(((BitmapDrawable) headerImageView.getDrawable()).getBitmap(), ArticleFormActivity.this, article.getId() + "." + path[path.length - 1], Utiles.BEEZY_ARTICLE_IMAGE_DIR);
                    Log.e("SAVE IMAGE", article.getImage());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("NOT SAVE IMAGE", article.getImage());
                }

            } else {
                Toast.makeText(getActivity(), R.string.photonotsend, Toast.LENGTH_LONG).show();
            }

            try {
                getActivity().dismissDialog(MainActivity.PROGRESS_DIALOG_ID);
                outPutFile.delete() ;
            }catch (Exception e){
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
            getActivity().dismissDialog(MainActivity.PROGRESS_DIALOG_ID);
        }
    }

        */

}
