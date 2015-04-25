
var listContainsElements = function(list) {
	if (typeof list != 'undefined' && list != null && list.length > 0){
		return true
	}
	else{
		return false;
	}
};

//used in report 
var uniqueArray = function(duplicatesArray) {
	auxArray = [];
	for ( var i = 0; i < duplicatesArray.length; i++) {
		auxArray.push(duplicatesArray[i].infoTitle);
	}
	auxArray = jQuery.unique( auxArray );
	return auxArray;
};



		var filterNames = function(prop,reportMethods) {
			var aux = [];
			for ( var i = 0; i < prop.length; i++) {
				for ( var j = 0; j < reportMethods.length; j++) {
					if (prop[i].expId == reportMethods[j]) {
						aux.push(prop[i]);
					}
				}
			}
			return aux;
		};
		
		var cutArrayOfCharIfCharIsEquals = function(arrayChar, character){
			counter = 0;
			arrayCharAux = [];
	    	   while(counter < arrayChar.length && counter >= 0){ // iterate over the array o character until i get '=', I dont need the rest
	    		   if (arrayChar[counter] == character){
	    			   counter = -1
	    		   } else {
	    			   arrayCharAux.push(arrayChar[counter]);
	    			   counter = counter + 1;
	    		   }
	    	   }
	    	   return arrayCharAux;
		};
		
		
		var getPropName = function(prop){
		       words= prop.split(" "); // split the sentence in words
		       wordsAux = [];
		       charactersAux = [];
		       for (var i = 0; i < words.length; i++){
		    	   characters = words[i].split(""); // split each word in characters
		    	   charactersAux = cutArrayOfCharIfCharIsEquals(characters,'=');
		    	   wordsAux.push(charactersAux.join("")); // recover the word
		    	   charactersAux = [];
		       }
		       aux= wordsAux.join(", "); // join the words in a new sentence
		       aux= aux.substring(0,aux.length -2); // this cut the last ', '
		       return aux;
		};


var generateGroupedTables = function(exp){ // used in singleExpController
	var names = [];
	var properties = [];
	var methods = exp.methods;
	var methodProps = [];
	_.each(methods, function(method) {
		var props = [];
	    _.each(method.properties.sortedProperties, function(sProp){
	    	var prop = {};    	
	    	if (   sProp.key.indexOf('.') > -1  ){ // si contiene punto, pone como key el ultimo trozo del string y como padre el penultimo
	    		
	    		var substring = sProp.key.split('.');
	    			prop.len = substring.length;
	    			prop.lenList = substring;
	    			prop.parent = substring[substring.length - 2];
	    			prop.key = substring[substring.length - 1];
		    		prop.value = sProp.value;
	    	}
	    	else{
	    		prop.len = 1;
	    		prop.lenList = [''];
	    		prop.parent = '';
	    		prop.key = sProp.key;
	    		prop.value = sProp.value;
	    	}
	    	prop.childs = [];
	    	props.push(prop);
	    });
	    methodProps.push(props);
	});

	var methodProps2 = [];
	_.each(methodProps, function(method) {
		var finalRoots = [];
		var roots = _.filter(method, function(num){ return num.parent == ''; }); // list of roots (0 level)
		var childs = _.filter(method, function(num){ return num.parent != ''; });
	    _.each(childs, function(child){
	    	var indexRoot = 0;
	    	// search and insertion of childs to roots (1 level)
	    	 _.each(roots, function(root){ 
	    		 if (child.parent == root.key){
			    		root.childs.push(child);
			    		roots[indexRoot] = root;
	    		 }
	    		 indexRoot = indexRoot + 1;
	    	 });
	    	// search and insertion of grand-childs  (2 level)
	    	 indexRoot = 0;
	    	 _.each(roots, function(root){
	    		 var indexRootNieto = 0;
	    		 _.each(root.childs, function(r){ 
	    			 if (child.parent == r.key){
				    		r.childs.push(child);
				    		roots[indexRoot].childs[indexRootNieto] = r;
		    		 }  // child es hijo de root
	    			 indexRootNieto = indexRootNieto + 1;
	    		 });
	    		 indexRoot = indexRoot + 1;
	    	 });
	    	// search and insertion of grand-grand-childs (3 level)
	    	 indexRoot = 0;
	    	 _.each(roots, function(root){
	    		 var indexRootNieto = 0;
	    		 _.each(root.childs, function(r){
	    			 var indexRootBisNieto = 0;
	    			 _.each(r.childs, function(r2){
		    			 if (child.parent == r2.key){
					    		r2.childs.push(child);
					    		roots[indexRoot].childs[indexRootNieto] = r2;
			    		 }
		    			 indexRootBisNieto = indexRootBisNieto + 1;
	    			 });
	    			 indexRootNieto = indexRootNieto + 1;
	    		 });
	    		 indexRoot = indexRoot + 1;
	    	 });// end level 3
	    	
	    });// end eachChilds
	    methodProps2.push(roots);
	});
	return methodProps2;
};


var containListInfoRownameSpecificValue =  function(list,value){
	var bool = false;
	_.each(list, function(obj) {
		if (obj.rowname == value){
			bool = true;
		}
	});
	return bool;
};

var generateResumedTables = function(instances){
	var ret = [];
	_.each(instances[0].properties.sortedProperties, function(prop) {
		var table = {};
		table.name = prop.key;
		table.info = [];
		ret.push(table);
	});
	ret = _.filter(ret, function(obj){ return (obj.name != 'filename') && (obj.name != 'name') && (obj.name != 'usecase'); });
	_.each(instances, function(instance) {
		_.each(instance.properties.sortedProperties, function(srtProperties) { 
			for ( var i = 0;i<ret.length;i++){
				if (srtProperties.key == ret[i].name){// we are in correct table
					if (containListInfoRownameSpecificValue(ret[i].info, srtProperties.value)){
						for ( var j = 0;j<ret[i].info.length;j++){
							if(ret[i].info[j].rowname == srtProperties.value){
								ret[i].info[j].count = ret[i].info[j].count + 1;
							}	
						}
					}
					else{ 
						var row = {};
						row.rowname = srtProperties.value;
						row.count = 0;
						ret[i].info.push(row);
					}
				}
			}
		});
	});
	return ret;
};

