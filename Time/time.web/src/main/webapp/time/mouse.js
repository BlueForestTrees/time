(function() {
	function Mouse(drawer) {
		this.drawer = drawer;
	}

	/* COMMON */
	Mouse.prototype.install = function(filterSelect) {
		var data = {filterSelect : filterSelect};
		$('#phrases').on('dblclick.Textes', data, $.proxy(this.onPhrasesDblClick, this));
	}
	Mouse.prototype.installBar = function(bar, bucketSelect, filterSelect) {
		var data = {
				previousX : null,
				deltaX : null,
				bar : bar,
				move : false,
				bucketSelect : bucketSelect,
				drawer : this.drawer
			};
		$(bar.canvas).on('mousedown.Viewport', data, $.proxy(this.onMouseDown, this));
		$(bar.canvas).on('mousemove.ViewportTooltip', data, $.proxy(this.onMouseTooltip, this));
	};
	
	/* VIEWPORTTOOLTIP */
	Mouse.prototype.onMouseTooltip = function(event) {
		/*var bar = event.data.bar;
		var mousePosition = this.getMousePosition(event);
		var bucketPosition = bar.getBucketPosition(mousePosition);
		var toolTipText = Scale.getTooltipText(bar.scale, bucketPosition);*/
	};
	
	/* VIEWPORT */
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
		}
		event.data.move = false;

		$(window).off('mousemove.Viewport');
		$(window).off('mouseup.Viewport');
	};

	/* BUCKET SELECT */
	Mouse.prototype.onMouseClick = function(event) {
		var mousePosition = this.getMousePosition(event);
		var bucket = event.data.bar.searchBucketAt(mousePosition);
		if (bucket) {
			event.data.bucketSelect(bucket, event.data.bar);
		}
	};
	
	/* TEXT TO FILTER */
	Mouse.prototype.onPhrasesDblClick = function(event){	
		event.stopImmediatePropagation();
		if(window.getSelection()){
			event.data.filterSelect(window.getSelection().toString().trim());
		}
	}

	/* UTIL */
	Mouse.prototype.getMousePosition = function(event){
		//-1 hack pour la bordure de 1px
		return event.clientX-1;
	}

	Time.Mouse = Mouse;
})();