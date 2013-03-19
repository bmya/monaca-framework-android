/**
 * Plugin for InnovationPlus API
 * require PhoneGap2.2.0 or higher
 *
 * --has these API--
 * user.login
 * user.getAuthKey
 * user.removeAuthKey
 * 
 * profile.retrieveResource
 * profile.retrieveQueryResource
 * 
 * geolocation.retrieveOwnResource
 * geolocation.retrieveResource
 * geolocation.createResource
 * geolocation.deleteResource
 * geolocation.retrieveQueryResource
 * 
 * applicationResource.retrieveResource
 * applicationResource.retrieveQueryResource
 * applicationResource.createResource 
 * applicationResource.deleteResource
 * 
 * e.g. InnovationPlus.login('hoge', 'foo', function(JSONObject){success();}, function(int){fail();}); 
 * 
 * each methods has success and fail callback
 * note that fail callback is called when IPPQueryCallback#ippDidError(int) is called;
 */
window.InnovationPlus = window.InnovationPlus || {};
(function() {
	var pluginName = 'InnovationPlusPlugin';
	var exec = function(a, b, c, d, e) {
		if (monaca.apiQueue.exec != null) {
			//console.log('monaca exec');
			monaca.apiQueue.exec(a,b,c,d,e);
		} else {
			//console.log('cordova exec');
			cordova.exec(a,b,c,d,e);
		}
	}
	// User
	InnovationPlus.user = InnovationPlus.user || {};

	InnovationPlus.user.login = function(username, password, success, fail) {
		username = username || null;
		password = password || null;
		success = success || null;
		fail = fail || null;

		var loginJson = {
			'username' : username,
			'password' : password
		};
		exec(success, fail, pluginName, 'user.login', [loginJson]);
	}
	
	InnovationPlus.user.getAuthKey = function(callback) {
		callback = callback || null;
		exec(callback, null, pluginName, 'user.getAuthKey', null);
	}

	// Profile
	InnovationPlus.profile = InnovationPlus.profile || {};

	InnovationPlus.profile.retrieveResource = function(param, success, fail) {
		param = param || null;
		success = success || null;
		fail = fail || null;
		exec(success, fail, pluginName, 'profile.retrieveResource', [param]);
	}

	InnovationPlus.profile.retrieveQueryResource = function(param, success, fail) {
		param = param || null;
		success = success || null;
		fail = fail || null;
		exec(success, fail, pluginName, 'profile.retrieveQueryResource', [param]);
	}

	// Geolocation
	InnovationPlus.geolocation = InnovationPlus.geolocation || {};

	InnovationPlus.geolocation.retrieveOwnResource = function(success, fail) {
		success = success || null;
		fail = fail || null;
		exec(success, fail, pluginName, 'geolocation.retrieveOwnResource', null);
	}

	InnovationPlus.geolocation.retrieveResource = function(resourceId, success, fail) {
		success = success || null;
		fail = fail || null;
		resourceId = resourceId || null;
		exec(success, fail, pluginName, 'geolocation.retrieveResource', [resourceId]);
	}

	InnovationPlus.geolocation.createResource = function(requestJson, success, fail) {
		// will support geolocations.createResources
		success = success || null;
		fail = fail || null;
		requestJson = requestJson || null;
		exec(success, fail, pluginName, 'geolocation.createResource', [requestJson]);
	}

	InnovationPlus.geolocation.deleteResource = function(resourceId, success, fail) {
		success = success || null;
		fail = fail || null;
		requestJson = requestJson || null;
		exec(success, fail, pluginName, 'geolocation.deleteResource', [resourceId]);
	}

	InnovationPlus.geolocation.retrieveQueryResource = function(param, success, fail) {
		// originally 'geolocations' in API
		success = success || null;
		fail = fail || null;
		param = param || null;
		exec(success, fail, pluginName, 'geolocation.retrieveQueryResource', [param]);
	}

	// ApplicationResource
	InnovationPlus.applicationResource = InnovationPlus.applicationResource || {};

	InnovationPlus.applicationResource.retrieveResource = function(param, success, fail) {
		success = success || null;
		fail = fail || null;
		param = param || null;
		exec(success, fail, pluginName, 'applicationResource.retrieveResource', [param]);
	}

	InnovationPlus.applicationResource.retrieveQueryResource = function(param, success, fail) {
		// (document:8.2)
		success = success || null;
		fail = fail || null;
		param = param || null;
		exec(success, fail, pluginName, 'applicationResource.retrieveQueryResource', [param]);
	}

	InnovationPlus.applicationResource.createResource = function(requestJson, success, fail) {
		// will support createResources (document:8.3)
		success = success || null;
		fail = fail || null;
		requestJson = requestJson || null;
		exec(success, fail, pluginName, 'applicationResource.createResource', [requestJson]);
	}

	InnovationPlus.applicationResource.deleteResource = function(resourceId, success, fail) {
		success = success || null;
		fail = fail || null;
		resourceId = resourceId || null;
		exec(success, fail, pluginName, 'applicationResource.deleteResource', [resourceId]);
	}
})();