(function() {
    function drawer() {
        this.scaleheight = 0.3;
    }

    drawer.prototype.install = function() {
        this.hideBarsAfter(0);
        this.resizeAllBars();
        $(window).on('resize', this.resizeAllBars);
    };

    drawer.prototype.reduceBarsBefore = function(bar) {
        var previousBar = Time.scale.previous(bar);
        while (previousBar !== null) {
            $(previousBar.canvas).css({height : previousBar.reducedHeight});
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
        Time.drawer.drawBar(bar);
    };

    drawer.prototype.focusOn = function(bar) {
        this.reduceBarsBefore(bar);
        this.drawBar(bar);
        this.showBar(bar);
        this.unreduceBar(bar);
    };

    drawer.prototype.drawBar = function(bar) {
        bar.context.fillStyle = 'rgb(255,255,255)';
        bar.context.fillRect(0, 0, bar.canvas.width, bar.canvas.height);
        var nbBuckets = bar.buckets.length;
        for (var i = 0; i < nbBuckets; i++) {
            var bucket = bar.buckets[i];
            bar.context.fillStyle = bucket.color;
            bar.context.fillRect(bar.viewport.delta() + bucket.x, 0, 1, bar.canvas.height);
        }
    };

    drawer.prototype.hideBarsAfter = function(barIndex) {
        while (barIndex < Time.bars.length) {
            $(Time.bars[barIndex].canvas).hide();
            barIndex++;
        }
    };

    drawer.prototype.showBar = function(bar) {
        $(bar.canvas).show();
    };

    drawer.prototype.setPhrases = function(phrases, filter) {
        if (phrases.phraseList.length === 0) {
            this.addNoPhrases();
            return;
        }

        var prevOne = Time.view.phrases.children().last();
        var phraseOne = phrases.phraseList[0];
        if (!prevOne || phraseOne.text !== prevOne.text) {
            Time.view.phrases.append(this.buildHtmlPhrase(phraseOne, filter));
        }

        for (var i = 1; i < phrases.phraseList.length; i++) {
            var prev = phrases.phraseList[i - 1];
            var phrase = phrases.phraseList[i];
            if (phrase.text !== prev.text) {
                Time.view.phrases.append(this.buildHtmlPhrase(phrase, filter));
            }
        }
    };

    drawer.prototype.buildHtmlPhrase = function(phrase, filter) {
        var text = phrase.text.replace(filter, '<strong>' + filter + '</strong>');
        return ("<p date='" + phrase.date + "' page='" + phrase.pageUrl + "'>" + text + this.getLink(phrase) + "</p>");
    };

    drawer.prototype.getLink = function(phrase) {
        var pageName = decodeURIComponent(phrase.pageUrl).replace(/_/g, " ").substring(1);
        var tiptext = "source wikipedia : " + pageName;
        var url = "https://fr.wikipedia.org/wiki" + phrase.pageUrl;
        return "<a title=\"" + tiptext + "\" href=\"" + url + "\" onClick=\"Time.drawer.link('" + pageName + "')\" target=\"_blank\"><img src=\"http://upload.wikimedia.org/wikipedia/commons/6/64/Icon_External_Link.png\" /></a>";
    };

    drawer.prototype.link = function(pageName) {
        Time.anal.ga('send', 'event', 'link', pageName, Time.filter.term);
    };

    drawer.prototype.setPhraseTooltip = function(text) {
        Time.view.phrases.append("<h1>Il était une fois " + text + " . . .</h1>");
    };

    drawer.prototype.addTheEnd = function() {
        Time.view.phrases.append("<h1 style=\"text-align:center\">The END</h1>");
    };
    drawer.prototype.addNoPhrases = function() {
        Time.view.phrases.append("<h1 style=\"text-align:center\">Rien trouvé!</h1>");
    };

    Time.Drawer = drawer;
})();