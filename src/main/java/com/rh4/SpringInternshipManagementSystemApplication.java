package com.rh4;

import com.rh4.entities.MyUser;
import com.rh4.entities.SuperAdmin;
import com.rh4.repositories.SuperAdminRepo;
import com.rh4.repositories.UserRepo;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.rh4")
@OpenAPIDefinition(
		info = @Info(
				title = "Healthcare Backend",
				version = "1.0",
				description = "This is the backend API documentation for the Healthcare app."
		),
		security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
		name = "bearerAuth",
		type = SecuritySchemeType.HTTP,
		scheme = "bearer",
		bearerFormat = "JWT"
)
public class SpringInternshipManagementSystemApplication {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	public SuperAdminRepo superAdminRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SpringInternshipManagementSystemApplication.class, args);
	}

	@Bean
	public CommandLineRunner initSuperAdmin() {
		return args -> {
			boolean adminExists = userRepo.existsByRole("SUPERADMIN");

			if (!adminExists) {

				SuperAdmin s1 = new SuperAdmin();
				s1.setName("superadmin");
				s1.setEmailId("ims_super_admin@gmail.com");
				s1.setContactNo((long) 23123466);
				s1.setLocation("anonymous");
				s1.setPassword(passwordEncoder.encode("IMS@123"));
				s1.setCreatedAt(null);
				superAdminRepo.save(s1);

				MyUser user = new MyUser();
				user.setEmail("ims_super_admin@gmail.com");
				user.setPassword(passwordEncoder.encode("IMS@123"));
				user.setEnabled(true);
				user.setRole("SUPERADMIN");
				user.setUserId(s1.getSuperAdminId());

				userRepo.save(user);

				System.out.println("✅ Default super admin created: admin123@example.com / passcode");
			} else {
				System.out.println("ℹ️ Super admin already exists.");
			}
		};
	}

}
