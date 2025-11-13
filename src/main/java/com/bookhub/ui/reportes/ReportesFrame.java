package com.bookhub.ui.reportes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

public class ReportesFrame extends JFrame {

    private JTextField txtDescripcion;
    private JComboBox<String> cmbTipo;
    private JTable tablaReportes;
    private DefaultTableModel modeloTabla;
    private JButton btnGenerar, btnListar, btnEliminar, btnBuscar;

    public ReportesFrame() {
        setTitle("Módulo de Reportes - BookHub");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 🔹 Panel superior para formulario
        JPanel panelSuperior = new JPanel(new FlowLayout());
        panelSuperior.add(new JLabel("Descripción:"));
        txtDescripcion = new JTextField(15);
        panelSuperior.add(txtDescripcion);

        panelSuperior.add(new JLabel("Tipo:"));
        cmbTipo = new JComboBox<>(new String[]{"general", "disponibles", "activos"});
        panelSuperior.add(cmbTipo);

        btnGenerar = new JButton("Generar");
        btnListar = new JButton("Listar");
        btnBuscar = new JButton("Buscar por ID");
        btnEliminar = new JButton("Eliminar");

        panelSuperior.add(btnGenerar);
        panelSuperior.add(btnListar);
        panelSuperior.add(btnBuscar);
        panelSuperior.add(btnEliminar);

        add(panelSuperior, BorderLayout.NORTH);

        // 🔹 Tabla central
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Descripción", "Tipo", "Fecha"}, 0);
        tablaReportes = new JTable(modeloTabla);
        add(new JScrollPane(tablaReportes), BorderLayout.CENTER);

        // 🔹 Acciones de botones
        btnGenerar.addActionListener(e -> generarReporte());
        btnListar.addActionListener(e -> listarReportes());
        btnBuscar.addActionListener(e -> buscarPorId());
        btnEliminar.addActionListener(e -> eliminarReporte());

        setVisible(true);
    }

    private void generarReporte() {
        String descripcion = txtDescripcion.getText().trim();
        String tipo = (String) cmbTipo.getSelectedItem();

        if (descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La descripción no puede estar vacía.");
            return;
        }

        try {
            btnGenerar.setEnabled(false);
            URL url = new URL("http://localhost:8080/api/reportes");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInput = String.format("{\"descripcion\":\"%s\", \"tipo\":\"%s\"}", descripcion, tipo);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonInput.getBytes());
                os.flush();
            }

            if (conn.getResponseCode() == 200 || conn.getResponseCode() == 201) {
                JOptionPane.showMessageDialog(this, "Reporte generado exitosamente.");
                txtDescripcion.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error al generar reporte: " + conn.getResponseCode());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        } finally {
            btnGenerar.setEnabled(true);
        }
    }

    private void listarReportes() {
        try {
            URL url = new URL("http://localhost:8080/api/reportes");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = reader.lines().reduce("", (a, b) -> a + b);
            reader.close();

            modeloTabla.setRowCount(0);
            if (response.equals("[]")) {
                JOptionPane.showMessageDialog(this, "No hay reportes disponibles.");
                return;
            }

            String[] registros = response.split("\\},\\{");
            for (String reg : registros) {
                Vector<String> fila = new Vector<>();
                fila.add(extraerCampo(reg, "id"));
                fila.add(extraerCampo(reg, "descripcion"));
                fila.add(extraerCampo(reg, "tipo"));
                fila.add(extraerCampo(reg, "fechaGeneracion"));
                modeloTabla.addRow(fila);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al listar reportes: " + ex.getMessage());
        }
    }

    private void buscarPorId() {
        String id = JOptionPane.showInputDialog(this, "Ingrese el ID del reporte:");
        if (id == null || id.isEmpty()) return;

        try {
            URL url = new URL("http://localhost:8080/api/reportes/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String response = reader.lines().reduce("", (a, b) -> a + b);
                reader.close();

                JOptionPane.showMessageDialog(this, "Reporte encontrado:\n" + response);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el reporte con ID " + id);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void eliminarReporte() {
        int fila = tablaReportes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un reporte para eliminar.");
            return;
        }

        String id = modeloTabla.getValueAt(fila, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar reporte ID " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            URL url = new URL("http://localhost:8080/api/reportes/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");

            if (conn.getResponseCode() == 200) {
                JOptionPane.showMessageDialog(this, "Reporte eliminado correctamente.");
                listarReportes();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar reporte.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private String extraerCampo(String json, String campo) {
        try {
            int inicio = json.indexOf("\"" + campo + "\":") + campo.length() + 3;
            int fin = json.indexOf(",", inicio);
            if (fin == -1) fin = json.indexOf("}", inicio);
            return json.substring(inicio, fin).replace("\"", "").trim();
        } catch (Exception e) {
            return "";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ReportesFrame::new);
    }
}
