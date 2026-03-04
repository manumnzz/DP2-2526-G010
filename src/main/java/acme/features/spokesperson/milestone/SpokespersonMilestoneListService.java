
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
	public void authorise() {
		boolean status = false;
		int campaignId;
		Campaign c;
		Spokesperson sp;

		campaignId = super.getRequest().getData("campaignId", int.class);
		c = this.repository.findCampaignById(campaignId);

		if (c != null) {
			sp = this.repository.findSpokespersonByUserAccountId(super.getRequest().getPrincipal().getAccountId());
			status = sp != null && c.getSpokesperson().getId() == sp.getId();
		}
		super.setAuthorised(status);
	}

	@Override
	public void load() {
		int campaignId;
		campaignId = super.getRequest().getData("campaignId", int.class);
		this.campaign = this.repository.findCampaignById(campaignId);

		if (this.campaign != null)
			this.milestones = this.repository.findMilestonesByCampaignId(campaignId);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.milestones, "title", "kind", "effort");
		super.getResponse().addGlobal("campaignId", this.campaign.getId());
	}

}
