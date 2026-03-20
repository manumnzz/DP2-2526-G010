
package acme.features.auditor.auditReport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Principal;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.audit.AuditReport;
import acme.realms.Auditor;

@Service
public class AuditorAuditReportCreateService extends AbstractService<Auditor, AuditReport> {

	@Autowired
	private AuditorAuditReportRepository	repository;

	private AuditReport						auditReport;


	@Override
	public void authorise() {
		Principal principal;
		principal = super.getRequest().getPrincipal();
		super.setAuthorised(principal.hasRealmOfType(Auditor.class));
	}

	@Override
	public void load() {
		this.auditReport = new AuditReport();

		this.auditReport.setDraftMode(true);
		this.auditReport.setStartMoment(MomentHelper.getCurrentMoment());
		this.auditReport.setEndMoment(MomentHelper.getCurrentMoment());
	}

	@Override
	public void bind() {
		super.bindObject(this.auditReport, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		//		boolean valid;
		//
		//		valid = this.auditReport.getEndMoment().after(this.auditReport.getStartMoment());
		//		super.state(valid, "endMoment", "auditor.auditReport.error.end-before-start");
		//
		//		super.validateObject(this.auditReport);
	}

	@Override
	public void execute() {
		Principal principal;
		Auditor auditor;

		principal = super.getRequest().getPrincipal();
		auditor = this.repository.findAuditorById(principal.getActiveRealm().getId());
		this.auditReport.setAuditor(auditor);

		this.repository.save(this.auditReport);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.auditReport, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
	}
}
