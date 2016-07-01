(function() {

    function Scale() {
        this.seventiesInDays = 719528;
        this.yearCalendarLimit = 10000;
        this.dayCalendarLimit = this.yearCalendarLimit * 364.25;
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
        var years = this._bucketToYears(bucket);
        var days = this._bucketToDays(bucket);

        switch (this._getEchelle(years)) {
            case this.echelles.milliard:
                var nbard = this._round(years / this.echelles.milliard, 1);
                return "@" + nbard + "M";
            case this.echelles.million:
                var nbon = this._round(years / this.echelles.million, 1);
                return "@" + nbon + "m";
            case this.echelles.millier:
            case this.echelles.un:
                if(!this._insideCalendarLimit(years)){
                    return "@" + years;
                }else{
                    return "@" + this._formatDateFilter(this._daysToDate(days));
                }
            default:
                return 'never ever reached, yeah';
        }
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
                if(bucket.scale === 0){
                    return "De nos jours";
                }else {
                    return "en " + Math.round(years);
                }
            default:
                return 'never ever reached, yeah';
        }
    };

    Scale.prototype._round = function(value, dec) {
        //dec      = 0,  1,   2,    3
        //decimals = 1, 10, 100, 1000
        var decimals = Math.pow(10,dec);
        return Math.abs(Math.round(value * decimals) / decimals);
    };

    /**
     *
     * @param years
     * @returns {number}
     * @private
     */
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
        return this._daysToYears(this._bucketToDays(bucket));
    };

    Scale.prototype._bucketToDays = function(bucket){
        if(bucket.years){
            return bucket.years;
        }
        return this.scales[bucket.scale] * bucket.bucket;
    };

    Scale.prototype._daysToYears = function(days) {
        if (Math.abs(days) > this.dayCalendarLimit) {
            return days / 364.25;
        } else {
            return this._daysToDate(days).getFullYear();
        }
    };

    Scale.prototype._daysToDate = function(days){
        return new Date((days - this.seventiesInDays) * 24 * 60 * 60 * 1000);
    };

    Scale.prototype._getStart = function(years) {
        return years > 0 ? 'Dans ' : 'Il y a ';
    };

    Scale.prototype._formatDateFilter = function(date){
        return leadingZero(date.getMonth() + 1) + "/" +  leadingZero(date.getDate()) + "/" +  date.getFullYear();
    };

    function leadingZero(value){
        if(value < 10){
            return "0"+value;
        }else{
            return ""+value;
        }
    }

    Scale.prototype._insideCalendarLimit = function(year){
        return year > -400 && year < 3000;
    };

    Time.Scale = Scale;

})();