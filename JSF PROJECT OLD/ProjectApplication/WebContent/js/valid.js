var sis = sis || {};

sis.valid = {
		
		validateProjectForm : function(formId) {
			var isFormValid = true;
			
			if(!sis.valid.isRequiredFieldValid(formId, "title")) {
				isFormValid = false;
			}
			
			if(!sis.valid.isRequiredFieldValid(formId, "description")) {
				isFormValid = false;
			}
			
			var startDate = sis.valid.isDateFieldValid(formId, "startDate");			
			var endDate = sis.valid.isDateFieldValid(formId, "endDate");
			if(startDate.isValid && endDate.isValid) {
				if(startDate.year > endDate.year || startDate.year == endDate.year && startDate.month > endDate.month || startDate.year == endDate.year && startDate.month == endDate.month && startDate.day > endDate.day) {
					isFormValid = false;
					sis.valid.showErrorLabel("startDate", "Start Date must be less or equal than End Date!");
				}
			}
			else {
				isFormValid = false;
			}
			
			if(!sis.valid.isNumberFieldValid(formId, "amount")) {
				isFormValid = false;
			}
			
			if(!sis.valid.isRequiredFieldValid(formId, "currency")) {
				isFormValid = false;
			}
			
			if(!sis.valid.isRequiredFieldValid(formId, "sector")) {
				isFormValid = false;
			}
			
			if(!sis.valid.isRequiredFieldValid(formId, "subSector")) {
				isFormValid = false;
			}
			
			return isFormValid;
		},
		
		validateContactForm : function(formId) {
			var isFormValid = true;			
			
			if(!sis.valid.isRequiredFieldValid(formId, "firstName")) {
				isFormValid = false;
			}
			
			if(!sis.valid.isRequiredFieldValid(formId, "lastName")) {
				isFormValid = false;
			}
			
			if(!sis.valid.isRequiredFieldValid(formId, "organisation")) {
				isFormValid = false;
			}
			
			if(!sis.valid.isEmailFieldValid(formId, "email")) {
				isFormValid = false;
			}
			
			if(!sis.valid.isPhoneFieldValid(formId, "phone")) {
				isFormValid = false;
			}
			
			return isFormValid;
			
		},
		
		validateOrganisationForm : function(formId) {
			var isFormValid = true;
			
			if(!sis.valid.isRequiredFieldValid(formId, "organisation")) {
				isFormValid = false;
			}
			
			if(!sis.valid.isRequiredFieldValid(formId, "description")) {
				isFormValid = false;
			}
			
			return isFormValid;			
		},
		
		isRequiredFieldValid : function(formId, elementId, message /*optional*/) {
			var isValid = true;
			var element = document.getElementById(formId + ":" + elementId);
			var value = (element != null ? element.value.trim() : null);
			
			if(sis.utils.isEmptyString(value)) {
				sis.valid.showErrorLabel(elementId, message);
				isValid = false;
			}
			else {
				sis.valid.hideErrorLabel(elementId);
			}
			
			return isValid;
		},
		
		isDateFieldValid : function(formId, elementId, message /*optional*/) {
			var element = document.getElementById(formId + ":" + elementId);
			var value = (element != null ? element.value.trim() : null);
			var regexp = /^(\d{2})\/(\d{2})\/(\d{4})$/;
			var parsedValue = regexp.exec(value);
			
			if(parsedValue == null || parsedValue[1] > 31 || parsedValue[2] > 12) {
				sis.valid.showErrorLabel(elementId, message);
				return { isValid : false };
			}
			else {
				sis.valid.hideErrorLabel(elementId);
			}
			
			return { isValid : true, year : parsedValue[3], month : parsedValue[2], day : parsedValue[1] };
		},
		
		isNumberFieldValid : function(formId, elementId, message /*optional*/) {
			var isValid = true;
			var element = document.getElementById(formId + ":" + elementId);
			var value = (element != null ? element.value.trim() : null);
			var regexp = /^-?\d+(\.\d+)?$/ig;
			var parsedValue = regexp.exec(value);
			
			if(parsedValue == null) {
				sis.valid.showErrorLabel(elementId, message);
				isValid = false;
			}
			else {
				sis.valid.hideErrorLabel(elementId);
			}
			
			return isValid;
		},
		
		isEmailFieldValid : function(formId, elementId, message /*optional*/) {
			var isValid = true;
			var element = document.getElementById(formId + ":" + elementId);
			var value = (element != null ? element.value.trim() : null);
			var regexp = /^\w+@\w+\.[a-z]{2,3}$/ig;
			var parsedValue = regexp.exec(value);
			
			if(parsedValue == null) {
				sis.valid.showErrorLabel(elementId, message);
				isValid = false;
			}
			else {
				sis.valid.hideErrorLabel(elementId);
			}
			
			return isValid;
		},
		
		isPhoneFieldValid : function(formId, elementId, message /*optional*/) {
			var isValid = true;
			var element = document.getElementById(formId + ":" + elementId);
			var value = (element != null ? element.value.trim() : null);
			var regexp = /^\d{3}-\d{3}-\d{3}$/ig;
			var parsedValue = regexp.exec(value);
			
			if(parsedValue == null) {
				sis.valid.showErrorLabel(elementId, message);
				isValid = false;
			}
			else {
				sis.valid.hideErrorLabel(elementId);
			}
			
			return isValid;
		},
		
		showErrorLabel : function(forElementId, message /*optional*/) {
			if(sis.utils.isEmptyString(forElementId)) {
				return;
			}
			
			var label = document.getElementById("error_" + forElementId);
			
			if(label == null) {
				return;
			}
			
			if(!sis.utils.isEmptyString(message)) {
				label.innerHTML = message;
			}
			
			label.style.display = "block";
		},
		
		hideErrorLabel : function(forElementId) {
			if(sis.utils.isEmptyString(forElementId)) {
				return;
			}
			
			var label = document.getElementById("error_" + forElementId);
			
			if(label == null) {
				return;
			}
			
			label.style.display = "none";
		}
		
};
