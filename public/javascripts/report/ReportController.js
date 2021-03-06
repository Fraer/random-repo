(function () {
    var app = angular.module("airports");

    app.controller("ReportController", function ($scope, $http, $rootScope, growl, $location) {
        $rootScope.pageTitle = 'Reports';
        var that = this;

        that.highestAirports = [];
        that.fetchHighestAirports = function() {
            that.fetchingHighest = true;
            $http.get('/highestAirports').then(
                function (success) {
                    that.highestAirports = success.data;
                },
                function (err) { growl.error("Unable to fetch highest airports due to " + err.statusText); }
            ).finally(function() {
                that.fetchingHighest = false;
            });
        };
        that.fetchHighestAirports();

        that.lowestAirports = [];
        that.fetchLowestAirports = function() {
            that.fetchingLowest = true;
            $http.get('/lowestAirports').then(
                function (success) {
                    that.lowestAirports = success.data;
                },
                function (err) { growl.error("Unable to fetch games due to " + err.statusText); }
            ).finally(function() {
                that.fetchingLowest = false;
            });
        };
        that.fetchLowestAirports();

        that.currentCountry = null;
        that.countries = [];
        that.fetchCountries = function() {
                $http.get('/countries').then(
                    function (success) {
                        that.countries = success.data;
                    },
                    function (err) { growl.error("Unable to fetch countries due to " + err.statusText); }
                );
            };

        that.surfaceTypes = [];
        that.fetchSurfaceTypes = function() {
                that.fetchingSurfaces = true;
                $http.get('/surfaceTypes/' + that.currentCountry.code).then(
                    function (success) {
                        that.surfaceTypes = success.data;
                    },
                    function (err) { growl.error("Unable to fetch surface types by country due to " + err.statusText); }
                ).finally(function() {
                    that.fetchingSurfaces = false;
                });
            };

        that.mostCommonLatitudes = [];
        that.fetchLatitudes = function() {
            that.fetchingLatitudes = true;
            $http.get('/mostCommonLatitudes').then(
                function (success) {
                    that.mostCommonLatitudes = success.data;
                },
                function (err) { growl.error("Unable to fetch latitudes due to " + err.statusText); }
            ).finally(function() {
                that.fetchingLatitudes = false;
            });
        };
        that.fetchLatitudes();

    });
}());