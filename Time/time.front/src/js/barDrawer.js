(function() {
    function barDrawer() {
        this.fillLevel = 251;
    }

    barDrawer.prototype.install = function() {
        this._hideBar();
        this._updateSizeBar();
        $(window).on('resize', this._updateSizeBar);
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
        this._unreduceBar(bar);
    };

    barDrawer.prototype._hideBar = function(){
        $(Time.bar.canvas).hide();
    };

    barDrawer.prototype._updateSizeBar = function() {
        Time.barDrawer.drawBar(Time.bar);
        Time.tooltips.updateTooltips();
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