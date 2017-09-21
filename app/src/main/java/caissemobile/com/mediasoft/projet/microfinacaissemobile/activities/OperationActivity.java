package caissemobile.com.mediasoft.projet.microfinacaissemobile.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import caissemobile.com.mediasoft.projet.microfinacaissemobile.R;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Caissier;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Operation;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CaissierDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.DAOBase;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.OperationDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.fragments.OperationFragment;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.PrintPDA;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.PrintPDAMobiPrint3;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.PrinterUtils;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.Utiles;

public class OperationActivity extends AppCompatActivity implements OperationFragment.OnFragmentInteractionListener, ViewPager.OnPageChangeListener {

    OperationFragment operationFragment1 = null;
    OperationFragment operationFragment2 = null;
    OperationFragment operationFragment3 = null;
    OperationFragment operationFragment4 = null;
    OperationFragment operationFragment5 = null;
    OperationFragment operationFragment = null;
    public static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.FRENCH) ;



    private String bluetoothConfig = "imprimenteexterne";
    AlertDialog.Builder dateBox = null ;
    AlertDialog.Builder recapBox = null ;
    Button button = null ;
    private LayoutInflater mInflater= null ;
    private TabLayout tabLayout= null ;
    private ViewPager viewPager= null ;


    TextView depot = null ;
    TextView retrait = null ;
    TextView total = null ;
    TextView nbre = null ;
    TextView si1 = null ;
    TextView si2 = null ;
    final static int PROGRESS_DIALOG_ID = 0 ;

    private ProgressDialog mProgressBar;
    private int MAX_SIZE;
    private CollectionAdapter collectionAdapter;
    private int mPosition = 0 ;
    private AlertDialog alert;
    OperationDAO operationDAO = null ;
    private String dateFin = null;
    private String dateDebut = null ;
    CaissierDAO caissierDAO = null ;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation);


        init();
        setupToolbar();
        setupFragments();
        setupTablayout();

        refresh(mPosition) ;
    }

    private void refresh(int position) {
        DatePicker debut = new DatePicker(this) ;
        DatePicker fin = new DatePicker(this) ;
        try {
            if (dateDebut==null)   dateDebut = DAOBase.formatter2.format(DAOBase.formatterj.parse(preferences.getString("dateouvert", ""))) ;
            if (dateFin==null) dateFin = DAOBase.formatter2.format(DAOBase.formatterj.parse(preferences.getString("dateouvert", ""))) ;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ArrayList<Operation> operations = new ArrayList<Operation>() ;
        float mtn = 0 ;
        int nbr = 0;

        retrait.setText("0 F");
        depot.setText("0 F");

        if (position==0 || position==1){
            operations = operationDAO.getAll(1,java.sql.Date.valueOf(dateDebut), java.sql.Date.valueOf(dateFin));
            for (int i = 0; i<operations.size();++i){
                mtn += operations.get(i).getMontant() ;
            }
            retrait.setText(Utiles.formatMtn(mtn) + " F");
        }


        if (position==0||position==2){
            operations = operationDAO.getAll(2,java.sql.Date.valueOf(dateDebut), java.sql.Date.valueOf(dateFin));
            mtn = 0 ;
            for (int i = 0; i<operations.size();++i){
                mtn += operations.get(i).getMontant() ;
            }
            depot.setText(Utiles.formatMtn(mtn) + " F");
        }



        if (position==0||position==3){
            operations = operationDAO.getAll(3,java.sql.Date.valueOf(dateDebut), java.sql.Date.valueOf(dateFin));
            mtn = 0 ;
            for (int i = 0; i<operations.size();++i){
                mtn += operations.get(i).getMontant() ;
            }
            depot.setText(Utiles.formatMtn(mtn) + " F");
        }



        if (position==0||position==4){
            operations = operationDAO.getAll(4,java.sql.Date.valueOf(dateDebut), java.sql.Date.valueOf(dateFin));
            mtn = 0 ;
            for (int i = 0; i<operations.size();++i){
                mtn += operations.get(i).getMontant() ;
            }
            depot.setText(Utiles.formatMtn(mtn) + " F");
        }

        if (position==0){
            mtn = 0 ;
            operations = operationDAO.getAll(1,java.sql.Date.valueOf(dateDebut), java.sql.Date.valueOf(dateFin));
            for (int i = 0; i<operations.size();++i){
                mtn += operations.get(i).getMontant() ;
            }

            retrait.setText(Utiles.formatMtn(mtn));

            mtn = 0 ;
            operations = operationDAO.getAll(2,java.sql.Date.valueOf(dateDebut), java.sql.Date.valueOf(dateFin));
            for (int i = 0; i<operations.size();++i){
                mtn += operations.get(i).getMontant() ;
            }

            operations = operationDAO.getAll(3,java.sql.Date.valueOf(dateDebut), java.sql.Date.valueOf(dateFin));
            for (int i = 0; i<operations.size();++i){
                mtn += operations.get(i).getMontant() ;
            }

            operations = operationDAO.getAll(4,java.sql.Date.valueOf(dateDebut), java.sql.Date.valueOf(dateFin));
            for (int i = 0; i<operations.size();++i){
                mtn += operations.get(i).getMontant() ;
            }

            depot.setText(Utiles.formatMtn(mtn));


            operations = operationDAO.getAll(0, java.sql.Date.valueOf(dateDebut), java.sql.Date.valueOf(dateFin));

            for (int i = 0; i<operations.size();++i){
                mtn += operations.get(i).getMontant() ;
            }

            //total.setText(Utiles.formatMtn(mtn) + " F");
        }
        nbr = operations.size() ;
        //else total.setText("0 F");
        nbre.setText(String.valueOf(nbr));
    }


    private void init() {
        preferences = PreferenceManager.getDefaultSharedPreferences(OperationActivity.this) ;
        mInflater = LayoutInflater.from(this);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        depot = (TextView) findViewById(R.id.depot);
        retrait = (TextView) findViewById(R.id.retrait);
        //total = (TextView) findViewById(R.id.total);
        nbre = (TextView) findViewById(R.id.nbre);
        si1 = (TextView) findViewById(R.id.si1);
        si2 = (TextView) findViewById(R.id.si2);
        operationDAO = new OperationDAO(this) ;
        caissierDAO = new CaissierDAO(this) ;
        Caissier caissier = caissierDAO.getLast() ;

        si1.setText(Utiles.formatMtn(caissier.getSolde()) + " F");
        ArrayList<Operation> operations = operationDAO.getAll() ;
        float solde = caissier.getSolde();
        for (int j = 0; j < operations.size(); ++j) {
            if (operations.get(j).getTypeoperation() == 0 || operations.get(j).getTypeoperation()==3 || operations.get(j).getTypeoperation()==4) solde += operations.get(j).getMontant();
            else solde -= operations.get(j).getMontant();
        }

        si2.setText(Utiles.formatMtn(solde) + " F");
    }


    private void setupTablayout() {
        collectionAdapter = new CollectionAdapter(getSupportFragmentManager());
        viewPager.setAdapter(collectionAdapter);
        viewPager.addOnPageChangeListener(this);
        tabLayout.setupWithViewPager(viewPager);
    }


    private void setupFragments() {
        final FragmentManager fm = getSupportFragmentManager();
        //this.operationFragment = (OperationFragment) fm.findFragmentByTag(OperationFragment.TAG);
        if (this.operationFragment == null) {
            this.operationFragment = OperationFragment.newInstance(null,"0");
        }
        //this.operationFragment1 = (OperationFragment) fm.findFragmentByTag(OperationFragment.TAG);
        if (this.operationFragment1 == null) {
            this.operationFragment1 = OperationFragment.newInstance(null,"1");
        }
        //this.operationFragment2 = (OperationFragment) fm.findFragmentByTag(OperationFragment.TAG);
        if (this.operationFragment2 == null) {
            this.operationFragment2 = OperationFragment.newInstance(null,"2");
        }
        //this.operationFragment2 = (OperationFragment) fm.findFragmentByTag(OperationFragment.TAG);
        if (this.operationFragment3 == null) {
            this.operationFragment3 = OperationFragment.newInstance(null,"3");
        }
        //this.operationFragment2 = (OperationFragment) fm.findFragmentByTag(OperationFragment.TAG);
        if (this.operationFragment4 == null) {
            this.operationFragment4 = OperationFragment.newInstance(null,"4");
        }
        //this.operationFragment2 = (OperationFragment) fm.findFragmentByTag(OperationFragment.TAG);
        if (this.operationFragment5 == null) {
            this.operationFragment5 = OperationFragment.newInstance(null,"5");
        }
    }


    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Show menu icon
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_back);
        ab.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_operation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement

        if (id==android.R.id.home) finish();

        if (id == R.id.action_interval) {
            dateBox = new AlertDialog.Builder(this);
            ScrollView scrollView = (ScrollView) getLayoutInflater().inflate(R.layout.dialogbox,null);
            if (((ViewGroup)scrollView.getParent())!=null)((ViewGroup)scrollView.getParent()).removeAllViews();
            dateBox.setView(scrollView);
            dateBox.setTitle(getString(R.string.datechoice));

            final DatePicker debut = (DatePicker) scrollView.findViewById(R.id.dateDebut);
            final DatePicker fin = (DatePicker) scrollView.findViewById(R.id.dateFin);
            button = (Button) scrollView.findViewById(R.id.valider);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dateBox != null) {
                        dateDebut = String.valueOf(debut.getYear()) + "-" + String.valueOf(debut.getMonth() + 1) + "-" + String.valueOf(debut.getDayOfMonth());
                        dateFin = String.valueOf(fin.getYear()) + "-" + String.valueOf(fin.getMonth() + 1) + "-" + String.valueOf(fin.getDayOfMonth());
                        if (mPosition==0)operationFragment.interval(mPosition,dateDebut, dateFin);
                        else if (mPosition==1) operationFragment1.interval(mPosition,dateDebut,dateFin);
                        else if (mPosition==2)operationFragment2.interval(mPosition,dateDebut,dateFin);
                        else if (mPosition==3)operationFragment3.interval(mPosition,dateDebut,dateFin);
                        else if (mPosition==4)operationFragment4.interval(mPosition,dateDebut,dateFin);
                        else operationFragment5.interval(mPosition,dateDebut,dateFin);
                        dateBox = null;
                        alert.dismiss();
                        refresh(mPosition);
                    }
                }
            });
            alert = dateBox.show();
        } else if (id == R.id.action_recap) {

            recapBox = new AlertDialog.Builder(this);
            ScrollView scrollView = (ScrollView) getLayoutInflater().inflate(R.layout.recapbox,null);
            if (((ViewGroup)scrollView.getParent())!=null)((ViewGroup)scrollView.getParent()).removeAllViews();
            recapBox.setView(scrollView);

            final TextView nbr1 = (TextView) scrollView.findViewById(R.id.nbr1);
            final TextView nbr2 = (TextView) scrollView.findViewById(R.id.nbr2);
            final TextView nbr3 = (TextView) scrollView.findViewById(R.id.nbr3);
            final TextView nbr4 = (TextView) scrollView.findViewById(R.id.nbr4);

            final TextView mtn1 = (TextView) scrollView.findViewById(R.id.mtn1);
            final TextView mtn2 = (TextView) scrollView.findViewById(R.id.mtn2);
            final TextView mtn3 = (TextView) scrollView.findViewById(R.id.mtn3);
            final TextView mtn4 = (TextView) scrollView.findViewById(R.id.mtn4);

            final TextView interval = (TextView) scrollView.findViewById(R.id.interval);

            DatePicker debut = new DatePicker(this) ;
            DatePicker fin = new DatePicker(this) ;
            if (dateDebut==null)dateDebut = String.valueOf(debut.getYear()) + "-" + String.valueOf(debut.getMonth() + 1) + "-" + String.valueOf(debut.getDayOfMonth());
            if (dateFin==null)dateFin = String.valueOf(fin.getYear()) + "-" + String.valueOf(fin.getMonth() + 1) + "-" + String.valueOf(fin.getDayOfMonth());

            recapBox.setTitle(getString(R.string.recap) + " du ");
            interval.setText(DAOBase.formatterj.format(java.sql.Date.valueOf(dateDebut)) + " au " + DAOBase.formatterj.format(java.sql.Date.valueOf(dateFin)));

            ArrayList<Operation> operations = operationDAO.getAll(2,java.sql.Date.valueOf(dateDebut), java.sql.Date.valueOf(dateFin));

            float mtn = 0 ;
            for (int i = 0; i<operations.size();++i){
                mtn += operations.get(i).getMontant() ;
            }

            nbr1.setText(String.valueOf(operations.size()));
            mtn1.setText(Utiles.formatMtn(mtn) + " F");

            operations = operationDAO.getAll(1,java.sql.Date.valueOf(dateDebut), java.sql.Date.valueOf(dateFin));
            mtn = 0 ;
            for (int i = 0; i<operations.size();++i){
                mtn += operations.get(i).getMontant() ;
            }

            nbr2.setText(String.valueOf(operations.size()));
            mtn2.setText(Utiles.formatMtn(mtn) + " F");

            operations = operationDAO.getAll(3,java.sql.Date.valueOf(dateDebut), java.sql.Date.valueOf(dateFin));
            mtn = 0 ;
            for (int i = 0; i<operations.size();++i){
                mtn += operations.get(i).getMontant() ;
            }

            nbr3.setText(String.valueOf(operations.size()));
            mtn3.setText(Utiles.formatMtn(mtn) + " F");

            operations = operationDAO.getAll(4,java.sql.Date.valueOf(dateDebut), java.sql.Date.valueOf(dateFin));
            mtn = 0 ;
            for (int i = 0; i<operations.size();++i){
                mtn += operations.get(i).getMontant() ;
            }

            nbr4.setText(String.valueOf(operations.size()));
            mtn4.setText(Utiles.formatMtn(mtn) + " F");



            button = (Button) scrollView.findViewById(R.id.close);
            Button impBtn = (Button) scrollView.findViewById(R.id.imp);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alert.dismiss();
                }
            });
            impBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean imp = preferences.getBoolean(bluetoothConfig, false) ;
                    if (imp){
                        try {
                            PrinterUtils printerUtils = new PrinterUtils(OperationActivity.this) ;
                            printerUtils.printTicket(dateDebut,dateFin);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        try {
                            //PrintPDA printPDA = new PrintPDA(OperationActivity.this) ;
                            //printPDA.printTicket(dateDebut,dateFin);

                            PrintPDAMobiPrint3 printPDAMobiPrint3 = new PrintPDAMobiPrint3(OperationActivity.this);
                            printPDAMobiPrint3.printTicket(dateDebut,dateFin);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });


            alert = recapBox.show();
        }

        else if (id == R.id.action_imp){
            final CharSequence[] items = { getString(R.string.pdf), getString(R.string.xls), getString(R.string.fermer) };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //builder.setTitle(getString(R.string.action));
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {

                    if (items[item].equals(getString(R.string.pdf))) {
                        if (mPosition == 0) operationFragment.imprimePDFDoc("opérations");
                        else if (mPosition == 1)
                            operationFragment1.imprimePDFDoc("retrait");
                        else if (mPosition == 2)
                            operationFragment2.imprimePDFDoc("dépot");
                        else if (mPosition == 3)
                            operationFragment3.imprimePDFDoc("adhésion");
                        else if (mPosition == 4)
                            operationFragment4.imprimePDFDoc("remboursement");
                        else
                            operationFragment5.imprimePDFDoc("archiver");
                    } else if (items[item].equals(getString(R.string.xls))) {
                        if (mPosition == 0)
                            operationFragment.imprimeExcelDoc("opérations");
                        else if (mPosition == 1)
                            operationFragment1.imprimeExcelDoc("retrait");
                        else if (mPosition == 2)
                            operationFragment2.imprimeExcelDoc("dépot");
                        else if (mPosition == 3)
                            operationFragment3.imprimeExcelDoc("adhésion");
                        else if (mPosition == 4)
                            operationFragment4.imprimeExcelDoc("remboursement");
                        else
                            operationFragment5.imprimeExcelDoc("archiver");
                    } else {
                        dialog.dismiss();
                    }
                }
            });
            AlertDialog alertDialog = builder.show();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        super.onCreateDialog(id);
        if (mProgressBar == null) {
            mProgressBar = new ProgressDialog(this);
            mProgressBar.setCancelable(false);
            mProgressBar.setTitle(getString(R.string.send_loding));
            mProgressBar.setMessage(getString(R.string.wait));
            mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressBar.setMax(MAX_SIZE);
        }

        return mProgressBar;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     *
     * @param position             Position index of the first page currently being displayed.
     *                             Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset       Value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     *
     * @param position Position index of the new selected page.
     */
    @Override
    public void onPageSelected(int position) {
        mPosition = position ;
        refresh(mPosition);
        if (mPosition==0)operationFragment.interval(mPosition,dateDebut, dateFin);
        else if (mPosition==1) operationFragment1.interval(mPosition,dateDebut,dateFin);
        else if (mPosition==2)operationFragment2.interval(mPosition,dateDebut,dateFin);
        else if (mPosition==3)operationFragment3.interval(mPosition,dateDebut,dateFin);
        else if (mPosition==4)operationFragment4.interval(mPosition,dateDebut,dateFin);
        else operationFragment5.interval(mPosition,dateDebut,dateFin);
    }

    /**
     * Called when the scroll state changes. Useful for discovering when the user
     * begins dragging, when the pager is automatically settling to the current page,
     * or when it is fully stopped/idle.
     *
     * @param state The new scroll state.
     * @see ViewPager#SCROLL_STATE_IDLE
     * @see ViewPager#SCROLL_STATE_DRAGGING
     * @see ViewPager#SCROLL_STATE_SETTLING
     */
    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private class  CollectionAdapter extends FragmentPagerAdapter {
        int pages = 6 ;

        public CollectionAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0 : {
                    return operationFragment ;
                }
                case 1 : {
                    return operationFragment1 ;
                }
                case 2 : {
                    return operationFragment2 ;
                }
                case 3 : {
                    return operationFragment3 ;
                }
                case 4 : {
                    return operationFragment4 ;
                }
                case 5 : {
                    return operationFragment5 ;
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return pages;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0 : {
                    return getString(R.string.tout) ;
                }
                case 1 : {
                    return getString(R.string.rt) ;
                }
                case 2 : {
                    return getString(R.string.vse) ;
                }
                case 3 : {
                    return getString(R.string.adhesion) ;
                }
                case 4 : {
                    return getString(R.string.rembr) ;
                }
                case 5 : {
                    return getString(R.string.archives) ;
                }
            }
            return super.getPageTitle(position);
        }
    }


}


