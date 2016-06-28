(function() {
    function bar(scale) {
        this.height = 35;
        this.reducedHeight = 15;
        this.reducedOpacity = 0.25;
        this.reduced = false;
        this.scale = scale;
        this.isFirstBar = Time.scale.isFirstScale(this.scale);
        this.isLastBar = Time.scale.isLastScale(this.scale);
        this.viewport = new Time.Viewport(this.scale);
        this.buckets = [];
        this.context = Time.barFactory.buildCanvas(this.height, this.scale);
        this.canvas = this.context.canvas;
        this.amplitude = 10;
        this.loading = false;
        installEvents(this);
    }

    var installEvents = function(bar) {
        var data = {
            previousX : null,
            deltaX : null,
            move : false
        };
        $(bar.canvas).on('mousedown.Viewport', data, $.proxy(bar.mouseDownOnBar, bar));
    };

    /**
     * Cherche si un bucket est présent au plus près possible de l'endroit spécifié
     * @param mouseX coordonnée écran (event.clientX, window.mouseX)
     * @returns {*} Un objet contenant le nombre de buckets trouvés pendant la recheerche, et le bucket trouvé au plus près, ou null
     */
    bar.prototype.searchBucketAt = function(mouseX) {
        var bucket = null;
        
        var searchResult = this.searchNear(mouseX);
        if(searchResult !== null){
            var offset = searchResult.offset;
            var barBucketX = offset + mouseX;
            var viewPortBucketX = this.barXToViewportX(barBucketX);
            bucket = this.getBucketAt(viewPortBucketX);            
        }

        return bucket !== null ? {bucket : bucket, count : searchResult.count} : null;
    };

    /**
     * Cherche dans le canvas de la barre.
     * @param mouseX Où chercher dans la barre
     * @returns {*} Un objet avec le nombre de bucket trouvé et le bucket le plus proche possible ou null si rien trouvé.
     */
    bar.prototype.searchNear = function(mouseX) {
        var searchZone = this.context.getImageData(mouseX - this.amplitude, 10, 2 * this.amplitude, 1).data;
        var middle = this.amplitude;
        var found = null;
        var fillLevel = Time.barDrawer.fillLevel;
        var foundCount = 0;
        for (var i = 0, j = 0; i < searchZone.length; i += 4) {
            var bucket = this.bucketAt(searchZone, i);
            if (bucket){
                foundCount++;
                if(!found || Math.abs(j - middle) < Math.abs(found - middle)) {
                    found = j;
                }
            }
            j++;
        }
        return found ? {count : foundCount, offset : found - middle} : null;
    };
    
    /**
     * Cherche dans le canvas de la barre à droite de la position.
     * @param mouseX Où chercher dans la barre
     * @param amplitude la taille de la recherche
     * @returns {*} Un bucket le plus proche possible à droite ou undefined (pour ne pas être additionné à d'autres valeurs) si rien trouvé.
     */
    bar.prototype.searchRightOf = function(mouseX, amplitude) {
        var searchZone = this.context.getImageData(mouseX, 10, amplitude, 1).data;
        var found = undefined;
        for (var i = 0, j = 0; i < searchZone.length; i += 4) {
            if(this.bucketAt(searchZone, i)){
                found = mouseX + j;
                break;
            }
            j++;
        }
        return found;
    };
    
    bar.prototype.bucketAt = function(searchZone, i){
        var fillLevel = Time.barDrawer.fillLevel;
        return searchZone[i] !== fillLevel || searchZone[i + 1] !== fillLevel || searchZone[i + 2] !== fillLevel;
    };
    
    /**
     * Cherche dans le canvas de la barre à gauche de la position (exclu).
     * @param mouseX Où chercher dans la barre
     * @param amplitude la taille de la recherche
     * @returns {*} Un bucket le plus proche possible à droite ou undefined (pour ne pas être additionné à d'autres valeurs) si rien trouvé.
     */
    bar.prototype.searchLeftOf = function(mouseX, amplitude) {
        
        var searchZone = this.context.getImageData(mouseX - amplitude, 10, amplitude, 1).data;
        var fillLevel = Time.barDrawer.fillLevel;
        var found = undefined;
        //parcours de la searchZone en sens droite -> gauche
        for (var i = (amplitude - 1) * 4, j = 0; i >= 0; i -= 4) {
            if(this.bucketAt(searchZone, i)){
                found = mouseX - j - 1;
                break;
            }
            j++;
        }
        return found;
    };

    /**
     * @param bucketX La position sur la barre du bucket à chercher.
     * @returns {*} Le premier bucket tel que {bucket.x === bucketX} ou null
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
        if(this.buckets.length === 0){
            Time.barDrawer.hideBar(this);
            Time.tooltips.hideTooltips();
        }else if(this.buckets.length === 1){
            Time.barDrawer.hideBar(this);
            this.openSubBar(this.buckets[0]);
        }else{
            Time.barDrawer.drawBar(this);
            Time.tooltips.decorate(this);
        }
    };

    bar.prototype.mouseDownOnBar = function(event) {
        event.data.previousX = event.clientX;
        Time.view.window.on('mousemove.Viewport', event.data, $.proxy(this.onBarDrag, this));
        Time.view.window.on('mouseup.Viewport', event.data, $.proxy(this.onBarUp, this));
    };

    bar.prototype.onBarDrag = function(event) {
        var delta = event.clientX - event.data.previousX;
        if(delta !== 0){
            this.viewport.addToLocal(delta);
            Time.barDrawer.drawBar(this);
            event.data.previousX = event.clientX;
            event.data.move = true;
        }
    };

    bar.prototype.onBarUp = function(event) {
        if (!event.data.move) {
            this.onClick(event);
        }
        event.data.move = false;

        Time.view.window.off('mousemove.Viewport');
        Time.view.window.off('mouseup.Viewport');
    };

    bar.prototype.onClick = function(event) {
        var searchResult = this.searchBucketAt(event.clientX);
        if (searchResult) {
            var bucket = searchResult.bucket;
            Time.phrases.clearText();
            var bucketAlone = searchResult.count === 1 && bucket.count < 20;
            if (this.isLastBar || bucketAlone) {
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
    
    bar.prototype.locationOf = function(bucket){
        return this.viewport.delta() + bucket.x;
    }
    bar.prototype.firstLocation = function(){
        return this.locationOf(this.firstBucket());
    }
    bar.prototype.lastLocation = function(){
        return this.locationOf(this.lastBucket());
    }

    Time.Bar = bar;
})();