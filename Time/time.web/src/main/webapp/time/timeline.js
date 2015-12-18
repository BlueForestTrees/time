(function() {
    function timeline() {
        Time.view.throbber.hide();
        Time.bars = [ new Time.Bar(Scale.TEN9), new Time.Bar(Scale.TEN6), new Time.Bar(Scale.TEN3), new Time.Bar(Scale.TEN) ];
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

    Time.Timeline = timeline;

})();
