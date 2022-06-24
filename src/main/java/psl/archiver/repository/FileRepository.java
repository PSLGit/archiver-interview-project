package psl.archiver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import psl.archiver.model.File;

public interface FileRepository extends JpaRepository<File, Long> {

	File findByName(String name);
		
}