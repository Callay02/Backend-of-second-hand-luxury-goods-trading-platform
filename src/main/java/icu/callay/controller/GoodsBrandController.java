package icu.callay.controller;



import cn.dev33.satoken.util.SaResult;
import icu.callay.service.GoodsBrandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * (GoodsBrand)表控制层
 *
 * @author Callay
 * @since 2024-01-30 19:07:20
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("goodsBrand")
public class GoodsBrandController {

    @Autowired
    private GoodsBrandService goodsBrandService;

    @GetMapping("getGoodsBrand")
    public SaResult getGoodsBrand(){
        return goodsBrandService.getGoodsBrand();
    }
}

