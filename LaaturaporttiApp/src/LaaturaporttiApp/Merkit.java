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
 * Rekisterin kooste merkeistä joka osaa mm. lisätä uuden auton
 * @author Panu Niskakoski  panu.niskakoski@gmail.com
 * @version 6 Mar 2018
 *
 */
public class Merkit implements Iterable<Merkki>{
    
    private boolean muutettu = false;
    private String tiedostonPerusNimi = "merkit";
    
    // Taulukko merkeistä
    private final Collection<Merkki> alkiot = new ArrayList<Merkki>();

    /**
     * Merkkien alustaminen
     */
    public Merkit() {
        // Tämä riittää
    }
    
    /**
     * Lisaa uuden merkin tietorakenteeseen. Ottaa merkin omistukseensa
     * @param merkki lisättävän merkin viite. Huom tietorakenne muuttuu omistajaksi
     */
    public void lisaa(Merkki merkki) {
        alkiot.add(merkki);
        muutettu = true;
    }
    
    /**
     * Tallentaa merkit tiedostoon.
     * @throws SailoException jos tallennus epäonnistuu
     */
    public void tallenna() throws SailoException {
        if (!muutettu) return;
        
        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete();
        ftied.renameTo(fbak);
        
        try (PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath()))) {
            for (Merkki merkki : this) {
                fo.println(merkki.toString());
            }
        } catch (FileNotFoundException ex) {
            // throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");      //Tämä auki 7.vaiheessa
        } catch (IOException ex) {
            // throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }
        
        muutettu = false;
    }
    
    /**
     * Palauttaa kerhon merkkien lukumäärän
     * @return merkkien lukumäärä
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
     * Lukee merkit tiedostosta.
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
                Merkki merkki = new Merkki();
                merkki.parse(rivi);
                lisaa(merkki);
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
     * Iteraattori kaikkien merkkien läpikäymiseen
     * @return merkkiiteraattori
     */
    @Override
    public Iterator<Merkki> iterator() {
        return alkiot.iterator();
    }
    
    /**
     * Testiohjelma merkkirekisterille
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Merkit merkit = new Merkit();
        
        Merkki merkki1 = new Merkki();
        merkki1.rekisteroi();
        Merkki merkki2 = new Merkki();
        merkki2.rekisteroi();
        
        merkit.lisaa(merkki1);
        merkit.lisaa(merkki2);
        
        System.out.println("=============== Autot testi ===============");
        
        for (Merkki merk : merkit) {
            System.out.print(merk.getMerkkiId() + " ");
            merk.tulosta(System.out);
        }
    }

    /**
     * Palauttaa "taulukosta" hakuehtoon vastaavien merkkien viitteet
     * @param hakuehto hakuehto
     * @param k etsittävän kentän indeksi
     * @return tietorakenteen löytyneistä merkeistä
     */
    @SuppressWarnings("unused")
    public Collection<Merkki> etsi(String hakuehto, int k) {
        Collection<Merkki> loytyneet = new ArrayList<Merkki>();
        for (Merkki merkki : this) {
            loytyneet.add(merkki);
        }
        return loytyneet;
    }

    /**
     * Haetaan merkki
     * @param idNro mallin tunnusnumero jolle merkkiä haetaan
     * @return auton merkki
     */
    public String annaMerkki(int idNro) {
        for (Merkki merkki : alkiot)
            if (merkki.getMerkkiId() == idNro) return merkki.getMerkki();
        return null;
    }
    
    /**
     * Haetaan Merkki olio annetun merkki id:n perusteella
     * @param i annettu merkki id
     * @return joko löytynyt Merkki olio tai null jos sitä ei löydy
     */
    public Merkki anna(int i) {
        if (i < 0 || getLkm() < i)
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        for (Merkki merkki : this) {
            if (merkki.getMerkkiId() == i) return merkki;
        }
        return null;
    }

    /**
     * Palauttaa merkin nimen annetun merkki id:n perusteella
     * @param i annettu merkki id
     * @return joko löytyneen merkin nimi tai null, jos sitä ei löydy
     */
    public String getMerkki(int i) {
        if (i < 0 || getLkm() < i)
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        for (Merkki merkki : this) {
            if (merkki.getMerkkiId() == i) return merkki.getMerkki();
        }
        return null;
    }

    /**
     * Korvaa merkin tietorakenteessa. Ottaa merkin omistukseensa.
     * Etsitään samalla ID numerolla oleva merkki. Jos ei löydy,
     * niin lisätään uutena merkkinä.
     * @param merkki lisättävän merkinn viite. Huome tietorakenne muuttuu omistajaksi.
     */
    public void korvaaTaiLisaa(Merkki merkki) {
        int id = merkki.getMerkkiId();
        for (Merkki merkit : this) {
            if (merkit.getMerkkiId() == id) {
                merkit = merkki;
                muutettu = true;
                return;
            }
        }
    }

    /**
     * Asettaa annetun merkki id:n perusteella löytyneen
     * merkin nimeksi annetun merkkijonon
     * @param s annettu merkkijono
     * @param id annettu merkki id
     */
    public void setMerkki(String s, int id) {
        for (Merkki merkki : this) {
            if (merkki.getMerkkiId() == id) {
                merkki.setMerkki(s);
                break;
            }
        }
    }

    /**
     * Tarkistaa merkin nimen perusteella löytyykö merkeistä jo lisättävä merkki
     * ja palauttaa mahdollisesti tämän merkki id:n
     * @param merkinNimi annettu merkin nimi
     * @return löytyneen merkin merkki id tai null, jos merkkiä ei löydy
     */
    public int onkoMerkkiOlemassa(String merkinNimi) {
        int nro = 0;
        for (Merkki merkki : this) {
            if (merkinNimi.equals(merkki.getMerkki())) {
                return nro = merkki.getMerkkiId();
            }
        }
        nro = Merkki.getSeuraavaMerkkiNro();
        return nro;
    }
}
