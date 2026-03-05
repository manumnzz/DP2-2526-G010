
package acme.features.spokesperson.campaign;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.services.AbstractService;
import acme.entities.campaign.Campaign;
import acme.entities.milestone.Milestone;
import acme.realms.Spokesperson;

@Service
public class SpokespersonCampaignDeleteService extends AbstractService<Spokesperson, Campaign> {

	@Autowired
	private SpokespersonCampaignRepository	repository;

	private Campaign						campaign;


	@Override
	public void authorise() {
		boolean status = false;
		int id;
		Campaign c;
		Spokesperson sp;

		id = super.getRequest().getData("id", int.class);
		c = this.repository.findCampaignById(id);

		if (c != null) {
			sp = this.repository.findSpokespersonByUserAccountId(super.getRequest().getPrincipal().getAccountId());
			status = sp != null && c.isDraftMode() && c.getSpokesperson().getId() == sp.getId();
		}
		super.setAuthorised(status);
	}

	@Override
	public void load() {
		int id;
		id = super.getRequest().getData("id", int.class);
		this.campaign = this.repository.findCampaignById(id);
	}

	@Override
	public void bind() {
		// delete no necesita bind
	}

	@Override
	public void validate() {
		// delete no necesita validación adicional
	}

	@Override
	public void execute() {
		Collection<Milestone> milestones;
		milestones = this.repository.findMilestonesByCampaignId(this.campaign.getId());
		this.repository.deleteAll(milestones);
		this.repository.delete(this.campaign);
	}

	@Override
	public void unbind() {
		Tuple tuple;

		tuple = super.unbindObject(this.campaign, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");

		tuple.put("monthsActive", this.campaign.getMonthsActive());
		tuple.put("effort", this.campaign.getEffort());
		tuple.put("id", this.campaign.getId());
		tuple.put("readonly", false);

		super.getResponse().addData(tuple);
	}

}
