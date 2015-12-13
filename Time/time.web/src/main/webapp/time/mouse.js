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
        $(bar.canvas).on('mouseenter.ViewportTooltip', data, $.proxy(this.mouseEnterOnBar, this));
        $(bar.canvas).on('mousemove.ViewportTooltip', data, $.proxy(this.mouseMoveOnBar, this));
        $(bar.canvas).on('mousedown.Viewport', data, $.proxy(this.mouseDownOnBar, this));
        $(bar.canvas).on('mouseout.ViewportTooltip', data, $.proxy(this.mouseExitOfBar, this));
    };

    mouse.prototype.mouseEnterOnBar = function() {
        Time.view.toolTip.show();
    };
    mouse.prototype.mouseMoveOnBar = function(event) {
        var bar = event.data.bar;
        var mousePosition = this.getmousePosition(event);
        var bucketPosition = bar.getBucketPosition(mousePosition);
        var bucket = event.data.bar.searchBucketAt(mousePosition);
        var toolTipText = Scale.getTooltipText(Scale.getYearsSB(bar.scale, bucketPosition), bucket);
        var toolTipTop = $(bar.canvas).position().top + 5;
        var toolTipLeft = event.clientX + 20;
        var width = ((toolTipText.length + 1) * 6) + 'px';

        Time.view.toolTip.val(toolTipText);
        Time.view.toolTip.css({
            top : toolTipTop,
            left : toolTipLeft,
            width : width,
            position : 'absolute'
        });
    };
    mouse.prototype.mouseExitOfBar = function() {
        Time.view.toolTip.hide();
    };

    mouse.prototype.mouseDownOnBar = function(event) {
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
            this.onBarClick(event);
        }
        event.data.move = false;

        Time.view.window.off('mousemove.Viewport');
        Time.view.window.off('mouseup.Viewport');
    };

    mouse.prototype.onBarClick = function(event) {
        var mousePosition = this.getmousePosition(event);
        var bucket = event.data.bar.searchBucketAt(mousePosition);
        if (bucket) {
            event.data.bucketSelect(bucket, event.data.bar);
        }
    };

    mouse.prototype.getmousePosition = function(event) {
        // -1 hack pour la bordure de 1px
        return event.clientX - 1;
    };

    Time.Mouse = mouse;
})();