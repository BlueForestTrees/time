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
	Escale.prototype.date = function(scale, bucket){
		return this.multiplier(scale)*bucket;
	};
	Escale.prototype.firstSubBucket = function(scale, bucket){
		if(scale){
			var multiplierDelta = this.multiplier(scale) / this.multiplier(this.sub(scale));
			console.log("scale : ", scale, ", bucket : ", bucket, ", multiplierDelta : ", multiplierDelta);
			return multiplierDelta*bucket;			
		}else{
			return 0;
		}
	}
	
	
	Scale = new Escale();
			
})();