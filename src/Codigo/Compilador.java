package Codigo;

import Sematica.Funcion;
import Sematica.Variable;

import java.io.*;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Observable;

public class Compilador extends Observable {
    private int NUMLINEA;
    private String[] CODIGO;
    private String SALIDA;
    private Hashtable<String, Variable> tablaVariables = new Hashtable<>();
    private Hashtable<String, Funcion> tablaFunciones = new Hashtable<>();
    private String FUNCIONACTUAL;

    public Compilador(Hashtable<String, Variable> tablaVariables, Hashtable<String, Funcion> tablaFunciones) {
        SALIDA = "";
        this.tablaVariables = tablaVariables;
        this.tablaFunciones = tablaFunciones;
        FUNCIONACTUAL = null;
    }

    public void ejecutar(String entrada) {
        boolean fin = false;
        CODIGO = entrada.split("¬");
        NUMLINEA = 0;
        while (!fin) {
            String linea[] = CODIGO[NUMLINEA].split("#");
            buscarEjecucion(linea);
            NUMLINEA++;
            if (NUMLINEA == CODIGO.length) {
                fin = true;
            }
        }
    }

    private void buscarEjecucion(String[] lineas) {
        switch (lineas[0]) {
            case "print":
                IMPRIMIR(lineas[1]);
                break;
            case "if":
                IFELSE(lineas);
                break;
            case "for":
                FOR(lineas);
                break;
            case "while":
                WHILE(lineas);
                break;
            case "switch":
                SWITCH(lineas);
            case "class":
            case "}":
            case "main":
            case "else":
                System.out.println("x:" + Arrays.toString(lineas));
                break;
            case "var":
                nuevaVariable(lineas);
                break;
            case "return":
                System.out.println("R:" + Arrays.toString(lineas));
                break;
            case "function":
                crearFuncion(lineas);
                break;
            default:
                if (tablaFunciones.containsKey(lineas[0]))
                    LLAMARFUNCION(lineas);
                else
                    asignarValor(lineas);

        }
    }

    private void asignarValor(String[] lineas) {
        System.out.println("asig: " + Arrays.toString(lineas));
        if (tablaFunciones.containsKey(lineas[2])) {
            //llamar funcion para signarle el valor
            String[] lineasFuncion = new String[lineas.length - 2];
            System.arraycopy(lineas, 2, lineasFuncion, 0, lineas.length - 2);
            Object resultado = LLAMARFUNCION(lineasFuncion);
            Variable miVariable = tablaVariables.get(lineas[0]);
            miVariable.setValor(resultado);

        } else {
            Variable miVariable = null;

            if (FUNCIONACTUAL != null) {
                // se asigna valor en un variable de una funcion
                Funcion mifuncion = tablaFunciones.get(FUNCIONACTUAL);
                if (mifuncion.existeParametro(lineas[0]))
                    miVariable = mifuncion.getParametro(lineas[0]);
                else if (mifuncion.getVariables().containsKey(lineas[0]))
                    miVariable = mifuncion.getVariable(lineas[0]);

                miVariable.setValor(validarValor(miVariable.getTD(), lineas[2]));

            } else {
                // se asigna valor en un variable global
                miVariable = tablaVariables.get(lineas[0]);
                System.out.println("--->" + miVariable + " " + lineas[2]);
                miVariable.setValor(validarValor(miVariable.getTD(), lineas[2]));
            }
            tablaVariables.replace(miVariable.getID(), miVariable);
        }
    }

    private Object validarValor(String TD, String valor) {
        Object newValor = null;
        try {
            switch (TD) {
                case "int":
                    newValor = Integer.parseInt(valor);
                    break;
                case "float":
                    newValor = Float.parseFloat(valor);
                    break;
                case "string":
                    if (valor.matches("\"[a-zA-Z0-9 ]*\""))
                        newValor = valor.subSequence(1, valor.length() - 1);
                    break;
                case "char":
                    if (valor.matches("'[a-zA-Z0-9 ]'"))
                        newValor = valor.charAt(1);
                    break;
                case "boolean":
                    if (valor.equals("true") || valor.equals("false"))
                        newValor = Boolean.parseBoolean(valor);
                    break;
            }
        } catch (Exception e) {
            System.out.println(" tipo incorrecto " + e);
        }

        return newValor;
    }

