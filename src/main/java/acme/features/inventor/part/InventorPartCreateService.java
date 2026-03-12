package acme.features.inventor.part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.components.datatypes.Money;
import acme.client.services.AbstractService;
import acme.entities.inventions.Invention;
import acme.entities.inventions.Part;
import acme.entities.inventions.PartKind;
import acme.realms.Inventor;

@Service
public class InventorPartCreateService extends AbstractService<Inventor, Part> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private InventorPartRepository repository;

	private Part part;

	// AbstractService interface ----------------------------------------------

	@Override
	public void load() {
		int inventionId;
		Invention invention;
		Money defaultCost;

		inventionId = super.getRequest().getData("inventionId", int.class);
		invention = this.repository.findInventionById(inventionId);

		defaultCost = new Money();
		defaultCost.setAmount(0.0);
		defaultCost.setCurrency("EUR");

		this.part = super.newObject(Part.class);
		this.part.setName("");
		this.part.setDescription("");
		this.part.setCost(defaultCost);
		this.part.setKind(PartKind.CORE);   // o el valor por defecto que prefieras
		this.part.setInvention(invention);
	}

	@Override
	public void authorise() {
		boolean status;
		int inventionId;
		Invention invention;
		Inventor inventor;

		inventionId = super.getRequest().getData("inventionId", int.class);
		invention = this.repository.findInventionById(inventionId);
		inventor = (Inventor) super.getRequest().getPrincipal().getActiveRealm();

		status = inventor != null
			&& invention != null
			&& invention.isDraftMode()
			&& invention.getInventor() != null
			&& invention.getInventor().getId() == inventor.getId();

		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.part, "name", "description", "cost", "kind");
	}

	@Override
	public void validate() {
		super.validateObject(this.part);
	}

	@Override
	public void execute() {
		this.repository.save(this.part);
	}

	@Override
	public void unbind() {
		SelectChoices choices;
		Tuple tuple;

		choices = SelectChoices.from(PartKind.class, this.part.getKind());

		tuple = super.unbindObject(this.part, "name", "description", "cost", "kind");
		tuple.put("kinds", choices);
		tuple.put("inventionId", this.part.getInvention().getId());
		tuple.put("draftMode", this.part.getInvention().isDraftMode());
	}
}