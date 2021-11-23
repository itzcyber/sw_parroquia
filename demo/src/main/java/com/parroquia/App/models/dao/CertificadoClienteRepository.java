package com.parroquia.App.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.parroquia.App.models.entities.CertificadosClientes;

public interface CertificadoClienteRepository extends CrudRepository<CertificadosClientes, Integer> {

	@Query("select c from CertificadosClientes as c where c.cliente.id = ?1")
	List<CertificadosClientes> findFilesByClienteId(Integer id);

	@Modifying
	@Query("delete from CertificadosClientes as c where c.cliente.id = ?1 and c.modifiedFileName in (?2) ")
	void deleteFilesByClienteIdAndImagesNames(Integer id, List<String> removefiles);

	@Modifying
	@Query("delete from CertificadosClientes as c where c.cliente.id = ?1 ")
	void deleteFilesByClienteId(Integer id);

}
