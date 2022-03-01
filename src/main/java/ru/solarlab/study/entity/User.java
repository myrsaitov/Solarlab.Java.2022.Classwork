package ru.solarlab.study.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

import static javax.persistence.FetchType.EAGER;

/** Модель пользователя */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    /** Логин */
    @Id
    private String username;

    /** Пароль */
    private String password;

    /** Электронная почта */
    private String email;

    /** Пользователь активный или нет */
    private boolean enabled = true;
}