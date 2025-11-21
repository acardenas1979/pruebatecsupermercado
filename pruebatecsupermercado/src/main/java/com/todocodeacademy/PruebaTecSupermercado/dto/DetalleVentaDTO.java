package com.todocodeacademy.PruebaTecSupermercado.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleVentaDTO {
    private Long id;
    private String nombreProducto;
    private Integer cantidadProducto;
    private Double precioProducto;
    private Double subtotalProducto;
}
