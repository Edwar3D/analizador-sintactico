package Sintaxis;

import Lexico.Gramatica;


import java.util.Hashtable;

public class TablaAnalisis {
    private Hashtable<String, Celda[]> tabla;

    public TablaAnalisis() {

        tabla = new Hashtable<>();

        this.tabla.put("E",  new Celda[]{new Celda("class", "NMC { OFC main ( ) { LCA } }")});
        this.tabla.put("NMC", new Celda[]{new Celda("class","class ID :")});
        this.tabla.put("FC", new Celda[]{new Celda("function","function ID ( PRMS ) { LCA RD } OFC")});
        this.tabla.put("RD", new Celda[]{new Celda("return","return ID ;"), new Celda("}","vacio")});
        this.tabla.put("OFC", new Celda[]{new Celda("function","FC OFC"), new Celda("main","vacio")});
        this.tabla.put("PRMS", new Celda[]{new Celda("ID", "PRM RPRMS"), new Celda(")","vacio")});
        this.tabla.put("PRM", new Celda[]{new Celda("ID", "ID : TD")});
        this.tabla.put("RPRMS", new Celda[]{new Celda(",", ", PRM RPRMS"), new Celda(")","vacio")});
        this.tabla.put("LCA",new Celda[]{new Celda("ID", "OF RESTOLCA"),new Celda("print", "OF RESTOLCA"), new Celda("var", "OF RESTOLCA"),new Celda("if", "OF RESTOLCA"),new Celda("while", "OF RESTOLCA"), new Celda("for", "OF RESTOLCA"), new Celda("switch", "OF RESTOLCA"), new Celda("return", "vacio"), new Celda("}", "vacio"),new Celda("break", "vacio")});
        this.tabla.put("OF",new Celda[]{ new Celda("ID","ID AUX"),new Celda("print", "IMPRIMIR"), new Celda("var", "VARIABLE"),new Celda("if", "ELSEIF"),new Celda("while", "WHILE"), new Celda("for", "FOR"), new Celda("switch", "SWITCH"), });
        this.tabla.put("RESTOLCA", new Celda[]{new Celda("ID", "OF RESTOLCA"),new Celda("print", "OF RESTOLCA"), new Celda("var", "OF RESTOLCA"),new Celda("if", "OF RESTOLCA"),new Celda("while", "OF RESTOLCA"), new Celda("for", "OF RESTOLCA"), new Celda("switch", "OF RESTOLCA"), new Celda("return", "vacio"), new Celda("}", "vacio"), new Celda("break", "vacio"), new Celda("default", "vacio")});
        this.tabla.put("IMPRIMIR", new Celda[]{ new Celda("print", "print ( VALORIMPRIMIR ) ;")});
        this.tabla.put("VALORIMPRIMIR",new  Celda[]{new Celda("ID", "ID"), new Celda("CAD|CH|CF|FL","TIPO")});
        this.tabla.put("VARIABLE", new Celda[]{new Celda("var","var ID VARIABLEVALOR : TD ;")});
        this.tabla.put("VARIABLEVALOR", new Celda[]{ new Celda("=","= TIPO"),new Celda(":","vacio")});
        this.tabla.put("ELSEIF", new Celda[]{new Celda("if","PIF PELSE")});
        this.tabla.put("PIF", new Celda[]{ new Celda("if", "if ( CONDICION ) { LCA }")});
        this.tabla.put("PELSE", new Celda[]{new Celda("ID","vacio"),new Celda("print", "vacio"), new Celda("var", "vacio"),new Celda("if", "vacio"),new Celda("while", "vacio"), new Celda("for", "vacio"), new Celda("switch", "vacio"), new Celda("return", "vacio"), new Celda("else", "else { LCA }"), new Celda("}", "vacio"), new Celda("break", "vacio"), new Celda("default", "vacio")  });
        this.tabla.put("CONDICION",new Celda[]{new Celda("!","! CON"),new Celda("ID", "CON"), new Celda("BOOL","BOOL")  });
        this.tabla.put("CON", new Celda[]{ new Celda("ID","ID PCONDICION"), new Celda("BOOL","BOOL")});
        this.tabla.put("PCONDICION", new Celda[]{ new Celda("OPL","OPL TIPO"), new Celda(")","vacio")});
        this.tabla.put("WHILE", new Celda[]{new Celda("while","while ( CONDICION ) { LCA }")});
        this.tabla.put("FOR", new Celda[]{new Celda("for","for ( DI , CP , ACN ) { LCA }")});
        this.tabla.put("DI", new Celda[]{new Celda("ID", "ID = CF")});
        this.tabla.put("CP", new Celda[]{new Celda("ID", "ID OPL CONF")});
        this.tabla.put("CONF", new Celda[]{new Celda("ID", "ID"), new Celda("CF", "CF"), new Celda("FL", "FL")});
        this.tabla.put("ACN", new Celda[]{new Celda("ID", "ID AC")});
        this.tabla.put("AC", new Celda[]{new Celda("--", "--"), new Celda("++","++"), new Celda("OP","OP = VIF")});
        this.tabla.put("VIF", new Celda[]{new Celda("CF","CF"), new Celda("FL","FL")});
        this.tabla.put("SWITCH", new Celda[]{new Celda("switch","switch ( ID ) { CSS }")});
        this.tabla.put("CSS", new Celda[]{new Celda("case", "CSO OCS CSD")});
        this.tabla.put("CSO", new Celda[]{new Celda("case", "case TIPO : LCA break ;")});
        this.tabla.put("OCS",new Celda[]{new Celda("case", "CSO OCS"), new Celda("default","vacio")});
        this.tabla.put("CSD",new Celda[]{new Celda("default", "default : LCA ")});
        this.tabla.put("LLF",new Celda[]{new Celda("(", "( PARAM ) ;")});
        this.tabla.put("PARAM",new Celda[]{new Celda("ID|CAD|CH|CF|FL|BOOL", "TPARAM RPARAM"), new Celda(")","vacio")});
        this.tabla.put("TPARAM", new Celda[]{new Celda("ID","ID"),new Celda("CAD|CH|CF|FL|BOOL","TIPO")});
        this.tabla.put("TIPO", new Celda[]{new Celda("CAD", "CAD"), new Celda("CH", "CH"), new Celda("CF", "CF"),  new Celda("FL", "FL"),  new Celda("BOOL", "BOOL")});
        this.tabla.put("RPARAM", new Celda[]{new Celda(",", ", TPARAM RPARAM"), new Celda(")","vacio")});
        this.tabla.put("AUX", new Celda[]{ new Celda("(", "LLF"), new Celda("=", "ASIGNACION")});
        this.tabla.put("ASIGNACION", new Celda[]{ new Celda("=","= VALOR ;")});
        this.tabla.put("VALOR", new Celda[]{new Celda("ID", "ID FUNC"), new Celda("CAD|CH|CF|FL|BOOL","TIPO")});
        this.tabla.put("FUNC", new Celda[]{new Celda("(","( PARAM )"), new Celda(";", "vacio")});
    }

