package com.generation.gecko.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.gecko.model.Produto;
import com.generation.gecko.repository.CategoriaRepository;
import com.generation.gecko.repository.ProdutoRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProdutoController {
	// Injeção de Dependências
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	// CRUD
	@PostMapping
	public ResponseEntity<Produto> post(@Valid @RequestBody Produto produto){
		return categoriaRepository.findById(produto.getCategoria().getId())
				.map(resposta -> ResponseEntity.ok(produtoRepository.save(produto)))
				.orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
	}
	
	@PutMapping
	public ResponseEntity<Produto> put(@Valid @RequestBody Produto produto){
		return produtoRepository.findById(produto.getId())
				.map(respostaId -> categoriaRepository.findById(produto.getCategoria().getId())
						.map(respostaCategoria -> ResponseEntity.ok(produtoRepository.save(produto)))
						.orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@GetMapping
	public ResponseEntity<List<Produto>> getAll(){
		return ResponseEntity.ok(produtoRepository.findAll());
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("{id}")
	public void delete(@PathVariable Long id) {
		Optional<Produto> optional = produtoRepository.findById(id);
		
		if (optional.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		else {
			produtoRepository.deleteById(id);
		}
	}
	
	// Métodos personalizados
	@GetMapping("{id}")
	public ResponseEntity<Produto> getById(@PathVariable Long id){
		return produtoRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@GetMapping("/nome_descricao/{nome}/{descricao}")
	public ResponseEntity<List<Produto>> getByNomeOrDescricao(@PathVariable String nome, @PathVariable String descricao){
		return ResponseEntity.ok(produtoRepository.findAllByNomeContainingIgnoreCaseOrDescricaoContainingIgnoreCase(nome, descricao));
	}
	
	@GetMapping("/reciclavel")
	public ResponseEntity<List<Produto>> getByReciclavel(){
		return ResponseEntity.ok(produtoRepository.findAllByReciclavelTrue());
	}
	
	@GetMapping("/estado_novo")
	public ResponseEntity<List<Produto>> getByEstadoTrue(){
		return ResponseEntity.ok(produtoRepository.findAllByEstadoTrue());
	}
	
	@GetMapping("/estado_false")
	public ResponseEntity<List<Produto>> getByEstadoFalse(){
		return ResponseEntity.ok(produtoRepository.findAllByEstadoFalse());
	}
}
