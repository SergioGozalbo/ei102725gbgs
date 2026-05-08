package es.uji.ei1027.ei102725gbgs.model;

import java.time.LocalDate;

/**
 * Represents a contract record in the OVI system.
 */
public class RegistreContracte {
    /** Unique contract identifier. */
    private int idContrato;

    /** Identifier of the related selection. */
    private int idSeleccion;

    /** Contract start date. */
    private LocalDate fechaInicio;

    /** Contract end date (nullable). */
    private LocalDate fechaFin;

    /** URL or path to the contract PDF file. */
    private String urlPdf;

    /**
     * Builds an empty contract record.
     */
    public RegistreContracte() {
    }

    /**
     * Returns the contract identifier.
     *
     * @return contract identifier
     */
    public int getIdContrato() {
        return idContrato;
    }

    /**
     * Sets the contract identifier.
     *
     * @param idContrato contract identifier to assign
     */
    public void setIdContrato(int idContrato) {
        this.idContrato = idContrato;
    }

    /**
     * Returns the selection identifier.
     *
     * @return selection identifier
     */
    public int getIdSeleccion() {
        return idSeleccion;
    }

    /**
     * Sets the selection identifier.
     *
     * @param idSeleccion selection identifier to assign
     */
    public void setIdSeleccion(int idSeleccion) {
        this.idSeleccion = idSeleccion;
    }

    /**
     * Returns the contract start date.
     *
     * @return contract start date
     */
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Sets the contract start date.
     *
     * @param fechaInicio start date to assign
     */
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * Returns the contract end date.
     *
     * @return contract end date, or null
     */
    public LocalDate getFechaFin() {
        return fechaFin;
    }

    /**
     * Sets the contract end date.
     *
     * @param fechaFin end date to assign
     */
    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    /**
     * Returns the PDF URL/path.
     *
     * @return PDF URL/path
     */
    public String getUrlPdf() {
        return urlPdf;
    }

    /**
     * Sets the PDF URL/path.
     *
     * @param urlPdf PDF URL/path to assign
     */
    public void setUrlPdf(String urlPdf) {
        this.urlPdf = urlPdf;
    }

    /**
     * Builds a text representation of the contract record.
     *
     * @return contract record description string
     */
    @Override
    public String toString() {
        return ("RegistreContracte {%n"
            + "    idContrato=%d,%n"
            + "    idSeleccion=%d,%n"
            + "    fechaInicio=%s,%n"
            + "    fechaFin=%s%n"
            + "}").formatted(idContrato, idSeleccion, fechaInicio, fechaFin);
    }
}
