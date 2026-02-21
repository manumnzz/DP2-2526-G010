
package acme.features.principal;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Authenticated;
import acme.client.services.AbstractService;
import acme.entities.strategy.Strategy;

@Service
public class PrincipalStrategyListService extends AbstractService<Authenticated, Strategy> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private PrincipalStrategyRepository	repository;

	private Collection<Strategy>		strategies;

	// AbstractService interface ----------------------------------------------


	@Override
	public void load() {
		this.strategies = this.repository.findManyPublishedStrategies();
	}

	@Override
	public void authorise() {
		super.setAuthorised(true);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.strategies, "ticker", "name", "startMoment", "endMoment", "moreInfo");
	}
}
