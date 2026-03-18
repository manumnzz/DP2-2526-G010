
package acme.features.auditor.auditSection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Principal;
import acme.client.services.AbstractService;
import acme.entities.audit.AuditReport;
import acme.entities.audit.AuditSection;
import acme.realms.Auditor;

@Service
public class AuditorAuditSectionCreateService extends AbstractService<Auditor, AuditSection> {

	@Autowired
	private AuditorAuditSectionRepository	repository;

	private AuditSection					section;


	@Override
	public void authorise() {
		boolean status;
		int reportId;
		AuditReport report;
		Principal principal;

		reportId = super.getRequest().getData("reportId", int.class);
		report = this.repository.findAuditReportById(reportId);
		principal = super.getRequest().getPrincipal();

		status = report != null && report.isDraftMode() &&  // Solo se pueden añadir secciones a informes en borrador
			principal.hasRealmOfType(Auditor.class) && report.getAuditor().getId() == principal.getActiveRealm().getId();

		super.setAuthorised(status);
	}

	@Override
	public void load() {
		int reportId;
		AuditReport report;

		reportId = super.getRequest().getData("reportId", int.class);
		report = this.repository.findAuditReportById(reportId);

		this.section = new AuditSection();
		this.section.setAuditReport(report);
		this.section.setHours(1);
	}

	@Override
	public void bind() {
		super.bindObject(this.section, "name", "notes", "hours", "kind");
	}

	@Override
	public void validate() {
		boolean valid;

		valid = this.section.getHours() > 0;
		super.state(valid, "hours", "auditor.auditSection.error.hours-positive");

		super.validateObject(this.section);
	}

	@Override
	public void execute() {
		this.repository.save(this.section);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.section, "name", "notes", "hours", "kind");
	}
}
