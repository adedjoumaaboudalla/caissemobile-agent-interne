package caissemobile.com.mediasoft.projet.microfinacaissemobile.activities;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import caissemobile.com.mediasoft.projet.microfinacaissemobile.R;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.fragments.AutresInfoFragment;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.fragments.DemandeCreditFragment;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.fragments.EpargneObligatoireFragment;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.fragments.InfoMontantDureeFragment;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.fragments.InfoProduitFragment;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.fragments.InfosClientFragment;
import caissemobile.com.mediasoft.projet.microfinacaissemobile.fragments.RecapitulatifFragment;

public class DemandeCreditActivity extends AppCompatActivity implements DemandeCreditFragment.OnFragmentInteractionListener, EpargneObligatoireFragment.OnFragmentInteractionListener,  RecapitulatifFragment.OnFragmentInteractionListener, AutresInfoFragment.OnFragmentInteractionListener, InfoProduitFragment.OnFragmentInteractionListener, InfosClientFragment.OnFragmentInteractionListener, InfoMontantDureeFragment.OnFragmentInteractionListener {

    DemandeCreditFragment demandeCreditFragment = null ;
    InfoProduitFragment infoProduitFragment = null ;
    InfosClientFragment infosClientFragment = null ;
    InfoMontantDureeFragment infoMontantDureeFragment = null ;
    EpargneObligatoireFragment epargneObligatoireFragment = null ;
    AutresInfoFragment autresInfoFragment = null ;
    RecapitulatifFragment recapitulatifFragment = null ;
    private int start = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demande_credit);


        init() ;
        setupFragments() ;
        setupToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId() ;

        if (id == android.R.id.home) {
            quitter();
            return true ;
        }

        return super.onOptionsItemSelected(item);

    }

    private void init() {

    }

    /*
        initialiser les fragments
     */
    private void setupFragments() {
        final FragmentManager fm = getSupportFragmentManager();
        if (this.demandeCreditFragment == null) {
            this.demandeCreditFragment = DemandeCreditFragment.newInstance(null,null);
        }

        if (this.infoProduitFragment == null) {
            this.infoProduitFragment = InfoProduitFragment.newInstance(null,null);
        }

        if (this.infosClientFragment == null) {
            this.infosClientFragment = InfosClientFragment.newInstance(null,null);
        }

        if (this.infoMontantDureeFragment == null) {
            this.infoMontantDureeFragment = InfoMontantDureeFragment.newInstance(null,null);
        }

        if (this.epargneObligatoireFragment == null) {
            this.epargneObligatoireFragment = EpargneObligatoireFragment.newInstance(null,null);
        }

        if (this.autresInfoFragment == null) {
            this.autresInfoFragment = AutresInfoFragment.newInstance(null,null);
        }

        if (this.recapitulatifFragment == null) {
            this.recapitulatifFragment = RecapitulatifFragment.newInstance(null,null);
        }
        showFragment(demandeCreditFragment);
    }


    /*
        Afficher un fragment
     */
    private void showFragment(final Fragment fragment) {
        if (fragment == null)         return;
        final FragmentManager fm = getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();
        //ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        ft.replace(R.id.frame, fragment);
        ft.commit();
    }


    private void showFragment(final Fragment fragment,int sens) {
        final FragmentManager fm = getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();
// We can also animate the changing of fragment

        if (sens == 0)ft.setCustomAnimations(R.anim.left_to_right_in, R.anim.left_to_right_out);
        else ft.setCustomAnimations(R.anim.right_to_left_in, R.anim.right_to_left_out);
        ft.replace(R.id.frame, fragment);
        ft.commit();
    }



    /*
    Initialiser la toolbar
     */
    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Show menu icon
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.mipmap.ic_back);
        ab.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {
        if (uri.equals(Uri.parse(DemandeCreditFragment.SUIVANT))){
            showFragment(infoProduitFragment,0);
        }
        else if (uri.equals(Uri.parse(InfoProduitFragment.SUIVANT))){
            showFragment(infosClientFragment,0);
        }
        else if (uri.equals(Uri.parse(InfoProduitFragment.PRECEDANT))){
            showFragment(demandeCreditFragment,1);
        }
        else if (uri.equals(Uri.parse(InfosClientFragment.SUIVANT))){
            showFragment(infoMontantDureeFragment,0);
        }
        else if (uri.equals(Uri.parse(InfosClientFragment.PRECEDANT))){
            showFragment(infoProduitFragment,1);
        }
        else if (uri.equals(Uri.parse(InfoMontantDureeFragment.SUIVANT))){
            showFragment(epargneObligatoireFragment,0);
        }
        else if (uri.equals(Uri.parse(InfoMontantDureeFragment.PRECEDANT))){
            showFragment(infosClientFragment,1);
        }
        else if (uri.equals(Uri.parse(EpargneObligatoireFragment.SUIVANT))){
            showFragment(autresInfoFragment,0);
        }
        else if (uri.equals(Uri.parse(EpargneObligatoireFragment.PRECEDANT))){
            showFragment(infoMontantDureeFragment,1);
        }
        else if (uri.equals(Uri.parse(AutresInfoFragment.SUIVANT))){
            showFragment(recapitulatifFragment,0);
        }
        else if (uri.equals(Uri.parse(AutresInfoFragment.PRECEDANT))){
            showFragment(epargneObligatoireFragment,1);
        }
    }




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            quitter();
            return true ;
        }
        return super.onKeyDown(keyCode, event);
    }




    private void quitter() {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this) ;
            builder.setTitle(getString(R.string.credi)) ;
            builder.setMessage(getString(R.string.credit)) ;
            builder.setPositiveButton(getString(R.string.quiter), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }) ;
            builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }) ;
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

}
