(function() {
	var app = angular.module('optsicom', ['ngRoute'])
	.config(function($routeProvider){
        $routeProvider
        .when("/", {
            controller: "appCtrl",
            controllerAs: "vm",
            templateUrl: "home.html"
        })
        .when("/single-exp/:expId", {
            controller: "singleExperimentController", 
            controllerAs: "singleExpCtrl",
            templateUrl: "single-experiment.html"
        })
        .when("/experiments", {
            controller: "experimentsController",
            controllerAs: "expsCtrl",
            templateUrl: "experiments.html"
        })
        .when("/report/:expId", {
            controller: "reportController",
            controllerAs: "reportCtrl",
            templateUrl: "report.html"
        });
	});

	//Directives
//	app.directive("experiments", function() {
//		return {
//			restrict : 'E',
//			templateUrl : "experiments.html"
//		};
//	});
//
//	app.directive("singleExperiment", function() {
//		return {
//			restrict : 'E',
//			templateUrl : "single-experiment.html"
//		};
//	});

	//Controllers

	app.controller("MainController",  [ '$scope', '$route', '$routeParams', '$location', function($scope, $route, $routeParams, $location) {
		$scope.$route = $route;
	     $scope.$location = $location;
	     $scope.$routeParams = $routeParams;
	} ]);

	app.controller('experimentsController', [ '$http', function($http) {
		var optsicomExps = this;
		optsicomExps.experiments = [];
		$http.get('/api/experiments').success(function(data) {
			optsicomExps.experiments = data;
		}).error(function(data) {
			optsicomExps.experiments = []; //error
		});
	} ]);

	app.controller('singleExperimentController', [ '$http','$scope', '$routeParams', function($http,$scope, $routeParams) {
//		$scope.params = $routeParams;
		this.expIdAux = $routeParams;
		this.expId = this.expIdAux.expId;
		var optsicomExp = this;
		optsicomExp.experiment = {};
		optsicomExp.methodNames = {};
		$http.get('/api/' + this.expId).success(function(data) {
			optsicomExp.experiment = data;
			
		}).error(function(data) {
			optsicomExp.experiment = {};
		});
		
		$http.get('/api/' + this.expId + '/experimentNameMethod').success(function(methodData) {
			optsicomExp.methodNames = methodData;
		}).error(function(methodData) {
			optsicomExp.methodNames = {};
		});

	} ]);
	

	app.controller('reportController', [ '$http','$scope', '$routeParams', function($http,$scope, $routeParams) {

		this.expIdAux = $routeParams;
		this.expId = this.expIdAux.expId;
		var optsicomReport = this;
		optsicomReport.report = {};
		optsicomReport.reportConfiguration = {'expId':this.expId};
		optsicomReport.methodNames = {};
		optsicomReport.bestValuesView = false;
		optsicomReport.configurationView = false;
		optsicomReport.methodNamesView = [];
		optsicomReport.filterNames = function(prop,reportMethods) {
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


		optsicomReport.callMethodNames = function(){
			var allMethodsNames = [];
			optsicomReport.methodNamesView = [];
	    	$http.get('/api/' + this.expId + '/experimentNameMethod').success(function(methodData) {
	    		var allMethodsNames = methodData
	    		for(var i = 0; i < allMethodsNames.length; i++){
	    			var method = {};
	    			method.id = allMethodsNames[i].expId;
	    			method.name = allMethodsNames[i].expName;
	    			method.checked = false;
		    		optsicomReport.methodNamesView.push(method);
			    }
		    	optsicomReport.methodNames = optsicomReport.filterNames(methodData, optsicomReport.report.reportConfiguration.methods);
		   		for(var i = 0; i < allMethodsNames.length; i++){
		   			for(var j = 0; j < optsicomReport.methodNames.length; j++){
		   				if (allMethodsNames[i].expId == optsicomReport.methodNames[j].expId){
		   					optsicomReport.methodNamesView[i].checked = true;
	    				}
	    			}
		    	}
			}).error(function(methodData) {
				optsicomReport.methodNames = {};
			});
		};   
		       
		optsicomReport.initController = function(){    
			$http({
			    url: '/api/' + this.expId + '/report',
			    method: 'POST',
			    headers: { 'Content-Type': 'application/json' },
			    data: optsicomReport.reportConfiguration //data passed as a requestBody
			    
			}).success(function(data) {
				optsicomReport.report = data;
				optsicomReport.callMethodNames();
			}).error(function(data) {
				optsicomReport.report = {};
			});
			this.getPropertyName = function(prop){
			       words= prop.split(" "); // split the sentence in words
			       wordsAux = [];
			       charactersAux = [];
			       for (var i = 0; i < words.length; i++){
			    	   characters = words[i].split(""); // split each word in characters
			    	   charactersAux = [];
			    	   counter = 0;
			    	   while(counter < characters.length && counter >= 0){ // iterate over the array o character until i get '=', I dont need the rest
			    		   if (characters[counter] == '='){
			    			   counter = -1
			    		   } else {
			    			   charactersAux.push(characters[counter]);
			    			   counter = counter + 1;
			    		   }
			    	   }
			    	   wordsAux.push(charactersAux.join("")); // recover the word
			    	   charactersAux = [];
			       }
			       aux= wordsAux.join(", "); // join the words in a new sentence
			       aux= aux.substring(0,aux.length -2); // this cut the last ', '
			       return aux;
			};
		}
		
		optsicomReport.initController();
		optsicomReport.updateReportConfiguration = function() {
			optsicomReport.reportConfiguration.bestValues = optsicomReport.bestValuesView;
			optsicomReport.reportConfiguration.configuration = optsicomReport.configurationView;
			optsicomReport.reportConfiguration.methods = [];
			optsicomReport.reportConfiguration.methods = optsicomReport.getMethodSelected(optsicomReport.methodNamesView);
			if(typeof optsicomReport.reportConfiguration.methods != 'undefined' && optsicomReport.reportConfiguration.methods != null && optsicomReport.reportConfiguration.methods.length > 0){
				optsicomReport.reportConfiguration.configuration = true;
			}else{
				optsicomReport.reportConfiguration.configuration = false;
			}
			optsicomReport.initController();
		};
		
		optsicomReport.getMethodSelected = function(methods) {
			var list = [];
			for (var i = 0; i < methods.length; i++){
				if (methods[i].checked){
					list.push(methods[i].id);
				}
			}
			return list;
		};
	} ]);
})();
