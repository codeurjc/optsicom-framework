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
        .when("/opciones", {
            controller: "appCtrl",
            controllerAs: "vm",
            templateUrl: "opciones.html"
        });
	});

	//Directives
	app.directive("experiments", function() {
		return {
			restrict : 'E',
			templateUrl : "experiments.html"
		};
	});

	app.directive("singleExperiment", function() {
		return {
			restrict : 'E',
			templateUrl : "single-experiment.html"
		};
	});

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
		$http.get('/api/' + this.expId).success(function(data) {
			optsicomExp.experiment = data;
		}).error(function(data) {
			optsicomExp.experiment = {}; //error
		});
	} ]);
	


	
	
	
	// configure html5 to get links working on jsfiddle
	  $locationProvider.html5Mode(true);
	

})();
