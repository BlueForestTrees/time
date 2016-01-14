(function() {
    function histoires() {
        Time.scale = new Time.Scale();
        Time.view.throbber.hide();
        Time.barloading = new Time.Barloading();
        Time.bars = this.buildBars();
        Time.drawer = new Time.Drawer();
        Time.phrases = new Time.Phrases();
        Time.data = new Time.Data();
        Time.bucketFactory = new Time.BucketFactory();
        Time.filter = new Time.Filter();
        Time.tooltips = new Time.Tooltip();

        Time.drawer.install();
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
