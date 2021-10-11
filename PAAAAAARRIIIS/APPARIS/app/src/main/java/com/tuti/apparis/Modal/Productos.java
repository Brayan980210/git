package com.tuti.apparis.Modal;

public class Productos {

    private String nombrep,codigo,precio,venta,stock,categoria,fecha,hora,imagen,pid;


    public Productos(){

    }

    public Productos(String nombrep, String codigo, String precio, String venta, String stock, String categoria, String fecha, String hora, String imagen, String pid) {
        this.nombrep = nombrep;
        this.codigo = codigo;
        this.precio = precio;
        this.venta = venta;
        this.stock = stock;
        this.categoria = categoria;
        this.fecha = fecha;
        this.hora = hora;
        this.imagen = imagen;
        this.pid = pid;
    }

    public String getNombrep() {
        return nombrep;
    }

    public void setNombrep(String nombrep) {
        this.nombrep = nombrep;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getVenta() {
        return venta;
    }

    public void setVenta(String venta) {
        this.venta = venta;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
