# ✂️ BarberProSystem

**Sistema Integral de Gestión para Barberías** - Administración de clientes, citas con alertas automáticas (WhatsApp/Email), inventario, ventas de productos/servicios y facturación en PDF.

![Java Version](https://img.shields.io/badge/Java-17+-blue.svg)
![Database](https://img.shields.io/badge/SQLite-3.x-green.svg)
![License](https://img.shields.io/badge/License-MIT-green.svg)
![Version](https://img.shields.io/badge/Version-1.0.0-brightgreen.svg)

---

## 📋 Descripción

**BarberProSystem** es una aplicación de escritorio desarrollada en Java que optimiza la gestión completa de una barbería. El sistema permite administrar clientes, agendar citas con **alertas automáticas vía WhatsApp y email**, controlar el inventario de productos, realizar ventas de servicios y productos, y generar **facturas profesionales en formato PDF**.

**Ventaja clave:** Utiliza **SQLite** como motor de base de datos → No necesita instalación de servidor, es ultrarrápido y todo el sistema se puede llevar en un USB.

---

## ✨ Características Principales

| Módulo | Funcionalidades |
|--------|----------------|
| 👥 **Clientes** | Registro completo, historial de visitas, preferencias, búsqueda avanzada |
| 📅 **Citas** | Agenda visual, confirmación automática, recordatorios, cancelación |
| 📱 **Alertas WhatsApp** | Confirmación de cita, recordatorio 24h antes, aviso de cancelación |
| ✉️ **Alertas Email** | Bienvenida, confirmación de cita, recordatorios, promociones |
| 📦 **Inventario** | Control de stock, productos (shampoo, cera, tintes, etc.), alertas de stock bajo |
| 💰 **Ventas** | Venta de servicios + productos, carrito de compra, múltiples formas de pago |
| 🧾 **Facturación PDF** | Generación automática de facturas profesionales con logo, datos del negocio, detalle de venta |
| 📊 **Reportes** | Ventas diarias/semanales/mensuales, productos más vendidos, ingresos por barbero |
| 💾 **Backup Integrado** | Copia de seguridad de la base de datos SQLite con un clic |

---

## 🛠️ Tecnologías Utilizadas

| Tecnología | Versión | Propósito |
|------------|---------|------------|
| Java | 17+ | Lenguaje principal |
| Swing | - | Interfaz gráfica de usuario |
| SQLite | 3.36+ | Base de datos embebida |
| JDBC (sqlite-jdbc) | 3.42.0+ | Conexión a SQLite |
| iTextPDF / Apache PDFBox | 5.5+ | Generación de facturas PDF |
| JavaMail API | 1.6+ | Envío de correos electrónicos |
| Maven | 3.8+ | Gestión de dependencias |

---

## 📦 Requisitos Previos

**¡Mínimos requisitos!** Solo necesitas:

- [Java JDK/JRE 17 o superior](https://www.oracle.com/java/technologies/downloads/)
- [Git](https://git-scm.com/) (opcional, para clonar)
- Credenciales SMTP (Gmail, Outlook, etc. para email)

> ✅ **No necesitas instalar MySQL ni ningún servidor de base de datos. SQLite va incluido en el proyecto.**

---

## 🚀 Instalación y Ejecución

### 1️⃣ Descargar o clonar el proyecto

```bash
git clone https://github.com/tu-usuario/BarberProSystem.git
cd BarberProSystem