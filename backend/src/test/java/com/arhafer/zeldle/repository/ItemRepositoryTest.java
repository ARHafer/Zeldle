package com.arhafer.zeldle.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jdbc.test.autoconfigure.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJdbcTest
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepo;

    @Autowired
    private JdbcTemplate template;

    private static final String INSERT_QUERY = "INSERT INTO items (id) VALUES (?)";

    @BeforeEach
    void setUp() {
        template.update("CREATE TABLE items(id INTEGER PRIMARY KEY);");
    } // Goes without saying, but this is NOT the actual schema.

    /*
     * When this query is run given a list of excluded IDs, it should randomly select an item ID, excluding any in the
     * given list.
     */
    @Test
    void getRandomIdGivenExclusions_actuallyExcludes() {
        template.update(INSERT_QUERY, 1);
        template.update(INSERT_QUERY, 2);
        template.update(INSERT_QUERY, 3);
        template.update(INSERT_QUERY, 4);
        List<Integer> excludedIds = List.of(1, 3);

        int selectedId = itemRepo.getRandomIdGivenExclusions(excludedIds);

        assertThat(selectedId).isEven();
    }
}
