
package acme.features.any.campaign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.campaign.Campaign;

@Service
public class AnyCampaignShowService extends AbstractService<Any, Campaign> {

	@Autowired
	private AnyCampaignRepository	repository;

	private Campaign				campaign;


	@Override
	public void load() {
		int id;
		try {
			id = super.getRequest().getData("id", int.class);
			this.campaign = this.repository.findCampaignById(id);
		} catch (Exception e) {
			this.campaign = null;
		}
	}

	@Override
	public void authorise() {
		boolean status;
		status = this.campaign != null && !this.campaign.isDraftMode();
		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		Tuple tuple;
		Double effort;

		tuple = super.unbindObject(this.campaign, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");

		effort = this.repository.computeCampaignEffort(this.campaign.getId());
		tuple.put("effort", effort == null ? 0.0 : effort);
		tuple.put("monthsActive", this.campaign.getMonthsActive());
		tuple.put("spokespersonId", this.campaign.getSpokesperson().getId());
		tuple.put("spokesperson.userAccount.identity.fullName", this.campaign.getSpokesperson().getUserAccount().getIdentity().getFullName());
		tuple.put("readonly", true);

		super.getResponse().addData(tuple);
	}

}
