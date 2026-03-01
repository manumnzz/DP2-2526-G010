
package acme.features.spokesperson.campaign;

import java.util.Date;

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
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		this.campaign = this.repository.findCampaignById(id);
	}

	@Override
	public void authorise() {
		boolean status = false;

		if (this.campaign != null) {
			int userAccountId = super.getRequest().getPrincipal().getAccountId();
			Spokesperson spokesperson = this.repository.findSpokespersonByUserAccountId(userAccountId);

			status = this.campaign.isDraftMode() && this.campaign.getSpokesperson().getId() == spokesperson.getId();
		}

		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.campaign, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		int count = this.repository.countMilestonesByCampaignId(this.campaign.getId());
		super.state(count > 0, "*", "spokesperson.campaign.form.error.no-milestones");

		if (!super.getResponse().getErrors().hasErrors("startMoment") && this.campaign.getStartMoment() != null) {
			Date now = new Date();
			super.state(this.campaign.getStartMoment().after(now), "startMoment", "spokesperson.campaign.form.error.start-future");
		}

		if (!super.getResponse().getErrors().hasErrors("endMoment") && this.campaign.getEndMoment() != null) {
			Date now = new Date();
			super.state(this.campaign.getEndMoment().after(now), "endMoment", "spokesperson.campaign.form.error.end-future");
		}

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
