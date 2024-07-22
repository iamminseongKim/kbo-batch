package hello.kbobatch.batch.player;

import hello.kbobatch.domain.Team;
import hello.kbobatch.domain.type.TeamName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
    Team findByTeamCode(TeamName teamCode);
}
