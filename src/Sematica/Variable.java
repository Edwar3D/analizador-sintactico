package Sematica;

public class Variable {
    private String ID;
    private String TD;
    private Object valor;

    public Variable(){ }

    public Variable (String ID, String TIPODEDATO, Object VALOR){
        this.ID = ID;
        this.TD = TIPODEDATO;
        this.valor = VALOR;
        /*if(valor != null)
            System.out.println("V>>>>"+valor+"--"+valor.getClass().toString());*/
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setTD(String TD) {
        this.TD = TD;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }

    public String getID() {
        return ID;
    }

    public String getTD() {
        return TD;
    }

    public Object getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return "Var{" +
                "ID='" + ID + '\'' +
                ", TD='" + TD + '\'' +
                ", valor=" + valor +
                '}';
    }
}
