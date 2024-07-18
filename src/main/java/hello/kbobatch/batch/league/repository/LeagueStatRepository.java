package hello.kbobatch.batch.league.repository;

import hello.kbobatch.domain.LeagueStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeagueStatRepository extends JpaRepository<LeagueStat, Integer> {

}
