package application;

import java.lang.invoke.StringConcatException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.LoadException;
import javafx.scene.control.ComboBox;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

public class DoctorPageController implements Initializable {
	@FXML
	private TableColumn CregID;
	@FXML
	private TableColumn CptName;
	@FXML
	private TableColumn CregDate;
	@FXML
	private TableColumn CnumType1;
	@FXML
	private TableColumn CsoName;
	@FXML
	private TableColumn CdtID;
	@FXML
	private TableColumn CdtName;
	@FXML
	private TableColumn CnumType2;
	@FXML
	private TableColumn CregCount;
	@FXML
	private TableColumn CIncome;
	@FXML
    private DatePicker DP_Start;
	@FXML
    private DatePicker DP_End;
	@FXML
    private TableView PtTable;
	@FXML
    private TableView InTable;

    private ObservableList<RegData> ptData;// = FXCollections.observableArrayList();
    private ObservableList<IncomeData> inData;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setPtTable();
        initializeDate();
        setInTable();

        Timer timer  = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //setPtTable();
                setPtTable();
            }
        },0,1000);
        //set datepicker format

    }

    public void setPtTable() {
        try {
            CurPt dt = CurPt.getInstance();
            Dbutil db = new Dbutil();
            ptData = db.getRegInfo(dt.getCurPt());
            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ObservableList<TableColumn> obList = PtTable.getColumns();

        obList.get(0).setCellValueFactory(new PropertyValueFactory("regID"));
        obList.get(1).setCellValueFactory(new PropertyValueFactory("ptName"));
        obList.get(2).setCellValueFactory(new PropertyValueFactory("regDate"));
        obList.get(3).setCellValueFactory(new PropertyValueFactory("numType"));

        PtTable.setItems(ptData);
    }

    public void setInTable() {
        try {
            Dbutil db = new Dbutil();
            inData = db.getIncome(DP_Start.getValue(),DP_End.getValue());
            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ObservableList<TableColumn> obList = InTable.getColumns();

        obList.get(0).setCellValueFactory(new PropertyValueFactory("soName"));
        obList.get(1).setCellValueFactory(new PropertyValueFactory("dtID"));
        obList.get(2).setCellValueFactory(new PropertyValueFactory("dtName"));
        obList.get(3).setCellValueFactory(new PropertyValueFactory("numType"));
        obList.get(4).setCellValueFactory(new PropertyValueFactory("regCount"));
        obList.get(5).setCellValueFactory(new PropertyValueFactory("Income"));

        InTable.setItems(inData);
    }


    public void initializeDate() {
        StringConverter converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            @Override
            public String toString(LocalDate date) {
                if(date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if(string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        DP_Start.setConverter(converter);
        DP_End.setConverter(converter);
        DP_Start.setValue(LocalDate.now());
        DP_End.setValue(LocalDate.now());
    }


}
