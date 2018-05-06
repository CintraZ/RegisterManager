package application;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
//import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;

public class mainPageController implements Initializable{
	@FXML
	private TextField textField_numType;
	@FXML
	private TextField textField_numName;
	@FXML
	private TextField textField_Amount;
	@FXML
	private TextField textField_curAmount;
	@FXML
	private TextField textField_ecg;
	@FXML
	private TextField textField_regID;
	@FXML
	private TextField textField_soName;
	@FXML
	private TextField textField_dtName;
	@FXML
	private Button btn_ok;
	@FXML
	private Button btn_clr;
	@FXML
	private Button btn_exit;
	@FXML
	private Label Label_full;
	@FXML
	private Label Label_change;
	
	
	private ListView<String> listviewSo;
	private ListView<String> listviewDt;
	private ListView<String> listviewEx;
	private ListView<String> listviewNN;
	
	
	private ObservableList<String> dataSo;
	private ObservableList<String> dataDt;
	private ObservableList<String> dataDtold;
	private ObservableList<String> dataEx;
	private ObservableList<String> dataNN;
	private ObservableList<String> dataNNold;
	
	private Dbutil db;
	
	private Popup soNamePop;
	private Popup dtNamePop;
	private Popup numTypePop;
	private Popup numNamePop;
	
	
	/*
	 * textfield_soName handle
	 * 
	 * trigger: mouse
	 */
	public void textField_soName_On()  throws NullPointerException, SQLException{
		textField_soName.requestFocus();
		if(textField_soName.getText().isEmpty() && listviewSo.getSelectionModel().getSelectedIndex() != -1) {
			listviewSo.getSelectionModel().select(null);
		}
		listviewSo.getSelectionModel().selectedItemProperty().addListener(
				(ObservableValue<? extends String> observable, String oldValue, String newValue) ->{
					if(listviewSo.getItems() != null) {
						textField_soName.setText(newValue);
						textField_dtName.clear();
						textField_numName.clear();
						textField_numType.clear();
						textField_Amount.clear();
						soNamePop.hide();
					}
				});
		
		showPopup(soNamePop,textField_soName, listviewSo);
	}
	
	public void textField_dtName_On() throws Exception {
		textField_dtName.requestFocus();
		if(textField_soName.getText().isEmpty()) {
			dataDt = db.getDtName();
		}
		else
		{
			dataDt = db.getDtofSo(textField_soName.getText());
		}
		if(!dataDtold.equals(dataDt)) {
			System.out.println("not equals");
			dataDtold.clear();
			dataDtold.addAll(dataDt);
			listviewDt.setItems(dataDtold);
			dtNamePop.getContent().clear();
			dtNamePop.getContent().add(listviewDt);
		}
		listviewDt.getSelectionModel().selectedItemProperty().addListener(
				(ObservableValue<? extends String> observable, String oldValue, String newValue) ->{
					if(!listviewDt.getSelectionModel().isEmpty()) {
						textField_dtName.setText(newValue);
						textField_numType.clear();
						dtNamePop.hide();
					}
				});
		
		showPopup(dtNamePop,textField_dtName, listviewDt);
	}
	
	public void textField_numType_On() throws SQLException {
		textField_numType.requestFocus();
		if(textField_dtName.getText().isEmpty()) {
			dataEx.clear();
			dataEx.addAll("专家号","普通号");
		}
		else
		{
			if(db.getExofDt(textField_dtName.getText())) {
				dataEx.clear();
				dataEx.addAll("专家号","普通号");
			} else {
				dataEx.clear();
				dataEx.add("普通号");

			}
		}
		
		listviewEx.getSelectionModel().selectedItemProperty().addListener(
				(ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
					if(!listviewEx.getSelectionModel().isEmpty()) {
						textField_numType.setText(newValue);
						numTypePop.hide();
					}
				});
		
		showPopup(numTypePop,textField_numType,listviewEx);
	}

