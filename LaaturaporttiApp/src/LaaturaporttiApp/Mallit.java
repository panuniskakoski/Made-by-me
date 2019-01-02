/**
 * 
 */
package LaaturaporttiApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Rekisterin kooste malleista joka osaa mm. lisätä uuden auton
 * @author Panu Niskakoski  panu.niskakoski@gmail.com
 * @version 6 Mar 2018
 *
 */
public class Mallit implements Iterable<Malli> {
    
    private boolean muutettu = false;
    private String tiedostonPerusNimi = "mallit";
    
    /** Taulukko malleista */
    private final Collection<Malli> alkiot = new ArrayList<Malli>();

    /**
     * Oletusmuodostaja
     */
    public Mallit() {
        // Attribuuttien oma alustus riittää
    }
    
    /**
     * Lisää uuden mallin tietorakenteeseen. Ottaa mallin omistukseensa.
     * @param malli lisättävä malli. Huom tietorakenne muuttuu omistajaksi.
     */
    public void lisaa(Malli malli) {
        alkiot.add(malli);
        muutettu = true;
    }
    
    /**
     * Lukee mallit tiedostosta.
     * @param tied tiedoston nimen alkuosa
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta(String tied) throws SailoException {
        setTiedostonPerusNimi(tied);
        try (BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi()))) {
            
            String rivi;
            while ((rivi = fi.readLine()) != null) {
                rivi = rivi.trim();
                if ("".equals(rivi) || rivi.charAt(0) == ';') continue;
                Malli malli = new Malli();
                malli.parse(rivi);
                lisaa(malli);
            }
            muutettu = false;
            
        } catch (FileNotFoundException e) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea"); 
        } catch (IOException e) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
    }
    
    /**
     * Luetaan aikaisemmin annetun nimisestä tiedostosta
     * @throws SailoException jos tulee poikkeus
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonPerusNimi());
    }
    
    /**
     * Tallentaa mallit tiedostoon.
     * @throws SailoException jos tallennus epäonnistuu
     */
    public void tallenna() throws SailoException {
        if (!muutettu) return;
        
        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete();
        ftied.renameTo(fbak);
        
        try (PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath()))) {
            for (Malli malli : this) {
                fo.println(malli.toString());
            }
        } catch (FileNotFoundException ex) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch (IOException ex) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }
        
        muutettu = false;
    }
    
    /**
     * Palauttaa rekisterin mallien lukumäärän
     * @return mallien lukumäärä
     */
    public int getLkm() {
        return alkiot.size();
    }
    
    /**
     * Asettaa tiedoston perusnimen ilman tarkenninta
     * @param tied tallennustiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String tied) {
        tiedostonPerusNimi = tied;
    }
    
    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }
    
    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonNimi() {
        return tiedostonPerusNimi + ".dat";
    }
    
    /**
     * Palauttaa varakopiotiedoston nimen
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }
    
    /**
     * Iteraattori kaikkien mallien läpikäymiseen
     * @return malliiteraattori
     */
    @Override
    public Iterator<Malli> iterator() {
        return alkiot.iterator();
    }
    
    /**
     * Laitetaan muutos, jolloin pakotetaan tallentamaan.
     */
    public void setMuutos() {
        muutettu = true;
    }
    
    /**
     * Testiohjelma mallirekisterille
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Mallit mallit = new Mallit();

        Malli malli1 = new Malli();
        Malli malli2 = new Malli();
        
        mallit.lisaa(malli1);
        mallit.lisaa(malli2);
        
        System.out.println("=========== Mallit testi ===========");
        
        for (Malli malli : mallit) {
            System.out.println(malli.getMalliId() + " ");
            malli.tulosta(System.out);
        }  
    }

    /**
     * Palauttaa "taulukosta" hakuehtoon vastaavien mallien viitteet
     * @param hakuehto hakuehto
     * @param k etsittävän kentän indeksi
     * @return tietorakenteen löytyneistä malleista
     */
    @SuppressWarnings("unused")
    public Collection<Malli> etsi(String hakuehto, int k) {
        Collection<Malli> loytyneet = new ArrayList<Malli>();
        for (Malli malli : this) {
            loytyneet.add(malli);
        }
        return loytyneet;
    }

    /**
     * Haetaan mallin nimi
     * @param idNro auton tunnusnumero jolle mallia haetaan
     * @return auton mallin nimi tai mikäli sitä ei löydy niin null
     */
    public String annaMalli(int idNro) {
        for (Malli malli : alkiot)
            if (malli.getMalliId() == idNro) return malli.getMalli();
        return null;
    }

    /**
     * Hakee Malli olion
     * @param i ID jolla mallia haetaan
     * @return malli olio tai mikäli sitä ei löydy niin null
     */
    public Malli anna(int i) {
        if (i < 0 || getLkm() < i)
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        for (Malli malli : this) {
            if (malli.getMalliId() == i) return malli;
        }
        return null;
    }

    /**
     * Haetaan mallin nimi
     * @param i auton tunnusnumero jolle mallia haetaan
     * @return auton mallin nimi tai mikäli sitä ei löydy niin null
     */
    public String getMalli(int i) {
        if (i < 0 || getLkm() < i)
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        for (Malli malli : this) {
            if (malli.getMalliId() == i) return malli.getMalli();
        }
        return null;
    }

    /**
     * Korvaa mallin tietorakenteessa. Ottaa mallin omistukseensa.
     * Etsitään samalla ID numerolla oleva malli. Jos ei löydy,
     * niin lisätään uutena mallina.
     * @param malli lisättävän mallin viite. Huome tietorakenne muuttuu omistajaksi.
     */
    public void korvaaTaiLisaa(Malli malli) {
        int id = malli.getMalliId();
        for (Malli mallit : this) {
            if (mallit.getMalliId() == id) {
                mallit = malli;
                muutettu = true;
                return;
            }
        }
    }

    /**
     * Käy läpi malleja ja mikäli annettu id vastaa auton mallia
     * siirrytään malli-luokkaan asettamaan annettu merkkijono mallin nimeksi
     * @param s annettu merkkijono
     * @param id annettu malli ID
     */
    public void setMalli(String s, int id) {
        for (Malli malli : this) {
            if (malli.getMalliId() == id) {
                malli.setMalli(s);
                break;
            }
        }
    }

    /**
     * Tarkistaa malleista mallin nimen perusteella, että onko tällaista
     * mallia jo olemassa
     * @param mallinNimi mallin nimi
     * @return valmiin mallin id numeron tai mikäli valmista ei ole, niin 
     * palauttaa uudelle mallille annettavan malli id:n
     */
    public int onkoMalliOlemassa(String mallinNimi) {
        int nro = 0;
        for (Malli malli : this) {
            if (mallinNimi.equals(malli.getMalli())) {
                return nro = malli.getMalliId();
            }
        }
        nro = Malli.getSeuraavaMalliNro();
        return nro;
    }
}