    private void IMPRIMIR(String valor) {
        System.out.println("IMP:  " + valor);
        if (FUNCIONACTUAL == null) {
            if (tablaVariables.containsKey(valor)) {
                addSalida(tablaVariables.get(valor).getValor().toString());
            } else {
                if (valor.matches("\"[a-zA-Z0-9 ]*\"") || valor.matches("\'[a-zA-Z0-9 ]*\'")) {
                    addSalida(valor.substring(1, valor.length() - 1));
                } else {
                    addSalida(valor);
                }
            }
        } else {
            System.out.println("------>" + valor);
            if (tablaFunciones.get(FUNCIONACTUAL).getVariables().containsKey(valor)) {
                addSalida(tablaFunciones.get(FUNCIONACTUAL).getVariables().get(valor).getValor().toString());
            } else if (tablaFunciones.get(FUNCIONACTUAL).existeParametro(valor)) {
                addSalida(tablaFunciones.get(FUNCIONACTUAL).getParametro(valor).getValor().toString());
            } else {
                addSalida(valor);
            }
        }


    }

    private boolean IFELSE(String[] lineas) {
        System.out.println("if->" + Arrays.toString(lineas));
        boolean ISTRUE = CONDICION(lineas);
        System.out.println("IF RESULTDO " + ISTRUE + Arrays.toString(lineas));

        //salir de la declaracionde if
        NUMLINEA++;
        lineas = CODIGO[NUMLINEA].split("#");
        while (!lineas[0].equals("}")) {
            System.out.println("if:" + Arrays.toString(lineas));
            if (ISTRUE) {
                buscarEjecucion(lineas);
            } else {
                leerLineas(lineas);
            }
            NUMLINEA++;
            lineas = CODIGO[NUMLINEA].split("#");
        }

        //lineas de else
        NUMLINEA += 1;
        lineas = CODIGO[NUMLINEA].split("#");
        if (lineas[0].equals("else")) {
            NUMLINEA += 1;
            lineas = CODIGO[NUMLINEA].split("#");
            while (!lineas[0].equals("}")) {
                System.out.println("else: " + Arrays.toString(lineas));
                if (!ISTRUE)
                    buscarEjecucion(lineas);
                else
                    leerLineas(lineas);
                NUMLINEA++;
                lineas = CODIGO[NUMLINEA].split("#");
            }

        } else {
            NUMLINEA -= 1;
        }

        return ISTRUE;
    }

    private void FOR(String[] lineas) {
        System.out.println("for->" + Arrays.toString(lineas));
        String IDCONTADOR = lineas[1];
        String accion = lineas[8];
        float incremento = 0;
        if (lineas.length > 10)
            incremento = Float.parseFloat(lineas[10]);
        float contador = (int) tablaVariables.get(lineas[1]).getValor();
        float limite = 0;
        if (tablaVariables.containsKey(lineas[6])) {
            limite = Float.parseFloat(tablaVariables.get(lineas[6]).getValor().toString());
        } else {
            limite = Float.parseFloat(lineas[6]);
        }
        int lineaActual = NUMLINEA + 1;
        float i = contador;
        switch (lineas[5]) {
            case "<":
                while (i < limite) {
                    i = cuerpoFor(i, accion, incremento, lineaActual, IDCONTADOR);
                }
                break;
            case "<=":
                while (i <= limite) {
                    i = cuerpoFor(i, accion, incremento, lineaActual, IDCONTADOR);
                }
                break;
            case ">":
                while (i > limite) {
                    i = cuerpoFor(i, accion, incremento, lineaActual, IDCONTADOR);
                }
                break;
            case ">=":
                while (i >= limite) {
                    i = cuerpoFor(i, accion, incremento, lineaActual, IDCONTADOR);
                }
                break;
            case "==":
                while (i == limite) {
                    i = cuerpoFor(i, accion, incremento, lineaActual, IDCONTADOR);
                }
                break;
            case "!=":
                while (i != limite) {
                    i = cuerpoFor(i, accion, incremento, lineaActual, IDCONTADOR);
                }
                break;
        }
    }

