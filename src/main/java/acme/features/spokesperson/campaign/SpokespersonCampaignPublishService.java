
package acme.features.spokesperson.campaign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.services.AbstractService;
import acme.entities.campaign.Campaign;
import acme.realms.Spokesperson;

@Service
public class SpokespersonCampaignPublishService extends AbstractService<Spokesperson, Campaign> {

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
		int id = super.getRequest().getData("id", int.class);
		this.campaign = this.repository.findCampaignById(id);
	}

	@Override
	public void bind() {
		// No bind on publish
	}

	@Override
	public void validate() {
		int count = this.repository.countMilestonesByCampaignId(this.campaign.getId());
		super.state(count > 0, "*", "spokesperson.campaign.form.error.no-milestones");

		if (this.campaign.getStartMoment() != null && this.campaign.getEndMoment() != null)
			super.state(this.campaign.getStartMoment().before(this.campaign.getEndMoment()), "endMoment", "spokesperson.campaign.form.error.start-before-end");
	}

	@Override
	public void execute() {
		this.campaign.setDraftMode(false);
		this.repository.save(this.campaign);
	}

	@Override
	public void unbind() {
		Tuple tuple;
		tuple = super.unbindObject(this.campaign, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode", "monthsActive", "effort");
		tuple.put("id", this.campaign.getId());
		tuple.put("readonly", true);
	}

}
