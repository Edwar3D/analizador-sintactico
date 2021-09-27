package Lexico;



import java.util.ArrayList;


public class Principal {

    PalabraReservada automataPalabraReservada;
    ArrayList listElements;
    ArrayList indefinidos;
    Diccionario diccionario = new Diccionario();;
    String aux;

    int contadorEntrada;
    char arrayDatos[];
    int estadoActual;

    public void analizar(String entrada) {
        automataPalabraReservada = new PalabraReservada();
        listElements = new ArrayList();
        indefinidos = new ArrayList();

        aux = "";
        arrayDatos = entrada.toCharArray();
        int estadoInicio = 0;
        boolean fin = false;
        estadoActual = estadoInicio;
        contadorEntrada = 0;

        while (fin == false) {
            if (contadorEntrada > arrayDatos.length - 1) {
                fin = true;
                break;
            }

            if (estadoActual == 0) {
                if (arrayDatos[contadorEntrada] == '+') {
                    concatenarEvaluar(24);
                } else if (arrayDatos[contadorEntrada] == '-') {
                    concatenarEvaluar(25);
                } else if (arrayDatos[contadorEntrada] == '*') {
                    addLista("*");
                } else if (arrayDatos[contadorEntrada] == '/') {
                    concatenarEvaluar(20);
                } else if (arrayDatos[contadorEntrada] == '&') {
                    concatenarEvaluar(23);
                } else if (arrayDatos[contadorEntrada] == '|') {
                    concatenarEvaluar(23);
                } else if (arrayDatos[contadorEntrada] == '(') {
                    addLista("(");
                } else if (arrayDatos[contadorEntrada] == ')') {
                    addLista(")");
                } else if (arrayDatos[contadorEntrada] == '{') {
                    addLista("{");
                } else if (arrayDatos[contadorEntrada] == '}') {
                    addLista("}");
                } else if (arrayDatos[contadorEntrada] == ',') {
                    addLista(",");
                } else if (arrayDatos[contadorEntrada] == ':') {
                    addLista(":");
                }else if (arrayDatos[contadorEntrada] == '=') {
                    concatenarEvaluar(22);
                } else if (arrayDatos[contadorEntrada] == '!') {
                    concatenarEvaluar(22);
                } else if (arrayDatos[contadorEntrada] == '>') {
                    concatenarEvaluar(22);
                } else if (arrayDatos[contadorEntrada] == '<') {
                    concatenarEvaluar(22);
                } else if (arrayDatos[contadorEntrada] == '"') {
                    concatenarEvaluar(15);
                } else if (arrayDatos[contadorEntrada] == ';') {
                    addLista(";");
                } else if (Character.isDigit(arrayDatos[contadorEntrada])) {
                    concatenarEvaluar(17);
                } else if (Character.isLetter(arrayDatos[contadorEntrada])) {
                    concatenarEvaluar(19);
                } else if (arrayDatos[contadorEntrada] == ' ' || arrayDatos[contadorEntrada] == '\t' || arrayDatos[contadorEntrada] == '\n') {
                } else {
                    concatenarEvaluar(999);
                }
                contadorEntrada++;
                continue;
            }

            if (estadoActual == 15) {
                if (arrayDatos[contadorEntrada] == '"') {
                    aux += arrayDatos[contadorEntrada];
                    contadorEntrada++;
                    addLista(aux);
                    aux = "";
                    estadoActual = 0;
                } else {
                    aux += arrayDatos[contadorEntrada];
                    contadorEntrada++;
                    isFinalCadena();
                }
                continue;
            }

            if (estadoActual == 17) {
                if (Character.isDigit(arrayDatos[contadorEntrada])) {
                    aux += arrayDatos[contadorEntrada];
                    contadorEntrada++;
                    isFinalCadena();
                } else if (arrayDatos[contadorEntrada] == '.') {
                    aux += arrayDatos[contadorEntrada];
                    estadoActual = 18;
                    contadorEntrada++;
                    isFinalCadena();
                } else {
                    evaluarIsAceptado();
                }
                continue;
            }

            if (estadoActual == 18) {
                if (Character.isDigit(arrayDatos[contadorEntrada])) {
                    aux += arrayDatos[contadorEntrada];
                    contadorEntrada++;
                    isFinalCadena();
                } else {
                    evaluarIsAceptado();
                }
                continue;
            }

            if (estadoActual == 19) {
                if (Character.isLetterOrDigit(arrayDatos[contadorEntrada])) {
                    aux += arrayDatos[contadorEntrada];
                    contadorEntrada++;
                    isFinalCadena();
                } else {
                    evaluarIsAceptado();
                }
                continue;
            }

            if (estadoActual == 20) {
                if (arrayDatos[contadorEntrada] == '/') {
                    aux += arrayDatos[contadorEntrada];
                    estadoActual = 21;
                    contadorEntrada++;
                    isFinalCadena();
                } else {
                    addLista(aux);
                    aux = "";
                    estadoActual = 0;
                }
                continue;
            }

            if (estadoActual == 21) {
                if (arrayDatos[contadorEntrada] == '\n') {
                    addLista(aux);
                    aux = "";
                    estadoActual = 0;
                    contadorEntrada++;
                } else {
                    aux += arrayDatos[contadorEntrada];
                    contadorEntrada++;
                    isFinalCadena();
                }
                continue;
            }

            if (estadoActual == 22) {
                if (arrayDatos[contadorEntrada] == '=') {
                    aux += arrayDatos[contadorEntrada];
                    contadorEntrada++;
                    addLista(aux);
                    aux = "";
                    estadoActual = 0;
                } else {
                    addLista(aux);
                    aux = "";
                    estadoActual = 0;
                }
                continue;
            }
            if (estadoActual == 23) {
                if (arrayDatos[contadorEntrada] == '&' || arrayDatos[contadorEntrada] == '|') {
                    aux += arrayDatos[contadorEntrada];
                    contadorEntrada++;
                    addLista(aux);
                    aux = "";
                    estadoActual = 0;
                } else {
                    addLista(aux);
                    aux = "";
                    estadoActual = 0;
                }
                continue;
            }
            if(estadoActual == 24 ){
                if (arrayDatos[contadorEntrada] == '+') {
                    aux += arrayDatos[contadorEntrada];
                    contadorEntrada++;
                    addLista(aux);
                    aux = "";
                    estadoActual = 0;
                } else {
                    addLista(aux);
                    aux = "";
                    estadoActual = 0;
                }
                continue;
            }

            if(estadoActual == 25 ){
                if (arrayDatos[contadorEntrada] == '-') {
                    aux += arrayDatos[contadorEntrada];
                    contadorEntrada++;
                    addLista(aux);
                    aux = "";
                    estadoActual = 0;
                } else {
                    addLista(aux);
                    aux = "";
                    estadoActual = 0;
                }
                continue;
            }

            if (estadoActual == 999) {
                evaluarIsAceptado();
            }
        }
    }

