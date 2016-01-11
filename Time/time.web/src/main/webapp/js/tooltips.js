(function() {
    function tooltips() {
        this.currentBar = null;
    }

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
    tooltips.prototype.mouseEnterOnBar = function(event) {
        //console.log(event.type, event.target);
    };
    tooltips.prototype.mouseMoveOnBar = function(event) {
        Time.tooltips.updateTooltips(event.clientX);
    };
    tooltips.prototype.mouseExitOfBar = function(event) {
        //console.log(event.type, event.target);
    };

    // DESSOUS DE LA BARRE ACTIVE
    tooltips.prototype.decorate = function(bar) {
        if (this.currentBar) {
            this.undecorate(this.currentBar);
        }
        this.currentBar = bar;
        this.currentBar.viewport.setListener(this.updateTooltips);

        $(bar.convas).on('mouseenter.Tooltip', bar, this.mouseEnterOnBar);
        $(bar.canvas).on('mousemove.Tooltip', bar, this.mouseMoveOnBar);
        $(bar.canvas).on('mouseout.Tooltip', bar, this.mouseExitOfBar);

        this.updateTooltips();
    };

    tooltips.prototype.undecorate = function(bar) {
        bar.viewport.setListener(null);
        $(bar.canvas).off('mouseenter.Tooltip');
        $(bar.canvas).off('mousemove.Tooltip');
        $(bar.canvas).off('mouseout.Tooltip');
    };

    tooltips.prototype.updateTooltips = function(mouseX) {
        var width = window.innerWidth;
        var tooltipsXs = [0.1 * width, 0.45 * width, 0.8 * width];
        if(mouseX){
            tooltipsXs[Time.tooltips.getNearest(mouseX, tooltipsXs)] = mouseX;
        }
        Time.tooltips.toolTipAt(Time.view.activeBarTips[0], tooltipsXs[0]);
        Time.tooltips.toolTipAt(Time.view.activeBarTips[1], tooltipsXs[1]);
        Time.tooltips.toolTipAt(Time.view.activeBarTips[2], tooltipsXs[2]);
    };

    tooltips.prototype.getNearest = function(mouseX, xS){
        return xS.map(function(x){return Math.abs(mouseX - x);}).reduce(function(nearest, current, index){
            if(current < nearest.distance){
                return {distance:current, index:index};
            }else{
                return nearest;
            }
        },{distance:1000000, index:null}).index;
    };
    
    tooltips.prototype.toolTipAt = function(tooltip, tooltipX) {
        var scale = Time.tooltips.currentBar.scale;
        var bucketPosition = Time.tooltips.currentBar.getBucketPosition(Time.tooltips.currentBar.getmousePosition({
            clientX : tooltipX
        }));
        var toolTipText = Time.tooltips.getTooltipText({
            scale : scale,
            bucket : bucketPosition
        });
        var toolTipTop = $(Time.tooltips.currentBar.canvas).position().top + 9 + Time.tooltips.currentBar.height;
        // 15 => position before/after
        var toolTipLeft = tooltipX - 15;
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