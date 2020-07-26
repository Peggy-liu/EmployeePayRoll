package com.payroll.example;





import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;

@SpringBootApplication
public class EmployeePayrollApplication implements CommandLineRunner {
	@Autowired
	private ApplicationContext context;
	
	 private static Logger LOG = (Logger) LoggerFactory
		      .getLogger(EmployeePayrollApplication.class);

	public static void main(String[] args) {
		
		SpringApplication.run(EmployeePayrollApplication.class, args);
	
		
		
		
	}

	@Override
	public void run(String... args) throws Exception {
	
		
		LOG.warn(context.getBean(AuthenticationManager.class).toString());
	}

}
