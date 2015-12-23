(function() {
    function timeline() {
        Time.view.throbber.hide();
        Time.bars = this.buildBars();
        Time.drawer = new Time.Drawer();
        Time.phrases = new Time.Phrases();
        Time.data = new Time.Data();
        Time.bucketFactory = new Time.BucketFactory();
        Time.filter = new Time.Filter();

        Time.drawer.install();
        Time.filter.install();
        Time.phrases.install();

        Time.history.popState();
        window.onpopstate = Time.history.popState;
    }

    timeline.prototype.buildBars = function() {
        var bars = [];
        for (var i = 0; i < Scale.scaleCount; i++) {
            bars.push(new Time.Bar(i));
        }
        return bars;
    };

    Time.Timeline = timeline;

})();
