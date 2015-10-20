(function(){
	function Bar(){
		this.viewport = new Time.Viewport(0);
		this.buckets = new Time.BucketsFactory().generateBuckets(100);
		this.context = new Time.CanvasFactory().build(25);
		this.canvas = this.context.canvas;
	}
	Time.Bar = Bar;
})();