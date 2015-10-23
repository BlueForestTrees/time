(function(){
	Scale = {
			ONE : 'ONE',
			TEN : 'TEN',
			TEN3 : 'TEN3',
			TEN6 : 'TEN6',
			TEN9 : 'TEN9',
			multiplier : function(scale){
				return Scale.details[scale].multiplier;
			},
			sublevel : function(scale){
				return Scale.details[scale].sublevel;
			},
			details : {
				ONE : {
					multiplier : 1,
					uplevel : 'TEN'
				},
				TEN : {
					sublevel : 'ONE',
					multiplier : 10,
					uplevel : 'TEN3'
				},
				TEN3 : {
					sublevel : 'TEN',
					multiplier : 10000,
					uplevel : 'TEN6'
				},
				TEN6 : {
					sublevel : 'TEN3',
					multiplier : 10000000,
					uplevel : 'TEN9'
				},
				TEN9 : {
					sublevel : "TEN6",
					multiplier : 10000000000
				},
			}
	};
})();