package caissemobile.com.mediasoft.projet.microfinacaissemobile;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import caissemobile.com.mediasoft.projet.microfinacaissemobile.activities.BilletageActivity;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.activities.ConnexionActivity;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.activities.CreditActivity;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.activities.DemandeCreditActivity;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.activities.ListeMembreActivity;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.activities.OperationActivity;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.activities.ParametreActivity;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Caissier;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Membre;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Operation;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.OperationSec;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CaissierDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CategorieDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CompteDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.DAOBase;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.MembreDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.OperationDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.OperationSecDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.database.MembreHelper;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.fragments.AdhesionFragment;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.fragments.AdhesionGroupeFragment;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.fragments.DemandeCreditFragment;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.fragments.EpargneFragment;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.fragments.RembroussementFragment;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.fragments.TontineFragment;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.Url;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.Utiles;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity implements AdhesionFragment.OnFragmentInteractionListener,  AdhesionGroupeFragment.OnFragmentInteractionListener, RembroussementFragment.OnFragmentInteractionListener, EpargneFragment.OnFragmentInteractionListener, TontineFragment.OnFragmentInteractionListener{

    private static final int INSCRIPTION_CODE = 0;
    private static final String POSITION = "position";
    private static final int REQUEST_CODE = 1;
    private static final String ACTIF = "actif";
    public static final String COMPTE = "compte";
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView = null;
    AppBarLayout appBarLayout = null ;
    private CollectionPagerAdapter mCollectionPagerAdapter ;
    public final static int PROGRESS_DIALOG_ID = 0 ;
    private ProgressDialog mProgressBar;
    private int MAX_SIZE;
    private SharedPreferences preferences = null ;
    CaissierDAO caissierDAO = null ;

    MsgUpdateReceiver msgUpdateReceiver = null ;

    public static int active = 0 ;

    long temps = 0 ;

    private int mPosition = 0 ;
    private EpargneFragment epargneFragment = null ;
    private TontineFragment tontineFragment = null;
    private AdhesionFragment adhesionFragment = null;
    private AdhesionGroupeFragment adhesionGroupeFragment = null;
    private RembroussementFragment rembroussementFragment = null;
    private DemandeCreditFragment demandecreditFragment = null;

    boolean actif = false ;
    private String msg = null ;
    private OperationDAO operationDAO;


    private static final String AUTHORITY_OPERATION = "caissemobile.com.mediasoft.projet.microfinacaissemobile.Operation";
    private static final String AUTHORITY_CAISSE = "caissemobile.com.mediasoft.projet.microfinacaissemobile.Caisse";
    private static final int MINUTE = 60;
    public static String ACCOUNT_TYPE = "";
    Account mAccount;
    // The account name
    public static final String ACCOUNT = "Microfina caisse mobile account";
    private ContentResolver mResolver;

    public static final long SECONDS_PER_MINUTE = 60L;
    public static final long SYNC_INTERVAL_IN_MINUTES = 2L;
    public static final long SYNC_INTERVAL = SYNC_INTERVAL_IN_MINUTES * SECONDS_PER_MINUTE;
    private int etape = 0;
    private CategorieDAO categorieDAO;
    private OperationSecDAO operationSecDAO;
    private CompteDAO compteDAO;
    private MembreDAO membreDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialisation();
        setupToolBar();
        setupFragments();
        setupNavigation();


        if (savedInstanceState!=null) actif = savedInstanceState.getBoolean(ACTIF) ;

       /* Intent intent = new Intent(MainActivity.this,MainActivity.class) ;
        if (!actif) {
            startActivityForResult(intent, REQUEST_CODE);
        }
        else   */
        actif = true ;
        ArrayList<Operation> operations = operationDAO.getAll() ;
        float solde = caissierDAO.getLast().getSolde() ;
        for (int i = 0; i<operations.size();++i){
            if (operations.get(i).getTypeoperation() == 0 || operations.get(i).getTypeoperation()==3 || operations.get(i).getTypeoperation()==4) solde += operations.get(i).getMontant();
            else solde -= operations.get(i).getMontant();
        }
        setTitle(getString(R.string.solde1) + Utiles.formatMtn(solde));
        lanceSyncAdapter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ACTIF, actif);
    }




    private void setupNavigation() {

        mCollectionPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager()) ;
        mViewPager.setAdapter(mCollectionPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mNavigationView = (NavigationView) findViewById(R.id.navigation);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id) {
                    case R.id.menu_operation: {
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        Intent intent = null;
                        intent = new Intent(MainActivity.this, OperationActivity.class);
                        startActivity(intent);
                    }
                    break;
                    case R.id.menu_billetage: {
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        Intent intent = null;
                        intent = new Intent(MainActivity.this, BilletageActivity.class);
                        startActivity(intent);
                    }
                    break;
                    case R.id.menu_changepass: {
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        changePassword() ;
                    }
                    break;
                    case R.id.menu_parametre: {
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        final EditText editText = new EditText(MainActivity.this);
                        editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        editText.setSingleLine(true);
                        builder.setView(editText);
                        builder.setTitle(getString(R.string.password));
                        builder.setPositiveButton(getString(R.string.valider), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String code = editText.getText().toString();

                                Intent intent = null;
                                intent = new Intent(MainActivity.this, ParametreActivity.class);
                                if (code.equals(preferences.getString("adminpass","admin")))startActivity(intent);
                            }
                        });
                        builder.setNegativeButton(getString(R.string.annuler), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        final AlertDialog alert = builder.create();
                        alert.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface arg0) {
                                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.my_accent));
                                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.my_accent));
                            }
                        });
                        alert.show();

                    };break ;
                    case R.id.menu_transfert: {

                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        if (!Utiles.isConnected(MainActivity.this)) {
                            Toast.makeText(MainActivity.this, R.string.anyconnexion, Toast.LENGTH_SHORT).show();
                            return true;
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        final EditText editText = new EditText(MainActivity.this);
                        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        builder.setView(editText);
                        builder.setTitle(getString(R.string.transfert));
                        builder.setMessage(getString(R.string.transfertmsg));
                        builder.setPositiveButton(getString(R.string.valider), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Caissier caissier = caissierDAO.getLast();
                                if (caissier==null) {
                                    Toast.makeText(MainActivity.this, R.string.anycaisse, Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                String pass = editText.getText().toString();
                                if (caissier.getPassword().equals(pass)) {
                                    etape = 1 ;
                                    transfert() ;
                                }
                                else  Toast.makeText(MainActivity.this, getString(R.string.passincorrect), Toast.LENGTH_LONG).show();

                            }
                        });
                        builder.setNegativeButton(getString(R.string.annuler), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

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
                    break;
                }
                return false;
            }
        });


    }


    private void changePassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this) ;
        ScrollView scrollView = (ScrollView) getLayoutInflater().inflate(R.layout.changepass,null);

        final EditText password = (EditText) scrollView.findViewById(R.id.password);
        final EditText password1 = (EditText) scrollView.findViewById(R.id.password1);
        final EditText confirmed = (EditText) scrollView.findViewById(R.id.confirmed);
        Button valider = (Button) scrollView.findViewById(R.id.valider);
        Button annuler = (Button) scrollView.findViewById(R.id.annuler);


        final SharedPreferences preferences = (SharedPreferences) PreferenceManager.getDefaultSharedPreferences(this);

        builder.setTitle(R.string.app_name) ;
        builder.setView(scrollView) ;
        final AlertDialog alert = builder.show();;

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().toString().equals(preferences.getString("adminpass","admin"))) {
                    if (password1.getText().toString().equals(confirmed.getText().toString())){
                        if (confirmed.getText().toString().length()>=6) {
                            SharedPreferences.Editor editor = preferences.edit() ;
                            editor.putString("adminpass",password1.getText().toString()) ;
                            editor.commit() ;
                            Toast.makeText(MainActivity.this, R.string.passwordmodifier, Toast.LENGTH_SHORT).show();
                            alert.dismiss();
                        }
                        else Toast.makeText(MainActivity.this, R.string.tropcourt, Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(MainActivity.this, R.string.passpareil, Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(MainActivity.this, R.string.passincorrect, Toast.LENGTH_SHORT).show();
            }
        });

        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        builder.setTitle(R.string.app_name) ;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }

        if (id == R.id.menu_operation) {
            Intent intent = null;
            intent = new Intent(MainActivity.this, OperationActivity.class);
            startActivity(intent);
        }
