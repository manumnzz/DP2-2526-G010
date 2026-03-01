package acme.constraints;

import java.util.Date;

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
		// HINT: invention can be null
		assert context != null;

		boolean result;

		if (invention == null)
			result = true;
		else {
			{
				boolean uniqueInvention;
				Invention existing;

				existing = this.repository.findInventionByTicker(invention.getTicker());
				uniqueInvention = existing == null || existing.equals(invention);

				super.state(context, uniqueInvention, "ticker", "acme.validation.invention.duplicated-ticker.message");
			}
			{
				boolean correctPeriod;
				Date start, end;

				start = invention.getStartMoment();
				end = invention.getEndMoment();

				// si alguno es null, dejamos que lo gestione @Mandatory/@ValidMoment
				correctPeriod = start == null || end == null || MomentHelper.isAfter(end, start);

				super.state(context, correctPeriod, "endMoment", "acme.validation.invention.period.message");
			}

			result = !super.hasErrors(context);
		}

		return result;
	}
}