    private float cuerpoFor(float i, String accion, float incremento, int lineaActual, String IDCONTADOR) {
        //ejecutar las lineas
        NUMLINEA = lineaActual;
        tablaVariables.get(IDCONTADOR).setValor(i);
        String[] lineas = CODIGO[NUMLINEA].split("#");
        //cuerpo de for
        while (!lineas[0].equals("}")) {
            System.out.println("for: " + i + "->" + Arrays.toString(lineas));
            buscarEjecucion(lineas);
            NUMLINEA++;
            lineas = CODIGO[NUMLINEA].split("#");
        }
        switch (accion) {
            case "++":
                i++;
                break;
            case "--":
                i--;
                break;
            case "+":
                i += incremento;
                break;
            case "-":
                i -= incremento;
                break;
            case "/":
                i /= incremento;
                break;
            case "*":
                i = i * incremento;
                break;
        }
        return i;
    }

    private boolean CONDICION(String[] lineas) {
        int posicion = 0;
        boolean negado = false;
        boolean ISTRUE = false;
        Variable variable = null;
        if (FUNCIONACTUAL == null) {
            variable = tablaVariables.get(lineas[posicion + 1]);
        } else {
            Funcion miFuncion = tablaFunciones.get(FUNCIONACTUAL);
            if (miFuncion.getVariables().containsKey(lineas[posicion + 1])) {
                variable = miFuncion.getVariable(lineas[posicion + 1]);
            } else {
                variable = miFuncion.getParametro(lineas[posicion + 1]);
            }
        }

        if (lineas.length <= 4) {

            if (variable.getValor().toString().equals("true")) {
                ISTRUE = true;
            }
        } else if (variable.getTD().equals("int") || variable.getTD().equals("float")) {
            switch (lineas[posicion + 2]) {
                case "==":
                    if (Float.parseFloat(variable.getValor().toString()) == Float.parseFloat(lineas[posicion + 3]))
                        ISTRUE = true;
                    break;
                case "!=":
                    if (Float.parseFloat(variable.getValor().toString()) != Float.parseFloat(lineas[posicion + 3]))
                        ISTRUE = true;
                    break;
                case "<=":
                    if (Float.parseFloat(variable.getValor().toString()) <= Float.parseFloat(lineas[posicion + 3]))
                        ISTRUE = true;
                    break;
                case ">=":
                    if (Float.parseFloat(variable.getValor().toString()) >= Float.parseFloat(lineas[posicion + 3]))
                        ISTRUE = true;
                    break;
                case ">":
                    if (Float.parseFloat(variable.getValor().toString()) > Float.parseFloat(lineas[posicion + 3]))
                        ISTRUE = true;
                    break;
                default:
                    if (Float.parseFloat(variable.getValor().toString()) < Float.parseFloat(lineas[posicion + 3]))
                        ISTRUE = true;
                    break;
            }

        } else if (variable.getTD().equals("boolean")) {
            if (lineas[posicion + 2].equals("==")) {
                if (variable.getValor().toString().equals(lineas[posicion + 3]))
                    ISTRUE = true;
            } else if (lineas[posicion + 2].equals("!=")) {
                if (!variable.getValor().toString().equals(lineas[posicion + 3]))
                    ISTRUE = true;
            }

        } else {
            if (lineas[posicion + 2].equals("==")) {
                if (variable.getValor().toString().equals(lineas[posicion + 3].substring(1, lineas[posicion + 3].length() - 1)))
                    ISTRUE = true;
            } else if (lineas[posicion + 2].equals("!=")) {
                if (!variable.getValor().toString().equals(lineas[posicion + 3].substring(1, lineas[posicion + 3].length() - 1)))
                    ISTRUE = true;
            }

        }

        if (negado)
            ISTRUE = !ISTRUE;

        return ISTRUE;
    }

