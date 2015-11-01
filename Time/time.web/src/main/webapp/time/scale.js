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
			millier : 1000
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
		var absYears = Math.abs(years);
		var negative = years < 0;
		var echelle = this.getEchelle(absYears);
		
		switch(echelle){
		//Il y a [3] milliard(s) d'année(s)
		case this.echelles.milliard:
			return (negative ? "il y a " : "dans ") + this.prepare(years, this.echelles.milliard) + " milliards d'années";
		case this.echelles.million:
			return (negative ? "il y a " : "dans ") + this.prepare(years, this.echelles.million) + " millions d'années";
		default:
			return (negative ? "il y a " : "dans ") + this.prepare(years, this.echelles.millier) + " ans";
		break;
		}
		
				/*
		if(scale === this.TEN9){
			var milliard = bucket / 364.25 * 10
			var milliardRound = Math.round(milliard);
			if(milliardRound < 0){
				if(milliardRound > -4){
					return "il y a " + Math.abs(Math.round(milliard*10)/10) + " milliards d'années";
				}else{					
					return "il y a " + Math.abs(milliardRound) + " milliards d'années";
				}
			}else if(milliardRound === 0){
				if(milliard < 0){					
					var million = milliard * 1000;
					var millionRound = Math.abs(Math.round(million));
					return "il y a " + millionRound + " millions d'années";
				}else{
					var million = milliard * 1000;
					var millionRound = Math.abs(Math.round(million));
					return "dans " + millionRound + " millions d'années";
				}
			}else{
				return "dans " + milliardRound + " milliards d'années";
			}
		}else{			
			var multiplier = this.multiplier(scale);
			var yearsCompTo0 = multiplier * bucket / 364.25;
			return yearsCompTo0 + " années / JC";
		}*/
	};
	
	Escale.prototype.getEchelle = function(years){
		if (parseInt(years / this.echelles.milliard) > 0){
			return this.echelles.milliard;
		}else if(parseInt(years / this.echelles.million) > 0){
			return this.echelles.million;
		}else {
			return this.echelles.millier;
		}
	}
	
	Escale.prototype.prepare = function(value, echelle){
		var decimals = 1;//mettre à 10 pour avoir une décimale
		return Math.abs(Math.round(value/echelle*decimals)/decimals);
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