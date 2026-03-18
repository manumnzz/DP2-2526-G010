
package acme.features.auditor.auditSection;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Principal;
import acme.client.services.AbstractService;
import acme.entities.audit.AuditReport;
import acme.entities.audit.AuditSection;
import acme.realms.Auditor;

@Service
public class AuditorAuditSectionListService extends AbstractService<Auditor, AuditSection> {

	@Autowired
	private AuditorAuditSectionRepository	repository;

	private Collection<AuditSection>		sections;


	@Override
	public void authorise() {
		boolean status;
		int reportId;
		AuditReport report;
		Principal principal;

		reportId = super.getRequest().getData("reportId", int.class);
		report = this.repository.findAuditReportById(reportId);
		principal = super.getRequest().getPrincipal();

		status = report != null && principal.hasRealmOfType(Auditor.class) && report.getAuditor().getId() == principal.getActiveRealm().getId();

		super.setAuthorised(status);
	}

	@Override
	public void load() {
		int reportId;
		reportId = super.getRequest().getData("reportId", int.class);
		this.sections = this.repository.findSectionsByReportId(reportId);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.sections, "name", "hours", "kind");
	}
}
