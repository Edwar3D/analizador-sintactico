package Lexico;

public class PalabraReservada {
    private String resultado;
    private String [][] palabras = {//palabras reservadas
            {"class","Asigancion de la clase"},
            {"main","Para crear la clase principal"},
            {"function","Declaración de una función"},
            {"return","Asignación de retorno de datos"},
            {"print","Impresion por consola"},
            {"var","Creacion de una variable"},
            {"if","Evaluar una expresión condicional"},
            {"else","valor de defaut con la sentencia if"},
            {"while","Secuencia de contro repetitiva"},
            {"for","Secuencia de contro repetitiva"},
            {"switch","Mecanismo de control de selección"},
            {"case","Evaluación de caso de la variable"},
            {"default","Caso por default la variable"},
            {"break","Paro forzado de la sentencia"},
            {"int","Variables de tipo entero"}, //tipos de datos
            {"float","Variables decimales "},
            {"char","Variables de tipo Caracter"},
            {"string","Variables de cadenas de Caracteres"},
            {"boolean","Variables de valor TRUE o FALSE"},
            {"double","Varibales de tipo double"},
    };

    public void evaluarCadena(String cadena){
        resultado = "";
        int estadoInicio = 0;
        int estadoActual = estadoInicio;
        int estadoFinalPR[] ={1,2,3,4,5,6,7,8,9,10,11,12,13,14};
        int estadoFinalTD[] = {14,15,17,16,18,19,20};
        boolean fin = false;

        while(fin==false){
            if(estadoActual == 0){
                if(cadena.equals(palabras[0][0])){
                    estadoActual = 1;
                }
                if(cadena.equals(palabras[1][0])){
                    estadoActual = 2;
                }
                if(cadena.equals(palabras[2][0])){
                    estadoActual = 3;
                }
                if(cadena.equals(palabras[3][0])){
                    estadoActual = 4;
                }
                if(cadena.equals(palabras[4][0])){
                    estadoActual = 5;
                }
                if(cadena.equals(palabras[5][0])){
                    estadoActual = 6;
                }
                if(cadena.equals(palabras[6][0])){
                    estadoActual = 7;
                }
                if(cadena.equals(palabras[7][0])){
                    estadoActual = 8;
                }
                if(cadena.equals(palabras[8][0])){
                    estadoActual = 9;
                }
                if(cadena.equals(palabras[9][0])){
                    estadoActual = 10;
                }
                if(cadena.equals(palabras[10][0])){
                    estadoActual = 11;
                }
                if(cadena.equals(palabras[11][0])){
                    estadoActual = 12;
                }
                if(cadena.equals(palabras[12][0])){
                    estadoActual = 13;
                }
                if(cadena.equals(palabras[13][0])){
                    estadoActual = 14;
                }
                if(cadena.equals(palabras[14][0])){
                    estadoActual = 15;
                }
                if(cadena.equals(palabras[15][0])){
                    estadoActual = 16;
                }
                if(cadena.equals(palabras[16][0])){
                    estadoActual = 17;
                }
                if(cadena.equals(palabras[17][0])){
                    estadoActual = 18;
                }
                if(cadena.equals(palabras[18][0])){
                    estadoActual = 19;
                }
                if(cadena.equals(palabras[19][0])){
                    estadoActual = 20;
                }
                else{
                    break;
                }
                continue;
            }
            if(estadoActual == 1){
                fin = true;
            }
            if(estadoActual == 2){
                fin = true;
            }
            if(estadoActual == 3){
                fin = true;
            }
            if(estadoActual == 4){
                fin = true;
            }
            if(estadoActual == 5){
                fin = true;
            }
            if(estadoActual == 6){
                fin = true;
            }
            if(estadoActual == 7){
                fin = true;
            }
            if(estadoActual == 8){
                fin = true;
            }
            if(estadoActual == 9){
                fin = true;
            }
            if(estadoActual == 10){
                fin = true;
            }
            if(estadoActual == 11){
                fin = true;
            }
            if(estadoActual == 12){
                fin = true;
            }
            if(estadoActual == 13){
                fin = true;
            }
            if(estadoActual == 14){
                fin = true;
            }
            if(estadoActual == 15){
                fin = true;
            }
            if(estadoActual == 16){
                fin = true;
            }
            if(estadoActual == 17){
                fin = true;
            }
            if(estadoActual == 18){
                fin = true;
            }
            if(estadoActual == 19){
                fin = true;
            }
            if(estadoActual == 20){
                fin = true;
            }
        }
        if(estadoActual == estadoFinalPR[0]  ||
                estadoActual == estadoFinalPR[1]  ||
                estadoActual == estadoFinalPR[2]  ||
                estadoActual == estadoFinalPR[3]  ||
                estadoActual == estadoFinalPR[4]  ||
                estadoActual == estadoFinalPR[5]  ||
                estadoActual == estadoFinalPR[6]  ||
                estadoActual == estadoFinalPR[7]  ||
                estadoActual == estadoFinalPR[8]  ||
                estadoActual == estadoFinalPR[9]  ||
                estadoActual == estadoFinalPR[10] ||
                estadoActual == estadoFinalPR[11] ||
                estadoActual == estadoFinalPR[12] ||
                estadoActual == estadoFinalPR[13]
        ){
            resultado = "Palabra reservada";
        }
        else{
            if(estadoActual == estadoFinalTD[0]  ||
                    estadoActual == estadoFinalTD[1]  ||
                    estadoActual == estadoFinalTD[2]  ||
                    estadoActual == estadoFinalTD[3]  ||
                    estadoActual == estadoFinalTD[4]  ||
                    estadoActual == estadoFinalTD[5]  ||
                    estadoActual == estadoFinalTD[6]
            ){
                resultado = "TD";
            }
            else{
                resultado = null;
            }
        }
    }

    public String getResultado(){
        return resultado;
    }

    public String[][] palabrasReservadas(){
        String [][] palabrasRes = new String[13][2];
        for (int i = 0; i < 14; i++) {
            palabrasRes[i][0] = palabras[i][0];
            palabrasRes[i][1] = palabras[i][1];
        }
        return palabrasRes;
    }

    public String[][] tipoDatos(){
        String [][] tipos = new String[8][2];
        int contador=0;
        for (int i = 14; i < palabras.length; i++) {
            tipos[contador][0] = palabras[i][0];
            tipos[contador][1] = palabras[i][1];
            contador++;
        }

        return tipos;
    }

}
