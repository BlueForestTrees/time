(function() {
    function filter() {
        this.term = null;
    }

    filter.prototype.install = function() {
        Time.view.termInput.on("click", function() {
            Time.view.termInput.select();
        });
        Time.view.termInput.keypress($.proxy(this.termInputKeyPress, this));
    };

    filter.prototype.termInputKeyPress = function(e) {
        if (e.which === 13) {
            this.termInputKeyEnterPress();
        } else {
            this.checkNeedSynonyms();
        }
    };

    filter.prototype.checkNeedSynonyms = function() {
        var saisie = Time.view.termInput.val();
        var term = saisie.trim();
        var isTwoSpace = saisie.endsWith('  ');
        var isOneWord = !term.includes(' ');

        if (isTwoSpace && isOneWord) {
            Time.data.getSynonyms(term, $.proxy(this.onSynonyms, this));
        }
    };

    filter.prototype.onSynonyms = function(synonyms) {
        var saisie = Time.view.termInput.val().trim();
        var newSaisie = saisie + ' ' + synonyms.join(' ');
        Time.view.termInput.val(newSaisie);
        Time.view.termInput[0].selectionStart = saisie.length;
        Time.view.termInput[0].selectionEnd = newSaisie.length;
    };

    filter.prototype.termInputKeyEnterPress = function() {
        var term = Time.view.termInput.val();
        this.onFilter(term);
    };

    filter.prototype.onFilter = function(term) {
        this.term = term;
        ga('send', 'pageview','/'+term.replace(/ /g,"_"));
        Time.view.termInput.val(term);
        Time.phrases.clearText();
        Time.drawer.hideBar(0);
        Time.bars[0].loadBuckets(this.term);
        Time.phrases.lastSearch = null;//pour arrêter l'infinite scroll
        Time.phrases.loadFirstPhrases();
    };

    Time.Filter = filter;
})();