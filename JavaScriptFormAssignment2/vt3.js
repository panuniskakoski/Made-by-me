// data-muuttuja sisältää kaiken tarvittavan ja on rakenteeltaan lähes samankaltainen kuin viikkotehtävässä 2
// Rastileimaukset on siirretty tupa-rakenteesta suoraan jokaisen joukkueen yhteyteen
//
// voit tutkia tarkemmin käsiteltävää tietorakennetta konsolin kautta 
// tai json-editorin kautta osoitteessa http://jsoneditoronline.org/
// Jos käytät json-editoria niin avaa data osoitteesta:
// http://appro.mit.jyu.fi/tiea2120/vt/vt3/data.json

"use strict";

console.log(data);

window.onload = function() {
    
    function luoLomake() {
        
        // Ensin luodaan leimaustavat, siten että eri vaihtoehdot haetaan tietokannasta.
        let leimat = [];
        for (let joukkue of data.joukkueet) {
            for (let i=0; i < joukkue.leimaustapa.length; i++) {
                if (!leimat.includes(joukkue.leimaustapa[i])) leimat.push(joukkue.leimaustapa[i]);               
            }
        }
                
        //let leimaustapa = document.getElementById("leimaTaulukko");      
        for (let i=0; i < leimat.length; i++) {
            

            if (i===0) {
                let di = document.createElement("di");
                di.setAttribute("id", "leimaTaulukko")
                leimaustapa.appendChild(di);
            } 

                let leima = document.createElement("input");
                leima.setAttribute("type", "checkbox");
                leima.setAttribute("id", leimat[i]);
                leima.setAttribute("name", "CB");
                
                let label = document.createElement("label");
                label.setAttribute("id", "leimaLab");
                label.textContent = leimat[i];
                leimaustapa.appendChild(label);
                label.appendChild(leima);
        }
                                            
        // Lopuksi luodaan erikseen "Lomake" kohta. Tämä siksi, että sitä ei löydy tietokannasta, mutta se on esimerkissä.        
        let leima = document.createElement("input");
        leima.setAttribute("type", "checkbox");
        leima.setAttribute("id", "Lomake");
        leima.setAttribute("name", "CB");
        
        let label = document.createElement("label");
        label.setAttribute("id", "leimaLab");
        label.textContent = "Lomake";
        leimaustapa.appendChild(label);
        label.appendChild(leima);
                                            

        
        // Tällä napilla lisätään jäsenkenttien määrää
        let buttonEnemman = document.createElement("button");
        buttonEnemman.textContent = "+";
        let jasenet = document.getElementById("jasenet");
        jasenet.appendChild(buttonEnemman);
        buttonEnemman.addEventListener('click', lisaaInput);
        buttonEnemman.setAttribute("id", "plus");
        buttonEnemman.setAttribute("type", "button");
        
        // Pitää lukua kenttien määrästä
        let count = 6;
        
        // Funktio jolla lisätään kenttä
        function lisaaInput() {
            let str = "jasen" + count;
            
            let nappi = document.getElementById("plus");
            
            let pJ = document.createElement("p");
            let labelPJ = document.createElement("label");
            let inputPJ = document.createElement("input");
            inputPJ.setAttribute("id", str);
            inputPJ.setAttribute("name", "jasen");
            
            jasenet.insertBefore(pJ, nappi);
            
            pJ.appendChild(labelPJ);
            labelPJ.textContent = "Jäsen " + count + ":    ";
            labelPJ.appendChild(inputPJ);
            
            count += 1;
        }       
    }
    luoLomake();
    
    // Kerätään tiedot lomakkeesta
    function keraaTiedot() {
        
        // Joukkueen nimi
        let nimiInput = document.getElementById("joukkueenNimi").value.trim();
        
        // Luontiaika
        let aikaInput = document.getElementById("luontiAika").value.trim();
        aikaInput = aikaInput.replace("T", " ");
        
        // Leimaustavat
        let leimat = document.getElementsByName("CB");
        let leimaustavat = [];
        
        for (let i of leimat) {
            if (i.checked === true) leimaustavat.push(i.id);
        }
        
        // Sarja
        let sarjaNimi = "";
        let sarjat = document.getElementsByName("sarja");
        
        for (let i of sarjat) {
            if (i.checked === true) sarjaNimi = i.id;
        }
        
        // Luodaan taulukko sarjoille ja otetaan sarjaId talteen
        let sarjatTaul = [];
        let sarjaId = "";
        for (let kisa of data.kisat) {
            for (let sarja of kisa.sarjat) {
                if (!sarjatTaul.includes(sarja.nimi)) sarjatTaul.push(sarja.nimi);
                if (sarja.nimi === sarjaNimi) sarjaId = sarja.id; 
            }
        }
        
        // Joukkueen jäsenet
        let jasenet = [];      
        let jasenKentat = document.getElementsByName("jasen");
        
        for (let jasen of jasenKentat) {
            if (jasen.value.trim().length > 0) jasenet.push(jasen.value); 
        }
        
        // Tallennetaan hyväksytyt tiedot tietokantaan
        function tallennaTiedot() {
            let joukkue = {
                nimi: nimiInput,
                jasenet: jasenet,
                sarja: sarjaId,
                seura: null,
                id: luoId(), // TODO: tähän random numero gen juttu
                leimaustapa: leimaustavat,
                luontiaika: aikaInput,
                matka: 0,
                pisteet: 0,
                rastit: []
                };
            
            data.joukkueet.push(joukkue);
            tyhjennaLomake();
            tyhjennaLista();
            luoLista();
            }
        
        // Luo joukkueelle uniikin 16-numeroisen id numeron
        function luoId() {
            let id = Math.floor(1000000000000000 + Math.random() * 9000000000000000);
            for (let jouk of data.joukkueet) {
                if (jouk.id === id) luoId();
            }
            return id;
        }
        
        // Tähän luodaan tarkistin, mikä tarkastaa, että kaikki tarvittavat tiedot on syötetty,
        // muuten tietoja ei tallenneta.
        if (nimiInput.length > 0 &&
            aikaInput.length > 0 &&
            onkoLeimaustapa() &&
            onkoTarpeeksiJasenia() &&
            onkoJoukkueOlemassa() &&
            valittuKilpailu()) tallennaTiedot();
        
        // Tarkistetaan onko nimi jo käytössä
        function onkoJoukkueOlemassa() {
            for (let joukkue of data.joukkueet) {
                if (nimiInput.trim() === joukkue.nimi.trim()) {
                    document.getElementById("joukkueenNimi").setCustomValidity("Nimi on jo käytössä!");
                    return false;
                }
            }
            document.getElementById("joukkueenNimi").setCustomValidity("");
            return true;
        }
        
        function valittuKilpailu() {
            let valittu = document.getElementById("kilpailu");
            if (valittu.value === "Ei kilpailua") {
                document.getElementById("kilpailu").setCustomValidity("Valitse kilpailu!");
                return false;
            } else {
                document.getElementById("kilpailu").setCustomValidity("");
                return true;
            }
        }
        
        // Tarkistaa onko tarpeeksi jäseniä täytetty
        function onkoTarpeeksiJasenia() {
            if (jasenet.length < 2) {
                document.getElementById("jasen1").setCustomValidity("Joukkueessa täytyy olla vähintään kaksi jäsentä!");
                document.getElementById("jasen2").setCustomValidity("Joukkueessa täytyy olla vähintään kaksi jäsentä!");
                return false;
            } else { 
                document.getElementById("jasen1").setCustomValidity("");
                document.getElementById("jasen2").setCustomValidity("");
                return true; 
            }
        }
        
        // Tarkistaa onko merkattu vähintään yksi leimaustapa
        function onkoLeimaustapa() {
            if (leimaustavat.length < 1) {
                for (let i=0; i < leimat.length; i++) {
                    document.getElementById(leimat[i].id).setCustomValidity("Vähintään yksi leimaustapa pitää olla valittuna!");
                }
                    return false;
                } else {
                    for (let i=0; i < leimat.length; i++) {
                    document.getElementById(leimat[i].id).setCustomValidity(""); 
                    }
                    return true;
                }
            }
        
        // Funktio joka tyhjentää lomakkeen kentät tallennuksen yhteydessä
        function tyhjennaLomake() {
            let nimiInput = document.getElementById("joukkueenNimi");
            let aikaInput = document.getElementById("luontiAika");
            
            nimiInput.value = "";
            aikaInput.value = "";
            
            let leimat = document.getElementsByName("CB");
            for (let leima of leimat) {
                leima.checked = false;
            }
            
            let sarja = document.getElementsByName("sarja");
            sarja[0].checked = true;
            
            let jasenKentat = document.getElementsByName("jasen");
            for (let kentta of jasenKentat) {
                kentta.value = "";
            }
            
            document.getElementById("kilpailu").value = "Ei kilpailua";
        } 
        
        // Tyhjentää joukkuelistan
        function tyhjennaLista() {
            let root = document.getElementById("lista");
            while (root.firstChild) {
                root.removeChild(root.firstChild);
            }
        }
    }
    
    // Kerätään kilpailun tiedot ylös kentistä
    function keraaKilpailunTiedot() {        
        let nimiKentta = document.getElementById("kilpailunNimi");
        
        // Käsittelee kilpailun nimen, sekä selvittää onko se kelvollinen
        function keraaNimi() {
            let nimi = nimiKentta.value.trim();
            if (nimi.length < 1) {
                document.getElementById("kilpailunNimi").setCustomValidity("Kilpailun nimi ei voi olla tyhjänä!");
                return false;
            } else {
                for (let kilp of data.kisat) {
                    if (kilp.nimi === nimi) {
                        document.getElementById("kilpailunNimi").setCustomValidity("Kilpailun nimi on jo käytössä!");
                        return false;
                    }
                }
                document.getElementById("kilpailunNimi").setCustomValidity("");
                return true;
            }
        }
        
        let kestoKentta = document.getElementById("kilpailunKesto");
        
        // Käsittelee kilpailun keston, sekä selvittää onko se kelvollinen
        function keraaKesto() {
            let kesto = kestoKentta.value.trim();
            kesto = parseInt(kesto);
            if (kesto > 0) {
                document.getElementById("kilpailunKesto").setCustomValidity("");
                return true;               
            } else {
                document.getElementById("kilpailunKesto").setCustomValidity("Viallinen syöte! Ilmoita kesto lukuna joka kuvaa tuntien määrää!");
                return false; 
            }
        }
        
        let alkuaikaKentta = document.getElementById("alkuaika");   // "2018-06-20T11:11"
        
        let loppuaikaKentta = document.getElementById("loppuaika");
        
        // Keraa tiedot syötetyistä ajoista, sekä selvittää ovatko ne kelvollisia
        function keraaAika() {
            let alkuaika = alkuaikaKentta.value;
            let loppuaika = loppuaikaKentta.value;
            let kesto = document.getElementById("kilpailunKesto").value;
            
            // Funktio joka lisää kilpailun keston alkuaikaan ja palauttaa sitten minimi loppumisajan
            function loppuaikaPlusKesto() {
                let kalenteriEiKarkaus = [31,28,31,30,31,30,31,31,30,31,30,31];
                let kalenteriKarkaus = [31,29,31,30,31,30,31,31,30,31,30,31];
                
                let v = parseInt(alkuaika.substring(0,4));
                let kk = parseInt(alkuaika.substring(5,7));
                let pv = parseInt(alkuaika.substring(8,10));
                let h = parseInt(alkuaika.substring(11,13));
                let min = parseInt(alkuaika.substring(14,16));
                
                h = h + parseInt(kesto);
                let pvlaskuri = 0;
                
                if (h > 23) {
                    while(h > 23) {
                        h = h - 24;
                        pvlaskuri += 1;
                    }
                    pv = pv + pvlaskuri;
                    
                    if (v % 4 === 0) {
                        let paivat = kalenteriKarkaus[kk-1];
                        if (pv > paivat) {
                            pv = pv - paivat;
                            kk += 1;
                            if (kk > kalenteriKarkaus.length) {
                                kk = 1;
                                v += 1;
                            }
                        }
                    } else {
                        let paivat = kalenteriEiKarkaus[kk-1];
                        if (pv > paivat) {
                            pv = pv - paivat;
                            kk += 1;
                            if (kk > kalenteriKarkaus.length) {
                                kk = 1;
                                v += 1;
                            }
                        }
                    }
                }
                
                // Takaa, että yksinumeroisissa numeroissa on 0 edessä
                function takaisinStr(n) {
                    return n > 9 ? "" + n: "0" + n;
                }
                
                v = takaisinStr(v).toString();
                kk = takaisinStr(kk).toString();
                pv = takaisinStr(pv).toString();
                h = takaisinStr(h).toString();
                min = takaisinStr(min).toString();
                
                let tulos = v + "-" + kk + "-" + pv + "T" + h + ":" + min;
                return tulos;
            }
            let minLoppuaika = loppuaikaPlusKesto();
            
            // Tarkistetaan täyttyvätkö kriteerit
            if (alkuaika.length < 1 || loppuaika.length < 1) {
                document.getElementById("alkuaika").setCustomValidity("Molemmat aikakentät tulee täyttää!");
                return false; 
                document.getElementById("loppuaika").setCustomValidity("Molemmat aikakentät tulee täyttää!");
                return false;                 
            } else {
                if (alkuaika > (loppuaikaPlusKesto)) {
                    document.getElementById("alkuaika").setCustomValidity("Alkuaika on myöhempi kuin loppuaika!");
                    return false; 
                } else {
                    if (minLoppuaika > loppuaika) {
                        document.getElementById("loppuaika").setCustomValidity("Alkuaika + kesto kestää pitempää, kuin asetettu loppumisaika!");
                        return false; 
                    } else {
                        document.getElementById("alkuaika").setCustomValidity("");
                        document.getElementById("loppuaika").setCustomValidity("");
                        return true;
                    }
                }
            }
            
        }
        
        if (keraaNimi() && keraaKesto() && keraaAika()) {
            let kilpailu = {
                nimi: nimiKentta.value.trim(),
                id: luoId,
                loppuaika: loppuaikaKentta.value,
                kesto: kestoKentta.value,
                alkuaika: alkuaikaKentta.value,
                sarjat: data.kisat[0].sarjat
            };
            
            data.kisat.push(kilpailu);
            
            let valikko = document.getElementById("kilpailu");
            let uusi = document.createElement("option");
            uusi.textContent = nimiKentta.value.trim();
            valikko.appendChild(uusi);
                

            nimiKentta.value = "";
            kestoKentta.value = "";
            alkuaikaKentta.value = "";
            loppuaikaKentta.value = "";
        }
        
        // Luo joukkueelle uniikin 16-numeroisen id numeron
        function luoId() {
            let id = Math.floor(1000000000000000 + Math.random() * 9000000000000000);
            for (let kisa of data.kisat) {
                if (kisa.id === id) luoId();
            }
            return id;
        }      
    }
    
    // Globaali muuttuja joka muistaa klikatun joukkueen IDn
    var joukkueenIDnumero = 0;
        
    // Funktio joka luo listan joukkueista
    function luoLista() {
        let joukkueet = [];
        for (let joukkue of data.joukkueet) {
            joukkueet.push(joukkue.nimi);
        }
        joukkueet.sort();       // Huom! Listan lajittelu on case sensitive. Isot kirjaimet ovat pieniä suurempiarvoisempia.
        
        let spot = document.getElementById("lista");
            
        for (let joukkue of joukkueet) {
            let li = document.createElement("li");            
            let a = document.createElement("a");
            a.textContent = joukkue;
            a.setAttribute('href', "#joukkueenlisays");
            li.appendChild(a);
            spot.appendChild(li);
            li.addEventListener('click', muutaTietoja);            
            
            function muutaTietoja() {    
                for (let jouk of data.joukkueet) {
                    if (jouk.nimi === joukkue) {
                        joukkueenIDnumero = jouk.id;
                        
                        document.getElementById("joukkueenNimi").value = jouk.nimi;
                        
                        let valitunJoukkueenNimi = document.getElementById("joukkueenNimi").value;
                        
                        let luontiaika = jouk.luontiaika;
                        luontiaika = luontiaika.replace(" ", "T"); 
                        document.getElementById("luontiAika").value = luontiaika;
                        luontiaika = luontiaika.replace("T", " "); 
                        let leimat = document.getElementsByName("CB");
                        
                        // Poistetaan leimastavoista rastit
                        for (let leima of leimat) {
                            leima.checked = false;
                            for (let lei of jouk.leimaustapa) {
                                if (lei === leima.id) leima.checked = true;
                            }
                        }
                        
                        // Etsitään oikea sarja ja kilpailu
                        let sarjaId = jouk.sarja;
                        let kisaId = "";
                        let sarjaNimi = "";
                        for (let kisa of data.kisat) {
                            for (let sarja of kisa.sarjat) {
                                if (sarja.id === jouk.sarja) {
                                    kisaId = sarja.kilpailu;
                                    sarjaNimi = sarja.nimi;
                                }
                            }
                        }
                        
                        // Nyt haetaan oikea kilpailu
                        let kisa = "";
                        for (let kis of data.kisat) {
                            if (kis.id === kisaId) kisa = kis.nimi;
                        }
                        
                        // Nyt voidaan hakea oikea kilpailu
                        document.getElementById("kilpailu").value = kisa;
                        vaihdaSarjat();
                        
                        // Merkataan oikea sarja seuraavaksi
                        let sarjat = document.getElementsByName("sarja")
                        for (let sarja of sarjat) {
                            if (sarjaNimi === sarja.id) sarja.checked = true;
                        }
                        
                        // Tyhjennetään jäsen kentät
                        let jasenetTaulut = document.getElementsByName("jasen");
                        for (let i of jasenetTaulut) {
                            i.value = "";
                        }
                        
                        // Sijoitetaan jäsenet kenttiin
                        let count = 0;
                        for (let jasen of jouk.jasenet) {
                            if (count === jasenetTaulut.length) lisaaInput();
                            jasenetTaulut[count].value = jasen;
                            count++;
                        }
                        
                        // Luo tarvittaessa uuden jäsenkentän
                        function lisaaInput() {
                            let str = "jasen" + count;
                            
                            let nappi = document.getElementById("plus");
                            
                            let pJ = document.createElement("p");
                            let labelPJ = document.createElement("label");
                            let inputPJ = document.createElement("input");
                            inputPJ.setAttribute("id", str);
                            inputPJ.setAttribute("name", "jasen");
                            
                            jasenet.insertBefore(pJ, nappi);
                            
                            pJ.appendChild(labelPJ);
                            labelPJ.textContent = "Jäsen " + count + ":    ";
                            labelPJ.appendChild(inputPJ);
                            
                            count += 1;
                        }
                        
                        let nimiInput = document.getElementById("joukkueenNimi").value;
                        luontiaika = document.getElementById("luontiAika").value;
                        

                        // Tarkistetaan, että onko leimaustapa/tavat rastitettu
                        function onkoLeimaustapa() {
                            
                            // Leimaustavat
                            let leim2 = document.getElementsByName("CB");
                            let leimaustavat = [];
                            
                            for (let i of leim2) {
                                if (i.checked === true) leimaustavat.push(i.id);
                            }
                            
                            // Ensin luodaan leimaustavat, siten että eri vaihtoehdot haetaan tietokannasta.
                            let leim1 = [];
                            for (let joukkue of data.joukkueet) {
                                for (let i=0; i < joukkue.leimaustapa.length; i++) {
                                    if (!leim1.includes(joukkue.leimaustapa[i])) leim1.push(joukkue.leimaustapa[i]);               
                                }
                            }
                            
                            if (leimaustavat.length < 1) {
                                for (let i=0; i < leim2.length; i++) {
                                    document.getElementById(leim2[i].id).setCustomValidity("Vähintään yksi leimaustapa pitää olla valittuna!");
                                }
                                    return false;
                                } else {
                                    for (let i=0; i < leim2.length; i++) {
                                    document.getElementById(leim2[i].id).setCustomValidity(""); 
                                    }
                                    return true;
                                }
                        }
                        

                        // Tarkistaa onko tarpeeksi jäseniä täytetty
                        function onkoTarpeeksiJasenia() {
                            
                            // Joukkueen jäsenet
                            let jasenet = [];      
                            let jasenKentat = document.getElementsByName("jasen");
                            
                            for (let jasen of jasenKentat) {
                                if (jasen.value.trim().length > 0) jasenet.push(jasen.value); 
                            }
                            
                            if (jasenet.length < 2) {
                                document.getElementById("jasen1").setCustomValidity("Joukkueessa täytyy olla vähintään kaksi jäsentä!");
                                document.getElementById("jasen2").setCustomValidity("Joukkueessa täytyy olla vähintään kaksi jäsentä!");
                                return false;
                            } else { 
                                document.getElementById("jasen1").setCustomValidity("");
                                document.getElementById("jasen2").setCustomValidity("");
                                return true; 
                            }
                        }
                        
                        // Tarkistetaan onko nimi jo käytössä
                        function onkoJoukkueOlemassa() {
                            for (let joukkue of data.joukkueet) {
                                if (nimiInput.trim() === joukkue.nimi.trim() && joukkue.id !== jouk.id) {
                                    document.getElementById("joukkueenNimi").setCustomValidity("Nimi on jo käytössä!");
                                    return false;
                                }
                            }
                            document.getElementById("joukkueenNimi").setCustomValidity("");
                            return true;
                        }
                        
                        // Tarkistetaan, että onko kilpailu valittu
                        function valittuKilpailu() {
                            let valittu = document.getElementById("kilpailu");
                            if (valittu.value === "Ei kilpailua") {
                                document.getElementById("kilpailu").setCustomValidity("Valitse kilpailu!");
                                return false;
                            } else {
                                document.getElementById("kilpailu").setCustomValidity("");
                                return true;
                            }
                        }     
                        
                        break;
                    }  
                }
                tallennaMuutokset.disabled = false;
            }  
            
                        // Tallennetaan kriteerien käsittelyn jälkeen muokatut tiedot tietokantaan
                        function tallennaTietokantaan() {
                            
                        let nimiInput = document.getElementById("joukkueenNimi").value;
                        let luontiaika = document.getElementById("luontiAika").value;
                        

                        // Tarkistetaan, että onko leimaustapa/tavat rastitettu
                        function onkoLeimaustapa() {
                            
                            // Leimaustavat
                            let leim2 = document.getElementsByName("CB");
                            let leimaustavat = [];
                            
                            for (let i of leim2) {
                                if (i.checked === true) leimaustavat.push(i.id);
                            }
                            
                            // Ensin luodaan leimaustavat, siten että eri vaihtoehdot haetaan tietokannasta.
                            let leim1 = [];
                            for (let joukkue of data.joukkueet) {
                                for (let i=0; i < joukkue.leimaustapa.length; i++) {
                                    if (!leim1.includes(joukkue.leimaustapa[i])) leim1.push(joukkue.leimaustapa[i]);               
                                }
                            }
                            
                            if (leimaustavat.length < 1) {
                                for (let i=0; i < leim2.length; i++) {
                                    document.getElementById(leim2[i].id).setCustomValidity("Vähintään yksi leimaustapa pitää olla valittuna!");
                                }
                                    return false;
                                } else {
                                    for (let i=0; i < leim2.length; i++) {
                                    document.getElementById(leim2[i].id).setCustomValidity(""); 
                                    }
                                    return true;
                                }
                        }
                        
                        // Tarkistaa onko tarpeeksi jäseniä täytetty
                        function onkoTarpeeksiJasenia() {
                            
                            // Joukkueen jäsenet
                            let jasenet = [];      
                            let jasenKentat = document.getElementsByName("jasen");
                            
                            for (let jasen of jasenKentat) {
                                if (jasen.value.trim().length > 0) jasenet.push(jasen.value); 
                            }
                            
                            if (jasenet.length < 2) {
                                document.getElementById("jasen1").setCustomValidity("Joukkueessa täytyy olla vähintään kaksi jäsentä!");
                                document.getElementById("jasen2").setCustomValidity("Joukkueessa täytyy olla vähintään kaksi jäsentä!");
                                return false;
                            } else { 
                                document.getElementById("jasen1").setCustomValidity("");
                                document.getElementById("jasen2").setCustomValidity("");
                                return true; 
                            }
                        }
                        
                        // Tarkistetaan onko nimi jo käytössä
                        function onkoJoukkueOlemassa() {
                            for (let joukkue of data.joukkueet) {
                                if (nimiInput.trim() === joukkue.nimi.trim() && joukkue.id !== joukkueenIDnumero) {
                                    document.getElementById("joukkueenNimi").setCustomValidity("Nimi on jo käytössä!");
                                    return false;
                                }
                            }
                            document.getElementById("joukkueenNimi").setCustomValidity("");
                            return true;
                        }
                        
                        // Tarkistetaan, että onko kilpailu valittu
                        function valittuKilpailu() {
                            let valittu = document.getElementById("kilpailu");
                            if (valittu.value === "Ei kilpailua") {
                                document.getElementById("kilpailu").setCustomValidity("Valitse kilpailu!");
                                return false;
                            } else {
                                document.getElementById("kilpailu").setCustomValidity("");
                                return true;
                            }
                        }
                            
                            if (document.getElementById("joukkueenNimi").value.length > 0 &&
                            document.getElementById("luontiAika").value.length > 0 &&
                            onkoLeimaustapa() &&
                            onkoTarpeeksiJasenia() &&
                            onkoJoukkueOlemassa() &&
                            valittuKilpailu()) {
                                
                                let jouk = {};
                                
                                for (let joukkue of data.joukkueet) {
                                    if (joukkueenIDnumero === joukkue.id) {
                                        jouk = joukkue;
                                        break;
                                    }
                                }
                                
                                jouk.nimi = document.getElementById("joukkueenNimi").value.trim();
                                jouk.luontiaika = document.getElementById("luontiAika").value;
                                
                                // Leimaustavat
                                let leimat = document.getElementsByName("CB");
                                let leimaustavat = [];
                                
                                for (let i of leimat) {
                                    if (i.checked === true) leimaustavat.push(i.id);
                                }
                                
                                // Sarja
                                let sarjaNimi = "";
                                let sarjat = document.getElementsByName("sarja");
                                
                                for (let i of sarjat) {
                                    if (i.checked === true) sarjaNimi = i.id;
                                }
                                
                                // Luodaan taulukko sarjoille ja otetaan sarjaId talteen
                                let sarjatTaul = [];
                                let sarjaId = "";
                                for (let kisa of data.kisat) {
                                    for (let sarja of kisa.sarjat) {
                                        if (!sarjatTaul.includes(sarja.nimi)) sarjatTaul.push(sarja.nimi);
                                        if (sarja.nimi === sarjaNimi) sarjaId = sarja.id; 
                                    }
                                }
                                
                                // Joukkueen jäsenet
                                let jasenet = [];      
                                let jasenKentat = document.getElementsByName("jasen");
                                
                                for (let jasen of jasenKentat) {
                                    if (jasen.value.trim().length > 0) jasenet.push(jasen.value); 
                                }
                                
                                jouk.jasenet = jasenet;
                                jouk.sarja = sarjaId;
                                jouk.leimaustapa = leimaustavat;
                                
                                tyhjennaLomake();
                                tyhjennaLista();
                                luoLista();
                                
                                // Funktio joka tyhjentää lomakkeen kentät tallennuksen yhteydessä
                                function tyhjennaLomake() {
                                    let nimiInput = document.getElementById("joukkueenNimi");
                                    let aikaInput = document.getElementById("luontiAika");
                                    
                                    nimiInput.value = "";
                                    aikaInput.value = "";
                                    
                                    let leimat = document.getElementsByName("CB");
                                    for (let leima of leimat) {
                                        leima.checked = false;
                                    }
                                    
                                    let sarja = document.getElementsByName("sarja");
                                    sarja[0].checked = true;
                                    
                                    let jasenKentat = document.getElementsByName("jasen");
                                    for (let kentta of jasenKentat) {
                                        kentta.value = "";
                                    }
                                    
                                    document.getElementById("kilpailu").value = "Ei kilpailua";
                                } 
                                
                                // Tyhjentää joukkuelistan
                                function tyhjennaLista() {
                                    let root = document.getElementById("lista");
                                    while (root.firstChild) {
                                        root.removeChild(root.firstChild);
                                    }
                                }
                            }
                        }
                          
                let tallennaMuutokset = document.getElementById("tallennaMuutokset");
                tallennaMuutokset.setAttribute("type", "button");
                tallennaMuutokset.addEventListener('click', tallennaTietokantaan);
                tallennaMuutokset.disabled = true;
                }
        

    }
    luoLista();
    
    // Luodaan vaihtoehdot alasvetovalikkoon
    function luoAlasvetoValikko() {
        let valikko = document.getElementById("kilpailu");
        for (let kisa of data.kisat) {
            let uusi = document.createElement("option");
            uusi.textContent = kisa.nimi;
            valikko.appendChild(uusi);
        }
    }
    luoAlasvetoValikko();
    
    function vaihdaSarjat() {
        
        // Sitten luodaan sarjat, siten että eri vaihtoehdot haetaan tietokannasta.
        let valittu = document.getElementById("kilpailu").value;
        
        // Ensin tyhjennetään taulu
        let di = document.getElementById("sarjaTaulukko");
        while(di.firstChild) {
            di.removeChild(di.firstChild);
        }
        
        let sarjatTaul = [];
        for (let kisa of data.kisat) {
            if (kisa.nimi === valittu) {
                for (let sarja of kisa.sarjat) {
                    if (!sarjatTaul.includes(sarja.nimi)) sarjatTaul.push(sarja.nimi); 
                }
            } else {
                let di = document.getElementById("sarjaTaulukko");
                while(di.firstChild) {
                    di.removeChild(di.firstChild);
                }
            }
        }
        
        // HUOM! Olen tietoinen, että tällä tavalla järjestys on 4h, 2h, 8h (toisin kuin esimerkissä (2h, 4h, 8h)).
        // Oletan, että se että nappulat ladataan tietokannasta on tärkeämpää, kuin että järjestys on oikea.
        // Mielestäni fiksumpaa olisi, vaihtaa järjestys tietokannasta. Jos tämä ei kelpaa, niin asia korjataan.
        for (let i=0; i < sarjatTaul.length; i++) {  

                let sarja = document.createElement("input");
                sarja.setAttribute("type", "radio");
                sarja.setAttribute("id", sarjatTaul[i]);
                sarja.setAttribute("name", "sarja");
            if (i===0) sarja.checked = true;
                
                let label = document.createElement("label");
                label.setAttribute("id", "radioS");
                label.textContent = sarjatTaul[i];
                sarjaTaulukko.appendChild(label);
                label.appendChild(sarja);
        }
    }
        
    let alasveto = document.getElementById("kilpailu");
    alasveto.addEventListener('change', vaihdaSarjat);
    
    let tallennaButton = document.getElementById("tallenna");
    tallennaButton.setAttribute("type", "button");
    tallennaButton.addEventListener('click', keraaTiedot);
    
    let tallennaKisa = document.getElementById("tallennaKisa");
    tallennaKisa.setAttribute("type", "submit");
    tallennaKisa.addEventListener('click', keraaKilpailunTiedot);
}



