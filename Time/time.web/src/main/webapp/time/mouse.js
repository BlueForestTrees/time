(function() {
	function Mouse(drawer) {
		this.drawer = drawer;
	}

	Mouse.prototype.install = function(bar, bucketSelect) {
		var data = {
				previousX : null,
				deltaX : null,
				bar : bar,
				move : false,
				bucketSelect : bucketSelect,
				drawer : this.drawer
			};
		$(bar.canvas).on('mousedown.Viewport', data, $.proxy(this.onMouseDown, this));
		$(bar.canvas).on('mousemove.Tooltip', data, $.proxy(this.onMouseTooltip, this));
	};
	
	Mouse.prototype.onMouseTooltip = function(event) {
		var bar = event.data.bar;
		var mousePosition = this.getMousePosition(event);
		var bucketPosition = bar.getBucketPosition(mousePosition);
		var toolTipText = Scale.getTooltipText(bar.scale, bucketPosition);
		console.log(toolTipText);
	};
	
	Mouse.prototype.onMouseDown = function(event) {
		event.data.previousX = event.clientX;
		$(window).on('mousemove.Viewport', event.data, $.proxy(this.onViewportDrag, this));
		$(window).on('mouseup.Viewport', event.data, $.proxy(this.onViewportUp, this));
	};
	
	Mouse.prototype.onViewportDrag = function(event) {
		event.data.bar.viewport.local += event.clientX - event.data.previousX;
		event.data.drawer.draw(event.data.bar);
		event.data.previousX = event.clientX;
		event.data.move = true;
	};
	
	Mouse.prototype.onViewportUp = function(event) {
		if (!event.data.move) {
			this.onMouseClick(event);
		}else{
			console.log("viewport : ",event.data.bar.viewport.delta());
		}
		event.data.move = false;

		$(window).off('mousemove.Viewport');
		$(window).off('mouseup.Viewport');
	};

	Mouse.prototype.onMouseClick = function(event) {
		var bucket = event.data.bar.searchBucketAt(this.getMousePosition(event));
		if (bucket) {
			event.data.bucketSelect(bucket, event.data.bar);
		}
	};
	
	Mouse.prototype.getMousePosition = function(event){
		//-1 hack pour la bordure de 1px
		return event.clientX-1;
	}

	Time.Mouse = Mouse;
})();