package acme.features.any.part;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.inventions.Invention;
import acme.entities.inventions.Part;

@Repository
public interface AnyPartRepository extends AbstractRepository {
	
	@Query("Select i from Invention i where i.id = :inventionId")
	Invention findInventionById(int inventionId);
	
	@Query("Select p from Part p where p.invention.id = :inventionId")
	Collection<Part> findPartsByInventionId(int inventionId);

	@Query("Select p from Part p where p.id = :id")
	Part findPartById(int id);
	
}
