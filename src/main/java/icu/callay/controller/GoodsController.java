package icu.callay.controller;



import cn.dev33.satoken.util.SaResult;
import icu.callay.entity.Goods;
import icu.callay.entity.GoodsBrand;
import icu.callay.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * (Goods)表控制层
 *
 * @author Callay
 * @since 2024-01-30 19:08:08
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping("getRandomGoodsInfo")
    public SaResult getRandomGoodsInfo(){
        return goodsService.getRandomGoodsInfo();
    }

    @GetMapping("getGoodsByType")
    public SaResult getGoodsByType(@RequestParam(name="type") int type){
        return goodsService.getGoodsByType(type);
    }

    @GetMapping("getPageByType")
    public SaResult getPageByType(@RequestParam("type") int type, @RequestParam("page") int page, @RequestParam("rows") int rows){
        return goodsService.getPageByType(type,page,rows);
    }

    //封装后输出
    @GetMapping("getGoodsById")
    public SaResult getGoodsById(@RequestParam("id")int id){
        return goodsService.getGoodsById(id);
    }

    @GetMapping("getGoodsPageByState")
    public SaResult getGoodsPage(@RequestParam("state")int state,@RequestParam("page")int page,@RequestParam("rows")int rows){
        return goodsService.getGoodsPageByState(state,page,rows);
    }

    @PostMapping("addGoods")
    public SaResult addGoods(@RequestBody Goods goods){
        return goodsService.addGoods(goods);
    }

    @PostMapping("deleteGoodsById")
    public SaResult deleteGoodsById(@RequestBody Goods goods){
        return goodsService.deleteGoodsById(goods);
    }

    @PostMapping("updateGoods")
    public SaResult updateGoods(@RequestBody Goods goods){
        return goodsService.updateGoods(goods);
    }

    //未封装
    @GetMapping("getGoodsByIdNoVo")
    public SaResult getGoodsByIdNoVo(@RequestParam Long id){
        return goodsService.getGoodsByIdNoVo(id);
    }

    @PostMapping("updateGoodsById")
    public SaResult updateGoodsById(@RequestBody Goods goods){
        return goodsService.updateGoodsById(goods);
    }
}

