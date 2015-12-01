(function() {

    function scale() {
        this.ONE = 'ONE';
        this.TEN = 'TEN';
        this.TEN3 = 'TEN3';
        this.TEN6 = 'TEN6';
        this.TEN9 = 'TEN9';
        this.details = {
            ONE : {
                multiplier : 1,
                up : this.TEN
            },
            TEN : {
                sub : this.ONE,
                multiplier : 10,
                up : this.TEN3
            },
            TEN3 : {
                sub : this.TEN,
                multiplier : 10000,
                up : this.TEN6
            },
            TEN6 : {
                sub : this.TEN3,
                multiplier : 10000000,
                up : this.TEN9
            },
            TEN9 : {
                sub : this.TEN6,
                multiplier : 10000000000
            }
        };
        this.echelles = {
            milliard : 1000000000,
            million : 1000000,
            millier : 1000,
            un : 1
        };
    }

    scale.prototype.multiplier = function(scale) {
        return this.details[scale].multiplier;
    };

    scale.prototype.sub = function(scale) {
        return this.details[scale].sub;
    };
    scale.prototype.up = function(scale) {
        return this.details[scale].up;
    };
    scale.prototype.getTooltipText = function(scale, bucketX, bucket) {
        var years = this.getYears(scale, bucketX);
        var start = bucketX > 0 ? 'Dans ' : 'Il y a ';
        var echelle = this.getEchelle(years);
        var end = bucket ? " ("+bucket.count+")" : "";

        switch (echelle) {
        case this.echelles.milliard:
            return start + Math.abs(Math.round(years / this.echelles.milliard)) + " milliards d'années" + end;
        case this.echelles.million:
            return start + this.dec(years / this.echelles.million) + " millions d'années" + end;
        case this.echelles.millier:
        case this.echelles.un:
            var negative = years < 0;
            var roundYears = Math.round(years);
            if (roundYears === 0)
                return "De nos jours" + end;
            if (negative)
                return roundYears + " av. JC" + end;
            else
                return "en " + roundYears + end;
            break;
        default:
            return 'WWWOOOOOWWW';
        }
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

    scale.prototype.getYears = function(scale, bucket) {
        return this.multiplier(scale) * bucket / 364.25;
    };

    scale.prototype.firstSubBucket = function(scale, bucket) {
        if (scale) {
            var multiplierDelta = this.multiplier(scale) / this.multiplier(this.sub(scale));
            return multiplierDelta * bucket;
        } else {
            return 0;
        }
    };

    Scale = new scale();

})();