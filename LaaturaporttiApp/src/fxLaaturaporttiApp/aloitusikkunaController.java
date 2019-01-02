package fxLaaturaporttiApp;

import LaaturaporttiApp.Rekisteri;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * @author Panu Niskakoski  panu.niskakoski@gmail.com
 * @version 6 Feb 2018
 *
 */
@SuppressWarnings("unused")
public class aloitusikkunaController implements ModalControllerInterface<String>{
    
    @FXML private Button buttonSuljeIkkuna;
    private Rekisteri rekisteri;
 
    /**
     * Sulkee aloitusikkunan
     */
    @FXML private void handleSuljeIkkuna() {
        Stage stage = (Stage) buttonSuljeIkkuna.getScene().getWindow();
        stage.close();
    }

    @Override
    public String getResult() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void handleShown() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setDefault(String arg0) {
        // TODO Auto-generated method stub
        
    }

    /**
     * Tarkistetaan onko tallennus tehty
     * @return true jos saa sulkea sovelluksen, false jos ei
     */
    public boolean voikoSulkea() {
        return true;
    }

    /**
     * @param rekisteri Rekisteri jota käytetään tässä käyttöliittymässä
     */
    public void setRekisteri(Rekisteri rekisteri) {
        this.rekisteri = rekisteri;
        
    }

}
