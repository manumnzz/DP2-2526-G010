package acme.features.any.inventon;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.inventions.Invention;

@Service
public class AnyInventionListService extends AbstractService<Any, Invention>{
	
	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyInventionRepository repository;
	
	private Collection<Invention> inventions;
	
	// AbstractService interface -------------------------------------------
	
	@Override
	public void load() {
		Date currentMoment;
		
		currentMoment = MomentHelper.getCurrentMoment();
		this.inventions = this.repository.findInventionsByAvailability(currentMoment);
	}
	
	@Override
	public void authorise() {
		super.setAuthorised(true);
	}
	
	@Override
	public void unbind() {
	    super.unbindObjects(this.inventions, //
	        "ticker", "name", "description", "startMoment", "endMoment", //
	        "moreInfo", "draftMode", "monthsActive", "cost");
	}

}
