package com.deezer.sdk.harmonicfield;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.deezer.sdk.harmonicfield.ch.ConfDeezer;

public class Main extends Activity
{
    private Intent intentPlayGround;
    private ConfDeezer vo_confDeezer;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
      super.onCreate(savedInstanceState);
      // Se é a primeira vez que o usúario está entrando no sistema, já envia para a
      // tela de explicação do sistema de cara.
      if(!(ConfDeezer.arquivoConfExiste(ConfDeezer.vs_NomeArquivoConf))){
          intentPlayGround = new Intent(Main.this, Explanation.class);
          intentPlayGround.putExtra("tipoexplicacao",1);
          Main.this.startActivity(intentPlayGround);
          this.finish();
      }else{
          vo_confDeezer = ConfDeezer.lerConfiguracaoArquivo(ConfDeezer.vs_NomeArquivoConf);
          setContentView(R.layout.mainsplash);
          Button btnMaior = (Button) findViewById(R.id.btnCampoMaior);
          Button btnMenor = (Button) findViewById(R.id.btnCampoMenorHarmonico);
          Button btnMenorMel = (Button) findViewById(R.id.btnCampoMenorMelodico);
          Button btnConfg = (Button) findViewById(R.id.btnCampoConfBolinha);

          btnMaior.setOnClickListener(new Button.OnClickListener() {
              public void onClick(View v) {
                  iniciaJogo(1);
              }
          });
          btnMenor.setOnClickListener(new Button.OnClickListener() { public void onClick(View v) {        iniciaJogo(2);     } });
          btnMenorMel.setOnClickListener(new Button.OnClickListener() {
              public void onClick(View v) {
                  iniciaJogo(3);
              }
          });
          btnConfg.setOnClickListener(new Button.OnClickListener() {
              public void onClick(View v) {
                  iniciaJogo(0);
              }
          });
      }
    }
    public void iniciaJogo(int vi_Opcao)
    {
        // Se o cara marcou a opcao de configurar o jogo
        if((vi_Opcao == 0))
        {
            vo_confDeezer.resetaConfiguracoes(getResources());
            intentPlayGround = new Intent(Main.this, Explanation.class);
            intentPlayGround.putExtra("tipoexplicacao",1);
        }else{
            intentPlayGround = new Intent(Main.this, PlacarActivity.class);
            // Informa se o campo harmonico gerado sera menor, maior ou melodico
            intentPlayGround.putExtra("tipocampo", vi_Opcao);
        }
        Main.this.startActivity(intentPlayGround);
    }
}
