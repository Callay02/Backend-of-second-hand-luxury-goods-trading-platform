package icu.callay.controller;



import cn.dev33.satoken.util.SaResult;
import icu.callay.entity.GoodsType;
import icu.callay.service.GoodsTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * (GoodsType)表控制层
 *
 * @author Callay
 * @since 2024-01-30 19:07:47
 */
@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("goodsType")
public class GoodsTypeController {

    private final GoodsTypeService goodsTypeService;

    /**
     * @return SaResult
     * @author Callay
     * &#064;description 获取商品类型信息
     * &#064;2024/4/5 14:14
     */
    @GetMapping("getGoodsType")
    public SaResult getGoodsType(){
        return goodsTypeService.getGoodsType();
    }

    /**
     * @param page:
     * @param rows:
     * @return SaResult
     * @author Callay
     * &#064;description 分页查询商品类型信息
     * &#064;2024/4/5 14:14
     */
    @GetMapping("getGoodsTypePage")
    public SaResult getGoodsTypePage(@RequestParam int page,@RequestParam int rows){
        return goodsTypeService.getGoodsTypePage(page,rows);
    }

    /**
     * @param goodsType:
     * @return SaResult
     * @author Callay
     * &#064;description 管理员添加商品类型
     * &#064;2024/4/5 14:14
     */
    @PostMapping("addGoodsType")
    public SaResult addGoodsType(@RequestBody GoodsType goodsType){
        return goodsTypeService.addGoodsType(goodsType);
    }

    /**
     * @param type:
     * @return SaResult
     * @author Callay
     * &#064;description 根据商品类型id删除商品类型
     * &#064;2024/4/5 14:14
     */
    @GetMapping("deleteTypeById")
    public SaResult deleteTypeById(@RequestParam("type")int type){
        return goodsTypeService.deleteTypeById(type);
    }

    /**
     * @param goodsType:
     * @return SaResult
     * @author Callay
     * &#064;description 更新商品类型信息
     * &#064;2024/4/5 14:15
     */
    @PostMapping("updateTypeName")
    public SaResult updateTypeName(@RequestBody GoodsType goodsType){
        return goodsTypeService.updateTypeName(goodsType);
    }
}

