var date = 0;
var word = "";
var page = 0;
var sens = "avant";

$(document).ready(function() {
	$(".loadingMark").hide();
	$(".reindexingMark").hide();
	$(".facetingMark").hide();
});

function searchBtClick() {
	var date = $(".dateInput").val();
	$(".dateInput").empty();
	search(date);
	addHistoryButton(date);
}

function withDate(date) {
	this.date = date;
	search();
}

function search() {
	$(".loadingMark").show();
	word = $(".wordInput").val();
	var sens = $("input:checked").val();
	var jqxhr = $.get("find/", {
		date : date,
		word : word,
		sens : sens
	}).done(function(data) {
		$(".loadingMark").hide();
		dataIsComing(data);
	}).fail(function() {
	});
}

function reindexBtClick() {
	$(".reindexingMark").show();
	var jqxhr = $.get("reindex/").done(function(data) {
		$(".reindexingMark").hide();
	}).fail(function() {
	});
}

function facetsBtClick() {
	$(".facetingMark").show();
	word = $(".wordInput").val();
	var jqxhr = $.get("facets/", {
		scale : "TEN",
		word : word,
		page : 0
	}).done(function(data) {
		$(".facetingMark").hide();
		facetsIsComing(data);
	}).fail(function() {
	});
}

function addHistoryButton(date) {
	if (date != "") {
		$(".historyBts").append("<input type='button' onClick='search(" + date + ");' value='" + date + "'/>");
	}
}

function dataIsComing(data) {
	$('.phrases').empty();
	for (phraseIndex in data) {
		$(".phrases").append(asParagraph(data[phraseIndex]));
	}
}
function facetsIsComing(data) {
	$('.phrases').empty();
	for (facetIndex in data.facets) {
		$(".phrases").append(asParagraphFacet(data.facets[facetIndex]));
	}
}

function asParagraph(phrase) {
	return "<p>" + phrase.date + " => " + phrase.text + "</p>"
}
function asParagraphFacet(facet) {
	return "<p>" + facet.date + " => " + facet.count + "</p>"
}

function page(page) {
	this.page = page;
	search();
}
