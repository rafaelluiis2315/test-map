package com.liferay.testmap.DTO;

import com.liferay.testmap.Model.Client;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ClientDaoImpl implements ClientDao {

    private List<Client> clients = new ArrayList<>();


    @Override
    public void save(Client client) {
        client.setId(System.currentTimeMillis());
        clients.add(client);
    }

    @Override
    public void deleteById(Long id) {
        Client client = clients.stream()
                .filter((u) -> u.getId().equals(id))
                .collect(Collectors.toList()).get(0);

        try {
            client.getTestMapFile().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        clients.removeIf((c) -> c.getId().equals(id));
    }

    @Override
    public List<Client> getAll() {
        return clients;
    }

}

