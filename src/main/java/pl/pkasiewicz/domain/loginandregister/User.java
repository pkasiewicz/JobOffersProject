package pl.pkasiewicz.domain.loginandregister;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
class User {
    private String id;
    private String username;
    private String password;
}
