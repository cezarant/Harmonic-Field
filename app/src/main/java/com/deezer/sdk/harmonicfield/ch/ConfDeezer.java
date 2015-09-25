package com.deezer.sdk.harmonicfield.ch;

import android.content.res.Resources;
import android.os.Environment;

import com.deezer.sdk.harmonicfield.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ConfDeezer implements Serializable
{
	private int vi_notasLiberadas = 3;
	private int vi_FrameRate;
	private Resources res;
	private int contEtapaConfiguracao;
	private int _vAlturaCampo = 3;
    public static int totalMusicasCadaNivel = 3;
    private int _raio = 0;
    private Ball[] vetBalls;
	public int get_vAlturaCampo(){return _vAlturaCampo;}
	public void set_vAlturaCampo(int _vAlturaCampo){_vAlturaCampo = _vAlturaCampo;}
	public static final String vs_NomeArquivoConf = "confdeezer.txt";
    public static String vs_Exception = "";
	public void resetaConfiguracoes(Resources vo_Res)
	{
		_raio = 0;
		setVi_FrameRate(20);
		setContEtapaConfiguracao(0);
		gravaBolinhas(this,vo_Res);
	}
	public static Ball[] inicializaJogo(Nivel va_Nivel)
	{
		Ball[] balls =  new Ball[2];
		for (int i = 0; i < balls.length; i++)
		{
			balls[i] = new Ball();
			balls[i].xVelocity = va_Nivel.iVelocidadeBolinha;
			balls[i].yVelocity = va_Nivel.iVelocidadeBolinha;
            balls[i].raio = 30;
		}		
		return balls;
	} 	
	public static Nivel lerConf(int idNivel)
	{	
		Nivel nivelAtual = new Nivel();
		switch(idNivel)
		{
			case 0:					
				nivelAtual.iQtdBolinhas = 3;
				nivelAtual.iVelocidadeBolinha = 11;
				break;	
			case 1:			
				nivelAtual.iQtdBolinhas =4; 
				nivelAtual.iVelocidadeBolinha = 6;				
				break;
			case 2:				
				nivelAtual.iQtdBolinhas = 5;
				nivelAtual.iVelocidadeBolinha = 10;
				break;			
		}
		return nivelAtual; 
	}
	public static void gravarArquivo(String vs_Bolinhas) throws FileNotFoundException
	{
		FileOutputStream fos;
		File arq = new File(Environment.getExternalStorageDirectory(), ConfDeezer.vs_NomeArquivoConf);
		byte[] vetBytes = vs_Bolinhas.getBytes();
		fos = new FileOutputStream(arq);
		try {
			fos.write(vetBytes);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    public static boolean arquivoConfExiste(String vs_Arquivo)
    {
        File file = new File(Environment.getExternalStorageDirectory(),
                             ConfDeezer.vs_NomeArquivoConf);
        return file.exists();
    }
    public static String gravaBolinhas(ConfDeezer vo_ConfDeezer,Resources vo_Res)
	{
		ObjectOutput out = null;
		File arq = new File(Environment.getExternalStorageDirectory(), ConfDeezer.vs_NomeArquivoConf);
		try{
			arq.createNewFile();
			out = new ObjectOutputStream(new FileOutputStream(arq));
			out.writeObject(vo_ConfDeezer);
			out.close();
		} catch (FileNotFoundException e){
		     return e.getMessage();
		} catch (IOException e) {
			return e.getMessage();
		}
		return vo_Res.getString(R.string.bolinhaconfigurada);
	}
	public static ConfDeezer lerConfiguracaoArquivo(String nomeArq)
	{
		ConfDeezer vo_ConfDeezer = new ConfDeezer();
		File vo_File = new File(Environment.getExternalStorageDirectory(),nomeArq);
		try {
			ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(vo_File));
			vo_ConfDeezer = (ConfDeezer) objectInputStream.readObject();
			objectInputStream.close();
		} catch (IOException e){
			vo_ConfDeezer.vs_Exception = e.getMessage();
		} catch (ClassNotFoundException e){
            vo_ConfDeezer.vs_Exception = e.getMessage();
		}
		return vo_ConfDeezer;
	}
	public static String leArquivo(Resources res,int vi_TipoCampo) throws IOException
	{
		final Resources resources = res;
		InputStream inputStream = null;
		switch (vi_TipoCampo)
		{
			case 1: inputStream = resources.openRawResource(R.raw.camposharmonicosmaiores); break;
			case 2: inputStream = resources.openRawResource(R.raw.camposharmonicosmenores); break;
			case 3: inputStream = resources.openRawResource(R.raw.camposharmonicosmaioresmelodicos); break;
			default:inputStream = resources.openRawResource(R.raw.camposharmonicosmaiores); break;
		}

		if(inputStream == null)
		   throw new IOException(res.getString(R.string.warncampo));

		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder vs_Retorno = new StringBuilder();

		try {
			String line;
			while ((line = reader.readLine()) != null)
				vs_Retorno.append(line);
		} catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			reader.close();
		}
		return vs_Retorno.toString();
	}
	public int getVi_notasLiberadas(){return vi_notasLiberadas;}
	public void setVi_notasLiberadas(int vi_notasLiberadas){this.vi_notasLiberadas = vi_notasLiberadas;}
	public Ball[] getVetBalls() {
		return vetBalls;
	}
	public void setVetBalls(Ball[] vetBalls) {
		this.vetBalls = vetBalls;
	}
    public int get_raio() { return _raio;}
    public void set_raio(int _raio){ this._raio = _raio;}
	public int getVi_FrameRate() {
		return vi_FrameRate;
	}
	public void setVi_FrameRate(int vi_FrameRate) {
		this.vi_FrameRate = vi_FrameRate;
	}
	public int getContEtapaConfiguracao() {
		return contEtapaConfiguracao;
	}
	public void setContEtapaConfiguracao(int contEtapaConfiguracao) {
		this.contEtapaConfiguracao = contEtapaConfiguracao;
	}
}
