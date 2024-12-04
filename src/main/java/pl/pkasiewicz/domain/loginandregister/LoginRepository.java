package pl.pkasiewicz.domain.loginandregister;

import java.util.Optional;

interface LoginRepository {

    Optional<User> findByUsername(String username);
    User save(User user);
}
