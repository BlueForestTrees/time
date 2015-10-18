(function(){
	function BarDrawer(){
		
	}
	BarDrawer.prototype.resize = function(bar){
		bar.canvas.width = window.innerWidth - 2;
		this.draw(bar);
	};
	BarDrawer.prototype.draw = function(bar){
		bar.context.clearRect(0, 0, bar.canvas.width, bar.canvas.height);
		var nbBuckets = bar.buckets.length;
		for (var i = 0; i < nbBuckets; i++) {
			var bucket = bar.buckets[i];
			bar.context.fillStyle = bucket.color;
			bar.context.fillRect(bar.viewport.x + bucket.x, 0, 1, bar.canvas.height);
		}
	};
	Time.BarDrawer = BarDrawer;
})();