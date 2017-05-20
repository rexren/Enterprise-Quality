'use strict';

angular.module('enterprise-quality').service('InpectionService', function($http) {
    var service = {
    	inspectionObj: {},
    		
    	setInspection: function(inspectionObj){
    		this.inspectionObj = inspectionObj;
    	},
    
    	getInspection: function(){
    		return this.inspection;
    	}
       
    };
    return service;
});
