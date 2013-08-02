var scanner = require('./devices');
var shell = require('shelljs');

scanner(function(err, devices){
	console.log('devices:' + devices);
	 if (err){
	 	// console.log('[BUILD] Error scanning for android devices: ' + devices);
	 } else {
        var numDs = 0;
        for (var d in devices) if (devices.hasOwnProperty(d)) numDs++;
        if (numDs > 0) {                    
                for (var d in devices) if (devices.hasOwnProperty(d)) (function(id) {
                    var device = devices[id];
                    var version = device.version;
                    var model = device.model;
                    console.log('device:' + JSON.stringify(d) + ', model:' + model );

                    var deploy_cmd = 'ant -Dadb.device.arg="-s ' + d + '" custom-location';
                    shell.exec( deploy_cmd, {silent:false, async:false}, function(code, output) {
				        if (code > 0) {
				            console.log('Could not test on device: ' + JSON.stringify(device));
				        } else {
				     		console.log('ant debug install success')
				        }
				    });
                }(d));                      
        }else{
            console.log('[BUILD] Error no device connected');
        }
    }
}); 