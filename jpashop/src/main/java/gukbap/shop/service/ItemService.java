package gukbap.shop.service;

import gukbap.shop.domain.item.Item;
import gukbap.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    @Transactional  //update 된다면 dirty checking이 일어나서 update query가 쏴진다. 수정 코드는 service에서 위임받아서 해준다 절대 controller에서 해주지 말자 (controller는 DTO만 쏴준다)
    public void updateItem(Long id, String name, int price) {
        Item item = itemRepository.findOne(id);
        item.setName(name);
        item.setPrice(price);
    }



}
