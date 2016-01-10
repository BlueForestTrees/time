(function() {
    function timeline() {
        Time.scale = new Time.Scale();
        Time.view.throbber.hide();
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
        Time.tooltips.install(Time.bars);

        Time.historic.popState();
        window.onpopstate = Time.historic.popState;

    }

    timeline.prototype.buildBars = function() {
        var bars = [];
        for (var i = 0; i < Time.scale.scaleCount; i++) {
            bars.push(new Time.Bar(i));
        }
        return bars;
    };

    timeline.prototype.activateBar = function(bar) {
        this.activeBar = bar;
        Time.drawer.focusOn(bar);
        Time.tooltips.decorate(this.activeBar);
    };

    Time.Timeline = timeline;

})();
