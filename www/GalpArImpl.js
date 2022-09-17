var exec = require('cordova/exec');

exports.openObject = function (params, success, error) {
    exec(success, error, 'GalpArImpl', 'openObject', [params]);
};
