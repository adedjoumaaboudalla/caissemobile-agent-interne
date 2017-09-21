package caissemobile.com.mediasoft.projet.microfinacaissemobile.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;

import caissemobile.com.mediasoft.projet.microfinacaissemobile.R;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.activities.OperationActivity;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Categorie;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Membre;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Operation;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.AgenceDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CategorieDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.DAOBase;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.MembreDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.OperationDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.PrintPDA;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.PrintPDAMobiPrint3;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.PrinterUtils;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.Utiles;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OperationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OperationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OperationFragment extends Fragment {
    public static final String TAG = "OperationFragment";
    public static final String OPERATION_ID = "operation_id";
    public static final String HIDE = "hide";
    public static final String SHOW = "show";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ListView lv = null;
    ArrayList<Operation> operations = null;
    ListeOperationAdapter adapter = null;
    OperationDAO operationDAO = null;
    LinearLayout empty;
    LinearLayout full;
    LinearLayout progress;
    Button actualiser;
    private int type = 0;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private LayoutInflater mInflater;
    private SharedPreferences preferences;
    private MembreDAO membreDAO;
    private String bluetoothConfig = "imprimenteexterne";
    private OperationActivity mParent;
    private CategorieDAO categorieDAO;

    public OperationFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OperationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OperationFragment newInstance(String param1, String param2) {
        OperationFragment fragment = new OperationFragment();
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        refresh();
        //operations = new ArrayList<Operation>() ;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_operation, container, false);
        operationDAO = new OperationDAO(getActivity());
        membreDAO = new MembreDAO(getActivity());
        categorieDAO = new CategorieDAO(getActivity());

        mInflater = inflater;

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        empty = (LinearLayout) v.findViewById(R.id.vide);
        lv = (ListView) v.findViewById(R.id.liste);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Operation operation = adapter.getItem(i);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                ScrollView rl = (ScrollView) getActivity().getLayoutInflater().inflate(R.layout.operationdetails, null);

                Button fermer = (Button) rl.findViewById(R.id.annuler);
                Button imp = (Button) rl.findViewById(R.id.imp);


                ScrollView.LayoutParams param = new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                param.setMargins(5, 5, 5, 5);

                TextView agence = (TextView) rl.findViewById(R.id.agence);
                TextView numpiece = (TextView) rl.findViewById(R.id.numpiece);
                TextView numcompte = (TextView) rl.findViewById(R.id.numcompte);
                TextView nummenbre = (TextView) rl.findViewById(R.id.nummenbre);
                TextView nom = (TextView) rl.findViewById(R.id.nomprenom);
                TextView montant = (TextView) rl.findViewById(R.id.montant);
                TextView libelle = (TextView) rl.findViewById(R.id.libelle);
                TextView date = (TextView) rl.findViewById(R.id.date);

                AgenceDAO agenceDAO = new AgenceDAO(getActivity());

                if (operation.getSync()==0)numpiece.setText(operation.getNumpiece());
                else numpiece.setText(operation.getNumpicedef());

                if (operation.getTypeoperation()==3) {
                    Membre membre = membreDAO.getOne(operation.getUser_id()) ;
                    if (membre==null) membre = membreDAO.getByNumMembre(operation.getNummenbre()) ;
                    if (membre!=null)nummenbre.setText(membre.getNummembre());
                    else nummenbre.setVisibility(View.GONE);
                }
                else{
                    nummenbre.setVisibility(View.GONE);
                }

                numcompte.setText(operation.getNumcompte());
                if (operation.getNumcompte().contains("/G/"))nom.setText(operation.getNom());
                else nom.setText(operation.getNom() + " " + operation.getPrenom());
                montant.setText(Utiles.formatMtn(operation.getMontant()));
                libelle.setText(operation.getLibelle());
                date.setText(DAOBase.formatterj.format(operation.getDateoperation()));

                if (agenceDAO.getOne(operation.getAgence())==null) {
                    agence.setVisibility(View.GONE);
                }
                else agence.setText(agenceDAO.getOne(operation.getAgence()).getNomagence());
                Log.e("NIVEAU","3") ;

                builder.setView(rl);
                final AlertDialog alert = builder.show();

                fermer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.dismiss();
                    }
                });

                imp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Membre membre = membreDAO.getOne(operation.getUser_id());
                        if (membre==null) membre = membreDAO.getByNumMembre(operation.getNummenbre()) ;
                        if (operation.getTypeoperation() == 3){
                            if (membre!=null) {
                                Log.e("PRINT","1") ;
                                imprimeTicket(membre, categorieDAO.getOne(membre.getCategorie()),operation.getNumproduit());
                            }
                        }

                        else {
                                if (membre!=null)imprimeTicket(operation);
                        }
                        Toast.makeText(getActivity(), "Impression lancée", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        return v;
    }


    private void imprimeTicket(Operation operation) {

        String lib = "";
        if (operation.getTypeoperation() == 0) lib = "OPERATION D'EPARGNE";
        else if (operation.getTypeoperation() == 1) lib = "OPERATION DE TONTINE";
        else if (operation.getTypeoperation() == 4) lib = "REMBOURSEMENT DE CREDIT";
        boolean imp = preferences.getBoolean(bluetoothConfig, false);
        if (imp) {
            try {
                PrinterUtils printerUtils = new PrinterUtils(getActivity());
                printerUtils.printTicket(operation, lib + "(DUPLICATA)");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                //PrintPDA printPDA = new PrintPDA(getActivity());
                //printPDA.printTicket(operation, lib);

                PrintPDAMobiPrint3 printPDAMobiPrint3 = new PrintPDAMobiPrint3(getActivity());
                printPDAMobiPrint3.printTicket(operation, lib + "(DUPLICATA)");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mParent = (OperationActivity) getActivity();
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    private void imprimeTicket(Membre membre, Categorie categorie, String numproduit) {
        boolean imp = preferences.getBoolean(bluetoothConfig, false);
        if (imp) {
            try {
                PrinterUtils printerUtils = new PrinterUtils(getActivity());
                printerUtils.printTicket(categorie, "ADHESION"  + "(DUPLICATA)", membre, numproduit);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                //PrintPDA printPDA = new PrintPDA(getActivity());
                //printPDA.printTicket(categorie, "ADHESION", membre);

                PrintPDAMobiPrint3 printPDAMobiPrint3 = new PrintPDAMobiPrint3(getActivity());
                printPDAMobiPrint3.printTicket(categorie, "ADHESION"  + "(DUPLICATA)", membre,numproduit);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void interval(int i, String dateDebut, String dateFin) {
        if (getActivity() == null) return;
        if (operationDAO == null) operationDAO = new OperationDAO(getActivity());
        try {
            if (Integer.parseInt(mParam2)!=5) operations = operationDAO.getAll(i, DAOBase.formatter2.parse(dateDebut), DAOBase.formatter2.parse(dateFin));
            else operations = operationDAO.getArchiver(DAOBase.formatter2.parse(dateDebut), DAOBase.formatter2.parse(dateFin));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e("mParam2", mParam2);
        Log.e("SIZE", String.valueOf(operations.size()));
        if (operations != null) {
            adapter = new ListeOperationAdapter(operations);
            lv.setAdapter(adapter);
        }
    }

    private void refresh() {
        if (operationDAO == null) operationDAO = new OperationDAO(getActivity());
        if (Integer.parseInt(mParam2)!=5)operations = operationDAO.getAll(Integer.parseInt(mParam2), null, null);
        else operations = operationDAO.getArchiver(null, null);
        Log.e("mParam2", mParam2);
        Log.e("SIZE", String.valueOf(operations.size()));
        if (operations != null) {
            adapter = new ListeOperationAdapter(operations);
            lv.setAdapter(adapter);
        }
    }

    public void imprimePDFDoc(final String titre) {
        final EditText edittext = new EditText(getActivity());
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setMessage(getString(R.string.docpdf));
        alert.setTitle(getString(R.string.ficname));

        alert.setView(edittext);
        edittext.setText("Liste des operations");

        alert.setPositiveButton(R.string.valider, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                String name = edittext.getText().toString();
                Utiles.createandDisplayOperationPdf(operations, titre, getActivity());
            }
        });

        alert.setNegativeButton(R.string.annuler, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        if (operations.size()!=0)alert.show();
        else Toast.makeText(getActivity(), R.string.anyop, Toast.LENGTH_LONG).show();
    }

    public void imprimeExcelDoc(final String titre) {
        final EditText edittext = new EditText(getActivity());
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setMessage(getString(R.string.docexcel));
        alert.setTitle(getString(R.string.ficname));

        alert.setView(edittext);
        edittext.setText("Liste des operations");

        alert.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                String name = edittext.getText().toString();
                Utiles.createandDisplayOperationExcel(operations, titre, getActivity());
            }
        });

        alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        if (operations.size()!=0)alert.show();
        else Toast.makeText(getActivity(), R.string.anyop, Toast.LENGTH_LONG).show();
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

    static class ViewHolder {
        TextView numcpt;
        TextView numpiece;
        TextView mte1;
        TextView mte2;
        TextView date;
        TextView type;
        TextView sync;
        ImageView imageView = null;

        public ViewHolder(View v) {
            imageView = (ImageView) v.findViewById(R.id.imageview);
            date = (TextView) v.findViewById(R.id.date);
            mte1 = (TextView) v.findViewById(R.id.mte1);
            mte2 = (TextView) v.findViewById(R.id.mte2);
            numpiece = (TextView) v.findViewById(R.id.numpiece);
            numcpt = (TextView) v.findViewById(R.id.numcpt);
            type = (TextView) v.findViewById(R.id.type);
            sync = (TextView) v.findViewById(R.id.sync);
        }
    }

    public class ListeOperationAdapter extends BaseAdapter {

        ArrayList<Operation> operations = new ArrayList<Operation>();
        MembreDAO membreDAO = null;

        public ListeOperationAdapter(ArrayList<Operation> usg) {
            operations = usg;
            membreDAO = new MembreDAO(getActivity());
            if (operations.size() <= 0) {
                empty.setVisibility(View.VISIBLE);
            } else {
                empty.setVisibility(View.GONE);
            }
        }

        @Override
        public int getCount() {
            if (operations == null) return 0;
            return operations.size();
        }

        public Operation getItem(int position) {
            return operations.get(position);
        }

        @Override
        public long getItemId(int position) {
            return operations.get(position).getId();
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.operationitem, null);
                holder = new ViewHolder(convertView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Operation operation = (Operation) getItem(position);


            if (operation != null) {
                Membre membre = membreDAO.getOne(operation.getUser_id());

                if (operation.getTypeoperation() == 0) {
                    holder.imageView.setImageResource(R.mipmap.ic_depot);
                    holder.type.setText(R.string.depo);
                    holder.mte1.setText(Utiles.formatMtn(operation.getMontant()));
                    holder.mte2.setText("0");
                } else if (operation.getTypeoperation() == 1) {
                    holder.imageView.setImageResource(R.mipmap.ic_retrait);
                    holder.type.setText(R.string.retrait);
                    holder.mte2.setText(Utiles.formatMtn(operation.getMontant()));
                    holder.mte1.setText("0");
                } else if (operation.getTypeoperation() == 4) {
                    holder.imageView.setImageResource(R.mipmap.ic_credit);
                    holder.type.setText(R.string.rembour);
                    holder.mte2.setText(Utiles.formatMtn(operation.getMontant()));
                    holder.mte1.setText("0");
                } else {
                    holder.imageView.setImageResource(R.mipmap.ic_membre);
                    holder.type.setText(R.string.adh);
                    holder.mte1.setText(Utiles.formatMtn(operation.getMontant()));
                    holder.mte2.setText("0");
                }

                if (operation.getTypeoperation() < 3)
                    holder.date.setText(DAOBase.formatterj.format(operation.getDateoperation()));
                else if (operation.getTypeoperation() == 4) holder.date.setText(DAOBase.formatterj.format(operation.getDateoperation()));
                else holder.date.setText(DAOBase.formatterj.format(operation.getDateoperation()));

                if (operation.getTypeoperation() < 3)
                    holder.numcpt.setText("M: " + operation.getNumcompte());
                else if (operation.getTypeoperation() == 4)
                    holder.numcpt.setText("No: " + operation.getNumcompte());
                else if(membre!=null) holder.numcpt.setText(membre.getNom() + " " + membre.getPrenom());

                if (operation.getTypeoperation() < 3)
                    holder.numpiece.setText("P: " + operation.getNumpiece());
                else if (operation.getTypeoperation() == 4)
                    holder.numpiece.setText("P: " + operation.getNumpiece());
                else holder.numpiece.setText("");

                /*
                if (operation.getTypeoperation()<3) holder.mte.setText("Mte: "+Utiles.formatMtn( operation.getMontant()));
                else if (operation.getTypeoperation()==4) holder.mte.setText("Mte: "+Utiles.formatMtn( operation.getMontant()));
                else holder.mte.setText("Sexe: "+membre.getSexe());
                */

                Log.e("SYNC ETAT", String.valueOf(operation.getSync())) ;
                if (operation.getSync()==1) holder.sync.setVisibility(View.VISIBLE);
                else holder.sync.setVisibility(View.GONE);
            }
            ;

            return convertView;
        }

        public void addData(ArrayList<Operation> usg) {
            if (usg == null) operations = new ArrayList<Operation>();
            operations = usg;

            if (this.operations.size() <= 0) empty.setVisibility(View.VISIBLE);
            else empty.setVisibility(View.GONE);
            notifyDataSetChanged();
        }


        public void update(ArrayList<Operation> operations) {
            if (operations.size() == 0) return;
            this.operations = operations;

            if (this.operations.size() <= 0) empty.setVisibility(View.VISIBLE);
            else empty.setVisibility(View.GONE);
            notifyDataSetChanged();
        }


    }


}
