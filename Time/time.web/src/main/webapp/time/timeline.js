(function(){
	function Timeline(){
		this.drawer = new Time.BarDrawer();
		this.bars = [
					new Time.Bar(this.drawer),
					new Time.Bar(this.drawer),
					new Time.Bar(this.drawer),
					new Time.Bar(this.drawer),
					new Time.Bar(this.drawer)
		             ];
	}
	Time.Timeline = Timeline;
})();