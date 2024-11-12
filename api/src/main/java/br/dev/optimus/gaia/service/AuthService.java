package br.dev.optimus.gaia.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import br.dev.optimus.gaia.exception.InvalidCredentialsException;
import br.dev.optimus.gaia.model.User;

@Service
public class AuthService {
    private final UserService service;
    private final PasswordEncoder pwdEncoder;
    private final JwtEncoder jwtEncoder;

    public AuthService(UserService service, PasswordEncoder pwdEncoder, JwtEncoder jwtEncoder) {
        this.service = service;
        this.pwdEncoder = pwdEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    public record Response(String token, User user) {
    }

    public Response auth(User.Credentials credentials) {
        var user = service.findByUsername(credentials.username());
        if (!pwdEncoder.matches(credentials.password(), user.getPassword())) {
            throw new InvalidCredentialsException("invalid credentials");
        }
        return new Response(generateToken(user), user);
    }

    public User me() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return service.findByUsername(auth.getName());
    }

    private String generateToken(User user) {
        var now = Instant.now();
        var expiresIn = now.plus(24, ChronoUnit.HOURS);
        var scopes = user.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        var claims = JwtClaimsSet.builder()
                .issuer("gaia")
                .issuedAt(now)
                .expiresAt(expiresIn)
                .subject(user.getUsername())
                .claim("scopes", scopes)
                .build();
        var params = JwtEncoderParameters.from(claims);
        return jwtEncoder.encode(params).getTokenValue();
    }

}
