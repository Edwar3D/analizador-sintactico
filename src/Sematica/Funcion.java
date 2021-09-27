package Sematica;

import java.util.ArrayList;
import java.util.Hashtable;

public class Funcion {
    private String ID;
    private Hashtable<String,Variable> variables = new Hashtable<>();
    private Variable retorno = new Variable();
    private ArrayList<Variable> parametros = new ArrayList<>();
    private String LCA = null;
    private int numlineaInicio = 0;

    public Funcion (String ID, int NUMLINEA){
        this.ID = ID;
        LCA = "";
        numlineaInicio = NUMLINEA;
    }

    public void setVariable(Variable variable) {
       variables.put(variable.getID(), variable);
    }

    public void setRetorno(Variable retorno) {
        this.retorno = retorno;
    }

    public String getID() {
        return ID;
    }

    public Variable getVariable(String ID) {
        return variables.get(ID);
    }

    public Hashtable<String, Variable> getVariables() {
        return variables;
    }

    public Variable getRetorno() {
        return retorno;
    }

    public ArrayList<Variable> getParametros() {
        return parametros;
    }

    public Variable getParametro(String ID){
        boolean fin = false;
        Variable parametro = null;
        int i = 0;
        while (!fin){
            if(parametros.get(i).getID().equals(ID)){
                fin = true;
                parametro = parametros.get(i);
            }
            if(i == parametros.size()-1)
                fin= true;

            i++;
        }
        return parametro;
    }

    public void setParametro(Variable parametro) {
        parametros.add(parametro);
    }

    public boolean existeParametro(String ID){
        boolean fin = false;
        boolean encontrado = false;
        int i = 0;
        if(parametros.size()>0)
        while (!fin){
            if(parametros.get(i).getID().equals(ID)){
                fin = true;
                encontrado = true;
            }
            if(i == parametros.size()-1)
                fin= true;

            i++;
        }

        return encontrado;
    }

    public void setLCA(String LCA) {
        this.LCA = LCA +"\n"+ LCA;
    }

    public String getLCA() {
        return LCA;
    }

    public int getNumlineaInicio() {
        return numlineaInicio;
    }

    @Override
    public String toString() {
        return "Fun{" +
                "ID='" + ID + '\'' +
                "LI= " +numlineaInicio+
                ", PRM=" + parametros.toString()+
                ", VARS=" + variables.toString() +
                ", RETURN=" + retorno +
                '}';
    }
}
