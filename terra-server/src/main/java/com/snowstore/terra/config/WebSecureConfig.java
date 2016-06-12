package com.snowstore.terra.config;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.configurers.ldap.LdapAuthenticationProviderConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class WebSecureConfig extends WebSecurityConfigurerAdapter {
	
	@Bean
	public Md5PasswordEncoder passwordEncoder() {
		return new Md5PasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests().antMatchers("/css/**", "/js/**", "/images/**", "/fonts/**", "/getconfig*").permitAll()
				.anyRequest().fullyAuthenticated().and().formLogin().loginPage("/login").defaultSuccessUrl("/", true)
				.failureUrl("/login?error").permitAll();
		http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
	}

	@Configuration
	protected static class AuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter {
		@Value("${ldap.url}")
		private String ldapUrl;
		
		@Value("${ldap.manager.dn}")
		private String ldapManagerDn;
		
		@Value("${ldap.manager.password}")
		private String ldapManagerPassword;

		@Override
		public void init(AuthenticationManagerBuilder auth) throws Exception {
			
			LdapAuthenticationProviderConfigurer<AuthenticationManagerBuilder> ldapAuthentication = auth
					.ldapAuthentication();
			ldapAuthentication.passwordCompare().passwordAttribute("userPassword")
					.passwordEncoder(new PasswordEncoder() {

						@Override
						public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
							return true;
						}

						@Override
						public String encodePassword(String rawPass, Object salt) {
							MessageDigest digest;
							try {
								digest = MessageDigest.getInstance("MD5");
								digest.update(rawPass.getBytes("UTF8"));
								String md5Password = new String(Base64.encode(digest.digest()));
								return "{MD5}" + md5Password;
							} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
								e.printStackTrace();
							}
							return "";
						}
					});
			ldapAuthentication.userSearchFilter("(cn={0})").userSearchBase("ou=IT,dc=JL,dc=com")
					.groupSearchBase("ou=IT,dc=JL,dc=com").groupSearchFilter("(memberUid={0})")
					.groupRoleAttribute("cn").contextSource().url(ldapUrl)
					.managerDn(ldapManagerDn).managerPassword(ldapManagerPassword);
		}
	}
}
