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
        $(this.canvas).on('dblclick.bucket', $.proxy(this.onBarDblClick, this));
    };

    bar.prototype.searchBucketAt = function(mousePosition) {
        var foundBucket = null;
        var offset = this.searchNearest(mousePosition);
        if (offset !== null) {
            foundBucket = this.getBucketAt(offset + this.getBucketPosition(mousePosition));
        }
        return foundBucket;
    };

    bar.prototype.searchNearest = function(mousePosition) {
        var searchZone = this.context.getImageData(mousePosition - this.amplitude, 10, 2 * this.amplitude, 1).data;
        var middle = this.amplitude;
        var found = null;
        for (var i = 0, j = 0; i < searchZone.length; i += 4) {
            var isNotWhite = searchZone[i] < 255 || searchZone[i + 1] < 255 || searchZone[i + 2] < 255;
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
    bar.prototype.getBucketPosition = function(position) {
        return position - this.viewport.delta();
    };

    bar.prototype.startLoading = function () {
        this.loading = true;
        this.loadingPhase(this.getLoadingArray());
    };
    
    var throbberWidth = 2000;
    var loadingSpeed = 60;
    bar.prototype.getLoadingArray = function(){
        return [
                {color:"#CDCDCD"},{color:"#CDCDCD"},{color:"#CDCDCD"},{color:"#CDCDCD"},{color:"#CDCDCD"},{color:"#CDCDCD"},
                {color:"#CDCDCD"},{color:"#CDCDCD"},{color:"#CDCDCD"},{color:"#CDCDCD"},{color:"#CDCDCD"},{color:"#CDCDCD"},
                {color:"#FFFFFF"}
                ];
    };
    bar.prototype.loadingPhase = function(loadingArray){
        if(this.loading) {
            Time.drawer.drawBar(this, this.getBucketArray(loadingArray));
            this.animateArray(loadingArray);
            var that = this;
            setTimeout(function(){that.loadingPhase(loadingArray);}, loadingSpeed);
        }
    };
    bar.prototype.getBucketArray = function(loadingArray){
        var nbBuckets = loadingArray.length;
        var gap = throbberWidth / nbBuckets;
        var throbberWidthHalf = throbberWidth / 2;
        var screenHalf = this.canvas.width / 2;
        var throbberX = screenHalf - throbberWidthHalf;

        return loadingArray.map(function(bucket, index){
            return {
                color : this.transformColor(bucket.color),
                x : -this.viewport.delta() + throbberX + gap*index
            };
        },this);
    };
    bar.prototype.animateArray = function(loadingArray){
        loadingArray.push(loadingArray.shift());
    };
    bar.prototype.transformColor = function(color){
        return color;
    };
    bar.prototype.stopLoading = function () {
        this.loading = false;
    };
    bar.prototype.loadBuckets = function(term, parentBucket) {
        this.startLoading();
        Time.drawer.focusOn(this);
        Time.data.getBuckets(term, this.scale, $.proxy(this.onBuckets, this));
        this.viewport.lookAt(parentBucket);
        Time.tooltips.decorate(null);
    };

    bar.prototype.onBuckets = function(bucketsDTO) {
        this.stopLoading();
        this.buckets = Time.bucketFactory.getBuckets(bucketsDTO);
        Time.drawer.drawBar(this, this.buckets);
        Time.tooltips.decorate(this);
    };

    bar.prototype.mouseDownOnBar = function(event) {
        event.data.previousX = event.clientX;
        Time.view.window.on('mousemove.Viewport', event.data, $.proxy(this.onBarDrag, this));
        Time.view.window.on('mouseup.Viewport', event.data, $.proxy(this.onBarUp, this));
    };

    bar.prototype.onBarDrag = function(event) {
        this.viewport.addToLocal(event.clientX - event.data.previousX);
        Time.drawer.drawBar(this, this.buckets);
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
        Time.bars[this.scale + 1].loadBuckets(Time.filter.term, bucket.bucket);
    };

    bar.prototype.onBarDblClick = function(event) {
        var bucket = this.searchBucketAt(this.getmousePosition(event));
        if (bucket) {
            this.beginStory(bucket);
        }
    };

    bar.prototype.beginStory = function(bucket) {
        Time.phrases.clearText();
        Time.phrases.loadPhrases(this.scale, bucket.x);
        Time.historic.pushState(Time.filter.term);
    };

    bar.prototype.getmousePosition = function(event) {
        // -1 hack pour la bordure de 1px
        return event.clientX - 1;
    };

    Time.Bar = bar;
})();