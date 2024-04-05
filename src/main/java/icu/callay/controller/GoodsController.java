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

    private GoodsService goodsService;

    @Autowired
    public void GoodsService(GoodsService goodsService){
        this.goodsService=goodsService;
    }

    /**
     * @return SaResult
     * @author Callay
     * &#064;description 获取随机商品信息(用于普通用户首页展示)
     * &#064;2024/4/5 14:03
     */
    @GetMapping("getRandomGoodsInfo")
    public SaResult getRandomGoodsInfo(){
        return goodsService.getRandomGoodsInfo();
    }

    /**
     * @param type:
     * @return SaResult
     * @author Callay
     * &#064;description 更加商品类型获取商品信息
     * &#064;2024/4/5 14:04
     */
    @GetMapping("getGoodsByType")
    public SaResult getGoodsByType(@RequestParam(name="type") int type){
        return goodsService.getGoodsByType(type);
    }

    /**
     * @param type:
     * @param page:
     * @param rows:
     * @return SaResult
     * @author Callay
     * &#064;description 根据商品类型分页查询商品信息
     * &#064;2024/4/5 14:04
     */
    @GetMapping("getPageByType")
    public SaResult getPageByType(@RequestParam("type") int type, @RequestParam("page") int page, @RequestParam("rows") int rows){
        return goodsService.getPageByType(type,page,rows);
    }

    /**
     * @param id:
     * @return SaResult
     * @author Callay
     * &#064;description 根据商品id查询商品信息(封装后输出)
     * &#064;2024/4/5 14:05
     */
    @GetMapping("getGoodsById")
    public SaResult getGoodsById(@RequestParam("id")int id){
        return goodsService.getGoodsById(id);
    }

    /**
     * @param state:
     * @param page:
     * @param rows:
     * @return SaResult
     * @author Callay
     * &#064;description 根据商品出售状态分页查询商品信息
     * &#064;2024/4/5 14:06
     */
    @GetMapping("getGoodsPageByState")
    public SaResult getGoodsPage(@RequestParam("state")String state,@RequestParam("page")int page,@RequestParam("rows")int rows){
        return goodsService.getGoodsPageByState(state,page,rows);
    }

    /**
     * @param goods:
     * @return SaResult
     * @author Callay
     * &#064;description 管理员添加商品
     * &#064;2024/4/5 14:06
     */
    @PostMapping("addGoods")
    public SaResult addGoods(@RequestBody Goods goods){
        return goodsService.addGoods(goods);
    }

    /**
     * @param goods:
     * @return SaResult
     * @author Callay
     * &#064;description 管理员根据商品id删除商品
     * &#064;2024/4/5 14:07
     */
    @PostMapping("deleteGoodsById")
    public SaResult deleteGoodsById(@RequestBody Goods goods){
        return goodsService.deleteGoodsById(goods);
    }

    /**
     * @param goods:
     * @return SaResult
     * @author Callay
     * &#064;description 管理员更新商品信息
     * &#064;2024/4/5 14:07
     */
    @PostMapping("updateGoods")
    public SaResult updateGoods(@RequestBody Goods goods){
        return goodsService.updateGoods(goods);
    }

    /**
     * @param id:
     * @return SaResult
     * @author Callay
     * &#064;description 管理员更新商品信息时用到(未封装)
     * &#064;2024/4/5 14:08
     */
    @GetMapping("getGoodsByIdNoVo")
    public SaResult getGoodsByIdNoVo(@RequestParam Long id){
        return goodsService.getGoodsByIdNoVo(id);
    }

    /**
     * @param goods:
     * @return SaResult
     * @author Callay
     * &#064;description 根据商品id更新商品信息
     * &#064;2024/4/5 14:10
     */
    @PostMapping("updateGoodsById")
    public SaResult updateGoodsById(@RequestBody Goods goods){
        return goodsService.updateGoodsById(goods);
    }

    /**
     * @param searchGoodsVo:
     * @return SaResult
     * @author Callay
     * &#064;description 根据品牌、类型、详情分页查找商品(可选条件查询)
     * &#064;2024/4/5 14:10
     */
    @PostMapping("getGoodsPageByBrandAndTypeAndInfo")
    public SaResult getGoodsPageByBrandAndTypeAndInfo(@RequestBody SearchGoodsVo searchGoodsVo){
        return goodsService.getGoodsPageByBrandAndTypeAndInfo(searchGoodsVo);
    }

    /**
     * @param searchGoodsVo:
     * @return SaResult
     * @author Callay
     * &#064;description 管理员根据id、品牌、类型、详情分页查找商品(可选条件查询)
     * &#064;2024/4/5 16:23
     */
    @PostMapping("adminGetGoodsPageByBrandAndTypeAndInfo")
    public SaResult adminGetGoodsPageByBrandAndTypeAndInfo(@RequestBody SearchGoodsVo searchGoodsVo){
        return goodsService.adminGetGoodsPageByBrandAndTypeAndInfo(searchGoodsVo);
    }
}

