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
 * e.g. InnovationPlus.login('MyId@hogehoge.foo', 'foobar4000', onSucceeded(JSONObject), onFailed(int)); 
 * 
 * each methods has success and fail callback
 * note that fail callback is called when IPPQueryCallback#ippDidError(int) is called;
 * 
 * errorcodes defined by this plugin 
 * -20 : no auth key
 * -40 : internal error
 * -60 : invalid parameter
 */
window.InnovationPlus = window.InnovationPlus || {};
(function() {
	var pluginName = 'InnovationPlusPlugin';
	var exec = function(a, b, c, d, e) {
		if (monaca.apiQueue.exec != null) {
			//console.log('-------------monaca exec------------------');
			monaca.apiQueue.exec(a,b,c,d,e);
		} else {
			//console.log('-------------cordova exec---------------------');
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
		exec(success, fail, pluginName, 'User.login', [loginJson]);
	}
	
	InnovationPlus.user.getAuthKey = function(callback) {
		callback = callback || null;
		exec(callback, null, pluginName, 'User.getAuthKey', null);
	}
	
	InnovationPlus.user.removeAuthKey = function(callback) {
		callback = callback || null;
		exec(callback, null, pluginName, 'User.removeAuthKey', null);
	}

	// Profile
	InnovationPlus.profile = InnovationPlus.profile || {};

	InnovationPlus.profile.retrieveResource = function(fields, success, fail) {
		fields = fields || null;
		success = success || null;
		fail = fail || null;
		exec(success, fail, pluginName, 'Profile.retrieveResource', [fields]);
	}

	InnovationPlus.profile.retrieveQueryResource = function(param, success, fail) {
		param = param || null;
		success = success || null;
		fail = fail || null;
		exec(success, fail, pluginName, 'Profile.retrieveQueryResource', [param]);
	}

	// Geolocation
	InnovationPlus.geolocation = InnovationPlus.geolocation || {};

	InnovationPlus.geolocation.retrieveOwnResource = function(success, fail) {
		success = success || null;
		fail = fail || null;
		exec(success, fail, pluginName, 'Geolocation.retrieveOwnResource', null);
	}

	InnovationPlus.geolocation.retrieveResource = function(resourceId, success, fail) {
		success = success || null;
		fail = fail || null;
		resourceId = resourceId || null;
		exec(success, fail, pluginName, 'Geolocation.retrieveResource', [resourceId]);
	}

	InnovationPlus.geolocation.createResource = function(requestJson, success, fail) {
		/* supports geolocations.createResources
		* requestJson format should be
		* {}
		*/
		success = success || null;
		fail = fail || null;
		requestJson = requestJson || null;
		exec(success, fail, pluginName, 'Geolocation.createResource', [requestJson]);
	}

	InnovationPlus.geolocation.deleteResource = function(resourceId, success, fail) {
		success = success || null;
		fail = fail || null;
		requestJson = requestJson || null;
		exec(success, fail, pluginName, 'Geolocation.deleteResource', [resourceId]);
	}

	InnovationPlus.geolocation.retrieveQueryResource = function(param, success, fail) {
		// originally 'Geolocations' in API
		// 'bound' : [top, bottom, left, right];
		// 'radiusSquare' : [centerLatitude, centerLongitude, radiusSquare]
		success = success || null;
		fail = fail || null;
		param = param || null;
		exec(success, fail, pluginName, 'Geolocation.retrieveQueryResource', [param]);
	}

	// ApplicationResource
	InnovationPlus.applicationResource = InnovationPlus.applicationResource || {};

	InnovationPlus.applicationResource.retrieveResource = function(resourceId, success, fail) {
		success = success || null;
		fail = fail || null;
		field = field || null;
		
		exec(success, fail, pluginName, 'ApplicationResource.retrieveResource', [resourceId]);
	}

	InnovationPlus.applicationResource.retrieveQueryResource = function(param, success, fail) {
		// (8.2)
		success = success || null;
		fail = fail || null;
		param = param || null;
		exec(success, fail, pluginName, 'ApplicationResource.retrieveQueryResource', [param]);
	}

	InnovationPlus.applicationResource.createResource = function(requestJson, success, fail) {
		// supports createResources (8.3)
		success = success || null;
		fail = fail || null;
		requestJson = requestJson || null;
		exec(success, fail, pluginName, 'ApplicationResource.createResource', [requestJson]);
	}

	InnovationPlus.applicationResource.deleteResource = function(resourceId, success, fail) {
		success = success || null;
		fail = fail || null;
		resourceId = resourceId || null;
		exec(success, fail, pluginName, 'ApplicationResource.deleteResource', [resourceId]);
	}
})();