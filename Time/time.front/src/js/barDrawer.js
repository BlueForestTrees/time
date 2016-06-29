(function() {
    function barDrawer() {
        this.fillLevel = 251;
    }

    barDrawer.prototype.install = function() {
        this._hideAllBars();
        this._updateSizeAllBars();
        $(window).on('resize', this._updateSizeAllBars);
        delete barDrawer.prototype.install;
    };

    barDrawer.prototype.drawBar = function(bar, explicitBuckets) {
        bar.normalize();
        var buckets = explicitBuckets ? explicitBuckets : bar.buckets;
        bar.context.fillStyle = 'rgb('+this.fillLevel+','+this.fillLevel+','+this.fillLevel+')';
        bar.context.fillRect(0, 0, bar.canvas.width, bar.canvas.height);

        buckets.forEach(function(bucket){
            bar.context.fillStyle = bucket.color;
            bar.context.fillRect(bar.viewport.toCanvasX(bucket.x), 0, 1, bar.canvas.height);
        });
    };

    barDrawer.prototype.focusOn = function(bar) {
        this._reduceOtherThan(bar);
        this._unreduceBar(bar);
    };

    barDrawer.prototype.hideBarsAfter = function(bar) {
        var scale = bar.scale + 1;
        while (scale < Time.bars.length) {
            this.hideBar(Time.bars[scale]);
            scale++;
        }
    };

    barDrawer.prototype.hideBar = function(bar){
        $(bar.canvas).hide();
    };



    barDrawer.prototype._updateSizeAllBars = function() {
        Time.bars.forEach(Time.barDrawer._updateSizeBar);
    };
    
    barDrawer.prototype._updateSizeBar = function(bar) {
        Time.barDrawer.drawBar(bar);
        Time.tooltips.updateTooltips();
    };

    barDrawer.prototype._hideAllBars = function() {
        var scale = 0;
        while (scale < Time.bars.length) {
            this.hideBar(Time.bars[scale]);
            scale++;
        }
    };
    
    barDrawer.prototype._reduceOtherThan = function(bar) {
        var previousBar = Time.scale.previous(bar);
        while (previousBar !== null) {
            this._reduceBar(previousBar);
            previousBar = Time.scale.previous(previousBar);
        }
        var nextBar = Time.scale.next(bar);
        while (nextBar !== null) {
            this._reduceBar(nextBar);
            nextBar = Time.scale.next(nextBar);
        }
    };
    
    barDrawer.prototype._reduceBar = function(bar){
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

    barDrawer.prototype._unreduceBar = function(bar) {
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

    Time.BarDrawer = barDrawer;
})();