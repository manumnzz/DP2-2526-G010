
package acme.features.any.campaign;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.campaign.Campaign;

@Repository
public interface AnyCampaignRepository extends AbstractRepository {

	@Query("select c from Campaign c join fetch c.spokesperson where c.draftMode = false")
	Collection<Campaign> findPublishedCampaigns();

	@Query("select c from Campaign c where c.id = :id")
	Campaign findCampaignById(int id);

	@Query("select coalesce(sum(m.effort), 0) from Milestone m where m.campaign.id = :campaignId")
	Double computeCampaignEffort(int campaignId);

}
