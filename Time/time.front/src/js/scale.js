(function() {

    function Scale() {
        this.seventiesInDays = 719528;
        this.max = 10000 * 364.25;
        this.scales = [ 10000000000, 10000, 500, 10 ];
        this.echelles = {
            milliard : 1000000000,
            million : 1000000,
            millier : 1000,
            un : 1
        };
    }

    /**
     * The default Scale is the farest
     * @returns {number}
     */
    Scale.prototype.defaultScale = function(){
        return 0;
    };

    Scale.prototype.dayToHuman = function(day){
        var bucket = {
            years : this._daysToYears(day)
        };
        return this.bucketToHuman(bucket);
    };

    Scale.prototype.bucketToFilter = function(bucket){
        return "TODO scale.js l.31";
    };

    Scale.prototype.bucketToHuman = function(bucket) {
        var years = this._bucketToYears(bucket);
        var start = this._getStart(years);
        switch (this._getEchelle(years)) {
            case this.echelles.milliard:
                var nbard = this._round(years / this.echelles.milliard, 1);
                return start + nbard + " milliard" + (nbard > 1 ? "s" : "") + " d'années";
            case this.echelles.million:
                var nbon = this._round(years / this.echelles.million, 1);
                return start + nbon + " million" + (nbon > 1 ? "s" : "") + " d'années";
            case this.echelles.millier:
            case this.echelles.un:
                var roundYears = Math.round(years);
                if (roundYears === 0)
                    return "De nos jours";
                else
                    return "en " + roundYears;
            default:
                return 'WWWOOOOOWWW';
        }
    };

    Scale.prototype._round = function(value, dec) {
        //dec      = 0,  1,   2,    3
        //decimals = 1, 10, 100, 1000
        var decimals = Math.pow(10,dec);
        return Math.abs(Math.round(value * decimals) / decimals);
    };

    Scale.prototype._getEchelle = function(years) {
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

    Scale.prototype._bucketToYears = function(bucket) {
        if(bucket.years){
            return bucket.years;
        }
        return this._daysToYears(this.scales[bucket.scale] * bucket.bucket);
    };

    Scale.prototype._daysToYears = function(days) {
        if (Math.abs(days) > this.max) {
            return days / 364.25;
        } else {
            var daysEpoch = days - this.seventiesInDays;
            var date = new Date(daysEpoch * 24 * 60 * 60 * 1000);
            return date.getFullYear();
        }
    };

    Scale.prototype._getStart = function(years) {
        return years > 0 ? 'Dans ' : 'Il y a ';
    };

    Time.Scale = Scale;

})();