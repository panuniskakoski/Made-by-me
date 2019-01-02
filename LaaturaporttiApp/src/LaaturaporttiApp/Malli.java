/**
 * 
 */
package LaaturaporttiApp;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Rekisterin malli joka osaa mm. itse huolehtia id:st‰‰n.
 * @author Panu Niskakoski  panu.niskakoski@gmail.com
 * @version 6 Mar 2018
 */
public class Malli implements Cloneable {
    
    private int     malliId;
    private String  malli       = "";
    private int     merkkiId;
    
    private static int seuraavaNro  = 1;
    
    /**
     * Alustetaan malli
     */
    public Malli() {
        // T‰m‰ riitt‰‰
    }
    
    /**
     * Alustetaan tietyn auton malli ja merkki
     * @param malliId mallin ID numero
     * @param merkkiId merkin ID numero
     */
    public Malli(int malliId, int merkkiId) {
        this.malliId = malliId;
        this.merkkiId = merkkiId;
    }
    
    /**
     * Apumetodi, jolla saadaan t‰ytetty‰ testiarvot mallille.
     */
    public void taytaMallinTiedot() {
        malli = "Micra";
    }
    
    /**
     * Tulostetaan mallin tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(String.format("%03d", malliId) + " " + merkkiId + " " + malli);
    }
    
    /**
     * Tulostetaan mallin tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    /**
     * Antaa mallille seuraavan id-numeron
     * @return mallin uusi Malli-ID
     * @example
     * <pre name="test">
     *      Malli malli = new Malli();
     *      malli.getMalliId() === 0;
     *      malli.rekisteroi();
     *      Malli malli2 = new Malli();
     *      malli2.rekisteroi();
     *      int n1 = malli.getMalliId();
     *      int n2 = malli2.getMalliId();
     *      n1 === n2 - 1;
     * </pre>
     */
    public int rekisteroi() {
        malliId = seuraavaNro;
        seuraavaNro += 1;
        return malliId;
    }
    
    /**
     * Antaa mallille jo olemassaolevan malli id:n
     * @param valmisMalliId olemassaoleva malli id
     */
    public void rekisteroi(int valmisMalliId) {
        this.malliId = valmisMalliId;
    }
    
    /**
     * Tehd‰‰n identtinen klooni mallista
     */
    @Override
    public Malli clone() throws CloneNotSupportedException {
        return (Malli)super.clone();
    }
    
    /**
     * Palauttaa mallin tiedot merkkijonona jonka voi tallentaa
     * tiedostoon.
     * @return malli tolppaeroteltuna merkkijonona
     * @example
     * <pre name="test">
     *      Malli malli = new Malli();
     *      malli.parse("1  |  500    |  1");
     *      malli.toString().startsWith("1|500|") === true;
     * </pre>
     */
    @Override
    public String toString() {
        return "" + getMalliId() + "|" + malli + "|" + merkkiId;
    }
    
    /**
     * Selvitt‰‰ mallin tiedot | erotellusta merkkijonosta.
     * Pit‰‰ huolen ett‰ seuraavaNro on suurempi kuin tuleva malliId.
     * @param rivi jolle mallin tiedot otetaan
     * @example
     * <pre name="test">
     *      Malli malli = new Malli();
     *      malli.parse("1  |  500    |  1");
     *      malli.getMalliId() === 1;
     *      malli.toString().startsWith("1|500|") === true;
     *      
     *      malli.rekisteroi();
     *      int n = malli.getMalliId();
     *      malli.parse(""+(n+20));
     *      malli.rekisteroi();
     *      malli.getMalliId() === n+20+1;
     *      </pre>
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        setMalliId(Mjonot.erota(sb, '|', getMalliId()));
        malli = Mjonot.erota(sb, '|', malli);
        merkkiId = Mjonot.erota(sb, '|', merkkiId);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        return this.toString().equals(obj.toString());
    }
    
    @Override
    public int hashCode() {
        return malliId;
    }
    
    /**
     * P‰‰ohjelma jossa voidaan ajaa kokeiluja
     * @param args ei k‰ytˆss‰
     */
    public static void main(String[] args) {
        Malli malli = new Malli();
        malli.tulosta(System.out);
    }
    
    /**
     * Hakee numeron joka olisi seuraavan rekisterˆidyn mallin malli id
     * @return seuraava malli id
     */
    public static int getSeuraavaMalliNro() {
        int nro = seuraavaNro;
        return nro;
    }
    
    /**
     * Palauttaa mallin id-numeron
     * @return mallin id-numero
     */
    public int getMalliId() {
        return malliId;
    }
    
    /**
     * Hakee auton mallin merkkijonona
     * @return auton malli
     */
    public String getMalli() {
        return malli;
    }
    
    /**
     * Palauttaa mallin merkki ID:n
     * @return merkki ID
     */
    public int getMerkkiId() {
        return merkkiId;
    }

    /**
     * Asettaa malli-ID:n ja samalla varmistaa ett‰
     * seuraava numero on aina suurempi kuin t‰h‰n
     * menness‰ suurin.
     * @param nro asetettava malliId
     */
    private void setMalliId (int nro) {
        malliId = nro;
        if (malliId >= seuraavaNro) seuraavaNro = malliId + 1;
    }
    
    /**
     * Asettaa auton mallin
     * @param s haluttu malli merkkijonona
     * @return null
     */
    public String setMalli(String s) {
        malli = s;
        return null;
    }
    
    /**
     * Asettaa valmiin merkki ID:n mallille
     * @param valmisMerkkiId valmis Merkki ID
     */
    public void setMerkkiId(int valmisMerkkiId) {
        merkkiId = valmisMerkkiId;
    }
}
