package icu.callay.controller;



import cn.dev33.satoken.util.SaResult;
import icu.callay.entity.Goods;
import icu.callay.entity.GoodsBrand;
import icu.callay.service.GoodsService;
import icu.callay.vo.SearchGoodsVo;
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

    //根据商品出售状态获取商品信息
    @GetMapping("getGoodsPageByState")
    public SaResult getGoodsPage(@RequestParam("state")int state,@RequestParam("page")int page,@RequestParam("rows")int rows){
        return goodsService.getGoodsPageByState(state,page,rows);
    }

    //添加商品
    @PostMapping("addGoods")
    public SaResult addGoods(@RequestBody Goods goods){
        return goodsService.addGoods(goods);
    }

    //管理员删除商品
    @PostMapping("deleteGoodsById")
    public SaResult deleteGoodsById(@RequestBody Goods goods){
        return goodsService.deleteGoodsById(goods);
    }

    @PostMapping("updateGoods")
    public SaResult updateGoods(@RequestBody Goods goods){
        return goodsService.updateGoods(goods);
    }

    //未封装(在管理员更新商品信息时用到)
    @GetMapping("getGoodsByIdNoVo")
    public SaResult getGoodsByIdNoVo(@RequestParam Long id){
        return goodsService.getGoodsByIdNoVo(id);
    }

    //根据商品id更新商品信息
    @PostMapping("updateGoodsById")
    public SaResult updateGoodsById(@RequestBody Goods goods){
        return goodsService.updateGoodsById(goods);
    }

    //根据品牌、类型、详情分页查找商品
    @PostMapping("getGoodsPageByBrandAndTypeAndInfo")
    public SaResult getGoodsPageByBrandAndTypeAndInfo(@RequestBody SearchGoodsVo searchGoodsVo){
        return goodsService.getGoodsPageByBrandAndTypeAndInfo(searchGoodsVo);
    }
}

