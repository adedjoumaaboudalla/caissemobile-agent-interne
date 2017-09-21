package caissemobile.com.mediasoft.projet.microfinacaissemobile.activities;

import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceActivity;

import caissemobile.com.mediasoft.projet.microfinacaissemobile.R;

public class ParametreActivity extends PreferenceActivity {

    public static final String TYPE_COMMUNICATION = "type_communication";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
