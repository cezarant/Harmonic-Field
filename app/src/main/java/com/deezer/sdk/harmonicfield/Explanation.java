package com.deezer.sdk.harmonicfield;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 412001 on 11/08/2015.
 */
public class Explanation extends Activity
{
    private Button btnProximo;
    private int contEtapa;
    private TextView txtCampo;
    private TextView txtErrosAcertos;
    private Animation anim;
    private ImageView imgExplicacao;
    private Bundle bundle;
    private Integer vi_tipoExplicacao;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explicacao);
        imgExplicacao = (ImageView) findViewById(R.id.imgBemVindo);
        anim = AnimationUtils.loadAnimation(Explanation.this, R.anim.scale);
        btnProximo = (Button)findViewById(R.id.btnproximo);
        txtCampo = (TextView)findViewById(R.id.txtCampo);
        txtErrosAcertos = (TextView)findViewById(R.id.txtErrosAcertos);

        btnProximo.startAnimation(anim);
        txtCampo.startAnimation(anim);
        txtErrosAcertos.startAnimation(anim);

        bundle = getIntent().getExtras();
        vi_tipoExplicacao = bundle.getInt("tipoexplicacao");
        // Aiai até quando java só vai aceitar inteiro no switch?
        // 1 - Primeiro Acesso
        // 2 - Apresentacao da primeira fase
        // 3 - Fim de uma fase.
        switch (vi_tipoExplicacao)
        {
            case 1:
                btnProximo.setOnClickListener(new Button.OnClickListener(){
                    public void onClick(View v){
                        primeiroAcessoCelular();
                    }
                });
                break;
            case 2:
                txtCampo.setText(getResources().getString(R.string.primeirafase));
                txtErrosAcertos.setText(getResources().getString(R.string.primeirafaseExpl));
                btnProximo.setOnClickListener(new Button.OnClickListener(){
                    public void onClick(View v){
                      apresentacaPrimeiraFase();
                    }
                });
                break;
            case 3:
                txtCampo.setText(getResources().getString(R.string.fimFase1) + " "+
                                 bundle.getInt("numerofase") + " ª " +
                                 getResources().getString(R.string.fase));
                txtErrosAcertos.setText(getResources().getString(R.string.fimFase2));
                imgExplicacao.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.medalha));
                btnProximo.setOnClickListener(new Button.OnClickListener(){
                    public void onClick(View v){
                        fimFase();
                    }
                });
                break;
            case 4:
                txtCampo.setText(getResources().getString(R.string.musicaConcluida));
                txtErrosAcertos.setText(getResources().getString(R.string.musicaConcluida2));
                imgExplicacao.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.falha));
                btnProximo.setOnClickListener(new Button.OnClickListener(){
                    public void onClick(View v){
                        fimFase();
                    }
                });
            case 5:
                PackageInfo pinfo = null;
                try {
                    pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                int versionNumber = pinfo.versionCode;
                txtCampo.setText(getResources().getString(R.string.version) + versionNumber);
                txtErrosAcertos.setText("Email: cezarantsouza@gmail.com");
                imgExplicacao.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.medalha));
                btnProximo.setText(getResources().getString(R.string.backGame));
                btnProximo.setOnClickListener(new Button.OnClickListener(){
                    public void onClick(View v){
                        fimFase();
                    }
                });
                break;
        }
    }
    private void apresentacaPrimeiraFase()
    {
        Intent intentPlayGround;
        // Se o cara marcou a opcao de configurar o jogo
        intentPlayGround = new Intent(Explanation.this, HamonicFieldActivity.class);
        // Informa se o campo harmonico gerado sera menor, maior ou melodico
        intentPlayGround.putExtra("tipocampo",0);
        Explanation.this.startActivity(intentPlayGround);
        this.finish();
    }
    private void fimFase()
    {
        Intent intentPlayGround;
        intentPlayGround = new Intent(Explanation.this, PlacarActivity.class);
        // Informa se o campo harmonico gerado sera menor, maior ou melodico
        intentPlayGround.putExtra("tipocampo", 0);
        this.finish();
    }
    private void primeiroAcessoCelular()
    {
        switch (contEtapa)
        {
            case 0: primeiraEtapa(); break;
            case 1: segundaEtapa(); break;
            case 2: terceiraEtapa(); break;
            case 3: quartaEtapa(); break;
        }
    }
    private void primeiraEtapa(){
        txtCampo.setText(getResources().getString(R.string.primeiraEtapa1));
        txtErrosAcertos.setText(getResources().getString(R.string.primeiraEtapa2));
        btnProximo.setText(getResources().getString(R.string.entendi));
        imgExplicacao.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.a));
        contEtapa++;
    }
    private void segundaEtapa(){
        txtCampo.setText(getResources().getString(R.string.segundaEtapa1));
        txtErrosAcertos.setText(getResources().getString(R.string.segundaEtapa2));
        btnProximo.setText(getResources().getString(R.string.entendi));
        imgExplicacao.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.jonh));
        contEtapa++;
    }
    private void terceiraEtapa(){
        txtCampo.setText(getResources().getString(R.string.terceiraEtapa1));
        txtErrosAcertos.setText(getResources().getString(R.string.terceiraEtapa2));
        btnProximo.setText(getResources().getString(R.string.entendi));
        imgExplicacao.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.paul));
        contEtapa++;
    }
    private void quartaEtapa()
    {
        Intent intentPlayGround;
        // Se o cara marcou a opcao de configurar o jogo
        intentPlayGround = new Intent(Explanation.this, HamonicFieldActivity.class);
        // Informa se o campo harmonico gerado sera menor, maior ou melodico
        intentPlayGround.putExtra("tipocampo",0);
        Explanation.this.startActivity(intentPlayGround);
        this.finish();
    }
}