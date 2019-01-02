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
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import fi.jyu.mit.ohj2.WildChars;

/**
 * Rekisterin kooste autoista joka osaa mm. lis�t� uuden auton
 * @author Panu Niskakoski  panu.niskakoski@gmail.com
 * @version 4 Mar 2018
 *
 */
public class Autot implements Iterable<Auto> {
    
    private static int       MAX_AUTOJA         = Integer.MAX_VALUE;
    private boolean          muutettu           = false;
    private int              lkm                = 0;
    private String           kokoNimi           = "rekisteri";
    private String           tiedostonPerusNimi = "autot";
    private Auto[]           alkiot             = new Auto[5];

    /**
     * Oletusmuodostaja
     */
    public Autot() {
        // T�m� riitt��
    }
    
    /**
     * Lisaa uuden auton tietorakenteeseen. Ottaa auton omistukseensa
     * @param auto Lis�tt�v� auto
     * @throws SailoException jos jokin menee pieleen
     */
    public void lisaa(Auto auto) throws SailoException {
        if (lkm >= MAX_AUTOJA) throw new SailoException("Liikaa alkioita");
        if (lkm >= alkiot.length) alkiot = Arrays.copyOf(alkiot, lkm+5);
        alkiot[lkm] = auto;
        lkm++;
        muutettu = true;
    }
    
