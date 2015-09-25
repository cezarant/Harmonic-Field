package com.deezer.sdk.harmonicfield.ui;

import java.util.Random;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.Toast;

import com.deezer.sdk.harmonicfield.R;
import com.deezer.sdk.harmonicfield.ch.Ball;
import com.deezer.sdk.harmonicfield.ch.ConfDeezer;
import com.deezer.sdk.harmonicfield.ch.HarmonicField;
import com.deezer.sdk.harmonicfield.ch.Nivel;
import com.deezer.sdk.harmonicfield.ch.MusicalNote;


@SuppressLint("ClickableViewAccessibility")
public class HarmonicFieldView extends ImageView
{
	public Ball vo_BallConfiguracao;
    public boolean trocarMusica = false;
    public boolean trocandoMusica = false;
    private int numeroMusicas = 0;
	public int raioTela = 20;
	public int clicouQualquerBolinha = 0;
	public int qtdInteracoes = 0;
	public boolean vbConfInicial;
	public int valorRaioYDuos;
	private Context mContext; 
	public Nivel nivelAtual = new Nivel();
	public Paint ptBolinha = new Paint();
	public Ball[] balls;
	public Handler h;
	public int FRAME_RATE = 10;
	public int idNivel = 1;
	public Random numRand = new Random();
	public boolean vbTrocarNota;
	public HarmonicField vo_Campo;
	public int contAcertos = 0;
    public boolean vbUsuarioConcluiuTudo;
	public int contErros = 0;
	public boolean vbParar = false;
	public boolean vb_Acertou = false;
	public String vs_NotaProcurada = "";
	public Context contexto_app;
	public int valorTotalErros = 6;
	public Vibrator vo_Vibrador;
	public ConfDeezer vo_confDeezer;
	public Typeface tf;
	public boolean NotaEstaNoCampo(int i)
	{
		boolean notaEstaNoCampo = false;
		for (int j = 0; j < vo_Campo.getNotasCampoHarmonico().length; j++)
		{
			vs_NotaProcurada = balls[i].get_musicalNoteBolinha().get_vs_Nota();
			if(vo_Campo.getNotasCampoHarmonico()[j].get_vs_Nota().
				        equals(balls[i].get_musicalNoteBolinha().get_vs_Nota()))
			{
				// 	Se a nota faz parte do campo harmônico
				vo_Campo.getNotasCampoHarmonico()[j].setVbMarcado(true);
				Toast.makeText(mContext,getResources().getString(R.string.notaEncontrada) +
						       vo_Campo.getNotasCampoHarmonico()[j].get_vs_Nota(),
						       Toast.LENGTH_SHORT).show();
				notaEstaNoCampo = true;
				// sai do loop.
				j = vo_Campo.getNotasCampoHarmonico().length;
			}
		}
		return notaEstaNoCampo;
	}
	/**
	 * Procedimento que gera uma nota randomica para ficar navegando na tela.
	 * */
	public MusicalNote recuperaNotaRandomica(int valorRandomico)
	{
		MusicalNote musicalNoteRetorno = null;
		// Se ja tiver passado um tempinho sem vir nenhuma nota que pertence ao campo harmonico
		// esta variavel dá uma forcadinha de barra.
		if(qtdInteracoes > 15)
		{
			for (int i = 0; i < vo_Campo.getNotasCampoHarmonico().length; i++)						
				if(!vo_Campo.getNotasCampoHarmonico()[i].isVbMarcado())				
					musicalNoteRetorno = vo_Campo.getNotasCampoHarmonico()[i];
			qtdInteracoes = 0; 
		}else{			
			musicalNoteRetorno = vo_Campo.getNotasGerais()[valorRandomico];
			qtdInteracoes++;
		}
		return musicalNoteRetorno;
	}
	public HarmonicFieldView(Context context, AttributeSet attrs)
	{ 		
		super(context, attrs);
		contexto_app = context;
		mContext = context;		
    }	
	public void iniciarSistema(int vi_TipodeCampo,boolean primeiraExecucao,ConfDeezer a_ConfDeezer)
	{
        vo_confDeezer = a_ConfDeezer;
		FRAME_RATE = vo_confDeezer.getVi_FrameRate();
        iniciaJogoNormalmente(vi_TipodeCampo, primeiraExecucao);
	}
	public void iniciaTelaConfiguracao(ConfDeezer aConfDeezer)
	{
		vo_confDeezer = aConfDeezer;
		h = new Handler();

	}
	public void iniciaJogoNormalmente(int vi_TipodeCampo, boolean primeiraExecucao)
	{
		vo_Campo = new HarmonicField(numRand.nextInt(vo_confDeezer.getVi_notasLiberadas()),
                                      this.getResources(),vi_TipodeCampo);
		lerConf(idNivel);
        liberaApenasAlgumasNotas();
        // Formata o layout que as balls vão ter.
		// -------------------------------------------------------------------------------------
		ptBolinha.setTextAlign(Align.CENTER);
		tf = Typeface.createFromAsset(this.getResources().getAssets(), "robotoregular.ttf");
		ptBolinha.setTypeface(tf);
		// -------------------------------------------------------------------------------------
		qtdInteracoes = 0;
		for (int j = 0; j < balls.length; j++)
            balls[j].set_musicalNoteBolinha(recuperaNotaRandomica(numRand.nextInt(24)));

		vbTrocarNota = false;
		if(primeiraExecucao)
        {
            h = new Handler();
        }else{
            vbParar = false;
            h.postDelayed(r, FRAME_RATE);
        }
	}
    private void liberaApenasAlgumasNotas(){
        // Libera duas notas para facilitar o trabalho do usuario.
        vo_Campo.getNotasCampoHarmonico()[1].setVbMarcado(true);
        vo_Campo.getNotasCampoHarmonico()[4].setVbMarcado(true);
    }

