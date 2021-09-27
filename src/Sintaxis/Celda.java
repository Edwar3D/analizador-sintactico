package Sintaxis;

public class Celda {
    private String columna;
    private String valor;

    public Celda (String columna, String valor){
        this.columna = columna;
        this.valor = valor ;
    }

    public String getcolumna() {
        return columna;
    }

    public String getValor() {
        return valor;
    }
}
