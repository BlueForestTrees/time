(function() {
    function barLoading() {
        this.loading = false;
        this.loadingSpeed = 45;
    }

    barLoading.prototype.startLoading = function (bar) {
        this.loading = true;
        this._loadingPhase(bar, this._getLoadingArray());
        Time.view.timeline.css({cursor: 'progress'});
    };

    barLoading.prototype.stopLoading = function () {
        Time.view.timeline.css({cursor: 'pointer'});
        this.loading = false;
    };

    barLoading.prototype._getLoadingArray = function(){
        return [
            {color:"#000000"},{color:"#888888"},
            {color:"#999999"},{color:"#999999"},
            {color:"#DDDDDD"},{color:"#DDDDDD"},{color:"#DDDDDD"},{color:"#DDDDDD"},
            {color:"#EEEEEE"},{color:"#EEEEEE"},{color:"#EEEEEE"},{color:"#EEEEEE"},
            {color:"#FFFFFF"},{color:"#FFFFFF"},
            {color:"#EEEEEE"},{color:"#EEEEEE"},{color:"#EEEEEE"},{color:"#EEEEEE"},
            {color:"#DDDDDD"},{color:"#DDDDDD"},{color:"#DDDDDD"},
            {color:"#BBBBBB"}
        ];
    };

    barLoading.prototype._loadingPhase = function(bar, loadingArray){
        if(this.loading) {
            Time.barDrawer.drawBar(bar, this._getBucketArray(bar, loadingArray));
            this._animateArray(loadingArray);
            var that = this;
            setTimeout(function(){that._loadingPhase(bar, loadingArray);}, this.loadingSpeed);
        }else{
            Time.barDrawer.drawBar(bar);
        }
    };

    barLoading.prototype._animateArray = function(loadingArray){
        loadingArray.unshift(loadingArray.pop());
    };

    barLoading.prototype._getBucketArray = function(bar, loadingArray){
        var throbberWidth = bar.canvas.width;
        var nbBuckets = loadingArray.length;
        var gap = throbberWidth / nbBuckets;
        var throbberWidthHalf = throbberWidth * 0.5;
        var screenHalf = bar.canvas.width * 0.5;
        var throbberX = screenHalf - throbberWidthHalf;

        return loadingArray.map(function(bucket, index){
            return {
                color : this._transformColor(bucket.color),
                x : -bar.viewport.delta() + throbberX + gap*index
            };
        },this);
    };

    barLoading.prototype._transformColor = function(color){
        return color;
    };
    
    Time.BarLoading = barLoading;
})();