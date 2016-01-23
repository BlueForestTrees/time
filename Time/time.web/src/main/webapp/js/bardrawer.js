(function() {
    function barDrawer() {
        this.fillLevel = 251;
    }

    barDrawer.prototype.install = function() {
        this.hideBarsAfter(Time.bars[0]);
        this.updateSizeAllBars();
        $(window).on('resize', this.updateSizeAllBars);
    };

    barDrawer.prototype.updateSizeAllBars = function() {
        Time.bars.forEach(Time.barDrawer.updateSizeBar);
    };

    barDrawer.prototype.updateSizeBar = function(bar) {
        bar.canvas.width = window.innerWidth - 2;
        bar.viewport.global = bar.canvas.width * 0.7;
        Time.barDrawer.drawBar(bar);
        Time.tooltips.updateTooltips();
    };

    barDrawer.prototype.focusOn = function(bar) {
        //this.hideBarsAfter(bar);
        this.reduceBarsBefore(bar);
        this.showBar(bar);
        this.unreduceBar(bar);
        this.drawBar(bar);
    };

    barDrawer.prototype.hideBarsAfter = function(bar) {
        var scale = bar.scale;
        while (scale < Time.bars.length) {
            this.hideBar(Time.bars[scale]);
            scale++;
        }
    };

    barDrawer.prototype.hideBar = function(bar){
        $(bar.canvas).hide();
    };

    barDrawer.prototype.reduceBarsBefore = function(bar) {
        var previousBar = Time.scale.previous(bar);
        while (previousBar !== null) {
            this.reduceBar(previousBar);
            previousBar = Time.scale.previous(previousBar);
        }
    };
    
    barDrawer.prototype.reduceBar = function(bar){
        $(bar.canvas).attr({
            height : bar.reducedHeight
        });
        this.drawBar(bar);
        bar.focusOnEnter();
    };

    barDrawer.prototype.showBar = function(bar) {
        $(bar.canvas).show();
    };

    barDrawer.prototype.unreduceBar = function(bar) {
        $(bar.canvas).attr({
            height : bar.height
        });
    };

    barDrawer.prototype.drawBar = function(bar, explicitBuckets) {
        var buckets = explicitBuckets ? explicitBuckets : bar.buckets;
        bar.context.fillStyle = 'rgb('+this.fillLevel+','+this.fillLevel+','+this.fillLevel+')';
        bar.context.fillRect(0, 0, bar.canvas.width, bar.canvas.height);
        var nbBuckets = buckets.length;
        for (var i = 0; i < nbBuckets; i++) {
            var bucket = buckets[i];
            bar.context.fillStyle = bucket.color;
            bar.context.fillRect(bar.viewport.delta() + bucket.x, 0, 1, bar.canvas.height);
        }
    };

    Time.BarDrawer = barDrawer;
})();