package gukbap.shop.controller;

import gukbap.shop.domain.item.Sundaegukbap;
import gukbap.shop.domain.item.Item;
import gukbap.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping(value = "/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new SundaegukbapForm());
        return "items/createItemForm";
    }

    @PostMapping(value = "/items/new")
    public String create(SundaegukbapForm form) {
        Sundaegukbap sundaegukbap = new Sundaegukbap();
        sundaegukbap.setName(form.getName());
        sundaegukbap.setPrice(form.getPrice());
        sundaegukbap.setStockQuantity(form.getStockQuantity());
        sundaegukbap.setChef(form.getChef());
        sundaegukbap.setBrand(form.getBrand());
        itemService.saveItem(sundaegukbap);
        return "redirect:/items";
    }

    /**
     * 상품 목록
     */
    @GetMapping(value = "/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }


    @GetMapping(value = "/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        //여기서 찾은 item을 바로 수정해주면안된다. 왜냐하면 준영속성 상태이기 때문이다.
        // 준영속성이 된 이유는 service @Transactional이 종료되는 시점(saveItem())에 em.close()가 이뤄지고
        // 이때 엔티티들은 1차캐시에서 준영속성 상태로 변경된다.
        Sundaegukbap item = (Sundaegukbap) itemService.findOne(itemId);
        SundaegukbapForm form = new SundaegukbapForm();
        form.setId(item.getId());   //@TODO: id를 Set한다니 말도안된다.
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setChef(item.getChef());
        form.setBrand(item.getBrand());
        model.addAttribute("form", form);
        return "items/updateItemForm";

    }

    @PostMapping(value = "/items/{itemId}/edit")
    public String updateItem(@ModelAttribute("form") SundaegukbapForm form) {
        itemService.updateItem(form.getId(), form.getName(), form.getPrice());
        return "redirect:/items";
    }
}