    public ArrayList getElementos(){
        return listElements;
    }


    public void addLista(String cadena) {
        String significado = "";
        boolean isEncontrado = false;
        if (estadoActual == 19) {
            automataPalabraReservada.evaluarCadena(cadena);
            if (automataPalabraReservada.getResultado() != null) {
                significado = automataPalabraReservada.getResultado();
                isEncontrado = true;
            }
        }
        if (!isEncontrado) {
            significado = diccionario.getDescriptionOf(cadena);
            if (significado.equals("Indefinido")) {
                indefinidos.add(cadena);
            }
        }
        listElements.add(new Gramatica(cadena,significado));
    }

    public void concatenarEvaluar(int numEstadoActual) {
        aux += arrayDatos[contadorEntrada];
        estadoActual = numEstadoActual;
        if (contadorEntrada + 1 == arrayDatos.length) {
            addLista(aux);
            aux = "";
            estadoActual = 0;
        }
    }

    public void evaluarIsAceptado() {
        if (arrayDatos[contadorEntrada] == ' ' || arrayDatos[contadorEntrada] == '\t' || arrayDatos[contadorEntrada] == '\n') {
            addLista(aux);
            aux = "";
            estadoActual = 0;
            contadorEntrada++;
        } else if (String.valueOf(arrayDatos[contadorEntrada]).matches("[*=+\\-/;,:&|!\\[\\](){}<>]")) {
            addLista(aux);
            aux = "";
            estadoActual = 0;
        } else {
            aux += arrayDatos[contadorEntrada];
            estadoActual = 999;
            contadorEntrada++;
            isFinalCadena();
        }
    }

    public void isFinalCadena() {
        if (contadorEntrada == arrayDatos.length) {
            addLista(aux);
            aux = "";
            estadoActual = 0;
            contadorEntrada++;
        }
    }

    public ArrayList getIndefinidos(){
        return indefinidos;
    }
}
