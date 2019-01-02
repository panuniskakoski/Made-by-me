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
 * Rekisterin kooste merkeist� joka osaa mm. lis�t� uuden auton
 * @author Panu Niskakoski  panu.niskakoski@gmail.com
 * @version 6 Mar 2018
 *
 */
public class Merkit implements Iterable<Merkki>{
    
    private boolean muutettu = false;
    private String tiedostonPerusNimi = "merkit";
    
    // Taulukko merkeist�
    private final Collection<Merkki> alkiot = new ArrayList<Merkki>();

    /**
     * Merkkien alustaminen
     */
    public Merkit() {
        // T�m� riitt��
    }
    
    /**
     * Lisaa uuden merkin tietorakenteeseen. Ottaa merkin omistukseensa
     * @param merkki lis�tt�v�n merkin viite. Huom tietorakenne muuttuu omistajaksi
     */
    public void lisaa(Merkki merkki) {
        alkiot.add(merkki);
        muutettu = true;
    }
    
    /**
     * Tallentaa merkit tiedostoon.
     * @throws SailoException jos tallennus ep�onnistuu
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
            // throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");      //T�m� auki 7.vaiheessa
        } catch (IOException ex) {
            // throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }
        
        muutettu = false;
    }
    
    /**
     * Palauttaa kerhon merkkien lukum��r�n
     * @return merkkien lukum��r�
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
     * Palauttaa tiedoston nimen, jota k�ytet��n tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }
    
    /**
     * Palauttaa tiedoston nimen, jota k�ytet��n tallennukseen
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
     * @throws SailoException jos lukeminen ep�onnistuu
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
     * Luetaan aikaisemmin annetun nimisest� tiedostosta
     * @throws SailoException jos tulee poikkeus
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonPerusNimi());
    }
    
    /**
     * Iteraattori kaikkien merkkien l�pik�ymiseen
     * @return merkkiiteraattori
     */
    @Override
    public Iterator<Merkki> iterator() {
        return alkiot.iterator();
    }
    
    /**
     * Testiohjelma merkkirekisterille
     * @param args ei k�yt�ss�
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
     * @param k etsitt�v�n kent�n indeksi
     * @return tietorakenteen l�ytyneist� merkeist�
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
     * @param idNro mallin tunnusnumero jolle merkki� haetaan
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
     * @return joko l�ytynyt Merkki olio tai null jos sit� ei l�ydy
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
     * @return joko l�ytyneen merkin nimi tai null, jos sit� ei l�ydy
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
     * Etsit��n samalla ID numerolla oleva merkki. Jos ei l�ydy,
     * niin lis�t��n uutena merkkin�.
     * @param merkki lis�tt�v�n merkinn viite. Huome tietorakenne muuttuu omistajaksi.
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
     * Asettaa annetun merkki id:n perusteella l�ytyneen
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
     * Tarkistaa merkin nimen perusteella l�ytyyk� merkeist� jo lis�tt�v� merkki
     * ja palauttaa mahdollisesti t�m�n merkki id:n
     * @param merkinNimi annettu merkin nimi
     * @return l�ytyneen merkin merkki id tai null, jos merkki� ei l�ydy
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
