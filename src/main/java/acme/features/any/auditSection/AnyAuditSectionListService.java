
package acme.features.any.auditSection;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.audit.AuditSection;

@Service
public class AnyAuditSectionListService extends AbstractService<Any, AuditSection> {

	@Autowired
	private AnyAuditSectionRepository	repository;

	private Collection<AuditSection>	sections;


	@Override
	public void authorise() {
		super.setAuthorised(true);
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
