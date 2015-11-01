(function(){
	
	function Tests(){
		var echelle = Scale.getEchelle(-988.3121899988);
		if(echelle !== Scale.echelles.millier){
			console.log("ERREUR Scale.getEchelle(-988.3121899988)=",echelle);
		}
	}
	
	Time.Tests = new Tests();
})();