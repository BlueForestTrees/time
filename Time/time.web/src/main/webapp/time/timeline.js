(function(){
	function Timeline(){
		this.bars = {};
		this.bars[Scale.TEN9] = new Time.Bar(Scale.TEN9);
		this.bars[Scale.TEN6] = new Time.Bar(Scale.TEN6);
		this.bars[Scale.TEN3] = new Time.Bar(Scale.TEN3);
		this.bars[Scale.TEN] = new Time.Bar(Scale.TEN);
		this.bars[Scale.ONE] = new Time.Bar(Scale.ONE);
		this.drawer = new Time.BarDrawer(this.bars);
		this.mouse = new Time.Mouse(this.drawer);
		this.data = new Time.Data();
		this.bucketFactory = new Time.BucketFactory();
		this.filter = '';
		
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
			this.mouse.install(bar, onBucketSelectCall);
		}
		
		//INIT BAR 1
		var bar = this.bars[Scale.TEN9];
		this.data.getFacets(this.filter, bar.scale,null, $.proxy(this.onBuckets,this, bar))
		this.drawer.hide(Scale.sub(bar.scale));
		
		//FILTRE
		$("#filtreInput").keypress($.proxy(this.onFiltreKeyPress,this));
		$("input[type='text']").on("click", function () {$(this).select();});
	}
	
	Timeline.prototype.onFiltreKeyPress = function(e){
		if (e.which == 13) {
			this.onFiltreEnter();
		}
	}
	
	Timeline.prototype.onFiltreEnter = function(){
		this.filter = $("#filtreInput").val();
		var bar = this.bars[Scale.TEN9];
		this.data.getFacets(this.filter, bar.scale,null, $.proxy(this.onBuckets,this, bar))
		this.drawer.hide(Scale.sub(bar.scale));
		this.drawer.clearText();
	};
	
	Timeline.prototype.onBucketSelect = function(bucket, bar){
		this.drawer.hide(Scale.sub(bar.scale));
		this.drawer.clearText();
		//affiche les phrases
		if(bucket.count < 50 || bar.scale === Scale.TEN){
			this.data.getPhrases(this.filter, Scale.sub(bar.scale), bucket.x, $.proxy(this.onPhrases,this));
		//niveau de detail++
		}else{
			var subBar = this.bars[Scale.sub(bar.scale)];
			this.data.getFacets(this.filter, subBar.scale, bucket.bucket, $.proxy(this.onBuckets,this, subBar));
		}
	};
	
	Timeline.prototype.onBuckets = function(bar, facetsDTO){
		bar.viewport.local = -Scale.firstSubBucket(Scale.up(bar.scale), facetsDTO.parentBucket);
		bar.buckets = this.bucketFactory.getBuckets(facetsDTO);
		this.drawer.showBar(bar);
		this.drawer.draw(bar);
	};
	
	Timeline.prototype.onPhrases = function(phrases){
		this.drawer.setPhrases(phrases, this.filter);
	};
	
	Time.Timeline = Timeline;
})();
