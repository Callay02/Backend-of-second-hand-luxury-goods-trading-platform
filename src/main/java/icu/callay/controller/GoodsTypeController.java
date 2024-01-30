package icu.callay.controller;



import cn.dev33.satoken.util.SaResult;
import icu.callay.entity.GoodsType;
import icu.callay.service.GoodsTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * (GoodsType)表控制层
 *
 * @author makejava
 * @since 2024-01-30 19:07:47
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("goodsType")
public class GoodsTypeController {

    @Autowired
    private GoodsTypeService goodsTypeService;

    @GetMapping("getGoodsType")
    public SaResult getGoodsType(){
        return goodsTypeService.getGoodsType();
    }
}

