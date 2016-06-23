(function () {
    var app = angular.module("airports");

    app.controller("QueryController", function ($scope, $http, $rootScope, growl, $location) {
         $rootScope.pageTitle = 'Query';
         var that = this;

         that.userText = null;

         that.isBusy = false;

         that.page = { items: [], from: 0, to: 0, total: 0 };
         that.currentPage = 1;
         that.itemsPerPage = 10;
         that.maxSize = 5;

         that.updatePage = function(newCurrentPage) {
             $http.get('/airportsByCountryCode',
                    {
                         params: {
                           code: that.userText,
                           p: newCurrentPage-1,
                           s: that.itemsPerPage,
                         }
                     }
             ).then(
                 function (success) {
                     that.page = success.data;
                 },
                 function (err) { growl.error("Unable to find airports due to " + err.statusText); }
             );
         };

        that.selectRow = function(row) {
            console.log("row selected ->>>>>>>>");
        };
    });
}());