package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.inventions.Invention;
import acme.entities.inventions.InventionRepository;

@Validator
public class InventionValidator extends AbstractValidator<ValidInvention, Invention> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private InventionRepository repository;

	// ConstraintValidator interface ------------------------------------------

	@Override
	protected void initialise(final ValidInvention annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Invention invention, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (invention == null)
			result = true;
		else {

			// Ticker único
			{
				boolean uniqueInvention;
				Invention existing;

				existing = this.repository.findInventionByTicker(invention.getTicker());
				uniqueInvention = existing == null || existing.equals(invention);

				super.state(context, uniqueInvention, "ticker", "acme.validation.invention.duplicated-ticker.message");
			}

			// Inventor obligatorio por seguridad
			{
				boolean hasInventor;

				hasInventor = invention.getInventor() != null;

				super.state(context, hasInventor, "inventor", "acme.validation.invention.inventor-required.message");
			}
			// End moment posterior a start moment
			if (invention.getStartMoment() != null && invention.getEndMoment() != null)
				super.state(context, MomentHelper.isAfter(invention.getEndMoment(), invention.getStartMoment()), "endMoment", "inventor.invention.form.error.end-after-start");


			result = !super.hasErrors(context);
		}

		return result;
	}
}