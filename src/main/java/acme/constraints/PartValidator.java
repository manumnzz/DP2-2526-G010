package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.inventions.Part;

@Validator
public class PartValidator extends AbstractValidator<ValidPart, Part> {

	// ConstraintValidator interface ------------------------------------------

	@Override
	protected void initialise(final ValidPart annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Part part, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (part == null)
			result = true;
		else {

			// Name no demasiado largo
			{
				boolean validNameLength;
				String name;

				name = part.getName();
				validNameLength = name == null || name.trim().length() <= 75;

				super.state(context, validNameLength, "name", "acme.validation.part.name-too-long.message");
			}

			// Description no demasiado larga
			{
				boolean validDescriptionLength;
				String description;

				description = part.getDescription();
				validDescriptionLength = description == null || description.trim().length() <= 255;

				super.state(context, validDescriptionLength, "description", "acme.validation.part.description-too-long.message");
			}

			// Cost obligatorio por seguridad
			{
				boolean hasCost;

				hasCost = part.getCost() != null;

				super.state(context, hasCost, "cost", "acme.validation.part.cost-required.message");
			}

			// Kind obligatorio por seguridad
			{
				boolean hasKind;

				hasKind = part.getKind() != null;

				super.state(context, hasKind, "kind", "acme.validation.part.kind-required.message");
			}

			// Invention obligatoria por seguridad
			{
				boolean hasInvention;

				hasInvention = part.getInvention() != null;

				super.state(context, hasInvention, "invention", "acme.validation.part.invention-required.message");
			}

			result = !super.hasErrors(context);
		}

		return result;
	}
}