    public String[] consulta(String noTerminal, Gramatica entrada){
        System.out.println(noTerminal+" E:"+entrada.toString());
        String celda ="";
        String posicion = buscarValor(entrada);

        Celda[] fila = tabla.get(noTerminal); // obtener la fila de la tabla

        boolean encontrado = false;

        for (Celda value : fila) { //busca si se encuntra en alguna columana
            String columna = value.getcolumna();
            if (columna.contains("|")) { // en caso de CAD|CH|CF|FL
                String[] posiciones = columna.split("\\|");
                for (String opcion : posiciones) {
                    if (opcion.equals(posicion)) {
                        celda = value.getValor();
                        encontrado = true;
                        break;
                    }
                }
            } else {
                if (columna.equals(posicion)) {
                    encontrado = true;
                    celda = value.getValor();
                }
            }

        }

        if(encontrado)
            return celda.split(" ");
        else
            return null;
    }

    public String buscarValor(Gramatica entrada){
        String posicion ="";
        switch (entrada.getSignificado()){
            case "ID":
                posicion = "ID";
                break;
            case "TD":
                posicion = "TD";
                break;
            case "CAD":
                posicion = "CAD";
                break;
            case "CH":
                posicion = "CH";
                break;
            case "CF":
                posicion = "CF";
                break;
            case "FL":
                posicion = "FL";
                break;
            case "BOOL":
                posicion = "BOOL";
                break;
            case "OPL":
                posicion = "OPL";
                break;
            case "OP":
                posicion = "OP";
                break;
            default:
                posicion = entrada.getElemento();
        }

        return posicion;
    }

    public Hashtable<String, Celda[]> getTabla() {
        return tabla;
    }
}
