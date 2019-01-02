"use strict";
$(document).ready(function() {
    
    // Luodaan kartta
    var div = $("#map");
	div.css("width", Math.round((window.innerWidth)/2) + "px");
	div.css("height", Math.round((window.innerHeight)) + "px");
    
    let mymap = new L.map('map', {
     crs: L.TileLayer.MML.get3067Proj()
    }).setView([62.2333, 25.7333], 11);
    L.tileLayer.mml_wmts({ layer: "maastokartta" }).addTo(mymap);
    
    // Piirretään rastit
    function piirraRastit() {
        let points = [];
        let rastikoodit = [];
        let marker;

        for (let rasti of data.rastit) {
            let lon = rasti.lon;
            let lat = rasti.lat;
            
            let koodi = rasti.koodi;

            let circle = L.circle(
                [lat, lon], {
                    color: 'red',
                    fillOpacity: 0,
                    radius: 150,
                }
            ).addTo(mymap).on("click", valitseYmpyra);
            
            let tulos = circle._mRadius;
            
            //circle.bindTooltip(koodi).openTooltip();
            
            //let muutettuLon = 200/((40075000*Math.cos(lat))/360);

            let kohta = L.latLng(lat,lon).toBounds(400);
            let merkkilat = kohta._northEast.lat;  
            let rastikoodi = new L.Marker([merkkilat, lon], {
                icon: new L.DivIcon({
                    className: 'my-div-icon',
                    html: "<span class='my-div-span'>" + koodi + "</span>"
                })
            }).addTo(mymap);
            rastikoodit.push(rastikoodi);
            
            //circle.bindTooltip(koodi, {permanent: true, offset: L.point({x: 0, y: 0})}).openTooltip(); //// TODO TÄSTÄ JATKUU!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            
            // Funktio joka käsittelee ympyrän valitsemisen
            function valitseYmpyra() {
                
                for(let rasti of points) {
                    if (rasti.options.fillOpacity===1) {
                        mymap.removeLayer(rasti);
                        rasti.options.fillOpacity = 0.0;
                        rasti.addTo(mymap);
                        mymap.removeLayer(marker);
                    }
                }

                mymap.removeLayer(this);
                this.options.fillOpacity = 1.0;
                this.addTo(mymap);
                
                marker = L.marker([this._latlng.lat, this._latlng.lng], {draggable:'true'}).addTo(mymap).on("click", poistaMerkki);
                
                // Poistaa markkeria klikatessa markkerin, sekä siirtää rastin markkerin osoittamiin koordinaatteihin
                function poistaMerkki() {
                    mymap.removeLayer(this);
                    let lisataan = false;
                    
                    for(let rasti of points) {
                        if (rasti.options.fillOpacity===1) {
                            mymap.removeLayer(rasti);
                            rasti.options.fillOpacity = 0.0;
                            
                            for (let koodi of rastikoodit) {
                                if (parseFloat(rasti._latlng.lng)===parseFloat(koodi._latlng.lng)) mymap.removeLayer(koodi);
                                
                            }
                            // Tehdään muutos tietokantaan
                            for (let dataRasti of data.rastit) {
                                let uusi;
                                if (parseFloat(rasti._latlng.lat)===parseFloat(dataRasti.lat) && parseFloat(rasti._latlng.lng)===parseFloat(dataRasti.lon)){
                                    lisataan = true;
                                    for (let a of polylines) {
                                        let count = 0;
                                        for (let i=0; i<a[0]._latlngs.length; i++) {
                                            if (parseFloat(a[0]._latlngs[i].lat)===parseFloat(dataRasti.lat) && parseFloat(a[0]._latlngs[i].lng)===parseFloat(dataRasti.lon)) {
                                                mymap.removeLayer(a[0]);
                                                a[0]._latlngs[i].lat = this._latlng.lat;
                                                a[0]._latlngs[i].lng = this._latlng.lng;
                                                
                                                uusi = a[0];
                                                uusi.addTo(mymap);
                                                count++;
                                            }
                                        }
                                    }
                                    if (lisataan) {
                                        
                                        let kohta = L.latLng(this._latlng.lat, this._latlng.lng).toBounds(400);
                                        let merkkilat = kohta._northEast.lat;
                                        
                                        for (let koodi of rastikoodit) {
                                            if (parseFloat(dataRasti.lon)===parseFloat(koodi._latlng.lng)) {
                                                koodi._latlng.lng = this._latlng.lng;
                                                koodi._latlng.lat = merkkilat;
                                                koodi.addTo(mymap);
                                                break;
                                            }
                                        }
                                                                            
                                        dataRasti.lat = this._latlng.lat;
                                        dataRasti.lon = this._latlng.lng;
                                        rasti._latlng.lat = this._latlng.lat;
                                        rasti._latlng.lng = this._latlng.lng;
                                        rasti.addTo(mymap);
                                        }
                                }
                            }
                            rasti.addTo(mymap);
                            mymap.removeLayer(marker);
                            break;
                        }
                        
                        lisataan = false;
                    }
                }
            }
            
            points.push(circle);
        } 
        let ympyrat = new L.featureGroup(points);
        mymap.fitBounds(ympyrat.getBounds());
        
    }
    piirraRastit();
    
    // Taulukko joka pitää listaa polylineistä
    let polylines = [];
    
    // Lisätään joukkueet
    function listaaJoukkueet() {
        let i = 1;

        for (let joukkue of data.joukkueet) {
            let matka = 0;
            
            // Selvitetään joukkueen kulkema matka
            function laskeMatka() {
                
                let reitti = [];
                let matka = 0;
                
                for (let rasti of joukkue.rastit) {
                    let id = parseInt(rasti.id);
                    
                    for (let rasti of data.rastit) {
                        if (id===parseInt(rasti.id)) {
                            let koord = [rasti.lat, rasti.lon];
                            reitti.push(koord);
                            break;
                        }
                    }
                }
                
                if (reitti.length > 2) {
                    for (let i=1; i<reitti.length; i++) {
                        
                        if (i===1) {
                        let a1 = parseFloat(reitti[0][0]);
                        let b1 = parseFloat(reitti[0][1]);
                        let a2 = parseFloat(reitti[1][0]);
                        let b2 = parseFloat(reitti[1][1]);
                        matka += getDistanceFromLatLonInKm(a1, b1, a2, b2);
                        } else {
                            let a1 = parseFloat(reitti[i-1][0]);
                            let b1 = parseFloat(reitti[i-1][1]);
                            let a2 = parseFloat(reitti[i][0]);
                            let b2 = parseFloat(reitti[i][1]);
                            matka += getDistanceFromLatLonInKm(a1, b1, a2, b2);
                        }
                        
                        // Funktio joka laskee kahden pisteen välisen etäisyyden
                        function getDistanceFromLatLonInKm(lat1,lon1,lat2,lon2) {
                            var R = 6371; // Radius of the earth in km
                            var dLat = deg2rad(lat2-lat1);  // deg2rad below
                            var dLon = deg2rad(lon2-lon1); 
                            var a = 
                                Math.sin(dLat/2) * Math.sin(dLat/2) +
                                Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * 
                                Math.sin(dLon/2) * Math.sin(dLon/2)
                            ; 
                            var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
                            var d = R * c; // Distance in km
                            return d;
                        }
                        
                        function deg2rad(deg) {
                            return deg * (Math.PI/180)
                        }
                    }
                }
                return matka;
            }
            
            // Pyöristää luvun haluttuun desimaaliin
            function pyorista(numero, decimal) {
                return Math.round(numero * Math.pow(10, decimal)) / Math.pow(10, decimal);
            }
            
            
            let jouk = joukkue.nimi;
            let rivi = document.createElement("li");
            rivi.innerHTML = jouk + " (" + pyorista(laskeMatka(), 3) + "km)";
            $("#jouklist").append(rivi);
            rivi.style.backgroundColor = rainbow(data.joukkueet.length, i);
            rivi.setAttribute("draggable", "true");
            rivi.setAttribute("id", jouk);
            rivi.addEventListener("dragstart", function(e) {
                // raahataan datana elementin id-attribuutin arvo
                e.dataTransfer.setData("text/plain", rivi.getAttribute("id"));
                event.dataTransfer.effectAllowed = "move";
            });
            i++;
        }
        
        // Lisätään Drag&Drop kartalla-listaukseen
        let drop1 = document.getElementById("kartlist");
        drop1.addEventListener("dragover", function(e) {
            e.preventDefault();
            // Set the dropEffect to move
            e.dataTransfer.dropEffect = "move"
        });
        
        // Taulukko joka pitää listaa polylineistä
        // let polylines = [];
        
        drop1.addEventListener("drop", function(e) {
            e.preventDefault();
            
            var dataS = e.dataTransfer.getData("text");
            // lisätään tämän elementin sisään
            e.target.insertBefore(document.getElementById(dataS), drop1.firstChild);
                     
            // Luodaan karttareitit
            for (let joukkue of data.joukkueet) {
                if (dataS===joukkue.nimi) {
                let reitti = [];
                let matka = 0;
                
                for (let rasti of joukkue.rastit) {
                    let id = parseInt(rasti.id);
                    
                    for (let rasti of data.rastit) {
                        if (id===parseInt(rasti.id)) {
                            let koord = [rasti.lat, rasti.lon];
                            reitti.push(koord);
                            break;
                        }
                    }
                }
                let vari = document.getElementById(dataS);
                let polyline = L.polyline(reitti, {color: vari.style.backgroundColor}).addTo(mymap);
                
                polylines.push([polyline, dataS]);    
                }
            }
    
        });
        
        // Lisätään Drag&Drop joukkueet-listaukseen
        let drop2 = document.getElementById("jouklist");
        drop2.addEventListener("dragover", function(e) {
            
            e.preventDefault();
            // Set the dropEffect to move
            e.dataTransfer.dropEffect = "move"
            
        });
        
        drop2.addEventListener("drop", function(e) {
            e.preventDefault();
            
            var dataS = e.dataTransfer.getData("text");
            // lisätään tämän elementin sisään
            drop2.appendChild(document.getElementById(dataS));
            
            let count = 0;
            
            for (let pol of polylines) {
                if (pol[1]===dataS) {
                    pol[0].remove();
                    
                    break;
                }
                count++;
            }
            
            let uusipolylines = [];
            for (let i=0; i<polylines.length;i++) {
                if (i!==count) uusipolylines.push(polylines[i]);
            }
            polylines = uusipolylines;
            
            // polylines = polylines.splice(count,0);  // Miksei tämä toimi TODO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                
        });
        
        // Funktio värien generointiin
        function rainbow(numOfSteps, step) {
            // This function generates vibrant, "evenly spaced" colours (i.e. no clustering). This is ideal for creating easily distinguishable vibrant markers in Google Maps and other apps.
            // Adam Cole, 2011-Sept-14
            // HSV to RBG adapted from: http://mjijackson.com/2008/02/rgb-to-hsl-and-rgb-to-hsv-color-model-conversion-algorithms-in-javascript
            let r, g, b;
            let h = step / numOfSteps;
            let i = ~~(h * 6);
            let f = h * 6 - i;
            let q = 1 - f;
            switch(i % 6){
                case 0: r = 1; g = f; b = 0; break;
                case 1: r = q; g = 1; b = 0; break;
                case 2: r = 0; g = 1; b = f; break;
                case 3: r = 0; g = q; b = 1; break;
                case 4: r = f; g = 0; b = 1; break;
                case 5: r = 1; g = 0; b = q; break;
            }
            let c = "#" + ("00" + (~ ~(r * 255)).toString(16)).slice(-2) + ("00" + (~ ~(g * 255)).toString(16)).slice(-2) + ("00" + (~ ~(b * 255)).toString(16)).slice(-2);
            return (c);
        }   
    }
    listaaJoukkueet();
});