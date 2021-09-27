package Sematica;


import java.util.Arrays;
import java.util.Hashtable;

public class Semantica {

    private Hashtable<String, Variable> tablaVariables = new Hashtable<>();
    private Hashtable<String, Funcion> tablaFunciones = new Hashtable<>();

    private int NUMLINEA;
    private String[] lineasCodigo;
    private String RESULTADO;
    private String FUNCIONACTUAL;
    private Boolean error;
    private String mensaje;

    private boolean fin;

    public Semantica() {
        error = false;
        fin = false;
    }

    public void revisarSemantica(String CODIGO) {
        mensaje = "==SEMANTICA CORRECTA==";
        RESULTADO = "";
        NUMLINEA = 0;
        //dividir por lineas de codigo
        lineasCodigo = CODIGO.split("¬");
        System.out.println(lineasCodigo.length);

        while (!fin) {
            if (lineasCodigo[NUMLINEA].length() > 1) {
                //dividir por columnas
                String[] linea = lineasCodigo[NUMLINEA].split("#");
                System.out.println("RS:" + Arrays.toString(linea));
                verificarLinea(linea);
            }

            NUMLINEA++;
            if (NUMLINEA == lineasCodigo.length)
                fin = true;
        }

        System.out.println("///FUNCIONES");
        tablaFunciones.forEach((s, funcion) ->
                System.out.println(s + "= " + funcion.toString())
        );
        System.out.println("///VARIABLES");
        tablaVariables.forEach((s, variable) -> {
            System.out.println(s + "= " + variable.toString());
        });

        System.out.println(RESULTADO);
    }

    public void verificarLinea(String[] linea) {

        switch (linea[0]) {
            case "class":
                tablaFunciones.put(linea[1], new Funcion(linea[1], NUMLINEA));
                break;
            case "function":
                nuevaFuncion(linea, NUMLINEA);
                break;
            case "return":
                addReturn(linea);
                break;
            case "var":
                if (FUNCIONACTUAL != null)
                    addVariableAFuncion(linea);
                else
                    nuevaVariable(linea);
                break;
            case "main":
                FUNCIONACTUAL = null;
                break;
            case "if":
                condicional(linea);
                break;
            case "else":
            case "{":
            case "break":
                System.out.println(Arrays.toString(linea));
                break;
            case "while":
                condicional(linea);
                break;
            case "switch":
                if (tablaVariables.containsKey(linea[1])) {
                    Variable variableCON = tablaVariables.get(linea[1]);
                    System.out.println("switch con " + variableCON.getID());

                    while (!linea[0].equals("default")) {
                        NUMLINEA++;
                        linea = lineasCodigo[NUMLINEA].split("#");
                        System.out.println("sw:" + Arrays.toString(linea));

                        if (linea[0].equals("case")) {
                            if (validarValor(variableCON.getTD(), linea[1]) != null) {
                                // dividir primera linea de case
                                String[] sublinea = new String[linea.length - 3];
                                for (int i = 3; i < linea.length; i++) {
                                    sublinea[i - 3] = linea[i];
                                }
                                linea = sublinea;
                                System.out.println("c:" + Arrays.toString(linea));
                                while (!linea[0].equals("break")) {

                                    verificarLinea(linea);
                                    NUMLINEA++;
                                    linea = lineasCodigo[NUMLINEA].split("#");
                                    System.out.println("c_:" + Arrays.toString(linea));
                                }
                            } else {
                                if(!error)
                                    error("Error switch por " + linea[1] + " invalido para " + variableCON.getTD());
                            }
                        }
                    }
                    //verificar primera linea de default
                    String[] sublinea = new String[linea.length - 2];
                    for (int i = 2; i < linea.length; i++) {
                        sublinea[i - 2] = linea[i];
                    }
                    linea = sublinea;
                    System.out.println("d:" + Arrays.toString(linea));
                    verificarLinea(linea);
                } else {
                    //System.out.println("Error variable no encontrada: "+ linea[1]);
                    error("Error variable no encontrada: " + linea[1]);
                }
                break;
            case "print":
                valorImprimir(linea);
                break;
            case "for":
                estructuraFor(linea);
                break;
            default:
                if (tablaFunciones.containsKey(linea[0]))
                    llamarFuncion(linea);
                else
                    asignarValor(linea);
        }
    }

