/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;

import DAO_Generic.GenericDAO;
import DTO.Administrador;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author steli
 */
public class Tela_LoginController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtPassword;

    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;

    @FXML
    void btnLogar(ActionEvent event) {
        GenericDAO bb = new GenericDAO();
        Administrador aa = (Administrador) bb.logarEmail(txtEmail.getText());

        if (aa != null && aa.getPassword().equals(txtPassword.getText())) {
            try {
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("Login");
                alerta.setHeaderText("Login efetuado com sucesso!!!!");
                // alerta.setContentText("Deseja salvar antes de Fechar");
                if (alerta.showAndWait().get() == ButtonType.OK) {

                    Tela_Principal(event);
                }

            } catch (IOException e) {
                e.printStackTrace(); // Trate ou registre erros adequadamente
            }
        } else {
            // Exiba uma mensagem de erro ao usuário, informando que as credenciais são inválidas.
        }
    }

    @FXML
    public void Tela_Principal(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Inicio.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
