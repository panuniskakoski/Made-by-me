/**
 * 
 */
package LaaturaporttiApp;

import java.io.File;
import java.util.Collection;

/**
 * Rekisteri luokka. Pit‰‰ Autot, Mallit ja Merkit aisoissa.
 * V‰litt‰‰ kaiken tarvittavan tiedon j‰senistˆihin.
 * @author Panu Niskakoski  panu.niskakoski@gmail.com
 * @version 5 Mar 2018
 *
 */
public class Rekisteri {
    
    private Autot autot = new Autot();
    private Mallit mallit = new Mallit();
    private Merkit merkit = new Merkit();
    
    /**
     * Poistaa autoista halutun auton
     * @param auto joka poistetaan
     * @return montako autoa poistettiin
     */
    public int poista(Auto auto) {
        if (auto == null) return 0;
        int ret = autot.poista(auto.getIdNro());
        return ret;
    }
    
    /**
     * Palauttaa rekisterin autojen m‰‰r‰n
     * @return autojen m‰‰r‰
     */
    public int getAutoja() {
        return autot.getLkm();
    }
    
    /**
     * Lis‰‰ auton rekisteriin
     * @param auto lis‰tt‰v‰ auto
     * @throws SailoException jos lis‰yst‰ ei voida tehd‰
     */
    public void lisaa(Auto auto) throws SailoException {
        autot.lisaa(auto);
    }
    
    /**
     * Lis‰‰ mallin rekisteriin
     * @param malli lis‰tt‰v‰ malli
     * @throws SailoException jos lis‰yst‰ ei voida tehd‰
     */
    public void lisaa(Malli malli) throws SailoException {
        mallit.lisaa(malli);
    }
    
    /**
     * Lis‰‰ merkin rekisteriin
     * @param merkki lis‰tt‰v‰ merkki
     * @throws SailoException jos lis‰yst‰ ei voida tehd‰
     */
    public void lisaa(Merkki merkki) throws SailoException {
        merkit.lisaa(merkki);
    }
    
    /**
     * Palauttaa "taulukosta" hakuehtoon vastaavien autojen viitteet
     * @param hakuehto hakuehto
     * @param k etsitt‰v‰n kent‰n indeksi
     * @param rekisteri Rekisteri josta voidaan hakea
     * @return tietorakenteen lˆytyneist‰ autoista
     * @throws SailoException Jos jotakin menee v‰‰rin
     */
    public Collection<Auto> etsiAutoista(String hakuehto, int k, Rekisteri rekisteri) throws SailoException {
        return autot.etsi(hakuehto, k, rekisteri);
    }
    
    /**
     * Palauttaa "taulukosta" hakuehtoon vastaavien mallien viitteet
     * @param hakuehto hakuehto
     * @param k etsitt‰v‰n kent‰n indeksi
     * @return tietorakenteen lˆytyneist‰ merkeist‰
     * @throws SailoException Jos jotakin menee v‰‰rin
     */
    public Collection<Malli> etsiMalleista(String hakuehto, int k) throws SailoException {
        return mallit.etsi(hakuehto, k);
    }
    
    /**
     * Palauttaa "taulukosta" hakuehtoon vastaavien merkkien viitteet
     * @param hakuehto hakuehto
     * @param k etsitt‰v‰n kent‰n indeksi
     * @return tietorakenteen lˆytyneist‰ merkeist‰
     * @throws SailoException Jos jotakin menee v‰‰rin
     */
    public Collection<Merkki> etsiMerkeista(String hakuehto, int k) throws SailoException {
        return merkit.etsi(hakuehto, k);
    }
    
    /**
     * Haetaan auton malli
     * @param auto auto jolle mallia haetaan
     * @return viite haettuun malliin
     */
    public String annaMalli(Auto auto) {
        return mallit.annaMalli(auto.getIdNro());
    }
    
    /**
     * Haetaan auton merkki
     * @param malli malli jolle merkki‰ haetaan
     * @return viite haettuun merkkiin
     */
    public String annaMerkki(Malli malli) {
        return merkit.annaMerkki(malli.getMalliId());
    }
    
    /**
     * Asetetaan tiedostojen perusnimet
     * @param nimi uusi nimi
     */
    public void setTiedosto(String nimi) {
        File dir = new File(nimi);
        dir.mkdirs();
        String hakemistonNimi = "";
        if (!nimi.isEmpty()) hakemistonNimi = nimi + "/";
        autot.setTiedostonPerusNimi(hakemistonNimi + "autot");
        mallit.setTiedostonPerusNimi(hakemistonNimi + "mallit");
        merkit.setTiedostonPerusNimi(hakemistonNimi + "merkit");
    }
    
    /**
     * Lukee rekisterin tiedot tiedostosta
     * @param nimi jota k‰ytet‰‰n lukemisessa
     * @throws SailoException jos lukeminen ep‰onnistuu
     */
    public void lueTiedostosta(String nimi) throws SailoException {
        autot = new Autot(); // jos luetaan olemassa olevaa niin helpoin tyhjent‰‰ n‰in
        mallit = new Mallit();
        merkit = new Merkit();
        
        setTiedosto(nimi);
        autot.lueTiedostosta();
        mallit.lueTiedostosta();
        merkit.lueTiedostosta();
    }
    
    /**
     * Tallntaa rekisterin tiedot tiedostoon.
     * Vaikka autojen tallentaminen ep‰onnistuisi, niin yritet‰‰n silti tallentaa
     * merkit ja mallit ennen poikkeuksen heitt‰mist‰.
     * @throws SailoException jos tallentamisessa ongelmia
     */
    public void tallenna() throws SailoException {
        String virhe = "";
        try {
            autot.tallenna();
        } catch (SailoException ex) {
            virhe = ex.getMessage();
        }
        
        try {
            mallit.tallenna();
        } catch (SailoException ex) {
            virhe += ex.getMessage();
        }
        
        try {
            merkit.tallenna();
        } catch (SailoException ex) {
            virhe += ex.getMessage();
        }
        
        if (!"".equals(virhe)) throw new SailoException(virhe);
    }
    
