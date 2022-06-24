package psl.archiver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import psl.archiver.model.FileHistory;

public interface FileLogRepository extends JpaRepository<FileHistory, Long> {


}