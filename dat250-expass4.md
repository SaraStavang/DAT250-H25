Links:
[Backend source](./src/main/java)
[Application class](./src/main/java/com/example/DemoApplication.java)

When I started integrating JPA into my project, I ran into several problems.

At first, my tests were green because I was manually configuring an EntityManagerFactory with H2, but when I tried to run the Spring Boot application, Hibernate did not start. 
The logs showed no schema generation, and the H2 console could not find my mem:polls database.

The problem turned out to be my Gradle configuration. I had manually added jakarta.persistence and hibernate-core dependencies, which conflicted with the versions Spring Boot expected.
As a result, auto-configuration was broken.

I solved this by cleaning up my build.gradle.kts file and letting Spring Boot manage the versions through spring-boot-starter-data-jpa and com.h2database:h2. 
After this change, Hibernate started correctly and generated my tables.

I enabled the H2 web console in my application.properties with:
spring.h2.console.enabled=true
spring.h2.console.path=/h2
spring.datasource.url=jdbc:h2:mem:polls;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.jpa.hibernate.ddl-auto=create

Then I ran the backend with ./gradlew bootRun and navigated to http://localhost:8080/h2.
Logging in with JDBC URL jdbc:h2:mem:polls, user sa, and no password gave me access to the in-memory database.
<img width="1426" height="884" alt="Skjermbilde 2025-09-21 kl  09 10 51" src="https://github.com/user-attachments/assets/f91cc87a-40cf-4fe1-a43b-e8517e71077c" />
<img width="1115" height="712" alt="Skjermbilde 2025-09-21 kl  13 54 55" src="https://github.com/user-attachments/assets/35890865-7573-4a05-9858-fb5d16c7e93f" />
<img width="516" height="359" alt="Skjermbilde 2025-09-21 kl  13 59 31" src="https://github.com/user-attachments/assets/9a5b64d8-7970-4909-a8cc-7452d54c6c63" />
<img width="803" height="398" alt="Skjermbilde 2025-09-22 kl  19 35 07" src="https://github.com/user-attachments/assets/6fd96d71-cc16-497b-a909-7888fef42b4e" />

