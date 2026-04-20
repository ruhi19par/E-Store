package com.example.lcwd.Electronic;

import com.example.lcwd.Electronic.entities.Role;
import com.example.lcwd.Electronic.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication

public class ElectronicStoreApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;
	@Value("${normal.role.id}")
	private String role_normal_id;
@Value("${admin.role.id}")
	private String role_admin_id;

	@Override
	public void run(String... args) throws Exception {
		System.out.println(passwordEncoder.encode("123456"));

		try{
	Role role_admin=Role.builder().roleId(role_admin_id).rollName("ROLE_ADMIN").build();
			Role role_normal=Role.builder().roleId(role_normal_id).rollName("ROLE_NORMAL").build();
			roleRepository.save(role_normal);
			roleRepository.save(role_admin);

		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
