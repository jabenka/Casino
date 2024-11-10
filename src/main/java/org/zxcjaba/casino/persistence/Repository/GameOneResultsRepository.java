package org.zxcjaba.casino.persistence.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zxcjaba.casino.persistence.Entity.GameOneResultsEntity;


@Repository
public interface GameOneResultsRepository extends JpaRepository<GameOneResultsEntity,Long> {


}
