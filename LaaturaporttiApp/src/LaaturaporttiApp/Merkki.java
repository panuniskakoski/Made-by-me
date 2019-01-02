/**
 * 
 */
package LaaturaporttiApp;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Rekisterin merkki joka osaa mm. itse huolehtia id:st‰‰n.
 * @author Panu Niskakoski  panu.niskakoski@gmail.com
 * @version 6 Mar 2018
 */
public class Merkki implements Cloneable {
    
    private int     merkkiId;
    private String  merkki       = "";
    
    private static int seuraavaNro  = 1;
    
    /**
     * Alustetaan merkki
     */
    public Merkki() {
        // T‰m‰ riitt‰‰
    }
    
    /**
     * Arvotaan satunnainen kokonaisluku v‰lille [ala,yla]
     * @param ala arvonnan alaraja
     * @param yla arvonnan ylraja
     * @return satunnainen luku v‰lilt‰ [ala,yl‰]
     */
    public static int rand(int ala, int yla) {
        double n = (yla-ala)*Math.random() + ala;
        return (int)Math.round(n);
    }
    
    /**
     * Apumetodi, jolla saadaan t‰ytetty‰ testiarvot merkille.
     */
    public void taytaMerkinTiedot() {
        merkki = "Audi" + rand(1000, 9999);
    }
    
    /**
     * Tulostetaan merkin tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(String.format("%03d", merkkiId) + " " + merkki);
    }
    
    /**
     * Tulostetaan merkin tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    /**
     * Antaa merkille seuraavan id-numeron
     * @return merkin uusi tunnus_nro
     * @example
     * <pre name="test">
     *      Merkki merkki = new Merkki();
     *      merkki.getMerkkiId() === 0;
     *      merkki.rekisteroi();
     *      Merkki merkki2 = new Merkki();
     *      merkki2.rekisteroi();
     *      int n1 = merkki.getMerkkiId();
     *      int n2 = merkki2.getMerkkiId();
     *      n1 === n2 - 1;
     * </pre>
     */
    public int rekisteroi() {
        merkkiId = seuraavaNro;
        seuraavaNro += 1;
        return merkkiId;        
    }
    
    /**
     * Rekisterˆi merkin valmiin merkki id:n perusteella
     * @param valmisMerkkiId valmis merkki id
     */
    public void rekisteroi(int valmisMerkkiId) {
        merkkiId = valmisMerkkiId;
    }
    
    /**
     * Tehd‰‰n identtinen klooni merkist‰
     */
    @Override
    public Merkki clone() throws CloneNotSupportedException {
        return (Merkki)super.clone();
    }
    
    /**
     * Palauttaa merkin tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return merkki tolppaeroteltuna merkkijonona
     * @example
     * <pre name="test">
     *      Merkki merkki = new Merkki();
     *      merkki.parse("1  |Fiat");
     *      merkki.toString().startsWith("1|Fiat") === true;
     * </pre>
     */
    @Override
    public String toString() {
        return "" + getMerkkiId() + "|" + merkki;
    }
    
    /**
     * Selvitt‰‰ merkin tiedot | erotellusta merkkijonosta.
     * Pit‰‰ huolen ett‰ seuraavaNro on suurempi kuin tuleva merkkiId.
     * @param rivi josta merkin tiedot otetaan.
     * @example
     * <pre name="test">
     *      Merkki merkki = new Merkki();
     *      merkki.parse("1  |   Fiat");
     *      merkki.getMerkkiId() === 1;
     *      merkki.toString().startsWith("1|Fiat") === true;
     *      
     *      merkki.rekisteroi();
     *      int n = merkki.getMerkkiId();
     *      merkki.parse(""+(n+20));
     *      merkki.rekisteroi();
     *      merkki.getMerkkiId() === n+20+1;
     *      </pre>
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        setMerkkiId(Mjonot.erota(sb, '|', getMerkkiId()));
        merkki = Mjonot.erota(sb, '|', merkki);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        return this.toString().equals(obj.toString());
    }
    
    @Override
    public int hashCode() {
        return merkkiId;
    }
    
    /**
     * Testiohjelma Merkeille.
     * @param args ei k‰ytˆss‰
     */
    public static void main(String[] args) {
        Merkki merkki = new Merkki();
        merkki.tulosta(System.out);
    }

    /**
     * Hakee merkin nimen merkkijonona
     * @return merkin nimi
     */
    public String getMerkki() {
        return merkki;
    }

    /**
     * Asettaa merkin nimeksi halutun merkkijonon
     * @param s annettu merkkijono
     * @return null
     */
    public String setMerkki(String s) {
        merkki = s;
        return null;
    }
    
    /**
     * Palauttaa merkin id-numeron
     * @return merkin id-numero
     */
    public int getMerkkiId() {
        return merkkiId;
    }

    /**
     * Asettaa merkki ID:n ja samalla varmistaa ett‰
     * seuraava numero on aina suurempi kuin t‰h‰n menness‰ suurin.
     * @param nro asetettava merkki ID
     */
    private void setMerkkiId(int nro) {
        merkkiId = nro;
        if (merkkiId >= seuraavaNro) seuraavaNro = merkkiId + 1;
    }

    /**
     * Hakee seuraavalle rekisterˆit‰v‰lle merkille annettavan merkki id:n
     * @return seuraava merkki id
     */
    public static int getSeuraavaMerkkiNro() {
        int nro = seuraavaNro;
        return nro;
    }

}
