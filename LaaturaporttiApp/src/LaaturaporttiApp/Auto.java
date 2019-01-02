/**
 * 
 */
package LaaturaporttiApp;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Rekisterin auto joka osaa mm. itse huolehtia id:stään.
 * @author Panu Niskakoski  panu.niskakoski@gmail.com
 * @version 4 Mar 2018
 * 
 */
public class Auto implements Cloneable {
    
    private int     idNro;
    private String  asiakas             = "";
    private String  puhelin             = "";
    private String  sposti              = "";
    private String  rekisteriNro        = "";
    private int     malliId             = 1;    
    private int     vuosimalli          = 0;
    private String  lisaysPvm           = "";
    
    private int     autonJarrut         = 0;
    private int     tuulilasiJaIkkunat  = 0;
    private int     renkaidenKumit      = 0;
    private int     vanteet             = 0;
    private int     rengaskulmat        = 0;
    private int     maalipinta          = 0;
    private int     korroosio           = 0;
    private int     tekniikka           = 0;
    private int     sisatilat           = 0;
    private int     turvallisuus        = 0;
    private int     lisavarusteet       = 0;
    private int     huoltohistoria      = 0;
    private int     autoDna             = 0;
    
    private String  kuva                = "";
    private String  yhteenveto          = "";
    
    private static int seuraavaNro      = 1;
    private static int seuraavaMalliNro = 1;
    
    /**
     * Alustetaan auto
     */
    public Auto() {
        //Tämä riittää
    }
    
    /**
     * Arvotaan satunnainen kokonaisluku välille [ala,yla]
     * @param ala arvonnan alaraja
     * @param yla arvonnan ylraja
     * @return satunnainen luku väliltä [ala,ylä]
     */
    public static int rand(int ala, int yla) {
        double n = (yla-ala)*Math.random() + ala;
        return (int)Math.round(n);
    }
    
    /**
     * Apumetodi, jolla saadaan täytettyä testiarvot autoille.
     */
    public void taytaAutonTiedot() {
        asiakas = "Laitela Ismo" + " " + rand(1000, 9999);
        puhelin = "0509115526";
        sposti = "ismo.laitela@gmail.com";
        rekisteriNro = "FRT-555";
        malliId = 1;      
        vuosimalli = 1994;
        lisaysPvm = "23.03.2017";
        
        autonJarrut = 100;
        tuulilasiJaIkkunat = 100;
        renkaidenKumit = 80;
        vanteet = 100;
        rengaskulmat = 100;
        maalipinta = 90;
        korroosio = 80;
        tekniikka = 100;
        sisatilat = 90;
        turvallisuus = 100;
        lisavarusteet = 80;
        huoltohistoria = 50;
        autoDna = 100;
        
        kuva = "kuvat\\autot\\frt555.png";
        yhteenveto = "Huoltohistoriaa huomioimatta, auto hyvässä kunnossa.";       
    }
    
    /**
     * Tulostetaan auton tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(String.format("%03d", idNro) + " " + asiakas + " "
                + puhelin + " \n" + sposti + " " + rekisteriNro + " \n"
                + malliId + " \n"
                + " \n" + vuosimalli + " " + lisaysPvm);
        out.println("Auton jarrut: " + autonJarrut + "\nTuulilasi ja ikkunat: " 
                + tuulilasiJaIkkunat + "\nRenkaat (kumit): " + renkaidenKumit
                + "\nVanteet: " + vanteet + "\nRengaskulmat: " + rengaskulmat
                + "\nMaalipinta: " + maalipinta + "\nKorroosio: " + korroosio
                + "\nTekniikka: " + tekniikka + "\nSisätilat: " + sisatilat
                + "\nTurvallisuus: " + turvallisuus + "\nLisävarusteet: "
                + lisavarusteet + "\nHuoltohistoria: " + huoltohistoria
                + "\nAutoDNA: " + autoDna);
        out.println("Polku kuvaan: " + kuva);
        out.println("Yhteenveto: " + yhteenveto);
    }

    /**
     * Tulostetaan auton tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    /**
     * Antaa autolle seuraavan id-numeron
     * @return auton uusi ID numero
     * @example
     * <pre name="test">
     *      Auto auto1 = new Auto();
     *      auto1.getIdNro() === 0;
     *      auto1.rekisteroi();
     *      Auto auto2 = new Auto();
     *      auto2.rekisteroi();
     *      int n1 = auto1.getIdNro();
     *      int n2 = auto2.getIdNro();
     *      n1 === n2 - 1;
     * </pre>
     */
    public int rekisteroi() {
        idNro = seuraavaNro;
        seuraavaNro++;
        malliId = seuraavaMalliNro;
        seuraavaMalliNro += 1;
        return idNro;
    }
    
