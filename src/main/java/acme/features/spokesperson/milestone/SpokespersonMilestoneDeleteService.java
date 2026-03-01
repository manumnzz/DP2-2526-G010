
package acme.features.spokesperson.milestone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.milestone.Milestone;
import acme.realms.Spokesperson;

@Service
public class SpokespersonMilestoneDeleteService extends AbstractService<Spokesperson, Milestone> {

	@Autowired
	private SpokespersonMilestoneRepository	repository;

	private Milestone						milestone;


	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		this.milestone = this.repository.findMilestoneById(id);
	}

	@Override
	public void authorise() {
		boolean status = false;

		if (this.milestone != null) {
			int userAccountId = super.getRequest().getPrincipal().getAccountId();
			Spokesperson spokesperson = this.repository.findSpokespersonByUserAccountId(userAccountId);

			status = this.milestone.getCampaign().isDraftMode() && this.milestone.getCampaign().getSpokesperson().getId() == spokesperson.getId();
		}

		super.setAuthorised(status);
	}

	@Override
	public void execute() {
		this.repository.delete(this.milestone);
	}

}
