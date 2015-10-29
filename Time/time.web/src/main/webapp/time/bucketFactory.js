(function(){
	function BucketFactory(){
		this.min = 0;
		this.max = 1000;
		this.middle = this.min + (this.max - this.min) / 2;
		this.minColor = 0;
		this.maxColor = 255;
		this.middleColor = this.minColor + (this.maxColor - this.minColor) / 2;
	}
	BucketFactory.prototype.generateBuckets = function(nbBuckets) {
		var buckets = [];
		for (var i = 0; i < nbBuckets; i++) {
			var x = parseInt((Math.random() - 0.333) * 3000); // -1000 à 2000
			var count = parseInt(Math.random()*1000);
			var color = this.getColor(count);
			var bucket = {
				x : x,
				color : color
			};
			buckets.push(bucket);
		}
		buckets.sort(function compare(a, b) {
			  	if (a.x < b.x)
				     return -1;
				  if (a.x > b.x)
				     return 1;
				  return 0;
				});
		return buckets;
	};
	
	BucketFactory.prototype.getColor = function(count){
		var r = this.getRed(count);
		var g = this.getGreen(count);
		return 'rgb('+r+','+g+',0)';
	};
	
	BucketFactory.prototype.getBuckets = function(facetsDTO){
		for (var i = 0; i < facetsDTO.facets.length; i++) {
			var facet = facetsDTO.facets[i];
			facet.x = facet.bucket;
			facet.color = this.getColor(facet.count);
		}
		facetsDTO.facets.sort(function compare(a, b) {
												  	if (a.x < b.x)
													     return -1;
													  if (a.x > b.x)
													     return 1;
													  return 0;
													});
		return facetsDTO.facets;
	};
	
	BucketFactory.prototype.getGreen = function(count){
		return parseInt(Math.max(this.minColor, this.maxColor - count / this.max * (this.maxColor - this.minColor) * 2));
	};
	BucketFactory.prototype.getRed = function(count) {
		return parseInt(this.maxColor - Math.max(0, count - this.middle) / this.max * (this.maxColor - this.minColor) * 2);
	};
	
	Time.BucketFactory = BucketFactory;
})();