package icu.callay.controller;



import cn.dev33.satoken.util.SaResult;
import icu.callay.entity.ShoppingCart;
import icu.callay.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * (ShoppingCart)表控制层
 *
 * @author Callay
 * @since 2024-02-07 11:40:36
 */
@RestController
@RequestMapping("shoppingCart")
public class ShoppingCartController {

    private ShoppingCartService shoppingCartService;
    @Autowired
    public void ShoppingCartService(ShoppingCartService shoppingCartService){
        this.shoppingCartService=shoppingCartService;
    }

    /**
     * @param shoppingCart:
     * @return SaResult
     * @author Callay
     * &#064;description 添加商品至购物车
     * &#064;2024/4/5 14:53
     */
    @PostMapping("addToShoppingCart")
    public SaResult addToShoppingCart(@RequestBody ShoppingCart shoppingCart){
        return shoppingCartService.addToShoppingCart(shoppingCart);
    }

    /**
     * @param id:
     * @return SaResult
     * @author Callay
     * &#064;description 根据用户id查找已添加购物车商品信息
     * &#064;2024/4/5 14:53
     */
    @GetMapping("getShoppingCartById")
    public SaResult getShoppingCartById(@RequestParam("id")int id){
        return shoppingCartService.getShoppingCartById(id);
    }

    /**
     * @param uid:
     * @param gid:
     * @return SaResult
     * @author Callay
     * &#064;description 根据用户id和商品id删除购物车内商品信息
     * &#064;2024/4/5 14:54
     */
    //TODO
    @GetMapping("deleteShoppingCartById")
    public SaResult deleteShoppingCartById(@RequestParam("uid")Long uid,@RequestParam("gid")Long gid){
        return shoppingCartService.deleteShoppingCartById(uid,gid);
    }
}

