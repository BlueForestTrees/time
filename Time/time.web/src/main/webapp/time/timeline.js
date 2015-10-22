(function(){
	function Timeline(){
		this.drawer = new Time.BarDrawer();
		this.bars = [
					new Time.Bar(this.drawer, this.bucketSelect),
					new Time.Bar(this.drawer, this.bucketSelect),
					new Time.Bar(this.drawer, this.bucketSelect),
					new Time.Bar(this.drawer, this.bucketSelect),
					new Time.Bar(this.drawer, this.bucketSelect)
		             ];
	}
	
	Timeline.prototype.bucketSelect = function(bucket){
		console.log(bucket);
	};
	
	Time.Timeline = Timeline;
})();