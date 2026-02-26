
package acme.features.fundraiser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.strategy.Strategy;
import acme.realms.Fundraiser;

@Service
public class FundraiserStrategyDeleteService extends AbstractService<Fundraiser, Strategy> {

	@Autowired
	private FundraiserStrategyRepository	repository;

	private Strategy						strategy;


	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		this.strategy = this.repository.findOneStrategyById(id);
	}

	@Override
	public void authorise() {
		boolean status = false;

		if (this.strategy != null) {
			int userAccountId = super.getRequest().getPrincipal().getAccountId();
			Fundraiser fundraiser = this.repository.findFundraiserByUserAccountId(userAccountId);

			status = this.strategy.isDraftMode() && this.strategy.getFundraiser().getId() == fundraiser.getId();
		}

		super.setAuthorised(status);
	}

	@Override
	public void validate() {

	}

	@Override
	public void execute() {
		this.repository.delete(this.strategy);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.strategy, "ticker", "name", "description", "draftMode");
	}
}
