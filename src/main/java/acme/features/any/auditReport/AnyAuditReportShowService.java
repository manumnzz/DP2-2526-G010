
package acme.features.any.auditReport;

import java.util.Collection;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.audit.AuditReport;
import acme.entities.audit.AuditSection;

@Service
public class AnyAuditReportShowService extends AbstractService<Any, AuditReport> {

	@Autowired
	private AnyAuditReportRepository	repository;

	private AuditReport					auditReport;
	private Collection<AuditSection>	sections;


	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.auditReport = this.repository.findAuditReportById(id);
		this.sections = this.repository.findSectionsByReportId(id);
		Hibernate.initialize(this.auditReport.getSections());
	}

	@Override
	public void authorise() {
		boolean status;
		status = this.auditReport != null && !this.auditReport.isDraftMode();
		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		Tuple tuple;

		tuple = super.unbindObject(this.auditReport, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode", "monthsActive");

		tuple.put("sections", this.sections);
		tuple.put("auditorName", this.auditReport.getAuditor().getUserAccount().getIdentity().getFullName());
		tuple.put("auditorId", this.auditReport.getAuditor().getId());
	}
}
