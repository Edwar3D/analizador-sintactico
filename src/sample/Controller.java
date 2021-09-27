package sample;

import Codigo.Compilador;
import Lexico.Gramatica;
import Sematica.Semantica;
import Sintaxis.PredictivoNoRecursivo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Controller implements Observer {


    @FXML
    private TextArea txtEntrada;

    @FXML
    private TextArea txtPasos;

    @FXML
    private TextArea txtSalida;

    @FXML
    private TableView<Gramatica> tblComponent;

    @FXML
    public TableColumn<Gramatica,String> clmEntrada;
    @FXML
    public TableColumn<Gramatica,String> clmNomenclatura;


    @FXML
    void iniciar(MouseEvent event) {
       if(!txtEntrada.getText().isEmpty()) {
           PredictivoNoRecursivo algoritmo = new PredictivoNoRecursivo();
           algoritmo.iniciar(txtEntrada.getText());

           txtSalida.setText(algoritmo.getMensaje());

           txtPasos.setText(algoritmo.getPasos());

           cargarComponentes(algoritmo.getEntradas());
           if(!algoritmo.isError()){
               Semantica semantica = new Semantica();
               semantica.revisarSemantica(algoritmo.getContenido());
               txtSalida.setText(txtSalida.getText() +"\n\t"+ semantica.getMensaje());
               if(algoritmo.isError() || semantica.getError())
                   error();
               else{
                   Compilador compilador = new Compilador(semantica.getTablaVariables(), semantica.getTablaFunciones());
                   compilador.addObserver(this);
                   compilador.ejecutar(algoritmo.getContenido());
                   System.out.println(compilador.getSALIDA());
               }

           }else{
               txtSalida.setText(algoritmo.getMensaje());
               error();
           }

       }
    }

    @FXML
    public void limpiar(MouseEvent actionEvent) {
        txtEntrada.setText("");
        txtSalida.setText("");
        txtPasos.setText("");
        tblComponent.getItems().clear();
    }

    private void error(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("FALLO EN EN CÓDIGO");
        alert.show();
    }

    private void notError(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("ANLISIS EXITOSO");
        alert.show();
    }

    private void cargarComponentes(ArrayList<Gramatica> componentes){

        ObservableList<Gramatica> data = FXCollections.observableList(componentes);

        tblComponent.setItems(data);
    }

    @FXML
    private void initialize() {
        clmEntrada.setCellValueFactory(new PropertyValueFactory<Gramatica, String>("Elemento"));
        clmNomenclatura.setCellValueFactory(new PropertyValueFactory<Gramatica, String>("significado"));
    }


    @Override
    public void update(Observable o, Object arg) {
        txtSalida.setText(txtSalida.getText()+ " \n"+arg.toString());
    }
}
