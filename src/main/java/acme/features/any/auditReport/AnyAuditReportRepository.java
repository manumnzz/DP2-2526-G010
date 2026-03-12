
package acme.features.any.auditReport;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.audit.AuditReport;
import acme.entities.audit.AuditSection;

@Repository
public interface AnyAuditReportRepository extends AbstractRepository {

	@Query("SELECT ar FROM AuditReport ar WHERE ar.draftMode = false")
	Collection<AuditReport> findPublishedAuditReports();

	@Query("SELECT ar FROM AuditReport ar WHERE ar.id = :id")
	AuditReport findAuditReportById(int id);

	@Query("SELECT s FROM AuditSection s WHERE s.auditReport.id = :reportId")
	Collection<AuditSection> findSectionsByReportId(int reportId);
}
