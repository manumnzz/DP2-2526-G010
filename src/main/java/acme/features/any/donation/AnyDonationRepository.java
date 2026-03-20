
package acme.features.any.donation;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.sponsorships.Donation;
import acme.entities.sponsorships.Sponsorship;

@Repository
public interface AnyDonationRepository extends AbstractRepository {

	@Query("select d from Donation d where d.sponsorship.id = :sponsorshipId and d.sponsorship.draftMode = false")
	Collection<Donation> findManyDonationsByPublishedSponsorshipId(int sponsorshipId);

	@Query("select d from Donation d where d.id = :id and d.sponsorship.draftMode = false")
	Donation findOneDonationByIdIfPublishedSponsorship(int id);

	@Query("select s from Sponsorship s where s.id = :sponsorshipId and s.draftMode = false")
	Sponsorship findOnePublishedSponsorshipById(int sponsorshipId);
}
