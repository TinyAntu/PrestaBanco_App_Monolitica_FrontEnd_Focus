package tinyantu.prestabancobackend.services;

import tinyantu.prestabancobackend.entities.DocumentEntity;
import tinyantu.prestabancobackend.repositories.DocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DocumentServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @InjectMocks
    private DocumentService documentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Tests for saveDocument
    @Test
    public void testSaveDocument_ValidDocument() {
        DocumentEntity document = new DocumentEntity();
        document.setId(1L);

        when(documentRepository.save(any(DocumentEntity.class))).thenReturn(document);

        DocumentEntity savedDocument = documentService.saveDocument(document);

        assertNotNull(savedDocument);
        assertEquals(1L, savedDocument.getId());
        verify(documentRepository, times(1)).save(document);
    }

    @Test
    public void testSaveDocument_NullDocument() {
        assertThrows(IllegalArgumentException.class, () -> documentService.saveDocument(null));
    }

    @Test
    public void testSaveDocument_SaveFailure() {
        DocumentEntity document = new DocumentEntity();
        when(documentRepository.save(any(DocumentEntity.class))).thenThrow(new RuntimeException("Save failed"));

        Exception exception = assertThrows(RuntimeException.class, () -> documentService.saveDocument(document));
        assertEquals("Save failed", exception.getMessage());
    }

    @Test
    public void testSaveDocument_WithSpecialCharacters() {
        DocumentEntity document = new DocumentEntity();
        document.setFilename("Doc$%#@!");

        when(documentRepository.save(any(DocumentEntity.class))).thenReturn(document);

        DocumentEntity savedDocument = documentService.saveDocument(document);
        assertNotNull(savedDocument);
        assertEquals("Doc$%#@!", savedDocument.getFilename());
    }

    @Test
    public void testSaveDocument_WithLongName() {
        DocumentEntity document = new DocumentEntity();
        document.setFilename("A very long document name that exceeds typical limits...");

        when(documentRepository.save(any(DocumentEntity.class))).thenReturn(document);

        DocumentEntity savedDocument = documentService.saveDocument(document);
        assertNotNull(savedDocument);
        assertEquals("A very long document name that exceeds typical limits...", savedDocument.getFilename());
    }

    @Test
    public void testSaveDocument_ConcurrentModification() {
        DocumentEntity document = new DocumentEntity();
        when(documentRepository.save(any(DocumentEntity.class))).thenThrow(new ConcurrentModificationException());

        assertThrows(ConcurrentModificationException.class, () -> documentService.saveDocument(document));
    }

    @Test
    public void testSaveDocument_InvalidDocumentType() {
        DocumentEntity document = new DocumentEntity();
        document.setDoc_type("INVALID_TYPE");

        when(documentRepository.save(any(DocumentEntity.class))).thenThrow(new IllegalArgumentException("Invalid document type"));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> documentService.saveDocument(document));

        assertEquals("Invalid document type", exception.getMessage());
        verify(documentRepository, times(1)).save(document);
    }

    // Tests for getDocuments
    @Test
    public void testGetDocuments_ValidId() {
        Long creditId = 1L;
        DocumentEntity doc1 = new DocumentEntity();
        DocumentEntity doc2 = new DocumentEntity();
        List<DocumentEntity> documents = Arrays.asList(doc1, doc2);

        when(documentRepository.findByIdCredit(creditId)).thenReturn(documents);

        List<DocumentEntity> result = documentService.getDocuments(creditId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(documentRepository, times(1)).findByIdCredit(creditId);
    }

    @Test
    public void testGetDocuments_NoDocuments() {
        Long creditId = 1L;

        when(documentRepository.findByIdCredit(creditId)).thenReturn(Collections.emptyList());

        List<DocumentEntity> result = documentService.getDocuments(creditId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(documentRepository, times(1)).findByIdCredit(creditId);
    }

    @Test
    public void testGetDocuments_NonExistentId() {
        Long creditId = 999L;

        when(documentRepository.findByIdCredit(creditId)).thenReturn(Collections.emptyList());

        List<DocumentEntity> result = documentService.getDocuments(creditId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(documentRepository, times(1)).findByIdCredit(creditId);
    }

    @Test
    public void testGetDocuments_DatabaseError() {
        Long creditId = 1L;
        when(documentRepository.findByIdCredit(creditId)).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> documentService.getDocuments(creditId));
    }

    @Test
    public void testGetDocuments_MultipleCalls() {
        Long creditId = 1L;
        DocumentEntity doc1 = new DocumentEntity();
        DocumentEntity doc2 = new DocumentEntity();
        List<DocumentEntity> documents = Arrays.asList(doc1, doc2);

        when(documentRepository.findByIdCredit(creditId)).thenReturn(documents);

        List<DocumentEntity> result1 = documentService.getDocuments(creditId);
        List<DocumentEntity> result2 = documentService.getDocuments(creditId);

        assertEquals(result1, result2);
        verify(documentRepository, times(2)).findByIdCredit(creditId);
    }

    @Test
    public void testGetDocuments_InvalidArgument() {
        assertThrows(IllegalArgumentException.class, () -> documentService.getDocuments(null));
    }

    @Test
    public void testGetDocuments_CallsRepositoryOnce() {
        Long creditId = 1L;
        DocumentEntity document = new DocumentEntity();
        List<DocumentEntity> documents = Collections.singletonList(document);

        when(documentRepository.findByIdCredit(creditId)).thenReturn(documents);
        List<DocumentEntity> result = documentService.getDocuments(creditId);

        verify(documentRepository, times(1)).findByIdCredit(creditId);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(document, result.get(0));
    }

    // Tests for getDocumentById
    @Test
    public void testGetDocumentById_ValidId() {
        Long documentId = 1L;
        DocumentEntity document = new DocumentEntity();
        document.setId(documentId);

        when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));

        DocumentEntity result = documentService.getDocumentById(documentId);

        assertNotNull(result);
        assertEquals(documentId, result.getId());
        verify(documentRepository, times(1)).findById(documentId);
    }

    @Test
    public void testGetDocumentById_NotFound() {
        Long documentId = 1L;

        when(documentRepository.findById(documentId)).thenReturn(Optional.empty());

        DocumentEntity result = documentService.getDocumentById(documentId);

        assertNull(result);
        verify(documentRepository, times(1)).findById(documentId);
    }

    @Test
    public void testGetDocumentById_DatabaseError() {
        Long documentId = 1L;

        when(documentRepository.findById(documentId)).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> documentService.getDocumentById(documentId));
    }

    @Test
    public void testGetDocumentById_NegativeId() {
        Long documentId = -1L;

        when(documentRepository.findById(documentId)).thenReturn(Optional.empty());

        DocumentEntity result = documentService.getDocumentById(documentId);

        assertNull(result);
        verify(documentRepository, times(1)).findById(documentId);
    }

    @Test
    public void testGetDocumentById_ConcurrentAccess() {
        Long documentId = 1L;

        when(documentRepository.findById(documentId)).thenThrow(new ConcurrentModificationException());

        assertThrows(ConcurrentModificationException.class, () -> documentService.getDocumentById(documentId));
    }

    @Test
    public void testGetDocumentById_CallsRepositoryOnce() {
        Long documentId = 1L;
        DocumentEntity document = new DocumentEntity();
        document.setId(documentId);

        when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));
        DocumentEntity result = documentService.getDocumentById(documentId);

        verify(documentRepository, times(1)).findById(documentId);
        assertNotNull(result);
    }

    @Test
    public void testGetDocumentById_NullId() {
        Long documentId = null;
        DocumentEntity result = documentService.getDocumentById(documentId);
        assertNull(result);
        verify(documentRepository, never()).findById(anyLong());
    }

}


