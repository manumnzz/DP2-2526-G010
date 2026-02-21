
package acme.features.principal;

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import acme.client.components.principals.Authenticated;
import acme.client.controllers.AbstractController;
import acme.entities.strategy.Strategy;

@Controller
public class PrincipalStrategyController extends AbstractController<Authenticated, Strategy> {

	@PostConstruct
	protected void initialise() {
		super.setMediaType(MediaType.TEXT_HTML);

		super.addBasicCommand("list", PrincipalStrategyListService.class);
		super.addBasicCommand("show", PrincipalStrategyShowService.class);
	}
}
