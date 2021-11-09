package com.utcl.oneultratech.file.service.repository;

import com.utcl.oneultratech.file.service.model.FileDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDBRepository extends JpaRepository<FileDB,String> {
}
