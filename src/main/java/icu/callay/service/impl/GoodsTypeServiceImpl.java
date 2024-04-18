package icu.callay.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.callay.mapper.GoodsTypeMapper;
import icu.callay.entity.GoodsType;
import icu.callay.service.GoodsTypeService;
import icu.callay.vo.GoodsTypePageVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * (GoodsType)表服务实现类
 *
 * @author Callay
 * @since 2024-01-30 19:07:47
 */
@Service("goodsTypeService")
@RequiredArgsConstructor
public class GoodsTypeServiceImpl extends ServiceImpl<GoodsTypeMapper, GoodsType> implements GoodsTypeService {

    private final GoodsTypeMapper goodsTypeMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult getGoodsType() {
        try{
            List<GoodsType> goodsTypeList =goodsTypeMapper.getGoodsType();
            return SaResult.data(goodsTypeList);
        }
        catch (Exception e) {
            throw new RuntimeException("获取商品类型失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult getGoodsTypePage(int page, int rows) {
        try {
            Page<GoodsType> goodsTypePage = new Page<>(page,rows);
            goodsTypeMapper.selectPage(goodsTypePage,null);

            GoodsTypePageVo goodsTypePageVo = new GoodsTypePageVo();
            goodsTypePageVo.setGoodsTypeList(goodsTypePage.getRecords());
            goodsTypePageVo.setTotal(goodsTypePage.getTotal());

            return SaResult.data(goodsTypePageVo);
        }
        catch (Exception e){
            throw new RuntimeException("获取商品类型失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult addGoodsType(GoodsType goodsType) {
        try {
            if(count(new QueryWrapper<GoodsType>().eq("type",goodsType.getType()))==0){
                save(goodsType);
                return SaResult.ok("添加成功");
            }
            return SaResult.error("该类型已存在");

        }
        catch (Exception e){
            throw new RuntimeException("添加商品类型失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult deleteTypeById(int type) {
        try{
            remove(new QueryWrapper<GoodsType>().eq("type",type));
            return SaResult.ok("删除成功");
        }
        catch (Exception e){
            throw new RuntimeException("删除商品类型失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaResult updateTypeName(GoodsType goodsType) {
        try {
            update(new UpdateWrapper<GoodsType>().eq("type",goodsType.getType()).set("name",goodsType.getName()));
            return SaResult.ok("修改成功");
        }
        catch (Exception e){
            throw new RuntimeException("修改商品类型失败");
        }
    }
}


