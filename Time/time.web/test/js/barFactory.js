(function() {
    function barFactory() {
        this.min = 0;
        this.max = 1000;
        this.middle = this.min + (this.max - this.min) / 2;
        this.minColor = 0;
        this.maxColor = 255;
        this.middleColor = this.minColor + (this.maxColor - this.minColor) / 2;
    }

    barFactory.prototype.buildCanvas = function(height, scale) {		
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

    barFactory.prototype.buildBuckets = function(bucketsDTO) {
        for (var i = 0; i < bucketsDTO.buckets.length; i++) {
            var bucket = bucketsDTO.buckets[i];
            bucket.x = bucket.bucket;
            bucket.color = this.getColor(bucket.count);
            bucket.scale = bucketsDTO.scale;
        }
        
        bucketsDTO.buckets.sort(this.triBuckets);
        
        return bucketsDTO.buckets;
    };
    
    barFactory.prototype.triBuckets = function(a,b){
        return b.x - a.x;
    }
    
    barFactory.prototype.getColor = function(count) {
        var r = this.getRed(count);
        var g = this.getGreen(count);
        return 'rgb(' + r + ',' + g + ',0)';
    };
    barFactory.prototype.getGreen = function(count) {
        return parseInt(Math.max(this.minColor, this.maxColor - count / this.max * (this.maxColor - this.minColor) * 2));
    };
    barFactory.prototype.getRed = function(count) {
        return parseInt(this.maxColor - Math.max(0, count - this.middle) / this.max * (this.maxColor - this.minColor) * 2);
    };

    Time.BarFactory = barFactory;
})();