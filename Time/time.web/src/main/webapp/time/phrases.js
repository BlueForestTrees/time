(function() {
    function phrases() {
        this.lastSearch = null;
        this.isSearching = false;
    }

    phrases.prototype.install = function() {
        Time.view.phrases.on('dblclick.Textes', this.onPhrasesDblClick);
        Time.view.window.on('scroll', $.proxy(this.maybeMorePhrases, this));
    };

    phrases.prototype.onPhrasesDblClick = function(event) {
        event.stopImmediatePropagation();
        if (window.getSelection()) {
            var term = window.getSelection().toString().trim();
            Time.view.termInput.val(term);
            Time.filter.onFilter(term);
        }
    };

    phrases.prototype.loadPhrases = function(scale, bucket, term) {
        Time.data.getPhrases(term, scale, bucket, null, $.proxy(this.onPhrases, this, scale, bucket));
    };

    phrases.prototype.maybeMorePhrases = function() {
        if (!this.isSearching && this.lastSearch && this.isBottomVisible()) {
            this.isSearching = true;
            Time.data.getPhrases(this.lastSearch.term, this.lastSearch.scale, this.lastSearch.bucket, this.lastSearch.lastKey, $.proxy(this.onPhrases, this, this.lastSearch.scale, this.lastSearch.bucket));
        }
    };

    phrases.prototype.onPhrases = function(scale, xBucket, phrases) {
        Time.drawer.setPhrases(phrases, this.term);
        this.isSearching = false;
        if (phrases.lastKey) {
            this.lastSearch = {
                term : this.term,
                scale : scale,
                bucket : xBucket,
                lastKey : phrases.lastKey
            };
            this.maybeMorePhrases();
        } else {
            this.lastSearch = null;
        }
    };

    phrases.prototype.isBottomVisible = function() {
        var docViewBottom = Time.view.window.scrollTop() + Time.view.window.height();
        var elemBottom = Time.view.bottom.offset().top + Time.view.bottom.height();
        return (elemBottom - 500) <= docViewBottom;
    };

    Time.Phrases = phrases;
})();