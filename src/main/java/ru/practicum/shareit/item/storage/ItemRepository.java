package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("SELECT it FROM Item it WHERE LOWER(it.name) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
            "LOWER(it.description) LIKE LOWER(CONCAT('%', :description, '%')) AND it.available = :available")
    List<Item> search(@Param("name") String name, @Param("description") String description,
                      @Param("available") Boolean available);

    List<Item> findItemByOwnerId(Long id);
}
