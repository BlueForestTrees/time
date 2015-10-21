(function(){
	function BucketsFactory(){
		this.min = 0;
		this.max = 1000;
		this.middle = this.min + (this.max - this.min) / 2;
		this.minColor = 0;
		this.maxColor = 255;
		this.middleColor = this.minColor + (this.maxColor - this.minColor) / 2;
	}
	BucketsFactory.prototype.generateBuckets = function(nbBuckets) {
		var buckets = [];
		for (var i = 0; i < nbBuckets; i++) {
			var x = parseInt((Math.random() - 0.333) * 3000); // -1000 à 2000
			var count = parseInt(Math.random()*1000);
			var r = this.getRed(count);
			var g = this.getGreen(count);
			var bucket = {
				x : x,
				color : 'rgb('+r+','+g+','+0+')'
			};
			buckets.push(bucket);
		}
		return buckets;
	};
	
	BucketsFactory.prototype.getGreen = function(count){
		return parseInt(Math.max(this.minColor, this.maxColor - count / this.max * (this.maxColor - this.minColor) * 2));
	}
	BucketsFactory.prototype.getRed = function(count) {
		return parseInt(this.maxColor - Math.max(0, count - this.middle) / this.max * (this.maxColor - this.minColor) * 2);
	}
	
	Time.BucketsFactory = BucketsFactory;
})();