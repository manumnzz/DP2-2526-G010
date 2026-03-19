package acme.features.any.inventor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.realms.Inventor;

@Service
public class AnyInventorShowService extends AbstractService<Any, Inventor> {

	@Autowired
	private AnyInventorRepository repository;

	private Inventor inventor;


	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.inventor = this.repository.findOneInventorById(id);
	}

	@Override
	public void authorise() {
		super.setAuthorised(this.inventor != null);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.inventor, "bio", "kewWords", "licensed");
	}
}