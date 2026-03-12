package acme.features.any.inventon;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.inventions.Invention;
import acme.realms.Inventor;

@Repository
public interface AnyInventionRepository extends AbstractRepository {
	
	@Query("select i from Invention i where i.draftMode = false and i.startMoment <= :currentMoment and i.endMoment >= :currentMoment order by i.ticker")
	Collection<Invention> findInventionsByAvailability(Date currentMoment);

	@Query("select i from Invention i where i.id = :id")
	Invention findInventionById(int id);
	
	@Query("select i from Inventor i")
	Collection<Inventor> findAllInventors();
	
	

}
