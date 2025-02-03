package pl.pkasiewicz.domain.loginandregister;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface LoginRepository extends MongoRepository<User, String> {

    Optional<User> findByUsername(String username);
}
