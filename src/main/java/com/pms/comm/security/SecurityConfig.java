package com.pms.comm.security;



import com.pms.comm.security.jwt.JwtAuthenticationFilter;
import com.pms.comm.security.jwt.JwtAuthorizationFilter;
import com.pms.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	private final AdminRepository adminRepository;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				.formLogin().disable()
				.httpBasic().disable()

				.addFilter(new JwtAuthenticationFilter(authenticationManager()))
				.addFilter(new JwtAuthorizationFilter(authenticationManager(),adminRepository))
				.authorizeRequests()
//				.antMatchers("/test").authenticated()
//				.antMatchers("/test2").authenticated()
//				.antMatchers("/wish/**").authenticated()
//				.antMatchers("/booking/**").authenticated()
//				.antMatchers("/coupon/**").authenticated()
//				.antMatchers("/review/write").authenticated()
				.anyRequest().permitAll();
	}
}






