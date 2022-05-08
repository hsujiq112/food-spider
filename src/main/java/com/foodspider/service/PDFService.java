package com.foodspider.service;

import com.foodspider.model.Restaurant;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayOutputStream;
import java.util.AbstractMap;
import java.util.Map;
import java.util.UUID;

/**
 * The PDFService
 */
public class PDFService {

    @Autowired
    private RestaurantService restaurantService;

    /**
     * Create the pdf.
     *
     * @param restaurantID the restaurant id
     * @return the binary with the restaurant's name
     * @throws Exception the exception
     */
    public Map.Entry<ByteArrayOutputStream, String> createPDF(UUID restaurantID) throws Exception {
        var restaurant = (Restaurant) Hibernate.unproxy(restaurantService.getByID(restaurantID));
        var document = new PDDocument();
        var page = new PDPage();
        document.addPage(page);
        var contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
        contentStream.setLeading(14.5f);
        contentStream.newLineAtOffset(25, 700);
        var restaurantString = restaurant.toString();
        var restaurantStrings = restaurantString.split("\r");
        for (var string : restaurantStrings) {
            contentStream.showText(string);
            contentStream.newLineAtOffset(0, -15);
        }
        var bOutStream = new ByteArrayOutputStream();
        contentStream.endText();
        contentStream.close();
        document.save(bOutStream);
        document.close();
        return new AbstractMap.SimpleEntry<>(bOutStream, restaurant.getName());
    }
}
