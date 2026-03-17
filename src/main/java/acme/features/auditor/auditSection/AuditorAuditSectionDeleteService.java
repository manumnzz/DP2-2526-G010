
package acme.features.auditor.auditSection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Principal;
import acme.client.services.AbstractService;
import acme.entities.audit.AuditReport;
import acme.entities.audit.AuditSection;
import acme.realms.Auditor;

@Service
public class AuditorAuditSectionDeleteService extends AbstractService<Auditor, AuditSection> {

	@Autowired
	private AuditorAuditSectionRepository	repository;

	private AuditSection					section;


	@Override
	public void authorise() {
		boolean status;
		int id;
		AuditReport report;
		Principal principal;

		id = super.getRequest().getData("id", int.class);
		this.section = this.repository.findSectionById(id);
		report = this.section != null ? this.section.getAuditReport() : null;
		principal = super.getRequest().getPrincipal();

		status = this.section != null && report != null && report.isDraftMode() &&  // Solo borrable si el informe está en borrador
			principal.hasRealmOfType(Auditor.class) && report.getAuditor().getId() == principal.getActiveRealm().getId();

		super.setAuthorised(status);
	}

	@Override
	public void load() {
	}

	@Override
	public void execute() {
		this.repository.delete(this.section);
	}

}
