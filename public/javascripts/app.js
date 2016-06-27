(function() {
   var app = angular.module('airports',['ngRoute', 'ui.bootstrap', 'angular-growl']);
    app.config(function($routeProvider, growlProvider){
        $routeProvider
            .when('/', {
                templateUrl: 'assets/javascripts/home.html',
            })
            .when('/query', {
                templateUrl: 'assets/javascripts/query/query.html',
                controller: 'QueryController',
                controllerAs: 'ctrl'
            })
            .when('/reports', {
                templateUrl: 'assets/javascripts/report/report.html',
                controller: 'ReportController',
                controllerAs: 'ctrl'
            })
            .otherwise({
                templateUrl: 'assets/javascripts/home/notFound.html'
            });

        growlProvider.globalTimeToLive(10000);
    });
}());