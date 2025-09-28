package br.com.fiap.ultimateteam.user;

import br.com.fiap.ultimateteam.team.Team;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "app_user") // "user" is a reserved keyword in some databases
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String avatarUrl;

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER)
    private Team team;

}
