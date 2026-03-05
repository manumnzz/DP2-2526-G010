
package acme.features.spokesperson.campaign;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.services.AbstractService;
import acme.entities.campaign.Campaign;
import acme.realms.Spokesperson;

@Service
public class SpokespersonCampaignCreateService extends AbstractService<Spokesperson, Campaign> {

	@Autowired
	private SpokespersonCampaignRepository	repository;

	private Campaign						campaign;


	@Override
	public void load() {
		int userAccountId = super.getRequest().getPrincipal().getAccountId();
		Spokesperson spokesperson = this.repository.findSpokespersonByUserAccountId(userAccountId);

		this.campaign = new Campaign();
		this.campaign.setSpokesperson(spokesperson);
		this.campaign.setDraftMode(true);
	}

	@Override
	public void authorise() {
		super.setAuthorised(true);
	}

	@Override
	public void bind() {
		super.bindObject(this.campaign, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		super.validateObject(this.campaign);

		if (!super.getResponse().getErrors().hasErrors("startMoment") && !super.getResponse().getErrors().hasErrors("endMoment")) {
			Date start = this.campaign.getStartMoment();
			Date end = this.campaign.getEndMoment();

			if (start != null && end != null && !start.before(end))
				super.state(false, "endMoment", "spokesperson.campaign.form.error.end-before-start");
		}
	}

	@Override
	public void execute() {
		this.repository.save(this.campaign);
	}

	@Override
	public void unbind() {
		Tuple tuple = super.unbindObject(this.campaign, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
		tuple.put("draftMode", true);
	}
}
