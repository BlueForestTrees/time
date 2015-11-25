(function() {
    function mouse() {

    }

    mouse.prototype.installBar = function(bar, bucketSelect) {
        var data = {
            previousX : null,
            deltaX : null,
            bar : bar,
            move : false,
            bucketSelect : bucketSelect
        };
        $(bar.canvas).on('mouseenter.ViewportTooltip', data, $.proxy(this.onEnterTooltip, this));
        $(bar.canvas).on('mousemove.ViewportTooltip', data, $.proxy(this.onMoveTooltip, this));
        $(bar.canvas).on('mousedown.Viewport', data, $.proxy(this.onDownBar, this));
        $(bar.canvas).on('mouseout.ViewportTooltip', data, $.proxy(this.onOutTooltip, this));
    };

    /* VIEWPORTTOOLTIP */
    mouse.prototype.onEnterTooltip = function() {
        Time.view.toolTip.show();
    };
    mouse.prototype.onMoveTooltip = function(event) {
        var bar = event.data.bar;
        var mousePosition = this.getmousePosition(event);
        var bucketPosition = bar.getBucketPosition(mousePosition);
        var toolTipText = Scale.getTooltipText(bar.scale, bucketPosition);
        var toolTipTop = $(bar.canvas).position().top + 14;
        var toolTipLeft = event.clientX+20;

        Time.view.toolTip.val(toolTipText);
        Time.view.toolTip.css({top: toolTipTop, left: toolTipLeft, position:'absolute'});
    };
    mouse.prototype.onOutTooltip = function() {
        Time.view.toolTip.hide();
    };


    /* VIEWPORT */
    mouse.prototype.onDownBar = function(event) {
        event.data.previousX = event.clientX;
        Time.view.window.on('mousemove.Viewport', event.data, $.proxy(this.onBarDrag, this));
        Time.view.window.on('mouseup.Viewport', event.data, $.proxy(this.onBarUp, this));
    };

    mouse.prototype.onBarDrag = function(event) {
        event.data.bar.viewport.local += event.clientX - event.data.previousX;
        Time.drawer.drawBar(event.data.bar);
        event.data.previousX = event.clientX;
        event.data.move = true;
    };

    mouse.prototype.onBarUp = function(event) {
        if (!event.data.move) {
            this.onmouseClick(event);
        }
        event.data.move = false;

        Time.view.window.off('mousemove.Viewport');
        Time.view.window.off('mouseup.Viewport');
    };

    /* BUCKET SELECT */
    mouse.prototype.onmouseClick = function(event) {
        var mousePosition = this.getmousePosition(event);
        var bucket = event.data.bar.searchBucketAt(mousePosition);
        if (bucket) {
            event.data.bucketSelect(bucket, event.data.bar);
        }
    };

    /* UTIL */
    mouse.prototype.getmousePosition = function(event) {
        // -1 hack pour la bordure de 1px
        return event.clientX - 1;
    };

    Time.Mouse = mouse;
})();