    /**
     * Palauttaa viitteen i:teen autoon
     * @param i monennenko auton viite halutaan
     * @return viite autoon, jonka indeksi on i
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella
     */
    public Auto anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || lkm <= i)
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot[i];
    }
    
    /**
     * Lukee autot-rekisterin tiedostosta.
     * @param tied tiedoston perunimi
     * @throws SailoException jos lukeminen ep�onnistuu
     */
    public void lueTiedostosta(String tied) throws SailoException {
        setTiedostonPerusNimi(tied);
        try (BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi()))) {
            String rivi = fi.readLine();
            
            while ((rivi = fi.readLine()) != null) {
                rivi = rivi.trim();
                if ("".equals(rivi) || rivi.charAt(0) == ';') continue;
                Auto auto = new Auto();
                auto.parse(rivi); // voisi olla virhek�sittely
                lisaa(auto);
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
     * Tallentaa autot-rekisterin tiedostoon.
     * @throws SailoException jos tallennus ep�onnistuu
     */
    public void tallenna() throws SailoException {
        if (!muutettu) return;
        
        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete(); // if...System.err.println("Ei voi tuhota");
        ftied.renameTo(fbak); //if...System.err.println("Ei voi nimet�");
        
        try (PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath()))) {
            for (Auto auto : this) {
                fo.println(auto.toString());
            }
            //} catch (IOException e) { // ei heit� poikkeusta
            // throw new SailoException("Tallentamisessa ongelmia: " + e.getMessage());
        } catch (FileNotFoundException ex) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch (IOException ex) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }
        
        muutettu = false;
    }
    
    /**
     * Palauttaa rekisterin koko nimen
     * @return Rekisterin koko nimi merkkijonona
     */
    public String getKokoNimi() {
        return kokoNimi;
    }
    
    /**
     * Palauttaa rekisterin autojen lukum��r�n
     * @return autojen lukum��r�
     */
    public int getLkm() {
        return lkm;
    }
    
    /**
     * Palauttaa tiedoston nimen, jota k�ytet��n tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }
    
    /**
     * Asettaa tiedoston perusnimen ilman tarkenninta
     * @param nimi tallennustiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String nimi) {
        tiedostonPerusNimi = nimi;
    }
    
    /**
     * Palauttaa tiedoton nimen, jota k�ytet��n tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonNimi() {
        return getTiedostonPerusNimi() + ".dat";
    }
    
    /**
     * Palauttaa varakopiotiedoston nimen
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }
    
    /**
     * Luokka j�senten iteroimiseksi.
     */
    public class AutotIterator implements Iterator<Auto> {
        private int kohdalla = 0;
        
        /**
         * Onko olemassa viel� seuraavaa autoa
         * @see java.util.Iterator#hasNext()
         * @return true jos on viel� autoja
         */
        @Override
        public boolean hasNext() {
            return kohdalla < getLkm();
        }
        
        /**
         * Annetaan seuraava auto
         * @return seuraava auto
         * @throws NoSuchElementException jos seuraavaa alkiota ei en�� ole
         * @see java.util.Iterator#next()
         */
        @Override
        public Auto next() throws NoSuchElementException {
            if (!hasNext()) throw new NoSuchElementException("Ei ole");
            return anna(kohdalla++);
        }
        
        /**
         * Tuhoamista ei ole toteutettu
         * @throws UnsupportedOperationException aina
         * @see java.util.Iterator#remove()
         */
        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Ei poisteta");
        }
    }

    /**
     * Palautetaan iteraattori autoistaan.
     * @return auto iteraattori
     */
    @Override
    public Iterator<Auto> iterator() {
        return new AutotIterator();
    }
    
    /**
     * Palauttaa "taulukosta" hakuehtoon vastaavien autojen viitteet
     * @param hakuehto hakuehto
     * @param k etsitt�v�n kent�n indeksi
     * @param rekisteri Rekisteri josta voidaan hakea
     * @return tietorakenteen l�ytyneist� autoista
     */
    public Collection<Auto> etsi(String hakuehto, int k, Rekisteri rekisteri) {
        String ehto = "*";
        if (hakuehto != null && hakuehto.length() > 0) ehto = hakuehto;
        int hk = k;
        if (hk < 0) hk = 1;
        Collection<Auto> loytyneet = new ArrayList<Auto>();
        for (Auto auto : this) {
            if (WildChars.onkoSamat(auto.anna(hk, rekisteri), ehto)) loytyneet.add(auto);
        }
        return loytyneet;
    }
    
    /**
     * Testiohjelma autorekisterille
     * @param args ei k�yt�ss�
     */
    public static void main(String[] args) {
        Autot autot = new Autot();
        
        Auto auto1 = new Auto();
        Auto auto2 = new Auto();
        auto1.rekisteroi();
        auto1.taytaAutonTiedot();
        auto2.rekisteroi();
        auto2.taytaAutonTiedot();
        
        try {
            autot.lisaa(auto1);
            autot.lisaa(auto2);                        
            
            System.out.println("=========== Autot testi ===========");
            
            for (int i = 0; i < autot.getLkm(); i++) {
                Auto auto = autot.anna(i);
                System.out.println("Auton nro: " + i);
                auto.tulosta(System.out);
            }
        } catch (SailoException ex ) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Korvaa auton tietorakenteessa. Ottaa auton omistukseensa.
     * Etsit��n samalla ID numerolla oleva auto. Jos ei l�ydy,
     * niin lis�t��n uutena autona.
     * @param auto lis�tt�v�n auton viite. Huome tietorakenne muuttuu omistajaksi.
     */
    public void korvaaTaiLisaa(Auto auto) {
        int id = auto.getIdNro();
        for (int i = 0; i < lkm; i++) {
            if (alkiot[i].getIdNro() == id) {
                alkiot[i] = auto;
                muutettu = true;
                return;
            }
        }
        
    }

    /**
     * Poistaa auton jolla on valittuu tunnusnumero
     * @param idNro poistettavan auton ID numero
     * @return 1 jos poistettiin, 0 jos ei l�ydy
     */
    public int poista(int idNro) {
        int ind = etsiId(idNro);
        if (ind < 0) return 0;
        lkm--;
        for (int i = ind; i < lkm; i++) {
            alkiot[i] = alkiot[i + 1];
        }
        alkiot[lkm] = null;
        muutettu = true;
        return 1;
    }

    /**
     * Etsii auton id:n perusteella
     * @param idNro ID numero, jonka mukaan etsit��n
     * @return l�tyneen auton indeksi tai -1 jos ei l�ydy
     */
    public int etsiId(int idNro) {
        for (int i = 0; i < lkm; i++) {
            if (idNro == alkiot[i].getIdNro()) return i;
        }
        return -1;
    }

}
