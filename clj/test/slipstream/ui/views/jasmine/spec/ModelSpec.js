	describe("model", function() {
	
	 it("extractOutputParameters should return an array of output parameters sorted by their names", function() {
      var outputParameters = window.SlipStream.model.extractOutputParameters(json);

			expect(outputParameters).toEqual(
				[
				{					
      				"category": "Output",
      				"defaultValue": null,
      				"defaultValueFromParent": null,
      				"description": "hostname/ip of the image",
      				"id": 54,
      				"inheritedEnumValue": false,
      				"instructions": null,
      				"mandatory": true,
      				"name": "hostname",
      				"order": 0,
      				"readonly": false,
      				"restrictedValue": false,
      				"safeValue": "'null'",
      				"set": false,
      				"true": false,
      				"type": "String",
      				"value": null
    			},
    			{
      				"category": "Output",
      				"defaultValue": null,
      				"defaultValueFromParent": null,
      				"description": "Cloud instance id",
      				"id": 56,
      				"inheritedEnumValue": false,
      				"instructions": null,
      				"mandatory": true,
      				"name": "instanceid",
      				"order": 0,
      				"readonly": false,
      				"restrictedValue": false,
      				"safeValue": "'null'",
      				"set": false,
      				"true": false,
      				"type": "String",
      				"value": null
    			},
				{
      				"category": "Output",
      				"defaultValue": "123",
      				"defaultValueFromParent": null,
      				"description": "",
      				"id": 59,
      				"inheritedEnumValue": false,
      				"instructions": null,
      				"mandatory": false,
      				"name": "param1",
      				"order": 0,
      				"readonly": false,
      				"restrictedValue": false,
      				"safeValue": "'123'",
      				"set": true,
      				"true": false,
      				"type": "String",
      				"value": "123"},
      			{
      				"category": "Output",
      				"defaultValue": "2",
      				"defaultValueFromParent": null,
      				"description": "",
      				"id": 57,
      				"inheritedEnumValue": false,
      				"instructions": null,
      				"mandatory": false,
      				"name": "zzz",
      				"order": 0,
      				"readonly": false,
      				"restrictedValue": false,
      				"safeValue": "'2'",
      				"set": true,
      				"true": false,
      				"type": "String",
      				"value": "2"
    			}]);

          var namesOfOutputParameters = $.map(outputParameters, function(p) {return p.name;});
          expect(namesOfOutputParameters.sort()).toEqual(namesOfOutputParameters);
		});	

    it("extractOutputParametersNames should return an array of output parameters names sorted", function() {
        var outputParameterNames = window.SlipStream.model.extractOutputParametersNames(json);              
        expect(outputParameterNames).toEqual(["hostname", "instanceid", "param1", "zzz"]);
    });
		
    it("extractInputParameters should return an array of input parameters sorted by their names", function(){
				var inputParameters = window.SlipStream.model.extractInputParameters(json);
              
				expect(inputParameters).toEqual(
				[
				{
      				"category": "Input",
      				"defaultValue": "1",
      				"defaultValueFromParent": null,
      				"description": "",
      				"id": 55,
      				"inheritedEnumValue": false,
      				"instructions": null,
      				"mandatory": false,
      				"name": "abc",
      				"order": 0,
      				"readonly": false,
      				"restrictedValue": false,
      				"safeValue": "'1'",
      				"set": true,
      				"true": false,
      				"type": "String",
      				"value": "1" },
      			{
      				"category": "Input",
      				"defaultValue": null,
      				"defaultValueFromParent": null,
      				"description": "",
      				"id": 61,
      				"inheritedEnumValue": false,
      				"instructions": null,
      				"mandatory": false,
      				"name": "param2",
      				"order": 0,
      				"readonly": false,
      				"restrictedValue": false,
      				"safeValue": "'null'",
      				"set": false,
      				"true": false,
      				"type": "String",
      				"value": null }]);

        var namesOfInputParameters = $.map(inputParameters, function(p) {return p.name;});
        expect(namesOfInputParameters.sort()).toEqual(namesOfInputParameters);
				
		});

		it("extractInputParametersNames should return an array of input parameters names sorted", function() {
      var inputParameterNames = window.SlipStream.model.extractInputParametersNames(json);				
			expect(inputParameterNames).toEqual(["abc", "param2"]);
		});


	var json = 
{
  "authz": {
    "groupCreateChildren": false,
    "groupDelete": false,
    "groupGet": false,
    "groupPost": false,
    "groupPut": false,
    "inheritedGroupMembers": true,
    "ownerCreateChildren": true,
    "ownerDelete": true,
    "ownerGet": true,
    "ownerPost": true,
    "ownerPut": true,
    "publicCreateChildren": false,
    "publicDelete": false,
    "publicGet": false,
    "publicPost": false,
    "publicPut": false,
    "user": "super"
  },
  "base": true,
  "category": "Image",
  "cloudImageIdentifiers": [],
  "cloudNames": [
    "default"
  ],
  "commit": {
    "author": "super",
    "comment": ""
  },
  "creation": 1417630176548,
  "customVersion": null,
  "deleted": false,
  "description": "wonderfull image",
  "guardedParent": {
    "authz": {
      "groupCreateChildren": true,
      "groupDelete": true,
      "groupGet": true,
      "groupPost": false,
      "groupPut": true,
      "inheritedGroupMembers": true,
      "ownerCreateChildren": true,
      "ownerDelete": true,
      "ownerGet": true,
      "ownerPost": false,
      "ownerPut": true,
      "publicCreateChildren": false,
      "publicDelete": false,
      "publicGet": false,
      "publicPost": false,
      "publicPut": false,
      "user": "super"
    },
    "category": "Project",
    "children": [],
    "cloudNames": null,
    "commit": {
      "author": "super",
      "comment": "Initial version of this project."
    },
    "creation": 1417629919320,
    "customVersion": null,
    "deleted": false,
    "description": "test in ze boat",
    "guardedParent": null,
    "lastModified": 1417629919321,
    "logoLink": null,
    "moduleReference": null,
    "name": "joe1",
    "owner": "super",
    "parameters": {},
    "parent": "module/",
    "published": null,
    "resourceUri": "module/joe1/1",
    "runs": null,
    "shortName": "joe1",
    "tag": null,
    "version": 1,
    "virtual": false
  },
  "isBase": true,
  "lastModified": 1418138618811,
  "loginUser": "",
  "logoLink": "",
  "moduleReference": null,
  "name": "joe1/image1",
  "owner": "super",
  "packages": [],
  "packagesEmpty": true,
  "parameters": {
    "hostname": {
      "category": "Output",
      "defaultValue": null,
      "defaultValueFromParent": null,
      "description": "hostname/ip of the image",
      "id": 54,
      "inheritedEnumValue": false,
      "instructions": null,
      "mandatory": true,
      "name": "hostname",
      "order": 0,
      "readonly": false,
      "restrictedValue": false,
      "safeValue": "'null'",
      "set": false,
      "true": false,
      "type": "String",
      "value": null
    },
    "abc": {
      "category": "Input",
      "defaultValue": "1",
      "defaultValueFromParent": null,
      "description": "",
      "id": 55,
      "inheritedEnumValue": false,
      "instructions": null,
      "mandatory": false,
      "name": "abc",
      "order": 0,
      "readonly": false,
      "restrictedValue": false,
      "safeValue": "'1'",
      "set": true,
      "true": false,
      "type": "String",
      "value": "1"
    },
    "instanceid": {
      "category": "Output",
      "defaultValue": null,
      "defaultValueFromParent": null,
      "description": "Cloud instance id",
      "id": 56,
      "inheritedEnumValue": false,
      "instructions": null,
      "mandatory": true,
      "name": "instanceid",
      "order": 0,
      "readonly": false,
      "restrictedValue": false,
      "safeValue": "'null'",
      "set": false,
      "true": false,
      "type": "String",
      "value": null
    },
    "zzz": {
      "category": "Output",
      "defaultValue": "2",
      "defaultValueFromParent": null,
      "description": "",
      "id": 57,
      "inheritedEnumValue": false,
      "instructions": null,
      "mandatory": false,
      "name": "zzz",
      "order": 0,
      "readonly": false,
      "restrictedValue": false,
      "safeValue": "'2'",
      "set": true,
      "true": false,
      "type": "String",
      "value": "2"
    },
    "extra.disk.volatile": {
      "category": "Cloud",
      "defaultValue": null,
      "defaultValueFromParent": null,
      "description": "Volatile extra disk in GB",
      "id": 58,
      "inheritedEnumValue": false,
      "instructions": null,
      "mandatory": true,
      "name": "extra.disk.volatile",
      "order": 0,
      "readonly": false,
      "restrictedValue": false,
      "safeValue": "'null'",
      "set": false,
      "true": false,
      "type": "String",
      "value": null
    },
    "param1": {
      "category": "Output",
      "defaultValue": "123",
      "defaultValueFromParent": null,
      "description": "",
      "id": 59,
      "inheritedEnumValue": false,
      "instructions": null,
      "mandatory": false,
      "name": "param1",
      "order": 0,
      "readonly": false,
      "restrictedValue": false,
      "safeValue": "'123'",
      "set": true,
      "true": false,
      "type": "String",
      "value": "123"
    },
    "network": {
      "category": "Cloud",
      "defaultValue": "Public",
      "defaultValueFromParent": null,
      "description": "Network type",
      "id": 60,
      "inheritedEnumValue": false,
      "instructions": null,
      "mandatory": true,
      "name": "network",
      "order": 0,
      "readonly": false,
      "restrictedValue": false,
      "safeValue": "'Public'",
      "set": true,
      "true": false,
      "type": "Enum",
      "value": "Public"
    },
    "param2": {
      "category": "Input",
      "defaultValue": null,
      "defaultValueFromParent": null,
      "description": "",
      "id": 61,
      "inheritedEnumValue": false,
      "instructions": null,
      "mandatory": false,
      "name": "param2",
      "order": 0,
      "readonly": false,
      "restrictedValue": false,
      "safeValue": "'null'",
      "set": false,
      "true": false,
      "type": "String",
      "value": null
    }
  },
  "parent": "module/joe1",
  "platform": "centos",
  "preRecipe": "",
  "preRecipeEmpty": true,
  "published": null,
  "recipe": "",
  "recipeEmpty": true,
  "resourceUri": "module/joe1/image1/20",
  "runs": {
    "list": []
  },
  "shortName": "image1",
  "tag": null,
  "targets": [
    {
      "id": 13,
      "name": "onvmadd",
      "script": ""
    },
    {
      "id": 14,
      "name": "onvmremove",
      "script": ""
    },
    {
      "id": 16,
      "name": "report",
      "script": ""
    },
    {
      "id": 15,
      "name": "execute",
      "script": ""
    }
  ],
  "version": 20,
  "virtual": true
};


	});
