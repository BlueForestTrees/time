(function() {
    function BarFactory() {
        this.min = 0;
        this.max = 1000;
        this.middle = this.min + (this.max - this.min) / 2;
        this.minColor = 0;
        this.maxColor = 255;
        this.middleColor = this.minColor + (this.maxColor - this.minColor) / 2;
    }

    BarFactory.prototype.buildCanvas = function(height, scale) {		
        var canvasAttributes = {
            id : 'bar#' + scale,
            width : '100%',
            height : height + 'px'
        };
        var canvasCss = {
            borderTop : '1px solid #CDCDCD'
        };

        $('<canvas>').attr(canvasAttributes)
                     .css(canvasCss)
                     .appendTo(Time.view.timeline);

        return document.getElementById(canvasAttributes.id).getContext("2d");
    };

    BarFactory.prototype.buildBuckets = function(bucketsDTO) {
        for (var i = 0; i < bucketsDTO.buckets.length; i++) {
            var bucket = bucketsDTO.buckets[i];
            bucket.x = bucket.bucket;
            bucket.color = this._getColor(bucket.count);
            bucket.scale = bucketsDTO.scale;
        }
        
        bucketsDTO.buckets.sort(this._triBuckets);
        return bucketsDTO.buckets;
    };
    
    BarFactory.prototype._triBuckets = function(a,b){
        return a.x - b.x;
    };
    
    BarFactory.prototype._getColor = function(count) {
        var r = this._getRed(count);
        var g = this._getGreen(count);
        return 'rgb(' + r + ',' + g + ',0)';
    };
    BarFactory.prototype._getGreen = function(count) {
        return parseInt(Math.max(this.minColor, this.maxColor - count / this.max * (this.maxColor - this.minColor) * 2));
    };
    BarFactory.prototype._getRed = function(count) {
        return parseInt(this.maxColor - Math.max(0, count - this.middle) / this.max * (this.maxColor - this.minColor) * 2);
    };

    Time.BarFactory = BarFactory;
})();