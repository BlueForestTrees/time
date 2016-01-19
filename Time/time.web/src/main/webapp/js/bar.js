(function() {
    function bar(scale) {
        this.height = 35;
        this.reducedHeight = 10;
        this.scale = scale;
        this.isFirstBar = Time.scale.isFirstScale(this.scale);
        this.isLastBar = Time.scale.isLastScale(this.scale);
        this.viewport = new Time.Viewport(this.scale);
        this.buckets = [];
        this.context = new Time.CanvasFactory().build(this.height, this.scale);
        this.canvas = this.context.canvas;
        this.amplitude = 10;
        this.loading = false;
        this.installEvents();
    }

    bar.prototype.installEvents = function() {
        var data = {
            previousX : null,
            deltaX : null,
            move : false
        };
        $(this.canvas).on('mousedown.Viewport', data, $.proxy(this.mouseDownOnBar, this));
    };

    bar.prototype.searchBucketAt = function(mouseX) {
        var barX = this.mouseXToBarX(mouseX);
        var offset = this.searchNearest(barX);
        var viewportX = this.barXToViewportX(barX);
        var bucketX = viewportX + offset;
        var bucket = null;
        if (offset !== null) {
            bucket = this.getBucketAt(bucketX);
        }

        console.log({mouseX : mouseX, barX : barX, offset : offset, viewportX : viewportX, bucketX : bucketX, bucket : bucket});

        return bucket;
    };

    bar.prototype.searchNearest = function(mousePosition) {
        var searchZone = this.context.getImageData(mousePosition - this.amplitude, 10, 2 * this.amplitude, 1).data;
        var middle = this.amplitude;
        var found = null;
        var fillLevel = Time.drawer.fillLevel;
        for (var i = 0, j = 0; i < searchZone.length; i += 4) {
            var isNotWhite = searchZone[i] !== fillLevel || searchZone[i + 1] !== fillLevel || searchZone[i + 2] !== fillLevel;
            if (isNotWhite && (!found || Math.abs(j - middle) < Math.abs(found - middle))) {
                found = j;
            }
            j++;
        }
        return found ? found - middle : null;
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
    bar.prototype.barXToViewportX = function(position) {
        return position - this.viewport.delta();
    };

    bar.prototype.loadBuckets = function(term, parentBucket) {
        Time.barloading.startLoading(this);
        Time.drawer.focusOn(this);
        Time.data.getBuckets(term, this.scale, $.proxy(this.onBuckets, this));
        this.viewport.lookAt(parentBucket);
        Time.tooltips.decorate(null);
    };

    bar.prototype.onBuckets = function(bucketsDTO) {
        Time.barloading.stopLoading();
        this.buckets = Time.bucketFactory.getBuckets(bucketsDTO);
        Time.tooltips.decorate(this);
        if(this.buckets.length === 0){
            Time.drawer.hideBar(this);
            Time.tooltips.hideTooltips();
        }else if(this.buckets.length === 1){
            Time.drawer.hideBar(this);
            this.openSubBar(this.buckets[0]);
        }else{
            Time.drawer.drawBar(this);
        }
    };

    bar.prototype.mouseDownOnBar = function(event) {
        event.data.previousX = event.clientX;
        Time.view.window.on('mousemove.Viewport', event.data, $.proxy(this.onBarDrag, this));
        Time.view.window.on('mouseup.Viewport', event.data, $.proxy(this.onBarUp, this));
    };

    bar.prototype.onBarDrag = function(event) {
        this.viewport.addToLocal(event.clientX - event.data.previousX);
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
        var bucket = this.searchBucketAt(event.clientX);
        if (bucket) {
            if (this.isLastBar) {
                this.beginStory(bucket);
            } else {
                this.openSubBar(bucket);
            }
        }
    };

    bar.prototype.openSubBar = function(bucket) {
        Time.bars[this.scale + 1].loadBuckets(Time.filter.term, bucket.bucket);
    };

    bar.prototype.beginStory = function(bucket) {
        Time.phrases.clearText();
        Time.phrases.loadPhrases(this.scale, bucket.x);
        Time.historic.pushState(Time.filter.term);
    };

    bar.prototype.mouseXToBarX = function(mouseX) {
        // -1 hack pour la bordure de 1px
        return mouseX - 1;
    };

    Time.Bar = bar;
})();