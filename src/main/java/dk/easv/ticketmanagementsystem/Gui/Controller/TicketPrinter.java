package dk.easv.ticketmanagementsystem.Gui.Controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;
import dk.easv.ticketmanagementsystem.BE.Ticket;

import java.awt.Desktop;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class TicketPrinter {

    public static void printTicket(Ticket ticket) {
        try {
            UUID ticketUUID = UUID.randomUUID();
            String qrCodePath = generateQRCode(ticketUUID.toString(), "QRCode.png");
            String barcodePath = generateBarcode(ticketUUID.toString(), "Barcode.png");

            String pdfPath = "Ticket_" + ticketUUID + ".pdf";
            createPDF(ticket, qrCodePath, barcodePath, pdfPath);

            File pdfFile = new File(pdfPath);
            if (pdfFile.exists() && Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(pdfFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String generateQRCode(String text, String fileName) throws Exception {
        int width = 200, height = 200;
        BitMatrix matrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height);
        Path path = Paths.get(fileName);
        MatrixToImageWriter.writeToPath(matrix, "PNG", path);
        return fileName;
    }

    private static String generateBarcode(String text, String fileName) throws Exception {
        int width = 300, height = 100;
        BitMatrix matrix = new MultiFormatWriter().encode(text, BarcodeFormat.CODE_128, width, height);
        Path path = Paths.get(fileName);
        MatrixToImageWriter.writeToPath(matrix, "PNG", path);
        return fileName;
    }

    private static void createPDF(Ticket ticket, String qrPath, String barcodePath, String pdfPath) throws IOException {
        PdfWriter writer = new PdfWriter(pdfPath);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(new Paragraph("Event Ticket").setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("Name: " + ticket.getCustomerName()));
        document.add(new Paragraph("Email: " + ticket.getCustomerEmail()));
        document.add(new Paragraph("Ticket Type: " + ticket.getTicketType()));
        document.add(new Paragraph("Event: " + ticket.getEvent().getName()));

        Image qrCode = new Image(com.itextpdf.io.image.ImageDataFactory.create(qrPath)).scaleToFit(150, 150);
        document.add(qrCode);

        Image barcode = new Image(com.itextpdf.io.image.ImageDataFactory.create(barcodePath)).scaleToFit(250, 100);
        document.add(barcode);

        document.close();
    }
}

