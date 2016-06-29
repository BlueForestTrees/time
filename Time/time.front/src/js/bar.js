(function () {
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

    bar.prototype.aLeftBucket = function(){
        return this._searchRightOf(0.05 * window.innerWidth, 2000);
    };

    bar.prototype.aRightBucket = function(){
        return this._searchLeftOf(0.9 * window.innerWidth, 2000);
    };

    bar.prototype.firstBucket = function(){
        return this.buckets[0];
    };
    bar.prototype.lastBucket = function(){
        return this.buckets[this.buckets.length-1];
    };

    bar.prototype.loadBuckets = function (term, parentBucket) {
        Time.barLoading.startLoading(this);
        Time.barDrawer.focusOn(this);
        Time.data.getBuckets(term, this.scale, $.proxy(this._onBuckets, this));
        this.viewport.lookAt(parentBucket);
        Time.tooltips.decorate(null);
    };

    bar.prototype.barXToViewportX = function (barX) {
        return barX - this.viewport.delta();
    };

    bar.prototype.focusOnEnter = function () {
        $(this.canvas).on('mouseenter.focusAtEnter', $.proxy(this._onEnter, this));
    };



    
    /**
     * Cherche dans le canvas de la barre à droite de la position.
     * @param mouseX Où chercher dans la barre
     * @param amplitude la taille de la recherche
     * @returns {*} Un bucket le plus proche possible à droite ou undefined (pour ne pas être additionné à d'autres valeurs) si rien trouvé.
     */
    bar.prototype._searchRightOf = function (mouseX, amplitude) {
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
     * @param amplitude la taille de la recherche
     * @returns {*} Un bucket le plus proche possible à droite ou undefined (pour ne pas être additionné à d'autres valeurs) si rien trouvé.
     */
    bar.prototype._searchLeftOf = function (mouseX, amplitude) {

        var searchZone = this.context.getImageData(mouseX - amplitude, 10, amplitude, 1).data;
        var fillLevel = Time.barDrawer.fillLevel;
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
     * @returns {*} Un objet avec le nombre de bucket trouvé et le bucket le plus proche possible ou null si rien trouvé.
     */
    bar.prototype._searchNear = function (mouseX) {
        var searchZone = this.context.getImageData(mouseX - this.amplitude, 10, 2 * this.amplitude, 1).data;
        var middle = this.amplitude;
        var found = null;
        var fillLevel = Time.barDrawer.fillLevel;
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
    bar.prototype._bucketAt = function (searchZone, i) {
        var fillLevel = Time.barDrawer.fillLevel;
        return searchZone[i] !== fillLevel || searchZone[i + 1] !== fillLevel || searchZone[i + 2] !== fillLevel;
    };

    /**
     * @param bucketX La position sur la barre du bucket à chercher.
     * @returns {*} Le premier bucket tel que {bucket.x === bucketX} ou null
     */
    bar.prototype._getBucketAt = function (viewPortBucketX) {
        if (viewPortBucketX !== undefined && viewPortBucketX !== null) {
            for (var i = 0; i < this.buckets.length; i++) {
                var bucket = this.buckets[i];
                if (bucket.x === viewPortBucketX) {
                    return bucket;
                }
            }
        }
        return null;
    };

    bar.prototype._onBuckets = function (bucketsDTO) {
        Time.barLoading.stopLoading();
        this.buckets = Time.barFactory.buildBuckets(bucketsDTO);
        if (this.buckets.length === 0) {
            Time.barDrawer.hideBar(this);
            Time.tooltips.hideTooltips();
        } else if (this.buckets.length === 1) {
            Time.barDrawer.hideBar(this);
            this._openSubBar(this.buckets[0]);
        } else {
            Time.barDrawer.drawBar(this);
            Time.tooltips.decorate(this);
        }
    };

    bar.prototype._mouseDownOnBar = function (event) {
        event.data.previousX = event.clientX;
        Time.view.window.on('mousemove.Viewport', event.data, $.proxy(this._onBarDrag, this));
        Time.view.window.on('mouseup.Viewport', event.data, $.proxy(this._onBarUp, this));
    };

    bar.prototype._onBarDrag = function (event) {
        var increment = event.clientX - event.data.previousX;
        if (increment !== 0) {
            this.viewport.move(increment);
            Time.barDrawer.drawBar(this);
            event.data.previousX = event.clientX;
            event.data.move = true;
        }
    };

    bar.prototype._onBarUp = function (event) {
        if (!event.data.move) {
            this._onClick(event);
        }
        event.data.move = false;

        Time.view.window.off('mousemove.Viewport');
        Time.view.window.off('mouseup.Viewport');
    };

    bar.prototype._onClick = function (event) {
        var searchResult = this._searchBucketAt(event.clientX);
        if (searchResult) {
            var bucket = searchResult.bucket;
            Time.phrases.clearText();
            var bucketAlone = searchResult.count === 1 && bucket.count < 20;
            if (this.isLastBar || bucketAlone) {
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
    bar.prototype._searchBucketAt = function (mouseX) {
        var bucket = null;

        var searchResult = this._searchNear(mouseX);
        if (searchResult !== null) {
            var offset = searchResult.offset;
            var barBucketX = offset + mouseX;
            var viewPortBucketX = this.barXToViewportX(barBucketX);
            bucket = this._getBucketAt(viewPortBucketX);
        }

        return bucket !== null ? {bucket: bucket, count: searchResult.count} : null;
    };

    bar.prototype._openSubBar = function (bucket) {
        Time.bars[this.scale + 1].loadBuckets(Time.filter.term, bucket.bucket);
    };

    bar.prototype._beginStory = function (bucket) {
        Time.phrases.clearText();
        Time.phrases.loadPhrases(this.scale, bucket.x);
        Time.historic.pushState(Time.filter.term);
    };

    bar.prototype._onEnter = function () {
        Time.barDrawer.focusOn(this);
        Time.tooltips.decorate(this);
        $(this.canvas).off('mouseenter.focusAtEnter');
    };

    Time.Bar = bar;
})();