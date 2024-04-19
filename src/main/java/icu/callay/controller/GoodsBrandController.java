package icu.callay.controller;



import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.util.SaResult;
import icu.callay.entity.GoodsBrand;
import icu.callay.service.GoodsBrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
@RequestMapping("goodsBrand")
public class GoodsBrandController {

    private final GoodsBrandService goodsBrandService;

    /**
     * @return SaResult
     * @author Callay
     * &#064;description 获取品牌信息
     * &#064;2024/4/5 14:01
     */
    @GetMapping("getGoodsBrand")
    public SaResult getGoodsBrand(){
        return goodsBrandService.getGoodsBrand();
    }

    /**
     * @param page:
     * @param rows:
     * @return SaResult
     * @author Callay
     * &#064;description 分页获取品牌信息
     * &#064;2024/4/5 14:02
     */
    @GetMapping("getGoodsBrandPage")
    public SaResult getGoodsBrandPage(@RequestParam("page") int page,@RequestParam("rows") int rows){
        return goodsBrandService.getGoodsBrandPage(page,rows);
    }

    /**
     * @param goodsBrand:
     * @return SaResult
     * @author Callay
     * &#064;description 添加品牌
     * &#064;2024/4/5 14:02
     */
    @PostMapping("addBrand")
    @SaCheckRole("管理员")
    public SaResult addBrand(@RequestBody GoodsBrand goodsBrand){
        return goodsBrandService.addBrand(goodsBrand);
    }

    /**
     * @param goodsBrand:
     * @return SaResult
     * @author Callay
     * &#064;description 更加品牌id删除品牌
     * &#064;2024/4/5 14:02
     */
    @PostMapping("deleteBrandById")
    @SaCheckRole("管理员")
    public SaResult deleteBrandById(@RequestBody GoodsBrand goodsBrand){
        return goodsBrandService.deleteBrandById(goodsBrand);
    }

    /**
     * @param goodsBrand:
     * @return SaResult
     * @author Callay
     * &#064;description 更新品牌信息
     * &#064;2024/4/5 14:02
     */
    @PostMapping("updateBrand")
    @SaCheckRole("管理员")
    public SaResult updateBrand(@RequestBody GoodsBrand goodsBrand){
        return goodsBrandService.updateBrand(goodsBrand);
    }
}

