package controller;

import app.App;
import classes.DatabaseConnector;
import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import oracle.jdbc.OracleTypes;

public class BuyNumberController implements Initializable {

    @FXML
    private Label lblNumbers;
    @FXML
    private MenuButton mbMenuBar;
    @FXML
    private MenuItem mitnAvailable;
    @FXML
    private MenuItem mitnReserved;
    @FXML
    private MenuItem mitnBought;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnConfirm;
    @FXML
    private TextField tfldOwner;

  @Override
    public void initialize(URL url, ResourceBundle rb) {
        
       initLbl();
        loadMenuBar();
    }    

    @FXML
    private void setText(ActionEvent event) {
        MenuItem selected = (MenuItem) event.getSource();
        
        mbMenuBar.setText(selected.getText());
    }

    @FXML
    private void backToRaffle(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/openRaffle.fxml"));
            Scene scene  = new Scene(root);
            App.loadScene(scene);
            openRaffleController.selectedNumbers.clear();
        } catch (IOException ex) {
            Logger.getLogger(openRaffleController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  
    private void loadMenuBar(){
        int size = openRaffleController.selectedNumbers.size();
        
        if(size == 1){
            String number = openRaffleController.selectedNumbers.get(0);
            
            try (Connection conn = DatabaseConnector.getConnection()) {
                try (CallableStatement methodDB = conn.prepareCall("{call Obtener_Estado(?, ?, ?, ?)}")) {
                    methodDB.setInt(1, mainController.actualRaffle.getId());
                    methodDB.setInt(2, Integer.parseInt(number));
                    methodDB.registerOutParameter(3, OracleTypes.NUMBER);
                    methodDB.registerOutParameter(4, OracleTypes.VARCHAR);
                    methodDB.execute();
                    
                    selectMenuBarText(methodDB.getInt(3));
                    tfldOwner.setText(methodDB.getString(4));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
        }
        
        
    }
    
    private void selectMenuBarText(int opt){
        if(opt == 1){
            mbMenuBar.setText("Disponible");
            return;
        }
        
        if(opt == 2){
            mbMenuBar.setText("Reservado");
            return;
        }
        
        if(opt == 3){
            mbMenuBar.setText("Comprado");
        }
    }

    @FXML
    private void saveNumberData(ActionEvent event) {
        int size = openRaffleController.selectedNumbers.size();
        
        for(int i = 0; i < size;i++){
         try (Connection conn = DatabaseConnector.getConnection()) {
                try (CallableStatement methodDB = conn.prepareCall("{call Guardar_Estado(?, ?, ?, ?)}")) {
                    methodDB.setInt(1, mainController.actualRaffle.getId());
                    methodDB.setInt(2,Integer.parseInt(openRaffleController.selectedNumbers.get(i)));
                    methodDB.setInt(3, convertMenuBarText());
                    methodDB.setString(4, tfldOwner.getText());
                    methodDB.execute();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
         backToRaffle(event);
    }
    
    private int  convertMenuBarText(){
        String text =mbMenuBar.getText();
        
        if("Disponible".equals(text)){
            return 1;
        }
        
        if("Reservado".equals(text)){
            return 2;
        }
        
        return 3;
    }
    
    private void initLbl(){
        int size = openRaffleController.selectedNumbers.size();
        lblNumbers.setText(openRaffleController.selectedNumbers.get(0));
        
        for(int i = 1; i < size - 1 ; i++){
             lblNumbers.setText(lblNumbers.getText() +", "+ openRaffleController.selectedNumbers.get(i));
        }
        
        if(size >= 2){
            lblNumbers.setText(lblNumbers.getText() + ", " + openRaffleController.selectedNumbers.get(size - 1) );
        }
    }
}
