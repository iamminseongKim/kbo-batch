package hello.kbobatch.domain;

import hello.kbobatch.domain.type.TeamName;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "team_code", nullable = false)
    private TeamName teamCode;

    @Column(name = "team_name", nullable = false)
    private String name;

    private Integer gameCount;
    private Double parkFactor;

    @OneToMany(mappedBy = "team")
    private List<Player> players = new ArrayList<>();


}
