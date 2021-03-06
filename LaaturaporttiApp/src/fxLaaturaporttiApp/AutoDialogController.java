package fxLaaturaporttiApp;

import java.net.URL;
import java.util.ResourceBundle;

import LaaturaporttiApp.Auto;
import LaaturaporttiApp.Malli;
import LaaturaporttiApp.Merkki;
import LaaturaporttiApp.Rekisteri;
import LaaturaporttiApp.SailoException;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * @author Panu Niskakoski  panu.niskakoski@gmail.com
 * @version 10 Apr 2018
 *
 * Ei ostata viel�:
 * - Ladata kuvaa
 */
public class AutoDialogController implements ModalControllerInterface<Auto>, Initializable {

    @FXML private TextField editAsiakas;
    @FXML private TextField editPuhelin;
    @FXML private TextField editSposti;
    @FXML private TextField editRekisteriNro;
    @FXML private TextField editMerkki;
    @FXML private TextField editMalli;
    @FXML private TextField editVuosimalli;
    @FXML private TextField editLisatty;
    
    @FXML private TextArea editYhteenveto;
    
    @FXML private TextField editAutonJarrut; 
    @FXML private TextField editTuulilasiJaIkkunat;
    @FXML private TextField editRenkaat;
    @FXML private TextField editVanteet;
    @FXML private TextField editRengaskulmat;
    @FXML private TextField editMaalipinta;
    @FXML private TextField editKorroosio;
    @FXML private TextField editTekniikka;
    @FXML private TextField editSisatilat;
    @FXML private TextField editTurvallisuus;
    @FXML private TextField editLisavarusteet;
    @FXML private TextField editHuoltohistoria;
    @FXML private TextField editAutoDNA;
    
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();
    }
    
    /**
     * T��ll� k�sitell��n kaikki tarvittava, kun painetaan auton lis�yksen lopuksi nappia
     * "Tallenna ja Sulje". Siksi t��ll�, ett� p��st��n k�siksi Auto-luokan lis�ksi luokkiin
     * Malli ja Merkki.
     * @throws SailoException mik�li jokin menee pieleen
     */
    @FXML private void handleLisaaAuto() throws SailoException {
        if (autoKohdalla != null && autoKohdalla.getAsiakas().trim().equals("")) {
            return;
        }
        // Luodaan Malli ja Merkki
        Malli malliKohdalla1 = new Malli();
        Merkki merkkiKohdalla1 = new Merkki();
        
        // Asetetaan sy�tteet Mallille ja Merkille
        malliKohdalla1.setMalli(editMalli.getText());
        merkkiKohdalla1.setMerkki(editMerkki.getText());
        
        // Asetetaan sy�tteet Autolle
        autoKohdalla.setAsiakas(editAsiakas.getText());
        autoKohdalla.setPuhelin(editPuhelin.getText());
        autoKohdalla.setSposti(editSposti.getText());
        autoKohdalla.setRekisteriNro(editRekisteriNro.getText());
        autoKohdalla.setVuosimalli(editVuosimalli.getText());
        autoKohdalla.setLisatty(editLisatty.getText());
        
        autoKohdalla.setYhteenveto(editYhteenveto.getText());
        
        autoKohdalla.setAutonJarrut(editAutonJarrut.getText());
        autoKohdalla.setTuulilasiJaIkkunat(editTuulilasiJaIkkunat.getText());
        autoKohdalla.setRenkaat(editRenkaat.getText());
        autoKohdalla.setVanteet(editVanteet.getText());
        autoKohdalla.setRengaskulmat(editRengaskulmat.getText());
        autoKohdalla.setMaalipinta(editMaalipinta.getText());
        autoKohdalla.setKorroosio(editKorroosio.getText());
        autoKohdalla.setTekniikka(editTekniikka.getText());
        autoKohdalla.setSisatilat(editSisatilat.getText());
        autoKohdalla.setTurvallisuus(editTurvallisuus.getText());
        autoKohdalla.setLisavarusteet(editLisavarusteet.getText());
        autoKohdalla.setHuoltohistoria(editHuoltohistoria.getText());
        autoKohdalla.setAutoDna(editAutoDNA.getText());
        
        // Jos Merkki on jo taulukossa, ei sit� lis�t�.
        // Jos Merkki ei ole taulukossa, rekister�id��n se ja lis�t��n se taulukkoon
        if (rekisteri.onkoMerkkiOlemassa(merkkiKohdalla1.getMerkki()) == Merkki.getSeuraavaMerkkiNro()) {
        merkkiKohdalla1.rekisteroi();
        rekisteri.lisaa(merkkiKohdalla1);
        malliKohdalla1.setMerkkiId(merkkiKohdalla1.getMerkkiId());
        } else {
            int merkkiIdNro = rekisteri.onkoMerkkiOlemassa(merkkiKohdalla1.getMerkki());
            merkkiKohdalla1.rekisteroi(merkkiIdNro);
        }
        
        // Jos Malli on jo taulukossa, ei sit� lis�t�.
        // Jos Malli ei ole taulukossa, rekister�id��n se ja lis�t��n se taulukkoon
        if (rekisteri.onkoMalliOlemassa(malliKohdalla1.getMalli()) == Malli.getSeuraavaMalliNro()) {
        malliKohdalla1.rekisteroi();
        malliKohdalla1.setMerkkiId(merkkiKohdalla1.getMerkkiId());
        autoKohdalla.rekisteroi(malliKohdalla1.getMalliId());
        rekisteri.lisaa(malliKohdalla1);
        rekisteri.lisaa(autoKohdalla);
        } else {
            malliKohdalla1.rekisteroi(rekisteri.onkoMalliOlemassa(malliKohdalla1.getMalli()));
            malliKohdalla1.setMerkkiId(merkkiKohdalla1.getMerkkiId());
            autoKohdalla.rekisteroi(rekisteri.onkoMalliOlemassa(malliKohdalla1.getMalli()));
            rekisteri.lisaa(autoKohdalla);
        }
        ModalController.closeStage(editAsiakas);
    }
    
    @FXML private void handlePeruuta() {
        autoKohdalla = null;
        ModalController.closeStage(editAsiakas);
    }
    
    /**
     * K�sitell��n kuvan lataaminen
     */
    @FXML void handleLataaKuva() {
        Dialogs.showMessageDialog("Ei osata viel� ladata kuvaa!");
    }
    
