(function(){
	function BarDrawer(bars){
		this.bars = bars;
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
	
	/*BarDrawer.prototype.hide = function(scale){
		do{
			var bar = this.bars[scale];
			this.hideBar(bar);	
			scale = Scale.sublevel(scale);
		}while(scale);
	};
	
	BarDrawer.prototype.hideBar = function(bar){
		//console.log("hide scale " + bar.scale);
	};*/
	
	Time.BarDrawer = BarDrawer;
})();