package one.digitalinnovation.gof.service.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.ClienteRepository;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.model.EnderecoRepository;
import one.digitalinnovation.gof.service.ClienteService;
import one.digitalinnovation.gof.service.ViaCepService;

@Service
public class ClienteServiceImplementation implements ClienteService{

	@Autowired
	ClienteRepository clienteRepository; //Padrão Singleton
	@Autowired
	EnderecoRepository enderecoRepository; //Padrão Singleton
	@Autowired
	ViaCepService viaCepService; //Padrão Singleton

	
	@Override
	public Iterable<Cliente> buscarTodos() {
		return clienteRepository.findAll();
	}

	@Override
	public Cliente buscarPorId(Long id) {
		Optional<Cliente> cliente = Optional.ofNullable(clienteRepository.findById(id).orElseGet(() ->{
			return null;
		}));
		return cliente.get();
	}

	@Override
	public void inserir(Cliente cliente) {
		String cep = cliente.getEndereco().getCep();
		Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
			Endereco novoEndereco = viaCepService.consultarCep(cep);
			enderecoRepository.save(novoEndereco);
			return novoEndereco;
		});
		cliente.setEndereco(endereco);
		clienteRepository.save(cliente);
	}
	
	@Override
	public void atualizar(Long id, Cliente cliente) {
		Optional<Cliente> clienteDB = clienteRepository.findById(id);

		if (clienteDB.isPresent()) {
			inserir(cliente);
		}
	}

	@Override
	public void deletar(Long id) {
		clienteRepository.deleteById(id);
		
	}

}
