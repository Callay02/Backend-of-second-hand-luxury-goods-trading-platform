package icu.callay.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import icu.callay.entity.ShoppingCart;

/**
 * (ShoppingCart)表服务接口
 *
 * @author Callay
 * @since 2024-02-07 11:40:36
 */
public interface ShoppingCartService extends IService<ShoppingCart> {

    SaResult addToShoppingCart(ShoppingCart shoppingCart);

    SaResult getShoppingCartById(int id);

    SaResult deleteShoppingCartById(Long uid, Long gid);
}


