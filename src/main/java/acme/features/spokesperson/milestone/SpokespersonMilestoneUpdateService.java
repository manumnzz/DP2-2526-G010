
package acme.features.spokesperson.milestone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.datatypes.MilestoneKind;
import acme.entities.milestone.Milestone;
import acme.realms.Spokesperson;

@Service
public class SpokespersonMilestoneUpdateService extends AbstractService<Spokesperson, Milestone> {

	@Autowired
	private SpokespersonMilestoneRepository	repository;

	private Milestone						milestone;


	@Override
	public void load() {
		int id;
		id = super.getRequest().getData("id", int.class);
		this.milestone = this.repository.findMilestoneById(id);
	}

	@Override
	public void authorise() {
		boolean status = false;
		Spokesperson sp;

		if (this.milestone != null) {
			sp = this.repository.findSpokespersonByUserAccountId(super.getRequest().getPrincipal().getAccountId());
			status = sp != null && this.milestone.getCampaign().isDraftMode() && this.milestone.getCampaign().getSpokesperson().getId() == sp.getId();
		}
		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.milestone, "title", "achievements", "effort", "kind");
	}

	@Override
	public void validate() {
		super.validateObject(this.milestone);
	}

	@Override
	public void execute() {
		this.repository.save(this.milestone);
	}

	@Override
	public void unbind() {
		Tuple tuple;
		SelectChoices kinds;

		kinds = SelectChoices.from(MilestoneKind.class, this.milestone.getKind());
		tuple = super.unbindObject(this.milestone, "title", "achievements", "effort");
		tuple.put("kind", kinds.getSelected().getKey());
		tuple.put("kinds", kinds);
		tuple.put("campaignId", this.milestone.getCampaign().getId());
		tuple.put("draftMode", this.milestone.getCampaign().isDraftMode());
		tuple.put("readonly", false);

		super.getResponse().addData(tuple);
	}

}
