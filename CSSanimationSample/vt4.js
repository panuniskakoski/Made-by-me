"use strict";

window.onload = function() {
    
    // Tässä luodaan taustalla heiluvat tolpat, määrää muuttamalla voidaan lisätä tai vähentää tolppien lukumäärää.
    function luoTolpat() {
        let taulu = document.getElementById("tolpat");
        let maara = 12;  // +1, joka on alkuperäinen
        let kesto = 0.15;
        let i = 0;
            
        while(i<maara) {    
            var tolppa = document.getElementById('tolppa');
            let clone = tolppa.cloneNode(true);
            let delay = kesto.toString() + "s";
            clone.style.animationDelay = delay;
            taulu.appendChild(clone);
            kesto += 0.15;
            i++;
        }
    }
    
    luoTolpat();
    
    // Luodaan kanien puoliskot
    function bunnyV() {
        let canvas = document.getElementById("canvasV");
        canvas.setAttribute("width", "383px");
        canvas.setAttribute("height", "600px");
        let img = document.getElementById("bunny");
        img.src = "bunny.png";
        let ctx = canvas.getContext("2d");
        ctx.drawImage(img, 0, 0, 191.5, 600, 0, 0, 191.5, 600);
    }
    
    bunnyV();
    
    function bunnyO() {
        let canvas = document.getElementById("canvas");
        canvas.setAttribute("width", "383px");
        canvas.setAttribute("height", "600px");
        let img = document.getElementById("bunny");
        img.src = "bunny.png";
        let ctx = canvas.getContext("2d");
        ctx.drawImage(img, 191.5, 0, 191.5, 600, 0, 0, 191.5, 600);
    }
    
    bunnyO();
}




