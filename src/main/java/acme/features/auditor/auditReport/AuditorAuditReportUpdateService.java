
package acme.features.auditor.auditReport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Principal;
import acme.client.services.AbstractService;
import acme.entities.audit.AuditReport;
import acme.realms.Auditor;

@Service
public class AuditorAuditReportUpdateService extends AbstractService<Auditor, AuditReport> {

	@Autowired
	private AuditorAuditReportRepository	repository;

	private AuditReport						auditReport;


	@Override
	public void authorise() {
		boolean status;
		int id;
		Principal principal;

		id = super.getRequest().getData("id", int.class);
		this.auditReport = this.repository.findAuditReportById(id);
		principal = super.getRequest().getPrincipal();

		status = this.auditReport != null && this.auditReport.isDraftMode() &&  // Solo editable si está en borrador
			principal.hasRealmOfType(Auditor.class) && this.auditReport.getAuditor().getId() == principal.getActiveRealm().getId();

		super.setAuthorised(status);
	}

	@Override
	public void load() {
	}

	@Override
	public void bind() {
		super.bindObject(this.auditReport, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		boolean valid;

		// Validar que endMoment > startMoment
		valid = this.auditReport.getEndMoment().after(this.auditReport.getStartMoment());
		super.state(valid, "endMoment", "auditor.auditReport.error.end-before-start");

		super.validateObject(this.auditReport);
	}

	@Override
	public void execute() {
		this.repository.save(this.auditReport);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.auditReport, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
	}
}
