# 00_mise_en_place
# Generation des POJOs 

Cette étape montre comment passer des réponses des services REST de l'api du cfp de devoxx vers des JavaBeans.

Le plugin maven est capable de générer les classes Java depuis un schémas JSON.

Le service http://jsonschema.net/ permet de générer des schémas JSON depuis un document JSON valide, c'est une aide précieuse.

Voici les deux documents types qui nous ont servis  à générer les schémas.

Pour lancer la compilation est la génération des Javabeans : mvn -gs devoxx-settings.xml clean install.

Pour lancer l'application avec le TomEE embedded : mvn -gs devoxx-settings.xml  tomee-embedded:run

Il y a deux utilisateurs configuré pour le basic auth, devoxx et devoxx2. Le mot de passe est identique au login.

Links : 
-------
    {
        "links":[{
			"href": "http://cfp.devoxx.be/api/conferences/DV14/schedules/monday/",
			"rel": "http://cfp.devoxx.be/api/profile/schedule",
			"title": "Schedule for Monday 10th November 2015"
		}]
	}

Slots :
-------
    {
    	"slots":[{
	    	"fromTimeMillis":-1716607680,
	    	"toTimeMillis":-1705807680,
	    	"roomSetup":"theatre",
	    	"roomName":"Room 4",
	    	"roomId":"room4",
	    	"slotId":"uni_room4_monday_10_13h30_16h30",
	    	"notAllocated":false,
	    	"day":"monday",
	    	"fromTime":"13:30",
	    	"roomCapacity":364,
	    	"toTime":"16:30",
	    	"talk":{
	    		"id":"QKM-0129",
	    		"summary":"Ever wondered about all the new ...... ",
	    		"title":"Mastering xPaaS - get down and dirty in the OpenShift Cloud ",
	    		"talkType":"University",
	    		"track":"Cloud & BigData",
	    		"summaryAsHtml":"<p>Ever wondered about all t...",
	    		"speakers":[{
	    			"link":{
	    				"title":"Eric D. Schabell",
	    				"rel":"http://cfp.devoxx.be/api/profile/speaker",
	    				"href":"http://cfp.devoxx.be/api/conferences/DevoxxFR2015/speakers/8c77c532774576a25bdc3fb6e51e067d5112d1c3"
	    			},
	    			"name":"Eric D. Schabell"
	    		}],
	    		"lang":"en"
	    	}
	    }]
	}
