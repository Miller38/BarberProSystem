package com.barberpro.controller;

import com.barberpro.dao.DashboardDAO;
import com.barberpro.view.HomePanel;



import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import org.jfree.data.category.DefaultCategoryDataset;

public class HomeController {

    private HomePanel view;

    private DashboardDAO dao;

    public HomeController(
            HomePanel view) {

        this.view = view;

        dao = new DashboardDAO();

        cargarEstadisticas();

        cargarGrafico();
    }

    // ESTADISTICAS
    private void cargarEstadisticas() {

        view.lblClientes.setText(
                String.valueOf(
                        dao.totalClientes()));

        view.lblCitas.setText(
                String.valueOf(
                        dao.totalCitasHoy()));

        view.lblIngresos.setText(
                "$" + dao.totalIngresos());

        view.lblServicios.setText(
                String.valueOf(
                        dao.totalServicios()));
    }

    // GRAFICO
    private void cargarGrafico() {

        DefaultCategoryDataset dataset =
                new DefaultCategoryDataset();

        dataset.addValue(
                100,
                "Ingresos",
                "Lunes");

        dataset.addValue(
                200,
                "Ingresos",
                "Martes");

        dataset.addValue(
                150,
                "Ingresos",
                "Miércoles");

        dataset.addValue(
                300,
                "Ingresos",
                "Jueves");

        dataset.addValue(
                250,
                "Ingresos",
                "Viernes");

        JFreeChart chart =
                ChartFactory.createBarChart(
                        "Ingresos semanales",
                        "Día",
                        "Ingresos",
                        dataset);

        ChartPanel panel =
                new ChartPanel(chart);

        view.chartPanel.removeAll();

        view.chartPanel.add(panel);

        view.chartPanel.revalidate();
    }
}