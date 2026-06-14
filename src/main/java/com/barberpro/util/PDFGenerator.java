package com.barberpro.util;

import com.barberpro.model.Venta;
import com.barberpro.model.DetalleVenta;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import java.awt.Desktop;
import java.io.File;

public class PDFGenerator {

    public static void generarFactura(Venta venta) {

        try {
            //=================================================
            // CREAR CARPETA FACTURAS
            //=================================================
            File carpeta = new File("facturas");
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            String numeroFactura = String.format("%06d", venta.getId());
            String destino = "facturas/FACTURA_" + numeroFactura + ".pdf";

            System.out.println("Guardando PDF en: " + new File(destino).getAbsolutePath());

            //=================================================
            // PDF
            //=================================================
            PdfWriter writer = new PdfWriter(destino);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            //=================================================
            // ENCABEZADO
            //=================================================
            Paragraph empresa = new Paragraph("BARBER PRO")
                    .setBold()
                    .setFontSize(22)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(empresa);

            document.add(new Paragraph("Sistema Profesional de Barbería")
                    .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("FACTURA DE VENTA")
                    .setBold()
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("Factura No: " + numeroFactura));
            document.add(new Paragraph("Fecha: " + venta.getFecha()));
            document.add(new Paragraph(" "));

            //=================================================
            // DATOS DEL CLIENTE
            //=================================================
            document.add(new Paragraph("DATOS DEL CLIENTE").setBold());
            document.add(new Paragraph("Cliente: " + venta.getCliente()));
            document.add(new Paragraph(" "));

            //=================================================
            // DETALLE DE ITEMS (SERVICIOS Y PRODUCTOS)
            //=================================================
            document.add(new Paragraph("DETALLE DE LA VENTA").setBold());
            document.add(new Paragraph(" "));

            // Tabla de items con 5 columnas
            Table tablaItems = new Table(UnitValue.createPercentArray(new float[]{15, 45, 15, 15, 20}));
            tablaItems.setWidth(UnitValue.createPercentValue(100));
            
            // Encabezados de la tabla
            tablaItems.addCell("Tipo");
            tablaItems.addCell("Descripción");
            tablaItems.addCell("Cantidad");
            tablaItems.addCell("Precio Unit.");
            tablaItems.addCell("Subtotal");

            // Agregar cada item
            for (DetalleVenta detalle : venta.getDetalles()) {
                String tipo = detalle.getTipoItem().equals("SERVICIO") ? "Servicio" : "Producto";
                tablaItems.addCell(tipo);
                tablaItems.addCell(detalle.getNombre());
                tablaItems.addCell(String.valueOf(detalle.getCantidad()));
                tablaItems.addCell("$ " + String.format("%.2f", detalle.getPrecioUnitario()));
                tablaItems.addCell("$ " + String.format("%.2f", detalle.getSubtotal()));
            }

            document.add(tablaItems);
            document.add(new Paragraph(" "));

            //=================================================
            // TABLA DE TOTALES
            //=================================================
            Table tablaTotales = new Table(2);
            tablaTotales.setWidth(UnitValue.createPercentValue(60));
            tablaTotales.setTextAlignment(TextAlignment.RIGHT);
            
            tablaTotales.addCell("Subtotal:");
            tablaTotales.addCell("$ " + String.format("%.2f", venta.getSubtotal()));
            
            tablaTotales.addCell("IVA (19%):");
            tablaTotales.addCell("$ " + String.format("%.2f", venta.getImpuesto()));
            
            tablaTotales.addCell("Descuento:");
            tablaTotales.addCell("$ " + String.format("%.2f", venta.getDescuento()));
            
            tablaTotales.addCell("Método Pago:");
            tablaTotales.addCell(venta.getMetodoPago());
            
            tablaTotales.addCell("Estado:");
            tablaTotales.addCell(venta.getEstado());

            document.add(tablaTotales);
            document.add(new Paragraph(" "));

            //=================================================
            // TOTAL
            //=================================================
            Paragraph total = new Paragraph("TOTAL PAGADO: $ " + String.format("%.2f", venta.getTotal()))
                    .setBold()
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.RIGHT);
            document.add(total);

            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            //=================================================
            // PIE DE PÁGINA
            //=================================================
            document.add(new Paragraph("Gracias por preferir BARBER PRO")
                    .setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("¡Te esperamos pronto!")
                    .setTextAlignment(TextAlignment.CENTER));

            document.close();

            System.out.println("PDF generado correctamente");

            //=================================================
            // ABRIR AUTOMATICAMENTE
            //=================================================
            Desktop.getDesktop().open(new File(destino));

        } catch (Exception e) {
            System.err.println("Error generando PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Método sobrecargado para compatibilidad con código antiguo
    // (si llaman al método antiguo con un solo servicio)
    @Deprecated
    public static void generarFacturaLegacy(Venta venta) {
        generarFactura(venta);
    }
}