(function(){
	function Viewport(){
		this.global = 1000;
		this.local = 0;
	}
	
	Viewport.prototype.delta = function(){
		return this.global + this.local;
	}
	
	Time.Viewport = Viewport;
})();