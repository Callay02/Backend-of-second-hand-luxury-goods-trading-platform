package icu.callay.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.callay.mapper.GoodsBrandMapper;
import icu.callay.entity.GoodsBrand;
import icu.callay.service.GoodsBrandService;
import icu.callay.vo.GoodsBrandPageVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * (GoodsBrand)表服务实现类
 *
 * @author Callay
 * @since 2024-01-30 19:07:20
 */
@Service("goodsBrandService")
@RequiredArgsConstructor
public class GoodsBrandServiceImpl extends ServiceImpl<GoodsBrandMapper, GoodsBrand> implements GoodsBrandService {

    private final GoodsBrandMapper goodsBrandMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult getGoodsBrand() {
        try {
            List<GoodsBrand> goodsBrandList = goodsBrandMapper.getGoodsBrand();
            return SaResult.data(goodsBrandList);
        }
        catch (Exception e){
            throw new RuntimeException("获取品牌信息失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult getGoodsBrandPage(int page, int rows) {
        try {
            Page<GoodsBrand> brandPage = new Page<>(page,rows);
            goodsBrandMapper.selectPage(brandPage,null);

            GoodsBrandPageVo goodsBrandPageVo = new GoodsBrandPageVo();
            goodsBrandPageVo.setGoodsBrandList(brandPage.getRecords());
            goodsBrandPageVo.setTotal(brandPage.getTotal());

            return SaResult.data(goodsBrandPageVo);
        }
        catch (Exception e){
            throw new RuntimeException("获取品牌信息失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult addBrand(GoodsBrand goodsBrand) {
        try {
            if(count(new QueryWrapper<GoodsBrand>().eq("id",goodsBrand.getId()))==0){
                save(goodsBrand);
                return SaResult.ok("添加成功");
            }
            return SaResult.error("该品牌已存在");
        }
        catch (Exception e){
            throw new RuntimeException("添加品牌失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult deleteBrandById(GoodsBrand goodsBrand) {
        try{
            remove(new QueryWrapper<GoodsBrand>().eq("id",goodsBrand.getId()));
            return SaResult.ok("删除成功");
        }
        catch (Exception e){
            throw new RuntimeException("删除品牌失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult updateBrand(GoodsBrand goodsBrand) {
        try {
            update(new UpdateWrapper<GoodsBrand>().eq("id",goodsBrand.getId()).set("name",goodsBrand.getName()));
            return SaResult.ok("修改成功");
        }
        catch (Exception e){
            throw new RuntimeException("修改品牌信息失败");
        }
    }
}


