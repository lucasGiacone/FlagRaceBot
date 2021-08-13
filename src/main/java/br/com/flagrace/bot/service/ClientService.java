package br.com.flagrace.bot.service;

import br.com.flagrace.bot.model.Client;
import br.com.flagrace.bot.repository.ClientRepository;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    @Transactional()
    public Client create(Client client){
        return this.findById(client.getId()) == null ? this.clientRepository.save(client) : null;
    }
    @Transactional()
    public Client update(Client client){
        return this.clientRepository.save(client);
    }

    @Transactional()
    public Client findById(Long id){
        Optional<Client> client = this.clientRepository.findById(id);
        return client.orElse(null);
    }

    @Transactional()
    public Client findOrCreateByAuthor(User author){
        Client client = findById(author.getIdLong());
        return client == null ? this.create(new Client(author.getIdLong(), author.getName())) : client;
    }
}
