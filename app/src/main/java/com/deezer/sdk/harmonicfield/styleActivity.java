package com.deezer.sdk.harmonicfield;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.deezer.sdk.harmonicfield.ui.styleListAdapter;

/**
 * Created by cezar on 07/09/2015.
 */
public class styleActivity  extends Activity
{
    private Bundle bundle;
    private String[] vetEstilos;
    private Integer[] vetImagens;
    private ListView list;
    private int vi_OpcaoCampoHarmonico;
    public  void gerenciaEstilosLiberados()
    {
        vetEstilos = new String[3];
        vetImagens = new Integer[3];

        vetEstilos[0] = "Rock Internacional";
        vetImagens[0] = R.drawable.cadeadoaberto;

        vetEstilos[1] = "Gospel";
        vetImagens[1] = R.drawable.cadeado;

        vetEstilos[2] = "MPB";
        vetImagens[2] = R.drawable.cadeado;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.styleactivity);
        bundle = getIntent().getExtras();
        vi_OpcaoCampoHarmonico = bundle.getInt("tipocampo");
        gerenciaEstilosLiberados();
        styleListAdapter adapter=new styleListAdapter(this, vetEstilos, vetImagens, 1);
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
    private void abreJogo(int posicao)
    {
        Intent intentPlayGround = null;
        if(posicao > 0) {
            Toast.makeText(getApplicationContext(), "Este estilo não está liberado ainda.",
                    Toast.LENGTH_SHORT).show();
        }else {
            intentPlayGround = new Intent(styleActivity.this, PlacarActivity.class);
            intentPlayGround.putExtra("tipocampo", vi_OpcaoCampoHarmonico);
        }
        styleActivity.this.startActivity(intentPlayGround);
        this.finish();
    }
}