    /**
     * Antaa autolle seuraavan id-numeron
     * Lisäksi asettaa valmiin mallin ID:n autolle
     * @param valmisMalliId olemassa oleva malli ID
     */
    public void rekisteroi(int valmisMalliId) {
        idNro = seuraavaNro;
        seuraavaNro++;
        malliId = valmisMalliId;        
    }
    
    /**
     * Palauttaa auton id-numeron
     * @return auton id-numero
     */
    public int getIdNro() {
        return idNro;
    }
    
    /**
     * Asettaa id-numeron ja samalla varmistaa että
     * seuraava numero on aina suurempi kuin tähän mennessä suurin.
     * @param nro asetettava tunnusnumero
     */
    private void setIdNro(int nro) {
        idNro = nro;
        if (idNro >= seuraavaNro) seuraavaNro = idNro + 1;
    }

    /**
     * Palauttaa auton tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return auto tolppaeroteltuna merkkijonona
     * @example
     * <pre name="test">
     *      Auto auto = new Auto();
     *      auto.parse("1|        Laitela Ismo|    455006 |ismo.laitela@gmail.com|FRT-555 | 1|2005|24.06.2015|100|100|60|80 |100|100|100|100|100|80|70|40|100 |fiat500.png|Auto kaikenkaikkiaan hyvässä kunnossa.");
     *      auto.toString().startsWith("\n1|Laitela Ismo|455006|ismo.laitela@gmail.com|FRT-555|") === true;
     * </pre>
     */
    @Override
    public String toString() {
        return "\n" +
                getIdNro() + "|" +
                asiakas + "|" +
                puhelin + "|" +
                sposti + "|" +
                rekisteriNro + "|" +
                malliId + "|" +
                vuosimalli + "|" +
                lisaysPvm + "|" +
                autonJarrut + "|" +
                tuulilasiJaIkkunat + "|" +
                renkaidenKumit + "|" +
                vanteet + "|" +
                rengaskulmat + "|" +
                maalipinta + "|" +
                korroosio + "|" +
                tekniikka + "|" +
                sisatilat + "|" +
                turvallisuus + "|" +
                lisavarusteet + "|" +
                huoltohistoria + "|" +
                autoDna + "|" +
                kuva + "|" +
                yhteenveto;
    }

    /**
     * Selvittää auton tiedot | erotellusta merkkijonosta
     * Pitää huolen että seuraavaNro on suurempi kuin tuleva idNro.
     * @param rivi josta auton tiedot otetaan
     * @example
     * <pre name="test">
     *      Auto auto = new Auto();
     *      auto.parse("1|        Laitela Ismo|    455006 |ismo.laitela@gmail.com|FRT-555 | 1|2005|24.06.2015|100|100|60|80 |100|100|100|100|100|80|70|40|100 |fiat500.png|Auto kaikenkaikkiaan hyvässä kunnossa.");
     *      auto.getIdNro() === 1;
     *      auto.toString().startsWith("\n1|Laitela Ismo|455006|") === true;
     *      
     *      auto.rekisteroi();
     *      int n = auto.getIdNro();
     *      auto.parse(""+(n+20));
     *      auto.rekisteroi();
     *      auto.getIdNro() === n+20+1;
     * </pre>
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        setIdNro(Mjonot.erota(sb, '|', getIdNro()));
        asiakas = Mjonot.erota(sb, '|', asiakas);
        puhelin = Mjonot.erota(sb, '|', puhelin);
        sposti = Mjonot.erota(sb, '|', sposti);
        rekisteriNro = Mjonot.erota(sb, '|', rekisteriNro);
        malliId = Mjonot.erota(sb, '|', malliId);
        vuosimalli = Mjonot.erota(sb, '|', vuosimalli);
        lisaysPvm = Mjonot.erota(sb, '|', lisaysPvm);
        
        autonJarrut = Mjonot.erota(sb, '|', autonJarrut);
        tuulilasiJaIkkunat = Mjonot.erota(sb, '|', tuulilasiJaIkkunat);
        renkaidenKumit = Mjonot.erota(sb, '|', renkaidenKumit);
        vanteet = Mjonot.erota(sb, '|', vanteet);
        rengaskulmat = Mjonot.erota(sb, '|', rengaskulmat);
        maalipinta = Mjonot.erota(sb, '|', maalipinta);
        korroosio = Mjonot.erota(sb, '|', korroosio);
        tekniikka = Mjonot.erota(sb, '|', tekniikka);
        sisatilat = Mjonot.erota(sb, '|', sisatilat);
        turvallisuus = Mjonot.erota(sb, '|', turvallisuus);
        lisavarusteet = Mjonot.erota(sb, '|', lisavarusteet);
        huoltohistoria = Mjonot.erota(sb, '|', huoltohistoria);
        autoDna = Mjonot.erota(sb, '|', autoDna);
        
        kuva = Mjonot.erota(sb, '|', kuva);
        yhteenveto = Mjonot.erota(sb, '|', yhteenveto); 
    }
    
    /**
     * Tehdään identtinen klooni autosta
     * @return Object kloonattu auto
     */
    @Override
    public Auto clone() throws CloneNotSupportedException {
        Auto uusi;
        uusi = (Auto) super.clone();
        return uusi;
    }
    
