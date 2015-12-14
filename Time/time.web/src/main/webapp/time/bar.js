(function() {
    function bar(scale) {
        this.scale = scale;
        this.isLastBar = this.scale === Scale.TEN;
        this.viewport = new Time.Viewport(this.scale);
        this.buckets = [];
        this.context = new Time.CanvasFactory().build(35, scale);
        this.canvas = this.context.canvas;
        this.amplitude = 10;
        this.installEvents();
    }

    bar.prototype.installEvents = function() {
        var data = {
            previousX : null,
            deltaX : null,
            move : false
        };
        $(this.canvas).on('mouseenter.ViewportTooltip', data, this.mouseEnterOnBar);
        $(this.canvas).on('mousemove.ViewportTooltip', data, $.proxy(this.mouseMoveOnBar, this));
        $(this.canvas).on('mousedown.Viewport', data, $.proxy(this.mouseDownOnBar, this));
        $(this.canvas).on('mouseout.ViewportTooltip', data, $.proxy(this.mouseExitOfBar, this));
        $(this.canvas).on('dblclick.bucket', $.proxy(this.onBarDblClick, this));
    };

    bar.prototype.searchBucketAt = function(mousePosition) {
        var searchedBucketPosition = this.getBucketPosition(mousePosition);
        var imageData = this.context.getImageData(mousePosition - this.amplitude, 10, 2 * this.amplitude, 1);
        var offset = this.searchIn(imageData);
        if (offset !== null) {
            var bucket = this.getBucketAt(offset + searchedBucketPosition);
            return bucket;
        } else {
            return null;
        }
    };

    bar.prototype.getBucketPosition = function(mousePosition) {
        return mousePosition - this.viewport.delta();
    };

    bar.prototype.getBucketAt = function(xBucket) {
        for (var i = 0; i < this.buckets.length; i++) {
            var bucket = this.buckets[i];
            if (bucket.x === xBucket) {
                return bucket;
            }
        }
        return null;
    };

    bar.prototype.searchIn = function(imageData) {
        var middle = this.amplitude;
        var data = imageData.data;
        var found = null;
        for (var i = 0, j = 0; i < data.length; i += 4) {
            var isNotWhite = data[i] < 255 || data[i + 1] < 255 || data[i + 2] < 255;
            if (isNotWhite && (!found || Math.abs(j - middle) < Math.abs(found - middle))) {
                found = j;
            }
            j++;
        }

        return found ? found - middle : null;
    };

    bar.prototype.loadBuckets = function(term, parentBucket) {
        Time.view.throbber.show();
        Time.data.getBuckets(term, this.scale, $.proxy(this.onBuckets, this));
        this.viewport.lookAt(parentBucket);
    };

    bar.prototype.onBuckets = function(bucketsDTO) {
        Time.view.throbber.hide();
        this.buckets = Time.bucketFactory.getBuckets(bucketsDTO);
        Time.drawer.drawShowBar(this);
    };

    bar.prototype.mouseEnterOnBar = function() {
        Time.view.toolTip.show();
    };
    bar.prototype.mouseMoveOnBar = function(event) {
        var mousePosition = this.getmousePosition(event);
        var bucketPosition = this.getBucketPosition(mousePosition);
        var bucket = this.searchBucketAt(mousePosition);
        var toolTipText = Scale.getTooltipText(Scale.getYearsSB(this.scale, bucketPosition), bucket);
        var toolTipTop = $(this.canvas).position().top + 5;
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
    bar.prototype.mouseExitOfBar = function() {
        Time.view.toolTip.hide();
    };

    bar.prototype.mouseDownOnBar = function(event) {
        event.data.previousX = event.clientX;
        Time.view.window.on('mousemove.Viewport', event.data, $.proxy(this.onBarDrag, this));
        Time.view.window.on('mouseup.Viewport', event.data, $.proxy(this.onBarUp, this));
    };

    bar.prototype.onBarDrag = function(event) {
        this.viewport.local += event.clientX - event.data.previousX;
        Time.drawer.drawBar(this);
        event.data.previousX = event.clientX;
        event.data.move = true;
    };

    bar.prototype.onBarUp = function(event) {
        if (!event.data.move) {
            this.onBarClick(event);
        }
        event.data.move = false;

        Time.view.window.off('mousemove.Viewport');
        Time.view.window.off('mouseup.Viewport');
    };

    bar.prototype.onBarClick = function(event) {
        var bucket = this.searchBucketAt(this.getmousePosition(event));
        if (bucket) {
            if (this.isLastBar) {
                this.beginStory(bucket);
            } else {
                this.openSubBar(bucket);
            }
        }
    };

    bar.prototype.openSubBar = function(bucket) {
        Time.drawer.hideBars(Time.bars.indexOf(this) + 1);
        var subBar = Time.bars[Time.bars.indexOf(this) + 1];
        subBar.loadBuckets(Time.filter.term, bucket.bucket);
    };

    bar.prototype.onBarDblClick = function(event) {
        var bucket = this.searchBucketAt(this.getmousePosition(event));
        if (bucket) {
            this.beginStory(bucket);
        }
    };

    bar.prototype.beginStory = function(bucket) {
        Time.phrases.clearText();
        Time.drawer.setPhraseTooltip(Scale.getTooltipText(Scale.getYearsSB(this.scale, bucket.x)));
        Time.phrases.loadPhrases(this.scale, bucket.x);
    };

    bar.prototype.getmousePosition = function(event) {
        // -1 hack pour la bordure de 1px
        return event.clientX - 1;
    };

    Time.Bar = bar;
})();