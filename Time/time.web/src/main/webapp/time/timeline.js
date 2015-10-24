(function(){
	function Timeline(){
		this.drawer = new Time.BarDrawer();
		this.barmouse = new Time.BarMouse(this.drawer);
		var callbackBucketSelect = $.proxy(this.bucketSelect,this);
		this.bars = {};
		this.bars[Scale.TEN9] = new Time.Bar(Scale.TEN9, this.drawer, callbackBucketSelect);
		this.bars[Scale.TEN6] = new Time.Bar(Scale.TEN6, this.drawer, callbackBucketSelect);
		this.bars[Scale.TEN3] = new Time.Bar(Scale.TEN3, this.drawer, callbackBucketSelect);
		this.bars[Scale.TEN] = new Time.Bar(Scale.TEN, this.drawer, callbackBucketSelect);
		this.bars[Scale.ONE] = new Time.Bar(Scale.ONE, this.drawer, callbackBucketSelect);
		
		//DRAWER
		for(var scale in this.bars){
			var bar = this.bars[scale];
			this.drawer.resize(bar);			
		}
		$(window).resize($.proxy(function() {
			for(var scale in this.bars){
				var bar = this.bars[scale];
				this.drawer.resize(bar);
			}
		},this));
		
		//TODO BARMOUSE à remonter/refactorer ici
		for(var scale in this.bars){
			var bar = this.bars[scale];
			this.barmouse.installMouse(bar, $.proxy(this.bucketSelect, this));
		}
	}
	
	Timeline.prototype.bucketSelect = function(bucket, bar){
		//TODO afficher/masquer les barres
		console.log(this);
		console.log(bar);
		console.log(bucket);
	}
	
	Time.Timeline = Timeline;
})();