    @Override
    public boolean equals(Object auto) {
        if (auto == null) return false;
        return this.toString().equals(auto.toString());
    }
    
    @Override
    public int hashCode() {
        return idNro;
    }
    
    /**
     * Testipääohjelma kokeiluja varten
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Auto auto1 = new Auto();
        Auto auto2 = new Auto();
        auto1.rekisteroi();
        auto2.rekisteroi();
        
        auto1.tulosta(System.out);
        auto2.tulosta(System.out);
        
        auto1.taytaAutonTiedot();
        auto2.taytaAutonTiedot();
        
        auto1.tulosta(System.out);
        auto2.tulosta(System.out);
    }

    /**
     * Hakee auton asiakkaan nimen merkkijonana
     * @return Asiakkaan nimi
     */
    public String getAsiakas() {
        return asiakas;
    }
    
    /**
     * Hakee auton puhelinnumeron merkkijonona
     * @return Puhelinnumero
     */
    public String getPuhelin() {
        return puhelin;
    }

    /**
     * Hakee auton sähköpostin merkkijonona
     * @return Sähköposti
     */
    public String getSposti() {
        return sposti;
    }

    /**
     * Hakee auton rekisterinumeron merkkijonona
     * @return rekisterinumero
     */
    public String getRekisteriNro() {
        return rekisteriNro;
    }

    /**
     * Hakee auton vuosimallin ja muuttaa sen merkkijonoksi
     * @return vuosimalli
     */
    public String getVuosimalli() {
        return Integer.toString(vuosimalli);
    }

    /**
     * Hakee päiväyksen millon auto on lisätty merkkijonona
     * @return lisäyspäivämäärä
     */
    public String getLisatty() {
        return lisaysPvm;
    }

    /**
     * Hakee auton yhteenvedon merkkijonona
     * @return yhteenveto
     */
    public String getYhteenveto() {
        return yhteenveto;
    }

    /**
     * Hakee auton jarrujen kuntoprosentin ja muuttaa sen merkkijonoksi
     * @return jarrujen kunto
     */
    public String getAutonJarrut() {
        return Integer.toString(autonJarrut);
    }
    
    /**
     * Hakee auton tuulilasien ja ikkunoiden kuntoprosentin ja muuttaa sen merkkijonoksi
     * @return tuulilasin ja ikkunoiden kunto
     */
    public String getTuulilasiJaIkkunat() {
        return Integer.toString(tuulilasiJaIkkunat);
    }

    /**
     * Hakee auton renkaiden kuntoprosentin ja muuttaa sen merkkijonoksi
     * @return renkaiden kunto
     */
    public String getRenkaat() {
        return Integer.toString(renkaidenKumit);
    }

    /**
     * Hakee auton vanteiden kuntoprosentin ja muuttaa sen merkkijonoksi
     * @return vanteiden kunto
     */
    public String getVanteet() {
        return Integer.toString(vanteet);
    }

