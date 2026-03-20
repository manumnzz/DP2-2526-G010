
package acme.features.any.auditSection;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.audit.AuditSection;

@Repository
public interface AnyAuditSectionRepository extends AbstractRepository {

	@Query("SELECT s FROM AuditSection s WHERE s.auditReport.id = :reportId")
	Collection<AuditSection> findSectionsByReportId(int reportId);

}
