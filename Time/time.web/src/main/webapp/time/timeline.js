(function(){
	function Timeline(){
		this.drawer = new Time.BarDrawer();
		this.bars = [
					new Time.Bar(Scale.ONE, this.drawer),
					new Time.Bar(Scale.TEN, this.drawer),
					new Time.Bar(Scale.TEN3, this.drawer),
					new Time.Bar(Scale.TEN6, this.drawer),
					new Time.Bar(Scale.TEN9, this.drawer)
		             ];
	}
	
	Time.Timeline = Timeline;
})();