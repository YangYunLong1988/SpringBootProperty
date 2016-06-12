package com.snowstore.terra.component;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

	@Override
	public String getCurrentAuditor() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
}
