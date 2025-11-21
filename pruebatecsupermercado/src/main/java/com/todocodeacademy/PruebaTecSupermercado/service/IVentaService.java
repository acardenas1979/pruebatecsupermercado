package com.todocodeacademy.PruebaTecSupermercado.service;

import com.todocodeacademy.PruebaTecSupermercado.dto.VentaDTO;
import com.todocodeacademy.PruebaTecSupermercado.model.Venta;

import java.util.List;

public interface IVentaService {
    List<VentaDTO> getVentas();
    VentaDTO createVenta(VentaDTO ventaDTO);
    VentaDTO updateVenta(Long id, VentaDTO ventaDTO);
    void deleteVenta(Long id);
}
