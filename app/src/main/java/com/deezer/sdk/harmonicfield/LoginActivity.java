package com.deezer.sdk.harmonicfield;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;


import com.deezer.sdk.model.Permissions;
import com.deezer.sdk.network.connect.SessionStore;

/**
 * Presents two buttons, one to log in with a deezer account and access a user's playlists, the
 * other to play deezer radios
 *
 * @author Deezer
 */
public class LoginActivity extends BaseActivity {

    /**
     * Permissions requested on Deezer accounts.
     * <p/>
     * cf : http://developers.deezer.com/api/permissions
     */
    protected static final String[] PERMISSIONS = new String[]{
            Permissions.BASIC_ACCESS, Permissions.OFFLINE_ACCESS
    };


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.btnInicarTreino).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(LoginActivity.this, Main.class);
                // Intent intent = new Intent(LoginActivity.this, Cognatos.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btnSobre).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {
                Intent intentPlayGround;
                // Se o cara marcou a opcao de configurar o jogo
                intentPlayGround = new Intent(LoginActivity.this, Explanation.class);
                intentPlayGround.putExtra("tipoexplicacao",5);
                LoginActivity.this.startActivity(intentPlayGround);
            }
        });

        SessionStore sessionStore = new SessionStore();

        if (sessionStore.restore(mDeezerConnect, this)){
            Toast.makeText(this, "Already logged in !", Toast.LENGTH_LONG).show();
        }
    }
}
