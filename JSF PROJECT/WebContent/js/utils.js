var sis = sis || {};

sis.utils = {
		
		confirm : function(message) {
			return confirm(message);
		},
		
		isEmptyString : function(str) {
			return str == undefined || str == null || str.length == 0;
		}
		
};