	public void textField_numName_On() throws Exception {
		if(textField_soName.getText().isEmpty()) {
			dataNN = db.getNumName();
		} else {
			dataNN = db.getNumofSo(textField_soName.getText());
		}
		if(!dataNNold.equals(dataNN)) {
			dataNNold.clear();
			dataNNold.addAll(dataNN);
			listviewNN.setItems(dataNNold);
			numNamePop.getContent().clear();
			numNamePop.getContent().add(listviewNN);
		}
		
		listviewNN.getSelectionModel().selectedItemProperty().addListener(
				(ObservableValue<? extends String> observable, String oldValue, String newValue) ->{
					if(!listviewNN.getSelectionModel().isEmpty()) {
						textField_numName.setText(newValue);
						try {
							Double cost = db.getCostofNum(textField_numName.getText());
							textField_Amount.setText(String.valueOf(cost));
							numNamePop.hide();
							CurPt cp = CurPt.getInstance();
							//Dbutil db = new Dbutil();
							//if the deposited amount greater than num cost
							if(db.getDepAmountOfPt(cp.getCurPt()) >= cost ) {
								textField_curAmount.setDisable(true);
								textField_curAmount.setText(cost.toString());
								textField_ecg.setDisable(true);
								db.UpdateDepAmountOfPt(cp.getCurPt(),-cost);
							}

						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
		showPopup(numNamePop,textField_numName,listviewNN);
	}

	public void textField_Amount_On() {

	}

	public void textField_curAmount_On() {
		textField_curAmount.requestFocus();
	}

	public void btn_ok_On() throws SQLException {
		if(!textField_soName.getText().isEmpty() && !textField_dtName.getText().isEmpty() &&
				!textField_numType.getText().isEmpty() && !textField_numName.getText().isEmpty() && 
				!textField_curAmount.getText().isEmpty() && !textField_ecg.getText().isEmpty()) {

			if(!db.CheckCountofNum(textField_numName.getText())) {
				Label_full.setVisible(true);
			} else {
				CurPt pt = CurPt.getInstance();
				System.out.println(pt.getCurPt());
				//int maxNum = db.getRegNum();
				Double cost = db.getCostofNum(textField_numName.getText());
				if(!Double.valueOf(textField_Amount.getText()).equals(cost)) {
					textField_Amount.setText(String.valueOf(cost));
					Label_change.setVisible(true);
				}
				int maxNum = db.UpdateRegNum(textField_dtName.getText(), pt.getCurPt(), textField_numName.getText(),
						Double.valueOf(textField_Amount.getText()));

				textField_regID.setText(String.valueOf(maxNum));
				System.out.println("OK");
			}

		}
	}

	public void Btnclear() {
		textField_curAmount.clear();
		textField_regID.clear();
		textField_ecg.clear();
		textField_Amount.clear();
		textField_dtName.clear();
		textField_numName.clear();
		textField_numType.clear();
		textField_soName.clear();

		Label_change.setVisible(false);
		Label_full.setVisible(false);

		//listviewSo.getSelectionModel().select(null);
		listviewDt.getSelectionModel().select(null);
		listviewNN.getSelectionModel().select(null);
		listviewEx.getSelectionModel().select(null);
	}

	public void Btnexit() {
		Stage stage = (Stage)getWindow();
		stage.close();
	}

	
	//public void textField_Cost
	
	/*
	 * show TextField's list
	 */
	public void showPopup(Popup popup,TextField textfield, ListView<String> listView) {
		listView.setPrefWidth(textfield.getWidth());
		listView.setPrefHeight(listView.getItems().size() * 24);
		popup.show(getWindow(),
					  getWindow().getX() + textfield.localToScene(0,0).getX() + textfield.getScene().getX(),
					  getWindow().getY() + textfield.localToScene(0,0).getY() + textfield.getScene().getY() + textfield.getHeight());
		System.out.println("popup");
	}
	
	
	
	/*
	 * get the main window
	 */
	public Window getWindow() {
		return textField_soName.getScene().getWindow();
	}
	
	/*
	 * (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			db = new Dbutil();
			dataSo = db.getsoName();
			//db.closeConnection();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			initializeData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initializeList();
		initializePop();
		
		textField_Amount.setEditable(false);;
		textField_regID.setEditable(false);
		Label_full.setVisible(false);
		Label_change.setVisible(false);
		
		//add listener of TextField_curAmount	
		textField_curAmount.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> vo, String arg1, String arg2) {
				// TODO Auto-generated method stub
				if(!textField_curAmount.getText().isEmpty()) {
					Double curAmount = Double.parseDouble(textField_curAmount.getText());
					Double Amount = Double.parseDouble(textField_Amount.getText());
					textField_ecg.setText(String.valueOf(curAmount-Amount));
				} else {
					textField_ecg.clear();
				}
			}
		});
	}
	/*
	 * initialize popup
	 */
	public void initializePop() {
		//initialize soNamePop
		soNamePop = new Popup();
		soNamePop.setAutoHide(true);
		soNamePop.getContent().add(listviewSo);
		//initialize dtNamePop
		dtNamePop = new Popup();
		dtNamePop.setAutoHide(true);
		//initialize
		numTypePop = new Popup();
		numTypePop.setAutoHide(true);
		numTypePop.getContent().add(listviewEx);
		//initialize
		numNamePop = new Popup();
		numNamePop.setAutoHide(true);
		numNamePop.getContent().add(listviewNN);
		//initialize 
		
	}
	public void initializeData() throws Exception {
		dataSo = db.getsoName();
		
		dataDtold = FXCollections.observableArrayList();
		dataDtold.add("");
		
		dataEx = FXCollections.observableArrayList();
		dataEx.addAll("专家号","普通号");
		
		dataNN = FXCollections.observableArrayList();
		dataNN.addAll("感冒","乙肝");
		
		dataNNold = FXCollections.observableArrayList();
		dataNNold.add("");
	}
	public void initializeList() {
		listviewSo = new ListView<String>();
		listviewSo.setItems(dataSo);
		listviewDt = new ListView<String>();
		
		listviewEx = new ListView<String>();
		listviewEx.setItems(dataEx);
		
		listviewNN = new ListView<String>();
	}
}