    public Runnable r = new Runnable()
	{
		@Override
		public void run(){invalidate();}
	};
	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas c)
	{					
		if(!trocandoMusica){
            if(!vbParar)
            {
                switch (vo_confDeezer.getContEtapaConfiguracao())
                {
                    case 0: configuraTamanhoBolinha(c);	break;
                    case 1: desenhaBolinhaNormalmente(c);break;
                    default: desenhaBolinhaNormalmente(c);break;
                }
            }else{
                nivelConcluido();
            }
        }
	}
	private void desenhaBolinhaNormalmente(Canvas c)
	{
		for (int i = 0; i < balls.length; i++)
        {
            calculaTrajetoriaBolinha(i);
            if (vbTrocarNota)
                for (int j = 0; j < balls.length; j++)
                    balls[j].set_musicalNoteBolinha(recuperaNotaRandomica(numRand.nextInt(24)));

            ptBolinha.setColor(balls[i].get_musicalNoteBolinha().get_valorCor());
            c.drawCircle(balls[i].get_xBolinha(), balls[i].get_yBolinha(),
					     vo_confDeezer.get_raio(), ptBolinha);
            ptBolinha.setColor(Color.WHITE);
            c.drawText(balls[i].get_musicalNoteBolinha().get_vs_Nota(), balls[i].get_xBolinha(),
                       balls[i].get_yBolinha(), ptBolinha);
        }
		vbTrocarNota = false;
		h.postDelayed(r,FRAME_RATE);
	}
    public void resetBolinhas(){
        h.postDelayed(r,FRAME_RATE);
    }

	private void configuraTamanhoBolinha(Canvas c)
	{
		vo_BallConfiguracao = new Ball();
		ptBolinha.setColor(Color.parseColor("#FF9900"));
		vo_BallConfiguracao.set_xBolinha(getWidth() / 2);
		vo_BallConfiguracao.set_yBolinha(getHeight() / 2);
		c.drawCircle(vo_BallConfiguracao.get_xBolinha(), vo_BallConfiguracao.get_yBolinha(),
				raioTela, ptBolinha);
		h.postDelayed(r, 30);
	}
	public void nivelConcluido()
	{
	    if(numeroMusicas < ConfDeezer.totalMusicasCadaNivel)
           tocaMaisMusicas();
        else
            fimDasMusicas();
	}
    private void fimDasMusicas()
    {
        vo_confDeezer.set_vAlturaCampo(numRand.nextInt(vo_confDeezer.getVi_notasLiberadas() + 1));
        if((vo_confDeezer.getVi_notasLiberadas() + 1) > 12)
            vo_confDeezer.setVi_notasLiberadas(1);
        else
            vo_confDeezer.setVi_notasLiberadas(vo_confDeezer.getVi_notasLiberadas() + 1);
        // Grava o arquivo para caso o usuario pare de jogar
        ConfDeezer.gravaBolinhas(vo_confDeezer, getResources());
        contErros = 0;
        contAcertos = 0;
        vbUsuarioConcluiuTudo = true;
    }
    private void tocaMaisMusicas()
    {
        String vs_Mensagem;
        vs_Mensagem = getResources().getString(R.string.etapaConcluida);
        vs_Mensagem += (numeroMusicas +1) + " ª "+
                getResources().getString(R.string.musica);
        numeroMusicas++;
        new AlertDialog.Builder(contexto_app).
                setTitle(getResources().getString(R.string.continuarJogo)).setMessage(vs_Mensagem)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        reiniciaSistema();
                        iniciarSistema(1, false, vo_confDeezer);
                        trocarMusica = true;
                        trocandoMusica = true;
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, getResources().getString(R.string.atemais),
                                Toast.LENGTH_SHORT).show();
                    }
                }).setIcon(android.R.drawable.ic_dialog_alert).show();
    }

    public void calculaTrajetoriaBolinha(int indice)
	{		
		if (balls[indice].xBolinha <0 && balls[indice].yBolinha <0)
		{
			iniciaTrajeto(indice);
		}else{
			balls[indice].xBolinha += balls[indice].xVelocity;
			
			if(!balls[indice].bLinhaReta)
				balls[indice].yBolinha += balls[indice].yVelocity;
	    		
			if (pegouCantoDireito(balls[indice].xBolinha,indice) ||
					(pegouCantoEsquerdo(balls[indice].xBolinha,indice)))
				balls[indice].xVelocity = balls[indice].xVelocity*-1;
				
			if ((pegouChao(balls[indice].yBolinha,indice)) ||
					(pegouTopo(balls[indice].yBolinha,indice)))
				balls[indice].yVelocity = balls[indice].yVelocity*-1;
		}		
	}	
	public void iniciaTrajeto(int indice)
	{
		int vi_Proximidade = indice;	
		if(!balls[indice].bProxima)
			vi_Proximidade = (vi_Proximidade * 2) + 1;
		else 
			vi_Proximidade = 1;
		
		balls[indice].xBolinha = this.getWidth() / (vi_Proximidade);
		balls[indice].yBolinha = this.getHeight()/ (vi_Proximidade);
	}
	public void reiniciaSistema()
	{
		contErros = 0;
		setContAcertos(0);
		for (int j2 = 0; j2 < vo_Campo.getNotasCampoHarmonico().length; j2++)
			vo_Campo.getNotasCampoHarmonico()[j2].setVbMarcado(false);
		// Libera duas notas para facilitar o trabalho do usuario.
        liberaApenasAlgumasNotas();
	}
	// Configura as configurações 
	public void lerConf(int idNivel)
	{		
		nivelAtual = ConfDeezer.lerConf(idNivel);
		balls = ConfDeezer.inicializaJogo(nivelAtual);
	}
	/** Identifica se pegou em algum dos cantos do celular **/	
	public boolean pegouTopo(int valorYbolinha, int indice){return ((valorYbolinha < numRand.nextInt(5)) && (indice > -1));}
	public boolean pegouChao(int valorYbolinha, int indice){return (valorYbolinha > this.getHeight() && (indice > -1));}
	public boolean pegouCantoEsquerdo(int valorXBolinha,int indice){return (valorXBolinha < numRand.nextInt(10));}
	public boolean pegouCantoDireito(int valorXbolinha,int indice){return (valorXbolinha > this.getWidth() && (indice > -1));}
	public int getContAcertos(){return contAcertos;}
	public void setContAcertos(int contAcertos) {	this.contAcertos = contAcertos;}	
}