/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};

/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {

/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId])
/******/ 			return installedModules[moduleId].exports;

/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			exports: {},
/******/ 			id: moduleId,
/******/ 			loaded: false
/******/ 		};

/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);

/******/ 		// Flag the module as loaded
/******/ 		module.loaded = true;

/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}


/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;

/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;

/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "/";

/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(0);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */
/***/ function(module, exports, __webpack_require__) {

	__webpack_require__(1);
	//TODO mettre dans boot.js
	Time = {};
	__webpack_require__ (5);
	__webpack_require__ (6);
	__webpack_require__ (7);
	__webpack_require__ (8);
	__webpack_require__ (9);
	__webpack_require__ (10);
	//require ("./js/bar.js");
	//require ("./js/barDrawer.js");
	//require ("./js/barLoading.js");
	__webpack_require__ (11);
	__webpack_require__ (12);
	__webpack_require__ (13);
	//require ("./js/data.mock.js");
	__webpack_require__ (14);
	__webpack_require__ (15);
	__webpack_require__ (16);
	__webpack_require__ (17);

/***/ },
/* 1 */
/***/ function(module, exports, __webpack_require__) {

	// style-loader: Adds some css to the DOM by adding a <style> tag

	// load the styles
	var content = __webpack_require__(2);
	if(typeof content === 'string') content = [[module.id, content, '']];
	// add the styles to the DOM
	var update = __webpack_require__(4)(content, {});
	if(content.locals) module.exports = content.locals;
	// Hot Module Replacement
	if(false) {
		// When the styles change, update the <style> tags
		if(!content.locals) {
			module.hot.accept("!!../../node_modules/css-loader/index.js!./histoires.css", function() {
				var newContent = require("!!../../node_modules/css-loader/index.js!./histoires.css");
				if(typeof newContent === 'string') newContent = [[module.id, newContent, '']];
				update(newContent);
			});
		}
		// When the module is disposed, remove the <style> tags
		module.hot.dispose(function() { update(); });
	}

/***/ },
/* 2 */
/***/ function(module, exports, __webpack_require__) {

	exports = module.exports = __webpack_require__(3)();
	// imports
	exports.push([module.id, "@import url(http://maxcdn.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css);", ""]);

	// module
	exports.push([module.id, ".fa-search:before {\r\n\tcontent: url(/img/loupe.svg);\r\n}\r\n\r\nhtml, body {\r\n\twidth: 100%;\r\n\theight: 100%;\r\n\tmargin: 0px;\r\n\tbackground: #ffffff;\r\n}\r\n\r\n.textIntro h1 {\r\n\tmargin-bottom: 5px;\r\n}\r\n\r\np {\r\n\tmargin-top: 0px;\r\n\tmargin-bottom: 5px;\r\n}\r\n\r\n.home {\r\n\tbackground: #ffffff;\r\n\tposition: absolute;\r\n\ttop: 0px;\r\n\tleft: 0px;\r\n\twidth: 100%;\r\n\theight: 100%;\r\n}\r\n\r\n.homebox {\r\n\tposition: absolute;\r\n\ttop: 40%;\r\n\tleft: 50%;\r\n\ttransform: translate(-50%, -50%);\r\n}\r\n\r\n.titles {\r\n\ttop: 30%;\r\n\tposition: absolute;\r\n\ttransform: translate(-50%, -15%);\r\n}\r\n\r\n.logo {\r\n\tbackground-size:376px 130px;\r\n\theight:130px;\r\n\twidth:376px;\r\n\tposition: absolute;\r\n\ttop: 40%;\r\n\tleft: 50%;\r\n\ttransform: translate(-50%, -100%);\r\n\tbackground: url(/img/logo.png) no-repeat;\r\n}\r\n.search {\r\n\tposition: relative;\r\n\tdisplay: inline-block;\r\n\tcolor: #aaa;\r\n\tpadding-bottom: 3em;\r\n}\r\n\r\n.subtitle {\r\n\tcolor: #51598C;\r\n\tfont-weight: bold;\r\n\ttext-align: center;\r\n}\r\n\r\n.subtitle2{\r\n}\r\n.subtitle1{\r\n\tpadding-bottom: 2em;\r\n}\r\n\r\n.phraseHeader{\r\n\ttext-align: left;\r\n\tfont-size: small;\r\n\tmargin-top: 15px;\r\n}\r\n\r\n.search input {\r\n\twidth: 450px;\r\n\theight: 32px;\r\n\tpadding-right: 30px;\r\n\ttext-align: left;\r\n\tfont-family: Lucida Console;\r\n\tfont-size: 25px;\r\n\tborder: 1px solid #CDCDCD;\r\n\tborder-radius: 3px;\r\n\ttext-indent: 10px;\r\n}\r\n\r\nh1, h2, h3{\r\n\tfont-family: Lucida Console;\r\n}\r\n\r\n.search .fa-search {\r\n\tposition: absolute;\r\n\tright: 0px;\r\n}\r\n\r\n.top7 {\r\n\ttop: 7px;\r\n}\r\n\r\n.hand {\r\n\tcursor:pointer;\r\n}\r\n\r\n.timelines {\r\n\tline-height: 0px;\r\n}\r\n\r\n.header {\r\n\tbox-shadow: rgba(11, 17, 25, 0.25) 0 2px 5px;\r\n\tposition: fixed;\r\n\ttop: 0;\r\n\tright: 0;\r\n\tleft: 0;\r\n\tline-height: 55px;\r\n\ttext-align: center;\r\n\tbackground: #f2f2f2;\r\n}\r\n\r\n.logo-petit {\r\n    background-size:178px 134px;\r\n    height:55px;\r\n    width:250px;\r\n    position: absolute;\r\n    top: 0px;\r\n    left: 0px;\r\n    background: url(/img/logo-petit.png) no-repeat;\r\n}\r\n\r\n.throbber {\r\n\tbackground-size:32px 32px;\r\n\theight:32px;\r\n\twidth:32px;\r\n\tposition: absolute;\r\n\ttop: 10px;\r\n\tleft: 240px;\r\n\tbackground: url(/img/throbber.gif) no-repeat;\r\n}\r\n\r\n.content {\r\n\tdisplay : none;\r\n}\r\n.bottom {\r\n\theight : 80px;\r\n}\r\n.throbber{\r\n\theight: 400px;\r\n\ttext-align: center;\r\n\tdisplay : none;\r\n}\r\n\r\n.footer {\r\n    position: fixed;\r\n    bottom: 0;\r\n    right: 0;\r\n    left: 0;\r\n    line-height: 40px;\r\n    border-top: 1px solid #e4e4e4;\r\n    background: #f2f2f2;\r\n    color: #666\r\n}\r\n\r\n.footer .right{\r\n    float: right;\r\n    font-size: small;\r\n    font-family: arial,sans-serif;\r\n}\r\n.footer .right a{\r\n    margin-right: 30px;\r\n}\r\n\r\na:hover{\r\n\ttext-decoration: underline;\r\n\tcursor:pointer;\r\n}\r\n\r\n.footer .left{\r\n    float: left;\r\n    font-size: small;\r\n    font-family: arial,sans-serif;\r\n}\r\n.footer .left a{\r\n\tmargin-left: 30px;\r\n}\r\n\r\n.phrases {\r\n\tmargin-top: 150px;\r\n}\r\n\r\n.throbber {\r\n\tmargin-top: 0;\r\n\tvertical-align: middle;\r\n\tmargin-top: expression(( 150 - this.height)/2);\r\n}\r\n\r\n.bartip {\r\n\tposition: absolute;\r\n\ttext-align: center;\r\n\tbackground: #FFF;\r\n\tfont: italic normal 90% Georgia, serif;\r\n\theight: 20px;\r\n\tvisible: false;\r\n\topacity: 0;\r\n\tpointer-events: none;\r\n}\r\n\r\n.mousetip {\r\n\tborder: 0px solid;\r\n}\r\n\r\n.bartip {\r\n\tborder: 1px solid #CDCDCD;\r\n\tbox-shadow: rgba(11, 17, 25, 0.1) 0 2px 5px;\r\n}\r\n\r\n.bartip:before, .bartip:after {\r\n\tcontent: \"\";\r\n\tposition: absolute;\r\n\ttop: 0%;\r\n\tleft: 15px;\r\n\tz-index: 1;\r\n}\r\n\r\n.bartip:before {\r\n\tborder-bottom: 8px solid #CDCDCD;\r\n\tborder-left: 8px solid transparent;\r\n\tborder-top: 8px solid transparent;\r\n\tborder-right: 8px solid transparent;\r\n\tmargin-top: -16px;\r\n}\r\n\r\n.bartip:after {\r\n\tborder-bottom: 8px solid #FFF;\r\n\tborder-left: 8px solid transparent;\r\n\tborder-top: 8px solid transparent;\r\n\tborder-right: 8px solid transparent;\r\n\tmargin-top: -15px;\r\n}\r\n", ""]);

	// exports


/***/ },
/* 3 */
/***/ function(module, exports) {

	/*
		MIT License http://www.opensource.org/licenses/mit-license.php
		Author Tobias Koppers @sokra
	*/
	// css base code, injected by the css-loader
	module.exports = function() {
		var list = [];

		// return the list of modules as css string
		list.toString = function toString() {
			var result = [];
			for(var i = 0; i < this.length; i++) {
				var item = this[i];
				if(item[2]) {
					result.push("@media " + item[2] + "{" + item[1] + "}");
				} else {
					result.push(item[1]);
				}
			}
			return result.join("");
		};

		// import a list of modules into the list
		list.i = function(modules, mediaQuery) {
			if(typeof modules === "string")
				modules = [[null, modules, ""]];
			var alreadyImportedModules = {};
			for(var i = 0; i < this.length; i++) {
				var id = this[i][0];
				if(typeof id === "number")
					alreadyImportedModules[id] = true;
			}
			for(i = 0; i < modules.length; i++) {
				var item = modules[i];
				// skip already imported module
				// this implementation is not 100% perfect for weird media query combinations
				//  when a module is imported multiple times with different media queries.
				//  I hope this will never occur (Hey this way we have smaller bundles)
				if(typeof item[0] !== "number" || !alreadyImportedModules[item[0]]) {
					if(mediaQuery && !item[2]) {
						item[2] = mediaQuery;
					} else if(mediaQuery) {
						item[2] = "(" + item[2] + ") and (" + mediaQuery + ")";
					}
					list.push(item);
				}
			}
		};
		return list;
	};


/***/ },
/* 4 */
/***/ function(module, exports, __webpack_require__) {

	/*
		MIT License http://www.opensource.org/licenses/mit-license.php
		Author Tobias Koppers @sokra
	*/
	var stylesInDom = {},
		memoize = function(fn) {
			var memo;
			return function () {
				if (typeof memo === "undefined") memo = fn.apply(this, arguments);
				return memo;
			};
		},
		isOldIE = memoize(function() {
			return /msie [6-9]\b/.test(window.navigator.userAgent.toLowerCase());
		}),
		getHeadElement = memoize(function () {
			return document.head || document.getElementsByTagName("head")[0];
		}),
		singletonElement = null,
		singletonCounter = 0,
		styleElementsInsertedAtTop = [];

	module.exports = function(list, options) {
		if(false) {
			if(typeof document !== "object") throw new Error("The style-loader cannot be used in a non-browser environment");
		}

		options = options || {};
		// Force single-tag solution on IE6-9, which has a hard limit on the # of <style>
		// tags it will allow on a page
		if (typeof options.singleton === "undefined") options.singleton = isOldIE();

		// By default, add <style> tags to the bottom of <head>.
		if (typeof options.insertAt === "undefined") options.insertAt = "bottom";

		var styles = listToStyles(list);
		addStylesToDom(styles, options);

		return function update(newList) {
			var mayRemove = [];
			for(var i = 0; i < styles.length; i++) {
				var item = styles[i];
				var domStyle = stylesInDom[item.id];
				domStyle.refs--;
				mayRemove.push(domStyle);
			}
			if(newList) {
				var newStyles = listToStyles(newList);
				addStylesToDom(newStyles, options);
			}
			for(var i = 0; i < mayRemove.length; i++) {
				var domStyle = mayRemove[i];
				if(domStyle.refs === 0) {
					for(var j = 0; j < domStyle.parts.length; j++)
						domStyle.parts[j]();
					delete stylesInDom[domStyle.id];
				}
			}
		};
	}

	function addStylesToDom(styles, options) {
		for(var i = 0; i < styles.length; i++) {
			var item = styles[i];
			var domStyle = stylesInDom[item.id];
			if(domStyle) {
				domStyle.refs++;
				for(var j = 0; j < domStyle.parts.length; j++) {
					domStyle.parts[j](item.parts[j]);
				}
				for(; j < item.parts.length; j++) {
					domStyle.parts.push(addStyle(item.parts[j], options));
				}
			} else {
				var parts = [];
				for(var j = 0; j < item.parts.length; j++) {
					parts.push(addStyle(item.parts[j], options));
				}
				stylesInDom[item.id] = {id: item.id, refs: 1, parts: parts};
			}
		}
	}

	function listToStyles(list) {
		var styles = [];
		var newStyles = {};
		for(var i = 0; i < list.length; i++) {
			var item = list[i];
			var id = item[0];
			var css = item[1];
			var media = item[2];
			var sourceMap = item[3];
			var part = {css: css, media: media, sourceMap: sourceMap};
			if(!newStyles[id])
				styles.push(newStyles[id] = {id: id, parts: [part]});
			else
				newStyles[id].parts.push(part);
		}
		return styles;
	}

	function insertStyleElement(options, styleElement) {
		var head = getHeadElement();
		var lastStyleElementInsertedAtTop = styleElementsInsertedAtTop[styleElementsInsertedAtTop.length - 1];
		if (options.insertAt === "top") {
			if(!lastStyleElementInsertedAtTop) {
				head.insertBefore(styleElement, head.firstChild);
			} else if(lastStyleElementInsertedAtTop.nextSibling) {
				head.insertBefore(styleElement, lastStyleElementInsertedAtTop.nextSibling);
			} else {
				head.appendChild(styleElement);
			}
			styleElementsInsertedAtTop.push(styleElement);
		} else if (options.insertAt === "bottom") {
			head.appendChild(styleElement);
		} else {
			throw new Error("Invalid value for parameter 'insertAt'. Must be 'top' or 'bottom'.");
		}
	}

	function removeStyleElement(styleElement) {
		styleElement.parentNode.removeChild(styleElement);
		var idx = styleElementsInsertedAtTop.indexOf(styleElement);
		if(idx >= 0) {
			styleElementsInsertedAtTop.splice(idx, 1);
		}
	}

	function createStyleElement(options) {
		var styleElement = document.createElement("style");
		styleElement.type = "text/css";
		insertStyleElement(options, styleElement);
		return styleElement;
	}

	function createLinkElement(options) {
		var linkElement = document.createElement("link");
		linkElement.rel = "stylesheet";
		insertStyleElement(options, linkElement);
		return linkElement;
	}

	function addStyle(obj, options) {
		var styleElement, update, remove;

		if (options.singleton) {
			var styleIndex = singletonCounter++;
			styleElement = singletonElement || (singletonElement = createStyleElement(options));
			update = applyToSingletonTag.bind(null, styleElement, styleIndex, false);
			remove = applyToSingletonTag.bind(null, styleElement, styleIndex, true);
		} else if(obj.sourceMap &&
			typeof URL === "function" &&
			typeof URL.createObjectURL === "function" &&
			typeof URL.revokeObjectURL === "function" &&
			typeof Blob === "function" &&
			typeof btoa === "function") {
			styleElement = createLinkElement(options);
			update = updateLink.bind(null, styleElement);
			remove = function() {
				removeStyleElement(styleElement);
				if(styleElement.href)
					URL.revokeObjectURL(styleElement.href);
			};
		} else {
			styleElement = createStyleElement(options);
			update = applyToTag.bind(null, styleElement);
			remove = function() {
				removeStyleElement(styleElement);
			};
		}

		update(obj);

		return function updateStyle(newObj) {
			if(newObj) {
				if(newObj.css === obj.css && newObj.media === obj.media && newObj.sourceMap === obj.sourceMap)
					return;
				update(obj = newObj);
			} else {
				remove();
			}
		};
	}

	var replaceText = (function () {
		var textStore = [];

		return function (index, replacement) {
			textStore[index] = replacement;
			return textStore.filter(Boolean).join('\n');
		};
	})();

	function applyToSingletonTag(styleElement, index, remove, obj) {
		var css = remove ? "" : obj.css;

		if (styleElement.styleSheet) {
			styleElement.styleSheet.cssText = replaceText(index, css);
		} else {
			var cssNode = document.createTextNode(css);
			var childNodes = styleElement.childNodes;
			if (childNodes[index]) styleElement.removeChild(childNodes[index]);
			if (childNodes.length) {
				styleElement.insertBefore(cssNode, childNodes[index]);
			} else {
				styleElement.appendChild(cssNode);
			}
		}
	}

	function applyToTag(styleElement, obj) {
		var css = obj.css;
		var media = obj.media;
		var sourceMap = obj.sourceMap;

		if(media) {
			styleElement.setAttribute("media", media)
		}

		if(styleElement.styleSheet) {
			styleElement.styleSheet.cssText = css;
		} else {
			while(styleElement.firstChild) {
				styleElement.removeChild(styleElement.firstChild);
			}
			styleElement.appendChild(document.createTextNode(css));
		}
	}

	function updateLink(linkElement, obj) {
		var css = obj.css;
		var media = obj.media;
		var sourceMap = obj.sourceMap;

		if(sourceMap) {
			// http://stackoverflow.com/a/26603875
			css += "\n/*# sourceMappingURL=data:application/json;base64," + btoa(unescape(encodeURIComponent(JSON.stringify(sourceMap)))) + " */";
		}

		var blob = new Blob([css], { type: "text/css" });

		var oldSrc = linkElement.href;

		linkElement.href = URL.createObjectURL(blob);

		if(oldSrc)
			URL.revokeObjectURL(oldSrc);
	}


/***/ },
/* 5 */
/***/ function(module, exports) {

	
	$(document).ready(function() {
	    Time.view = {
	        termInput : $("#termInput"),
	        homeTermInput : $('#homeTermInput'),
	        bottom : $(".bottom"),
	        timeline : $(".timelines"),
	        phrases : $('.phrases'),
	        window : $(window),
	        activeBarTips : [ $("#activeBarTip1"), $("#activeBarTip2"), $("#activeBarTip3") ],
	        throbber : $(".throbber"),
	        home : $(".home"),
	        content : $(".content")
	    };
	    Time.anal.init();
	    Time.anal.ga('create', 'UA-70863369-1', 'auto');
	    Time.anal.ga('send', 'pageview', '/');
	    Time.histoires = new Time.Histoires();
	});

/***/ },
/* 6 */
/***/ function(module, exports) {

	(function() {

	    Time.anal = {};

	    Time.anal.init = function() {
	        if(document.location.hostname !== 'localhost'){
	            (function(i, s, o, g, r, a, m) {
	                i['GoogleAnalyticsObject'] = r;
	                i[r] = i[r] || function() {
	                    (i[r].q = i[r].q || []).push(arguments);
	                }, i[r].l = 1 * new Date();
	                a = s.createElement(o), m = s.getElementsByTagName(o)[0];
	                a.async = 1;
	                a.src = g;
	                m.parentNode.insertBefore(a, m);
	            })(window, document, 'script', '//www.google-analytics.com/analytics.js', 'ga');
	            Time.anal.on = true;
	        }else{
	            console.log('No GA since localhost');
	        }
	    };

	    Time.anal.ga = function(a, b, c, d, e) {
	        if (Time.anal.on) {
	            ga(a, b, c, d, e);
	        }
	    };

	})();

/***/ },
/* 7 */
/***/ function(module, exports) {

	(function() {
	    function Histoires() {
	        Time.scale = new Time.Scale();
	        Time.view.throbber.hide();
	        Time.barFactory = new Time.BarFactory();
	        //Time.bar = new Time.Bar();
	        //Time.barLoading = new Time.BarLoading();
	        //Time.barDrawer = new Time.BarDrawer();
	        Time.phrases = new Time.Phrases();
	        Time.phrasesdrawer = new Time.PhrasesDrawer();
	        Time.data = new Time.Data();
	        Time.filter = new Time.Filter();
	        Time.tooltips = new Time.Tooltip();

	        //Time.barDrawer.install();
	        Time.filter.install(Time.view);
	        Time.phrases.install();

	        Time.historic.popState();
	        window.onpopstate = Time.historic.popState;
	    }

	    Time.Histoires = Histoires;

	})();


/***/ },
/* 8 */
/***/ function(module, exports) {

	(function() {
	    Time.historic = {};

	    Time.historic.term = "";

	    Time.historic.popState = function() {
	        var url = document.location.href;

	        if(url.indexOf("/") > -1){
	            Time.historic.term = decodeURIComponent(url.split("/")[url.split("/").length-1].substring(1));
	        }
	        if (Time.historic.term) {
	            Time.filter.onFilterFromHome(Time.historic.term, true);
	        }
	    };

	    Time.historic.pushState = function(term) {
	        if (Time.historic.term !== term) {
	            history.pushState("", "", "/#" + encodeURIComponent(term));
	        }
	    };

	})();


/***/ },
/* 9 */
/***/ function(module, exports) {

	(function() {
	    function Viewport(_scale) {
	        this._local = 0;
	        this._zoom = 1;
	        this._scale = _scale;
	    }

	    Viewport.prototype.delta = function() {
	        return Math.round(this._local);
	    };

	    Viewport.prototype.toCanvasX = function (bucketX) {
	        // y = ax + b
	        return this._zoom * bucketX + this.delta();
	    };

	    Viewport.prototype.toBucketX = function (canvasX) {
	        // x = ( y - b ) / a
	        return Math.round(this.toBucketFloatX(canvasX));
	    };

	    Viewport.prototype.toBucketFloatX = function (canvasX){
	        return (canvasX - this.delta()) / this._zoom;
	    };

	    Viewport.prototype.normalize = function (x1, x2, y1, y2){
	        //on évalue a
	        this._zoom = (y2 - y1) / (x2 - x1);
	        //qu'on réutilise pour avoir b
	        this._local = y1 - this._zoom*x1;
	    };

	    Time.Viewport = Viewport;
	})();

/***/ },
/* 10 */
/***/ function(module, exports) {

	(function() {
	    function BarFactory() {
	        this.min = 0;
	        this.max = 1000;
	        this.middle = this.min + (this.max - this.min) / 2;
	        this.minColor = 0;
	        this.maxColor = 255;
	        this.middleColor = this.minColor + (this.maxColor - this.minColor) / 2;
	    }

	    BarFactory.prototype.buildCanvas = function(height, scale) {		
	        var canvasAttributes = {
	            id : 'bar#' + scale,
	            width : '100%',
	            height : height + 'px'
	        };
	        var canvasCss = {
	            borderTop : '1px solid #CDCDCD'
	        };

	        $('<canvas>').attr(canvasAttributes)
	                     .css(canvasCss)
	                     .appendTo(Time.view.timeline);

	        return document.getElementById(canvasAttributes.id).getContext("2d");
	    };

	    BarFactory.prototype.buildBuckets = function(bucketsDTO) {
	        for (var i = 0; i < bucketsDTO.buckets.length; i++) {
	            var bucket = bucketsDTO.buckets[i];
	            bucket.x = bucket.bucket;
	            delete bucket.bucket;
	            bucket.color = this._getColor(bucket.count);
	            bucket.scale = bucketsDTO.scale;
	            bucket.day = Time.scale.bucketToDays(bucket);
	            bucket.year = Time.scale.bucketToYears(bucket);
	            bucket.human = Time.scale.bucketToHuman(bucket);
	        }
	        
	        bucketsDTO.buckets.sort(this._triBuckets);
	        return bucketsDTO.buckets;
	    };
	    
	    BarFactory.prototype._triBuckets = function(a,b){
	        return a.x - b.x;
	    };
	    
	    BarFactory.prototype._getColor = function(count) {
	        var r = this._getRed(count);
	        var g = this._getGreen(count);
	        var b = this._getBlue(count);
	        return 'rgb(' + r + ',' + g + ',' + b + ')';
	    };
	    BarFactory.prototype._getRed = function(count) {
	        return parseInt(this.maxColor - Math.max(0, count - this.middle) / this.max * (this.maxColor - this.minColor) * 2);
	    };
	    BarFactory.prototype._getGreen = function(count) {
	        return parseInt(Math.max(this.minColor, this.maxColor - count / this.max * (this.maxColor - this.minColor) * 2));
	    };
	    BarFactory.prototype._getBlue = function() {
	        return 0;
	    };

	    Time.BarFactory = BarFactory;
	})();

/***/ },
/* 11 */
/***/ function(module, exports, __webpack_require__) {

	(function(){
	    
	    function PhrasesDrawer(){

	    }
	    
	    PhrasesDrawer.prototype.setPhrases = function(phrases, term) {
	        if (phrases.total === 0) {
	            this.addNoPhrases(term, phrases);
	            return;
	        }

	        var lastPhrase = Time.view.phrases.children().last();
	        var phraseOne = phrases.phraseList[0];
	        if (!lastPhrase || phraseOne.text !== lastPhrase.text) {
	            Time.view.phrases.append(this.buildHtmlPhrase(phraseOne));
	        }

	        for (var i = 1; i < phrases.phraseList.length; i++) {
	            var prev = phrases.phraseList[i - 1];
	            var phrase = phrases.phraseList[i];
	            if (phrase.text !== prev.text) {
	                Time.view.phrases.append(this.buildHtmlPhrase(phrase));
	            }
	        }
	    };

	    function titleChanged(phrase) {
	        var newTitle = phrase.title !== this.lastTitle;
	        this.lastTitle = phrase.title;
	        return newTitle;
	    }

	    PhrasesDrawer.prototype.buildHtmlPhrase = function(phrase) {

	        var source = Time.sources[phrase.type];
	        phrase.pageName = source.getPageName(phrase);
	        phrase.pageNameEscaped = this.htmlEncode(phrase.pageName);
	        phrase.tipTextHeader = this.htmlEncode(source.tipTextHeader);
	        phrase.imgUrl = source.imgUrl;
	        phrase.author = source.getAuthor(phrase);

	        return ((titleChanged(phrase) ? "<div class='phraseHeader'><i>${title}</i></div>" : "") +
	                    "<p date='${date}' page='${url}'>[...] ${text} [...]" +
	                        "<a title='${tipTextHeader} : ${pageNameEscaped} de ${author}' href='${url}' target='_blank' onClick='Time.drawer.link(${pageName})'>" +
	                            "<img src='${imgUrl}'/>" +
	                        "</a>" +
	                    "</p>").replace(/\${[a-zA-Z]*}/g, function(v){return phrase[v.substring(2,v.length-1)];});
	    };

	    PhrasesDrawer.prototype.htmlEncode = function(value){
	        return value.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;").replace(/'/g, "&#039;")
	    };

	    PhrasesDrawer.prototype.link = function(pageName) {
	        Time.anal.ga('send', 'event', 'link', pageName, Time.filter.term);
	    };

	    PhrasesDrawer.prototype.addTextIntro = function(humanDate, nbPhrases) {
	        Time.view.phrases.append("<div class=\"textIntro\"><h2>Il était une fois " + this.firstToLowerCase(humanDate) + " . . .</h2>");
	        Time.view.phrases.append( true ? (nbPhrases + " phrase") + (nbPhrases > 1 ? "s" : "") : "" + "</i>" + "</div>");
	    };

	    PhrasesDrawer.prototype.addNoPhrases = function(term, phrases) {
	        if(phrases.alternatives == null || phrases.alternatives.length ===0){
	            Time.view.phrases.append("<br><br><h2 style=\"text-align:center\">Aucun résultat pour <i>" + term + "</i></h2>");
	        }else{
	            var tryWith = "<br><br><h2 style=\"text-align:center\">0 résultats pour <i>" + term + "</i> :(<br><br>Essayez avec ces mots: ";

	            phrases.alternatives.forEach(function(alternative){
	                tryWith += "<i><a href='/#"+alternative+"'>" + alternative + "</a></i>, ";
	            });
	            tryWith = tryWith.slice(0, -2);
	            tryWith += "</h2>";
	            Time.view.phrases.append(tryWith);
	        }
	    };
	    
	    PhrasesDrawer.prototype.addTheEnd = function() {
	        Time.view.phrases.append("<h1 style=\"text-align:center\">_________________________________</h1>");
	    };
	    
	    PhrasesDrawer.prototype.firstToLowerCase = function( str ) {
	        return str.substr(0, 1).toLowerCase() + str.substr(1);
	    }

	    Time.PhrasesDrawer = PhrasesDrawer;
	})();

/***/ },
/* 12 */
/***/ function(module, exports) {

	(function() {

	    function Scale() {
	        this.seventiesInDays = 719528;
	        this.yearCalendarLimit = 10000;
	        this.dayCalendarLimit = this.yearCalendarLimit * 364.25;
	        this.scales = [ 10000000000, 10000, 500, 10 ];
	        this.echelles = {
	            milliard : 1000000000,
	            million : 1000000,
	            millier : 1000,
	            un : 1
	        };
	    }

	    /**
	     * The default Scale is the farest
	     * @returns {number}
	     */
	    Scale.prototype.defaultScale = function(){
	        return 0;
	    };

	    Scale.prototype.dayToHuman = function(day){
	        return this.bucketToHuman({day:day});
	    };

	    Scale.prototype.bucketToRightFilter = function(bucket){
	        bucket.x++;
	        return this.bucketToFilter(bucket);
	    };

	    Scale.prototype.bucketToFilter = function(bucket){
	        var years = this.bucketToYears(bucket);
	        var days = this.bucketToDays(bucket);
	        var filter = "";
	        var echelle = this._getEchelle(years);

	        switch (echelle) {
	            case this.echelles.milliard:
	                filter = "@" + this._round(years / this.echelles.milliard, 1) + "M";
	                break;
	            case this.echelles.million:
	                filter = "@" + this._round(years / this.echelles.million, 1) + "m";
	                break;
	            case this.echelles.millier:
	            case this.echelles.un:
	                if(!this._insideCalendarLimit(years)){
	                    filter = "@" + Math.min(years);
	                }else{
	                    filter = "@" + this._formatDateFilter(this._daysToDate(days));
	                }
	                break;
	            default:
	        }
	        return filter;
	    };

	    Scale.prototype.bucketToHuman = function(bucket) {
	        var years = this.bucketToYears(bucket);
	        var start = this._getStart(years);
	        var human = "";
	        switch (this._getEchelle(years)) {
	            case this.echelles.milliard:
	                var nbard = this._positiveRound(years / this.echelles.milliard, 1);
	                human = start + nbard + " milliard" + (nbard > 1 ? "s" : "") + " d'années";
	                break;
	            case this.echelles.million:
	                var nbon = this._positiveRound(years / this.echelles.million, 1);
	                human = start + nbon + " million" + (nbon > 1 ? "s" : "") + " d'années";
	                break;
	            case this.echelles.millier:
	            case this.echelles.un:
	                if(bucket.scale === "0"){
	                    human = "De nos jours";
	                }else {
	                    human = "en " + Math.round(years);
	                }
	                break;
	            default:
	        }
	        return human;
	    };

	    Scale.prototype._round = function(value, dec) {
	        var negative = value > 0 ? 1 : -1;
	        return this._positiveRound(value, dec)*negative
	    };

	    Scale.prototype._positiveRound = function(value, dec) {
	        //dec      = 0,  1,   2,    3
	        //decimals = 1, 10, 100, 1000
	        var decimals = Math.pow(10,dec);
	        return Math.abs(Math.round(value * decimals) / decimals);
	    };

	    /**
	     *
	     * @param years
	     * @returns {number}
	     * @private
	     */
	    Scale.prototype._getEchelle = function(years) {
	        years = Math.abs(Math.round(years));
	        if (Math.round(years / this.echelles.milliard) > 0) {
	            return this.echelles.milliard;
	        } else if (Math.round(years / this.echelles.million) > 0) {
	            return this.echelles.million;
	        } else if (Math.round(years / this.echelles.millier) > 0) {
	            return this.echelles.millier;
	        } else {
	            return this.echelles.un;
	        }
	    };

	    Scale.prototype.bucketToYears = function(bucket) {
	        return this._daysToYears(this.bucketToDays(bucket));
	    };

	    Scale.prototype.bucketToDays = function(bucket){
	        if(bucket.day || bucket.day === 0){
	            return bucket.day;
	        }
	        return this.scales[bucket.scale] * bucket.x;
	    };

	    Scale.prototype._daysToYears = function(days) {
	        if (Math.abs(days) > this.dayCalendarLimit) {
	            return days / 364.25;
	        } else {
	            return this._daysToDate(days).getFullYear();
	        }
	    };

	    Scale.prototype._daysToDate = function(days){
	        var msForEpoch = (days - this.seventiesInDays) * 24 * 60 * 60 * 1000;
	        return new Date(msForEpoch);
	    };

	    Scale.prototype._getStart = function(years) {
	        return years > 0 ? 'Dans ' : 'Il y a ';
	    };

	    Scale.prototype._formatDateFilter = function(date){
	        return leadingZero(date.getDate()) + "/" +  leadingZero(date.getMonth()+1) + "/" +  date.getFullYear();
	    };

	    function leadingZero(value){
	        if(value < 10){
	            return "0"+value;
	        }else{
	            return ""+value;
	        }
	    }

	    Scale.prototype._insideCalendarLimit = function(year){
	        return year > 1800 && year < 2050;
	    };

	    Time.Scale = Scale;

	})();

/***/ },
/* 13 */
/***/ function(module, exports) {

	(function() {

	    function Data() {

	    }

	    Data.prototype.getBuckets = function(term, callback) {
	        var params = {
	            term : term
	        };
	        Time.anal.ga('send', 'event', 'buckets', term);
	        $.get("api/buckets", params).done(callback);
	    };

	    Data.prototype.getPhrases = function(request, lastKey, callback) {
	        var params = {
	            request : request,
	            lastKey : lastKey
	        };
	        Time.anal.ga('send', 'event', 'phrases', request);
	        $.get("api/phrases", params).done(callback);
	    };

	    Data.prototype.getSynonyms = function(term, callback) {
	        var params = {
	            term : term
	        };
	        Time.anal.ga('send', 'event', 'synonyms', term);
	        $.get("api/synonyms", params).done(callback);
	    };

	    Time.Data = Data;

	})();

/***/ },
/* 14 */
/***/ function(module, exports) {

	(function() {
	    function Filter() {
	        this.term = null;
	    }

	    Filter.prototype.install = function(view) {
	        view.termInput.on("keyup",$.proxy(this.termInputKeyPress, this));
	        view.homeTermInput.on("keyup",$.proxy(this.homeTermInputKeyPress, this));
	    };

	//HOME
	    Filter.prototype.homeTermInputKeyPress = function(e) {
	        if (e.which === 13) {
	            this.homeTermInputKeyEnterPress();
	        }
	    };

	    Filter.prototype.homeTermInputKeyEnterPress = function() {
	        var term = Time.view.homeTermInput.val();
	        this.onFilterFromHome(term, false);
	    };

	    Filter.prototype.onFilterFromHome = function(term, ignoreHistory){
	        Time.view.homeTermInput.off("keyup");
	        Time.view.home.remove();
	        Time.view.content.show();
	        delete Filter.prototype.homeTermInputKeyEnterPress;
	        delete Filter.prototype.homeTermInputKeyPress;
	        delete Filter.prototype.onFilterFromHome;
	        Filter.prototype.onFilterFromHome = this.onFilter;
	        delete Time.view.homeTermInput;
	        delete Time.view.home;

	        this.onFilter(term, ignoreHistory);
	    };
	//!HOME

	    Filter.prototype.termInputKeyPress = function(e) {
	        if (e.which === 13) {
	            this.termInputKeyEnterPress();
	        } else {
	            this.checkGetSynonymsTrigger();
	        }
	    };


	    Filter.prototype.checkGetSynonymsTrigger = function() {
	        var saisie = Time.view.termInput.val();
	        var term = saisie.trim();
	        var isTwoSpace = saisie.endsWith('  ');
	        var isOneWord = !term.includes(' ');

	        if (isTwoSpace && isOneWord) {
	            Time.data.getSynonyms(term, $.proxy(this.onSynonyms, this));
	        }
	    };

	    Filter.prototype.onSynonyms = function(synonyms) {
	        var saisie = Time.view.termInput.val().trim();
	        var newSaisie = saisie + ' ' + synonyms.join(' ');
	        Time.view.termInput.val(newSaisie);
	        Time.view.termInput[0].selectionStart = saisie.length;
	        Time.view.termInput[0].selectionEnd = newSaisie.length;
	    };

	    Filter.prototype.termInputKeyEnterPress = function() {
	        var term = Time.view.termInput.val();
	        this.onFilter(term);
	    };

	    //Lancement d'une recherche
	    Filter.prototype.onFilter = function(term, ignoreHistory) {
	        this.term = term;
	        Time.anal.ga('send', 'event', 'search', term);
	        Time.view.termInput.val(term);
	        Time.phrases.clearText();
	        Time.phrases.lastSearch = null;
	        Time.phrases.loadPhrases();

	        if (!ignoreHistory) {
	            Time.historic.pushState(term);
	        }
	    };

	    Filter.prototype.onPeriodFilter = function (leftFilter, rightFilter) {
	        this.onFilter(applyFilters(Time.view.termInput.val(), leftFilter, rightFilter));
	    };

	    /**
	     * Remplace les filters existants dans term par ceux spécifiés.
	     * @param term
	     * @param leftFilter ajouté à term
	     * @param rightFilter ajouté si différent de left
	     * @returns {string}
	     */
	    function applyFilters(term, leftFilter, rightFilter){
	        var partArray = term.split(" ").filter(removeUndesired);
	        partArray.push(leftFilter);
	        if(leftFilter !== rightFilter){
	            partArray.push(rightFilter);
	        }
	        return partArray.join(" ");
	    }

	    function removeUndesired(part){
	        return (part || "").length > 0 && part.charAt(0) !== "@";
	    }

	    Time.Filter = Filter;
	})();

/***/ },
/* 15 */
/***/ function(module, exports) {

	(function() {
	    function Phrases() {
	        this.lastSearch = null;
	        this.isSearching = false;
	    }

	    Phrases.prototype.install = function() {
	        Time.view.phrases.on('dblclick.Textes', this.onPhrasesDblClick);
	        Time.view.window.on('scroll', $.proxy(this.scroll, this));
	    };

	    Phrases.prototype.onPhrasesDblClick = function(event) {
	        event.stopImmediatePropagation();
	        if (window.getSelection()) {
	            var term = window.getSelection().toString().trim();
	            Time.filter.onFilter(term);
	        }
	    };

	    Phrases.prototype.loadPhrases = function() {
	        Time.view.throbber.show();
	        var lastKey = null;
	        Time.data.getPhrases(Time.filter.term, lastKey, $.proxy(this.onFirstPhrases, this));
	    };

	    Phrases.prototype.onFirstPhrases = function(phrases) {
	        if (phrases.total > 0) {
				var day = phrases.phraseList[0].date;
	            var humanDate = Time.scale.dayToHuman(day);
	            Time.phrasesdrawer.addTextIntro(humanDate, phrases.total);
	        }
	        this.onPhrases(phrases);
	    };

	    Phrases.prototype.onPhrases = function(phrases) {
	        Time.view.throbber.hide();
	        Time.phrasesdrawer.setPhrases(phrases, Time.filter.term);
	        this.isSearching = false;
	        if (phrases.lastKey) {
	            this.lastSearch = {
	                lastKey : phrases.lastKey
	            };
	            this.maybeMorePhrases();
	        } else {
	            this.lastSearch = null;
	            if(!phrases.alternatives){
	                Time.phrasesdrawer.addTheEnd();
	            }
	        }
	    };

	    Phrases.prototype.scroll = function() {
	        this.maybeMorePhrases();
	        if (Time.view.window.scrollTop() < 20) {
	            Time.tooltips.showTooltips();
	        } else {
	            Time.tooltips.hideTooltips();
	        }
	    };

	    Phrases.prototype.maybeMorePhrases = function() {
	        if (!this.isSearching && this.lastSearch && this.isBottomVisible()) {
	            this.isSearching = true;
	            Time.view.throbber.show();
	            Time.data.getPhrases(Time.filter.term, this.lastSearch.lastKey, $.proxy(this.onPhrases, this));
	        }
	    };

	    Phrases.prototype.isBottomVisible = function() {
	        var docViewBottom = Time.view.window.scrollTop() + Time.view.window.height();
	        var elemBottom = Time.view.bottom.offset().top + Time.view.bottom.height();
	        return (elemBottom - 500) <= docViewBottom;
	    };

	    Phrases.prototype.clearText = function() {
	        Time.view.phrases.empty();
	        $(window).scrollTop(0);
	    };

	    Time.Phrases = Phrases;
	})();

/***/ },
/* 16 */
/***/ function(module, exports) {

	(function() {
	    function Tooltips() {
	        this.currentBar = null;
	    }

	    // SUR LA BARRE CONTENANT LA SOURIS
	    Tooltips.prototype.mouseMoveOnBar = function(event) {
	        Time.tooltips.updateTooltips(null, event.clientX);
	    };

	    // DESSOUS DE LA BARRE ACTIVE
	    Tooltips.prototype.decorate = function(bar) {
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
	    
	    Tooltips.prototype.mouseOutOnBar = function (){
	        Time.tooltips.updateTooltips();
	    };

	    Tooltips.prototype.undecorate = function(bar) {
	        $(bar.canvas).off('mousemove.Tooltip');
	        $(bar.canvas).off('mouseout.Tooltip');
	        this.hideTooltips();
	    };

	    Tooltips.prototype.updateTooltips = function(animate, mouseX) {
	        if(!Time.tooltips.currentBar){
	            return;
	        }

	        if(Time.tooltips.currentBar.monoBucket()){
	            Time.tooltips.toolTipAt(Time.view.activeBarTips[1], Time.tooltips.currentBar.middle(), animate);
	        }else{
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
	        }
	    };

	    Tooltips.prototype.getNearest = function(mouseX, xS){
	        return xS.map(function(x){return Math.abs(mouseX - x);}).reduce(function(nearest, current, index){
	            if(current < nearest.distance){
	                return {distance:current, index:index};
	            }else{
	                return nearest;
	            }
	        },{distance:1000000, index:null}).index;
	    };
	    
	    Tooltips.prototype.showTooltips = function(){
	        this.updateTooltips();
	    };
	    Tooltips.prototype.hideTooltips = function(){
	        Time.view.activeBarTips.forEach(function(tooltip){
	            tooltip.css({opacity:0});
	        });
	    };
	    
	    Tooltips.prototype.toolTipAt = function(tooltip, tooltipX, animate) {
	        var humanDate = Time.scale.bucketToHuman({
	            scale : Time.tooltips.currentBar.scale,
	            x : Time.tooltips.currentBar.viewport.toBucketX(tooltipX)
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

	    Time.Tooltip = Tooltips;
	})();

/***/ },
/* 17 */
/***/ function(module, exports) {

	(function(){
	    Time.sources = {
	            'FILE' : {
	                'tipTextHeader' : 'Lien vers le livre',
	                'imgUrl' : 'http://www.ecoagris.org/AjaxControls/KoolTreeView/icons/book.gif',
	                'getPageName' : function(phrase){
	                    return phrase.title;
	                },
	                'getAuthor' : function(phrase){
	                    return phrase.author
	                }
	            },
	            'WIKI' : {
	                'tipTextHeader' : 'Lien vers l\'article',
	                'imgUrl' : 'http://upload.wikimedia.org/wikipedia/commons/6/64/Icon_External_Link.png',
	                'getPageName' : function(phrase){
	                    return decodeURIComponent(phrase.url).split(/[\/]+/).pop().replace(/_/g, " ");
	                },
	                'getAuthor' : function(phrase){
	                    return 'Wikipédia'
	                }
	            },
	            'WEB_PAGE' : {
	                'tipTextHeader' : 'Lien vers la page web',
	                'imgUrl' : 'img/browser-world-globe-planet-icone-8648-16.png',
	                'getPageName' : function(phrase){
	                    return phrase.title;
	                },
	                'getAuthor' : function(phrase){
	                    return phrase.author
	                }
	            }
	    };
	})();

/***/ }
/******/ ]);