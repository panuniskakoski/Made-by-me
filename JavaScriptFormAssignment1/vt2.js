// data-muuttuja on sama kuin viikkotehtävässä 1.
//

"use strict";

console.log(data);


window.onload = function() {
    
// Ohjelma tulostaa joukkueiden nimet ja pisteet pisteiden mukaisessa laskevassa 
//suuruusjärjestyksessä. Ts. Eniten pisteitä saanut joukkue tulostetaan ensimmäisenä.
function luoSivunSisalto() {
    
    function luoTulostaulukko() {
        var taulukkoJoukkueet = [];
        
        var merkintaRekisteri = [];
        
        for (var sarja of data.sarjat) {
            
            var sarjaNimi = sarja.nimi;
            
            for (var joukkue of sarja.joukkueet) {
                
                var nimi = joukkue.nimi;
                var pisteet = 0;
                var id = parseInt(joukkue.id);
                var jasenet = joukkue.jasenet;
                
                // Nyt meillä on joukkue id, lähdetään etsimään rastimerkintää jossa sama id
                for (var rasA of data.tupa) {
                    if (parseInt(rasA.joukkue) === id) {
                        var rastiKoodi = parseInt(rasA.rasti);
                        
                        // Nyt meillä on rastin koodi, lähdetään etsimään rasteista rastia jolla sama id
                        for (var rasB of data.rastit) {
                            if (parseInt(rasB.id) === rastiKoodi) {
                                
                                var duplikaatti = false;
                                
                                for (var merkinta of merkintaRekisteri) {
                                    if (merkinta.joukId === id && merkinta.rastId === rastiKoodi) {
                                        duplikaatti = true;
                                    }
                                }                                
                                    
                                // Meillä on oikea rasti, jos koodi alkaa kokonaisluvulla, lisätään piste
                                if (duplikaatti === false && !isNaN(parseInt(rasB.koodi.substring(0,1)))) {
                                    var piste = parseInt(rasB.koodi.substring(0,1));
                                    pisteet += piste;
                                    
                                    var merkinta = {
                                        joukId: parseInt(id),
                                        rastId: parseInt(rastiKoodi)
                                    };
                                    
                                    merkintaRekisteri.push(merkinta);
                                    
                                    }
                                }
                            }
                        }
                    }

                // Luodaan joukkue
                var joukkue = {
                    joukkueNimi: nimi,
                    joukkuePisteet: pisteet,
                    sarjanNimi: sarjaNimi,
                    jasenet: jasenet,
                    id: id
                };
            
                taulukkoJoukkueet.push(joukkue);
            }
        }        
        
            // Luodaan taulukot jokaiselle sarjalle
        var kaikkiJoukkueet = [];
        var sarja2hJoukkueet = [];
        var sarja4hJoukkueet = [];
        var sarja8hJoukkueet = [];
        
        for (let jouk of taulukkoJoukkueet) {
        
        if (jouk.sarjanNimi === "2h") {
        var sarja2h = {
            joukkueNimi: joukkue.nimi,
            joukkuePisteet: joukkue.pisteet,
            sarjanNimi: joukkue.sarjanNimi,
            jasenet: jouk.jasenet,
            id: jouk.id
            };
            
        sarja2hJoukkueet.push(jouk);
        }
        
        if (jouk.sarjanNimi === "4h") {
            var sarja4h = {
            joukkueNimi: joukkue.nimi,
            joukkuePisteet: joukkue.pisteet,
            sarjanNimi: joukkue.sarjanNimi,
            jasenet: jouk.jasenet,
            id: jouk.id
            };
            
        sarja4hJoukkueet.push(jouk);
        }
        
        if (jouk.sarjanNimi === "8h") {
            var sarja8h = {
            joukkueNimi: joukkue.nimi,
            joukkuePisteet: joukkue.pisteet,
            sarjanNimi: joukkue.sarjanNimi,
            jasenet: jouk.jasenet,
            id: jouk.id
            };
            
        sarja8hJoukkueet.push(jouk);
        }
        }
        
        // Lisätään joukkuetaulukot yhteen isoon taulukkoon
        kaikkiJoukkueet.push(sarja2hJoukkueet);
        kaikkiJoukkueet.push(sarja4hJoukkueet);
        kaikkiJoukkueet.push(sarja8hJoukkueet);
        
        // Lajittelee joukkueet nimien perusteella
        for (let i of kaikkiJoukkueet) {
            i.sort(function (a, b) {
            let nimiA = a.joukkueNimi.toUpperCase();
            let nimiB = b.joukkueNimi.toUpperCase();
            return (nimiA < nimiB) ? -1 : (nimiA > nimiB) ? 1 : 0;
        });
            
        // Lajittelee joukkueet pisteiden perusteella
        for (let i of kaikkiJoukkueet) {
            i.sort(function (a, b) {
                return b.joukkuePisteet - a.joukkuePisteet;
            });
        }    
            
        }    

        
        console.log("-----------------------------------------------------------------------------------------------------------------------------------------------------------");
        // NYT TULOSTETTAVAT ON OK
        // Seuraavaksi tehdään sivun asettelut
        
        let div = document.getElementById('tupa'); 
        let table = document.createElement("table");
        
        div.appendChild(table);
        
        let caption = document.createElement("caption");
        
        table.appendChild(caption);
        caption.textContent = "Tulokset";
    
        let tr1 = document.createElement("tr");
        
        table.appendChild(tr1);
        
        let jouk = document.createElement("th");
        let sarj = document.createElement("th");
        let pist = document.createElement("th");
        
        tr1.append(sarj);
        sarj.textContent = "Sarja";
        
        tr1.append(jouk);
        jouk.textContent = "Joukkue";
        
        tr1.append(pist);
        pist.textContent = "Pisteet"
        
        for (let sarja of kaikkiJoukkueet) {
            for (let joukkue of sarja) {
                
                let tr = document.createElement("tr");
                table.appendChild(tr);
                
                let sarj = document.createElement("td");
                let jouk = document.createElement("a");
                let nimet = document.createElement("br");
                let pisteet = document.createElement("td");
                
                tr.append(sarj);
                sarj.textContent = joukkue.sarjanNimi;
        
                tr.append(jouk);
                jouk.textContent = joukkue.joukkueNimi + ": ";
         
                // Ankkuroidaan joukkueiden nimet
                jouk.setAttribute('href', "#joukkue");
                jouk.addEventListener('click', muutaTietoja);
                
                // Funktio joka käsittelee tietojen muuttumisen
                function muutaTietoja() {
                    
                    let idNro = joukkue.id;
                    
                    buttonTallennaMuutokset.disabled = false;
                    
                    // Tyhjennetään ensin kentät
                    for (let i=3; i < kentat.length; i++) {
                        kentat[i].value = "";
                    }
                    
                    kentat[3].value = joukkue.joukkueNimi;
                    let count = 4;
                    for (let i=0; i < joukkue.jasenet.length; i++) {
                        if(i > 1) lisaaInput();
                        kentat[count].value = joukkue.jasenet[i];
                        count += 1;
                    }

                    buttonTallennaMuutokset.addEventListener('click', muutosTietokannassa);
                }
                
                // Suoritetaan muutos tietokannassa
                function muutosTietokannassa() {

                    let joukkueenNimi = kentat[3].value;
                    
                    for (let sarja of data.sarjat) {
                        for (let joukkue of sarja.joukkueet) {
                            if (joukkue.nimi === joukkueenNimi) {
                                joukkue.nimi = kentat[3].value;
                                joukkue.jasenet = [];
                                
                                for (let i=4; i < kentat.length; i++) {
                                    if (kentat[i].value.length > 0) {
                                        joukkue.jasenet.push(kentat[i].value);
                                    }
                                }
                                break;
                            }
                        }
                    }
                    
                    // Tyhjentää div elementin, että uusi data voidaan syöttää tilalle
                    function clearBox() {
                        var div = document.getElementById('tupa');
                        while(div.firstChild) {
                            div.removeChild(div.firstChild);
                        }
                    }
                    
                    for (let i=3; i < kentat.length; i++) {
                        kentat[i].value = "";
                    }
                    
                    buttonTallennaMuutokset.disabled = true;
                    buttonLisaaJoukkue.disabled = true;
                    
                    clearBox();
                    luoTulostaulukko();
                }

                // Tehdään jäsenistä string ja määritellään nimien listaus
                let jasenetStr = "";
                let count = 1;
                for (let jasen of joukkue.jasenet) {
                    if (count < joukkue.jasenet.length) jasenetStr = jasenetStr + " " + jasen + ", ";      
                    if (count === joukkue.jasenet.length) jasenetStr = jasenetStr + " " + jasen + "."; 
                    count += 1;
                }
                
                tr.append(jasenetStr);
                nimet.textContent = jasenetStr;
                
                tr.append(pisteet);
                pisteet.textContent = joukkue.joukkuePisteet;
            }
        }
    }
    luoTulostaulukko();
    
    console.log("-----------------------------------------------------------------------------------------------------------------------------------------------------------");
    // NYT TULOSTETTAVAT KAIKKI TULOSTUU OK SIVULLE
    // Seuraavaksi lisätään rastin-lisäys-systeemi
    
    let form = document.getElementsByTagName("form");
    let lomake = form[0];
    
    let fieldset = document.createElement("fieldset");
    
    lomake.appendChild(fieldset);
    
    let legend = document.createElement("legend");
    
    fieldset.appendChild(legend);
    
    legend.textContent = "Rastin tiedot";
    
    let p1 = document.createElement("p");
    let p2 = document.createElement("p");
    let p3 = document.createElement("p");
    let p4 = document.createElement("p");
    
    fieldset.appendChild(p1);
    fieldset.appendChild(p2);
    fieldset.appendChild(p3);
    fieldset.appendChild(p4);
    
    let labelLat = document.createElement("label");
    let labelLon = document.createElement("label");
    let labelKoodi = document.createElement("label");
    let buttonLisaaRasti = document.createElement("button");
    buttonLisaaRasti.setAttribute("type", "button");
    
    p1.appendChild(labelLat);
    p2.appendChild(labelLon);
    p3.appendChild(labelKoodi);
    p4.appendChild(buttonLisaaRasti);
    buttonLisaaRasti.setAttribute("id", "rasti");
    buttonLisaaRasti.disabled = true;
    
    p1.textContent = "Lat:  ";
    p2.textContent = "Lon:  ";
    p3.textContent = "Koodi:    ";
    buttonLisaaRasti.textContent = "Lisää rasti";
    
    let inputLat = document.createElement("input");
    let inputLon = document.createElement("input");
    let inputKoodi = document.createElement("input");
    
    p1.appendChild(inputLat);
    p2.appendChild(inputLon);
    p3.appendChild(inputKoodi);
    
    // Mikäli kentät on täytetty, nappi tulee käyttöön  
    inputLat.addEventListener('change', tarkistaKentatRasti);
    inputLon.addEventListener('change', tarkistaKentatRasti);
    inputKoodi.addEventListener('change', tarkistaKentatRasti);
    
    function tarkistaKentatRasti() {
        if (kentat[0].value.length > 0 && kentat[1].value.length > 0 && kentat[2].value.length > 0) {
            buttonLisaaRasti.disabled = false;
        } else {
            buttonLisaaRasti.disabled = true;
        }
    }
    
    console.log("-----------------------------------------------------------------------------------------------------------------------------------------------------------");
    // NYT NÄKYY KENTÄT JA NAPPI
    // Seuraavaksi lisätään ominaisuus, jossa lomakkeeseen lisätyt tiedot lisätään tietoihin
    
    var lom = document.getElementsByTagName("form");
    lom[0].action = "pohja.html";
    
    var kentat = document.getElementsByTagName("input");
    
    buttonLisaaRasti.addEventListener("click", lisaaRasti);
    
    // Funktio lisää rastin data.rastit taulukkoon, mikäli mikään input-kentistä ei ole tyhjänä
    function lisaaRasti() {
        if (kentat[0].value.length > 0 && kentat[1].value.length > 0 && kentat[2].value.length > 0) {
            
            let uusiRasti = {
                "lon": kentat[0].value,
                "lat": kentat[1].value,
                "koodi": kentat[2].value
            };
            
            data.rastit.push(uusiRasti);
            
            kentat[0].value = "";
            kentat[1].value = "";
            kentat[2].value = "";
            
            buttonLisaaRasti.disabled = true;
        }
    }
    
    console.log("-----------------------------------------------------------------------------------------------------------------------------------------------------------");
    // Seuraavaksi luodaan joukkueen lisäyksen lomake
    
    let lomakeJoukkue = form[1];  
    lomakeJoukkue.setAttribute("id", "lisaaJoukkue");
    let fieldsetJoukkue = document.createElement("fieldset");    
    lomakeJoukkue.appendChild(fieldsetJoukkue);
    
    let legendUusiJoukkue = document.createElement("legend");    
    fieldsetJoukkue.appendChild(legendUusiJoukkue);    
    legendUusiJoukkue.textContent = "Uusi joukkue";
    
    let pJ = document.createElement("p");
    fieldsetJoukkue.appendChild(pJ);
    
    let labelNimi = document.createElement("label");
    pJ.appendChild(labelNimi);
    labelNimi.textContent = "Nimi:  ";
    
    let inputPJ = document.createElement("input");
    labelNimi.appendChild(inputPJ);
    inputPJ.setAttribute("id", "joukkueNimi");

    
    let fieldsetJoukkueSis = document.createElement("fieldset");
    fieldsetJoukkue.appendChild(fieldsetJoukkueSis);
    
    let legendJasenet = document.createElement("legend");
    fieldsetJoukkueSis.appendChild(legendJasenet);
    legendJasenet.textContent = "Jäsenet";
    
    let pJ1 = document.createElement("p");
    let labelPJ1 = document.createElement("label");
    let inputPJ1 = document.createElement("input");
    inputPJ1.setAttribute("id", "jasen1");

    
    let pJ2 = document.createElement("p");
    let labelPJ2 = document.createElement("label");
    let inputPJ2 = document.createElement("input");
    inputPJ2.setAttribute("id", "jasen2");

    

    
    fieldsetJoukkueSis.appendChild(pJ1);
    pJ1.appendChild(labelPJ1);
    labelPJ1.textContent = "Jäsen 1:    ";
    labelPJ1.appendChild(inputPJ1);
    
    fieldsetJoukkueSis.appendChild(pJ2);
    pJ2.appendChild(labelPJ2);
    labelPJ2.textContent = "Jäsen 2:    ";
    labelPJ2.appendChild(inputPJ2);
    
    // Tällä napilla lisätään jäsenkenttien määrää
    let buttonEnemman = document.createElement("button");
    buttonEnemman.textContent = "+";
    fieldsetJoukkueSis.appendChild(buttonEnemman);
    buttonEnemman.addEventListener('click', lisaaInput);
    buttonEnemman.setAttribute("id", "plus");
    buttonEnemman.setAttribute("type", "button");
    
    // Pitää lukua kenttien määrästä
    let count = 3;
    
    // Funktio jolla lisätään kenttä
    function lisaaInput() {

        let str = "jasen" + count;
        
        let nappi = document.getElementById("plus");
        
        let pJ = document.createElement("p");
        let labelPJ = document.createElement("label");
        let inputPJ = document.createElement("input");
        inputPJ.setAttribute("id", str);
        
        fieldsetJoukkueSis.insertBefore(pJ, nappi);
        
        pJ.appendChild(labelPJ);
        labelPJ.textContent = "Jäsen " + count + ":    ";
        labelPJ.appendChild(inputPJ);
        
        count += 1;
    }
    
    // Nappi josta lisätään joukkue
    let buttonLisaaJoukkue = document.createElement("button");
    fieldsetJoukkue.appendChild(buttonLisaaJoukkue);
    buttonLisaaJoukkue.setAttribute("name", "joukkue");
    buttonLisaaJoukkue.setAttribute("type", "button");
    buttonLisaaJoukkue.textContent = "Lisää joukkue";
    buttonLisaaJoukkue.disabled = true;
    
    // Nappi josta tallennetaan muutokset
    let buttonTallennaMuutokset = document.createElement("button");
    fieldsetJoukkue.appendChild(buttonTallennaMuutokset);
    buttonTallennaMuutokset.setAttribute("name", "muutos");
    buttonTallennaMuutokset.setAttribute("type", "button");
    buttonTallennaMuutokset.textContent = "Tallenna muutokset";
    buttonTallennaMuutokset.disabled = true;
    
    // Mikäli kentät on täytetty, nappi tulee käyttöön  
    inputPJ.addEventListener('change', tarkistaKentatJoukkue);
    inputPJ1.addEventListener('change', tarkistaKentatJoukkue);
    inputPJ2.addEventListener('change', tarkistaKentatJoukkue);
    
    function tarkistaKentatJoukkue() {
        if (kentat[3].value.length > 0 && kentat[4].value.length > 0 && kentat[5].value.length > 0) {
            buttonLisaaJoukkue.disabled = false;
        } else {
            buttonLisaaJoukkue.disabled = true;
        }
    }
    
    console.log("-----------------------------------------------------------------------------------------------------------------------------------------------------------");
    // NYT NÄKYY KENTÄT JA NAPPI
    // Seuraavaksi lisätään ominaisuus, jossa lomakkeeseen lisätyt tiedot lisätään tietoihin
    
    lom[1].action = "pohja.html";
    
    buttonLisaaJoukkue.addEventListener("click", lisaaJoukkue);

    
    // Funktio lisää joukkueen data.sarjat.sarja[0].joukkueet taulukkoon, mikäli mikään input-kentissä on joukkueen nimi ja vähintään kaksi jäsentä
    function lisaaJoukkue() {
        if (kentat[3].value.length > 0 && kentat[4].value.length > 0 && kentat[5].value.length > 0) {
            

            // Luodaan joukkue tietorakenteeseen
            let uusiJoukkue = {
                "nimi": kentat[3].value,
                "jasenet": []
            };
            
            for (let i=4; i < kentat.length; i++) {
                if (kentat[i].value.length > 0) uusiJoukkue.jasenet.push(kentat[i].value);
            }
    
            data.sarjat[1].joukkueet.push(uusiJoukkue);   
            
            let i = data.sarjat[1].joukkueet.length;
            let uusi = data.sarjat[1].joukkueet[i-1];
            
            // Tyhjentää div elementin, että uusi data voidaan syöttää tilalle
            function clearBox() {
                var div = document.getElementById('tupa');
                while(div.firstChild) {
                    div.removeChild(div.firstChild);
                }
            }
            
            for (let i=3; i < kentat.length; i++) {
                kentat[i].value = "";
            }
            
            buttonLisaaJoukkue.disabled = true;
            
            clearBox();
            luoTulostaulukko();
        }
        
    }
}
    
    luoSivunSisalto();

}

