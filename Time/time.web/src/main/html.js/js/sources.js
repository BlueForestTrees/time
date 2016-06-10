(function(){
    Time.sources = {
            'FILE' : {
                'tipTextHeader' : 'Lien vers le livre',
                'imgUrl' : 'http://www.ecoagris.org/AjaxControls/KoolTreeView/icons/book.gif',
                'getPageName' : function(phrase){
                    return phrase.title;
                }
            },
            'WIKI' : {
                'tipTextHeader' : 'Lien vers l\'article wikipédia',
                'imgUrl' : 'http://upload.wikimedia.org/wikipedia/commons/6/64/Icon_External_Link.png',
                'getPageName' : function(phrase){
                    return decodeURIComponent(phrase.pageUrl).split(/[\/]+/).pop().replace(/_/g, " ").substring(1);
                }
            },
            'WEBPAGE' : {
                'tipTextHeader' : 'Lien vers la page web',
                'imgUrl' : 'img/browser-world-globe-planet-icone-8648-16.png',
                'getPageName' : function(phrase){
                    return phrase.title;
                }
            }
    };
})();