(function() {
	var app = angular.module('optsicom', []);

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

	app.controller("MainController", function() {

	});

	app.controller('experimentsController', [ '$http', function($http) {
		var optsicomExps = this;
		optsicomExps.experiments = [];
		$http.get('/api/experiments').success(function(data) {
			optsicomExps.experiments = data;
		}).error(function(data) {
			optsicomExps.experiments = []; //error
		});
	} ]);

	app.controller('singleExperimentController', [ '$http', function($http) {
		var optsicomExp = this;
		optsicomExp.experiment = {};
		$http.get('/api/1551').success(function(data) {
			optsicomExp.experiment = data;
		}).error(function(data) {
			optsicomExp.experiment = {}; //error
		});
	} ]);
	


	
	
	
	
	

})();
