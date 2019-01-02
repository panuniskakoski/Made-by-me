package LaaturaporttiApp;

/**
 * Poikkeusluokka tietorakenteesta aiheutuville poikkeuksille
 * @author Panu Niskakoski
 * @version 4 Mar 2018
 */
public class SailoException extends Exception {
    private static final long serialVersionUID = 1L;
    
    /**
     * Poikkeuksen muodostaja jolle tuodaan poikkeuksessa 
     * käytettävä viesti
     * @param viesti poikkeuksen viesti
     */
    public SailoException(String viesti) {
        super(viesti);
    }
}
