
package acme.features.fundraiser.strategy;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.services.AbstractService;
import acme.entities.strategy.Strategy;
import acme.realms.Fundraiser;

@Service
public class FundraiserStrategyUpdateService extends AbstractService<Fundraiser, Strategy> {

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
	public void bind() {
		super.bindObject(this.strategy, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		super.validateObject(this.strategy);

		if (!super.getResponse().getErrors().hasErrors("startMoment") && !super.getResponse().getErrors().hasErrors("endMoment")) {
			Date start = this.strategy.getStartMoment();
			Date end = this.strategy.getEndMoment();

			if (start != null && end != null && !start.before(end))
				super.state(false, "endMoment", "fundraiser.strategy.error.start-before-end");
		}
	}

	@Override
	public void execute() {
		this.repository.save(this.strategy);
	}

	@Override
	public void unbind() {
		Tuple tuple = super.unbindObject(this.strategy, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
		tuple.put("readonly", !this.strategy.isDraftMode());
	}
}
