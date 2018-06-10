package com.paguenet.repository;

import org.springframework.data.repository.CrudRepository;

import com.paguenet.models.Cliente;
import com.paguenet.models.Pagamento;

public interface ClienteRepository extends CrudRepository<Cliente, String>{
	
	Iterable<Cliente> findByPagamento(Pagamento pagamento);
	Cliente findByIdCliente(long idCliente);
}
