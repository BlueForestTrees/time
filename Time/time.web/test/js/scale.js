(function() {

    function scale() {
        this.seventiesInDays = 719528;
        this.daysToMillis = 24 * 60 * 60 * 1000;
        this.max = 10000 * 364, 25;

        this.scaleCount = 4;
        this.scales = [ 10000000000, 10000, 500, 10 ];

        this.echelles = {
            milliard : 1000000000,
            million : 1000000,
            millier : 1000,
            un : 1
        };
    }

    scale.prototype.previous = function(bar) {
        if (bar.scale > 0) {
            return Time.bars[bar.scale - 1];
        } else {
            return null;
        }
    };
    
    scale.prototype.next = function(bar) {
        if (bar.scale < Time.bars.length-1) {
            return Time.bars[bar.scale + 1];
        } else {
            return null;
        }
    };

    scale.prototype.isFirstScale = function(scale) {
        return scale === 0;
    };

    scale.prototype.isLastScale = function(scale) {
        return scale === this.scaleCount - 1;
    };

    scale.prototype.dec = function(value) {
        var decimals = Math.abs(value) < 10 ? 10 : 1;
        return Math.abs(Math.round(value * decimals) / decimals);
    };

    scale.prototype.getEchelle = function(years) {
        years = Math.abs(Math.round(years));
        if (Math.round(years / this.echelles.milliard) > 0) {
            return this.echelles.milliard;
        } else if (Math.round(years / this.echelles.million) > 0) {
            return this.echelles.million;
        } else if (Math.round(years / this.echelles.millier) > 0) {
            return this.echelles.millier;
        } else {
            return this.echelles.un;
        }
    };

    scale.prototype.bucketToYears = function(bucket) {
        if(bucket.years){
            return bucket.years;
        }
        return this.daysToYears(this.scales[bucket.scale] * bucket.bucket);
    };
    scale.prototype.daysToYears = function(days) {
        if (Math.abs(days) > this.max) {
            return days / 364.25;
        } else {
            var daysEpoch = days - this.seventiesInDays;
            var date = new Date(daysEpoch * 24 * 60 * 60 * 1000);
            return date.getFullYear();
        }
    };

    scale.prototype.firstSubBucket = function(scaleIndex, bucket) {
        if (scaleIndex > 0) {
            return bucket * this.scales[scaleIndex] / this.scales[scaleIndex + 1];
        } else {
            return 0;
        }
    };

    Time.Scale = scale;

})();