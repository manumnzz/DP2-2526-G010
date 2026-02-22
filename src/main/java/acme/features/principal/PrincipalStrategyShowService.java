
package acme.features.principal;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.principals.Authenticated;
import acme.client.services.AbstractService;
import acme.entities.strategy.Strategy;
import acme.entities.tactic.Tactic;
import acme.realms.Fundraiser;

@Service
public class PrincipalStrategyShowService extends AbstractService<Authenticated, Strategy> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private PrincipalStrategyRepository	repository;

	private Strategy					strategy;

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

		Collection<Tactic> tactics = this.repository.findManyPublishedTacticsByStrategyId(strategyId);
		Fundraiser fundraiser = this.strategy.getFundraiser();

		Double expectedPercentage = this.repository.sumExpectedPercentageByStrategyId(strategyId);

		tuple.put("tactics", tactics);
		tuple.put("fundraiser", fundraiser);
		tuple.put("expectedPercentage", expectedPercentage);

		tuple.put("readonly", true);
	}
}
