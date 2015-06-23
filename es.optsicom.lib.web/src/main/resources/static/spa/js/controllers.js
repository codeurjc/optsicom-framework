// ----------------Controllers----------------
//***************** Main Controller
	app.controller("MainController",  [ '$scope', '$route', '$routeParams', '$location','$anchorScroll', function($scope, $route, $routeParams, $location,$anchorScroll) {
		$scope.$route = $route;
	     $scope.$location = $location;
	     $scope.$routeParams = $routeParams;
	     $scope.scrollTo = function(id) {
	    	$(document).ready(function(){
			    $location.hash(id);
			    $anchorScroll();
	    	});
	      };
	} ]);
	
//***************** List of experiments Controller
	app.controller('experimentsController', [ '$http','$scope', function($http,$scope) {
		$scope.init = function(){
			$('#theme-config').hide();
	     };
	     $scope.init();
		
		var optsicomExps = this;
		optsicomExps.experiments = [];
		optsicomExps.expIdsChecked = [];
		optsicomExps.getExperiments = function(){
			$http.get('/api/experiments').success(function(data) {
				optsicomExps.experiments = data;
				for(var i = 0; i < optsicomExps.experiments.length; i++){
					var exp = {};
	    			exp.id = optsicomExps.experiments[i].id;
	    			exp.checked = false;
	    			optsicomExps.expIdsChecked.push(exp);
				}
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
		optsicomExps.urlMerge = 0;
		optsicomExps.buildUrlMerge = function(){
			var aux = [];
			for (var i = 0; i < optsicomExps.expIdsChecked.length; i++){
				if (optsicomExps.expIdsChecked[i].checked){					
					aux.push(optsicomExps.expIdsChecked[i].id);
				}
			}
			optsicomExps.urlMerge = splitListToString(aux,",")
		};
	} ]);
	
//***************** Single experiment Controller
	app.controller('singleExperimentController', [ '$http','$scope', '$routeParams', function($http,$scope, $routeParams) {
		$scope.init = function(){
			$('#theme-config').hide();
	     };
	     $scope.init();
		var optsicomExp = this;
		optsicomExp.expIdAux = $routeParams;
		optsicomExp.expId = optsicomExp.expIdAux.expId;
		optsicomExp.experiment = {};
		optsicomExp.methodNames = {};
		optsicomExp.experimentName = {};
		optsicomExp.expMethodLists=[];
		optsicomExp.resumedTables=[];
		$http.get('/api/' + stringToList(optsicomExp.expId) + '/experimentNameMethod').success(function(methodData) {
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
	
//***************** Report and Merge Controller
	app.controller('reportController', [ '$http','$scope', '$routeParams', function($http,$scope, $routeParams) {
		$scope.init = function(){
			$('#theme-config').show();
	     };
	     $scope.init();
		var optsicomReport = this;
		optsicomReport.expIdAux = $routeParams;
		optsicomReport.expId = optsicomReport.expIdAux.expIds;
		if (typeof optsicomReport.expId === 'undefined'){
			optsicomReport.expId = optsicomReport.expIdAux.expId;
		}
		optsicomReport.report = {};
		optsicomReport.reportConfiguration = {'expIds':stringToList(optsicomReport.expId)};
		optsicomReport.methodNames = {};
		optsicomReport.bestValuesView = false;
		optsicomReport.configurationView = false;
		optsicomReport.methodNamesView = [];
		optsicomReport.uniqueArrayHeaders = [];
		optsicomReport.reportConfiguration.bestValues = false;
		optsicomReport.reportConfiguration.configuration = false;
		optsicomReport.reportConfiguration.methods = [];
		optsicomReport.callMethodNames = function(){
			var allMethodsNames = [];
			optsicomReport.methodNamesView = [];
	    	$http.get('/api/' + stringToList(optsicomReport.expId) + '/experimentNameMethod').success(function(methodData) {
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
		
		optsicomReport.getMethodSelected = function(methods) {
			var list = [];
			for (var i = 0; i < methods.length; i++){
				if (methods[i].checked){
					list.push(methods[i].id);
				}
			}
			return list;
		};

		optsicomReport.initController = function(){    
			$http({
				    url: getUrlReportOrMerge(optsicomReport.expId,optsicomReport.getMethodSelected(optsicomReport.methodNamesView),optsicomReport.reportConfiguration.bestValues,optsicomReport.reportConfiguration.configuration) ,
				    method: 'GET'  
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
			optsicomReport.reportConfiguration.methods = optsicomReport.getMethodSelected(optsicomReport.methodNamesView);
			if(listContainsElements(optsicomReport.reportConfiguration.methods)){
				optsicomReport.reportConfiguration.configuration = true;
			}else{
				optsicomReport.reportConfiguration.configuration = false;
			}
			optsicomReport.initController(); // update report
		};
		
	} ]);