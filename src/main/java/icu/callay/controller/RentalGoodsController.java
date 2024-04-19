package icu.callay.controller;



import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.util.SaResult;
import icu.callay.entity.RentalGoods;
import icu.callay.service.RentalGoodsService;
import icu.callay.vo.SearchGoodsVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * (RentalGoods)表控制层
 *
 * @author Callay
 * @since 2024-03-25 19:26:19
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("rentalGoods")
public class RentalGoodsController {

    private final RentalGoodsService rentalGoodsService;

    /**
     * @param state:
     * @param page:
     * @param rows:
     * @return SaResult
     * @author Callay
     * &#064;description 根据state分页查询商品信息
     * &#064;2024/4/5 14:42
     */
    @GetMapping("getGoodsPageByState")
    public SaResult getGoodsPage(@RequestParam("state") String state,@RequestParam("page")int page, @RequestParam("rows")int rows){
        return rentalGoodsService.getGoodsPageByState(state,page,rows);
    }

    /**
     * @param rentalGoods:
     * @return SaResult
     * @author Callay
     * &#064;description 添加租赁商品
     * &#064;2024/4/5 14:43
     */
    @PostMapping("addGoods")
    @SaCheckRole("管理员")
    public SaResult addGoods(@RequestBody RentalGoods rentalGoods){
        return rentalGoodsService.addGoods(rentalGoods);
    }

    /**
     * @param id:
     * @return SaResult
     * @author Callay
     * &#064;description 在管理员更新商品信息时用到(未封装)
     * &#064;2024/4/5 14:43
     */
    @GetMapping("getGoodsByIdNoVo")
    @SaCheckRole("管理员")
    public SaResult getGoodsByIdNoVo(@RequestParam String id){
        return rentalGoodsService.getGoodsByIdNoVo(id);
    }

    /**
     * @param rentalGoods:
     * @return SaResult
     * @author Callay
     * &#064;description 根据商品id更新商品信息
     * &#064;2024/4/5 14:43
     */
    @PostMapping("updateGoodsById")
    @SaCheckRole("管理员")
    public SaResult updateGoodsById(@RequestBody RentalGoods rentalGoods){
        return rentalGoodsService.updateGoodsById(rentalGoods);
    }

    /**
     * @param rentalGoods:
     * @return SaResult
     * @author Callay
     * &#064;description 管理员根据商品id删除商品
     * &#064;2024/4/5 14:43
     */
    @PostMapping("deleteGoodsById")
    @SaCheckRole("管理员")
    public SaResult deleteGoodsById(@RequestBody RentalGoods rentalGoods){
        return rentalGoodsService.deleteGoodsById(rentalGoods);
    }

    /**
     * @param searchGoodsVo:
     * @return SaResult
     * @author Callay
     * &#064;description 根据品牌、类型、详情分页查找商品
     * &#064;2024/4/5 14:44
     */
    @PostMapping("getGoodsPageByBrandAndTypeAndInfo")
    public SaResult getGoodsPageByBrandAndTypeAndInfo(@RequestBody SearchGoodsVo searchGoodsVo){
        return rentalGoodsService.getGoodsPageByBrandAndTypeAndInfo(searchGoodsVo);
    }

    /**
     * @param id:
     * @return SaResult
     * @author Callay
     * &#064;description 根据商品id获取商品信息(封装后输出)
     * &#064;2024/4/5 14:44
     */
    @GetMapping("getGoodsById")
    public SaResult getGoodsById(@RequestParam("id")String id){
        return rentalGoodsService.getGoodsById(id);
    }

    /**
     * @param searchGoodsVo:
     * @return SaResult
     * @author Callay
     * &#064;description 管理员根据商品id、品牌、类型、详情分页查找商品
     * &#064;2024/4/5 16:39
     */
    @PostMapping("adminGetGoodsPageByBrandAndTypeAndInfo")
    @SaCheckRole("管理员")
    public SaResult adminGetGoodsPageByBrandAndTypeAndInfo(@RequestBody SearchGoodsVo searchGoodsVo){
        return rentalGoodsService.adminGetGoodsPageByBrandAndTypeAndInfo(searchGoodsVo);
    }

}

