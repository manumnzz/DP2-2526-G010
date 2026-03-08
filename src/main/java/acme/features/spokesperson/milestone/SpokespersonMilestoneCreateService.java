
package acme.features.spokesperson.milestone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.datatypes.MilestoneKind;
import acme.entities.campaign.Campaign;
import acme.entities.milestone.Milestone;
import acme.realms.Spokesperson;

@Service
public class SpokespersonMilestoneCreateService extends AbstractService<Spokesperson, Milestone> {

	@Autowired
	private SpokespersonMilestoneRepository	repository;

	private Campaign						campaign;
	private Milestone						milestone;


	@Override
	public void load() {
		int campaignId;
		campaignId = super.getRequest().getData("campaignId", int.class);
		this.campaign = this.repository.findCampaignById(campaignId);
		this.milestone = new Milestone();
		this.milestone.setCampaign(this.campaign);
	}

	@Override
	public void authorise() {
		boolean status = false;
		Spokesperson sp;

		if (this.campaign != null) {
			sp = this.repository.findSpokespersonByUserAccountId(super.getRequest().getPrincipal().getAccountId());
			status = sp != null && this.campaign.isDraftMode() && this.campaign.getSpokesperson().getId() == sp.getId();
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
		tuple = super.unbindObject(this.milestone, "title", "achievements", "effort", "kind");
		tuple.put("kinds", kinds);
		tuple.put("campaignId", this.campaign.getId());
		tuple.put("readonly", false);

		super.getResponse().addData(tuple);
	}

}
