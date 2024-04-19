package controller;

import app.App;
import classes.DatabaseConnector;
import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import oracle.jdbc.OracleTypes;

public class winnerRaffleController implements Initializable {

    @FXML
    private Label lblTitle;
    @FXML
    private Label lblNumber;
    @FXML
    private Label lblWinner;
    @FXML
    private Button btnWinner;
    @FXML
    private Label lblPrize;
    @FXML
    private Button btnBack;

    String rngNum;
    int state;
    String owner;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblTitle.setText(mainController.actualRaffle.getDescription());
        lblWinner.setVisible(false);
        lblNumber.setVisible(false);
        lblPrize.setVisible(false);
    }    

    @FXML
    private void generateWinner(ActionEvent event) {
        Random rng = new Random();
        
        int winnerNum = rng.nextInt(mainController.actualRaffle.getNumbers()) + 1;
        rngNum =String.valueOf(winnerNum);
        conectDB(winnerNum);
        
        lblNumber.setText(rngNum);
        lblNumber.setVisible(true);
        
        if(state == 3){
            lblWinner.setText(owner);
        }
        lblWinner.setVisible(true);
        
        lblPrize.setText(mainController.actualRaffle.getPrize());
        lblPrize.setVisible(true);
        
        btnWinner.setVisible(false);
    }

    @FXML
    private void backToMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/openRaffle.fxml"));
        Scene scene  = new Scene(root);
        App.loadScene(scene);
    }
    
    private void conectDB(int number){
         try (Connection conn = DatabaseConnector.getConnection()) {
                try (CallableStatement methodDB = conn.prepareCall("{call Obtener_Estado(?, ?, ?, ?)}")) {
                    methodDB.setInt(1, mainController.actualRaffle.getId());
                    methodDB.setInt(2, number);
                    methodDB.registerOutParameter(3, OracleTypes.NUMBER);
                    methodDB.registerOutParameter(4, OracleTypes.VARCHAR);
                    methodDB.execute();
                    
                    state = methodDB.getInt(3);
                    owner = methodDB.getString(4);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
}
