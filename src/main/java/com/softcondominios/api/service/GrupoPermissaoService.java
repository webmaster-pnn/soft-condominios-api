package com.softcondominios.api.service;


import javax.transaction.Transactional;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.softcondominios.api.domain.GrupoPermissaoDomain;
import com.softcondominios.api.repository.GrupoPermissaoRepository;

@Service
public class GrupoPermissaoService {

	@Autowired
	GrupoPermissaoRepository grupoPermissaoRepository;
	
	public Page<GrupoPermissaoDomain> findAll(Pageable pageable){
		
		return grupoPermissaoRepository.findAll(pageable);
	}
	
	
	public GrupoPermissaoDomain findByDescricao(String descricao) {
		return grupoPermissaoRepository.findByDescricao(descricao).orElseThrow(()-> objectNotFoundException("Permissão não existe!"));
	}
	
	public GrupoPermissaoDomain save(GrupoPermissaoDomain g) {
		return grupoPermissaoRepository.save(g);
	}
	
	@Transactional
	private ObjectNotFoundException objectNotFoundException(String message) {
		return new ObjectNotFoundException(
				message, GrupoPermissaoDomain.class.getName());
	}
}