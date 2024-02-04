package icu.callay.controller;



import cn.dev33.satoken.util.SaResult;
import icu.callay.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * (Goods)表控制层
 *
 * @author makejava
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
}

