(function() {
    function timeline() {
        Time.view.throbber.hide();
        Time.bars = [ new Time.Bar(Scale.TEN9), new Time.Bar(Scale.TEN6), new Time.Bar(Scale.TEN3), new Time.Bar(Scale.TEN) ];
        Time.drawer = new Time.Drawer();
        Time.mouse = new Time.Mouse();
        Time.phrases = new Time.Phrases();
        Time.data = new Time.Data();
        Time.bucketFactory = new Time.BucketFactory();
        Time.filter = new Time.Filter();

        // BARMOUSE
        var onBucketSelectCall = $.proxy(this.onBucketSelect, this);
        Time.bars.forEach(function(bar) {
            Time.mouse.installBar(bar, onBucketSelectCall);
        }, this);

        // INIT BAR 0
        Time.drawer.hideBar(0);
        Time.drawer.install();
        Time.filter.install();
        Time.phrases.install();
    }

    timeline.prototype.onBucketSelect = function(bucket, bar) {
        Time.drawer.hideBar(Time.bars.indexOf(bar) + 1);
        Time.drawer.clearText();
        // affiche les phrases
        if (bucket.count < 50 || bar.scale === Scale.TEN) {
            Time.drawer.setPhraseTooltip(Scale.getTooltipText(bar.scale, bucket.x));
            Time.phrases.loadPhrases(bar.scale, bucket.x);
            // niveau de detail++
        } else {
            var subBar = Time.bars[Time.bars.indexOf(bar) + 1];
            var parentBucket = bucket.bucket;
            subBar.loadBuckets(Time.filter.term, parentBucket);
        }
    };

    Time.Timeline = timeline;
})();
