package com.softcondominios.api.rest.resource;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.softcondominios.api.domain.ColaboradorDomain;
import com.softcondominios.api.domain.GrupoPermissaoDomain;
import com.softcondominios.api.domain.UserDomain;
import com.softcondominios.api.rest.dto.NewColaboradorDto;
import com.softcondominios.api.rest.dto.ViewColaboradorDto;
import com.softcondominios.api.service.ColaboradorService;
import com.softcondominios.api.service.GrupoPermissaoService;
import com.softcondominios.api.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/colaboradores")
@Api("API Rest colaboradores")
@CrossOrigin(origins = "*")
public class ColaboradorResource {
	@Autowired
	private ColaboradorService colaboradorService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	@Autowired
	private GrupoPermissaoService grupoPermissaoService;
	

	
	
	@ApiOperation("Retorna uma lista de colaboradores")
	@GetMapping
	public ResponseEntity<Page<ViewColaboradorDto>> findAll(Pageable page){
			
		
		Page<ViewColaboradorDto> colaboradorDto = convertDto(colaboradorService.findAll(page).getContent());
		return ResponseEntity.ok(colaboradorDto); 
	
	}
	
	@ApiOperation("Cria colaboradores")
	@PostMapping
	public ResponseEntity<NewColaboradorDto> save(@RequestBody NewColaboradorDto colaborador){
		
		ColaboradorDomain colaboradorDomain = colaboradorService.save(createColaborador(colaborador));
	
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(colaboradorDomain.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	
	
	
	
	
	
	
	private ColaboradorDomain createColaborador(NewColaboradorDto newColaborador) {
		
		ColaboradorDomain colaborador = new ColaboradorDomain(newColaborador);
		colaborador.setUsuario(createUser(newColaborador));
		
		
		return colaborador;
	}

	private UserDomain createUser(NewColaboradorDto newColaborador) {
		
		UserDomain user = new UserDomain(newColaborador);
		user.setGrupoPermissao(findGruposByDescricao(newColaborador.getFuncao()));
		user.setPassword(criptografarSenha(user.getPassword()));
		return userService.save(user);
	}
	
	
	private String criptografarSenha(String senha) {
		return bCryptPasswordEncoder.encode(senha);
	}
	
	private Set<GrupoPermissaoDomain> convertByHashSet(GrupoPermissaoDomain grupoPermissaoDomain){
		Set<GrupoPermissaoDomain> permissao = new HashSet<GrupoPermissaoDomain>();
		permissao.add(grupoPermissaoDomain);
		return permissao;
	}
	
	private Set<GrupoPermissaoDomain> findGruposByDescricao(String descricao) {
		
		return convertByHashSet(grupoPermissaoService.findByDescricao(descricao));
	}
	
	private Page<ViewColaboradorDto> convertDto(List<ColaboradorDomain> colaborador) {
		return new PageImpl<>(colaborador.stream().map(c -> new ViewColaboradorDto(c)).collect(Collectors.toList())); 
	}
}
