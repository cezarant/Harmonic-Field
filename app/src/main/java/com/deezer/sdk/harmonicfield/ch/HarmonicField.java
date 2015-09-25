package com.deezer.sdk.harmonicfield.ch;

import android.content.res.Resources;
import android.graphics.Color;
import java.io.IOException;

public class HarmonicField
{    
	private MusicalNote[] notasTipoCampo;
	private MusicalNote[] notasGerais;
	private String[] acordesEncontrados;
	private String[] raizesEncontradas;
	public MusicalNote[] getNotasGerais(){	return notasGerais;}
	public void setNotasGerais(MusicalNote[] notasGerais) {this.notasGerais = notasGerais;}
	private MusicalNote[] notasDoCampo = new MusicalNote[7];
	private void inicializaNotas(Resources res)
	{		
		notasGerais = new MusicalNote[24];
		// Padrao azul de cor
		notasGerais[0] = new MusicalNote("C",Color.parseColor("#097054"));
		notasGerais[1] = new MusicalNote("Cm",Color.parseColor("#097054"));
		notasGerais[2] = new MusicalNote("C#",Color.parseColor("#097054"));
		notasGerais[3] = new MusicalNote("C#m",Color.parseColor("#097054"));
		// Padrao vermelho de cor
		notasGerais[4] = new MusicalNote("D",Color.parseColor("#1663FB"));
		notasGerais[5] = new MusicalNote("Dm",Color.parseColor("#1663FB"));
		notasGerais[6] = new MusicalNote("D#",Color.parseColor("#1663FB"));
		notasGerais[7] = new MusicalNote("D#m",Color.parseColor("#1663FB"));
		// Padrao verde de cor
		notasGerais[8] = new MusicalNote("E",Color.parseColor("#697284"));
		notasGerais[9] = new MusicalNote("Em",Color.parseColor("#697284"));
		notasGerais[10] = new MusicalNote("F",Color.parseColor("#697284"));
		notasGerais[11] = new MusicalNote("Fm",Color.parseColor("#697284"));
		notasGerais[12] = new MusicalNote("F#",Color.parseColor("#697284"));
		notasGerais[13] = new MusicalNote("F#m",Color.parseColor("#697284"));
		// Padrao cinza de cor
		notasGerais[14] = new MusicalNote("G",Color.parseColor("#0D3B96"));
		notasGerais[15] = new MusicalNote("Gm",Color.parseColor("#0D3B96"));
		notasGerais[16] = new MusicalNote("G#",Color.parseColor("#0D3B96"));
		notasGerais[17] = new MusicalNote("G#m",Color.parseColor("#0D3B96"));
		// Padrao Magenta de cor
		notasGerais[18] = new MusicalNote("A",Color.parseColor("#002A1E"));
		notasGerais[19] = new MusicalNote("Am",Color.parseColor("#002A1E"));
		notasGerais[20] = new MusicalNote("A#",Color.parseColor("#002A1E"));
		notasGerais[21] = new MusicalNote("A#m",Color.parseColor("#002A1E"));
		// Padrao Preto de cor
		notasGerais[22] = new MusicalNote("B",Color.BLACK);
		notasGerais[23] = new MusicalNote("Bm",Color.BLACK);
	}
	public MusicalNote[] recuperaNotasTipoCampo(int vi_NotasLiberadas)
	{
		String vs_Nota = "";
		notasTipoCampo = new MusicalNote[raizesEncontradas.length];
		for (int i = 0;i< raizesEncontradas.length;i++)
		{
			vs_Nota = raizesEncontradas[i].split(";")[0];
			notasTipoCampo[i] = new MusicalNote(vs_Nota,Color.BLACK);
			if(i < vi_NotasLiberadas)
			   notasTipoCampo[i].setVbMarcado(true);
		}
		return notasTipoCampo;
	}
	public HarmonicField(int notaRaiz, Resources res, int tipoCampo)
	{
		inicializaNotas(res);
		StringBuilder vs_CamposHarmonicosMaiores = new StringBuilder();
		try {
			vs_CamposHarmonicosMaiores.append(ConfDeezer.leArquivo(res, tipoCampo));
			setRaizesEncontradas(vs_CamposHarmonicosMaiores.toString().split("@"));
			setAcordesEncontrados(getRaizesEncontradas()[notaRaiz].split(";"));
			int contadorCampo = 0; 
			
			for (int j = 0; j < getAcordesEncontrados().length; j++)
			{			
				for (int i = 0; i < notasGerais.length; i++)
				{				
					if(notasGerais[i].get_vs_Nota().equals(getAcordesEncontrados()[j])){
						notasDoCampo[contadorCampo] = notasGerais[i];
						contadorCampo++;
					}
				}				 
			}
		}catch (IOException e){

			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	public boolean todasNotasAcertadas()
	{
		int contAcertos = 0; 
		for (int i = 0; i < notasDoCampo.length; i++)		
			if(notasDoCampo[i].isVbMarcado())
				contAcertos++;		
		return contAcertos == notasDoCampo.length;
	} 	
	public MusicalNote[] getNotasCampoHarmonico(){return notasDoCampo;}

	public String[] getAcordesEncontrados() {
		return acordesEncontrados;
	}

	public void setAcordesEncontrados(String[] acordesEncontrados) {
		this.acordesEncontrados = acordesEncontrados;
	}

	public String[] getRaizesEncontradas() {
		return raizesEncontradas;
	}

	public void setRaizesEncontradas(String[] raizesEncontradas) {
		this.raizesEncontradas = raizesEncontradas;
	}
}
