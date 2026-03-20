package acme.features.inventor.invention;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.inventions.Invention;
import acme.realms.Inventor;

@Service
public class InventorInventionPublishService extends AbstractService<Inventor, Invention> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private InventorInventionRepository repository;

	private Invention invention;

	// AbstractService interface ----------------------------------------------

	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.invention = this.repository.findInventionById(id);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.invention != null && this.invention.isDraftMode() && this.invention.getInventor().isPrincipal();

		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.invention, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		boolean hasParts = !this.repository.findPartsByInventionId(this.invention.getId()).isEmpty();
		super.state(hasParts, "*", "inventor.invention.form.error.no-parts");

		Date now = MomentHelper.getCurrentMoment();

		if (!super.getResponse().getErrors().hasErrors("startMoment") && this.invention.getStartMoment() != null)
			super.state(MomentHelper.isAfter(this.invention.getStartMoment(), now), "startMoment", "inventor.invention.form.error.start-future");

		if (!super.getResponse().getErrors().hasErrors("endMoment") && this.invention.getEndMoment() != null)
			super.state(MomentHelper.isAfter(this.invention.getEndMoment(), now), "endMoment", "inventor.invention.form.error.end-future");

		if (this.invention.getStartMoment() != null && this.invention.getEndMoment() != null)
			super.state(MomentHelper.isAfter(this.invention.getEndMoment(), this.invention.getStartMoment()), "endMoment", "inventor.invention.form.error.start-before-end");

		super.state(this.invention.isDraftMode(), "*", "inventor.invention.form.error.already-published");
	}
	
	@Override
	public void execute() {
		this.invention.setDraftMode(false);
		this.repository.save(this.invention);
	}

	@Override
	public void unbind() {
		Tuple tuple;

		tuple = super.unbindObject(this.invention, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode", "monthsActive", "cost");

		super.getResponse().addData(tuple);
	}
}