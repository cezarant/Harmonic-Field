package com.deezer.sdk.harmonicfield.ch;

import java.io.Serializable;

public class MusicalNote implements Serializable
{
	private boolean vbMarcado; 
	private String _vs_Nota;	
	private int _valorCor;
	public String get_vs_Nota(){
		return _vs_Nota;
	}
	public void set_vs_Nota(String _vs_Nota)
	{
		this._vs_Nota = _vs_Nota;
	}		
	public MusicalNote(String as_Nota, int as_valorCor)
	{		
		this._vs_Nota = as_Nota;		
		this._valorCor = as_valorCor;
	}
	public boolean isVbMarcado() {
		return vbMarcado;
	}
	public void setVbMarcado(boolean vbMarcado) {
		this.vbMarcado = vbMarcado;
	}
	public int get_valorCor() {
		return _valorCor;
	}
	public void set_valorCor(int _valorCor) {
		this._valorCor = _valorCor;
	} 	
}
