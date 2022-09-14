var exec = require('cordova/exec');

exports.scan = function (params, success, error) {

    
    exec(success, error, 'OSBarcodeScanner', 'scan', []);
};
