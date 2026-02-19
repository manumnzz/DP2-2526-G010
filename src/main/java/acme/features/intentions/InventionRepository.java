
package acme.features.intentions;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface InventionRepository extends AbstractRepository {

	@Query("select sum(p.cost.amount) from Part p where p.invention.id = :inventionId")
	Double getSummaryCostAmountByInventionId(int inventionId);

	@Query("select p.cost.currency from Part p where p.invention.id = :inventionId")
	String getAnyCurrencyByInventionId(int inventionId);

}
