package org.zxcjaba.casino.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zxcjaba.casino.persistence.entity.GameOneResultsEntity;


@Repository
public interface GameOneResultsRepository extends JpaRepository<GameOneResultsEntity,Long> {


}
