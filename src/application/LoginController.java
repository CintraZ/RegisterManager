package application;

import java.sql.*;

import java.io.IOException;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.RadioButton;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;



public class LoginController implements Initializable{
	@FXML
	private AnchorPane Pane;
	@FXML
	private Button btn_Login;
	@FXML
	private TextField TextField_User;
	@FXML
	private TextField TextField_Pass;
	@FXML
	private Button btn_Cancel;
	@FXML
	private RadioButton Radiobtn_Doctor;
	@FXML
	private RadioButton Radiobtn_Patient;
	@FXML
	private Label Label_Error;
	
	/*
	 * handle login event 
	 * entry main page if both of user and password are true
	 */
	public void btn_Login_On(ActionEvent event) throws Exception {

		try {
			Dbutil db = new Dbutil();
			String pwd;
			Parent mainPage_parent = null;
			Scene mainPage_scene = null;
			Stage mainStage = null;
			//patient login
			if(Radiobtn_Patient.isSelected()) {
				 pwd = db.getPwdofptID(TextField_User.getText());
				if(pwd.equals(TextField_Pass.getText())) {
					db.UpdatePtLoginDate(TextField_User.getText());
					mainPage_parent = FXMLLoader.load(getClass().getResource("/application/mainPage.fxml"));
					mainPage_scene = new Scene(mainPage_parent);
					mainStage = (Stage)((Node)event.getSource()).getScene().getWindow();
					
					mainStage.hide();
					mainStage.setScene(mainPage_scene);
					mainStage.show();
					
					Label_Error.setVisible(false);
					
				} else {
					Label_Error.setVisible(true);
				}
			} else if(Radiobtn_Doctor.isSelected()) {  //doctor login
				pwd = db.getPwdofdtID(TextField_User.getText());
				if(pwd.equals(TextField_Pass.getText())) {
					db.UpdateDtLoginDate(TextField_User.getText());
					mainPage_parent = FXMLLoader.load(getClass().getResource("/application/DoctorPage.fxml"));
					mainPage_scene = new Scene(mainPage_parent);
					mainStage = (Stage)((Node)event.getSource()).getScene().getWindow();
					
					mainStage.hide();
					mainStage.setScene(mainPage_scene);
					mainStage.show();
					
					Label_Error.setVisible(false);
					
				} else {
					Label_Error.setVisible(true);
				}
			}
			CurPt cpt = CurPt.getInstance();
			cpt.setCurPt(TextField_User.getText());
			db.closeConnection();
			
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
		
	}
	
	public void btn_Cancel_On(ActionEvent event) throws IOException {
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		
		stage.close();
	}
	
	public void Radiobtn_Doctor_On() {
		
	}
	
	public void Radiobtn_Patient_ON() {
		
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		//initialize the Login page
		final ToggleGroup group = new ToggleGroup();
		Radiobtn_Patient.setSelected(true);
		Radiobtn_Doctor.setToggleGroup(group);
		Radiobtn_Patient.setToggleGroup(group);
	}

}
