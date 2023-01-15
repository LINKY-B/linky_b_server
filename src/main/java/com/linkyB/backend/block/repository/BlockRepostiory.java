package com.linkyB.backend.block.repository;

import com.linkyB.backend.block.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepostiory extends JpaRepository<Block, Long> {
}
