(function() {
    function filter() {
        this.term = null;
    }

    filter.prototype.install = function() {
        Time.view.termInput.on("click", function() {
            Time.view.termInput.select();
        });
        Time.view.termInput.keypress($.proxy(this.onFiltreKeyPress, this));
    };

    filter.prototype.onFiltreKeyPress = function(e) {
        if (e.which === 13) {
            this.onFiltreEnter();
        } else {
            this.checkNeedSynonyms();
        }
    };

    filter.prototype.checkNeedSynonyms = function() {
        var saisie = Time.view.termInput.val();
        var term = saisie.trim();
        var isTwoSpace = saisie.endsWith(' ');
        var isOneWord = !term.includes(' ');

        if (isTwoSpace && isOneWord) {
            Time.data.getSynonyms(term, $.proxy(this.onSynonyms, this));
        }
    };

    filter.prototype.onSynonyms = function(synonyms) {
        var saisie = Time.view.termInput.val().trim();
        var newSaisie = saisie + ' ' + synonyms.join(' ');
        Time.view.termInput.val(newSaisie);
        //TODO menu deroulant proposant les synonymes
    };

    filter.prototype.onFiltreEnter = function() {
        this.term = Time.view.termInput.val();
        this.onFilter();
    };

    filter.prototype.onFilter = function() {
        Time.drawer.hideBar(0);
        var bar = Time.bars[0];
        bar.loadBuckets(this.term);
        Time.drawer.clearText();
    };

    Time.Filter = filter;
})();