package tinyantu.prestabancobackend.repositories;

import tinyantu.prestabancobackend.entities.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentEntity,Long> {

    List<DocumentEntity> findByIdCredit(Long id);


}
