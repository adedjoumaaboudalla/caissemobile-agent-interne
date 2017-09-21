package caissemobile.com.mediasoft.projet.microfinacaissemobile.activities;

import android.Manifest;
import android.accounts.Account;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.nbbse.mobiprint3.Printer;


import caissemobile.com.mediasoft.projet.microfinacaissemobile.MainActivity;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.R;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Admin;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Agence;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Billet;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Caissier;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Categorie;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Compte;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Credit;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Membre;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Nationalite;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Operation;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.OperationSec;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Produit;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Profession;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.classes.Zone;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.AdminDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.AgenceDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.BilletDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CaissierDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CategorieDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CompteDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.CreditDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.DAOBase;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.MembreDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.NationaliteDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.OperationDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.OperationSecDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.ProduitDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.ProfessionDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.ZoneDAO;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.database.AgenceHelper;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.database.BilletHelper;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.database.CaissierHelper;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.database.CategorieHelper;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.database.MembreHelper;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.database.NationaliteHelper;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.database.ProduitHelper;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.database.ProfessionHelper;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.database.ZoneHelper;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.fragments.RembroussementFragment;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.PrintPDA;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.PrintPDAMobiPrint3;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.Url;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.utiles.Utiles;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class ConnexionActivity extends AppCompatActivity {

    public static final String ADMINPASS = "adminpassword";
    private static final String CORRECT = "success";
    private static final String ECHEC = "echec";
    private SharedPreferences preferences = null;
    EditText login = null ;
    EditText password = null ;
    Button annuler = null ;
    Button valider = null ;
    Button testCOnnexion = null ;
    ImageView config = null ;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView = null;
    AppBarLayout appBarLayout = null ;

    TextView gichet = null ;
    TextView solde = null ;
    Button connecter = null ;
    private AlertDialog alert = null ;


    CaissierDAO caissierDAO = null ;
    CategorieDAO categorieDAO = null ;
    BilletDAO billetDAO = null ;
    ZoneDAO zoneDAO = null ;
    ProfessionDAO professionDAO = null ;
    NationaliteDAO nationaliteDAO = null ;
    private LoadCaisierTask loadCaisierTask = null ;
    private ProgressDialog mProgressBar = null ;


    private static final int REQUEST_CODE = 0;
    private static final int PROGRESS_DIALOG_ID = 1;
    private static final int LOAD_DIALOG_ID = 2;
    private static int MAX_SIZE = 7;
    public static final String JOURNEE = "dateouvert";
    public static final String DEBUTPIECE = "debut_piece";
    public static final String LOGIN = "login";

    public static final String IP = "ip";
    public static final String DB = "db";
    public static final String PASS = "passwo";
    private AgenceDAO agenceDAO = null ;
    private ProduitDAO produitDAO = null ;
    private LoadAgenceTask loadAgenceTask;
    private String code = null;
    private LoadAdminIntercaisseTask loadAdminIntercaisse;
    private AdminDAO adminDAO;
    private LoadMembreTask loadMembre = null ;
    private MembreDAO membreDAO;
    private CreditDAO creditDAO;
    private LoadCreditTask loadCreditTask;
    private LoadCompteTask loadCompteTask;
    private CompteDAO compteDAO;
    private int VALUE = 0;
    private ArrayList<Operation> operations;
    private OperationDAO operationDAO;
    private OperationSecDAO operationSecDAO;
    private TextView nomprenom;
    private TextView journee;
    private Caissier caissier;
    private int etape  = 1;
    private Printer print;
    //private LoadCarnetTask loadCarnetTask = null ;

    private static final int MY_PERMISSIONS_REQUEST = 1;
    private String ancienneDate = null ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        initialisation() ;
        setupToolBar();
        setupNavigation();
        setTitle(R.string.app_name);


        refreshData();
    }




    private void initialisation() {
        gichet = (TextView) findViewById(R.id.guichet);
        journee = (TextView) findViewById(R.id.journee);
        nomprenom = (TextView) findViewById(R.id.nomprenom);
        solde = (TextView) findViewById(R.id.solde);
        connecter = (Button) findViewById(R.id.connecter);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drLayout) ;



        operationDAO = new OperationDAO(this) ;
        membreDAO = new MembreDAO(this) ;
        operationSecDAO = new OperationSecDAO(this) ;
        caissierDAO = new CaissierDAO(this) ;
        caissier = caissierDAO.getLast() ;

        connecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /*
                print = Printer.getInstance();
                print.printBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.logoalide));
                print.printText("");
                print.printText("");
                print.printText("");
                print.printText("");
                */

                AlertDialog.Builder builder = new AlertDialog.Builder(ConnexionActivity.this);
                ScrollView sc = (ScrollView) getLayoutInflater().inflate(R.layout.connnexion_box_layout, null);
                builder.setView(sc);

                login = (EditText) sc.findViewById(R.id.pseuso);
                password = (EditText) sc.findViewById(R.id.password);
                valider = (Button) sc.findViewById(R.id.valider);
                testCOnnexion = (Button) sc.findViewById(R.id.testconnexion);

                builder.setCancelable(true);

                final AlertDialog alert = builder.create();
                alert.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.my_accent));
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.my_accent));
                    }
                });
                alert.show();


                valider.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ConnexionActivity.this, MainActivity.class);
                        Caissier caissier = caissierDAO.getLast();
                        if (caissier==null) {
                            Toast.makeText(ConnexionActivity.this, R.string.anycaisse, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String pass = password.getText().toString();
                        //Utiles.getMD5EncryptedString("");
                        Log.e("PASSWORD TAPER", pass);
                        Log.e("PASSWORD ", caissier.getPassword());



                        if (caissier.getLogin().equals(login.getText().toString()) && caissier.getPassword().equals(pass)) {
                            alert.dismiss();
                            startActivity(intent);
                            //setResult(RESULT_OK);
                            //finish();
                        }
                        else  Toast.makeText(ConnexionActivity.this, getString(R.string.loginoupass), Toast.LENGTH_LONG).show();


                        /*
                        CheckPassword checkPassword = new CheckPassword(password.getText().toString(), login.getText().toString());


                        // SI le mode de connexion est Internet
                        if (!preferences.getBoolean(ParametreActivity.TYPE_COMMUNICATION, false)) {
                            if (!Utiles.isConnected(ConnexionActivity.this)) {
                                Toast.makeText(ConnexionActivity.this, R.string.anyconnexion, Toast.LENGTH_LONG).show();
                                return;
                            }
                            checkPassword.execute(Url.getCheckPass(ConnexionActivity.this));
                        }
                        // Sinon si le mode de connexion est Sms
                        else {
                            String msg = caissier.getCodeguichet() + ":" + password.getText().toString() + ":" + login.getText().toString();
                            sendSMS(msg, 3);
                        }
                         */

                    }
                });

                testCOnnexion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //printHandler();

                        // ARETIRER
                        TestConnexion testConnexion = new TestConnexion();
                        // SI le mode de connexion est Internet
                        if (!Utiles.isConnected(ConnexionActivity.this)) {
                            Toast.makeText(ConnexionActivity.this, R.string.anyconnexion, Toast.LENGTH_LONG).show();
                            return;
                        }
                        testConnexion.execute(Url.getTestConnexion(ConnexionActivity.this));
                    }
                });
            }
        });

        caissierDAO = new CaissierDAO(ConnexionActivity.this) ;


        preferences = PreferenceManager.getDefaultSharedPreferences(ConnexionActivity.this) ;
        ancienneDate = preferences.getString("dateouvert",null) ;
    }



    public void printHandler() {

        this.print = Printer.getInstance();
        Date d = new Date();
        CharSequence s = DateFormat.format("dd-MM-yyyy hh:mm", d.getTime());
        if(this.print.getPaperStatus() != 1) {
            Toast.makeText(this, "Pas de papier.", Toast.LENGTH_LONG).show();
        } else {
            this.print.printBitmap(this.getResources().openRawResource(R.raw.logo_sbee));
            this.print.printText("--------------------------------");
            this.print.printText("ok i\'m cool good");
            this.print.printEndLine();
            this.setResult(-1);
            this.finish();
        }
    }

    public void sendSMS(String msg,int i) {
        SmsManager manager = SmsManager.getDefault();
        String numero = Url.getSmsNumber(this) ;
        switch (i){
            case 0 : manager.sendTextMessage(numero, null, "cmi " + msg + ":", null, null); break;
            case 1 : manager.sendTextMessage(numero, null, "cmo " + msg + ":", null, null); break;
            case 2 : manager.sendTextMessage(numero, null, "cma " + msg + ":", null, null); break;
            case 3 : manager.sendTextMessage(numero, null, "cmc " + msg + ":", null, null); break;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this) ;
        builder.setMessage(getString(R.string.msgsentt)) ;
        builder.setTitle(getString(R.string.msgsent)) ;
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Utiles.deleteAllMsg(ConnexionActivity.this);
            }
        }) ;
        builder.show() ;
    }





    private void setupNavigation() {

        mNavigationView = (NavigationView) findViewById(R.id.navigation);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id) {
                    case R.id.menu_operation: {
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        if (caissierDAO.getLast()==null) {
                            Toast.makeText(ConnexionActivity.this,"Aucune donnée",Toast.LENGTH_LONG).show();
                            return true;
                        }
                        Intent intent = null;
                        //intent = new Intent(ConnexionActivity.this, OperationActivity.class);
                        //startActivity(intent);
                    }
                    break;
                    case R.id.menu_billetage: {
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        if (caissierDAO.getLast()==null) {
                            Toast.makeText(ConnexionActivity.this,"Aucune donnée",Toast.LENGTH_LONG).show();
                            return true;
                        }
                        Intent intent = null;
                        intent = new Intent(ConnexionActivity.this, BilletageActivity.class);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(ConnexionActivity.this);
                        final EditText editText = new EditText(ConnexionActivity.this);
                        editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        editText.setSingleLine(true);
                        builder.setView(editText);
                        builder.setTitle(getString(R.string.password));
                        builder.setPositiveButton(getString(R.string.valider), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                code = editText.getText().toString();

                                Intent intent = null;
                                intent = new Intent(ConnexionActivity.this, ParametreActivity.class);
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

                    };break ;/*
                    case R.id.menu_licence: {
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        Intent intent = null;
                        intent = new Intent(ConnexionActivity.this, LicenceActivity.class);
                        startActivity(intent);
                    }
                    break;
                    */
                    case R.id.menu_chargement: {
                        mDrawerLayout.closeDrawer(Gravity.LEFT);

                        AlertDialog.Builder builder = new AlertDialog.Builder(ConnexionActivity.this);
                        final EditText editText = new EditText(ConnexionActivity.this);
                        builder.setView(editText);
                        builder.setTitle(getString(R.string.codeguichet));
                        builder.setPositiveButton(getString(R.string.valider), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                code = editText.getText().toString();
                                if (code.trim().length() > 0) chargement(0);
                            }
                        });

                        builder.setNegativeButton(getString(R.string.annuler), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        if (getSyncData()==0)   {
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
                        else makeAlertDialog(getString(R.string.datanontrans),false);
                    }
                    break;
                    case R.id.menu_transfert: {
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        if (!Utiles.isConnected(ConnexionActivity.this)) {
                            Toast.makeText(ConnexionActivity.this, R.string.anyconnexion, Toast.LENGTH_SHORT).show();
                            return true;
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(ConnexionActivity.this);
                        final EditText editText = new EditText(ConnexionActivity.this);
                        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        builder.setView(editText);
                        builder.setTitle(getString(R.string.transfert));
                        builder.setMessage(getString(R.string.transfertmsg));
                        builder.setPositiveButton(getString(R.string.valider), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Caissier caissier = caissierDAO.getLast();
                                if (caissier==null) {
                                    Toast.makeText(ConnexionActivity.this, R.string.anycaisse, Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                String pass = editText.getText().toString();
                                if (caissier.getPassword().equals(pass)) {
                                    etape = 1 ;
                                    transfert() ;
                                }
                                else  Toast.makeText(ConnexionActivity.this, getString(R.string.passincorrect), Toast.LENGTH_LONG).show();

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
                            Toast.makeText(ConnexionActivity.this, R.string.passwordmodifier, Toast.LENGTH_SHORT).show();
                            alert.dismiss();
                        }
                        else Toast.makeText(ConnexionActivity.this, R.string.tropcourt, Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(ConnexionActivity.this, R.string.passpareil, Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(ConnexionActivity.this, R.string.passincorrect, Toast.LENGTH_SHORT).show();
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




    private void setupToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        // Show menu icon
        final android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.mipmap.menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    private void makeAlertDialog(String string, final boolean delete) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ConnexionActivity.this) ;
        builder.setTitle(R.string.app_name) ;
        builder.setMessage(string) ;
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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



    @Override
    protected Dialog onCreateDialog(int id) {
        super.onCreateDialog(id);
        if (id == LOAD_DIALOG_ID) {
            mProgressBar = new ProgressDialog(this);
            mProgressBar.setCancelable(false);
            mProgressBar.setProgress(VALUE);
            mProgressBar.setTitle(getString(R.string.loding));
            mProgressBar.setMessage(getString(R.string.wait1));
            mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressBar.setMax(MAX_SIZE);
        } else if (id == PROGRESS_DIALOG_ID) {
            mProgressBar = new ProgressDialog(this);
            mProgressBar.setProgress(VALUE);
            mProgressBar.setCancelable(false);
            mProgressBar.setTitle(getString(R.string.send_loding));
            mProgressBar.setMessage(getString(R.string.wait));
            mProgressBar.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                                 @Override
                                                 public void onCancel(DialogInterface dialog) {
                                                     try {
                                                         loadCaisierTask.cancel(true);
                                                         loadCaisierTask.cancel(true);
                                                         loadAgenceTask.cancel(true);
                                                         loadAdminIntercaisse.cancel(true);
                                                         loadMembre.cancel(true) ;
                                                         loadCreditTask.cancel(true) ;
                                                         loadCompteTask.cancel(true);
                                                     } catch (Exception e) {
                                                         e.printStackTrace();
                                                     }
                                                 }
                                             }

            );
            mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressBar.setMax(MAX_SIZE);
        }

        return mProgressBar;
    }



    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        mProgressBar.setProgress(VALUE);
    }

    private void actualiser(int i){
        operationDAO = new OperationDAO(ConnexionActivity.this) ;
        if (i<0) {
            operations = operationDAO.getAll() ;
            MAX_SIZE = operations.size() ;
            VALUE=0 ;
        }

        if (mProgressBar!=null)mProgressBar.setProgress(VALUE);

    }

    private void chargement(int i) {
        MAX_SIZE=7 ;
        VALUE = i;
        if (i>0)mProgressBar.setProgress(VALUE);
        switch (i){
            case 0 :{
                showDialog(LOAD_DIALOG_ID);
                loadCaisierTask = new LoadCaisierTask(code);
                loadCaisierTask.execute(Url.getLoadCaissierUrl(ConnexionActivity.this));
            } break;
            case 1 :{
                loadAgenceTask = new LoadAgenceTask() ;
                loadAgenceTask.execute(Url.getLoadAgenceUrl(this)) ;
            } break;
            /*
            case 2 :{
                loadAdminIntercaisse = new LoadAdminIntercaisseTask() ;
                loadAdminIntercaisse.execute(Url.getLoadAdminUrl(this)) ;
            } break;
            */
            case 3 :{
                loadMembre = new LoadMembreTask(code) ;
                loadMembre.execute(Url.getLoadMembre(this)) ;
            } break;
            case 4 :{
                loadCreditTask = new LoadCreditTask() ;
                loadCreditTask.execute(Url.getLoadCreditUrl(this)) ;
            } break;
            case 5 :{
                loadCompteTask = new LoadCompteTask() ;
                loadCompteTask.execute(Url.getLoadCompte(this));
            } break;
            case 6 :{
                /*
                if (preferences.getBoolean("membre",true)){
                    loadCarnetTask = new LoadCarnetTask() ;
                    loadCarnetTask.execute(Url.getLoadCarnet(this));
                }
                else
                */ chargement(7);
            } break;
        }
    }

    // LES ASYNC TASK DE CHARGEMENT

    public class LoadCaisierTask extends AsyncTask<String,Void,String> {

        String codeClient = null ;

        public LoadCaisierTask(String code) {
            codeClient = code ;
        }

        @Override
        protected String doInBackground(String... urls) {
            //Licence licence = new LicenceDAO(ConnexionActivity.this).getLast() ;

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

            RequestBody formBody = new FormBody.Builder()
                    .add(CaissierHelper.ID, codeClient)
                    .add("iMei", iMei)
                    .add("macAddress", macAddress)
                    .add("licence", "MDIJ-IBSN-GWCT-UNXK")
                    .build();

            String result = "";
            try {
                result = Utiles.POST(urls[0],formBody);
            } catch (IOException e) {
                e.printStackTrace();
            }

                if (result!=null) Log.e("DEBUG", result) ;

                JSONObject json = null ;
                try {
                    json = new JSONObject(result);
                    JSONArray caissiers = json.getJSONArray("caissiers") ;

                    caissierDAO = new CaissierDAO(ConnexionActivity.this) ;
                    compteDAO = new CompteDAO(ConnexionActivity.this) ;
                    caissierDAO.clean() ;
                    //compteDAO.clean() ;
                    Caissier caissier = null ;
                    for (int i = 0; i < caissiers.length() ; i++){
                        caissier = new Caissier() ;
                        caissier.setId(caissiers.getJSONObject(i).getInt(CaissierHelper.ID));
                        caissier.setAgence_id(caissiers.getJSONObject(i).getString(CaissierHelper.AGENCE_ID));
                        caissier.setLogin(caissiers.getJSONObject(i).getString(CaissierHelper.LOGIN));
                        caissier.setPassword(caissiers.getJSONObject(i).getString(CaissierHelper.PASSWORD));
                        caissier.setNomprenom(caissiers.getJSONObject(i).getString(CaissierHelper.NOM_PRENOM));
                        caissier.setCodeguichet(caissiers.getJSONObject(i).getString(CaissierHelper.CODE_GUICHET));
                        caissier.setSolde((float) caissiers.getJSONObject(i).getDouble(CaissierHelper.SOLDE));
                        caissier.setRetraitMax(caissiers.getJSONObject(i).getDouble(CaissierHelper.RETRAITMAX));

                        caissier.setDescription(caissiers.getJSONObject(i).getString(CaissierHelper.DESCRIPTION));
                        caissier.setDdb(caissiers.getJSONObject(i).getString(CaissierHelper.DDB));
                        caissier.setDi(caissiers.getJSONObject(i).getString(CaissierHelper.DI));
                        caissier.setDp(caissiers.getJSONObject(i).getString(CaissierHelper.DP));
                        caissier.setDl(caissiers.getJSONObject(i).getString(CaissierHelper.DL));
                        caissier.setPassword(caissiers.getJSONObject(i).getString(CaissierHelper.PASSWORD));
                        if (!caissiers.getJSONObject(i).getString(CaissierHelper.NUMPIECE).equals("null"))caissier.setNumpiece(caissiers.getJSONObject(i).getInt(CaissierHelper.NUMPIECE));

                        caissierDAO.clean() ;
                        caissierDAO.add(caissier) ;

                        SharedPreferences.Editor edit = preferences.edit() ;
                        Date date = null ;
                        try {
                            date = DAOBase.formatter.parse(caissiers.getJSONObject(i).getString("dateouvert")) ;
                            edit.putString(JOURNEE,DAOBase.formatterj.format(date)) ;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        edit.putString(DEBUTPIECE,caissiers.getJSONObject(i).getString("debutpiece")) ;
                        edit.commit() ;
                        //refreshData();
                    }

                    JSONArray categories = json.getJSONArray("categories") ;
                    categorieDAO = new CategorieDAO(ConnexionActivity.this) ;
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

                        Log.v("NUMCATEGORIE",categorie.getNumCategorie()) ;
                        Log.v("NUMPRODUIT",categorie.getNumproduit()) ;

                        categorieDAO.add(categorie) ;
                    }

                    if (mProgressBar!=null)mProgressBar.setProgress(VALUE);

                    JSONArray billets = null ;
                    billets = json.getJSONArray("billets") ;

                    billetDAO = new BilletDAO(ConnexionActivity.this) ;
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

                    nationaliteDAO = new NationaliteDAO(ConnexionActivity.this) ;
                    nationaliteDAO.clean() ;
                    Nationalite nationalite = null ;
                    for (int i = 0; i < nationalites.length() ; i++){
                        nationalite = new Nationalite() ;

                        nationalite.setNumnation(nationalites.getJSONObject(i).getInt(NationaliteHelper.NUMNATIONALITE));
                        nationalite.setLibelle(nationalites.getJSONObject(i).getString(NationaliteHelper.LIBELLE));
                        nationaliteDAO.add(nationalite) ;
                    }


                    JSONArray professions = json.getJSONArray("professions") ;

                    professionDAO = new ProfessionDAO(ConnexionActivity.this) ;
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

                    zoneDAO = new ZoneDAO(ConnexionActivity.this) ;
                    zoneDAO.clean() ;
                    Zone zone = null ;

                    for (int i = 0; i < zones.length() ; i++){
                        zone = new Zone() ;

                        zone.setDescription(zones.getJSONObject(i).getString(ZoneHelper.DESCRIPTION));
                        zoneDAO.add(zone) ;
                    }

                    JSONArray produits = json.getJSONArray("produits") ;

                    produitDAO = new ProduitDAO(ConnexionActivity.this) ;
                    produitDAO.clean() ;
                    Produit produit = null ;

                    for (int i = 0; i < produits.length() ; i++){
                        produit = new Produit() ;

                        produit.setLibelle(produits.getJSONObject(i).getString(ProduitHelper.LIBELLE));
                        produit.setNumproduit(produits.getJSONObject(i).getString(ProduitHelper.NUMPRODUIT));
                        produit.setCode(produits.getJSONObject(i).getString(ProduitHelper.CODE));
                        produit.setDepotmini(produits.getJSONObject(i).getDouble(ProduitHelper.MONTANT_MINI));
                        produitDAO.add(produit) ;
                    }

                    //Toast.makeText(ConnexionActivity.this, R.string.loadsuccess, Toast.LENGTH_SHORT).show();

                    //dismissDialog(PROGRESS_DIALOG_ID);

                    return CORRECT ;
                } catch (JSONException e) {
                    e.printStackTrace();
                    //VALUE = 0 ;
                    //Toast.makeText(ConnexionActivity.this, R.string.echec, Toast.LENGTH_SHORT).show();
                    return ECHEC ;
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    //VALUE = 0 ;
                    return ECHEC ;
                    //Toast.makeText(ConnexionActivity.this, R.string.licenceexp, Toast.LENGTH_SHORT).show();
                }

        }


        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            super.onPostExecute(result);
            /*Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

            Intent intent = new Intent(Intent.ACTION_VIEW) ;
            intent.put
                    */
            if ( result != null && result.contains("LICENCE_ERROR")) {
                dismissDialog(LOAD_DIALOG_ID);
                makeAlertDialog(getString(R.string.licenceexp),false) ;
            }
            else if ( result != null && result.contains("CAISSIER_ERROR"))   {
                dismissDialog(LOAD_DIALOG_ID);
                makeAlertDialog(getString(R.string.aucuncaissier),false) ;
            }
            else if ( result != null && result.contains("INCONNU"))   {
                dismissDialog(LOAD_DIALOG_ID);
                makeAlertDialog(getString(R.string.peripheriqueinconnu),false) ;
            }
            else if (result.equals(CORRECT))  chargement(1) ;
            else  {
                dismissDialog(LOAD_DIALOG_ID);
                makeAlertDialog(getString(R.string.echec),false) ;
            }

        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }




    public class LoadAgenceTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            TelephonyManager telephonyManager =  ( TelephonyManager ) getSystemService (Context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId ();
            Log.e("DEBUG", urls[0]) ;

            String result = "";
            try {
                result = Utiles.GET(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result ;
        }


        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            super.onPostExecute(result);
            JSONObject json = null;

            Log.e("DEBUG",result) ;
            try {
                json = new JSONObject(result);
                JSONArray agences = json.getJSONArray("agences") ;

                agenceDAO = new AgenceDAO(ConnexionActivity.this) ;
                agenceDAO.clean() ;
                Agence agence = null ;
                for (int i = 0; i < agences.length() ; i++){
                    agence = new Agence() ;
                    agence.setNumagence(agences.getJSONObject(i).getString(AgenceHelper.NUMAGENCE));
                    agence.setNombd(agences.getJSONObject(i).getString(AgenceHelper.NOM_DB));
                    agence.setNumeroSms(agences.getJSONObject(i).getString(AgenceHelper.NUMERO_SMS));
                    agence.setIpbd(agences.getJSONObject(i).getString(AgenceHelper.IP_DB));
                    agence.setCompteLiaison(agences.getJSONObject(i).getString(AgenceHelper.COMPTE_LIAISON));
                    agence.setLogin(agences.getJSONObject(i).getString(AgenceHelper.LOGIN));
                    agence.setPasswordbd(agences.getJSONObject(i).getString(AgenceHelper.PASSWORD_DB));
                    agence.setNomagence(agences.getJSONObject(i).getString(AgenceHelper.NOMAGENCE));
                    agenceDAO.add(agence) ;
                }
                //dismissDialog(L);
                chargement(3);

                //Toast.makeText(ConnexionActivity.this, R.string.loadsuccess, Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                e.printStackTrace();
                dismissDialog(LOAD_DIALOG_ID);
                //VALUE = 0 ;
                Toast.makeText(ConnexionActivity.this, R.string.echec, Toast.LENGTH_SHORT).show();
            }

        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showDialog(PROGRESS_DIALOG_ID);
        }
    }



    public class LoadAdminIntercaisseTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            TelephonyManager telephonyManager =  ( TelephonyManager ) getSystemService (Context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId ();
            Log.e("DEBUG", urls[0]) ;

            String result = "";
            try {
                result = Utiles.GET(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result ;
        }


        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            super.onPostExecute(result);
            JSONObject json = null;

            Log.e("DEBUG",result) ;
            try {
                json = new JSONObject(result);
                JSONArray admins = json.getJSONArray("admins") ;

                adminDAO = new AdminDAO(ConnexionActivity.this) ;
                adminDAO.clean() ;
                Admin admin = null ;
                for (int i = 0; i < admins.length() ; i++){
                    admin = new Admin() ;
                    admin.setLogin(admins.getJSONObject(i).getString("Nomutil"));
                    admin.setPassword(admins.getJSONObject(i).getString("Password"));
                    adminDAO.add(admin) ;
                }

                //dismissDialog(PROGRESS_DIALOG_ID);

                chargement(3);
                //dismissDialog(LOAD_DIALOG_ID);

            } catch (JSONException e) {
                e.printStackTrace();
                dismissDialog(LOAD_DIALOG_ID);
                //VALUE = 0 ;
                Toast.makeText(ConnexionActivity.this, R.string.echec, Toast.LENGTH_SHORT).show();
            }

        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showDialog(PROGRESS_DIALOG_ID);
        }
    }

    private void refreshData() {
        demandePermission();
        Caissier caissier = caissierDAO.getLast() ;
        if (caissier!=null){
            caissier.setJournee(preferences.getString(JOURNEE,"")) ;
            caissierDAO.update(caissier) ;
            gichet.setText(getString(R.string.guichet) + " " + caissier.getCodeguichet());
            nomprenom.setText(caissier.getLogin());
            journee.setText(getString(R.string.jrne)+caissier.getJournee());
            nomprenom.setVisibility(View.VISIBLE);

            ArrayList<Operation> operations = operationDAO.getAll() ;
            float mtn = caissierDAO.getLast().getSolde() ;
            for (int j = 0; j < operations.size(); ++j) {
                if (operations.get(j).getTypeoperation() == 0 || operations.get(j).getTypeoperation()==3 || operations.get(j).getTypeoperation()==4) mtn += operations.get(j).getMontant();
                else mtn -= operations.get(j).getMontant();
            }

            solde.setText(getString(R.string.solde) + " " + Utiles.formatMtn(mtn));
        }

    }


    public void demandePermission(){

        // Demande de Permissions
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //Cela signifie que la permission à déjà était
                //demandé et l'utilisateur l'a refusé
                //Vous pouvez aussi expliquer à l'utilisateur pourquoi
                //cette permission est nécessaire et la redemander
            } else {
                //Sinon demander la permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST);
            }
        }
        else if (ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)) {

            } else {
                //Sinon demander la permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST);
            }
        }
        else if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_PHONE_STATE)) {

            } else {
                //Sinon demander la permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // La permission est garantie
                } else {
                    // La permission est refusée
                }
                refreshData();
                return;
            }
        }
    }





    public class TestConnexion extends AsyncTask<String,Void,String> {

        public TestConnexion() {

        }

        @Override
        protected String doInBackground(String... urls) {
            //TelephonyManager telephonyManager =  ( TelephonyManager ) getSystemService (Context.TELEPHONY_SERVICE);
            //String imei = telephonyManager.getDeviceId ();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ConnexionActivity.this) ;

            operations = operationDAO.getOpAdhesion();

            String typop = "" ;


            Log.e("SIZE MEMBRE", String.valueOf(operations.size())) ;

            RequestBody formBody = new FormBody.Builder()
                    .add("licence", "MDIJ-IBSN-GWCT-UNXK")
                    .build();

            String result = "";
            try {
                result = Utiles.POST(urls[0],formBody);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result ;

        }


        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            super.onPostExecute(result);

            Log.e("RESULT",result) ;

            if (result.contains("OK:") && result.split(":").length==2) {
                Toast.makeText(ConnexionActivity.this, getString(R.string.connexionok), Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(ConnexionActivity.this, getString(R.string.connexionechec), Toast.LENGTH_LONG).show();
            }
            dismissDialog(PROGRESS_DIALOG_ID);

        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG_ID);
        }
    }



    public class LoadMembreTask extends AsyncTask<String,Void,String> {

        String code = null ;
        public LoadMembreTask(String code) {
            this.code = code ;
        }


        @Override
        protected String doInBackground(String... urls) {
            TelephonyManager telephonyManager =  ( TelephonyManager ) getSystemService (Context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId ();
            Log.e("DEBUG", urls[0]) ;
            Log.d("ancienneDate",String.valueOf(ancienneDate)) ;

            RequestBody formBody = new FormBody.Builder()
                    .add("guichet", code)
                    .add("ancienneDate", String.valueOf(ancienneDate))
                    .add("journee", preferences.getString(JOURNEE, ""))
                    .build();

            String result = "";
            try {
                result = Utiles.POST(urls[0],formBody);
            } catch (IOException e) {
                e.printStackTrace();
            }

            JSONObject json = null;

            Log.e("DEBUG",result) ;
            try {
                json = new JSONObject(result);
                JSONArray membres = json.getJSONArray("membres") ;


                membreDAO = new MembreDAO(ConnexionActivity.this) ;
                //membreDAO.clean() ;
                Membre membre = null ;
                for (int i = 0; i < membres.length() ; i++){
                    membre = new Membre() ;
                    membre.setNummembre(membres.getJSONObject(i).getString("NUMMEMBRE"));
                    membre.setNom(membres.getJSONObject(i).getString("NOM"));
                    membre.setPrenom(membres.getJSONObject(i).getString("PRENOM"));
                    membre.setTel(membres.getJSONObject(i).getString("TELEPHONE"));
                    membre.setSexe(membres.getJSONObject(i).getString("SEXE"));
                    if (!membres.getJSONObject(i).getString("PROFESSION").equals("null"))membre.setProfession(membres.getJSONObject(i).getInt("PROFESSION"));
                    membre.setNationalite(membres.getJSONObject(i).getInt("NATIONALITE"));
                    membre.setAdresse(membres.getJSONObject(i).getString("ADRESSE"));
                    if (membre.getNummembre().contains("/G/"))membre.setType(1);
                    else membre.setType(0);
                    membre.setCodemembre(membres.getJSONObject(i).getString("codemembre"));
                    membre.setNumgroupe(membres.getJSONObject(i).getString("numgroupe"));
                    membre.setCodeRetrait(membres.getJSONObject(i).getString("code_retrait"));
                    membre.setActivite("");
                    membre.setPoste("");
                    try {
                        membre.setIdpersonne(membres.getJSONObject(i).getString("IDPERSONNE"));
                    }
                    catch (Exception e){
                        //e.printStackTrace();
                    }
                    try {
                        membre.setDateadhesion(DAOBase.formatter6.parse(membres.getJSONObject(i).getString("DATE_ADHESION")));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        membre.setDateNaiss(DAOBase.formatter6.parse(membres.getJSONObject(i).getString("DATE_ADHESION")));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    membre.setSync(1);

                    membreDAO.add(membre);
                }

                //dismissDialog(PROGRESS_DIALOG_ID);

                return CORRECT ;
                //Toast.makeText(ConnexionActivity.this, R.string.loadsuccess, Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                e.printStackTrace();
                return ECHEC ;
                //VALUE = 0 ;
            }
        }


        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            super.onPostExecute(result);
            if (result.equals(ECHEC)) {
                dismissDialog(LOAD_DIALOG_ID);
                Toast.makeText(ConnexionActivity.this, R.string.echec, Toast.LENGTH_SHORT).show();
            }
            else chargement(4);
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showDialog(PROGRESS_DIALOG_ID);
        }
    }

    public class LoadCompteTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            TelephonyManager telephonyManager =  ( TelephonyManager ) getSystemService (Context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId ();
            Log.e("DEBUG", urls[0]) ;
            Log.d("ancienneDate",String.valueOf(ancienneDate)) ;

            RequestBody formBody = new FormBody.Builder()
                    .add("guichet", code)
                    .add("ancienneDate", String.valueOf(ancienneDate))
                    .add("journee", preferences.getString(JOURNEE, ""))
                    .build();

            String result = "";

            try {
                result = Utiles.POST(urls[0],formBody);
            } catch (IOException e) {
                e.printStackTrace();
            }

            JSONObject json = null;

            Log.e("DEBUG",result) ;
            try {
                json = new JSONObject(result);
                JSONArray comptes = json.getJSONArray("comptes") ;

                compteDAO = new CompteDAO(ConnexionActivity.this) ;
                //compteDAO.clean() ;
                Compte compte = null ;
                for (int i = 0; i < comptes.length() ; i++){
                    compte = new Compte() ;

                    compte.setNumcompte(comptes.getJSONObject(i).getString("NUMCOMPTE"));
                    compte.setSexe(comptes.getJSONObject(i).getString("SEXE"));
                    compte.setPrenom(comptes.getJSONObject(i).getString("PRENOM"));
                    compte.setSolde((float) comptes.getJSONObject(i).getDouble("SOLDE"));
                    compte.setNom(comptes.getJSONObject(i).getString("NOM"));
                    compte.setNummembre(comptes.getJSONObject(i).getString("NUMMEMBRE"));
                    compte.setNumProduit(comptes.getJSONObject(i).getString("NUMPRODUIT"));
                    compte.setProduit(comptes.getJSONObject(i).getString("PRODUIT"));
                    compte.setMise(comptes.getJSONObject(i).getString("MISE"));
                    compte.setMiseLibre(comptes.getJSONObject(i).getString("MISELIBRE"));
                    compte.setType(Compte.EPARGNE) ;
                    compte.setNbrecredit(comptes.getJSONObject(i).getInt("credit")); ;
                    try {
                        compte.setDatecreation(DAOBase.formatter6.parse(comptes.getJSONObject(i).getString("DATECREATION")));
                        compte.setPin(comptes.getJSONObject(i).getString("PIN"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    compteDAO.add(compte) ;
                }

                return CORRECT ;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return ECHEC ;

        }


        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            super.onPostExecute(result);
            dismissDialog(LOAD_DIALOG_ID);
            if (result.equals(ECHEC)){
                Toast.makeText(ConnexionActivity.this, R.string.echec, Toast.LENGTH_SHORT).show();
                caissierDAO.clean() ;
            }
            else if (result.equals(CORRECT)){
                //operationDAO.clean() ;
                //operationSecDAO.clean() ;
                makeAlertDialog(getString(R.string.loadsuccess),true) ;
                operationDAO.archiver() ;
                refreshData() ;
                chargement(6);
            }
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showDialog(PROGRESS_DIALOG_ID);
        }
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
            Toast.makeText(ConnexionActivity.this, R.string.noconnexion, Toast.LENGTH_SHORT).show();
            return;
        }

        if (operationDAO.getNonSync().size() != 0 && etape == 1){
            Log.e("SYNC","OPERATION LANCE") ;
            SendOperationTask sendOperationTask = new SendOperationTask();
            sendOperationTask.execute(Url.getSendOperationUrl(ConnexionActivity.this));
            return;
        }
        else if (etape==1)etape = 2 ;


        if (operationDAO.getNonSync().size() != 0 && etape == 2){
            Log.e("SYNC","PARTENAIRE LANCE") ;
            SendMembreTask sendMembreTask = new SendMembreTask() ;
            sendMembreTask.execute(Url.getAddMembreUrl(ConnexionActivity.this)) ;
            return;
        }
        else if (etape==2) etape = 3 ;


        if (operationDAO.getNonSync().size() != 0 && etape == 3){
            Log.e("SYNC","PHOTO LANCE") ;
            SendRemboursementTask rembroussementTask = new SendRemboursementTask() ;
            rembroussementTask.execute(Url.getRembrUrl(ConnexionActivity.this)) ;
            return;
        }
        else if (etape==3) etape = 4 ;

        if (membreDAO.getNonPhotoSync().size() != 0 && etape == 4){
            Log.e("SYNC","PHOTO LANCE") ;
            UpdateImageTask updateImageTask = new UpdateImageTask() ;
            updateImageTask.execute() ;
            return;
        }
        else if (etape==4) etape = 5 ;


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
            operationDAO.archiver() ;
            makeAlertDialog(getString(R.string.trsuccess),false);
        }
    }

    private int getSyncData() {
        return  operationDAO.getNonSync().size();
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


            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ConnexionActivity.this) ;

            Log.e("DEBUG","ETAT") ;
            operations = operationDAO.getNonSync();
            VALUE = 0 ;
            String typop = "" ;
            Log.e("DEBUG", String.valueOf(operations.size())) ;

            Date date = new Date() ;
            try {
                date = DAOBase.formatterj.parse(preferences.getString(JOURNEE, "")) ;
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

            FormBody.Builder formBuilder = null ;

            for (int j = 0;j<operations.size();++j){
                operation = operations.get(j) ;
                if (operation.getTypeoperation()>=3) continue;
                formBuilder = new FormBody.Builder();

                formBuilder.add("iMei", iMei) ;
                formBuilder.add("macAddress", macAddress);
                formBuilder.add("NumCompte", operation.getNumcompte()) ;
                formBuilder.add("interface", "E") ;
                formBuilder.add("NumProduit", operation.getNumproduit()) ;
                formBuilder.add("journee",  preferences.getString("dateouvert", "")) ;
                formBuilder.add("montant1", Utiles.formatMtn2(operation.getMontant())) ;
                formBuilder.add("montant2", Utiles.formatMtn2(operation.getMontant())) ;
                if (operation.getTypeoperation()==0)formBuilder.add("TypeOperation", "VSE") ;
                else formBuilder.add("TypeOperation", "RE") ;

                formBuilder.add("mise", "0") ;
                formBuilder.add("AgenceClient",  operation.getAgence()) ;
                formBuilder.add("guichet", String.valueOf(c.getCodeguichet())) ;
                formBuilder.add("numagence", String.valueOf(c.getAgence_id())) ;

                formBuilder.add("ddb", c.getDdb()) ;
                formBuilder.add("dl", c.getDl()) ;
                formBuilder.add("dp", c.getDp()) ;
                formBuilder.add("di", c.getDi()) ;
                formBuilder.add("forcer","1") ;

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
                RequestBody formBody = formBuilder.build() ;
                String result = "";
                try {
                    result = Utiles.POST(urls[0],formBody);
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
                VALUE++ ;
                actualiser(VALUE);
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
            if (operationDAO==null) operationDAO = new OperationDAO(ConnexionActivity.this) ;
            if (operationSecDAO==null) operationSecDAO = new OperationSecDAO(ConnexionActivity.this) ;
            if (compteDAO==null) compteDAO = new CompteDAO(ConnexionActivity.this) ;

            showDialog(PROGRESS_DIALOG_ID);
            c = new CaissierDAO(ConnexionActivity.this).getLast() ;
            Log.e("DEBUG","PRE EXECUTE") ;
        }
    }




    public class SendRemboursementTask extends AsyncTask<String,Void,String> {

        OperationDAO operationDAO = null ;
        OperationSecDAO operationSecDAO = null ;
        Caissier c  =null;
        private ArrayList<Operation> operations;
        Operation operation ;
        private String result;

        public SendRemboursementTask() {
            Log.e("DEBUG","ETAT") ;
        }

        @Override
        protected String doInBackground(String... urls) {
            //TelephonyManager telephonyManager =  ( TelephonyManager ) getSystemService (Context.TELEPHONY_SERVICE);
            //String imei = telephonyManager.getDeviceId ();


            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ConnexionActivity.this) ;

            Log.e("DEBUG","ETAT") ;
            operations = operationDAO.getNonSync();
            VALUE = 0 ;
            String typop = "" ;
            Log.e("DEBUG", String.valueOf(operations.size())) ;

            Date date = new Date() ;
            try {
                date = DAOBase.formatterj.parse(preferences.getString(JOURNEE, "")) ;
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


            FormBody.Builder formBuilder = null ;

            for (int j = 0;j<operations.size();++j){
                operation = operations.get(j) ;
                operation = operations.get(j) ;
                if (operation.getTypeoperation()!=4) continue;
                preferences = PreferenceManager.getDefaultSharedPreferences(ConnexionActivity.this) ;
                date = new Date() ;
                formBuilder = new FormBody.Builder() ;
                formBuilder.add("iMei", iMei) ;
                formBuilder.add("macAddress", macAddress);
                formBuilder.add("numcredit",operation.getNumcompte()) ;
                formBuilder.add("journee",preferences.getString("dateouvert", "")) ;
                formBuilder.add("montant",String.valueOf(operation.getMontant())) ;
                formBuilder.add("guichet",c.getCodeguichet()) ;
                formBuilder.add("forcer","1") ;


                try {
                    result = Utiles.POST(Url.getRembrUrl(ConnexionActivity.this),formBuilder.build());
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

                VALUE++ ;
                actualiser(VALUE);
            }

            return "FINISH" ;
        }


        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            super.onPostExecute(result);
            Log.e("DEBUG", result) ;
            etape = 4 ;
            transfert() ;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (operationDAO==null) operationDAO = new OperationDAO(ConnexionActivity.this) ;
            if (operationSecDAO==null) operationSecDAO = new OperationSecDAO(ConnexionActivity.this) ;
            if (compteDAO==null) compteDAO = new CompteDAO(ConnexionActivity.this) ;

            showDialog(PROGRESS_DIALOG_ID);
            c = new CaissierDAO(ConnexionActivity.this).getLast() ;
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

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ConnexionActivity.this) ;

            operations = operationDAO.getNonSync();

            String typop = "" ;

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


            Log.e("SIZE MEMBRE", String.valueOf(operations.size())) ;
            for (int j = 0;j<operations.size();++j) {
                operation = operations.get(j);
                Log.e("TYPE OP", String.valueOf(operation.getTypeoperation())) ;
                if (operation.getTypeoperation()!=3) continue;
                membre = membreDAO.getOne(operation.getUser_id()) ;

                String tel = membre.getTel() ;

                if (!tel.startsWith("00229") && !tel.startsWith("+229")) tel =  "+229"+ tel ;
                if (tel.startsWith("00229")) tel = tel.replace("00229","+229") ;

                FormBody.Builder formBuilder = new FormBody.Builder();
                formBuilder.add("guichet",String.valueOf(c.getCodeguichet()));
                formBuilder.add("iMei", iMei) ;
                formBuilder.add("macAddress", macAddress);
                formBuilder.add("numproduit",operation.getNumproduit());
                formBuilder.add("journee", DAOBase.formatterj.format(operation.getDateoperation()));
                formBuilder.add("Mt_Operation", String.valueOf(membre.getMontant()));
                formBuilder.add("NumCategorie",String.valueOf(categorieDAO.getOne(membre.getCategorie()).getNumCategorie()));
                formBuilder.add("NumProfession",String.valueOf(membre.getProfession()));
                formBuilder.add("NumNationalite",String.valueOf(membre.getNationalite()));
                formBuilder.add(MembreHelper.NOM, membre.getNom());
                formBuilder.add(MembreHelper.PRENOM,membre.getPrenom());
                formBuilder.add(MembreHelper.SEXE,membre.getSexe());
                formBuilder.add(MembreHelper.TEL,tel);
                formBuilder.add(MembreHelper.ADRESSE,membre.getAdresse());
                formBuilder.add(MembreHelper.DATE, DAOBase.formatterj.format(membre.getDateadhesion()));
                formBuilder.add(MembreHelper.DATENAISS, DAOBase.formatterj.format(membre.getDateNaiss()));
                formBuilder.add("NumZone",membre.getZone());
                formBuilder.add("codeGuichet", String.valueOf(c.getCodeguichet()));
                formBuilder.add("forcer","1") ;

                formBuilder.add("lat","0");
                formBuilder.add("long","0");

                ArrayList<Membre> membreArrayList = membreDAO.getAllByNumMembre(String.valueOf(membre.getId()));

                Log.e("NOMBRE DE MEMBRE",String.valueOf(membreArrayList.size())) ;

                formBuilder.add("nbre",String.valueOf(membreArrayList.size()));

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
                if (membreArrayList.size()>0) url = Url.getAddMembreGroupeUrl(ConnexionActivity.this);
                else url = Url.getAddMembreUrl(ConnexionActivity.this);



                result = "" ;

                try {
                    result = Utiles.POST(url,formBuilder.build());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.e("RESULT MEMBRE",result) ;
                if (membre.getPhoto()!=null) Log.e("RESULT MEMBRE", membre.getPhoto()) ;

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
                    try {
                        Utiles.postTwillio(tel,msg) ;
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

                VALUE++ ;
                actualiser(VALUE);
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
            //sendRembroussementTask.execute(Url.getRembrUrl(ConnexionActivity.this)) ;
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //ConnexionActivity.this.showDialog(MainActivity.PROGRESS_DIALOG_ID);
            if (membreDAO==null) membreDAO = new MembreDAO(ConnexionActivity.this) ;
            if (caissierDAO==null) caissierDAO = new CaissierDAO(ConnexionActivity.this) ;
            if (categorieDAO==null) categorieDAO = new CategorieDAO(ConnexionActivity.this) ;
            if (operationDAO==null) operationDAO = new OperationDAO(ConnexionActivity.this) ;
            c = new CaissierDAO(ConnexionActivity.this).getLast() ;
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
                    response = Utiles.uploadFile(filePath, membre.getNummembre(), ConnexionActivity.this);
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
            membreDAO = new MembreDAO(ConnexionActivity.this) ;
            preferences = PreferenceManager.getDefaultSharedPreferences(ConnexionActivity.this) ;
            //showDialog(ArticleFormActivity.PROGRESS_DIALOG_ID);
        }
    }






    public class LoadCreditTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            TelephonyManager telephonyManager =  ( TelephonyManager ) getSystemService (Context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId ();
            Log.e("DEBUG", urls[0]) ;

            RequestBody formBody = new FormBody.Builder()
                    .add("guichet", code)
                    .add("journee", preferences.getString(JOURNEE, ""))
                    .build();

            String result = "";

            try {
                result = Utiles.POST(urls[0],formBody);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result ;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            JSONObject json = null;

            Log.e("DEBUG",result) ;
            try {
                json = new JSONObject(result);
                JSONArray credits = json.getJSONArray("credits") ;

                creditDAO = new CreditDAO(ConnexionActivity.this) ;
                creditDAO.clean() ;
                Credit credit = null ;
                for (int i = 0; i < credits.length() ; i++){
                    credit = new Credit() ;

                    credit.setNumcredit(credits.getJSONObject(i).getString("credit"));
                    credit.setNom(credits.getJSONObject(i).getString("nom"));
                    credit.setPrenom(credits.getJSONObject(i).getString("prenom"));
                    credit.setMontantpret((float) credits.getJSONObject(i).getDouble("montantpret"));
                    credit.setCreaditencours((float) credits.getJSONObject(i).getDouble("capital_restant"));
                    credit.setNummembre(credits.getJSONObject(i).getString("nummembre"));
                    // A REVOIR
                    //if (!credits.getJSONObject(i).getString("MENSUALITE").equals("null")) credit.setMensualite((float) credits.getJSONObject(i).getDouble("MENSUALITE"));
                    if (!credits.getJSONObject(i).getString("DUREEPRET").equals("null")) credit.setDurepret(credits.getJSONObject(i).getInt("DUREEPRET"));
                    try {
                        credit.setDatedeblocage(DAOBase.formatter.parse(credits.getJSONObject(i).getString("DATEDEBLOCAGE")));
                        //credit.setDatedebut(DAOBase.formatter.parse(credits.getJSONObject(i).getString("DATEDEBUT")));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    credit.setTauxan((float) credits.getJSONObject(i).getDouble("tauxan"));
                    credit.setSuivi(credits.getJSONObject(i).getString("AGENT_CREDIT"));
                    credit.setNomproduit(credits.getJSONObject(i).getString("produitcredit"));
                    if (!credits.getJSONObject(i).getString("reste_a_payer").equals("null"))credit.setReste_apayer((float) credits.getJSONObject(i).getDouble("reste_a_payer"));
                    if (!credits.getJSONObject(i).getString("capital_attendu").equals("null"))credit.setCapital_attendu((float) credits.getJSONObject(i).getDouble("capital_attendu"));
                    if (!credits.getJSONObject(i).getString("interet_attendu").equals("null"))credit.setInteret_attendu((float) credits.getJSONObject(i).getDouble("interet_attendu"));
                    if (!credits.getJSONObject(i).getString("interet_retard").equals("null"))credit.setInteret_retard((float) credits.getJSONObject(i).getDouble("interet_retard"));
                    if (!credits.getJSONObject(i).getString("capital_retard").equals("null"))credit.setCapital_retard((float) credits.getJSONObject(i).getDouble("capital_retard"));
                    if (!credits.getJSONObject(i).getString("total_retard").equals("null"))credit.setTotal_retard((float) credits.getJSONObject(i).getDouble("total_retard"));

                    creditDAO.add(credit) ;
                }

                //dismissDialog(PROGRESS_DIALOG_ID);

                chargement(5);
                //Toast.makeText(ConnexionActivity.this, R.string.loadsuccess, Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                e.printStackTrace();
                dismissDialog(LOAD_DIALOG_ID);
                //VALUE = 0 ;
                Toast.makeText(ConnexionActivity.this, R.string.echec, Toast.LENGTH_SHORT).show();
            }
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showDialog(PROGRESS_DIALOG_ID);
        }
    }




/*




    public class CheckPassword extends AsyncTask<String,Void,String> {

        CaissierDAO caissierDAO = null ;
        Caissier c  =null;
        String pass ;
        String login ;

        public CheckPassword(String password, String login) {
            pass = password ;
            this.login = login ;
        }

        @Override
        protected String doInBackground(String... urls) {
            //TelephonyManager telephonyManager =  ( TelephonyManager ) getSystemService (Context.TELEPHONY_SERVICE);
            //String imei = telephonyManager.getDeviceId ();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ConnexionActivity.this) ;

            operations = operationDAO.getOpAdhesion();

            String typop = "" ;


            Log.e("SIZE MEMBRE", String.valueOf(operations.size())) ;

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(14);
            nameValuePairs.add(new BasicNameValuePair("codeGuichet",String.valueOf(c.getCodeguichet())));
            nameValuePairs.add(new BasicNameValuePair("password", String.valueOf(pass)));
            nameValuePairs.add(new BasicNameValuePair("login", String.valueOf(login)));

            String result = Utiles.POST(urls[0],nameValuePairs);

            VALUE++ ;
            actualiser(VALUE);
            return result ;

        }


        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            super.onPostExecute(result);

            Log.e("RESULT",result) ;

            if (result.contains("OK:") && result.split(":").length==2) {
                alert.dismiss();
                Intent intent = new Intent(ConnexionActivity.this, MainActivity.class);
                startActivity(intent);
            }
            else {
                //Log.e("RESULT", getString(R.string.pieceerror));
                Toast.makeText(ConnexionActivity.this, getString(R.string.loginoupass), Toast.LENGTH_LONG).show();
            }

            dismissDialog(PROGRESS_DIALOG_ID);
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //ConnexionActivity.this.showDialog(MainActivity.L);
            if (caissierDAO==null) caissierDAO = new CaissierDAO(ConnexionActivity.this) ;
            c = new CaissierDAO(ConnexionActivity.this).getLast() ;
            showDialog(PROGRESS_DIALOG_ID);
        }
    }




    public class LoadCarnetTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            TelephonyManager telephonyManager =  ( TelephonyManager ) getSystemService (Context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId ();
            Log.e("DEBUG", urls[0]) ;

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("guichet", code));
            nameValuePairs.add(new BasicNameValuePair("journee", preferences.getString(JOURNEE, "")));
            return Utiles.POST(urls[0],nameValuePairs);
        }


        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            super.onPostExecute(result);
            JSONObject json = null;

            Log.e("DEBUG",result) ;
            try {
                json = new JSONObject(result);
                JSONArray comptes = json.getJSONArray("comptes") ;

                compteDAO = new CompteDAO(ConnexionActivity.this) ;
                //compteDAO.clean() ;
                Compte compte = null ;
                for (int i = 0; i < comptes.length() ; i++){
                    compte = new Compte() ;

                    compte.setNumcompte(comptes.getJSONObject(i).getString("NUMCOMPTE"));
                    compte.setSexe(comptes.getJSONObject(i).getString("SEXE"));
                    compte.setPrenom(comptes.getJSONObject(i).getString("PRENOM"));
                    compte.setSolde((float) comptes.getJSONObject(i).getDouble("SOLDE"));
                    compte.setNom(comptes.getJSONObject(i).getString("NOM"));
                    compte.setNummembre(comptes.getJSONObject(i).getString("NUMMEMBRE"));
                    compte.setNumProduit(comptes.getJSONObject(i).getString("NUMPRODUIT"));
                    compte.setProduit(comptes.getJSONObject(i).getString("PRODUIT"));
                    compte.setMise(comptes.getJSONObject(i).getString("MISE"));
                    compte.setMiseLibre(comptes.getJSONObject(i).getString("MISELIBRE"));
                    compte.setType(Compte.TONTINE) ;

                    try {
                        compte.setDatecreation(DAOBase.formatter6.parse(comptes.getJSONObject(i).getString("DATECREATION")));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }



                    compteDAO.add(compte) ;
                }

                dismissDialog(LOAD_DIALOG_ID);

                Toast.makeText(ConnexionActivity.this, R.string.loadsuccess, Toast.LENGTH_SHORT).show();

                refreshData() ;

            } catch (JSONException e) {
                e.printStackTrace();
                dismissDialog(LOAD_DIALOG_ID);
                VALUE = 0 ;
                Toast.makeText(ConnexionActivity.this, R.string.echec, Toast.LENGTH_SHORT).show();
            }

        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showDialog(PROGRESS_DIALOG_ID);
        }
    }

    public class SendRembroussementTask extends AsyncTask<String,Void,String> {

        Membre membre = null ;
        MembreDAO membreDAO = null ;

        OperationDAO operationDAO = null ;
        private ArrayList<Operation> operations;
        Operation operation ;
        private String result;

        CaissierDAO caissierDAO = null ;
        Caissier c  =null;

        public SendRembroussementTask() {

        }

        @Override
        protected String doInBackground(String... urls) {
            //TelephonyManager telephonyManager =  ( TelephonyManager ) getSystemService (Context.TELEPHONY_SERVICE);
            //String imei = telephonyManager.getDeviceId ();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ConnexionActivity.this) ;

            operations = operationDAO.getOpCredit();

            String typop = "" ;


            Log.e("SIZE CREDIT", String.valueOf(operations.size()));
            for (int i = 0;i<operations.size();++i) {
                operation = operations.get(i);

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
                nameValuePairs.add(new BasicNameValuePair("numcredit",operation.getNumcompte()));
                nameValuePairs.add(new BasicNameValuePair("journee", preferences.getString("dateouvert", "")));
                nameValuePairs.add(new BasicNameValuePair("montant",String.valueOf(operation.getMontant())));
                nameValuePairs.add(new BasicNameValuePair("guichet",c.getCodeguichet()));

                result = Utiles.POST(urls[0],nameValuePairs);

                Log.e("RESULT MEMBRE",result) ;
                Log.e("RESULT MEMBRE", String.valueOf(operation.getMontant())) ;

                if (result.equals("NUMPIECE")) Toast.makeText(ConnexionActivity.this, R.string.echec1,Toast.LENGTH_LONG).show();
                if (result.equals("AGENCEDIFF")) Toast.makeText(ConnexionActivity.this, R.string.echec,Toast.LENGTH_LONG).show();
                if (result.equals("NUMPECEECHEC1")) Toast.makeText(ConnexionActivity.this, R.string.echec2,Toast.LENGTH_LONG).show();
                if (result.equals("NUMPECEECHEC2")) Toast.makeText(ConnexionActivity.this, R.string.echec3,Toast.LENGTH_LONG).show();
                if (result.equals("NUMPECEECHEC3")) Toast.makeText(ConnexionActivity.this, R.string.echec4,Toast.LENGTH_LONG).show();
                if (result.contains("OK:") && result.split(":").length==3) {
                    operationDAO.delete(operation.getId()) ;
                    //Toast.makeText(ConnexionActivity.this, R.string.adhesionsuccess,Toast.LENGTH_LONG).show() ;
                }

                VALUE++ ;
                actualiser(VALUE);
            }

            return "" ;

        }


        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            super.onPostExecute(result);


            if (MAX_SIZE==VALUE) {
                operations = operationDAO.getAll() ;
                if (operations.size()>0) Toast.makeText(ConnexionActivity.this, R.string.echec, Toast.LENGTH_SHORT).show();
                else Toast.makeText(ConnexionActivity.this, R.string.trsuccess, Toast.LENGTH_SHORT).show();
            }

            Log.e("DEBUG", result) ;
            if (operationDAO==null) operationDAO = new OperationDAO(ConnexionActivity.this) ;
            dismissDialog(PROGRESS_DIALOG_ID);
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //ConnexionActivity.this.showDialog(MainActivity.PROGRESS_DIALOG_ID);
            if (caissierDAO==null) caissierDAO = new CaissierDAO(ConnexionActivity.this) ;
            if (operationDAO==null) operationDAO = new OperationDAO(ConnexionActivity.this) ;
            c = new CaissierDAO(ConnexionActivity.this).getLast() ;
        }
    }


*/

}
