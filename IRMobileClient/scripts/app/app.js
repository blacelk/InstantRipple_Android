﻿(function () {
    var irApp = angular.module('irApp', ['ngCordova', 'ionic', 'angularMoment', 'autocomplete', 'monospaced.qrcode']);

    ionic.Platform.ready(function () {
        if (typeof (window.tinyHippos) == "undefined") {
            if (!window.tinyHippos) {
                ionic.Platform.fullScreen(false, true);
            }
        }
    });

    irApp.config([
        '$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {
            $urlRouterProvider.otherwise('/login');

            $stateProvider
                .state('login', {
                    url: '/login',
                    templateUrl: 'views/login.html',
                    controller: 'loginController'
                })
                .state('unfunded', {
                    templateUrl: 'views/unfunded.html',
                    controller: 'unfundedController'
                })
                .state('balances', {
                    templateUrl: 'views/balances.html',
                    controller: 'balancesController'
                })
                .state('send', {
                    templateUrl: 'views/send.html',
                    controller: 'sendController'
                })
                .state('transactions', {
                    templateUrl: 'views/transactions.html',
                    controller: 'transactionsController'
                })
                .state('contacts', {
                    templateUrl: 'views/contacts.html',
                    controller: 'contactsController'
                })
                .state('trustlines', {
                    templateUrl: 'views/trustlines.html',
                    controller: 'trustlinesController'
                });
        }
    ]);
})();