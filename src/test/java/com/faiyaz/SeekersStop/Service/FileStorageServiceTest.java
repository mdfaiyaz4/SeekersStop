package com.faiyaz.SeekersStop.Service;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import com.faiyaz.SeekersStop.UserDefinedExceptions.ResourceNotFoundException;

import java.nio.charset.StandardCharsets;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileStorageServiceTest {

    private final FileStorageService fileStorageService = new FileStorageService();

    @Test
    void applicationSnapshotRemainsIndependentWhenCurrentCvIsReplaced() throws Exception {
        byte[] cvA = pdfBytes("CV-A");
        byte[] cvB = pdfBytes("CV-B");
        String currentCvPath = fileStorageService.storeCv(pdf(cvA, "cv-a.pdf"));
        String snapshotPath = null;
        String replacementCvPath = null;

        try {
            snapshotPath = fileStorageService.copyCvForApplication(currentCvPath);
            replacementCvPath = fileStorageService.storeCv(pdf(cvB, "cv-b.pdf"));
            fileStorageService.deleteCv(currentCvPath);

            Resource submittedCv = fileStorageService.loadApplicationCv(snapshotPath);
            Resource currentCv = fileStorageService.loadCv(replacementCvPath);

            assertNotEquals(currentCvPath, snapshotPath);
            assertArrayEquals(cvA, submittedCv.getContentAsByteArray());
            assertArrayEquals(cvB, currentCv.getContentAsByteArray());
        } finally {
            fileStorageService.deleteCv(currentCvPath);
            fileStorageService.deleteCv(replacementCvPath);
            fileStorageService.deleteApplicationCv(snapshotPath);
        }
    }

    @Test
    void missingLegacySnapshotAndPathsOutsideApplicationStorageAreUnavailable() {
        assertThrows(ResourceNotFoundException.class, () -> fileStorageService.loadApplicationCv(null));
        assertThrows(
                ResourceNotFoundException.class,
                () -> fileStorageService.loadApplicationCv("uploads/cv/legacy.pdf")
        );
    }

    private MockMultipartFile pdf(byte[] content, String filename) {
        return new MockMultipartFile("cv", filename, "application/pdf", content);
    }

    private byte[] pdfBytes(String label) throws Exception {
        String stream = "BT /F1 16 Tf 30 70 Td (" + label + ") Tj ET";
        List<String> objects = List.of(
                "1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n",
                "2 0 obj\n<< /Type /Pages /Kids [3 0 R] /Count 1 >>\nendobj\n",
                "3 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [0 0 300 144] " +
                        "/Resources << /Font << /F1 4 0 R >> >> /Contents 5 0 R >>\nendobj\n",
                "4 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>\nendobj\n",
                "5 0 obj\n<< /Length " + stream.getBytes(StandardCharsets.US_ASCII).length +
                        " >>\nstream\n" + stream + "\nendstream\nendobj\n"
        );

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write("%PDF-1.4\n".getBytes(StandardCharsets.US_ASCII));
        List<Integer> offsets = new ArrayList<>();
        for (String object : objects) {
            offsets.add(output.size());
            output.write(object.getBytes(StandardCharsets.US_ASCII));
        }

        int xrefOffset = output.size();
        StringBuilder xref = new StringBuilder("xref\n0 6\n0000000000 65535 f \n");
        for (Integer offset : offsets) {
            xref.append(String.format("%010d 00000 n \n", offset));
        }
        xref.append("trailer\n<< /Size 6 /Root 1 0 R >>\nstartxref\n")
                .append(xrefOffset)
                .append("\n%%EOF\n");
        output.write(xref.toString().getBytes(StandardCharsets.US_ASCII));
        return output.toByteArray();
    }
}