    //////////////FUNCIONES//////////
    private void nuevaFuncion(String[] lineas, int LINEA) {
        //# PR ID PRM , PRM  PRM -> ID : TD
        if (verificarID(lineas[1])) {
            Funcion miFuncion = new Funcion(lineas[1], LINEA);
            FUNCIONACTUAL = lineas[1];
            if (lineas.length == 6) { //tiene un parametro
                miFuncion.setParametro(new Variable(lineas[2], lineas[4], null));
            } else if (lineas.length > 6) { //tiene mas de un parametro
                for (int i = 2; i < lineas.length - 1; i = i + 3) {
                    if (!miFuncion.existeParametro(lineas[i]))
                        miFuncion.setParametro(new Variable(lineas[i], lineas[i + 2], null));
                    else {
                        //System.out.println("#######repetido########");
                        error("Error: parámetro " + lineas[i] + " repetida");
                    }
                }

            }
            tablaFunciones.put(lineas[1], miFuncion);
        } else {
            //System.out.println("######ERROR: " + lineas[1] + " repetido #############");
            error("Error: " + lineas[1] + "ya esta en uso");
        }
    }

    private void addReturn(String[] lineas) {
        //verificamos si la variable a retornar esta declarado
        System.out.println("->>" + FUNCIONACTUAL);
        Funcion mifuncion = tablaFunciones.get(FUNCIONACTUAL);
        if (mifuncion.existeParametro(lineas[1])) {
            mifuncion.setRetorno(mifuncion.getParametro(lineas[1]));
            System.out.println(mifuncion.getParametro(lineas[1]));
            tablaFunciones.replace(FUNCIONACTUAL, mifuncion);
        } else if (mifuncion.getVariables().containsKey(lineas[1])) {
            mifuncion.setRetorno(mifuncion.getVariables().get(lineas[1]));
            tablaFunciones.replace(FUNCIONACTUAL, mifuncion);
        } else {
            //System.out.println("###########Error ID no declarada para return");
            error("Error: " + lineas[1] + " NO declarada para return");
        }

    }

    private void addVariableAFuncion(String[] lineas) {
        Funcion miFuncion = tablaFunciones.get(FUNCIONACTUAL);
        // var ID = VALOR : TD   //var ID : TD
        if (!miFuncion.getVariables().containsKey(lineas[1]) && !miFuncion.existeParametro(lineas[1])) {
            if (lineas.length == 4) { //solo de declaró la variable
                miFuncion.setVariable(new Variable(lineas[1], lineas[3], null));
            } else {
                miFuncion.setVariable(new Variable(lineas[1], lineas[5], validarValor(lineas[5], lineas[3])));
            }
            tablaFunciones.replace(FUNCIONACTUAL, miFuncion);
        } else {
            //System.out.println("######error con ID: " + lineas[1] + " en funcion " + FUNCIONACTUAL);
            error("error con ID: " + lineas[1] + " en funcion " + FUNCIONACTUAL);
        }
    }

