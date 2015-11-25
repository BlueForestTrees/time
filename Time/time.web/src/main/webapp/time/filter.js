(function() {
    function filter() {

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

        }
    };

    filter.prototype.onFiltreEnter = function() {
        this.onFilter(Time.view.termInput.val());
    };

    filter.prototype.onFilter = function(term) {
        Time.drawer.hideBar(0);
        var bar = Time.bars[0];
        bar.loadBuckets(term);
        Time.drawer.clearText();
    };

    Time.Filter = filter;
})();