(function() {
    function bar(scale) {
        this.scale = scale;
        this.viewport = new Time.Viewport();
        this.buckets = [];
        this.context = new Time.CanvasFactory().build(50, scale);
        this.canvas = this.context.canvas;
        this.amplitude = 10;
    }

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
        Time.data.getBuckets(term, this.scale, $.proxy(this.onBuckets, this));
        this.lookAt(parentBucket);
    };

    bar.prototype.lookAt = function(parentBucket) {
        this.viewport.local = -Scale.firstSubBucket(Scale.up(this.scale), parentBucket);
    };

    bar.prototype.onBuckets = function(bucketsDTO) {
        this.buckets = Time.bucketFactory.getBuckets(bucketsDTO);
        Time.drawer.drawShowBar(this);
    };

    Time.Bar = bar;
})();