/*
 * Copyright 2017 Pamarin.com
 */
package com.pamarin.commons.security;

import com.pamarin.commons.security.hashing.SHA384Hashing;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author jittagornp &lt;http://jittagornp.me&gt; create : 2017/12/04
 */
public class HashBasedToken_matchesTest {

    private static final String HASHBASED_KEY = "xyz";

    private HashBasedToken hashBasedToken;

    @Before
    public void withImplementation() {
        hashBasedToken = new DefaultHashBasedToken(HASHBASED_KEY, new SHA384Hashing());
    }

    @Test
    public void shouldBeFalse_whenCantDecodeBase64Token() {
        String input = "dGVzdA=";
        boolean output = hashBasedToken.matches(input, null);
        boolean expected = false;
        assertThat(output).isEqualTo(expected);
    }
//
//    @Test
//    public void shouldBeFalse_whenTokenArrayNotSize4() {
//        String input = "Yjk4ZTIxYjQtY2UyYS0xMWU3LWFiYzQtY2VjMjc4YjZiNTBhOjQxMDI0MTk2MDAwMDA=";
//        boolean output = hashBasedToken.matches(input, null);
//        boolean expected = false;
//        assertThat(output).isEqualTo(expected);
//    }
//
//    @Test
//    public void shouldBeFalse_whenCantParseExpirationTime() {
//        String input = "N2NmYzVjYTg3MDhjOmI5OGUyMWI0LWNlMmEtMTFlNy1hYmM0LWNlYzI3OGI2YjUwYTp4eXo6MDliNzhjZTBiZTI3Y2ZmZDc4NTA1OWM0NmI3N2EzNDJjOTM2ODU0NjdjZmM1Y2E4NzA4Yzc2NjM4MWNhZmEzYQ==";
//        boolean output = hashBasedToken.matches(input, null);
//        boolean expected = false;
//        assertThat(output).isEqualTo(expected);
//    }
//
//    @Test
//    public void shouldBeFalse_whenTokenExpired() {
//        String input = "N2NmYzVjYTg3MDhjOmI5OGUyMWI0LWNlMmEtMTFlNy1hYmM0LWNlYzI3OGI2YjUwYTowOjA5Yjc4Y2UwYmUyN2NmZmQ3ODUwNTljNDZiNzdhMzQyYzkzNjg1NDY3Y2ZjNWNhODcwOGM3NjYzODFjYWZhM2E=";
//        boolean output = hashBasedToken.matches(input, null);
//        boolean expected = false;
//        assertThat(output).isEqualTo(expected);
//    }
//
//    @Test
//    public void shouldBeFalse_whenNotFoundUser() {
//
//        String input = "WDVjdEh3b2VzTGF4aDVrYzA2aSt5UEtRVVBrN28yWTV3ZTY5ZUN2SHpyMD06Yjk4ZTIxYjQtY2UyYS0xMWU3LWFiYzQtY2VjMjc4YjZiNTBhOjQ2NjgxNTgzODM2OTE6ZjVlZjIwNDIwOGE0NjkxZWNlN2RhMWY5N2ZmMzUwYTA4YzMwZDBiOThjNmFjNDQyMzkwN2VkMDY5NmI4MTJjOQ==";
//        boolean output = hashBasedToken.matches(input, username -> {
//            throw new UsernameNotFoundException("Not found user.");
//        });
//        boolean expected = false;
//        assertThat(output).isEqualTo(expected);
//    }
//
//    @Test
//    public void shouldBeFalse_whenInvalidToken(){
//        String input = "WDVjdEh3b2VzTGF4aDVrYzA2aSt5UEtRVVBrN28yWTV3ZTY5ZUN2SHpyMD06Yjk4ZTIxYjQtY2UyYS0xMWU3LWFiYzQtY2VjMjc4YjZiNTBhOjQ2NjgxNTgzODM2OTE6ZjVlZjIwNDIwOGE0NjkxZWNlN2RhMWY5N2ZmMzUwYTA4YzMwZA==";
//        boolean output = hashBasedToken.matches(input, username -> DefaultUserDetails.builder()
//                .username("b98e21b4-ce2a-11e7-abc4-cec278b6b50a")
//                .password("$2a$10$2SLqTL7.Ug9cyfRPFwxQeemMA4SeB9MymLjRl4RGR0h7aHwLDy7qC")
//                .build()
//        );
//        boolean expected = false;
//        assertThat(output).isEqualTo(expected);
//    }
//    
//    
//    @Test
//    public void shouldBeTrue_whenValidToken() {
//
//        String input = "WDVjdEh3b2VzTGF4aDVrYzA2aSt5UEtRVVBrN28yWTV3ZTY5ZUN2SHpyMD06Yjk4ZTIxYjQtY2UyYS0xMWU3LWFiYzQtY2VjMjc4YjZiNTBhOjQ2NjgxNTgzODM2OTE6ZjVlZjIwNDIwOGE0NjkxZWNlN2RhMWY5N2ZmMzUwYTA4YzMwZDBiOThjNmFjNDQyMzkwN2VkMDY5NmI4MTJjOQ==";
//        boolean output = hashBasedToken.matches(input, username -> DefaultUserDetails.builder()
//                .username("b98e21b4-ce2a-11e7-abc4-cec278b6b50a")
//                .password("$2a$10$2SLqTL7.Ug9cyfRPFwxQeemMA4SeB9MymLjRl4RGR0h7aHwLDy7qC")
//                .build()
//        );
//        boolean expected = true;
//        assertThat(output).isEqualTo(expected);
//    }
}
