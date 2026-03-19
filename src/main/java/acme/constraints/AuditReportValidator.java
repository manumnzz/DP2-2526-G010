
package acme.constraints;

import java.util.Collection;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import acme.client.helpers.MomentHelper;
import acme.entities.audit.AuditReport;
import acme.entities.audit.AuditSection;

public class AuditReportValidator implements ConstraintValidator<ValidAuditReport, AuditReport> {

	@Override
	public void initialize(final ValidAuditReport annotation) {
	}

	@Override
	public boolean isValid(final AuditReport auditReport, final ConstraintValidatorContext context) {
		if (auditReport == null)
			return true;

		boolean result = true;

		if (!auditReport.isDraftMode()) {
			Collection<AuditSection> sections = auditReport.getSections();
			if (sections == null || sections.isEmpty()) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate("acme.validation.auditReport.publish-without-sections.message").addPropertyNode("draftMode").addConstraintViolation();
				result = false;
			}
		}

		if (!auditReport.isDraftMode()) {
			if (MomentHelper.isBefore(auditReport.getStartMoment(), MomentHelper.getCurrentMoment())) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate("acme.validation.auditReport.start-past.message").addPropertyNode("startMoment").addConstraintViolation();
				result = false;
			}

			if (MomentHelper.isBefore(auditReport.getEndMoment(), MomentHelper.getCurrentMoment())) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate("acme.validation.auditReport.end-past.message").addPropertyNode("endMoment").addConstraintViolation();
				result = false;
			}
		}

		if (MomentHelper.isBefore(auditReport.getEndMoment(), auditReport.getStartMoment())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.auditReport.end-before-start.message").addPropertyNode("endMoment").addConstraintViolation();
			result = false;
		}

		return result;
	}
}
