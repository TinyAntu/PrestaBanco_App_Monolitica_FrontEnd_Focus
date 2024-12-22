package tinyantu.prestabancobackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "docs")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class DocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Lob
    private byte[] file; // Pdf file

    private String doc_type;

    private String filename;

    //From where they are
    private Long idCredit;
}
