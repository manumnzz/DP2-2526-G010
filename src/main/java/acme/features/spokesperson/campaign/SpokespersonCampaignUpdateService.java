
package acme.features.spokesperson.campaign;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.services.AbstractService;
import acme.entities.campaign.Campaign;
import acme.realms.Spokesperson;

@Service
public class SpokespersonCampaignUpdateService extends AbstractService<Spokesperson, Campaign> {

	@Autowired
	private SpokespersonCampaignRepository	repository;

	private Campaign						campaign;


	@Override
	public void load() {
		int id;
		id = super.getRequest().getData("id", int.class);
		this.campaign = this.repository.findCampaignById(id);
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
		super.bindObject(this.campaign, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		Date start;
		Date end;

		super.validateObject(this.campaign);

		if (!super.getResponse().getErrors().hasErrors("startMoment") && !super.getResponse().getErrors().hasErrors("endMoment")) {
			start = this.campaign.getStartMoment();
			end = this.campaign.getEndMoment();
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
		Tuple tuple;

		tuple = super.unbindObject(this.campaign, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
		tuple.put("monthsActive", this.campaign.getMonthsActive());
		tuple.put("effort", this.campaign.getEffort());
		tuple.put("id", this.campaign.getId());
		tuple.put("readonly", false);

		super.getResponse().addData(tuple);
	}

}
