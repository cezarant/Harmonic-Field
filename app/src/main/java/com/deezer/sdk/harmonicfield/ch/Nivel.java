package com.deezer.sdk.harmonicfield.ch;

public class Nivel{   
	public int id_Imagem;
    public int iVelocidadeBolinha; 
    public int iQtdBolinhas;
    public static long recuperaRadioDoNivelLiberado(int notasLiberadas)
    {
        long radioEscolhida = 0;
        // Este e o lugar onde as rádios de cada nivel
        // sao buscadas
        switch(notasLiberadas){
            // do
            case 1: radioEscolhida =  1352830347; break;
            // do sustenido
            case 2: radioEscolhida =  1352830347; break;
            // ré
            case 3: radioEscolhida =  1352832187; break;
            // ré sustenido
            case 4: radioEscolhida =  1366969287; break;
            // mi
            case 5: radioEscolhida =  1366970297; break;
            // fa
            case 6: radioEscolhida =  1366972327; break;
            // fa sustenido
            case 7: radioEscolhida =  1366974187; break;
            // sol
            case 8: radioEscolhida =  1366976337; break;
            // sol sustenido
            case 9: radioEscolhida =  1367390167; break;
            // lá
            case 10: radioEscolhida =  1367391947; break;
            // lá sustenido
            case 11: radioEscolhida =  1367397207; break;
            // sí
            case 12: radioEscolhida =  1367398837; break;
        }
        return radioEscolhida;
    }

}
