(function() {
    function drawer() {

    }

    drawer.prototype.install = function() {
        this.resizeAllBars();
        $(window).on('resize', this.resizeAllBars);
    };

    drawer.prototype.resizeAllBars = function() {
        Time.bars.forEach(Time.drawer.resizeBar);
    };

    drawer.prototype.resizeBar = function(bar) {
        bar.canvas.width = window.innerWidth - 2;
        Time.drawer.drawBar(bar);
    };

    drawer.prototype.drawShowBar = function(bar) {
        this.drawBar(bar);
        this.showBar(bar);
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

    drawer.prototype.hideBar = function(barIndex) {
        while (barIndex < Time.bars.length) {
            this.hideBarBar(Time.bars[barIndex]);
            barIndex++;
        }
    };

    drawer.prototype.hideBarBar = function(bar) {
        $(bar.canvas).fadeOut(100);
    };
    drawer.prototype.showBar = function(bar) {
        $(bar.canvas).fadeIn(100);
    };

    drawer.prototype.clearText = function() {
        Time.view.phrases.empty();
    };
    drawer.prototype.setPhrases = function(phrases, filter) {

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
        return ("<p date='" + phrase.date + "' page='" + phrase.pageUrl + "'>" + phrase.text + "</p>").replace(filter, '<strong>' + filter + '</strong>');
    };

    drawer.prototype.setPhraseTooltip = function(text) {
        Time.view.phrases.append("<h1>Il était une fois " + text + " . . .</h1>");
    };

    drawer.prototype.addTheEnd = function() {
        Time.view.phrases.append("<h1><center>The END<center></h1>");
    };

    Time.Drawer = drawer;
})();