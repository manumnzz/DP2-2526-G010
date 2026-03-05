
package acme.features.any.spokesperson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.realms.Spokesperson;

@Service
public class AnySpokespersonShowService extends AbstractService<Any, Spokesperson> {

	@Autowired
	private AnySpokespersonRepository	repository;

	private Spokesperson				spokesperson;


	@Override
	public void authorise() {
		boolean status;
		int id;
		Spokesperson sp;

		id = super.getRequest().getData("id", int.class);
		sp = this.repository.findSpokespersonById(id);
		status = sp != null;
		super.setAuthorised(status);
	}

	@Override
	public void load() {
		int id;
		id = super.getRequest().getData("id", int.class);
		this.spokesperson = this.repository.findSpokespersonById(id);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.spokesperson, "cv", "achievements", "licensed");
	}

}
