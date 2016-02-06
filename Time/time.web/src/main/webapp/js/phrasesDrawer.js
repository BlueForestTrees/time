(function(){
    
    function phrasesDrawer(){
        
    }
    
    phrasesDrawer.prototype.setPhrases = function(phrases, term) {
        if (phrases.total === 0) {
            this.addNoPhrases(term, phrases);
            return;
        }

        var prevOne = Time.view.phrases.children().last();
        var phraseOne = phrases.phraseList[0];
        if (!prevOne || phraseOne.text !== prevOne.text) {
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

    phrasesDrawer.prototype.buildHtmlPhrase = function(phrase) {
        return ("<p date='" + phrase.date + "' page='" + phrase.pageUrl + "'>" + phrase.text + this.getPhraseHeader(phrase) + this.getLink(phrase) + "</p>");
    };

    phrasesDrawer.prototype.getPhraseHeader = function(phrase) {
        var source = Time.sources[phrase.pageUrl];
        if (source) {
            return "";
        } else {
            return " <i>(" + this.getPageName(phrase) + ")</i>";
        }
    };
    
    phrasesDrawer.prototype.getLink = function(phrase) {
        var source = Time.sources[phrase.pageUrl];
        if (source) {
            return this.getBookLink(source);
        } else {
            return this.getWikiLink(phrase);
        }
    };

    phrasesDrawer.prototype.getBookLink = function(source) {
        var title = source.title;
        var url = source.url;
        var tiptext = "source livre : " + title;
        return "<a title=\"" + tiptext + "\" href=\"" + url + "\" onClick=\"Time.drawer.link('" + title + "')\" target=\"_blank\"><img src=\"http://www.ecoagris.org/AjaxControls/KoolTreeView/icons/book.gif\" /></a>";
    };

    phrasesDrawer.prototype.getWikiLink = function(phrase) {
        var pageName = this.getPageName(phrase);
        var tiptext = "source wikipedia : " + pageName;
        var url = "https://fr.wikipedia.org/wiki" + phrase.pageUrl;
        return "<a title=\"" + tiptext + "\" href=\"" + url + "\" onClick=\"Time.drawer.link('" + pageName + "')\" target=\"_blank\"><img src=\"http://upload.wikimedia.org/wikipedia/commons/6/64/Icon_External_Link.png\" /></a>";
    };
    
    phrasesDrawer.prototype.getPageName = function(phrase){
        return decodeURIComponent(phrase.pageUrl).replace(/_/g, " ").substring(1);    
    };

    phrasesDrawer.prototype.link = function(pageName) {
        Time.anal.ga('send', 'event', 'link', pageName, Time.filter.term);
    };

    phrasesDrawer.prototype.addTextIntro = function(text, nbPhrases) {
        Time.view.phrases.append("<h1>Il était une fois " + this.firstToLowerCase(text) + " . . .</h1>");
        Time.view.phrases.append("<i>" + Time.tooltips.getNbPages(nbPhrases) + "</i>");
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