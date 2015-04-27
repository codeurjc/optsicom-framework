
//(function () {
//    angular.module('inspinia', [
//        'ui.router',                    // Routing
//        'oc.lazyLoad',                  // ocLazyLoad
//        'ui.bootstrap',                 // Ui Bootstrap
//        'pascalprecht.translate',       // Angular Translate
//        'ngIdle'                        // Idle timer
//    ])
//})();
var app = {};
(function() {
	app = angular.module('optsicom', ['ngRoute'])
})();


// Other libraries are loaded dynamically in the config.js file using the library ocLazyLoad