package ru.hogwarts.school.tests;
import com.github.javafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.dto.StudentDtoIn;
import ru.hogwarts.school.dto.StudentDtoOut;
import ru.hogwarts.school.repository.StudentRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private StudentRepository studentRepository;

    private final Faker faker = new Faker();

    @AfterEach
    public void clean() {
        studentRepository.deleteAll();
    }

    @Test
    public StudentDtoOut createStudent() {
        StudentDtoIn studentDtoIn = generate();

        ResponseEntity<StudentDtoOut> responseEntity = testRestTemplate.postForEntity
                ("http://localhost:" + port + "/students",
                studentDtoIn, StudentDtoOut.class);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        StudentDtoOut studentDtoOut = responseEntity.getBody();

        Assertions.assertThat(studentDtoOut.getId()).isNotEqualTo(0L);
        Assertions.assertThat(studentDtoOut.getAge()).isEqualTo(studentDtoIn.getAge());
        Assertions.assertThat(studentDtoOut.getName()).isEqualTo(studentDtoIn.getName());
        return studentDtoOut;
    }

    public StudentDtoIn generate() {
        StudentDtoIn studentDtoIn = new StudentDtoIn();
        studentDtoIn.setAge(faker.random().nextInt(7,18));
        studentDtoIn.setName(faker.name().fullName());
        return studentDtoIn;
    }

    @Test
    public StudentDtoOut updateStudent() {
        StudentDtoOut created = createStudent();
        StudentDtoIn studentDtoIn = new StudentDtoIn();
        studentDtoIn.setName(faker.name().fullName());
        studentDtoIn.setAge(created.getAge());
        created.setName(faker.name().fullName());

        ResponseEntity<StudentDtoOut> responseEntity = testRestTemplate.exchange
                ("http://localhost:" + port + "/students/" + created.getId(),
                        HttpMethod.PUT, new HttpEntity<>(studentDtoIn), StudentDtoOut.class);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        StudentDtoOut studentDtoOut = responseEntity.getBody();

        Assertions.assertThat(studentDtoOut).isNotNull();
        Assertions.assertThat(studentDtoOut.getId()).isEqualTo(created.getId());
        Assertions.assertThat(studentDtoOut.getAge()).isEqualTo(studentDtoIn.getAge());
        Assertions.assertThat(studentDtoOut.getName()).isEqualTo(studentDtoIn.getName());


        long incorrectId = created.getId() + 1;
        ResponseEntity<String> stringResponseEntity = testRestTemplate.exchange(
                "http://localhost"+ port + "/students/"+ incorrectId, HttpMethod.PUT,
                        new HttpEntity<>(studentDtoIn), String.class);
        Assertions.assertThat(stringResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(stringResponseEntity.getBody())
                .isEqualTo("Студент с ID =" +incorrectId+ "не найден!");
        return studentDtoOut;

    }




}
