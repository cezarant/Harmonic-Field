package com.deezer.sdk.harmonicfield;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.deezer.sdk.model.Track;
import com.deezer.sdk.network.connect.SessionStore;
import com.deezer.sdk.network.request.event.DeezerError;
import com.deezer.sdk.network.request.event.OAuthException;
import com.deezer.sdk.player.RadioPlayer;
import com.deezer.sdk.player.event.RadioPlayerListener;
import com.deezer.sdk.player.exception.TooManyPlayersExceptions;
import com.deezer.sdk.player.networkcheck.WifiAndMobileNetworkStateChecker;
import com.deezer.sdk.harmonicfield.ch.Ball;
import com.deezer.sdk.harmonicfield.ch.ConfDeezer;
import com.deezer.sdk.harmonicfield.ch.Nivel;
import com.deezer.sdk.harmonicfield.ui.HarmonicFieldView;

import android.content.Intent;
import android.graphics.Color;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ouvidoperfeito.harmonicfield.R;

public class HamonicFieldActivity extends PlayerActivity implements RadioPlayerListener
{
	private Button btnAvancar;
    private Random numRand = new Random();
    private String vs_Musica = "-";
    private int qtdEtapas =0;
    private HarmonicFieldView vi_Campo;
    public TimerTask task;
    public final Handler handTimer = new Handler();
    public Timer timerAtual = new Timer();
    private ConfDeezer vo_confDeezer;
    private long vl_Track;
    private Bundle bundle;
    private EditText[] txtGrausCampo;
    private Animation anim;
    private TextView txtExplicacao1;
    private static long RADIO_SOUNDTRACKS = 0L;
    private RadioPlayer mRadioPlayer;
    private Equalizer mEqualizer;

	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        // restore existing deezer Connection
        new SessionStore().restore(mDeezerConnect, this);
        setContentView(R.layout.harmonicfield);
        vo_confDeezer = ConfDeezer.lerConfiguracaoArquivo(ConfDeezer.vs_NomeArquivoConf);
        createPlayer(Nivel.recuperaRadioDoNivelLiberado(vo_confDeezer.getVi_notasLiberadas()));
        bundle = getIntent().getExtras();
        anim = AnimationUtils.loadAnimation(HamonicFieldActivity.this, R.anim.scale);
        txtExplicacao1 = (TextView) findViewById(R.id.txtCampoDescricao);
        carregaPlacar();
        vi_Campo = (HarmonicFieldView) findViewById(R.id.imgCampo);
        vi_Campo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                gerenciaToutch(event);
                return false;
            }
        });
        // Se o usuario esta configurando as balls
        if(vo_confDeezer.getContEtapaConfiguracao() > 0)
        {
           iniciaMovimentoBolinhas();
           if(vo_confDeezer.getContEtapaConfiguracao() == 2)
             mRadioPlayer.playRadio(Nivel.recuperaRadioDoNivelLiberado(vo_confDeezer.getVi_notasLiberadas()));
        }else {
            preparaTelaConfiguracao();
            mRadioPlayer.stop();
        }
    }
    /**
     * Creates the Radio Player
     */
    private void createPlayer(long alRadioNumber)
    {
        try {
            setupPlayerUI(alRadioNumber);
            //build the player
            mRadioPlayer = new RadioPlayer(getApplication(), mDeezerConnect,
                    new WifiAndMobileNetworkStateChecker());
            mRadioPlayer.addPlayerListener(this);
            setAttachedPlayer(mRadioPlayer);
        }catch (OAuthException e){ handleError(e); }
        catch (DeezerError e)   { handleError(e); }
        catch (TooManyPlayersExceptions e) { handleError(e); }
    }
    private void setupPlayerUI(long alRadioNumber)
    {
        // for now hide the player
        setPlayerVisible(false);
        RADIO_SOUNDTRACKS = alRadioNumber;
        // disable unnecesary buttons
        setButtonEnabled(mButtonPlayerSeekBackward, false);
        setButtonEnabled(mButtonPlayerSeekForward, false);
        setButtonEnabled(mButtonPlayerSkipBackward, false);
        setButtonEnabled(mButtonPlayerSkipForward, false);
        setButtonEnabled(mButtonPlayerStop, false);
        setButtonEnabled(mButtonPlayerPause, false);
        setButtonEnabled(mButtonPlayerRepeat, false);
    }
    private void preparaTelaConfiguracao()
    {
        vi_Campo.vo_BallConfiguracao = new Ball();
        vi_Campo.iniciaTelaConfiguracao(vo_confDeezer);
        txtExplicacao1.setText(getResources().getString(R.string.configurarBolinha));
        for (EditText aTxtGrausCampo : txtGrausCampo) aTxtGrausCampo.setVisibility(View.GONE);
        Button btnAumentar = (Button) findViewById(R.id.btnAumentarTam);
        btnAumentar.setVisibility(View.VISIBLE);
        btnAumentar.setText("Aumentar");
        btnAumentar.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                aumentar();
            }
        });

        Button btnDiminuir = (Button) findViewById(R.id.btnDiminuirTam);
        btnDiminuir.setVisibility(View.VISIBLE);
        btnDiminuir.setText("Diminuir");
        btnDiminuir.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                diminuir();
            }
        });

        Button btnConcluido = (Button) findViewById(R.id.btnConcluido);
        btnConcluido.setVisibility(View.VISIBLE);
        btnConcluido.setText("Concluído");
        btnConcluido.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                concluir();
            }
        });
    }
    private void aumentar(){
        if (vo_confDeezer.getContEtapaConfiguracao() == 0)
            vi_Campo.raioTela++;
        else
            vi_Campo.FRAME_RATE--;
    }
    private void diminuir(){
        if (vo_confDeezer.getContEtapaConfiguracao() == 0)
            vi_Campo.raioTela--;
        else
            vi_Campo.FRAME_RATE++;
    }
    private void concluir()
    {
        if (vo_confDeezer.getContEtapaConfiguracao() == 0)
        {
            txtExplicacao1.setText(getResources().getString(R.string.configurarVelocidade));
            vo_confDeezer.set_raio(vi_Campo.raioTela);
            vo_confDeezer.setContEtapaConfiguracao(1);
            vo_confDeezer.setVi_FrameRate(25);
            vo_confDeezer.setVi_notasLiberadas(1);
            ConfDeezer.gravaBolinhas(vo_confDeezer, getResources());
            iniciaMovimentoBolinhas();
        }else{
            ConfDeezer vo_ConfDeezer = ConfDeezer.lerConfiguracaoArquivo(ConfDeezer.vs_NomeArquivoConf);
            vo_ConfDeezer.setVi_FrameRate(vi_Campo.FRAME_RATE);
            vo_ConfDeezer.setContEtapaConfiguracao(2);
            vo_confDeezer.setVi_notasLiberadas(1);
            Toast.makeText(getApplicationContext(),
                           ConfDeezer.gravaBolinhas(vo_ConfDeezer, getResources()),
                           Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }
    private void carregaPlacar()
    {
        txtGrausCampo = new EditText[7];
        txtGrausCampo[0] = (EditText) findViewById(R.id.editText1);
        txtGrausCampo[1] = (EditText) findViewById(R.id.editText2);
        txtGrausCampo[2] = (EditText) findViewById(R.id.editText3);
        txtGrausCampo[3] = (EditText) findViewById(R.id.editText4);
        txtGrausCampo[4] = (EditText) findViewById(R.id.editText5);
        txtGrausCampo[5] = (EditText) findViewById(R.id.editText6);
        txtGrausCampo[6] = (EditText) findViewById(R.id.editText7);
    }
    public void iniciaMovimentoBolinhas()
    {
         vi_Campo.iniciarSistema(bundle.getInt("tipocampo"), true,
                                 ConfDeezer.lerConfiguracaoArquivo(ConfDeezer.vs_NomeArquivoConf));
        ativaTimer();
        colocaGrausCampoHarmonico();
    }
    private boolean configuracaoBolinha(MotionEvent event, ConfDeezer vo_confDeezer)
    {
        if (vi_Campo.vo_BallConfiguracao.usuarioClicouNaBolinha((int) event.getX(),
                                                                (int) event.getY()))
        {
            vo_confDeezer.set_raio(vi_Campo.raioTela);
            vo_confDeezer.setContEtapaConfiguracao(1);
            ConfDeezer.gravaBolinhas(vo_confDeezer, getResources());
            iniciaMovimentoBolinhas();
        } else{
            vi_Campo.raioTela++;
        }
        return false;
    }
    private void controlaVelocidade(MotionEvent event)
    {
        vi_Campo.FRAME_RATE++;
        for (int i = 0; i < vi_Campo.balls.length; i++)
        {
            if(vi_Campo.balls[i].usuarioClicouNaBolinha((int)event.getX(),(int)event.getY()))
            {
                ConfDeezer vo_ConfDeezer = ConfDeezer.lerConfiguracaoArquivo(ConfDeezer.vs_NomeArquivoConf);
                vo_ConfDeezer.setVi_FrameRate(vi_Campo.FRAME_RATE);
                vo_ConfDeezer.setContEtapaConfiguracao(2);
                Toast.makeText(getApplicationContext(), ConfDeezer.gravaBolinhas(vo_ConfDeezer, getResources()),
                               Toast.LENGTH_SHORT).show();
                this.finish();
            }
        }
    }
    private void gerenciaToutch(MotionEvent event)
    {
        if (vo_confDeezer.getContEtapaConfiguracao() == 2)
            gerenciaCliquesBolinha(event);
    }
    private void gerenciaCliquesBolinha(MotionEvent event){
        if (!vi_Campo.vbParar){
            vi_Campo.vb_Acertou = false;
            vi_Campo.clicouQualquerBolinha = 0;
            verificaBolinhaEstaNoCampo(event);
            if (vi_Campo.clicouQualquerBolinha > 0)
                gerenciaErrosEAcertos();

            vi_Campo.vb_Acertou = false;
        }
        colocaGrausCampoHarmonico();
    }
    private void colocaGrausCampoHarmonico()
    {
        for(int i = 0;i < vi_Campo.vo_Campo.getNotasCampoHarmonico().length;i++)
        {
            txtGrausCampo[i].setTextSize(12);
            txtGrausCampo[i].setTextColor(Color.WHITE);
            if(vi_Campo.vo_Campo.getNotasCampoHarmonico()[i].isVbMarcado())
            {
              txtGrausCampo[i].setText(vi_Campo.vo_Campo.getNotasCampoHarmonico()[i].get_vs_Nota());
              txtGrausCampo[i].setBackgroundColor(Color.parseColor("#FF9900"));
            }else{
              txtGrausCampo[i].setBackgroundColor(Color.parseColor("#097054"));
              txtGrausCampo[i].setText("***");
            }
        }
    }
    public void atualizaPlacar()
    {
        String vs_Mensagem;
        if(vi_Campo.contErros == 6)
            vs_Mensagem = getResources().getString(R.string.warnQtdErros);
        else
            vs_Mensagem = getResources().getString(R.string.erros) + ":"+ vi_Campo.contErros;

        txtExplicacao1.setText(vs_Musica + " " +  vs_Mensagem);
    }
    public void verificaBolinhaEstaNoCampo(MotionEvent event)
    {
        for (int i = 0; i < vi_Campo.balls.length; i++)
        {
            if(vi_Campo.balls[i].usuarioClicouNaBolinha((int) event.getX(), (int) event.getY()))
            {
                atualizaPlacar();
                vi_Campo.clicouQualquerBolinha++;
                if(NotaEstaNoCampo(i))
                {
                    txtGrausCampo[i].startAnimation(anim);
                    vi_Campo.vb_Acertou = true;
                    if(vi_Campo.vo_Campo.todasNotasAcertadas())
                        vi_Campo.vbParar = true;

                    i = vi_Campo.balls.length;
                }
            }else{
                txtGrausCampo[i].clearAnimation();
            }
        }
    }
    public boolean NotaEstaNoCampo(int i)
    {
        boolean notaEstaNoCampo = false;
        for (int j = 0; j < vi_Campo.vo_Campo.getNotasCampoHarmonico().length; j++)
        {
            vi_Campo.vs_NotaProcurada = vi_Campo.balls[i].get_musicalNoteBolinha().get_vs_Nota();
            if (vi_Campo.vo_Campo.getNotasCampoHarmonico()[j].get_vs_Nota().
                                 equals(vi_Campo.balls[i].get_musicalNoteBolinha().get_vs_Nota()))
            {
                // 	Se a nota faz parte do campo harmonico
                vi_Campo.vo_Campo.getNotasCampoHarmonico()[j].setVbMarcado(true);
                notaEstaNoCampo = true;
                // sai do loop.
                j = vi_Campo.vo_Campo.getNotasCampoHarmonico().length;
            }
        }
        return notaEstaNoCampo;
    }
    public void gerenciaErrosEAcertos()
    {
        if(!vi_Campo.vb_Acertou)
        {
            if(vi_Campo.contErros < vi_Campo.valorTotalErros){
                vi_Campo.contErros++;
            }else{
                if(qtdEtapas < 2)
                {
                    qtdEtapas++;
                    vi_Campo.reiniciaSistema();
                }else{
                    mostraTelaFinalizacao(3,1);
                }
            }
        }
    }
    private void mostraTelaFinalizacao(int aiTipoExplicacao,int numeroFase)
    {
        Intent intentPlayGround;
        // Se o cara marcou a opcao de configurar o jogo
        intentPlayGround = new Intent(HamonicFieldActivity.this, Explanation.class);
        intentPlayGround.putExtra("tipoexplicacao",aiTipoExplicacao);
        intentPlayGround.putExtra("numerofase",numeroFase);
        vi_Campo.vbUsuarioConcluiuTudo = false;
        HamonicFieldActivity.this.startActivity(intentPlayGround);
        this.finish();
    }
    private void buscaMensagensDaTelaFilha()
    {
        // Como eu nao consegui uma maneira digna de enviar mensagens da tela filha
        // criei esta gambiarra.
        if(vi_Campo.vbUsuarioConcluiuTudo)
            mostraTelaFinalizacao(3,1);

        if(vi_Campo.trocarMusica){
            mRadioPlayer.skipToNextTrack();
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.musicatrocada),
                          Toast.LENGTH_SHORT).show();
            btnAvancar =  (Button) findViewById(R.id.btnAumentarTam);
            btnAvancar.setText(getResources().getString(R.string.forcaTroca));
            btnAvancar.setVisibility(View.VISIBLE);
            btnAvancar.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    mRadioPlayer.skipToNextTrack();
                }
            });
            vi_Campo.vbTrocarNota= true;
            vi_Campo.trocarMusica = false;
        }
    }
    // Timer que fica mudando as notas que estao sendo mostradas na tela.
    public void ativaTimer()
    {
        task = new TimerTask(){	public void run()
        {handTimer.post(new Runnable() { public void run(){
            vi_Campo.vbTrocarNota = true;
            buscaMensagensDaTelaFilha();
        }});}};
        timerAtual.schedule(task, 6000, 6000);
    }
    @Override
    protected void onResume(){
        super.onResume();
        if(vo_confDeezer.getContEtapaConfiguracao() == 2)
        {
            mRadioPlayer.playRadio(RadioPlayer.RadioType.PLAYLIST,
                    Nivel.recuperaRadioDoNivelLiberado(vo_confDeezer.getVi_notasLiberadas()));
        }
    }
    @Override
    public void onTooManySkipsException()
    {
         Toast.makeText(getApplicationContext(),
                 getResources().getString(R.string.manySkipDeezer),Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onAllTracksEnded(){}

    @Override
    public void onPlayTrack(Track track)
    {
        vs_Musica = track.getTitle();
        vl_Track = track.getId();
        atualizaPlacar();
        // Começa o movimento das bolinhas de novo.
        vi_Campo.trocandoMusica = false;
        vi_Campo.vbParar = false;
        vi_Campo.reiniciaSistema();
        colocaGrausCampoHarmonico();
        vi_Campo.resetBolinhas();
        btnAvancar =  (Button) findViewById(R.id.btnAumentarTam);
        btnAvancar.setVisibility(View.GONE);
    }
    @Override
    public void onTrackEnded(Track track)
    {
        //if(track.getId() == vl_Track)
            //mostraTelaFinalizacao(4,0);
    }
    @Override
    public void onRequestException(Exception e, Object o){ }
}
