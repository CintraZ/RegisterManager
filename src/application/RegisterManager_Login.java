package application;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;



public class RegisterManager_Login extends Application{
	@Override
	public void start(Stage primaryStage) throws Exception{
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/application/Login.fxml"));
			
			primaryStage.setTitle("RegisterManager");
			primaryStage.setScene(new Scene(root));
			//primaryStage.setWidth(460);
			//primaryStage.setHeight(235);
			//primaryStage.setResizable(false);
			
			primaryStage.show();

		} catch (Exception ex){
			ex.printStackTrace();
		}	
	}
	public static void main(String[] args) {
		launch(args);
	}
}
