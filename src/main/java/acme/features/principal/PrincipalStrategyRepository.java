
package acme.features.principal;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.strategy.Strategy;
import acme.entities.tactic.Tactic;
import acme.realms.Fundraiser;

@Repository
public interface PrincipalStrategyRepository extends AbstractRepository {

	@Query("select s from Strategy s where s.draftMode = false")
	Collection<Strategy> findManyPublishedStrategies();

	@Query("select s from Strategy s where s.id = :id")
	Strategy findOneStrategyById(int id);

	@Query("select t from Tactic t where t.strategy.id = :strategyId and t.draftMode = false")
	Collection<Tactic> findManyPublishedTacticsByStrategyId(int strategyId);

	@Query("select f from Fundraiser f where f.id = :id")
	Fundraiser findFundraiserById(int id);

	@Query("select coalesce(sum(t.expectedPercentage), 0) from Tactic t where t.strategy.id = :strategyId and t.draftMode = false")
	Double sumExpectedPercentageByStrategyId(int strategyId);
}
