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
		this.bars[Scale.TEN9].viewport.x = 900;
		this.data.getFacets(Scale.TEN9, '', $.proxy(this.onBuckets,this))
		
	}
	
	Timeline.prototype.onBucketSelect = function(bucket, bar){
		//this.drawer.hide(Scale.sublevel(bar.scale));
		if(bucket.count < 50){
			this.data.getPhrases(bar.scale, bucket.x,'', $.proxy(this.onPhrases,this));
		}
	}
	
	Timeline.prototype.onBuckets = function(facetsDTO){
		var buckets = this.bucketFactory.getBuckets(facetsDTO);
		console.log(buckets);
		//TODO pouvoir cliquer sur un bucket
		//TODO au clic de bucket charger la bar du dessous
		//TODO gérer les buckets en tuiles?
//Todo ajouter des phrases sympas sur ten9. Dans 3 milliard d'années etc.
		this.bars[Scale.TEN9].buckets = buckets;
		this.drawer.draw(this.bars[Scale.TEN9]);
	};
	
	Timeline.prototype.onPhrases = function(phrases){
		//TODO au clic sur un bucket qui a peu de count, virer les sousbarres et mettre les phrases
		console.log(phrases);
	};
	
	Time.Timeline = Timeline;
})();
