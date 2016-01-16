(function() {
    function drawer() {
        this.scaleheight = 0.3;
        this.fillLevel = 251;
    }

    drawer.prototype.install = function() {
        this.hideBarsAfter(Time.bars[0]);
        this.resizeAllBars();
        $(window).on('resize', this.resizeAllBars);
    };

    
    
    drawer.prototype.reduceBarsBefore = function(bar) {
        var previousBar = Time.scale.previous(bar);
        while (previousBar !== null) {
            $(previousBar.canvas).css({
                height : previousBar.reducedHeight
            });
            previousBar = Time.scale.previous(previousBar);
        }
    };

    drawer.prototype.unreduceBar = function(bar) {
        $(bar.canvas).css({
            height : bar.height
        });
    };

    drawer.prototype.resizeAllBars = function() {
        Time.bars.forEach(Time.drawer.resizeBar);
    };

    drawer.prototype.resizeBar = function(bar) {
        bar.canvas.width = window.innerWidth - 2;
        bar.viewport.global = bar.canvas.width * 0.7;
        Time.drawer.drawBar(bar);
        Time.tooltips.updateTooltips();
    };

    drawer.prototype.focusOn = function(bar) {
        this.hideBarsAfter(bar);
        this.reduceBarsBefore(bar);
        this.showBar(bar);
        this.unreduceBar(bar);
    };

    drawer.prototype.drawBar = function(bar, explicitBuckets) {
        var buckets = explicitBuckets ? explicitBuckets : bar.buckets;
        bar.context.fillStyle = 'rgb('+this.fillLevel+','+this.fillLevel+','+this.fillLevel+')';
        bar.context.fillRect(0, 0, bar.canvas.width, bar.canvas.height);
        var nbBuckets = buckets.length;
        for (var i = 0; i < nbBuckets; i++) {
            var bucket = buckets[i];
            bar.context.fillStyle = bucket.color;
            bar.context.fillRect(bar.viewport.delta() + bucket.x, 0, 1, bar.canvas.height);
        }
    };

    drawer.prototype.hideBar = function(bar){
        $(bar.canvas).hide();
    };

    drawer.prototype.hideBarsAfter = function(bar) {
        var scale = bar.scale;
        while (scale < Time.bars.length) {
            this.hideBar(Time.bars[scale]);
            scale++;
        }
    };

    drawer.prototype.showBar = function(bar) {
        $(bar.canvas).show();
    };

    drawer.prototype.setPhrases = function(phrases, term) {
        if (phrases.phraseList.length === 0) {
            this.addNoPhrases(term);
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

    drawer.prototype.buildHtmlPhrase = function(phrase) {
        return ("<p date='" + phrase.date + "' page='" + phrase.pageUrl + "'>" + phrase.text + this.getLink(phrase) + "</p>");
    };

    drawer.prototype.getLink = function(phrase) {
        var source = Time.sources[phrase.pageUrl];
        if (source) {
            return this.getBookLink(source);
        } else {
            return this.getWikiLink(phrase);
        }
    };

    drawer.prototype.getBookLink = function(source) {
        var title = source.title;
        var url = source.url;
        var tiptext = "source livre : " + title;
        return "<a title=\"" + tiptext + "\" href=\"" + url + "\" onClick=\"Time.drawer.link('" + title + "')\" target=\"_blank\"><img src=\"http://www.ecoagris.org/AjaxControls/KoolTreeView/icons/book.gif\" /></a>";
    };

    drawer.prototype.getWikiLink = function(phrase) {
        var pageName = decodeURIComponent(phrase.pageUrl).replace(/_/g, " ").substring(1);
        var tiptext = "source wikipedia : " + pageName;
        var url = "https://fr.wikipedia.org/wiki" + phrase.pageUrl;
        return "<a title=\"" + tiptext + "\" href=\"" + url + "\" onClick=\"Time.drawer.link('" + pageName + "')\" target=\"_blank\"><img src=\"http://upload.wikimedia.org/wikipedia/commons/6/64/Icon_External_Link.png\" /></a>";
    };

    drawer.prototype.link = function(pageName) {
        Time.anal.ga('send', 'event', 'link', pageName, Time.filter.term);
    };

    drawer.prototype.setPhraseTooltip = function(text, nbPhrases) {
        Time.view.phrases.append("<h1>Il était une fois " + text + " . . .</h1><i>" + Time.tooltips.getNbPages(nbPhrases) + "</i>");
    };

    drawer.prototype.addTheEnd = function() {
        Time.view.phrases.append("<h1 style=\"text-align:center\">The END</h1>");
    };
    drawer.prototype.addNoPhrases = function(term) {
        Time.view.phrases.append("<h1 style=\"text-align:center\">Aucun résultat pour <i>" + term + "</i></h1>");
    };

    Time.Drawer = drawer;
})();