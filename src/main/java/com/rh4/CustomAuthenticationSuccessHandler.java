package com.rh4;
  
import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.rh4.entities.MyUser;
import com.rh4.models.MyUserDetails;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler
{
	private boolean isAdmin(Authentication authentication) {
	    // Check if the user has the role "ADMIN"
	    return authentication.getAuthorities().stream()
	            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
	}
	private boolean isSuperAdmin(Authentication authentication) {
	    // Check if the user has the role "ADMIN"
	    return authentication.getAuthorities().stream()
	            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_SUPERADMIN"));
	}

	private boolean isGuide(Authentication authentication) {
	    // Check if the user has the role "GUIDE"
	    return authentication.getAuthorities().stream()
	            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_GUIDE"));
	}
	private boolean isUnderProcessIntern(Authentication authentication) {
	    // Check if the user has the role "GUIDE"
	    return authentication.getAuthorities().stream()
	            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_UNDERPROCESSINTERN"));
	}

	private boolean isIntern(Authentication authentication) {
	    // Check if the user has the role "INTERN"
	    return authentication.getAuthorities().stream()
	            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_INTERN"));
	}
	private MyUserDetails getMyUser(Authentication authentication) {
	    if (authentication.getPrincipal() instanceof MyUserDetails) {
	        return (MyUserDetails) authentication.getPrincipal();
	    }
	    return null; // Handle the case where the principal is not of type CustomUserDetails
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		System.out.println("Granted Authorities are: " + authentication.getAuthorities());
		
		// Get the username and role
		String username = authentication.getName();
		String role = authentication.getAuthorities().stream().findFirst().orElse(null).getAuthority();
		System.out.println(role);
		// Create a session and add attributes
		HttpSession session = request.getSession(true); // true creates a new session if it doesn't exist
		session.setAttribute("username", username);
		session.setAttribute("role", role);
		
		// Redirect URL based on user's role
        String targetUrl = determineTargetUrl(authentication);
        response.sendRedirect(targetUrl);
        
        // Append the user ID to the URL if the user is an admin
//        if (isAdmin(authentication)) {
//        	MyUserDetails user = getMyUser(authentication);
//            if (user != null) {
//                String adminId = user.getUser().getUserId();
//                if (adminId != null) {
//                    targetUrl += "/" + adminId;
//                } else {
//                }
//            } else {
//            }
//        }	
       
	}
	
	 private String determineTargetUrl(Authentication authentication) {
	        
		 if (isAdmin(authentication)) {
		        return "/bisag/admin/admin_dashboard";
		    } else if (isGuide(authentication)) {
		        return "/bisag/guide/guide_dashboard";
		    } else if (isIntern(authentication)) {
		        return "/bisag/intern/intern_dashboard";
		    }else if (isSuperAdmin(authentication)) {
		        return "/bisag/super_admin/super_admin_dashboard";
		    }else if (isUnderProcessIntern(authentication)) {
		        return "/under_process_intern";
		    } else {
		        return "/under_process_application";
		    }
	    }

}
