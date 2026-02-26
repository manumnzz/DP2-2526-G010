
package acme.features.fundraiser;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.strategy.Strategy;
import acme.realms.Fundraiser;

@Service
public class FundraiserStrategyListService extends AbstractService<Fundraiser, Strategy> {

	@Autowired
	private FundraiserStrategyRepository	repository;

	private Collection<Strategy>			strategies;


	@Override
	public void load() {
		int userAccountId = super.getRequest().getPrincipal().getAccountId();
		Fundraiser fundraiser = this.repository.findFundraiserByUserAccountId(userAccountId);

		this.strategies = this.repository.findManyStrategiesByFundraiserId(fundraiser.getId());
	}

	@Override
	public void authorise() {
		super.setAuthorised(true);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.strategies, "ticker", "name", "startMoment", "endMoment", "draftMode");
	}
}
