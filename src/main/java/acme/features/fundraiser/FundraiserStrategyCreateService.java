
package acme.features.fundraiser;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.services.AbstractService;
import acme.entities.strategy.Strategy;
import acme.realms.Fundraiser;

@Service
public class FundraiserStrategyCreateService extends AbstractService<Fundraiser, Strategy> {

	@Autowired
	private FundraiserStrategyRepository	repository;

	private Strategy						strategy;


	@Override
	public void load() {
		int userAccountId = super.getRequest().getPrincipal().getAccountId();
		Fundraiser fundraiser = this.repository.findFundraiserByUserAccountId(userAccountId);

		this.strategy = new Strategy();
		this.strategy.setFundraiser(fundraiser);
		this.strategy.setDraftMode(true);
	}

	@Override
	public void authorise() {
		super.setAuthorised(true);
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
		Tuple tuple = super.unbindObject(this.strategy, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
		tuple.put("draftMode", true);
	}
}
