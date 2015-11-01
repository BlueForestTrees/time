(function(){
	
	function Escale(){
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
	
	Escale.prototype.multiplier = function(scale){
		return this.details[scale].multiplier;
	};
	
	Escale.prototype.sub = function(scale){
		return this.details[scale].sub;
	};
	Escale.prototype.up = function(scale){
		return this.details[scale].up;
	};
	Escale.prototype.getTooltipText = function(scale, bucket){
		var years = this.getYears(scale, bucket);
		var echelle = this.getEchelle(years);
		
		switch(echelle){
		case this.echelles.milliard:
			return Math.round(years / this.echelles.milliard) + " milliards d'années";
		case this.echelles.million:
			return this.dec(years / this.echelles.million) + " millions d'années";
		case this.echelles.millier:
		case this.echelles.un:
			var negative = years < 0;
			var roundYears = Math.round(years);
			
			if(roundYears === 0)return "de nos jours";
			if(negative)		return roundYears + " av. JC";
			else 				return "en " + roundYears;
		break;
		}
	};
	
	Escale.prototype.dec = function(value){
		var decimals = Math.abs(value) < 10 ? 10 : 1;
		return Math.round(value*decimals)/decimals;
	};
	
	Escale.prototype.getEchelle = function(years){
		years = Math.abs(Math.round(years));
		if (Math.round(years / this.echelles.milliard) > 0){
			return this.echelles.milliard;
		}else if(Math.round(years / this.echelles.million) > 0){
			return this.echelles.million;
		}else if(Math.round(years / this.echelles.millier) > 0){
			return this.echelles.millier;
		}else{
			return this.echelles.un;
		}
	}
	
	Escale.prototype.getYears = function(scale, bucket){
		return this.multiplier(scale) * bucket / 364.25;
	}
	
	Escale.prototype.firstSubBucket = function(scale, bucket){
		if(scale){
			var multiplierDelta = this.multiplier(scale) / this.multiplier(this.sub(scale));
			return multiplierDelta*bucket;			
		}else{
			return 0;
		}
	}
	
	
	Scale = new Escale();
			
})();