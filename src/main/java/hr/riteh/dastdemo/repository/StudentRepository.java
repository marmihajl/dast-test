package hr.riteh.dastdemo.repository;

import hr.riteh.dastdemo.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
