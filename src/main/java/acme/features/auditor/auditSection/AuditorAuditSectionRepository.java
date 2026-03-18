
package acme.features.auditor.auditSection;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.audit.AuditReport;
import acme.entities.audit.AuditSection;

@Repository
public interface AuditorAuditSectionRepository extends AbstractRepository {

	@Query("SELECT s FROM AuditSection s WHERE s.auditReport.id = :reportId")
	Collection<AuditSection> findSectionsByReportId(int reportId);

	@Query("SELECT s FROM AuditSection s WHERE s.id = :id")
	AuditSection findSectionById(int id);

	@Query("SELECT ar FROM AuditReport ar WHERE ar.id = :reportId")
	AuditReport findAuditReportById(int reportId);
}
