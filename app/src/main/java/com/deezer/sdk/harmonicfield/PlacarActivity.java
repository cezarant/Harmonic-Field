package com.deezer.sdk.harmonicfield;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.deezer.sdk.harmonicfield.ch.HarmonicField;
import com.deezer.sdk.harmonicfield.ch.ConfDeezer;
import com.deezer.sdk.harmonicfield.ch.MusicalNote;
import com.deezer.sdk.harmonicfield.ui.CustomListAdapter;
/**
 * Created by 412001 on 13/08/2015.
 */
public class PlacarActivity extends Activity
{
    private ListView list;
    private String[] vetItens;
    private HarmonicField vo_Campo;
    private ConfDeezer vo_confDeezer;
    private String vs_Valor;
    private Integer[] vetImagens;
    private Bundle bundle;
    private MusicalNote[] vetNotasTipoCampo;
    public void gerenciaPlacar(int notasLiberadas,int vi_TipoCampo)
    {
      vo_Campo = new HarmonicField(vo_confDeezer.getVi_notasLiberadas(),this.getResources(),vi_TipoCampo);
      vetNotasTipoCampo = vo_Campo.recuperaNotasTipoCampo(notasLiberadas);
      vetItens = new String[vetNotasTipoCampo.length];
      vetImagens = new Integer[vetNotasTipoCampo.length];
      vetItens[0] = getResources().getString(R.string.apresentacao);
      vetImagens[0] = R.drawable.cadeado;
      for (int i=1;i< vetNotasTipoCampo.length;i++)
      {
          vetItens[i] = new String();
          vs_Valor = getResources().getString(R.string.fase)+ " : "+ (i + 1) + " - " +
                     vetNotasTipoCampo[i].get_vs_Nota();
          if(!vetNotasTipoCampo[i].isVbMarcado())
          {
            vs_Valor += " "+ getResources().getString(R.string.bloqueado);
            vetImagens[i] = R.drawable.cadeadoaberto;
          }else{
              vetImagens[i] = R.drawable.cadeado;
          }
          vetItens[i] = vs_Valor;
      }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vo_confDeezer = ConfDeezer.lerConfiguracaoArquivo(ConfDeezer.vs_NomeArquivoConf);
        bundle = getIntent().getExtras();
        gerenciaPlacar(vo_confDeezer.getVi_notasLiberadas(),bundle.getInt("tipocampo"));
        CustomListAdapter adapter=new CustomListAdapter(this, vetItens,
                                                        vetImagens, vo_confDeezer.getVi_notasLiberadas());
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id)
            {
                abreJogo(position);
            }
        });
    }
    private void abreJogo(int vi_posicao)
    {
        Intent intentPlayGround = null;
        if (vi_posicao < vo_confDeezer.getVi_notasLiberadas())
        {
            // Se for a primeira vez que o caboclo estiver acessando.
            if(vi_posicao == 0){
                intentPlayGround = new Intent(PlacarActivity.this, Explanation.class);
                intentPlayGround.putExtra("tipoexplicacao",2);
            }else{
                intentPlayGround = new Intent(PlacarActivity.this, HamonicFieldActivity.class);
            }
            // Informa se o campo harmonico gerado sera menor, maior ou melodico
            intentPlayGround.putExtra("tipocampo", bundle.getInt("tipocampo"));
            PlacarActivity.this.startActivity(intentPlayGround);
            this.finish();
        }
    }
}
