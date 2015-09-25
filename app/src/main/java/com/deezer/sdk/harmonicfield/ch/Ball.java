package com.deezer.sdk.harmonicfield.ch;


import java.io.Serializable;

public class Ball implements Serializable {
	public int xBolinha = -1;
	public int yBolinha = -1;
	public int xVelocity = 6;
	public int yVelocity = 2;
	public boolean bLinhaReta = false;
	public int raio = 30;
	private MusicalNote _musicalNoteBolinha;
	public boolean bProxima= false;
	public int get_xBolinha() {
		return xBolinha;
	}
	public void set_xBolinha(int _xBolinha) {
		this.xBolinha = _xBolinha;
	}
	public int get_yBolinha() {
		return yBolinha;
	}
	public void set_yBolinha(int _yBolinha) {
		this.yBolinha = _yBolinha;
	}
	public MusicalNote get_musicalNoteBolinha(){
		return _musicalNoteBolinha;
	}
	public void set_musicalNoteBolinha(MusicalNote _musicalNoteBolinha){
		this._musicalNoteBolinha = _musicalNoteBolinha;
	}
	public boolean usuarioClicouNaBolinha(int posX, int posY)
	{		
		int distanceX = this.xBolinha - posX;
		int distanceY = this.yBolinha - posY;
		return Math.sqrt((distanceX * distanceX) + (distanceY * distanceY)) <= this.raio;
	} 	
}
