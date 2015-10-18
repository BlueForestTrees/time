(function(){
	function BucketsFactory(){

	}
	BucketsFactory.prototype.generateBuckets = function(count) {
		var buckets = [];
		for (var i = 0; i < count; i++) {
			var x = (Math.random() - 0.333) * 30000; // -1000 à 2000
			var color = Math.random() * 16581375;
			var bucket = {
				x : x,
				color : color
			};
			buckets.push(bucket);
		}
		return buckets;
	};
	Time.BucketsFactory = BucketsFactory;
})();