    /**
     * Palauttaa i:n auton
     * @param i monesko auto palautetaan
     * @return viite i:teen autoon
     * @throws IndexOutOfBoundsException jos i v‰‰rin
     */
    public Auto annaAuto(int i) throws IndexOutOfBoundsException {
        return autot.anna(i);
    }
    
    /**
     * Palauttaa i:n mallin
     * @param i monesko malli palautetaan
     * @return viite i:teen malliin
     * @throws IndexOutOfBoundsException jos i v‰‰rin
     */
    public Malli annaMalli(int i) throws IndexOutOfBoundsException {
        return mallit.anna(i);
    }
    
    /**
     * Palauttaa annetun malli id:n perusteella halutun mallin nimen merkkijonona
     * @param i annettu malli id
     * @return mallin nimi merkkijonona
     */
    public String annaMallinNimi(int i) {
        return mallit.getMalli(i);
    }
    
    /**
     * Palauttaa i:n merkin
     * @param i monesko merkki palautetaan
     * @return viite i:teen merkkiin
     * @throws IndexOutOfBoundsException jos i v‰‰rin
     */
    public Merkki annaMerkki(int i) throws IndexOutOfBoundsException {
        return merkit.anna(i);
    }
    
    /**
     * Palauttaa annetun merkki id:n perusteella halutun merkin nimen merkkijonona
     * @param i annettu merkki id
     * @return merkin nimi merkkijonoa
     */
    public String annaMerkinNimi(int i) {
        return merkit.getMerkki(i);
    }

    /**
     * @param args ei k‰ytˆss‰
     * P‰‰ohjelman voi poistaa kun valmis
     */
    public static void main(String[] args) {
        Rekisteri rekisteri = new Rekisteri();
        Auto auto1 = new Auto();
        Auto auto2 = new Auto();
        
        auto1.rekisteroi();
        auto1.taytaAutonTiedot();
        auto2.rekisteroi();
        auto2.taytaAutonTiedot();
        
        try {
            rekisteri.lisaa(auto1);
            rekisteri.lisaa(auto2);
        } catch (SailoException e) {
            System.err.println("Virhe: " + e.getMessage());
        }

        
        System.out.println("====================== Rekisterin testi ======================");
        for (int i = 0; i < rekisteri.getAutoja(); i++) {
            Auto auto = rekisteri.annaAuto(i);
            System.out.println("Auto paikassa: " + i);
            auto.tulosta(System.out);
        }
    }

    /**
     * Korvaa auton tietorakenteesta. Katsoo samalla ett‰ onko kyseisell‰
     * tunnusnumerolla varustettua autoa olemassa. Jos ei ole niin, lis‰‰
     * sen uutena autona.
     * @param auto Viite autoon
     */
    public void korvaaTaiLisaa(Auto auto) {
        autot.korvaaTaiLisaa(auto);        
    }

    /**
     * Asettaa halutun merkkijonon annetun id:n omaavan mallin nimeksi
     * @param s annettu merkkijono
     * @param id annettu malli id
     */
    public void setMalli(String s, int id) {
        mallit.setMalli(s, id);
    }
    
    /**
     * Hakee annetun malli id:n perusteella halutun Malli-olion
     * @param id annettu id jolla etist‰‰n
     * @return mahdollisesti lˆytynyt Malli mallit-luokasta
     */
    public Malli getMalli(int id) {
        return mallit.anna(id);
    }
    
    /*
    public void korvaaTaiLisaa(Merkki merkki) {
        merkit.korvaaTaiLisaa(merkki);        
    }
    */

    /**
     * Asettaa annetun merkkijonon annetun merkki id:n omaavan merkin nimeksi
     * @param s annettu merkkijono
     * @param id annettu merkki id jolla etsit‰‰n
     */
    public void setMerkki(String s, int id) {
        merkit.setMerkki(s, id);
    }

    /**
     * Hakee annetun merkki id:n perusteella halutun Merkki-olion
     * @param id annettu id jolla etsit‰‰n
     * @return mahdollisesti lˆytynyt Merkki merkit-luokasta
     */
    public Merkki getMerkki(int id) {
        return merkit.anna(id);
    }

    /**
     * Tarkistaa mallin nimen perusteella, ett‰ onko olemassa mallia joka omaa
     * parametrina annetun nimen. Palauttaa joko lˆydetyn mallin malli id:n
     * tai vaihtoehtoisesti seuraavaksi rekisterˆit‰v‰n mallin id:n
     * @param mallinNimi annettu mallin nimi merkkijonona
     * @return lˆydetyn mallin id tai seuraavaksi rekisterˆit‰v‰n mallin id
     */
    public int onkoMalliOlemassa(String mallinNimi) {
        return mallit.onkoMalliOlemassa(mallinNimi);
    }

    /**
     * Tarkistaa merkin nimen perusteella, ett‰ onko olemassa merkki‰ joka omaa
     * parametrina annetun nimen. Palauttaa joko lˆydetyn merkin merkki id:n
     * tai vaihtoehtoisesti seuraavaksi rekisterˆit‰v‰n merkin id:n
     * @param merkinNimi annettu merkki nimi merkkijonona
     * @return lˆydetyn merkin id tai seuraavaksi rekisterˆit‰v‰n merkin id
     */
    public int onkoMerkkiOlemassa(String merkinNimi) {
        return merkit.onkoMerkkiOlemassa(merkinNimi);
    }

}