/*
        if (id == R.id.menu_credit) {
            Intent intent = null;
            intent = new Intent(MainActivity.this, DemandeCreditActivity.class);
            startActivity(intent);
        }
        */

        if (id == R.id.action_liste_membre) {
            Intent intent = null;
            intent = new Intent(MainActivity.this, ListeMembreActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE && resultCode==RESULT_OK){
            actif = true ;
        }
        else{
            //finish();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        active = 1 ;
        msgUpdateReceiver = new MsgUpdateReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        active = 0 ;
        //unregisterReceiver(msgUpdateReceiver);
    }

    private void initialisation() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drLayout) ;
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        caissierDAO = new CaissierDAO(this);
        operationDAO = new OperationDAO(this) ;
        membreDAO = new MembreDAO(this) ;
        operationSecDAO = new OperationSecDAO(this) ;
    }



    private void setupFragments() {
        final FragmentManager fm = getSupportFragmentManager();
        this.epargneFragment = (EpargneFragment) fm.findFragmentByTag(EpargneFragment.TAG);
        if (this.epargneFragment == null) {
            this.epargneFragment = new EpargneFragment();
        }
        this.tontineFragment = (TontineFragment) fm.findFragmentByTag(TontineFragment.TAG);
        if (this.tontineFragment == null) {
            this.tontineFragment = new TontineFragment();
        }
        this.adhesionFragment = (AdhesionFragment) fm.findFragmentByTag(AdhesionFragment.TAG);
        if (this.adhesionFragment == null) {
            this.adhesionFragment = new AdhesionFragment();
        }
        this.adhesionGroupeFragment = (AdhesionGroupeFragment) fm.findFragmentByTag(AdhesionFragment.TAG);
        if (this.adhesionGroupeFragment == null) {
            this.adhesionGroupeFragment = new AdhesionGroupeFragment();
        }
        this.rembroussementFragment = (RembroussementFragment) fm.findFragmentByTag(RembroussementFragment.TAG);
        if (this.rembroussementFragment == null) {
            this.rembroussementFragment = new RembroussementFragment();
        }

        this.demandecreditFragment = (DemandeCreditFragment) fm.findFragmentByTag(DemandeCreditFragment.TAG);
        if (this.demandecreditFragment == null) {
            this.demandecreditFragment = new DemandeCreditFragment();
        }

    }





    private void setupToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        // Show menu icon
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.mipmap.menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void selectAdhesionTab(int pos) {
        mPosition = pos ;

        mViewPager.setCurrentItem(mPosition);
        TabLayout.Tab tab = mTabLayout.getTabAt(mPosition);
        tab.select();
    }

    /*
    public void selectTontineFragment() {
        mPosition = 1 ;

        mViewPager.setCurrentItem(mPosition);
        TabLayout.Tab tab = mTabLayout.getTabAt(mPosition);
        tab.select();
    }
    */
    public void selectCaisseFragment() {
        mPosition = 0 ;

        mViewPager.setCurrentItem(mPosition);
        TabLayout.Tab tab = mTabLayout.getTabAt(mPosition);
        tab.select();
    }
    public void selectRembroussementFragment() {
        mPosition = 3 ;

        mViewPager.setCurrentItem(mPosition);
        TabLayout.Tab tab = mTabLayout.getTabAt(mPosition);
        tab.select();
    }


    private class CollectionPagerAdapter extends FragmentPagerAdapter {

        int nbrPage = 4 ;

        public CollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0 : {
                    return epargneFragment;
                }
                /*
                case 1 : {
                    return tontineFragment  ;
                } */
                case 1 : {
                    return adhesionFragment  ;
                }
                case 2 : {
                    return adhesionGroupeFragment  ;
                }
                case 3 : {
                    return rembroussementFragment  ;
                }
                case 4 : {
                    return demandecreditFragment  ;
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return nbrPage;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0 : {
                    //return getItem(position).getTitle() ;
                    return "EPARGNE" ;
                }
                /*
                case 1 : {
                    //return getItem(position).getTitle() ;
                    return "TONTINE" ;
                }
                */
                case 1 : {
                    //return getItem(position).getTitle() ;
                    return "ADHESION" ;
                }
                case 2 : {
                    //return getItem(position).getTitle() ;
                    return "GROUPE - ADHESION" ;
                }
                case 3 : {
                    //return getItem(position).getTitle() ;
                    return "REMBOURSEMENT" ;
                }
                case 4 : {
                    //return getItem(position).getTitle() ;
                    return "DEMANDE DE CREDIT" ;
                }
            }
            return null;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true ;
            }


            long i = Calendar.getInstance().getTimeInMillis() ;
            if (i-temps > 3000){
                temps = i ;
                Toast.makeText(this, R.string.clikagain, Toast.LENGTH_SHORT).show();
                return true ;
            }

        }
        return super.onKeyDown(keyCode, event);
    }


    public void sendSMS(String msg,int i) {
        SmsManager manager = SmsManager.getDefault();
        String numero = Url.getSmsNumber(this) ;
        switch (i){
            case 0 : manager.sendTextMessage(numero, null, "cmi " + msg + ":", null, null); break;
            case 1 : manager.sendTextMessage(numero, null, "cmo " + msg + ":", null, null); break;
            case 2 : manager.sendTextMessage(numero, null, "cma " + msg + ":", null, null); break;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this) ;
        builder.setMessage(getString(R.string.msgsentt)) ;
        builder.setTitle(getString(R.string.msgsent)) ;
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Utiles.deleteAllMsg(MainActivity.this);
            }
        }) ;
        builder.show() ;
    }


    private class MsgUpdateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //processResult(intent);
        }
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        super.onCreateDialog(id);

        if (mProgressBar == null){
            mProgressBar = new ProgressDialog(this) ;
            mProgressBar.setCancelable(false);
            mProgressBar.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                                 @Override
                                                 public void onCancel(DialogInterface dialog) {
                                                     try {
                                                         if (epargneFragment.loadCLientInfoTask!=null)
                                                             epargneFragment.loadCLientInfoTask.cancel(true) ;
                                                         if (tontineFragment.loadCLientInfoTask!=null)tontineFragment.loadCLientInfoTask.cancel(true) ;
                                                         if (adhesionFragment.sendMembreTask!=null)adhesionFragment.sendMembreTask.cancel(true) ;
                                                         if (adhesionGroupeFragment.sendMembreTask!=null)adhesionGroupeFragment.sendMembreTask.cancel(true) ;
                                                     }catch(Exception e){
                                                         e.printStackTrace();
                                                     }
                                                 }
                                             }

            );
            mProgressBar.setTitle(

                    getString(R.string.sending)

            );
            mProgressBar.setMessage(

                    getString(R.string.wait)

            );
            mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressBar.setMax(MAX_SIZE);
        }

        return mProgressBar ;
    }



    private void lanceSyncAdapter(Context context) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context) ;

        if (caissierDAO.getLast()==null) return;
        ACCOUNT_TYPE = context.getString(R.string.accounttype) ;
        // Get the content resolver for your app
        mResolver = context.getContentResolver();
        /*
         * Turn on periodic syncing
         */

        mAccount = CreateSyncAccount(context);
        // Time en second



        // Pass the settings flags by inserting them in a bundle
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

        ContentResolver.setSyncAutomatically(mAccount,AUTHORITY_OPERATION, true);
        ContentResolver.addPeriodicSync(mAccount,AUTHORITY_OPERATION, Bundle.EMPTY, SECONDS_PER_MINUTE);

        ContentResolver.setSyncAutomatically(mAccount,AUTHORITY_CAISSE, true);
        ContentResolver.addPeriodicSync(mAccount,AUTHORITY_CAISSE, Bundle.EMPTY, SECONDS_PER_MINUTE*30);

        /*
         * Request the sync for the default account, authority, and
         * manual sync settings
         */
        Log.e("SYNC ADAPTER","LANCE") ;
        ContentResolver.requestSync(mAccount, AUTHORITY_OPERATION, settingsBundle);
        //ContentResolver.requestSync(mAccount, AUTHORITY_CAISSE, settingsBundle);
    }



    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(context.ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        }

        return newAccount ;
    }




    private void transfert() {

        try {
            dismissDialog(PROGRESS_DIALOG_ID);
        }
        catch (Exception e){
            e.getMessage() ;
        }

        int sync = getSyncData() ;
        if (sync==0 && etape==1) {
            makeAlertDialog(getString(R.string.nosyncdata),false);
            return;
        }

        if (!Utiles.isConnected(this)){
            try {
                dismissDialog(PROGRESS_DIALOG_ID);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            Toast.makeText(MainActivity.this, R.string.noconnexion, Toast.LENGTH_SHORT).show();
            return;
        }

        if (operationDAO.getNonSync().size() != 0 && etape == 1){
            Log.e("SYNC","OPERATION LANCE") ;
            SendOperationTask sendOperationTask = new SendOperationTask();
            sendOperationTask.execute(Url.getSendOperationUrl(MainActivity.this));
            return;
        }
        else if (etape==1)etape = 2 ;


        if (operationDAO.getNonSync().size() != 0 && etape == 2){
            Log.e("SYNC","PARTENAIRE LANCE") ;
            SendMembreTask sendMembreTask = new SendMembreTask() ;
            sendMembreTask.execute(Url.getAddMembreUrl(MainActivity.this)) ;
            return;
        }
        else if (etape==2) etape = 3 ;


        if (membreDAO.getNonPhotoSync().size() != 0 && etape == 3){
            Log.e("SYNC","PHOTO LANCE") ;
            UpdateImageTask updateImageTask = new UpdateImageTask() ;
            updateImageTask.execute() ;
            return;
        }
        else if (etape==3) etape = 4 ;

        AlertDialog.Builder builder = new AlertDialog.Builder(this) ;
        builder.setMessage(getString(R.string.synctotal)) ;
        builder.setTitle(R.string.app_name) ;
        builder.setPositiveButton(getString(R.string.retry), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                etape = 1 ;
                transfert();
            }
        }) ;
        builder.setNegativeButton(R.string.annuler, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }) ;

        sync = getSyncData() ;

        if (sync>0) {
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
        else {
            etape=1 ;
            makeAlertDialog(getString(R.string.trsuccess),false);
        }
    }

    private int getSyncData() {
        return  operationDAO.getNonSync().size();
    }


    private void makeAlertDialog(String string, final boolean delete) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this) ;
        builder.setTitle(R.string.app_name) ;
        builder.setMessage(string) ;
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (delete){
                    operationDAO.clean() ;
                    operationSecDAO.clean() ;
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



    public class SendOperationTask extends AsyncTask<String,Void,String> {

        OperationDAO operationDAO = null ;
        OperationSecDAO operationSecDAO = null ;
        Caissier c  =null;
        private ArrayList<Operation> operations;
        Operation operation ;
        private String result;

        public SendOperationTask() {
            Log.e("DEBUG","ETAT") ;
        }

        @Override
        protected String doInBackground(String... urls) {
            //TelephonyManager telephonyManager =  ( TelephonyManager ) getSystemService (Context.TELEPHONY_SERVICE);
            //String imei = telephonyManager.getDeviceId ();


            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this) ;

            Log.e("DEBUG","ETAT") ;
            operations = operationDAO.getNonSync();
            String typop = "" ;
            Log.e("DEBUG", String.valueOf(operations.size())) ;

            Date date = new Date() ;
            try {
                date = DAOBase.formatterj.parse(preferences.getString(ConnexionActivity.JOURNEE, "")) ;
            } catch (ParseException e) {
                e.printStackTrace();
            }




            WifiManager wifiManager = null;
            wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wInfo = wifiManager.getConnectionInfo();
            String macAddress ="";
            if(wInfo!=null)macAddress = wInfo.getMacAddress();

            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String iMei = null ;
            iMei = telephonyManager.getDeviceId();
            try {
                Log.d("IMEI", iMei);
                Log.d("MACADRESS", macAddress);
            }catch (Exception e){
                e.printStackTrace();
            }

            for (int j = 0;j<operations.size();++j){
                operation = operations.get(j) ;
                if (operation.getTypeoperation()>=3) continue;
                FormBody.Builder formBuilder = new FormBody.Builder();

                formBuilder.add("iMei", iMei) ;
                formBuilder.add("macAddress", macAddress);
                formBuilder.add("NumCompte",operation.getNumcompte()) ;
                formBuilder.add("interface","E") ;
                formBuilder.add("NumProduit",operation.getNumproduit()) ;
                formBuilder.add("journee", preferences.getString("dateouvert", "")) ;
                formBuilder.add("montant1",Utiles.formatMtn2(operation.getMontant())) ;
                formBuilder.add("montant2",Utiles.formatMtn2(operation.getMontant())) ;
                if (operation.getTypeoperation()==0) formBuilder.add("TypeOperation", "VSE") ;
                else formBuilder.add("TypeOperation", "RE") ;

                formBuilder.add("mise", "0") ;
                formBuilder.add("AgenceClient", operation.getAgence()) ;
                formBuilder.add("numagence", String.valueOf(c.getAgence_id())) ;
                formBuilder.add("ddb", c.getDdb()) ;
                formBuilder.add("dl", c.getDl()) ;
                formBuilder.add("dp", c.getDp()) ;
                formBuilder.add("di", c.getDi()) ;


                ArrayList<OperationSec> operationSecs = operationSecDAO.getAllByNumOperation(operation.getId());;
                int n = operationSecs.size();
                Log.e("NBRE SOUS OP", String.valueOf(n)) ;
                formBuilder.add("nbre", String.valueOf(n)) ;
                if (n > 0)
                    for (int i = 0; i < n; i++) {
                        if (operationSecs.get(i).getMte() <= 0) continue;
                        formBuilder.add("nummembre" + i, String.valueOf(operationSecs.get(i).getNummembre())) ;
                        formBuilder.add("mte" + i, Utiles.formatMtn2(operationSecs.get(i).getMte())) ;
                        formBuilder.add("idpersonne" + i, operationSecs.get(i).getIdpersonne()) ;
                    }

                try {
                    result = Utiles.POST(Url.getSendOperationUrl(MainActivity.this),formBuilder.build());
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Log.e("RESULT OPERATION", result) ;


                if (result.equals("NUMPIECE")) Log.e("RESULT",getString(R.string.echec1));
                if (result.equals("AGENCEDIFF")) Log.e("RESULT",getString(R.string.echec));
                if (result.equals("NUMPECEECHEC1")) Log.e("RESULT",getString(R.string.echec2));
                if (result.equals("NUMPECEECHEC2")) Log.e("RESULT",getString(R.string.echec3));
                if (result.equals("NUMPECEECHEC3")) Log.e("RESULT", getString(R.string.echec4));
                else if (result.contains("OK:") && result.split(":").length>=3){
                    operation.setNumpicedef(result.split(":")[1]);
                    operation.setSync(1);
                    operationDAO.update(operation) ;
                }
            }

            return "FINISH" ;
        }


        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            super.onPostExecute(result);
            Log.e("DEBUG", result) ;
            etape = 2 ;
            transfert() ;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (operationDAO==null) operationDAO = new OperationDAO(MainActivity.this) ;
            if (operationSecDAO==null) operationSecDAO = new OperationSecDAO(MainActivity.this) ;
            if (compteDAO==null) compteDAO = new CompteDAO(MainActivity.this) ;

            showDialog(PROGRESS_DIALOG_ID);
            c = new CaissierDAO(MainActivity.this).getLast() ;
            Log.e("DEBUG","PRE EXECUTE") ;
        }
    }


    public class SendMembreTask extends AsyncTask<String,Void,String> {

        Membre membre = null ;
        MembreDAO membreDAO = null ;

        OperationDAO operationDAO = null ;
        private ArrayList<Operation> operations;
        Operation operation ;
        private String result;

        CaissierDAO caissierDAO = null ;
        Caissier c  =null;

        public SendMembreTask() {

        }

        @Override
        protected String doInBackground(String... urls) {
            //TelephonyManager telephonyManager =  ( TelephonyManager ) getSystemService (Context.TELEPHONY_SERVICE);
            //String imei = telephonyManager.getDeviceId ();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this) ;

            operations = operationDAO.getNonSync();

            String typop = "" ;


            Log.e("SIZE MEMBRE", String.valueOf(operations.size())) ;
            for (int j = 0;j<operations.size();++j) {
                operation = operations.get(j);
                if (operation.getTypeoperation()!=3) continue;
                membre = membreDAO.getOne(operation.getUser_id()) ;

                String tel = membre.getTel() ;

                if (!tel.startsWith("00229") && !tel.startsWith("+229")) tel =  "+229"+ tel ;
                if (tel.startsWith("00229")) tel = tel.replace("00229","+229") ;


                WifiManager wifiManager = null;
                wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo wInfo = wifiManager.getConnectionInfo();
                String macAddress ="";
                if(wInfo!=null)macAddress = wInfo.getMacAddress();

                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String iMei = null ;
                iMei = telephonyManager.getDeviceId();
                try {
                    Log.d("IMEI", iMei);
                    Log.d("MACADRESS", macAddress);
                }catch (Exception e){
                    e.printStackTrace();
                }

                FormBody.Builder formBuilder = new FormBody.Builder();

                formBuilder.add("guichet",String.valueOf(c.getCodeguichet())) ;
                formBuilder.add("iMei", iMei) ;
                formBuilder.add("macAddress", macAddress);
                formBuilder.add("numproduit",operation.getNumproduit()) ;
                formBuilder.add("journee", preferences.getString("dateouvert", "")) ;
                formBuilder.add("Mt_Operation", String.valueOf(membre.getMontant())) ;
                formBuilder.add("NumCategorie",String.valueOf(categorieDAO.getOne(membre.getCategorie()).getNumCategorie())) ;
                formBuilder.add("NumProfession",String.valueOf(membre.getProfession())) ;
                formBuilder.add("NumNationalite",String.valueOf(membre.getNationalite())) ;
                formBuilder.add(MembreHelper.NOM, membre.getNom()) ;
                formBuilder.add(MembreHelper.PRENOM, membre.getPrenom()) ;
                formBuilder.add(MembreHelper.SEXE, membre.getSexe()) ;
                formBuilder.add(MembreHelper.TEL, membre.getTel()) ;
                formBuilder.add(MembreHelper.ADRESSE, membre.getAdresse()) ;
                formBuilder.add(MembreHelper.DATE, DAOBase.formatterj.format(membre.getDateadhesion())) ;
                formBuilder.add(MembreHelper.DATENAISS, DAOBase.formatterj.format(membre.getDateNaiss())) ;
                formBuilder.add("NumZone",membre.getZone()) ;
                formBuilder.add("codeGuichet", String.valueOf(c.getCodeguichet())) ;
                formBuilder.add("lat","0") ;
                formBuilder.add("long","0") ;

                ArrayList<Membre> membreArrayList = membreDAO.getAllByNumMembre(String.valueOf(membre.getId()));

                Log.e("NOMBRE DE MEMBRE",String.valueOf(membreArrayList.size())) ;

                formBuilder.add("nbre",String.valueOf(membreArrayList.size())) ;

                for (int i = 0; i < membreArrayList.size(); i++) {
                    formBuilder.add("nummembre"+i, membreArrayList.get(i).getNummembre());
                    formBuilder.add("nom"+i, membreArrayList.get(i).getNom());
                    formBuilder.add("prenom"+i, membreArrayList.get(i).getPrenom());
                    formBuilder.add("poste"+i, membreArrayList.get(i).getPoste());
                    formBuilder.add("activite"+i, membreArrayList.get(i).getActivite());
                    formBuilder.add("sexe"+i, membreArrayList.get(i).getSexe());
                    formBuilder.add("datenaiss"+i, DAOBase.formatterj.format(membreArrayList.get(i).getDateNaiss()));
                    formBuilder.add("tel"+i, membreArrayList.get(i).getTel());
                    formBuilder.add("adresse"+i, membreArrayList.get(i).getAdresse());
                }

                String url = "" ;

                if (membreArrayList.size()>0) url = Url.getAddMembreGroupeUrl(MainActivity.this);
                else url = Url.getAddMembreUrl(MainActivity.this);


                try {
                    result = Utiles.POST(url,formBuilder.build());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.e("RESULT MEMBRE",result) ;
                if (membre.getPhoto()!=null) Log.e("RESULT MEMBRE", membre.getPhoto()) ;

                Log.e("RESULT MEMBRE",result) ;

                if (result.contains("OK:") && result.split(":").length>=3) {
                    membre.setNummembre(result.split(":")[2]);
                    membreDAO.update(membre) ;
                    operation.setSync(1);
                    operation.setNumpicedef(result.split(":")[1]);
                    operationDAO.update(operation) ;
                    String coderetrait = result.split(":")[3] ;
                    String msg = "ALIDE : Bienvenue au service mobile money d'ALIDE . Votre inscription est active et le code secret est : " +
                            coderetrait + "\nMerci" ;


                    Log.e("TEL", tel) ;
                    //Utiles.postTwillio(AUTH_TOKEN,ACCOUNT_SID,tel,msg) ;
                    //Toast.makeText(ConnexionActivity.this, R.string.adhesionsuccess,Toast.LENGTH_LONG).show() ;
                }
                else if (result.equals("BASE")){
                    Log.e("RESULT", getString(R.string.baseerror)); ;
                }
                else if (result.equals("NUMPIECE")){
                    Log.e("RESULT", getString(R.string.pieceerror)); ;
                }
                else if (result.equals("ECHEC")){
                    Log.e("RESULT", getString(R.string.pieceerror)); ;
                }
            }

            return "FINISH" ;

        }


        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            super.onPostExecute(result);

            etape = 3 ;
            transfert();
            //SendRembroussementTask sendRembroussementTask = new SendRembroussementTask() ;
            //sendRembroussementTask.execute(Url.getRembrUrl(MainActivity.this)) ;
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //MainActivity.this.showDialog(MainActivity.PROGRESS_DIALOG_ID);
            if (membreDAO==null) membreDAO = new MembreDAO(MainActivity.this) ;
            if (caissierDAO==null) caissierDAO = new CaissierDAO(MainActivity.this) ;
            if (categorieDAO==null) categorieDAO = new CategorieDAO(MainActivity.this) ;
            if (operationDAO==null) operationDAO = new OperationDAO(MainActivity.this) ;
            c = new CaissierDAO(MainActivity.this).getLast() ;
            try {
                showDialog(PROGRESS_DIALOG_ID);
            }
            catch (Exception e){
                e.getMessage() ;
            }
        }
    }







    public class UpdateImageTask extends AsyncTask<String, Void, String> {
        SharedPreferences preferences = null;
        Membre membre = null;
        String filepath = null;
        String[] path = null ;

        public UpdateImageTask() {
        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "" ;
            ArrayList<Membre> membres = membreDAO.getNonPhotoSync() ;
            String filePath = null ;
            for (int i = 0; i < membres.size(); i++) {
                membre = membres.get(i) ;
                filePath = membre.getPhoto() ;
                if (filePath != null) {
                    response = Utiles.uploadFile(filePath, membre.getNummembre(), MainActivity.this);
                    Log.e("DEGUB",response) ;
                    if (response.contains("OK:") && response.split(":").length==2) {
                        //ImageProcess ip = new ImageProcess(this) ;
                        File file = new File(membre.getPhoto()) ;
                        if (file.exists())file.delete() ;
                        String name = null;
                        name = response.split(":")[1] ;
                        //name = article.getId() + "." + path[path.length - 1];
                        membre.setPhoto(null);
                        membreDAO.update(membre) ;
                    }
                }
            }
            return "" ;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            etape = 4 ;
            transfert();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            membreDAO = new MembreDAO(MainActivity.this) ;
            preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this) ;
            //showDialog(ArticleFormActivity.PROGRESS_DIALOG_ID);
        }
    }




}
