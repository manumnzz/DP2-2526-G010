
package acme.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import acme.entities.audit.AuditSection;

public class AuditSectionValidator implements ConstraintValidator<ValidAuditSection, AuditSection> {

	@Override
	public void initialize(final ValidAuditSection annotation) {
	}

	@Override
	public boolean isValid(final AuditSection section, final ConstraintValidatorContext context) {
		if (section == null)
			return true;

		boolean result = true;

		if (section.getKind() == null) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.auditSection.kind-null.message").addPropertyNode("kind").addConstraintViolation();
			result = false;
		}

		if (section.getHours() == null || section.getHours() <= 0) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.auditSection.hours-positive.message").addPropertyNode("hours").addConstraintViolation();
			result = false;
		}

		return result;
	}
}
