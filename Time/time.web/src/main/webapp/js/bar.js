(function() {
    function bar(scale) {
        this.height = 35;
        this.reducedHeight = 15;
        this.reducedOpacity = 0.3;
        this.reduced = false;
        this.scale = scale;
        this.isFirstBar = Time.scale.isFirstScale(this.scale);
        this.isLastBar = Time.scale.isLastScale(this.scale);
        this.viewport = new Time.Viewport(this.scale);
        this.buckets = [];
        this.context = Time.barFactory.buildCanvas(this.height, this.scale);
        this.canvas = this.context.canvas;
        this.amplitude = 20;
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

    /**
     * Cherche si un bucket est présent à l'endroit spécifié
     * @param mouseX coordonnée écran (event.clientX, window.mouseX)
     * @returns {*} Le bucket trouvé au plus près, ou null
     */
    bar.prototype.searchBucketAt = function(mouseX) {
        var offset = this.searchNear(mouseX);
        var barBucketX = offset + mouseX;
        var viewPortBucketX = this.barXToViewportX(barBucketX);
        var bucket = this.getBucketAt(viewPortBucketX);

        return bucket;
    };

    /**
     * Cherche dans le canvas de la barre.
     * @param mouseX Où chercher dans la barre
     * @returns {*} Un bucket le plus proche possible ou undefined (pour ne pas être additionné à d'autres valeurs) si rien trouvé.
     */
    bar.prototype.searchNear = function(mouseX) {
        var searchZone = this.context.getImageData(mouseX - this.amplitude, 10, 2 * this.amplitude, 1).data;
        var middle = this.amplitude;
        var found = null;
        var fillLevel = Time.barDrawer.fillLevel;
        for (var i = 0, j = 0; i < searchZone.length; i += 4) {
            var isNotWhite = searchZone[i] !== fillLevel || searchZone[i + 1] !== fillLevel || searchZone[i + 2] !== fillLevel;
            if (isNotWhite && (!found || Math.abs(j - middle) < Math.abs(found - middle))) {
                found = j;
            }
            j++;
        }
        return found ? found - middle : undefined;
    };

    /**
     * @param bucketX La position sur la barre du bucket à chercher.
     * @returns {*} Le premier bucket tel que {bucket.x === bucketX}
     */
    bar.prototype.getBucketAt = function(viewPortBucketX) {
        if(viewPortBucketX !== undefined && viewPortBucketX !== null) {
            for (var i = 0; i < this.buckets.length; i++) {
                var bucket = this.buckets[i];
                if (bucket.x === viewPortBucketX) {
                    return bucket;
                }
            }
        }
        return null;
    };

    bar.prototype.barXToViewportX = function(barX) {
        return barX - this.viewport.delta();
    };

    bar.prototype.loadBuckets = function(term, parentBucket) {
        Time.barLoading.startLoading(this);
        Time.barDrawer.focusOn(this);
        Time.data.getBuckets(term, this.scale, $.proxy(this.onBuckets, this));
        this.viewport.lookAt(parentBucket);
        Time.tooltips.decorate(null);
    };

    bar.prototype.onBuckets = function(bucketsDTO) {
        Time.barLoading.stopLoading();
        this.buckets = Time.barFactory.buildBuckets(bucketsDTO);
        Time.tooltips.decorate(this);
        if(this.buckets.length === 0){
            Time.barDrawer.hideBar(this);
            Time.tooltips.hideTooltips();
        }else if(this.buckets.length === 1){
            Time.barDrawer.hideBar(this);
            this.openSubBar(this.buckets[0]);
        }else{
            Time.barDrawer.drawBar(this);
        }
    };

    bar.prototype.mouseDownOnBar = function(event) {
        event.data.previousX = event.clientX;
        Time.view.window.on('mousemove.Viewport', event.data, $.proxy(this.onBarDrag, this));
        Time.view.window.on('mouseup.Viewport', event.data, $.proxy(this.onBarUp, this));
    };

    bar.prototype.onBarDrag = function(event) {
        var delta = event.clientX - event.data.previousX;
        if(delta > 0){
            this.viewport.addToLocal();
            Time.barDrawer.drawBar(this);
            event.data.previousX = event.clientX;
            event.data.move = true;
        }
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

    bar.prototype.focusOnEnter = function(){
        $(this.canvas).on('mouseenter.focusAtEnter', $.proxy(this.onEnter, this));
    };
    bar.prototype.onEnter = function(){
        Time.barDrawer.focusOn(this);
        Time.tooltips.decorate(this);
        $(this.canvas).off('mouseenter.focusAtEnter');
    };
    
    bar.prototype.firstBucket = function(){
        return this.buckets[0];
    }
    bar.prototype.lastBucket = function(){
        return this.buckets[this.buckets.length-1];
    }

    Time.Bar = bar;
})();