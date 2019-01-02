package fxLaaturaporttiApp;
	
import LaaturaporttiApp.Rekisteri;
import fi.jyu.mit.fxgui.ModalController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * @author Panu Niskakoski
 * @version 6 Feb 2018
 *
 * Pääohjelma LaaturaporttiApp-ohjelman käynnistämiseksi
 */
@SuppressWarnings("unused")
public class RekisteriMain extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {

		    final FXMLLoader ldr = new FXMLLoader(getClass().getResource("RekisteriGUIView.fxml"));
		    final Pane root = (Pane)ldr.load();
		    final RekisteriGUIController rekisteriCtrl = (RekisteriGUIController)ldr.getController();
		    
		    final Scene scene = new Scene(root);
		    scene.getStylesheets().add(getClass().getResource("rekisteri.css").toExternalForm());
		    primaryStage.setScene(scene);
            primaryStage.setTitle("LaaturaporttiApp");
            
            Rekisteri rekisteri = new Rekisteri();
            rekisteriCtrl.setRekisteri(rekisteri);
            
            primaryStage.setOnCloseRequest((event) -> {
                if ( !rekisteriCtrl.voikoSulkea() ) event.consume(); 
            });
            
            primaryStage.show();
            if (!rekisteriCtrl.avaa()) Platform.exit();
            
            ModalController.showModal(aloitusikkunaController.class.getResource("aloitusikkuna.fxml"), "LaaturaporttiApp", null, "");

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Käynnistää käyttöliittymän
	 * @param args ei käytössä
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
