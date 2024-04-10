package controller;

import java.io.IOException;
import java.net.URL;
import app.App;
import classes.DatabaseConnector;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class newRaffleController implements Initializable {

    @FXML
    private Button backButton;
    @FXML
    private TextField tfldDescription;
    @FXML
    private TextField tfldPrize;
    @FXML
    private TextField tfldPrice;
    @FXML
    private DatePicker datepckInitDate;
    @FXML
    private TextField tfldTotalNums;
    @FXML
    private Button btnCreate;
    @FXML
    private Label lblTittle;

  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void backToMenu(ActionEvent event) throws IOException {
        backToMenu();
    }

    @FXML
    private void CreateRaffle(ActionEvent event) throws IOException{
        
        if(true){
            sendDataToDB();
            backToMenu();
            return;
        }
        
        return;
    }
    
    
    //private void
    
    private void backToMenu() throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        Scene scene  = new Scene(root);
        App.loadScene(scene);
    }
    
    private void sendDataToDB(){
        try(Connection conn = DatabaseConnector.getConnection()){
            try(CallableStatement agregarRifa = conn.prepareCall("{call Insertar_Rifa(?, ?, ?, ?, ?)}")){
                agregarRifa.setString(1, tfldDescription.getText());
                agregarRifa.setString(2, tfldPrize.getText());
                agregarRifa.setInt(3, Integer.parseInt(tfldPrice.getText()));
                agregarRifa.setString(4, datepckInitDate.getValue().toString());
                agregarRifa.setInt(5, Integer.parseInt(tfldTotalNums.getText()));
                agregarRifa.execute();
            }
        }
        
        catch(SQLException e){
            e.printStackTrace();
        }
    }
}



