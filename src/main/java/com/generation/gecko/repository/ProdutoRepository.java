package com.generation.gecko.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.generation.gecko.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long>{

	public List<Produto> findAllByNomeContainingIgnoreCaseOrDescricaoContainingIgnoreCase(@Param("nome") String nome, @Param("descricao") String descricao);   
	
	public List<Produto> findAllByReciclavelTrue();
	
	public List<Produto> findAllByEstadoTrue();
	
	public List<Produto> findAllByEstadoFalse();
}