    /**
     * Hakee rengaskulmien kuntoprosentin ja muuttaa sen merkkijonoksi
     * @return rengaskulmien kunto
     */
    public String getRengaskulmat() {
        return Integer.toString(rengaskulmat);
    }

    /**
     * Hakee auton maalipinnan kuntoprosentin ja muuttaa sen merkkijonoksi
     * @return maalipinnan kunto
     */
    public String getMaalipinta() {
        return Integer.toString(maalipinta);
    }

    /**
     * Hakee auton korroosioon liittyvän kuntoprosentin ja muuttaa sen merkkijonoksi
     * @return korroosion kuntoprosentti
     */
    public String getKorroosio() {
        return Integer.toString(korroosio);
    }

    /**
     * Hakee auton tekniikan kuntoprosentin ja muuttaa sen merkkijonoksi
     * @return tekniikan kunto
     */
    public String getTekniikka() {
        return Integer.toString(tekniikka);
    }

    /**
     * Hakee auton sisätilojen kuntoprosentin ja muuttaa sen merkkijonoksi
     * @return sisätilojen kunto
     */
    public String getSisatilat() {
        return Integer.toString(sisatilat);
    }

    /**
     * Hakee auton turvallisuuteen liittyvän kuntoprosentin ja muuttaa sen merkkijonoksi
     * @return turvallisuuden kuntoluokitus
     */
    public String getTurvallisuus() {
        return Integer.toString(turvallisuus);
    }

    /**
     * Hakee auton lisävarusteiden kuntoprosentin ja muuttaa sen merkkijonoksi
     * @return lisävarusteiden kunto
     */
    public String getLisavarusteet() {
        return Integer.toString(lisavarusteet);
    }

    /**
     * Hakee auton huoltohistorian kuntoprosentin ja muuttaa sen merkkijonoksi
     * @return huoltohistorian kunto
     */
    public String getHuoltohistoria() {
        return Integer.toString(huoltohistoria);
    }

    /**
     * Hakee auto DNA:n kuntoprosentin ja muuttaa sen merkkijonoksi
     * @return auto DNA kunto
     */
    public String getAutoDna() {
        return Integer.toString(autoDna);
    }

    /**
     * Asettaa auton asiakkaaseen tietyn merkkijonon
     * @param s haluttu merkkijono
     * @return null
     */
    public String setAsiakas(String s) {
        asiakas = s;
        return null;
    }
    
    /**
     * Asettaa auton puhelinnumeroon tietyn merkkijonon
     * @param s haluttu merkkijono
     * @return null
     */
    public String setPuhelin(String s) {
        puhelin = s;
        return null;
    }
    
    /**
     * Asettaa auton sähköpostiin tietyn merkkijonon
     * @param s haluttu merkkijono
     * @return null
     */
    public String setSposti(String s) {
        sposti = s;
        return null;
    }
    
    /**
     * Asettaa auton rekisterinumeroon tietyn merkkijonon
     * @param s haluttu merkkijono
     * @return null
     */
    public String setRekisteriNro(String s) {
        rekisteriNro = s;
        return null;
    }
    
    /**
     * Asettaa auton vuosimalliin tietyn merkkijonon
     * @param s haluttu vuosi
     * @return null
     */
    public String setVuosimalli(String s) {
        vuosimalli = Integer.parseInt(s);
        return null;
    }
    
    /**
     * Asettaa auton lisäyspäivämäärään tietyn merkkijonon
     * @param s haluttu merkkijono
     * @return null
     */
    public String setLisatty(String s) {
        lisaysPvm = s;
        return null;
    }
    
    /**
     * Asettaa auton jarruihin tietyn merkkijonon joka koostuu kokonaisluvuista
     * @param s haluttu luku
     * @return null
     */
    public String setAutonJarrut(String s) {
        autonJarrut = Integer.parseInt(s);
        return null;
    }
    
    /**
     * Asettaa auton tuulilasin ja ikkunoiden kuntoon tietyn merkkijonon joka koostuu kokonaisluvuista
     * @param s haluttu luku
     * @return null
     */
    public String setTuulilasiJaIkkunat(String s) {
        tuulilasiJaIkkunat = Integer.parseInt(s);
        return null;
    }
    
    /**
     * Asettaa auton renkaiden kuntoon tietyn merkkijonon joka koostuu kokonaisluvuista
     * @param s haluttu luku
     * @return null
     */
    public String setRenkaat(String s) {
        renkaidenKumit = Integer.parseInt(s);
        return null;
    }
    
