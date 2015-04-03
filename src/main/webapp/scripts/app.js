/**
 * Created by Yakanet on 21/02/2015.
 */
/*global angular*/
angular.module('DevoxxApp', ['ui.bootstrap', 'ngSanitize', 'ngAnimate'])
    .run(function ($rootScope, $location) {
        var websocket_url = "ws://" + $location.host() + ":" + $location.port() + "/ws/planning";
        var ws = new WebSocket(websocket_url);
        ws.onmessage = function (message) {
            $rootScope.$apply(function () {
                $rootScope.$broadcast('registred', angular.fromJson(message.data));
            });
        };
    })
    .controller('ConferenceCtrl', function ($scope, $http, $modal) {
        $scope.day = 'wednesday';
        $http.get('rest/conferences')
            .success(function (result) {
                $scope.agenda = result.filter(function (key) {
                    return !!key.talk;
                });
            });
        $http.get('rest/conferences/attendees')
            .success(function (result) {
                $scope.attendees = result;
            });
        $scope.$on('registred', function (event, slot) {
            if ($scope.attendees) { // in case this message arrives before attendees is initialized
                $scope.attendees[slot.slotId] = slot.attendees;
            }
        });
        $scope.select = function (conference) {
            $modal.open({
                templateUrl: 'views/conference-dialog.html',
                controller: 'ConferenceDialogCtrl',
                resolve: {
                    conference: function () {
                        return conference;
                    }
                }
            }).result.then(function (result) {
                    var action = result.attending ? 'increment' : 'decrement';
                    $http.head('rest/conferences/attendees/' + action + '/' + result.conference.slotId)
                });
        };
    })
    .controller('ConferenceDialogCtrl', function ($scope, $modalInstance, conference, $http) {
        $scope.conference = conference;
        $http.get('rest/attendee/slots').then(function (result) {
            $scope.isAttending = result.data.indexOf(conference.slotId) >= 0;
        });
        $scope.submit = function () {
            $modalInstance.close({attending: !$scope.isAttending, conference: conference});
        };
        $scope.cancel = function () {
            $modalInstance.dismiss();
        };
    })
    .directive('slot', function () {
        return {
            scope: {
                event: '=',
                attendee: '='
            },
            templateUrl: 'views/slot.html'
        };
    });