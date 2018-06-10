package com.paguenet.repository;

import org.springframework.data.repository.CrudRepository;

import com.paguenet.models.Pagamento;

public interface PagamentoRepository extends CrudRepository<Pagamento, String>{
	Pagamento findByCodigo(long codigo);
}
