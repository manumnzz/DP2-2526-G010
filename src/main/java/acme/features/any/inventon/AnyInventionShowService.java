package acme.features.any.inventon;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.principals.Any;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.inventions.Invention;
import acme.realms.Inventor;

@Service
public class AnyInventionShowService extends AbstractService<Any, Invention>{
	
	// Internal state ---------------------------------------------------------

		@Autowired
		private AnyInventionRepository repository;
		
		private Invention invention;
		
		// AbstractService interface -------------------------------------------
		
		@Override
		public void load() {
			int id;
			
			id = super.getRequest().getData("id", int.class);
			this.invention = this.repository.findInventionById(id);
		}
		
		@Override
		public void authorise() {
		    boolean status;

		    status = this.invention != null && 
		             !this.invention.isDraftMode();

		    super.setAuthorised(status);
		}
		
		@Override
		public void unbind() {
			Collection<Inventor> inventors;
			SelectChoices choices;
			Tuple tuple;

			inventors = this.repository.findAllInventors();
			choices = SelectChoices.from(inventors, "userAccount.username", this.invention.getInventor());

			tuple = super.unbindObject(this.invention,
				"ticker", "name", "description", "startMoment", "endMoment",
				"moreInfo", "draftMode", "monthsActive", "cost"
			);

			tuple.put("inventor", choices.getSelected().getKey());
			tuple.put("inventors", choices);
		}

}
