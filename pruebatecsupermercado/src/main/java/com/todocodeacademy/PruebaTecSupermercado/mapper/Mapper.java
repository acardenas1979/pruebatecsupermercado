package com.todocodeacademy.PruebaTecSupermercado.mapper;

import com.todocodeacademy.PruebaTecSupermercado.dto.DetalleVentaDTO;
import com.todocodeacademy.PruebaTecSupermercado.dto.ProductoDTO;
import com.todocodeacademy.PruebaTecSupermercado.dto.SucursalDTO;
import com.todocodeacademy.PruebaTecSupermercado.dto.VentaDTO;
import com.todocodeacademy.PruebaTecSupermercado.model.Producto;
import com.todocodeacademy.PruebaTecSupermercado.model.Sucursal;
import com.todocodeacademy.PruebaTecSupermercado.model.Venta;

import java.util.stream.Collectors;

public class Mapper {

    public static ProductoDTO toDTO(Producto producto) {
        if (producto == null) {return null;}

        return ProductoDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .categoria(producto.getCategoria())
                .precio(producto.getPrecio())
                .cantidad(producto.getCantidad())
                .build();
    }

    public static SucursalDTO toDTO(Sucursal sucursal) {
        if (sucursal == null) {return null;}

        return SucursalDTO.builder()
                .id(sucursal.getId())
                .nombre(sucursal.getNombre())
                .direccion(sucursal.getDireccion())
                .build();
    }

    public static VentaDTO toDTO(Venta venta) {
        if (venta == null) {return null;}

        var detalle = venta.getDetalleVenta().stream().map(detalleVenta ->
                DetalleVentaDTO.builder()
                    .id(detalleVenta.getProducto().getId())
                    .nombreProducto(detalleVenta.getProducto().getNombre())
                    .cantidadProducto(detalleVenta.getCantidadProducto())
                    .precioProducto(detalleVenta.getPrecioProducto())
                    .subtotalProducto(detalleVenta.getPrecioProducto() * detalleVenta.getCantidadProducto())
                    .build()
        ).collect(Collectors.toList());

        var total = detalle.stream().map(DetalleVentaDTO::getSubtotalProducto).reduce(0.0, Double::sum);

        return VentaDTO.builder()
                .id(venta.getId())
                .fecha(venta.getFecha())
                .idSucursal(venta.getSucursal().getId())
                .estado(venta.getEstado())
                .detalle(detalle)
                .total(total)
                .build();
    }
}
