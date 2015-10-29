(function(){
	function Timeline(){
		this.bars = {};
		this.bars[Scale.TEN9] = new Time.Bar(Scale.TEN9);
		this.bars[Scale.TEN6] = new Time.Bar(Scale.TEN6);
		this.bars[Scale.TEN3] = new Time.Bar(Scale.TEN3);
		this.bars[Scale.TEN] = new Time.Bar(Scale.TEN);
		this.bars[Scale.ONE] = new Time.Bar(Scale.ONE);
		this.drawer = new Time.BarDrawer(this.bars);
		this.barmouse = new Time.BarMouse(this.drawer);
		this.data = new Time.Data();
		this.bucketFactory = new Time.BucketFactory();
		
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
		
		//BARMOUSE
		var onBucketSelectCall = $.proxy(this.onBucketSelect,this);
		for(var scale in this.bars){
			var bar = this.bars[scale];
			this.barmouse.installMouse(bar, onBucketSelectCall);
		}
		
		//INIT DATA
		var bar = this.bars[Scale.TEN9];
		bar.viewport.x = 1000;
		this.data.getFacets(bar.scale,null,'', $.proxy(this.onBuckets,this, bar))
		this.drawer.hide(Scale.sublevel(bar.scale));
	}
	
	Timeline.prototype.onBucketSelect = function(bucket, bar){
		this.drawer.hide(Scale.sublevel(bar.scale));
		this.drawer.clearText();
		//affiche les phrases
		//if(bucket.count < 50){
		//	this.data.getPhrases(Scale.sublevel(bar.scale), bucket.x,'', $.proxy(this.onPhrases,this));
		//niveau de detail++
		//}else{
			var subBar = this.bars[Scale.sublevel(bar.scale)];
			subBar.viewport.x = bucket.bucket * 1000;
			console.log("viewport : " + subBar.viewport.x);
			this.data.getFacets(subBar.scale, bucket.bucket, '', $.proxy(this.onBuckets,this, subBar));
		//}
	}
	
	Timeline.prototype.onBuckets = function(bar, facetsDTO){
		bar.buckets = this.bucketFactory.getBuckets(facetsDTO);
		this.drawer.showBar(bar);
		this.drawer.draw(bar);
	};
	
	Timeline.prototype.onPhrases = function(phrases){
		this.drawer.setPhrases(phrases);
	};
	
	Time.Timeline = Timeline;
})();
