package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import app.App;
import classes.DatabaseConnector;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.ListView;
import oracle.jdbc.OracleTypes;

public class mainController implements Initializable {
    
    @FXML
    private Button btnNewRaffle;
    @FXML
    private Button btnClose;
    @FXML
    private ListView<String> tblRaffleList;
    @FXML
    private Button btnDeleteRaffle;
    @FXML
    private Button btnOpenRaffle;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       loadRaffleTableView(); 
    }    

    @FXML
    private void newRaffle(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/newRaffle.fxml"));
        Scene scene = new Scene(root);
       App.loadScene(scene);
               
    }

    @FXML
    private void closeRaffle(ActionEvent event) {
        System.exit(0);
    }
    
    private void loadRaffleTableView(){
    try(Connection conn = DatabaseConnector.getConnection()){
        try(CallableStatement methodDB = conn.prepareCall("{call Obtener_Nombres_Rifas(?)}")){
            methodDB.registerOutParameter(1, OracleTypes.CURSOR);
           methodDB.execute();
           
            ResultSet rs = (ResultSet) methodDB.getObject(1);
            while(rs.next()){
            String raffleName = rs.getString("descripcion");
            tblRaffleList.getItems().add(raffleName);
            }
        }
    }
    catch(SQLException e){
        e.printStackTrace();
    }
    
    }
    
}
