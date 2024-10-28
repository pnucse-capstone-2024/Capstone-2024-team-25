package com.topik.topikkorea.file.domain.repository;

import com.topik.topikkorea.file.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
