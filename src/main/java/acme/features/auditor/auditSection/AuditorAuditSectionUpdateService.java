
package acme.features.auditor.auditSection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Principal;
import acme.client.services.AbstractService;
import acme.entities.audit.AuditReport;
import acme.entities.audit.AuditSection;
import acme.realms.Auditor;

@Service
public class AuditorAuditSectionUpdateService extends AbstractService<Auditor, AuditSection> {

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

		status = this.section != null && report != null && report.isDraftMode() &&  // Solo editable si el informe está en borrador
			principal.hasRealmOfType(Auditor.class) && report.getAuditor().getId() == principal.getActiveRealm().getId();

		super.setAuthorised(status);
	}

	@Override
	public void load() {
		// Ya tenemos this.section del authorise
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
