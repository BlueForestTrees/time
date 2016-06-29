(function() {
    function tooltips() {
        this.currentBar = null;
    }
	
	tooltips.prototype.getNbPhrases = function(nbPhrases) {
        return nbPhrases ? (nbPhrases + " phrase") + (nbPhrases > 1 ? "s" : "") : "";
    };

	tooltips.prototype.dayToHuman = function(day){
		var bucket = {
			years : Time.scale.daysToYears(day)
		};
		return Time.tooltips.bucketToHuman(bucket);
	};

    tooltips.prototype.bucketToHuman = function(bucket) {
        var years = Time.scale.bucketToYears(bucket);
        var start = years > 0 ? 'Dans ' : 'Il y a ';
        var echelle = Time.scale.getEchelle(years);
        var end = Time.tooltips.getNbPhrases(bucket.count);
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
    tooltips.prototype.mouseMoveOnBar = function(event) {
        Time.tooltips.updateTooltips(null, event.clientX);
    };

    // DESSOUS DE LA BARRE ACTIVE
    tooltips.prototype.decorate = function(bar) {
        if (this.currentBar) {
            this.undecorate(this.currentBar);
        }
        if(bar){
            this.currentBar = bar;

            $(bar.canvas).on('mousemove.Tooltip', bar, this.mouseMoveOnBar);
            $(bar.canvas).on('mouseout.Tooltip', bar, this.mouseOutOnBar);

            this.updateTooltips();
        }
    };
    
    tooltips.prototype.mouseOutOnBar = function (){
        Time.tooltips.updateTooltips();
    };

    tooltips.prototype.undecorate = function(bar) {
        $(bar.canvas).off('mousemove.Tooltip');
        $(bar.canvas).off('mouseout.Tooltip');
        this.hideTooltips();
    };

    tooltips.prototype.updateTooltips = function(animate, mouseX) {
        if(!Time.tooltips.currentBar){
            return;
        }
        
        var tooltipsXs = [];
        tooltipsXs[0] = Time.tooltips.currentBar.aLeftBucket();
        tooltipsXs[2] = Time.tooltips.currentBar.aRightBucket();
        tooltipsXs[1] = 0.5 * (tooltipsXs[0]+tooltipsXs[2]);
        
        if(mouseX){
            tooltipsXs[Time.tooltips.getNearest(mouseX, tooltipsXs)] = mouseX;
        }
        
        Time.tooltips.toolTipAt(Time.view.activeBarTips[0], tooltipsXs[0], animate);
        Time.tooltips.toolTipAt(Time.view.activeBarTips[1], tooltipsXs[1], animate);
        Time.tooltips.toolTipAt(Time.view.activeBarTips[2], tooltipsXs[2], animate);
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
    
    tooltips.prototype.showTooltips = function(){
        this.updateTooltips();
    };
    tooltips.prototype.hideTooltips = function(){
        Time.view.activeBarTips.forEach(function(tooltip){
            tooltip.css({opacity:0});
        });
    };
    
    tooltips.prototype.toolTipAt = function(tooltip, tooltipX, animate) {
        var humanDate = Time.tooltips.bucketToHuman({
            scale : Time.tooltips.currentBar.scale,
            bucket : Time.tooltips.currentBar.viewport.toBucketX(tooltipX)
        });
        var toolTipTop = $(Time.tooltips.currentBar.canvas).position().top + Time.tooltips.currentBar.height + 7;
        // 22 => position à l'arrache pour que la flèche du tooltip coincide avec la souris.
        var toolTipLeft = tooltipX - 22;
        var width = ((humanDate.length + 1) * 8) + 'px';

        tooltip.text(humanDate);
        
        var css = {
                top : toolTipTop,
                width : width,
                opacity : 1
            };
        if(animate){            
            tooltip.animate({left : toolTipLeft}, 200);
        }else{
            css.left = toolTipLeft;
        }
        tooltip.css(css);
    };

    Time.Tooltip = tooltips;
})();