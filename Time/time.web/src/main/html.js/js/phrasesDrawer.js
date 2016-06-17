(function(){
    
    function phrasesDrawer(){

    }
    
    phrasesDrawer.prototype.setPhrases = function(phrases, term) {
        if (phrases.total === 0) {
            this.addNoPhrases(term, phrases);
            return;
        }

        var lastPhrase = Time.view.phrases.children().last();
        var phraseOne = phrases.phraseList[0];
        if (!lastPhrase || phraseOne.text !== lastPhrase.text) {
            Time.view.phrases.append(this.buildHtmlPhrase(phraseOne));
        }

        for (var i = 1; i < phrases.phraseList.length; i++) {
            var prev = phrases.phraseList[i - 1];
            var phrase = phrases.phraseList[i];
            if (phrase.text !== prev.text) {
                Time.view.phrases.append(this.buildHtmlPhrase(phrase));
            }
        }
    };

    function titleChanged(phrase) {
        var newTitle = phrase.title !== this.lastTitle;
        this.lastTitle = phrase.title;
        return newTitle;
    }

    phrasesDrawer.prototype.buildHtmlPhrase = function(phrase) {

        var source = Time.sources[phrase.type];
        phrase.pageName = source.getPageName(phrase);
        phrase.pageNameEscaped = phrase.pageName.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;").replace(/'/g, "&#039;");
        phrase.tipTextHeader = source.tipTextHeader;
        phrase.imgUrl = source.imgUrl;

        return ((titleChanged(phrase) ? "<div class='phraseHeader'><i>${title}</i></div>" : "") +
                    "<p date='${date}' page='${url}'>${text}" +
                        "<a title='${tipTextHeader} : ${pageNameEscaped} de ${author}' href='${url}' target='_blank' onClick='Time.drawer.link(${pageName})'>" +
                            "<img src='${imgUrl}'/>" +
                        "</a>" +
                    "</p>").replace(/\${[a-zA-Z]*}/g, function(v){return phrase[v.substring(2,v.length-1)];});
    };

    phrasesDrawer.prototype.inject = function(phrase, template){

    };
    
    phrasesDrawer.prototype.link = function(pageName) {
        Time.anal.ga('send', 'event', 'link', pageName, Time.filter.term);
    };

    phrasesDrawer.prototype.addTextIntro = function(moment, nbPhrases) {
        Time.view.phrases.append("<div class=\"textIntro\"><h1>Il était une fois " + this.firstToLowerCase(moment) + " . . .</h1>");
        Time.view.phrases.append("<i>" + Time.tooltips.getNbPhrases(nbPhrases) + "</i>" + "</div>");
    };

    phrasesDrawer.prototype.addNoPhrases = function(term, phrases) {
        if(phrases.alternatives == null || phrases.alternatives.length ===0){
            Time.view.phrases.append("<br><br><h2 style=\"text-align:center\">Aucun résultat pour <i>" + term + "</i></h2>");
        }else{
            var tryWith = "<br><br><h2 style=\"text-align:center\">0 résultats pour <i>" + term + "</i> :(<br><br>Essayez avec ces mots: ";

            phrases.alternatives.forEach(function(alternative){
                tryWith += "<i><a href='/"+alternative+"'>" + alternative + "</a></i>, ";
            });
            tryWith = tryWith.slice(0, -2);
            tryWith += "</h2>";
            Time.view.phrases.append(tryWith);
        }
    };
    
    phrasesDrawer.prototype.addTheEnd = function() {
        Time.view.phrases.append("<h1 style=\"text-align:center\">The END</h1>");
    };
    
    phrasesDrawer.prototype.firstToLowerCase = function( str ) {
        return str.substr(0, 1).toLowerCase() + str.substr(1);
    }

    Time.PhrasesDrawer = phrasesDrawer;
})();