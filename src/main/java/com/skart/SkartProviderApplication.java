package com.skart;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import com.skart.repository.UserRepository;
import com.skart.service.CustomUserDetails;
import com.skart.service.CustomUserDetailsService;

@SpringBootApplication
public class SkartProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkartProviderApplication.class, args);
	}

	@Configuration
	@EnableResourceServer
	protected static class ResourceServer extends ResourceServerConfigurerAdapter {

		@Override
		public void configure(HttpSecurity http) throws Exception {
			// @formatter:off

			http.authorizeRequests().antMatchers("/skart-app/article/**").hasRole("ADMIN")
					.antMatchers("/skart-app/admin/**").permitAll().and().formLogin().loginPage("/login").permitAll();

			/* http */
			// Just for laughs, apply OAuth protection to only 2
			// resources
			/*
			 * .requestMatcher(new OrRequestMatcher(new
			 * AntPathRequestMatcher("/"), new
			 * AntPathRequestMatcher("/article/**")))
			 * .authorizeRequests().anyRequest().access(
			 * "#oauth2.hasScope('read')");
			 */

			/*
			 * http.authorizeRequests().antMatchers("/article/**").permitAll()
			 * .antMatchers("/registration").permitAll().antMatchers("/admin/**"
			 * ).hasAuthority("ADMIN")
			 * .anyRequest().authenticated().and().csrf().disable().formLogin().
			 * loginPage("/login")
			 * .failureUrl("/login?error=true").defaultSuccessUrl("/admin/home")
			 * .usernameParameter("email")
			 * .passwordParameter("password").and().logout()
			 * .logoutRequestMatcher(new
			 * AntPathRequestMatcher("/logout")).logoutSuccessUrl("/").and()
			 * .exceptionHandling().accessDeniedPage("/access-denied");
			 */

			// @formatter:on
		}

		@Override
		public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
			resources.resourceId("skart-app");
		}

	}

	@Configuration
	@EnableAuthorizationServer
	protected static class OAuth2Config extends AuthorizationServerConfigurerAdapter {

		@Autowired
		private AuthenticationManager authenticationManager;

		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			endpoints.authenticationManager(authenticationManager);
		}

		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			// @formatter:off
			/*
			 * clients.inMemory().withClient("my-trusted-client")
			 * .authorizedGrantTypes("password", "authorization_code",
			 * "refresh_token", "implicit") .authorities("ROLE_CLIENT",
			 * "ROLE_TRUSTED_CLIENT") .scopes("read", "write",
			 * "trust").resourceIds("sparklr") .refreshTokenValiditySeconds(600)
			 * .accessTokenValiditySeconds(60).and()
			 * .withClient("my-client-with-registered-redirect")
			 * .authorizedGrantTypes("authorization_code").authorities(
			 * "ROLE_CLIENT") .scopes("read", "trust").resourceIds("sparklr")
			 * .redirectUris("http://anywhere?key=value").and()
			 * .withClient("my-client-with-secret")
			 * .authorizedGrantTypes("client_credentials", "password")
			 * .authorities("ROLE_CLIENT").scopes("read").resourceIds("sparklr")
			 * .secret("secret");
			 */
			clients.inMemory().withClient("my-trusted-client")
					.authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit",
							"client_credentials")
					.authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT").scopes("read", "write", "trust")
					.secret("password").accessTokenValiditySeconds(120).// Access
																		// token
																		// is
																		// only
																		// valid
																		// for 2
																		// minutes.
					refreshTokenValiditySeconds(600);// Refresh token is only
														// valid for 10 minutes.
			// @formatter:on
		}

	}

	@Configuration
	@EnableWebSecurity
	@EnableGlobalMethodSecurity(prePostEnabled = true)
	public static class SecurityConfiguration extends WebSecurityConfigurerAdapter {

		@Autowired
		private BCryptPasswordEncoder bCryptPasswordEncoder;

		@Autowired
		private DataSource dataSource;

		@Autowired
		UserRepository userRepo;

		@Autowired
		CustomUserDetailsService userDetails;

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {

			// auth.userDetailsService(userDetails).passwordEncoder(bCryptPasswordEncoder);
			auth.userDetailsService(userDetails);
		}

		/*
		 * @Override protected void configure(HttpSecurity http) throws
		 * Exception {
		 * 
		 * http.authorizeRequests().antMatchers("/").permitAll().antMatchers(
		 * "/artile").permitAll()
		 * .antMatchers("/registration").permitAll().antMatchers("/admin/**").
		 * hasAuthority("ADMIN")
		 * .anyRequest().authenticated().and().csrf().disable().formLogin().
		 * loginPage("/login")
		 * .failureUrl("/login?error=true").defaultSuccessUrl("/admin/home").
		 * usernameParameter("email")
		 * .passwordParameter("password").and().logout()
		 * .logoutRequestMatcher(new
		 * AntPathRequestMatcher("/logout")).logoutSuccessUrl("/").and()
		 * .exceptionHandling().accessDeniedPage("/access-denied"); }
		 * 
		 * @Override public void configure(WebSecurity web) throws Exception {
		 * web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**",
		 * "/js/**", "/images/**"); }
		 */

	}

}
