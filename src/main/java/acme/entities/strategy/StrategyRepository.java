
package acme.entities.strategy;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface StrategyRepository extends AbstractRepository {

	@Query("select s from Strategy s where s.ticker = :ticker")
	Strategy findStrategyByTicker(String ticker);

	@Query("select coalesce(sum(t.expectedPercentage), 0) from Tactic t where t.strategy.id = :strategyId")
	Double computeExpectedPercentage(int strategyId);
}
