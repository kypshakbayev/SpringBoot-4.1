package kz.bitlab.techorda2025.thirdProject.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, length = 50, unique = true)
    private String email;

    @Column(name = "password", nullable = false, length = 10)
    private String password;

    @Column(name = "full_name", nullable = false, length = 50)
    private String fullName;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Task> taskList;
}
