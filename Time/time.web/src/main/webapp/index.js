var scales = {
	ONE : {
		multiplier : 1,
		value : "ONE"
	},
	TEN : {
		sublevel : "ONE",
		multiplier : 10,
		value : "TEN"
	},
	TEN3 : {
		sublevel : "TEN",
		multiplier : 10000
	},
	TEN6 : {
		sublevel : "TEN3",
		multiplier : 10000000
	},
	TEN9 : {
		sublevel : "TEN6",
		label : "TEN9",
		multiplier : 10000000000
	},
};

$(document).ready(function() {

	$(".phrasesMark").hide();
	$(".reindexingMark").hide();

	goFacetsAll();
	
	$( ".filtrerBt" ).click(function() {
		goFacetsAll();
	});
});

function goFacetsAll(){
	$.get("facets/", {
		scale : scales.TEN9.label,
		word : $(".wordInput").val()
	}).done(function(facetgroup) {
		$(".facetingMark").hide();
		facetsIsComing(facetgroup);
	});
}


function goFacets(scale, bucket) {
	$(".facetingMark").show();
	$.get("facets/", {
		scale : scale,
		bucket : bucket,
		word : $(".wordInput").val()
	}).done(function(facetgroup) {
		$(".facetingMark").hide();
		facetsIsComing(facetgroup);
	});
}
function facetsIsComing(facetgroup) {
	var scale = facetgroup.scale;
	var facetsDiv = $(".facets"+scale);
	facetsDiv.empty();
	
	var sublevel = scales[scale].sublevel;
	while(sublevel != undefined){
		$(".facets"+sublevel).empty();
		sublevel = scales[sublevel].sublevel;
	}
	$('.phrases').empty();
	
	
	for (facetIndex in facetgroup.facets) {
		facetsDiv.append(facetToHtml(facetgroup.facets[facetIndex], scale));
	}
}

function facetToHtml(facet, scale) {
	var bucket = facet.date;
	var subscale = scales[scale].sublevel;
	if (scale == scales.TEN.value) {
		return "<p onclick='goPhrases(\"" + subscale + "\"," + bucket + ")'>" + facet.date + " => " + facet.count + "</p>";
	} else {
		return "<p onclick='goFacets(\"" + subscale + "\"," + bucket + ")'>" + facet.date + " => " + facet.count + "</p>";
	}
}





function goPhrases(scale, bucket) {
	$(".phrasesMark").show();
	
	$.get("phrases/", {
		scale : scale,
		bucket : bucket,
		word : $(".wordInput").val()
	}).done(function(phrases) {
		$(".phrasesMark").hide();
		phrasesIsComing(phrases);
	});
}
function phrasesIsComing(phrases) {
	$('.phrases').empty();
	for (facetIndex in phrases) {
		$(".phrases").append(phrasesToHtml(phrases[facetIndex]));
	}
}
function phrasesToHtml(phrase) {
	//+ phrase.id + " => " 
	return "<p>" + phrase.date + " => " + phrase.text + "</p>"
}
