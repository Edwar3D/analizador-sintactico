package Lexico;

public class Gramatica {
    String elemento;
    String significado;

    public Gramatica(String elemento, String significado) {
        this.elemento = elemento;
        this.significado = significado;
    }

    public String getElemento() {
        return elemento;
    }

    public void setElemento(String elemento) {
        this.elemento = elemento;
    }

    public String getSignificado() {
        return significado;
    }

    public void setSignificado(String significado) {
        this.significado = significado;
    }

    @Override
    public String toString() {
        return  elemento +": " + significado ;
    }
}
