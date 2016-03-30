(function() {
    function phrases() {
        this.lastSearch = null;
        this.isSearching = false;
    }

    phrases.prototype.install = function() {
        Time.view.phrases.on('dblclick.Textes', this.onPhrasesDblClick);
        Time.view.window.on('scroll', $.proxy(this.scroll, this));
    };

    phrases.prototype.onPhrasesDblClick = function(event) {
        event.stopImmediatePropagation();
        if (window.getSelection()) {
            var term = window.getSelection().toString().trim();
            Time.filter.onFilter(term);
        }
    };

    phrases.prototype.loadPhrases = function(scale, bucket) {
        Time.view.throbber.show();
        Time.data.getPhrases(Time.filter.term, scale, bucket, bucket, null, $.proxy(this.onFirstPhrases, this, scale, bucket));
    };

    phrases.prototype.onFirstPhrases = function(a, b, phrases) {
        if (phrases.total > 0) {
            var bucket = {
                years : Time.scale.daysToYears(phrases.phraseList[0].date)
            };
            var text = Time.tooltips.getTooltipText(bucket);
            Time.phrasesdrawer.addTextIntro(text, phrases.total);
        }
        this.onPhrases(null, null, phrases);
    };

    phrases.prototype.onPhrases = function(scale, xBucket, phrases) {
        Time.view.throbber.hide();
        Time.phrasesdrawer.setPhrases(phrases, Time.filter.term);
        this.isSearching = false;
        if (phrases.lastKey) {
            this.lastSearch = {
                scale : scale,
                bucket : xBucket,
                lastKey : phrases.lastKey
            };
            this.maybeMorePhrases();
        } else {
            this.lastSearch = null;
            if(!phrases.alternatives){
                Time.phrasesdrawer.addTheEnd();
            }
        }
    };

    phrases.prototype.scroll = function() {
        this.maybeMorePhrases();
        if (Time.view.window.scrollTop() < 20) {
            Time.tooltips.showTooltips();
        } else {
            Time.tooltips.hideTooltips();
        }
    };

    phrases.prototype.maybeMorePhrases = function() {
        if (!this.isSearching && this.lastSearch && this.isBottomVisible()) {
            this.isSearching = true;
            Time.view.throbber.show();
            Time.data.getPhrases(Time.filter.term, this.lastSearch.scale, this.lastSearch.bucket, this.lastSearch.bucket, this.lastSearch.lastKey, $.proxy(this.onPhrases, this, this.lastSearch.scale, this.lastSearch.bucket));
        }
    };

    phrases.prototype.isBottomVisible = function() {
        var docViewBottom = Time.view.window.scrollTop() + Time.view.window.height();
        var elemBottom = Time.view.bottom.offset().top + Time.view.bottom.height();
        return (elemBottom - 500) <= docViewBottom;
    };

    phrases.prototype.clearText = function() {
        Time.view.phrases.empty();
        $(window).scrollTop(0);
    };

    Time.Phrases = phrases;
})();