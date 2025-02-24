package com.tcs.printing;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

@WebServlet(name = "bill", urlPatterns = { "/bill" })
public class billServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jobId = request.getParameter("job_id");

        if (jobId == null || jobId.trim().isEmpty()) {
            response.setContentType("text/html");
            response.getWriter().println("<script>alert('Invalid job ID.'); window.history.back();</script>");
            return;
        }

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM jobs WHERE job_id = ?")) {

            ps.setString(1, jobId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String clientName = rs.getString("client_name");
                String company = rs.getString("company");
                String mobile = rs.getString("mobile");
                String dueDate = rs.getString("due_date");
                String status = rs.getString("status");
                String jobDetails=rs.getString("job_details");

                if (!"Completed".equalsIgnoreCase(status)) {
                    response.setContentType("text/html");
                    response.getWriter().println("<script>alert('Bill can only be generated for completed jobs.'); window.history.back();</script>");
                    return;
                }

                Document document = new Document(PageSize.A4);
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "inline; filename=Invoice_" + jobId + ".pdf");
                OutputStream out = response.getOutputStream();
                PdfWriter.getInstance(document, out);
                document.open();

                // Company Details
                Paragraph companyDetails = new Paragraph("Company Name: "+company+"\nStreet Address, City, ZIP\nPhone: "+mobile+"\nwww.companyname.com", new Font(Font.FontFamily.HELVETICA, 12));
                companyDetails.setAlignment(Element.ALIGN_LEFT);
                document.add(companyDetails);
                
                // Work Order Header
                Paragraph workOrder = new Paragraph("WORK ORDER", new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD));
                workOrder.setAlignment(Element.ALIGN_RIGHT);
                document.add(workOrder);
                
                // Job Details Table
                PdfPTable jobTable = new PdfPTable(2);
                jobTable.setWidthPercentage(100);
                jobTable.setSpacingBefore(10f);
                
                jobTable.addCell(getStyledCell("Bill ID: " + jobId, PdfPCell.ALIGN_LEFT));
                jobTable.addCell(getStyledCell("Due Date: " + dueDate, PdfPCell.ALIGN_RIGHT));
                jobTable.addCell(getStyledCell("Client Name: " + clientName, PdfPCell.ALIGN_LEFT));
                jobTable.addCell(getStyledCell("Company: " + company, PdfPCell.ALIGN_RIGHT));
                jobTable.addCell(getStyledCell("Mobile: " + mobile, PdfPCell.ALIGN_LEFT));
                
                document.add(jobTable);
                
                // Work Order Items Table
                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);
                table.setSpacingBefore(15f);
                table.setWidths(new float[]{1, 4, 1, 2, 2});
                
                table.addCell(getStyledHeaderCell("QTY"));
                table.addCell(getStyledHeaderCell("DESCRIPTION"));
                table.addCell(getStyledHeaderCell("TAXED"));
                table.addCell(getStyledHeaderCell("UNIT PRICE"));
                table.addCell(getStyledHeaderCell("TOTAL"));
                
                // Sample Row (Replace with DB Data in Future)
                
                table.addCell(getStyledCell("1", PdfPCell.ALIGN_CENTER));
                table.addCell(getStyledCell(jobDetails, PdfPCell.ALIGN_LEFT));
                table.addCell(getStyledCell("x", PdfPCell.ALIGN_CENTER));
                table.addCell(getStyledCell("150.00", PdfPCell.ALIGN_RIGHT));
                table.addCell(getStyledCell("2250.00", PdfPCell.ALIGN_RIGHT));
                
				/*
				 * table.addCell(getStyledCell("5", PdfPCell.ALIGN_CENTER));
				 * table.addCell(getStyledCell("Hourly Labor for ABC (5 hours)",
				 * PdfPCell.ALIGN_LEFT)); table.addCell(getStyledCell("",
				 * PdfPCell.ALIGN_CENTER)); table.addCell(getStyledCell("50.00",
				 * PdfPCell.ALIGN_RIGHT)); table.addCell(getStyledCell("250.00",
				 * PdfPCell.ALIGN_RIGHT));
				 */
                document.add(table);
                
                // Total Amount Table
                PdfPTable totalTable = new PdfPTable(2);
                totalTable.setWidthPercentage(50);
                totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
                
                totalTable.addCell(getStyledCell("SUBTOTAL", PdfPCell.ALIGN_LEFT));
                totalTable.addCell(getStyledCell("2500.00", PdfPCell.ALIGN_RIGHT));
                totalTable.addCell(getStyledCell("G (18%)", PdfPCell.ALIGN_LEFT));
                totalTable.addCell(getStyledCell("154.69", PdfPCell.ALIGN_RIGHT));
                totalTable.addCell(getStyledCell("TOTAL", PdfPCell.ALIGN_LEFT, BaseColor.BLUE));
                totalTable.addCell(getStyledCell("2654.69", PdfPCell.ALIGN_RIGHT, BaseColor.BLUE));
                
                document.add(totalTable);
                
                // Terms and Conditions
                Paragraph terms = new Paragraph("\nTerms & Conditions:", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD));
                document.add(terms);
                
                Paragraph termsContent = new Paragraph(
                        "1. Payment must be made within 30 days from the invoice date.\n" +
                        "2. The company is not liable for delays due to unforeseen circumstances.\n" +
                        "3. Any modifications or cancellations may incur additional charges.\n" +
                        "4. The company is not responsible for errors due to incorrect specifications.\n" +
                        "5. Any defects must be reported within 7 days of delivery.", 
                        new Font(Font.FontFamily.HELVETICA, 10)
                );
                document.add(termsContent);
                
                document.close();
                out.close();
            } else {
                response.setContentType("text/html");
                response.getWriter().println("<script>alert('Job not found.'); window.history.back();</script>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method to create styled table cells
    private PdfPCell getStyledCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, new Font(Font.FontFamily.HELVETICA, 12)));
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(5f);
        return cell;
    }

    // Helper method to create styled table headers
    private PdfPCell getStyledHeaderCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5f);
        return cell;
    }

    // Helper method to create styled table cells with color
    private PdfPCell getStyledCell(String text, int alignment, BaseColor color) {
        PdfPCell cell = getStyledCell(text, alignment);
        cell.setBackgroundColor(color);
        return cell;
    }
}
