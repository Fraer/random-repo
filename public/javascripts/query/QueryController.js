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

         that.asyncSelected = null;
         that.getLocation = function(val) {
             return $http.get('/fetchCountries', {
               params: {
                 input: val
               }
             }
             ).then(function(response){
               return response.data;
             });
           };

         that.updatePage = function(newCurrentPage) {
             that.searching = true;
             growl.info("Searching for airports in " + that.asyncSelected.name + " please wait.");
             $http.get('/airportsByCountryCode',
                    {
                         params: {
                           code: that.asyncSelected.code,
                           p: newCurrentPage-1,
                           s: that.itemsPerPage
                         }
                     }
             ).then(
                 function (success) {
                     that.page = success.data;
                     growl.success("Displaying from " + that.page.from + " to " + that.page.to + " out of " + that.page.total + " airports in " + that.asyncSelected.name);
                 },
                 function (err) { growl.error("Unable to find airports due to " + err.statusText); }
             ).finally(function(){
                     that.searching = false;
             });
         };

        that.selectRow = function(row) {
            console.log("row selected ->>>>>>>>");
        };
    });
}());