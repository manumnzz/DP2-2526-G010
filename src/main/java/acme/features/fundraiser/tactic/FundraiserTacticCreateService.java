
package acme.features.fundraiser.tactic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.datatypes.TacticKind;
import acme.entities.strategy.Strategy;
import acme.entities.tactic.Tactic;
import acme.realms.Fundraiser;

@Service
public class FundraiserTacticCreateService extends AbstractService<Fundraiser, Tactic> {

	@Autowired
	private FundraiserTacticRepository	repository;

	private Strategy					strategy;
	private Tactic						tactic;


	@Override
	public void load() {
		int strategyId = super.getRequest().getData("strategyId", int.class);

		this.strategy = this.repository.findOneStrategyById(strategyId);

		this.tactic = new Tactic();
		this.tactic.setStrategy(this.strategy);
		this.tactic.setDraftMode(true);
	}

	@Override
	public void authorise() {
		boolean status = false;

		if (this.strategy != null) {
			int userAccountId = super.getRequest().getPrincipal().getAccountId();
			Fundraiser fundraiser = this.repository.findFundraiserByUserAccountId(userAccountId);

			status = fundraiser != null && this.strategy.isDraftMode() && this.strategy.getFundraiser().getId() == fundraiser.getId();
		}

		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.tactic, "name", "notes", "expectedPercentage", "kind");
	}

	@Override
	public void validate() {
		super.validateObject(this.tactic);
	}

	@Override
	public void execute() {
		this.repository.save(this.tactic);
	}

	@Override
	public void unbind() {
		SelectChoices kinds = SelectChoices.from(TacticKind.class, this.tactic.getKind());
		Tuple tuple = super.unbindObject(this.tactic, "name", "notes", "expectedPercentage", "kind", "draftMode");

		tuple.put("kinds", kinds);
		tuple.put("strategyId", this.strategy.getId());
		tuple.put("readonly", false);
	}
}
