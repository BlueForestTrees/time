(function () {
    function Bar() {
        this.height = 35;
        this.reducedHeight = 15;
        this.reducedOpacity = 0.25;
        this.reduced = false;
        this.scale = Time.scale.defaultScale();
        this.viewport = new Time.Viewport(this.scale);
        this.buckets = [];
        this.context = Time.barFactory.buildCanvas(this.height, this.scale);
        this.canvas = this.context.canvas;
        this.amplitude = 10;
        this.loading = false;

        this.installEvents = function () {
            var data = {
                previousX: null,
                deltaX: null,
                move: false
            };
            $(this.canvas).on('mousedown.Viewport', data, $.proxy(this._mouseDownOnBar, this));
            delete this.installEvents;
        };
        this.installEvents();
    }

    Bar.prototype.monoBucket = function(){
        return this.buckets !== null && this.buckets.length === 1;
    };

    Bar.prototype.middle = function(){
        return this.canvas.width * 0.5;
    };

    Bar.prototype.aLeftBucket = function(){
        return this._searchRightOf(0.05 * this.canvas.width);
    };

    Bar.prototype.aRightBucket = function(){
        return this._searchLeftOf(0.95 * this.canvas.width);
    };

    Bar.prototype.firstBucket = function(){
        return this.buckets[0];
    };
    Bar.prototype.lastBucket = function(){
        return this.buckets[this.buckets.length-1];
    };

    Bar.prototype.loadBuckets = function (term) {
        Time.barLoading.startLoading(this);
        Time.barDrawer.focusOn(this);
        Time.data.getBuckets(term, this.scale, $.proxy(this._onBuckets, this));
        Time.tooltips.decorate(null);
    };

    Bar.prototype.focusOnEnter = function () {
        $(this.canvas).on('mouseenter.focusAtEnter', $.proxy(this._onEnter, this));
    };

    Bar.prototype.normalize = function(){
        var firstBucketX;
        var lastBucketX;
        var firstCanvasX;
        var lastCanvasX;
        if(this.buckets.length === 0) {
            //rien à normaliser
            firstBucketX = -1;
            lastBucketX = 1;
            firstCanvasX = 0.1 * this.canvas.width;
            lastCanvasX = 0.9 * this.canvas.width;
        }else if(this.buckets.length === 1) {
            //un seul bucket, il faut le mettre au centre.
            this.canvas.width = window.innerWidth;
            firstBucketX = this.firstBucket().x - 1;
            lastBucketX = this.firstBucket().x + 1;
            firstCanvasX = 0.1 * this.canvas.width;
            lastCanvasX = 0.9 * this.canvas.width;
        }else if(this.buckets.length > 1){
            this.canvas.width = window.innerWidth;
            firstBucketX = this.firstBucket().x;
            lastBucketX = this.lastBucket().x;
            firstCanvasX = 0.1 * this.canvas.width;
            lastCanvasX = 0.9 * this.canvas.width;
        }
        this.viewport.normalize(firstBucketX, lastBucketX, firstCanvasX, lastCanvasX);
    };


    
    /**
     * Cherche dans le canvas de la barre à droite de la position.
     * @param mouseX Où chercher dans la barre
     * @param amplitude la taille de la recherche, ou null pour chercher sur toute la longueur
     * @returns {*} Un mouseX le plus proche possible à droite ou undefined (pour ne pas être additionné à d'autres valeurs) si rien trouvé.
     */
    Bar.prototype._searchRightOf = function (mouseX, amplitude) {
        amplitude = amplitude || this.canvas.width - mouseX;
        var searchZone = this.context.getImageData(mouseX, 10, amplitude, 1).data;
        var found = undefined;
        for (var i = 0, j = 0; i < searchZone.length; i += 4) {
            if (this._bucketAt(searchZone, i)) {
                found = mouseX + j;
                break;
            }
            j++;
        }
        return found;
    };

    /**
     * Cherche dans le canvas de la barre à gauche de la position (exclu).
     * @param mouseX Où chercher dans la barre
     * @param amplitude la taille de la recherche, ou null pour chercher sur toute la longueur
     * @returns {*} Un mouseX le plus proche possible à droite ou undefined (pour ne pas être additionné à d'autres valeurs) si rien trouvé.
     */
    Bar.prototype._searchLeftOf = function (mouseX, amplitude) {
        amplitude = amplitude || mouseX;
        var searchZone = this.context.getImageData(mouseX - amplitude, 10, amplitude, 1).data;
        var found = undefined;
        //parcours de la searchZone en sens droite -> gauche
        for (var i = (amplitude - 1) * 4, j = 0; i >= 0; i -= 4) {
            if (this._bucketAt(searchZone, i)) {
                found = mouseX - j - 1;
                break;
            }
            j++;
        }
        return found;
    };

    /**
     * Cherche dans le canvas de la barre.
     * @param mouseX Où chercher dans la barre
     * @returns {*} Un objet avec le nombre de bucket trouvé et l'offset vers le bucket le plus proche possible, ou null si rien trouvé.
     */
    Bar.prototype._searchNear = function (mouseX) {
        var searchZone = this.context.getImageData(mouseX - this.amplitude, 10, 2 * this.amplitude, 1).data;
        var middle = this.amplitude;
        var found = null;
        var foundCount = 0;
        for (var i = 0, j = 0; i < searchZone.length; i += 4) {
            var bucket = this._bucketAt(searchZone, i);
            if (bucket) {
                foundCount++;
                if (!found || Math.abs(j - middle) < Math.abs(found - middle)) {
                    found = j;
                }
            }
            j++;
        }
        return found ? {count: foundCount, offset: found - middle} : null;
    };

    /**
     * Indique si la position contient un bucket ou pas.
     * @param searchZone un tébleau résulant de getImageData
     * @param i la position où regarder
     * @returns {boolean} vrai si la position ne contient pas la couleur de fond de la barre.
     * @private
     */
    Bar.prototype._bucketAt = function (searchZone, i) {
        var fillLevel = Time.barDrawer.fillLevel;
        return searchZone[i] !== fillLevel || searchZone[i + 1] !== fillLevel || searchZone[i + 2] !== fillLevel;
    };

    /**
     * @param bucketX La position sur la barre du bucket à chercher.
     * @returns {*} Le premier bucket tel que {bucket.x === bucketX} ou null
     */
    Bar.prototype._getBucketAt = function (bucketX) {
        if (bucketX !== undefined && bucketX !== null) {
            for (var i = 0; i < this.buckets.length; i++) {
                var bucket = this.buckets[i];
                if (bucket.x === bucketX) {
                    return bucket;
                }
            }
        }
        return null;
    };

    Bar.prototype._onBuckets = function (bucketsDTO) {
        this.buckets = Time.barFactory.buildBuckets(bucketsDTO);
        Time.barLoading.stopLoading();
        Time.barDrawer.drawBar(this);
        Time.tooltips.decorate(this);
    };

    Bar.prototype._mouseDownOnBar = function (event) {
        event.data.downX = event.data.previousX = event.clientX;
        //TODO install Bar selection
        Time.view.window.on('mousemove.Viewport', event.data, $.proxy(this._onBarDrag, this));
        Time.view.window.on('mouseup.Viewport', event.data, $.proxy(this._onBarUp, this));
    };

    Bar.prototype._onBarDrag = function (event) {
        var increment = event.clientX - event.data.previousX;
        if (increment !== 0) {
            //TODO manage Bar selection
            event.data.previousX = event.clientX;
            event.data.move = true;
        }
    };

    Bar.prototype._onBarUp = function (event) {
        if (!event.data.move) {
            //plus rien ici?
            //this._onClick(event);
        }else{
            var minMouseX = Math.min(event.data.downX, event.clientX);
            var maxMouseX = Math.max(event.data.downX, event.clientX);
            var amplitude = maxMouseX - minMouseX;
            var leftMouseX = this._searchRightOf(minMouseX, amplitude);
            var rightMouseX = this._searchLeftOf(maxMouseX, amplitude);
            var leftBucket = this._getBucketAt(this.viewport.toBucketX(leftMouseX));
            var rightBucket = this._getBucketAt(this.viewport.toBucketX(rightMouseX));
            var leftFilter = Time.scale.bucketToFilter(leftBucket);
            var rightFilter = Time.scale.bucketToFilter(rightBucket);
            //TODO rebrancher
            Time.filter.onPeriodFilter("@1000", "@2000");
        }
        event.data.move = false;
        Time.view.window.off('mousemove.Viewport');
        Time.view.window.off('mouseup.Viewport');
    };

    Bar.prototype._onClick = function (event) {
        var searchResult = this._searchBucketAt(event.clientX);
        if (searchResult) {
            var bucket = searchResult.bucket;
            Time.phrases.clearText();
            var bucketAlone = searchResult.count === 1 && bucket.count < 20;
            if (bucketAlone) {
                this._beginStory(bucket);
            } else {
                this._openSubBar(bucket);
            }
        }
    };

    /**
     * Cherche si un bucket est présent au plus près possible de l'endroit spécifié
     * @param mouseX coordonnée écran (event.clientX, window.mouseX)
     * @returns {*} Un objet contenant le nombre de buckets trouvés pendant la recheerche, et le bucket trouvé au plus près, ou null
     */
    Bar.prototype._searchBucketAt = function (mouseX) {
        var bucket = null;
        var searchResult = this._searchNear(mouseX);
        if (searchResult !== null) {
            var bucketX = this.viewport.toBucketX(mouseX + searchResult.offset);
            bucket = this._getBucketAt(bucketX);
        }

        return bucket !== null ? {bucket: bucket, count: searchResult.count} : null;
    };

    Bar.prototype._beginStory = function (bucket) {
        Time.phrases.clearText();
        Time.phrases.loadPhrases(this.scale, bucket.x);
        Time.historic.pushState(Time.filter.term);
    };

    Bar.prototype._onEnter = function () {
        Time.barDrawer.focusOn(this);
        Time.tooltips.decorate(this);
        $(this.canvas).off('mouseenter.focusAtEnter');
    };

    Time.Bar = Bar;
})();