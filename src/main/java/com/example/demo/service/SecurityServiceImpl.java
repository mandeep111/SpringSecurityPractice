package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SecurityServiceImpl implements SecurityService{

	@Autowired
	private AuthenticationManager authmanager;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	private static Logger logger = LoggerFactory.getLogger(SecurityServiceImpl.class);
	@Override
	public String findLoggedInUsername() {
		Object userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
		if (userDetails instanceof UserDetails) {
			return ((UserDetails)userDetails).getUsername();
		}
		return null;
	}

	@Override
	public void autoLogin(String username, String password) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		UsernamePasswordAuthenticationToken passwordToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
		
		if (passwordToken.isAuthenticated()) {
			SecurityContextHolder.getContext().setAuthentication(passwordToken);
			logger.debug(String.format("Auto login %s successfully!", username));
		}
	}

}