    private void WHILE(String[] lineas) {
        System.out.println("while->" + Arrays.toString(lineas));
        int lineaInicial = NUMLINEA + 1;
        int lineaFinal = NUMLINEA + 1;
        boolean ISTRUE = CONDICION(lineas);
        System.out.println("while RESULTDO " + ISTRUE + Arrays.toString(lineas));
        if (ISTRUE) {
            while (ISTRUE) {
                System.out.println("while RESULTDO " + ISTRUE + Arrays.toString(lineas));
                NUMLINEA = lineaInicial;
                String[] sublineas = CODIGO[NUMLINEA].split("#");
                while (!sublineas[0].equals("}")) {
                    System.out.println("while: " + Arrays.toString(sublineas));
                    buscarEjecucion(sublineas);
                    NUMLINEA++;
                    lineaFinal = NUMLINEA;
                    sublineas = CODIGO[NUMLINEA].split("#");
                }
                ISTRUE = CONDICION(lineas);
            }
        } else {

            System.out.println("while RESULTDO F" + ISTRUE + Arrays.toString(lineas));
            NUMLINEA = lineaInicial;
            String[] sublineas = CODIGO[NUMLINEA].split("#");
            while (!sublineas[0].equals("}")) {
                System.out.println("while: " + Arrays.toString(sublineas));
                leerLineas(sublineas);
                NUMLINEA++;
                lineaFinal = NUMLINEA;
                sublineas = CODIGO[NUMLINEA].split("#");
            }


        }

        NUMLINEA = lineaFinal;
    }

    private void SWITCH(String[] lineas) {
        System.out.println("SW:" + Arrays.toString(lineas));
        Variable miVariable = tablaVariables.get(lineas[1]);
        boolean encontrado = false;
        boolean condicion = false;

        while (!lineas[0].equals("}")) {

            NUMLINEA++;
            lineas = CODIGO[NUMLINEA].split("#");
            if (lineas[0].equals("case")) {
                condicion = CONDICION(new String[]{"switch", miVariable.getID(), "==", lineas[1], "{"});

                System.out.println("switch RESULTDO " + condicion + "->" + lineas[1]);
                System.out.println(Arrays.toString(lineas));
                String[] sublineCase = new String[lineas.length - 3];
                if (lineas.length - 3 >= 0) System.arraycopy(lineas, 3, sublineCase, 0, lineas.length - 3);
                lineas = sublineCase;
                if(!encontrado){
                    while (!lineas[0].equals("break")) {
                        System.out.println("c:" + Arrays.toString(lineas));
                        if (condicion) {
                            encontrado = true;
                            buscarEjecucion(lineas);
                        } else
                            leerLineas(lineas);

                        NUMLINEA++;
                        lineas = CODIGO[NUMLINEA].split("#");
                    }
                }else {
                    while (!lineas[0].equals("break")) {
                        System.out.println("c:" + Arrays.toString(lineas));
                        leerLineas(lineas);
                        NUMLINEA++;
                        lineas = CODIGO[NUMLINEA].split("#");
                    }
                }


            } else if (lineas[0].equals("default")) {
                System.out.println(Arrays.toString(lineas));
                String[] sublineCase = new String[lineas.length - 2];
                if (lineas.length - 2 >= 0) System.arraycopy(lineas, 2, sublineCase, 0, lineas.length - 2);
                lineas = sublineCase;

                while (!lineas[0].equals("}")) {
                    System.out.println("c:" + Arrays.toString(lineas));
                    if (!encontrado) {
                        buscarEjecucion(lineas);
                    } else
                        leerLineas(lineas);

                    NUMLINEA++;
                    lineas = CODIGO[NUMLINEA].split("#");
                }
            }
        }
    }

