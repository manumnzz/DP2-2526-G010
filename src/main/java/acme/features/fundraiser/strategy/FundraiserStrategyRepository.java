
package acme.features.fundraiser.strategy;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.entities.strategy.Strategy;
import acme.entities.tactic.Tactic;
import acme.realms.Fundraiser;

@Repository
public interface FundraiserStrategyRepository extends AbstractRepository {

	@Query("select f from Fundraiser f where f.userAccount.id = :userAccountId")
	Fundraiser findFundraiserByUserAccountId(int userAccountId);

	@Query("select ua from UserAccount ua where ua.id = :id")
	UserAccount findUserAccountById(int id);

	@Query("select s from Strategy s where s.fundraiser.id = :fundraiserId")
	Collection<Strategy> findManyStrategiesByFundraiserId(int fundraiserId);

	@Query("select s from Strategy s where s.id = :id")
	Strategy findOneStrategyById(int id);

	@Query("select count(t) from Tactic t where t.strategy.id = :strategyId")
	int countTacticsByStrategyId(int strategyId);

	@Query("select t from Tactic t where t.strategy.id = :strategyId")
	Collection<Tactic> findManyTacticsByStrategyId(int strategyId);

	@Query("select coalesce(sum(t.expectedPercentage), 0) from Tactic t where t.strategy.id = :strategyId")
	Double sumExpectedPercentageByStrategyId(int strategyId);
}
