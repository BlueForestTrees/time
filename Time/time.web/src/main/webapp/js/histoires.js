(function() {
    function histoires() {
        Time.scale = new Time.Scale();
        Time.view.throbber.hide();
        Time.barFactory = new Time.BarFactory();
        Time.bars = this.buildBars();
        Time.barLoading = new Time.BarLoading();
        Time.barDrawer = new Time.BarDrawer();
        Time.phrases = new Time.Phrases();
        Time.phrasesdrawer = new Time.PhrasesDrawer();
        Time.data = new Time.Data();
        Time.filter = new Time.Filter();
        Time.tooltips = new Time.Tooltip();

        Time.barDrawer.install();
        Time.filter.install(Time.view);
        Time.phrases.install();

        Time.historic.popState();
        window.onpopstate = Time.historic.popState;
    }

    histoires.prototype.buildBars = function() {
        var bars = [], i = 0;
        while(i < Time.scale.scaleCount) {
            bars.push(new Time.Bar(i));
            i++;
        }
        return bars;
    };

    Time.Histoires = histoires;

})();
