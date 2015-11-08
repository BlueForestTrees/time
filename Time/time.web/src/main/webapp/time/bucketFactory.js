(function() {
    function bucketFactory() {
        this.min = 0;
        this.max = 1000;
        this.middle = this.min + (this.max - this.min) / 2;
        this.minColor = 0;
        this.maxColor = 255;
        this.middleColor = this.minColor + (this.maxColor - this.minColor) / 2;
    }

    bucketFactory.prototype.getColor = function(count) {
        var r = this.getRed(count);
        var g = this.getGreen(count);
        return 'rgb(' + r + ',' + g + ',0)';
    };

    bucketFactory.prototype.getBuckets = function(bucketsDTO) {
        for (var i = 0; i < bucketsDTO.subbuckets.length; i++) {
            var facet = bucketsDTO.subbuckets[i];
            facet.x = facet.bucket;
            facet.color = this.getColor(facet.count);
        }
        return bucketsDTO.subbuckets;
    };

    bucketFactory.prototype.getGreen = function(count) {
        return parseInt(Math.max(this.minColor, this.maxColor - count / this.max * (this.maxColor - this.minColor) * 2));
    };
    bucketFactory.prototype.getRed = function(count) {
        return parseInt(this.maxColor - Math.max(0, count - this.middle) / this.max * (this.maxColor - this.minColor) * 2);
    };

    Time.BucketFactory = bucketFactory;
})();