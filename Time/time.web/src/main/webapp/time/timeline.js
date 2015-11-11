(function() {
    function timeline() {
        this.bars = [ new Time.Bar(Scale.TEN9), new Time.Bar(Scale.TEN6), new Time.Bar(Scale.TEN3), new Time.Bar(Scale.TEN) ];
        this.drawer = new Time.BarDrawer(this.bars);
        this.mouse = new Time.Mouse(this.drawer);
        this.data = new Time.Data();
        this.bucketFactory = new Time.BucketFactory();
        this.term = '';
        this.lastSearch = null;
        this.isSearching = false;

        // DRAWER
        this.bars.forEach(function(bar) {
            this.drawer.resize(bar);
        }, this);

        $(window).resize($.proxy(this.onWindowResize, this));

        // BARMOUSE
        this.mouse.install($.proxy(this.onFilterSelect, this), $.proxy(this.onScroll, this));
        var onBucketSelectCall = $.proxy(this.onBucketSelect, this);
        this.bars.forEach(function(bar) {
            this.mouse.installBar(bar, onBucketSelectCall);
        }, this);

        // INIT BAR 0
        var topbar = this.bars[0];
        this.data.getBuckets(this.term, topbar.scale, $.proxy(this.onBuckets, this, topbar, null));
        this.drawer.hide(1);

        // FILTRE

        $("#termInput").keypress($.proxy(this.onFiltreKeyPress, this));
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
        this.term = $("#termInput").val();
        this.onFilter();
    };

    timeline.prototype.onBucketSelect = function(bucket, bar) {
        this.drawer.hide(this.bars.indexOf(bar)+1);
        this.drawer.clearText();
        // affiche les phrases
        if (bucket.count < 50 || bar.scale === Scale.TEN) {
            this.data.getPhrases(this.term, bar.scale, bucket.x, null, $.proxy(this.onPhrases, this, bar.scale, bucket.x));
            // niveau de detail++
        } else {
            var subBar = this.bars[this.bars.indexOf(bar) + 1];
            var parentBucket= bucket.bucket;
            this.data.getBuckets(this.term, subBar.scale, $.proxy(this.onBuckets, this, subBar, parentBucket));
        }
    };

    timeline.prototype.onBuckets = function(bar, bucket, bucketsDTO) {
        bar.viewport.local = -Scale.firstSubBucket(Scale.up(bar.scale), bucket);
        bar.buckets = this.bucketFactory.getBuckets(bucketsDTO);
        this.drawer.showBar(bar);
        this.drawer.draw(bar);        
    };

    timeline.prototype.maybeMorePhrases = function(){
        if(!this.isSearching && this.lastSearch && this.isVisible($("#bottom"))){
            this.isSearching = true;
            this.data.getPhrases(this.lastSearch.term, this.lastSearch.scale, this.lastSearch.bucket, this.lastSearch.lastKey, $.proxy(this.onPhrases, this, this.lastSearch.scale, this.lastSearch.bucket));
        }
    };

    timeline.prototype.onPhrases = function(scale, xBucket, phrases) {
        this.drawer.setPhrases(phrases, this.term);
        this.isSearching = false;
        if(phrases.lastKey){
            this.lastSearch = {term : this.term, scale : scale, bucket : xBucket, lastKey : phrases.lastKey};
            this.maybeMorePhrases();
        }else{
            this.lastSearch = null;
        }
    };

    timeline.prototype.onFilterSelect = function(term) {
        this.term = term;
        $("#termInput").val(this.term);
        this.onFilter();
    };

    timeline.prototype.onScroll = function(){
        this.maybeMorePhrases();
    };

    timeline.prototype.onFilter = function() {
        this.drawer.hide(0);
        var bar = this.bars[0];
        this.data.getBuckets(this.term, bar.scale, $.proxy(this.onBuckets, this, bar, null));
        this.drawer.clearText();
    };

    timeline.prototype.onWindowResize = function() {
        this.bars.forEach(function(bar) {
            this.drawer.resize(bar);
        }, this);
    };

    timeline.prototype.isVisible = function(elem){
        var marge = 500;
        var $elem = $(elem);
        var $window = $(window);

        var docViewTop = $window.scrollTop();
        var docViewBottom = docViewTop + $window.height();

        var elemTop = $elem.offset().top;
        var elemBottom = elemTop + $elem.height();

        return (elemBottom-marge) <= docViewBottom;
    };

    Time.Timeline = timeline;
})();
