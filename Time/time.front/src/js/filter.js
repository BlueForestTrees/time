(function() {
    function filter() {
        this.term = null;
    }

    filter.prototype.install = function(view) {
        view.termInput.on("keyup",$.proxy(this.termInputKeyPress, this));
        view.homeTermInput.on("keyup",$.proxy(this.homeTermInputKeyPress, this));
    };

//HOME
    filter.prototype.homeTermInputKeyPress = function(e) {
        if (e.which === 13) {
            this.homeTermInputKeyEnterPress();
        }
    };

    filter.prototype.homeTermInputKeyEnterPress = function() {
        var term = Time.view.homeTermInput.val();
        this.onFilterFromHome(term, false);
    };

    filter.prototype.onFilterFromHome = function(term, ignoreHistory){
        Time.view.homeTermInput.off("keyup");
        Time.view.home.remove();
        Time.view.content.show();
        delete filter.prototype.homeTermInputKeyEnterPress;
        delete filter.prototype.homeTermInputKeyPress;
        delete filter.prototype.onFilterFromHome;
        filter.prototype.onFilterFromHome = this.onFilter;
        delete Time.view.homeTermInput;
        delete Time.view.home;

        this.onFilter(term, ignoreHistory);
    };
//!HOME

    filter.prototype.termInputKeyPress = function(e) {
        if (e.which === 13) {
            this.termInputKeyEnterPress();
        } else {
            this.checkGetSynonymsTrigger();
        }
    };


    filter.prototype.checkGetSynonymsTrigger = function() {
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

    filter.prototype.onFilter = function(term, ignoreHistory) {
        this.term = term;
        Time.anal.ga('send', 'event', 'search', term);
        Time.view.termInput.val(term);
        Time.phrases.clearText();
        Time.bar.loadBuckets(this.term);
        // pour arrêter l'infinite scroll d'une recherche précédente
        Time.phrases.lastSearch = null;
        Time.phrases.loadPhrases();

        if (!ignoreHistory) {
            Time.historic.pushState(term);
        }
    };

    filter.prototype.onPeriodFilter = function (leftFilter, rightFilter) {
        //TODO
        console.log("select from ", leftFilter, " to ", rightFilter);
    };

    Time.Filter = filter;
})();