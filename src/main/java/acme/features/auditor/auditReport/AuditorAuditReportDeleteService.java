
package acme.features.auditor.auditReport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Principal;
import acme.client.services.AbstractService;
import acme.entities.audit.AuditReport;
import acme.realms.Auditor;

@Service
public class AuditorAuditReportDeleteService extends AbstractService<Auditor, AuditReport> {

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

		status = this.auditReport != null && this.auditReport.isDraftMode() &&  // Solo borrable si está en borrador
			principal.hasRealmOfType(Auditor.class) && this.auditReport.getAuditor().getId() == principal.getActiveRealm().getId();

		super.setAuthorised(status);
	}

	@Override
	public void load() {
		// Ya tenemos this.auditReport del authorise
	}

	@Override
	public void bind() {
		// No hay bind en delete
	}

	@Override
	public void validate() {
		boolean hasSections;

		// No se puede borrar si tiene secciones (opcional, según reglas)
		hasSections = !this.repository.findSectionsByReportId(this.auditReport.getId()).isEmpty();
		super.state(!hasSections, "*", "auditor.auditReport.error.has-sections");
	}

	@Override
	public void execute() {
		this.repository.delete(this.auditReport);
	}

	@Override
	public void unbind() {
		// No hay unbind en delete
	}
}
