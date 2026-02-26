
package acme.features.fundraiser;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.services.AbstractService;
import acme.entities.strategy.Strategy;
import acme.entities.tactic.Tactic;
import acme.realms.Fundraiser;

@Service
public class FundraiserStrategyPublishService extends AbstractService<Fundraiser, Strategy> {

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

	}

	@Override
	public void validate() {
		int count = this.repository.countTacticsByStrategyId(this.strategy.getId());
		super.state(count > 0, "*", "fundraiser.strategy.error.no-tactics");

		if (!super.getResponse().getErrors().hasErrors("startMoment") && this.strategy.getStartMoment() != null) {
			Date now = new Date();
			super.state(this.strategy.getStartMoment().after(now), "startMoment", "fundraiser.strategy.error.start-future");
		}

		if (!super.getResponse().getErrors().hasErrors("endMoment") && this.strategy.getEndMoment() != null) {
			Date now = new Date();
			super.state(this.strategy.getEndMoment().after(now), "endMoment", "fundraiser.strategy.error.end-future");
		}

		if (this.strategy.getStartMoment() != null && this.strategy.getEndMoment() != null)
			super.state(this.strategy.getStartMoment().before(this.strategy.getEndMoment()), "endMoment", "fundraiser.strategy.error.start-before-end");
	}

	@Override
	public void execute() {
		this.strategy.setDraftMode(false);
		this.repository.save(this.strategy);

		Collection<Tactic> tactics = this.repository.findManyTacticsByStrategyId(this.strategy.getId());
		for (Tactic t : tactics) {
			t.setDraftMode(false);
			this.repository.save(t);
		}
	}

	@Override
	public void unbind() {
		Tuple tuple;

		tuple = super.unbindObject(this.strategy, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
		tuple.put("id", this.strategy.getId());
		tuple.put("readonly", true);
	}
}
