app.config(function($routeProvider){
    $routeProvider
    .when("/", {
    	controller: "experimentsController",
        controllerAs: "expsCtrl",
        templateUrl: "views/experiments.html"
    })
    .when("/single-exp/:expId", {
        controller: "singleExperimentController", 
        controllerAs: "singleExpCtrl",
        templateUrl: "views/single-experiment.html"
    })
    .when("/experiments", {
        controller: "experimentsController",
        controllerAs: "expsCtrl",
        templateUrl: "views/experiments.html"
    })
    .when("/merge/:expIds", {
        controller: "reportController",
        controllerAs: "reportCtrl",
        templateUrl: "views/report.html"
    })
    .when("/report/:expId", {
        controller: "reportController",
        controllerAs: "reportCtrl",
        templateUrl: "views/report.html",
        reloadOnSearch: false
    });
});