package Sintaxis;

import Lexico.Gramatica;
import Lexico.Principal;

import java.util.ArrayList;
import java.util.Stack;

public class PredictivoNoRecursivo {
    private int contador;
    private ArrayList<Gramatica> entradas;
    private String resultado;
    private boolean error;
    private String mensaje;
    private String pasos;
    private String contenido ;
    private final TablaAnalisis TABLA = new TablaAnalisis();


    public PredictivoNoRecursivo () {
        contador = 0;
        error = false;
        mensaje = "";
        resultado = "";
        pasos = "";
        contenido = "";
    }

    public void iniciar(String entrada){

        Principal principal = new Principal();
        principal.analizar(entrada);
        this.entradas = principal.getElementos();
        //System.out.println(entradas);

        Stack<String> pila = new Stack<>();

        String [] aux = TABLA.consulta("E",entradas.get(0));
        if(aux != null)
        for (int i = aux.length - 1; i >= 0; i--) {
            pila.push(aux[i]);
        }

        //System.out.println("pila => "+pila.toString());

        while (!pila.isEmpty() && contador<=entradas.size()-1) {
            pasos = pasos +"E:[";
            for (int i = entradas.size()-1; i >= contador; i--) {
                pasos = pasos + entradas.get(i).getElemento()+", ";
            }
            pasos = pasos +  "]\nP:"+pila.toString()+"\n\n";

            String topePila = pila.peek();
            Gramatica topeEntrada = entradas.get(contador);

            if(!TABLA.getTabla().containsKey(topePila)){// revisamos si es un terminal
               // System.out.println(TABLA.buscarValor(topeEntrada));
                if(topePila.equals(TABLA.buscarValor(topeEntrada))){
                    topePila = pila.pop();
                   // System.out.println("->: " + topePila + " | ->: " + topeEntrada.toString()+"\n");
                    generarCodigo(topeEntrada.getElemento());
                    contador++;
                }else {
                    error = true;
                    mensaje = "Error '"+topePila+"' causado por: "+entradas.get(contador).getElemento();
                   // System.out.println("error"+TABLA.buscarValor(topeEntrada)+"|"+topePila);
                    break;
                }
            }else{//es un no terminal
                topePila = pila.pop();
               // System.out.println("P:" + pila.toString() + " -->: " + topePila+"\n");
                String [] consulta = TABLA.consulta(topePila, topeEntrada);
                if(consulta != null){//verificar si hay produccion
                    if(!consulta[0].equals("vacio"))
                        for (int i = consulta.length - 1; i >= 0; i--) {
                            pila.push(consulta[i]);
                            //System.out.println("P:" + pila.toString() + " <--"+"\n");
                        }
                }else {
                    error = true;
                    mensaje = "Error '"+topePila+"' causado por: "+entradas.get(contador).getElemento();
                   // System.out.println("errorT");
                    break;
                }

            }
        }

        if (pila.isEmpty() && contador == entradas.size()){
            System.out.println("==Cadena ACEPTADA=="+"\n");
            error = false;
            mensaje = mensaje +"\n\t==SINTAXIS CORRECTA==";
            //iniciar Semantica
        }else{
            System.out.println("==Cadena NO ACEPTADA=="+"\n");
            mensaje= mensaje +"\n"+ "\t==EJECION FALLIDA==";
            error = true;
        }

        pasos = pasos +"E:[";
        for (int i = entradas.size()-1; i > contador; i--) {
            pasos = pasos + entradas.get(i).getElemento()+", ";
        }
        pasos = pasos +  "]\nP:"+pila.toString()+"\n\n";
    }

    public String getPasos() {
        return pasos;
    }

    public String getMensaje() {
        return mensaje;
    }

    public ArrayList<Gramatica> getEntradas() {
        return entradas;
    }

    public boolean isError() {
        return error;
    }

    private void generarCodigo(String segmento){
        if(segmento.charAt(0)==';')
            contenido = contenido  +"¬";
        else if(segmento.charAt(0)=='{' ||segmento.charAt(0)=='}')
            contenido = contenido + segmento +"¬";
        else if( segmento.equals("(") || segmento.equals(")") || segmento.equals(","));
        else{
            contenido = contenido + segmento +"#";
        }

    }


    public String getContenido() {
        return contenido;
    }
}
