package fxLaaturaporttiApp;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

import LaaturaporttiApp.Auto;
import LaaturaporttiApp.Malli;
import LaaturaporttiApp.Rekisteri;
import LaaturaporttiApp.SailoException;
import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * @author Panu Niskakoski  panu.niskakoski@gmail.com
 * @version 6 Feb 2018
 *
 * Ei osata viel‰:
 * - Ladata kuvaa
 * - Tulostaa
 * - Teett‰‰ PDF-tiedostoa
 */
public class RekisteriGUIController implements Initializable, ModalControllerInterface<String> {
    
    @FXML private TextField hakuehto;
    @FXML private ComboBoxChooser<String> cbKentat;
    @FXML private Label labelVirhe;
    @FXML private ListChooser<Auto> chooserAutot;
    @FXML private ScrollPane panelAuto;
    
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
    
    @FXML private TextField editHakuehto;
    
    private TextField edits[];

    @Override
    public String getResult() {
        // Auto-generated method stub
        return null;
    }

    @Override
    public void handleShown() {
        // Auto-generated method stub
        
    }

    @Override
    public void setDefault(String arg0) {
        // Auto-generated method stub
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();
    }
    
    @FXML private void handleHakuehto() {
        hae(0);
    }

    /**
     * K‰sitell‰‰n uuden auton lis‰‰miseen liittyv‰t toiminnot
     */
    @FXML protected void handleLisaaUusiAuto() {
        uusiAuto();
    }
    
    /**
     * K‰sitell‰‰n tietojen tallentaminen
     */
    @FXML private void handleTallenna() {
        tallenna();
    }
    
    /**
     * K‰sitell‰‰n kuvan lataaminen
     */
    @FXML void handleLataaKuva() {
        Dialogs.showMessageDialog("Ei osata viel‰ ladata kuvaa!");
    }
    
    /**
     * K‰sitell‰‰n raportin tulostaminen
     */
    @FXML private void handleTulosta() {
        Dialogs.showMessageDialog("Ei osata viel‰ tulostaa!");
        // TulostusController tulostusCtrl = TulostusController.tulosta(null);
        // tulostaValitut(tulostusCtrl.getTextArea());
    }
    
    /**
     * K‰sitell‰‰n pdf-tiedoston tuottaminen
     */
    @FXML void handleTeetaPDF() {
        Dialogs.showMessageDialog("Ei osata viel‰ teett‰‰ PDF:‰‰!");
    }
    
    /**
     * K‰sitell‰‰n auton poistaminen
     */
    @FXML void handlePoistaAuto() {
        poistaAuto();
    }

    /**
     * K‰sitell‰‰n tietojen muuttaminen
     */
    @FXML void handleMuutaTietoja() {
        muutaTietoja();
        
    }

    /**
     * K‰sitell‰‰n tietojen n‰ytt‰minen
     */
    @FXML void handleTietoja() {
        ModalController.showModal(aloitusikkunaController.class.getResource("aloitusikkuna.fxml"), "Tietoja", null, "");
    }
    
    /**
     * K‰sitell‰‰n sulje toimintoa
     */
    @FXML private void handleSulje() {
        tallenna();
        Platform.exit();
    }
    


//======================================================================================================================================
// T‰st‰ eteenp‰in ei k‰yttˆliittym‰‰n suoraan liittyv‰‰ koodia
    
    private String rekisterinnimi = "rekisteri";
    private Rekisteri rekisteri;
    private Auto autoKohdalla;
    
    /**
     * Tekee tarvittavat muut alustukset, nyt vaihdetaan GridPanen tilalle
     * yksi iso tekstikentt‰, johon voidaan tulostaa j‰senten tiedot.
     * Alustetaan myˆs j‰senlistan kuuntelija
     */
    protected void alusta() {
        chooserAutot.clear();
        chooserAutot.addSelectionListener(e -> naytaAuto());
        
        edits = new TextField[] {editAsiakas, editPuhelin, editSposti, editRekisteriNro,
                                 editMerkki, editMalli, editVuosimalli, editLisatty,
                                 editAutonJarrut, editTuulilasiJaIkkunat, editRenkaat,
                                 editVanteet, editRengaskulmat, editMaalipinta, editKorroosio,
                                 editTekniikka, editSisatilat, editTurvallisuus, editLisavarusteet,
                                 editHuoltohistoria, editAutoDNA};           
    }
    
    /*
    private void naytaVirhe(String virhe) {
        if (virhe == null || virhe.isEmpty()) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            return;
        }
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe");
    }
    
    /*
    private void setTitle(String title) {
        ModalController.getStage(chooserAutot).setTitle(title);
    }
    */
    
    /**
     * Alustaa rekisterin lukemalla sen valitun nimisest‰ tiedostosta
     * @param nimi tiedosto josta rekisterin tiedot luetaan
     * @return null jos onnistuu, muuten virhe tekstin‰
     */
    protected String lueTiedosto(String nimi) {
        rekisterinnimi = nimi;
        try {
            rekisteri.lueTiedostosta(nimi);
            haeAuto(0);
            return null;
        } catch (SailoException e) {
            haeAuto(0);
            String virhe = e.getMessage();
            if (virhe != null) Dialogs.showMessageDialog(virhe);
            return virhe;
        }
    }