    /**
     * Asettaa auton vanteiden kuntoon tietyn merkkijonon joka koostuu kokonaisluvuista
     * @param s haluttu luku
     * @return null
     */
    public String setVanteet(String s) {
        vanteet = Integer.parseInt(s);
        return null;
    }
    
    /**
     * Asettaa auton rengaskulmien kuntoon tietyn merkkijonon joka koostuu kokonaisluvuista
     * @param s haluttu luku
     * @return null
     */
    public String setRengaskulmat(String s) {
        rengaskulmat = Integer.parseInt(s);
        return null;
    }
    
    /**
     * Asettaa auton maalipinnan kuntoon tietyn merkkijonon joka koostuu kokonaisluvuista
     * @param s haluttu luku
     * @return null
     */
    public String setMaalipinta(String s) {
        maalipinta = Integer.parseInt(s);
        return null;
    }
    
    /**
     * Asettaa auton korroosion kuntoon tietyn merkkijonon joka koostuu kokonaisluvuista
     * @param s haluttu luku
     * @return null
     */
    public String setKorroosio(String s) {
        korroosio = Integer.parseInt(s);
        return null;
    }
    
    /**
     * Asettaa auton tekniikan kuntoon tietyn merkkijonon joka koostuu kokonaisluvuista
     * @param s haluttu luku
     * @return null
     */
    public String setTekniikka(String s) {
        tekniikka = Integer.parseInt(s);
        return null;
    }
    
    /**
     * Asettaa sisätilojen kuntoon tietyn merkkijonon joka koostuu kokonaisluvuista
     * @param s haluttu luku
     * @return null
     */
    public String setSisatilat(String s) {
        sisatilat = Integer.parseInt(s);
        return null;
    }
    
    /**
     * Asettaa turvallisuuden kuntoon tietyn merkkijonon joka koostuu kokonaisluvuista
     * @param s haluttu luku
     * @return null
     */
    public String setTurvallisuus(String s) {
        turvallisuus = Integer.parseInt(s);
        return null;
    }
    
    /**
     * Asettaa lisävarusteiden kuntoon tietyn merkkijonon joka koostuu kokonaisluvuista
     * @param s haluttu luku
     * @return null
     */
    public String setLisavarusteet(String s) {
        lisavarusteet = Integer.parseInt(s);
        return null;
    }
    
    /**
     * Asettaa huoltohistorian kuntoon tietyn merkkijonon joka koostuu kokonaisluvuista
     * @param s haluttu luku
     * @return null
     */
    public String setHuoltohistoria(String s) {
        huoltohistoria = Integer.parseInt(s);
        return null;
    }
    
    /**
     * Asettaa auton Auto DNA:han tietyn merkkijonon joka koostuu kokonaisluvuista
     * @param s haluttu luku
     * @return null
     */
    public String setAutoDna(String s) {
        autoDna = Integer.parseInt(s);
        return null;
    }
    
    /**
     * Asettaa auton yhteenvetoon halutun merkkijonon
     * @param s haluttu merkkijono
     * @return null
     */
    public String setYhteenveto(String s) {
        yhteenveto = s;
        return null;
    }

    /**
     * Palauttaa auton malli ID:n
     * @return malli ID
     */
    public int getMalliId() {
        return malliId;
    }

    /**
     * Asettaa auton malli ID:ksi halutun luvun
     * @param malliId2 haluttu luku
     */
    public void setMalliId(int malliId2) {
        malliId = malliId2;
    }

    /**
     * Antaa k:n kentän sisällön merkkijonona
     * @param k monesko kentän sisältö palautetaan
     * @param rekisteri Rekisteri josta voidaan hakea
     * @return kentän sisältö merkkijonona
     */
    public String anna(int k, Rekisteri rekisteri) {
            if (k == 0) return asiakas;
            if (k == 1) return rekisteriNro;
            
            int malliIdNro = malliId;
            Malli autonMalli = rekisteri.getMalli(malliId);
            int merkkiIdNro = autonMalli.getMerkkiId();
            
            if (k == 2) {
                return rekisteri.annaMerkinNimi(merkkiIdNro);
            }
            if (k == 3) {
                return rekisteri.annaMallinNimi(malliIdNro);
            }
            return "";
    }
}
