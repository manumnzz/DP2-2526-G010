
package acme.features.spokesperson.milestone;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.campaign.Campaign;
import acme.entities.milestone.Milestone;
import acme.realms.Spokesperson;

@Service
public class SpokespersonMilestoneListService extends AbstractService<Spokesperson, Milestone> {

	@Autowired
	private SpokespersonMilestoneRepository	repository;

	private Campaign						campaign;
	private Collection<Milestone>			milestones;


	@Override
	public void load() {
		int campaignId = super.getRequest().getData("campaignId", int.class);
		this.campaign = this.repository.findCampaignById(campaignId);

		if (this.campaign != null)
			this.milestones = this.repository.findMilestonesByCampaignId(campaignId);
	}

	@Override
	public void authorise() {
		boolean status = false;

		if (this.campaign != null) {
			int userAccountId = super.getRequest().getPrincipal().getAccountId();
			Spokesperson spokesperson = this.repository.findSpokespersonByUserAccountId(userAccountId);

			status = this.campaign.getSpokesperson().getId() == spokesperson.getId();
		}

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.milestones, "title", "kind", "effort");
	}
}
