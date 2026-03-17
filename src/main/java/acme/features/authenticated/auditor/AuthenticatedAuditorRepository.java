
package acme.features.authenticated.auditor;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.realms.Auditor;

@Repository
public interface AuthenticatedAuditorRepository extends AbstractRepository {

	@Query("SELECT a FROM Auditor a WHERE a.userAccount.id = :userAccountId")
	Auditor findAuditorByUserAccountId(int userAccountId);

	@Query("SELECT ua FROM UserAccount ua WHERE ua.id = :id")
	UserAccount findUserAccountById(int id);
}
