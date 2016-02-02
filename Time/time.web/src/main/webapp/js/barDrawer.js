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
        this.reduceOtherThan(bar);
        this.unreduceBar(bar);
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

    barDrawer.prototype.reduceOtherThan = function(bar) {
        var previousBar = Time.scale.previous(bar);
        while (previousBar !== null) {
            this.reduceBar(previousBar);
            previousBar = Time.scale.previous(previousBar);
        }
        var nextBar = Time.scale.next(bar);
        while (nextBar !== null) {
            this.reduceBar(nextBar);
            nextBar = Time.scale.next(nextBar);
        }
    };
    
    barDrawer.prototype.reduceBar = function(bar){
        if(!bar.reduced){
            bar.reduced = true;            
            
            $(bar.canvas).attr({
                height : bar.reducedHeight
            });
            
            $(bar.canvas).css({
                opacity : bar.reducedOpacity
            });
            
            this.drawBar(bar);
            bar.focusOnEnter();
        }
    };

    barDrawer.prototype.unreduceBar = function(bar) {
        bar.reduced = false;   
        $(bar.canvas).attr({
            height : bar.height
        });
        $(bar.canvas).css({
            opacity : 1
        });
        $(bar.canvas).show();
        this.drawBar(bar);
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