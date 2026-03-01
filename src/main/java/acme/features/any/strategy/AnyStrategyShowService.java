
package acme.features.any.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.strategy.Strategy;

@Service
public class AnyStrategyShowService extends AbstractService<Any, Strategy> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyStrategyRepository	repository;

	private Strategy				strategy;

	// AbstractService interface ---------------------------------------------


	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.strategy = this.repository.findOneStrategyById(id);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.strategy != null && !this.strategy.isDraftMode();
		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		Tuple tuple;

		tuple = super.unbindObject(this.strategy, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");

		int strategyId = this.strategy.getId();

		Double expectedPercentage = this.repository.sumExpectedPercentageByStrategyId(strategyId);
		Double monthsActive = this.strategy.getMonthsActive();

		tuple.put("expectedPercentage", expectedPercentage);
		tuple.put("monthsActive", monthsActive);
		tuple.put("fundraiserId", this.strategy.getFundraiser().getId());

		tuple.put("readonly", true);
	}
}
