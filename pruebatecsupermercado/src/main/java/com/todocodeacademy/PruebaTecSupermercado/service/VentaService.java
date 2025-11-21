package com.todocodeacademy.PruebaTecSupermercado.service;

import com.todocodeacademy.PruebaTecSupermercado.dto.DetalleVentaDTO;
import com.todocodeacademy.PruebaTecSupermercado.dto.VentaDTO;
import com.todocodeacademy.PruebaTecSupermercado.exception.NotFoundException;
import com.todocodeacademy.PruebaTecSupermercado.mapper.Mapper;
import com.todocodeacademy.PruebaTecSupermercado.model.DetalleVenta;
import com.todocodeacademy.PruebaTecSupermercado.model.Producto;
import com.todocodeacademy.PruebaTecSupermercado.model.Sucursal;
import com.todocodeacademy.PruebaTecSupermercado.model.Venta;
import com.todocodeacademy.PruebaTecSupermercado.repository.ProductoRepository;
import com.todocodeacademy.PruebaTecSupermercado.repository.SucursalRepository;
import com.todocodeacademy.PruebaTecSupermercado.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VentaService implements IVentaService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private SucursalRepository sucursalRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Override
    public List<VentaDTO> getVentas() {

        List<Venta> ventas = ventaRepository.findAll();

        List<VentaDTO> ventasDTO = new ArrayList<>();

        for(Venta v : ventas){
            ventasDTO.add(Mapper.toDTO(v));
        }

        return ventasDTO;
    }

    @Override
    public VentaDTO createVenta(VentaDTO ventaDTO) {
        if(ventaDTO==null) throw new RuntimeException("ventaDTO no existe");
        if(ventaDTO.getIdSucursal()==null) throw new RuntimeException("Debe indicar la sucursal");
        if(ventaDTO.getDetalle()==null || ventaDTO.getDetalle().isEmpty()) throw new RuntimeException("Debe incluir al menos un producto");

        Sucursal sucursal = sucursalRepository.findById(ventaDTO.getIdSucursal()).orElse(null);
        if(sucursal==null) throw new NotFoundException("Sucursal no existe");

        Venta venta = new Venta();
        venta.setFecha(ventaDTO.getFecha());
        venta.setEstado(ventaDTO.getEstado());
        venta.setSucursal(sucursal);
        venta.setTotal(ventaDTO.getTotal());

        List<DetalleVenta> detalles = new ArrayList<>();
        Double totalCalculado = 0.0;

        for(DetalleVentaDTO detalleVentaDTO : ventaDTO.getDetalle()){
            Producto producto = productoRepository.findByNombre(detalleVentaDTO.getNombreProducto()).orElse(null);
            if(producto==null){
                throw new RuntimeException("Producto no existe" + detalleVentaDTO.getNombreProducto());
            }

            DetalleVenta detalleVenta = new DetalleVenta();
            detalleVenta.setProducto(producto);
            detalleVenta.setPrecioProducto(detalleVentaDTO.getPrecioProducto());
            detalleVenta.setCantidadProducto(detalleVentaDTO.getCantidadProducto());
            detalleVenta.setVenta(venta);

            detalles.add(detalleVenta);
            totalCalculado = totalCalculado + (detalleVentaDTO.getPrecioProducto()*detalleVentaDTO.getCantidadProducto());
        }

        venta.setDetalleVenta(detalles);

        venta = ventaRepository.save(venta);

        return Mapper.toDTO(venta);
    }

    @Override
    public VentaDTO updateVenta(Long id, VentaDTO ventaDTO) {
        Venta venta = ventaRepository.findById(id).orElse(null);
        if(venta==null) throw new RuntimeException("Venta no existe");

        if(ventaDTO.getFecha()!=null)
            venta.setFecha(ventaDTO.getFecha());
        if(ventaDTO.getEstado()!=null)
            venta.setEstado(ventaDTO.getEstado());
        if(ventaDTO.getTotal()!=null)
            venta.setTotal(ventaDTO.getTotal());
        if(ventaDTO.getDetalle()!=null) {
            Sucursal sucursal = sucursalRepository.findById(ventaDTO.getIdSucursal()).orElse(null);
            if(sucursal==null) throw new NotFoundException("Sucursal no existe");

            venta.setSucursal(sucursal);
        }


        return Mapper.toDTO(ventaRepository.save(venta));
    }

    @Override
    public void deleteVenta(Long id) {
        if(!ventaRepository.existsById(id)){
            throw new NotFoundException("Venta no existe");
        }

        ventaRepository.deleteById(id);
    }
}
