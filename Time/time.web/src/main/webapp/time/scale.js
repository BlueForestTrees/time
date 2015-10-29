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
				uplevel : this.TEN
			},
			TEN : {
				sublevel : this.ONE,
				multiplier : 10,
				uplevel : this.TEN3
			},
			TEN3 : {
				sublevel : this.TEN,
				multiplier : 10000,
				uplevel : this.TEN6
			},
			TEN6 : {
				sublevel : this.TEN3,
				multiplier : 10000000,
				uplevel : this.TEN9
			},
			TEN9 : {
				sublevel : this.TEN6,
				multiplier : 10000000000
			}
		};
	}
	
	Escale.prototype.multiplier = function(scale){
		return this.details[scale].multiplier;
	};
	
	Escale.prototype.sublevel = function(scale){
		return this.details[scale].sublevel;
	};
	Escale.prototype.uplevel = function(scale){
		return this.details[scale].uplevel;
	};
	Escale.prototype.date = function(scale, bucket){
		return this.multiplier(scale)*bucket;
	};
	
	
	Scale = new Escale();
			
})();