    /**
     * N‰ytt‰‰ listasta valitun j‰senen tiedot, tilap‰isesti yhteen isoon edit-kentt‰‰n
     */
    private void naytaAuto() {
        autoKohdalla = chooserAutot.getSelectedObject();
        
        if (autoKohdalla == null) return;
        
        AutoDialogController.naytaAuto(autoKohdalla, edits);
        
        editYhteenveto.setText(autoKohdalla.getYhteenveto());
        
        editMalli.setText(rekisteri.annaMallinNimi(autoKohdalla.getMalliId()));
        
        int malliId = autoKohdalla.getMalliId();
        Malli malli = rekisteri.annaMalli(malliId);
        int merkkiId = malli.getMerkkiId();
        
        editMerkki.setText(rekisteri.annaMerkinNimi(merkkiId));
    }
    
  
    
    /**
     * Luetaan tiedoston nimi mik‰li se ei ole null
     * @return false jos null, muuten true
     */
    public boolean avaa() {
        lueTiedosto(rekisterinnimi);
        return true;
    }
    
    /**
     * Hakee auton tiedot listaan
     * @param anro auton numero, joka aktivoidaan haun j‰lkeen
     */
    protected void haeAuto(int anro) {
        chooserAutot.clear();
        
        int index = 0;
        Collection<Auto> autot;
        try {
            autot = rekisteri.etsiAutoista("", 0, rekisteri);
            int i = 0;
            for (Auto auto : autot) {
                if (auto.getIdNro() == anro) index = i;
                chooserAutot.add(auto.getAsiakas(), auto);
                i++;
            }
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Auton hakemisessa ongelmia! " + ex.getMessage());
        }
        chooserAutot.setSelectedIndex(index); // t‰st‰ tulee muutosviesti joka n‰ytt‰‰ auton
    }
    
    /**
     * Tietojen tallentaminen
     */
    private String tallenna() {
        try {
            rekisteri.tallenna();
            return null;
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Tallennuksessa ongelmia! " + ex.getMessage());
            return ex.getMessage();
        }   
    }
    
    /**
     * Tarkistaa onko tallennus tehty
     * @return true jos saa sulkea sovelluksen, false jos ei
     */
    public boolean voikoSulkea() {
        tallenna();
        return true;
    }
    
    
    /**
     * @param rekisteri Rekisteri jota k‰ytet‰‰n t‰ss‰ k‰yttˆliittym‰ss‰
     */
    public void setRekisteri(Rekisteri rekisteri) {
        this.rekisteri = rekisteri;
        naytaAuto();
    }

    /**
     * Luo uuden auton jota aletaan editoimaan
     */
    protected void uusiAuto() {
        Auto uusi = new Auto();
        uusi = AutoDialogController.kysyAuto(null, uusi, rekisteri);
        if (uusi == null) return;
        haeAuto(uusi.getIdNro());
    }
    
    private void muutaTietoja() {
        if (autoKohdalla == null) return;
        try {
            Auto auto;
            auto = AutoMuokkausDialogController.kysyAuto(null, autoKohdalla.clone(), rekisteri);
            if (auto == null) return;
            rekisteri.korvaaTaiLisaa(auto);
            haeAuto(auto.getIdNro());
        } catch (CloneNotSupportedException e) {
            //
        }
    }
    
    /**
     * Auton poistaminen
     * Kysyt‰‰n varmistus myˆs
     */
    private void poistaAuto() {
        if (autoKohdalla == null) return;
        if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko auto: " + autoKohdalla.getAsiakas(), "Kyll‰", "Ei") )
            return;
        rekisteri.poista(autoKohdalla);
        int index = chooserAutot.getSelectedIndex();
        haeAuto(0);
        chooserAutot.setSelectedIndex(index);
    }
    
    private void hae(int anr) {
        int anro = anr; // anro auton numero, joka aktivoidaan haun j‰lkeen
        if (anro <= 0) {
            Auto kohdalla = autoKohdalla;
            if (kohdalla != null) anro = kohdalla.getIdNro();
        }
        
        int k = cbKentat.getSelectionModel().getSelectedIndex();
                String ehto = hakuehto.getText();
        if (ehto.indexOf('*') < 0) ehto = "*" + ehto + "*";
        
        chooserAutot.clear();
        
        int index = 0;
        Collection<Auto> autot;
        try {
            autot = rekisteri.etsiAutoista(ehto, k, rekisteri);
            int i = 0;
            for (Auto auto : autot) {
                if (auto.getIdNro() == anro) index = i;
                chooserAutot.add(auto.getAsiakas(), auto);
                i++;
            }
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Auton hakemisessa ongelmia! " + ex.getMessage());
        }
        chooserAutot.getSelectionModel().select(index); // t‰st‰ tulee muutosviesti joka n‰ytt‰‰ auton
    }

}
