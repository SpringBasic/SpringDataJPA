package com.springjpa.repository;

import com.springjpa.entity.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    // SimpleJpaRepository
    @Test
    public void save() {
        // 식별자가 null 이기에 persist
        Item item = new Item();
        itemRepository.save(item);


        // merge
        // select 을 통해 기존 entity 을 db 에서 찾고 다시 수정 된 entity 을 삽입( 비 효율적 )
        // 변경 감지로 수정 하자 , merge 쓰는 일은 거의 없음 !
    }
}
