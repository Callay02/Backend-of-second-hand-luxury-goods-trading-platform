package icu.callay.controller;



import cn.dev33.satoken.util.SaResult;
import icu.callay.entity.Goods;
import icu.callay.entity.RentalGoods;
import icu.callay.service.RentalGoodsService;
import icu.callay.vo.SearchGoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * (RentalGoods)表控制层
 *
 * @author Callay
 * @since 2024-03-25 19:26:19
 */
@RestController
@RequestMapping("rentalGoods")
public class RentalGoodsController {

    @Autowired
    private RentalGoodsService rentalGoodsService;

    //更加state获取商品信息
    @GetMapping("getGoodsPageByState")
    public SaResult getGoodsPage(@RequestParam("state") String state,@RequestParam("page")int page, @RequestParam("rows")int rows){
        return rentalGoodsService.getGoodsPageByState(state,page,rows);
    }

    //添加商品
    @PostMapping("addGoods")
    public SaResult addGoods(@RequestBody RentalGoods rentalGoods){
        return rentalGoodsService.addGoods(rentalGoods);
    }

    //未封装(在管理员更新商品信息时用到)
    @GetMapping("getGoodsByIdNoVo")
    public SaResult getGoodsByIdNoVo(@RequestParam String id){
        return rentalGoodsService.getGoodsByIdNoVo(id);
    }

    //根据商品id更新商品信息
    @PostMapping("updateGoodsById")
    public SaResult updateGoodsById(@RequestBody RentalGoods rentalGoods){
        return rentalGoodsService.updateGoodsById(rentalGoods);
    }

    //管理员删除商品
    @PostMapping("deleteGoodsById")
    public SaResult deleteGoodsById(@RequestBody RentalGoods rentalGoods){
        return rentalGoodsService.deleteGoodsById(rentalGoods);
    }

    //根据品牌、类型、详情分页查找商品
    @PostMapping("getGoodsPageByBrandAndTypeAndInfo")
    public SaResult getGoodsPageByBrandAndTypeAndInfo(@RequestBody SearchGoodsVo searchGoodsVo){
        return rentalGoodsService.getGoodsPageByBrandAndTypeAndInfo(searchGoodsVo);
    }

    //封装后输出
    @GetMapping("getGoodsById")
    public SaResult getGoodsById(@RequestParam("id")String id){
        return rentalGoodsService.getGoodsById(id);
    }

}

