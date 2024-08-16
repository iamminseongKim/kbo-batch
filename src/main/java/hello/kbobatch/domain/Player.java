package hello.kbobatch.domain;

import hello.kbobatch.domain.type.HandsType;
import hello.kbobatch.domain.type.Position;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Player {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Position position;
    private String name;
    private String backNum;
    @Enumerated(EnumType.STRING)
    private HandsType type;
    private int age;
    private LocalDate birthDate;
    private int height;
    private int weight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "stat_id")
    private PlayerStat stat;

    //연관관계 편의 메서드 선수의 개인 팀 변경시 (팀 객체도 바꿔 줘야하기 때문에)
    public void changeTeam(Team team) {
        if (this.team != null) {
            this.team.getPlayers().remove(this);
        }
        this.team = team;
        team.getPlayers().add(this);
    }

    @Builder
    public Player(Position position, String name, String backNum, HandsType type, int age, LocalDate birthDate, int height, int weight, Team team, PlayerStat stat) {
        this.position = position;
        this.name = name;
        this.backNum = backNum;
        this.type = type;
        this.age = age;
        this.birthDate = birthDate;
        this.height = height;
        this.weight = weight;
        this.team = team;
        this.stat = stat;
    }

    public Player updateStat(PlayerStat stat) {
        this.stat = stat;
        return this;
    }
}