    private boolean verificarID(String ID) {
        if (!tablaVariables.containsKey(ID) && !tablaFunciones.containsKey(ID)) {
            return true;
        } else
            return false;
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
                default:
                    newValor = null;
            }
        } catch (Exception e) {
            //System.out.println(" tipo incorrecto " + e);
            error("Valor incorrecto " + e);
        }

        return newValor;
    }

    //////////////IMPRIMIR ////////////////
    private void valorImprimir(String[] lineas) {
        if (lineas[1].matches("[0-9]+") || lineas[1].matches("[0-9]+.[0-9]+") || lineas[1].matches(".[0-9]+")) {
            System.out.println("es cifra");
        } else if (lineas[1].matches("\"[a-zA-Z0-9 ]*\"") || lineas[1].matches("\'[a-zA-Z0-9 ]*\'")) {
            System.out.println("String o char");
        } else if (tablaVariables.containsKey(lineas[1])) {
            System.out.println("es variable");
        } else if (FUNCIONACTUAL != null) {
            Funcion miFuncion = tablaFunciones.get(FUNCIONACTUAL);
            if (miFuncion.existeParametro(lineas[1]) || miFuncion.getVariables().containsKey(lineas[1])) {
                System.out.println("encontrada");
            } else {
                error("Error dato a imprimir, variable " + lineas[1] + " no fue encontrada ");
            }
        } else {
            //System.out.println("Error dato a imprimir incorrecto");
            error("Error dato a imprimir incorrecto, " + lineas[1]);
        }
    }

    ////////////////////////VARIABLES ///////////////////////

    private void nuevaVariable(String[] lineas) {
        //var ID = VALOR : TIPO  var ID : tipo
        if (verificarID(lineas[1])) {
            if (lineas.length == 4)
                tablaVariables.put(lineas[1], new Variable(lineas[1], lineas[3], null));
            else
                tablaVariables.put(lineas[1], new Variable(lineas[1], lineas[5], validarValor(lineas[5], lineas[3])));
        } else {
            //System.out.println("///////////error variable " + lineas[1] + " ya existe");
            error("Error: la variable " + lineas[1] + " ya está en uso");
        }
    }

    private void asignarValor(String[] lineas) {
        /// ID = VALOR
        if (tablaVariables.containsKey(lineas[0])) {
            Variable miVariable = tablaVariables.get(lineas[0]);
            //ID = FUNCTION
            if (tablaFunciones.containsKey(lineas[2])) {
                Funcion miFuncion = tablaFunciones.get(lineas[2]);
                //EJECUTAR LA FUNCION
                FUNCIONACTUAL = miFuncion.getID();
                System.out.println("------EJECUCION DE " + miFuncion.getID());
                System.out.println("retorno " + miFuncion.getRetorno());
                if (miFuncion.getRetorno() != null) {
                    if (miVariable.getTD().equals(miFuncion.getRetorno().getTD())) {
                        miVariable.setValor(miFuncion.getRetorno().getValor());
                        //verificar los parametros
                        String[] lineasFuncion = new String[lineas.length - 2];
                        for (int i = 2; i < lineas.length; i++) {
                            lineasFuncion[i - 2] = lineas[i];
                        }
                        llamarFuncion(lineasFuncion);
                        tablaVariables.replace(lineas[0], miVariable);
                    } else {
                        //System.out.println("######## error: " + miFuncion.getRetorno().getTD() + " no puede ser " + miVariable.getTD());
                        error("Error: " + miFuncion.getRetorno().getTD() + " NO puede ser " + miVariable.getTD());
                    }
                } else {
                    //System.out.println("######## esta funcion no retorna ningun valor");
                    error("Funcion " + miFuncion.getID() + " NO retorna ningun valor");
                }
            } else {
                Object valor = validarValor(miVariable.getTD(), lineas[2]);
                if (valor != null) {
                    miVariable.setValor(valor);
                    tablaVariables.replace(lineas[0], miVariable);
                } else {
                    //System.out.println("### error: " + lineas[2] + " no es " + miVariable.getTD());
                    error("Error: " + lineas[2] + " no es " + miVariable.getTD());
                }
            }
        } else if (FUNCIONACTUAL != null) {
            Funcion miFuncion = tablaFunciones.get(FUNCIONACTUAL);
            Variable miVariable = null;
            if (miFuncion.existeParametro(lineas[0])) {
                miVariable = miFuncion.getParametro(lineas[0]);
                miVariable.setValor(validarValor(miVariable.getTD(), lineas[2]));
            } else if (miFuncion.getVariables().containsKey(lineas[0])) {
                miVariable = miFuncion.getVariable(lineas[0]);
                miVariable.setValor(validarValor(miVariable.getTD(), lineas[2]));
            } else {
                //System.out.println("Variable " + lineas[0] + " No fue encontrada en la funcion "+ FUNCIONACTUAL);
                error("Variable " + lineas[0] + " NO fue encontrada en la funcion " + FUNCIONACTUAL);
            }
        } else {//llamada de funciones
            //System.out.println("Variable " + lineas[0] + " No fue encontrada");
            error("Variable " + lineas[0] + " No fue encontrada");
        }

    }

    private void llamarFuncion(String[] lineas) {
        Funcion miFuncion = tablaFunciones.get(lineas[0]);
        if (miFuncion != null) {
            if (lineas.length == 1 && miFuncion.getParametros().size() == 0) {
            } else {
                if (miFuncion.getParametros().size() == lineas.length - 1) {//verificar si esta completo los parametros

                    for (int i = 0; i < miFuncion.getParametros().size(); i++) {
                        Object valor = null;
                        if (tablaVariables.containsKey(lineas[i + 1])) {
                            Variable entrada = tablaVariables.get(lineas[i + 1]);
                            if (miFuncion.getParametros().get(i).getTD().equals(entrada.getTD())) {
                                if (entrada.getValor() != null)
                                    valor = entrada.getValor();
                                else {
                                    error("Error en " + lineas[0] + ",  por " + lineas[i + 1] + ", varible null");
                                    i = miFuncion.getParametros().size();
                                }
                            } else {
                                error("Error en " + lineas[0] + ",  por " + lineas[i + 1] + " no es tipo " + miFuncion.getParametros().get(i).getTD());
                                i = miFuncion.getParametros().size();
                            }

                        } else {
                            valor = validarValor(miFuncion.getParametros().get(i).getTD(), lineas[i + 1]);
                            if (valor != null) {
                                miFuncion.getParametros().get(i).setValor(valor);
                            } else {
                                error("Error en " + lineas[0] + ",  por " + lineas[i + 1] + " no es " + miFuncion.getParametros().get(i).getTD());
                                i = miFuncion.getParametros().size();
                            }
                        }
                    }
                } else {
                    //System.out.println("### Error al llamar la funcion: " + miFuncion.getID());
                    error("Error al llamar la funcion: " + miFuncion.getID());
                }
            }
        } else {
            //System.out.println("####" + lineas[0] + " no encontrada");
            error("Error: " + lineas[0] + " no encontrada");
        }
    }

    private void condicional(String[] lineas) {
        Variable miVariable = null;
        int posicion = 0;
        if (lineas[1].equals("!")) {
            System.out.println("condicion negada");
            posicion = 1;
        }
        if (tablaVariables.containsKey(lineas[posicion + 1])) {
            miVariable = tablaVariables.get(lineas[posicion + 1]);
        } else if (FUNCIONACTUAL != null) {
            Funcion miFuncion = tablaFunciones.get(FUNCIONACTUAL);
            System.out.println(">>>" + FUNCIONACTUAL);

            if (miFuncion.existeParametro(lineas[posicion + 1])) {
                miVariable = miFuncion.getParametro(lineas[posicion + 1]);
            } else if (miFuncion.getVariables().containsKey(lineas[posicion + 1])) {
                miVariable = miFuncion.getVariable(lineas[posicion + 1]);
            } else {
                error("VARIABLE " + lineas[posicion + 1] + "no encontrada en " + FUNCIONACTUAL);
            }
        } else {
            if(lineas[posicion+1].equals("true") || lineas[posicion+1].equals("false"))
                System.out.println("booelan");
            else
                error("VARIABLE:  " + lineas[posicion + 1] + " NO DECLARADA");
        }

        if (miVariable != null) {
            System.out.println(miVariable);

            if (lineas.length <= 4 && miVariable.getTD().equals("boolean")) {
                System.out.println("if correcto +");

            } else if (miVariable.getTD().equals("int") || miVariable.getTD().equals("float")) {
                if (tablaVariables.containsKey(lineas[posicion + 3])) {// comparacion de dos variables
                    Variable miVariable2 = tablaVariables.get(lineas[posicion + 3]);
                    if (!miVariable2.getTD().equals("int") || miVariable2.getTD().equals("float")) {
                        //System.out.println("comparacion de " + miVariable.getTD() + " con " + miVariable2.getTD() + " es invalida");
                        error("comparacion de " + miVariable.getTD() + " con " + miVariable2.getTD() + " es invalida");
                    }
                } else {//comparacion con algun valor
                    //valida que sea entero o float
                    if (lineas[posicion + 3].matches("[0-9]+") || lineas[posicion + 3].matches("[0-9]+.[0-9]+") || lineas[posicion + 3].matches(".[0-9]+")) {
                        System.out.println("if correcto");
                    } else {
                        //System.out.println("Error comparacion de " + miVariable.getTD() + " con " + lineas[posicion+3] + " es invalida");
                        error("Error comparacion de " + miVariable.getTD() + " con " + lineas[posicion + 3] + " es invalida");
                    }
                }
            } else {//string char bool
                if (lineas[posicion + 2].equals("==") || lineas[posicion + 2].equals("!=")) {
                    Object valorV2 = validarValor(miVariable.getTD(), lineas[posicion + 3]);
                    if (valorV2 == null) {
                        //System.out.println(lineas[posicion+3] + " no puede ser comparada con tipo " + miVariable.getTD());
                        error(lineas[posicion + 3] + " no puede ser comparada con tipo " + miVariable.getTD());
                    } else {
                        System.out.println("if correcto");
                    }
                } else {
                    //System.out.println("Error de operador logico: " + miVariable.getTD() + " con " + lineas[posicion+2]);
                    error("Error de operador logico: " + miVariable.getTD() + " con " + lineas[posicion + 2]);
                }

            }
        }
    }

    //////////////FOR /////////////////

    private void estructuraFor(String[] lineas) {
        if (verificarID(lineas[1])) {
            if (lineas[1].equals(lineas[4]) && lineas[1].equals(lineas[7])) {
                Object valor = validarValor("int", lineas[3]);
                if (valor != null) {
                    tablaVariables.put(lineas[1], new Variable(lineas[1], "int", valor));
                    //System.out.println("Inializacion correcta");

                    if (tablaVariables.containsKey(lineas[6])) {// ID OPL ID
                        Variable variable = tablaVariables.get(lineas[6]);
                        if (variable.getTD().equals("int") || variable.getTD().equals("float")) {
                            //System.out.println("condicional correcta");
                            if (lineas.length == 12) {
                                if (lineas[10].matches("[0-9]+") || lineas[10].matches("[0-9]+.[0-9]+") || lineas[10].matches(".[0-9]+")) {
                                    //System.out.println("accion  correcta for");
                                } else {
                                    //System.out.println("accion  incorrecta "+lineas[10]+" no es una cifra");
                                    error("Accion  incorrecta en el cliclo for :" + lineas[10] + " no es una cifra");
                                }
                            }
                        }
                    } else if (lineas[6].matches("[0-9]+") || lineas[6].matches("[0-9]+.[0-9]+") || lineas[6].matches(".[0-9]+")) {

                    } else {
                        //System.out.println("error en la condicional por:"+ lineas[6]);
                        error("error en la condicional por:" + lineas[6]);
                    }
                } else {
                    //System.out.println("Error inicializacion Incorrecto");
                    error("Error inicializacion Incorrecta en el ciclo for");
                }

            } else {
                //System.out.println("Error en cliclo for");
                error("Error en cliclo for");
            }
        } else {
            //System.out.println("Error la variable "+lineas[1]+" ya ha sido usada");
            error("Error en cliclo for");
        }

    }

    private void error(String mensaje) {
        NUMLINEA = NUMLINEA + 1;
        this.mensaje = mensaje + " : linea " + NUMLINEA;
        this.error = true;
        this.fin = true;
    }

    public Boolean getError() {
        return error;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Hashtable<String, Funcion> getTablaFunciones() {
        return tablaFunciones;
    }

    public Hashtable<String, Variable> getTablaVariables() {
        return tablaVariables;
    }

    public String getRESULTADO() {
        return RESULTADO;
    }
}
