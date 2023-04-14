package com.liferay.testmap.DTO;

import com.liferay.testmap.Model.Client;

import java.util.List;

public interface ClientDao {

    void save(Client client);

    void deleteById(Long id);

    List<Client> getAll();
}

