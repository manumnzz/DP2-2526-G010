
package acme.features.any.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		Double expected;

		expected = this.repository.sumExpectedPercentageByStrategyId(this.strategy.getId());

		super.unbindObject(this.strategy, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");

		super.getResponse().addData("monthsActive", this.strategy.getMonthsActive());
		super.getResponse().addData("expectedPercentage", expected);

		super.getResponse().addData("fundraiserId", this.strategy.getFundraiser().getId());
	}
}
