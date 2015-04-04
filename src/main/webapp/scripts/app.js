/**
 * Created by Yakanet on 21/02/2015.
 */
/*global angular*/
angular.module('DevoxxApp', ['ui.bootstrap'])
.controller('ConferenceCtrl', function ($scope, $http) {
	  $http.get('rest/conferences')
      .success(function (result) {
          $scope.agenda = result.filter(function (key) {
              return !!key.talk;
          });
      });
})
.directive('slot', function () {
    return {
        scope: {
            event: '='
        },
        templateUrl: 'views/slot.html'
    };
});