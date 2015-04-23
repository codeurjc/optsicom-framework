(function() {
	var app = angular.module('optsicom', ['ngRoute'])
	.config(function($routeProvider){
        $routeProvider
        .when("/", {
        	controller: "experimentsController",
            controllerAs: "expsCtrl",
            templateUrl: "experiments.html"
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
        .when("/merge/:expIds", {
            controller: "mergeController",
            controllerAs: "mergeCtrl",
            templateUrl: "merge.html"
        })
        .when("/report/:expId", {
            controller: "reportController",
            controllerAs: "reportCtrl",
            templateUrl: "report.html"
        });
	});

// ----------------Controllers----------------
//***************** Main Controller
	app.controller("MainController",  [ '$scope', '$route', '$routeParams', '$location', function($scope, $route, $routeParams, $location) {
		$scope.$route = $route;
	     $scope.$location = $location;
	     $scope.$routeParams = $routeParams;
	} ]);
//***************** List of experiments Controller
	app.controller('experimentsController', [ '$http', function($http) {
		var optsicomExps = this;
		optsicomExps.experiments = [];
		optsicomExps.getExperiments = function(){
			$http.get('/api/experiments').success(function(data) {
				optsicomExps.experiments = data;
			}).error(function(data) {
				optsicomExps.experiments = [];
			});			
		};
		optsicomExps.getExperiments();
		optsicomExps.expDelete = function(deleteId){
			if (confirm("sure to delete?") == true){
				$http.delete('/api/' + deleteId).success(function (data, status) {
		            console.log('succesfully deleted' + deleteId);
		            alert('Experiment ' + deleteId + ' has been succesfully deleted');
		            optsicomExps.getExperiments(); //update experiment list
		        }).error(function(data) {
		        	console.log('delete error');
				});
			}
		};

	} ]);
//***************** Single experiment Controller
	app.controller('singleExperimentController', [ '$http','$scope', '$routeParams', function($http,$scope, $routeParams) {
		var optsicomExp = this;
		optsicomExp.expIdAux = $routeParams;
		optsicomExp.expId = optsicomExp.expIdAux.expId;
		optsicomExp.experiment = {};
		optsicomExp.methodNames = {};
		optsicomExp.experimentName = {};
		optsicomExp.expMethodLists=[];
		optsicomExp.resumedTables=[];
		$http.get('/api/' + optsicomExp.expId + '/experimentNameMethod').success(function(methodData) {
			optsicomExp.methodNames = methodData;
		}).error(function(methodData) {
			optsicomExp.methodNames = {};
		});
		$http.get('/api/' + optsicomExp.expId).success(function(data) {
			optsicomExp.experiment = data.experiment;
			optsicomExp.experimentName = data.name;
			optsicomExp.expMethodLists = generateGroupedTables(optsicomExp.experiment);
			optsicomExp.resumedTables = generateResumedTables(optsicomExp.experiment.instances);
		}).error(function(data) {
			optsicomExp.experiment = {};
		});
	} ]);
//***************** Merge/fusion experiments Controller
	app.controller('mergeController', [ '$http','$scope', '$routeParams', function($http,$scope, $routeParams) {
		var optsicomMrg = this;
		optsicomMrg.expIdAux = $routeParams;
		optsicomMrg.expIds = optsicomMrg.expIdAux.expIds;
		optsicomMrg.experiments = [];
		optsicomMrg.methodNames = [];
		$http.get('/api/merge/' + this.expIds).success(function(data) {
			optsicomMrg.experiments = data;
		}).error(function(data) {
			optsicomMrg.experiments = [];
		});
		optsicomMrg.convertStringToArray = function(expIdsString) {
			ids = expIdsString.split(",");
			return ids;
		};
		optsicomMrg.getMethodNames = function(expIds) {
			optsicomMrg.methodNames = [];
			for(var i = 0; i < expIds.length; i++){
				$http.get('/api/' + expIds[i] + '/experimentNameMethod').success(function(methodData) {
					optsicomMrg.methodNames.push(methodData);
				}).error(function(methodData) {
					optsicomMrg.methodNames = [];
				});
			}
		};
		optsicomMrg.getMethodNames(optsicomMrg.convertStringToArray(optsicomMrg.expIds));
	} ]);
	
//***************** Report Controller
	app.controller('reportController', [ '$http','$scope', '$routeParams', function($http,$scope, $routeParams) {
		var optsicomReport = this;
		optsicomReport.expIdAux = $routeParams;
		optsicomReport.expId = optsicomReport.expIdAux.expId;
		optsicomReport.report = {};
		optsicomReport.reportConfiguration = {'expId':optsicomReport.expId};
		optsicomReport.methodNames = {};
		optsicomReport.bestValuesView = false;
		optsicomReport.configurationView = false;
		optsicomReport.methodNamesView = [];
		optsicomReport.uniqueArrayHeaders = [];

		optsicomReport.callMethodNames = function(){
			var allMethodsNames = [];
			optsicomReport.methodNamesView = [];
	    	$http.get('/api/' + optsicomReport.expId + '/experimentNameMethod').success(function(methodData) {
	    		var allMethodsNames = methodData
	    		for(var i = 0; i < allMethodsNames.length; i++){
	    			var method = {};
	    			method.id = allMethodsNames[i].expId;
	    			method.name = allMethodsNames[i].expName;
	    			method.checked = false;
		    		optsicomReport.methodNamesView.push(method);
			    }
		    	optsicomReport.methodNames = filterNames(methodData, optsicomReport.report.reportConfiguration.methods);
		   		for(var i = 0; i < allMethodsNames.length; i++){
		   			for(var j = 0; j < optsicomReport.methodNames.length; j++){
		   				if (allMethodsNames[i].expId == optsicomReport.methodNames[j].expId){
		   					optsicomReport.methodNamesView[i].checked = true;
	    				}
	    			}
		    	}
		   		optsicomReport.uniqueArrayHeaders = uniqueArray(optsicomReport.report.reportTables[2].columnTitles);
			}).error(function(methodData) {
				optsicomReport.methodNames = {};
			});
		};   

		optsicomReport.initController = function(){    
			$http({
			    url: '/api/' + optsicomReport.expId + '/report',
			    method: 'POST',
			    headers: { 'Content-Type': 'application/json' },
			    data: optsicomReport.reportConfiguration //data passed as a requestBody
			    
			}).success(function(data) {
				optsicomReport.report = data;
				optsicomReport.callMethodNames();
			}).error(function(data) {
				optsicomReport.report = {};
			});
			
			optsicomReport.getPropertyName = getPropName;
		};
		optsicomReport.initController();
		optsicomReport.updateReportConfiguration = function() {
			optsicomReport.reportConfiguration.bestValues = optsicomReport.bestValuesView;
			optsicomReport.reportConfiguration.configuration = optsicomReport.configurationView;
			optsicomReport.reportConfiguration.methods = [];
			optsicomReport.reportConfiguration.methods = optsicomReport.getMethodSelected(optsicomReport.methodNamesView);
			if(listContainsElements(optsicomReport.reportConfiguration.methods)){
				optsicomReport.reportConfiguration.configuration = true;
			}else{
				optsicomReport.reportConfiguration.configuration = false;
			}
			optsicomReport.initController(); // update report
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