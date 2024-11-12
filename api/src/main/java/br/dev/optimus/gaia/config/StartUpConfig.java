package br.dev.optimus.gaia.config;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.dev.optimus.gaia.model.Group;
import br.dev.optimus.gaia.model.User;
import br.dev.optimus.gaia.service.GroupService;
import br.dev.optimus.gaia.service.UserService;

@Configuration
public class StartUpConfig {
    private final static Logger log = LoggerFactory.getLogger(StartUpConfig.class);
    private final GroupService groupService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public StartUpConfig(GroupService groupService, UserService userService, PasswordEncoder passwordEncoder) {
        this.groupService = groupService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    CommandLineRunner init() {
        if (groupService.count() == 0) {
            log.info("Creating default groups");

            var root = groupService.create(Group.builder()
                    .name("root")
                    .permissions(Set.of("root"))
                    .visible(false)
                    .editable(false)
                    .locked(true)
                    .build());

            groupService.create(Group.builder()
                    .name("nobody")
                    .permissions(Set.of("nobody"))
                    .visible(false)
                    .editable(false)
                    .locked(true)
                    .build());

            var admin = groupService.create(Group.builder()
                    .name("admin")
                    .permissions(Set.of("admin"))
                    .visible(true)
                    .editable(false)
                    .locked(true)
                    .build());

            groupService.create(Group.builder()
                    .name("user")
                    .permissions(Set.of("group:list", "user:list", "customer:list", "app:list"))
                    .visible(true)
                    .editable(true)
                    .locked(false)
                    .build());
            log.info("Creating default users");
            userService.create(User.builder()
                    .group(root)
                    .name("Root")
                    .username("root")
                    .password(passwordEncoder.encode("root"))
                    .email("root@change.me")
                    .editable(false)
                    .visible(false)
                    .locked(true)
                    .build());

            userService.create(User.builder()
                    .group(admin)
                    .name("Administrador")
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .email("admin@change.me")
                    .editable(false)
                    .visible(true)
                    .locked(true)
                    .build());
        }

        return args -> {
        };
    }
}
