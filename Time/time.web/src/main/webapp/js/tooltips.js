(function() {
    function tooltips() {
        this.currentBar = null;
    }

    tooltips.prototype.install = function(bars) {
        bars.forEach(function(bar) {
            $(bar.canvas).on('mouseenter.ViewportTooltip', bar, this.mouseEnterOnBar);
            $(bar.canvas).on('mousemove.ViewportTooltip', bar, this.mouseMoveOnBar);
            $(bar.canvas).on('mouseout.ViewportTooltip', bar, this.mouseExitOfBar);
        }, this);
    };

    tooltips.prototype.getNbPages = function(nbPhrases) {
        var phrasesParPage = 23;
        return nbPhrases ? " (" + (nbPhrases > phrasesParPage ? (1 + (Math.ceil(nbPhrases / phrasesParPage)) + " page") : (nbPhrases + " phrase")) + (nbPhrases > 1 ? "s" : "") + ")" : "";
    };

    tooltips.prototype.getTooltipText = function(bucket) {
        var years = Time.scale.bucketToYears(bucket);
        var start = years > 0 ? 'Dans ' : 'Il y a ';
        var echelle = Time.scale.getEchelle(years);
        var end = Time.tooltips.getNbPages(bucket.count);
        switch (echelle) {
        case Time.scale.echelles.milliard:
            var nbard = Math.abs(Math.round(years / Time.scale.echelles.milliard));
            return start + nbard + " milliard" + (nbard > 1 ? "s" : "") + " d'années" + end;
        case Time.scale.echelles.million:
            var nbon = Time.scale.dec(years / Time.scale.echelles.million);
            return start + nbon + " million" + (nbon > 1 ? "s" : "") + " d'années" + end;
        case Time.scale.echelles.millier:
        case Time.scale.echelles.un:
            var roundYears = Math.round(years);
            if (roundYears === 0)
                return "De nos jours" + end;
            else
                return "en " + roundYears + end;
            break;
        default:
            return 'WWWOOOOOWWW';
        }
    };

    // SUR LA BARRE CONTENANT LA SOURIS
    tooltips.prototype.mouseEnterOnBar = function() {
        Time.view.mouseBarTip.show();
    };
    tooltips.prototype.mouseMoveOnBar = function(event) {
        var bar = event.data;
        var mousePosition = bar.getmousePosition(event);
        var bucket = bar.searchBucketAt(mousePosition);
        // Si on a pas de bucket à la souris on en crée un factice pour générer
        // le tooltip quand même.
        if (bucket === null) {
            bucket = {
                bucket : bar.getBucketPosition(mousePosition),
                scale : bar.scale
            };
        }
        var toolTipText = Time.tooltips.getTooltipText(bucket);
        var toolTipTop = $(bar.canvas).position().top + 7;
        var toolTipLeft = event.clientX + 20;
        var width = ((toolTipText.length + 1) * 9) + 'px';

        Time.view.mouseBarTip.text(toolTipText);
        Time.view.mouseBarTip.css({
            top : toolTipTop,
            left : toolTipLeft,
            width : width,
            opacity : 1
        });
    };
    tooltips.prototype.mouseExitOfBar = function() {
        Time.view.mouseBarTip.hide();
    };

    // DESSOUS DE LA BARRE ACTIVE
    tooltips.prototype.decorate = function(bar) {
        if (this.currentBar) {
            this.currentBar.viewport.setListener(null);
        }
        this.currentBar = bar;
        this.currentBar.viewport.setListener(this.onCurrentBarChange);
        this.onCurrentBarChange();
    };

    tooltips.prototype.onCurrentBarChange = function() {
        var width = window.innerWidth;
        Time.tooltips.toolTipAt(Time.view.activeBarTips[0], 0.1 * width);
        Time.tooltips.toolTipAt(Time.view.activeBarTips[1], 0.45 * width);
        Time.tooltips.toolTipAt(Time.view.activeBarTips[2], 0.8 * width);
    };

    tooltips.prototype.toolTipAt = function(tooltip, at) {
        var scale = Time.tooltips.currentBar.scale;
        var bucketPosition = Time.tooltips.currentBar.getBucketPosition(Time.tooltips.currentBar.getmousePosition({
            clientX : at
        }));
        var toolTipText = Time.tooltips.getTooltipText({
            scale : scale,
            bucket : bucketPosition
        });
        var toolTipTop = $(Time.tooltips.currentBar.canvas).position().top + 9 + Time.tooltips.currentBar.height;
        // 15 => position before/after
        var toolTipLeft = at - 15;
        var width = ((toolTipText.length + 1) * 8) + 'px';

        tooltip.text(toolTipText);
        tooltip.css({
            top : toolTipTop,
            left : toolTipLeft,
            width : width,
            opacity : 1
        });
    };

    Time.Tooltip = tooltips;
})();