//================================================================================================
    
    private Auto autoKohdalla;
    private Rekisteri rekisteri;
    private TextField[] edits;
    
    @Override
    public Auto getResult() {
        return autoKohdalla;
    }

    @Override
    public void handleShown() {
        // Auto-generated method stub
        
    }
    
    private void alusta() {
        edits = new TextField[] {editAsiakas, editPuhelin, editSposti, editRekisteriNro,
                editMerkki, editMalli,
                editVuosimalli, editLisatty,
                editAutonJarrut, editTuulilasiJaIkkunat, editRenkaat,
                editVanteet, editRengaskulmat, editMaalipinta, editKorroosio,
                editTekniikka, editSisatilat, editTurvallisuus, editLisavarusteet,
                editHuoltohistoria, editAutoDNA};
        
        int i = 0;
        for (TextField edit : edits) {
            final int k = ++i;
            edit.setOnKeyReleased(e -> kasitteleMuutosAutoon(k, (TextField)(e.getSource())));
        }
        
        editYhteenveto.setOnKeyReleased(e -> yhteenvetoMuuttui());
    }

    @Override
    public void setDefault(Auto oletusAuto) {
        autoKohdalla = oletusAuto;
        naytaAuto(autoKohdalla, edits);
        naytaAutoYhteenveto(autoKohdalla);
        
    }
    
    /**
     * K�sitell��n muutokset auton tietoihin (paitsi merkki ja malli)
     * @param k tapaus mit� k�sitell��n
     * @param edit textfield mit� muokataan
     */
    private void kasitteleMuutosAutoon(int k, TextField edit) {
        if (autoKohdalla == null) return;
        String s = edit.getText();
        switch (k) {
            case 1 : autoKohdalla.setAsiakas(s); break;
            case 2 : autoKohdalla.setPuhelin(s); break;
            case 3 : autoKohdalla.setSposti(s); break;
            case 4 : autoKohdalla.setRekisteriNro(s); break;

            case 7 : autoKohdalla.setVuosimalli(s); break;
            case 8 : autoKohdalla.setLisatty(s); break;
            
            case 9 : autoKohdalla.setAutonJarrut(s); break;
            case 10 : autoKohdalla.setTuulilasiJaIkkunat(s); break;
            case 11 : autoKohdalla.setRenkaat(s); break;
            case 12 : autoKohdalla.setVanteet(s); break;
            case 13 : autoKohdalla.setRengaskulmat(s); break;
            case 14 : autoKohdalla.setMaalipinta(s); break;
            case 15 : autoKohdalla.setKorroosio(s); break;
            case 16 : autoKohdalla.setTekniikka(s); break;
            case 17 : autoKohdalla.setSisatilat(s); break;
            case 18 : autoKohdalla.setTurvallisuus(s); break;
            case 19 : autoKohdalla.setLisavarusteet(s); break;
            case 20 : autoKohdalla.setHuoltohistoria(s); break;
            case 21 : autoKohdalla.setAutoDna(s); break;
            default:
        }
    }
    
    /**
     * K�sitell��n muutos yhteenvedossa
     */
    private void yhteenvetoMuuttui() {
        String s = editYhteenveto.getText();
        autoKohdalla.setYhteenveto(s);
    }
    
    /**
     * N�ytet��n auton yhteenveto TextField komponentissa
     * @param auto n�ytett�v�n auton yhteenveto
     */
    public void naytaAutoYhteenveto(Auto auto) {
        if (auto == null) return;
        
        editYhteenveto.setText(auto.getYhteenveto());
    }
    
    /**
     * N�ytet��n auton malli TextField komponentissa
     * @param auto n�ytett�v� auto
     */
    public void naytaAutoMalli(Auto auto) {
        if (auto == null) return;
        
        editMalli.setText(rekisteri.annaMallinNimi(auto.getMalliId()));
    }
    
    /**
     * N�ytet��n auton merkki TextField komponentissa
     * @param malli n�ytett�v�n auton malli
     */
    public void naytaAutoMerkki(Malli malli) {
        if (malli == null) return;
        
        editMerkki.setText(rekisteri.annaMerkinNimi(malli.getMerkkiId()));
    }
    
    /**
     * N�ytet��n auton tiedot TextField komponentteihin
     * @param auto n�ytett�v� auto
     * @param edits taulukko edit-kentist� joihin tiedot pit�� laittaa
     */
    public static void naytaAuto(Auto auto, TextField[] edits) {
        if (auto == null) return;
        
        edits[0].setText(auto.getAsiakas());
        edits[1].setText(auto.getPuhelin());
        edits[2].setText(auto.getSposti());
        edits[3].setText(auto.getRekisteriNro());

        edits[6].setText(auto.getVuosimalli());
        edits[7].setText(auto.getLisatty());
        
        edits[8].setText(auto.getAutonJarrut());
        edits[9].setText(auto.getTuulilasiJaIkkunat());
        edits[10].setText(auto.getRenkaat());
        edits[11].setText(auto.getVanteet());
        edits[12].setText(auto.getRengaskulmat());
        edits[13].setText(auto.getMaalipinta());
        edits[14].setText(auto.getKorroosio());
        edits[15].setText(auto.getTekniikka());
        edits[16].setText(auto.getSisatilat());
        edits[17].setText(auto.getTurvallisuus());
        edits[18].setText(auto.getLisavarusteet());
        edits[19].setText(auto.getHuoltohistoria());
        edits[20].setText(auto.getAutoDna());
    }
    
    /**
     * Kysyt��n auto
     * @param modalityStage Kenelle modaalisuus
     * @param oletus Oletusauto
     * @param rekisteri Rekisteri
     * @return Kohde
     */
    public static Auto kysyAuto(Stage modalityStage, Auto oletus, Rekisteri rekisteri) {
        return ModalController.<Auto, AutoDialogController>showModal(
                AutoDialogController.class.getResource("autonlisaaminen.fxml"),
                "Rekisteri",
                modalityStage, oletus,
                ctrl -> ctrl.setRekisteri(rekisteri)
                );           
    }
    
    /**
     * Alustetaan rekisteri
     * @param kentta kentta
     * @param rekisteri rekisteri
     */
    private void setRekisteri(Rekisteri rekisteri) {
        this.rekisteri = rekisteri;
    }

}
