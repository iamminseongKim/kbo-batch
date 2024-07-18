package hello.kbobatch.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlayerStat {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stat_id")
    private Long id;

    private Integer pa;
    private Integer h;
    @Column(name = "2b")
    private Integer h_2b;
    @Column(name = "3b")
    private Integer h_3b;
    private Integer hr;
    private Integer sf;
    private Integer bb;
    private Integer ibb;
    private Integer hbp;
    private Double slg;
    private Double obp;

    private Double ops;
    private Integer wrcPlus;


}
