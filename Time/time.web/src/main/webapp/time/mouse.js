(function() {
    function mouse(drawer) {
        this.drawer = drawer;
    }

    /* COMMON */
    mouse.prototype.install = function(filterSelect, scroll) {
        var data = {
            filterSelect : filterSelect,
            scroll : scroll
        };
        $('.phrases').on('dblclick.Textes', data, $.proxy(this.onPhrasesDblClick, this));
        $(window).on('scroll', data, $.proxy(this.onScroll, this));
    };

    mouse.prototype.installBar = function(bar, bucketSelect) {
        var data = {
            previousX : null,
            deltaX : null,
            bar : bar,
            move : false,
            bucketSelect : bucketSelect,
            drawer : this.drawer
        };
        $(bar.canvas).on('mouseenter.ViewportTooltip', data, $.proxy(this.onEnterTooltip, this));
        $(bar.canvas).on('mousemove.ViewportTooltip', data, $.proxy(this.onMoveTooltip, this));
        $(bar.canvas).on('mousedown.Viewport', data, $.proxy(this.onDownBar, this));
        $(bar.canvas).on('mouseout.ViewportTooltip', data, $.proxy(this.onOutTooltip, this));
    };

    /* VIEWPORTTOOLTIP */
    mouse.prototype.onEnterTooltip = function() {
        $(".tooltip").show();
    };
    mouse.prototype.onMoveTooltip = function(event) {
        var bar = event.data.bar;
        var mousePosition = this.getmousePosition(event);
        var bucketPosition = bar.getBucketPosition(mousePosition);
        var toolTipText = Scale.getTooltipText(bar.scale, bucketPosition);
        $(".tooltip").val(toolTipText);
        $(".tooltip").css({top: $(bar.canvas).position().top + 34, left: event.clientX+20, position:'absolute'});
    };
    mouse.prototype.onOutTooltip = function() {
        $(".tooltip").hide();
    };


    /* VIEWPORT */
    mouse.prototype.onDownBar = function(event) {
        event.data.previousX = event.clientX;
        $(window).on('mousemove.Viewport', event.data, $.proxy(this.onBarDrag, this));
        $(window).on('mouseup.Viewport', event.data, $.proxy(this.onBarUp, this));
    };

    mouse.prototype.onBarDrag = function(event) {
        event.data.bar.viewport.local += event.clientX - event.data.previousX;
        event.data.drawer.draw(event.data.bar);
        event.data.previousX = event.clientX;
        event.data.move = true;
    };

    mouse.prototype.onBarUp = function(event) {
        if (!event.data.move) {
            this.onmouseClick(event);
        }
        event.data.move = false;

        $(window).off('mousemove.Viewport');
        $(window).off('mouseup.Viewport');
    };

    /* BUCKET SELECT */
    mouse.prototype.onmouseClick = function(event) {
        var mousePosition = this.getmousePosition(event);
        var bucket = event.data.bar.searchBucketAt(mousePosition);
        if (bucket) {
            event.data.bucketSelect(bucket, event.data.bar);
        }
    };

    /* TEXT TO FILTER */
    mouse.prototype.onPhrasesDblClick = function(event) {
        event.stopImmediatePropagation();
        if (window.getSelection()) {
            event.data.filterSelect(window.getSelection().toString().trim());
        }
    };

    mouse.prototype.onScroll = function(event){
        event.data.scroll();
    };

    /* UTIL */
    mouse.prototype.getmousePosition = function(event) {
        // -1 hack pour la bordure de 1px
        return event.clientX - 1;
    };

    Time.Mouse = mouse;
})();