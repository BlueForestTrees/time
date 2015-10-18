(function(){
	function Bar(height){
		this.viewport = new Time.Viewport(0);
		this.buckets = new Time.BucketsFactory().generateBuckets(10000);
		this.context = new Time.CanvasFactory().build(height);
		this.canvas = this.context.canvas;
	}
	Time.Bar = Bar;
})();