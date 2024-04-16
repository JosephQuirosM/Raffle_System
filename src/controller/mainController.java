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
import classes.Raffle;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import oracle.jdbc.OracleTypes;

public class mainController implements Initializable {
    
    public static Raffle actualRaffle;
    
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
    @FXML
    private AnchorPane sheet;
    
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

    @FXML
    private void deleteRaffle(ActionEvent event) {
        String selectedRaffle = tblRaffleList.getSelectionModel().getSelectedItem();
        if (selectedRaffle!=null){
            deleteRaffleDB(selectedRaffle);
        }
    }

    private void deleteRaffleDB(String selectedRaffle) {
       try(Connection conn = DatabaseConnector.getConnection()){
                try(CallableStatement methodDB = conn.prepareCall("{call Eliminar_Rifa(?)}")){
                  methodDB.setString(1, selectedRaffle);
                  methodDB.execute();
                  tblRaffleList.getItems().remove(selectedRaffle);
                  tblRaffleList.refresh();
                }
            }
            catch(SQLException e){
                e.printStackTrace();
            }
    }

    @FXML
    private void openRaffle(ActionEvent event) throws IOException {
        String selectedRaffle;
        selectedRaffle = tblRaffleList.getSelectionModel().getSelectedItem();
        
        if (selectedRaffle!=null){
        createObject(selectedRaffle);
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/openRaffle.fxml"));
        Scene scene = new Scene(root);
        App.loadScene(scene);
        }
       
       
    }

    private void createObject(String raffleDescription) {
        try(Connection conn = DatabaseConnector.getConnection()){
            try(CallableStatement obtenerRifa = conn.prepareCall("{call Obtener_Rifa(?, ?, ?, ?, ?, ?, ?)}")){
                obtenerRifa.setString(1,raffleDescription );
                obtenerRifa.registerOutParameter(2, Types.NUMERIC);
                obtenerRifa.registerOutParameter(3, Types.VARCHAR);
                obtenerRifa.registerOutParameter(4, Types.VARCHAR);
                obtenerRifa.registerOutParameter(5, Types.NUMERIC);
                obtenerRifa.registerOutParameter(6, Types.VARCHAR);
                obtenerRifa.registerOutParameter(7, Types.NUMERIC);
                obtenerRifa.execute();

                actualRaffle = new Raffle(obtenerRifa.getInt(2), obtenerRifa.getString(3), obtenerRifa.getString(4), obtenerRifa.getInt(5), obtenerRifa.getString(6), obtenerRifa.getInt(7));
            }
        }
        
        catch(SQLException e){
            e.printStackTrace();
        }
    }
    
}