    private Object LLAMARFUNCION(String[] lineas) {
        System.out.println("LLF: " + Arrays.toString(lineas));
        Funcion miFuncion = tablaFunciones.get(lineas[0]);
        FUNCIONACTUAL = miFuncion.getID();
        int lineaactual = NUMLINEA;
        NUMLINEA = miFuncion.getNumlineaInicio();
        if (lineas.length == 1) {//FUNCIONES SIN PARAMETROS
            while (!lineas[0].equals("}")) {
                NUMLINEA++;
                lineas = CODIGO[NUMLINEA].split("#");
                buscarEjecucion(lineas);
                System.out.println("F: " + Arrays.toString(lineas));
            }
            FUNCIONACTUAL = miFuncion.getID();
        } else {
            for (int i = 0; i < miFuncion.getParametros().size(); i++) {
                System.out.println("param--" + lineas[i + 1]);
                if (tablaVariables.containsKey(lineas[i + 1])) {// si es una varible como parametro
                    miFuncion.getParametros().get(i).setValor(tablaVariables.get(lineas[i + 1]).getValor());
                } else
                    miFuncion.getParametros().get(i).setValor(validarValor(miFuncion.getParametros().get(i).getTD(), lineas[i + 1]));
            }
            tablaFunciones.replace(miFuncion.getID(), miFuncion);
            System.out.println(tablaFunciones.get(miFuncion.getID()));
            while (!lineas[0].equals("}")) {
                NUMLINEA++;
                lineas = CODIGO[NUMLINEA].split("#");
                buscarEjecucion(lineas);
                System.out.println("F: " + Arrays.toString(lineas));
            }
            FUNCIONACTUAL = miFuncion.getID();
        }
        NUMLINEA = lineaactual;
        System.out.println("RRR  " + CODIGO[NUMLINEA]);

        FUNCIONACTUAL = null;
        return miFuncion.getRetorno().getValor();
    }

    private void crearFuncion(String[] lineas) {
        System.out.println("CF:" + Arrays.toString(lineas));
        while (!lineas[0].equals("}")) {
            NUMLINEA++;
            lineas = CODIGO[NUMLINEA].split("#");
            leerLineas(lineas);
        }
    }

    private void leerLineas(String[] lineas) {
        switch (lineas[0]) {
            case "print":
                System.out.println("ORPR" + Arrays.toString(lineas));
                break;
            case "if":
            case "else":
            case "for":
            case "while":
            case "switch":
                while (!lineas[0].equals("}")) {
                    NUMLINEA++;
                    lineas = CODIGO[NUMLINEA].split("#");
                    System.out.println("OR" + Arrays.toString(lineas));
                    leerLineas(lineas);
                }
                break;

            case "class":
            case "}":
            case "main":
            case "var":
            case "function":
                System.out.println("ORx:" + Arrays.toString(lineas));
                break;
            default:
                System.out.println("ORx:" + Arrays.toString(lineas));

        }

    }

    private void crearFuente(String contenido) {
        contenido = contenido.replace("¬", "\n");
        //contenido = contenido.replace("#" ," ");
        try {
            String ruta = "src/Codigo/myCod";
            File file = new File(ruta);
            // Si el archivo no existe es creado
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(contenido);
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void nuevaVariable(String[] lineas) {
        //var ID = VALOR : TIPO  var ID : tipo
        Variable variable = tablaVariables.get(lineas[1]);
        if (lineas.length == 4)
                tablaVariables.replace(lineas[1], variable);
        else{
            variable.setValor( validarValor(variable.getTD(), lineas[3]));
            tablaVariables.replace(lineas[1], variable);
            }

    }

    private void addSalida(String salida) {
        setChanged();
        notifyObservers(salida);
        clearChanged();

        SALIDA = SALIDA+"\n"+salida;
    }

    public String getSALIDA() {
        return SALIDA;
    }
}
