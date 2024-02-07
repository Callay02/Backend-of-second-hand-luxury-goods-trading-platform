package icu.callay.controller;



import cn.dev33.satoken.util.SaResult;
import icu.callay.entity.ShoppingCart;
import icu.callay.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * (ShoppingCart)表控制层
 *
 * @author makejava
 * @since 2024-02-07 11:40:36
 */
@RestController
@RequestMapping("shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("addToShoppingCart")
    public SaResult addToShoppingCart(@RequestBody ShoppingCart shoppingCart){
        return shoppingCartService.addToShoppingCart(shoppingCart);
    }

    @GetMapping("getShoppingCartById")
    public SaResult getShoppingCartById(@RequestParam("id")int id){
        return shoppingCartService.getShoppingCartById(id);
    }

    @GetMapping("deleteShoppingCartById")
    public SaResult deleteShoppingCartById(@RequestParam("uid")int uid,@RequestParam("gid")int gid){
        System.out.println(uid+" "+gid);
        return shoppingCartService.deleteShoppingCartById(uid,gid);
    }
}

