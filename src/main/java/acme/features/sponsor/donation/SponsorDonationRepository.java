
package acme.features.sponsor.donation;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.sponsorships.Donation;
import acme.entities.sponsorships.Sponsorship;
import acme.realms.Sponsor;

@Repository
public interface SponsorDonationRepository extends AbstractRepository {

	@Query("select s from Sponsor s where s.userAccount.id = :userAccountId")
	Sponsor findSponsorByUserAccountId(int userAccountId);

	@Query("select s from Sponsorship s where s.id = :id")
	Sponsorship findOneSponsorshipById(int id);

	@Query("select d from Donation d where d.id = :id")
	Donation findOneDonationById(int id);

	@Query("select d from Donation d where d.sponsorship.id = :sponsorshipId")
	Collection<Donation> findManyDonationsBySponsorshipId(int sponsorshipId);
}
