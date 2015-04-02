package fr.devoxx.javaeeasy.models;

import java.util.logging.Logger;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import fr.devoxx.javaeeasy.models.event.SlotUpdateEvent;

public class SlotLogger {

	@Inject
	private Logger logger;
	
	public void logActivity(@Observes final SlotUpdateEvent conferences){
		logger.info(String.format("Schedule activity (%d slots retrieved)", + conferences.getConferences().size()));
	}
}
