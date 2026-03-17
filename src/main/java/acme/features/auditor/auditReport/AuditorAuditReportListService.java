
package acme.features.auditor.auditReport;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Principal;
import acme.client.services.AbstractService;
import acme.entities.audit.AuditReport;
import acme.realms.Auditor;

@Service
public class AuditorAuditReportListService extends AbstractService<Auditor, AuditReport> {

	@Autowired
	private AuditorAuditReportRepository	repository;

	private Collection<AuditReport>			auditReports;


	@Override
	public void authorise() {
		boolean status;
		Principal principal;

		principal = super.getRequest().getPrincipal();
		status = principal.hasRealmOfType(Auditor.class);

		super.setAuthorised(status);
	}

	@Override
	public void load() {
		int auditorId;
		auditorId = super.getRequest().getPrincipal().getActiveRealm().getId();
		this.auditReports = this.repository.findAuditReportsByAuditorId(auditorId);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.auditReports, "ticker", "name", "startMoment", "endMoment", "draftMode");
	}
}
