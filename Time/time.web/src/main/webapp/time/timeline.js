(function(){
	function Timeline(){
		this.drawer = new Time.BarDrawer();
		this.bar1 = new Time.Bar(20);
		this.barmouse = new Time.BarMouse(this.bar1, this.drawer);
		
		this.drawer.resize(this.bar1);
		$(window).resize($.proxy(function() {
			this.drawer.resize(this.bar1);
		},this));
		
		this.barmouse.installMouse();
	}
	Time.Timeline = Timeline;
})();