
package acme.features.fundraiser.tactic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.datatypes.TacticKind;
import acme.entities.tactic.Tactic;
import acme.realms.Fundraiser;

@Service
public class FundraiserTacticShowService extends AbstractService<Fundraiser, Tactic> {

	@Autowired
	private FundraiserTacticRepository	repository;

	private Tactic						tactic;


	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		this.tactic = this.repository.findOneTacticById(id);
	}

	@Override
	public void authorise() {
		boolean status = false;

		if (this.tactic != null) {
			int userAccountId = super.getRequest().getPrincipal().getAccountId();
			Fundraiser fundraiser = this.repository.findFundraiserByUserAccountId(userAccountId);

			status = fundraiser != null && this.tactic.getStrategy().getFundraiser().getId() == fundraiser.getId();
		}

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		SelectChoices choices;
		Tuple tuple;

		choices = SelectChoices.from(TacticKind.class, this.tactic.getKind());

		tuple = super.unbindObject(this.tactic, "name", "notes", "expectedPercentage", "draftMode");
		tuple.put("kind", choices.getSelected().getKey());
		tuple.put("kinds", choices);

		tuple.put("strategyId", this.tactic.getStrategy().getId());
		tuple.put("readonly", !this.tactic.isDraftMode() || !this.tactic.getStrategy().isDraftMode());
	}
}
