(function() {
    function timeline() {
        this.bars = [ new Time.Bar(Scale.TEN9), new Time.Bar(Scale.TEN6), new Time.Bar(Scale.TEN3), new Time.Bar(Scale.TEN) ];
        this.drawer = new Time.BarDrawer(this.bars);
        this.mouse = new Time.Mouse(this.drawer);
        this.data = new Time.Data();
        this.bucketFactory = new Time.BucketFactory();
        this.filter = '';

        // DRAWER
        this.bars.forEach(function(bar) {
            this.drawer.resize(bar);
        }, this);

        $(window).resize($.proxy(this.onWindowResize, this));

        // BARMOUSE
        this.mouse.install($.proxy(this.onFilterSelect, this));
        var onBucketSelectCall = $.proxy(this.onBucketSelect, this);
        this.bars.forEach(function(bar) {
            this.mouse.installBar(bar, onBucketSelectCall);
        }, this);

        // INIT BAR 0
        var topbar = this.bars[0];
        this.data.getBuckets(this.filter, topbar.scale, null, $.proxy(this.onBuckets, this, topbar));
        this.drawer.hide(1);

        // FILTRE

        $("#filtreInput").keypress($.proxy(this.onFiltreKeyPress, this));
        $("input[type='text']").on("click", function() {
            $(this).select();
        });
    }

    timeline.prototype.onFiltreKeyPress = function(e) {
        if (e.which === 13) {
            this.onFiltreEnter();
        }
    };

    timeline.prototype.onFiltreEnter = function() {
        this.filter = $("#filtreInput").val();
        this.onFilter();
    };

    timeline.prototype.onBucketSelect = function(bucket, bar) {
        this.drawer.hide(Scale.sub(bar.scale));
        this.drawer.clearText();
        // affiche les phrases
        if (bucket.count < 50 || bar.scale === Scale.TEN) {
            this.data.getPhrases(this.filter, bar.scale, bucket.x, null, $.proxy(this.onPhrases, this, bar.scale, bucket.x));
            // niveau de detail++
        } else {
            var subBar = this.bars[this.bars.indexOf(bar) + 1];
            this.data.getBuckets(this.filter, subBar.scale, bucket.bucket, $.proxy(this.onBuckets, this, subBar));
        }
    };

    timeline.prototype.onBuckets = function(bar, bucketsDTO) {
        bar.viewport.local = -Scale.firstSubBucket(Scale.up(bar.scale), bucketsDTO.parentBucket);
        bar.buckets = this.bucketFactory.getBuckets(bucketsDTO);
        this.drawer.showBar(bar);
        this.drawer.draw(bar);
    };

    timeline.prototype.onPhrases = function(scale, xBucket, phrases) {
        this.drawer.setPhrases(phrases, this.filter);
        if(phrases.lastScoreDoc){
            this.data.getPhrases(this.filter, scale, xBucket, phrases.lastScoreDoc, $.proxy(this.onPhrases, this, scale, xBucket));
        }
    };

    timeline.prototype.onFilterSelect = function(filter) {
        this.filter = filter;
        $("#filtreInput").val(this.filter);
        this.onFilter();
    };

    timeline.prototype.onFilter = function() {
        var bar = this.bars[0];
        this.data.getFacets(this.filter, bar.scale, null, $.proxy(this.onBuckets, this, bar));
        this.drawer.hide(Scale.sub(bar.scale));
        this.drawer.clearText();
    };

    timeline.prototype.onWindowResize = function() {
        this.bars.forEach(function(bar) {
            this.drawer.resize(bar);
        }, this);
    };

    Time.Timeline = timeline;
})();
