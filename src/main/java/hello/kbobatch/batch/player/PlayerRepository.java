package hello.kbobatch.batch.player;

import hello.kbobatch.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    @Query("SELECT p FROM Player p JOIN FETCH p.team JOIN FETCH p.stat WHERE p.name = :name AND p.birthDate = :birthDate")
    Optional<Player> findByNameAndBirthDateWithTeamAndStat(@Param("name") String name, @Param("birthDate") LocalDate birthDate);
}
