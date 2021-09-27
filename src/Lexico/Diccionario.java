package Lexico;

import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Diccionario {
   private Hashtable<String, String> dic = new Hashtable<>();

    public Diccionario() {
        dic.put("(", "PA");
        dic.put(")", "PC");
        dic.put("{", "LLA");
        dic.put("}", "LLC");
        dic.put("<", "OPL");
        dic.put(">", "OPL");
        dic.put("==", "OPL");
        dic.put("!=", "OPL");
        dic.put(";", "PC");
        dic.put("<=", "OPL");
        dic.put(">=", "OPL");
        dic.put("/", "OP");
        dic.put("*", "OP");
        dic.put("-", "OP");
        dic.put("+", "OP");
        dic.put("=", "IG");
        dic.put(",", "SC");
        dic.put("true", "BOOL");
        dic.put("false", "BOOL");
        dic.put("var", "PR");
        dic.put("class", "PRC");
        dic.put(":", "SDP");
        dic.put("main", "PRM");
        dic.put("while", "PRW");
        dic.put("for", "NRF");
        dic.put("case","CA");
        dic.put("else","E");;
        dic.put("print","PRS");
        dic.put("if","IF");
        dic.put("switch","SW");
        dic.put("Main","PRM");
        dic.put("break","BR");
        dic.put("default","DF");
        dic.put("function","FU");
        dic.put("return","RET");
        dic.put("++","INCREMENTO");
        dic.put("--","DECREMENTO");
    }

    public String getDescriptionOf(String entrada) {
        //valida que exista la entrada
        if (dic.containsKey(entrada)) {
            return dic.get(entrada);
        }

        //valida que sea entero
        if (entrada.matches("[0-9]+")) {
            return "CF";
        }

        //valida que sea double
        if (isValidDouble(entrada)) {
            return "FL";
        }

        //valida que sea comentario
        Pattern p = Pattern.compile("^//");
        Matcher m = p.matcher(entrada);
        if (m.find()) {
            return "Comentario";
        }

        //valida que sea variable
        p = Pattern.compile("^[A-Za-z]+");
        m = p.matcher(entrada);
        if (m.find()) {
            if (entrada.matches("[a-zA-Z0-9_]+")) {
                return "ID";
            }
        }

        //valida que sea string
        p = Pattern.compile("^[\"][a-zA-Z0-9 ]+[\"]$");
        m = p.matcher(entrada);
        if (m.find()) {
            return "CAD";
            /*int ultimo = entrada.length() - 1;
            if (entrada.charAt(ultimo) == '\"' && entrada.length() >= 2) {
                return "CAD";
            }*/
        }

        //valida que sea char
        p = Pattern.compile("^\'");
        m = p.matcher(entrada);
        if (m.find()) {
            int ultimo = entrada.length() - 1;
            if (entrada.charAt(ultimo) == '\'' && entrada.length() == 3) {
                return "CH";
            }
        }

        return "Indefinido";
    }

    private static boolean isValidDouble(String s) {
        final String Digits = "(\\p{Digit}+)";
        final String HexDigits = "(\\p{XDigit}+)";
        final String Exp = "[eE][+-]?" + Digits;
        final String fpRegex
                = ("[\\x00-\\x20]*"
                + "[+-]?("
                + "NaN|"
                + "Infinity|"
                + "(((" + Digits + "(\\.)?(" + Digits + "?)(" + Exp + ")?)|"
                + "(\\.(" + Digits + ")(" + Exp + ")?)|"
                + "(("
                + "(0[xX]" + HexDigits + "(\\.)?)|"
                + "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")"
                + ")[pP][+-]?" + Digits + "))"
                + "[fFdD]?))"
                + "[\\x00-\\x20]*");
        return Pattern.matches(fpRegex, s);
    }

}
