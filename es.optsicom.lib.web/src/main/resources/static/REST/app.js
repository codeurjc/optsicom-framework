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
//		$scope.params = $routeParams;
		this.expIdAux = $routeParams;
		this.expId = this.expIdAux.expId;
		var optsicomReport = this;
		optsicomReport.report = {};
//		$http.post('/api/' + this.expId + '/report').success(function(data) {
//			optsicomReport.report = data;
//		}).error(function(data) {
//			optsicomReport.report = {};
//		});
		$http({
		    url: '/api/' + this.expId + '/report',
		    method: 'POST',
		    headers: { 'Content-Type': 'application/json' },
		    data: {'expId':this.expId} //data passed as a requestBody
		}).success(function(data) {
			optsicomReport.report = data;
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
		
//		optsicom.getPropertyName = function(){
//			return "hola".split("").reverse().join("");
//		};
		
		
//		data: JSON.stringify({application:app, from:d1, to:d2}),

	} ]);
	
	
	//filter

	
	
	
	
	
//	// configure html5 to get links working on jsfiddle
//	  $locationProvider.html5Mode(true);
	

})();
